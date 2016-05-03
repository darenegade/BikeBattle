package edu.hm.cs.bikebattle.app.api.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.hal.Jackson2HalModule;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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

  public static final String DEFAULT_BASE_URL = "https://moan.cs.hm.edu:8443/BikeBattleBackend/";

  private static Retrofit retrofit;

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new Jackson2HalModule());

    retrofit = createRetrofit(DEFAULT_BASE_URL);
  }

  /**
   * Changes the Base URL of the Clients.
   * @param baseUrl which the clients will use.
   */
  public static void changeBaseUrl(String baseUrl) {
    retrofit = createRetrofit(baseUrl);
  }

  private static Retrofit createRetrofit(String baseUrl) {
    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(ScalarsConverterFactory.create())
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
