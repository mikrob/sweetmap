package org.sweetmap.services.crawler.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.http.client.ClientProtocolException;
import org.hibernate.validator.InvalidStateException;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.entities.SiteNodeFile;
import org.sweetmap.enums.SiteNodeStatus;
import org.sweetmap.services.crawler.exception.Error404Exception;
import org.sweetmap.services.crawler.exception.FileTypeException;
import org.sweetmap.services.crawler.exception.HttpCodeException;
import org.sweetmap.services.crawler.exception.NullBaseUrlException;

// TODO barre de progression d'un crawl.
// TODO composant d'analyse d'un site.
// TODO passer toutes les URL en URL et pas en String
// TODO lock pour pas insérer en même temps?

/**
 * This class inspect a website.
 * @author max
 *
 */
@Name("websiteInspector")
public class WebsiteInspector {

  /**
   * Logger.
   */
  @Logger
  private Log logger;

  /**
   * pages limit to download.
   */
  private int pagesLimit;

  /**
   * number of pages proceeded.
   */
  private int nbProceed;

  /**
   * website base url.
   */
  private URL baseUrl;

  /**
   * list of the visited urls within the website.
   */
  private Map<String, Boolean> visitedUrls;

  /**
   * list of the known urls.
   */
  private List<String> urlsList;

  /**
   * list of website external urls.
   */
  private Map<String, Boolean> externalUrl;

  /**
   * start time of the inspection.
   */
  private Date beginInspection;

  /**
   * end time of the inspection.
   */
  private Date endInspection;

  /**
   * Cache where website that will be inserted soon in the database are declared.
   * This process is necessary to avoid possible multiple insertions.
   */
  private SiteNodeCreationCache siteNodeCreationCache;

  /**
   * The entityManager.
   */
  @In
  private EntityManager entityManager;

  /**
   * This list stored the website downloaded file paths.
   */
  private List<String> siteNodeFiles;

  /**
   * The downloader to fetch page.
   */
  private Downloader downloader;

  /**
   * The SiteNode reference.
   */
  private SiteNode siteNodeRef;

  /**
   * The forbidden path on the website.
   * This information comes from the robots.txt file.
   */
  private List<String> forbiddens;

  /**
   * The CrawlersStatus instance.
   */
  //private CrawlersStatus crawlersStatus;

//  /**
//   * The CrawlersStatus instance.
//   */
//  @In
//  private CrawlerStatusBean crawlersStatus;

  /**
   * Constructor.
   * @param websiteUrl : url of the webiste to inspect.
   * @throws MalformedURLException when a wrong url is given.
   */
  public WebsiteInspector(String websiteUrl) throws MalformedURLException {
    this();
    setBaseUrl(websiteUrl);
  }

  /**
   * Constructor.
   */
  public WebsiteInspector() {
    this.pagesLimit       = 10;
    this.nbProceed        = 0;
    this.baseUrl          = null;
    this.beginInspection  = null;
    this.endInspection    = null;
    siteNodeCreationCache = SiteNodeCreationCache.getInstance();
    this.downloader       = new Downloader();
  }


  /**
   * method to begin website inspection.
   * @throws NullBaseUrlException when baseUrl is not set
   */
  public void inspect() throws NullBaseUrlException {

    if (this.baseUrl == null) {
      throw new NullBaseUrlException();
    }

    visitedUrls   = new Hashtable<String, Boolean>();
    externalUrl   = new Hashtable<String, Boolean>();
    urlsList      = new LinkedList<String>();
    siteNodeFiles = new LinkedList<String>();
    forbiddens    = new LinkedList<String>();

    String websiteUrl = baseUrl.toString();
    if (websiteUrl.startsWith("http://")) {
      websiteUrl = websiteUrl.substring(7);
    } else if (websiteUrl.startsWith("https://")) {
      websiteUrl = websiteUrl.substring(8);
    }
    List<SiteNode> result = entityManager.createNamedQuery("checkExists")
      .setParameter("url", websiteUrl).getResultList();

    siteNodeRef = result.get(0);
    setProgressStatus(0);

    this.checkRobotsTxt();

    urlsList.add(this.baseUrl.toString());
    this.beginInspection = new Date();
    while (urlsList.size() > 0 && nbProceed < pagesLimit) {
      String url = ((LinkedList<String>) urlsList).getFirst();
      ((LinkedList<String>) urlsList).removeFirst();
      if (allowedUrl(url)) {
        inspectPage(url);
      } else {
        logger.debug("page interdite : " + url);
      }
    }
    if (nbProceed >= pagesLimit) {
      logger.debug("-> [Limite de page atteinte, fin de l'inspection]");
    } else {
      logger.debug("-> [Plus de page a traiter, fin de l'inspection]");
    }
    setProgressStatus(100);
    this.endInspection = new Date();
  }

  /**
   * This method checks the robots.txt file before the crawl.
   */
  private void checkRobotsTxt() {

    try {
      String url = this.baseUrl.toString() + "/robots.txt";
      downloader.download(url);
      logger.debug("j'ai un robots.txt sur " + this.baseUrl.toString());

      String filename = url.replace('/', '-');

      InputStream is = new FileInputStream("/tmp/" + filename);
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      String line;
      while ((line = br.readLine()) != null) {

        if (line.startsWith("Disallow:") || line.startsWith("disallow:")) {
          String disallow = line.substring(9);
          disallow = disallow.trim();
          disallow = this.baseUrl.toString() + disallow;
          logger.debug("dissalow : " + disallow);
          forbiddens.add(disallow);
        }
      }

    } catch (Error404Exception e) {
      logger.debug("pas de robots.txt");
    } catch (HttpCodeException e) {
      logger.debug("pas de robots.txt 2");
    } catch (Exception e) {
      logger.debug(e.getMessage());
    }
  }

  /**
   * This method checks if the url is allowed to be crawled regarding the robots.txt information.
   * @param url to test
   * @return true if we're allowed, false else.
   */
  private boolean allowedUrl(String url) {
    for (String path : forbiddens) {
      if (url.startsWith(path)) {
        return false;
      }
    }

    return true;
  }

  /**
   * The method set the progress value of the current crawl.
   * @param value to set in percent : [0 - 100].
   */
  private void setProgressStatus(long value) {
    CrawlersStatus.getInstance().getMapStatus().put(siteNodeRef, value);
    logger.debug("crawlerStatus site url = " + baseUrl.toString() + " value = "
        + CrawlersStatus.getInstance().getMapStatus().get(siteNodeRef));
    if (value >= 100L) {
      logger.debug("FINISHED");
      CrawlersStatus.getInstance().getMapStatus().remove(siteNodeRef);
    }
  }

