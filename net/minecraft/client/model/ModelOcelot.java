/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelOcelot
/*     */   extends ModelBase {
/*     */   ModelRenderer ocelotBackLeftLeg;
/*     */   ModelRenderer ocelotBackRightLeg;
/*     */   ModelRenderer ocelotFrontLeftLeg;
/*     */   ModelRenderer ocelotFrontRightLeg;
/*     */   ModelRenderer ocelotTail;
/*     */   ModelRenderer ocelotTail2;
/*     */   ModelRenderer ocelotHead;
/*     */   ModelRenderer ocelotBody;
/*  19 */   int field_78163_i = 1;
/*     */ 
/*     */   
/*     */   public ModelOcelot() {
/*  23 */     setTextureOffset("head.main", 0, 0);
/*  24 */     setTextureOffset("head.nose", 0, 24);
/*  25 */     setTextureOffset("head.ear1", 0, 10);
/*  26 */     setTextureOffset("head.ear2", 6, 10);
/*  27 */     this.ocelotHead = new ModelRenderer(this, "head");
/*  28 */     this.ocelotHead.addBox("main", -2.5F, -2.0F, -3.0F, 5, 4, 5);
/*  29 */     this.ocelotHead.addBox("nose", -1.5F, 0.0F, -4.0F, 3, 2, 2);
/*  30 */     this.ocelotHead.addBox("ear1", -2.0F, -3.0F, 0.0F, 1, 1, 2);
/*  31 */     this.ocelotHead.addBox("ear2", 1.0F, -3.0F, 0.0F, 1, 1, 2);
/*  32 */     this.ocelotHead.setRotationPoint(0.0F, 15.0F, -9.0F);
/*  33 */     this.ocelotBody = new ModelRenderer(this, 20, 0);
/*  34 */     this.ocelotBody.addBox(-2.0F, 3.0F, -8.0F, 4, 16, 6, 0.0F);
/*  35 */     this.ocelotBody.setRotationPoint(0.0F, 12.0F, -10.0F);
/*  36 */     this.ocelotTail = new ModelRenderer(this, 0, 15);
/*  37 */     this.ocelotTail.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
/*  38 */     this.ocelotTail.rotateAngleX = 0.9F;
/*  39 */     this.ocelotTail.setRotationPoint(0.0F, 15.0F, 8.0F);
/*  40 */     this.ocelotTail2 = new ModelRenderer(this, 4, 15);
/*  41 */     this.ocelotTail2.addBox(-0.5F, 0.0F, 0.0F, 1, 8, 1);
/*  42 */     this.ocelotTail2.setRotationPoint(0.0F, 20.0F, 14.0F);
/*  43 */     this.ocelotBackLeftLeg = new ModelRenderer(this, 8, 13);
/*  44 */     this.ocelotBackLeftLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
/*  45 */     this.ocelotBackLeftLeg.setRotationPoint(1.1F, 18.0F, 5.0F);
/*  46 */     this.ocelotBackRightLeg = new ModelRenderer(this, 8, 13);
/*  47 */     this.ocelotBackRightLeg.addBox(-1.0F, 0.0F, 1.0F, 2, 6, 2);
/*  48 */     this.ocelotBackRightLeg.setRotationPoint(-1.1F, 18.0F, 5.0F);
/*  49 */     this.ocelotFrontLeftLeg = new ModelRenderer(this, 40, 0);
/*  50 */     this.ocelotFrontLeftLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
/*  51 */     this.ocelotFrontLeftLeg.setRotationPoint(1.2F, 13.8F, -5.0F);
/*  52 */     this.ocelotFrontRightLeg = new ModelRenderer(this, 40, 0);
/*  53 */     this.ocelotFrontRightLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 10, 2);
/*  54 */     this.ocelotFrontRightLeg.setRotationPoint(-1.2F, 13.8F, -5.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  59 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*     */     
/*  61 */     if (this.isChild) {
/*     */       
/*  63 */       float f = 2.0F;
/*  64 */       GlStateManager.pushMatrix();
/*  65 */       GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
/*  66 */       GlStateManager.translate(0.0F, 10.0F * scale, 4.0F * scale);
/*  67 */       this.ocelotHead.render(scale);
/*  68 */       GlStateManager.popMatrix();
/*  69 */       GlStateManager.pushMatrix();
/*  70 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  71 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  72 */       this.ocelotBody.render(scale);
/*  73 */       this.ocelotBackLeftLeg.render(scale);
/*  74 */       this.ocelotBackRightLeg.render(scale);
/*  75 */       this.ocelotFrontLeftLeg.render(scale);
/*  76 */       this.ocelotFrontRightLeg.render(scale);
/*  77 */       this.ocelotTail.render(scale);
/*  78 */       this.ocelotTail2.render(scale);
/*  79 */       GlStateManager.popMatrix();
/*     */     }
/*     */     else {
/*     */       
/*  83 */       this.ocelotHead.render(scale);
/*  84 */       this.ocelotBody.render(scale);
/*  85 */       this.ocelotTail.render(scale);
/*  86 */       this.ocelotTail2.render(scale);
/*  87 */       this.ocelotBackLeftLeg.render(scale);
/*  88 */       this.ocelotBackRightLeg.render(scale);
/*  89 */       this.ocelotFrontLeftLeg.render(scale);
/*  90 */       this.ocelotFrontRightLeg.render(scale);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/*  96 */     this.ocelotHead.rotateAngleX = headPitch / 57.295776F;
/*  97 */     this.ocelotHead.rotateAngleY = netHeadYaw / 57.295776F;
/*     */     
/*  99 */     if (this.field_78163_i != 3) {
/*     */       
/* 101 */       this.ocelotBody.rotateAngleX = 1.5707964F;
/*     */       
/* 103 */       if (this.field_78163_i == 2) {
/*     */         
/* 105 */         this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
/* 106 */         this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 0.3F) * 1.0F * limbSwingAmount;
/* 107 */         this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F + 0.3F) * 1.0F * limbSwingAmount;
/* 108 */         this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.0F * limbSwingAmount;
/* 109 */         this.ocelotTail2.rotateAngleX = 1.7278761F + 0.31415927F * MathHelper.cos(limbSwing) * limbSwingAmount;
/*     */       }
/*     */       else {
/*     */         
/* 113 */         this.ocelotBackLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
/* 114 */         this.ocelotBackRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.0F * limbSwingAmount;
/* 115 */         this.ocelotFrontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.0F * limbSwingAmount;
/* 116 */         this.ocelotFrontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
/*     */         
/* 118 */         if (this.field_78163_i == 1) {
/*     */           
/* 120 */           this.ocelotTail2.rotateAngleX = 1.7278761F + 0.7853982F * MathHelper.cos(limbSwing) * limbSwingAmount;
/*     */         }
/*     */         else {
/*     */           
/* 124 */           this.ocelotTail2.rotateAngleX = 1.7278761F + 0.47123894F * MathHelper.cos(limbSwing) * limbSwingAmount;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/* 132 */     EntityOcelot entityocelot = (EntityOcelot)entitylivingbaseIn;
/* 133 */     this.ocelotBody.rotationPointY = 12.0F;
/* 134 */     this.ocelotBody.rotationPointZ = -10.0F;
/* 135 */     this.ocelotHead.rotationPointY = 15.0F;
/* 136 */     this.ocelotHead.rotationPointZ = -9.0F;
/* 137 */     this.ocelotTail.rotationPointY = 15.0F;
/* 138 */     this.ocelotTail.rotationPointZ = 8.0F;
/* 139 */     this.ocelotTail2.rotationPointY = 20.0F;
/* 140 */     this.ocelotTail2.rotationPointZ = 14.0F;
/* 141 */     this.ocelotFrontRightLeg.rotationPointY = 13.8F;
/* 142 */     this.ocelotFrontRightLeg.rotationPointZ = -5.0F;
/* 143 */     this.ocelotBackRightLeg.rotationPointY = 18.0F;
/* 144 */     this.ocelotBackRightLeg.rotationPointZ = 5.0F;
/* 145 */     this.ocelotTail.rotateAngleX = 0.9F;
/*     */     
/* 147 */     if (entityocelot.isSneaking()) {
/*     */       
/* 149 */       this.ocelotBody.rotationPointY++;
/* 150 */       this.ocelotHead.rotationPointY += 2.0F;
/* 151 */       this.ocelotTail.rotationPointY++;
/* 152 */       this.ocelotTail2.rotationPointY += -4.0F;
/* 153 */       this.ocelotTail2.rotationPointZ += 2.0F;
/* 154 */       this.ocelotTail.rotateAngleX = 1.5707964F;
/* 155 */       this.ocelotTail2.rotateAngleX = 1.5707964F;
/* 156 */       this.field_78163_i = 0;
/*     */     }
/* 158 */     else if (entityocelot.isSprinting()) {
/*     */       
/* 160 */       this.ocelotTail2.rotationPointY = this.ocelotTail.rotationPointY;
/* 161 */       this.ocelotTail2.rotationPointZ += 2.0F;
/* 162 */       this.ocelotTail.rotateAngleX = 1.5707964F;
/* 163 */       this.ocelotTail2.rotateAngleX = 1.5707964F;
/* 164 */       this.field_78163_i = 2;
/*     */     }
/* 166 */     else if (entityocelot.isSitting()) {
/*     */       
/* 168 */       this.ocelotBody.rotateAngleX = 0.7853982F;
/* 169 */       this.ocelotBody.rotationPointY += -4.0F;
/* 170 */       this.ocelotBody.rotationPointZ += 5.0F;
/* 171 */       this.ocelotHead.rotationPointY += -3.3F;
/* 172 */       this.ocelotHead.rotationPointZ++;
/* 173 */       this.ocelotTail.rotationPointY += 8.0F;
/* 174 */       this.ocelotTail.rotationPointZ += -2.0F;
/* 175 */       this.ocelotTail2.rotationPointY += 2.0F;
/* 176 */       this.ocelotTail2.rotationPointZ += -0.8F;
/* 177 */       this.ocelotTail.rotateAngleX = 1.7278761F;
/* 178 */       this.ocelotTail2.rotateAngleX = 2.670354F;
/* 179 */       this.ocelotFrontRightLeg.rotateAngleX = -0.15707964F;
/* 180 */       this.ocelotFrontRightLeg.rotationPointY = 15.8F;
/* 181 */       this.ocelotFrontRightLeg.rotationPointZ = -7.0F;
/* 182 */       this.ocelotBackRightLeg.rotateAngleX = -1.5707964F;
/* 183 */       this.ocelotBackRightLeg.rotationPointY = 21.0F;
/* 184 */       this.ocelotBackRightLeg.rotationPointZ = 1.0F;
/* 185 */       this.field_78163_i = 3;
/*     */     }
/*     */     else {
/*     */       
/* 189 */       this.field_78163_i = 1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelOcelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */