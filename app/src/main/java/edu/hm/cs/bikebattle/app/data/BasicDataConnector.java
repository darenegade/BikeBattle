package edu.hm.cs.bikebattle.app.data;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import edu.hm.cs.bikebattle.app.api.domain.BaseDto;
import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.DriveClient;
import edu.hm.cs.bikebattle.app.api.rest.RouteClient;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;
import edu.hm.cs.bikebattle.app.modell.assembler.RouteAssembler;
import edu.hm.cs.bikebattle.app.modell.assembler.TrackAssembler;
import edu.hm.cs.bikebattle.app.modell.assembler.UserAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nils on 03.05.2016.
 * Basic implementation for a connection to the backend.
 * No error handling!
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class BasicDataConnector implements DataConnector {

  public static final String TAG = "DataConnector";
  /**
   * Backend user client.
   */
  private final UserClient userClient;
  /**
   * Backend route client.
   */
  private final RouteClient routeClient;
  /**
   * Backend drive/track client.
   */
  private final DriveClient driveClient;

  /**
   * Client to get IDToken for Backend Auth.
   */
  private final GoogleApiClient googleApiClient;

  /**
   * Current maybe valid token.
   */
  private String currentToken;
  /**
   * Adress of the server. Remove.
   */
  private final String address = "https://moan.cs.hm.edu:8443/BikeBattleBackend/users/"; //TODO remove

  /**
   * Creates the clients for the backend.
   */
  public BasicDataConnector(GoogleApiClient client) {
    googleApiClient = client;
    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();
    driveClient = ClientFactory.getDriveClient();
  }

  /**
   * Converts the dto to bean.
   *
   * @param dto to convert.
   * @return converted object
   */
  @SuppressWarnings("unchecked")
  private static <V> V toBean(BaseDto dto) {
    if (dto.getClass().equals(RouteDto.class)) {
      return (V) RouteAssembler.toBean((RouteDto) dto);
    } else if (dto.getClass().equals(DriveDto.class)) {
      return (V) TrackAssembler.toBean((DriveDto) dto);
    } else if (dto.getClass().equals(UserDto.class)) {
      return (V) UserAssembler.toBean((UserDto) dto);
    } else {
      throw new IllegalArgumentException("Unknown dto type.");
    }
  }

  /**
   * Executes a get call for a List of resources.
   *
   * @param call     to execute
   * @param consumer consumer for result
   * @param <T>      dto
   * @param <V>      bean
   */
  private <T extends BaseDto, V> void executeGetListCall(final Call<Resources<Resource<T>>> call,
                                                         final Consumer<List<V>> consumer) {

    call.enqueue(new Callback<Resources<Resource<T>>>() {
      @Override
      public void onResponse(Call<Resources<Resource<T>>> call, Response<Resources<Resource<T>>> response) {

        if (response.isSuccessful()) {

          List<V> list = new LinkedList<V>();
          Collection<Resource<T>> resources = response.body().getContent();

          for (Resource<T> resource : resources) {
            list.add(BasicDataConnector.<V>toBean(resource.getContent()));
          }
          consumer.consume(list);
        } else {

          try {
            Log.d(TAG, response.errorBody().string());
          } catch (IOException e) {
            Log.e(TAG, "ERROR BODY NOT READABLE");
          }
          consumer.error(response.code(), null);
        }
      }

      @Override
      public void onFailure(Call<Resources<Resource<T>>> call, Throwable t) {
        consumer.error(-1, t);
      }
    });
  }

  /**
   * Executes a get call for a resource.
   *
   * @param call     to execute
   * @param consumer consumer for result
   * @param <T>      dto
   * @param <V>      bean
   */
  private <T extends BaseDto, V> void executeGetCall(final Call<Resource<T>> call,
                                                     final Consumer<V> consumer) {

    call.enqueue(new Callback<Resource<T>>() {
      @Override
      public void onResponse(Call<Resource<T>> call, Response<Resource<T>> response) {

        if (response.isSuccessful()) {

          consumer.consume(BasicDataConnector.<V>toBean(response.body().getContent()));

        } else {
          try {
            Log.d(TAG, response.errorBody().string());
          } catch (IOException e) {
            Log.e(TAG, "ERROR BODY NOT READABLE");
          }
          consumer.error(response.code(), null);
        }
      }

      @Override
      public void onFailure(Call<Resource<T>> call, Throwable t) {
        consumer.error(Consumer.EXCEPTION, t);
      }
    });
  }

  /**
   * Executes a write call.
   *
   * @param call     to execute
   * @param consumer for errors
   */
  private void executeWriteCall(final Call<Void> call, final Consumer<Void> consumer) {

    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {

        if (response.isSuccessful()) {

          consumer.consume(null);

        } else {

          try {
            Log.d(TAG, response.errorBody().string());
          } catch (IOException e) {
            Log.e(TAG, "ERROR BODY NOT READABLE");
          }
          consumer.error(response.code(), null);

        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable t) {
        consumer.error(Consumer.EXCEPTION, t);
      }
    });

  }

  /**
   * Get new valid token and set is as current.
   *
   * @return current valid token;
   */
  private String refreshAndGetToken() {
    Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {
      @Override
      public void onResult(@NonNull GoogleSignInResult googleSignInResult) {

        if (googleSignInResult.isSuccess() && googleSignInResult.getSignInAccount() != null) {
          currentToken = googleSignInResult.getSignInAccount().getIdToken();
        } else {
          //TODO Do something clever here.
        }
      }
    });
    return currentToken;
  }

  @Override
  public void login(String email, Consumer<User> consumer) {
    executeGetCall(userClient.findByEmail(refreshAndGetToken(), email), consumer);
  }

  @Override
  public void getRoutesByLocation(final Location location, final float distance,
                                  final Consumer<List<Route>> consumer) {

    executeGetListCall(routeClient.findNear(refreshAndGetToken(), location.getLongitude(),
        location.getLatitude(), distance), consumer);
  }

  @Override
  public void getUserById(final String id, final Consumer<User> consumer) {
    executeGetCall(userClient.findeOne(refreshAndGetToken(), id), consumer);
  }

  @Override
  public void getUserByName(String name, Consumer<List<User>> consumer) {
    executeGetListCall(userClient.findByNameContainingIgnoreCase(refreshAndGetToken(), name), consumer);
  }

  @Override
  public void getTracksByUser(User user, Consumer<List<Track>> consumer) {
    executeGetListCall(driveClient.findByOwnerOid(refreshAndGetToken(), user.getOid()), consumer);
  }

  @Override
  public void getRoutesByUser(User user, Consumer<List<Route>> consumer) {
    executeGetListCall(routeClient.findByOwnerOid(refreshAndGetToken(), user.getOid()), consumer);
  }

  @Override
  public void addTrack(Track track, User owner, Consumer<Void> consumer) {
    executeWriteCall(driveClient.create(refreshAndGetToken(), TrackAssembler.toDto(track)), consumer);
  }

  @Override
  public void deleteTrack(Track track, Consumer<Void> consumer) {
    executeWriteCall(driveClient.delete(refreshAndGetToken(), track.getOid()), consumer);
  }

  @Override
  public void addRoute(Route route, User user, Consumer<Void> consumer) {
    executeWriteCall(routeClient.create(refreshAndGetToken(), RouteAssembler.toDto(route)), consumer);
  }

  @Override
  public void deleteRoute(Route route, Consumer<Void> consumer) {
    executeWriteCall(routeClient.delete(refreshAndGetToken(), route.getOid()), consumer);
  }

  @Override
  public void createUser(User user, Consumer<Void> consumer) {
    executeWriteCall(userClient.create(refreshAndGetToken(), UserAssembler.toDto(user)), consumer);
  }

  @Override
  public void changeUserData(User user, Consumer<Void> consumer) {
    executeWriteCall(userClient.update(refreshAndGetToken(), user.getOid(), UserAssembler.toDto(user)), consumer);
  }

  @Override
  public void addFriend(User user, User friend, Consumer<Void> consumer) {
    executeWriteCall(userClient.addFriend(refreshAndGetToken(), user.getOid(), friend.getOid()), consumer);
  }

  @Override
  public void getFriends(User user, Consumer<List<User>> consumer) {
    executeGetListCall(userClient.getFriends(refreshAndGetToken(), user.getOid()), consumer);
  }

  @Override
  public void getAllRoutes(final Consumer<List<Route>> consumer) {
    executeGetListCall(routeClient.findAll(refreshAndGetToken()), consumer);
  }
}
