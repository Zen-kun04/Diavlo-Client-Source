/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.legacyimpl;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json.legacyimpl.NBTLegacyHoverEventSerializer;
/*    */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
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
/*    */ @ScheduledForRemoval(inVersion = "5.0.0")
/*    */ public interface NBTLegacyHoverEventSerializer
/*    */   extends LegacyHoverEventSerializer, NBTLegacyHoverEventSerializer
/*    */ {
/*    */   @Deprecated
/*    */   @ScheduledForRemoval(inVersion = "5.0.0")
/*    */   @NotNull
/*    */   static LegacyHoverEventSerializer get() {
/* 50 */     return NBTLegacyHoverEventSerializerImpl.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\gson\legacyimpl\NBTLegacyHoverEventSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */