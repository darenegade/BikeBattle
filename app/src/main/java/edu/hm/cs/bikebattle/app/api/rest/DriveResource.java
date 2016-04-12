package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
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
public class DriveResource extends Resource<DriveDto> {
  /**
   * This is a simple way to get the Resources "class".
   */
  public static final ParameterizedTypeReference<Resources<DriveResource>> LIST =
      new ParameterizedTypeReference<Resources<DriveResource>>() {
  };

  /**
   * Create a new Resource with a blank Drive_DTO.
   */
  public DriveResource() {
    super(new DriveDto());
  }

  /**
   * Create a new Resource from  a Drive_DTO and multiple Resources.
   *
   * @param content The Drive_DTO
   * @param links   The links to add to the resource
   */
  public DriveResource(DriveDto content, Link... links) {
    super(content, links);
  }

  /**
   * Create a new Resource from  a Drive_DTO and multiple Resources.
   *
   * @param content The Drive_DTO
   * @param links   The links to add to the resource
   */
  public DriveResource(DriveDto content, Iterable<Link> links) {
    super(content, links);
  }
}
