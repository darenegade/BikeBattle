package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import io.rx_cache.DynamicKey;
import io.rx_cache.EncryptKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.Reply;
import rx.Observable;

import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.data.cache
 * @author Rene Zarwel
 * Date: 06.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@EncryptKey("Secret") //Prevent NullPointer - Encryption is deactivated
public interface RouteCache {

  Observable<Reply<RouteDto>> routesFindeOne(
      Observable<RouteDto> oRoute, DynamicKey id, EvictDynamicKey update);

  Observable<Reply<List<RouteDto>>> RoutesFindAll(
      Observable<List<RouteDto>> oRoutes, EvictProvider evictProvider);

  Observable<Reply<List<RouteDto>>> RoutesFindByName(
      Observable<List<RouteDto>> oRoutes, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<List<RouteDto>>>  RoutesfindByNameContainingIgnoreCase(
      Observable<List<RouteDto>> oRoutes, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<List<RouteDto>>> RoutesFindByOwnerOid(
      Observable<List<RouteDto>> oRoutes, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<List<RouteDto>>> RoutesFindByDifficulty(
      Observable<List<RouteDto>> oRoutes, DynamicKey difficulty, EvictDynamicKey update);

  Observable<Reply<List<RouteDto>>>  RoutesFindByRoutetype(
      Observable<List<RouteDto>> oRoutes, DynamicKey routetyp, EvictDynamicKey update);

  Observable<Reply<List<RouteDto>>> RoutesFindNear(
      Observable<List<RouteDto>> oRoutes, DynamicKey latLongR, EvictDynamicKey update);

  Observable<Reply<UserDto>>  RoutesGetOwner(
      Observable<UserDto> oUser, DynamicKey id, EvictDynamicKey update);
}
