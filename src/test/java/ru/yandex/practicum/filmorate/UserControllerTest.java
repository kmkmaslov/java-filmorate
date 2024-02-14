package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest {

    private static UserController controller;
    private static User user;
    private static User invalidEmail;
    private static User invalidLogin;
    private static User invalidBirthday;

    @BeforeAll
    static void beforeAll() {
        invalidEmail = User.builder()
                .email("test.test")
                .login("login")
                .birthday(LocalDate.of(2022, 12, 12))
                .build();
        invalidLogin = User.builder()
                .email("test@test.test")
                .login("124314 4214")
                .birthday(LocalDate.of(2022, 12, 12))
                .build();
        invalidBirthday = User.builder()
                .email("test@test.test")
                .login("login")
                .birthday(LocalDate.of(2023, 12, 12))
                .build();
    }

    @BeforeEach
    void init() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
        user = User.builder()
                .email("test@test.test")
                .login("login")
                .birthday(LocalDate.of(2022, 12, 12))
                .build();
    }

    @Test
    void getListWithoutUser() {
        List<User> users = controller.get();
        assertNotNull(users);
        assertEquals(0, users.size());
    }

    @Test
    void getListWithUser() {
        controller.create(user);
        List<User> users = controller.get();
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    void userWithInvalidEmail() {
        Throwable thrown = catchThrowable(() -> controller.create(invalidEmail));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void userWithInvalidLogin() {
        Throwable thrown = catchThrowable(() -> controller.create(invalidLogin));
        assertThat(thrown).isInstanceOf(ValidationException.class);
    }

    @Test
    void userWithoutError() {
        controller.create(user);
        List<User> users = controller.get();
        assertNotNull(users);
        assertEquals(1, users.size());
        user.setName("adsadg dasgsdg");
        controller.update(user);
        assertNotNull(users);
        assertEquals(1, users.size());
    }

}
