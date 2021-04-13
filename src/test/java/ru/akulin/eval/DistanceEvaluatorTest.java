package ru.akulin.eval;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;
import ru.akulin.repository.DistanceRepository;
import ru.akulin.repository.LocationRepository;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class DistanceEvaluatorTest {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    DistanceRepository distanceRepository;
    @Autowired
    DistanceEvaluator distanceEvaluator;

    @Test
    void eval() {
        setupTest();
        List<RequestedDistance> result = distanceEvaluator.eval(testCase1);
        Assertions.assertEquals(Arrays.asList(expectedResult1), result);
    }

    private void setupTest() {
        Location[] locations = {
            new Location(1392685764L, "Tokyo", 35.6897, 139.6922),
            new Location(1360771077L, "Jakarta", -6.2146, 106.8451),
            new Location(1356872604L, "Delhi", 28.66, 77.23),
        };

        Distance[] distances = {
            new Distance(1392685764L, 1360771077L, 5786.0873823493575),
            new Distance(1392685764L, 1356872604L, 5833.417260856922),
        };

        locationRepository.saveAll(Arrays.asList(locations));
        distanceRepository.saveAll(Arrays.asList(distances));
    }

    private final EvalRequest testCase1 = new EvalRequest(
        new String[] {"1392685764", "1360771077"},
        new String[] {"1356872604", "ABC", null},
        EvalType.PREFER_CROWFLIGHT
        );

    private final RequestedDistance[] expectedResult1 = {
        new RequestedDistance("1392685764", "1356872604", 5833.417260856922),
        new RequestedDistance("1392685764", "ABC", null),
        new RequestedDistance("1392685764", null, null),
        new RequestedDistance("1360771077", "1356872604", 5009.694909372779),
        new RequestedDistance("1360771077", "ABC", null),
        new RequestedDistance("1360771077", null, null)
    } ;
}