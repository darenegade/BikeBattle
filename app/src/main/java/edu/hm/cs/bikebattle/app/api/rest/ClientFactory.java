package edu.hm.cs.bikebattle.app.api.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

  private static final String BASEURL = "http://10.0.2.2:8080/";

  private static final Retrofit retrofit;

  static {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    // set your desired log level
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    // add other interceptors here:

    // add logging as last interceptor
    httpClient.addInterceptor(logging);


    retrofit = new Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(httpClient.build())
            .addConverterFactory(JacksonConverterFactory.create())
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
