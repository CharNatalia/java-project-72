package hexlet.code.repository;

import hexlet.code.dto.urls.LastUrlsChecks;
import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            // Устанавливаем ID в сохраненную сущность
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static boolean existsByName(String name) throws SQLException {
        var sql = "SELECT name FROM urls WHERE name = ?";

        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (var rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static List<LastUrlsChecks> getEntities() throws SQLException {
        var sql = "SELECT DISTINCT ON (u.id)\n"
                + "    u.id,\n"
                + "    u.name,\n"
                + "    c.status_code,\n"
                + "    c.created_at\n"
                + "FROM urls u\n"
                + "LEFT JOIN url_checks c ON c.url_id = u.id\n"
                + "ORDER BY u.id, c.created_at DESC;";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<LastUrlsChecks>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var time = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = null;
                if (time != null) {
                    createdAt = time.toLocalDateTime();
                }
                var status = resultSet.getInt("status_code");
                var url = new LastUrlsChecks(id, name, createdAt, status);
                result.add(url);
            }
            return result;
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var time = resultSet.getTimestamp("created_at").toLocalDateTime();
                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(time);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static void removeAll() throws SQLException {
        var sql = "DELETE FROM urls";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
