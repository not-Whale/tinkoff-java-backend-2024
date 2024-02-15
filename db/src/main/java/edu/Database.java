package edu;

import edu.java.user.User;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Map<Long, User> users;

    public Database() {
        this.users = new HashMap<>();
    }

    public boolean hasUser(long userId) {
        return users.containsKey(userId);
    }

    public boolean hasUser(User user) {
        return users.containsValue(user);
    }

    public boolean createUser(User user) {
        if (!hasUser(user)) {
            users.put(user.getUserId(), user);
            return true;
        }
        return false;
    }

    public boolean createUser(long userId) {
        if (!hasUser(userId)) {
            users.put(userId, new User(userId));
            return true;
        }
        return false;
    }

    public boolean trackLinkForUser(User user, String link) {
        if (hasUser(user)) {
            return users.get(user.getUserId()).track(link);
        }
        return false;
    }

    public boolean trackLinkForUser(long userId, String link) {
        if (hasUser(userId)) {
            return users.get(userId).track(link);
        }
        return false;
    }

    public boolean untrackLinkForUser(User user, String link) {
        if (hasUser(user)) {
            return users.get(user.getUserId()).untrack(link);
        }
        return false;
    }

    public boolean untrackLinkForUser(long userId, String link) {
        if (hasUser(userId)) {
            return users.get(userId).untrack(link);
        }
        return false;
    }
}
