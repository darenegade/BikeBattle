package edu.hm.cs.bikebattle.app.api.rest;

import edu.hm.cs.bikebattle.app.api.domain.Drive_DTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.api.rest
 * Author(s): Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class Drive_Resource extends Resource<Drive_DTO> {
        /**
         * This is a simple way to get the Resources "class".
         */
        public static final ParameterizedTypeReference<Resources<Drive_Resource>> LIST = new ParameterizedTypeReference<Resources<Drive_Resource>>() {
        };

        /**
         * Create a new Resource with a blank Drive_DTO.
         */
        public Drive_Resource() {
            super(new Drive_DTO());
        }

        /**
         * Create a new Resource from  a Drive_DTO and multiple Resources.
         * @param content The Drive_DTO
         * @param links The links to add to the resource
         */
        public Drive_Resource(Drive_DTO content, Link... links) {
            super(content, links);
        }

        /**
         * Create a new Resource from  a Drive_DTO and multiple Resources.
         * @param content The Drive_DTO
         * @param links The links to add to the resource
         */
        public Drive_Resource(Drive_DTO content, Iterable<Link> links) {
            super(content, links);
        }
    }
