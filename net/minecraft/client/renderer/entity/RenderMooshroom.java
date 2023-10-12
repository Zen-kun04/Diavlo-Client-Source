/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityMooshroom;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderMooshroom extends RenderLiving<EntityMooshroom> {
/* 10 */   private static final ResourceLocation mooshroomTextures = new ResourceLocation("textures/entity/cow/mooshroom.png");
/*    */ 
/*    */   
/*    */   public RenderMooshroom(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/* 14 */     super(renderManagerIn, modelBaseIn, shadowSizeIn);
/* 15 */     addLayer(new LayerMooshroomMushroom(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityMooshroom entity) {
/* 20 */     return mooshroomTextures;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderMooshroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */