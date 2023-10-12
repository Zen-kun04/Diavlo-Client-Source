/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelQuadruped
/*    */   extends ModelBase {
/*  9 */   public ModelRenderer head = new ModelRenderer(this, 0, 0);
/*    */   public ModelRenderer body;
/*    */   public ModelRenderer leg1;
/*    */   public ModelRenderer leg2;
/*    */   public ModelRenderer leg3;
/*    */   public ModelRenderer leg4;
/* 15 */   protected float childYOffset = 8.0F;
/* 16 */   protected float childZOffset = 4.0F;
/*    */ 
/*    */   
/*    */   public ModelQuadruped(int p_i1154_1_, float p_i1154_2_) {
/* 20 */     this.head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, p_i1154_2_);
/* 21 */     this.head.setRotationPoint(0.0F, (18 - p_i1154_1_), -6.0F);
/* 22 */     this.body = new ModelRenderer(this, 28, 8);
/* 23 */     this.body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, p_i1154_2_);
/* 24 */     this.body.setRotationPoint(0.0F, (17 - p_i1154_1_), 2.0F);
/* 25 */     this.leg1 = new ModelRenderer(this, 0, 16);
/* 26 */     this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 27 */     this.leg1.setRotationPoint(-3.0F, (24 - p_i1154_1_), 7.0F);
/* 28 */     this.leg2 = new ModelRenderer(this, 0, 16);
/* 29 */     this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 30 */     this.leg2.setRotationPoint(3.0F, (24 - p_i1154_1_), 7.0F);
/* 31 */     this.leg3 = new ModelRenderer(this, 0, 16);
/* 32 */     this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 33 */     this.leg3.setRotationPoint(-3.0F, (24 - p_i1154_1_), -5.0F);
/* 34 */     this.leg4 = new ModelRenderer(this, 0, 16);
/* 35 */     this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
/* 36 */     this.leg4.setRotationPoint(3.0F, (24 - p_i1154_1_), -5.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 41 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*    */     
/* 43 */     if (this.isChild) {
/*    */       
/* 45 */       float f = 2.0F;
/* 46 */       GlStateManager.pushMatrix();
/* 47 */       GlStateManager.translate(0.0F, this.childYOffset * scale, this.childZOffset * scale);
/* 48 */       this.head.render(scale);
/* 49 */       GlStateManager.popMatrix();
/* 50 */       GlStateManager.pushMatrix();
/* 51 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 52 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 53 */       this.body.render(scale);
/* 54 */       this.leg1.render(scale);
/* 55 */       this.leg2.render(scale);
/* 56 */       this.leg3.render(scale);
/* 57 */       this.leg4.render(scale);
/* 58 */       GlStateManager.popMatrix();
/*    */     }
/*    */     else {
/*    */       
/* 62 */       this.head.render(scale);
/* 63 */       this.body.render(scale);
/* 64 */       this.leg1.render(scale);
/* 65 */       this.leg2.render(scale);
/* 66 */       this.leg3.render(scale);
/* 67 */       this.leg4.render(scale);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 73 */     float f = 57.295776F;
/* 74 */     this.head.rotateAngleX = headPitch / 57.295776F;
/* 75 */     this.head.rotateAngleY = netHeadYaw / 57.295776F;
/* 76 */     this.body.rotateAngleX = 1.5707964F;
/* 77 */     this.leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
/* 78 */     this.leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
/* 79 */     this.leg3.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
/* 80 */     this.leg4.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelQuadruped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */