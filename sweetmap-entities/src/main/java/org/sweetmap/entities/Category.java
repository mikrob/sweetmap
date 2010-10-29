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

import java.awt.Color;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.Length;


/**
 * Entity intended to represent a category.
 * @author leakim
 *
 */
@Entity
//@Table(name = "Category", uniqueConstraints = {@UniqueConstraint(columnNames = "name") })
//@Indexed
public class Category implements Serializable {


  /**
   *
   */
  private static final long serialVersionUID = 7914985749162794736L;


  /**
   * ID.
   */
  @Id @GeneratedValue
//  @DocumentId
  private Long id;


  /**
   * The name of the category.
   */
  @Length(max = 25)
  //@Field(index = Index.TOKENIZED)
  private String name;

  /**
   * The Category color.
   */
  private Color color;

  /**
   * The language of the category.
   */
  private String language;

  /**
   * the text entry linked to this category.
   */
  @OneToMany
  private List<TextEntry> textEntrySet;

  /**
   * Site Node List.
   */
  @OneToMany
  private List<SiteNode> siteNodeList;



  /**
   * @return the textEntrySet
   */
  public List<TextEntry> getTextEntrySet() {
    return textEntrySet;
  }


  /**
   * @param textEntrySet the textEntrySet to set
   */
  public void setTextEntrySet(List<TextEntry> textEntrySet) {
    this.textEntrySet = textEntrySet;
  }


  /**
   * @return the siteNodeList
   */
  public List<SiteNode> getSiteNodeList() {
    return siteNodeList;
  }


  /**
   * @param siteNodeList the siteNodeList to set
   */
  public void setSiteNodeList(List<SiteNode> siteNodeList) {
    this.siteNodeList = siteNodeList;
  }


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
   *Hash code method.
   *@return hashCode the hashcode.
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
   * Equals method.
   * @param obj the object to compare.
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
    if (!(obj instanceof Category)) {
      return false;
    }
    Category other = (Category) obj;
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
   * @return the color
   */
  public Color getColor() {
    return color;
  }


  /**
   * @param color the color to set
   */
  public void setColor(Color color) {
    this.color = color;
  }


  /**
   * @return the language
   */
  public String getLanguage() {
    return language;
  }


  /**
   * @param language the language to set
   */
  public void setLanguage(String language) {
    this.language = language;
  }



}

