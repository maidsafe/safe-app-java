// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.safe_app;

/// Represents an FFI-safe mutable data key.
public class MDataKey {
    private byte[] key;
    private long keyLen;

    public MDataKey() {
        this.key = new byte[]{};
    }

    public MDataKey(byte[] key, long keyLen) {
        this.key = key;
        this.keyLen = keyLen;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(final byte[] key) {
        this.key = key;
    }

    public long getKeyLen() {
        return keyLen;
    }

    public void setKeyLen(final long val) {
        this.keyLen = val;
    }

}

