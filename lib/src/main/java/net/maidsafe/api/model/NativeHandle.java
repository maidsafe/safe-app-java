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

/**
 * Handle to a complex object held in the SafeApp's object cache.
 * Complex objects are not moved across FFI boundary, instead a reference to the object is used.
 * The handle acts as a reference to the object for performing operations
 */
public class NativeHandle {

    private final IFreeFunc freeFunc;
    protected long handle;

    /**
     * Initialises a new handle
     * @param handle Handle to native object
     * @param freeFunc Function that frees the native object
     */
    public NativeHandle(final long handle, final IFreeFunc freeFunc) {
        this.handle = handle;
        this.freeFunc = freeFunc;
    }

    /**
     * Returns handle as long value
     * @return handle
     */
    public long toLong() {
        return handle;
    }

    @Override
    protected void finalize() throws Throwable {
        freeFunc.free(handle);
        super.finalize();
    }
}
