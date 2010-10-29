/*
 * $Id: DatabaseIntializer.java $
 *
 * Copyright Travelsoft, 2000-2008.
 */
package org.sweetmap.services.utils;

import javax.ejb.Local;

/**
 * Interface for DatabaseInitializerBean.
 * @author mikael.robert
 *
 */
@Local
public interface DatabaseIntializer {
  /**
   * Initialize default user.
   *
   */
  void initializeUser();

  /**
   * Initialization method.
   */
  void initializeCategory();

  /**
   * Initialize the properties.
   */
  void initializeProperties();

}
