package net.maidsafe.safe_app;

import java.util.concurrent.ExecutionException;

public interface CallbackResultAccountInfo {
    public void call(FfiResult result, AccountInfo accountInfo) throws ExecutionException;
}
