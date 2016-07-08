package edu.hm.cs.bikebattle.backend.event;

import edu.hm.cs.bikebattle.backend.domain.User;
import edu.hm.cs.bikebattle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 *  Event Listener for user repository events.
 *
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.domain
 * Author(s): Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Component
public class User_EventListener extends AbstractRepositoryEventListener<User> {
  // If you need access to the database you can autowire a Repository.
  //
  // @Autowired
  // <EntityName>Repository repo;

  @Autowired
  UserRepository repository;


  //Override Methods here to add your custom logic


  @Override
  protected void onBeforeLinkSave(User parent, Object linked) {

    //Prevent multiple friendship relations to same user
    if(linked instanceof List) {
      List linkedList = List.class.cast(linked);
      if(linkedList.get(0) != null && linkedList.get(0) instanceof User){
        @SuppressWarnings("unchecked") List<User> userList = linkedList;

        //Remove duplicates on friendList
        HashSet<User> users = new HashSet<>();
        users.addAll(userList);
        userList.clear();
        userList.addAll(users);


      }
    }

    super.onBeforeLinkSave(parent, linked);
  }
}
