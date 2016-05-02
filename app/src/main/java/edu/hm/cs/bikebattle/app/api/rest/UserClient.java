package edu.hm.cs.bikebattle.app.api.rest;


import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.UUID;

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
  Call<Void>  create(@Body UserDto entity);

  @PUT(BASE_PATH + "/{id}")
  @Headers("Content-Type: application/json")
  Call<Void>  update(@Path("id") UUID id, @Body UserDto entity);

  @DELETE(BASE_PATH + "/{id}")
  Call<Void> delete(@Path("id") UUID id);

  @GET(BASE_PATH + "/{id}")
  Call<Resource<UserDto>> findeOne(@Path("id") UUID id);

  @GET(BASE_PATH + "")
  Call<Resources<Resource<UserDto>>> findAll();

  @GET(BASE_PATH + "/search/findByName")
  Call<Resources<Resource<UserDto>>>  findByName(@Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  Call<Resources<Resource<UserDto>>>  findByNameContainingIgnoreCase(@Query("name") String name);

  //Relation Endpoints

  @POST(BASE_PATH + "/{id}/friends")
  @Headers("Content-Type: text/uri-list")
  Call<Void> addFriend(@Path("id") UUID id, @Body UUID friend);

  @GET(BASE_PATH + "/{id}/friends")
  Call<Resources<Resource<UserDto>>>  getFriends(@Path("id") UUID id);

}
