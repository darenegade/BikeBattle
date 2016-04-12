package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.RoutePointDto;
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
public class RoutePointRestClient extends RestClient<RoutePointResource> {

  /**
   * Builder of the RestClient.
   */
  public static class Builder {

    /**
     * Singleton of RestClient.
     **/
    private static RoutePointRestClient client;

    /**
     * Builds a new RestClient.
     * @param restTemplate Template to use
     * @return new Client
     */
    public static RoutePointRestClient build(RestTemplate restTemplate) {

      if (restTemplate == null) {
        return null;
      }

      client = new RoutePointRestClient(restTemplate);
      return client;
    }

    public static RoutePointRestClient build() {
      return client;
    }
  }

  private static final String BASE_RELATION = "RoutePoints";

  private RoutePointRestClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  public RoutePointResource create(RoutePointResource routePoint) {

    URI uri = URI.create(
        getTraverson().follow(BASE_RELATION).asLink().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.POST,
        new HttpEntity<RoutePointDto>(routePoint.getContent()),
        RoutePointResource.class
    ).getBody();
  }

  @Override
  public RoutePointResource update(RoutePointResource routePoint) {

    URI uri = URI.create(routePoint.getId().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.PUT,
        new HttpEntity<RoutePointDto>(routePoint.getContent()),
        RoutePointResource.class
    ).getBody();
  }

  @Override
  public void delete(Link id) {

    URI uri = URI.create(id.getHref());
    getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
  }

  @Override
  public Collection<RoutePointResource> findAll() {
    return getTraverson()
        .follow(BASE_RELATION)
        .toObject(RoutePointResource.LIST).getContent();
  }

  @Override
  public Collection<RoutePointResource> findAll(Link relation) {
    URI uri = URI.create(relation.getHref());
    return getRestTemplate()
        .exchange(uri, HttpMethod.GET, null, RoutePointResource.LIST)
        .getBody()
        .getContent();
  }

  @Override
  public RoutePointResource findOne(Link link) {
    URI uri = URI.create(link.getHref());

    RoutePointResource resource;

    try {
      resource = getRestTemplate().exchange(
          uri,
          HttpMethod.GET,
          null,
          RoutePointResource.class
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
