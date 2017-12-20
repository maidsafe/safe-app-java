package net.maidsafe.safe_app;

/// Represents a request to share mutable data
public class ShareMDataReq {
	public ShareMDataReq() { }
	private AppExchangeInfo app;

	public AppExchangeInfo getApp() {
		return app;
	}

	public void setApp(final AppExchangeInfo val) {
		app = val;
	}

	private ShareMData[] mdata;

	public ShareMData[] getMdaum() {
		return mdata;
	}

	public void setMdaum(final ShareMData[] val) {
		mdata = val;
	}

	private long mdataLen;

	public long getMdataLen() {
		return mdataLen;
	}

	public void setMdataLen(final long val) {
		mdataLen = val;
	}

	private long mdataCap;

	public long getMdataCap() {
		return mdataCap;
	}

	public void setMdataCap(final long val) {
		mdataCap = val;
	}

	public ShareMDataReq(AppExchangeInfo app, ShareMData[] mdata, long mdataLen, long mdataCap) { }
}

