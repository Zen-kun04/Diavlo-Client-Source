/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ 
/*    */ public class ModelArmorStandArmor
/*    */   extends ModelBiped
/*    */ {
/*    */   public ModelArmorStandArmor() {
/* 10 */     this(0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelArmorStandArmor(float modelSize) {
/* 15 */     this(modelSize, 64, 32);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ModelArmorStandArmor(float modelSize, int textureWidthIn, int textureHeightIn) {
/* 20 */     super(modelSize, 0.0F, textureWidthIn, textureHeightIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 25 */     if (entityIn instanceof EntityArmorStand) {
/*    */       
/* 27 */       EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
/* 28 */       this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
/* 29 */       this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
/* 30 */       this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
/* 31 */       this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
/* 32 */       this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
/* 33 */       this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
/* 34 */       this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
/* 35 */       this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
/* 36 */       this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
/* 37 */       this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
/* 38 */       this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
/* 39 */       this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
/* 40 */       this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
/* 41 */       this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
/* 42 */       this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
/* 43 */       this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
/* 44 */       this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
/* 45 */       this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
/* 46 */       this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
/* 47 */       this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
/* 48 */       this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
/* 49 */       copyModelAngles(this.bipedHead, this.bipedHeadwear);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelArmorStandArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */