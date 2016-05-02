package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
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
public interface RouteClient {

  String BASE_PATH = "/routes";


  //Route Endpoints

  @POST(BASE_PATH)
  Call<Void> create(@Body RouteDto entity);

  @PUT(BASE_PATH)
  @Headers("Content-Type: application/json")
  Call<Void>  update(@Body RouteDto entity);

  @DELETE(BASE_PATH + "/{id}")
  Call<Void> delete(@Path("id") UUID id);

  @GET(BASE_PATH + "/{id}")
  Call<Resource<RouteDto>> findeOne(@Path("id") UUID id);

  @GET(BASE_PATH + "")
  Call<Resources<Resource<RouteDto>>> findAll();

  @GET(BASE_PATH + "/search/findByName")
  Call<Resources<Resource<RouteDto>>>  findByName(@Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  Call<Resources<Resource<RouteDto>>>  findByNameContainingIgnoreCase(@Query("name") String name);

  @GET(BASE_PATH + "/search/findByOwnerOid")
  Call<Resources<Resource<RouteDto>>>  findByOwnerOid(@Query("oid") UUID oid);

  @GET(BASE_PATH + "/search/findByDifficulty")
  Call<Resources<Resource<RouteDto>>>  findByOwnerOid(@Query("difficulty") Difficulty difficulty);

  //Relation Endpoints

  @PUT(BASE_PATH + "/{id}/owner")
  @Headers("Content-Type: text/uri-list")
  Call<Void> setOwner(@Path("id") UUID id, @Body UUID owner);

  @GET(BASE_PATH + "/{id}/owner")
  Call<Resource<UserDto>>  getOwner(@Path("id") UUID id);


}
