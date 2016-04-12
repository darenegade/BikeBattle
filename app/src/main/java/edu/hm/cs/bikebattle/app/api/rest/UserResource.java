package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
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
public class UserResource extends Resource<UserDto> {
  /**
   * This is a simple way to get the Resources "class".
   */
  public static final ParameterizedTypeReference<Resources<UserResource>> LIST =
      new ParameterizedTypeReference<Resources<UserResource>>() {
      };

  /**
   * Create a new Resource with a blank User_DTO.
   */
  public UserResource() {
    super(new UserDto());
  }

  /**
   * Create a new Resource from  a User_DTO and multiple Resources.
   *
   * @param content The User_DTO
   * @param links   The links to add to the resource
   */
  public UserResource(UserDto content, Link... links) {
    super(content, links);
  }

  /**
   * Create a new Resource from  a User_DTO and multiple Resources.
   *
   * @param content The User_DTO
   * @param links   The links to add to the resource
   */
  public UserResource(UserDto content, Iterable<Link> links) {
    super(content, links);
  }
}
