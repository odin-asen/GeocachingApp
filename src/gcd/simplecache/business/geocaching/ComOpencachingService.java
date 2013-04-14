package gcd.simplecache.business.geocaching;

import java.util.List;

/**
 * This class communicates with <a href=http://www.opencaching.com>opencaching.com</a>.
 * It is possible to request information from the geocaching database.
 * <p/>
 * Author: Timm Herrmann<br/>
 * Date: 15.04.13
 */
public class ComOpencachingService extends GeocachingService {

  /* Constructors */
  /* Methods */
  /* Getter and Setter */

  @Override
  public boolean login() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void logout() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Geocache getCacheInfo(String cacheID, CacheParameter parameter) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public List<Geocache> fetchDatabase(CacheParameter parameter) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
