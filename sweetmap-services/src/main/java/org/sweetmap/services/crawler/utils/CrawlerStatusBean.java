package org.sweetmap.services.crawler.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.sweetmap.entities.SiteNode;

/**
 * The Crawler Status Bean manager.
 * @author leakim
 *
 */
@Scope(ScopeType.APPLICATION)
@Name("crawlerStatus")
@AutoCreate
public class CrawlerStatusBean {

  /**
   * Return the processing site node list.
   * @return list of site processed.
   */
  public List<SiteNode> getProcessingSiteNodeList() {
    List<SiteNode> list = new ArrayList<SiteNode>();
    Map<SiteNode, Long> map = CrawlersStatus.getInstance().getMapStatus();
    Set<Entry<SiteNode, Long>> entrySet = map.entrySet();
    Iterator<Entry<SiteNode, Long>> it = entrySet.iterator();
    while (it.hasNext()) {
      Entry<SiteNode, Long> entry = it.next();
      entry.getKey().setProgress(entry.getValue());
      list.add(entry.getKey());
    }
    return list;
  }

  /**
   * Init method.
   */
  public void init() {

  }



}
