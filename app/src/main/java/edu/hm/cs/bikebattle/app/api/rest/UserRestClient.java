package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class UserRestClient extends RestClient<UserResource> {

  /**
   * Builder of the RestClient.
   */
  public static class Builder {

    /**
     * Singleton of RestClient.
     **/
    private static UserRestClient client;

    /**
     * Builds a new RestClient.
     * @param restTemplate Template to use
     * @return new Client
     */
    public static UserRestClient build(RestTemplate restTemplate) {

      if (restTemplate == null) {
        return null;
      }

      client = new UserRestClient(restTemplate);
      return client;
    }

    public static UserRestClient build() {
      return client;
    }
  }

  private static final String BASE_RELATION = "Users";

  private UserRestClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  public UserResource create(UserResource user) {

    URI uri = URI.create(
        getTraverson().follow(BASE_RELATION).asLink().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.POST,
        new HttpEntity<UserDto>(user.getContent()),
        UserResource.class
    ).getBody();
  }

  @Override
  public UserResource update(UserResource user) {

    URI uri = URI.create(user.getId().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.PUT,
        new HttpEntity<UserDto>(user.getContent()),
        UserResource.class
    ).getBody();
  }

  @Override
  public void delete(Link id) {

    URI uri = URI.create(id.getHref());
    getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
  }

  @Override
  public Collection<UserResource> findAll() {
    return getTraverson()
        .follow(BASE_RELATION)
        .toObject(UserResource.LIST).getContent();
  }

  @Override
  public Collection<UserResource> findAll(Link relation) {
    URI uri = URI.create(relation.getHref());
    return getRestTemplate()
        .exchange(uri, HttpMethod.GET, null, UserResource.LIST)
        .getBody()
        .getContent();
  }

  @Override
  public UserResource findOne(Link link) {
    URI uri = URI.create(link.getHref());

    UserResource resource;

    try {
      resource = getRestTemplate().exchange(
          uri,
          HttpMethod.GET,
          null,
          UserResource.class
      ).getBody();

    } catch (HttpClientErrorException exception) {
      final HttpStatus statusCode = exception.getStatusCode();
      if (!HttpStatus.NOT_FOUND.equals(statusCode)) {
        throw exception;
      }
      if (Link.REL_SELF.equals(link.getRel())) {
        throw exception;
      }
      return null;
    }

    return resource;
  }

}
