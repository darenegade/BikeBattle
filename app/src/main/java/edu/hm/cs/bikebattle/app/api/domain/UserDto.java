package edu.hm.cs.bikebattle.app.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.domain
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  String name;

  String email;

  float size;

  float weight;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  List<String> friends;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  List<String> routes;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  List<String> drives;
}
