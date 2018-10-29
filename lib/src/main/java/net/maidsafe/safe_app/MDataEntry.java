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

/// Represents an FFI-safe mutable data (key, value) entry.
public class MDataEntry {
    private MDataKey key;
    private MDataValue value;

    public MDataEntry() {
        this.key = new MDataKey();
        this.value = new MDataValue();
    }

    public MDataEntry(MDataKey key, MDataValue value) {
        this.key = key;
        this.value = value;
    }

    public MDataKey getKey() {
        return key;
    }

    public void setKey(final MDataKey val) {
        this.key = val;
    }

    public MDataValue getValue() {
        return value;
    }

    public void setValue(final MDataValue val) {
        this.value = val;
    }

}

