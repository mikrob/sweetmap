package org.sweetmap.services.proxy;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;
import org.jboss.seam.web.ServletContexts;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.entities.TextEntry;

/**
 * The proxy manager.
 * @author leakim
 *
 */
@Name("proxyManager")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class ProxyManager {

  /**
   * The path for test only.
   */
  public static final String PATH = "/tmp/";

  /**
   * The logger.
   */
  @Logger
  private Log logger;

  /**
   * The siteNodeId.
   */
  private Long siteNodeId;

  /**
   * The Map navigation Manager.
   */
  @In(required = true)
  private MapNavigationManager mapNavigationManager;

  /**
   * Text Entry.
   */
  private String textEntry  = "<b>A une passante</b><br />Car j’ignore où tu fuis, tu ne sais où je vais,<br /> "
    + "Ô toi que j’eusse aimée, ô toi qui le savais !<br /><i>Charles Baudelaire</i>";

  /**
   * Escaped text entry.
   */
  private String escapedTextEntry;


  /**
   * Entity manager.
   */
  @In
  private EntityManager entityManager;

  /**
   * The url to analyse.
   */
  private String url;

  /**
   * The last url for back.
   */
  private String lastUrl;

  /**
   * The htmlCode.
   */
  private String htmlCode = "<b>Here will be present an html page proxyied</b>";

  /**
   * The l18n messages.
   */
  @In
  private Map<String, String> messages;

  /**
   * Initialization method.
   */
  @Create
  public void init() {
    if (LocaleSelector.instance().getLanguage().equals("fr")) {
      textEntry  = "<b>A une passante</b><br />"
        + "Car j’ignore où tu fuis, tu ne sais où je vais,<br /> "
        + "Ô toi que j’eusse aimée, ô toi qui le savais !<br />"
        + "<i>Charles Baudelaire</i>";
    } else {
      textEntry  = "<b>The Rose in the Deeps of his Heart</b><br />"
        + "With the earth and the sky and the water,<br /> "
        + "remade, like a casket of gold <br />"
        + "For my dreams of your image that blossoms <br />"
        + "a rose in the deeps of my heart.<br />"
        + "<i>William Butler Yeats</i>";
    }
  }

  /**
   * Run the proxy.
   */
  public void runProxy() {
    String urlCalled = ServletContexts.instance().getRequest().getRequestURL().toString();
    int sweetmap = urlCalled.indexOf("/sweetmap/");
    String localSiteUrl = urlCalled.substring(0, sweetmap + 10);
    if (siteNodeId != null) {
      siteNodeId++;
    }
    localSiteUrl += "proxy.seam?url=" + url;
    logger.debug("Run proxy CALLED for URL : #0 with local site url #1", url, localSiteUrl);
    if (canAccessUrl(url)) {
      reformatUrl();
      logger.debug("Run proxy for URL : #0 with local site url #1", url, localSiteUrl);
      IProxy proxy = ProxyFactory.getProxyInstance(url, localSiteUrl);
      try {
        proxy.getPage(ProxyManager.PATH);
        if (siteNodeId != null) {
          mapNavigationManager.addSite(siteNodeId);
        } else {
          logger.error("Site Node Id is null");
        }
        this.htmlCode = proxy.replaceUrl();
      } catch (Exception e) {
        this.htmlCode = "<b>Unable to get the page, probably a 404 error.</b>";
      }

    } else {
      String mess = messages.get("org.sweetmap.corpus.out");
      this.htmlCode = "<b>" + mess + "</b>";
    }

  }


  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }


  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    this.url = url;
  }


  /**
   * @return the htmlCode
   */
  public String getHtmlCode() {
    return htmlCode;
  }


  /**
   * @param htmlCode the htmlCode to set
   */
  public void setHtmlCode(String htmlCode) {
    this.htmlCode = htmlCode;
  }


  /**
   * @return the siteNodeId
   */
  public Long getSiteNodeId() {
    return siteNodeId;
  }


  /**
   * @param siteNodeId the siteNodeId to set
   */
  public void setSiteNodeId(Long siteNodeId) {
    this.siteNodeId = siteNodeId;
  }


  /**
   * @return the textEntry
   */
  public String getTextEntry() {
    logger.debug("GET Text ENTRY CALLED");
    if (siteNodeId != null) {
      TextEntry t = getRandomTextEntryForSiteNode();
      if (t != null) {
        this.textEntry = "<b>" + t.getTitle() + "</b>" + "<br />";
        this.textEntry += t.getContentForView();
        this.textEntry += "<br />" + "<i>" + t.getUser() + "</i>";
      }
    }
    return textEntry;
  }


  /**
   * @param textEntry the textEntry to set
   */
  public void setTextEntry(String textEntry) {
    this.textEntry = textEntry;
  }

  /**
   * Test if user can browse to this url (true if contained in database).
   * @param testedUrl the testedUrl.
   * @return true if contained in database
   */
  private boolean canAccessUrl(String testedUrl) {
    String withoutHTTP;
    if (testedUrl.contains("http://")) {
      withoutHTTP = testedUrl.substring(7, testedUrl.length());
    } else {
      withoutHTTP = testedUrl;
    }
    logger.debug("Testing url : #0", withoutHTTP);

    Query query = entityManager.createQuery("select siteNode from SiteNode siteNode where siteNode.url = :url");
    SiteNode node = null;
    boolean result = false;
    try {
      query.setParameter("url", withoutHTTP);
      node = (SiteNode) query.getSingleResult();
      if (node != null) {
        result = true;
      }
    } catch (Exception e) {
      result = false;
      logger.error("Exception in request");
    }
    if (url != null && !"".equals(url) && testedUrl.startsWith(url)) {
      result = true;
    }
    return result;
  }

  /**
   * Get a random text entry for a site node depending of its category.
   * @return textEntry the text entry.
   */
  @SuppressWarnings("unchecked")
  public TextEntry getRandomTextEntryForSiteNode() {
    logger.debug("Site Node ID = #0", siteNodeId);
    SiteNode siteNode = entityManager.find(SiteNode.class, this.siteNodeId);
    TextEntry tEntry = null;
    if (siteNode != null && siteNode.getCategory() != null) {
      logger.debug("Site node : #0", siteNode.getName());
      String request = "SELECT textEntry.id FROM TextEntry textEntry where textEntry.category = :category "
        + "AND textEntry.status = 0";
      Query query = entityManager.createQuery(request);
      query.setParameter("category", siteNode.getCategory());
      List<Long> idList = (List<Long>) query.getResultList();
      logger.debug("Searching random text for category : #0 with category language : #1, user language : #2",
          siteNode.getCategory().getName(), siteNode.getCategory().getLanguage(), LocaleSelector.instance().getLanguage());
      if (idList != null && idList.size() > 0) {
        logger.debug("ID LIST SIZE #0", idList.size());
        Random r = new Random();
        int minVal = 1;
        int maxVal  = idList.size();
        int valeur;
        if (maxVal == 1) {
          valeur = 0;
        } else {
          valeur = minVal + r.nextInt(maxVal - minVal);
        }

        logger.debug("Random : #0", valeur);
        Long id = idList.get(valeur);
        if (id != null) {
          tEntry = entityManager.find(TextEntry.class, id);
        }
      }
    }
    return tEntry;
  }


  /**
   * @return the escapedTextEntry
   */
  public String getEscapedTextEntry() {
    return escapedTextEntry;
  }


  /**
   * @param escapedTextEntry the escapedTextEntry to set
   */
  public void setEscapedTextEntry(String escapedTextEntry) {
    this.escapedTextEntry = escapedTextEntry;
  }


  /**
   * Reformat an url given.
   */
  public void reformatUrl() {
    if (url.startsWith("www.")) {
      String http = "http://";
      http += url;
      this.url = http;
    } else if (!url.startsWith("http://www.") && !url.startsWith("http://")
        && !url.startsWith("https://")) {
      String http = "http://www.";
      http += url;
      this.url = http;
    }

    if (!url.endsWith("/")) {
      this.url += "/";
    }
  }


  /**
   * @return the lastUrl
   */
  public String getLastUrl() {
    return lastUrl;
  }


  /**
   * @param lastUrl the lastUrl to set
   */
  public void setLastUrl(String lastUrl) {
    this.lastUrl = lastUrl;
  }






}
