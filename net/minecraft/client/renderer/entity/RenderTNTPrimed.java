/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.texture.TextureMap;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityTNTPrimed;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderTNTPrimed
/*    */   extends Render<EntityTNTPrimed> {
/*    */   public RenderTNTPrimed(RenderManager renderManagerIn) {
/* 16 */     super(renderManagerIn);
/* 17 */     this.shadowSize = 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 22 */     BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
/* 23 */     GlStateManager.pushMatrix();
/* 24 */     GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);
/*    */     
/* 26 */     if (entity.fuse - partialTicks + 1.0F < 10.0F) {
/*    */       
/* 28 */       float f = 1.0F - (entity.fuse - partialTicks + 1.0F) / 10.0F;
/* 29 */       f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 30 */       f *= f;
/* 31 */       f *= f;
/* 32 */       float f1 = 1.0F + f * 0.3F;
/* 33 */       GlStateManager.scale(f1, f1, f1);
/*    */     } 
/*    */     
/* 36 */     float f2 = (1.0F - (entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
/* 37 */     bindEntityTexture(entity);
/* 38 */     GlStateManager.translate(-0.5F, -0.5F, 0.5F);
/* 39 */     blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
/* 40 */     GlStateManager.translate(0.0F, 0.0F, 1.0F);
/*    */     
/* 42 */     if (entity.fuse / 5 % 2 == 0) {
/*    */       
/* 44 */       GlStateManager.disableTexture2D();
/* 45 */       GlStateManager.disableLighting();
/* 46 */       GlStateManager.enableBlend();
/* 47 */       GlStateManager.blendFunc(770, 772);
/* 48 */       GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
/* 49 */       GlStateManager.doPolygonOffset(-3.0F, -3.0F);
/* 50 */       GlStateManager.enablePolygonOffset();
/* 51 */       blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
/* 52 */       GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 53 */       GlStateManager.disablePolygonOffset();
/* 54 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 55 */       GlStateManager.disableBlend();
/* 56 */       GlStateManager.enableLighting();
/* 57 */       GlStateManager.enableTexture2D();
/*    */     } 
/*    */     
/* 60 */     GlStateManager.popMatrix();
/* 61 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityTNTPrimed entity) {
/* 66 */     return TextureMap.locationBlocksTexture;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderTNTPrimed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */