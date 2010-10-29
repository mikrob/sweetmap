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
package org.sweetmap.services.user;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.sweetmap.entities.CmsRole;
import org.sweetmap.entities.CmsUser;

/**
 *
 * @author leakim
 *
 */
@Name("authenticator")
public class Authenticator {
  /**
   * The logger.
   */
  @Logger
  private Log log;

  /** entityManager. */
  @In
  private EntityManager entityManager;

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
   *  authentification method.
   * @return true if the user is registered.
   */
  public boolean authenticate() {
    try {
      log.info("authenticating {0}", credentials.getUsername());
      Query query = entityManager.createQuery("from CmsUser where username = :username "
          + "and passwordHash = :password");
      query.setParameter("username", credentials.getUsername());
      query.setParameter("password", HashManager.instance().hash(credentials.getPassword()));

      CmsUser user = (CmsUser) query.getSingleResult();

      if (user.getUserRoles() != null) {
        for (CmsRole mr : user.getUserRoles()) {
          identity.addRole(mr.getRoleName());
        }
      }
      return true;
    } catch (NoResultException ex) {
      return false;
    }
  }

}
