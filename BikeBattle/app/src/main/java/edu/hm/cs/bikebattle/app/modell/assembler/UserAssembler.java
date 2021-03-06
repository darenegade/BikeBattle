package edu.hm.cs.bikebattle.app.modell.assembler;

import edu.hm.cs.bikebattle.app.api.domain.UserDto;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.modell.assembler
 * @author Rene Zarwel
 * Date: 12.04.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class UserAssembler {

  /**
   * Assembles a UserDTO from a User.
   * @param user - To build from.
   * @return userDTO
   */
  public static UserDto toDto(User user) {

    return UserDto.builder()
        .name(user.getName())
        .fotoUri(user.getFotoUri())
        .size(user.getSizeInMeter())
        .weight(user.getWeightInKg()).email(user.getEmail())
        .build();
  }

  /**
   * Assembles a User from a UserDTO.
   * @param userDto - To build from.
   * @return user
   */
  public static User toBean(UserDto userDto) {

    User user = new User(
        userDto.getName(),
        userDto.getEmail(),
        userDto.getWeight(),
        userDto.getSize()
    );

    user.setOid(userDto.getOid());
    user.setFotoUri(userDto.getFotoUri());

    return user;
  }

}
