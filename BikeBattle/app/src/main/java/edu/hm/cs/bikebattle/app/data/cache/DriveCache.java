package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
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
public interface DriveCache {

  Observable<Reply<DriveDto>> DrivesFindeOne(
      Observable<DriveDto> oDrive, DynamicKey id, EvictDynamicKey update);

  Observable<Reply<List<DriveDto>>> DrivesFindAll(
      Observable<List<DriveDto>> oDrives);

  Observable<Reply<List<DriveDto>>>  DrivesFindByRouteOid(
      Observable<List<DriveDto>> oDrives, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<List<DriveDto>>> DrivesFindByOwnerOid(
      Observable<List<DriveDto>> oDrives, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<List<TopDriveEntryDto>>> DriveTopTwentyOfRoute(
      Observable<List<TopDriveEntryDto>> oTopEntrys, DynamicKey oid, EvictDynamicKey update);

  Observable<Reply<RouteDto>> DrivesGetRoute(
      Observable<RouteDto> oRoute, DynamicKey id, EvictDynamicKey update);

  Observable<Reply<UserDto>>  DrivesGetOwner(
      Observable<UserDto> oUser, DynamicKey id, EvictDynamicKey update);
}
