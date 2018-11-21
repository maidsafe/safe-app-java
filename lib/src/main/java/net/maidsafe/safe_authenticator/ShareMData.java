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
 *  For use in `ShareMDataReq`. Represents a specific `MutableData` that is being shared.
 */
public class ShareMData {
    private long typeTag;
    private byte[] name;
    private PermissionSet perms;

    public ShareMData() {
        this.name = new byte[]{};
        this.perms = new PermissionSet();
    }

    public ShareMData(long typeTag, byte[] name, PermissionSet perms) {
        this.typeTag = typeTag;
        this.name = name;
        this.perms = perms;
    }

    public long getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(final long val) {
        this.typeTag = val;
    }

    public byte[] getName() {
        return name;
    }

    public void setName(final byte[] val) {
        this.name = val;
    }

    public PermissionSet getPerm() {
        return perms;
    }

    public void setPerm(final PermissionSet val) {
        this.perms = val;
    }

}

