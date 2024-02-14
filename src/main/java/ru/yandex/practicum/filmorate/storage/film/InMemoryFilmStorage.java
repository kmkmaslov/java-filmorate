package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @Override
    public List<Film> get() {
        log.debug("количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        validate(film);
        film.setId(++this.id);
        if (film.getLikesByUsers() == null) {
            film.setLikesByUsers(new HashSet<>());
        }
        films.put(film.getId(), film);
        log.debug("Фильм добавлен: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validate(film);
        int filmId = film.getId();
        if (!films.containsKey(filmId)) {
            log.debug("нет фильма с таким id: {}", filmId);
            throw new NotFoundException();
        }
        if (film.getLikesByUsers() == null) {
            film.setLikesByUsers(new HashSet<>());
        }
        films.put(filmId, film);
        log.debug("фильм с id {} обновлен: {}", filmId, film);
        return film;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else {
            throw new NotFoundException();
        }
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

    public static void validate(Film film) {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        long duration = film.getDuration();
        if (name == null || name.isEmpty()) {
            log.debug("название не может быть пустым");
            throw new ValidationException();
        } else if (description.length() > 200) {
            log.debug("максимальная длина описания — 200 символов, {}", name);
            throw new ValidationException();
        } else if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("дата релиза — не раньше 28 декабря 1895 года, {}", name);
            throw new ValidationException();
        } else if (duration < 0) {
            log.debug("продолжительность фильма должна быть положительной, {}", name);
            throw new ValidationException();
        }
    }

    private int compare(Film f0, Film f1) {
        return f1.getLikesByUsers().size() - f0.getLikesByUsers().size();
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return films.values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

}
