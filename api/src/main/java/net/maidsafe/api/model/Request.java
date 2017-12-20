package net.maidsafe.api.model;

public class Request {
    private String uri;
    private int reqId;

    public Request(String uri, int reqId) {
        this.uri = uri;
        this.reqId = reqId;
    }

    public int getReqId() {
        return reqId;
    }

    public String getUri() {
        return uri;
    }
}
