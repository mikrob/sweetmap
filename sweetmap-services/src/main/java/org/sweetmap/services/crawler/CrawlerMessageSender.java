package org.sweetmap.services.crawler;

import javax.jms.QueueSender;
import javax.jms.QueueSession;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * Seam Message Sender.
 * @author mikrob
 *
 */

@Name("crawlerMessageSender")
public class CrawlerMessageSender {
  /**
   * The logger.
   */
  @Logger
  private Log logger;

  /**
   * the QueueSender.
   */
  @In(create = true)
  private transient QueueSender crawlerQueueSender;

  /**
   * The queue sessions.
   */
  @In(create = true)
  private transient QueueSession queueSession;


  /**
   * Send a message to be receveid by the Phone.
   * @param file the file the uncompress.
   */
  public void send(String file) {
    try {
      crawlerQueueSender.send(queueSession.createObjectMessage(file));
    } catch (Exception ex) {
      logger.error("Cannot send a message to CrawlerMDB in crawlerMessageSender");
      ex.printStackTrace();
    }

  }


}
