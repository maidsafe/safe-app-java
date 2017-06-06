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
	private final long mDataEntriesHandle;

	public MutableData(Pointer appHandle, long mDataInfoHandle, long mDataEntriesHandle) {
		this.appHandle = appHandle;
		this.mDataInfoHandle = mDataInfoHandle;
		this.mDataEntriesHandle = mDataEntriesHandle;
		this.mDataBinding = BindingFactory.getInstance().getMutableData();
		this.callbackHelper = CallbackHelper.getInstance();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mDataBinding.mdata_info_free(appHandle, mDataInfoHandle, Pointer.NULL, callbackHelper.getResultCallBack(null));
        mDataBinding.mdata_entries_free(appHandle, mDataEntriesHandle, Pointer.NULL, callbackHelper.getResultCallBack(null));
        mDataBinding.mdata_keys_free(appHandle, mDataEntriesHandle, Pointer.NULL, callbackHelper.getResultCallBack(null));
        mDataBinding.mdata_values_free(appHandle, mDataEntriesHandle, Pointer.NULL, callbackHelper.getResultCallBack(null));
    }

}
