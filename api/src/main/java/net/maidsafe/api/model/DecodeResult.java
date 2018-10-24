package net.maidsafe.api.model;

public class DecodeResult {
    private final int reqId;


    public DecodeResult(final int reqId) {
        this.reqId = reqId;
    }

    public int getReqId() {
        return reqId;
    }
}
