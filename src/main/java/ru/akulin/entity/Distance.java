// Сущность "Расстояние"
// Хранит предварительно вычисленное расстояние между двумя расположениями

// Аннотации @Column должны быть привязаны к геттерам, а имена полей в БД должны быть указаны в нижнем регистре.
// Иначе фреймворк пытается преобразовать имена свойств по схеме fromId -> from_id.
// Видимо, должен быть способ как-то определить стратегию именования через конфигурацию, но я пока такого способа не знаю.

package ru.akulin.entity;

import ru.akulin.upload.DoubleFormatIgnoringAdapter;
import ru.akulin.upload.LongFormatIgnoringAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@IdClass(DistancePK.class)
public class Distance implements Validatable {

    private Long fromId;
    private Long toId;
    private Double distance;

    public Distance() {
    }

    public Distance(Long fromId, Long toId, Double distance) {
        this.fromId = fromId;
        this.toId = toId;
        this.distance = distance;
    }

    @Id
    @Column(name = "fromid")
    @XmlAttribute(name = "from")
    @XmlJavaTypeAdapter(LongFormatIgnoringAdapter.class)
    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    @Id
    @Column(name = "toid")
    @XmlAttribute(name = "to")
    @XmlJavaTypeAdapter(LongFormatIgnoringAdapter.class)
    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    @Column(name = "distance")
    @XmlAttribute(name = "dist")
    @XmlJavaTypeAdapter(DoubleFormatIgnoringAdapter.class)
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    @Transient // Иначе Hibernate отказывается запускать тесты. Непонятно...
    public boolean isValid() {
        return ((fromId == null) || (toId == null) || (distance == null)) ? false : true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Distance)) return false;

        Distance distance1 = (Distance) o;

        if (getFromId() != null ? !getFromId().equals(distance1.getFromId()) : distance1.getFromId() != null)
            return false;
        if (getToId() != null ? !getToId().equals(distance1.getToId()) : distance1.getToId() != null) return false;
        return getDistance() != null ? getDistance().equals(distance1.getDistance()) : distance1.getDistance() == null;
    }

    @Override
    public int hashCode() {
        int result = getFromId() != null ? getFromId().hashCode() : 0;
        result = 31 * result + (getToId() != null ? getToId().hashCode() : 0);
        result = 31 * result + (getDistance() != null ? getDistance().hashCode() : 0);
        return result;
    }
}