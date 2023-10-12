/*    */ package rip.diavlo.base.utils.render.gl;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.Vec3;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public final class GLUtil {
/*    */   private GLUtil() {
/*  9 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/*    */   public static void glVertex3D(Vec3 vector3d) {
/* 12 */     GL11.glVertex3d(vector3d.xCoord, vector3d.yCoord, vector3d.zCoord);
/*    */   }
/*    */   
/*    */   public static void enableDepth() {
/* 16 */     GlStateManager.enableDepth();
/* 17 */     GlStateManager.depthMask(true);
/*    */   }
/*    */   
/*    */   public static void disableDepth() {
/* 21 */     GlStateManager.disableDepth();
/* 22 */     GlStateManager.depthMask(false);
/*    */   }
/*    */   
/*    */   public static void setup2DRendering(boolean blend) {
/* 26 */     if (blend) {
/* 27 */       startBlend();
/*    */     }
/* 29 */     GlStateManager.disableTexture2D();
/*    */   }
/*    */   
/*    */   public static void end2DRendering() {
/* 33 */     GlStateManager.enableTexture2D();
/* 34 */     endBlend();
/*    */   }
/*    */   
/*    */   public static void startBlend() {
/* 38 */     GlStateManager.enableBlend();
/* 39 */     GlStateManager.blendFunc(770, 771);
/*    */   }
/*    */   
/*    */   public static void endBlend() {
/* 43 */     GlStateManager.disableBlend();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\render\gl\GLUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */