// Базовый класс для City и Location.
// Лучше было бы сделать City базовым для Location,
// но пока я не сумел заставить Hibernate нормально
// работать с такой конструкцией, так что имеем вот такой специальный класс.

// Аннотации @Column должны быть привязаны к геттерам, а имена полей в БД должны быть указаны в нижнем регистре.
// Иначе фреймворк пытается преобразовать имена свойств по схеме fromId -> from_id.
// Видимо, должен быть способ как-то определить стратегию именования через конфигурацию, но я пока такого способа не знаю.

package ru.akulin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.akulin.upload.LongFormatIgnoringAdapter;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@MappedSuperclass
abstract class AbstractCity implements Validatable{

    private Long id;
    private String name;

    protected AbstractCity() {
    }

    protected AbstractCity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "id")
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(LongFormatIgnoringAdapter.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCity)) return false;

        AbstractCity that = (AbstractCity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    @Transient // Иначе Hibernate отказывается запускать тесты. Непонятно...
    @JsonIgnore // Иначе клиенту будет вываливаться поле "valid"
    public boolean isValid() {
        if ((id == null) || (name == null))
            return false;
        return true;
    }
}
