/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json.LegacyHoverEventSerializer;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.Codec;
/*    */ import java.io.IOException;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ final class NBTLegacyHoverEventSerializerImpl
/*    */   implements LegacyHoverEventSerializer
/*    */ {
/* 36 */   static final NBTLegacyHoverEventSerializerImpl INSTANCE = new NBTLegacyHoverEventSerializerImpl();
/*    */   
/* 38 */   static final LegacyHoverEventSerializer NEW_INSTANCE = NBTLegacyHoverEventSerializer.get();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HoverEvent.ShowItem deserializeShowItem(@NotNull Component input) throws IOException {
/* 45 */     return NEW_INSTANCE.deserializeShowItem(input);
/*    */   }
/*    */ 
/*    */   
/*    */   public HoverEvent.ShowEntity deserializeShowEntity(@NotNull Component input, Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
/* 50 */     return NEW_INSTANCE.deserializeShowEntity(input, componentCodec);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Component serializeShowItem(HoverEvent.ShowItem input) throws IOException {
/* 55 */     return NEW_INSTANCE.serializeShowItem(input);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Component serializeShowEntity(HoverEvent.ShowEntity input, Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
/* 60 */     return NEW_INSTANCE.serializeShowEntity(input, componentCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\gson\legacyimpl\NBTLegacyHoverEventSerializerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */