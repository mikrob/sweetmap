package org.sweetmap.entities.transients.gephi.data;

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


import java.util.LinkedList;
import java.util.List;

import org.sweetmap.entities.transients.gephi.utils.collection.MultiSourceArrayList;

//TODO : commenter la classe Node

/**
 * Node of a graph. It knows the {@link Edge}.
 *
 * @author Mathieu Bastian
 */
public class Node implements Cloneable {
  /**
   *
   */
  private static int autoId = 0;

  /**
   *
   */
  private int id;

  /**
   *
   */
  public float x;

  /**
   *
   */
  public float y;

  /**
   *
   */
  public float z;

  /**
   *
   */
  private String label = "";

  /**
   *
   */
  public float r = 0f;

  /**
   *
   */
  public float g = 0f;

  /**
   *
   */
  public float b = 0f;

  /**
   *
   */
  public float size = 1f;

  /**
   * Space edges.
   */
  private MultiSourceArrayList<Edge> edges;

  /**
   *
   */
  private List<Edge> edgesIn;

  /**
   *
   */
  private List<Edge> edgesOut;

  /**
   * TempData.
   */
  public NodeTempData temp = new NodeTempData();

  /**
   *
   */
  public String text = "";

  /**
   *
   * @param x .
   * @param y .
   * @param z .
   * @param label .
   */
  public Node(float x, float y, float z, String label) {
    this();
    this.x = x;
    this.y = y;
    this.z = z;
    this.label = label;
  }

  /**
   *
   */
  public Node() {
    edges = new MultiSourceArrayList<Edge>();
    edgesIn = new LinkedList<Edge>();
    edgesOut = new LinkedList<Edge>();
    edges.addSource(edgesIn);
    edges.addSource(edgesOut);

    x = ((float) Math.random()) * 20 - 10.0f;
    y = ((float) Math.random()) * 20 - 10.0f;

    id = autoId++;
  }

  /**
   *
   * @return .
   */
  public float[] getLocation() {
    float[] location = {0.0f, 0.0f, 0.0f};
    location[0] = x;
    location[1] = y;
    location[2] = z;
    return location;
  }

  /**
   *
   * @return .
   */
  public int getNeighboursCount() {
    return edgesIn.size() + edgesOut.size();
  }

  /**
   *
   * @return .
   */
  public List<Node> getNeighbours() {
    //return nodeContext.getNeighbours();
    List<Node> result = new LinkedList<Node>();

    for (Edge tmp : edgesIn) {
      result.add(tmp.getNodeFrom());
    }

    for (Edge tmp : edgesOut) {
      result.add(tmp.getNodeTo());
    }

    return result;
  }

  /**
   *
   * @return .
   */
  public String getLabel() {
    return label;
  }

  /**
   *
   * @param label .
   */
  public void setLabel(String label) {
    this.label = label;
    if (text == null) {
      text = label;
    }
  }

  /**
   *
   * @return .
   */
  public float getSize() {
    return size;
  }

  /**
   *
   * @param size .
   */
  public void setSize(float size) {
    this.size = size;
  }

  /**
   *
   * @param e .
   */
  public void addEdge(Edge e) {
    if (this == e.getNodeFrom()) {
      edgesOut.add(e);
    } else {
      edgesIn.add(e);
    }
  }

  /**
   *
   * @return .
   */
  public List<Edge> getEdges() {
    return edges;
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

  /**
   *
   * @return .
   */
  public List<Edge> getEdgesIn() {
    return edgesIn;
  }

  /**
   *
   * @param edgesIn .
   */
  public void setEdgesIn(List<Edge> edgesIn) {
    this.edgesIn = edgesIn;
  }

  /**
   *
   * @return .
   */
  public List<Edge> getEdgesOut() {
    return edgesOut;
  }

  /**
   *
   * @param edgesOut .
   */
  public void setEdgesOut(List<Edge> edgesOut) {
    this.edgesOut = edgesOut;
  }

  /**
   * @return .
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(this.label);
    sb.append(" : x = ");
    sb.append(this.x);
    sb.append(" y = ");
    sb.append(this.y);

    return sb.toString();
  }
}
