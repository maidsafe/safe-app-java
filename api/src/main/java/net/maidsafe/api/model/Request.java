package net.maidsafe.api.model;

public class Request {
    private final String uri;
    private final int reqId;

    public Request(final String uri, final int reqId) {
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
