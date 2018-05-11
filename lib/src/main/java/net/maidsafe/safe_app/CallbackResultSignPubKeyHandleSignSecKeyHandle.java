package net.maidsafe.safe_app;

public interface CallbackResultSignPubKeyHandleSignSecKeyHandle {
	public void call(FfiResult result, long pkH, long skH);
}
