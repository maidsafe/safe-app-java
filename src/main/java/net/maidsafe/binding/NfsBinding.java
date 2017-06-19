package net.maidsafe.binding;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import net.maidsafe.api.model.NfsFile;
import net.maidsafe.binding.model.FfiCallback;

/**
 * Created by expertonetechnologies on 07/06/17.
 */
public interface NfsBinding extends Library {

    void file_fetch(Pointer appHandle, long infoHandle, String fileName, Pointer userData, FfiCallback.FileFetchCallback cb);

    void file_insert(Pointer appHandle, long infoHandle, String fileName, byte[] file, Pointer userData, FfiCallback.ResultCallback cb);

    void file_update(Pointer appHandle, long infoHandle, String fileName, byte[] file, long version, Pointer userData, FfiCallback.ResultCallback cb);
}
