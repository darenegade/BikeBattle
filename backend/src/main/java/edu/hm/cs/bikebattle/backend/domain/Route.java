package edu.hm.cs.bikebattle.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Route extends BaseEntity {

  @Length(min = 1)
  String name;

  boolean privat;

  @Min(0)
  float length;

  @NotNull
  @NonNull
  Difficulty difficulty;

  @NotNull
  @NonNull
  Routetyp routetyp;

  double[] start;

  List<RoutePoint> routePoints = new ArrayList<>();

  @NotNull
  @NonNull
  @DBRef
  User owner;
}
