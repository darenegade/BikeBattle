package edu.hm.cs.bikebattle.backend.config;

import edu.hm.cs.bikebattle.backend.domain.Drive;
import edu.hm.cs.bikebattle.backend.domain.Route;
import edu.hm.cs.bikebattle.backend.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.config
 * Author(s): Rene Zarwel
 * Date: 26.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(Drive.class);
    config.exposeIdsFor(Route.class);
    config.exposeIdsFor(User.class);
  }

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener() {
    return new ValidatingMongoEventListener(validator());
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    return new LocalValidatorFactoryBean();
  }

  @Override
  public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
    Validator validator = validator();
    //bean validation always before save and create
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
