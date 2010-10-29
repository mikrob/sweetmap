/**
 * sweetmapopyright (C) 2009 Mikael Robert
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
package org.sweetmap.services.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.TextEntry;

/**
 * @author leakim
 *
 */
@Name("searchService")
@Scope(ScopeType.PAGE)
public class SearchService {

  /**
   * logger.
   */
  @Logger
  private Log logger;

  /**
   * Hibernate entityManager.
   */
  @In
  private FullTextEntityManager entityManager;

  /**
   * The search pattern.
   */
  private String searchPattern;

  /**
   * The searchs results.
   */
  private List<TextEntry> searchResults;


  /**
   * Construct the search results.
   *
   */
  @SuppressWarnings("unchecked")
  public void  search() {
    if (searchResults != null) {
      logger.debug("Old result list size : #0", searchResults.size());
    }
    if (searchPattern == null || "".equals(searchPattern)) {
      searchPattern = null;
      searchResults =  null;
      //entityManager.createQuery("select be from TextEntry be order by date desc").getResultList();
    } else {
      Map<String, Float> boostPerField = new HashMap<String, Float>();
      boostPerField.put("tagList.name", 4f);
      boostPerField.put("title", 3f);
      boostPerField.put("category.name", 2f);
      boostPerField.put("content", 1f);
      String[] productFields =  {"title", "content", "tagList.name", "category.name"};

      QueryParser parser = new MultiFieldQueryParser(productFields, new StandardAnalyzer(), boostPerField);
      parser.setAllowLeadingWildcard(true);
      org.apache.lucene.search.Query luceneQuery = null;
      try {
        luceneQuery = parser.parse("*" + searchPattern + "*");
      } catch (ParseException e) {
        searchResults =   null;
      }

      searchResults =   entityManager.createFullTextQuery(luceneQuery, TextEntry.class)
        .setMaxResults(100).getResultList();
      logger.debug("Search count #0", searchResults.size());
    }
  }

  /**
   * Getter for search pattern.
   * @return searchPattern the search pattern.
   */
  public String getSearchPattern() {
    return searchPattern;
  }

  /**
   * Setter for search pattern.
   * @param searchPattern the search pattern.
   */
  public void setSearchPattern(String searchPattern) {
    this.searchPattern = searchPattern;
  }

  /**
   * @return the searchResults
   */
  public List<TextEntry> getSearchResults() {
    return searchResults;
  }

  /**
   * @param searchResults the searchResults to set
   */
  public void setSearchResults(List<TextEntry> searchResults) {
    this.searchResults = searchResults;
  }


}
