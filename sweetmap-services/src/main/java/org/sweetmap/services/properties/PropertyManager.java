/*
 * $Id$
 *
 * Copyright Accelya 2009.
 */
package org.sweetmap.services.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * This class handles all the configuration data for the application.
 *
 * @author mikael.robert
 * @version $Revision$ $Date$
 */
public final class PropertyManager {


  /** the singleton instance. */
  private static PropertyManager instance;

  /** The configuration properties.*/
  private Properties configProperties;


  /**
   * The path of the configPropertie file intialized during the boot sequence.
   */
  private String configPropertiesPath;


  /** constructor.
   *
   * */
  private PropertyManager()  {

  }

  /**
   * return the instance of the singleton.
   * @return instance the instance
   */
  public static PropertyManager getInstance() {
    if (instance == null) {
      instance = new PropertyManager();
    }
    return instance;
  }

  /**
   * Load the file properties.
   *
   */
  public void getProperties() {
    if (!"".equals(configPropertiesPath)) {
      configProperties = loadProperties(configPropertiesPath);
    }
  }

  /**
   * Load the properties for a given config file name.
   * @param filePath The config filePath
   * @return The loaded properties
   */
  private Properties loadProperties(String filePath) {
    Properties properties = null;
    try {
      InputStream inputStream = new FileInputStream(filePath);
      properties = new Properties();
      properties.load(inputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

  /**
   * Searches for the property with the specified key in this property list.
   * @param property
   *            the property key
   * @return the value in this property list with the specified key value.
   */
  public String getProperty(String property) {
    if (configProperties == null) {
      this.getProperties();
    }
    return configProperties.getProperty(property);
  }

  /**
   * Returns the property (as an integer) for the given key.
   *
   * @param property
   *            the property key.
   * @return the property value (as an integer).
   */
  public Integer getPropertyAsInt(String property) {
    String p = this.getProperty(property);
    if (p != null) {
      return Integer.parseInt(p);
    } else {
      return null;
    }
  }

  /**
   * Returns the property (as a float) for the given key.
   *
   * @param property
   *            the property key.
   * @return the property value (as an integer).
   */
  public Float getPropertyAsFloat(String property) {
    String p = this.getProperty(property);
    if (p != null) {
      return Float.parseFloat(p);
    } else {
      return null;
    }
  }

  /**
   * Returns the property (as an boolean) for the given key.
   * @param property the property key.
   * @return the property value (as an boolean).
   */
  public boolean getPropertyAsBool(String property) {
    return new Boolean(this.getProperty(property)).booleanValue();
  }

  /**
   * @return the configPropertiesPath
   */
  public String getConfigPropertiesPath() {
    return configPropertiesPath;
  }

  /**
   * @param configPropertiesPath the configPropertiesPath to set
   */
  public void setConfigPropertiesPath(String configPropertiesPath) {
    this.configPropertiesPath = configPropertiesPath;
  }

  /**
   * Return the properties file content as a string.
   * @return string representing the file content.
   */
  public String getFileAsString() {
    return configProperties.toString();
  }

}
