package edu.hm.cs.bikebattle.backend.event;

import edu.hm.cs.bikebattle.backend.domain.Route;
import edu.hm.cs.bikebattle.backend.domain.User;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *  Event Listener for route repository events.
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
public class Route_EventListener extends AbstractRepositoryEventListener<Route> {
	// If you need access to the database you can autowire a Repository.
	//
	// @Autowired
	// <EntityName>Repository repo;



	//Override Methods here to add your custom logic

  @Override
  protected void onBeforeCreate(Route entity) {

    //Set first point of route to the start point.
    if(entity.getRoutePoints().size() > 0)
      entity.setStart(new double[]{
          entity.getRoutePoints().get(0).getLongitude(),
          entity.getRoutePoints().get(0).getLatitude()
      });

    //Set current principle to owner of route.
    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder
        .getContext().getAuthentication();

    User user = (User) auth.getPrincipal();

    entity.setOwner(user);

    super.onBeforeCreate(entity);
  }

  @Override
  protected void onBeforeSave(Route entity) {
    //update start point
    if(entity.getRoutePoints().size() > 0)
      entity.setStart(new double[]{
          entity.getRoutePoints().get(0).getLongitude(),
          entity.getRoutePoints().get(0).getLatitude()
      });

    super.onBeforeSave(entity);
  }
}
