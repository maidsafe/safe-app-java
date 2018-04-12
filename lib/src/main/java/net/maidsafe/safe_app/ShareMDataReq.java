package net.maidsafe.safe_app;

/// Represents a request to share mutable data
public class ShareMDataReq {
	private AppExchangeInfo app;
	private ShareMData mdata;
	private long mdataLen;
	private long mdataCap;

	public ShareMDataReq() {
		this.app = new AppExchangeInfo();
		this.mdata = new ShareMData();
	}
	public ShareMDataReq(AppExchangeInfo app, ShareMData mdata, long mdataLen, long mdataCap) {
		this.app = app;
		this.mdata = mdata;
		this.mdataLen = mdataLen;
		this.mdataCap = mdataCap;
	}
	public AppExchangeInfo getApp() {
		return app;
	}

	public void setApp(final AppExchangeInfo val) {
		app = val;
	}

	public ShareMData getMdaum() {
		return mdata;
	}

	public void setMdaum(final ShareMData val) {
		mdata = val;
	}

	public long getMdataLen() {
		return mdataLen;
	}

	public void setMdataLen(final long val) {
		mdataLen = val;
	}

	public long getMdataCap() {
		return mdataCap;
	}

	public void setMdataCap(final long val) {
		mdataCap = val;
	}

}

