package edu.hm.cs.bikebattle.app.api.rest;


import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * Author(s): Rene Zarwel
 * Date: 14.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface UserClient {

  String BASE_PATH = "/users";

  //User Endpoints

  @POST(BASE_PATH)
  @Headers("Content-Type: application/json")
  Call<UserDto> create(@Body UserDto entity);

  @PUT(BASE_PATH)
  @Headers("Content-Type: application/json")
  UserDto update(@Body UserDto entity);

  @DELETE(BASE_PATH + "/{id}")
  void delete(@Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  UserDto findeOne(@Path("id") String id);

  @GET(BASE_PATH + "")
  List<UserDto> findAll();

  @GET(BASE_PATH + "/search/findByName")
  List<UserDto> findByName(@Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  List<UserDto> findByNameContainingIgnoreCase(@Query("name") String name);

  //Relation Endpoints

  @POST(BASE_PATH + "/{id}/routes")
  @Headers("Content-Type: text/uri-list")
  void setRoutes(@Path("id") String id, @Body List<String> routes);

  @POST(BASE_PATH + "/{id}/friends")
  @Headers("Content-Type: text/uri-list")
  void setFriends(@Path("id") String id, @Body List<String> friends);

  @POST(BASE_PATH + "/{id}/drives")
  @Headers("Content-Type: text/uri-list")
  void setDrives(@Path("id") String id, @Body List<String> drives);

  @GET(BASE_PATH + "/{id}/routes")
  List<RouteDto> getRoutes(@Path("id") String id);

  @GET(BASE_PATH + "/{id}/friends")
  List<UserDto> getFriends(@Path("id") String id);

  @GET(BASE_PATH + "/{id}/drives")
  List<DriveDto> getDrives(@Path("id") String id);


}
