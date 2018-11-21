// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.safe_authenticator;

/***
 *  Access container info.
 */
public class AccessContInfo {
    private byte[] id;
    private long tag;
    private byte[] nonce;

    public AccessContInfo() {
        this.id = new byte[]{};
        this.nonce = new byte[]{};
    }

    public AccessContInfo(byte[] id, long tag, byte[] nonce) {
        this.id = id;
        this.tag = tag;
        this.nonce = nonce;
    }

    public byte[] getId() {
        return id;
    }

    public void setId(final byte[] val) {
        this.id = val;
    }

    public long getTag() {
        return tag;
    }

    public void setTag(final long val) {
        this.tag = val;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(final byte[] val) {
        this.nonce = val;
    }

}

