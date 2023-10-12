/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ 
/*    */ public abstract class ModelBase
/*    */ {
/*    */   public float swingProgress;
/*    */   public boolean isRiding;
/*    */   public boolean isChild = true;
/* 16 */   public List<ModelRenderer> boxList = Lists.newArrayList();
/* 17 */   private Map<String, TextureOffset> modelTextureMap = Maps.newHashMap();
/* 18 */   public int textureWidth = 64;
/* 19 */   public int textureHeight = 32;
/*    */ 
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {}
/*    */ 
/*    */   
/*    */   public ModelRenderer getRandomModelBox(Random rand) {
/* 35 */     return this.boxList.get(rand.nextInt(this.boxList.size()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setTextureOffset(String partName, int x, int y) {
/* 40 */     this.modelTextureMap.put(partName, new TextureOffset(x, y));
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureOffset getTextureOffset(String partName) {
/* 45 */     return this.modelTextureMap.get(partName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void copyModelAngles(ModelRenderer source, ModelRenderer dest) {
/* 50 */     dest.rotateAngleX = source.rotateAngleX;
/* 51 */     dest.rotateAngleY = source.rotateAngleY;
/* 52 */     dest.rotateAngleZ = source.rotateAngleZ;
/* 53 */     dest.rotationPointX = source.rotationPointX;
/* 54 */     dest.rotationPointY = source.rotationPointY;
/* 55 */     dest.rotationPointZ = source.rotationPointZ;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setModelAttributes(ModelBase model) {
/* 60 */     this.swingProgress = model.swingProgress;
/* 61 */     this.isRiding = model.isRiding;
/* 62 */     this.isChild = model.isChild;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */