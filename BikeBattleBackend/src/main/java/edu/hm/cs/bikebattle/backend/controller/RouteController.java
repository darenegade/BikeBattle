package edu.hm.cs.bikebattle.backend.controller;

import edu.hm.cs.bikebattle.backend.domain.Route;
import edu.hm.cs.bikebattle.backend.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.controller
 * Author(s): Rene Zarwel
 * Date: 05.05.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@RestController
@ExposesResourceFor(Route.class)
@RequestMapping("/routes")
public class RouteController {

  @Autowired
  RouteRepository routeRepository;

  @RequestMapping("/search/findNear")
  public List<Route> findNearRoutes(@Param("longitude") Double longitude,@Param("latitude") Double latitude,@Param("r") Double r){
    return routeRepository.findByStartWithin(new Circle(new Point(longitude, latitude), r));
  }

}
