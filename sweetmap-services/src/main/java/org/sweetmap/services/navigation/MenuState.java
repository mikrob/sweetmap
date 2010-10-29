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
package org.sweetmap.services.navigation;

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author leakim
 *
 */
@AutoCreate
@Scope(ScopeType.SESSION)
@Name("menuState")
public class MenuState {

  /**
   * The menu content.
   */
  private Map <String, Boolean> menu;

  /**
   * The selected Menu Item.
   */
  private String selectedMenuItem;


  /**
   * Constructor.
   */
  public MenuState() {
  }




  /**
   * Initialize the menu.
   */
  @Create //@Begin(nested = true)
  public void init() {
    menu = new HashMap<String, Boolean>();
    menu.put("blog", false);
    menu.put("home", false);
    menu.put("search", false);

    menu.put("admin", false);
    menu.put("articles", false);
    menu.put("articleList", false);
    menu.put("addArticle", false);
    menu.put("categories", false);
    menu.put("addCategory", false);
    menu.put("categoryList", false);

    menu.put("tags", false);
    menu.put("addTag", false);
    menu.put("tagList", false);

    menu.put("users", false);
    menu.put("userList", false);
    menu.put("addUser", false);
    menu.put("roleList", false);
    menu.put("addRole", false);
  }


  /**
   * @return the menu
   */
  public Map<String, Boolean> getMenu() {
    return menu;
  }

  /**
   * @param menu the menu to set
   */
  public void setMenu(Map<String, Boolean> menu) {
    this.menu = menu;
  }


  /**
   * @return the selectedMenuItem
   */
  //  @End
  public String getSelectedMenuItem() {
    return selectedMenuItem;
  }

  /**
   * @param selectedMenuItem the selectedMenuItem to set
   */
  public void setSelectedMenuItem(String selectedMenuItem) {
    this.selectedMenuItem = selectedMenuItem;
  }


}
