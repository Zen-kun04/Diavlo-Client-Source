/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntitySquid;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderSquid extends RenderLiving<EntitySquid> {
/* 10 */   private static final ResourceLocation squidTextures = new ResourceLocation("textures/entity/squid.png");
/*    */ 
/*    */   
/*    */   public RenderSquid(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/* 14 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntitySquid entity) {
/* 19 */     return squidTextures;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void rotateCorpse(EntitySquid bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 24 */     float f = bat.prevSquidPitch + (bat.squidPitch - bat.prevSquidPitch) * partialTicks;
/* 25 */     float f1 = bat.prevSquidYaw + (bat.squidYaw - bat.prevSquidYaw) * partialTicks;
/* 26 */     GlStateManager.translate(0.0F, 0.5F, 0.0F);
/* 27 */     GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
/* 28 */     GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
/* 29 */     GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
/* 30 */     GlStateManager.translate(0.0F, -1.2F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float handleRotationFloat(EntitySquid livingBase, float partialTicks) {
/* 35 */     return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderSquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */