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

package org.sweetmap.entities.transients.gephi.utils.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//TODO : commenter la classe MultiSourceIterator

/**
 * Iterateur multisource. Permet d'itérer, à la suite, plusieurs iterateurs comme s'il s'agissait d'un seul.
 * <p>
 * Après l'instanciation, ajouter les iterateurs sources grâce à la méthode <code>addSource</code>.
 *
 * @author Mathieu Bastian
 * @param <T> Le contenu des listes. Exemple : NodeInSpace.
 * @see MultiSourceArrayList
 */
public class MultiSourceIterator<T> implements Iterator<T> {

  /**
   * .
   */
  private List<Iterator<T>> sources;

  /**
   *
   */
  private Iterator<T> currentIterator;

  /**
   *
   */
  private Iterator<Iterator<T>> sourcesIterator;

  /**
   * Constructor.
   */
  public MultiSourceIterator() {
    sources = new ArrayList<Iterator<T>>();
  }

  /**
   * @return .
   */
  public boolean hasNext() {

    if (currentIterator.hasNext()) {
      return true;
    } else {
      if (sourcesIterator.hasNext()) {
        currentIterator = sourcesIterator.next();
        return true;
      }
      return false;
    }
  }

  /**
   * @return .
   */
  public T next() {
    return currentIterator.next();
  }

  /**
   *
   */
  public void remove() {
    currentIterator.remove();
  }

  /**
   *
   * @param source .
   */
  public void addSource(Iterator<T> source) {
    if (source.hasNext()) {
      sources.add(source);
      sourcesIterator = sources.iterator();
      currentIterator = sourcesIterator.next();
    } else if (currentIterator == null) {
      sources.add(source);
      sourcesIterator = sources.iterator();
      currentIterator = sourcesIterator.next();
    }

  }

  /**
   *
   * @param source .
   */
  public void removeSource(Iterator<T> source) {
    sources.remove(source);
  }

}
