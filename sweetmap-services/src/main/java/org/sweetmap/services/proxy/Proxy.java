package org.sweetmap.services.proxy;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.sweetmap.enums.ProxyReplacementEnum;
import org.sweetmap.services.crawler.exception.FileTypeException;


/**
 * Object intended to represent a proxy for a web page.
 * @author leakim
 *
 */
public class Proxy implements IProxy {

  /**
   * The url visited.
   */
  private String url;

  /**
   * The url to replace.
   */
  private String urlToReplace;

  /**
   * Url Without the WWW.
   */
  private String urlWithoutWww = null;


  /**
   * The file downloaded.
   */
  private String file;

  /**
   * Set the url.
   * @param url the url.
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Getter for the url.
   * @return url the url.
   */
  public String getUrl() {
    return url;
  }


  /**
   * @return the file
   */
  public String getFile() {
    return file;
  }

  /**
   * @param file the file to set
   */
  public void setFile(String file) {
    this.file = file;
  }


  /**
   * Download the page on the website.
   * @param savePath the path where to save the page.
   */
  public void getPage(String savePath) {
    try {
      file = download(url, savePath);
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (FileTypeException e) {
      e.printStackTrace();
    }
  }

  /**
   * replace the urls.
   * @return file the html file modified.
   */
  public String replaceUrl() {
    InputStream ips;
    Source source = null;
    StringBuffer filePath  = new StringBuffer(ProxyManager.PATH);
    filePath.append(file);
    filePath.append(".html");
    try {
      ips = new FileInputStream(filePath.toString());
      source = new Source(ips);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //meme url mais sans www
    int www = url.indexOf("www.");
    if (www >= 0) {
      String begin = url.substring(0, www);
      int endStartAt = www + 4;
      String end = url.substring(endStartAt, url.length());
      urlWithoutWww = begin + end;
      //System.out.println("URL WITHOUT WWW BUILDED : " + urlWithoutWww);
    }

    OutputDocument outputDocument = new OutputDocument(source);
    outputDocument = replaceUrls(outputDocument, source);
    outputDocument = replaceStylesheet(outputDocument, source);
    outputDocument = replaceJavaScript(outputDocument, source);
    outputDocument = replaceImg(outputDocument, source);
    return outputDocument.toString();
  }


   /**
    * Download a page with the corresponding url, to the given savePath.
    * @param url the url .
    * @param savePath the path where to save.
    * @return filename the file name.
    * @throws IOException ex2.
    * @throws FileTypeException ex3.
    */
  private static String download(URL url, String savePath)
    throws IOException, FileTypeException {
    HttpClient httpclient = new DefaultHttpClient();
    /**
     * Proxy
     */
    System.out.println("URL : " + url.toString());
    HttpGet httpget       = new HttpGet(url.toString());
    HttpResponse response = httpclient.execute(httpget);
    String filename       = null;
    HttpEntity entity     = response.getEntity();


    if (entity != null) {
      String type = entity.getContentType().toString();
      if (type.indexOf("text/html") == -1) {
        httpget.abort();
        throw new FileTypeException(type);
      }

      InputStream is = entity.getContent();
      DataInputStream dis = new DataInputStream(new BufferedInputStream(is));

      filename = url.toString();
      filename = filename.replace('/', '-');

      FileOutputStream out = new FileOutputStream(savePath + filename + ".html");

      byte[] buffer = new byte[1024];
      int length = 0;
      do {
        length = dis.read(buffer);
        if (length != -1) {
          out.write(buffer, 0, length);
        }
      } while(length != -1);
    }
    httpget.abort();
    return filename;
  }
  /**
   * REplace the Urls.
   * @param doc the document.
   * @param source the source.
   * @return outputdocument th edocument.
   */
  private OutputDocument replaceUrls(OutputDocument doc, Source source) {
    //System.out.println("BEGIN REPLACE URL WITH URL :" + url);
    if (!urlToReplace.endsWith("/")) {
      urlToReplace += "/";
    }
    StringBuffer buffer = new StringBuffer();
    List<StartTag> linkStartTags = source.getAllStartTags(HTMLElementName.A);
    String converted = null;
    String testString = "proxy.seam?url=";
    int proxyseam = urlToReplace.indexOf(testString);
    String newUrlToReplace = null;
    if (proxyseam >= 0) {
      newUrlToReplace = urlToReplace.substring(0, proxyseam + testString.length());
    }

    for (Iterator<StartTag> i = linkStartTags.iterator(); i.hasNext();) {
      StartTag startTag = (StartTag) i.next();
      Attributes attributes = startTag.getAttributes();
      String href = attributes.getValue("href");
      if (href != null) {
        //System.out.println("TESTING URL : " + href);
        ProxyReplacementEnum replacementType = aHrefNneedToBeReplaced(href);
        switch(replacementType) {
          case NONE: {
            //converted = newUrlToReplace + href;
            converted = "#";
            break;
          }
          case SLASH: {
           // System.out.println("SLASH");
            //converted = urlToReplace + href.substring(1, href.length());
            converted = "#";
            break;
          }
          case DOTSLASH: {
            //System.out.println("DOT SLASH");
            //converted = urlToReplace + href.substring(2, href.length());
            converted = "#";
            break;
          }
          case DOUBLEDOTSLASH: {
            //System.out.println("DOUBLE DOT SLASH");
            //converted = urlToReplace + href.substring(3, href.length());
            converted = "#";
            break;
          }
          case CHARORNUMBER: {
            //System.out.println("CHAR OR NUMBER");
           // converted = urlToReplace + href;
            converted = "#";
            break;
          }
          case SAMEURL: {
            //System.out.println("SAME URL ");
            //converted = href.replaceAll(url, this.urlToReplace + url);
            converted = "#";
            break;
          }
          case SAMEURLBUTNOTWWW: {
            //System.out.println("SAME URL BUT NOT WWW ");
            //converted = newUrlToReplace + urlWithoutWww;
            converted = "#";
            break;
          }
          case SAMEURLWITHOUTWWW: {
            //System.out.println("SAME URL WITHOUT WWW");
            //System.out.println("HREF =" +href);
            //newUrlToReplace = urlToReplace.substring(0, proxyseam + testString.length());
            //converted = newUrlToReplace + href;
            converted = "#";
            //System.out.println("SET CONVERTED TO :" + converted);
            break;
          }
          default :
            break;
        }

        buffer = new StringBuffer();
        buffer.append("<a href=\"");
        buffer.append(converted);
        buffer.append("\">");
        doc.replace(startTag, buffer);
        /*
        System.out.println("Replace URL : " + startTag.toString() + "WITH" + buffer.toString());
        System.out.println();
        System.out.println();
        */

      }
    }
    return doc;
  }

  /**
   * REplace the stylesheet.
   * @param doc the document.
   * @param source the source.
   * @return outputdocument th edocument.
   */
  private OutputDocument replaceStylesheet(OutputDocument doc, Source source) {
    /**
     * stylesheet replacement
     */
    StringBuffer sb = new StringBuffer();
    List<StartTag> linkStartTagsLink = source.getAllStartTags(HTMLElementName.LINK);
    for (Iterator<StartTag> i = linkStartTagsLink.iterator(); i.hasNext();) {
      StartTag startTag = (StartTag) i.next();
      Attributes attributes = startTag.getAttributes();
      String rel = attributes.getValue("rel");
      if ("stylesheet".equalsIgnoreCase(rel)) {
        String href = attributes.getValue("href");
        boolean donothing = false;
        if (href != null && !href.startsWith("http") && !href.startsWith("www")) {
          sb = new StringBuffer();
          sb.append("<link rel=\"stylesheet\" ");
          Attribute typeAttribute = attributes.get("type");
          if (typeAttribute != null) {
            sb.append(" ");
            sb.append(typeAttribute);
          }
          sb.append(" media=\"screen\"");
          sb.append(" href=\"");
          ProxyReplacementEnum replacementType = aHrefNneedToBeReplaced(href);
          StringBuffer newHref = new StringBuffer(url);
          switch(replacementType) {
            case NONE: {
              newHref.append(href);
              break;
            }
            case SLASH: {
              //System.out.println("SLASH");
              newHref.append(href.substring(1, href.length()));
              break;
            }
            case DOTSLASH: {
              //System.out.println("DOT SLASH");
              newHref.append(href.substring(2, href.length()));
              break;
            }
            case DOUBLEDOTSLASH: {
              //System.out.println("DOUBLE DOT SLASH");
              newHref.append(href.substring(3, href.length()));
              break;
            }
            case CHARORNUMBER: {
              //System.out.println("CHAR OR NUMBER");
              newHref.append(href);
              break;
            }
            case SAMEURLWITHOUTWWW: {
              //System.out.println("SAME URL WITHOUT WWW");
              donothing = true;
              break;
            }
            default :
              break;


          }
          sb.append(newHref.toString());
          sb.append("\"");
          if (startTag.toString().endsWith("/>")) {
            sb.append(" />\n");
          } else {
            sb.append(">\n");
          }

          if (!donothing) {
            //System.out.println("Replace : " + startTag.toString() + " with " + sb.toString());
            doc.replace(startTag, sb);
          } else {
            //System.out.println("NOTHING TO DO FOR " + startTag.toString());
          }
        }
      }
    }
    return doc;
  }
  /**
   * REplace the javascript.
   * @param doc the document.
   * @param source the source.
   * @return outputdocument th edocument.
   */
  private OutputDocument replaceJavaScript(OutputDocument doc, Source source) {
    /**
     * Javascript replacement
     */
    StringBuffer sb2 = new StringBuffer();
    List<StartTag> linkStartTagsScript = source.getAllStartTags(HTMLElementName.SCRIPT);
    for (Iterator<StartTag> i = linkStartTagsScript.iterator(); i.hasNext();) {
      StartTag startTag2 = (StartTag) i.next();
      Attributes attributes = startTag2.getAttributes();
      String src = attributes.getValue("src");
      if (src != null && !src.startsWith("http://") && !src.startsWith("www")) {
       // System.out.println("SRC for Javascript = " + src);
        boolean donothing = false;
        sb2 = new StringBuffer();
        sb2.append("<script type=\"text/javascript\" ");
        sb2.append("src=\"");
        ProxyReplacementEnum replacementType = aHrefNneedToBeReplaced(src);
        StringBuffer newHref = new StringBuffer(url);
        switch(replacementType) {
          case NONE: {
            newHref.append(src);
            break;
          }
          case SLASH: {
            //System.out.println("SLASH");
            newHref.append(src.substring(1, src.length()));
            break;
          }
          case DOTSLASH: {
            //System.out.println("DOT SLASH");
            newHref.append(src.substring(2, src.length()));
            break;
          }
          case DOUBLEDOTSLASH: {
            //System.out.println("DOUBLE DOT SLASH");
            newHref.append(src.substring(3, src.length()));
            break;
          }
          case CHARORNUMBER: {
            //System.out.println("CHAR OR NUMBER");
            newHref.append(src);
            break;
          }
          case SAMEURLWITHOUTWWW: {
            //System.out.println("SAME URL WITHOUT WWW");
            donothing = true;
            break;
          }
          default :
            break;


        }
        sb2.append(newHref.toString());
        sb2.append("\"");
        if (startTag2.toString().endsWith("/>")) {
          sb2.append("/>\n");
        } else {
          sb2.append(">\n");
        }
        if (!donothing) {
          //System.out.println("Replace : " + startTag2.toString() + " with " + sb2.toString());
          doc.replace(startTag2, sb2);
        }
      }
    }
    return doc;
  }
  /**
   * REplace the images.
   * @param doc the document.
   * @param source the source.
   * @return outputdocument th edocument.
   */
  private OutputDocument replaceImg(OutputDocument doc, Source source) {

    StringBuffer sb3 = new StringBuffer();
    List<StartTag> linkStartTagsImg = source.getAllStartTags(HTMLElementName.IMG);
    for (Iterator<StartTag> i = linkStartTagsImg.iterator(); i.hasNext();) {
      StartTag startTag2 = (StartTag) i.next();
      Attributes attributes = startTag2.getAttributes();
      String src = attributes.getValue("src");
      String width = attributes.getValue("width");
      String clazz = attributes.getValue("class");
      String title = attributes.getValue("title");
      String alt = attributes.getValue("alt");
      String height = attributes.getValue("height");
      if (src != null && !src.startsWith("http://") && !src.startsWith("www")) {
        sb3 = new StringBuffer();
        boolean donothing = false;
        sb3.append("<img ");
        sb3.append("src=\"");
        ProxyReplacementEnum replacementType = aHrefNneedToBeReplaced(src);
        StringBuffer newHref = new StringBuffer(url);
        switch(replacementType) {
          case NONE: {
            newHref.append(src);
            break;
          }
          case SLASH: {
            //System.out.println("SLASH");
            newHref.append(src.substring(1, src.length()));
            break;
          }
          case DOTSLASH: {
            //System.out.println("DOT SLASH");
            newHref.append(src.substring(2, src.length()));
            break;
          }
          case DOUBLEDOTSLASH: {
            //System.out.println("DOUBLE DOT SLASH");
            newHref.append(src.substring(3, src.length()));
            break;
          }
          case CHARORNUMBER: {
            //System.out.println("CHAR OR NUMBER");
            newHref.append(src);
            break;
          }
          case SAMEURLWITHOUTWWW: {
            //System.out.println("SAME URL WITHOUT WWW");
            donothing = true;
            break;
          }
          default :
            break;


        }
        sb3.append(newHref.toString());
        sb3.append("\"");
        if (width != null) {
          sb3.append(" width=\"");
          sb3.append(width);
          sb3.append(" \"");
        }
        if (clazz != null) {
          sb3.append(" class=\"");
          sb3.append(clazz);
          sb3.append(" \"");
        }
        if (title != null) {
          sb3.append(" title=\"");
          sb3.append(title);
          sb3.append(" \"");
        }
        if (alt != null) {
          sb3.append(" alt=\"");
          sb3.append(alt);
          sb3.append(" \"");
        }
        if (height != null) {
          sb3.append(" height=\"");
          sb3.append(height);
          sb3.append(" \"");
        }
        if (startTag2.toString().endsWith("/>")) {
          sb3.append("/>\n");
        } else {
          sb3.append(">\n");
        }
        if (!donothing) {
          //System.out.println("Replace : " + startTag2.toString() + " with " + sb3.toString());
          doc.replace(startTag2, sb3);
        }
      }
    }
    return doc;
  }

   /**
    * Download a page with the given string url. (Build the Url Class)
    * @param url the url.
    * @param savePath the path.
    * @return fileName the fileName.
    * @throws IOException ex2.
    * @throws FileTypeException ex3.
    */
  private static String download(String url, String savePath)
    throws  IOException, FileTypeException {
    try {
      URL u = new URL(url);
      return download(u, savePath);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Set the url to replace.
   * @param urlToReplace the urlToReplace.
   */
  public void setUrlToReplace(String urlToReplace) {
    this.urlToReplace = urlToReplace;
  }

  /**
   * Getter for url to replace.
   * @return url the url to replace.
   */
  public String getUrlToReplace() {
    return this.urlToReplace;
  }

  /**
   * Test if a href need to replaced.
   * @param urlTested the url tested
   * @return true if need replacement.
   */
  private ProxyReplacementEnum aHrefNneedToBeReplaced(String urlTested) {
    // par defaut on considere que pas besoin de remplacement
    ProxyReplacementEnum result = ProxyReplacementEnum.NONE;
    // l'url est la meme (lien vers index home ...)
    String urlLessSlash = url.substring(0, url.length() - 1);
    if (urlTested.trim().equals(url.trim()) || urlTested.trim().equals(urlLessSlash)) {
      result = ProxyReplacementEnum.SAMEURL;
    }
    // commence avec /
    if (urlTested.startsWith("/")) {
      result = ProxyReplacementEnum.SLASH;
    }
    // commence avec ./
    if (urlTested.startsWith("./")) {
      result = ProxyReplacementEnum.DOTSLASH;
    }
    // commence avec ../
    if (urlTested.startsWith("../")) {
      result = ProxyReplacementEnum.DOUBLEDOTSLASH;
    }
    if (urlWithoutWww != null) {
      String urlWithoutWwwLessSlash = urlWithoutWww.substring(0, urlWithoutWww.length() - 1);
      if (urlTested.trim().equals(urlWithoutWww) || urlTested.trim().equals(urlWithoutWwwLessSlash)) {
        result = ProxyReplacementEnum.SAMEURLBUTNOTWWW;
      } else if (urlTested.trim().startsWith(urlWithoutWww)) {
        result = ProxyReplacementEnum.SAMEURLWITHOUTWWW;
      }

    }

    //commence par une lettre de a-z A-Z ou 0-9
    if (!urlTested.startsWith("http://") && !urlTested.startsWith("www.")) {
      Pattern p = Pattern.compile("^[a-zA-Z0-9]");
      Matcher m = p.matcher(urlTested);
      boolean match = m.find();
      if (match) {
        result = ProxyReplacementEnum.CHARORNUMBER;
      }
    }
    return result;
  }


}
