/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelSpider
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer spiderHead;
/*     */   public ModelRenderer spiderNeck;
/*     */   public ModelRenderer spiderBody;
/*     */   public ModelRenderer spiderLeg1;
/*     */   public ModelRenderer spiderLeg2;
/*     */   public ModelRenderer spiderLeg3;
/*     */   public ModelRenderer spiderLeg4;
/*     */   public ModelRenderer spiderLeg5;
/*     */   public ModelRenderer spiderLeg6;
/*     */   public ModelRenderer spiderLeg7;
/*     */   public ModelRenderer spiderLeg8;
/*     */   
/*     */   public ModelSpider() {
/*  22 */     float f = 0.0F;
/*  23 */     int i = 15;
/*  24 */     this.spiderHead = new ModelRenderer(this, 32, 4);
/*  25 */     this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
/*  26 */     this.spiderHead.setRotationPoint(0.0F, i, -3.0F);
/*  27 */     this.spiderNeck = new ModelRenderer(this, 0, 0);
/*  28 */     this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
/*  29 */     this.spiderNeck.setRotationPoint(0.0F, i, 0.0F);
/*  30 */     this.spiderBody = new ModelRenderer(this, 0, 12);
/*  31 */     this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
/*  32 */     this.spiderBody.setRotationPoint(0.0F, i, 9.0F);
/*  33 */     this.spiderLeg1 = new ModelRenderer(this, 18, 0);
/*  34 */     this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  35 */     this.spiderLeg1.setRotationPoint(-4.0F, i, 2.0F);
/*  36 */     this.spiderLeg2 = new ModelRenderer(this, 18, 0);
/*  37 */     this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  38 */     this.spiderLeg2.setRotationPoint(4.0F, i, 2.0F);
/*  39 */     this.spiderLeg3 = new ModelRenderer(this, 18, 0);
/*  40 */     this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  41 */     this.spiderLeg3.setRotationPoint(-4.0F, i, 1.0F);
/*  42 */     this.spiderLeg4 = new ModelRenderer(this, 18, 0);
/*  43 */     this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  44 */     this.spiderLeg4.setRotationPoint(4.0F, i, 1.0F);
/*  45 */     this.spiderLeg5 = new ModelRenderer(this, 18, 0);
/*  46 */     this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  47 */     this.spiderLeg5.setRotationPoint(-4.0F, i, 0.0F);
/*  48 */     this.spiderLeg6 = new ModelRenderer(this, 18, 0);
/*  49 */     this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  50 */     this.spiderLeg6.setRotationPoint(4.0F, i, 0.0F);
/*  51 */     this.spiderLeg7 = new ModelRenderer(this, 18, 0);
/*  52 */     this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  53 */     this.spiderLeg7.setRotationPoint(-4.0F, i, -1.0F);
/*  54 */     this.spiderLeg8 = new ModelRenderer(this, 18, 0);
/*  55 */     this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
/*  56 */     this.spiderLeg8.setRotationPoint(4.0F, i, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  61 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  62 */     this.spiderHead.render(scale);
/*  63 */     this.spiderNeck.render(scale);
/*  64 */     this.spiderBody.render(scale);
/*  65 */     this.spiderLeg1.render(scale);
/*  66 */     this.spiderLeg2.render(scale);
/*  67 */     this.spiderLeg3.render(scale);
/*  68 */     this.spiderLeg4.render(scale);
/*  69 */     this.spiderLeg5.render(scale);
/*  70 */     this.spiderLeg6.render(scale);
/*  71 */     this.spiderLeg7.render(scale);
/*  72 */     this.spiderLeg8.render(scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/*  77 */     this.spiderHead.rotateAngleY = netHeadYaw / 57.295776F;
/*  78 */     this.spiderHead.rotateAngleX = headPitch / 57.295776F;
/*  79 */     float f = 0.7853982F;
/*  80 */     this.spiderLeg1.rotateAngleZ = -f;
/*  81 */     this.spiderLeg2.rotateAngleZ = f;
/*  82 */     this.spiderLeg3.rotateAngleZ = -f * 0.74F;
/*  83 */     this.spiderLeg4.rotateAngleZ = f * 0.74F;
/*  84 */     this.spiderLeg5.rotateAngleZ = -f * 0.74F;
/*  85 */     this.spiderLeg6.rotateAngleZ = f * 0.74F;
/*  86 */     this.spiderLeg7.rotateAngleZ = -f;
/*  87 */     this.spiderLeg8.rotateAngleZ = f;
/*  88 */     float f1 = -0.0F;
/*  89 */     float f2 = 0.3926991F;
/*  90 */     this.spiderLeg1.rotateAngleY = f2 * 2.0F + f1;
/*  91 */     this.spiderLeg2.rotateAngleY = -f2 * 2.0F - f1;
/*  92 */     this.spiderLeg3.rotateAngleY = f2 * 1.0F + f1;
/*  93 */     this.spiderLeg4.rotateAngleY = -f2 * 1.0F - f1;
/*  94 */     this.spiderLeg5.rotateAngleY = -f2 * 1.0F + f1;
/*  95 */     this.spiderLeg6.rotateAngleY = f2 * 1.0F - f1;
/*  96 */     this.spiderLeg7.rotateAngleY = -f2 * 2.0F + f1;
/*  97 */     this.spiderLeg8.rotateAngleY = f2 * 2.0F - f1;
/*  98 */     float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
/*  99 */     float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * limbSwingAmount;
/* 100 */     float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * limbSwingAmount;
/* 101 */     float f6 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 4.712389F) * 0.4F) * limbSwingAmount;
/* 102 */     float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
/* 103 */     float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 3.1415927F) * 0.4F) * limbSwingAmount;
/* 104 */     float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 1.5707964F) * 0.4F) * limbSwingAmount;
/* 105 */     float f10 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 4.712389F) * 0.4F) * limbSwingAmount;
/* 106 */     this.spiderLeg1.rotateAngleY += f3;
/* 107 */     this.spiderLeg2.rotateAngleY += -f3;
/* 108 */     this.spiderLeg3.rotateAngleY += f4;
/* 109 */     this.spiderLeg4.rotateAngleY += -f4;
/* 110 */     this.spiderLeg5.rotateAngleY += f5;
/* 111 */     this.spiderLeg6.rotateAngleY += -f5;
/* 112 */     this.spiderLeg7.rotateAngleY += f6;
/* 113 */     this.spiderLeg8.rotateAngleY += -f6;
/* 114 */     this.spiderLeg1.rotateAngleZ += f7;
/* 115 */     this.spiderLeg2.rotateAngleZ += -f7;
/* 116 */     this.spiderLeg3.rotateAngleZ += f8;
/* 117 */     this.spiderLeg4.rotateAngleZ += -f8;
/* 118 */     this.spiderLeg5.rotateAngleZ += f9;
/* 119 */     this.spiderLeg6.rotateAngleZ += -f9;
/* 120 */     this.spiderLeg7.rotateAngleZ += f10;
/* 121 */     this.spiderLeg8.rotateAngleZ += -f10;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */