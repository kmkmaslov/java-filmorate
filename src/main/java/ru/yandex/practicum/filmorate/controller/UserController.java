package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @GetMapping("/users")
    public List<User> get() {
        log.debug("количество пользователей: {}", users.size());
        return users.values().parallelStream().collect(Collectors.toList());
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        boolean isCorrect = validate(user);
        if (!isCorrect) {
            throw new ValidationException();
        }
        String name = user.getName();
        String login = user.getLogin();
        if (name == null || name.isEmpty()) {
            user.setName(login);
            log.debug("новое имя {} для {}", user.getName(), login);
        }
        user.setId(++this.id);
        users.put(user.getId(), user);
        log.debug("новый пользователь: {}", user);
        return user;
    }

    public static boolean validate(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        if (email == null || !email.contains("@")) {
            log.debug("Электронная почта не указана или не указан символ '@'");
            return false;
        } else if (login == null || login.isEmpty() || login.contains(" ")) {
            log.debug("Логин пользователя с электронной почтой {} не указан или содержит пробел", email);
            return false;
        } else if (birthday.isAfter(LocalDate.now())) {
            log.debug("Дата рождения пользователя с логином {} указана будущим числом", login);
            return false;
        }
        return true;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            log.debug("Не найден, {}", userId);
            throw new ValidationException();
        }
        users.put(userId, user);
        log.debug("данные {} для {} обновлены", user, userId);
        return user;
    }
}