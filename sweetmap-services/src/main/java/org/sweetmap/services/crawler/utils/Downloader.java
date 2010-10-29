package org.sweetmap.services.crawler.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.sweetmap.services.crawler.exception.Error404Exception;
import org.sweetmap.services.crawler.exception.FileTypeException;
import org.sweetmap.services.crawler.exception.HttpCodeException;

// TODO se faire passer pour firefox
// TODO temporiser le downloader : genre 3 ou 4 secondes.

/**
 * This class let the crawler download a page from a url.
 * @author max
 *
 */
public class Downloader {

  /**
   * HttpClient : web page downloader.
   */
  private HttpClient httpclient;

  /**
   * Default constructor.
   */
  public Downloader() {
    this.resetHttpClient();
  }

  /**
   * This method reset the HttpClient and its parameters.
   */
  public void resetHttpClient() {
    this.httpclient = new DefaultHttpClient();
    httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (X11; U; Linux i686; fr; rv:1.9.1.1) Gecko/20090715 Firefox/3.5.1");
  }

  /**
   * Method to download a page from a given URL.
   * @param url to fetch
   * @return filename where the content was downloaded
   * @throws IOException in case of error while writting in the file
   * @throws FileTypeException in case of unsupported file format at the url
   * @throws Error404Exception in case of 404 page
   * @throws HttpCodeException in case of unsupported http code
   */
  public String download(URL url) throws IOException, FileTypeException, Error404Exception, HttpCodeException {

    HttpGet httpget       = new HttpGet(url.toString());
    HttpResponse response = httpclient.execute(httpget);
    String filename       = null;
    HttpEntity entity     = response.getEntity();

    int returnCode = response.getStatusLine().getStatusCode();

    try {
      // determine a random number between [ 500 ; 4000[ millisec.
      int number = (int) (Math.random() * 3500.0) + 500;
      Thread.sleep(number);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    switch (returnCode) {
      case 200:
        // code de retour normal
        break;
      case 404:
        // this is done to fix a httpclient bug.
        // in case of an 404 error, we abort the request an this cause an exception for the next request.
        this.resetHttpClient();
        throw new Error404Exception(url.toString());
      default:
        this.resetHttpClient();
        throw new HttpCodeException(returnCode);
    }

    if (entity != null) {

      // on teste si c'est un fichier html.
      String type = entity.getContentType().toString();
      if (type.indexOf("text/html") == -1 && type.indexOf("text/plain") == -1) {
        httpget.abort();
        throw new FileTypeException(type);
      }

      InputStream is = entity.getContent();
      DataInputStream dis = new DataInputStream(new BufferedInputStream(is));

      filename = url.toString();
      filename = filename.replace('/', '-');

      FileOutputStream out = new FileOutputStream("/tmp/" + filename);

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
   * Method to download a page from a given URL.
   * @param url to fetch
   * @return filename where the content was downloaded
   * @throws IOException in case of error while writting in the file
   * @throws FileTypeException in case of unsupported file format at the url
   * @throws Error404Exception in case of 404 page
   * @throws HttpCodeException in case of unsupported http code
   */
  public String download(String url)
    throws IOException, FileTypeException, Error404Exception, HttpCodeException {
    try {
      URL u = new URL(url);
      return this.download(u);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * method to set a proxy.
   * @param host for the proxy
   * @param port for the proxy
   */
  public void setProxy(String host, int port) {
    HttpHost proxy = new HttpHost(host, port);
    httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

  }
}
