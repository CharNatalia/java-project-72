package hexlet.code.repository;

import hexlet.code.model.LastUrlsChecks;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck check) throws SQLException {
        String sql = "INSERT INTO url_checks (url_id, status_code, h1, title, description) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setInt(2, check.getStatusCode());
            preparedStatement.setString(3, check.getH1());
            preparedStatement.setString(4, check.getTitle());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            // Устанавливаем ID в сохраненную сущность
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> getEntitiesByUrlId(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC, id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var h1 = resultSet.getString("h1");
                var title = resultSet.getString("title");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var check = new UrlCheck(urlId, statusCode, h1, title, description);
                check.setId(id);
                check.setCreatedAt(createdAt);
                result.add(check);
            }
            return result;
        }
    }

    public static List<LastUrlsChecks> getLastChecks() throws SQLException {
        var sql = "SELECT " +
                "DISTINCT ON (urls.id) urls.id, urls.name, url_checks.status_code, url_checks.created_at, url_checks.id AS check_id " +
                "FROM urls LEFT JOIN url_checks " +
                "ON url_checks.url_id = urls.id " +
                "ORDER BY urls.id, url_checks.created_at DESC, url_checks.id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<LastUrlsChecks>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var time = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = null;
                var checkId = resultSet.getLong("check_id");
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

    public static Optional<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        var sql = "SELECT *\n" +
                "FROM url_checks\n" +
                "WHERE url_id = ?\n" +
                "ORDER BY created_at DESC, id DESC\n" +
                "LIMIT 1";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var h1 = resultSet.getString("h1");
                var title = resultSet.getString("title");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                UrlCheck urlCheck = new UrlCheck(urlId, statusCode, h1, title, description);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                return Optional.of(urlCheck);
            }
            return Optional.empty();
        }

    }
}
