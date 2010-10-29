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



import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.sweetmap.entities.TextEntry;

/**
 * Text Entry Home.
 * @author leakim
 *
 */
@Name("textEntryHome")
public class TextEntryHome extends EntityHome<TextEntry> {

  /**
   *
   */
  private static final long serialVersionUID = 3141975971653011468L;

  /**
   * logger.
   */
  @Logger
  private Log logger;


  /**
   * Used to get the request parameter for manage the entity passed.
   */
  @RequestParameter
  private Long textEntryId;


  /**
   * The tags.
   */
  private String tags;

  /**
   *  The Identity.
   */
  @In
  private Identity identity;

  /**
   * The Credentials.
   */
  @In
  private Credentials credentials;

  /**
   * Return id for the object.
   * @return {@link TextEntryHome}
   */
  @Override
  public Object getId() {
    if (textEntryId == null) {
      return super.getId();
    } else {
      return textEntryId;
    }
  }

  /**
   * Create a new conversation.
   */
  @Override @Begin(join = true)
  public void create() {
    super.create();

  }

  /**
   * Persist method.
   * @return success if can persist.
   */
  @Override
  public String persist() {
    this.instance.setAddedBy(credentials.getUsername());
    return super.persist();
  }

  /**
   * update override.
   * @return sucess if succesfuly updated.
   */
  @Override
  public String update() {
    this.instance.setAddedBy(credentials.getUsername());
    return super.update();
  }



}
