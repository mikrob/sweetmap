/*
 * $Id: UserList.java $
 *
 * Copyright Travelsoft, 2000-2008.
 */

package org.sweetmap.services.user;


import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.sweetmap.entities.CmsUser;

/**
 * UserLIst.
 * @author seamgen
 *
 */
@Name("userList")
public class UserList extends EntityQuery<CmsUser> {

  /**
   * uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * constructor.
   */
  public UserList() {
    setEjbql("select user from CmsUser user");
  }
}
