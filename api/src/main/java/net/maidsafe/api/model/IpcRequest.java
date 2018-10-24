package net.maidsafe.api.model;



public class IpcRequest {

    private final int reqId;

    public IpcRequest(final int reqId) {
        this.reqId = reqId;
    }

    public IpcRequest() {
        reqId = -1;
    }

    public int getReqId() {
        return reqId;
    }
}


