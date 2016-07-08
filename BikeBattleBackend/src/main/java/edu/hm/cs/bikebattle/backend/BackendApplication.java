package edu.hm.cs.bikebattle.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main Application to start the Spring Backend.
 */
@SpringBootApplication
@EnableMongoAuditing
public class BackendApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(BackendApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

}