/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ 
/*    */ public final class GLUtil
/*    */ {
/*    */   private GLUtil() {
/*  8 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/*    */   public static void enableDepth() {
/* 11 */     GlStateManager.enableDepth();
/* 12 */     GlStateManager.depthMask(true);
/*    */   }
/*    */   
/*    */   public static void disableDepth() {
/* 16 */     GlStateManager.disableDepth();
/* 17 */     GlStateManager.depthMask(false);
/*    */   }
/*    */   
/*    */   public static void setup2DRendering(boolean blend) {
/* 21 */     if (blend) {
/* 22 */       startBlend();
/*    */     }
/* 24 */     GlStateManager.disableTexture2D();
/*    */   }
/*    */   
/*    */   public static void end2DRendering() {
/* 28 */     GlStateManager.enableTexture2D();
/* 29 */     endBlend();
/*    */   }
/*    */   
/*    */   public static void startBlend() {
/* 33 */     GlStateManager.enableBlend();
/* 34 */     GlStateManager.blendFunc(770, 771);
/*    */   }
/*    */   
/*    */   public static void endBlend() {
/* 38 */     GlStateManager.disableBlend();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\GLUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */