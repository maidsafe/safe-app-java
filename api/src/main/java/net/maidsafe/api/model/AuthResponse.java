package net.maidsafe.api.model;

import net.maidsafe.safe_app.AuthGranted;

public class AuthResponse extends DecodeResult {
    private AuthGranted authGranted;

    public AuthResponse(int reqId, AuthGranted authGranted) {
        super(reqId);
        this.authGranted = authGranted;
    }

    public AuthGranted getAuthGranted() {
        return authGranted;
    }
}
