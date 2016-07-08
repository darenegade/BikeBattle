package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.Reply;
import rx.Observable;

import java.util.List;

/**
 * API Response Cache of Route Endpoint.
 *
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.data.cache
 * @author Rene Zarwel
 * Date: 06.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface RouteCache {

  /**
   * Find Route.
   * @param oRoute - Route.
   * @param id - Routen Id.
   * @param update - EvictDynamicKey.
   * @return Route.
   */
  Observable<Reply<RouteDto>> routesFindeOne(
      Observable<RouteDto> oRoute, DynamicKey id, EvictDynamicKey update);

  /**
   * Find all routes.
   * @param oRoutes - Route.
   * @param evictProvider - EvictDynamicKey.
   * @return all routes
   */
  Observable<Reply<List<RouteDto>>> RoutesFindAll(
      Observable<List<RouteDto>> oRoutes, EvictProvider evictProvider);

  /**
   * Find route by name.
   * @param oRoutes - Route.
   * @param name - Route name.
   * @param update - EvictDynamicKey.
   * @return route
   */
  Observable<Reply<List<RouteDto>>> RoutesFindByName(
      Observable<List<RouteDto>> oRoutes, DynamicKey name, EvictDynamicKey update);

  /**
   * Find route by name.
   * @param oRoutes - Route.
   * @param name - Route name.
   * @param update - EvictDynamicKey.
   * @return route
   */
  Observable<Reply<List<RouteDto>>>  RoutesfindByNameContainingIgnoreCase(
      Observable<List<RouteDto>> oRoutes, DynamicKey name, EvictDynamicKey update);

  /**
   * Find route by owner.
   * @param oRoutes - Route.
   * @param oid - DynamicKey, Owner.
   * @param update - EvictDynamicKey.
   * @return route
   */
  Observable<Reply<List<RouteDto>>> RoutesFindByOwnerOid(
      Observable<List<RouteDto>> oRoutes, DynamicKey oid, EvictDynamicKey update);

  /**
   * Find route by difficulty.
   * @param oRoutes - Route.
   * @param difficulty - dificulty of the route.
   * @param update - EvictDynamicKey.
   * @return route
   */
  Observable<Reply<List<RouteDto>>> RoutesFindByDifficulty(
      Observable<List<RouteDto>> oRoutes, DynamicKey difficulty, EvictDynamicKey update);

  /**
   * Find route by type.
   * @param oRoutes - Route.
   * @param routetyp - Route typ.
   * @param update - EvictDynamicKey.
   * @return route
   */
  Observable<Reply<List<RouteDto>>>  RoutesFindByRoutetype(
      Observable<List<RouteDto>> oRoutes, DynamicKey routetyp, EvictDynamicKey update);

  /**
   * Find route near position.
   * @param oRoutes - Route.
   * @param latLongR - Position.
   * @param update - EvictDynamicKey.
   * @return route
   */
  Observable<Reply<List<RouteDto>>> RoutesFindNear(
      Observable<List<RouteDto>> oRoutes, DynamicKey latLongR, EvictDynamicKey update);

  /**
   * Find all routes by owner.
   * @param oUser - User.
   * @param id - User Id.
   * @param update - EvictDynamicKey.
   * @return all routes.
   */
  Observable<Reply<UserDto>>  RoutesGetOwner(
      Observable<UserDto> oUser, DynamicKey id, EvictDynamicKey update);
}
