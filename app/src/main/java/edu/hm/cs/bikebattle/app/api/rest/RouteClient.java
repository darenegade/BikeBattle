package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
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
public interface RouteClient {

  String BASE_PATH = "/routes";


  //Route Endpoints

  @POST(BASE_PATH + "")
  @Headers("Content-Type: application/json")
  RouteDto create(@Body RouteDto entity);

  @PUT(BASE_PATH + "")
  @Headers("Content-Type: application/json")
  RouteDto update(@Body RouteDto entity);

  @DELETE(BASE_PATH + "/{id}")
  void delete(@Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  RouteDto findeOne(@Path("id") String id);

  @GET(BASE_PATH + "")
  List<RouteDto> findAll();

  @GET(BASE_PATH + "/search/findByName")
  List<RouteDto> findByName(@Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  List<RouteDto> findByNameContainingIgnoreCase(@Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  List<RouteDto> findByDifficulty(@Query("difficulty") Difficulty difficulty);


}
