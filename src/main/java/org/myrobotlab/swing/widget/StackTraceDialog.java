package org.myrobotlab.swing.widget;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.swing.RuntimeGui;
import org.slf4j.Logger;

public class StackTraceDialog extends JDialog implements ActionListener, MouseListener {

  private static final long serialVersionUID = 1L;
  public final static Logger log = LoggerFactory.getLogger(StackTraceDialog.class);

  RuntimeGui parent;
  private JTextArea traceArea = null;
  JScrollPane scrollPane = null;
  
  public StackTraceDialog(RuntimeGui parent) {
    super(parent.swingGui.getFrame(), "stack traces");
    this.parent = parent;
    Container display = getContentPane();
    // north
    JPanel north = new JPanel();
    display.add(north, BorderLayout.NORTH);
    // TODO: create a better way to navigate the stack traces in a swing gui..
    traceArea = new JTextArea("Current Stack Traces\n", 5, 10);
    traceArea.setLineWrap(true);
    traceArea.setEditable(false);
    traceArea.setBackground(SystemColor.control);
    scrollPane = new JScrollPane(traceArea);
    DefaultCaret caret = (DefaultCaret) traceArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    // build our report to and add it to the text area
    Set<Thread> threads = Thread.getAllStackTraces().keySet();
    StringBuilder traceBuilder = new StringBuilder();
    for (Thread t : threads) {
      traceBuilder.append("Thread:" + t.getId() + " " + t.getName() + " Status:" + t.getState() +"\n");
      for (StackTraceElement ele : t.getStackTrace()) {
        traceBuilder.append("  " + ele.getClassName() + " " + ele.getFileName() + ":" + ele.getLineNumber() + "\n");
      }
      traceBuilder.append("\n");
    }
    traceArea.append(traceBuilder.toString());
    display.add(scrollPane, BorderLayout.CENTER);
    // south
    JPanel south = new JPanel();
    display.add(south, BorderLayout.SOUTH);
    // TODO: add a button to refresh..
    setSize(320, 600);
    setVisible(true);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    
  }

}