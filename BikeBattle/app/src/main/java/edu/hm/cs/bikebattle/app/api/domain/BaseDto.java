package edu.hm.cs.bikebattle.app.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base Class of all entities.
 *
 *
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.domain
 * @author Rene Zarwel
 * Date: 18.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String oid;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  String version;
}
