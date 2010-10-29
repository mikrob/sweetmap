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

import javax.ejb.Local;

import org.sweetmap.entities.TextEntry;

/**
 * @author leakim
 *
 */
@Local
public interface TextEntryListInterface {

  /**
   * Init method.
   */
  void init();

  /**
   * return the result list.
   *
   */
  void initTextEntries();

  /**
   * add a comment to the current text entry.
   */
  void addComment();

  /**
   * Destroy method.
   */
  void destroy();

  /**
   * @return the currentPage
   */
  int getCurrentPage();

  /**
   * @param currentPage the currentPage to set
   */
  void setCurrentPage(int currentPage);


  /**
   * @return the textEntry
   */
  TextEntry getTextEntry();

  /**
   * @param textEntry the textEntry to set
   */
  void setTextEntry(TextEntry textEntry);

  /**
   * delete a text entry.
   */
  void deleteTextEntry();
}
