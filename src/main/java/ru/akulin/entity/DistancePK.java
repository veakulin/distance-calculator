// Первичный ключ для сущности "Расстояние"
// Вынесен в отдельный класс, так как ключ составной

package ru.akulin.entity;

import java.io.Serializable;

public class DistancePK implements Serializable {

    private Long fromId;
    private Long toId;

    public DistancePK() {
    }

    public DistancePK(Long fromId, Long toId) {
        this.fromId = fromId;
        this.toId = toId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }
}
