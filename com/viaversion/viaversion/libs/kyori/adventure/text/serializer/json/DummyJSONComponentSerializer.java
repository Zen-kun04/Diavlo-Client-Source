/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.json;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
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
/*    */ final class DummyJSONComponentSerializer
/*    */   implements JSONComponentSerializer
/*    */ {
/* 31 */   static final JSONComponentSerializer INSTANCE = new DummyJSONComponentSerializer();
/*    */ 
/*    */   
/*    */   private static final String UNSUPPORTED_MESSAGE = "No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?";
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Component deserialize(@NotNull String input) {
/* 40 */     throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public String serialize(@NotNull Component component) {
/* 45 */     throw new UnsupportedOperationException("No JsonComponentSerializer implementation found\n\nAre you missing an implementation artifact like adventure-text-serializer-gson?\nIs your environment configured in a way that causes ServiceLoader to malfunction?");
/*    */   }
/*    */   
/*    */   static final class BuilderImpl
/*    */     implements JSONComponentSerializer.Builder {
/*    */     @NotNull
/*    */     public JSONComponentSerializer.Builder downsampleColors() {
/* 52 */       return this;
/*    */     }
/*    */     
/*    */     @NotNull
/*    */     public JSONComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer serializer) {
/* 57 */       return this;
/*    */     }
/*    */     
/*    */     @NotNull
/*    */     public JSONComponentSerializer.Builder emitLegacyHoverEvent() {
/* 62 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public JSONComponentSerializer build() {
/* 67 */       return DummyJSONComponentSerializer.INSTANCE;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\json\DummyJSONComponentSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */