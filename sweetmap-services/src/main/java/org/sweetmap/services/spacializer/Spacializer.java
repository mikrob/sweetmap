package org.sweetmap.services.spacializer;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.SiteNode;
import org.sweetmap.entities.transients.gephi.data.Edge;
import org.sweetmap.entities.transients.gephi.data.Node;
import org.sweetmap.entities.transients.gephi.data.Space;
import org.sweetmap.enums.SiteNodeStatus;
import org.sweetmap.services.spacializer.gephi.layout.ForceAtlas;

/**
 * This class takes care of the SiteNode spacialiszation.
 * @author max
 *
 */
@Name("spacializer")
@Stateless
public class Spacializer implements SpacializerInterface {

  /**
   * The logger.
   */
  @Logger
  private static Log logger;

  /**
   * The entityManager.
   */
  @In
  private EntityManager entityManager;

  /**
   *
   */
  private Map<Integer, SiteNode> siteNodesMap;


  /**
   * This method spacialize the SiteNode when the cron is triggered.
   * @param when : begin date.
   * @param cron : interval, specified as a cron string.
   * @return nothing.
   */
  @Asynchronous
  public QuartzTriggerHandle processSpacialization(@Expiration Date when, @IntervalCron String cron) {
    logger.debug("spacialization is starting, go go go!");
    this.proceed();
    return null;
  }

  /**
   *
   */
  @Asynchronous
  public void proceed() {

    logger.debug("building space for english nodes...");
    Space space = this.buildSpace("en");
    logger.debug("running algo...");
    this.runAlgo(space);
    logger.debug("updating nodes...");
    this.updateSiteNodes(space);
    logger.debug("end of spazialisation");

    logger.debug("building space for french nodes...");
    space = this.buildSpace("fr");
    logger.debug("running algo...");
    this.runAlgo(space);
    logger.debug("updating nodes...");
    this.updateSiteNodes(space);
    logger.debug("end of spazialisation");
  }

  /**
   * This method run the force atlas algorithm over the space.
   * @param space where the algo should run.
   */
  private void runAlgo(Space space) {
    ForceAtlas algo = new ForceAtlas();
    algo.setSpace(space);
    algo.setDefaultValues();

    Date beginDate = new Date();
    Date nowDate = new Date();

    while ((nowDate.getTime() - beginDate.getTime()) / 1000 < 30) {
      nowDate = new Date();
      algo.goAlgo();
    }

    /*for (Node node : space.getNodes()) {
      logger.debug("after algo : " + node);
    }*/
  }

  /**
   * This method build a Space from SiteNodes.
   * @param language to set.
   * @return the space built.
   */
  private Space buildSpace(String language) {
    Query query = entityManager.createQuery("select node from SiteNode node where node.siteNodeStatus=:status "
        + "and language=:language");
    query.setParameter("status", SiteNodeStatus.VALIDATED);
    query.setParameter("language", language);
    List<SiteNode> list = (List<SiteNode>) query.getResultList();
    logger.debug("Spacializer is rebuilding his space ...");
    siteNodesMap = new HashMap<Integer, SiteNode>();

    Space space = new Space();
    Map<Integer, Node> nodesMap   = new HashMap<Integer, Node>();

    int count1 = 0;
    int count2 = 0;

    for (SiteNode siteNode : list) {
      //logger.debug("node  :" + siteNode.getUrl());

      siteNodesMap.put(siteNode.getId().intValue(), siteNode);

      Node node;
      if (nodesMap.containsKey(siteNode.getId().intValue())) {
        node = nodesMap.get(siteNode.getId().intValue());
      } else {
        node = this.siteNode2Node(siteNode);
        space.getNodes().add(node);
        count1++;
        nodesMap.put(siteNode.getId().intValue(), node);
      }

      for (SiteNode nodeLink : siteNode.getConnectedSiteNodes()) {
        if (nodeLink.getSiteNodeStatus() == SiteNodeStatus.VALIDATED) {
          Node temp;
          if (nodesMap.containsKey(nodeLink.getId().intValue())) {
            temp = nodesMap.get(nodeLink.getId().intValue());
          } else {
            temp = this.siteNode2Node(nodeLink);
            space.getNodes().add(temp);
            count2++;
            nodesMap.put(nodeLink.getId().intValue(), temp);
          }

          Edge edge = new Edge(node, temp);
          node.addEdge(edge);
          space.getEdges().add(edge);
          //logger.debug("    linked to : " + nodeLink.getUrl());
        }
      }
    }
    //logger.debug("nb nodes : #0", space.getNodes().size());
    //logger.debug("count1 : #0 | count2 : #1 | sum : #2", count1, count2, list.size());

    return space;
  }

  /**
   *
   * @param space where the new informations are.
   */
  private void updateSiteNodes(Space space) {
    for (Node node : space.getNodes()) {
      if (siteNodesMap.containsKey(node.getId())) {
        SiteNode siteNode = siteNodesMap.get(node.getId());
        siteNode.setxPos(new Double(node.x));
        siteNode.setyPos(new Double(node.y));
        //logger.debug("updating id : " + siteNode.getId() + " x:" + siteNode.getxPos() + " y:" + siteNode.getyPos());
        entityManager.merge(siteNode);
      } else {
        logger.debug("uber warning : this siteNode is not stored in the map... that's quite strange actually.");
      }
    }
    //logger.debug("updated #0 nodes.", space.getNodes().size());
    entityManager.flush();
  }

  /**
   * This method convert a SiteNode to a Node.
   * @param siteNode to convert
   * @return the Node.
   */
  private Node siteNode2Node(SiteNode siteNode) {
    Float x;
    Float y;

    if (siteNode.getxPos() == 0.0) {
      double temp = (Math.random() * 2) - 1.0;
      x = new Float(temp);
    } else {
      x = new Float(siteNode.getxPos());
    }

    if (siteNode.getyPos() == 0.0) {
      double temp = (Math.random() * 2) - 1.0;
      y = new Float(temp);
    } else {
      y = new Float(siteNode.getyPos());
    }


    Node node = new Node(x, y, 0.0f, siteNode.getUrl());
    node.setId(siteNode.getId().intValue());

    return node;
  }

  /**
   *
   */
  @Remove
  public void remove() {


  }

}
