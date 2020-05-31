package org.myrobotlab.service.meta;

import org.myrobotlab.framework.Platform;
import org.myrobotlab.framework.ServiceType;
import org.myrobotlab.logging.LoggerFactory;
import org.slf4j.Logger;

public class BoofCvMeta {
  public final static Logger log = LoggerFactory.getLogger(BoofCvMeta.class);
  
  /**
   * This static method returns all the details of the class without it having
   * to be constructed. It has description, categories, dependencies, and peer
   * definitions.
   * 
   * @return ServiceType - returns all the data
   * 
   */
  static public ServiceType getMetaData() {

    ServiceType meta = new ServiceType("org.myrobotlab.service.BoofCv");
    Platform platform = Platform.getLocalInstance();
    meta.addDescription("a very portable vision library using pure Java");
    meta.setAvailable(true);
    // add dependency if necessary
    meta.addDependency("org.boofcv", "boofcv-core", "0.31");
    meta.addDependency("org.boofcv", "boofcv-swing", "0.31");
    meta.addDependency("org.boofcv", "boofcv-openkinect", "0.31");
    meta.addCategory("vision", "video");
    /*
     * meta.exclude("org.bytedeco", "javacv");
     * meta.exclude("org.bytedeco.javacpp-presets", "opencv");
     */
    return meta;
  }
  
  
}
