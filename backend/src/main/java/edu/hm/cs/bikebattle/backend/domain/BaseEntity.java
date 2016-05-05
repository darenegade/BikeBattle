package edu.hm.cs.bikebattle.backend.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.domain
 * Author(s): Rene Zarwel
 * Date: 27.03.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
@Getter
@Setter
public abstract class BaseEntity implements Cloneable, Serializable {

    @Id
    BigInteger oid;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
