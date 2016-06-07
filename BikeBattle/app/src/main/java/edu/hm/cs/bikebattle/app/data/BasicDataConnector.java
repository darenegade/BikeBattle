package edu.hm.cs.bikebattle.app.data;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
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
import edu.hm.cs.bikebattle.app.data.cache.CacheFactory;
import edu.hm.cs.bikebattle.app.data.cache.DriveCache;
import edu.hm.cs.bikebattle.app.data.cache.RouteCache;
import edu.hm.cs.bikebattle.app.data.cache.UserCache;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;
import edu.hm.cs.bikebattle.app.modell.assembler.RouteAssembler;
import edu.hm.cs.bikebattle.app.modell.assembler.TrackAssembler;
import edu.hm.cs.bikebattle.app.modell.assembler.UserAssembler;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import okhttp3.Cache;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
   * Local Cache for Users.
   */
  private final UserCache userCache;
  /**
   * Local Cache for Routes.
   */
  private final RouteCache routeCache;
  /**
   * Local Cache for Drives.
   */
  private final DriveCache driveCache;

  /**
   * Client to get IDToken for Backend Auth.
   */
  private final GoogleApiClient googleApiClient;

  /**
   * Creates the clients for the backend.
   */
  public BasicDataConnector(Context context, GoogleApiClient client) {
    googleApiClient = client;

    //Create a 10MB Cache
    ClientFactory.setCache(new Cache(context.getCacheDir(), 1024 * 1024 * 10));

    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();
    driveClient = ClientFactory.getDriveClient();

    userCache = CacheFactory.getUserCache(context.getCacheDir());
    routeCache = CacheFactory.getRouteCache(context.getCacheDir());
    driveCache = CacheFactory.getDriveCache(context.getCacheDir());
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
   * @param observable to execute
   * @param consumer   consumer for result
   * @param <T>        dto
   * @param <V>        bean
   */
  private <T extends BaseDto, V> void executeGetListCall(final Observable<Reply<Resources<Resource<T>>>> observable,
                                                         final Consumer<List<V>> consumer) {

    observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Reply<Resources<Resource<T>>>>() {

          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            consumer.error(-1, e);
          }

          @Override
          public void onNext(Reply<Resources<Resource<T>>> reply) {

            List<V> list = new LinkedList<V>();
            Collection<Resource<T>> resources = reply.getData().getContent();

            for (Resource<T> resource : resources) {
              list.add(BasicDataConnector.<V>toBean(resource.getContent()));
            }
            consumer.consume(list);
          }
        });
  }

  /**
   * Executes a get call for a resource.
   *
   * @param observable to execute
   * @param consumer   consumer for result
   * @param <T>        dto
   * @param <V>        bean
   */
  private <T extends BaseDto, V> void executeGetCall(final Observable<Reply<Resource<T>>> observable,
                                                     final Consumer<V> consumer) {

    observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Reply<Resource<T>>>() {

          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            consumer.error(Consumer.EXCEPTION, e);
          }

          @Override
          public void onNext(Reply<Resource<T>> reply) {
            consumer.consume(BasicDataConnector.<V>toBean(reply.getData().getContent()));
          }
        });
  }

  /**
   * Executes a write call.
   *
   * @param observable to execute
   * @param consumer   for errors
   */
  private void executeWriteCall(final Observable<Void> observable, final Consumer<Void> consumer) {

    observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Void>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            consumer.error(Consumer.EXCEPTION, e);
          }

          @Override
          public void onNext(Void reply) {
            consumer.consume(null);
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
          tokenConsumer.consume("Bearer " + googleSignInResult.getSignInAccount().getIdToken());
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
        executeGetCall(
            userCache.findByEmail(userClient.findByEmail(input, email), new DynamicKey(email), new EvictDynamicKey(true)),
            consumer);
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
        executeGetListCall(
            routeCache.findNear(
                routeClient.findNear(input, location.getLongitude(), location.getLatitude(), distance),
                new DynamicKey(new double[]{location.getLongitude(), location.getLatitude(), distance}),
                new EvictDynamicKey(true)),
            consumer);
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
        executeGetCall(
            userCache.findeOne(
                userClient.findeOne(input, id),
                new DynamicKey(id),
                new EvictDynamicKey(true)),
            consumer);
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
        executeGetListCall(
            userCache.findByNameContainingIgnoreCase(
                userClient.findByNameContainingIgnoreCase(input, name),
                new DynamicKey(name),
                new EvictDynamicKey(true)),
            consumer);
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
        executeGetListCall(
            driveCache.findByOwnerOid(
                driveClient.findByOwnerOid(input, user.getOid()),
                new DynamicKey(user.getOid()),
                new EvictDynamicKey(true)),
            consumer);
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
        executeGetListCall(
            routeCache.findByOwnerOid(
                routeClient.findByOwnerOid(input, user.getOid()),
                new DynamicKey(user.getOid()),
                new EvictDynamicKey(true)),
            consumer);
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
        executeWriteCall(
            driveClient.create(input, TrackAssembler.toDto(track)),
            consumer);
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
        executeGetListCall(
            userCache.getFriends(
                userClient.getFriends(input, user.getOid()),
                new DynamicKey(user.getOid()),
                new EvictDynamicKey(true)),
            consumer);
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
        executeGetListCall(
            routeCache.findAll(
                routeClient.findAll(input)),
            consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }
}
