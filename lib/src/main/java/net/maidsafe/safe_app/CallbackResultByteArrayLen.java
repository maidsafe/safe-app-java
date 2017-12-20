package net.maidsafe.safe_app;

public interface CallbackResultByteArrayLen {
	public void call(FfiResult result, byte[] signedDataPtr);
}
