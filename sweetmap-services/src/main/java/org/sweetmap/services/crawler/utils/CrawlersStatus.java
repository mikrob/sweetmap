package org.sweetmap.services.crawler.utils;

import java.util.HashMap;
import java.util.Map;

import org.sweetmap.entities.SiteNode;

/**
 * The aim of the class is to give information about current crawls and their status.
 * @author max
 *
 */
public final class CrawlersStatus {


  /**
   * CrawlersStatus instance.
   */
  private static CrawlersStatus instance = null;

  /**
   * The map where information are stocked.
   */
  private Map<SiteNode, Long> mapStatus;



  /**
   * constructor.
   */
  private CrawlersStatus() {
    this.mapStatus = new HashMap<SiteNode, Long>();
  }

  /**
   * CrawlersStatus instance getter.
   * @return the CrawlersStatus instance
   */
  public static CrawlersStatus getInstance() {
    if (instance == null) {
      instance = new CrawlersStatus();
    }
    return instance;
  }

  /**
   * @return the mapStatus
   */
  public Map<SiteNode, Long> getMapStatus() {
    return mapStatus;
  }
}
