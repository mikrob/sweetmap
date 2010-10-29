package org.sweetmap.enums;

/**
 * The Site Node Action.
 * @author leakim
 *
 */
public enum SiteNodeAction {
  /**
   * Text entry is published.
   */
  NOTHING ("Nothing"),
  /**
   *Text entry is unpublished.
   */
  CRAWL("Crawl"),


  /**
   * Waiting status.
   */
  KEYWORD_EXTRACT("Keywords Extraction");

  /**
   * fullname.
   */
  private String fullname;


  /**
   * Constructor.
   * @param fullnam1 the real name of the status.
   */
  private SiteNodeAction(String fullnam1) {
    fullname = fullnam1;
  }

  /**
   * return the full name.
   * @return fullname the full name.
   */
  public String getFullName() {
    return fullname;
  }

}
