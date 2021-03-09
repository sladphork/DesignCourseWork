package robhopkins.wc.iam.sheet;

import robhopkins.wc.iam.user.domain.UserId;

public interface Token {
    Token EMPTY = new Token() {
        @Override
        public String toJson() {
            return "{}";
        }

        @Override
        public UserId userId() {
            return UserId.from("");
        }
    };

    String toJson();
    UserId userId();
    // Add isEmpty/valid?
//    "iss": "Wylie College",
//        "sub": [username],
//        "name": [user's name],
//        "role": [student, registrar, professor],
//        "exp": [ISO 8601 timestamp]
}
