/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.model.ModelZombie;
/*    */ import net.minecraft.client.model.ModelZombieVillager;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityZombie;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderZombie extends RenderBiped<EntityZombie> {
/* 18 */   private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");
/* 19 */   private static final ResourceLocation zombieVillagerTextures = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
/*    */   
/*    */   private final ModelBiped field_82434_o;
/*    */   private final ModelZombieVillager zombieVillagerModel;
/*    */   private final List<LayerRenderer<EntityZombie>> field_177121_n;
/*    */   private final List<LayerRenderer<EntityZombie>> field_177122_o;
/*    */   
/*    */   public RenderZombie(RenderManager renderManagerIn) {
/* 27 */     super(renderManagerIn, (ModelBiped)new ModelZombie(), 0.5F, 1.0F);
/* 28 */     LayerRenderer layerrenderer = this.layerRenderers.get(0);
/* 29 */     this.field_82434_o = this.modelBipedMain;
/* 30 */     this.zombieVillagerModel = new ModelZombieVillager();
/* 31 */     addLayer(new LayerHeldItem(this));
/* 32 */     LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
/*    */       {
/*    */         protected void initArmor()
/*    */         {
/* 36 */           this.modelLeggings = (ModelBase)new ModelZombie(0.5F, true);
/* 37 */           this.modelArmor = (ModelBase)new ModelZombie(1.0F, true);
/*    */         }
/*    */       };
/* 40 */     addLayer(layerbipedarmor);
/* 41 */     this.field_177122_o = Lists.newArrayList(this.layerRenderers);
/*    */     
/* 43 */     if (layerrenderer instanceof LayerCustomHead) {
/*    */       
/* 45 */       removeLayer(layerrenderer);
/* 46 */       addLayer(new LayerCustomHead(this.zombieVillagerModel.bipedHead));
/*    */     } 
/*    */     
/* 49 */     removeLayer(layerbipedarmor);
/* 50 */     addLayer(new LayerVillagerArmor(this));
/* 51 */     this.field_177121_n = Lists.newArrayList(this.layerRenderers);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 56 */     func_82427_a(entity);
/* 57 */     super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityZombie entity) {
/* 62 */     return entity.isVillager() ? zombieVillagerTextures : zombieTextures;
/*    */   }
/*    */ 
/*    */   
/*    */   private void func_82427_a(EntityZombie zombie) {
/* 67 */     if (zombie.isVillager()) {
/*    */       
/* 69 */       this.mainModel = (ModelBase)this.zombieVillagerModel;
/* 70 */       this.layerRenderers = this.field_177121_n;
/*    */     }
/*    */     else {
/*    */       
/* 74 */       this.mainModel = (ModelBase)this.field_82434_o;
/* 75 */       this.layerRenderers = this.field_177122_o;
/*    */     } 
/*    */     
/* 78 */     this.modelBipedMain = (ModelBiped)this.mainModel;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void rotateCorpse(EntityZombie bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 83 */     if (bat.isConverting())
/*    */     {
/* 85 */       p_77043_3_ += (float)(Math.cos(bat.ticksExisted * 3.25D) * Math.PI * 0.25D);
/*    */     }
/*    */     
/* 88 */     super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */