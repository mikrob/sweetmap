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

package org.sweetmap.services.spacializer.gephi.layout;

import org.sweetmap.entities.transients.gephi.data.Node;
import org.sweetmap.services.spacializer.gephi.layout.AbstractAlgorithm;
import org.sweetmap.services.spacializer.gephi.layout.Algorithm;


//TODO : commenter la classe ForceVector

/**
 * Force vector algorithm class.
 *
 * @author Mathieu Jacomy
 */
public abstract class ForceVector extends AbstractAlgorithm implements Algorithm {

  /**
   *
   */
  public double speed;

  /**
   *
   */
  public double cooling;

  /**
   * default constructor.
   */
  public ForceVector() {

  }

  /**
   *
   */
  public void setDefaultValues() {
    speed = 1;
    cooling = 1;
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @return .
   */
  protected float distanceXYZ(Node n1, Node n2) {
    double sqrsum = ((n2.x - n1.x) * (n2.x - n1.x)) + ((n2.y - n1.y) * (n2.y - n1.y)) + ((n2.z - n1.z) * (n2.z - n1.z));
    return (float) (Math.sqrt(sqrsum));
  }

  /**
   *
   * @return .
   */
  protected float getAutoArea() {
    return 10f * (float) Math.sqrt(space.getNodesCount());
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcBiRepulsor(Node n1, Node n2, double c) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;

    // distance tout court
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

    if (dist > 0) {
      double f = repulsion(c, dist);

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   * @param verticalization .
   */
  protected void fcBiRepulsorY(Node n1, Node n2, double c, double verticalization) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;

    // distance tout court
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

    if (dist > 0) {
      double f = repulsion(c, dist);

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += verticalization * yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= verticalization * yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcBiRepulsorNoCollide(Node n1, Node n2, double c) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;
    // distance (from the border of each node)
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist) - n1.size - n2.size;

    if (dist > 0) {
      double f = repulsion(c, dist);

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    } else if (dist != 0) {
      //flat repulsion
      double f = -c;

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcUniRepulsor(Node n1, Node n2, double c) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;

    // distance tout court
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

    if (dist > 0) {
      double f = repulsion(c, dist);

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcBiAttractor(Node n1, Node n2, double c){
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;
    // distance tout court
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

    if (dist > 0) {
      double f = attraction(c, dist);

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcBiAttractorNoCollide(Node n1, Node n2, double c) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;
    // distance (from the border of each node)
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist) - n1.size - n2.size;

    if (dist > 0) {
      double f = attraction(c, dist);

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcBiFlatAttractor(Node n1, Node n2, double c) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;
    // distance tout court
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

    if (dist > 0) {
      double f = -c;

      n1.temp.dx += xDist / dist * f;
      n1.temp.dy += yDist / dist * f;

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param n1 .
   * @param n2 .
   * @param c .
   */
  protected void fcUniAttractor(Node n1, Node n2, float c) {
    // distance en x entre les deux noeuds
    double xDist = n1.x - n2.x;
    double yDist = n1.y - n2.y;
    // distance tout court
    double dist = (float) Math.sqrt(xDist * xDist + yDist * yDist);

    if (dist > 0) {
      double f = attraction(c, dist);

      n2.temp.dx -= xDist / dist * f;
      n2.temp.dy -= yDist / dist * f;
    }
  }

  /**
   *
   * @param c .
   * @param dist .
   * @return .
   */
  protected double attraction(double c, double dist) {
    return 0.01 * -c * dist;
  }

  /**
   *
   * @param c .
   * @param dist .
   * @return .
   */
  protected double repulsion(double c, double dist) {
    return 0.001 * c / dist;
  }
}
