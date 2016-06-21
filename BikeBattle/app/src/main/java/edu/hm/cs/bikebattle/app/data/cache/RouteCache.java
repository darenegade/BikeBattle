package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import rx.Observable;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.data.cache
 * Author(s): Rene Zarwel
 * Date: 06.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface RouteCache {

  Observable<Reply<Resource<RouteDto>>> findeOne(
      Observable<Resource<RouteDto>> oRoute, DynamicKey id, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<RouteDto>>>> findAll(
      Observable<Resources<Resource<RouteDto>>> oRoutes);

  Observable<Reply<Resources<Resource<RouteDto>>>> findByName(
      Observable<Resources<Resource<RouteDto>>> oRoutes, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<RouteDto>>>>  findByNameContainingIgnoreCase(
      Observable<Resources<Resource<RouteDto>>> oRoutes, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<RouteDto>>>> findByOwnerOid(
      Observable<Resources<Resource<RouteDto>>> oRoutes, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<RouteDto>>> > findByDifficulty(
      Observable<Resources<Resource<RouteDto>>> oRoutes, DynamicKey difficulty, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<RouteDto>>>>  findByRoutetype(
      Observable<Resources<Resource<RouteDto>>> oRoutes, DynamicKey routetyp, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<RouteDto>>>>  findNear(
      Observable<Resources<Resource<RouteDto>>> oRoutes, DynamicKey latLongR, EvictDynamicKey update);

  Observable<Reply<Resource<UserDto>>>  getOwner(
      Observable<Resource<UserDto>> oUser, DynamicKey id, EvictDynamicKey update);
}
