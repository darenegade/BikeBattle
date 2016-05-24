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
  private void generateToken(final Consumer<String> tokenConsumer) {
    Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {
      @Override
      public void onResult(@NonNull GoogleSignInResult googleSignInResult) {

        if (googleSignInResult.isSuccess() && googleSignInResult.getSignInAccount() != null) {
          tokenConsumer.consume(googleSignInResult.getSignInAccount().getIdToken());
        } else {
          tokenConsumer.error(googleSignInResult.getStatus().getStatusCode(), null);
        }
      }
    });
  }

  @Override
  public void login(final String email, final Consumer<User> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetCall(userClient.findByEmail(input, email), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getRoutesByLocation(final Location location, final float distance,
                                  final Consumer<List<Route>> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(routeClient.findNear(input, location.getLongitude(),
            location.getLatitude(), distance), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getUserById(final String id, final Consumer<User> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetCall(userClient.findeOne(input, id), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getUserByName(final String name, final Consumer<List<User>> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(userClient.findByNameContainingIgnoreCase(input, name), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getTracksByUser(final User user, final Consumer<List<Track>> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(driveClient.findByOwnerOid(input, user.getOid()), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getRoutesByUser(final User user, final Consumer<List<Route>> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(routeClient.findByOwnerOid(input, user.getOid()), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void addTrack(final Track track, final User owner, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(driveClient.create(input, TrackAssembler.toDto(track)), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void deleteTrack(final Track track, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(driveClient.delete(input, track.getOid()), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void addRoute(final Route route, final User user, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(routeClient.create(input, RouteAssembler.toDto(route)), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void deleteRoute(final Route route, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(routeClient.delete(input, route.getOid()), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void createUser(final User user, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(userClient.create(input, UserAssembler.toDto(user)), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void changeUserData(final User user, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(userClient.update(input, user.getOid(), UserAssembler.toDto(user)), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void addFriend(final User user, final User friend, final Consumer<Void> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeWriteCall(userClient.addFriend(input, user.getOid(), friend.getOid()), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getFriends(final User user, final Consumer<List<User>> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(userClient.getFriends(input, user.getOid()), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getAllRoutes(final Consumer<List<Route>> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(routeClient.findAll(input), consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }
}
