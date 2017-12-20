package net.maidsafe.api.model;

public class DecodeResult {
    private int reqId;

    public DecodeResult(int reqId) {
        this.reqId = reqId;
    }

    public int getReqId() {
        return reqId;
    }
}
