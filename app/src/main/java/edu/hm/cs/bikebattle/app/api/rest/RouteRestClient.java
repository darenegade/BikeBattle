package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.RouteDto;
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
public class RouteRestClient extends RestClient<RouteResource> {

  /**
   * Builder of the RestClient.
   */
  public static class Builder {

    /**
     * Singleton of RestClient.
     **/
    private static RouteRestClient client;

    /**
     * Builds a new RestClient.
     * @param restTemplate Template to use
     * @return new Client
     */
    public static RouteRestClient build(RestTemplate restTemplate) {

      if (restTemplate == null) {
        return null;
      }

      client = new RouteRestClient(restTemplate);
      return client;
    }

    public static RouteRestClient build() {
      return client;
    }
  }

  private static final String BASE_RELATION = "Routes";

  private RouteRestClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  public RouteResource create(RouteResource route) {


    URI uri = URI.create(BASEPATH.toString() + BASE_RELATION);

    return getRestTemplate().exchange(
        uri, HttpMethod.POST,
        new HttpEntity<RouteDto>(route.getContent()),
        RouteResource.class
    ).getBody();
  }

  @Override
  public RouteResource update(RouteResource route) {

    URI uri = URI.create(route.getId().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.PUT,
        new HttpEntity<RouteDto>(route.getContent()),
        RouteResource.class
    ).getBody();
  }

  @Override
  public void delete(Link id) {

    URI uri = URI.create(id.getHref());
    getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
  }

  @Override
  public Collection<RouteResource> findAll() {
    URI uri = URI.create(BASEPATH.toString());

    return getRestTemplate().exchange(uri, HttpMethod.GET, null, RouteResource.LIST)
        .getBody().getContent();
  }

  @Override
  public Collection<RouteResource> findAll(Link relation) {
    URI uri = URI.create(relation.getHref());
    return getRestTemplate()
        .exchange(uri, HttpMethod.GET, null, RouteResource.LIST)
        .getBody()
        .getContent();
  }

  @Override
  public RouteResource findOne(Link link) {
    URI uri = URI.create(link.getHref());

    RouteResource resource;

    try {
      resource = getRestTemplate().exchange(
          uri,
          HttpMethod.GET,
          null,
          RouteResource.class
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
