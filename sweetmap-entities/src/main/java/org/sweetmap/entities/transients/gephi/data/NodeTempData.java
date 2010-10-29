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

//TODO : commenter la classe NodeTempData

/**
 * Temporary data about a {@link Node}. Used in {@link Algorithm}.
 * @author Mathieu Jacomy
 */
public class NodeTempData {

  /**
   *
   */
  Node node;

  /**
   *
   */
  public float dx = 0;

  /**
   *
   */
  public float dy = 0;

  /**
   *
   */
  public double angle = 0;

  /**
   *
   */
  public float dangle = 0;

  /**
   *
   */
  public float oldDx = 0;

  /**
   *
   */
  public float oldDy = 0;

  /**
   *
   */
  public float potential = 0;

  /**
   *
   */
  public float potentialCalc = 0;

  /**
   *
   */
  public float freeze = 0;

  /**
   *
   */
  public float walkPheromone = 0;

  /**
   *
   */
  public ArrayList<Float> colorPheromone = null;

  /**
   *
   */
  public ArrayList<Float> oldColorPheromone = null;

  /**
   *
   */
  public Node closest = null;

  /**
   *
   */
  public double closestDistance = Double.MAX_VALUE;

  /**
   *
   */
  public int layer = 0;

  /**
   *
   */
  public NodeTempData(){
    this.node = node;
  }
}
