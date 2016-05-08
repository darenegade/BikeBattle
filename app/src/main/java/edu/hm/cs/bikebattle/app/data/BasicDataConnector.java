package edu.hm.cs.bikebattle.app.data;

import android.location.Location;

import org.springframework.hateoas.Resource;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
   * Creates the clients for the backend.
   */
  public BasicDataConnector() {
    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();
    driveClient = ClientFactory.getDriveClient();
  }

  @Override
  public Route[] getRoutesByLocation(Location location, float distance) {
    //TODO api missing
    return new Route[0];
  }

  @Override
  public User getUserById(UUID id) {
    Call<Resource<UserDto>> call = userClient.findeOne(id);
    try {
      //TODO check response code (see test cases) and handle errors
      Response<Resource<UserDto>> userDtoResponse = call.execute();
      Resource<UserDto> resource = userDtoResponse.body();
      UserDto userDto = resource.getContent();
      User user = UserAssembler.toBean(userDto);
      return user;
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return null;
  }

  @Override
  public List<User> getUserByName(String name) {
    List<User> users = new LinkedList<User>();
    try {
      //TODO check response code (see test cases) and handle errors
      Collection<Resource<UserDto>> userDtos = userClient.findByNameContainingIgnoreCase(name)
          .execute().body().getContent();
      for (Resource<UserDto> userDtoResource : userDtos) {
        users.add(UserAssembler.toBean(userDtoResource.getContent()));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return users;
  }

  @Override
  public List<Track> getTracksByUser(User user) {
    List<Track> tracks = new LinkedList<Track>();
    try {
      Collection<Resource<DriveDto>> driveDtos = driveClient.findByOwnerOid(user.getOid())
          .execute().body().getContent();
      for (Resource<DriveDto> driveDtoResource : driveDtos) {
        tracks.add(TrackAssembler.toBean(driveDtoResource.getContent()));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return tracks;
  }

  @Override
  public List<Route> getRoutesByUser(User user) {
    List<Route> routes = new LinkedList<Route>();
    try {
      Collection<Resource<RouteDto>> routeDtos = routeClient.findByOwnerOid(user.getOid())
          .execute().body().getContent();
      for (Resource<RouteDto> routeDtoResource : routeDtos) {
        routes.add(RouteAssembler.toBean(routeDtoResource.getContent()));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return routes;
  }

  @Override
  public void addTrack(Track track) {
    try {
      driveClient.create(TrackAssembler.toDto(track)).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void deleteTrack(Track track) {
    try {
      driveClient.delete(track.getOid()).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void addRoute(Route route) {
    try {
      routeClient.create(RouteAssembler.toDto(route)).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void deleteRoute(Route route) {
    try {
      routeClient.delete(route.getOid()).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void createUser(User user) {
    try {
      userClient.create(UserAssembler.toDto(user)).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void changeUserData(User user) {
    try {
      userClient.update(user.getOid(), UserAssembler.toDto(user)).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void addFriend(User user, User friend) {
    try {
      userClient.addFriend(user.getOid(), friend.getOid().toString()).execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public List<User> getFriends(User user) {
    List<User> friends = new LinkedList<User>();
    try {
      Collection<Resource<UserDto>> resources = userClient.getFriends(user.getOid()).execute()
          .body().getContent();
      for (Resource<UserDto> resource : resources) {
        friends.add(UserAssembler.toBean(resource.getContent()));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return friends;
  }
}
