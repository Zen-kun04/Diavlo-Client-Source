/*    */ package com.viaversion.viaversion.libs.kyori.adventure.bossbar;
/*    */ 
/*    */ import java.util.Collections;
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
/*    */ public interface BossBarImplementation
/*    */ {
/*    */   @Internal
/*    */   @NotNull
/*    */   static <I extends BossBarImplementation> I get(@NotNull BossBar bar, @NotNull Class<I> type) {
/* 48 */     return BossBarImpl.ImplementationAccessor.get(bar, type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Internal
/*    */   @NotNull
/*    */   default Iterable<? extends BossBarViewer> viewers() {
/* 59 */     return Collections.emptyList();
/*    */   }
/*    */   
/*    */   @Internal
/*    */   public static interface Provider {
/*    */     @Internal
/*    */     @NotNull
/*    */     BossBarImplementation create(@NotNull BossBar param1BossBar);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\bossbar\BossBarImplementation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */