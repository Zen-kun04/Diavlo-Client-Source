/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelGhast;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityGhast;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderGhast extends RenderLiving<EntityGhast> {
/* 10 */   private static final ResourceLocation ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
/* 11 */   private static final ResourceLocation ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
/*    */ 
/*    */   
/*    */   public RenderGhast(RenderManager renderManagerIn) {
/* 15 */     super(renderManagerIn, (ModelBase)new ModelGhast(), 0.5F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityGhast entity) {
/* 20 */     return entity.isAttacking() ? ghastShootingTextures : ghastTextures;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityGhast entitylivingbaseIn, float partialTickTime) {
/* 25 */     float f = 1.0F;
/* 26 */     float f1 = (8.0F + f) / 2.0F;
/* 27 */     float f2 = (8.0F + 1.0F / f) / 2.0F;
/* 28 */     GlStateManager.scale(f2, f1, f2);
/* 29 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */