package org.sweetmap.services.blog;

import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.sweetmap.entities.Category;

/**
 * Return the category list for the legend.
 * @author leakim
 *
 */
@Name("legendCategoryList")
@Scope(ScopeType.STATELESS)
public class LegendCategoryList {

  /**
   * the logger.
   */
  @Logger
  private Log logger;
  /**
   * The entity manager.
   *
   */
  @In
  private EntityManager entityManager;
  /**
   * Make the statut list.
   * @return list of status.
   */
  @Unwrap
  @SuppressWarnings("unchecked")
  public List<Category> getTextEntryStatutList() {
    List<Category> list = null;
    boolean languageLimit = !Identity.instance().hasRole("Administrator");
    String ejbQl = "select category from Category category";
    if (languageLimit) {
      logger.debug("SET CATEGORY LANGUAGE TO : #0", LocaleSelector.instance().getLanguage());
      ejbQl += " WHERE category.language = '" + LocaleSelector.instance().getLanguage() + "'";
    }
    try {
      list = (List<Category>) entityManager.createQuery(ejbQl).getResultList();
    } catch (Exception e) {
      logger.error("Error while getting categories: #0", e.getCause().getMessage());
    }
    return list;
  }
}
