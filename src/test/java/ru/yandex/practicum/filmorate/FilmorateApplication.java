package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplication {

	@Test
	void validFilm() {
		Film film = Film.builder()
				.name("zxfbfb")
				.description("1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 ")
				.releaseDate(LocalDate.of(1895, 12, 28))
				.duration(0)
				.build();
		//assertTrue(FilmController.validate(film));
	}

	@Test
	void invalidFilmName() {
		Film film = Film.builder()
				.description("1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 ")
				.releaseDate(LocalDate.of(1896, 12, 28))
				.duration(120)
				.build();
		//assertFalse(FilmController.validate(film));
	}


	@Test
	void invalidFilmDescription() {
		Film film = Film.builder()
				.name("dsfbsdfbbdfsdfb")
				.description("123456789".repeat(201))
				.releaseDate(LocalDate.of(2222, 12, 22))
				.duration(120)
				.build();
		//assertFalse(FilmController.validate(film));
	}

	@Test
	void invalidFilmDate() {
		Film film = Film.builder()
				.name("fsdhsdfhsdfh")
				.description("1234567890 1234567890 1234567890 1234567890 1234567890")
				.releaseDate(LocalDate.of(1111, 11, 11))
				.duration(120)
				.build();
		//assertFalse(FilmController.validate(film));
	}

	@Test
	void invalidFilmLength() {
		Film film = Film.builder()
				.name("fsdhsdfhsdfh")
				.description("1234567890 1234567890 1234567890 1234567890 1234567890")
				.releaseDate(LocalDate.of(1896, 12, 28))
				.duration(-120)
				.build();
		//assertFalse(FilmController.validate(film));
	}

	@Test
	void invalidUser() {
		User user = User.builder()
				.email("my@email.com")
				.login("Login")
				.birthday(LocalDate.of(2000, 11, 11))
				.build();
		//assertTrue(UserController.validate(user));
	}

	@Test
	void invalidEmail() {
		User user = User.builder()
				.email("test.com")
				.login("Login")
				.birthday(LocalDate.of(2000, 11, 11))
				.build();
		//assertFalse(UserController.validate(user));
	}

	@Test
	void validUser() {
		User user = User.builder()
				.email("test@test.com")
				.login("Log in")
				.birthday(LocalDate.of(2000, 11, 11))
				.build();
		//assertFalse(UserController.validate(user));
	}

	@Test
	void invalidUserBirthday() {
		User user = User.builder()
				.email("test@test.com")
				.login("Login")
				.birthday(LocalDate.of(2222, 11, 11))
				.build();
		//assertFalse(UserController.validate(user));
	}
}