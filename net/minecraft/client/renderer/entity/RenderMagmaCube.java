/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelMagmaCube;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityMagmaCube;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderMagmaCube extends RenderLiving<EntityMagmaCube> {
/* 10 */   private static final ResourceLocation magmaCubeTextures = new ResourceLocation("textures/entity/slime/magmacube.png");
/*    */ 
/*    */   
/*    */   public RenderMagmaCube(RenderManager renderManagerIn) {
/* 14 */     super(renderManagerIn, (ModelBase)new ModelMagmaCube(), 0.25F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityMagmaCube entity) {
/* 19 */     return magmaCubeTextures;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityMagmaCube entitylivingbaseIn, float partialTickTime) {
/* 24 */     int i = entitylivingbaseIn.getSlimeSize();
/* 25 */     float f = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (i * 0.5F + 1.0F);
/* 26 */     float f1 = 1.0F / (f + 1.0F);
/* 27 */     float f2 = i;
/* 28 */     GlStateManager.scale(f1 * f2, 1.0F / f1 * f2, f1 * f2);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */