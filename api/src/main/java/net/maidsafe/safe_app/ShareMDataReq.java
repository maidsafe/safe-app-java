package net.maidsafe.safe_app;

/// Represents a request to share mutable data
public class ShareMDataReq {
    private AppExchangeInfo app;
    private ShareMDaum mdata;
    private long mdataLen;
    private long mdataCap;

    public AppExchangeInfo getApp() {
        return app;
    }

    public void setApp(final AppExchangeInfo val) {
        app = val;
    }

    public ShareMDaum getMdaum() {
        return mdata;
    }

    public void setMdaum(final ShareMDaum val) {
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

