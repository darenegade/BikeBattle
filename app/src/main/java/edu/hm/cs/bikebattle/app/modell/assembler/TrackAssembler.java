package edu.hm.cs.bikebattle.app.modell.assembler;

import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.modell.Track;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell.assembler
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class TrackAssembler {

  /**
   * Assembles a RouteDTO from a Route.
   *
   * @param track to build from
   * @return routeDTO
   */
  public static DriveDto toDto(Track track) {

    return DriveDto.builder()

            .build();
  }

  /**
   * Assembles a User from a UserDTO.
   *
   * @param routeDto to build from
   * @return user
   */
  public static Track toBean(DriveDto routeDto) {

    return new Track();

  }

}
