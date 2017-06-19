package net.maidsafe.api.model;

/**
 * Created by expertonetechnologies on 13/06/17.
 */
public class NameAndTag {
    private byte[] name;
    private long typeTag;

    public NameAndTag(byte[] name, long typeTag) {
        this.name = name;
        this.typeTag = typeTag;
    }

    public byte[] getName() {
        return name;
    }

    public long getTypeTag() {
        return typeTag;
    }
}
