package org.sweetmap.services.spacializer.gephi.layout;
/*
Copyright 2008 WebAtlas
Authors : Mathieu Bastian, Mathieu Jacomy, Julian Bilcke
Website : http://www.gephi.org

This file is part of Gephi.

Gephi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gephi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gephi.  If not, see <http://www.gnu.org/licenses/>.
 */


//TODO : commenter la classe ForceAtlas

import org.sweetmap.entities.transients.gephi.data.Edge;
import org.sweetmap.entities.transients.gephi.data.Node;
import org.sweetmap.services.spacializer.gephi.layout.Algorithm;


/**
 * Force vector algorithm class.
 *
 * @author Mathieu Jacomy
 */
public class ForceAtlas extends ForceVector implements Algorithm {

  /**
   *
   */
  public double inertia;

  /**
   *
   */
  public double repulsionStrength;

  /**
   *
   */
  public double attractionStrength;

  /**
   *
   */
  public double maxDisplacement;

  /**
   *
   */
  public boolean freezeBalance;

  /**
   *
   */
  public double freezeStrength;

  /**
   *
   */
  public double freezeInertia;

  /**
   *
   */
  public double gravity;

  /**
   *
   */
  public boolean outboundAttractionDistribution;

  /**
   *
   */
  public boolean adjustSizes;

  /**
   * @return .
   */
  public boolean initAlgo() {
    return true;
  }

  /**
   *
   */
  public void setDefaultValues() {
    super.setDefaultValues();
    inertia = 0.1;
    repulsionStrength = 10;
    attractionStrength = 10;
    maxDisplacement = 10;
    freezeBalance = true;
    freezeStrength = 30;
    freezeInertia = 0.2;
    gravity = 30;
    outboundAttractionDistribution = false;
    adjustSizes = false;
  }

  /**
   *
   */
  public void goAlgo() {
    for (Node n : space.getNodes()) {
      n.temp.oldDx = n.temp.dx;
      n.temp.oldDy = n.temp.dy;
      n.temp.dx *= inertia;
      n.temp.dy *= inertia;
    }
    // repulsion
    if (adjustSizes) {
      for (Node n1 : space.getNodes()) {
        for (Node n2 : space.getNodes()) {
          if (n1 != n2) {
            fcBiRepulsorNoCollide(n1, n2, repulsionStrength * (1 + n1.getNeighboursCount()) * (1 + n2.getNeighboursCount()));
          }
        }
      }
    } else {
      for (Node n1 : space.getNodes()) {
        for (Node n2 : space.getNodes()) {
          if (n1 != n2) {
            fcBiRepulsor(n1, n2, repulsionStrength * (1 + n1.getNeighboursCount()) * (1 + n2.getNeighboursCount()));
          }
        }
      }
    }
    // attraction
    if (adjustSizes) {
      if (outboundAttractionDistribution) {
        for (Edge e : space.getEdges()) {
          Node nf = e.getNodeFrom();
          Node nt = e.getNodeTo();
          //double bonus = (nf.getNodeContext().fixed || nt.getNodeContext().fixed)?(100):(1);
          double bonus = 1;
          fcBiAttractorNoCollide(nf, nt, bonus * attractionStrength / (1 + nf.getNeighboursCount()));
        }
      } else {
        for (Edge e : space.getEdges()) {
          Node nf = e.getNodeFrom();
          Node nt = e.getNodeTo();
          //double bonus = (nf.getNodeContext().fixed || nt.getNodeContext().fixed)?(100):(1);
          double bonus = 1;
          fcBiAttractorNoCollide(nf, nt, bonus * attractionStrength);
        }
      }
    } else {
      if (outboundAttractionDistribution) {
        for (Edge e : space.getEdges()) {
          Node nf = e.getNodeFrom();
          Node nt = e.getNodeTo();
          //double bonus = (nf.getNodeContext().fixed || nt.getNodeContext().fixed)?(100):(1);
          double bonus = 1;
          fcBiAttractor(nf, nt, bonus * attractionStrength / (1 + nf.getNeighboursCount()));
        }
      } else {
        for (Edge e : space.getEdges()) {
          Node nf = e.getNodeFrom();
          Node nt = e.getNodeTo();
          //double bonus = (nf.getNodeContext().fixed || nt.getNodeContext().fixed)?(100):(1);
          double bonus = 1;
          fcBiAttractor(nf, nt, bonus * attractionStrength);
        }
      }
    }
    // gravity
    for (Node n : space.getNodes()) {

      double d = 0.0001 + Math.sqrt(n.x * n.x + n.y * n.y);
      double gf = 0.0001 * gravity * d;
      n.temp.dx -= gf * n.x / d;
      n.temp.dy -= gf * n.y / d;
    }
    // speed
    if (freezeBalance) {
      for (Node n : space.getNodes()) {
        n.temp.dx *= speed * 10f;
        n.temp.dy *= speed * 10f;

      }
    } else {
      for (Node n : space.getNodes()) {
        n.temp.dx *= speed;
        n.temp.dy *= speed;
      }
    }
    // apply forces
    for (Node n : space.getNodes()) {
      //if(!n.getNodeContext().fixed){
      double d = 0.0001 + Math.sqrt(n.temp.dx * n.temp.dx + n.temp.dy * n.temp.dy);
      float ratio;
      if (freezeBalance) {
        n.temp.freeze = (float) (freezeInertia * n.temp.freeze + (1 - freezeInertia) * 0.1 * freezeStrength * (Math.sqrt(Math.sqrt((n.temp.oldDx - n.temp.dx) * (n.temp.oldDx - n.temp.dx) + (n.temp.oldDy - n.temp.dy) * (n.temp.oldDy - n.temp.dy)))));
        ratio = (float) Math.min(d / (d * (1f + n.temp.freeze)), maxDisplacement / d);
      } else {
        ratio = (float) Math.min(1, maxDisplacement / d);
      }
      n.temp.dx *= ratio / cooling;
      n.temp.dy *= ratio / cooling;
      float x = n.x + n.temp.dx;
      float y = n.y + n.temp.dy;
      if (Float.isNaN(x) || Float.isNaN(y)) {
        System.out.println("NAN error on " + n.getLabel());
      }
      n.x = x;
      n.y = y;
      //}
    }
  }
}
