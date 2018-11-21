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

/**
 * Represents an application ID in the process of asking permissions
 */
public class AppExchangeInfo {
    private String id;
    private String scope;
    private String name;
    private String vendor;

    public AppExchangeInfo() {
        this.id = new String();
        this.scope = new String();
        this.name = new String();
        this.vendor = new String();
    }

    public AppExchangeInfo(String id, String scope, String name, String vendor) {
        this.id = id;
        this.scope = scope;
        this.name = name;
        this.vendor = vendor;
    }

    public String getId() {
        return id;
    }

    public void setId(final String val) {
        this.id = val;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(final String val) {
        this.scope = val;
    }

    public String getName() {
        return name;
    }

    public void setName(final String val) {
        this.name = val;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(final String val) {
        this.vendor = val;
    }

}

