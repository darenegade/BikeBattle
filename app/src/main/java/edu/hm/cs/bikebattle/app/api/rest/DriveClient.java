package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
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
interface DriveClient {

  String BASE_PATH = "/drives";


  //Drive Endpoints

  @POST(BASE_PATH + "")
  @Headers("Content-Type: application/json")
  DriveDto create(@Body DriveDto entity);

  @PUT(BASE_PATH + "")
  @Headers("Content-Type: application/json")
  DriveDto update(@Body DriveDto entity);

  @DELETE(BASE_PATH + "/{id}")
  void delete(@Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  DriveDto findeOne(@Path("id") String id);

  @GET(BASE_PATH + "")
  List<DriveDto> findAll();

  @GET(BASE_PATH + "/search/findByRouteOid")
  List<DriveDto> findByRouteOid(@Query("oid") String oid);

  //Relatiins Endpoints

  @POST(BASE_PATH + "/{id}/route")
  @Headers("Content-Type: text/uri-list")
  void setRoute(@Path("id") String id, @Body String route);

  @GET(BASE_PATH + "/{id}/routes")
  List<RouteDto> getRoutes(@Path("id") String id);

}
