/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ public class ModelPlayer
/*     */   extends ModelBiped
/*     */ {
/*     */   public ModelRenderer bipedLeftArmwear;
/*     */   public ModelRenderer bipedRightArmwear;
/*     */   public ModelRenderer bipedLeftLegwear;
/*     */   public ModelRenderer bipedRightLegwear;
/*     */   public ModelRenderer bipedBodyWear;
/*     */   private ModelRenderer bipedCape;
/*     */   private ModelRenderer bipedDeadmau5Head;
/*     */   private boolean smallArms;
/*     */   
/*     */   public ModelPlayer(float p_i46304_1_, boolean p_i46304_2_) {
/*  19 */     super(p_i46304_1_, 0.0F, 64, 64);
/*  20 */     this.smallArms = p_i46304_2_;
/*  21 */     this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
/*  22 */     this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i46304_1_);
/*  23 */     this.bipedCape = new ModelRenderer(this, 0, 0);
/*  24 */     this.bipedCape.setTextureSize(64, 32);
/*  25 */     this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46304_1_);
/*     */     
/*  27 */     if (p_i46304_2_) {
/*     */       
/*  29 */       this.bipedLeftArm = new ModelRenderer(this, 32, 48);
/*  30 */       this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
/*  31 */       this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
/*  32 */       this.bipedRightArm = new ModelRenderer(this, 40, 16);
/*  33 */       this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
/*  34 */       this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
/*  35 */       this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
/*  36 */       this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
/*  37 */       this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
/*  38 */       this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
/*  39 */       this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
/*  40 */       this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
/*     */     }
/*     */     else {
/*     */       
/*  44 */       this.bipedLeftArm = new ModelRenderer(this, 32, 48);
/*  45 */       this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_);
/*  46 */       this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
/*  47 */       this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
/*  48 */       this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
/*  49 */       this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
/*  50 */       this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
/*  51 */       this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
/*  52 */       this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
/*     */     } 
/*     */     
/*  55 */     this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
/*  56 */     this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_);
/*  57 */     this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
/*  58 */     this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
/*  59 */     this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
/*  60 */     this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
/*  61 */     this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
/*  62 */     this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
/*  63 */     this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
/*  64 */     this.bipedBodyWear = new ModelRenderer(this, 16, 32);
/*  65 */     this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46304_1_ + 0.25F);
/*  66 */     this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  71 */     super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
/*  72 */     GlStateManager.pushMatrix();
/*     */     
/*  74 */     if (this.isChild) {
/*     */       
/*  76 */       float f = 2.0F;
/*  77 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  78 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  79 */       this.bipedLeftLegwear.render(scale);
/*  80 */       this.bipedRightLegwear.render(scale);
/*  81 */       this.bipedLeftArmwear.render(scale);
/*  82 */       this.bipedRightArmwear.render(scale);
/*  83 */       this.bipedBodyWear.render(scale);
/*     */     }
/*     */     else {
/*     */       
/*  87 */       if (entityIn.isSneaking())
/*     */       {
/*  89 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/*  92 */       this.bipedLeftLegwear.render(scale);
/*  93 */       this.bipedRightLegwear.render(scale);
/*  94 */       this.bipedLeftArmwear.render(scale);
/*  95 */       this.bipedRightArmwear.render(scale);
/*  96 */       this.bipedBodyWear.render(scale);
/*     */     } 
/*     */     
/*  99 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderDeadmau5Head(float p_178727_1_) {
/* 104 */     copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
/* 105 */     this.bipedDeadmau5Head.rotationPointX = 0.0F;
/* 106 */     this.bipedDeadmau5Head.rotationPointY = 0.0F;
/* 107 */     this.bipedDeadmau5Head.render(p_178727_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderCape(float p_178728_1_) {
/* 112 */     this.bipedCape.render(p_178728_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 117 */     super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
/* 118 */     copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
/* 119 */     copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
/* 120 */     copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
/* 121 */     copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
/* 122 */     copyModelAngles(this.bipedBody, this.bipedBodyWear);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderRightArm() {
/* 127 */     this.bipedRightArm.render(0.0625F);
/* 128 */     this.bipedRightArmwear.render(0.0625F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderLeftArm() {
/* 133 */     this.bipedLeftArm.render(0.0625F);
/* 134 */     this.bipedLeftArmwear.render(0.0625F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvisible(boolean invisible) {
/* 139 */     super.setInvisible(invisible);
/* 140 */     this.bipedLeftArmwear.showModel = invisible;
/* 141 */     this.bipedRightArmwear.showModel = invisible;
/* 142 */     this.bipedLeftLegwear.showModel = invisible;
/* 143 */     this.bipedRightLegwear.showModel = invisible;
/* 144 */     this.bipedBodyWear.showModel = invisible;
/* 145 */     this.bipedCape.showModel = invisible;
/* 146 */     this.bipedDeadmau5Head.showModel = invisible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRenderArm(float scale) {
/* 151 */     if (this.smallArms) {
/*     */       
/* 153 */       this.bipedRightArm.rotationPointX++;
/* 154 */       this.bipedRightArm.postRender(scale);
/* 155 */       this.bipedRightArm.rotationPointX--;
/*     */     }
/*     */     else {
/*     */       
/* 159 */       this.bipedRightArm.postRender(scale);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */