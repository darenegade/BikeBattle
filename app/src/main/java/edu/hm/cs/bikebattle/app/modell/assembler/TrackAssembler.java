package edu.hm.cs.bikebattle.app.modell.assembler;

import android.location.Location;
import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.api.domain.MeasurementDto;
import edu.hm.cs.bikebattle.app.api.domain.RoutePointDto;
import edu.hm.cs.bikebattle.app.modell.Track;

import java.util.LinkedList;
import java.util.List;

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

    List<MeasurementDto> measurements = new LinkedList<MeasurementDto>();

    for (Location location : track) {
      measurements.add(new MeasurementDto(location.getSpeed(),
          new RoutePointDto(
              location.getLatitude(),
              location.getLongitude(),
              location.getAltitude(),
              location.getTime())));
    }


    return DriveDto.builder()
            .averageSpeed(track.getAverageSpeed_in_kmh())
            .measurements(measurements)
            .build();
  }

  /**
   * Assembles a User from a UserDTO.
   *
   * @param routeDto to build from
   * @return user
   */
  public static Track toBean(DriveDto routeDto) {

    Track bean = new Track();
    bean.setOid(routeDto.getOid());

    for (MeasurementDto measurementDto : routeDto.getMeasurements()) {
      Location location = new Location("");
      location.setLatitude(measurementDto.getRoutePoint().getLatitude());
      location.setLongitude(measurementDto.getRoutePoint().getLongitude());
      location.setAltitude(measurementDto.getRoutePoint().getAltitude());
      location.setTime(measurementDto.getRoutePoint().getTime());
      location.setSpeed(measurementDto.getSpeed());

      bean.add(location);
    }

    return bean;

  }

}
