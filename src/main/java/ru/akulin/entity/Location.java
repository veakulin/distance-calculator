// Сущность "Расположение".
// Состоит из идентификатора, названия, долготы и широты.
// Наследуется не от City, а от AbstractCity.
// См. комментарий к AbstractCity

// Аннотации @Column должны быть привязаны к геттерам, а имена полей в БД должны быть указаны в нижнем регистре.
// Иначе фреймворк пытается преобразовать имена свойств по схеме fromId -> from_id.
// Видимо, должен быть способ как-то определить стратегию именования через конфигурацию, но я пока такого способа не знаю.


package ru.akulin.entity;

import ru.akulin.upload.DoubleFormatIgnoringAdapter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "city")
public class Location extends AbstractCity {

    private Double latitude;
    private Double longitude;

    public Location() {
    }

    public Location(Long id, String name, Double latitude, Double longitude) {
        super(id, name);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Column(name = "latitude")
    @XmlAttribute(name = "lat")
    @XmlJavaTypeAdapter(DoubleFormatIgnoringAdapter.class)
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Column(name = "longitude")
    @XmlAttribute(name = "lng")
    @XmlJavaTypeAdapter(DoubleFormatIgnoringAdapter.class)
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    @Transient // Иначе Hibernate отказывается запускать тесты. Непонятно...
    public boolean isValid() {
        if ((!super.isValid()) || (latitude == null) || (longitude == null))
            return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        if (!super.equals(o)) return false;

        Location location = (Location) o;

        if (getLatitude() != null ? !getLatitude().equals(location.getLatitude()) : location.getLatitude() != null)
            return false;
        return getLongitude() != null ? getLongitude().equals(location.getLongitude()) : location.getLongitude() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        return result;
    }
}
