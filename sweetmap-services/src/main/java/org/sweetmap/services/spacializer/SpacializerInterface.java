package org.sweetmap.services.spacializer;

import java.util.Date;

import javax.ejb.Local;

import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

/**
 *
 * @author max
 *
 */
@Local
public interface SpacializerInterface {
  /**
   *
   */
  void proceed();

  /**
   *
   */
  void remove();


  /**
   * This method spacialize the SiteNode when the cron is triggered.
   * @param when : begin date.
   * @param cron : interval, specified as a cron string.
   * @return nothing.
   */
  QuartzTriggerHandle processSpacialization(@Expiration Date when, @IntervalCron String cron);

}
