// Парсер XML-файлов.
// Работает с файлами такой структуры:

// <root>                                       - Корневой элемент обязателен
//   <locations>                                - Элемент может отсутствовать или повторяться несколько раз на этом же уровне
//     <location id="" name="" lat="" lng="" /> - Элемент может отсутствовать или повторяться несколько раз на этом же уровне
//   </locations>
//   <distances>                                - То же, что для locations
//     <distance from="" to="" dist="" />
//   </distances>
// <root>

// Большая часть проблем разметки игнорируется, т.е. из файла достаётся всё, что можно.
// Фатальные ошибки парсера JAXB оформляются в исключение InputFileParserException.

// Возможно, что в файле окажется множество больших блоков locations и distances.
// Элементы каждого типа будут перемещены в единый возвращаемый список.
// Например, если в файле было несколько блоков locations,
// то все элементы location будут перемещены в единый результирующий список List<Location>.

// Парсер умеет обрабатывать некоторые виды ошибок, типа некорректных входных значений.
// Но синтаксические ошибки, типа <location id= name="">, будут приводить к исключению.

// Если дело дойдёт до собеседования, расскажу, почему сделано так как сделано, почему LinkedList и т.д. :)

package ru.akulin.upload;

import org.springframework.core.io.InputStreamSource;
import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;
import ru.akulin.entity.Validatable;
import ru.akulin.exception.InputFileParserException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class XmlInputFileParser implements InputFileParser {

    // Отдаем все расположения из файла
    public List<Location> parseLocations(InputStreamSource inputStreamSource) {
        Root<LocationSubRoot, Location> locationRoot = parse(inputStreamSource, LocationRoot.class);
        return prepareResult(locationRoot, Location.class);
    }

    // Отдаем все расстояния из файла
     public List<Distance> parseDistances(InputStreamSource inputStreamSource) {
        Root<DistanceSubRoot, Distance> distanceRoot = parse(inputStreamSource, DistanceRoot.class);
        return prepareResult(distanceRoot, Distance.class);
    }

    // Внутренняя кухня
    // Парсим файл в какие-то объекты. В какие - укажет клиент.
    // На выход отдаем корень дерева.
    // Конкретно в нашем случае это будет
    // либо root -> locations -> location,
    // либо root -> distances -> distance.
    private <R extends Root<S, I>, S extends SubRoot<I>, I extends Validatable> Root<S, I> parse (InputStreamSource inputStreamSource, Class<R> clazz) {
        try (InputStream inputStream = inputStreamSource.getInputStream()) {
            JAXBContext jaxbCtx = JAXBContext.newInstance(clazz);
            Unmarshaller um = jaxbCtx.createUnmarshaller();
            um.setEventHandler(validationEventHandler);

            return (Root<S, I>)um.unmarshal(inputStream);
        }
        catch (Exception cause) {

            throw new InputFileParserException("Во время загрузки данных из входного файла произошла ошибка.", cause);
        }
    }

    // Можно было бы обойтись и без этого обработчика,
    // но мало ли что, вдруг захочется протоколировать сообщения валидатора о проблемах
    private final ValidationEventHandler validationEventHandler = event -> {
        if (event.getSeverity() == ValidationEvent.FATAL_ERROR)
            throw new InputFileParserException("Неустранимая ошибка валидатора XML-разметки: " + event.getMessage());
        return true;
    };

    // Собираем единый результирующий список.
    // Вместо for используем while, т.к. for не разрешает менять коллекцию при переборе,
    // а нам же надо именно перемещать элементы.
    // Так же можно было бы предусмотреть дополнительную оптимизацию,
    // т.к., например, у нас может оказаться единственный большой список расположений
    // и тогда нет смысла куда-то его перемещать.
    // Но даже очень большие связные списки перетекают друг в друга очень быстро,
    // так что пока и без оптимизации всё работает неплохо.
    private <S extends SubRoot<I>, I extends Validatable> List<I> prepareResult(Root<S, I> root, Class<I> clazz) {
        List<I> result = new LinkedList<>();
        if ((root != null) && (root.getSubRoots() != null))
            while (!root.getSubRoots().isEmpty()) {
                S subRoot = root.getSubRoots().get(0);
                if ((subRoot != null) && (subRoot.getItems() != null))
                    while (!subRoot.getItems().isEmpty()) {
                        I item = subRoot.getItems().get(0);
                        if ((item != null) && (item.isValid()))
                            result.add(clazz.cast(item));
                        subRoot.getItems().remove(item);
                    }
                root.getSubRoots().remove(subRoot);
            }
        return result;
    }

    private interface Root<S extends SubRoot<I>, I extends Validatable> {
        List<S> getSubRoots();
    }

    private interface SubRoot<I extends Validatable> {
        List<I> getItems();
    }

    // Классы сопоставления с XML.
    // Сделаны вложенными приватными. Ну куда их еще применить?
    // Реализация-то специфическая.
    @XmlRootElement(name = "root")
    @XmlAccessorType(XmlAccessType.NONE)
    private static class LocationRoot implements Root<LocationSubRoot, Location> {

        private List<LocationSubRoot> subRoots = new LinkedList<>();

        public LocationRoot() {
        }

        @XmlElement(name = "locations")
        public List<LocationSubRoot> getSubRoots() {
            return subRoots;
        }

        public void setSubRoots(List<LocationSubRoot> subRoots) {
            this.subRoots = subRoots;
        }
    }

    private static class LocationSubRoot implements SubRoot<Location> {

        private List<Location> items = new LinkedList<>();

        public LocationSubRoot() {
        }

        @XmlElement(name = "location")
        public List<Location> getItems() {
            return items;
        }

        public void setItems(List<Location> items) {
            this.items = items;
        }
    }

    @XmlRootElement(name = "root")
    private static class DistanceRoot implements Root<DistanceSubRoot, Distance> {

        private List<DistanceSubRoot> subRoots = new LinkedList<>();

        public DistanceRoot() {
        }

        @XmlElement(name = "distances")
        public List<DistanceSubRoot> getSubRoots() {
            return subRoots;
        }

        public void setSubRoots(List<DistanceSubRoot> subRoots) {
            this.subRoots = subRoots;
        }
    }

    private static class DistanceSubRoot implements SubRoot<Distance> {

        private List<Distance> items = new LinkedList<>();

        public DistanceSubRoot() {
        }

        @XmlElement(name = "distance")
        public List<Distance> getItems() {
            return items;
        }

        public void setItems(List<Distance> items) {
            this.items = items;
        }
    }
}

