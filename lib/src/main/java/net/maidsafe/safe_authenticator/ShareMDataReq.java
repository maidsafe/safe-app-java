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

/// Represents a request to share mutable data
public class ShareMDataReq {
	private AppExchangeInfo app;
	private ShareMData[] mdata;
	private long mdataLen;
	private long mdataCap;

	public ShareMDataReq() {
		this.app = new AppExchangeInfo();
		this.mdata = new ShareMData[] {};
	}
	public ShareMDataReq(AppExchangeInfo app, ShareMData[] mdata, long mdataLen, long mdataCap) {
		this.app = app;
		this.mdata = mdata;
		this.mdataLen = mdataLen;
		this.mdataCap = mdataCap;
	}
	public AppExchangeInfo getApp() {
		return app;
	}

	public void setApp(final AppExchangeInfo val) {
		this.app = val;
	}

	public ShareMData[] getMdaum() {
		return mdata;
	}

	public void setMdaum(final ShareMData[] val) {
		this.mdata = val;
	}

	public long getMdataLen() {
		return mdataLen;
	}

	public void setMdataLen(final long val) {
		this.mdataLen = val;
	}

	public long getMdataCap() {
		return mdataCap;
	}

	public void setMdataCap(final long val) {
		this.mdataCap = val;
	}

}

