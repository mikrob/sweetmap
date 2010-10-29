package org.sweetmap.services.crawler.exception;

/**
 * This exception is thrown when the baseUrl parameter is not set.
 * @author max
 *
 */
public class NullBaseUrlException extends Exception {

  /**
   * serial ID.
   */
  private static final long serialVersionUID = -9059161300257007898L;

  /**
   * Constructor.
   */
  public NullBaseUrlException() {
    super("The base url parameter is not set.");
  }

}
