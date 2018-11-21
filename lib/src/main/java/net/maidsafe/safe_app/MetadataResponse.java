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

/***
 *  User metadata for Mutable Data
 */
public class MetadataResponse {
    private String name;
    private String description;
    private byte[] xorName;
    private long typeTag;

    public MetadataResponse() {
        this.name = new String();
        this.description = new String();
        this.xorName = new byte[]{};
    }

    public MetadataResponse(String name, String description, byte[] xorName, long typeTag) {
        this.name = name;
        this.description = description;
        this.xorName = xorName;
        this.typeTag = typeTag;
    }

    public String getName() {
        return name;
    }

    public void setName(final String val) {
        this.name = val;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String val) {
        this.description = val;
    }

    public byte[] getXorName() {
        return xorName;
    }

    public void setXorName(final byte[] val) {
        this.xorName = val;
    }

    public long getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(final long val) {
        this.typeTag = val;
    }

}

