/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelBoat;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityBoat;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderBoat extends Render<EntityBoat> {
/* 12 */   private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");
/* 13 */   protected ModelBase modelBoat = (ModelBase)new ModelBoat();
/*    */ 
/*    */   
/*    */   public RenderBoat(RenderManager renderManagerIn) {
/* 17 */     super(renderManagerIn);
/* 18 */     this.shadowSize = 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 23 */     GlStateManager.pushMatrix();
/* 24 */     GlStateManager.translate((float)x, (float)y + 0.25F, (float)z);
/* 25 */     GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
/* 26 */     float f = entity.getTimeSinceHit() - partialTicks;
/* 27 */     float f1 = entity.getDamageTaken() - partialTicks;
/*    */     
/* 29 */     if (f1 < 0.0F)
/*    */     {
/* 31 */       f1 = 0.0F;
/*    */     }
/*    */     
/* 34 */     if (f > 0.0F)
/*    */     {
/* 36 */       GlStateManager.rotate(MathHelper.sin(f) * f * f1 / 10.0F * entity.getForwardDirection(), 1.0F, 0.0F, 0.0F);
/*    */     }
/*    */     
/* 39 */     float f2 = 0.75F;
/* 40 */     GlStateManager.scale(f2, f2, f2);
/* 41 */     GlStateManager.scale(1.0F / f2, 1.0F / f2, 1.0F / f2);
/* 42 */     bindEntityTexture(entity);
/* 43 */     GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 44 */     this.modelBoat.render((Entity)entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
/* 45 */     GlStateManager.popMatrix();
/* 46 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityBoat entity) {
/* 51 */     return boatTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */