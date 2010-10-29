package org.sweetmap.services.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.Filter;
import org.jboss.seam.web.AbstractFilter;

/**
 * Add the wanted filter to the http header.
 * @author leakim
 *
 */
@Startup
@Scope(ScopeType.APPLICATION)
@Name("responseHeaderFilter")
@BypassInterceptors
@Filter(within = "org.jboss.seam.web.ajax4jsfFilter")
public class ResponseHeaderFilter extends AbstractFilter {

  /**
   * Add the http header to bypass the proxy cache.
   * @param req the request.
   * @param res the response.
   * @param filt the filter.
   * @throws IOException ex.
   * @throws ServletException ex2.
   */
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain filt) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;
    response.addHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
    response.addHeader("Cache-Control", "private, no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
    filt.doFilter(req, response);
  }

}