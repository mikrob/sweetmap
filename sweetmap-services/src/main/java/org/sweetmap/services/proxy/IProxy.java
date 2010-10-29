package org.sweetmap.services.proxy;

/**
 * Interface representing a Proxy.
 * @author leakim
 *
 */
public interface IProxy {

  /**
   * Set the url.
   * @param url the url to go.
   */
  void setUrl(String url);

  /**
   * The url to replace.
   * @param urlToReplace the url to replace.
   */
  void setUrlToReplace(String urlToReplace);

  /**
   * get The page.
   * @param savePath the path to save the file.
   */
  void getPage(String savePath);

  /**
   * Act as a proxy by replacing the foreign url.
   * @return file the file modified.
   */
  String replaceUrl();
}
