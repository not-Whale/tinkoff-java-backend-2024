package edu.java.bot.repositories.user_repository;

import edu.java.bot.repositories.user_repository.user.State;
import edu.java.bot.repositories.user_repository.user.User;
import edu.java.bot.exceptions.NoSuchUserException;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class UserRepository {
    private static final String NO_SUCH_USER_MESSAGE = "Такого пользователя нет.";

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
        if (!hasUser(userId)) {
            throw new NoSuchUserException(NO_SUCH_USER_MESSAGE);
        }
        users.get(userId).setState(state);
    }

    public State getUserState(long userId) {
        if (!hasUser(userId)) {
            throw new NoSuchUserException(NO_SUCH_USER_MESSAGE);
        }
        return users.get(userId).getState();
    }

    public boolean trackLinkForUser(long userId, @NonNull String link) {
        if (!hasUser(userId)) {
            throw new NoSuchUserException(NO_SUCH_USER_MESSAGE);
        }
        return users.get(userId).track(link);
    }

    public boolean untrackLinkForUser(long userId, @NonNull String link) {
        if (!hasUser(userId)) {
            throw new NoSuchUserException(NO_SUCH_USER_MESSAGE);
        }
        return users.get(userId).untrack(link);
    }

    public String[] getUserLinks(long userId) {
        if (!hasUser(userId)) {
            throw new NoSuchUserException(NO_SUCH_USER_MESSAGE);
        }
        return users.get(userId).getLinks();
    }
}
