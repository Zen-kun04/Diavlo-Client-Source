/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ public class ModelEnderman
/*     */   extends ModelBiped
/*     */ {
/*     */   public boolean isCarrying;
/*     */   public boolean isAttacking;
/*     */   
/*     */   public ModelEnderman(float p_i46305_1_) {
/*  12 */     super(0.0F, -14.0F, 64, 32);
/*  13 */     float f = -14.0F;
/*  14 */     this.bipedHeadwear = new ModelRenderer(this, 0, 16);
/*  15 */     this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46305_1_ - 0.5F);
/*  16 */     this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
/*  17 */     this.bipedBody = new ModelRenderer(this, 32, 16);
/*  18 */     this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46305_1_);
/*  19 */     this.bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
/*  20 */     this.bipedRightArm = new ModelRenderer(this, 56, 0);
/*  21 */     this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  22 */     this.bipedRightArm.setRotationPoint(-3.0F, 2.0F + f, 0.0F);
/*  23 */     this.bipedLeftArm = new ModelRenderer(this, 56, 0);
/*  24 */     this.bipedLeftArm.mirror = true;
/*  25 */     this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  26 */     this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f, 0.0F);
/*  27 */     this.bipedRightLeg = new ModelRenderer(this, 56, 0);
/*  28 */     this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  29 */     this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f, 0.0F);
/*  30 */     this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
/*  31 */     this.bipedLeftLeg.mirror = true;
/*  32 */     this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, p_i46305_1_);
/*  33 */     this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/*  38 */     super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
/*  39 */     this.bipedHead.showModel = true;
/*  40 */     float f = -14.0F;
/*  41 */     this.bipedBody.rotateAngleX = 0.0F;
/*  42 */     this.bipedBody.rotationPointY = f;
/*  43 */     this.bipedBody.rotationPointZ = -0.0F;
/*  44 */     this.bipedRightLeg.rotateAngleX -= 0.0F;
/*  45 */     this.bipedLeftLeg.rotateAngleX -= 0.0F;
/*  46 */     this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX * 0.5D);
/*  47 */     this.bipedLeftArm.rotateAngleX = (float)(this.bipedLeftArm.rotateAngleX * 0.5D);
/*  48 */     this.bipedRightLeg.rotateAngleX = (float)(this.bipedRightLeg.rotateAngleX * 0.5D);
/*  49 */     this.bipedLeftLeg.rotateAngleX = (float)(this.bipedLeftLeg.rotateAngleX * 0.5D);
/*  50 */     float f1 = 0.4F;
/*     */     
/*  52 */     if (this.bipedRightArm.rotateAngleX > f1)
/*     */     {
/*  54 */       this.bipedRightArm.rotateAngleX = f1;
/*     */     }
/*     */     
/*  57 */     if (this.bipedLeftArm.rotateAngleX > f1)
/*     */     {
/*  59 */       this.bipedLeftArm.rotateAngleX = f1;
/*     */     }
/*     */     
/*  62 */     if (this.bipedRightArm.rotateAngleX < -f1)
/*     */     {
/*  64 */       this.bipedRightArm.rotateAngleX = -f1;
/*     */     }
/*     */     
/*  67 */     if (this.bipedLeftArm.rotateAngleX < -f1)
/*     */     {
/*  69 */       this.bipedLeftArm.rotateAngleX = -f1;
/*     */     }
/*     */     
/*  72 */     if (this.bipedRightLeg.rotateAngleX > f1)
/*     */     {
/*  74 */       this.bipedRightLeg.rotateAngleX = f1;
/*     */     }
/*     */     
/*  77 */     if (this.bipedLeftLeg.rotateAngleX > f1)
/*     */     {
/*  79 */       this.bipedLeftLeg.rotateAngleX = f1;
/*     */     }
/*     */     
/*  82 */     if (this.bipedRightLeg.rotateAngleX < -f1)
/*     */     {
/*  84 */       this.bipedRightLeg.rotateAngleX = -f1;
/*     */     }
/*     */     
/*  87 */     if (this.bipedLeftLeg.rotateAngleX < -f1)
/*     */     {
/*  89 */       this.bipedLeftLeg.rotateAngleX = -f1;
/*     */     }
/*     */     
/*  92 */     if (this.isCarrying) {
/*     */       
/*  94 */       this.bipedRightArm.rotateAngleX = -0.5F;
/*  95 */       this.bipedLeftArm.rotateAngleX = -0.5F;
/*  96 */       this.bipedRightArm.rotateAngleZ = 0.05F;
/*  97 */       this.bipedLeftArm.rotateAngleZ = -0.05F;
/*     */     } 
/*     */     
/* 100 */     this.bipedRightArm.rotationPointZ = 0.0F;
/* 101 */     this.bipedLeftArm.rotationPointZ = 0.0F;
/* 102 */     this.bipedRightLeg.rotationPointZ = 0.0F;
/* 103 */     this.bipedLeftLeg.rotationPointZ = 0.0F;
/* 104 */     this.bipedRightLeg.rotationPointY = 9.0F + f;
/* 105 */     this.bipedLeftLeg.rotationPointY = 9.0F + f;
/* 106 */     this.bipedHead.rotationPointZ = -0.0F;
/* 107 */     this.bipedHead.rotationPointY = f + 1.0F;
/* 108 */     this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
/* 109 */     this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
/* 110 */     this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
/* 111 */     this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
/* 112 */     this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
/* 113 */     this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
/*     */     
/* 115 */     if (this.isAttacking) {
/*     */       
/* 117 */       float f2 = 1.0F;
/* 118 */       this.bipedHead.rotationPointY -= f2 * 5.0F;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelEnderman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */