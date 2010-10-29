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

// TODO : commenter la classe Edge

/**
 * Edge of a graph. It contains the {@link Node} source and destination.
 *
 * @author Mathieu Bastian
 */
public class Edge {
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
  private Node nodeFrom;

  /**
   *
   */
  private Node nodeTo;

  /**
   *
   */
  private float cardinal = 1;

  /**
   *
   * @param from .
   * @param to .
   */
  public Edge(Node from, Node to) {
    this.nodeFrom = from;
    this.nodeTo = to;
    id = autoId++;
  }

  /**
   *
   * @return .
   */
  public Node getNodeFrom() {
    return nodeFrom;
  }

  /**
   *
   * @param nodeFrom .
   */
  public void setNodeFrom(Node nodeFrom) {
    this.nodeFrom = nodeFrom;
  }

  /**
   *
   * @return .
   */
  public Node getNodeTo() {
    return nodeTo;
  }

  /**
   *
   * @param nodeTo .
   */
  public void setNodeTo(Node nodeTo) {
    this.nodeTo = nodeTo;
  }

  /**
   *
   * @return .
   */
  public float getCardinal() {
    return cardinal;
  }

  /**
   *
   * @param cardinal .
   */
  public void setCardinal(float cardinal) {
    this.cardinal = cardinal;
  }

  /**
   *
   * @param inc .
   */
  public void incCardinal(float inc) {
    this.cardinal += inc;
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
}
