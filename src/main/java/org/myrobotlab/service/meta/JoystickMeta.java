package org.myrobotlab.service.meta;

import org.myrobotlab.framework.Platform;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.service.meta.abstracts.Meta;
import org.myrobotlab.service.meta.abstracts.MetaData;
import org.slf4j.Logger;

public class JoystickMeta  extends Meta {
  public final static Logger log = LoggerFactory.getLogger(JoystickMeta.class);
  
  /**
   * This static method returns all the details of the class without it having
   * to be constructed. It has description, categories, dependencies, and peer
   * definitions.
   * 
   * @return MetaData - returns all the data
   * 
   */
  public MetaData getMetaData() {

    MetaData meta = new MetaData("org.myrobotlab.service.Joystick");
    Platform platform = Platform.getLocalInstance();
    meta.addDescription("service allows interfacing with a keyboard, joystick or gamepad");
    meta.addCategory("control","telerobotics");
    meta.addDependency("net.java.jinput", "jinput", "2.0.7");

    log.info("Joystick.getMetaData {}  isArm() {}", platform, platform.isArm());
    if (platform.isArm()) {
      log.info("loading arm binaries");
      meta.addDependency("jinput-natives", "jinput-natives-armv7.hfp", "2.0.7", "zip");
    } else {
      log.info("loading non-arm binaries");
      meta.addDependency("jinput-natives", "jinput-natives", "2.0.7", "zip");
    }
    // meta.addDependency("net.java.jinput", "jinput-platform", "2.0.7");
    // meta.addArtifact("net.java.jinput", "natives-windows");
    // meta.addArtifact("net.java.jinput", "natives-linux");
    // meta.addArtifact("")
    return meta;
  }
  
}

