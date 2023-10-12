/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.projectile.EntityArrow;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ public class RenderArrow extends Render<EntityArrow> {
/* 14 */   private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");
/*    */ 
/*    */   
/*    */   public RenderArrow(RenderManager renderManagerIn) {
/* 18 */     super(renderManagerIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 23 */     bindEntityTexture(entity);
/* 24 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 25 */     GlStateManager.pushMatrix();
/* 26 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 27 */     GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
/* 28 */     GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
/* 29 */     Tessellator tessellator = Tessellator.getInstance();
/* 30 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 31 */     int i = 0;
/* 32 */     float f = 0.0F;
/* 33 */     float f1 = 0.5F;
/* 34 */     float f2 = (0 + i * 10) / 32.0F;
/* 35 */     float f3 = (5 + i * 10) / 32.0F;
/* 36 */     float f4 = 0.0F;
/* 37 */     float f5 = 0.15625F;
/* 38 */     float f6 = (5 + i * 10) / 32.0F;
/* 39 */     float f7 = (10 + i * 10) / 32.0F;
/* 40 */     float f8 = 0.05625F;
/* 41 */     GlStateManager.enableRescaleNormal();
/* 42 */     float f9 = entity.arrowShake - partialTicks;
/*    */     
/* 44 */     if (f9 > 0.0F) {
/*    */       
/* 46 */       float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
/* 47 */       GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
/*    */     } 
/*    */     
/* 50 */     GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
/* 51 */     GlStateManager.scale(f8, f8, f8);
/* 52 */     GlStateManager.translate(-4.0F, 0.0F, 0.0F);
/* 53 */     GL11.glNormal3f(f8, 0.0F, 0.0F);
/* 54 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 55 */     worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex(f4, f6).endVertex();
/* 56 */     worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex(f5, f6).endVertex();
/* 57 */     worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex(f5, f7).endVertex();
/* 58 */     worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex(f4, f7).endVertex();
/* 59 */     tessellator.draw();
/* 60 */     GL11.glNormal3f(-f8, 0.0F, 0.0F);
/* 61 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 62 */     worldrenderer.pos(-7.0D, 2.0D, -2.0D).tex(f4, f6).endVertex();
/* 63 */     worldrenderer.pos(-7.0D, 2.0D, 2.0D).tex(f5, f6).endVertex();
/* 64 */     worldrenderer.pos(-7.0D, -2.0D, 2.0D).tex(f5, f7).endVertex();
/* 65 */     worldrenderer.pos(-7.0D, -2.0D, -2.0D).tex(f4, f7).endVertex();
/* 66 */     tessellator.draw();
/*    */     
/* 68 */     for (int j = 0; j < 4; j++) {
/*    */       
/* 70 */       GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/* 71 */       GL11.glNormal3f(0.0F, 0.0F, f8);
/* 72 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 73 */       worldrenderer.pos(-8.0D, -2.0D, 0.0D).tex(f, f2).endVertex();
/* 74 */       worldrenderer.pos(8.0D, -2.0D, 0.0D).tex(f1, f2).endVertex();
/* 75 */       worldrenderer.pos(8.0D, 2.0D, 0.0D).tex(f1, f3).endVertex();
/* 76 */       worldrenderer.pos(-8.0D, 2.0D, 0.0D).tex(f, f3).endVertex();
/* 77 */       tessellator.draw();
/*    */     } 
/*    */     
/* 80 */     GlStateManager.disableRescaleNormal();
/* 81 */     GlStateManager.popMatrix();
/* 82 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityArrow entity) {
/* 87 */     return arrowTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */