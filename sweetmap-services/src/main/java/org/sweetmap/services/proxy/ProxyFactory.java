package org.sweetmap.services.proxy;


/**
 * The proxy factory.
 * Inteded to build proxies object.
 * @author leakim
 *
 */
public class ProxyFactory {

  /**
   * return a proxy instance.
   * @param websiteUrl the url for the proxy.
   * @param urlToReplace the url to replace with
   * @return proxy the proxy.
   */
  public static IProxy getProxyInstance(String websiteUrl, String urlToReplace) {
    IProxy proxy = new Proxy();
    proxy.setUrl(websiteUrl);
    proxy.setUrlToReplace(urlToReplace);
    return proxy;
  }
}
