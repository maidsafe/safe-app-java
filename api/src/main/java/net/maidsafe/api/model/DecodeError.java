package net.maidsafe.api.model;

import net.maidsafe.safe_app.FfiResult;
import net.maidsafe.utils.Helper;

public class DecodeError extends DecodeResult {
    private final Exception exception;


    public DecodeError(final int reqId, final FfiResult result) {
        super(reqId);
        exception = Helper.ffiResultToException(result);
    }

    public Exception getException() {
        return exception;
    }
}
