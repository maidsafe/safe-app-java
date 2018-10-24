package net.maidsafe.api.model;

public class EncryptKeyPair {

    private final NativeHandle secretEncryptKey;
    private final NativeHandle publicEncryptKey;


    public EncryptKeyPair(final NativeHandle publicEncryptKey, final NativeHandle secretEncryptKey) {
        this.secretEncryptKey = secretEncryptKey;
        this.publicEncryptKey = publicEncryptKey;
    }

    public NativeHandle getPublicEncryptKey() {
        return publicEncryptKey;
    }

    public NativeHandle getSecretEncryptKey() {
        return secretEncryptKey;
    }
}
