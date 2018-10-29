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

/// Represents a requested set of changes to the permissions of a mutable data.
public class PermissionSet {
    private boolean read;
    private boolean insert;
    private boolean update;
    private boolean delete;
    private boolean managePermissions;

    public PermissionSet() {

    }

    public PermissionSet(boolean read, boolean insert, boolean update, boolean delete, boolean managePermissions) {
        this.read = read;
        this.insert = insert;
        this.update = update;
        this.delete = delete;
        this.managePermissions = managePermissions;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(final boolean val) {
        this.read = val;
    }

    public boolean getInsert() {
        return insert;
    }

    public void setInsert(final boolean val) {
        this.insert = val;
    }

    public boolean getUpdate() {
        return update;
    }

    public void setUpdate(final boolean val) {
        this.update = val;
    }

    public boolean getDelete() {
        return delete;
    }

    public void setDelete(final boolean val) {
        this.delete = val;
    }

    public boolean getManagePermission() {
        return managePermissions;
    }

    public void setManagePermission(final boolean val) {
        this.managePermissions = val;
    }

}

