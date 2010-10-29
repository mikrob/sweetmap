package org.sweetmap.services.blog;

import java.util.List;

import javax.ejb.Local;

import org.sweetmap.entities.SiteNode;

/**
 * Interface for site node list bean.
 * @author leakim
 *
 */
@Local
public interface SiteNodeList {

  /**
   * Init method.
   */
  void init();

  /**
   * Factory for textEntries.
   */
  void initSiteNodes();

  /**
   * delete a text entry.
   */
  void deleteTextEntry();

  /**
   * Destroy method.
   */
  void destroy();

  /**
   * @return the currentPage
   */
  int getCurrentPage();

  /**
   * @param currentPage the currentPage to set
   */
  void setCurrentPage(int currentPage);

  /**
   * @return the siteNode
   */
  SiteNode getSiteNode();

  /**
   * @param siteNode the siteNode to set
   */
  void setSiteNode(SiteNode siteNode);

  /**
   * @return the siteNodeFilter
   */
  SiteNode getSiteNodeFilter();

  /**
   * @param siteNodeFilter the siteNodeFilter to set
   */
  void setSiteNodeFilter(SiteNode siteNodeFilter);

  /**
   * @return the siteNodes
   */
  List<SiteNode> getSiteNodes();

  /**
   * @param siteNodes the siteNodes to set
   */
  void setSiteNodes(List<SiteNode> siteNodes);

}
