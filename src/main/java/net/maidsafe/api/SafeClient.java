package net.maidsafe.api;

import net.maidsafe.api.model.App;
import net.maidsafe.api.model.MutableData;

public class SafeClient {
	private final App app;
	private final Crypto crypto;
	private final CipherOption cipherOption;
	private final ImmutableData immutableData;
	private final Container accessContainer;
	private final MDataInfo mDataInfo;

	public SafeClient(App app) {
		this.app = app;
		crypto = new Crypto(app);
		cipherOption = new CipherOption(app);
		immutableData = new ImmutableData(app);
		accessContainer = new Container(app);
		mDataInfo = new MDataInfo(app);
	}

	public boolean isAnonymous() {
		return app.isAnonymous();
	}

	public Crypto crypto() {
		return crypto;
	}

	public CipherOption cipherOpt() {
		return cipherOption;
	}

	public ImmutableData immutableData() {
		return immutableData;
	}

	public Container container() {
		return accessContainer;
	}

	public MDataInfo mDataInfo() {
	    return mDataInfo;
    }

}
