// Алгоритм Crowflight (он же Straight-line, он же Haversine formula).
// Просто скопипащен из интернета.

package ru.akulin.eval;

public final class CrowflightAlgorithm {

    public static Double eval(Double lat1, Double lng1, Double lat2, Double lng2) {

        // Если какой-то координаты нет... ну нет так нет.
        if ((lat1 == null) || (lng1 == null) || (lat2 == null) || (lng2 == null))
            return null; // Да, я знаю, что null возвращать нехорошо, но здесь это вполне уместно

        double earth_r = 6371.0; // Радиус земли в километрах
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = earth_r * c; // Расстояние в км
        return d;
    }
}
