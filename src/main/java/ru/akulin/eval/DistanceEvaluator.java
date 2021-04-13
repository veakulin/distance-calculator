// Класс для вычисления расстояния между двумя координатами.

package ru.akulin.eval;

import ru.akulin.entity.Distance;
import ru.akulin.entity.DistancePK;
import ru.akulin.entity.Location;
import ru.akulin.exception.DistanceEvaluatorException;
import ru.akulin.repository.DistanceRepository;
import ru.akulin.repository.LocationRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class DistanceEvaluator {

    private LocationRepository locationRepository;
    private DistanceRepository distanceRepository;

    public DistanceEvaluator(LocationRepository locationRepository, DistanceRepository distanceRepository) {
        this.locationRepository = Objects.requireNonNull(locationRepository);
        this.distanceRepository = Objects.requireNonNull(distanceRepository);
    }

    // Вычисляем расстояния по запросу клиента.
    // В любом случае метод возвращает хотя бы пустую коллекцию объектов RequestedDistance.
    public List<RequestedDistance> eval(EvalRequest evalRequest){

        List<RequestedDistance> preparedEvalResponse = prepareEvalResponse(evalRequest);

        for (RequestedDistance rd : preparedEvalResponse) {
            try {

                // Попробуем выбрать оба расположения из базы.
                // Если случится какое-то исключение, значит расстояние уже точно не посчитать.
                // Например, пользователь запросил айдишник, который не приводится к лонгу.
                Location f /* from */ = locationRepository.findById(Long.parseLong(rd.getFrom())).orElseThrow();
                Location t /* to   */ = locationRepository.findById(Long.parseLong(rd.getTo())).orElseThrow();

                // Ок, значит оба нашлись в базе.
                // Как минимум можно посчитать crowflight-ом.
                switch (evalRequest.getEvalType()) {
                    case EvalType.CROWFLIGHT: {
                        rd.setDistance(crowflight(f, t));
                        break;
                    }
                    case EvalType.DISTANCE_MAP: {
                        rd.setDistance(distanceMap(f, t));
                        break;
                    }
                    case EvalType.PREFER_CROWFLIGHT: {
                        rd.setDistance(crowflight(f, t));

                        // Если удалось посчитать предпочтительным способом, то выходим.
                        if (rd.getDistance() != null)
                            break;

                        // Иначе пробуем запасной вариант.
                        rd.setDistance(distanceMap(f, t));
                        break;
                    }
                    case EvalType.PREFER_DISTANCE_MAP: {
                        rd.setDistance(distanceMap(f, t));

                        // Аналогично.
                        if (rd.getDistance() != null)
                            break;
                        rd.setDistance(crowflight(f, t));

                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            catch (NoSuchElementException | NumberFormatException cause) {
                // NoSuchElementException - расположения нет в БД, это нормально, так что промолчим.
                // Пользователь просто получит пустое расстояние.

                // NumberFormatException - пользователь прислал кривой id, который не приводится к Long.
                // Здесь лучше бы уточнить у архитекторов, как сие обрабатывать. Возможно, нужно накрыть тазом весь запрос.
                // Пока же буду считать такой расклад нормальной бизнес-логикой и ничего не стану предпринимать.
            }

            catch (Exception cause) {
                // Видимо случилась какая-то неожиданная беда.
                throw new DistanceEvaluatorException("Во время расчёта расстояний произошла ошибка.", cause);
            }
        }

        return preparedEvalResponse;
    }

    // Пользоветель прислал запрос:
    // From:        | To:
    // -------------|-----
    // "1"          | "2"
    // "illegal_id" |
    // null         |
    //
    // Превращаем этот запрос в заготовку ответа с пустыми значениями расстояний:
    // From:        | To: | Distance:
    // -------------|-----|-----------
    // "1"          | "2" | null
    // "illegal_id" | "2" | null
    // null         | "2" | null
    //
    // В любом случае отвечаем на запрос клиента прям вот буквально, даже если тот
    // отправил в запросе какую-нибудь хрень, типа пустого списка From,
    // или айдишника, который не приводится к Long.
    // Как говориться, какой запрос - такой ответ.
    // Любая косячная комбинация в запросе вернется клиенту с пустым расстоянием.

    // Метод длинноват, но пока нет хороших идей, как сделать короче.
    private List<RequestedDistance> prepareEvalResponse(EvalRequest evalRequest) {

        List<RequestedDistance> preparedEvalResponse = new LinkedList<>();

        // Здесь всё понятно - возвращаем клиенту пустую коллекцию.
        if (evalRequest == null) {
            return preparedEvalResponse;
        }

        String[] f /* откуда */ = evalRequest.getFrom();
        String[] t /* куда */ = evalRequest.getTo();

        // Если какой-то из входных массивов не содержит элементов, он превращается в null.
        // Таким образом можно сократить количество проверок.
        f = f == null ? null : f.length == 0 ? null : f;
        t = t == null ? null : t.length == 0 ? null : t;

        // Пользователь хочет узнать растояние от ниоткуда до никуда.
        if ((f == null) && (t == null))
            return preparedEvalResponse;

        // Известно откуда (ещё не факт, что такое место существует в базе), но в никуда.
        if ((f != null) && (t == null))
            for (String i : f)
                preparedEvalResponse.add(new RequestedDistance(i, null, null));

        // Аналогично - из ниоткуда куда-то.
        if (f == null)
            for (String i : t)
                preparedEvalResponse.add(new RequestedDistance(null, i, null));

        // Известно откуда и куда.
        if ((f != null) && (t != null))
            for (String i : f)
                for (String j : t)
                    preparedEvalResponse.add(new RequestedDistance(i, j, null));

        return preparedEvalResponse;
    }

    // Поищем готовое расстояние в БД.
    private Double distanceMap(Location f, Location t) {
        Distance distance;

        // Или нашлось, или null.
        distance = distanceRepository.findById(new DistancePK(f.getId(), t.getId())).orElse(null);

        if (distance == null)
            // Запасной вариант - поищем расстояние для зеркальной пары расположений.
            distance = distanceRepository.findById(new DistancePK(t.getId(), f.getId())).orElse(null);

        return distance != null ? distance.getDistance() : null;
    }

    // Просто обвязка для длинной строчки, чтобы не писать ее два раза
    private Double crowflight(Location f, Location t) {
        return CrowflightAlgorithm.eval(f.getLatitude(), f.getLongitude(), t.getLatitude(), t.getLongitude());
    }

    public LocationRepository getLocationRepository() {
        return locationRepository;
    }

    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = Objects.requireNonNull(locationRepository);
    }

    public DistanceRepository getDistanceRepository() {
        return distanceRepository;
    }

    public void setDistanceRepository(DistanceRepository distanceRepository) {
        this.distanceRepository = Objects.requireNonNull(distanceRepository);
    }
}
