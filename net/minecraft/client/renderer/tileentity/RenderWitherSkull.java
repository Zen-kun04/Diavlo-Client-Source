/*    */ package net.minecraft.client.renderer.tileentity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelSkeletonHead;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderWitherSkull extends Render<EntityWitherSkull> {
/* 12 */   private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
/* 13 */   private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
/* 14 */   private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
/*    */ 
/*    */   
/*    */   public RenderWitherSkull(RenderManager renderManagerIn) {
/* 18 */     super(renderManagerIn);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private float func_82400_a(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
/*    */     float f;
/* 25 */     for (f = p_82400_2_ - p_82400_1_; f < -180.0F; f += 360.0F);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     while (f >= 180.0F)
/*    */     {
/* 32 */       f -= 360.0F;
/*    */     }
/*    */     
/* 35 */     return p_82400_1_ + p_82400_3_ * f;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityWitherSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 40 */     GlStateManager.pushMatrix();
/* 41 */     GlStateManager.disableCull();
/* 42 */     float f = func_82400_a(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
/* 43 */     float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
/* 44 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 45 */     float f2 = 0.0625F;
/* 46 */     GlStateManager.enableRescaleNormal();
/* 47 */     GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 48 */     GlStateManager.enableAlpha();
/* 49 */     bindEntityTexture((Entity)entity);
/* 50 */     this.skeletonHeadModel.render((Entity)entity, 0.0F, 0.0F, 0.0F, f, f1, f2);
/* 51 */     GlStateManager.popMatrix();
/* 52 */     super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityWitherSkull entity) {
/* 57 */     return entity.isInvulnerable() ? invulnerableWitherTextures : witherTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\tileentity\RenderWitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */