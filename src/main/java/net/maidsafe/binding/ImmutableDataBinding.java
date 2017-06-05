package net.maidsafe.binding;

import net.maidsafe.binding.model.FfiCallback.DataCallback;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.binding.model.FfiCallback.PointerCallback;
import net.maidsafe.binding.model.FfiCallback.ResultCallback;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface ImmutableDataBinding extends Library {

	void idata_new_self_encryptor(Pointer app, Pointer user, HandleCallback cb);

	void idata_write_to_self_encryptor(Pointer app, long handle, byte[] data,
			long dataLen, Pointer user, ResultCallback cb);

	void idata_close_self_encryptor(Pointer app, long writerHandle,
			long cipherOptHandle, Pointer userData, PointerCallback cb);

	void idata_fetch_self_encryptor(Pointer app, byte[] xorName,
			Pointer userData, HandleCallback cb);

	void idata_size(Pointer app, long handle, Pointer userData,
			HandleCallback cb);

	void idata_read_from_self_encryptor(Pointer app, long reader, long from,
			long to, Pointer user, DataCallback cb);

	void idata_self_encryptor_writer_free(Pointer app, long handle,
			Pointer userData, ResultCallback cb);

	void idata_self_encryptor_reader_free(Pointer app, long handle,
			Pointer userData, ResultCallback cb);
}
