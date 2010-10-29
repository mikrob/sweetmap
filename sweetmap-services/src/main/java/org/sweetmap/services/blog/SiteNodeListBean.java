package org.sweetmap.services.blog;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.enums.SiteNodeStatus;
import org.sweetmap.services.crawler.CrawlerMessageSender;
import org.sweetmap.services.spacializer.SpacializerInterface;
import org.sweetmap.services.utils.LazyList;

/**
 * The Site Node List.
 * @author leakim
 *
 */
@Scope(ScopeType.CONVERSATION)
@Name("siteNodeListBean")
public class SiteNodeListBean {


  /**
   * the page size.
   */
  private static final int PAGE_SIZE = 40;


  /**
   * The ejbql restrictions used by the filters.
   */
  private static final String[] RESTRICTIONS = {
    " lower(siteNode.name) like concat(lower(#{siteNodeListBean.siteNodeFilter.name}),'%')",
    " lower(siteNode.url) like concat(lower(#{siteNodeListBean.siteNodeFilter.url}),'%')",
    " lower(siteNode.description) like concat(lower(#{siteNodeListBean.siteNodeFilter.description}),'%')",
    " siteNode.siteNodeStatus = #{siteNodeListBean.siteNodeFilter.siteNodeStatus} ",
    " siteNode.category = #{siteNodeListBean.siteNodeFilter.category}",
    " siteNode.language = #{siteNodeListBean.siteNodeFilter.language}", };

  /**
   * The logger.
   */
  @Logger
  private Log logger;


  /**
   * Entity manager.
   */
  @In(required = true)
  private EntityManager entityManager;
  /**
   * The result list.
   */
  @DataModel
  private List<SiteNode> siteNodes;

  /**
   * The current text entry.
   */
  @Out(required = false)
  @DataModelSelection
  private SiteNode siteNode;

  /**
   * The filter.
   */
  private SiteNode siteNodeFilter = new SiteNode();


  /**
  *
  */
  private int currentPage = 1;

  /**
   * Crawler message sender.
   */
  @In(required = true, create = true)
  private CrawlerMessageSender crawlerMessageSender;

  /**
   * The spacialization component.
   */
  @In(required = true, create = true)
  private SpacializerInterface spacializer;

 /**
  * Init method.
  */
  @Create
  @Begin
  public void init() {
    logger.debug("Create Site Node list manager.");
    initSiteNodes();
  }

  /**
   * Factory for textEntries.
   */
  public void initSiteNodes() {
    logger.debug("Init site nodes");
    String ejbQl = "from SiteNode siteNode";
    StringBuffer ejbQLStd = new StringBuffer("select siteNode ");
    StringBuffer ejbQLCount = new StringBuffer("select count(siteNode) ");
    ejbQLCount.append(ejbQl);
    ejbQLStd.append(ejbQl);
    addRestrictions(ejbQLCount);
    addRestrictions(ejbQLStd);
    try {
      Query query = entityManager.createQuery(ejbQLStd.toString());
      long count = (Long) entityManager.createQuery(ejbQLCount.toString()).getSingleResult();
      siteNodes = new LazyList<SiteNode>(query, PAGE_SIZE, count, currentPage);
    } catch (NoResultException ex) {
      logger.error("No results found in text entry");
      FacesMessages.instance().add(Severity.WARN, "No results found in text entry");
    }
  }

  /**
   * Add restrictions to en ejqbl request.
   * @param buffer the buffer.
   */
  private void addRestrictions(StringBuffer buffer) {

    List<String> restrictionsToAdd = new ArrayList<String>();
    if (siteNodeFilter.getName() != null && !"".equals(siteNodeFilter.getName())) {
      restrictionsToAdd.add(RESTRICTIONS[0]);
    }
    if (siteNodeFilter.getUrl() != null && !"".equals(siteNodeFilter.getUrl())) {
      restrictionsToAdd.add(RESTRICTIONS[1]);
    }
    if (siteNodeFilter.getDescription() != null && !"".equals(siteNodeFilter.getDescription())) {
      restrictionsToAdd.add(RESTRICTIONS[2]);
    }
    if (siteNodeFilter.getSiteNodeStatus() != null) {
      restrictionsToAdd.add(RESTRICTIONS[3]);
    }
    if (siteNodeFilter.getCategory() != null && !"".equals(siteNodeFilter.getCategory().getName())) {
      restrictionsToAdd.add(RESTRICTIONS[4]);
    }
    if (siteNodeFilter.getLanguage() != null && !"".equals(siteNodeFilter.getLanguage())) {
      restrictionsToAdd.add(RESTRICTIONS[5]);
    }
    if (restrictionsToAdd.size() > 0) {
      buffer.append(" WHERE ");
    }
    for (int idx = 0; idx < restrictionsToAdd.size(); idx++) {
      buffer.append(restrictionsToAdd.get(idx));
      if (idx < restrictionsToAdd.size() - 1) {
        buffer.append(" AND ");
      }
    }
  }

