package edu.hm.cs.bikebattle.app.api.rest;

import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collection;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public abstract class RestClient<T> {

  public static final URI BASEPATH = URI.create("http://localhost:8080/");

  public static final String SEARCH = "search";


  /**
   * The restTemplate used for the HTTP Requests.
   */
  @Getter
  private final RestTemplate restTemplate;

  /**
   * Default Constructor.
   * @param restTemplate Connection to Backend
   */
  public RestClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Method to create an new entity on the Backend.
   * @param entity to create
   * @return created entity
   */
  public abstract T create(T entity);

  /**
   * Method to update an entity on the Backend.
   * @param entity to update
   * @return updated entity
   */
  public abstract T update(T entity);

  /**
   * Method to delete an entity on the Backend.
   * @param id Link of entity
   */
  public abstract void delete(Link id);

  /**
   * Get all entities from the Backend.
   * @return all entities
   */
  public abstract Collection<T> findAll();

  /**
   * Get all entities from the Backend on the specific endpoint.
   * @param relation endpoint
   * @return all entities
   */
  public abstract Collection<T> findAll(Link relation);

  /**
   * Get a specific entity from the backend.
   * @param link of entity
   * @return matching entity
   */
  public abstract T findOne(Link link);


}
