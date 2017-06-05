package net.maidsafe.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

import net.maidsafe.binding.model.AuthUriResponse;
import net.maidsafe.binding.model.FfiCallback;
import net.maidsafe.binding.model.FfiResult.ByVal;
import net.maidsafe.api.Exception;

public class CallbackHelper implements Cloneable {

	private final Map<Integer, Callback> callbackPool;
	private static CallbackHelper instance;

	private CallbackHelper() {
		callbackPool = new HashMap<>();
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


	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
