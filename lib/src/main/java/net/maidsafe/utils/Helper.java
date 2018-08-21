package net.maidsafe.utils;

import net.maidsafe.safe_app.FfiResult;

import java.util.concurrent.CompletionException;

public class Helper {
    public static Exception ffiResultToException(FfiResult result) {
        return new CompletionException(new Exception(result.getDescription() + " : " + result.getErrorCode()));
    }

    public static Exception ffiResultToException(net.maidsafe.safe_authenticator.FfiResult result) {
        return new CompletionException(new Exception(result.getDescription() + " : " + result.getErrorCode()));
    }
}
