package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{
    @Override
    public List<User> get() {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getUserById(Integer userId) {
        return null;
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
