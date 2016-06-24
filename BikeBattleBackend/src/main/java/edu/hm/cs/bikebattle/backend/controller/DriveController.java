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
import java.util.List;

/**
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

  @RequestMapping("/top20")
  public List<TopDriveEntry> topTwenty(@Param("routeOid")String routeOid){

    List<Drive> drives = driveRepository.findByRouteOid(routeOid);

    Collections.sort(drives, new Comparator<Drive>() {
      @Override
      public int compare(Drive o1, Drive o2) {
        return Float.compare(o1.getTotalTime(), o2.getTotalTime());
      }
    });

    List<Drive> topDrives = new ArrayList<>();

    for (Drive drive : drives) {
      if (!topDrives.contains(drive))
        topDrives.add(drive);
    }

    topDrives = topDrives.subList(0, topDrives.size() > 20 ? 20 : topDrives.size());

    List<TopDriveEntry> topEntrys = new ArrayList<>();

    for (Drive topDrive : topDrives) {
      topEntrys.add(new TopDriveEntry(
          topDrive.getOwner().getName(),
          topDrive.getOwner().getFotoUri(),
          topDrive.getTotalTime(),
          topDrive.getAverageSpeed()));
    }

    return topEntrys;

  }

}
