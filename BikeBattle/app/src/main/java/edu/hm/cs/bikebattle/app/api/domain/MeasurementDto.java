package edu.hm.cs.bikebattle.app.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Mesurement information.
 *
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.domain
 * @author Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDto {
  /**current speed.*/
  float speed;
  /**current point.  */
  RoutePointDto routePoint;
}
