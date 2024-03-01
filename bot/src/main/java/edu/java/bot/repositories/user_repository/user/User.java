package edu.java.bot.repositories.user_repository.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import static edu.java.bot.repositories.user_repository.user.State.NOT_REGISTERED;

public class User {
    private final long userId;

    @Setter
    @Getter
    private State state;

    @Getter
    private final Set<String> trackList;

    public User(long userId) {
        this(userId, NOT_REGISTERED);
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

    public List<String> getLinks() {
        return this.trackList.stream().toList();
    }
}
