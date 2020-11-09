package robhopkins.wc.professors.iam.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class UnauthenticatedException extends IAMException {
    public UnauthenticatedException() {
        super("User is unauthorized.");
    }

    @Override
    protected int status() {
        return HttpResponseStatus.UNAUTHORIZED.code();
    }
}
