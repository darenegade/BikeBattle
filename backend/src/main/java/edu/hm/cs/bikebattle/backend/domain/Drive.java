package edu.hm.cs.bikebattle.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.domain
 * Author(s): Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drive extends BaseEntity {

  @Min(0)
  float totalTime;

  @Min(0)
  float averageSpeed;

  List<Measurement> measurements = new ArrayList<>();

  @NotNull
  @NonNull
  @DBRef
  Route route;

  @NotNull
  @NonNull
  @DBRef
  User owner;

}
