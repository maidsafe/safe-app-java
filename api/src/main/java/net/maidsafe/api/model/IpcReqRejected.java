package net.maidsafe.api.model;

/***
 * Represents a rejected Ipc Request
 */
public class IpcReqRejected extends IpcRequest {

    private final String message;

    public IpcReqRejected(final String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
