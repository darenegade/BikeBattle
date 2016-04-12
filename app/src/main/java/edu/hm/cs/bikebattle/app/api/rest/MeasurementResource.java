package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.MeasurementDto;
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
public class MeasurementResource extends Resource<MeasurementDto> {
  /**
   * This is a simple way to get the Resources "class".
   */
  public static final ParameterizedTypeReference<Resources<MeasurementResource>> LIST =
      new ParameterizedTypeReference<Resources<MeasurementResource>>() {
  };

  /**
   * Create a new Resource with a blank Measurement_DTO.
   */
  public MeasurementResource() {
    super(new MeasurementDto());
  }

  /**
   * Create a new Resource from  a Measurement_DTO and multiple Resources.
   *
   * @param content The Measurement_DTO
   * @param links   The links to add to the resource
   */
  public MeasurementResource(MeasurementDto content, Link... links) {
    super(content, links);
  }

  /**
   * Create a new Resource from  a Measurement_DTO and multiple Resources.
   *
   * @param content The Measurement_DTO
   * @param links   The links to add to the resource
   */
  public MeasurementResource(MeasurementDto content, Iterable<Link> links) {
    super(content, links);
  }
}
