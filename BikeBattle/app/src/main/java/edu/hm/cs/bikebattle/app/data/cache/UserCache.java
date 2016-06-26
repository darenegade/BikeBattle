package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.EvictProvider;
import io.rx_cache.Reply;
import rx.Observable;

import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.data.cache
 * Author(s): Rene Zarwel
 * Date: 06.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface UserCache {

  Observable<Reply<UserDto>> findeOne(
      Observable<UserDto> oUser, DynamicKey userId, EvictDynamicKey update);

  Observable<Reply<List<UserDto>>> findAll(
      Observable<List<UserDto>> oUsers, EvictProvider update);

  Observable<Reply<List<UserDto>>> findByName(
      Observable<List<UserDto>> oUsers, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<List<UserDto>>> findByNameContainingIgnoreCase(
      Observable<List<UserDto>> oUsers, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<UserDto>> findByEmail(
      Observable<UserDto> oUser, DynamicKey userMail, EvictDynamicKey update);

  Observable<Reply<List<UserDto>>> getFriends(
      Observable<List<UserDto>> oUsers, DynamicKey userId, EvictDynamicKey update);
}
