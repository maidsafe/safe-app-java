package net.maidsafe.binding;

import net.maidsafe.binding.model.FfiCallback.BooleanCallback;
import net.maidsafe.binding.model.FfiCallback.DataCallback;
import net.maidsafe.binding.model.FfiCallback.HandleCallback;
import net.maidsafe.binding.model.FfiCallback.ResultCallback;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface AccessContainerBinding extends Library {

	void access_container_refresh_access_info(Pointer app, Pointer userData,
			ResultCallback cb);

	void access_container_get_names(Pointer app, Pointer userData,
			DataCallback cb);

	void access_container_get_container_mdata_info(Pointer app, String name,
			Pointer userData, HandleCallback cb);

	void access_container_is_permitted(Pointer app, String name,
			int permission, Pointer userData, BooleanCallback cb);

}
