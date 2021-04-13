// Сущность "Город"
// Состоит только из идентификатора и названия

package ru.akulin.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "city")
public class City extends AbstractCity {

    public City() {
        super();
    }

    public City(Long id, String name) {
        super(id, name);
    }
}
