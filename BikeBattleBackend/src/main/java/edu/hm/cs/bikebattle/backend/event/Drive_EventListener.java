package edu.hm.cs.bikebattle.backend.event;

import edu.hm.cs.bikebattle.backend.domain.Drive;
import edu.hm.cs.bikebattle.backend.domain.User;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Event Listener for drive repository events.
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
public class Drive_EventListener extends AbstractRepositoryEventListener<Drive> {

  // If you need access to the database you can autowire a Repository.
  //
  // @Autowired
  // <EntityName>Repository repo;


  //Override Methods here to add your custom logic

  @Override
  protected void onBeforeCreate(Drive entity) {

    //Setting the current principle as owner.
    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder
        .getContext().getAuthentication();

    User user = (User) auth.getPrincipal();

    entity.setOwner(user);

    super.onBeforeCreate(entity);
  }
}
