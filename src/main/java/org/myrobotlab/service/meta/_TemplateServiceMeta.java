package org.myrobotlab.service.meta;

import org.myrobotlab.framework.Platform;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.service.meta.abstracts.Meta;
import org.myrobotlab.service.meta.abstracts.MetaData;
import org.slf4j.Logger;

public class _TemplateServiceMeta  extends Meta {
  public final static Logger log = LoggerFactory.getLogger(_TemplateServiceMeta.class);
  
  /**
   * This static method returns all the details of the class without it having
   * to be constructed. It has description, categories, dependencies, and peer
   * definitions.
   * 
   * @return MetaData - returns all the data
   * 
   */
  public MetaData getMetaData() {

    MetaData meta = new MetaData("org.myrobotlab.service._TemplateService");
    
    // use a platform - to do logic on dependency depending on platform
    Platform platform = Platform.getLocalInstance();
    // if (platform.isArm()) { ....
    
    // add a cool description
    meta.addDescription("used as a general template");
    
    // false will prevent it being seen in the ui
    meta.setAvailable(true); 
    
    // add dependencies if necessary
    // meta.addDependency("com.twelvemonkeys.common", "common-lang", "3.1.1");

    meta.setAvailable(false);
    
    // add it to one or many categories
    meta.addCategory("general");
    
    // add a sponsor to this service
    // the person who will do maintenance
    // meta.setSponsor("GroG"); 
    
    return meta;
  }

  
}

