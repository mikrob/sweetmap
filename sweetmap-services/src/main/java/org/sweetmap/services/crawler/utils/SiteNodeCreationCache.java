package org.sweetmap.services.crawler.utils;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * The goal of this class is to make sure a given website is only registered
 * one time in the database.
 * @author max
 *
 */
public final class SiteNodeCreationCache {

  /**
   * Singleton reference.
   */
  private static SiteNodeCreationCache cacheReference;

  /**
   * HashMap where SiteNode websites that will be soon inserted in the database
   * are declared to avoid multiple insertions.
   */
  private Map<String, Boolean> cacheMap;

  /**
   * HashMap where websites that are currently crawl are declared.
   */
  private WeakHashMap<String, Boolean> cacheMapCrawling;

  /**
   * Private constructor.
   */
  private SiteNodeCreationCache() {
    cacheMap = new WeakHashMap<String, Boolean>();
    cacheMapCrawling = new WeakHashMap<String, Boolean>();
  }

  /**
   * Method to get singleton reference.
   * @return singleton reference
   */
  public static SiteNodeCreationCache getInstance() {
    if (cacheReference == null) {
      cacheReference = new SiteNodeCreationCache();
    }
    return cacheReference;
  }

  /**
   * this method insert a website url in the hashmap.
   * @param url to add in the hashmap
   * @return true if the website was successfully added.
   */
  public synchronized boolean addWebsite(String url) {
    if (cacheMap.containsKey(url)) {
      return false;
    }
    cacheMap.put(url, true);
    return true;
  }

  /**
   * This method let the crawler declare which website is currently under crawl.
   * In this way, other crawler won't add this website to the database.
   * @param url to add in the hashmap
   * @return true if the website was successfully added.
   */
  public synchronized boolean addWebsiteToCrawl(String url) {
    if (cacheMapCrawling.containsKey(url)) {
      return false;
    }
    cacheMapCrawling.put(url, true);
    return true;
  }

  /**
   * check if the given url is already registered in the haspmap.
   * @param url to check
   * @return true if already registered, else false.
   */
  public boolean keyExists(String url) {
    if (cacheMapCrawling.containsKey(url) || cacheMap.containsKey(url)) {
      return true;
    }
    return false;
  }

  /**
   * Method to remove an url from the hashmap.
   * @param url to remove
   */
  public void removeKey(String url) {
    // TODO a faire plus proprement.
    if (keyExists(url)) {
      cacheMap.remove(url);
      cacheMapCrawling.remove(url);
    }
  }

  /**
   * Return the items number in the cache.
   * @return items number in the cache
   */
  public int size() {
    return cacheMap.size();
  }
}
