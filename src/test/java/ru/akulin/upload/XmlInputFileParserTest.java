package ru.akulin.upload;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;

import java.nio.charset.StandardCharsets;
import java.util.List;

class XmlInputFileParserTest {

    @Test
    void parseLocations() {
        MockMultipartFile xmlFile = new MockMultipartFile("sample.xml", xml.getBytes(StandardCharsets.UTF_8));
        InputFileParser parser = new XmlInputFileParser();
        List<Location> actualLocations = parser.parseLocations(xmlFile);
        Assertions.assertArrayEquals(expectedLocations, actualLocations.toArray());
    }

    @Test
    void parseDistances() {
        MockMultipartFile xmlFile = new MockMultipartFile("sample.xml", xml.getBytes(StandardCharsets.UTF_8));
        InputFileParser parser = new XmlInputFileParser();
        List<Distance> actualDistances = parser.parseDistances(xmlFile);
        Assertions.assertArrayEquals(expectedDistances, actualDistances.toArray());
    }

    private final String xml =
        "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>" +
        "<root>" +
        // Нормальные данные
        "    <locations>" +
        "        <location id='1392685764' name='Tokyo' lat='35.6897' lng='139.6922'/>" +
        "        <location id='1360771077' name='Jakarta' lat='-6.2146' lng='106.8451'/>" +
        "        <location id='1356872604' name='Delhi' lat='28.66' lng='77.23'/>" +
        "    </locations>" +
        // Подстава №1
        "    <locations />" +
        // Подстава №2
        "    <locations>" +
        "        <location x='0' id='' lat='sd'/>" +
        "    </locations>" +
        // Подстава №3
        "    <locations>" +
        "        <location id='345345'>" +
        "        <id>111</id>" +
        "        </location>" +
        "    </locations>" +
        // Аналогично
        "    <distances>" +
        "        <distance from='1392685764' to='1360771077' dist='5786.0873823493575'/>" +
        "        <distance from='1392685764' to='1356872604' dist='5833.417260856922'/>" +
        "    </distances>" +
        "    <distances />" +
        "    <distances>" +
        "        <distance from='podstavka' to='13.60771077' dist='5786.0873823493575'/>" +
        "    </distances>" +
        "</root>";

    private final Location[] expectedLocations = {
            new Location(1392685764L, "Tokyo", 35.6897, 139.6922),
            new Location(1360771077L, "Jakarta", -6.2146, 106.8451),
            new Location(1356872604L, "Delhi", 28.66, 77.23),
    };

    private final Distance[] expectedDistances = {
            new Distance(1392685764L, 1360771077L, 5786.0873823493575),
            new Distance(1392685764L, 1356872604L, 5833.417260856922),
    };
}