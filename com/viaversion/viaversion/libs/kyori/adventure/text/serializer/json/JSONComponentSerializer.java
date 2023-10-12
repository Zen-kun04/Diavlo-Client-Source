/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.ComponentSerializer;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.PlatformAPI;
/*    */ import java.util.function.Supplier;
/*    */ import org.jetbrains.annotations.ApiStatus.Internal;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
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
/*    */ 
/*    */ public interface JSONComponentSerializer
/*    */   extends ComponentSerializer<Component, Component, String>
/*    */ {
/*    */   @NotNull
/*    */   static JSONComponentSerializer json() {
/* 50 */     return JSONComponentSerializerAccessor.Instances.INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Builder builder() {
/* 60 */     return JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
/*    */   }
/*    */   
/*    */   @PlatformAPI
/*    */   @Internal
/*    */   public static interface Provider {
/*    */     @PlatformAPI
/*    */     @Internal
/*    */     @NotNull
/*    */     JSONComponentSerializer instance();
/*    */     
/*    */     @PlatformAPI
/*    */     @Internal
/*    */     @NotNull
/*    */     Supplier<JSONComponentSerializer.Builder> builder();
/*    */   }
/*    */   
/*    */   public static interface Builder {
/*    */     @NotNull
/*    */     Builder downsampleColors();
/*    */     
/*    */     @NotNull
/*    */     Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer param1LegacyHoverEventSerializer);
/*    */     
/*    */     @NotNull
/*    */     Builder emitLegacyHoverEvent();
/*    */     
/*    */     JSONComponentSerializer build();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\json\JSONComponentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */