package edu.hm.cs.bikebattle.app.modell.assembler;

import android.location.Location;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.RoutePointDto;
import edu.hm.cs.bikebattle.app.modell.Route;

import java.util.LinkedList;
import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell.assembler
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class RouteAssembler {

  /**
   * Assembles a RouteDTO from a Route.
   *
   * @param route to build from
   * @return routeDTO
   */
  public static RouteDto toDto(Route route) {

    List<RoutePointDto> routePoints = new LinkedList<RoutePointDto>();
    for (Location location : route) {
      routePoints.add(new RoutePointDto(
          location.getLatitude(),
          location.getLongitude(),
          location.getAltitude(),
          location.getTime()));
    }

    return RouteDto.builder()
            .name(route.getName())
            .difficulty(route.getDifficulty())
            .privat(route.isPrivateRoute())
            .routePoints(routePoints)
            .build();
  }

  /**
   * Assembles a User from a UserDTO.
   *
   * @param routeDto to build from
   * @return user
   */
  public static Route toBean(RouteDto routeDto) {

    Route route = new Route(
        routeDto.getName(),
        routeDto.isPrivat()
    );

    route.setOid(routeDto.getOid());
    route.setDifficulty(routeDto.getDifficulty());
    route.setRoutetyp(routeDto.getRoutetyp());

    for (RoutePointDto routePointDto : routeDto.getRoutePoints()) {
      Location location = new Location("");

      location.setLongitude(routePointDto.getLongitude());
      location.setLatitude(routePointDto.getLatitude());
      location.setAltitude(routePointDto.getAltitude());
      location.setTime(routePointDto.getTime());

      route.add(location);
    }

    return route;
  }

}