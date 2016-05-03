package edu.hm.cs.bikebattle.app.client;

import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.DriveClient;
import edu.hm.cs.bikebattle.app.api.rest.RouteClient;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import junit.framework.TestCase;

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

  private UserClient userClient;
  private RouteClient routeClient;
  private DriveClient driveClient;



  protected void setUp() throws Exception {

    //Change BaseUrl to test against local running Backend
    ClientFactory.changeBaseUrl("http://10.0.2.2:8080/");

    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();
    driveClient = ClientFactory.getDriveClient();


  }

  protected void tearDown() throws Exception {
  }

  //Drive Tests
  public void testFindOne() throws Exception{



  }

  public void testFindAll() throws Exception{


  }
}