package net.maidsafe.api.model;

import com.sun.jna.Pointer;
import net.maidsafe.binding.BindingFactory;
import net.maidsafe.binding.MutableDataBinding;
import net.maidsafe.utils.CallbackHelper;

public class MutableData {

	private final Pointer appHandle;
	private final long mDataInfoHandle;
	private final CallbackHelper callbackHelper;
	private final MutableDataBinding mDataBinding;

	public MutableData(Pointer appHandle, long mDataInfoHandle) {
		this.appHandle = appHandle;
		this.mDataInfoHandle = mDataInfoHandle;
		this.callbackHelper = CallbackHelper.getInstance();
		this.mDataBinding = BindingFactory.getInstance().getMutableData();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mDataBinding.mdata_info_free(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getResultCallBack(null));
	}

}
