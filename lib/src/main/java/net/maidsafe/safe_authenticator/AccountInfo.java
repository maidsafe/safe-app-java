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
 *  Represents the FFI-safe account info.
 */
public class AccountInfo {
    private long mutationsDone;
    private long mutationsAvailable;

    public AccountInfo() {

    }

    public AccountInfo(long mutationsDone, long mutationsAvailable) {
        this.mutationsDone = mutationsDone;
        this.mutationsAvailable = mutationsAvailable;
    }

    public long getMutationsDone() {
        return mutationsDone;
    }

    public void setMutationsDone(final long val) {
        this.mutationsDone = val;
    }

    public long getMutationsAvailable() {
        return mutationsAvailable;
    }

    public void setMutationsAvailable(final long val) {
        this.mutationsAvailable = val;
    }

}

