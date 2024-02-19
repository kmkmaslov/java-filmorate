package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> get() {
        List<User> users = new ArrayList<>();

        log.debug("количество пользователей: {}", users.size());
        return new ArrayList<>(users.size());
    }

    @Override
    public User create(User user) {

        log.debug("новый пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {

        log.debug("данные {} для {} обновлены", user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Integer userId) {

        log.info("Нет пользователя с id: {}", userId);
        throw new NotFoundException();
    }

    @Override
    public List<User> addToFriends(Integer userId, Integer friendId) {
        List<User> friends = new ArrayList<>();

        return friends;
    }

    @Override
    public void deleteFromFriends(Integer userId, Integer friendId) {

    }

    @Override
    public List<User> getFriends(Integer userId) {
        List<User> friends = new ArrayList<>();

        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        List<User> friends = new ArrayList<>();

        return friends;
    }

}
