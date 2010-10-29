/*
 * $Id$
 *
 * Copyright Dreamisle.net 2009.
 */
package org.sweetmap.services.webservices;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.enums.SiteNodeStatus;


/**
 * Data Site Node Exporter (WebService with resteasy).
 * Access : http://serverUrl:8080/sweetmap/seam/resource/rest/SiteNodeExporter
 * @author leakim
 *
 */
@Path("/SiteNodeExporter")
@Name("siteNodeExporter")
@Scope(ScopeType.SESSION)
public class SiteNodeDataExporterWS {


  /**
   * The logger.
   */
  @Logger
  private Log logger;
  /**
   * The entityManager.
   */
  @In
  private EntityManager entityManager;


  /**
   * Get Site node from database and generate a xml stream in response to a http get request
   * on http://serverUrl:8080/sweetmap/seam/resource/rest/SiteNodeExporter.
   *
   * @return xmlStream the stream.
   */
  @GET
  //@Path("/{customerId}") used previously to test param
  @Produces("text/xml")
  @SuppressWarnings("unchecked")
  public String getCustomer(/*@PathParam("customerId") int id*/) {
    logger.debug("WEB SERVICE CALLED");
    logger.debug(" USER LANGUAGE = #0", LocaleSelector.instance().getLanguage());
    Locale loc = LocaleSelector.instance().getLocale();
    Query query = entityManager.createQuery(
        "select node from SiteNode node where node.language = :language AND node.siteNodeStatus = :status");

    query.setParameter("language", LocaleSelector.instance().getLanguage().trim());
    query.setParameter("status", SiteNodeStatus.VALIDATED);

    List<SiteNode> nodeSiteList = (List<SiteNode>) query.getResultList();
    StringBuffer xmlBuffer = null;
    StringBuffer edgeBuffer = new StringBuffer("<edges>");
    int edgeId = 0;
    try {
      if (nodeSiteList != null && nodeSiteList.size() > 0) {
        logger.info("Generating gexf xml file");
        xmlBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlBuffer.append("<gexf xmlns=\"http://www.gephi.org/gexf\" xmlns:viz=\"http://www.gephi.org/gexf/viz\">\n");
        xmlBuffer.append("<graph type=\"static\">\n");
        xmlBuffer.append("<attributes class=\"node\" type=\"static\">\n");
        xmlBuffer.append("<attribute id=\"label\" title=\"label\" type=\"string\"/>\n");
        xmlBuffer.append("</attributes>\n");
        xmlBuffer.append("<nodes>\n");
        for (SiteNode node : nodeSiteList) {
          xmlBuffer.append("<node id=\"" + node.getId() + "\" label=\"" + node.getName() + "\">\n");
          if (node.getCategory() != null && node.getCategory().getColor() != null) {
            xmlBuffer.append("<viz:color b=\"" + node.getCategory().getColor().getBlue()
                + "\" g=\"" + node.getCategory().getColor().getGreen()
                + "\" r=\"" + node.getCategory().getColor().getRed() + "\"/>\n");
          } else {
            xmlBuffer.append("<viz:color b=\"0\" g=\"0\" r=\"255\"/>\n");
          }
          xmlBuffer.append("<viz:position x=\"" + node.getxPos() + "\" y=\"" + node.getyPos() + "\" z=\"0.0\"/>\n");
          xmlBuffer.append("<viz:size value=\"2.0029697\"/>\n");
          xmlBuffer.append("<attvalues>\n");
          xmlBuffer.append("<attvalue id=\"0\" value=\"" + node.getUrl() + "\"/>\n");
          xmlBuffer.append("</attvalues>\n");
          xmlBuffer.append("</node>\n");
          for (SiteNode nodeConnected : node.getConnectedSiteNodes()) {
            if (nodeConnected.getSiteNodeStatus() != null
                && nodeConnected.getSiteNodeStatus().equals(SiteNodeStatus.VALIDATED)) {
              edgeBuffer.append("<edge id=\"");
              edgeBuffer.append(edgeId);
              edgeBuffer.append("\" source=\"");
              edgeBuffer.append(node.getId());
              edgeBuffer.append("\"");
              edgeBuffer.append(" target=\"");
              edgeBuffer.append(nodeConnected.getId());
              edgeBuffer.append("\"/> \n");
            }
            edgeId++;
          }
        }
        xmlBuffer.append("</nodes>\n");
        edgeBuffer.append("</edges> \n");
        xmlBuffer.append(edgeBuffer.toString());
        xmlBuffer.append("</graph>\n");
        xmlBuffer.append("</gexf>\n");

      } else {
        logger.error("No Node found");
        xmlBuffer = new StringBuffer("No node found");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return xmlBuffer.toString();
  }

}