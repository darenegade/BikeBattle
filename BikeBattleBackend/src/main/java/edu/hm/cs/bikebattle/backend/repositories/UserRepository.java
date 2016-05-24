package edu.hm.cs.bikebattle.backend.repositories;

import edu.hm.cs.bikebattle.backend.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.repositories
 * Author(s): Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@RepositoryRestResource(exported = true)
public interface UserRepository extends CrudRepository<User, String> {

  User findByEmail(@Param("email") String email);

  List<User> findByName(@Param("name") String name);

  List<User> findByNameContainingIgnoreCase(@Param("name") String name);
}
