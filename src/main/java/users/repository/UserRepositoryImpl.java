package users.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import users.config.UserStatus;
import users.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .birthDate(rs.getDate("birth_date") != null ?
                    rs.getDate("birth_date").toLocalDate() : null)
            .status(UserStatus.valueOf(rs.getString("status")))
            .build();

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (name, birth_date, status) VALUES (:name, :birthDate, :status)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", user.getName());
        params.addValue("birthDate", user.getBirthDate() != null ?
                Date.valueOf(user.getBirthDate()) : null);
        params.addValue("status", user.getStatus() != null ?
                user.getStatus().name() : UserStatus.REGISTERED.name());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        user.setId(generatedId);

        if (user.getStatus() == null) {
            user.setStatus(UserStatus.REGISTERED);
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(sql, params, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY id";
        return namedParameterJdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update");
        }

        String sql = "UPDATE users SET name = :name, birth_date = :birthDate, status = :status WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("name", user.getName());
        params.addValue("birthDate", user.getBirthDate() != null ?
                Date.valueOf(user.getBirthDate()) : null);
        params.addValue("status", user.getStatus().name());

        int updatedRows = namedParameterJdbcTemplate.update(sql, params);

        if (updatedRows > 0) {
            return user;
        }

        throw new RuntimeException("Failed to update user with id: " + user.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM users WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        int deletedRows = namedParameterJdbcTemplate.update(sql, params);
        return deletedRows > 0;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM users";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}