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
import edu.hm.cs.bikebattle.app.api.domain.TopDriveEntryDto;
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
import io.rx_cache.EvictProvider;
import io.rx_cache.Reply;
import okhttp3.Cache;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nils on 03.05.2016.
 * Basic implementation for a connection to the backend.
 * No error handling!
 *
 * @author Nils Bernhardt, René Zarwel
 * @version 1.0
 */
public class CachingDataConnector implements DataConnector {

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
  public CachingDataConnector(Context context, GoogleApiClient client) {
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
   * If the bean is not known, the dto is returned.
   *
   * @param dto to convert.
   * @return converted object
   */
  @SuppressWarnings("unchecked")
  private static <V> V toBean(Object dto) {
    if(dto == null){
      return null;
    }
    if (dto.getClass().equals(RouteDto.class)) {
      return (V) RouteAssembler.toBean((RouteDto) dto);
    } else if (dto.getClass().equals(DriveDto.class)) {
      return (V) TrackAssembler.toBean((DriveDto) dto);
    } else if (dto.getClass().equals(UserDto.class)) {
      return (V) UserAssembler.toBean((UserDto) dto);
    } else {
      return (V) dto;
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
  private <T, V> void executeGetListCall(final Observable<Reply<List<T>>> observable,
                                                         final Consumer<List<V>> consumer) {

    observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Reply<List<T>>>() {

          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            consumer.error(-1, e);
          }

          @Override
          public void onNext(Reply<List<T>> reply) {

            LinkedList<V> buffer = new LinkedList<V>();

            for (T entry : reply.getData()) {
              buffer.add(CachingDataConnector.<V>toBean(entry));
            }

            consumer.consume(buffer);
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
  private <T extends BaseDto, V> void executeGetCall(final Observable<Reply<T>> observable,
                                                     final Consumer<V> consumer) {

    observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Reply<T>>() {

          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            consumer.error(Consumer.EXCEPTION, e);
          }

          @Override
          public void onNext(Reply<T> reply) {
            consumer.consume(CachingDataConnector.<V>toBean(reply.getData()));
          }
        });
  }

  /**
   * Executes a write call.
   *
   * @param observable to execute
   * @param consumer   for errors
   */
  private void executeCreateCall(final Observable<String> observable, final Consumer<String> consumer) {

    observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            consumer.error(Consumer.EXCEPTION, e);
          }

          @Override
          public void onNext(String reply) {
            consumer.consume(reply);
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

  /**
   * Login Method with Cache implementation see {@link #login(String, Consumer)}
   */
  public void login(final String email, final Consumer<User> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetCall(
            userCache.findByEmail(userClient.findByEmail(input, email).map(new Func1<Resource<UserDto>, UserDto>() {
              @Override
              public UserDto call(Resource<UserDto> userDtoResource) {
                return userDtoResource.getContent();
              }
            }), new DynamicKey(email), new EvictDynamicKey(refresh)),
            consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void login(final String email, final Consumer<User> consumer) {
    login(email,consumer,false);
  }

  /**
   * Get Routes by Location Method with Cache implementation see {@link #getRoutesByLocation(Location, float, Consumer)}
   */
  public void getRoutesByLocation(final Location location, final float distance,
                                  final Consumer<List<Route>> consumer, boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            routeCache.findNear(
                routeClient.findNear(input, location.getLongitude(), location.getLatitude(), distance).map(new Func1<Resources<Resource<RouteDto>>, List<RouteDto>>() {
                  @Override
                  public List<RouteDto> call(Resources<Resource<RouteDto>> resources) {
                    LinkedList<RouteDto> buffer = new LinkedList<RouteDto>();
                    for (Resource<RouteDto> routeDtoResource : resources.getContent()) {
                      buffer.add(routeDtoResource.getContent());
                    }
                    return buffer;
                  }
                }),
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
  public void getRoutesByLocation(final Location location, final float distance,
                                  final Consumer<List<Route>> consumer) {
    getRoutesByLocation(location, distance, consumer, false);
  }

  /**
   * Get User by Id Method with Cache implementation see {@link #getUserById(String, Consumer)}
   */
  public void getUserById(final String id, final Consumer<User> consumer, final boolean refresh) {
    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetCall(
            userCache.findeOne(
                userClient.findeOne(input, id).map(new Func1<Resource<UserDto>, UserDto>() {
                  @Override
                  public UserDto call(Resource<UserDto> userDtoResource) {
                    return userDtoResource.getContent();
                  }
                }),
                new DynamicKey(id),
                new EvictDynamicKey(refresh)),
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
    getUserById(id, consumer, false);
  }

  /**
   * Get User by Name Method with Cache implementation see {@link #getUserByName(String, Consumer)}
   */
  public void getUserByName(final String name, final Consumer<List<User>> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            userCache.findByNameContainingIgnoreCase(
                userClient.findByNameContainingIgnoreCase(input, name).map(new Func1<Resources<Resource<UserDto>>, List<UserDto>>() {
                  @Override
                  public List<UserDto> call(Resources<Resource<UserDto>> resources) {
                    LinkedList<UserDto> buffer = new LinkedList<UserDto>();
                    for (Resource<UserDto> userDtoResource : resources.getContent()) {
                      buffer.add(userDtoResource.getContent());
                    }
                    return buffer;
                  }
                }),
                new DynamicKey(name),
                new EvictDynamicKey(refresh)),
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
    getUserByName(name, consumer, false);
  }

  /**
   * Get Tracks by User Method with Cache implementation see {@link #getTracksByUser(User, Consumer)}
   */
  public void getTracksByUser(final User user, final Consumer<List<Track>> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            driveCache.findByOwnerOid(
                driveClient.findByOwnerOid(input, user.getOid()).map(new Func1<Resources<Resource<DriveDto>>, List<DriveDto>>() {
                  @Override
                  public List<DriveDto> call(Resources<Resource<DriveDto>> resources) {
                    LinkedList<DriveDto> buffer = new LinkedList<DriveDto>();
                    for (Resource<DriveDto> resource : resources.getContent()) {
                      buffer.add(resource.getContent());
                    }
                    return buffer;
                  }
                }),
                new DynamicKey(user.getOid()),
                new EvictDynamicKey(refresh)),
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
    getTracksByUser(user, consumer, false);
  }

  /**
   * Get Top20 of route Method with Cache implementation see {@link #getTopTwentyOfRoute(Route, Consumer)}
   */
  public void getTopTwentyOfRoute(final Route route, final Consumer<List<TopDriveEntryDto>> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            driveCache.topTwentyOfRoute(
                driveClient.topTwentyOfRoute(input, route.getOid()).map(new Func1<Resources<Resource<TopDriveEntryDto>>, List<TopDriveEntryDto>>() {
                  @Override
                  public List<TopDriveEntryDto> call(Resources<Resource<TopDriveEntryDto>> resources) {
                    LinkedList<TopDriveEntryDto> buffer = new LinkedList<TopDriveEntryDto>();
                    for (Resource<TopDriveEntryDto> resource : resources.getContent()) {
                      buffer.add(resource.getContent());
                    }
                    return buffer;
                  }
                }),
                new DynamicKey(route.getOid()),
                new EvictDynamicKey(refresh)),
            consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getTopTwentyOfRoute(final Route route, final Consumer<List<TopDriveEntryDto>> consumer) {
    getTopTwentyOfRoute(route, consumer, false);
  }

  /**
   * Get routes by user Method with Cache implementation see {@link #getRoutesByUser(User, Consumer)}
   */
  public void getRoutesByUser(final User user, final Consumer<List<Route>> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            routeCache.findByOwnerOid(
                routeClient.findByOwnerOid(input, user.getOid()).map(new Func1<Resources<Resource<RouteDto>>, List<RouteDto>>() {
                  @Override
                  public List<RouteDto> call(Resources<Resource<RouteDto>> resources) {
                    LinkedList<RouteDto> buffer = new LinkedList<RouteDto>();
                    for (Resource<RouteDto> resource : resources.getContent()) {
                      buffer.add(resource.getContent());
                    }
                    return buffer;
                  }
                }),
                new DynamicKey(user.getOid()),
                new EvictDynamicKey(refresh)),
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
    getRoutesByUser(user, consumer, false);
  }

  @Override
  public void addTrack(final Track track, final User owner, final Consumer<String> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeCreateCall(
            driveClient.create(input, TrackAssembler.toDto(track)).map(new Func1<Response<Void>, String>() {
              @Override
              public String call(Response<Void> voidResponse) {
                return voidResponse.headers().get("Location");
              }
            }),
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
  public void addRoute(final Route route, final User user, final Consumer<String> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeCreateCall(routeClient.create(input, RouteAssembler.toDto(route)).map(new Func1<Response<Void>, String>() {
          @Override
          public String call(Response<Void> voidResponse) {
            return voidResponse.headers().get("Location");
          }
        }), consumer);
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
  public void createUser(final User user, final Consumer<String> consumer) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeCreateCall(userClient.create(input, UserAssembler.toDto(user)).map(new Func1<Response<Void>, String>() {
          @Override
          public String call(Response<Void> voidResponse) {
            return voidResponse.headers().get("Location");
          }
        }), consumer);
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

  /**
   * Get friends of user Method with Cache implementation see {@link #getFriends(User, Consumer)}
   */
  public void getFriends(final User user, final Consumer<List<User>> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            userCache.getFriends(
                userClient.getFriends(input, user.getOid()).map(new Func1<Resources<Resource<UserDto>>, List<UserDto>>() {
                  @Override
                  public List<UserDto> call(Resources<Resource<UserDto>> resources) {
                    LinkedList<UserDto> buffer = new LinkedList<UserDto>();
                    for (Resource<UserDto> resource : resources.getContent()) {
                      buffer.add(resource.getContent());
                    }
                    return buffer;
                  }
                }),
                new DynamicKey(user.getOid()),
                new EvictDynamicKey(refresh)),
            consumer);
      }

      @Override
      public void error(int error, Throwable exception) {
        consumer.error(error, exception);
      }
    });
  }

  @Override
  public void getFriends(final User user, final Consumer<List<User>> consumer) {
    getFriends(user, consumer, false);
  }

  /**
   * Get All saved routes Method with Cache implementation see {@link #getAllRoutes(Consumer)}
   */
  public void getAllRoutes(final Consumer<List<Route>> consumer, final boolean refresh) {

    generateToken(new Consumer<String>() {
      @Override
      public void consume(String input) {
        executeGetListCall(
            routeCache.findAll(
                routeClient.findAll(input).map(new Func1<Resources<Resource<RouteDto>>, List<RouteDto>>() {
                  @Override
                  public List<RouteDto> call(Resources<Resource<RouteDto>> resources) {
                    LinkedList<RouteDto> buffer = new LinkedList<RouteDto>();
                    for (Resource<RouteDto> resource : resources.getContent()) {
                      buffer.add(resource.getContent());
                    }
                    return buffer;
                  }
                }), new EvictProvider(refresh)),
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
    getAllRoutes(consumer, false);
  }
}
