package net.maidsafe.safe_app;

public interface CallbackResultSEReaderHandle {
	public void call(FfiResult result, long seH);
}
