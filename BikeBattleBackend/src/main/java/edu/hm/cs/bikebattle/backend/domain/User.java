package edu.hm.cs.bikebattle.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.domain
 * Author(s): Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */

@EqualsAndHashCode(callSuper = true, exclude = "friends")
@ToString(exclude = "friends")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User extends BaseEntity {

  @Length(min = 1)
  String name;

  @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
      + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
  @NotNull
  @NonNull
  @Indexed(unique = true)
  String email;

  @Min(0)
  Float size;

  @Min(0)
  Float weight;

  @DBRef
  Set<User> friends = new HashSet<>();
}
