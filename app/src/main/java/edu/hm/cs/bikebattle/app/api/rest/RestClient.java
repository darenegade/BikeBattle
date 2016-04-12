package edu.hm.cs.bikebattle.app.api.rest;

import lombok.Getter;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class RestClient {

    private static final URI BASEPATH = URI.create("http://localhost:8080/");

    public static final String SEARCH = "search";


    /** The restTemplate used for the HTTP Requests. */
    @Getter
    private final RestTemplate restTemplate;

    /** Used to follow HATEOAS relations. */
    @Getter
    private final Traverson traverson;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        traverson = new Traverson(BASEPATH, MediaTypes.HAL_JSON);
        traverson.setRestOperations(restTemplate);
    }
}
