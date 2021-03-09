package robhopkins.wc.iam.request.users;

import com.rabbitmq.client.*;
import org.jboss.logmanager.LogManager;
import org.json.JSONObject;
import robhopkins.wc.iam.request.exception.IAMException;
import robhopkins.wc.iam.user.Secrets;
import robhopkins.wc.iam.user.User;
import robhopkins.wc.iam.user.UserBuilder;
import robhopkins.wc.iam.user.Users;
import robhopkins.wc.iam.user.domain.Role;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

final class UserEvents {

    private final Users users;
    UserEvents(final Users users) {
        this.users = users;
    }

    void start(final String host) {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        try {
            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            bindProfessors(channel);
            bindStudents(channel);
            bindRegistrars(channel);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException("Unable to start Event Listening", e);
        }
    }


    private void bindProfessors(final Channel channel) throws IOException {
        channel.queueDeclare("professors", false, false, false, null);
        channel.basicConsume("professors", true, this::handleProfessors, consumerTag -> { });
    }

    private void bindStudents(final Channel channel) throws IOException {
        channel.queueDeclare("students", false, false, false, null);
        channel.basicConsume("students", true, this::handleStudents, consumerTag -> { });
    }

    private void bindRegistrars(final Channel channel) throws IOException {
        channel.queueDeclare("registrars", false, false, false, null);
        channel.basicConsume("registrars", true, this::handleRegistrars, consumerTag -> { });
    }

    private void handleProfessors(final String consumerTag, final Delivery message) throws IOException {
        message.getProperties().getHeaders().get("event");
        final String jsonBody = new String(message.getBody(), "UTF-8");
        LogManager.getLogManager().getLogger("IAM Events Processor").info(" [x] Received event '"+ message.getProperties().getHeaders().get("event")
            + "' with body: " + "'" + message + "'");

        final JSONObject json = new JSONObject(jsonBody);
        // TODO: This will work, but ugh!
        final String username = (json.getString("firstName").charAt(0) + json.getString("lastName")).toLowerCase();
        final User user = UserBuilder.newBuilder(json)
            .withUsername(username)
            .withRole(Role.PROFESSOR)
            .build();
        try {
            users.add(user);
            Secrets.newInstance().getFor(user).set();
        } catch (IAMException e) {

        }
    }

    private void handleStudents(final String consumerTag, final Delivery message) throws IOException {

    }

    private void handleRegistrars(final String consumerTag, final Delivery message) throws IOException {

    }
}
