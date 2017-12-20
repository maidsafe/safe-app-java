package net.maidsafe.api.model;

public class MDataEntry {
    private byte[] key;
    private byte[] value;
    private long version;

    public MDataEntry(byte[] key, byte[] value, long version) {
        this.key = key;
        this.value = value;
        this.version = version;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public long getVersion() {
        return version;
    }
}
