package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
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
public interface DriveCache {

  Observable<Reply<Resource<DriveDto>>> findeOne(
      Observable<Resource<DriveDto>> oDrive, DynamicKey id, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<DriveDto>>>> findAll(
      Observable<Resources<Resource<DriveDto>>> oDrives);

  Observable<Reply<Resources<Resource<DriveDto>>>>  findByRouteOid(
      Observable<Resources<Resource<DriveDto>>> oDrives, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<DriveDto>>>>  findByOwnerOid(
      Observable<Resources<Resource<DriveDto>>> oDrives, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<TopDriveEntryDto>>>>  topTwentyOfRoute(
      Observable<Resources<Resource<TopDriveEntryDto>>> oTopEntrys, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<Resource<RouteDto>>>  getRoute(
      Observable<Resource<RouteDto>> oRoute, DynamicKey id, EvictDynamicKey update);

  Observable<Reply<Resource<UserDto>>>  getOwner(
      Observable<Resource<UserDto>> oUser, DynamicKey id, EvictDynamicKey update);
}
