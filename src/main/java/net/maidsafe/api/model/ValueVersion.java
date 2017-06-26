package net.maidsafe.api.model;

/**
 * Created by expertonetechnologies on 13/06/17.
 */
public class ValueVersion {
    private byte[] value;
    private long version;

    public ValueVersion(byte[] value, long version) {
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
