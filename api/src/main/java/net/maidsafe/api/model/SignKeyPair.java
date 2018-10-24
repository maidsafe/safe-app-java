package net.maidsafe.api.model;

public class SignKeyPair {
    private final NativeHandle publicSignKey;
    private final NativeHandle secretSignKey;

    public SignKeyPair(final NativeHandle publicSignKey, final NativeHandle secretSignKey) {
        this.publicSignKey = publicSignKey;
        this.secretSignKey = secretSignKey;
    }

    public NativeHandle getPublicSignKey() {
        return publicSignKey;
    }

    public NativeHandle getSecretSignKey() {
        return secretSignKey;
    }
}
