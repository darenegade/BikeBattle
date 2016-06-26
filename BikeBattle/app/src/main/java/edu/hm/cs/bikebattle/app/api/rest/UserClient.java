package edu.hm.cs.bikebattle.app.api.rest;


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
 * Author(s): Rene Zarwel
 * Date: 14.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public interface UserClient {

  String BASE_PATH = "BikeBattleBackend/users";

  //User Endpoints

  @POST(BASE_PATH)
  Observable<Response<Void>>  create(@Header("Authorization")String token, @Body UserDto entity);

  @PUT(BASE_PATH + "/{id}")
  @Headers("Content-Type: application/json")
  Observable<Void>  update(@Header("Authorization")String token, @Path("id") String id, @Body UserDto entity);

  @DELETE(BASE_PATH + "/{id}")
  Observable<Void> delete(@Header("Authorization")String token, @Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  Observable<Resource<UserDto>> findeOne(@Header("Authorization")String token, @Path("id") String id);

  @GET(BASE_PATH + "")
  Observable<Resources<Resource<UserDto>>> findAll(@Header("Authorization")String token);

  @GET(BASE_PATH + "/search/findByName")
  Observable<Resources<Resource<UserDto>>>  findByName(@Header("Authorization")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  Observable<Resources<Resource<UserDto>>>  findByNameContainingIgnoreCase(@Header("Authorization")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByEmail")
  Observable<Resource<UserDto>> findByEmail(@Header("Authorization")String token, @Query("email") String email);

  //Relation Endpoints

  @POST(BASE_PATH + "/{id}/friends")
  @Headers("Content-Type: text/uri-list")
  Observable<Void> addFriend(@Header("Authorization")String token, @Path("id") String id, @Body String friend);

  @GET(BASE_PATH + "/{id}/friends")
  Observable<Resources<Resource<UserDto>>>  getFriends(@Header("Authorization")String token, @Path("id") String id);

}
