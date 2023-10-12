/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityRabbit;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelRabbit
/*     */   extends ModelBase {
/*     */   ModelRenderer rabbitLeftFoot;
/*     */   ModelRenderer rabbitRightFoot;
/*     */   ModelRenderer rabbitLeftThigh;
/*     */   ModelRenderer rabbitRightThigh;
/*     */   ModelRenderer rabbitBody;
/*     */   ModelRenderer rabbitLeftArm;
/*     */   ModelRenderer rabbitRightArm;
/*     */   ModelRenderer rabbitHead;
/*     */   ModelRenderer rabbitRightEar;
/*     */   ModelRenderer rabbitLeftEar;
/*     */   ModelRenderer rabbitTail;
/*     */   ModelRenderer rabbitNose;
/*  23 */   private float field_178701_m = 0.0F;
/*  24 */   private float field_178699_n = 0.0F;
/*     */ 
/*     */   
/*     */   public ModelRabbit() {
/*  28 */     setTextureOffset("head.main", 0, 0);
/*  29 */     setTextureOffset("head.nose", 0, 24);
/*  30 */     setTextureOffset("head.ear1", 0, 10);
/*  31 */     setTextureOffset("head.ear2", 6, 10);
/*  32 */     this.rabbitLeftFoot = new ModelRenderer(this, 26, 24);
/*  33 */     this.rabbitLeftFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
/*  34 */     this.rabbitLeftFoot.setRotationPoint(3.0F, 17.5F, 3.7F);
/*  35 */     this.rabbitLeftFoot.mirror = true;
/*  36 */     setRotationOffset(this.rabbitLeftFoot, 0.0F, 0.0F, 0.0F);
/*  37 */     this.rabbitRightFoot = new ModelRenderer(this, 8, 24);
/*  38 */     this.rabbitRightFoot.addBox(-1.0F, 5.5F, -3.7F, 2, 1, 7);
/*  39 */     this.rabbitRightFoot.setRotationPoint(-3.0F, 17.5F, 3.7F);
/*  40 */     this.rabbitRightFoot.mirror = true;
/*  41 */     setRotationOffset(this.rabbitRightFoot, 0.0F, 0.0F, 0.0F);
/*  42 */     this.rabbitLeftThigh = new ModelRenderer(this, 30, 15);
/*  43 */     this.rabbitLeftThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
/*  44 */     this.rabbitLeftThigh.setRotationPoint(3.0F, 17.5F, 3.7F);
/*  45 */     this.rabbitLeftThigh.mirror = true;
/*  46 */     setRotationOffset(this.rabbitLeftThigh, -0.34906584F, 0.0F, 0.0F);
/*  47 */     this.rabbitRightThigh = new ModelRenderer(this, 16, 15);
/*  48 */     this.rabbitRightThigh.addBox(-1.0F, 0.0F, 0.0F, 2, 4, 5);
/*  49 */     this.rabbitRightThigh.setRotationPoint(-3.0F, 17.5F, 3.7F);
/*  50 */     this.rabbitRightThigh.mirror = true;
/*  51 */     setRotationOffset(this.rabbitRightThigh, -0.34906584F, 0.0F, 0.0F);
/*  52 */     this.rabbitBody = new ModelRenderer(this, 0, 0);
/*  53 */     this.rabbitBody.addBox(-3.0F, -2.0F, -10.0F, 6, 5, 10);
/*  54 */     this.rabbitBody.setRotationPoint(0.0F, 19.0F, 8.0F);
/*  55 */     this.rabbitBody.mirror = true;
/*  56 */     setRotationOffset(this.rabbitBody, -0.34906584F, 0.0F, 0.0F);
/*  57 */     this.rabbitLeftArm = new ModelRenderer(this, 8, 15);
/*  58 */     this.rabbitLeftArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
/*  59 */     this.rabbitLeftArm.setRotationPoint(3.0F, 17.0F, -1.0F);
/*  60 */     this.rabbitLeftArm.mirror = true;
/*  61 */     setRotationOffset(this.rabbitLeftArm, -0.17453292F, 0.0F, 0.0F);
/*  62 */     this.rabbitRightArm = new ModelRenderer(this, 0, 15);
/*  63 */     this.rabbitRightArm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2);
/*  64 */     this.rabbitRightArm.setRotationPoint(-3.0F, 17.0F, -1.0F);
/*  65 */     this.rabbitRightArm.mirror = true;
/*  66 */     setRotationOffset(this.rabbitRightArm, -0.17453292F, 0.0F, 0.0F);
/*  67 */     this.rabbitHead = new ModelRenderer(this, 32, 0);
/*  68 */     this.rabbitHead.addBox(-2.5F, -4.0F, -5.0F, 5, 4, 5);
/*  69 */     this.rabbitHead.setRotationPoint(0.0F, 16.0F, -1.0F);
/*  70 */     this.rabbitHead.mirror = true;
/*  71 */     setRotationOffset(this.rabbitHead, 0.0F, 0.0F, 0.0F);
/*  72 */     this.rabbitRightEar = new ModelRenderer(this, 52, 0);
/*  73 */     this.rabbitRightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
/*  74 */     this.rabbitRightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
/*  75 */     this.rabbitRightEar.mirror = true;
/*  76 */     setRotationOffset(this.rabbitRightEar, 0.0F, -0.2617994F, 0.0F);
/*  77 */     this.rabbitLeftEar = new ModelRenderer(this, 58, 0);
/*  78 */     this.rabbitLeftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
/*  79 */     this.rabbitLeftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
/*  80 */     this.rabbitLeftEar.mirror = true;
/*  81 */     setRotationOffset(this.rabbitLeftEar, 0.0F, 0.2617994F, 0.0F);
/*  82 */     this.rabbitTail = new ModelRenderer(this, 52, 6);
/*  83 */     this.rabbitTail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2);
/*  84 */     this.rabbitTail.setRotationPoint(0.0F, 20.0F, 7.0F);
/*  85 */     this.rabbitTail.mirror = true;
/*  86 */     setRotationOffset(this.rabbitTail, -0.3490659F, 0.0F, 0.0F);
/*  87 */     this.rabbitNose = new ModelRenderer(this, 32, 9);
/*  88 */     this.rabbitNose.addBox(-0.5F, -2.5F, -5.5F, 1, 1, 1);
/*  89 */     this.rabbitNose.setRotationPoint(0.0F, 16.0F, -1.0F);
/*  90 */     this.rabbitNose.mirror = true;
/*  91 */     setRotationOffset(this.rabbitNose, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setRotationOffset(ModelRenderer p_178691_1_, float p_178691_2_, float p_178691_3_, float p_178691_4_) {
/*  96 */     p_178691_1_.rotateAngleX = p_178691_2_;
/*  97 */     p_178691_1_.rotateAngleY = p_178691_3_;
/*  98 */     p_178691_1_.rotateAngleZ = p_178691_4_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 103 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/* 105 */     if (this.isChild) {
/*     */       
/* 107 */       float f = 2.0F;
/* 108 */       GlStateManager.pushMatrix();
/* 109 */       GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
/* 110 */       this.rabbitHead.render(scale);
/* 111 */       this.rabbitLeftEar.render(scale);
/* 112 */       this.rabbitRightEar.render(scale);
/* 113 */       this.rabbitNose.render(scale);
/* 114 */       GlStateManager.popMatrix();
/* 115 */       GlStateManager.pushMatrix();
/* 116 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 117 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 118 */       this.rabbitLeftFoot.render(scale);
/* 119 */       this.rabbitRightFoot.render(scale);
/* 120 */       this.rabbitLeftThigh.render(scale);
/* 121 */       this.rabbitRightThigh.render(scale);
/* 122 */       this.rabbitBody.render(scale);
/* 123 */       this.rabbitLeftArm.render(scale);
/* 124 */       this.rabbitRightArm.render(scale);
/* 125 */       this.rabbitTail.render(scale);
/* 126 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else {
/*     */       
/* 130 */       this.rabbitLeftFoot.render(scale);
/* 131 */       this.rabbitRightFoot.render(scale);
/* 132 */       this.rabbitLeftThigh.render(scale);
/* 133 */       this.rabbitRightThigh.render(scale);
/* 134 */       this.rabbitBody.render(scale);
/* 135 */       this.rabbitLeftArm.render(scale);
/* 136 */       this.rabbitRightArm.render(scale);
/* 137 */       this.rabbitHead.render(scale);
/* 138 */       this.rabbitRightEar.render(scale);
/* 139 */       this.rabbitLeftEar.render(scale);
/* 140 */       this.rabbitTail.render(scale);
/* 141 */       this.rabbitNose.render(scale);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 147 */     float f = ageInTicks - entityIn.ticksExisted;
/* 148 */     EntityRabbit entityrabbit = (EntityRabbit)entityIn;
/* 149 */     this.rabbitLeftEar.rotateAngleX = headPitch * 0.017453292F;
/* 150 */     this.rabbitHead.rotateAngleY = netHeadYaw * 0.017453292F;
/* 151 */     this.rabbitNose.rotateAngleY -= 0.2617994F;
/* 152 */     this.rabbitNose.rotateAngleY += 0.2617994F;
/* 153 */     this.field_178701_m = MathHelper.sin(entityrabbit.func_175521_o(f) * 3.1415927F);
/* 154 */     this.rabbitRightThigh.rotateAngleX = (this.field_178701_m * 50.0F - 21.0F) * 0.017453292F;
/* 155 */     this.rabbitRightFoot.rotateAngleX = this.field_178701_m * 50.0F * 0.017453292F;
/* 156 */     this.rabbitRightArm.rotateAngleX = (this.field_178701_m * -40.0F - 11.0F) * 0.017453292F;
/*     */   }
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */