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
package org.sweetmap.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.security.management.RoleName;

/**
 * Cms Role Entity.
 * @author leakim
 *
 */
@Name("cmsRole")
@Entity
@Table(name = "CmsRole", uniqueConstraints = @UniqueConstraint(columnNames = "rolename"))
public class CmsRole implements Serializable {

  /**
   * serial uid.
   */
  private static final long serialVersionUID = 1L;

  /** id. */
  @Id @GeneratedValue
  private Long roleId;

  /** the role rolename;. */
  @RoleName
  private String roleName;


  /**
   * @return the id
   */
  public Long getRoleId() {
    return roleId;
  }

  /**
   * @param id the id to set
   */
  public void setRoleId(Long id) {
    this.roleId = id;
  }

  /**
   * @return the name
   */
  public String getRoleName() {
    return roleName;
  }

  /**
   * @param name the name to set
   */
  public void setRoleName(String name) {
    this.roleName = name;
  }

}
