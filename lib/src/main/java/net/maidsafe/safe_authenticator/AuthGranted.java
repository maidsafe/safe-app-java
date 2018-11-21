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
 *  Represents the authentication response.
 */
public class AuthGranted {
    private AppKeys appKeys;
    private AccessContInfo accessContainerInfo;
    private AccessContainerEntry accessContainerEntry;
    private byte[] bootstrapConfig;
    private long bootstrapConfigLen;
    private long bootstrapConfigCap;

    public AuthGranted() {
        this.appKeys = new AppKeys();
        this.accessContainerInfo = new AccessContInfo();
        this.accessContainerEntry = new AccessContainerEntry();
        this.bootstrapConfig = new byte[]{};
    }

    public AuthGranted(AppKeys appKeys, AccessContInfo accessContainerInfo, AccessContainerEntry accessContainerEntry, byte[] bootstrapConfig, long bootstrapConfigLen, long bootstrapConfigCap) {
        this.appKeys = appKeys;
        this.accessContainerInfo = accessContainerInfo;
        this.accessContainerEntry = accessContainerEntry;
        this.bootstrapConfig = bootstrapConfig;
        this.bootstrapConfigLen = bootstrapConfigLen;
        this.bootstrapConfigCap = bootstrapConfigCap;
    }

    public AppKeys getAppKey() {
        return appKeys;
    }

    public void setAppKey(final AppKeys val) {
        this.appKeys = val;
    }

    public AccessContInfo getAccessContainerInfo() {
        return accessContainerInfo;
    }

    public void setAccessContainerInfo(final AccessContInfo val) {
        this.accessContainerInfo = val;
    }

    public AccessContainerEntry getAccessContainerEntry() {
        return accessContainerEntry;
    }

    public void setAccessContainerEntry(final AccessContainerEntry val) {
        this.accessContainerEntry = val;
    }

    public byte[] getBootstrapConfig() {
        return bootstrapConfig;
    }

    public void setBootstrapConfig(final byte[] val) {
        this.bootstrapConfig = val;
    }

    public long getBootstrapConfigLen() {
        return bootstrapConfigLen;
    }

    public void setBootstrapConfigLen(final long val) {
        this.bootstrapConfigLen = val;
    }

    public long getBootstrapConfigCap() {
        return bootstrapConfigCap;
    }

    public void setBootstrapConfigCap(final long val) {
        this.bootstrapConfigCap = val;
    }

}

