package net.maidsafe.api.model;

public class UnregisteredClientResponse extends DecodeResult {
    private byte[] bootstrapConfig;

    public UnregisteredClientResponse(int reqId, byte[] bootstrapConfig) {
        super(reqId);
        this.bootstrapConfig = bootstrapConfig;
    }

    public byte[] getBootstrapConfig() {
        return bootstrapConfig;
    }
}
