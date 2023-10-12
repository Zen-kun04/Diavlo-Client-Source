/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import net.minecraft.util.Vec3;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class RenderHelper
/*    */ {
/*  9 */   private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
/* 10 */   private static final Vec3 LIGHT0_POS = (new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
/* 11 */   private static final Vec3 LIGHT1_POS = (new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();
/*    */ 
/*    */   
/*    */   public static void disableStandardItemLighting() {
/* 15 */     GlStateManager.disableLighting();
/* 16 */     GlStateManager.disableLight(0);
/* 17 */     GlStateManager.disableLight(1);
/* 18 */     GlStateManager.disableColorMaterial();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void enableStandardItemLighting() {
/* 23 */     GlStateManager.enableLighting();
/* 24 */     GlStateManager.enableLight(0);
/* 25 */     GlStateManager.enableLight(1);
/* 26 */     GlStateManager.enableColorMaterial();
/* 27 */     GlStateManager.colorMaterial(1032, 5634);
/* 28 */     float f = 0.4F;
/* 29 */     float f1 = 0.6F;
/* 30 */     float f2 = 0.0F;
/* 31 */     GL11.glLight(16384, 4611, setColorBuffer(LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D));
/* 32 */     GL11.glLight(16384, 4609, setColorBuffer(f1, f1, f1, 1.0F));
/* 33 */     GL11.glLight(16384, 4608, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
/* 34 */     GL11.glLight(16384, 4610, setColorBuffer(f2, f2, f2, 1.0F));
/* 35 */     GL11.glLight(16385, 4611, setColorBuffer(LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D));
/* 36 */     GL11.glLight(16385, 4609, setColorBuffer(f1, f1, f1, 1.0F));
/* 37 */     GL11.glLight(16385, 4608, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
/* 38 */     GL11.glLight(16385, 4610, setColorBuffer(f2, f2, f2, 1.0F));
/* 39 */     GlStateManager.shadeModel(7424);
/* 40 */     GL11.glLightModel(2899, setColorBuffer(f, f, f, 1.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
/* 45 */     return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
/*    */   }
/*    */ 
/*    */   
/*    */   private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
/* 50 */     colorBuffer.clear();
/* 51 */     colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
/* 52 */     colorBuffer.flip();
/* 53 */     return colorBuffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void enableGUIStandardItemLighting() {
/* 58 */     GlStateManager.pushMatrix();
/* 59 */     GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
/* 60 */     GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
/* 61 */     enableStandardItemLighting();
/* 62 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\RenderHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */