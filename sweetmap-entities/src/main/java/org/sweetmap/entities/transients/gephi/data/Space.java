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

package org.sweetmap.entities.transients.gephi.data;

import java.util.ArrayList;
import java.util.List;


//TODO : commenter la classe Space

/**
 * Spaces are the container for a lot of data. They represent first partitions of the graph, but they don't
 * deal directly with other spaces, so they are only graph data containers. Spaces also contain data relative
 * to the Space in the system, like some preferences about the visualization. These data may be present or
 * not depending of the implementation.
 *
 * @author Mathieu Bastian
 */
public class Space implements Cloneable {

  /**
   *
   */
  private static int autoId = 0;

  /**
   *
   */
  private List<Node> nodes;

  /**
   *
   */
  private List<Edge> edges;

  /**
   *
   */
  private String name;

  /**
   *
   */
  private int id;

  /**
   *
   */
  public Space() {
    nodes = new ArrayList<Node>();
    edges = new ArrayList<Edge>();
    id = autoId++;
  }

  /**
   *
   */
  public void clearChilds() {
    nodes = null;
    edges = null;
  }

  /**
   *
   * @return .
   */
  public float[] getGraphLimits() {
    float[] graphLimits = new float[4];

    graphLimits[0] = Float.MAX_VALUE;
    graphLimits[1] = Float.NEGATIVE_INFINITY;
    graphLimits[2] = Float.MAX_VALUE;
    graphLimits[3] = Float.NEGATIVE_INFINITY;

    for (Node n : getNodes()) {
      if (!Float.isNaN(n.x) && !Float.isNaN(n.y)) {
        graphLimits[0] = Math.min(graphLimits[0], n.x);
        graphLimits[1] = Math.max(graphLimits[1], n.x);
        graphLimits[2] = Math.min(graphLimits[2], n.y);
        graphLimits[3] = Math.max(graphLimits[3], n.y);
      }
    }

    return graphLimits;
  }

  /**
   *
   * @return .
   */
  public int getNodesCount() {
    return nodes.size();
  }

  /**
   *
   * @return .
   */
  public List<Node> getNodes() {
    return nodes;
  }

  /**
   *
   * @return .
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param name .
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   *
   * @return .
   */
  public int getId() {
    return id;
  }

  /**
   *
   * @param id .
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   *
   * @return .
   */
  public List<Edge> getEdges() {
    return edges;
  }

  /**
   * @return .
   */
  public String toString() {
    return name;
  }

  /**
   *
   * @return .
   */
  public static int getAUTOID() {
    return autoId;
  }

  /**
   *
   * @param autoid .
   */
  public static void setAUTOID(int autoid) {
    autoId = autoid;
  }

}
