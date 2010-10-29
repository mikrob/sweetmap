package org.sweetmap.services.proxy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;


/**
 *
 * @author leakim
 *
 */
@Stateful
@Scope(ScopeType.SESSION)
@AutoCreate
@Name("mapNavigationManager")
public class MapNavigationManagerBean implements MapNavigationManager {


  /**
   * The logger.
   */
  @Logger
  private Log logger;
  /**
   * The site node visited.
   */
  private Map<Long, SiteNode> visitedSites;

  /**
   * The entity manager.
   */
  @In(required = true)
  private EntityManager entityManager;

  /**
   * The init method.
   */
  @Create
  public void init() {
    logger.debug("Creating Map Navigation Manager");
    this.visitedSites = new HashMap<Long, SiteNode>();

  }

  /**
   * Add a site found by its id.
   * @param id the id.
   */
  public void addSite(Long id) {
    logger.debug("ADD site #0", id);
    if (id != null) {
      SiteNode siteNode = null;
      try {
        siteNode = entityManager.find(SiteNode.class, id);
        this.visitedSites.put(id, siteNode);
      } catch (Exception ex) {
        logger.error("No Site Node found for Id : #0", id);
      }
    } else {
      logger.error("Id gived to MapNavigationManager.addsite is null or < 0");
    }
  }

  /**
   * Test if a site has been visited.
   * @param siteId the site.
   * @return true if the site has been visited.
   */
  public boolean isVisitedSite(Long siteId) {
    return visitedSites.containsKey(siteId);
  }

  /**
   * Return the visited site as a String containing the Id comma separated.
   * @return visitedSite the list.
   */
  public String getVisitedsSiteAsString() {
    logger.debug("VISITED SITES CALLED");
    StringBuffer buffer = new StringBuffer();
    Set<Map.Entry<Long, SiteNode>> theSet = visitedSites.entrySet();
    Iterator<Map.Entry<Long, SiteNode>> it = theSet.iterator();
    while (it.hasNext()) {
      Map.Entry<Long, SiteNode> entry = it.next();
      buffer.append(entry.getKey());
      if (it.hasNext()) {
        buffer.append(",");
      }
    }
    return buffer.toString();
  }

  /**
   * Destroy.
   */
  @Destroy
  @Remove
  public void destroy() {
    logger.info("Closing Session Scoped EJB Component : MapNavigation Manager.");
  }

  /**
   * @return the visitedSites
   */
  public Map<Long, SiteNode> getVisitedSites() {
    return visitedSites;
  }

  /**
   * @param visitedSites the visitedSites to set
   */
  public void setVisitedSites(Map<Long, SiteNode> visitedSites) {
    this.visitedSites = visitedSites;
  }





}
