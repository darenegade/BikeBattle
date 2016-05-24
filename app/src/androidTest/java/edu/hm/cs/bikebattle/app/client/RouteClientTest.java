package edu.hm.cs.bikebattle.app.client;

import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.RoutePointDto;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.RouteClient;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import junit.framework.TestCase;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class RouteClientTest extends TestCase {

  public static final String TEST_BASE_URL = "http://10.0.2.2:8080/";

  public static final String TOKEN = "Bearer INSERT TOKEN HERE";


  private UserClient userClient;
  private RouteClient routeClient;


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

  private RouteDto route2 = RouteDto.builder()
      .name("route2")
      .difficulty(Difficulty.EASY)
      .routetyp(Routetyp.OFFROAD)
      .length(100)
      .build();

  protected void setUp() throws Exception {

    //Change BaseUrl to test against local running Backend
    //ClientFactory.changeBaseUrl(TEST_BASE_URL);

    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();

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

    //Create Route 2
    route2.setOwner("http://localhost:8080/users/" + user1.getOid());
    route2.setRoutePoints(routePoints);

    response = routeClient.create(TOKEN,route2).execute();

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    route2.setOid(oid);

  }

  protected void tearDown() throws Exception {
    routeClient.delete(TOKEN,route1.getOid()).execute();
    routeClient.delete(TOKEN,route2.getOid()).execute();
    userClient.delete(TOKEN,user1.getOid()).execute();
  }

  //Route Tests
  public void testFindOne() throws Exception{

    Response<Resource<RouteDto>> routeDtoResponse = routeClient.findeOne(TOKEN,route1.getOid()).execute();

    assertEquals(200, routeDtoResponse.code());

    assertEquals(route1, routeDtoResponse.body().getContent());


  }

  public void testFindAll() throws Exception{

    Response<Resources<Resource<RouteDto>>> routeDtoResponse = routeClient.findAll(TOKEN).execute();

    assertEquals(200, routeDtoResponse.code());

    Collection<Resource<RouteDto>> routesResources = routeDtoResponse.body().getContent();

    ArrayList<RouteDto> routes = new ArrayList<RouteDto>();
    for (Resource<RouteDto> routeResource : routesResources) {
      routes.add(routeResource.getContent());
    }

    assertTrue(routes.contains(route1));
    assertTrue(routes.contains(route2));

  }
}