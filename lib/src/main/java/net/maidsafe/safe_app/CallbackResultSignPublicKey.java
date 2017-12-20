package net.maidsafe.safe_app;

public interface CallbackResultSignPublicKey {
	public void call(FfiResult result, byte[] pubSignKey);
}
