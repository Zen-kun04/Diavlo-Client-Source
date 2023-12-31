/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ 
/*     */ public class ModelIronGolem
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer ironGolemHead;
/*     */   public ModelRenderer ironGolemBody;
/*     */   public ModelRenderer ironGolemRightArm;
/*     */   public ModelRenderer ironGolemLeftArm;
/*     */   public ModelRenderer ironGolemLeftLeg;
/*     */   public ModelRenderer ironGolemRightLeg;
/*     */   
/*     */   public ModelIronGolem() {
/*  18 */     this(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelIronGolem(float p_i1161_1_) {
/*  23 */     this(p_i1161_1_, -7.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelIronGolem(float p_i46362_1_, float p_i46362_2_) {
/*  28 */     int i = 128;
/*  29 */     int j = 128;
/*  30 */     this.ironGolemHead = (new ModelRenderer(this)).setTextureSize(i, j);
/*  31 */     this.ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i46362_2_, -2.0F);
/*  32 */     this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, p_i46362_1_);
/*  33 */     this.ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, p_i46362_1_);
/*  34 */     this.ironGolemBody = (new ModelRenderer(this)).setTextureSize(i, j);
/*  35 */     this.ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i46362_2_, 0.0F);
/*  36 */     this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, p_i46362_1_);
/*  37 */     this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, p_i46362_1_ + 0.5F);
/*  38 */     this.ironGolemRightArm = (new ModelRenderer(this)).setTextureSize(i, j);
/*  39 */     this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
/*  40 */     this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
/*  41 */     this.ironGolemLeftArm = (new ModelRenderer(this)).setTextureSize(i, j);
/*  42 */     this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
/*  43 */     this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
/*  44 */     this.ironGolemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(i, j);
/*  45 */     this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i46362_2_, 0.0F);
/*  46 */     this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
/*  47 */     this.ironGolemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(i, j);
/*  48 */     this.ironGolemRightLeg.mirror = true;
/*  49 */     this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + p_i46362_2_, 0.0F);
/*  50 */     this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  55 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  56 */     this.ironGolemHead.render(scale);
/*  57 */     this.ironGolemBody.render(scale);
/*  58 */     this.ironGolemLeftLeg.render(scale);
/*  59 */     this.ironGolemRightLeg.render(scale);
/*  60 */     this.ironGolemRightArm.render(scale);
/*  61 */     this.ironGolemLeftArm.render(scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/*  66 */     this.ironGolemHead.rotateAngleY = netHeadYaw / 57.295776F;
/*  67 */     this.ironGolemHead.rotateAngleX = headPitch / 57.295776F;
/*  68 */     this.ironGolemLeftLeg.rotateAngleX = -1.5F * func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
/*  69 */     this.ironGolemRightLeg.rotateAngleX = 1.5F * func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
/*  70 */     this.ironGolemLeftLeg.rotateAngleY = 0.0F;
/*  71 */     this.ironGolemRightLeg.rotateAngleY = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/*  76 */     EntityIronGolem entityirongolem = (EntityIronGolem)entitylivingbaseIn;
/*  77 */     int i = entityirongolem.getAttackTimer();
/*     */     
/*  79 */     if (i > 0) {
/*     */       
/*  81 */       this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * func_78172_a(i - partialTickTime, 10.0F);
/*  82 */       this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * func_78172_a(i - partialTickTime, 10.0F);
/*     */     }
/*     */     else {
/*     */       
/*  86 */       int j = entityirongolem.getHoldRoseTick();
/*     */       
/*  88 */       if (j > 0) {
/*     */         
/*  90 */         this.ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * func_78172_a(j, 70.0F);
/*  91 */         this.ironGolemLeftArm.rotateAngleX = 0.0F;
/*     */       }
/*     */       else {
/*     */         
/*  95 */         this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_;
/*  96 */         this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private float func_78172_a(float p_78172_1_, float p_78172_2_) {
/* 103 */     return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / p_78172_2_ * 0.25F;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */