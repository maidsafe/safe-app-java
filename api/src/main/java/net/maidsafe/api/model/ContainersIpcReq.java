package net.maidsafe.api.model;

import net.maidsafe.safe_authenticator.ContainersReq;

/***
 * Represents an Ipc Request for a Container Request
 */
public class ContainersIpcReq extends IpcRequest {

    private final ContainersReq containersReq;

    /***
     * Initializes a ContainersIpcReq object
     * @param reqId Request ID
     * @param containersReq Containers Request
     */
    public ContainersIpcReq(final int reqId, final ContainersReq containersReq) {
        super(reqId);
        this.containersReq = containersReq;
    }

    public ContainersReq getContainersReq() {
        return containersReq;
    }

}

