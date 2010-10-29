package org.sweetmap.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity intended to manage file properties.
 * @author leakim
 *
 */
@Entity
public class PropertiesFile implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1370825645874217453L;

  /**
   *  Id.
   */
  @Id @GeneratedValue
  private Long id;

  /**
   *
   */
  private String filePath;

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the filePath
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * @param filePath the filePath to set
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  ///TODO Add hashCode and equals.


}
