package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.MeasurementDto;
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
public class MeasurementRestClient extends RestClient<MeasurementResource> {

  /**
   * Builder of the RestClient.
   */
  public static class Builder {

    /**
     * Singleton of RestClient.
     **/
    private static MeasurementRestClient client;

    /**
     * Builds a new RestClient.
     * @param restTemplate Template to use
     * @return new Client
     */
    public static MeasurementRestClient build(RestTemplate restTemplate) {

      if (restTemplate == null) {
        return null;
      }

      client = new MeasurementRestClient(restTemplate);
      return client;
    }

    public static MeasurementRestClient build() {
      return client;
    }
  }

  private static final String BASE_RELATION = "Measurements";

  private MeasurementRestClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  public MeasurementResource create(MeasurementResource measurement) {

    URI uri = URI.create(
        getTraverson().follow(BASE_RELATION).asLink().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.POST,
        new HttpEntity<MeasurementDto>(measurement.getContent()),
        MeasurementResource.class
    ).getBody();
  }

  @Override
  public MeasurementResource update(MeasurementResource measurement) {

    URI uri = URI.create(measurement.getId().getHref());

    return getRestTemplate().exchange(
        uri,
        HttpMethod.PUT,
        new HttpEntity<MeasurementDto>(measurement.getContent()),
        MeasurementResource.class
    ).getBody();
  }

  @Override
  public void delete(Link id) {

    URI uri = URI.create(id.getHref());
    getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
  }

  @Override
  public Collection<MeasurementResource> findAll() {
    return getTraverson()
        .follow(BASE_RELATION)
        .toObject(MeasurementResource.LIST).getContent();
  }

  @Override
  public Collection<MeasurementResource> findAll(Link relation) {
    URI uri = URI.create(relation.getHref());
    return getRestTemplate()
        .exchange(uri, HttpMethod.GET, null, MeasurementResource.LIST)
        .getBody()
        .getContent();
  }

  @Override
  public MeasurementResource findOne(Link link) {
    URI uri = URI.create(link.getHref());

    MeasurementResource resource;

    try {
      resource = getRestTemplate().exchange(
          uri,
          HttpMethod.GET,
          null,
          MeasurementResource.class
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
