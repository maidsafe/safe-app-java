package net.maidsafe.utils;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import net.maidsafe.api.Exception;
import net.maidsafe.api.model.*;
import net.maidsafe.binding.model.AuthUriResponse;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.binding.model.FfiResult.ByVal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CallbackHelper implements Cloneable {

    private final Map<Integer, Callback> callbackPool;
    private final Map<Integer, Long> forEachCounter;
    private static CallbackHelper instance;

    private CallbackHelper() {
        callbackPool = new HashMap<>();
        forEachCounter = new HashMap<>();
    }

    public static synchronized CallbackHelper getInstance() {
        if (instance == null) {
            instance = new CallbackHelper();
        }
        return instance;
    }

    private synchronized void addToPool(Callback cb) {
        callbackPool.put(cb.hashCode(), cb);
    }

    private synchronized void removeFromPool(Callback cb) {
        callbackPool.put(cb.hashCode(), cb);
    }

    public FfiCallback.Auth getAuthCallback(
            final CompletableFuture<AuthUriResponse> future) {
        final FfiCallback.Auth cb = new FfiCallback.Auth() {

            @Override
            public void onResponse(Pointer userData, ByVal result, int reqId,
                                   String uri) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(new AuthUriResponse(reqId, uri));
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.ResultCallback getResultCallBack(
            final CompletableFuture<Void> future) {
        final FfiCallback.ResultCallback cb = new FfiCallback.ResultCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result) {
                removeFromPool(this);
                if (future == null) {
                    return;
                }
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(null);
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.HandleCallback getHandleCallBack(
            final CompletableFuture<Long> future) {
        final FfiCallback.HandleCallback cb = new FfiCallback.HandleCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result, long handle) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(handle);
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.TwoHandleCallback getTwoHandleCallBack(
            final CompletableFuture<List<Long>> future) {
        final FfiCallback.TwoHandleCallback cb = new FfiCallback.TwoHandleCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result,
                                   long handleOne, long handleTwo) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(Arrays.asList(handleOne, handleTwo));
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.PointerCallback getPointerCallback(
            final CompletableFuture<Pointer> future) {
        final FfiCallback.PointerCallback cb = new FfiCallback.PointerCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result,
                                   Pointer pointer) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(pointer);
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.BooleanCallback getBooleanCallback(
            final CompletableFuture<Boolean> future) {
        final FfiCallback.BooleanCallback cb = new FfiCallback.BooleanCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result, boolean flag) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(flag);
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.DataCallback getDataCallback(
            final CompletableFuture<byte[]> future) {
        final FfiCallback.DataCallback cb = new FfiCallback.DataCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result,
                                   Pointer data, long dataLen) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(data.getByteArray(0, (int) dataLen));
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.DataWithVersionCallback getDataWithVersionCallback(
            final CompletableFuture<byte[]> future) {
        final FfiCallback.DataWithVersionCallback cb = new FfiCallback.DataWithVersionCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result,
                                   Pointer data, long dataLen, long version) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(data.getByteArray(0, (int) dataLen));
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.DataCallback getStringArrayCallback(
            final CompletableFuture<List<String>> future) {
        final FfiCallback.DataCallback cb = new FfiCallback.DataCallback() {

            @Override
            public void onResponse(Pointer userData, ByVal result,
                                   Pointer data, long dataLen) {
                removeFromPool(this);
                if (result.isError()) {
                    future.completeExceptionally(new Exception(result));
                    return;
                }
                future.complete(Arrays.asList(data.getStringArray(0,
                        (int) dataLen)));
            }
        };
        addToPool(cb);
        return cb;
    }

    public FfiCallback.ForEachPermissionsCallback getForEachPermissionsCallback(MDataPermissions.ForEachCallback iteration, long size) {
        final FfiCallback.ForEachPermissionsCallback forEachPermission = new FfiCallback.ForEachPermissionsCallback() {
            @Override
            public void onResponse(Pointer userData, long signKeyHandle, long permissionSetHandle) {
                iteration.onData(new MDataPermission(signKeyHandle, permissionSetHandle));
                forEachCounter.put(this.hashCode(), forEachCounter.get(this.hashCode()) + 1);
                if (forEachCounter.get(this.hashCode()) == size) {
                    iteration.completed();
                    removeFromPool(this);
                    forEachCounter.remove(this.hashCode());
                }
            }
        };
        forEachCounter.put(forEachPermission.hashCode(), 0L);
        addToPool(forEachPermission);

        return forEachPermission;
    }

    public FfiCallback.ForEachValuesCallback getForEachValueCallback(MDataValues.ForEachCallback iteration, long size) {
        final FfiCallback.ForEachValuesCallback forEachValue = new FfiCallback.ForEachValuesCallback() {
            @Override
            public void onResponse(Pointer userData, Pointer value, long valueLen, long version) {
                iteration.onData(new MDataValue(value, valueLen, valueLen));
                forEachCounter.put(this.hashCode(), forEachCounter.get(this.hashCode()) + 1);
                if (forEachCounter.get(this.hashCode()) == size) {
                    iteration.completed();
                    removeFromPool(this);
                    forEachCounter.remove(this.hashCode());
                }
            }
        };
        forEachCounter.put(forEachValue.hashCode(), 0L);
        addToPool(forEachValue);

        return forEachValue;
    }

    public FfiCallback.ForEachKeysCallback getForEachKeyCallback(MDataKeys.ForEachCallback iteration, long size) {
        final FfiCallback.ForEachKeysCallback forEachKey = new FfiCallback.ForEachKeysCallback() {
            @Override
            public void onResponse(Pointer userData, Pointer key, long keyLen) {
                iteration.onData(new MDataKey(key, keyLen));
                forEachCounter.put(this.hashCode(), forEachCounter.get(this.hashCode()) + 1);
                if (forEachCounter.get(this.hashCode()) == size) {
                    iteration.completed();
                    removeFromPool(this);
                    forEachCounter.remove(this.hashCode());
                }
            }
        };
        forEachCounter.put(forEachKey.hashCode(), 0L);
        addToPool(forEachKey);

        return forEachKey;
    }

    public FfiCallback.ForEachCallback getForEachEntryCallback(MDataEntries.ForEachCallback iteration, long size) {
        final FfiCallback.ForEachCallback forEachEntry = new FfiCallback.ForEachCallback() {
            @Override
            public void onResponse(Pointer userData, Pointer key, long keyLen, Pointer data, long dataLen, long version) {
                iteration.onData(new MDataEntry(key, keyLen, data, dataLen));
                forEachCounter.put(this.hashCode(), forEachCounter.get(this.hashCode()) + 1);
                if (forEachCounter.get(this.hashCode()) == size) {
                    iteration.completed();
                    removeFromPool(this);
                    forEachCounter.remove(this.hashCode());
                }
            }
        };
        forEachCounter.put(forEachEntry.hashCode(), 0L);
        addToPool(forEachEntry);

        return forEachEntry;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
