package edu.hm.cs.bikebattle.app.data.cache;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.EvictProvider;
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
public interface UserCache {

  Observable<Reply<Resource<UserDto>>> findeOne(
      Observable<Resource<UserDto>> oUser, DynamicKey userId, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<UserDto>>>> findAll(
      Observable<Resources<Resource<UserDto>>> oUsers, EvictProvider update);

  Observable<Reply<Resources<Resource<UserDto>>>> findByName(
      Observable<Resources<Resource<UserDto>>> oUsers, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<UserDto>>>> findByNameContainingIgnoreCase(
      Observable<Resources<Resource<UserDto>>> oUsers, DynamicKey name, EvictDynamicKey update);

  Observable<Reply<Resource<UserDto>>> findByEmail(
      Observable<Resource<UserDto>> oUser, DynamicKey userMail, EvictDynamicKey update);

  Observable<Reply<Resources<Resource<UserDto>>>> getFriends(
      Observable<Resources<Resource<UserDto>>> oUsers, DynamicKey userId, EvictDynamicKey update);
}
