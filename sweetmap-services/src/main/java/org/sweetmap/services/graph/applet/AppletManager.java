package org.sweetmap.services.graph.applet;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


/**
 * The applet manager.
 * @author leakim
 *
 */
@Name("appletManager")
@Scope(ScopeType.PAGE)
public class AppletManager {


  /**
   * Return the applet code.
   * @return tag the tag to insert the applet.
   */
  public String getAppletTag() {
    String context = "http://cartedutendre.inzemix.org/testJung/";
    String codebase = context + "jar/";
    StringBuffer appletTag = new StringBuffer();
    appletTag.append("<applet archive=\"test.jar, collections-generic-4.01.jar, colt-1.2.0.jar, concurrent-1.3.4.jar, "
        + " jung-3d-2.0.jar, jung-algorithms-2.0.jar, jung-api-2.0.jar, jung-graph-impl-2.0.jar, jung-io-2.0.jar,"
        + " jung-jai-2.0.jar, jung-visualization-2.0.jar, stax-api-1.0.1.jar, wstx-asl-3.2.6.jar, jaxen-1.1.1.jar, "
        + " dom4j-1.6.1.jar, commons-collections-3.2.1.jar\"");
    appletTag.append(" code=\"testJung.AppletApplication\"");
    appletTag.append(" codebase=\"").append(codebase).append("\"");
    appletTag.append(" height=\"900\" id=\"applet1\" width=\"900\">");
    appletTag.append("</applet>");
    return appletTag.toString();
  }
}
