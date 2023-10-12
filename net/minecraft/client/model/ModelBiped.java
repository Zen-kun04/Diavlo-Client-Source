/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelBiped
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer bipedHead;
/*     */   public ModelRenderer bipedHeadwear;
/*     */   public ModelRenderer bipedBody;
/*     */   public ModelRenderer bipedRightArm;
/*     */   public ModelRenderer bipedLeftArm;
/*     */   public ModelRenderer bipedRightLeg;
/*     */   public ModelRenderer bipedLeftLeg;
/*     */   public int heldItemLeft;
/*     */   public int heldItemRight;
/*     */   public boolean isSneak;
/*     */   public boolean aimedBow;
/*     */   
/*     */   public ModelBiped() {
/*  23 */     this(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBiped(float modelSize) {
/*  28 */     this(modelSize, 0.0F, 64, 32);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
/*  33 */     this.textureWidth = textureWidthIn;
/*  34 */     this.textureHeight = textureHeightIn;
/*  35 */     this.bipedHead = new ModelRenderer(this, 0, 0);
/*  36 */     this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
/*  37 */     this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
/*  38 */     this.bipedHeadwear = new ModelRenderer(this, 32, 0);
/*  39 */     this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
/*  40 */     this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
/*  41 */     this.bipedBody = new ModelRenderer(this, 16, 16);
/*  42 */     this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
/*  43 */     this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
/*  44 */     this.bipedRightArm = new ModelRenderer(this, 40, 16);
/*  45 */     this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
/*  46 */     this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
/*  47 */     this.bipedLeftArm = new ModelRenderer(this, 40, 16);
/*  48 */     this.bipedLeftArm.mirror = true;
/*  49 */     this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
/*  50 */     this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
/*  51 */     this.bipedRightLeg = new ModelRenderer(this, 0, 16);
/*  52 */     this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
/*  53 */     this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
/*  54 */     this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
/*  55 */     this.bipedLeftLeg.mirror = true;
/*  56 */     this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
/*  57 */     this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  62 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  63 */     GlStateManager.pushMatrix();
/*     */     
/*  65 */     if (this.isChild) {
/*     */       
/*  67 */       float f = 2.0F;
/*  68 */       GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
/*  69 */       GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
/*  70 */       this.bipedHead.render(scale);
/*  71 */       GlStateManager.popMatrix();
/*  72 */       GlStateManager.pushMatrix();
/*  73 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  74 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  75 */       this.bipedBody.render(scale);
/*  76 */       this.bipedRightArm.render(scale);
/*  77 */       this.bipedLeftArm.render(scale);
/*  78 */       this.bipedRightLeg.render(scale);
/*  79 */       this.bipedLeftLeg.render(scale);
/*  80 */       this.bipedHeadwear.render(scale);
/*     */     }
/*     */     else {
/*     */       
/*  84 */       if (entityIn.isSneaking())
/*     */       {
/*  86 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/*  89 */       this.bipedHead.render(scale);
/*  90 */       this.bipedBody.render(scale);
/*  91 */       this.bipedRightArm.render(scale);
/*  92 */       this.bipedLeftArm.render(scale);
/*  93 */       this.bipedRightLeg.render(scale);
/*  94 */       this.bipedLeftLeg.render(scale);
/*  95 */       this.bipedHeadwear.render(scale);
/*     */     } 
/*     */     
/*  98 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 103 */     this.bipedHead.rotateAngleY = netHeadYaw / 57.295776F;
/* 104 */     this.bipedHead.rotateAngleX = headPitch / 57.295776F;
/* 105 */     this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F;
/* 106 */     this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
/* 107 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/* 108 */     this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 109 */     this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
/* 110 */     this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
/* 111 */     this.bipedRightLeg.rotateAngleY = 0.0F;
/* 112 */     this.bipedLeftLeg.rotateAngleY = 0.0F;
/*     */     
/* 114 */     if (this.isRiding) {
/*     */       
/* 116 */       this.bipedRightArm.rotateAngleX += -0.62831855F;
/* 117 */       this.bipedLeftArm.rotateAngleX += -0.62831855F;
/* 118 */       this.bipedRightLeg.rotateAngleX = -1.2566371F;
/* 119 */       this.bipedLeftLeg.rotateAngleX = -1.2566371F;
/* 120 */       this.bipedRightLeg.rotateAngleY = 0.31415927F;
/* 121 */       this.bipedLeftLeg.rotateAngleY = -0.31415927F;
/*     */     } 
/*     */     
/* 124 */     if (this.heldItemLeft != 0)
/*     */     {
/* 126 */       this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.31415927F * this.heldItemLeft;
/*     */     }
/*     */     
/* 129 */     this.bipedRightArm.rotateAngleY = 0.0F;
/* 130 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/*     */     
/* 132 */     switch (this.heldItemRight) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 140 */         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * this.heldItemRight;
/*     */         break;
/*     */       
/*     */       case 3:
/* 144 */         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * this.heldItemRight;
/* 145 */         this.bipedRightArm.rotateAngleY = -0.5235988F;
/*     */         break;
/*     */     } 
/* 148 */     this.bipedLeftArm.rotateAngleY = 0.0F;
/*     */     
/* 150 */     if (this.swingProgress > -9990.0F) {
/*     */       
/* 152 */       float f = this.swingProgress;
/* 153 */       this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f) * 3.1415927F * 2.0F) * 0.2F;
/* 154 */       this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
/* 155 */       this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
/* 156 */       this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
/* 157 */       this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
/* 158 */       this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
/* 159 */       this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
/* 160 */       this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
/* 161 */       f = 1.0F - this.swingProgress;
/* 162 */       f *= f;
/* 163 */       f *= f;
/* 164 */       f = 1.0F - f;
/* 165 */       float f1 = MathHelper.sin(f * 3.1415927F);
/* 166 */       float f2 = MathHelper.sin(this.swingProgress * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
/* 167 */       this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX - f1 * 1.2D + f2);
/* 168 */       this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
/* 169 */       this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927F) * -0.4F;
/*     */     } 
/*     */     
/* 172 */     if (this.isSneak) {
/*     */       
/* 174 */       this.bipedBody.rotateAngleX = 0.5F;
/* 175 */       this.bipedRightArm.rotateAngleX += 0.4F;
/* 176 */       this.bipedLeftArm.rotateAngleX += 0.4F;
/* 177 */       this.bipedRightLeg.rotationPointZ = 4.0F;
/* 178 */       this.bipedLeftLeg.rotationPointZ = 4.0F;
/* 179 */       this.bipedRightLeg.rotationPointY = 9.0F;
/* 180 */       this.bipedLeftLeg.rotationPointY = 9.0F;
/* 181 */       this.bipedHead.rotationPointY = 1.0F;
/*     */     }
/*     */     else {
/*     */       
/* 185 */       this.bipedBody.rotateAngleX = 0.0F;
/* 186 */       this.bipedRightLeg.rotationPointZ = 0.1F;
/* 187 */       this.bipedLeftLeg.rotationPointZ = 0.1F;
/* 188 */       this.bipedRightLeg.rotationPointY = 12.0F;
/* 189 */       this.bipedLeftLeg.rotationPointY = 12.0F;
/* 190 */       this.bipedHead.rotationPointY = 0.0F;
/*     */     } 
/*     */     
/* 193 */     this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
/* 194 */     this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
/* 195 */     this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
/* 196 */     this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
/*     */     
/* 198 */     if (this.aimedBow) {
/*     */       
/* 200 */       float f3 = 0.0F;
/* 201 */       float f4 = 0.0F;
/* 202 */       this.bipedRightArm.rotateAngleZ = 0.0F;
/* 203 */       this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 204 */       this.bipedRightArm.rotateAngleY = -(0.1F - f3 * 0.6F) + this.bipedHead.rotateAngleY;
/* 205 */       this.bipedLeftArm.rotateAngleY = 0.1F - f3 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
/* 206 */       this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
/* 207 */       this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
/* 208 */       this.bipedRightArm.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
/* 209 */       this.bipedLeftArm.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
/* 210 */       this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
/* 211 */       this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
/* 212 */       this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
/* 213 */       this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
/*     */     } 
/*     */     
/* 216 */     copyModelAngles(this.bipedHead, this.bipedHeadwear);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setModelAttributes(ModelBase model) {
/* 221 */     super.setModelAttributes(model);
/*     */     
/* 223 */     if (model instanceof ModelBiped) {
/*     */       
/* 225 */       ModelBiped modelbiped = (ModelBiped)model;
/* 226 */       this.heldItemLeft = modelbiped.heldItemLeft;
/* 227 */       this.heldItemRight = modelbiped.heldItemRight;
/* 228 */       this.isSneak = modelbiped.isSneak;
/* 229 */       this.aimedBow = modelbiped.aimedBow;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvisible(boolean invisible) {
/* 235 */     this.bipedHead.showModel = invisible;
/* 236 */     this.bipedHeadwear.showModel = invisible;
/* 237 */     this.bipedBody.showModel = invisible;
/* 238 */     this.bipedRightArm.showModel = invisible;
/* 239 */     this.bipedLeftArm.showModel = invisible;
/* 240 */     this.bipedRightLeg.showModel = invisible;
/* 241 */     this.bipedLeftLeg.showModel = invisible;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRenderArm(float scale) {
/* 246 */     this.bipedRightArm.postRender(scale);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelBiped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */