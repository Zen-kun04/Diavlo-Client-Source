package com.viaversion.viaversion.api.protocol;

import java.util.Collection;
import java.util.List;

public interface ProtocolPipeline extends SimpleProtocol {
  void add(Protocol paramProtocol);
  
  void add(Collection<Protocol> paramCollection);
  
  boolean contains(Class<? extends Protocol> paramClass);
  
  <P extends Protocol> P getProtocol(Class<P> paramClass);
  
  List<Protocol> pipes();
  
  List<Protocol> reversedPipes();
  
  boolean hasNonBaseProtocols();
  
  void cleanPipes();
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\protocol\ProtocolPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */