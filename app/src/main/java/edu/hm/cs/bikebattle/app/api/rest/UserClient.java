package edu.hm.cs.bikebattle.app.api.rest;


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
public interface UserClient {

  String BASE_PATH = "BikeBattleBackend/users";

  //User Endpoints

  @POST(BASE_PATH)
  @Headers("Authorization: Bearer {token}")
  Call<Void>  create(@Header("token")String token, @Body UserDto entity);

  @PUT(BASE_PATH + "/{id}")
  @Headers({
      "Authorization: Bearer {token}",
      "Content-Type: application/json"})
  Call<Void>  update(@Header("token")String token, @Path("id") String id, @Body UserDto entity);

  @DELETE(BASE_PATH + "/{id}")
  @Headers("Authorization: Bearer {token}")
  Call<Void> delete(@Header("token")String token, @Path("id") String id);

  @GET(BASE_PATH + "/{id}")
  @Headers("Authorization: Bearer {token}")
  Call<Resource<UserDto>> findeOne(@Header("token")String token, @Path("id") String id);

  @GET(BASE_PATH + "")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<UserDto>>> findAll(@Header("token")String token);

  @GET(BASE_PATH + "/search/findByName")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<UserDto>>>  findByName(@Header("token")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByNameContainingIgnoreCase")
  @Headers("Authorization: Bearer {token}")
  Call<Resources<Resource<UserDto>>>  findByNameContainingIgnoreCase(@Header("token")String token, @Query("name") String name);

  @GET(BASE_PATH + "/search/findByEmail")
  @Headers("Authorization: Bearer {token}")
  Call<Resource<UserDto>> findByEmail(@Header("token")String token, @Query("email") String email);

  //Relation Endpoints

  @POST(BASE_PATH + "/{id}/friends")
  @Headers({
      "Authorization: Bearer {token}",
      "Content-Type: text/uri-list"})
  Call<Void> addFriend(@Header("token")String token, @Path("id") String id, @Body String friend);

  @GET(BASE_PATH + "/{id}/friends")
  Call<Resources<Resource<UserDto>>>  getFriends(@Header("token")String token, @Path("id") String id);

}
