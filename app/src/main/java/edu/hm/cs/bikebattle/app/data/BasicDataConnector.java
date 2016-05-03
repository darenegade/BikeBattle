package edu.hm.cs.bikebattle.app.data;

import android.location.Location;

import org.springframework.hateoas.Resource;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import edu.hm.cs.bikebattle.app.api.rest.DriveClient;
import edu.hm.cs.bikebattle.app.api.rest.RouteClient;
import edu.hm.cs.bikebattle.app.api.rest.UserClient;
import edu.hm.cs.bikebattle.app.modell.Route;
import edu.hm.cs.bikebattle.app.modell.Track;
import edu.hm.cs.bikebattle.app.modell.User;
import edu.hm.cs.bikebattle.app.modell.assembler.UserAssembler;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Nils on 03.05.2016.
 */
public class BasicDataConnector implements DataConnector {

  private final UserClient userClient;

  private final RouteClient routeClient;

  private final DriveClient driveClient;

  public BasicDataConnector(){
    userClient = ClientFactory.getUserClient();
    routeClient = ClientFactory.getRouteClient();
    driveClient = ClientFactory.getDriveClient();
  }

  @Override
  public Route[] getRoutesByLocation(Location location, float distance) {
    return new Route[0];
  }

  @Override
  public User getUserById(UUID id) {
    Call<Resource<UserDto>> call = userClient.findeOne(id);
    try {
      Response<Resource<UserDto>> userDtoResponse = call.execute();
      Resource<UserDto> resource = userDtoResponse.body();
      UserDto userDto = resource.getContent();//check response code (see test cases)
      User user = UserAssembler.toBean(userDto);
      return user;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<User> getUserByName(String name) {
    return null;
  }

  @Override
  public List<Track> getTracksByUser(User user) {
    return null;
  }

  @Override
  public List<Route> getRoutesByUser(User user) {
    return null;
  }

  @Override
  public void addTrack(Track track) {

  }

  @Override
  public void deleteTrack(Track track) {

  }

  @Override
  public void addRoute(Route route) {

  }

  @Override
  public void deleteRoute(Route route) {

  }

  @Override
  public void createUser(User user) {

  }

  @Override
  public void changeUserData(User user) {

  }

  @Override
  public void addFriend(User user, User friend) {

  }

  @Override
  public List<User> getFriends(User user) {
    return null;
  }
}
