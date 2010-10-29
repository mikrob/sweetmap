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

package org.sweetmap.services.search;

import java.util.List;

import javax.ejb.Remove;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.Category;
import org.sweetmap.entities.Tag;
import org.sweetmap.entities.TextEntry;

/**
 * Index Blog entry at startup.
 *
 * @author Mikael Robert
 */
@Name("indexerService")
@Scope(ScopeType.APPLICATION)
//@Startup
public class IndexerService {

  /**
   * The logger.
   */
  @Logger
  private Log logger;

  /**
   * Hibernate Search Entity Manager.
   */
  @In
  private FullTextEntityManager entityManager;

  /**
   * Creation method called on application startup.
   */
  @SuppressWarnings("unchecked")
  @Create
  public void indexTextEntry() {
    List blogEntries = entityManager.createQuery("select be from TextEntry be").getResultList();
    entityManager.purgeAll(TextEntry.class);
    logger.debug("Indexing textEntries ....");
    for (Object be : blogEntries) {
      entityManager.index(be);
    }

    entityManager.purgeAll(Tag.class);
    List tagList = entityManager.createQuery("select tag From Tag tag").getResultList();
    logger.debug("Indexing Tags....");
    for (Object tag : tagList) {
      entityManager.index(tag);
    }

    entityManager.purgeAll(Category.class);
    List categoryList = entityManager.createQuery("select category From Category category").getResultList();
    logger.debug("Indexing Categories....");
    for (Object category : categoryList) {
      entityManager.index(category);
    }

  }

  /**
   * Destroy/Remove method.
   */
  @Remove
  @Destroy
  public void stop() {

  }
}
