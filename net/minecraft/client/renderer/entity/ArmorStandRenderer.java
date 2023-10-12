/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.ModelArmorStand;
/*    */ import net.minecraft.client.model.ModelArmorStandArmor;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class ArmorStandRenderer extends RendererLivingEntity<EntityArmorStand> {
/* 14 */   public static final ResourceLocation TEXTURE_ARMOR_STAND = new ResourceLocation("textures/entity/armorstand/wood.png");
/*    */ 
/*    */   
/*    */   public ArmorStandRenderer(RenderManager p_i46195_1_) {
/* 18 */     super(p_i46195_1_, (ModelBase)new ModelArmorStand(), 0.0F);
/* 19 */     LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
/*    */       {
/*    */         protected void initArmor()
/*    */         {
/* 23 */           this.modelLeggings = (ModelBase)new ModelArmorStandArmor(0.5F);
/* 24 */           this.modelArmor = (ModelBase)new ModelArmorStandArmor(1.0F);
/*    */         }
/*    */       };
/* 27 */     addLayer(layerbipedarmor);
/* 28 */     addLayer(new LayerHeldItem(this));
/* 29 */     addLayer(new LayerCustomHead((getMainModel()).bipedHead));
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityArmorStand entity) {
/* 34 */     return TEXTURE_ARMOR_STAND;
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelArmorStand getMainModel() {
/* 39 */     return (ModelArmorStand)super.getMainModel();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void rotateCorpse(EntityArmorStand bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 44 */     GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canRenderName(EntityArmorStand entity) {
/* 49 */     return entity.getAlwaysRenderNameTag();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\ArmorStandRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */