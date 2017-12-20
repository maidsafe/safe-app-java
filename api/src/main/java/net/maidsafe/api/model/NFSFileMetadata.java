package net.maidsafe.api.model;

public class NFSFileMetadata extends net.maidsafe.safe_app.File {
    private long version;

    public NFSFileMetadata() {
    }

    public NFSFileMetadata(net.maidsafe.safe_app.File ffiFile, long version) {
        this.version = version;
        this.setCreatedSec(ffiFile.getCreatedNsec());
        this.setCreatedSec(ffiFile.getCreatedSec());
        this.setDataMapName(ffiFile.getDataMapName());
        this.setModifiedNsec(ffiFile.getModifiedNsec());
        this.setModifiedSec(ffiFile.getModifiedSec());
        this.setSize(ffiFile.getSize());
        this.setUserMetadataCap(ffiFile.getUserMetadataCap());
        this.setUserMetadataPtr(ffiFile.getUserMetadataPtr());
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
