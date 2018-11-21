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
 * Represents a Request
 */
public class Request {

    private final String uri;
    private final int reqId;

    /***
     * Initialises a new Request instance
     * @param uri  Encoded request
     * @param reqId  Request ID
     */
    public Request(final String uri, final int reqId) {
        this.uri = uri;
        this.reqId = reqId;
    }

    /***
     * Returns the Request ID
     * @return Request ID as int
     */
    public int getReqId() {
        return reqId;
    }

    /***
     * Returns the encoded request
     * @return Request URI as a String
     */
    public String getUri() {
        return uri;
    }
}
