/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelCreeper;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityCreeper;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderCreeper extends RenderLiving<EntityCreeper> {
/* 12 */   private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");
/*    */ 
/*    */   
/*    */   public RenderCreeper(RenderManager renderManagerIn) {
/* 16 */     super(renderManagerIn, (ModelBase)new ModelCreeper(), 0.5F);
/* 17 */     addLayer(new LayerCreeperCharge(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityCreeper entitylivingbaseIn, float partialTickTime) {
/* 22 */     float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
/* 23 */     float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
/* 24 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 25 */     f *= f;
/* 26 */     f *= f;
/* 27 */     float f2 = (1.0F + f * 0.4F) * f1;
/* 28 */     float f3 = (1.0F + f * 0.1F) / f1;
/* 29 */     GlStateManager.scale(f2, f3, f2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getColorMultiplier(EntityCreeper entitylivingbaseIn, float lightBrightness, float partialTickTime) {
/* 34 */     float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
/*    */     
/* 36 */     if ((int)(f * 10.0F) % 2 == 0)
/*    */     {
/* 38 */       return 0;
/*    */     }
/*    */ 
/*    */     
/* 42 */     int i = (int)(f * 0.2F * 255.0F);
/* 43 */     i = MathHelper.clamp_int(i, 0, 255);
/* 44 */     return i << 24 | 0xFFFFFF;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityCreeper entity) {
/* 50 */     return creeperTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */