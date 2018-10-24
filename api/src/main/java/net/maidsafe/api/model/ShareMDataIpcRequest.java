package net.maidsafe.api.model;

import net.maidsafe.safe_authenticator.MetadataResponse;
import net.maidsafe.safe_authenticator.ShareMDataReq;


public class ShareMDataIpcRequest extends IpcRequest {

    private final MetadataResponse metadataResponse;
    private final ShareMDataReq shareMDataReq;


    public ShareMDataIpcRequest(final int reqId, final ShareMDataReq shareMDataReq, final MetadataResponse metadataResponse) {
        super(reqId);
        this.shareMDataReq = shareMDataReq;
        this.metadataResponse = metadataResponse;
    }

    public MetadataResponse getMetadataResponse() {
        return metadataResponse;
    }

    public ShareMDataReq getShareMDataReq() {
        return shareMDataReq;
    }

}
