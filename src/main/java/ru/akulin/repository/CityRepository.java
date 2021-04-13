// Автореализуемый репозиторий только для чтения городов.

package ru.akulin.repository;

import ru.akulin.entity.City;

public interface CityRepository extends ReadOnlyRepository<City, Long> {
}
