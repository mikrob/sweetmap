/**
 * sweetmapopyright (C) 2009 Mikael Robert
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

import java.security.MessageDigest;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.util.Hex;

/**
 * @author leakim
 *
 */
@Name("hashManager")
public class HashManager {
  /**
   * The hash function.
   */
  private String hashFunction = "MD5";

  /**
   * The charset for hash.
   */
  private String charset = "UTF-8";

  /**
   * hash method.
   * @param password the password to hash.
   * @return hashedPassword
   */
  public String hash(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance(hashFunction);
      md.update(password.getBytes(charset));
      byte[] raw = md.digest();
      return new String(Hex.encodeHex(raw));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * return the charset.
   * @return charset the charset.
   */
  public String getCharset() {
    return charset;
  }

  /**
   * set the charset.
   * @param charset the charset to set.
   */
  public void setCharset(String charset) {
    this.charset = charset;
  }

  /**
   * Return the hashFunction.
   * @return hashFunction the hash function.
   */
  public String getHashFunction() {
    return hashFunction;
  }

  /**
   * Set the hash function.
   * @param hashFunction the hashfunction to set.
   */
  public void setHashFunction(String hashFunction) {
    this.hashFunction = hashFunction;
  }

  /**
   * return the instance of the hash manager.
   * @return the instance.
   */
  public static HashManager instance() {
    return (HashManager) Component.getInstance(HashManager.class);
  }

}
