package robhopkins.wc.iam.request.users;

import io.quarkus.runtime.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.UsersFactory;
import robhopkins.wc.iam.user.domain.UserId;
import robhopkins.wc.iam.user.domain.Username;
import robhopkins.wc.iam.user.exception.UserNotFoundException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.function.Predicate;

@Startup
@ApplicationScoped
public class RequestUsers implements Users {

    private final Users target;
    private final UserEvents events;
    @ConfigProperty(name = "wc.mq.host")
    String mqHost;

    public RequestUsers() {
        target = UsersFactory.newFactory().create();
        events = new UserEvents(target);
    }

    @Override
    public User get(final UserId id) throws UserNotFoundException, IAMException {
        return target.get(id);
    }

    @Override
    public User get(final Username username) throws UserNotFoundException, IAMException {
        return target.get(username);
    }

    @Override
    public Collection<User> find(final Predicate<User> filter) throws IAMException {
        return target.find(filter);
    }

    @Override
    public Collection<User> getAll() throws IAMException {
        return target.getAll();
    }

    @Override
    public User add(final User user) throws IAMException {
        return target.add(user);
    }

    @PostConstruct
    void start() {
        events.start(mqHost);
    }
}
