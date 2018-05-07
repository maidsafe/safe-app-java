package net.maidsafe.safe_app;

public interface CallbackResultSEWriterHandle {
	public void call(FfiResult result, long seH);
}
