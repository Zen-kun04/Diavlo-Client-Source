package net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;
import net.arikia.dev.drpc.DiscordUser;

public interface JoinRequestCallback extends Callback {
  void apply(DiscordUser paramDiscordUser);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\arikia\dev\drpc\callbacks\JoinRequestCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */