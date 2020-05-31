package org.myrobotlab.service.meta;

import org.myrobotlab.framework.Platform;
import org.myrobotlab.framework.ServiceType;
import org.myrobotlab.logging.LoggerFactory;
import org.slf4j.Logger;

public class AudioFileMeta {
  public final static Logger log = LoggerFactory.getLogger(AudioFileMeta.class);
  
  /**
   * This static method returns all the details of the class without it having
   * to be constructed. It has description, categories, dependencies, and peer
   * definitions.
   * 
   * @return ServiceType - returns all the data
   * 
   */
  static public ServiceType getMetaData() {

    ServiceType meta = new ServiceType("org.myrobotlab.service.AudioFile");
    Platform platform = Platform.getLocalInstance();
    meta.addDescription("can play audio files on multiple tracks");
    meta.addCategory("sound","music");

    meta.addDependency("javazoom", "jlayer", "1.0.1");
    meta.addDependency("com.googlecode.soundlibs", "mp3spi", "1.9.5.4");
    meta.addDependency("com.googlecode.soundlibs", "vorbisspi", "1.0.3.3"); // is
    // this
    // being
    // used
    // ?

    /*
     * meta.addDependency("javazoom.spi", "1.9.5");
     * meta.addDependency("javazoom.jl.player", "1.0.1");
     * meta.addDependency("org.tritonus.share.sampled.floatsamplebuffer",
     * "0.3.6");
     */
    return meta;
  }

  
  
}
