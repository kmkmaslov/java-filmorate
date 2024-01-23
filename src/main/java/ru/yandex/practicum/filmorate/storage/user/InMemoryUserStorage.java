package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @Override
    public List<User> get() {
        log.debug("количество пользователей: {}", users.size());
        return users.values().parallelStream().collect(Collectors.toList());
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

    @Override
    public User create(User user) {
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

    @Override
    public User update(User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            log.debug("Не найден, {}", userId);
            throw new ValidationException();
        }
        users.put(userId, user);
        log.debug("данные {} для {} обновлены", user, userId);
        return user;
    }

    @Override
    public User getUserById(Integer userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public List<User> addToFriends(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public void deleteFromFriends(Integer userId, Integer friendId) {

    }

    @Override
    public List<User> getFriends(Integer userId) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        return null;
    }
}
