package org.myrobotlab.serial;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.myrobotlab.framework.QueueStats;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.service.interfaces.SerialDataListener;
import org.slf4j.Logger;

/**
 * 
 * @author Grog
 *
 */

public abstract class Port implements Runnable, SerialControl {

  public final static Logger log = LoggerFactory.getLogger(Port.class);

  String portName;

  transient HashMap<String, SerialDataListener> listeners = new HashMap<>();

  static int pIndex = 0;

  /**
   * Thread for reading if required - in case of PortQueue and PortStream (but
   * not PortJSSC)
   */
  transient Thread readingThread = null;
  boolean listening = false;
  

  public boolean debug = true;
  public boolean debugTX = true;
  public boolean debugRX = false;

  QueueStats stats = new QueueStats();

  // hardware serial port details
  // default convention over configuration
  // int rate = 57600;
  int rate = 115200;
  int dataBits = 8;
  int stopBits = 1;
  int parity = 0;

  int txErrors;
  int rxErrors;

  boolean isOpen = false;

  // FIXME - find a better way to handle this
  // necessary - to be able to invoke
  // "nameless" port implementation to query "hardware" ports
  // overloading a "Port" and a PortQuery - :P
  public Port() {
  }

  public Port(String portName) {
    this.stats.name = portName;
    this.portName = portName;
    stats.interval = 1000;
  }

  public Port(String portName, int rate, int dataBits, int stopBits, int parity) throws IOException {
    this(portName);
    this.rate = rate;
    this.dataBits = dataBits;
    this.stopBits = stopBits;
    this.parity = parity;
  }

  public void close() {

    listening = false;
    if (readingThread != null) {
      readingThread.interrupt();
    }
    readingThread = null;

    log.info("closed port {}", portName);
  }

  public String getName() {
    return portName;
  }

  abstract public boolean isHardware();

  public boolean isCTS() {
    return false;
  }

  public boolean isDSR() {
    return false;
  }

  public int getStopBits() {
    return stopBits;
  }

  public int getBaudRate() {
    return rate;
  }

  public int getDataBits() {
    return dataBits;
  }

  public int getParity() {
    return parity;
  }

  public boolean isListening() {
    return listening;
  }

  public boolean isOpen() {
    return isOpen;
  }

  public void listen(Map<String, SerialDataListener> listeners) {
    this.listeners.putAll(listeners);
    if (readingThread == null) {
      ++pIndex;
      readingThread = new Thread(this, String.format("%s.portListener %s", portName, pIndex));
      readingThread.start();
    } else {
      log.info("{} already listening", portName);
    }
  }

  public void listen(SerialDataListener listener) {
    Map<String, SerialDataListener> sdl = new HashMap<>();
    sdl.put(listener.getName(), listener);
    listen(sdl);
  }

  public void open() throws IOException {
    log.info("opening port {}", portName);
    isOpen = true;
  }

  // abstract public int read() throws Exception;

  abstract public byte[] readBytes() throws Exception;
  /**
   * reads from Ports input stream and puts it on the Serials main RX line - to
   * be published and buffered - PortJSSC uses the thread of the library to "push" serial data
   */
  @Override
  public void run() {
    log.info("Listening on port {}", portName);
    listening = true;
    Integer newByte = -1;
    try {
      while (listening) {
        // read everything that's available on the port.
        byte[] buffer = readBytes();
        if (buffer == null) { 
          // TODO: maybe this be a tight loop. we might want some sort of thread sleep here?
          continue;
        }
        // we have data.. let's publish it.
        for (String key : listeners.keySet()) {
          listeners.get(key).onBytes(buffer);
          // log.info(String.format("%d",newByte));
        }
        // TODO: better stats.. for now.. keeping previous behavior.
        for (int i = 0; i<buffer.length;i++) {
          ++stats.total;
          if (stats.total % stats.interval == 0) {
            stats.ts = System.currentTimeMillis();
            stats.delta = stats.ts - stats.lastTS;
            stats.lineSpeed = (8 * stats.interval) / stats.delta;
            for (String key : listeners.keySet()) {
              listeners.get(key).updateStats(stats);
            }
            stats.lastTS = stats.ts;
          }
        }
      }
      log.info("{} no longer listening - last byte {} ", portName, newByte);
    } catch (InterruptedException e) {
      log.info("port {} interrupted - stopping listener", portName);
    } catch (Exception e1) {
      log.error("port reading thread threw", e1);
    } finally {
      log.info("stopped listening on {}", portName);
    }
  }

  /**
   * "real" serial function stubbed out in the abstract class in case the serial
   * implementation does not actually implement this method e.g. (bluetooth,
   * iostream, tcp/ip)
   * 
   * @param state
   * 
   */
  public void setDTR(boolean state) {
  }

  /**
   * The way rxtxLib currently works - is it will give a -1 on a read when it
   * has no data to give although in the specification this means end of stream
   * - for rxtxLib this is not necessarily the end of stream. The implementation
   * there - the thread is in rxtx - and will execute serialEvent when serial
   * data has arrived. This might have been a design decision. The thread which
   * calls this is in the rxtxlib - so we have it call the run() method of a
   * non-active thread class.
   * 
   * needs to be buried in rxtxlib implementation
   * 
   * @param b
   *          the byte
   * @throws Exception
   *           TODO
   * 
   */
  abstract public void write(int b) throws Exception;

  abstract public void write(int[] data) throws Exception;

  abstract public void write(byte[] data) throws Exception;

  public boolean setParams(int rate, int dataBits, int stopBits, int parity) throws Exception {
    log.debug("setSerialPortParams {} {} {} {}", rate, dataBits, stopBits, parity);
    this.rate = rate;
    this.dataBits = dataBits;
    this.stopBits = stopBits;
    this.parity = parity;
    return true;
  }

}
