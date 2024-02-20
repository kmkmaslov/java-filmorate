package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final RatingDbStorage ratingStorage;

    @Test
    void getFriends() {
        User user = User.builder()
                .name("name1")
                .email("test1@test.test")
                .login("login1")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        Optional<User> userOpt = Optional.ofNullable(userStorage.create(user));
        assertThat(userOpt).isPresent();

        List<User> emptyListOfFriends = userStorage.getFriends(userOpt.get().getId());
        assertNotNull(emptyListOfFriends);
        assertEquals(0, emptyListOfFriends.size());

        User friend = User.builder()
                .name("name2")
                .email("test2@test.test")
                .login("login2")
                .birthday(LocalDate.of(2000, 7, 7))
                .build();
        Optional<User> friendOpt = Optional.ofNullable(userStorage.create(friend));
        assertThat(friendOpt).isPresent();
        List<User> friends = userStorage.addToFriends(userOpt.get().getId(), friendOpt.get().getId());
        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals("name2", friends.get(0).getName());
    }

    @Test
    void getCommonFriends() {
        User user4 = User.builder()
                .name("name1")
                .email("test1@test.test")
                .login("login1")
                .birthday(LocalDate.of(2000, 4, 4))
                .build();
        Optional<User> user4Opt = Optional.ofNullable(userStorage.create(user4));
        assertThat(user4Opt).isPresent();

        User user5 = User.builder()
                .name("name2")
                .email("test2@test.test")
                .login("login2")
                .birthday(LocalDate.of(2000, 5, 5))
                .build();
        Optional<User> user5Opt = Optional.ofNullable(userStorage.create(user5));
        assertThat(user5Opt).isPresent();

        User user6 = User.builder()
                .name("name3")
                .email("test3@test.test")
                .login("login3")
                .birthday(LocalDate.of(2000, 6, 6))
                .build();
        Optional<User> user6Opt = Optional.ofNullable(userStorage.create(user6));
        assertThat(user6Opt).isPresent();


        userStorage.addToFriends(user4Opt.get().getId(), user5Opt.get().getId());
        List<User> friendsOfUser4 = userStorage.addToFriends(user4Opt.get().getId(), user6Opt.get().getId());
        assertNotNull(friendsOfUser4);
        assertEquals(2, friendsOfUser4.size());
    }

    @Test
    void getPopularFilm() {
        Film film1 = Film.builder()
                .name("zxfbfb")
                .description("123456789")
                .releaseDate(LocalDate.of(2222, 2, 2))
                .duration(100)
                .mpa(Rating.builder().id(3).build())
                .build();
        Optional<Film> film1Opt = Optional.ofNullable(filmStorage.create(film1));
        assertThat(film1Opt).isPresent();

        Film film2 = Film.builder()
                .name("zxfbfbzxfbfb")
                .description("123456789123456789")
                .releaseDate(LocalDate.of(3333, 2, 2))
                .duration(80)
                .mpa(Rating.builder().id(4).build())
                .build();
        Optional<Film> film2Opt = Optional.ofNullable(filmStorage.create(film2));
        assertThat(film2Opt).isPresent();

        User user1 = User.builder()
                .name("name1")
                .email("test1@test.test")
                .login("login1")
                .birthday(LocalDate.of(2010, 10, 10))
                .build();
        Optional<User> user1Opt = Optional.ofNullable(userStorage.create(user1));
        assertThat(user1Opt).isPresent();

        User user2 = User.builder()
                .name("name2")
                .email("test2@test.test")
                .login("login2")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        Optional<User> user2Opt = Optional.ofNullable(userStorage.create(user2));
        assertThat(user2Opt).isPresent();

        filmStorage.addLike(film1Opt.get().getId(), user1Opt.get().getId());
        filmStorage.addLike(film1Opt.get().getId(), user2Opt.get().getId());

        Film film1WithLike = filmStorage.getFilmById(film1Opt.get().getId());
        Set<Integer> likesByUsersFilm1 = film1WithLike.getLikesByUsers();
        assertNotNull(likesByUsersFilm1);
        assertEquals(2, likesByUsersFilm1.size());
        assertTrue(likesByUsersFilm1.contains(user1Opt.get().getId()));
        assertTrue(likesByUsersFilm1.contains(user2Opt.get().getId()));

        filmStorage.addLike(film2Opt.get().getId(), user1Opt.get().getId());

        Film film2WithLike = filmStorage.getFilmById(film2Opt.get().getId());
        Set<Integer> likesByUsersFilm2 = film2WithLike.getLikesByUsers();
        assertNotNull(likesByUsersFilm2);
        assertEquals(1, likesByUsersFilm2.size());
        assertTrue(likesByUsersFilm2.contains(user1Opt.get().getId()));

        List<Film> popularFilm = filmStorage.getPopularFilms(1);
        assertNotNull(popularFilm);
        assertEquals(1, popularFilm.size());
        assertTrue(popularFilm.contains(filmStorage.getFilmById(film1Opt.get().getId())));
    }

    @Test
    void getGenre() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.findGenreById(1));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    void getGenres() {
        List<Genre> genres = genreStorage.findAll();
        assertNotNull(genres);
        assertEquals(6, genres.size());
    }

    @Test
    void getMPARatings() {
        List<Rating> ratings = ratingStorage.findAll();
        assertNotNull(ratings);
        assertEquals(5, ratings.size());
    }

    @Test
    void getMPA() {
        Optional<Rating> ratingOptional = Optional.ofNullable(ratingStorage.findRatingById(5));
        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(rating -> assertThat(rating).hasFieldOrPropertyWithValue("id", 5))
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("name", "NC-17"));
    }

}