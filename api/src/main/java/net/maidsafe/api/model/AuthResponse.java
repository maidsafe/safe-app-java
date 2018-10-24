package net.maidsafe.api.model;

import net.maidsafe.safe_app.AuthGranted;

public class AuthResponse extends DecodeResult {
    private final AuthGranted authGranted;

    public AuthResponse(final int reqId, final AuthGranted authGranted) {
        super(reqId);
        this.authGranted = authGranted;
    }

    public AuthGranted getAuthGranted() {
        return authGranted;
    }
}
