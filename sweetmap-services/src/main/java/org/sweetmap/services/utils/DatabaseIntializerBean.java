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
package org.sweetmap.services.utils;

import java.awt.Color;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.sweetmap.entities.Category;
import org.sweetmap.entities.CmsRole;
import org.sweetmap.entities.CmsUser;
import org.sweetmap.entities.PropertiesFile;
import org.sweetmap.services.properties.PropertyManager;
import org.sweetmap.services.user.HashManager;


/**
 * Initialize the database.
 * @author mikael.robert
 *
 */

@Name("databaseIntializer")
@Stateless
public class DatabaseIntializerBean implements DatabaseIntializer  {

  /**
   * the loger.
   */
  @Logger
  private Log logger;

  /**
   * The entity manager.
   */
  @In
  private EntityManager entityManager;


  /**
   * Initialize default user.
   *
   */
  public void initializeUser() {
    CmsUser user;
    CmsUser user2;
    try {
      Query query1 = entityManager.createQuery(
          "select cmsUser FROM CmsUser cmsUser where userName = :username");
      query1.setParameter("username", "admin");
      query1.getSingleResult();
    } catch (NoResultException ex) {

      logger.debug("Add default user admin");
      user = new CmsUser();
      CmsRole cmsRole = new CmsRole();
      cmsRole.setRoleName("Administrator");
      CmsRole cmsRole2 = new CmsRole();
      cmsRole2.setRoleName("User");
      CmsRole author = new CmsRole();
      author.setRoleName("Author");
      entityManager.persist(author);
      entityManager.persist(cmsRole);
      entityManager.persist(cmsRole2);

      user.setUserName("admin");
      user.setFirstName("admin");
      user.setLastName("Administrateur");
      user.setEmail("admin@dreamisle.net");
      user.setPasswordHash(HashManager.instance().hash("admin"));
      user.getUserRoles().add(cmsRole);
      user.getUserRoles().add(cmsRole2);
      user.getUserRoles().add(author);

      user2 = new CmsUser();
      user2.setUserName("testauthor");
      user2.setPasswordHash(HashManager.instance().hash("testauthor"));
      user2.getUserRoles().add(author);
      user2.setFirstName("Test");
      user2.setLastName("Author");
      user2.setEmail("authortest@cartedutendre.com");

      entityManager.flush();
      entityManager.persist(user);
      entityManager.persist(user2);

      logger.debug("Add the default admin and author");
    }

  }

  /**
   * Initialization method.
   */
  public void initializeCategory() {
    Category category;
    try {
      Query query2 = entityManager.createQuery(
          "select category FROM Category category where name = :name");
      query2.setParameter("name", "Standard");
      query2.getSingleResult();
    } catch (NoResultException ex) {
      logger.debug("Add default category Standard");
      category = new Category();
      category.setName("Standard");
      //english
      Category c1 = new Category();
      c1.setLanguage("en");
      c1.setName("Poetry");
      c1.setColor(new Color(217, 43, 233));
      Category c2 = new Category();
      c2.setLanguage("en");
      c2.setName("Encounter");
      c2.setColor(Color.GREEN);
      Category c3 = new Category();
      c3.setLanguage("en");
      c3.setName("Relationship advice");
      c3.setColor(Color.BLUE);
      Category c4 = new Category();
      c4.setLanguage("en");
      c4.setName("Divorce");
      c4.setColor(Color.RED);

      //french
      Category c1f = new Category();
      c1f.setLanguage("fr");
      c1f.setName("Poesie");
      c1f.setColor(new Color(217, 43, 233));
      Category c2f = new Category();
      c2f.setLanguage("fr");
      c2f.setName("Rencontre");
      c2f.setColor(Color.GREEN);
      Category c3f = new Category();
      c3f.setLanguage("fr");
      c3f.setName("Conseil Amoureux");
      c3f.setColor(Color.BLUE);
      Category c4f = new Category();
      c4f.setLanguage("fr");
      c4f.setName("Divorce");
      c4f.setColor(Color.RED);


      entityManager.persist(c1);
      entityManager.persist(c2);
      entityManager.persist(c3);
      entityManager.persist(c4);
      entityManager.persist(c1f);
      entityManager.persist(c2f);
      entityManager.persist(c3f);
      entityManager.persist(c4f);
      entityManager.persist(category);
      entityManager.flush();
      logger.debug("Add the default category");
    }
  }

  /**
   * Initialize the properties.
   */
  public void initializeProperties() {
    try {
      PropertiesFile file = entityManager.find(PropertiesFile.class, new Long(1));
      logger.debug("Set File path to : #0", file.getFilePath());
      PropertyManager.getInstance().setConfigPropertiesPath(file.getFilePath());
      PropertyManager.getInstance().getProperties();
    } catch (Exception e) {
      logger.error("Error while loading default properties file path");
    }

  }
}
