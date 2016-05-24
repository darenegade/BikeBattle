package edu.hm.cs.bikebattle.app.client;

import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.api.domain.MeasurementDto;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.RoutePointDto;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.DriveClient;
import edu.hm.cs.bikebattle.app.api.rest.RouteClient;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import junit.framework.TestCase;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class DriveClientTest extends TestCase {

  public static final String TEST_BASE_URL = "http://10.0.2.2:8080/";

  public static final String TOKEN = "Bearer INSERT TOKEN HERE";

  private UserClient userClient;
  private RouteClient routeClient;
  private DriveClient driveClient;


  private UserDto user1 = UserDto.builder()
      .name("hans")
      .email("hans@hans.de")
      .build();

  private RouteDto route1 = RouteDto.builder()
      .name("route")
      .difficulty(Difficulty.EASY)
      .routetyp(Routetyp.CITY)
      .length(12)
      .build();

  private DriveDto drive1 = DriveDto.builder()
      .averageSpeed(25)
      .totalTime(25)
      .build();

  private DriveDto drive2 = DriveDto.builder()
      .averageSpeed(20)
      .totalTime(50)
      .build();

  protected void setUp() throws Exception {

    //Change BaseUrl to test against local running Backend
    ClientFactory.changeBaseUrl(TEST_BASE_URL);

    userClient = ClientFactory.getUserClient(TOKEN);
    routeClient = ClientFactory.getRouteClient(TOKEN);
    driveClient = ClientFactory.getDriveClient(TOKEN);

    Response<Void> response;
    String[] tmp;
    String oid;

    //Create User 1
    response = userClient.create(TOKEN,user1).execute();
    assertEquals(201, response.code());

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    user1.setOid(oid);

    //RoutePoints

    ArrayList<RoutePointDto> routePoints = new ArrayList<RoutePointDto>();
    routePoints.add(new RoutePointDto(70.2,-80.1,110,1234));
    routePoints.add(new RoutePointDto(70.23,-80.15,115,1235));

    //Create Route 1
    route1.setOwner( "http://localhost:8080/users/" + user1.getOid());
    route1.setRoutePoints(routePoints);

    response = routeClient.create(TOKEN,route1).execute();

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    route1.setOid(oid);

    //Create Drive 1
    drive1.setOwner("http://localhost:8080/users/" + user1.getOid());
    drive1.setRoute("http://localhost:8080/routes/" + route1.getOid());

    List<MeasurementDto> measurements = new ArrayList<MeasurementDto>();
    measurements.add(new MeasurementDto(25, routePoints.get(0)));
    measurements.add(new MeasurementDto(24, routePoints.get(1)));
    drive1.setMeasurements(measurements);

    response = driveClient.create(TOKEN,drive1).execute();

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    drive1.setOid(oid);

    //Create Drive 2
    drive2.setOwner("http://localhost:8080/users/" + user1.getOid());
    drive2.setRoute("http://localhost:8080/routes/" + route1.getOid());

    drive2.setMeasurements(measurements);

    response = driveClient.create(TOKEN,drive2).execute();

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    drive2.setOid(oid);

  }

  protected void tearDown() throws Exception {
    driveClient.delete(TOKEN,drive1.getOid()).execute();
    driveClient.delete(TOKEN,drive2.getOid()).execute();
    routeClient.delete(TOKEN,route1.getOid()).execute();
    userClient.delete(TOKEN,user1.getOid()).execute();
  }

  //Route Tests
  public void testFindOne() throws Exception{

    Response<Resource<DriveDto>> driveDtoResponse = driveClient.findeOne(TOKEN,drive1.getOid()).execute();

    assertEquals(200, driveDtoResponse.code());

    assertEquals(drive1, driveDtoResponse.body().getContent());


  }

  public void testFindAll() throws Exception{

    Response<Resources<Resource<DriveDto>>> driveDtoResponse = driveClient.findAll(TOKEN).execute();

    assertEquals(200, driveDtoResponse.code());

    Collection<Resource<DriveDto>> drivesResources = driveDtoResponse.body().getContent();

    ArrayList<DriveDto> routes = new ArrayList<DriveDto>();
    for (Resource<DriveDto> driveResource : drivesResources) {
      routes.add(driveResource.getContent());
    }

    assertTrue(routes.contains(drive1));
    assertTrue(routes.contains(drive2));

  }
}