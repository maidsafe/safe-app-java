package net.maidsafe.api.model;

import net.maidsafe.safe_authenticator.AuthReq;

public class AuthIpcRequest extends IpcRequest {

    private final AuthReq authReq;

    public AuthReq getAuthReq() {
        return authReq;
    }

    public AuthIpcRequest(final int reqId, final AuthReq authReq) {
        super(reqId);
        this.authReq = authReq;
    }

}
