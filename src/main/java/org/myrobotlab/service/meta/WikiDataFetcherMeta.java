package org.myrobotlab.service.meta;

import org.myrobotlab.framework.Platform;
import org.myrobotlab.framework.ServiceType;
import org.myrobotlab.logging.LoggerFactory;
import org.slf4j.Logger;

public class WikiDataFetcherMeta {
  public final static Logger log = LoggerFactory.getLogger(WikiDataFetcherMeta.class);
  
  /**
   * This static method returns all the details of the class without it having
   * to be constructed. It has description, categories, dependencies, and peer
   * definitions.
   * 
   * @return ServiceType - returns all the data
   * 
   */
  static public ServiceType getMetaData() {

    ServiceType meta = new ServiceType("org.myrobotlab.service.WikiDataFetcher");
    Platform platform = Platform.getLocalInstance();
    meta.addDescription("This service grab data from wikidata website");
    meta.addCategory("ai");
    meta.setSponsor("beetlejuice");
    meta.addDependency("org.wikidata.wdtk", "wdtk-client", "0.8.0");
    meta.exclude("org.slf4j", "slf4j-log4j12");
    // force using httpClient service httpcomponents version
    meta.exclude("org.apache.httpcomponents", "httpcore");
    meta.exclude("org.apache.httpcomponents", "httpclient");
    meta.addPeer("httpClient", "HttpClient", "httpClient");
    // force using same jackson version as polly
    /*
    meta.exclude("com.fasterxml.jackson.core", "jackson-core");
    meta.exclude("com.fasterxml.jackson.core", "jackson-databind");
    meta.exclude("com.fasterxml.jackson.core", "jackson-annotations");
    */
    meta.addDependency("com.fasterxml.jackson.core", "jackson-core", "2.9.9");
    meta.addDependency("com.fasterxml.jackson.core", "jackson-databind", "2.9.10.4");
    meta.addDependency("com.fasterxml.jackson.core", "jackson-annotations", "2.9.9");
    /*
     * meta.addDependency("org.wikidata.wdtk", "0.8.0-SNAPSHOT");
     * meta.addDependency("org.apache.commons.httpclient", "4.5.2");
     * meta.addDependency("org.apache.commons.commons-lang3", "3.3.2");
     * meta.addDependency("com.fasterxml.jackson.core", "2.5.0");
     */
    meta.setCloudService(true);
    return meta;
  }
  
  
}

