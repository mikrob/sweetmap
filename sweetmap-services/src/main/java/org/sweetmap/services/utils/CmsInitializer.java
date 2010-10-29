/**
 * sweetmappyright (C) 2009 Mikael Robert
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sweetmap.services.utils;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.log.Log;
import org.sweetmap.services.crawler.CrawlerInitializer;
import org.sweetmap.services.properties.PropertyManager;

/**
 * Crm initializer. Initialize base data for the good processing of the application.
 * @author leakim
 *
 */
@Name("cmsInitializer")
public class CmsInitializer {

  /**
   * Logger.
   */
  @Logger
  private Log logger;
  /**
   * Intializer ejb stateless.
   */
  @In(required = true, create = true)
  private DatabaseIntializer databaseIntializer;

  /**
   * The crawler initializer.
   */
  @In(required = true, create = true)
  private CrawlerInitializer crawlerInitializer;
  /**
   * Initialization method.
   */
  @Observer("org.jboss.seam.postInitialization")
  public void initialize() {
    logger.debug("Initializing application ...........");
    databaseIntializer.initializeUser();
    databaseIntializer.initializeCategory();
    crawlerInitializer.init();
    databaseIntializer.initializeProperties();
    if (PropertyManager.getInstance().getPropertyAsBool("proxy")) {
      logger.debug("Need to add proxy");
    }
  }


}
