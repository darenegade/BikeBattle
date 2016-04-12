package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.RoutePoint_DTO;
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
public class RoutePoint_RestClient extends RestClient{

    /**
     * Builder of the RestClient
     */
    public static class RoutePoint_RestClient_Builder{

        /** Singleton of RestClient **/
        private static RoutePoint_RestClient client;

        public static RoutePoint_RestClient build(RestTemplate restTemplate){

            if(restTemplate == null)
                return null;

            client = new RoutePoint_RestClient(restTemplate);
            return client;
        }

        public static RoutePoint_RestClient build(){
            return client;
        }
    }

    private static final String BASE_RELATION = "RoutePoints";

    private RoutePoint_RestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public RoutePoint_Resource create(RoutePoint_Resource routePoint){

        URI uri = URI.create(
                getTraverson().follow(BASE_RELATION).asLink().getHref());

        return getRestTemplate().exchange(uri, HttpMethod.POST, new HttpEntity<RoutePoint_DTO>(routePoint.getContent()),RoutePoint_Resource.class).getBody();
    }

    public RoutePoint_Resource update(RoutePoint_Resource routePoint) {

        URI uri = URI.create(routePoint.getId().getHref());

        return getRestTemplate().exchange(uri, HttpMethod.PUT, new HttpEntity<RoutePoint_DTO>(routePoint.getContent()), RoutePoint_Resource.class).getBody();
    }

    public void delete(Link id) {

        URI uri = URI.create(id.getHref());
        getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
    }

    public Collection<RoutePoint_Resource> findAll() {
        return getTraverson()
                .follow(BASE_RELATION)
                .toObject(RoutePoint_Resource.LIST).getContent();
    }

    public Collection<RoutePoint_Resource> findAll(Link relation) {
        URI uri = URI.create(relation.getHref());
        return getRestTemplate()
                .exchange(uri, HttpMethod.GET, null, RoutePoint_Resource.LIST)
                .getBody()
                .getContent();
    }

    public RoutePoint_Resource findOne(Link link) {
        URI uri = URI.create(link.getHref());

        RoutePoint_Resource resource;

        try {
            resource = getRestTemplate().exchange(uri, HttpMethod.GET, null, RoutePoint_Resource.class).getBody();
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
