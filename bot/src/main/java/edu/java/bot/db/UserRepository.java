package edu.java.bot.db;

import edu.java.bot.db.user.State;
import edu.java.bot.db.user.User;
import lombok.NonNull;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<Long, User> users;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public boolean hasUser(long userId) {
        return users.containsKey(userId);
    }

    public boolean createUser(long userId) {
        if (!hasUser(userId)) {
            users.put(userId, new User(userId));
            return true;
        }
        return false;
    }

    public boolean setUserState(long userId, @NonNull State state) {
        if (hasUser(userId)) {
            users.get(userId).setState(state);
            return true;
        }
        return false;
    }

    public State getUserState(long userId) {
        return hasUser(userId) ? users.get(userId).getState() : null;
    }

    public boolean trackLinkForUser(long userId, @NonNull String link) {
        if (hasUser(userId)) {
            return users.get(userId).track(link);
        }
        return false;
    }

    public boolean untrackLinkForUser(long userId, @NonNull String link) {
        if (hasUser(userId)) {
            return users.get(userId).untrack(link);
        }
        return false;
    }

    public String[] getUserLinks(long userId) {
        if (hasUser(userId)) {
            return users.get(userId).getLinks();
        }
        return new String[] {};
    }
}
