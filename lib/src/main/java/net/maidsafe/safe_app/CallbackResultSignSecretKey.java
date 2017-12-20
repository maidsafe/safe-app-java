package net.maidsafe.safe_app;

public interface CallbackResultSignSecretKey {
	public void call(FfiResult result, byte[] pubSignKey);
}
