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
package org.sweetmap.services.blog;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.Tag;

/**
 * @author leakim
 *
 */
@Name("textEntryManager")
@Scope(ScopeType.CONVERSATION)
public class TextEntryManager {

  /**
   * The logg er.
   */
  @Logger
  private Log logger;

  /**
   * Hibernate entityManager.
   */
  @In(create = true, required = true)
  private FullTextEntityManager entityManager;

  /**
   * initialization method.
   */
  @Create
  public void create() {
    logger.debug("Create autocomplete tag service");
  }


  /**
   * Auto complete tag list.
   * @param event the event.
   * @return tagList the list of tag matching.
   */
  @SuppressWarnings("unchecked")
  public List<Tag> autoComplete(Object event) {
    List<Tag> tagList = new ArrayList<Tag>(0);
    String entry = (String) event;
    if (entry != null && !"".equals(entry)) {
      logger.debug("Calling auto complete method for #0", entry);
      QueryParser parser =  new QueryParser("name", new StandardAnalyzer());
      parser.setAllowLeadingWildcard(true);
      org.apache.lucene.search.Query luceneQuery = null;
      try {
        luceneQuery = parser.parse("*" + entry + "*");
        logger.debug(luceneQuery.toString());
      } catch (ParseException e) {
        tagList =   null;
        logger.debug("Parse Exception for Lucene query");
      }
      tagList = (List<Tag>)  entityManager.createFullTextQuery(luceneQuery, Tag.class).getResultList();
      if (tagList != null) {
        logger.debug("Tag List Size Foudn #0", tagList.size());
      }
    }
    return tagList;
  }
}
