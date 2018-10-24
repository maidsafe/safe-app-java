package net.maidsafe.api.model;

/***
 * Represents an Error in the Ipc Request
 */
public class IpcReqError extends IpcRequest {

    private final int code;
    private final String description;
    private final String message;

    public IpcReqError(final int code, final String description, final String message) {
        super();
        this.code = code;
        this.description = description;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

}
