package ru.akulin.upload;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;
import ru.akulin.repository.LocationRepository;

import javax.sql.DataSource;
import java.util.Arrays;

@SpringBootTest
class JdbcUploaderTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    LocationRepository locationRepository;

    @Autowired


    @Test
    void uploadLocations() {
        JdbcUploader jdbcUploader = new JdbcUploader(dataSource);
        long actualUpdates = jdbcUploader.uploadLocations(Arrays.asList(locations));
        Assertions.assertEquals(4, actualUpdates);
    }

    @Test
    void uploadDistances() {
        JdbcUploader jdbcUploader = new JdbcUploader(dataSource);
        long actualUpdates = jdbcUploader.uploadDistances(Arrays.asList(distances));
        Assertions.assertEquals(2, actualUpdates);
    }

    private final Location[] locations = {
            new Location(1392685764L, "Tokyo", 35.6897, 139.6922),
            new Location(1360771077L, "Jakarta", -6.2146, 106.8451),
            new Location(1356872604L, "Delhi", 28.66, 77.23),
            new Location(1356872604L, "Delhi", 28.66, 77.23)
    };

    private final Distance[] distances = {
            new Distance(1392685764L, 1360771077L, 5786.0873823493575),
            new Distance(1392685764L, 1356872604L, 5833.417260856922),
    };
}