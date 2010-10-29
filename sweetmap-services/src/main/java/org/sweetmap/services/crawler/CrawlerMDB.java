package org.sweetmap.services.crawler;

import java.net.MalformedURLException;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.Pool;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.services.crawler.exception.NullBaseUrlException;
import org.sweetmap.services.crawler.utils.SiteNodeCreationCache;
import org.sweetmap.services.crawler.utils.WebsiteInspector;
import org.sweetmap.services.properties.PropertyManager;


/**
 * Mdb intented to manage the messages from the queue/crawlerQueue.
 * @author leakim
 *
 */
//@Depends("jboss.j2ee:service=EARDeployment,url='sweetmap.ear'")
@MessageDriven(name = "CrawlerDrivenBean", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/crawlerQueue"),
    @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "10"),
    @ActivationConfigProperty(propertyName = "maxMessages", propertyValue = "1"),
    @ActivationConfigProperty(propertyName = "transactionTimeout", propertyValue = "6000"),
    @ActivationConfigProperty(propertyName = "keepAlive", propertyValue = "6000000")
    })
@Name("crawlerDrivenBean")
@Pool(value = "StrictMaxPool", maxSize = 8, timeout = 3000000)
public class CrawlerMDB implements MessageListener {

  /**
   * Logger.
   */
  @Logger
  private Log logger;

  /**
   * The entityManager.
   */
  @In
  private EntityManager entityManager;

  /**
   * Cache used to prevent data duplication in the database while inserting sitenodes.
   */
  private SiteNodeCreationCache siteNodeCreationCache;

  /**
   * Website inspector.
   */
  @In(create = true, required = true)
  private WebsiteInspector websiteInspector;

  /**
   * Constructor.
   */
  public CrawlerMDB() {
    siteNodeCreationCache = SiteNodeCreationCache.getInstance();
    //logger.debug("Create Crawler MDB instance");
  }

  /**
   * Callback when a message is received.
   * @param msg the msg that the component intercepted.
   */
  public void onMessage(Message msg) {
    ObjectMessage objMessage = (ObjectMessage) msg;

    try {
      String website = objMessage.getObject().toString();
      logger.debug("Je dois crawler le site : #0", website);
      logger.debug("Debut du crawl");

      websiteInspector.setBaseUrl(website);
      websiteInspector.setPagesLimit(PropertyManager.getInstance().getPropertyAsInt("crawlerPageLimit"));

      if (PropertyManager.getInstance().getPropertyAsBool("proxy")) {
        int port = PropertyManager.getInstance().getPropertyAsInt("proxyport");
        String host = PropertyManager.getInstance().getProperty("proxyurl");
        websiteInspector.setProxy(host, port);
      }


      /*
       * TODO un site ne répond plus -> on le vire de la base?
       * TODO mise a jour des données d'un site quand on effectue un nouveau crawl.
       */
      siteNodeCreationCache.addWebsiteToCrawl(website);
      websiteInspector.inspect();
      logger.debug("website crawled. Proceed to data insertions now.");

      websiteInspector.persistData();
      //clearCache(list);

      /// TODO : Essayer de balancer ca :
      ///      entityManager.flush();
      ///
      logger.debug("End of Crawl");
    } catch (JMSException e) {
      logger.error("JMS Exception Catched");
    } catch (NullBaseUrlException e) {
      logger.debug("Error : baseURL is not set.");
    } catch (MalformedURLException e1) {
      logger.debug("The given url is not correct.");
    } catch (Exception e) {
      logger.error("Crawler ERROR while crawling : ");
      e.printStackTrace();
    }

  }

  /**
   * Method to clear the cache from recently inserted data.
   * @param nodes list of url to remove from cache.
   */
  private void clearCache(List<SiteNode> nodes) {
    for (SiteNode node : nodes) {
      siteNodeCreationCache.removeKey(node.getUrl());
    }
    //siteNodeCreationCache.removeKey(url)
  }

  /**
   * Initialization method.
   */
//  @PreDestroy
//  public void init() {
//    logger.debug("DELETE MDB INSTANCE");
//  }
}


