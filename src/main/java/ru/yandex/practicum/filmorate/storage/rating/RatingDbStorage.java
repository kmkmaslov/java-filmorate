package ru.yandex.practicum.filmorate.storage.rating;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class RatingDbStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Rating> findAll() {
        List<Rating> ratings = new ArrayList<>();
        String sql = "select * from ratings";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sql);
        while (ratingRows.next()) {
            ratings.add(takeRating(ratingRows));
        }
        log.info("Количество фильмов с рейтингом: {}", ratings.size());
        return ratings;
    }

    @Override
    public Rating findRatingById(Integer ratingId) {
        String sql = "select * from ratings where id = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sql, ratingId);
        if (ratingRows.next()) {
            Rating rating = takeRating(ratingRows);
            log.info("Рейтинг: {}", rating);
            return rating;
        } else {
            log.info("Нет рейтинга для id: {}", ratingId);
            throw new NotFoundException();
        }
    }

    private Rating takeRating(SqlRowSet rows) {
        return Rating.builder()
                .id(rows.getInt("id"))
                .name(rows.getString("name"))
                .build();
    }
}
