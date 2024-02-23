package edu.java.bot.db.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import static edu.java.bot.db.user.State.NEW_USER;

@Getter
public class User {
    private final long userId;

    @Setter private State state;

    private final Set<String> trackList;

    public User(long userId) {
        this(userId, NEW_USER, new String[] {});
    }

    public User(long userId, @NonNull State state) {
        this(userId, state, new String[] {});
    }

    public User(long userId, @NonNull State state, @NonNull String[] trackList) {
        this.userId = userId;
        this.state = state;
        this.trackList = new HashSet<>();
        this.trackList.addAll(List.of(trackList));
    }

    public boolean track(@NonNull String link) {
        return this.trackList.add(link);
    }

    public boolean untrack(@NonNull String link) {
        return this.trackList.remove(link);
    }

    public String[] getLinks() {
        return this.trackList.toArray(String[]::new);
    }
}
