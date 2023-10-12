/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ 
/*     */ public class ModelArmorStand
/*     */   extends ModelArmorStandArmor
/*     */ {
/*     */   public ModelRenderer standRightSide;
/*     */   public ModelRenderer standLeftSide;
/*     */   public ModelRenderer standWaist;
/*     */   public ModelRenderer standBase;
/*     */   
/*     */   public ModelArmorStand() {
/*  16 */     this(0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelArmorStand(float p_i46306_1_) {
/*  21 */     super(p_i46306_1_, 64, 64);
/*  22 */     this.bipedHead = new ModelRenderer(this, 0, 0);
/*  23 */     this.bipedHead.addBox(-1.0F, -7.0F, -1.0F, 2, 7, 2, p_i46306_1_);
/*  24 */     this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  25 */     this.bipedBody = new ModelRenderer(this, 0, 26);
/*  26 */     this.bipedBody.addBox(-6.0F, 0.0F, -1.5F, 12, 3, 3, p_i46306_1_);
/*  27 */     this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  28 */     this.bipedRightArm = new ModelRenderer(this, 24, 0);
/*  29 */     this.bipedRightArm.addBox(-2.0F, -2.0F, -1.0F, 2, 12, 2, p_i46306_1_);
/*  30 */     this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
/*  31 */     this.bipedLeftArm = new ModelRenderer(this, 32, 16);
/*  32 */     this.bipedLeftArm.mirror = true;
/*  33 */     this.bipedLeftArm.addBox(0.0F, -2.0F, -1.0F, 2, 12, 2, p_i46306_1_);
/*  34 */     this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
/*  35 */     this.bipedRightLeg = new ModelRenderer(this, 8, 0);
/*  36 */     this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, p_i46306_1_);
/*  37 */     this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
/*  38 */     this.bipedLeftLeg = new ModelRenderer(this, 40, 16);
/*  39 */     this.bipedLeftLeg.mirror = true;
/*  40 */     this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 11, 2, p_i46306_1_);
/*  41 */     this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
/*  42 */     this.standRightSide = new ModelRenderer(this, 16, 0);
/*  43 */     this.standRightSide.addBox(-3.0F, 3.0F, -1.0F, 2, 7, 2, p_i46306_1_);
/*  44 */     this.standRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  45 */     this.standRightSide.showModel = true;
/*  46 */     this.standLeftSide = new ModelRenderer(this, 48, 16);
/*  47 */     this.standLeftSide.addBox(1.0F, 3.0F, -1.0F, 2, 7, 2, p_i46306_1_);
/*  48 */     this.standLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  49 */     this.standWaist = new ModelRenderer(this, 0, 48);
/*  50 */     this.standWaist.addBox(-4.0F, 10.0F, -1.0F, 8, 2, 2, p_i46306_1_);
/*  51 */     this.standWaist.setRotationPoint(0.0F, 0.0F, 0.0F);
/*  52 */     this.standBase = new ModelRenderer(this, 0, 32);
/*  53 */     this.standBase.addBox(-6.0F, 11.0F, -6.0F, 12, 1, 12, p_i46306_1_);
/*  54 */     this.standBase.setRotationPoint(0.0F, 12.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/*  59 */     super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
/*     */     
/*  61 */     if (entityIn instanceof EntityArmorStand) {
/*     */       
/*  63 */       EntityArmorStand entityarmorstand = (EntityArmorStand)entityIn;
/*  64 */       this.bipedLeftArm.showModel = entityarmorstand.getShowArms();
/*  65 */       this.bipedRightArm.showModel = entityarmorstand.getShowArms();
/*  66 */       this.standBase.showModel = !entityarmorstand.hasNoBasePlate();
/*  67 */       this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
/*  68 */       this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
/*  69 */       this.standRightSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
/*  70 */       this.standRightSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
/*  71 */       this.standRightSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
/*  72 */       this.standLeftSide.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
/*  73 */       this.standLeftSide.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
/*  74 */       this.standLeftSide.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
/*  75 */       this.standWaist.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
/*  76 */       this.standWaist.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
/*  77 */       this.standWaist.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
/*  78 */       float f = (entityarmorstand.getLeftLegRotation().getX() + entityarmorstand.getRightLegRotation().getX()) / 2.0F;
/*  79 */       float f1 = (entityarmorstand.getLeftLegRotation().getY() + entityarmorstand.getRightLegRotation().getY()) / 2.0F;
/*  80 */       float f2 = (entityarmorstand.getLeftLegRotation().getZ() + entityarmorstand.getRightLegRotation().getZ()) / 2.0F;
/*  81 */       this.standBase.rotateAngleX = 0.0F;
/*  82 */       this.standBase.rotateAngleY = 0.017453292F * -entityIn.rotationYaw;
/*  83 */       this.standBase.rotateAngleZ = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/*  89 */     super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
/*  90 */     GlStateManager.pushMatrix();
/*     */     
/*  92 */     if (this.isChild) {
/*     */       
/*  94 */       float f = 2.0F;
/*  95 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/*  96 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/*  97 */       this.standRightSide.render(scale);
/*  98 */       this.standLeftSide.render(scale);
/*  99 */       this.standWaist.render(scale);
/* 100 */       this.standBase.render(scale);
/*     */     }
/*     */     else {
/*     */       
/* 104 */       if (entityIn.isSneaking())
/*     */       {
/* 106 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/* 109 */       this.standRightSide.render(scale);
/* 110 */       this.standLeftSide.render(scale);
/* 111 */       this.standWaist.render(scale);
/* 112 */       this.standBase.render(scale);
/*     */     } 
/*     */     
/* 115 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public void postRenderArm(float scale) {
/* 120 */     boolean flag = this.bipedRightArm.showModel;
/* 121 */     this.bipedRightArm.showModel = true;
/* 122 */     super.postRenderArm(scale);
/* 123 */     this.bipedRightArm.showModel = flag;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */