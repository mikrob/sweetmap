package org.sweetmap.services.crawler.exception;

/**
 * This class let the crawler declare that it found a 404 page while crawling.
 * @author max
 *
 */
public class Error404Exception extends Exception {

  /**
   * serial ID.
   */
  private static final long serialVersionUID = -398353644575923200L;

  /**
   * class constructor.
   * @param url of the 404 page
   */
  public Error404Exception(String url) {
    super("The url : " + url + " doesn't exist.");
  }
}
