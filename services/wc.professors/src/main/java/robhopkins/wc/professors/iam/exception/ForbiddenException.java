package robhopkins.wc.professors.iam.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public final class ForbiddenException extends IAMException {

    public ForbiddenException() {
        super("User is forbidden from performing this operation.");
    }
    @Override
    protected int status() {
        return HttpResponseStatus.FORBIDDEN.code();
    }
}
