/*
 * $Id$
 *
 * Copyright Dreamisle.net 2009.
 */
package org.sweetmap.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.validator.Length;

/**
 * Comment entity.
 * @author leakim
 *
 */
@Entity
public class Comment implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 4553805804730162320L;

  /**
   * Id.
   */
  @Id @GeneratedValue
  private Long id;


  /**
   *
   */
  private Date postedDate = new Date();

  /**
   * The title of the comment.
   */
  @Length(max = 20)
  private String title;


  /**
   * The content of the comment.
   */
  @Length(max = 100000)
  @Basic(fetch = FetchType.LAZY)
  private String content;

  /**
   * The text entry which owns this comment.
   */
  @ManyToOne
  private TextEntry textEntry;

  /**
   * Constructor.
   */
  public Comment() {
    this.content = null;
    this.title = null;
  }

  /**
   * Return a formated title.
   * @return title formated.
   */
  @Transient
  public String getFormatedTitle() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    StringBuilder sb = new StringBuilder();
    if (this.getPostedDate() != null) {
      sb.append(sdf.format(this.getPostedDate()));
    }
    sb.append(" : ");
    sb.append(this.getTitle());
    return sb.toString();
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
   * @return the date
   */
  public Date getPostedDate() {
    return postedDate;
  }


  /**
   * @param date the date to set
   */
  public void setPostedDate(Date date) {
    this.postedDate = date;
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
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }





}
