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

/***
 * Represents an Application object
 */
public class App {
    private final String id;
    private final String name;
    private final String vendor;
    private final String version;

    /***
     * Initialises an App object for the given information
     * @param id  App ID
     * @param name  App Name
     * @param vendor  App Vendor
     * @param version  App Version
     */
    public App(final String id, final String name, final String vendor, final String version) {
        this.id = id;
        this.name = name;
        this.vendor = vendor;
        this.version = version;
    }

    /***
     * Returns the App ID
     * @return The App's ID as a String
     */
    public String getId() {
        return id;
    }

    /***
     * Returns the App name
     * @return The App's name as a String
     */
    public String getName() {
        return name;
    }

    /***
     * Returns the App vendor
     * @return The App's vendor as a String
     */
    public String getVendor() {
        return vendor;
    }

    /***
     * Returns the App version
     * @return The App's version as a String
     */
    public String getVersion() {
        return version;
    }
}
