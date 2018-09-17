package net.maidsafe.models;

import net.maidsafe.safe_authenticator.AuthReq;
import net.maidsafe.safe_authenticator.ContainersReq;
import net.maidsafe.safe_authenticator.MetadataResponse;
import net.maidsafe.safe_authenticator.ShareMDataReq;

public interface IpcRequest {


  class AuthIpcRequest implements IpcRequest {

    public AuthReq authReq;
    public int reqId;

    public AuthIpcRequest(int reqId, AuthReq authReq) {
      this.reqId = reqId;
      this.authReq = authReq;
    }

  }

  class UnregisteredIpcRequest implements IpcRequest {

    public byte[] extraData;
    public int reqId;

    public UnregisteredIpcRequest(int reqId, byte[] extraData) {
      this.reqId = reqId;
      this.extraData = extraData;
    }

  }

  class ContainersIpcReq implements IpcRequest {

    public ContainersReq containersReq;
    public int reqId;

    public ContainersIpcReq(int reqId, ContainersReq containersReq) {
      this.reqId = reqId;
      this.containersReq = containersReq;
    }

  }

  class ShareMDataIpcRequest implements IpcRequest {

    public MetadataResponse metadataResponse;
    public int reqId;
    public ShareMDataReq shareMDataReq;

    public ShareMDataIpcRequest(int reqId, ShareMDataReq shareMDataReq, MetadataResponse metadataResponse) {
      this.reqId = reqId;
      this.shareMDataReq = shareMDataReq;
      this.metadataResponse = metadataResponse;
    }

  }

  class IpcReqRejected implements IpcRequest {

    public final String message;

    public IpcReqRejected(String message) {
      this.message = message;
    }
  }

  class IpcReqError implements IpcRequest {

    public final int code;
    public final String description;
    public final String message;

    public IpcReqError(int code, String description, String message) {
      this.code = code;
      this.description = description;
      this.message = message;
    }

  }

}


