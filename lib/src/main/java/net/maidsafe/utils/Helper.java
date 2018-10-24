package net.maidsafe.utils;

import net.maidsafe.safe_app.FfiResult;

public final class Helper {
    private Helper() {
        throw new java.lang.RuntimeException("Cannot instantiate utilities classs");
    }
    public static Exception ffiResultToException(final FfiResult result) {
        return new Exception(result.getDescription() + " : " + result.getErrorCode());
    }

    public static Exception ffiResultToException(final net.maidsafe.safe_authenticator.FfiResult result) {
        return new Exception(result.getDescription() + " : " + result.getErrorCode());
    }
}
