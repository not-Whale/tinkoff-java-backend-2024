package edu;

import edu.java.user.State;
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

    public boolean createUser(long userId) {
        if (!hasUser(userId)) {
            users.put(userId, new User(userId));
            return true;
        }
        return false;
    }

    public boolean setUserState(long userId, State state) {
        if (hasUser(userId)) {
            users.get(userId).setState(state);
            return true;
        }
        return false;
    }

    public State getUserState(long userId) {
        return hasUser(userId) ? users.get(userId).getState() : null;
    }

    public boolean trackLinkForUser(long userId, String link) {
        if (hasUser(userId)) {
            return users.get(userId).track(link);
        }
        return false;
    }

    public boolean untrackLinkForUser(long userId, String link) {
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
