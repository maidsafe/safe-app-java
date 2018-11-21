// Copyright 2018 MaidSafe.net limited.
//
// This SAFE Network Software is licensed to you under the MIT license
// <LICENSE-MIT or http://opensource.org/licenses/MIT> or the Modified
// BSD license <LICENSE-BSD or https://opensource.org/licenses/BSD-3-Clause>,
// at your option. This file may not be copied, modified, or distributed
// except according to those terms. Please review the Licences for the
// specific language governing permissions and limitations relating to use
// of the SAFE Network Software.
package net.maidsafe.api;

import net.maidsafe.api.model.NativeHandle;
import net.maidsafe.safe_app.NativeBindings;

/***
 * Represents a pointer to the app in native code
 */
class AppHandle extends NativeHandle {

    /***
     * Returns an AppHandle object
     * @param handle
     */
    AppHandle(final long handle) {
        super(handle, h -> NativeBindings.appFree(h));
    }

    /***
     * Frees the AppHandle object
     */
    public void invalidate() {
        if (handle > 0) {
            NativeBindings.appFree(handle);
            this.handle = -1;
        }
    }

    /***
     * Returns the long value of the AppHandle
     * @return handle
     */
    @Override
    public long toLong() {
        if (handle < 0) {
            throw new java.lang.RuntimeException("Session Invalidated");
        }
        return handle;
    }

}
