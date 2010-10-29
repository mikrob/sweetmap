/**
 * sweetmappyright (C) 2009 Mikael Robert
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sweetmap.services.utils;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

/**
 * @author mikael.robert
 * This list load data with lazy method to avoid problem of memory for big data list.
 * @param <T> the object type.
 */
public class LazyList<T> extends AbstractList<T> {

  /**
   * The query.
   *
   * */
  private Query query;

  /**
   * cache for loaded datas.
   * */
  private Map<Integer, T> loaded;

  /**
   *  total number of results.
   *  */
  private long numResults;

  /**
   * currentPage.
   */
  private int currentPage;


  /**
   * the page size.
   * */
  private int pageSize;

  /**
   * constructor.
   * */
  public LazyList() {
    loaded = new HashMap<Integer, T>();
  }

  /**
   * Create a LazyList backed by the given query, using pageSize results
   * per page, and expecting numResults from the query.
   * @param query the query to get the datas.
   * @param pageSize the page size wanted.
   * @param numResults the number of results of the dataset.
   * @param currentPage the currentPage.
   */
  public LazyList(Query query, int pageSize, long numResults, int currentPage) {
    this();
    this.query = query;
    this.pageSize = pageSize;
    this.numResults = numResults;
    this.currentPage = currentPage;
  }

  /**
   * Fetch an item, loading it from the query results if it hasn't already
   * been.
   * @param i the first result.
   * @return object the object.
   */
  @SuppressWarnings("unchecked")
  public T get(int i) {
    //clearCache();

    if (!loaded.containsKey(i)) {
      List<T> results = (List<T>) query.setFirstResult(i).setMaxResults(pageSize).getResultList();
      for (int j = 0; j < results.size(); j++) {
        loaded.put(i + j, results.get(j));
      }
    }
    return loaded.get(i);
  }

  /**
   * Clear the cache.
   */
  @SuppressWarnings("unused")
  private void clearCache() {
    if (loaded.size() > 50) {
      int firstElementToKeep = currentPage * pageSize - pageSize;
      int lastElementToKeep = currentPage  * pageSize - 1;
      int cpt = 0;
      for (int idx = 0; idx < loaded.size(); idx++) {
        if (idx < firstElementToKeep || idx > lastElementToKeep) {
          loaded.remove(idx);
          cpt++;
        }
      }
    }
  }

  /**
   * Return the total number of items in the list. This is done by
   * using an equivalent COUNT query for the backed query.
   * @return size the size.
   */
  public int size() {
    return (int) numResults;
  }

  /**
   * update the number of results expected in this list.
   * @param numResults the number of results total.
   * */
  public void setNumResults(long numResults) {
    this.numResults = numResults;
  }

  /**
   * get the loaded cache size.
   * @return size the size.
   */
  public int getLoadedSize() {
    return loaded.size();
  }

  /**
   * remove an element.
   * @param elt the element to remove.
   */
  public void remove(T elt) {
    Iterator<Map.Entry<Integer, T>> it = loaded.entrySet().iterator();
    boolean found = false;
    while (it.hasNext() && !found) {
      if (it.next().getValue().equals(elt)) {
        it.remove();
        found = true;
      }
    }
  }

}
