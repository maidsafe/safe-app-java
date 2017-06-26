package net.maidsafe.api.model;

import com.sun.jna.Pointer;

public class MDataKey {
    private Pointer key;
    private long length;

    public MDataKey(Pointer key, long length) {
        this.key = key;
        this.length = length;
    }

    public byte[] getKey(){
        return key.getByteArray(0, (int)this.length);
    }
}
