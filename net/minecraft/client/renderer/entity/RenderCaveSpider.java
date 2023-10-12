/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityCaveSpider;
/*    */ import net.minecraft.entity.monster.EntitySpider;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderCaveSpider extends RenderSpider<EntityCaveSpider> {
/*  9 */   private static final ResourceLocation caveSpiderTextures = new ResourceLocation("textures/entity/spider/cave_spider.png");
/*    */ 
/*    */   
/*    */   public RenderCaveSpider(RenderManager renderManagerIn) {
/* 13 */     super(renderManagerIn);
/* 14 */     this.shadowSize *= 0.7F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityCaveSpider entitylivingbaseIn, float partialTickTime) {
/* 19 */     GlStateManager.scale(0.7F, 0.7F, 0.7F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityCaveSpider entity) {
/* 24 */     return caveSpiderTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderCaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */