package net.maidsafe.api.model;

import com.sun.jna.Pointer;

import java.util.Date;

/**
 * Created by expertonetechnologies on 08/06/17.
 */
public class NfsFile {
    private Pointer filePtr;
    private long version;

    public NfsFile(Pointer filePtr, long version) {
        this.filePtr = filePtr;
        this.version = version;
    }

    public Pointer getFilePtr() {
        return filePtr;
    }

    public long getVersion() {
        return version;
    }
}
