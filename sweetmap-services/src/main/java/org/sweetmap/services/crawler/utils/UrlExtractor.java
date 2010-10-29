package org.sweetmap.services.crawler.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

/**
 * The goal of this class is to extract http links from a html content.
 * @author max
 *
 */
public class UrlExtractor {

  /**
   * Logger.
   */
  @Logger
  private Log logger;

  /**
   * List of all urls contened in the html.
   */
  private List<URL> urlList;

  /**
   * List of all internal domain urls.
   */
  private List<String> localUrlList;

  /**
   * List of all external domain urls.
   */
  private List<String> externalUrlList;

  /**
   * domain of the website.
   */
  private URL domain;

  /**
   * Constructor.
   */
  public UrlExtractor() {
    urlList         = new LinkedList<URL>();
    localUrlList    = null;
    externalUrlList = null;
  }

  /**
   * This method convert is string url to a regular URL.
   * @param strUrl to convert
   * @return URL created.
   * @throws MalformedURLException when it's not possible to formed an URL with the given string.
   */
  private URL string2url(String strUrl) throws MalformedURLException {
    URL url = null;

    // if the URL doesn't start with http:// or https:// then we need to fill it.
    if (!strUrl.startsWith("http://") && !strUrl.startsWith("https://")) {

      if (strUrl.startsWith("/")) {
        url = new URL(domain.toString() + strUrl.toLowerCase());
      } else {
        url = new URL(domain.toString() + "/" + strUrl.toLowerCase());
      }
    } else {
      url = new URL(strUrl.toLowerCase());
    }

    return url;
  }

  /**
   * This method checks that the url doesn't contain funny caracters or javascript calls.
   * @param url to valid
   * @return true or false
   */
  private boolean validURL(String url) {
    // TODO rechercher des caractères interdits.

    if (url == null) {
      return false;
    }

    if (url.startsWith("javascript:") || url.startsWith("mailto:")) {
      return false;
    }

    return true;
  }

  /**
   * method to extract urls from a html file.
   * @param fileName name of the file
   */
  public void proceed(String fileName) {

    InputStream ips;
    Source source = null;
    try {
      ips = new FileInputStream(fileName);
      source = new Source(ips);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    List<Element> elements;

    // traitement des <a href=URL>
    elements = source.getAllElements(HTMLElementName.A);
    for (Element elt : elements) {
      try {
        if (validURL(elt.getAttributeValue("href"))) {
          urlList.add(string2url(elt.getAttributeValue("href")));
        }
      } catch (MalformedURLException e) {
        logger.debug("Impossible to deal with this link : " + elt.getAttributeValue("href"));
      }
    }

    // traitement des <meta http-equiv=refresh content=X;url=URL>
    elements = source.getAllElements(HTMLElementName.META);
    for (Element elt : elements) {
      String refreshHttp = elt.getAttributeValue("http-equiv");
      if (refreshHttp != null && refreshHttp.equals("refresh")) {
        String attribute = elt.getAttributeValue("content");
        int index = attribute.indexOf("=");
        if (index != -1) {
          attribute = attribute.substring(index + 1);
          attribute = attribute.trim();
          try {
            urlList.add(string2url(attribute));
          } catch (MalformedURLException e) {
            logger.debug("Impossible to deal with this link : " + attribute);
          }
        }
      }
    }
  }

  /**
   * Determine all the local urls from the urls list.
   */
  private void findLocalDomainUrls() {
    /*TODO : ignorer les urls avec un # -> ou trouver la page a laquelle elles appartiennent.*/

    this.localUrlList    = new LinkedList<String>();
    this.externalUrlList = new LinkedList<String>();

    for (URL url : this.urlList) {

      if (this.isLocalDomainUrl(url)) {
        this.localUrlList.add(url.toString().toLowerCase());
      } else {
        this.externalUrlList.add(url.toString().toLowerCase());
      }
    }
  }

  /**
   * localUrlList getter.
   * @return urls list
   */
  public List<String> getLocalDomainUrls() {
    if (this.localUrlList == null) {
      this.findLocalDomainUrls();
    }
    return localUrlList;
  }

  /**
   * externalUrlList getter.
   * @return urls list
   */
  public List<String> getExternalUrls() {
    if (this.externalUrlList == null) {
      this.findLocalDomainUrls();
    }
    return externalUrlList;
  }

  /**
   * Determine if the given url is internal domain or not.
   * @param url to test
   * @return true or false
   */
  private boolean isLocalDomainUrl(URL url) {
    if (url.getHost().equals(domain.getHost())) {
      return true;
    }
    return false;
  }

  /**
   * domain setter.
   * @param domain to set
   */
  public void setDomain(URL domain) {
    /* TODO
     * - determiner si http:// ou pas
     * - determiner si www ou pas
     */
    this.domain = domain;
  }
}