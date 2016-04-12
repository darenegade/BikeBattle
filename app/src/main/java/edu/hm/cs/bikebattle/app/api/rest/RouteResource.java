package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class RouteResource extends Resource<RouteDto> {
  /**
   * This is a simple way to get the Resources "class".
   */
  public static final ParameterizedTypeReference<Resources<RouteResource>> LIST =
      new ParameterizedTypeReference<Resources<RouteResource>>() {
  };

  /**
   * Create a new Resource with a blank Route_DTO.
   */
  public RouteResource() {
    super(new RouteDto());
  }

  /**
   * Create a new Resource from  a Route_DTO and multiple Resources.
   *
   * @param content The Route_DTO
   * @param links   The links to add to the resource
   */
  public RouteResource(RouteDto content, Link... links) {
    super(content, links);
  }

  /**
   * Create a new Resource from  a Route_DTO and multiple Resources.
   *
   * @param content The Route_DTO
   * @param links   The links to add to the resource
   */
  public RouteResource(RouteDto content, Iterable<Link> links) {
    super(content, links);
  }
}
