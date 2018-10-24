package net.maidsafe.api.model;

public class UnregisteredClientResponse extends DecodeResult {
    private final byte[] bootstrapConfig;


    public UnregisteredClientResponse(final int reqId, final byte[] bootstrapConfig) {
        super(reqId);
        this.bootstrapConfig = bootstrapConfig.clone();
    }

    public byte[] getBootstrapConfig() {
        return bootstrapConfig.clone();
    }
}
