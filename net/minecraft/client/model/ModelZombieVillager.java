/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelZombieVillager
/*    */   extends ModelBiped
/*    */ {
/*    */   public ModelZombieVillager() {
/* 10 */     this(0.0F, 0.0F, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelZombieVillager(float p_i1165_1_, float p_i1165_2_, boolean p_i1165_3_) {
/* 15 */     super(p_i1165_1_, 0.0F, 64, p_i1165_3_ ? 32 : 64);
/*    */     
/* 17 */     if (p_i1165_3_) {
/*    */       
/* 19 */       this.bipedHead = new ModelRenderer(this, 0, 0);
/* 20 */       this.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 8, 8, p_i1165_1_);
/* 21 */       this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1165_2_, 0.0F);
/*    */     }
/*    */     else {
/*    */       
/* 25 */       this.bipedHead = new ModelRenderer(this);
/* 26 */       this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1165_2_, 0.0F);
/* 27 */       this.bipedHead.setTextureOffset(0, 32).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, p_i1165_1_);
/* 28 */       this.bipedHead.setTextureOffset(24, 32).addBox(-1.0F, -3.0F, -6.0F, 2, 4, 2, p_i1165_1_);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 34 */     super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
/* 35 */     float f = MathHelper.sin(this.swingProgress * 3.1415927F);
/* 36 */     float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
/* 37 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/* 38 */     this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 39 */     this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
/* 40 */     this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
/* 41 */     this.bipedRightArm.rotateAngleX = -1.5707964F;
/* 42 */     this.bipedLeftArm.rotateAngleX = -1.5707964F;
/* 43 */     this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
/* 44 */     this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
/* 45 */     this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
/* 46 */     this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
/* 47 */     this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
/* 48 */     this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelZombieVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */