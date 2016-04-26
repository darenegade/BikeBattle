package edu.hm.cs.bikebattle.app.client;

import android.util.Log;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import junit.framework.TestCase;
import org.springframework.hateoas.Resource;
import retrofit2.Response;

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

  private UserClient client = ClientFactory.getUserClient();

  private UserDto user = UserDto.builder()
      .name("hans")
      .email("hans@hans.de")
      .size(1.8F)
      .weight(65F)
      .build();

  protected void setUp() throws Exception {
    Response<Void> response = client.create(user).execute();

    String[] tmp = response.headers().get("Location").split("/");
    String oid = tmp[tmp.length - 1];

    user.setOid(UUID.fromString(oid));
    Log.d("----TEST----", oid);
  }

  protected void tearDown() throws Exception {
    client.delete(user.getOid().toString()).execute();
  }

  //User Tests
  public void testGetUser() throws Exception{


    Response<Resource<UserDto>> userDtoResponse = client.findeOne(user.getOid().toString()).execute();

    assertEquals(200, userDtoResponse.code());

    assertEquals(user, userDtoResponse.body().getContent());

  }

}