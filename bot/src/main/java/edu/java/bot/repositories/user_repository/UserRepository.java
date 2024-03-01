package edu.java.bot.repositories.user_repository;

import edu.java.bot.exceptions.NoSuchUserException;
import edu.java.bot.repositories.user_repository.user.State;
import edu.java.bot.repositories.user_repository.user.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long, User> users;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public boolean hasUser(long userId) {
        return users.containsKey(userId);
    }

    public void createUserIfDoesNotExists(long userId) {
        if (!hasUser(userId)) {
            users.put(userId, new User(userId));
        }
    }

    public void setUserState(long userId, @NonNull State state) {
        userMustExists(userId);
        users.get(userId).setState(state);
    }

    public State getUserState(long userId) {
        userMustExists(userId);
        return users.get(userId).getState();
    }

    public boolean trackLinkForUser(long userId, @NonNull String link) {
        userMustExists(userId);
        return users.get(userId).track(link);
    }

    public boolean untrackLinkForUser(long userId, @NonNull String link) {
        userMustExists(userId);
        return users.get(userId).untrack(link);
    }

    public List<String> getUserLinks(long userId) {
        userMustExists(userId);
        return users.get(userId).getLinks();
    }

    private void userMustExists(long userId) {
        if (!hasUser(userId)) {
            throw new NoSuchUserException("Такого пользователя нет.");
        }
    }
}
