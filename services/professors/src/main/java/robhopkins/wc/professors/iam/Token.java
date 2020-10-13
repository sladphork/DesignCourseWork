package robhopkins.wc.professors.iam;

import io.vertx.core.json.JsonObject;

import java.time.Instant;

public interface Token {
    JsonObject toJson();

    //    "iss": "Wylie College",
//        "sub": [username],
//        "name": [user's name],
//        "role": [student, registrar, professor],
//        "exp": [ISO 8601 timestamp]
}
