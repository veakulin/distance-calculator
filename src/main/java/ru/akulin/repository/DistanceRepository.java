// Автореализуемый репозиторий для расстояний.

package ru.akulin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.akulin.entity.Distance;
import ru.akulin.entity.DistancePK;

public interface DistanceRepository extends CrudRepository<Distance, DistancePK> {
}
