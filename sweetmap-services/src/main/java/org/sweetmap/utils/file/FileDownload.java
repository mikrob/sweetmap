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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

/**
 * @author mikael.robert
 * TODO move to package Util
 */
@Name("downloadFile")
public class FileDownload {


  /**
   * logger.
   */
  @Logger
  private static Log logger;


  /**
   * the user file to download.
   */
  private File userFile;

  /**
   * the name of the file to download.
   */
  private String userFileName;


  /**
   * @param userFile
   *            the userFile to set
   */
  public void setUserFile(File userFile) {
    this.userFile = userFile;
  }

  /**
   * @param userFileName
   *            the userFileName to set
   */
  public void setUserFileName(String userFileName) {
    this.userFileName = userFileName;
  }

  /**
   * @return the userFile
   */
  public File getUserFile() {
    if (this.userFile != null) {
      logger.info("the file to download is not null");
    } else {
      logger.warn("the file to download is null !!!");
    }
    return userFile;
  }

  /**
   * return the file content type.
   * @return contentType the content type.
   */
  public Object getContentType() {
    return getContentType(getUserFile().getName());
  }

  /**
   * return the file name.
   * @return fileName the fileName
   */
  public String getFileName() {
    if (userFileName != null && !userFileName.equals("")) {
      return userFileName;
    }
    return userFile.getName();
  }

  /**
   * return the file content.
   * @return content the content.
   * @throws IOException ex.
   */
  public byte[] getContent() throws IOException {
    byte[] result = convertFileToByteArray(getUserFile());
    return result;
  }


  /**
   * @return the content size.
   * @throws IOException the exception.
   */
  int getContentSize() throws IOException {
    return getContent().length;
  }

  /**
   * render the byte[] on the servlet response.
   * @throws IOException ex.
   */
  public void render() throws IOException {
    FacesContext facesContext =  FacesContext.getCurrentInstance();
    if (!facesContext.getResponseComplete()) {
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
      response.setContentType(getContentType().toString());
      response.setContentLength(getContentSize());
      // Set the content disposition
      response.setHeader("Content-disposition",
          "attachment; filename=" + getFileName());
      ServletOutputStream out = response.getOutputStream();
      out.write(getContent());
      out.flush();
      facesContext.responseComplete();
    }
  }

  /**
   * convert io.file to byte[].
   *
   * @param file
   *            the file to convert
   * @throws IOException
   *             the exception generated
   * @return the byte array.
   */
  public static byte[] convertFileToByteArray(File file) throws IOException {
    //logger.info("file length before converting : " + file.length());
    byte[] fileInByte = new byte[(int) file.length()];
    logger.info("byte array created");
    try {
      FileInputStream fileInputStream = new FileInputStream(file);
      logger.info("reading ...");
      fileInputStream.read(fileInByte);
      logger.info("reading finished sucessfully");
    } catch (FileNotFoundException e) {
      logger.error("File Not Found.", e);
    } catch (IOException e1) {
      logger.error("Error Reading The File.", e1);
    }
    logger.info("byte array length : #0", fileInByte.length);
    return fileInByte;
  }


  /**
   * This method gives the name of extension file.
   * @param fileName the name of file.
   * @return the type of file extension.
   */
  public static String getContentType(String fileName)  {
    String contentType = FileExtensionEnum.getExtension(fileName).getContentType();
    return contentType;
  }

}
