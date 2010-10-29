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
package org.sweetmap.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.Length;

/**
 * Entity intended to represent a Text Entry Tag.
 * @author leakim
 *
 */
@Entity
@Table(name = "Tag", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name") })
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Indexed
public class Tag implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 5395866041619709201L;

  /**
   * The id.
   */
  @Id @GeneratedValue
//  @DocumentId
  private Long id;

  /**
   * The tag name.
   */
  @Length(max = 20)
 // @Field(index = Index.TOKENIZED)
  private String name;

  /**
   * text entry list.
   */
  @Basic(fetch = FetchType.LAZY)
  @ManyToMany
  private List<TextEntry> textEntryList;



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
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   *  Hash code method.
   *  @return hashCode the hashcode.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  /**
   * equals method.
   * @param obj the object to compare to.
   * @return true if obj are equals.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Tag)) {
      return false;
    }
    Tag other = (Tag) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  /**
   * @return the textEntryList
   */
  public List<TextEntry> getTextEntryList() {
    return textEntryList;
  }

  /**
   * @param textEntryList the textEntryList to set
   */
  public void setTextEntryList(List<TextEntry> textEntryList) {
    this.textEntryList = textEntryList;
  }


}
