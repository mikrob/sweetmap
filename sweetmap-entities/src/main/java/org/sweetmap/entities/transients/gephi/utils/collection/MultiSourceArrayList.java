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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

//TODO : commenter la classe MultiSourceArrayList

/**
 * ArrayList multisource en lecture seule. C'est une liste de liste.
 * <p>
 * Inspiré du Design Pattern Composite, cette classe est à la fois un conteneur de {@link List}
 * et une {@link List} elle même. L'utilisateur utilise donc exactement de la
 * même manière un {@link ArrayList} standard comme une {@link MultiSourceArrayList}, excepté que cette
 * dernière est en lecture seule.
 * <p>
 * Après l'instanciation, il faut ajouter les {@link List} sources avec la méthode <code>addSource</code>.
 * Supporte les iterateurs grâce à la classe {@link MultiSourceIterator}. Il est possible aussi de supprimer
 * des sources.
 * <p>
 * Les operations non supportés provoquent des {@link UnsupportedOperationException}.
 *
 * @author Mathieu Bastian
 * @param <E> Le contenu des listes. Exemple : NodeInSpace.
 * @see MultiSourceIterator
 */
public class MultiSourceArrayList<E> implements List<E> {
  /**
   *
   */
  private List<List<E>> sources;

  /**
   *
   */
  private MultiSourceIterator<E> iterator;

  /**
   *
   */
  public MultiSourceArrayList() {
    sources = new ArrayList<List<E>>();
  }

  /**
   *
   * @param l .
   */
  public void addSource(List<E> l) {
    sources.add(l);
  }

  /**
   *
   * @param l .
   */
  public void removeSource(List<E> l) {
    sources.remove(l);
  }

  /**
   *
   * @param index .
   * @return .
   */
  public List<E> getSource(int index) {
    return sources.get(index);
  }

  /**
   *
   */
  public void clearSources() {
    sources.clear();
  }

  /**
   *
   * @return .
   */
  public int countSources() {
    return sources.size();
  }

  /**
   * @param e .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public boolean add(E e) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param index .
   * @param element .
   * @throws UnsupportedOperationException .
   */
  public void add(int index, E element) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();

  }

  /**
   * @param c .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public boolean addAll(Collection<? extends E>  c) throws UnsupportedOperationException  {
    throw new UnsupportedOperationException();
  }

  /**
   * @param index .
   * @param c .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public boolean addAll(int index, Collection<? extends E> c) throws UnsupportedOperationException  {
    throw new UnsupportedOperationException();
  }

  /**
   *
   */
  public void clear() {
    for (List<E> l : sources) {
      l.clear();
    }
  }

  /**
   * @param o .
   * @return .
   */
  public boolean contains(Object o) {
    for (List<E> l : sources) {
      if (l.contains(o)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param c .
   * @return .
   */
  public boolean containsAll(Collection< ? > c) {
    for (Iterator< ? > itr = c.iterator(); itr.hasNext();) {
      Object e = itr.next();
      if (!contains(e)) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param index .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public E get(int index) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param o .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public int indexOf(Object o) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @return .
   */
  public boolean isEmpty() {
    for (List<E> l : sources) {
      if (!l.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return .
   */
  public Iterator<E> iterator() {
    if (sources.size() == 0) {
      return new ArrayList<E>().iterator();
    }

    iterator = new MultiSourceIterator<E>();
    for (List<E> l : sources) {
      iterator.addSource(l.iterator());
    }
    return iterator;
  }

  /**
   *
   */
  public void removeLastIterate() {
    iterator.remove();
  }

  /**
   * @param o .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public int lastIndexOf(Object o) throws UnsupportedOperationException  {
    throw new UnsupportedOperationException();
  }

  /**
   * @throws UnsupportedOperationException .
   * @return .
   */
  public ListIterator<E> listIterator() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param index .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public ListIterator<E> listIterator(int index) throws UnsupportedOperationException  {
    throw new UnsupportedOperationException();
  }

  /**
   * @param o .
   * @return .
   */
  public boolean remove(Object o) {
    for (List<E> l : sources) {
      if (l.remove(o)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param index .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public E remove(int index) throws UnsupportedOperationException  {
    throw new UnsupportedOperationException();
  }

  /**
   * @param c .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param c .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param index .
   * @param element .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public E set(int index, Object element) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @return .
   */
  public int size() {
    int size = 0;
    for (List<E> l : sources) {
      size += l.size();
    }
    return size;
  }

  /**
   * @param fromIndex .
   * @param toIndex .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public List<E> subList(int fromIndex, int toIndex) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @throws UnsupportedOperationException .
   * @return .
   */
  public E[] toArray() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  /**
   * @param a .
   * @throws UnsupportedOperationException .
   * @return .
   */
  public E[] toArray(Object[] a) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

}
