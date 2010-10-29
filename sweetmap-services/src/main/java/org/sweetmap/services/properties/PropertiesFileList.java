/*
 * $Id$
 *
 * Copyright Accelya 2009.
 */
package org.sweetmap.services.properties;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.sweetmap.entities.PropertiesFile;

/**
 * EntityQuery component for propertiesFile.
 * @author leakim
 *
 */
@Name("propertiesFileList")
public class PropertiesFileList extends EntityQuery<PropertiesFile> {

  /**
   *
   */
  private static final long serialVersionUID = 7467118765482360596L;

  /**
   * Constructor.
   */
  public PropertiesFileList() {
    setEjbql("select propertiesFile from PropertiesFile propertiesFile");
  }
}
