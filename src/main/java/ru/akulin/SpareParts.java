package ru.akulin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akulin.eval.DistanceEvaluator;
import ru.akulin.repository.DistanceRepository;
import ru.akulin.repository.LocationRepository;

@Configuration
public class SpareParts {

    @Bean
    DistanceEvaluator getDistanceEvaluator(LocationRepository locationRepository, DistanceRepository distanceRepository){
        return new DistanceEvaluator(locationRepository, distanceRepository);
    }
}
