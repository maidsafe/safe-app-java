package net.maidsafe.api.model;

public class UnregisteredIpcRequest extends IpcRequest {

    private final byte[] extraData;

    public UnregisteredIpcRequest(final int reqId, final byte[] extraData) {
        super(reqId);
        this.extraData = extraData.clone();
    }

    public byte[] getExtraData() {
        return extraData.clone();
    }
}
