package edu.hm.cs.bikebattle.app.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
@AllArgsConstructor
@NoArgsConstructor
public class User_DTO {

    String name;

    String email;

    float size;

    float weight;

    List<String> friends = new ArrayList<String>();

    List<String> routes = new ArrayList<String>();

    List<String> drives = new ArrayList<String>();
}
