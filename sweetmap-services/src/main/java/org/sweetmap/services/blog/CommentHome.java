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
import org.sweetmap.entities.Comment;


/**
 * Entity home intended to manage a comment.
 * @author leakim
 *
 */
@Name("commentHome")
public class CommentHome extends EntityHome<Comment> {
  /**
   *
   */
  private static final long serialVersionUID = -5772799495282195559L;
  /**
   * the comment id past by param.
   */
  @RequestParameter
  private Long commentId;

  /**
   * id getter.
   * @return id the id.
   */
  @Override
  public Object getId() {
    if (commentId == null) {
      return super.getId();
    } else {
      return commentId;
    }
  }

  /**
   * Creation method.
   */
  @Override @Begin(join = true)
  public void create() {
    super.create();
  }

}
