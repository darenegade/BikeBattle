package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.DriveDto;
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
public class DriveRestClient extends RestClient<DriveResource> {

  /**
   * Builder of the RestClient.
   */
  public static class Builder {

    /**
     * Singleton of RestClient.
     **/
    private static DriveRestClient client;

    /**
     * Builds a new RestClient.
     * @param restTemplate Template to use
     * @return new Client
     */
    public static DriveRestClient build(RestTemplate restTemplate) {

      if (restTemplate == null) {
        return null;
      }

      client = new DriveRestClient(restTemplate);
      return client;
    }


    public static DriveRestClient build() {
      return client;
    }
  }

  private static final String BASE_RELATION = "Drives";

  private DriveRestClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  public DriveResource create(DriveResource drive) {

    URI uri = URI.create(BASEPATH.toString() + BASE_RELATION);

    return getRestTemplate().exchange(
        uri, HttpMethod.POST,
        new HttpEntity<DriveDto>(drive.getContent()),
        DriveResource.class
    ).getBody();

  }

  @Override
  public DriveResource update(DriveResource drive) {

    URI uri = URI.create(drive.getId().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.PUT,
        new HttpEntity<DriveDto>(drive.getContent()),
        DriveResource.class
    ).getBody();
  }

  @Override
  public void delete(Link id) {

    URI uri = URI.create(id.getHref());
    getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
  }

  @Override
  public Collection<DriveResource> findAll() {
    URI uri = URI.create(BASEPATH.toString());

    return getRestTemplate().exchange(uri, HttpMethod.GET, null, DriveResource.LIST)
        .getBody().getContent();
  }

  @Override
  public Collection<DriveResource> findAll(Link relation) {
    URI uri = URI.create(relation.getHref());
    return getRestTemplate()
        .exchange(uri, HttpMethod.GET, null, DriveResource.LIST)
        .getBody()
        .getContent();
  }

  @Override
  public DriveResource findOne(Link link) {
    URI uri = URI.create(link.getHref());

    DriveResource resource;

    try {
      resource = getRestTemplate().exchange(
          uri,
          HttpMethod.GET,
          null,
          DriveResource.class
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
