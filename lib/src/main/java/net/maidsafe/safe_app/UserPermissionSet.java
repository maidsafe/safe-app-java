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
 *  FFI object representing a (User, Permission Set) pair.
 */
public class UserPermissionSet {
    private long userH;
    private PermissionSet permSet;

    public UserPermissionSet() {
        this.permSet = new PermissionSet();
    }

    public UserPermissionSet(long userH, PermissionSet permSet) {
        this.userH = userH;
        this.permSet = permSet;
    }

    public long getUserH() {
        return userH;
    }

    public void setUserH(final long val) {
        this.userH = val;
    }

    public PermissionSet getPermSet() {
        return permSet;
    }

    public void setPermSet(final PermissionSet val) {
        this.permSet = val;
    }

}

