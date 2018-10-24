package net.maidsafe.api.model;

public class NFSFileMetadata extends net.maidsafe.safe_app.File {
    private long version;

    public NFSFileMetadata() {
        super();
    }


    public NFSFileMetadata(final net.maidsafe.safe_app.File ffiFile, final long version) {
        super();
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


    public void setVersion(final long version) {
        this.version = version;
    }

}
