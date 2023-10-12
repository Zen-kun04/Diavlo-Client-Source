/*    */ package com.viaversion.viaversion.libs.kyori.adventure.text;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.util.Nag;
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
/*    */ final class LegacyFormattingDetected
/*    */   extends Nag
/*    */ {
/*    */   private static final long serialVersionUID = -947793022628807411L;
/*    */   
/*    */   LegacyFormattingDetected(Component component) {
/* 32 */     super("Legacy formatting codes have been detected in a component - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.advntr.dev) for more information. Component: " + component);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\LegacyFormattingDetected.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */