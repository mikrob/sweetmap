/*
 * $Id: UserHome.java $
 *
 * Copyright Travelsoft, 2000-2008.
 */
package org.sweetmap.services.user;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Expressions.ValueExpression;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.CmsRole;
import org.sweetmap.entities.CmsUser;

/**
 * UserHome.
 * @author seamgen
 *
 */
@Name("userHome")
public class UserHome extends EntityHome<CmsUser> {

  /**
   *uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The logger.
   */
  @Logger
  private Log logger;

  /** userId. */
  @RequestParameter
  private Long userId;

  /**
   * the roleList.
   */
  private List<CmsRole> roleList = new ArrayList<CmsRole>(0);

  /**
   * Persist method.
   * @return success if can persist.
   */

  @Override
  public String persist() {
    this.instance.setPasswordHash(HashManager.instance().hash(this.instance.getPasswordHash()));
    /*for (CmsRole role : roleList) {
      if (role != null && role.getRoleName() != null) {
        this.getInstance().getUserRoles().add(role);
      }
    }
    this.getEntityManager().flush();*/
    return super.persist();

  }

  /**
   * Update method.
   * @return success on update success.
   */
  @Override
  public String update()  {
    this.instance.setPasswordHash(HashManager.instance().hash(this.instance.getPasswordHash()));
    /*for (CmsRole role : roleList) {
      if (role != null && role.getRoleName() != null) {
        this.getInstance().getUserRoles().add(role);
      }
    }*/
    return super.update();
  }


  /** return the id.
   * @return userId the userId.
   * */
  @Override
  public Object getId() {
    if (userId == null) {
      return super.getId();
    } else {
      return userId;
    }
  }

  /**
   * create tthis.
   */
  @Override @Begin(join = true)
  public void create() {
    super.create();
    for (CmsRole role : this.getInstance().getUserRoles()) {
      this.roleList.add(role);
    }
  }

  /**
   * override message creation.
   * @return the message
   */
  @SuppressWarnings("unchecked")
  public ValueExpression<String> getCreatedMessage() {
    return createValueExpression("L'utilisateur a été crée.");
  }

  /**
   * override message creation.
   * @return the message
   */
  @SuppressWarnings("unchecked")
  public ValueExpression<String> getUpdatedMessage() {
    return createValueExpression("L'utilisateur a été mis à jour.");
  }

  /**
   *  override message creation.
   * @return the message
   */
  @SuppressWarnings("unchecked")
  public ValueExpression getDeletedMessage() {
    return createValueExpression("L'utilisateur a été supprimé.");
  }


  /**
   * @return the roleList
   */
  public List<CmsRole> getRoleList() {
    return roleList;
  }


  /**
   * @param roleList the roleList to set
   */
  public void setRoleList(List<CmsRole> roleList) {
    this.roleList = roleList;
  }



}
