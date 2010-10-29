package org.sweetmap.services.blog;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.services.crawler.CrawlerMessageSender;

/**
 * The site node Entity home.
 * @author leakim
 *
 */
@Name("siteNodeHome")
public class SiteNodeHome extends EntityHome<SiteNode> {

  /**
   *
   */
  private static final long serialVersionUID = -8214541970732733966L;

  /**
   * logger.
   */
  @Logger
  private Log logger;


  /**
   * Used to get the request parameter for manage the entity passed.
   */
  @RequestParameter
  private Long siteNodeId;

  /**
   * The Faces messages.
   */
  @In
  private FacesMessages facesMessages;

  /**
   * The entity manager.
   */
  @In
  private EntityManager entityManager;

  /**
   * Crawler message sender.
   */
  @In(required = true, create = true)
  private CrawlerMessageSender crawlerMessageSender;

  /**
   * Return id for the object.
   * @return {@link TextEntryHome}
   */
  @Override
  public Object getId() {
    if (siteNodeId == null) {
      return super.getId();
    } else {
      return siteNodeId;
    }
  }

  /**
   * Persist method.
   * @return success if can persist.
   */
  @Override
  public String persist() {
    logger.debug("Persist called");

    // if the language match the category language
    // we persist the siteNode and crawl it.
    if (correctLanguageAndCategory()) {
      String result = super.persist();
      String theUrl = this.instance.getUrl();

      if (!theUrl.startsWith("http://")) {
        String http = "http://";
        theUrl = http + theUrl;
      }
      logger.debug("Send message to crawler with url : #0", theUrl);
      crawlerMessageSender.send(theUrl);
      return result;
    } else {
      facesMessages.add(Severity.ERROR, "Language for category and site node are not equals");
      return "error";
    }
  }

  /**
   * update override.
   * @return sucess if succesfuly updated.
   */
  @Override
  public String update() {
    logger.debug("Update called");
    if (correctLanguageAndCategory()) {
      return super.update();
    } else {
      facesMessages.add(Severity.ERROR, "Language for cateogry and site node are not equals");
      return "error";
    }
  }

  /**
   * update override.
   * @return sucess if succesfuly updated.
   */
  @Override
  @Transactional
  public String remove() {
    logger.debug("Delete called");
    String sqlQuery = "DELETE FROM  `SiteNode_SiteNode`  WHERE  `connectedSiteNodes_id` = :id "
      + "OR  `SiteNode_id` = :otherid ;";
    try {
      Query query = entityManager.createNativeQuery(sqlQuery);
      query.setParameter("id", this.instance.getId());
      query.setParameter("otherid", this.instance.getId());
      query.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return super.remove();
  }

  /**
   * Test if category and site node language are the sames.
   * @return true or false.
   */
  public boolean correctLanguageAndCategory() {
    return this.instance.getLanguage().equals(this.instance.getCategory().getLanguage());
  }

  /**
   * Create a new conversation.
   */
  @Override @Begin(join = true)
  public void create() {
    logger.debug("Create site node home for node #0", siteNodeId);
    super.create();

  }



}
