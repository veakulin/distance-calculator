// Автореализуемый репозиторий для расположений.

package ru.akulin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.akulin.entity.Location;

public interface LocationRepository extends CrudRepository<Location, Long> {
}
