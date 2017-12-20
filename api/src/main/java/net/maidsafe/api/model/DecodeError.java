package net.maidsafe.api.model;

import net.maidsafe.safe_app.FfiResult;
import net.maidsafe.utils.Helper;

public class DecodeError extends DecodeResult {
    private Exception exception;

    public DecodeError(int reqId, FfiResult result) {
        super(reqId);
        exception = Helper.ffiResultToException(result);
    }

    public Exception getException() {
        return exception;
    }
}
