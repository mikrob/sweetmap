/**
 * sweetmapopyright (C) 2009 Mikael Robert
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
package org.sweetmap.utils.file;

import org.apache.log4j.Logger;


/**
 * This class enumerates the differents types of extension validate.
 *
 * @author Fabre-Lucien
 *
 */
public enum FileExtensionEnum {

  /**
   * The list of the types of extenssion.
   */

  /**
   * .doc.
   */
  DOC(".doc", "application/msword"),

  /**
   * .csv.
   */
  CSV(".csv", "application/msexcel"),

  /**
   * .txt.
   */
  TXT(".txt", "text/plain"),

  /**
   * .pdf.
   */
  PDF(".pdf", "application/pdf"),

  /**
   * .rtf.
   */
  RTF(".rtf", "application/rtf"),

  /**
   * .odt.
   */
  ODT(".odt", "application/vnd.oasis.opendocument.text"),

  /**
   * .gif.
   */
  GIF(".gif", "image/gif"),

  /**
   * .jpg.
   */
  JPG(".jpg", "image/jpeg");

  /**
   * the logger.
   */
  public static final Logger LOGGER
    = Logger.getLogger(FileExtensionEnum.class);


  /**
   * the extension.
   */
  private String extensionString;


  /**
   * the contentType.
   */
  private String contentType;


  /**
   * the class constructor.
   * @param extensionName the name of extension.
   * @param contentTypeName the name of contentType.
   */
  FileExtensionEnum(String extensionName, String contentTypeName) {
    this.setExtensionString(extensionName);
    this.setContentType(contentTypeName);
  }


  /**
   * This class manages the ExtensionFileType.
   * @param fileName the name of file.
   * @return the type of file extension.
   */
  public static FileExtensionEnum getExtension(String fileName) {
    LOGGER.debug("-> start getExtension method: ");

    FileExtensionEnum result = null;

    if (fileName.toLowerCase().endsWith(DOC.getExtensionString())) {
      LOGGER.debug("found DOC extension for " + fileName);
      result = DOC;
    } else if (fileName.toLowerCase().endsWith(TXT.getExtensionString())) {
      LOGGER.debug("found TXT extension for " + fileName);
      result = TXT;
    } else if (fileName.toLowerCase().endsWith(PDF.getExtensionString())) {
      LOGGER.debug("found PDF extension for " + fileName);
      result = PDF;
    } else if (fileName.toLowerCase().endsWith(RTF.getExtensionString())) {
      LOGGER.debug("found RTF extension for " + fileName);
      result = RTF;
    } else if (fileName.toLowerCase().endsWith(ODT.getExtensionString())) {
      LOGGER.debug("found ODT extension for " + fileName);
      result = ODT;
    } else if (fileName.toLowerCase().endsWith(GIF.getExtensionString())) {
      LOGGER.debug("found GIF extension for " + fileName);
      result = GIF;
    } else if (fileName.toLowerCase().endsWith(JPG.getExtensionString())) {
      LOGGER.debug("found JPG extension for " + fileName);
      result = JPG;
    }  else if (fileName.toLowerCase().endsWith(CSV.getExtensionString())) {
      LOGGER.debug("found CSV extension for " + fileName);
      result = CSV;
    } else {
      LOGGER.error("-> the extension is not exist " + fileName);
    }

    return result;
  }

  /**
   * the setter of the extensionString.
   * @param extensionString the extensionString to set.
   */
  public void setExtensionString(String extensionString) {
    this.extensionString = extensionString;
  }

  /**
   * the getter of the extensionString.
   * @return the extensionString
   */
  public String getExtensionString() {
    return extensionString;
  }

  /**
   * the setter of the contentType.
   * @param contentType the contentType to set.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * the getter of the contentType.
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }
}
