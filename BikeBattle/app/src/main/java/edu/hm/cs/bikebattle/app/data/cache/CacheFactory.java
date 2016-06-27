package edu.hm.cs.bikebattle.app.data.cache;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.JacksonSpeaker;
import org.springframework.hateoas.hal.Jackson2HalModule;

import java.io.File;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.data.cache
 * @author Rene Zarwel
 * Date: 06.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class CacheFactory {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    //Configure ObjectMapper to operate with Spring Data
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new Jackson2HalModule());
  }


  private static <S> S createCache(Class<S> serviceClass, File cacheDir){
    return new RxCache.Builder()
        .useExpiredDataIfLoaderNotAvailable(true)
        .persistence(cacheDir, new JacksonSpeaker())
        .using(serviceClass);
  }

  /**
   * Creates a new UserCache.
   * @param cacheDir cache file to use
   * @return new UserCache
   */
  public static UserCache getUserCache(File cacheDir){
    return createCache(UserCache.class, cacheDir);
  }

  /**
   * Creates a new RouteCache.
   * @param cacheDir cache file to use
   * @return new RouteCache
   */
  public static RouteCache getRouteCache(File cacheDir){
    return createCache(RouteCache.class, cacheDir);
  }

  /**
   * Creates a new DriveCache.
   * @param cacheDir cache file to use
   * @return new DriveCache
   */
  public static DriveCache getDriveCache(File cacheDir){
    return createCache(DriveCache.class, cacheDir);
  }
}