  /**
   * delete a text entry.
   * @param theSiteNode to remove.
   */
  public void deleteSiteNode(SiteNode theSiteNode) {
    logger.debug("Delete Called for Site node : #0", theSiteNode.getUrl());
    if (siteNode != null) {
      entityManager.remove(theSiteNode);
      ((LazyList<SiteNode>) siteNodes).remove(theSiteNode);
      logger.debug("Remove site node");
      initSiteNodes();
    }
  }

  /**
   *  Launch the spacialization component to update SiteNodes position.
   */
  public void launchSpacialization() {
    spacializer.proceed();
  }

  /**
   * Launch a crawl by sending a jms message to the crawler.
   * @param theSiteNode to crawl.
   */
  public void launchCrawl(SiteNode theSiteNode) {
    String theUrl = theSiteNode.getUrl();
    logger.debug("Crawl asked for site : #0", theUrl);
    if (!theUrl.startsWith("http://")) {
      String http = "http://";
      theUrl = http + theUrl;
    }
    logger.debug("Send message to crawler with url : #0", theUrl);
    crawlerMessageSender.send(theUrl);
  }

  /**
   * validate a site node.
   * @param theSiteNode siteNode.
   */
  public void validateNode(SiteNode theSiteNode) {
    theSiteNode.setSiteNodeStatus(SiteNodeStatus.VALIDATED);
    entityManager.merge(theSiteNode);
    launchCrawl(theSiteNode);
    initSiteNodes();
  }

  /**
   * Validate all site note checked in the user interface.
   */
  public void validateAll() {
    for (SiteNode node : this.siteNodes) {
      if (node.getChecked()) {
        logger.debug("Launch validate for #0", node.getUrl());
        validateNode(node);
      }
    }
  }

  /**
   * Unvalidate all site note checked in the user interface.
   */
  public void unvalidateAll() {
    for (SiteNode node : this.siteNodes) {
      if (node.getChecked()) {
        unvalidateNode(node);
      }
    }
  }

  /**
   * unvalidate a site node.
   * @param theSiteNode siteNode.
   */
  public void unvalidateNode(SiteNode theSiteNode) {
    theSiteNode.setSiteNodeStatus(SiteNodeStatus.UNVALIDATED);
    entityManager.merge(theSiteNode);
    initSiteNodes();
  }

  /**
   * recrawl all the validated website.
   */
  public void reCrawl() {
    Query query = entityManager.createQuery(
      "select node from SiteNode node where node.siteNodeStatus = :status");
    query.setParameter("status", SiteNodeStatus.VALIDATED);

    List<SiteNode> result = (List<SiteNode>) query.getResultList();
    for (SiteNode node : result) {
      launchCrawl(node);
    }
  }

  /**
   * Destroy method.
   */
  @End
  @Destroy
  public void destroy() {
    logger.debug("Destroying conversation component and closing conversation for : text entry list");
  }

  /**
   * @return the currentPage
   */
  public int getCurrentPage() {
    return currentPage;
  }

  /**
   * @param currentPage the currentPage to set
   */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * @return the siteNode
   */
  public SiteNode getSiteNode() {
    return siteNode;
  }

  /**
   * @param siteNode the siteNode to set
   */
  public void setSiteNode(SiteNode siteNode) {
    this.siteNode = siteNode;
  }

  /**
   * @return the siteNodeFilter
   */
  public SiteNode getSiteNodeFilter() {
    return siteNodeFilter;
  }

  /**
   * @param siteNodeFilter the siteNodeFilter to set
   */
  public void setSiteNodeFilter(SiteNode siteNodeFilter) {
    this.siteNodeFilter = siteNodeFilter;
  }

  /**
   * @return the siteNodes
   */
  public List<SiteNode> getSiteNodes() {
    return siteNodes;
  }

  /**
   * @param siteNodes the siteNodes to set
   */
  public void setSiteNodes(List<SiteNode> siteNodes) {
    this.siteNodes = siteNodes;
  }



}
