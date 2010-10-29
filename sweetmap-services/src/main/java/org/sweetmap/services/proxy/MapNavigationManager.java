package org.sweetmap.services.proxy;

import java.util.Map;

import javax.ejb.Local;

import org.sweetmap.entities.SiteNode;


/**
 * Interface reprensting a map navigation manager.
 * @author leakim
 *
 */
@Local
public interface MapNavigationManager {

  /**
   * The init method.
   */
  void init();

  /**
   * Add a site found by its id.
   * @param id the id.
   */
  void addSite(Long id);

  /**
   * Test if a site has been visited.
   * @param siteId the site.
   * @return true if the site has been visited.
   */
  boolean isVisitedSite(Long siteId);

  /**
   * Destroy.
   */
  void destroy();

  /**
   * @return the visitedSites
   */
  Map<Long, SiteNode> getVisitedSites();

  /**
   * @param visitedSites the visitedSites to set
   */
  void setVisitedSites(Map<Long, SiteNode> visitedSites);

  /**
   * Return the visited site as a String containing the Id comma separated.
   * @return visitedSite the list.
   */
  String getVisitedsSiteAsString();

}
