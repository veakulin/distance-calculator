// Интерфейс позволяет игнорировать ошибки преобразования строк из
// входного xml в Long

package ru.akulin.upload;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class LongFormatIgnoringAdapter extends XmlAdapter<String, Long> {
    @Override
    public String marshal(Long value) throws Exception {
        return value.toString();
    }

    @Override
    public Long unmarshal(String value) throws Exception {
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException cause) {
            // В xml встретилось значение, которое не получается привести к Long.
            // Наверное неплохо было бы как-то дать знать клиенту, на каком именно значении споткнулся парсер.
            return null;
        }
    }
}