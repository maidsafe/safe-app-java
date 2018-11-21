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
 *  Application registered in the authenticator
 */
public class RegisteredApp {
    private AppExchangeInfo appInfo;
    private ContainerPermissions[] containers;
    private long containersLen;
    private long containersCap;

    public RegisteredApp() {
        this.appInfo = new AppExchangeInfo();
        this.containers = new ContainerPermissions[]{};
    }

    public RegisteredApp(AppExchangeInfo appInfo, ContainerPermissions[] containers, long containersLen, long containersCap) {
        this.appInfo = appInfo;
        this.containers = containers;
        this.containersLen = containersLen;
        this.containersCap = containersCap;
    }

    public AppExchangeInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(final AppExchangeInfo val) {
        this.appInfo = val;
    }

    public ContainerPermissions[] getContainer() {
        return containers;
    }

    public void setContainer(final ContainerPermissions[] val) {
        this.containers = val;
    }

    public long getContainersLen() {
        return containersLen;
    }

    public void setContainersLen(final long val) {
        this.containersLen = val;
    }

    public long getContainersCap() {
        return containersCap;
    }

    public void setContainersCap(final long val) {
        this.containersCap = val;
    }

}

