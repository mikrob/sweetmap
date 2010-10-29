package org.sweetmap.enums;

/**
 * Enum that represent the url replacement need for an url in the proxy.
 * @author leakim
 *
 */
public enum ProxyReplacementEnum {

  /**
   * No replacement needed.
   */
  NONE,
  /**
   * url starting with / .
   */
  SLASH,
  /**
   * url starting with ./ .
   */
  DOTSLASH,

  /**
   * Url starting with ../ .
   */
  DOUBLEDOTSLASH,

  /**
   * Url starting with a char or a number : ^[a-zA-Z0-9] .
   */
  CHARORNUMBER,

  /**
   * Url starting with the same url but without wwww.
   * e.g. http://www.dreamisle.net/wordpress/ <-> http://dreamisle.net/wordpress/ .
   */
  SAMEURLWITHOUTWWW,

  /**
   * The url is the same.
   */
  SAMEURL,

  /**
   * The url is the same but without www.
   * e.g. http://www.dreamisle.net <-> http://dreamisle.net
   */
  SAMEURLBUTNOTWWW;
}
