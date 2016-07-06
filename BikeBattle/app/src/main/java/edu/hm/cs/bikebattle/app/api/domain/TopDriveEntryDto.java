package edu.hm.cs.bikebattle.app.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.domain
 * @author Rene Zarwel
 * Date: 24.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TopDriveEntryDto {
  /**Name of the best driver.*/
  String name;
  /**Picture from the user of the best driver.*/
  String fotoUri;
  /**Total time of the route from the best drivers.*/
  float totalTime;
  /**Average Speed of the route from the best drivers.*/
  float averageSpeed;
}
