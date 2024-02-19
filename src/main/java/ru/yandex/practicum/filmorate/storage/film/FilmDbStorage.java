package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList get() {
        List<Film> films = new ArrayList<>();


        log.debug("количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {

        log.debug("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {

        log.debug("фильм с id {} обновлен: {}", film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Integer filmId) {

        log.info("Нет фильма с id: {}", filmId);
        throw new NotFoundException();
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        film.addLike(userId);
        update(film);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        film.deleteLike(userId);
        update(film);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = new ArrayList<>();

        log.info("Количество популярных фильмов: {}", films.size());
        return films;
    }

}
