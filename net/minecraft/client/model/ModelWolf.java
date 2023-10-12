/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelWolf
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer wolfHeadMain;
/*     */   public ModelRenderer wolfBody;
/*     */   public ModelRenderer wolfLeg1;
/*     */   public ModelRenderer wolfLeg2;
/*     */   public ModelRenderer wolfLeg3;
/*     */   public ModelRenderer wolfLeg4;
/*     */   ModelRenderer wolfTail;
/*     */   ModelRenderer wolfMane;
/*     */   
/*     */   public ModelWolf() {
/*  22 */     float f = 0.0F;
/*  23 */     float f1 = 13.5F;
/*  24 */     this.wolfHeadMain = new ModelRenderer(this, 0, 0);
/*  25 */     this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
/*  26 */     this.wolfHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
/*  27 */     this.wolfBody = new ModelRenderer(this, 18, 14);
/*  28 */     this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
/*  29 */     this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
/*  30 */     this.wolfMane = new ModelRenderer(this, 21, 0);
/*  31 */     this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
/*  32 */     this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
/*  33 */     this.wolfLeg1 = new ModelRenderer(this, 0, 18);
/*  34 */     this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  35 */     this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
/*  36 */     this.wolfLeg2 = new ModelRenderer(this, 0, 18);
/*  37 */     this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  38 */     this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
/*  39 */     this.wolfLeg3 = new ModelRenderer(this, 0, 18);
/*  40 */     this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  41 */     this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
/*  42 */     this.wolfLeg4 = new ModelRenderer(this, 0, 18);
/*  43 */     this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  44 */     this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
/*  45 */     this.wolfTail = new ModelRenderer(this, 9, 18);
/*  46 */     this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
/*  47 */     this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
/*  48 */     this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
/*  49 */     this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
/*  50 */     this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  55 */     super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
/*  56 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/*  58 */     if (this.isChild) {
/*     */       
/*  60 */       float f = 2.0F;
/*  61 */       GlStateManager.pushMatrix();
/*  62 */       GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
/*  63 */       this.wolfHeadMain.renderWithRotation(scale);
/*  64 */       GlStateManager.popMatrix();
/*  65 */       GlStateManager.pushMatrix();
/*  66 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  67 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  68 */       this.wolfBody.render(scale);
/*  69 */       this.wolfLeg1.render(scale);
/*  70 */       this.wolfLeg2.render(scale);
/*  71 */       this.wolfLeg3.render(scale);
/*  72 */       this.wolfLeg4.render(scale);
/*  73 */       this.wolfTail.renderWithRotation(scale);
/*  74 */       this.wolfMane.render(scale);
/*  75 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else {
/*     */       
/*  79 */       this.wolfHeadMain.renderWithRotation(scale);
/*  80 */       this.wolfBody.render(scale);
/*  81 */       this.wolfLeg1.render(scale);
/*  82 */       this.wolfLeg2.render(scale);
/*  83 */       this.wolfLeg3.render(scale);
/*  84 */       this.wolfLeg4.render(scale);
/*  85 */       this.wolfTail.renderWithRotation(scale);
/*  86 */       this.wolfMane.render(scale);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/*  92 */     EntityWolf entitywolf = (EntityWolf)entitylivingbaseIn;
/*     */     
/*  94 */     if (entitywolf.isAngry()) {
/*     */       
/*  96 */       this.wolfTail.rotateAngleY = 0.0F;
/*     */     }
/*     */     else {
/*     */       
/* 100 */       this.wolfTail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
/*     */     } 
/*     */     
/* 103 */     if (entitywolf.isSitting()) {
/*     */       
/* 105 */       this.wolfMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
/* 106 */       this.wolfMane.rotateAngleX = 1.2566371F;
/* 107 */       this.wolfMane.rotateAngleY = 0.0F;
/* 108 */       this.wolfBody.setRotationPoint(0.0F, 18.0F, 0.0F);
/* 109 */       this.wolfBody.rotateAngleX = 0.7853982F;
/* 110 */       this.wolfTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
/* 111 */       this.wolfLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
/* 112 */       this.wolfLeg1.rotateAngleX = 4.712389F;
/* 113 */       this.wolfLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
/* 114 */       this.wolfLeg2.rotateAngleX = 4.712389F;
/* 115 */       this.wolfLeg3.rotateAngleX = 5.811947F;
/* 116 */       this.wolfLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
/* 117 */       this.wolfLeg4.rotateAngleX = 5.811947F;
/* 118 */       this.wolfLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
/*     */     }
/*     */     else {
/*     */       
/* 122 */       this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
/* 123 */       this.wolfBody.rotateAngleX = 1.5707964F;
/* 124 */       this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
/* 125 */       this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
/* 126 */       this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
/* 127 */       this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
/* 128 */       this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
/* 129 */       this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
/* 130 */       this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
/* 131 */       this.wolfLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
/* 132 */       this.wolfLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F) * 1.4F * p_78086_3_;
/* 133 */       this.wolfLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F) * 1.4F * p_78086_3_;
/* 134 */       this.wolfLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
/*     */     } 
/*     */     
/* 137 */     this.wolfHeadMain.rotateAngleZ = entitywolf.getInterestedAngle(partialTickTime) + entitywolf.getShakeAngle(partialTickTime, 0.0F);
/* 138 */     this.wolfMane.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.08F);
/* 139 */     this.wolfBody.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.16F);
/* 140 */     this.wolfTail.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.2F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 145 */     super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
/* 146 */     this.wolfHeadMain.rotateAngleX = headPitch / 57.295776F;
/* 147 */     this.wolfHeadMain.rotateAngleY = netHeadYaw / 57.295776F;
/* 148 */     this.wolfTail.rotateAngleX = ageInTicks;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */