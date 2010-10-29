package org.sweetmap.enums;

/**
 * Site Node Status.
 * @author leakim
 *
 */
public enum SiteNodeStatus {

  /**
   * Text entry is published.
   */
  VALIDATED ("validated"),
  /**
   *Text entry is unpublished.
   */
  UNVALIDATED ("unvalidated"),


  /**
   * Waiting status.
   */
  WAITING("waiting"),

  /**
   * Unseen status.
   */
  UNSEEN("unseen");

  /**
   * fullname.
   */
  private String fullname;


  /**
   * Constructor.
   * @param fullnam1 the real name of the status.
   */
  private SiteNodeStatus(String fullnam1) {
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
