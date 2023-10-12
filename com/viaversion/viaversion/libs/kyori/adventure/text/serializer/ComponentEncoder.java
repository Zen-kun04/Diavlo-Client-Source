/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer;
/*    */ 
/*    */ import org.jetbrains.annotations.Contract;
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
/*    */ public interface ComponentEncoder<I extends com.viaversion.viaversion.libs.kyori.adventure.text.Component, R>
/*    */ {
/*    */   @NotNull
/*    */   R serialize(@NotNull I paramI);
/*    */   
/*    */   @Contract(value = "!null -> !null; null -> null", pure = true)
/*    */   @Nullable
/*    */   default R serializeOrNull(@Nullable I component) {
/* 61 */     return serializeOr(component, null);
/*    */   }
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
/*    */   @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
/*    */   @Nullable
/*    */   default R serializeOr(@Nullable I component, @Nullable R fallback) {
/* 76 */     if (component == null) return fallback;
/*    */     
/* 78 */     return serialize(component);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\ComponentEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */