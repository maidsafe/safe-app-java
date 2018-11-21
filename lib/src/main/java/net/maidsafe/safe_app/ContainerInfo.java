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
 *  Information about a container (name, `MDataInfo` and permissions)
 */
public class ContainerInfo {
    private String name;
    private MDataInfo mdataInfo;
    private PermissionSet permissions;

    public ContainerInfo() {
        this.name = new String();
        this.mdataInfo = new MDataInfo();
        this.permissions = new PermissionSet();
    }

    public ContainerInfo(String name, MDataInfo mdataInfo, PermissionSet permissions) {
        this.name = name;
        this.mdataInfo = mdataInfo;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(final String val) {
        this.name = val;
    }

    public MDataInfo getMdataInfo() {
        return mdataInfo;
    }

    public void setMdataInfo(final MDataInfo val) {
        this.mdataInfo = val;
    }

    public PermissionSet getPermission() {
        return permissions;
    }

    public void setPermission(final PermissionSet val) {
        this.permissions = val;
    }

}

