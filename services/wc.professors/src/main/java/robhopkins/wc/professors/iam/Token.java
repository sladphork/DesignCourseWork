package robhopkins.wc.professors.iam;

import org.json.JSONObject;

import java.util.Base64;

public final class Token {
    public static final Token EMPTY = new Token(new JSONObject());

    public static Token from(final String json) {
        final JSONObject obj = new JSONObject(
            new String(Base64.getDecoder().decode(json))
        );
        return new Token(obj);
    }

    private final JSONObject json;

    private Token(final JSONObject json) {
        this.json = json;
    }

    public String username() {
        return json.getString("sub");
    }

    public boolean isValid(final String role) {
        return true;
        // TODO: Re-enable once we're happy with everything.
//        return role.equalsIgnoreCase(json.getString("role"))
//            && Instant.now().isBefore(Instant.parse(json.getString("exp")));
    }
}
