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
 *  Represents the needed keys to work with the data.
 */
public class AppKeys {
    private byte[] ownerKey;
    private byte[] encKey;
    private byte[] signPk;
    private byte[] signSk;
    private byte[] encPk;
    private byte[] encSk;

    public AppKeys() {
        this.ownerKey = new byte[]{};
        this.encKey = new byte[]{};
        this.signPk = new byte[]{};
        this.signSk = new byte[]{};
        this.encPk = new byte[]{};
        this.encSk = new byte[]{};
    }

    public AppKeys(byte[] ownerKey, byte[] encKey, byte[] signPk, byte[] signSk, byte[] encPk, byte[] encSk) {
        this.ownerKey = ownerKey;
        this.encKey = encKey;
        this.signPk = signPk;
        this.signSk = signSk;
        this.encPk = encPk;
        this.encSk = encSk;
    }

    public byte[] getOwnerKey() {
        return ownerKey;
    }

    public void setOwnerKey(final byte[] val) {
        this.ownerKey = val;
    }

    public byte[] getEncKey() {
        return encKey;
    }

    public void setEncKey(final byte[] val) {
        this.encKey = val;
    }

    public byte[] getSignPk() {
        return signPk;
    }

    public void setSignPk(final byte[] val) {
        this.signPk = val;
    }

    public byte[] getSignSk() {
        return signSk;
    }

    public void setSignSk(final byte[] val) {
        this.signSk = val;
    }

    public byte[] getEncPk() {
        return encPk;
    }

    public void setEncPk(final byte[] val) {
        this.encPk = val;
    }

    public byte[] getEncSk() {
        return encSk;
    }

    public void setEncSk(final byte[] val) {
        this.encSk = val;
    }

}

