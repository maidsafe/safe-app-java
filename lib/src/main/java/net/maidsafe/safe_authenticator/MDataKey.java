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

/// Represents an FFI-safe mutable data key.
public class MDataKey {
	private byte[] val;
	private long valLen;

	public MDataKey() {
		this.val = new byte[] {};
	}
	public MDataKey(byte[] val, long valLen) {
		this.val = val;
		this.valLen = valLen;
	}
	public byte[] getVal() {
		return val;
	}

	public void setVal(final byte[] val) {
		this.val = val;
	}

	public long getValLen() {
		return valLen;
	}

	public void setValLen(final long val) {
		this.valLen = val;
	}

}

