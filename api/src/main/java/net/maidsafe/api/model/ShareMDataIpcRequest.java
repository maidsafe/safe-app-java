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

import net.maidsafe.safe_authenticator.MetadataResponse;
import net.maidsafe.safe_authenticator.ShareMDataReq;

/***
 * Represents an Share MData IPC Request
 */
public class ShareMDataIpcRequest extends IpcRequest {

    private final MetadataResponse[] metadataResponse;
    private final ShareMDataReq shareMDataReq;


    /***
     * Initialises a ShareMDataReq object
     * @param reqId Request ID
     * @param shareMDataReq Share Mutable Data request
     * @param metadataResponse Array of metadata responses
     */
    public ShareMDataIpcRequest(final int reqId, final ShareMDataReq shareMDataReq,
                                final MetadataResponse[] metadataResponse) {
        super(reqId);
        this.shareMDataReq = shareMDataReq;
        this.metadataResponse = metadataResponse;
    }

    /**
     * Returns the metadata response array
     * @return Array of MetadataResponse objects
     */
    public MetadataResponse[] getMetadataResponse() {
        return metadataResponse.clone();
    }

    /**
     * Returns the Shared Mutable Data request
     * @return ShareMDataReq object
     */
    public ShareMDataReq getShareMDataReq() {
        return shareMDataReq;
    }

}
