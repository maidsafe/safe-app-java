package net.maidsafe.model;

public class MDataValue {
    private byte[] value;
    private long version;

    public MDataValue(byte[] value, long version) {
        this.value = value;
        this.version = version;
    }

    public byte[] getValue() {
        return value;
    }

    public long getVersion() {
        return version;
    }
}
