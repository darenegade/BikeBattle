package edu.hm.cs.bikebattle.app.client;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import junit.framework.TestCase;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class ClientTest extends TestCase {

  public static final String TEST_BASE_URL = "http://10.0.2.2:8080/";

  private UserClient client;

  private UserDto user1 = UserDto.builder()
      .name("hans")
      .email("hans@hans.de")
      .size(1.8F)
      .weight(65F)
      .build();

  private UserDto user2 = UserDto.builder()
      .name("peter")
      .email("peter@hans.de")
      .size(1.8F)
      .weight(65F)
      .build();

  protected void setUp() throws Exception {

    //Change BaseUrl to test against local running Backend
    ClientFactory.changeBaseUrl(TEST_BASE_URL);

    client = ClientFactory.getUserClient();

    Response<Void> response;
    String[] tmp;
    String oid;

    //Create User 1
    response = client.create(user1).execute();
    assertEquals(201, response.code());

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    user1.setOid(UUID.fromString(oid));

    //Create User 2
    response = client.create(user2).execute();
    assertEquals(201, response.code());

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    user2.setOid(UUID.fromString(oid));
  }

  protected void tearDown() throws Exception {
    client.delete(user1.getOid()).execute();
    client.delete(user2.getOid()).execute();
  }

  //User Tests
  public void testFindOne() throws Exception{


    Response<Resource<UserDto>> userDtoResponse = client.findeOne(user1.getOid()).execute();

    assertEquals(200, userDtoResponse.code());

    assertEquals(user1, userDtoResponse.body().getContent());

  }

  public void testFindAll() throws Exception{


    Response<Resources<Resource<UserDto>>> userDtoResponse = client.findAll().execute();

    assertEquals(200, userDtoResponse.code());

    Collection<Resource<UserDto>> usersResources = userDtoResponse.body().getContent();

    ArrayList<UserDto> users = new ArrayList<UserDto>();
    for (Resource<UserDto> userResource : usersResources) {
      users.add(userResource.getContent());
    }

    assertTrue(users.contains(user1));
    assertTrue(users.contains(user2));

  }

  public void testPutGetFriends() throws Exception{


    //Put Friends
    Response<Void> userDtoResponse = client.addFriend(
        user1.getOid(), user1.getOid().toString())
        .execute();

    assertEquals(204, userDtoResponse.code());

    userDtoResponse = client.addFriend(
        user1.getOid(),  user2.getOid().toString())
        .execute();

    assertEquals(204, userDtoResponse.code());

    //Get Friends
    Response<Resources<Resource<UserDto>>> userDtoResponse2 = client.getFriends(user1.getOid()).execute();

    assertEquals(200, userDtoResponse2.code());

    Collection<Resource<UserDto>> usersResources = userDtoResponse2.body().getContent();

    ArrayList<UserDto> users = new ArrayList<UserDto>();
    for (Resource<UserDto> userResource : usersResources) {
      users.add(userResource.getContent());
    }

    assertTrue(users.contains(user1));
    assertTrue(users.contains(user2));

  }
}