package robhopkins.wc.iam.signin;

import robhopkins.wc.iam.user.domain.UserId;

public interface Token {
    String toJson();
    UserId userId();
    // Add isEmpty/valid?
//    "iss": "Wylie College",
//        "sub": [username],
//        "name": [user's name],
//        "role": [student, registrar, professor],
//        "exp": [ISO 8601 timestamp]
}
