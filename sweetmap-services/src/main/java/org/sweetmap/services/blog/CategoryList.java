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

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.international.LocaleSelector;
import org.jboss.seam.security.Identity;
import org.sweetmap.entities.Category;


/**
 * Bean intended to manage list of category.
 * @author leakim
 *
 */
@Name("categoryList")
public class CategoryList extends EntityQuery<Category> {

  /**
   *
   */
  private static final long serialVersionUID = -6312230641516637054L;


  /**
   * Constructor.
   */
  public CategoryList() {
    boolean languageLimit = !Identity.instance().hasRole("Administrator");
    String ejbQl = "select category from Category category";
    if (languageLimit) {
      System.out.println("SET CATEGORY LANGUAGE TO : " + LocaleSelector.instance().getLanguage());
      ejbQl += " WHERE category.language = '" + LocaleSelector.instance().getLanguage() + "'";
    }
    setEjbql(ejbQl);

  }
}
