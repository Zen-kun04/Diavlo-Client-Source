package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;

public interface DisconnectedCallback extends Callback {
  void apply(int paramInt, String paramString);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\arikia\dev\drpc\callbacks\DisconnectedCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */