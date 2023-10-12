/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelIronGolem;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderIronGolem extends RenderLiving<EntityIronGolem> {
/* 11 */   private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");
/*    */ 
/*    */   
/*    */   public RenderIronGolem(RenderManager renderManagerIn) {
/* 15 */     super(renderManagerIn, (ModelBase)new ModelIronGolem(), 0.5F);
/* 16 */     addLayer(new LayerIronGolemFlower(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityIronGolem entity) {
/* 21 */     return ironGolemTextures;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void rotateCorpse(EntityIronGolem bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 26 */     super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
/*    */     
/* 28 */     if (bat.limbSwingAmount >= 0.01D) {
/*    */       
/* 30 */       float f = 13.0F;
/* 31 */       float f1 = bat.limbSwing - bat.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
/* 32 */       float f2 = (Math.abs(f1 % f - f * 0.5F) - f * 0.25F) / f * 0.25F;
/* 33 */       GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */