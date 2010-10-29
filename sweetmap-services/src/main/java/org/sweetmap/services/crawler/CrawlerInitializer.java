package org.sweetmap.services.crawler;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


/**
 * The crawler initializer.
 * @author leakim
 *
 */
@Name("crawlerInitializer")
@Scope(ScopeType.STATELESS)
@AutoCreate
public class CrawlerInitializer {

  /**
   * Crawler message sender.
   */
  @In(required = true, create = true)
  private CrawlerMessageSender crawlerMessageSender;


  /**
   * init method.
   */
  @Create
  public void init() {
    //crawlerMessageSender.send("http://dreamisle.net");
    //crawlerMessageSender.send("http://inzemix.org");
  }
}
