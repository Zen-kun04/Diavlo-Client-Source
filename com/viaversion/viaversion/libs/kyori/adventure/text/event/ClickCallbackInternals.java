/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text.event;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.audience.Audience;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.permission.PermissionChecker;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.Services;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.TriState;
/*    */ import java.util.function.Supplier;
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
/*    */ final class ClickCallbackInternals
/*    */ {
/* 36 */   static final PermissionChecker ALWAYS_FALSE = PermissionChecker.always(TriState.FALSE);
/*    */   
/* 38 */   static final ClickCallback.Provider PROVIDER = Services.service(ClickCallback.Provider.class)
/* 39 */     .orElseGet(Fallback::new);
/*    */   
/*    */   static final class Fallback implements ClickCallback.Provider {
/*    */     @NotNull
/*    */     public ClickEvent create(@NotNull ClickCallback<Audience> callback, ClickCallback.Options options) {
/* 44 */       return ClickEvent.suggestCommand("Callbacks are not supported on this platform!");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\event\ClickCallbackInternals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */