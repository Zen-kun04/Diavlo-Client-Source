/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelWither;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerWitherAura;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.boss.BossStatus;
/*    */ import net.minecraft.entity.boss.EntityWither;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderWither extends RenderLiving<EntityWither> {
/* 12 */   private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
/* 13 */   private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
/*    */ 
/*    */   
/*    */   public RenderWither(RenderManager renderManagerIn) {
/* 17 */     super(renderManagerIn, (ModelBase)new ModelWither(0.0F), 1.0F);
/* 18 */     addLayer(new LayerWitherAura(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityWither entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 23 */     BossStatus.setBossStatus((IBossDisplayData)entity, true);
/* 24 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityWither entity) {
/* 29 */     int i = entity.getInvulTime();
/* 30 */     return (i > 0 && (i > 80 || i / 5 % 2 != 1)) ? invulnerableWitherTextures : witherTextures;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityWither entitylivingbaseIn, float partialTickTime) {
/* 35 */     float f = 2.0F;
/* 36 */     int i = entitylivingbaseIn.getInvulTime();
/*    */     
/* 38 */     if (i > 0)
/*    */     {
/* 40 */       f -= (i - partialTickTime) / 220.0F * 0.5F;
/*    */     }
/*    */     
/* 43 */     GlStateManager.scale(f, f, f);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */