package edu.hm.cs.bikebattle.app.modell.assembler;

import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.modell.Route;
import org.springframework.hateoas.Resource;

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
  public static Resource<RouteDto> toDto(Route route) {

    return new Resource<RouteDto>(
        RouteDto.builder()
            .name(route.getName())
            .difficulty(route.getDifficulty())
            .privat(route.isPrivateRoute())
            .build(),
        route.getLinks());
  }

  /**
   * Assembles a User from a UserDTO.
   *
   * @param resource to build from
   * @return user
   */
  public static Route toBean(Resource<RouteDto> resource) {

    RouteDto routeDto = resource.getContent();

    Route route = new Route(
        routeDto.getName(),
        routeDto.isPrivat(),
        routeDto.getDifficulty()
    );

    route.add(resource.getLinks());

    return route;

  }

}
