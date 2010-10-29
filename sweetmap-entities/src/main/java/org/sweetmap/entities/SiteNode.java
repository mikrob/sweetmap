package org.sweetmap.entities;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.sweetmap.enums.SiteNodeAction;
import org.sweetmap.enums.SiteNodeStatus;

/**
 * Entity bean intended to represent a site node.
 * @author leakim
 *
 */
/**
 * @author leakim
 *
 */
@Entity
@NamedQueries({
  @NamedQuery(name = "checkExists", query = "select s from SiteNode s where s.url = :url")
})
@Table(name = "SiteNode", uniqueConstraints = @UniqueConstraint(columnNames = "url"))
public class SiteNode implements Serializable {

  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 6684961913347776179L;

  /**
   * The <code>Id</code>.
   */
  @Id
  @GeneratedValue
  private Long id;

  /**
   * The site <code>name</code>.
   */
  @Length(max = 50)
  private String name;


  /**
   * The site <code>url</code>.
   */
  /// TODO Validate the url.
  @NotNull
  private String url;


  /**
   * The description.
   */
  private String description;

  /**
   * Pages number in the website.
   */
  private int pageNumber;

  /**
   * The site node status Validated or not etc ...
   */
  private SiteNodeStatus siteNodeStatus;


  /**
   * The text entry category wanted.
   */
  @ManyToOne
  private Category category;

  /**
   * The xPos.
   */
  private Double xPos = 0d;

  /**
   * The yPos.
   */
  private Double yPos = 0d;

  /**
   * The language of the text entry.
   */
  private String language;

  /**
   * Transient Boolean to know if the site node is checked.
   */
  @Transient
  private Boolean checked;

  /**
   * the <code>connected siteNodes</code>.
   */
  @Basic(fetch = FetchType.LAZY)
  @ManyToMany(targetEntity = SiteNode.class, cascade = CascadeType.ALL)
  private List<SiteNode> connectedSiteNodes;

  /**
   * The website keywords, separated by a coma.
   */
  private String keywords;

  /**
   * This field is set to true if we should extract keywords from the website.
   */
  private Boolean searchKeywords = false;

  /**
   * The site node action to do on it.
   */
  private SiteNodeAction siteNodeAction;


  /**
   * The files associated to this siteNode.
   */
  @Basic(fetch = FetchType.LAZY)
  @OneToMany(targetEntity = SiteNodeFile.class, cascade = CascadeType.ALL)
  private List<SiteNodeFile> siteNodeFiles;

  /**
   * The site node progression in crawl.
   */
  @Transient
  private Long progress = new Long(0);

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the siteNodeFiles
   */
  public List<SiteNodeFile> getSiteNodeFiles() {
    return siteNodeFiles;
  }

  /**
   * @param siteNodeFiles the siteNodeFiles to set
   */
  public void setSiteNodeFiles(List<SiteNodeFile> siteNodeFiles) {
    this.siteNodeFiles = siteNodeFiles;
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
    this.name = name.toLowerCase();
  }

  /**
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
    String tmp = url.toLowerCase();

    if (tmp.startsWith("http://")) {
      tmp = tmp.substring(7);
    } else if (tmp.startsWith("https://")) {
      tmp = tmp.substring(8);
    }

    if (tmp.endsWith("/")) {
      tmp = tmp.substring(0, tmp.length() - 1);
    }

    this.url = tmp;
  }

  /**
   * @return the connectedSiteNodes
   */
  public List<SiteNode> getConnectedSiteNodes() {
    return connectedSiteNodes;
  }

  /**
   * @param connectedSiteNodes the connectedSiteNodes to set
   */
  public void setConnectedSiteNodes(List<SiteNode> connectedSiteNodes) {
    this.connectedSiteNodes = connectedSiteNodes;
  }


  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the page number.
   */
  public int getPageNumber() {
    return pageNumber;
  }

  /**
   * @param pageNumber the page number to set.
   */
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  /**
   * @return the siteNodeStatus
   */
  public SiteNodeStatus getSiteNodeStatus() {
    return siteNodeStatus;
  }

  /**
   * @param siteNodeStatus the siteNodeStatus to set
   */
  public void setSiteNodeStatus(SiteNodeStatus siteNodeStatus) {
    this.siteNodeStatus = siteNodeStatus;
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
   * @return the keywords list
   */
  public String getKeywords() {
    return keywords;
  }

  /**
   * @param keywords list to set
   */
  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  /**
   * @return the searchKeywords value (true / false)
   */
  public Boolean getSearchKeywords() {
    return searchKeywords;
  }

  /**
   * @param searchKeywords to set (true / false)
   */
  public void setSearchKeywords(Boolean searchKeywords) {
    this.searchKeywords = searchKeywords;
  }


  /**
   * @return the xPos
   */
  public Double getxPos() {
    return xPos;
  }

  /**
   * @param xpos the xPos to set
   */
  public void setxPos(Double xpos) {
    this.xPos = xpos;
  }

  /**
   * @return the yPos
   */
  public Double getyPos() {
    return yPos;
  }

  /**
   * @param ypos the yPos to set
   */
  public void setyPos(Double ypos) {
    this.yPos = ypos;
  }

  /**
   * @return the siteNodeAction
   */
  public SiteNodeAction getSiteNodeAction() {
    return siteNodeAction;
  }

  /**
   * @param siteNodeAction the siteNodeAction to set
   */
  public void setSiteNodeAction(SiteNodeAction siteNodeAction) {
    this.siteNodeAction = siteNodeAction;
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
   * @return the progress
   */
  public Long getProgress() {
    return progress;
  }

  /**
   * @param progress the progress to set
   */
  public void setProgress(Long progress) {
    this.progress = progress;
  }

  /**
   * hashCode method.
   * @return hashCode the hashCode.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result
        + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((keywords == null) ? 0 : keywords.hashCode());
    result = prime * result + ((language == null) ? 0 : language.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + pageNumber;
    result = prime * result
        + ((searchKeywords == null) ? 0 : searchKeywords.hashCode());
    result = prime * result
        + ((siteNodeAction == null) ? 0 : siteNodeAction.hashCode());
    result = prime * result
        + ((siteNodeStatus == null) ? 0 : siteNodeStatus.hashCode());
    result = prime * result + ((url == null) ? 0 : url.hashCode());
    return result;
  }

  /**
   * Equals method.
   * @param obj the obj to compare to.
   * @return true if equals.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof SiteNode)) {
      return false;
    }
    SiteNode other = (SiteNode) obj;
    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (keywords == null) {
      if (other.keywords != null) {
        return false;
      }
    } else if (!keywords.equals(other.keywords)) {
      return false;
    }
    if (language == null) {
      if (other.language != null) {
        return false;
      }
    } else if (!language.equals(other.language)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (pageNumber != other.pageNumber) {
      return false;
    }
    if (searchKeywords == null) {
      if (other.searchKeywords != null) {
        return false;
      }
    } else if (!searchKeywords.equals(other.searchKeywords)) {
      return false;
    }
    if (siteNodeAction == null) {
      if (other.siteNodeAction != null) {
        return false;
      }
    } else if (!siteNodeAction.equals(other.siteNodeAction)) {
      return false;
    }
    if (siteNodeStatus == null) {
      if (other.siteNodeStatus != null) {
        return false;
      }
    } else if (!siteNodeStatus.equals(other.siteNodeStatus)) {
      return false;
    }
    if (url == null) {
      if (other.url != null) {
        return false;
      }
    } else if (!url.equals(other.url)) {
      return false;
    }
    return true;
  }

  /**
   * @return the checked
   */
  public Boolean getChecked() {
    return checked;
  }

  /**
   * @param checked the checked to set
   */
  public void setChecked(Boolean checked) {
    this.checked = checked;
  }







}
