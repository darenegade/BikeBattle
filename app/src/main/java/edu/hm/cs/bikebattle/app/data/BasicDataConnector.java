package edu.hm.cs.bikebattle.app.data;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nils on 03.05.2016.
 * Basic implementation for a connection to the backend.
 * No error handling!
 *
 * @author Nils Bernhardt
 * @version 1.0
 */
public class BasicDataConnector implements DataConnector {
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
   * Adress of the server. Remove.
   */
  private final String address = "https://moan.cs.hm.edu:8443/BikeBattleBackend/users/"; //TODO remove
  /**
   * Activity to run consumer methods on.
   */
  private final Activity activity;

  /**
   * Creates the clients for the backend.
   *
   * @param activity Activity to run consumer tasks on.
   */
  public BasicDataConnector(Activity activity) {
    this.activity = activity;
    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();
    driveClient = ClientFactory.getDriveClient();
  }

  /**
   * Checks the Http code. If the Code is not successful the consumer is notified.
   *
   * @param code     http code
   * @param consumer to notify
   * @return true if successful
   */
  private boolean checkHttpCode(int code, Consumer consumer) {
    if (code / 100 == 2) {
      return true;
    } else {
      runErrorOnUiThread(consumer, code, null);
      return false;
    }
  }

  /**
   * Runs the consumer consume method on the UI thread of the activity.
   *
   * @param consumer consumer
   * @param argument argument for consume
   * @param <T>      Consumer value
   */
  private <T> void runConsumerOnUiThread(final Consumer<T> consumer, final T argument) {
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        consumer.consume(argument);
      }
    });
  }

  /**
   * Runs the error method of the consumer on the activity UI thread.
   *
   * @param consumer  consumer
   * @param code      error code
   * @param exception thrown exception
   */
  private void runErrorOnUiThread(final Consumer consumer, final int code,
                                  final IOException exception) {
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        consumer.error(code, exception);
      }
    });
  }

  /**
   * Converts the dto to bean.
   *
   * @param dto to convert.
   * @return converted object
   */
  private Object toBean(BaseDto dto) {
    if (dto.getClass().equals(RouteDto.class)) {
      return RouteAssembler.toBean((RouteDto) dto);
    } else if (dto.getClass().equals(DriveDto.class)) {
      return TrackAssembler.toBean((DriveDto) dto);
    } else if (dto.getClass().equals(UserDto.class)) {
      return UserAssembler.toBean((UserDto) dto);
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
    new Thread() {
      @Override
      public void run() {
        try {
          Response<Resources<Resource<T>>> response = call.execute();
          if (checkHttpCode(response.code(), consumer)) {
            List<V> list = new LinkedList<V>();
            Collection<Resource<T>> resources = response.body().getContent();
            for (Resource<T> resource : resources) {
              list.add((V) toBean(resource.getContent()));
            }
            runConsumerOnUiThread(consumer, list);
          }
        } catch (IOException exception) {
          runErrorOnUiThread(consumer, Consumer.IO_EXCEPTION, exception);
        }
      }
    }.start();
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
    new Thread() {
      @Override
      public void run() {
        try {
          Response<Resource<T>> response = call.execute();
          if (checkHttpCode(response.code(), consumer)) {
            runConsumerOnUiThread(consumer, (V) toBean(response.body().getContent()));
          }
        } catch (IOException exception) {
          runErrorOnUiThread(consumer, Consumer.IO_EXCEPTION, exception);
        }
      }
    }.start();
  }

  /**
   * Executes a write call.
   *
   * @param call     to execute
   * @param consumer for errors
   */
  private void executeWriteCall(final Call<Void> call, final Consumer consumer) {
    new Thread() {
      @Override
      public void run() {
        try {
          Log.d("Call", call.toString());
          checkHttpCode(call.execute().code(), consumer);
        } catch (IOException exception) {
          runErrorOnUiThread(consumer, Consumer.IO_EXCEPTION, exception);
        }
      }
    }.start();
  }

  @Override
  public void getRoutesByLocation(final Location location, final float distance,
                                  final Consumer<List<Route>> consumer) {
    executeGetListCall(routeClient.findNear(location.getLongitude(),
        location.getLatitude(), distance), consumer);
  }

  @Override
  public void getUserById(final String id, final Consumer<User> consumer) {
    executeGetCall(userClient.findeOne(id), consumer);
  }

  @Override
  public void getUserByName(String name, Consumer<List<User>> consumer) {
    executeGetListCall(userClient.findByNameContainingIgnoreCase(name), consumer);
  }

  @Override
  public void getTracksByUser(User user, Consumer<List<Track>> consumer) {
    executeGetListCall(driveClient.findByOwnerOid(user.getOid()), consumer);
  }

  @Override
  public void getRoutesByUser(User user, Consumer<List<Route>> consumer) {
    executeGetListCall(routeClient.findByOwnerOid(user.getOid()), consumer);
  }

  @Override
  public void addTrack(Track track, User owner, Consumer consumer) {
    DriveDto driveDto = TrackAssembler.toDto(track);
    driveDto.setOwner(address + owner.getOid().toString()); //TODO rene backend
    executeWriteCall(driveClient.create(driveDto), consumer);
  }

  @Override
  public void deleteTrack(Track track, Consumer consumer) {
    executeWriteCall(driveClient.delete(track.getOid()), consumer);
  }

  @Override
  public void addRoute(Route route, User user, Consumer consumer) {
    RouteDto routeDto = RouteAssembler.toDto(route);
    routeDto.setOwner(address + user.getOid()); //TODO rene backend
    executeWriteCall(routeClient.create(routeDto), consumer);
  }

  @Override
  public void deleteRoute(Route route, Consumer consumer) {
    executeWriteCall(routeClient.delete(route.getOid()), consumer);
  }

  @Override
  public void createUser(User user, Consumer consumer) {
    executeWriteCall(userClient.create(UserAssembler.toDto(user)), consumer);
  }

  @Override
  public void changeUserData(User user, Consumer consumer) {
    executeWriteCall(userClient.update(user.getOid(), UserAssembler.toDto(user)), consumer);
  }

  @Override
  public void addFriend(User user, User friend, Consumer consumer) {
    executeWriteCall(userClient.addFriend(user.getOid(), friend.getOid().toString()), consumer);
  }

  @Override
  public void getFriends(User user, Consumer<List<User>> consumer) {
    executeGetListCall(userClient.getFriends(user.getOid()), consumer);
  }

  @Override
  public void getAllRoutes(final Consumer<List<Route>> consumer) {
    executeGetListCall(routeClient.findAll(), consumer);
  }
}
