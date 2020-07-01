package org.myrobotlab.service.meta.abstracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.myrobotlab.codec.CodecUtils;
import org.myrobotlab.framework.ServiceReservation;
import org.myrobotlab.framework.repo.ServiceArtifact;
import org.myrobotlab.framework.repo.ServiceData;
import org.myrobotlab.framework.repo.ServiceDependency;
import org.myrobotlab.framework.repo.ServiceExclude;
import org.myrobotlab.logging.LoggerFactory;
import org.myrobotlab.service.meta.abstracts.AbstractMetaData;
import org.slf4j.Logger;

/**
 * MetaData describes most of the data about a service that is static.
 * It's dependencies, categories and other meta information.
 * 
 * It also describes its list of peers. They define a list of group this 
 * service expects to interact with.  When a service is started it gets
 * a copy of MetaData based on its instance name. When a new instance is
 * created a list of ServiceData.overrides are consulted, to allow the user
 * to override actual name and type information.
 * 
 */
public class MetaData implements Serializable {

  transient public final static Logger log = LoggerFactory.getLogger(MetaData.class);

  private static final long serialVersionUID = 1L;

  public static ServiceType fromMetaData(AbstractMetaData meta) {
    ServiceType st = new ServiceType();
    st.peers = meta.peers;
    st.available = meta.isAvailable();
    st.categories = meta.categories;
    st.dependencies = meta.dependencies;
    st.description = meta.getDescription();
    st.includeServiceInOneJar = meta.includeServiceInOneJar();
    st.isCloudService = meta.isCloudService();
    st.lastDependency = meta.getLastDependency();
    st.license = meta.getLicense();
    st.link = meta.getLink();
    st.name = meta.getName();
    st.peers = meta.getPeers();
    st.requiresKeys = meta.requiresKeys();
    st.simpleName = meta.getSimpleName();
    st.sponsor = meta.getSponsor();
    st.state = meta.getState();
    st.todo = meta.getTodo();
    return st;
  }
  
  /**
   * available in the UI(s)
   */
  Boolean available = true;
  
  transient public Set<String> categories = new HashSet<String>();
  /**
   * dependency keys of with key structure {org}-{version}
   */
  public List<ServiceDependency> dependencies = new ArrayList<ServiceDependency>();
  /**
   * description of what the service does
   */
  String description = null;
  Boolean includeServiceInOneJar = false;
  transient private ServiceDependency lastDependency;

  String license;// = "Apache";

  String link;
  String name;
  public Map<String, ServiceReservation> peers = new TreeMap<String, ServiceReservation>();
  Boolean requiresKeys = false;

  String simpleName;

  /**
   * if true the dependency of this service are packaged in the build
   * of myrobotlab.jar
  Boolean includeServiceInOneJar = false;
  
  /**
   * service requires an internet connection because some or all of its
   * functionality 
   */
  Boolean isCloudService = false;
  
  /**
   * used for appending ServiceExcludes to the ServiceDependencies
   */
  transient private ServiceDependency lastDependency;
  
  /**
   * license of the service
   */
  String license;// = "Apache";
  
  /**
   * relevant site to the Service
   */
  String link;
  
  /**
   * full type name of the service
   */
  String name;
  
  /**
   * key'd structure of other services that are necessary for the correct function of this service
   * can be modified with overrides before starting named instance of this service
   */
  public Map<String, ServiceReservation> peers = new TreeMap<String, ServiceReservation>();

  String state = null;
  /**
   * simple class name of this service
   */
  String todo;
  Integer workingLevel = null;

  protected String serviceName;

  /**
   * what is left TODO on this service for it to be ready for release
   */
  String todo;
  
  Integer workingLevel = null;
  public MetaData() {
  }

  public MetaData(Class<?> clazz) {
    this.name = clazz.getCanonicalName();
    this.simpleName = clazz.getSimpleName();
  }

  public MetaData(String name) {
    this.name = CodecUtils.makeFullTypeName(name);
    this.simpleName = name.substring(name.lastIndexOf(".") + 1);
  }

  public void addArtifact(String orgId, String classifierId) {
    lastDependency.add(new ServiceArtifact(orgId, classifierId));  
  }

  public void addCategory(String... categories) {
    for (int i = 0; i < categories.length; ++i) {
      this.categories.add(categories[i]);
    }
  }

  public void addDependency(String groupId, String artifactId) {
    addDependency(groupId, artifactId, null, null);
  }

