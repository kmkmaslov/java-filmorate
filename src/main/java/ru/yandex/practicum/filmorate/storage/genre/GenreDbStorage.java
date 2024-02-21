package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> takeGenre2(rs));
    }

    private Genre takeGenre2(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public Genre findGenreById(Integer genreId) {
        String sql = "select * from genres where id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, genreId);
        if (genreRows.next()) {
            Genre genre = takeGenre(genreRows);
            log.info("Жанр: {}", genre);
            return genre;
        } else {
            log.info("Нет жанра для id: {}", genreId);
            throw new NotFoundException();
        }
    }

    private Genre takeGenre(SqlRowSet rows) {
        return Genre.builder()
                .id(rows.getInt("id"))
                .name(rows.getString("name"))
                .build();
    }
}
