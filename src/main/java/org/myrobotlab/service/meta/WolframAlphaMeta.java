package org.myrobotlab.service.meta;

import org.myrobotlab.framework.Platform;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.service.meta.abstracts.Meta;
import org.myrobotlab.service.meta.abstracts.MetaData;
import org.slf4j.Logger;

public class WolframAlphaMeta  extends Meta {
  public final static Logger log = LoggerFactory.getLogger(WolframAlphaMeta.class);
  
  /**
   * This static method returns all the details of the class without it having
   * to be constructed. It has description, categories, dependencies, and peer
   * definitions.
   * 
   * @return MetaData - returns all the data
   * 
   */
  public MetaData getMetaData() {

    MetaData meta = new MetaData("org.myrobotlab.service.WolframAlpha");
    Platform platform = Platform.getLocalInstance();
    meta.addCategory("ai", "cloud");
    // TODO http should be removed as a dependency and added as a Peer

    meta.addDescription("Run queries against wolfram alpha!");
    meta.addDependency("WolframAlpha", "WolframAlpha", "1.1");

    // FIXME - add Mrl Service HttpClient Peer - don't include dependency
    // directly
    /*
     * - currently Runtime provides these dependencies
     * meta.addDependency("org.apache.httpcomponents", "httpclient", "4.5.2");
     * meta.addDependency("org.apache.httpcomponents", "httpcore", "4.4.6");
     */

    meta.setCloudService(true);
    return meta;
  }
  
}