  public void addDependency(String groupId, String artifactId, String version) {
    addDependency(groupId, artifactId, version, null);
  }

  public void addDependency(String groupId, String artifactId, String version, String ext) {
    ServiceDependency library = new ServiceDependency(groupId, artifactId, version, ext);
    lastDependency = library;
    dependencies.add(library);
  }

  public void addDescription(String description) {
    this.description = description;
  }

  public void addLicense(String license) {
    this.license = license;
  }

  public void addPeer(String key, String peerType, String comment) {
    peers.put(key, new ServiceReservation(key, null, peerType, comment));
  }

  public void addPeer(String key, String actualName, String peerType, String comment) {
    peers.put(key, new ServiceReservation(key, actualName, peerType, comment));
  }

  public void addTodo(String todo) {
    this.todo = todo;
  }

  @Override
  public int compare(ServiceType o1, ServiceType o2) {
    return o1.name.compareTo(o2.name);
  }


  public void exclude(String groupId, String artifactId) {
    // get last dependency
    // dependencies
    if (lastDependency == null) {
      log.error("DEPENDENCY NOT DEFINED - CANNOT EXCLUDE");
    }
    lastDependency.add(new ServiceExclude(groupId, artifactId));
  }

  /*
  public void addRootPeer(String actualName, String peerType, String comment) {
    peers.put(actualName, new ServiceReservation(actualName, actualName, peerType, comment, true, true));
  }*/

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getLicense() {
    return license;
  }

  public String getLink() {
    return link;
  }

  public boolean includeServiceInOneJar() {
    return includeServiceInOneJar;
  }

  public String getName() {
    return name;
  }

  public Map<String, ServiceReservation> getPeers() {
    return peers;
  }

  public String getSimpleName() {
    return simpleName;
  }

  public boolean includeServiceInOneJar() {
    return includeServiceInOneJar;
  }

  public void includeServiceInOneJar(Boolean b) {
    includeServiceInOneJar = b;
  }

  public boolean isAvailable() {
    return available;
  }

  public boolean requiresKeys() {
    return requiresKeys;
  }


  public void setAvailable(boolean b) {
    this.available = b;
  }

  public void setLicenseProprietary() {
    addLicense("proprietary");
  }

  public void setLicenseApache() {
    addLicense("apache");
  }

  public void setLicenseGplV3() {
    addLicense("gplv3");
  }

  public void setLicenseProprietary() {
    addLicense("proprietary");
  }

  public void setLink(String link) {
    this.link = link;
  }

  public void setRequiresKeys(boolean b) {
    requiresKeys = b;
  }

  public void setSponsor(String sponsor) {
    this.sponsor = sponsor;
  }

  /**
   * sharing means sharePeer is forced - while addPeer will check before adding
   * 
   * @param key
   *          k
   * @param actualName
   *          n
   * @param peerType
   *          n
   * @param comment
   *          comment
   */
  public void sharePeer(String key, String actualName, String peerType, String comment) {
    peers.put(key, new ServiceReservation(key, actualName, peerType, comment));
  }

  public int size() {
    return dependencies.size();
  }
  
  public String getPeerActualName(String peerKey) {

 
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if (serviceName != null) {
      sb.append(String.format("\n%s %s\n", serviceName, simpleName));
    } else {
      sb.append(String.format("\n%s\n", simpleName));
    }
    
    for (ServiceReservation sr : peers.values()) {
      sb.append(sr).append("\n");
    }
    
    return sb.toString();
  }

  // GAH ! - more convertions for smaller pr :(
  public static AbstractMetaData toMetaData(ServiceType type) {
    AbstractMetaData meta = new AbstractMetaData();
    meta.peers = type.peers;
    meta.setAvailable(type.isAvailable());
    meta.categories = type.categories;
    meta.dependencies = type.dependencies;
    meta.setDescription(type.getDescription());
    meta.setIncludeServiceInOneJar(type.includeServiceInOneJar());
    meta.setIsCloudService(type.isCloudService);
    meta.setLastDependency(type.lastDependency);
    meta.setLicense(type.getLicense());
    meta.setLink(type.getLink());
    meta.setName(type.getName());
    meta.peers = type.getPeers();
    meta.setRequiresKeys(type.requiresKeys());
    meta.setSimpleName(type.getSimpleName());
    meta.setSponsor(type.sponsor);
    meta.setState(type.state);
    meta.setTodo(type.todo);
    return meta;
  }


}
