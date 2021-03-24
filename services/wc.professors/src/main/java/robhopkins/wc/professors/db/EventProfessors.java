package robhopkins.wc.professors.db;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.jboss.logmanager.LogManager;
import org.json.JSONObject;
import robhopkins.wc.professors.Professor;
import robhopkins.wc.professors.Professors;
import robhopkins.wc.professors.domain.ObjectId;
import robhopkins.wc.professors.exception.ProfessorNotFoundException;
import robhopkins.wc.professors.exception.ServerException;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

// NOTE: Don't like this name; once we're done implementing it might change.
public class EventProfessors implements Professors {

    private final Professors target;
    public EventProfessors(final Professors target) {
        this.target = target;
    }
    @Override
    public Professor get(final ObjectId id) throws ProfessorNotFoundException, ServerException {
        return target.get(id);
    }

    @Override
    public Collection<Professor> getAll() throws ServerException {
        return target.getAll();
    }

    @Override
    public Professor add(final Professor professor) throws ServerException {
        final Professor added = target.add(professor);
        send(added);
        return added;
    }

    @Override
    public Professor update(final Professor professor) throws ProfessorNotFoundException, ServerException {
        final Professor updated = target.update(professor);
        // Send the event to mq;
        return updated;
    }

    @Override
    public void delete(final ObjectId id) {
        try {
            final Professor toDelete = get(id);
            target.delete(id);
            // Send the professor or just the id to mq
        } catch (ProfessorNotFoundException | ServerException e) {
            // We'll just log this.
            e.printStackTrace();
        }
    }

    private void send(final Professor professor) {
        try {
            LogManager.getLogManager().getLogger("EventProfessors").info("\nSending Professor.");
            final ConnectionFactory factory = new ConnectionFactory();
            // TODO: Make this configurable.
            factory.setHost("rabbitmq");

            final Connection connection = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare("professors", false, false, false, null);
            final AMQP.BasicProperties props = (new AMQP.BasicProperties.Builder())
                .contentType("application/json")
                .contentEncoding("UTF-8")
                .headers(Map.of("event", "added")) // TODO: Inject event
                .build();
            channel.basicPublish("", "professors", props,
                ProfessorForEvent.newInstance(professor).toBytes());
        } catch (Exception e) {
            LogManager.getLogManager().getLogger("EventProfssors").severe(
                "Isssues with sending: " + e.getMessage() + "\n\n"
            );
        }
    }


    private static final class ProfessorForEvent implements Professor.ProfessorPopulator {
        static ProfessorForEvent newInstance(final Professor professor) {
            return new ProfessorForEvent(professor);
        }
        private final JSONObject json;
        private final Professor professor;

        ProfessorForEvent(final Professor professor) {
            json = new JSONObject();
            this.professor = professor;
        }

        @Override
        public void firstName(final String value) {
            json.put("firstName", value);
        }

        @Override
        public void lastName(final String value) {
            json.put("lastName", value);
        }

        @Override
        public void id(final ObjectId id) {
            json.put("id", id.toString());
        }

        @Override
        public void departmentId(final ObjectId id) {
            // Temporary until we populate faculty
            json.put("departmentId", Optional.ofNullable(id).map(ObjectId::toString).orElse(""));
        }

        @Override
        public void email(final String value) {
            json.put("email", value);
        }

        byte[] toBytes() {
            return toString().getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public String toString() {
            professor.populate(this);
            return json.toString(2);
        }
    }
}
