package net.maidsafe.api.model;

import com.sun.jna.Pointer;

public class MDataValue {
    private Pointer value;
    private long length;
    private long version;

    public MDataValue(Pointer value, long length, long version) {
        this.value = value;
        this.length = length;
        this.version = version;
    }

    public byte[] getValue(){
        return value.getByteArray(0, (int)this.length);
    }

    public long getVersion(){
        return this.version;
    }
}
