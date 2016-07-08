package edu.hm.cs.bikebattle.backend.controller;

import edu.hm.cs.bikebattle.backend.domain.Drive;
import edu.hm.cs.bikebattle.backend.repositories.DriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Controller for non default drive endpoints.
 *
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.controller
 * Author(s): Rene Zarwel
 * Date: 05.05.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@RestController
@ExposesResourceFor(Drive.class)
@RequestMapping("/drives")
public class DriveController {

  @Autowired
  DriveRepository driveRepository;

  /**
   * Endpoint to get the TOP20 of a route.
   *
   * @param routeOid route to use.
   * @return Top 20 drives of given route.
   */
  @RequestMapping("/top20")
  public List<TopDriveEntry> topTwenty(@Param("routeOid") String routeOid) {

    List<Drive> drives = driveRepository.findByRouteOid(routeOid);

    //Sort all Drives by total time
    Collections.sort(drives, new Comparator<Drive>() {
      @Override
      public int compare(Drive o1, Drive o2) {
        return Float.compare(o1.getTotalTime(), o2.getTotalTime());
      }
    });

    //Get top 20 entries
    LinkedHashMap<String, Drive> topDrives = new LinkedHashMap<>();
    Iterator<Drive> driveIterator = drives.iterator();

    Drive drive;
    while (driveIterator.hasNext() && topDrives.size() < 20) {
      drive = driveIterator.next();

      if (!topDrives.containsKey(drive.getOwner().getEmail()))
        topDrives.put(drive.getOwner().getEmail(), drive);

    }


    //Map entries to TopDriveEntries
    List<TopDriveEntry> topEntrys = new ArrayList<>();

    for (Drive topDrive : topDrives.values()) {
      topEntrys.add(new TopDriveEntry(
          topDrive.getOwner().getName(),
          topDrive.getOwner().getFotoUri(),
          topDrive.getTotalTime(),
          topDrive.getAverageSpeed()));
    }

    return topEntrys;

  }

}
