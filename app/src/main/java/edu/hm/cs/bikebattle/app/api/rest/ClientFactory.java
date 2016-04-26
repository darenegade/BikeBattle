package edu.hm.cs.bikebattle.app.api.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.hal.Jackson2HalModule;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * Author(s): Rene Zarwel
 * Date: 14.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class ClientFactory {

  private static final String BASE_URL = "http://10.0.2.2:8080/";

  private static final Retrofit retrofit;

  static {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new Jackson2HalModule());

    retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .build();
  }

  public static UserClient getUserClient() {
    return retrofit.create(UserClient.class);
  }

  public static RouteClient getRouteClient() {
    return retrofit.create(RouteClient.class);
  }

  public static DriveClient getDriveClient() {
    return retrofit.create(DriveClient.class);
  }
}
