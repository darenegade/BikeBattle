package edu.hm.cs.bikebattle.app.api.rest;


import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
interface UserClient {

  @POST("/users")
  @Headers("Content-Type: application/json")
  UserDto create(@Body UserDto entity);

  @PUT("/users")
  @Headers("Content-Type: application/json")
  UserDto update(@Body UserDto entity);

  @DELETE("/users/{id}")
  void delete(@Path("id") String id);

  @GET("/users/{id}")
  UserDto findeOne(@Path("id") String id);

  @GET("/users")
  List<UserDto> findAll();

}
