package edu.hm.cs.bikebattle.app.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.domain
 * Author(s): Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriveDto {

  float totalTime;

  float averageSpeed;

  List<MeasurementDto> measurements = new ArrayList<MeasurementDto>();

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String route;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  String owner;
}
