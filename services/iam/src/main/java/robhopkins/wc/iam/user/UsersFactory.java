package robhopkins.wc.iam.user;

import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class UsersFactory {
    public static UsersFactory newFactory() {
        return new UsersFactory();
    }

    private UsersFactory() {
    }

    public Users create() {
        return new CompositeUsers(
            create(RegistrarUsers.class),
            create(StudentUsers.class),
            create(ProfessorUsers.class)
        );
    }

    private Users create(final Class<? extends AbstractUsers> type) {
        try {
            final AbstractUsers users = type.getDeclaredConstructor(User[].class).newInstance((Object)new User[]{});
            users.load();
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final class CompositeUsers implements Users {

        private final Users[] targets;

        CompositeUsers(final Users... targets) {
            this.targets = targets;
        }

        @Override
        public User get(final UserId id) throws UserNotFoundException {
            return Arrays.stream(targets)
                .map(target -> getForMap(target, id))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
        }

        @Override
        public User get(final Username username) throws UserNotFoundException {
            return Arrays.stream(targets)
                .map(target -> getForMap(target, username))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(username));
        }

        @Override
        public Collection<User> find(final Predicate<User> filter) {
            return Arrays.stream(targets)
                .flatMap(target -> target.find(filter).stream())
                .collect(Collectors.toSet());
        }

        private User getForMap(final Users target, final UserId id) {
            try {
                return target.get(id);
            } catch (UserNotFoundException e) {
                return null;
            }
        }

        private User getForMap(final Users target, final Username username) {
            try {
                return target.get(username);
            } catch (UserNotFoundException e) {
                return null;
            }
        }
    }
}
