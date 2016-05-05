package edu.hm.cs.bikebattle.backend.event;

import edu.hm.cs.bikebattle.backend.domain.Route;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
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
    if(entity.getRoutePoints().size() > 0)
      entity.setStart(new double[]{
          entity.getRoutePoints().get(0).getLongitude(),
          entity.getRoutePoints().get(0).getLatitude()
      });

    super.onBeforeCreate(entity);
  }

  @Override
  protected void onBeforeSave(Route entity) {
    if(entity.getRoutePoints().size() > 0)
      entity.setStart(new double[]{
          entity.getRoutePoints().get(0).getLongitude(),
          entity.getRoutePoints().get(0).getLatitude()
      });

    super.onBeforeSave(entity);
  }
}
