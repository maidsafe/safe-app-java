package net.maidsafe.safe_app;

/// FFI result wrapper
public class FfiResult {
    private int errorCode;
    private String description;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final int val) {
        errorCode = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String val) {
        description = val;
    }

}

