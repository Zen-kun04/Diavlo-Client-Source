/*    */ package com.viaversion.viaversion.libs.kyori.adventure.audience;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collector;
/*    */ import java.util.stream.Collectors;
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
/*    */ public final class Audiences
/*    */ {
/*    */   static final Collector<? super Audience, ?, ForwardingAudience> COLLECTOR;
/*    */   
/*    */   static {
/* 40 */     COLLECTOR = Collectors.collectingAndThen(
/* 41 */         Collectors.toCollection(ArrayList::new), audiences -> Audience.audience(Collections.unmodifiableCollection(audiences)));
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
/*    */   
/*    */   @NotNull
/*    */   public static Consumer<? super Audience> sendingMessage(@NotNull ComponentLike message) {
/* 56 */     return audience -> audience.sendMessage(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\audience\Audiences.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */