package net.maidsafe.binding;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class BindingFactory implements Cloneable {

	private static BindingFactory factory;
	private final String LIB_NAME = "safe_app";
	private final String WIN_DEP_LIB = "libwinpthread-1";
	private final AuthBinding auth;
	private final CryptoBinding crypto;
	private final CipherOptBinding cipherOpt;
	private final ImmutableDataBinding immutableData;
	private final MutableDataBinding mutableData;
	private final AccessContainerBinding accessContainer;

	private BindingFactory() {
		final String OS = System.getProperty("os.name").toLowerCase();
		if (OS.contains("win")) {
			NativeLibrary.getInstance(WIN_DEP_LIB);
		}

		NativeLibrary.getInstance(LIB_NAME);
		auth = Native.loadLibrary(AuthBinding.class);
		crypto = Native.loadLibrary(CryptoBinding.class);
		cipherOpt = Native.loadLibrary(CipherOptBinding.class);
		immutableData = Native.loadLibrary(ImmutableDataBinding.class);
		mutableData = Native.loadLibrary(MutableDataBinding.class);
		accessContainer = Native.loadLibrary(AccessContainerBinding.class);
	}

	public static synchronized BindingFactory getInstance() {
		if (factory == null) {
			factory = new BindingFactory();
		}
		return factory;
	}

	public AuthBinding getAuth() {
		return auth;
	}

	public CryptoBinding getCrypto() {
		return crypto;
	}

	public CipherOptBinding getCipherOpt() {
		return cipherOpt;
	}

	public ImmutableDataBinding getImmutableData() {
		return immutableData;
	}

	public AccessContainerBinding getAccessContainer() {
		return accessContainer;
	}

	public MutableDataBinding getMutableData() {
		return mutableData;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
