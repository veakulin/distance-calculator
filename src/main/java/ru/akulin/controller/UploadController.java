// Контроллер загрузки данных из XML-файла в БД

package ru.akulin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;
import ru.akulin.exception.UploadControllerException;
import ru.akulin.upload.InputFileParser;
import ru.akulin.upload.JdbcUploader;
import ru.akulin.upload.XmlInputFileParser;
import javax.sql.DataSource;
import java.util.List;

@RestController
@RequestMapping(path = "/upload")
public class UploadController {

    @Autowired
    DataSource dataSource;

    @GetMapping(path = "")
    public String get() {
        return "Здесь может быть инструкция по использованию конечной точки.";
    }

    @PostMapping(path = "", consumes ="multipart/form-data", produces = "application/json")
    public void post(@RequestParam(name = "file") MultipartFile inputFile) {
        try {
            // Можно было бы создать parser как приватный элемент через @Autowired.
            // Но, скорее всего, загружать данные придется не слишком часто.
            // Так что ни к чему ему висеть в памяти без дела.
            InputFileParser parser = new XmlInputFileParser();
            List<Location> locList = parser.parseLocations(inputFile);
            List<Distance> distList = parser.parseDistances(inputFile);

            JdbcUploader loader = new JdbcUploader(dataSource);
            loader.uploadLocations(locList);
            loader.uploadDistances(distList);
        }
        catch (Exception cause) {
            throw new UploadControllerException("Во время загрузки данных произошла ошибка.", cause);
        }
    }
}
