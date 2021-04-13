package ru.akulin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.akulin.eval.DistanceEvaluator;
import ru.akulin.repository.DistanceRepository;
import ru.akulin.repository.LocationRepository;

@SpringBootApplication
public class EntryPoint {

    public static void main(String[] args) {
        SpringApplication.run(EntryPoint.class, args);
    }
}
