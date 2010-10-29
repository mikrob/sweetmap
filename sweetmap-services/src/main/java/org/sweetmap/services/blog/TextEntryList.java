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


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.sweetmap.entities.Comment;
import org.sweetmap.entities.TextEntry;
import org.sweetmap.services.utils.LazyList;

/**
 * text Entry list. Seam Query Components.
 * @author leakim
 *
 */
@Name("textEntryList")
@Scope(ScopeType.CONVERSATION)
public class TextEntryList {

  /**
   * The logger.
   */
  @Logger
  private Log logger;

  /**
   * Entity manager.
   */
  @In
  private EntityManager entityManager;
  /**
   * The result list.
   */
  @DataModel
  private List<TextEntry> textEntries;

  /**
   * The current text entry.
   */
  @Out(required = false)
  @DataModelSelection
  private TextEntry textEntry;

  /**
   *  The Identity.
   */
  @In
  private Identity identity;

  /**
   *
   */
  private int currentPage = 1;
  /**
   * Init method.
   */
  @Create
  @Begin
  public void init() {
    logger.debug("Create Text entry list manager.");
  }

  /**
   * Factory for textEntries.
   */
  @Factory("textEntries")
  public void initTextEntries() {
    boolean languageLimit = !identity.hasRole("Administrator");
    String ejbQl = "from TextEntry textEntry ";
    if (languageLimit) {
      logger.info("user is only author we should limit the list to his language ");
      ejbQl += " WHERE textEntry.language = :lang ";
    }

    ejbQl += "order by textEntry.date desc";
    StringBuilder ejbQLStd = new StringBuilder("select textEntry ");
    StringBuilder ejbQLCount = new StringBuilder("select count(textEntry) ");
    ejbQLCount.append(ejbQl);
    ejbQLStd.append(ejbQl);
    try {
      logger.debug("Query = #0", ejbQLStd.toString());
      Query query = entityManager.createQuery(ejbQLStd.toString());
      if (languageLimit) {
        query.setParameter("lang", LocaleSelector.instance().getLanguage());
      }
      Query queryCount = entityManager.createQuery(ejbQLCount.toString());
      if (languageLimit) {
        queryCount.setParameter("lang", LocaleSelector.instance().getLanguage());
      }
      long count = (Long) queryCount.getSingleResult();
      textEntries = new LazyList<TextEntry>(query, 20, count, currentPage);
    } catch (NoResultException ex) {
      logger.error("No results found in text entry");
      FacesMessages.instance().add(Severity.WARN, "No results found in text entry");
    }
  }

  /**
   * add a comment to the current text entry.
   */
  public void addComment() {
    if (textEntry != null) {
      logger.debug("Selected text entry = #0", textEntry.getTitle());
      textEntry.getCommentList().add(0, new Comment());
      entityManager.flush();
    } else {
      logger.debug("Textentry IS NULL");
    }
  }

  /**
   * delete a text entry.
   */
  public void deleteTextEntry() {
    if (textEntry != null) {
      entityManager.remove(textEntry);
      ((LazyList<TextEntry>) textEntries).remove(textEntry);
      initTextEntries();
      logger.debug("Remove text entry");
    }
  }

  /**
   * Destroy method.
   */
  //@End
  @Destroy
  public void destroy() {
    logger.debug("Destroying conversation component and closing conversation for : text entry list");
  }

  /**
   * @return the currentPage
   */
  public int getCurrentPage() {
    return currentPage;
  }

  /**
   * @param currentPage the currentPage to set
   */
  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * @return the textEntry
   */
  public TextEntry getTextEntry() {
    return textEntry;
  }

  /**
   * @param textEntry the textEntry to set
   */
  public void setTextEntry(TextEntry textEntry) {
    this.textEntry = textEntry;
  }

}
