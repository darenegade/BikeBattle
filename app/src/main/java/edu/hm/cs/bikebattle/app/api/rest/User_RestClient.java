package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.User_DTO;
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
public class User_RestClient extends RestClient{

    /**
     * Builder of the RestClient
     */
    public static class User_RestClient_Builder{

        /** Singleton of RestClient **/
        private static User_RestClient client;

        public static User_RestClient build(RestTemplate restTemplate){

            if(restTemplate == null)
                return null;

            client = new User_RestClient(restTemplate);
            return client;
        }

        public static User_RestClient build(){
            return client;
        }
    }

    private static final String BASE_RELATION = "Users";

    private User_RestClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public User_Resource create(User_Resource user){

        URI uri = URI.create(
                getTraverson().follow(BASE_RELATION).asLink().getHref());

        return getRestTemplate().exchange(uri, HttpMethod.POST, new HttpEntity<User_DTO>(user.getContent()),User_Resource.class).getBody();
    }

    public User_Resource update(User_Resource user) {

        URI uri = URI.create(user.getId().getHref());

        return getRestTemplate().exchange(uri, HttpMethod.PUT, new HttpEntity<User_DTO>(user.getContent()), User_Resource.class).getBody();
    }

    public void delete(Link id) {

        URI uri = URI.create(id.getHref());
        getRestTemplate().exchange(uri, HttpMethod.DELETE, null, Void.class);
    }

    public Collection<User_Resource> findAll() {
        return getTraverson()
                .follow(BASE_RELATION)
                .toObject(User_Resource.LIST).getContent();
    }

    public Collection<User_Resource> findAll(Link relation) {
        URI uri = URI.create(relation.getHref());
        return getRestTemplate()
                .exchange(uri, HttpMethod.GET, null, User_Resource.LIST)
                .getBody()
                .getContent();
    }

    public User_Resource findOne(Link link) {
        URI uri = URI.create(link.getHref());

        User_Resource resource;

        try {
            resource = getRestTemplate().exchange(uri, HttpMethod.GET, null, User_Resource.class).getBody();
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
