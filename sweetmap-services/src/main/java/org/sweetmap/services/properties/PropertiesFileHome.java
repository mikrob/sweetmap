package org.sweetmap.services.properties;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;
import org.sweetmap.entities.PropertiesFile;

/**
 * Home component for PropertiesFIle.
 * @author leakim
 *
 */
@Name("propertiesFileHome")
public class PropertiesFileHome extends EntityHome<PropertiesFile> {

  /**
   *
   */
  private static final long serialVersionUID = 7901189259772280738L;
  /**
   * The request parameter used to get the propertiesFile Id.
   */
  @RequestParameter
  private Long propertiesFileId;

  /**
   * Override the getId method to give it the given id with request parameter.
   * @return id the id.
   */
  @Override
  public Object getId() {
    if (propertiesFileId == null) {
      return super.getId();
    } else {
      return propertiesFileId;
    }
  }

  /**
   * Ovveride the create method if needed.
   */
  @Override @Begin
  public void create() {
    super.create();
  }

  /**
   * Return the content of this properties file.
   * @return string representation of the file content.
   */
  public String getContent() {
    if (this.instance.getFilePath().equals(PropertyManager.getInstance().getConfigPropertiesPath())) {
      return PropertyManager.getInstance().getFileAsString();
    } else {
      return null;
    }
  }

}
