package edu.hm.cs.bikebattle.app;

import android.support.multidex.MultiDexApplication;
import edu.hm.cs.bikebattle.app.data.cache.CacheFactory;
import edu.hm.cs.bikebattle.app.data.cache.DriveCache;
import edu.hm.cs.bikebattle.app.data.cache.RouteCache;
import edu.hm.cs.bikebattle.app.data.cache.UserCache;

/**
 * Returns all needed Caches.
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app
 * Author(s): Rene Zarwel
 * Date: 29.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class BikeBattleApplication extends MultiDexApplication {
  /**User Cache.*/
  private UserCache userCache;
  /**Drive Cache.*/
  private DriveCache driveCache;
  /**Route Cache.*/
  private RouteCache routeCache;

  @Override public void onCreate() {
    super.onCreate();
    userCache = CacheFactory.getUserCache(getCacheDir());
    driveCache = CacheFactory.getDriveCache(getCacheDir());
    routeCache = CacheFactory.getRouteCache(getCacheDir());
  }

  /**
   * Returns the User Cache.
   * @return User Cache.
   */
  public UserCache getUserCache() {
    return userCache;
  }

  /**
   * Returns the Drive Cache.
   * @return Drive Cache
   */
  public DriveCache getDriveCache() {
    return driveCache;
  }

  /**
   * Returns the Route Cache.
   * @return Route Cache.
   */
  public RouteCache getRouteCache() {
    return routeCache;
  }
}
