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
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;
import org.sweetmap.entities.Category;



/**
 * Bean intended to manage a category entity.
 * @author leakim
 *
 */
@Name("categoryHome")
public class CategoryHome extends EntityHome<Category> {

  /**
   *
   */
  private static final long serialVersionUID = 217092239711174916L;
  /**
   * the category Id.
   */
  @RequestParameter
  private Long categoryId;

  /**
   * return the id.
   * @return the id.
   */
  @Override
  public Object getId() {
    if (categoryId == null) {
      return super.getId();
    } else {
      return categoryId;
    }
  }

  /**
   * Create the EntityHome.
   */
  @Override @Begin(join = true)
  public void create() {
    super.create();
  }

}
