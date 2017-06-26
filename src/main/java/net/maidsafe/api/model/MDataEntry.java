package net.maidsafe.api.model;

import com.sun.jna.Pointer;

public class MDataEntry {
    private Pointer key;
    private long keyLen;
    private Pointer data;
    private long dataLen;

    public MDataEntry(Pointer key, long keyLen, Pointer data, long dataLen) {
        this.key = key;
        this.keyLen =keyLen;
        this.data = data;
        this.dataLen = dataLen;
    }

    public byte[] getKey() {
        return key.getByteArray(0, (int)this.keyLen);
    }

    public byte[] getData() {
        return data.getByteArray(0, (int)this.dataLen);
    }
}
