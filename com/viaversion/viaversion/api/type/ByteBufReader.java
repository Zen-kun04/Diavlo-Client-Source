package com.viaversion.viaversion.api.type;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ByteBufReader<T> {
  T read(ByteBuf paramByteBuf) throws Exception;
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\type\ByteBufReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */