package net.maidsafe.safe_app;

/// Represents the needed keys to work with the data.
public class AppKey {
    private byte[] ownerKey;
    private byte[] encKey;
    private byte[] signPk;
    private byte[] signSk;
    private byte[] encPk;
    private byte[] encSk;

    public byte[] getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(final byte[] val) {
        ownerKey = val;
    }

    public byte[] getEncKey() {
        return encKey;
    }

    public void setEncKey(final byte[] val) {
        encKey = val;
    }

    public byte[] getSignPk() {
        return signPk;
    }

    public void setSignPk(final byte[] val) {
        signPk = val;
    }

    public byte[] getSignSk() {
        return signSk;
    }

    public void setSignSk(final byte[] val) {
        signSk = val;
    }

    public byte[] getEncPk() {
        return encPk;
    }

    public void setEncPk(final byte[] val) {
        encPk = val;
    }

    public byte[] getEncSk() {
        return encSk;
    }

    public void setEncSk(final byte[] val) {
        encSk = val;
    }

}

