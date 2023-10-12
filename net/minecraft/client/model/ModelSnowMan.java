/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelSnowMan
/*    */   extends ModelBase
/*    */ {
/*    */   public ModelRenderer body;
/*    */   public ModelRenderer bottomBody;
/*    */   public ModelRenderer head;
/*    */   public ModelRenderer rightHand;
/*    */   public ModelRenderer leftHand;
/*    */   
/*    */   public ModelSnowMan() {
/* 16 */     float f = 4.0F;
/* 17 */     float f1 = 0.0F;
/* 18 */     this.head = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
/* 19 */     this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, f1 - 0.5F);
/* 20 */     this.head.setRotationPoint(0.0F, 0.0F + f, 0.0F);
/* 21 */     this.rightHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
/* 22 */     this.rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, f1 - 0.5F);
/* 23 */     this.rightHand.setRotationPoint(0.0F, 0.0F + f + 9.0F - 7.0F, 0.0F);
/* 24 */     this.leftHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
/* 25 */     this.leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, f1 - 0.5F);
/* 26 */     this.leftHand.setRotationPoint(0.0F, 0.0F + f + 9.0F - 7.0F, 0.0F);
/* 27 */     this.body = (new ModelRenderer(this, 0, 16)).setTextureSize(64, 64);
/* 28 */     this.body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, f1 - 0.5F);
/* 29 */     this.body.setRotationPoint(0.0F, 0.0F + f + 9.0F, 0.0F);
/* 30 */     this.bottomBody = (new ModelRenderer(this, 0, 36)).setTextureSize(64, 64);
/* 31 */     this.bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, f1 - 0.5F);
/* 32 */     this.bottomBody.setRotationPoint(0.0F, 0.0F + f + 20.0F, 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 37 */     super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
/* 38 */     this.head.rotateAngleY = netHeadYaw / 57.295776F;
/* 39 */     this.head.rotateAngleX = headPitch / 57.295776F;
/* 40 */     this.body.rotateAngleY = netHeadYaw / 57.295776F * 0.25F;
/* 41 */     float f = MathHelper.sin(this.body.rotateAngleY);
/* 42 */     float f1 = MathHelper.cos(this.body.rotateAngleY);
/* 43 */     this.rightHand.rotateAngleZ = 1.0F;
/* 44 */     this.leftHand.rotateAngleZ = -1.0F;
/* 45 */     this.rightHand.rotateAngleY = 0.0F + this.body.rotateAngleY;
/* 46 */     this.leftHand.rotateAngleY = 3.1415927F + this.body.rotateAngleY;
/* 47 */     this.rightHand.rotationPointX = f1 * 5.0F;
/* 48 */     this.rightHand.rotationPointZ = -f * 5.0F;
/* 49 */     this.leftHand.rotationPointX = -f1 * 5.0F;
/* 50 */     this.leftHand.rotationPointZ = f * 5.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 55 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 56 */     this.body.render(scale);
/* 57 */     this.bottomBody.render(scale);
/* 58 */     this.head.render(scale);
/* 59 */     this.rightHand.render(scale);
/* 60 */     this.leftHand.render(scale);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelSnowMan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */