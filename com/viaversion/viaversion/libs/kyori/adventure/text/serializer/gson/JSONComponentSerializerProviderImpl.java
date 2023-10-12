/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json.JSONComponentSerializer;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.Services;
/*    */ import java.util.function.Supplier;
/*    */ import org.jetbrains.annotations.ApiStatus.Internal;
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
/*    */ @Internal
/*    */ public final class JSONComponentSerializerProviderImpl
/*    */   implements JSONComponentSerializer.Provider, Services.Fallback
/*    */ {
/*    */   @NotNull
/*    */   public JSONComponentSerializer instance() {
/* 41 */     return GsonComponentSerializer.gson();
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Supplier<JSONComponentSerializer.Builder> builder() {
/* 46 */     return GsonComponentSerializer::builder;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return "JSONComponentSerializerProviderImpl[GsonComponentSerializer]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\gson\JSONComponentSerializerProviderImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */