package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = new ArrayList<>();
        String SQL = "select * from _GENRES";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(SQL);
        while (genreRows.next()) {
            genres.add(takeGenre(genreRows));
        }
        log.info("Количество фильмов с жанром: {}", genres.size());
        return genres;
    }

    @Override
    public Genre findGenreById(Integer genreId) {
        String SQL = "select * from _GENRES where id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(SQL, genreId);
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
