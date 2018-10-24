package net.maidsafe.api;

final class ClientTypeFactory {
  private final Class clientType;
  private static ClientTypeFactory instance;

  private ClientTypeFactory(final Class clientType) {
    this.clientType = clientType;
  }

  public static ClientTypeFactory load(final Class clientType) {
    synchronized (ClientTypeFactory.class) {
      if(instance == null) {
        instance = new ClientTypeFactory(clientType);
      }
      return instance;
    }
  }
  public Class getClientType() {
    return clientType;
  }

}