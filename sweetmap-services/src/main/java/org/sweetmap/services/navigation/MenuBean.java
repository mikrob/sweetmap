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

import javax.faces.event.ActionEvent;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

/**
 * @author leakim
 *
 */

@Name("menuBean")
public class MenuBean {
  /**
   * The menu state.
   */
  @In(create = true, required = true)
  private MenuState menuState;
  /**
   * Constructor.
   */
  public MenuBean() {

  }

  /**
   * Select an item.
   * @param event the event.
   */
  public void select(ActionEvent event) {
    menuState.setSelectedMenuItem(event.getComponent().getId());
  }

  /**
   * @return the menuState
   */
  public MenuState getMenuState() {
    return menuState;
  }

  /**
   * @param menuState the menuState to set
   */
  public void setMenuState(MenuState menuState) {
    this.menuState = menuState;
  }

  /**
   *Pipo.
   */
  public void begin() {


  }

}
