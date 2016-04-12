package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.Measurement_DTO;
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
public class Measurement_RestClient extends RestClient{

    /**
     * Builder of the RestClient
     */
    public static class Measurement_RestClient_Builder{

        /** Singleton of RestClient **/
        private static Measurement_RestClient client;

        public static Measurement_RestClient build(RestTemplate restTemplate){

            if(restTemplate == null)
                return null;

            client = new Measurement_RestClient(restTemplate);
            return client;
        }

        public static Measurement_RestClient build(){
            return client;
        }
    }

    private static final String BASE_RELATION = "Measurements";

    private Measurement_RestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public Measurement_Resource create(Measurement_Resource measurement){

        URI uri = URI.create(
                getTraverson().follow(BASE_RELATION).asLink().getHref());

        return getRestTemplate().exchange(uri, HttpMethod.POST, new HttpEntity<Measurement_DTO>(measurement.getContent()),Measurement_Resource.class).getBody();
    }

    public Measurement_Resource update(Measurement_Resource measurement) {

        URI uri = URI.create(measurement.getId().getHref());

        return getRestTemplate().exchange(uri, HttpMethod.PUT, new HttpEntity<Measurement_DTO>(measurement.getContent()), Measurement_Resource.class).getBody();
    }

    public void delete(Link id) {

        URI uri = URI.create(id.getHref());
        getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
    }

    public Collection<Measurement_Resource> findAll() {
        return getTraverson()
                .follow(BASE_RELATION)
                .toObject(Measurement_Resource.LIST).getContent();
    }

    public Collection<Measurement_Resource> findAll(Link relation) {
        URI uri = URI.create(relation.getHref());
        return getRestTemplate()
                .exchange(uri, HttpMethod.GET, null, Measurement_Resource.LIST)
                .getBody()
                .getContent();
    }

    public Measurement_Resource findOne(Link link) {
        URI uri = URI.create(link.getHref());

        Measurement_Resource resource;

        try {
            resource = getRestTemplate().exchange(uri, HttpMethod.GET, null, Measurement_Resource.class).getBody();
        } catch (HttpClientErrorException e) {
            final HttpStatus statusCode = e.getStatusCode();
            if (!HttpStatus.NOT_FOUND.equals(statusCode))
                throw e;
            if (Link.REL_SELF.equals(link.getRel()))
                throw e;
            return null;
        }

        return resource;
    }

}
