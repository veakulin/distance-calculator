// Специальный класс для загрузки расположений и расстояний в БД через JDBC.
// Работает примерно в 2-2.5 раза быстрее, чем метод Repository.saveAll() из Spring Data.
// На загрузках миллионов строк это может быть существенно.
// И да, чего только не сделаешь, лишь бы код не дублировать...

package ru.akulin.upload;

import ru.akulin.entity.Distance;
import ru.akulin.entity.Location;
import ru.akulin.exception.JdbcUploaderException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class JdbcUploader {

    private DataSource dataSource;

    public JdbcUploader() {
    }

    public JdbcUploader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Загружаем расположения в БД
    public long uploadLocations(List<Location> locations) {
        String insertCmd = "insert into city (id, name, latitude, longitude) values (?, ?, ?, ?)";
        String updateCmd = "update city set name = ?, latitude = ?, longitude = ? where id = ?";

        // Когда будет нужно, обратимся сюда за параметрами для insertCmd
        ParameterSequencer<Location> insertParameterSequencer = (insertParameterSource) -> {
            Location location = insertParameterSource;
            return new Object[] { location.getId(), location.getName(), location.getLatitude(), location.getLongitude() };
        };

        // Когда будет нужно, обратимся сюда за параметрами для updateCmd
        ParameterSequencer<Location> updateParameterSequencer = (updateParameterSource) -> {
            Location location = updateParameterSource;
            return new Object[] { location.getName(), location.getLatitude(), location.getLongitude(), location.getId() };
        };

        return upload(locations, insertCmd, updateCmd, insertParameterSequencer, updateParameterSequencer);
    }

    // Загружаем расстояния в БД.
    public long uploadDistances(List<Distance> distances) {
        String insertCmd = "insert into distance (fromId, toId, distance) values (?, ?, ?)";
        String updateCmd = "update distance set distance = ? where (fromId = ?) and (toId = ?)";

        // Когда будет нужно, обратимся сюда за параметрами для insertCmd
        ParameterSequencer<Distance> insertParameterSequencer = (insertParameterSource) -> {
            Distance distance = insertParameterSource;
            return new Object[] { distance.getFromId(), distance.getToId(), distance.getDistance() };
        };

        // Когда будет нужно, обратимся сюда за параметрами для updateCmd
        ParameterSequencer<Distance> updateParameterSequencer = (updateParameterSource) -> {
            Distance distance = updateParameterSource;
            return new Object[] { distance.getDistance(), distance.getFromId(), distance.getToId() };
        };

        return upload(distances, insertCmd, updateCmd, insertParameterSequencer, updateParameterSequencer);
    }

    // Вся низкоуровневая работа с БД.
    // На выход отдает количество выполненных вставок и обновлений.
    // Возможно, стоит отдавать на выход вошедший список.
    private <T> long upload(List<T> items, String insertCmd, String updateCmd, ParameterSequencer<T> insertParameterSequencer, ParameterSequencer<T> updateParameterSequencer) {

        long totalUpdates = 0; // Счетчик вставок и обновлений

        try (Connection conn = dataSource.getConnection();
             // Для каждой команды приготовим отдельный оператор
             PreparedStatement insertStatement = conn.prepareStatement(insertCmd);
             PreparedStatement updateStatement = conn.prepareStatement(updateCmd)) {

            // Обернем всё одной большой транзакцией.
            // Чтобы не получилось так, что между insert и update кто-то удалит обновляемую запись,
            // установим уровень изоляции REPEATABLE_READ
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);

            for (T item : items) {

                long updatesCounter = 0;

                setStatementParams(insertStatement, insertParameterSequencer.getSequence(item));
                try {
                    // Если запись уже существует, то возникнет SQLIntegrityConstraintViolationException.
                    updatesCounter = insertStatement.executeUpdate();
                }
                catch (SQLIntegrityConstraintViolationException cause) {
                    // Оказалось, что запись уже существует.
                    // Не страшно, попробуем её обновить.
                    setStatementParams(updateStatement, updateParameterSequencer.getSequence(item));
                    updatesCounter = updateStatement.executeUpdate();
                }
                totalUpdates+=updatesCounter;
            }

            conn.commit();
        }
        catch (Exception cause) {
            // Случилось что-то невообразимое
            throw new JdbcUploaderException("Во время загрузки данных в БД произошла ошибка", cause);
        }

        return totalUpdates;
    }

    private void setStatementParams(PreparedStatement statement, Object[] params) throws SQLException {
        for (int ix = 0; ix <= params.length - 1; ix++) // Установим по очереди параметры оператора
            statement.setObject(ix + 1, params[ix]);
    }

    private interface ParameterSequencer<T> {
        // Возвращаемый массив содержит параметры в порядке их включения в объект PreparedStatement
        Object[] getSequence(T parameterSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
