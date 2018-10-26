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

import net.maidsafe.utils.IFreeFunc;

public class NativeHandle {
    protected long handle;
    final private IFreeFunc freeFunc;

    public NativeHandle(final long handle, final IFreeFunc freeFunc) {
        this.handle = handle;
        this.freeFunc = freeFunc;
    }

    public long toLong() {
        return handle;
    }

    @Override
    protected void finalize() throws Throwable {
        freeFunc.free(handle);
        super.finalize();
    }
}