  /**
   * method to inspect a page.
   * @param url to inspect.
   */
  private void inspectPage(String url) {
    logger.debug("-> telechargement de : " + url);
    // on regarde si l'url a deja ete inspectee
    if (!visitedUrls.containsKey(url)) {

      try {
        downloader.download(url);

        UrlExtractor uextract = new UrlExtractor();
        uextract.setDomain(this.baseUrl);

        String filename = url.replace('/', '-');
        uextract.proceed("/tmp/" + filename);
        siteNodeFiles.add("/tmp/" + filename);

        // on ajoute les nouvelles URL locales
        urlsList.addAll(uextract.getLocalDomainUrls());

        //on ajoute les nouvelles URL externes
        for (String tmp : uextract.getExternalUrls()) {
          URL tmpUrl = new URL(tmp);
          if (!externalUrl.containsKey(tmpUrl.getHost())) {
            externalUrl.put(tmpUrl.getHost().toLowerCase(), true);
          }
        }

        nbProceed++;
        Float progress = (new Float(nbProceed) / new Float(pagesLimit)) * 100;
        setProgressStatus(progress.intValue());
        logger.debug("    informations extracted");

      } catch (ClientProtocolException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (FileTypeException e) {
        // rien a faire, on ignore la page
        logger.debug("    download aborted, content type is unknown : " + e.getFileType());
      } catch (Error404Exception e) {
        // TODO : gerer les 404.
        logger.debug("ERROR 404. Not yet handled.");
      } catch (HttpCodeException e) {
        // TODO : gerer le log de ce genre d'ex
        logger.debug(e.getMessage());
      } catch (Exception e) {
        logger.debug("Exception : "+e.getMessage());
      }

      //on declare l'URL comme visitée.
      visitedUrls.put(url, true);
    } else {
      logger.debug("    deja visite...");
    }
  }

  /**
   * externalUrls getter.
   * @return externalUrls list
   */
  public Enumeration<String> getExternalUrls() {
    return ((Hashtable<String, Boolean>) externalUrl).keys();
  }

  /**
   * Method to set a pages limit to inspect.
   * @param limit to set
   */
  public void setPagesLimit(int limit) {
    this.pagesLimit = limit;
  }

  /**
   * Return the inspection time.
   * @return time needed to inspect
   */
  public long getElapsedTime() {
    long begin = beginInspection.getTime();
    long end   = endInspection.getTime();

    return (end - begin) / 1000;
  }

  /**
   * Method to set a proxy.
   * @param host for the proxy
   * @param port for the proxy
   */
  public void setProxy(String host, int port) {
    downloader.setProxy(host, port);
  }

  /**
   * Method to set the base url parameter before launching the crawl.
   *
   * @param website : url of the website to crawl.
   * @throws MalformedURLException when a wrong url is given.
   */
  public void setBaseUrl(String website) throws MalformedURLException {
    URL url;

    if (!website.startsWith("http://") && !website.startsWith("https://")) {
      url = new URL("http://" + website);
    } else {
      url = new URL(website);
    }

    this.baseUrl = url;
  }

  /**
   * This method check if the website is already registered in the database.
   * @param url to check
   * @return true if the website is known in the database, else false
   */
  @SuppressWarnings("unchecked")
  private boolean websiteKnown(String url) {

    List<SiteNode> result = entityManager.createNamedQuery("checkExists").setParameter("url", url).getResultList();

    if (result.size() == 0) {
      return false;
    }
    return true;
  }

  /**
   * Method to create or update siteNodes in the database.
   */
  public void persistData() {
    List<SiteNode> connectedNodes = new LinkedList<SiteNode>();

    Enumeration<String> extUrls = ((Hashtable<String, Boolean>) externalUrl).keys();
    String url;

    SiteNode currentWebsite;

    String siteUrl = baseUrl.toString();
    if (siteUrl.startsWith("http://")) {
      siteUrl = siteUrl.substring(7);
    } else if (siteUrl.startsWith("https://")) {
      siteUrl = siteUrl.substring(8);
    }

    List<SiteNode> result = entityManager.createNamedQuery("checkExists")
    .setParameter("url", siteUrl).getResultList();

    if (result.size() == 0) {
      logger.debug("ERROR : SiteNode not found... (" + baseUrl.toString() + ")");
    }
    currentWebsite = result.get(0);


    while (extUrls.hasMoreElements()) {
      url = extUrls.nextElement();

      if (url.startsWith("http://")) {
        url = url.substring(7);
      } else if (url.startsWith("https://")) {
        url = url.substring(8);
      }

      Query query = entityManager.createQuery(
        "select node from SiteNode node where node.url = :url");
      query.setParameter("url", url);

      result = (List<SiteNode>) query.getResultList();

      SiteNode node;
      boolean error = false;
      if (result.size() == 0) {
        node = new SiteNode();
        node.setUrl(url);
        node.setName(url);
        node.setLanguage(currentWebsite.getLanguage());
        node.setSiteNodeStatus(SiteNodeStatus.UNSEEN);
        if (url.length() <= 50) {
          entityManager.persist(node);
        } else {
          error = true;
        }
      } else {
        node = result.get(0);
      }
      if (!error) {
        connectedNodes.add(node);
      }
    }

    currentWebsite.setName(this.baseUrl.toString());
    currentWebsite.setConnectedSiteNodes(connectedNodes);
    currentWebsite.setPageNumber(this.nbProceed);

    List<SiteNodeFile> fileList = new LinkedList<SiteNodeFile>();
    for (String filePath : this.siteNodeFiles) {
      SiteNodeFile snf = new SiteNodeFile();
      snf.setPath(filePath);
      entityManager.persist(snf);
      fileList.add(snf);
    }
    currentWebsite.setSiteNodeFiles(fileList);

    entityManager.merge(currentWebsite);

  }
}
