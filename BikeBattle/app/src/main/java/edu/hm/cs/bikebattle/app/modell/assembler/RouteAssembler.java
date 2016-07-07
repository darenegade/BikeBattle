package edu.hm.cs.bikebattle.app.modell.assembler;

import android.location.Location;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.RoutePointDto;
import edu.hm.cs.bikebattle.app.modell.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell.assembler
 * @author Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class RouteAssembler {

  /**
   * Assembles a RouteDTO from a Route.
   * @param route - To build from.
   * @return routeDTO
   */
  public static RouteDto toDto(Route route) {

    List<RoutePointDto> routePoints = new ArrayList<RoutePointDto>();
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
        .length(route.getDistanceInM())
        .routetyp(route.getRoutetyp())
        .build();
  }

  /**
   * Assembles a User from a UserDTO.
   * @param routeDto - To build from.
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

    if(routeDto.getOwner() != null)
      route.setOwner(UserAssembler.toBean(routeDto.getOwner()));

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
