package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static FilmController controller;
    private static Film film;
    private static Film invalidFimName;
    private static Film invalidFilmDescription;
    private static Film invalidFilmDate;
    private static Film invalidFilmDuration;

    @BeforeAll
    static void beforeAll() {
        invalidFimName = Film.builder()
                .description("123456789".repeat(10))
                .releaseDate(LocalDate.of(1896, 12, 28))
                .duration(120)
                .build();
        invalidFilmDescription = Film.builder()
                .name("zxfbfb")
                .description("123456789".repeat(201))
                .releaseDate(LocalDate.of(1896, 12, 28))
                .duration(120)
                .build();
        invalidFilmDate = Film.builder()
                .name("zxfbfb")
                .description("123456789".repeat(10))
                .releaseDate(LocalDate.of(1890, 12, 28))
                .duration(120)
                .build();
        invalidFilmDuration = Film.builder()
                .name("zxfbfb")
                .description("123456789".repeat(10))
                .releaseDate(LocalDate.of(1896, 12, 28))
                .duration(-120)
                .build();
    }

    @BeforeEach
    void init() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage(), new UserService(new InMemoryUserStorage())));
        film = Film.builder()
                .name("zxfbfb")
                .description("123456789".repeat(10))
                .releaseDate(LocalDate.of(1896, 12, 28))
                .duration(120)
                .build();
    }

    @Test
    void getListWithoutFilms() {
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(0, films.size());
    }

    @Test
    void getListWithFilms() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
    }

    @Test
    void filmWithInvalidName() {
        Throwable thrown = catchThrowable(() -> controller.create(invalidFimName));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void filmWithInvalidDescription() {
        Throwable thrown = catchThrowable(() -> controller.create(invalidFilmDescription));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void filmWithInvalidDate() {
        Throwable thrown = catchThrowable(() -> controller.create(invalidFilmDate));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void filmWithInvalidDuration() {
        Throwable thrown = catchThrowable(() -> controller.create(invalidFilmDuration));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void filmWithoutErrors() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
        film.setDuration(222);
        controller.update(film);
        assertNotNull(films);
        assertEquals(1, films.size());
    }

    @Test
    void createAndUpdateFilmWithInvalidName() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
        film.setName("");
        Throwable thrown = catchThrowable(() -> controller.update(film));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void createAndUpdateFilmWithInvalidDescription() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
        film.setDescription(String.valueOf(invalidFilmDescription));
        Throwable thrown = catchThrowable(() -> controller.update(film));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void createAndUpdateFilmWithInvalidDate() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
        film.setReleaseDate(LocalDate.of(1890, 12, 28));
        Throwable thrown = catchThrowable(() -> controller.update(film));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void createAndUpdateFilmWithInvalidDuration() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
        film.setDuration(-1);
        Throwable thrown = catchThrowable(() -> controller.update(film));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void createAndUpdateFilmWithInvalidId() {
        controller.create(film);
        List<Film> films = controller.get();
        assertNotNull(films);
        assertEquals(1, films.size());
        film.setId(2);
        Throwable thrown = catchThrowable(() -> controller.update(film));
        assertThat(thrown).isInstanceOf(NotFoundException.class);
    }
}
