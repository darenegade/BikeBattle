package edu.hm.cs.bikebattle.app.client;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.hateoas.Resource;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@RunWith(JUnit4.class)
public class ClientTest {

  //User Tests

  @Test
  public void CreateUser(){

    UserDto user = UserDto.builder()
        .name("hans")
        .email("hans@hans.de")
        .size(1.8F)
        .weight(65F)
        .build();

    UserClient client = ClientFactory.getUserClient();

    Response<Resource<UserDto>> userDtoResponse = null;
    try {
      userDtoResponse = client.create(user).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }


    assertEquals(200, userDtoResponse.code());
    assertEquals(user, userDtoResponse.body().getContent());

  }

}