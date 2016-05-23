package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.Difficulty;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.Routetyp;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

  String BASE_PATH = "BikeBattleBackend/routes";


  //Route Endpoints

  @POST(BASE_PATH)
  @Headers("Authorization: Bearer {token}")
  Call<Void> create(@Header("token")String token, @Body RouteDto entity);

  @PUT(BASE_PATH)
  @Headers({
      "Authorization: Bearer {token}",
      "Content-Type: application/json"})
  Call<Void>  update(@Header("token")String token, @Body RouteDto entity);

  @DELETE(BASE_PATH + "/{id}")
  @Headers("Authorization: Bearer {token}")
  Call<Void> delete(@Header("token")String token, @Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  @Headers("Authorization: Bearer {token}")
  Call<Resource<RouteDto>> findeOne(@Header("token")String token, @Path("id") String id);

  @GET(BASE_PATH + "")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>> findAll(@Header("token")String token);

  @GET(BASE_PATH + "/search/findByName")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>>  findByName(@Header("token")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>>  findByNameContainingIgnoreCase(@Header("token")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByOwnerOid")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>>  findByOwnerOid(@Header("token")String token, @Query("oid") String oid);

  @GET(BASE_PATH + "/search/findByDifficulty")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>>  findByDifficulty(@Header("token")String token, @Query("difficulty") Difficulty difficulty);

  @GET(BASE_PATH + "/search/findByRoutetype")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>>  findByRoutetype(@Header("token")String token, @Query("routetype") Routetyp routetyp);

  @GET(BASE_PATH + "/search/findNear")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<RouteDto>>>  findNear(
      @Header("token")String token,
      @Query("longitude") double longitude,
      @Query("latitude") double latitude,
      @Query("r") double r);

  //Relation Endpoints

  @PUT(BASE_PATH + "/{id}/owner")
  @Headers({
      "Authorization: Bearer {token}",
      "Content-Type: text/uri-list"})
  Call<Void> setOwner(@Header("token")String token, @Path("id") String id, @Body String owner);

  @GET(BASE_PATH + "/{id}/owner")
  @Headers("Authorization: Bearer {token}")
  Call<Resource<UserDto>>  getOwner(@Header("token")String token, @Path("id") String id);


}
