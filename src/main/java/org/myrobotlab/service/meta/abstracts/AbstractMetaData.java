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
public class AbstractMetaData implements Serializable {

  transient public final static Logger log = LoggerFactory.getLogger(AbstractMetaData.class);

  private static final long serialVersionUID = 1L;

  /**
   * available in the UI(s)
   */
  protected Boolean available = true; // why not ? :P
  
  /**
   * Set of categories this service belongs to
   */
  transient public Set<String> categories = new HashSet<String>();
  
  /**
   * dependency keys of with key structure {org}-{version}
   */
  public List<ServiceDependency> dependencies = new ArrayList<ServiceDependency>();
  
  /**
   * description of what the service does
   */
  protected String description = null;
  
  /**
   * if true the dependency of this service are packaged in the build
   * of myrobotlab.jar
   */
  protected Boolean includeServiceInOneJar = false;
  
  /**
   * service requires an internet connection because some or all of its
   * functionality 
   */
  protected Boolean isCloudService = false;
  
  /**
   * used for appending ServiceExcludes to the ServiceDependencies
   */
  transient private ServiceDependency lastDependency;
  
  /**
   * license of the service
   */
  protected String license;// = "Apache";
  
  /**
   * relevant site to the Service
   */
  protected String link;
  
  /**
   * full type name of the service
   */
  protected String name;
  
  /**
   * key'd structure of other services that are necessary for the correct function of this service
   * can be modified with overrides before starting named instance of this service
   */
  public Map<String, ServiceReservation> peers = new TreeMap<String, ServiceReservation>();

  /**
   * true if the service requires a key e.g. Polly
   */
  protected Boolean requiresKeys = false;
  
  /**
   * instance name of service this MetaData belongs to
   * e.g. "i01"
   */
  protected String serviceName;

  /**
   * simple class name of this service
   */
  protected String simpleName;

  /**
   * the single sponsor of this service
   */
  protected String sponsor;

  /**
   * service life-cycle state 
   * inactive | created | registered | running | stopped | released
   */
  protected String state = null;

  /**
   * what is left TODO on this service for it to be ready for release
   */
  protected String todo;
  
  protected Integer workingLevel = null;
  
  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  public Boolean getIncludeServiceInOneJar() {
    return includeServiceInOneJar;
  }

  public void setIncludeServiceInOneJar(Boolean includeServiceInOneJar) {
    this.includeServiceInOneJar = includeServiceInOneJar;
  }

  public Boolean getIsCloudService() {
    return isCloudService;
  }

  public void setIsCloudService(Boolean isCloudService) {
    this.isCloudService = isCloudService;
  }

  public Boolean getRequiresKeys() {
    return requiresKeys;
  }

  public void setRequiresKeys(Boolean requiresKeys) {
    this.requiresKeys = requiresKeys;
  }

  public Integer getWorkingLevel() {
    return workingLevel;
  }

  public void setWorkingLevel(Integer workingLevel) {
    this.workingLevel = workingLevel;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSimpleName(String simpleName) {
    this.simpleName = simpleName;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setTodo(String todo) {
    this.todo = todo;
  }

  public AbstractMetaData() {
  }

  public AbstractMetaData(Class<?> clazz) {
    this.name = clazz.getCanonicalName();
    this.simpleName = clazz.getSimpleName();
  }

  public AbstractMetaData(String name) {
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

  public void addTodo(String todo) {
    this.todo = todo;
  }

  public void exclude(String groupId, String artifactId) {
    // get last dependency
    // dependencies
    if (lastDependency == null) {
      log.error("DEPENDENCY NOT DEFINED - CANNOT EXCLUDE");
    }
    lastDependency.add(new ServiceExclude(groupId, artifactId));
  }

  public List<ServiceDependency> getDependencies() {
    return dependencies;
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

  public void setCloudService(boolean b) {
    isCloudService = b;
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

  public int size() {
    return dependencies.size();
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

  public ServiceReservation getPeer(String peerKey) {
    if (peers.get(peerKey) == null) {
      log.warn("{} not found in peer keys - possible keys follow:", peerKey);
      for (String key : peers.keySet()) {
        log.warn(key);
      }
    }
    return peers.get(peerKey);
  }
  
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
  
  public String getServiceName() {
    return serviceName;
  }

  /**
   * typical adding of a service reservation ..
   * the actual name is left null, so that this template
   * will dynamically generate peer names depending on the parents name
   * 
   * @param key
   * @param peerType
   * @param comment
   */
  public void addPeer(String key, String peerType, String comment) {
    peers.put(key, new ServiceReservation(key, null, peerType, comment));
  }

  public void addPeer(String key, String actualName, String peerType, String comment) {
    peers.put(key, new ServiceReservation(key, actualName, peerType, comment));
  }
  
  public String getPeerActualName(String peerKey) {

    // return local defined name
    ServiceReservation peer = peers.get(peerKey);
    if (peer != null) {
      if (peer.actualName != null) {
        return peer.actualName;
      }
    }
    return null;
  }

  public Boolean isCloudService() {
    return isCloudService;
  }

  public String getSponsor() {
    return sponsor;
  }

  public ServiceDependency getLastDependency() {
    return lastDependency;
  }

  public String getState() {
    return state;
  }

  public String getTodo() {    
    return todo;
  }

  public void setLastDependency(ServiceDependency lastDependency) {
    this.lastDependency = lastDependency;
  }

}