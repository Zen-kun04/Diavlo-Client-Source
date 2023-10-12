/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelVillager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderVillager extends RenderLiving<EntityVillager> {
/* 11 */   private static final ResourceLocation villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");
/* 12 */   private static final ResourceLocation farmerVillagerTextures = new ResourceLocation("textures/entity/villager/farmer.png");
/* 13 */   private static final ResourceLocation librarianVillagerTextures = new ResourceLocation("textures/entity/villager/librarian.png");
/* 14 */   private static final ResourceLocation priestVillagerTextures = new ResourceLocation("textures/entity/villager/priest.png");
/* 15 */   private static final ResourceLocation smithVillagerTextures = new ResourceLocation("textures/entity/villager/smith.png");
/* 16 */   private static final ResourceLocation butcherVillagerTextures = new ResourceLocation("textures/entity/villager/butcher.png");
/*    */ 
/*    */   
/*    */   public RenderVillager(RenderManager renderManagerIn) {
/* 20 */     super(renderManagerIn, (ModelBase)new ModelVillager(0.0F), 0.5F);
/* 21 */     addLayer(new LayerCustomHead((getMainModel()).villagerHead));
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelVillager getMainModel() {
/* 26 */     return (ModelVillager)super.getMainModel();
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityVillager entity) {
/* 31 */     switch (entity.getProfession()) {
/*    */       
/*    */       case 0:
/* 34 */         return farmerVillagerTextures;
/*    */       
/*    */       case 1:
/* 37 */         return librarianVillagerTextures;
/*    */       
/*    */       case 2:
/* 40 */         return priestVillagerTextures;
/*    */       
/*    */       case 3:
/* 43 */         return smithVillagerTextures;
/*    */       
/*    */       case 4:
/* 46 */         return butcherVillagerTextures;
/*    */     } 
/*    */     
/* 49 */     return villagerTextures;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime) {
/* 55 */     float f = 0.9375F;
/*    */     
/* 57 */     if (entitylivingbaseIn.getGrowingAge() < 0) {
/*    */       
/* 59 */       f = (float)(f * 0.5D);
/* 60 */       this.shadowSize = 0.25F;
/*    */     }
/*    */     else {
/*    */       
/* 64 */       this.shadowSize = 0.5F;
/*    */     } 
/*    */     
/* 67 */     GlStateManager.scale(f, f, f);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */