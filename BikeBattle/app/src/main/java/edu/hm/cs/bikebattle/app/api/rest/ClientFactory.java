package edu.hm.cs.bikebattle.app.api.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.hateoas.hal.Jackson2HalModule;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

/**
 * Creates a communication between the app and the backend.
 *
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * @author Rene Zarwel
 * Date: 14.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class ClientFactory {
  /** URL.*/
  public static final String DEFAULT_BASE_URL = "https://moan.cs.hm.edu:8443/";
  /** Object Mapper.*/
  private static final ObjectMapper mapper = new ObjectMapper();
  /** Http Client Builder.*/
  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  static {
    //Configure ObjectMapper to operate with Spring Data
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new Jackson2HalModule());
  }

  private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
      .baseUrl(DEFAULT_BASE_URL)
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(JacksonConverterFactory.create(mapper));

  private static <S> S createService(Class<S> serviceClass, final String token) {

    //Add authorization if existing
    if (token != null) {
      httpClient.addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          Request original = chain.request();

          Request.Builder requestBuilder = original.newBuilder()
              .header("Accept", "application/json")
              .header("Authorization",
                  "Bearer" + " " + token)
              .method(original.method(), original.body());

          Request request = requestBuilder.build();
          return chain.proceed(request);
        }
      });
    }

    //Build Service
    OkHttpClient client = httpClient.build();
    return retrofitBuilder.client(client).build()
        .create(serviceClass);
  }

  public static void setCache(Cache cache){
    httpClient.cache(cache);
  }

  /**
   * Changes the Base URL of the Clients.
   * @param baseUrl - which the clients will use.
   */
  public static void changeBaseUrl(String baseUrl) {
    retrofitBuilder.baseUrl(baseUrl);
  }

  /**
   * Get simple UserClient.
   * @return UserClient
   */
  public static UserClient getUserClient() {
    return createService(UserClient.class, null);
  }

  /**
   * Get simple UserClient with Authorization.
   * @return userClient
   */
  public static UserClient getUserClient(final String token) {
    return createService(UserClient.class, token);
  }

  /**
   * Get simple RouteClient.
   * @return routeClient
   */
  public static RouteClient getRouteClient() {
    return createService(RouteClient.class, null);
  }

  /**
   * Get simple RouteClient with Authorization.
   * @return routeClient
   */
  public static RouteClient getRouteClient(final String token) {
    return createService(RouteClient.class, token);
  }

  /**
   * Get simple DriveClient.
   * @return driveClient
   */
  public static DriveClient getDriveClient() {
    return createService(DriveClient.class, null);
  }

  /**
   * Get simple DriveClient with Authorization.
   * @return driveClient
   */
  public static DriveClient getDriveClient(final String token) {
    return createService(DriveClient.class, token);
  }
}
