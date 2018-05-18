package net.maidsafe.api;

public class ClientTypeFactory implements Cloneable {
  private Class clientType;
  private static ClientTypeFactory instance;

  private ClientTypeFactory(Class clientType) {
    this.clientType = clientType;
  }

  public static synchronized ClientTypeFactory load(Class clientType) {
    if(instance == null) {
      instance = new ClientTypeFactory(clientType);
    }
    return instance;
  }
  public Class getClientType() {
    return clientType;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }
}