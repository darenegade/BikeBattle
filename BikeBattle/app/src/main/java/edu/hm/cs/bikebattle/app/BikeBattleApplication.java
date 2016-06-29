package edu.hm.cs.bikebattle.app;

import android.support.multidex.MultiDexApplication;
import edu.hm.cs.bikebattle.app.data.cache.CacheFactory;
import edu.hm.cs.bikebattle.app.data.cache.DriveCache;
import edu.hm.cs.bikebattle.app.data.cache.RouteCache;
import edu.hm.cs.bikebattle.app.data.cache.UserCache;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app
 * Author(s): Rene Zarwel
 * Date: 29.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class BikeBattleApplication extends MultiDexApplication {

  private UserCache userCache;
  private DriveCache driveCache;
  private RouteCache routeCache;

  @Override public void onCreate() {
    super.onCreate();
    userCache = CacheFactory.getUserCache(getCacheDir());
    driveCache = CacheFactory.getDriveCache(getCacheDir());
    routeCache = CacheFactory.getRouteCache(getCacheDir());
  }

  public UserCache getUserCache() {
    return userCache;
  }

  public DriveCache getDriveCache() {
    return driveCache;
  }

  public RouteCache getRouteCache() {
    return routeCache;
  }
}
