package org.sweetmap.services.crawler.exception;

/**
 * Exception class that declare unsupported http return code by the crawler.
 * @author max
 *
 */
public class HttpCodeException extends Exception {

  /**
   * Serial ID.
   */
  private static final long serialVersionUID = 1815193082222405144L;

  /**
   * Constructor.
   */
  public HttpCodeException() {
    super();
  }

  /**
   * Constructor.
   * @param code not supported
   */
  public HttpCodeException(int code) {
    super("Unsupported HTTP code : " + code);
  }
}
