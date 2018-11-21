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
 *  Represents the set of permissions for a given container
 */
public class ContainerPermissions {
    private String contName;
    private PermissionSet access;

    public ContainerPermissions() {
        this.contName = new String();
        this.access = new PermissionSet();
    }

    public ContainerPermissions(String contName, PermissionSet access) {
        this.contName = contName;
        this.access = access;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(final String val) {
        this.contName = val;
    }

    public PermissionSet getAccess() {
        return access;
    }

    public void setAccess(final PermissionSet val) {
        this.access = val;
    }

}

