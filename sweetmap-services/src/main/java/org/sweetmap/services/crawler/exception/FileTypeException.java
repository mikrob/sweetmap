package org.sweetmap.services.crawler.exception;

/**
 * Exception class that declare unsupported file format by the crawler.
 * @author max
 *
 */
public class FileTypeException extends Exception {

  /**
   * serial ID.
   */
  private static final long serialVersionUID = -825972712491116974L;

  /**
   * file type.
   */
  private final String fileType;

  /**
   * Constructor.
   */
  public FileTypeException() {
    super();
    this.fileType = "";
  }

  /**
   * Constructor.
   * @param fileType not supported
   */
  public FileTypeException(String fileType) {
    super("The following file type is not supported : " + fileType);
    this.fileType = fileType;
  }

  /**
   * getter for fileType.
   * @return the file type.
   */
  public String getFileType() {
    return this.fileType;
  }
}
