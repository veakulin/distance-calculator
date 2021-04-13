// Интерфейс позволяет игнорировать ошибки преобразования строк из
// входного xml в Double

package ru.akulin.upload;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class DoubleFormatIgnoringAdapter extends XmlAdapter<String, Double> {
    @Override
    public String marshal(Double value) throws Exception {
        return value.toString();
    }

    @Override
    public Double unmarshal(String value) throws Exception {
        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException cause) {
            // В xml встретилось значение, которое не получается привести к Double.
            // Наверное неплохо было бы как-то дать знать клиенту, на каком именно значении споткнулся парсер.
            return null;
        }
    }
}