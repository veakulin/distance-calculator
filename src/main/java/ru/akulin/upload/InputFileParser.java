// Объявим вот такой интерфейс,
// вдруг вместо xml надо будет разбирать csv.

package ru.akulin.upload;

import org.springframework.core.io.InputStreamSource;
import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;

import java.util.List;

public interface InputFileParser {
    List<Location> parseLocations(InputStreamSource inputStreamSource);
    List<Distance> parseDistances(InputStreamSource inputStreamSource);
}
