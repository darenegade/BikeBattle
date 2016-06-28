package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * @author Rene Zarwel
 * Date: 14.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface RouteClient {

  String BASE_PATH = "BikeBattleBackend/routes";


  //Route Endpoints

  @POST(BASE_PATH)
  Observable<Response<Void>> create(@Header("Authorization")String token, @Body RouteDto entity);

  @PUT(BASE_PATH)
  @Headers("Content-Type: application/json")
  Observable<Void> update(@Header("Authorization")String token, @Body RouteDto entity);

  @DELETE(BASE_PATH + "/{id}")
  Observable<Void> delete(@Header("Authorization")String token, @Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  Observable<Resource<RouteDto>> findeOne(@Header("Authorization")String token, @Path("id") String id);

  @GET(BASE_PATH + "")
  Observable<Resources<Resource<RouteDto>>> findAll(@Header("Authorization")String token);

  @GET(BASE_PATH + "/search/findByName")
  Observable<Resources<Resource<RouteDto>>>  findByName(@Header("Authorization")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  Observable<Resources<Resource<RouteDto>>>  findByNameContainingIgnoreCase(@Header("Authorization")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByOwnerOid")
  Observable<Resources<Resource<RouteDto>>>  findByOwnerOid(@Header("Authorization")String token, @Query("oid") String oid);

  @GET(BASE_PATH + "/search/findByDifficulty")
  Observable<Resources<Resource<RouteDto>>>  findByDifficulty(@Header("Authorization")String token, @Query("difficulty") Difficulty difficulty);

  @GET(BASE_PATH + "/search/findByRoutetype")
  Observable<Resources<Resource<RouteDto>>>  findByRoutetype(@Header("Authorization")String token, @Query("routetype") Routetyp routetyp);

  @GET(BASE_PATH + "/search/findNear")
  Observable<Resources<Resource<RouteDto>>>  findNear(
      @Header("Authorization")String token,
      @Query("longitude") double longitude,
      @Query("latitude") double latitude,
      @Query("r") double r);

  //Relation Endpoints

  @PUT(BASE_PATH + "/{id}/owner")
  @Headers("Content-Type: text/uri-list")
  Observable<Void> setOwner(@Header("Authorization")String token, @Path("id") String id, @Body String owner);

  @GET(BASE_PATH + "/{id}/owner")
  Observable<Resource<UserDto>>  getOwner(@Header("Authorization")String token, @Path("id") String id);


}
