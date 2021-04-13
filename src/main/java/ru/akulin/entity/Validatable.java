// Любая реализация этого интерфейса должна сообщить,
// является ли внутреннее состояние объекта корректным.
// Нужен для разбора входных файлов.

package ru.akulin.entity;

public interface Validatable {
    boolean isValid();
}
