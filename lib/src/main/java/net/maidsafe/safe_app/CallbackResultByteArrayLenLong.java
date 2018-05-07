package net.maidsafe.safe_app;

public interface CallbackResultByteArrayLenLong {
	public void call(FfiResult result, byte[] content, long version);
}
