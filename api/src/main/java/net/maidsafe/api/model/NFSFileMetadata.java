// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
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
