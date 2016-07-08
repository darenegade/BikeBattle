package edu.hm.cs.bikebattle.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.io.Serializable;
import java.util.Date;

/**
 * Base class of all entities.
 *
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
    String oid;

    @Version
    private Long version;

    @LastModifiedDate
    @JsonIgnore
    private Date lastModified;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
