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

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class UserClientTest extends TestCase {

  public static final String TEST_BASE_URL = "http://10.0.2.2:8080/";
  public static final String TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjNhODIyN2VhYjZhYWNlY2UxYzk2NWYzNjAzMDJiOThiZTkzNDVkMTcifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdWQiOiIxMDA1NTUzMzExNTA4LXI5MHBvMWhmODg4cmtyMnU0NjRjY29vOHZ2b2prNzV1LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTE3NTMyNDE4NjE3MDk5NTY4MTI4IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF6cCI6IjEwMDU1NTMzMTE1MDgtZDQ3aWJ2ZTM0NzRla282MDIxajgxNXJyZHNvMWgyN2wuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJlbWFpbCI6ImRhcmVuZWdhZGVAZ214LmRlIiwiaWF0IjoxNDY0MDk1NjczLCJleHAiOjE0NjQwOTkyNzMsIm5hbWUiOiJIYW5zIFd1cnN0IiwicGljdHVyZSI6Imh0dHBzOi8vbGg1Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tVER4cl9tT0c0eTQvQUFBQUFBQUFBQUkvQUFBQUFBQUFBQkEvRjNja2w2ZW9uNTAvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IkhhbnMiLCJmYW1pbHlfbmFtZSI6Ild1cnN0IiwibG9jYWxlIjoiZGUifQ.hgsdyhdxkBax7hLR5e14ViuORmgz9uMose-GdtOTIuJoSWMC9hhbUQ-Y86dsNQb6tloJMtWXuuA6WQdbRDFdBUMn9YwQ9QA2MwDWBiEJOXOIxg75q8nmeKsYd6r6VUkCdzBv984HMWpmlKv7IQtSaGJPLoZLzMZCmKSvcbZ74W2a0sJyeH90SgPX7hMw5OWMFWnYANg5hBl7HrnvKDA4dXIskbY9OMg14WlkJp_7l0QZPaSSPkgNmG_X0Jvwpe2JMoFjDf-CiF01EVk5d8SfTB8_OEcwXQcibGL-HVKUkfyosEQdNXCEi9080-i-0haVaiFvba8iP8NPqZarAtkcgw";

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
    response = client.create(TOKEN, user1).execute();
    assertEquals(201, response.code());

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    user1.setOid(oid);

    //Create User 2
    response = client.create(TOKEN,user2).execute();
    assertEquals(201, response.code());

    tmp = response.headers().get("Location").split("/");
    oid = tmp[tmp.length - 1];

    user2.setOid(oid);
  }

  protected void tearDown() throws Exception {
    client.delete(TOKEN,user1.getOid()).execute();
    client.delete(TOKEN,user2.getOid()).execute();
  }

  //User Tests
  public void testFindOne() throws Exception{


    Response<Resource<UserDto>> userDtoResponse = client.findeOne(TOKEN,user1.getOid()).execute();

    assertEquals(200, userDtoResponse.code());

    assertEquals(user1, userDtoResponse.body().getContent());

  }

  public void testFindAll() throws Exception{


    Response<Resources<Resource<UserDto>>> userDtoResponse = client.findAll(TOKEN).execute();

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
    Response<Void> userDtoResponse = client.addFriend(TOKEN,
        user1.getOid(),  user2.getOid())
        .execute();

    assertEquals(204, userDtoResponse.code());

    //Get Friends
    Response<Resources<Resource<UserDto>>> userDtoResponse2 = client.getFriends(TOKEN,user1.getOid()).execute();

    assertEquals(200, userDtoResponse2.code());

    Collection<Resource<UserDto>> usersResources = userDtoResponse2.body().getContent();

    ArrayList<UserDto> users = new ArrayList<UserDto>();
    for (Resource<UserDto> userResource : usersResources) {
      users.add(userResource.getContent());
    }

    assertTrue(users.contains(user2));

  }
}