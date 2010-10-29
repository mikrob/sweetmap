/**
 *
 sweetmap copyright (C) 2009 Mikael Robert
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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.security.management.UserFirstName;
import org.jboss.seam.annotations.security.management.UserLastName;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;


/**
 *
 * @author leakim
 *
 */
@Name("cmUser")
@Entity
@Table(name = "CmsUser", uniqueConstraints = {
    @UniqueConstraint(columnNames = "userName"),
    @UniqueConstraint(columnNames = "email") })
public class CmsUser implements Serializable {

  /**
   * Serial Uid.
   */
  private static final long serialVersionUID = 1L;


  /** id. */
  @Id @GeneratedValue
  private Long id;

  /** name. */
  @NotNull
  @UserPrincipal
  private String userName;

  /** password. */
  @NotNull
  @UserPassword(hash = "md5")
  @Length(min = 4)
  private String passwordHash;

  /** email. */
  @NotNull
  @Email
  private String email;


  /** the user firstname. */
  @NotNull @Length(max = 40)
  @UserFirstName
  private String firstName;

  /** the user lastname. */
  @NotNull @Length(max = 40)
  @UserLastName
  private String lastName;


  /** the user roles. */
  @UserRoles
  @ManyToMany(targetEntity = CmsRole.class)
  @JoinTable(name = "UserRoles",
      joinColumns = @JoinColumn(name = "UserId"),
      inverseJoinColumns = @JoinColumn(name = "RoleId"))
  private List<CmsRole> userRoles = new ArrayList<CmsRole>();
  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


  /** get the full name.
   * @return fullname the firstName + lastName
   *  */
  @Transient
  public String getName() {
    return firstName + " " + lastName;
  }

  /**
   * @return the userRoles
   */
  public List<CmsRole> getUserRoles() {
    return userRoles;
  }

  /**
   * @param userRoles the userRoles to set
   */
  public void setUserRoles(List<CmsRole> userRoles) {
    this.userRoles = userRoles;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id
   *            the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the username
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @param name
   *            the name to set
   */
  public void setUserName(String name) {
    this.userName = name;
  }

  /**
   * @return the passwordHash
   */
  public String getPasswordHash() {
    return passwordHash;
  }

  /**
   * @param passwordHash
   *            the passwordHash to set
   */
  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *            the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }


}
