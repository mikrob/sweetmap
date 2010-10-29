package org.sweetmap.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity bean intended to represent a file associated to a SiteNode.
 * @author max
 */
@Entity
public class SiteNodeFile implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 6822405456504573716L;

  /**
   * Id.
   */
  @GeneratedValue
  @Id
  private Long id;

  /**
   * the path where the file is stored.
   */
  private String path;

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
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @param path the path to set
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @return hascode.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
  }

  /**
   * @param obj the object to compare.
   * @return true if equal
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SiteNodeFile)) {
      return false;
    }
    SiteNodeFile other = (SiteNodeFile) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (path == null) {
      if (other.path != null) {
        return false;
      }
    } else if (!path.equals(other.path)) {
      return false;
    }
    return true;
  }

}
