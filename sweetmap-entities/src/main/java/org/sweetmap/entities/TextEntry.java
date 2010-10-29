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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.sweetmap.enums.TextEntryStatus;

/**
 * Entity which represent a user text entry.
 * @author leakim
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Name("textEntry")
public class TextEntry implements Serializable {

  /**
   * uid.
   */
  private static final long serialVersionUID = 792694656532097709L;

  /**
   * Unique Id.
   */
  @Id @GeneratedValue
  private Long id;

  /**
   * Text Entry title.
   */
  @NotNull @Length(max = 70)
  private String title;

  /**
   * Text Content.
   */
  @NotNull
  @Basic(fetch = FetchType.LAZY)
  @Length(max = 500)
  private String content;


  /**
   *
   */
  @NotNull
  private Date date = new Date();

  /**
   * The language of the text entry.
   */
  private String language;


  /**
   * The category.
   */

  @ManyToOne
  private Category category;

  /**
   * The status.
   */
  private TextEntryStatus status;

  /**
   * The user who writed this text entry.
   */
  private String user;

  /**
   * The user who added this text.
   */
  private String addedBy;


  /**
   * The comments of the text entry.
   */
  @OneToMany(cascade = CascadeType.ALL)
  @OrderBy(value = "postedDate desc")
  private List<Comment> commentList = new ArrayList<Comment>();


  /**
   * The tags list for the text entry.
   */
  @ManyToMany(cascade = CascadeType.ALL)
  @OrderBy(value = "name")
  private List<Tag> tagList = new ArrayList<Tag>();

  /**
   *
   */
  public TextEntry() {
    this.commentList.add(0, new Comment());

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
   * @return the title
   */
  public String getTitle() {
    return title;
  }


  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }


  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * Return the content by replacing the \n by br.
   * @return content.
   */
  public String getContentForView() {
    return content.replaceAll("\n", "<br \\/>");
  }


  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }


  /**
   * @return the date
   */
  public Date getDate() {
    return date;
  }


  /**
   * @param date the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }


  /**
   * @return the category
   */
  public Category getCategory() {
    return category;
  }


  /**
   * @param category the category to set
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Return a formated title.
   * @return title formated.
   */
  @Transient
  public String getFormatedTitle() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    StringBuilder sb = new StringBuilder();
    if (this.getDate() != null) {
      sb.append(sdf.format(this.getDate()));
    }
    sb.append(" : ");
    sb.append(this.getTitle());
    return sb.toString();
  }


  /**
   * @return the commentList
   */
  public List<Comment> getCommentList() {
    return commentList;
  }


  /**
   * @param commentList the commentList to set
   */
  public void setCommentList(List<Comment> commentList) {
    this.commentList = commentList;
  }
  /**
   * @return the tagList
   */
  public List<Tag> getTagList() {
    return tagList;
  }
  /**
   * @param tagList the tagList to set
   */
  public void setTagList(List<Tag> tagList) {
    this.tagList = tagList;
  }

  /**
   * @return the status
   */
  public TextEntryStatus getStatus() {
    return status;
  }
  /**
   * @param status the status to set
   */
  public void setStatus(TextEntryStatus status) {
    this.status = status;
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

  /**
   * Hash code method.
   * @return hashCode the hashcode.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
      + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    return result;
  }
  /**
   * equals method.
   * @param obj the object to compare to.
   * @return true if object are equals.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof TextEntry)) {
      return false;
    }
    TextEntry other = (TextEntry) obj;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (title == null) {
      if (other.title != null) {
        return false;
      }
    } else if (!title.equals(other.title)) {
      return false;
    }
    return true;
  }
  /**
   * @return the user
   */
  public String getUser() {
    return user;
  }
  /**
   * @param user the user to set
   */
  public void setUser(String user) {
    this.user = user;
  }
  /**
   * @return the addedBy
   */
  public String getAddedBy() {
    return addedBy;
  }
  /**
   * @param addedBy the addedBy to set
   */
  public void setAddedBy(String addedBy) {
    this.addedBy = addedBy;
  }



}
