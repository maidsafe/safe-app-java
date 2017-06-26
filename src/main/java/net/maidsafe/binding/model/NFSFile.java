package net.maidsafe.binding.model;

import com.sun.jna.Structure;
import net.maidsafe.api.model.XorName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by expertonetechnologies on 14/06/17.
 */
public class NFSFile extends Structure {

    public long size;
    public long created_sec;
    public long modified_sec;
    public long created_nsec;
    public long modified_nsec;
    public byte[] userMetaData;
    public XorName dataMapName;
    public long userMetaDataLen;
    public long userMetaDataCap;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("size", "created_sec", "created_nsec", "modified", "modified_nsec", "userMetaData", "dataMapName", "userMetaDataLen", "userMetaDataCap");
    }
}
