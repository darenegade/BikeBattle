package edu.hm.cs.bikebattle.backend.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response of TOP20 Endpoint.
 * Representing an entry of the top 20.
 *
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.backend.controller
 * Author(s): Rene Zarwel
 * Date: 24.06.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Data
@AllArgsConstructor
public class TopDriveEntry {
  String name;
  String fotoUri;
  float totalTime;
  float averageSpeed;
}
