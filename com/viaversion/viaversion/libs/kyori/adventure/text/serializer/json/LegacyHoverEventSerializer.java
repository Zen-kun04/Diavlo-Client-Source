package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json;

import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public interface LegacyHoverEventSerializer {
  HoverEvent.ShowItem deserializeShowItem(@NotNull Component paramComponent) throws IOException;
  
  @NotNull
  Component serializeShowItem(HoverEvent.ShowItem paramShowItem) throws IOException;
  
  HoverEvent.ShowEntity deserializeShowEntity(@NotNull Component paramComponent, Codec.Decoder<Component, String, ? extends RuntimeException> paramDecoder) throws IOException;
  
  @NotNull
  Component serializeShowEntity(HoverEvent.ShowEntity paramShowEntity, Codec.Encoder<Component, String, ? extends RuntimeException> paramEncoder) throws IOException;
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\json\LegacyHoverEventSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */