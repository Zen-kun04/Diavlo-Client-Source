/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityWolf;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderWolf extends RenderLiving<EntityWolf> {
/* 11 */   private static final ResourceLocation wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
/* 12 */   private static final ResourceLocation tamedWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
/* 13 */   private static final ResourceLocation anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
/*    */ 
/*    */   
/*    */   public RenderWolf(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/* 17 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/* 18 */     addLayer(new LayerWolfCollar(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected float handleRotationFloat(EntityWolf livingBase, float partialTicks) {
/* 23 */     return livingBase.getTailRotation();
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityWolf entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 28 */     if (entity.isWolfWet()) {
/*    */       
/* 30 */       float f = entity.getBrightness(partialTicks) * entity.getShadingWhileWet(partialTicks);
/* 31 */       GlStateManager.color(f, f, f);
/*    */     } 
/*    */     
/* 34 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityWolf entity) {
/* 39 */     return entity.isTamed() ? tamedWolfTextures : (entity.isAngry() ? anrgyWolfTextures : wolfTextures);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */