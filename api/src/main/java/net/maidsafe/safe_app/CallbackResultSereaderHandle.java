package net.maidsafe.safe_app;

public interface CallbackResultSereaderHandle {
    public void call(FfiResult result, long seH);
}
