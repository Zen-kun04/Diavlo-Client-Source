/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityBat;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelBat
/*    */   extends ModelBase
/*    */ {
/*    */   private ModelRenderer batHead;
/*    */   private ModelRenderer batBody;
/*    */   private ModelRenderer batRightWing;
/*    */   private ModelRenderer batLeftWing;
/*    */   private ModelRenderer batOuterRightWing;
/*    */   private ModelRenderer batOuterLeftWing;
/*    */   
/*    */   public ModelBat() {
/* 18 */     this.textureWidth = 64;
/* 19 */     this.textureHeight = 64;
/* 20 */     this.batHead = new ModelRenderer(this, 0, 0);
/* 21 */     this.batHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
/* 22 */     ModelRenderer modelrenderer = new ModelRenderer(this, 24, 0);
/* 23 */     modelrenderer.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
/* 24 */     this.batHead.addChild(modelrenderer);
/* 25 */     ModelRenderer modelrenderer1 = new ModelRenderer(this, 24, 0);
/* 26 */     modelrenderer1.mirror = true;
/* 27 */     modelrenderer1.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
/* 28 */     this.batHead.addChild(modelrenderer1);
/* 29 */     this.batBody = new ModelRenderer(this, 0, 16);
/* 30 */     this.batBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
/* 31 */     this.batBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
/* 32 */     this.batRightWing = new ModelRenderer(this, 42, 0);
/* 33 */     this.batRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
/* 34 */     this.batOuterRightWing = new ModelRenderer(this, 24, 16);
/* 35 */     this.batOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
/* 36 */     this.batOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
/* 37 */     this.batLeftWing = new ModelRenderer(this, 42, 0);
/* 38 */     this.batLeftWing.mirror = true;
/* 39 */     this.batLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
/* 40 */     this.batOuterLeftWing = new ModelRenderer(this, 24, 16);
/* 41 */     this.batOuterLeftWing.mirror = true;
/* 42 */     this.batOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
/* 43 */     this.batOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
/* 44 */     this.batBody.addChild(this.batRightWing);
/* 45 */     this.batBody.addChild(this.batLeftWing);
/* 46 */     this.batRightWing.addChild(this.batOuterRightWing);
/* 47 */     this.batLeftWing.addChild(this.batOuterLeftWing);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 52 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 53 */     this.batHead.render(scale);
/* 54 */     this.batBody.render(scale);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 59 */     if (((EntityBat)entityIn).getIsBatHanging()) {
/*    */       
/* 61 */       float f = 57.295776F;
/* 62 */       this.batHead.rotateAngleX = headPitch / 57.295776F;
/* 63 */       this.batHead.rotateAngleY = 3.1415927F - netHeadYaw / 57.295776F;
/* 64 */       this.batHead.rotateAngleZ = 3.1415927F;
/* 65 */       this.batHead.setRotationPoint(0.0F, -2.0F, 0.0F);
/* 66 */       this.batRightWing.setRotationPoint(-3.0F, 0.0F, 3.0F);
/* 67 */       this.batLeftWing.setRotationPoint(3.0F, 0.0F, 3.0F);
/* 68 */       this.batBody.rotateAngleX = 3.1415927F;
/* 69 */       this.batRightWing.rotateAngleX = -0.15707964F;
/* 70 */       this.batRightWing.rotateAngleY = -1.2566371F;
/* 71 */       this.batOuterRightWing.rotateAngleY = -1.7278761F;
/* 72 */       this.batLeftWing.rotateAngleX = this.batRightWing.rotateAngleX;
/* 73 */       this.batLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY;
/* 74 */       this.batOuterLeftWing.rotateAngleY = -this.batOuterRightWing.rotateAngleY;
/*    */     }
/*    */     else {
/*    */       
/* 78 */       float f1 = 57.295776F;
/* 79 */       this.batHead.rotateAngleX = headPitch / 57.295776F;
/* 80 */       this.batHead.rotateAngleY = netHeadYaw / 57.295776F;
/* 81 */       this.batHead.rotateAngleZ = 0.0F;
/* 82 */       this.batHead.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 83 */       this.batRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 84 */       this.batLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
/* 85 */       this.batBody.rotateAngleX = 0.7853982F + MathHelper.cos(ageInTicks * 0.1F) * 0.15F;
/* 86 */       this.batBody.rotateAngleY = 0.0F;
/* 87 */       this.batRightWing.rotateAngleY = MathHelper.cos(ageInTicks * 1.3F) * 3.1415927F * 0.25F;
/* 88 */       this.batLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY;
/* 89 */       this.batRightWing.rotateAngleY *= 0.5F;
/* 90 */       this.batOuterLeftWing.rotateAngleY = -this.batRightWing.rotateAngleY * 0.5F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelBat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */