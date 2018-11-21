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

/***
 *  FFI wrapper for `MDataInfo`.
 */
public class MDataInfo {
    private byte[] name;
    private long typeTag;
    private boolean hasEncInfo;
    private byte[] encKey;
    private byte[] encNonce;
    private boolean hasNewEncInfo;
    private byte[] newEncKey;
    private byte[] newEncNonce;

    public MDataInfo() {
        this.name = new byte[]{};
        this.encKey = new byte[]{};
        this.encNonce = new byte[]{};
        this.newEncKey = new byte[]{};
        this.newEncNonce = new byte[]{};
    }

    public MDataInfo(byte[] name, long typeTag, boolean hasEncInfo, byte[] encKey, byte[] encNonce, boolean hasNewEncInfo, byte[] newEncKey, byte[] newEncNonce) {
        this.name = name;
        this.typeTag = typeTag;
        this.hasEncInfo = hasEncInfo;
        this.encKey = encKey;
        this.encNonce = encNonce;
        this.hasNewEncInfo = hasNewEncInfo;
        this.newEncKey = newEncKey;
        this.newEncNonce = newEncNonce;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(final byte[] val) {
        this.name = val;
    }

    public long getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(final long val) {
        this.typeTag = val;
    }

    public boolean getHasEncInfo() {
        return hasEncInfo;
    }

    public void setHasEncInfo(final boolean val) {
        this.hasEncInfo = val;
    }

    public byte[] getEncKey() {
        return encKey;
    }

    public void setEncKey(final byte[] val) {
        this.encKey = val;
    }

    public byte[] getEncNonce() {
        return encNonce;
    }

    public void setEncNonce(final byte[] val) {
        this.encNonce = val;
    }

    public boolean getHasNewEncInfo() {
        return hasNewEncInfo;
    }

    public void setHasNewEncInfo(final boolean val) {
        this.hasNewEncInfo = val;
    }

    public byte[] getNewEncKey() {
        return newEncKey;
    }

    public void setNewEncKey(final byte[] val) {
        this.newEncKey = val;
    }

    public byte[] getNewEncNonce() {
        return newEncNonce;
    }

    public void setNewEncNonce(final byte[] val) {
        this.newEncNonce = val;
    }

}

