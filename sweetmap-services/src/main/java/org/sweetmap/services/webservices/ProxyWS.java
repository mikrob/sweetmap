package org.sweetmap.services.webservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.sweetmap.services.proxy.ProxyManager;

/**
 * Proxy WS.
 * @author leakim
 *
 */
@Path("/ProxyWS")
@Name("proxyWS")
public class ProxyWS {

  /**
   * The logger.
   */
  @Logger
  private Log logger;
  /**
   * The entityManager.
   */
  @In
  private ProxyManager proxyManager;


  /**
   * Get Site node from database and generate a xml stream in response to a http get request
   * on http://serverUrl:8080/sweetmap/seam/resource/rest/ProxyWS.
   * @param url the url.
   * @return xmlStream the stream.
   */
  @GET
  @Path("/{url}")
  @Produces("text/html")
  public String getCustomer(@PathParam("url") String url) {
    logger.debug("PROXYWS Called with url #0", url);
    String virgReplaced = url.replaceAll(",,,", "/");
    logger.debug("VIRGULE REPLACED #0", virgReplaced);
    String newUrl = "http://";
    newUrl += virgReplaced;
    if (!newUrl.endsWith("/")) {
      newUrl += "/";
    }

    proxyManager.setUrl(newUrl);
    proxyManager.runProxy();
    return proxyManager.getHtmlCode();
  }

}
