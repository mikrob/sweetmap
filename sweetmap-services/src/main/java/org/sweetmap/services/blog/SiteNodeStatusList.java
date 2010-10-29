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

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Unwrap;
import org.sweetmap.enums.SiteNodeStatus;

/**
 * @author leakim
 *
 */
@Name("siteNodeStatusList")
public class SiteNodeStatusList {

  /**
   * Make the statut list.
   * @return list of status.
   */
  @Unwrap
  public List<SiteNodeStatus> getSiteNodeStatusList() {
    List<SiteNodeStatus> list = new ArrayList<SiteNodeStatus>();
    for (SiteNodeStatus status : SiteNodeStatus.values()) {
      list.add(status);
    }
    return list;
  }
}
