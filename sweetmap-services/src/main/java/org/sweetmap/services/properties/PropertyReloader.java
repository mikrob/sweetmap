package org.sweetmap.services.properties;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;


/**
 * property reloader.
 * @author leakim
 *
 */
@Name("propertyReloader")
@Scope(ScopeType.STATELESS)
public class PropertyReloader {

  /**
   * The logger.
   */
  @Logger
  private static Log logger;
  /**
   * The Faces messages.
   */
  @In
  private FacesMessages facesMessages;

  /**
   * Reload properties.
   */
  public void reloadProperties() {
    PropertyManager.getInstance().getProperties();
    logger.debug("Properties reloaded.");
    facesMessages.add(Severity.INFO, "Config Properties have been reloaded");
  }

}