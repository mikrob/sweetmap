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
package org.sweetmap.enums;

/**
 * @author leakim
 *
 */
public enum TextEntryStatus {
  /**
   * Text entry is published.
   */
  PUBLISHED ("publié"),
  /**
   *Text entry is unpublished.
   */
  UNPUBLISHED ("non publié");


  /**
   * fullname.
   */
  private String fullname;


  /**
   * Constructor.
   * @param fullnam1 the real name of the status.
   */
  private TextEntryStatus(String fullnam1) {
    fullname = fullnam1;
  }

  /**
   * return the full name.
   * @return fullname the full name.
   */
  public String getFullName() {
    return fullname;
  }
}
