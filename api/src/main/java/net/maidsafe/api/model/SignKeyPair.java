package net.maidsafe.api.model;

public class SignKeyPair {
    private NativeHandle publicSignKey;
    private NativeHandle secretSignKey;

    public SignKeyPair(NativeHandle publicSignKey, NativeHandle secretSignKey) {
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
