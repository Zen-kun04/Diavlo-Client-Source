/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelChicken
/*    */   extends ModelBase
/*    */ {
/*    */   public ModelRenderer head;
/*    */   public ModelRenderer body;
/*    */   public ModelRenderer rightLeg;
/*    */   public ModelRenderer leftLeg;
/*    */   public ModelRenderer rightWing;
/*    */   public ModelRenderer leftWing;
/*    */   public ModelRenderer bill;
/*    */   public ModelRenderer chin;
/*    */   
/*    */   public ModelChicken() {
/* 20 */     int i = 16;
/* 21 */     this.head = new ModelRenderer(this, 0, 0);
/* 22 */     this.head.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
/* 23 */     this.head.setRotationPoint(0.0F, (-1 + i), -4.0F);
/* 24 */     this.bill = new ModelRenderer(this, 14, 0);
/* 25 */     this.bill.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
/* 26 */     this.bill.setRotationPoint(0.0F, (-1 + i), -4.0F);
/* 27 */     this.chin = new ModelRenderer(this, 14, 4);
/* 28 */     this.chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
/* 29 */     this.chin.setRotationPoint(0.0F, (-1 + i), -4.0F);
/* 30 */     this.body = new ModelRenderer(this, 0, 9);
/* 31 */     this.body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
/* 32 */     this.body.setRotationPoint(0.0F, i, 0.0F);
/* 33 */     this.rightLeg = new ModelRenderer(this, 26, 0);
/* 34 */     this.rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
/* 35 */     this.rightLeg.setRotationPoint(-2.0F, (3 + i), 1.0F);
/* 36 */     this.leftLeg = new ModelRenderer(this, 26, 0);
/* 37 */     this.leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
/* 38 */     this.leftLeg.setRotationPoint(1.0F, (3 + i), 1.0F);
/* 39 */     this.rightWing = new ModelRenderer(this, 24, 13);
/* 40 */     this.rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6);
/* 41 */     this.rightWing.setRotationPoint(-4.0F, (-3 + i), 0.0F);
/* 42 */     this.leftWing = new ModelRenderer(this, 24, 13);
/* 43 */     this.leftWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6);
/* 44 */     this.leftWing.setRotationPoint(4.0F, (-3 + i), 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 49 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*    */     
/* 51 */     if (this.isChild) {
/*    */       
/* 53 */       float f = 2.0F;
/* 54 */       GlStateManager.pushMatrix();
/* 55 */       GlStateManager.translate(0.0F, 5.0F * scale, 2.0F * scale);
/* 56 */       this.head.render(scale);
/* 57 */       this.bill.render(scale);
/* 58 */       this.chin.render(scale);
/* 59 */       GlStateManager.popMatrix();
/* 60 */       GlStateManager.pushMatrix();
/* 61 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 62 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 63 */       this.body.render(scale);
/* 64 */       this.rightLeg.render(scale);
/* 65 */       this.leftLeg.render(scale);
/* 66 */       this.rightWing.render(scale);
/* 67 */       this.leftWing.render(scale);
/* 68 */       GlStateManager.popMatrix();
/*    */     }
/*    */     else {
/*    */       
/* 72 */       this.head.render(scale);
/* 73 */       this.bill.render(scale);
/* 74 */       this.chin.render(scale);
/* 75 */       this.body.render(scale);
/* 76 */       this.rightLeg.render(scale);
/* 77 */       this.leftLeg.render(scale);
/* 78 */       this.rightWing.render(scale);
/* 79 */       this.leftWing.render(scale);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 85 */     this.head.rotateAngleX = headPitch / 57.295776F;
/* 86 */     this.head.rotateAngleY = netHeadYaw / 57.295776F;
/* 87 */     this.bill.rotateAngleX = this.head.rotateAngleX;
/* 88 */     this.bill.rotateAngleY = this.head.rotateAngleY;
/* 89 */     this.chin.rotateAngleX = this.head.rotateAngleX;
/* 90 */     this.chin.rotateAngleY = this.head.rotateAngleY;
/* 91 */     this.body.rotateAngleX = 1.5707964F;
/* 92 */     this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
/* 93 */     this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
/* 94 */     this.rightWing.rotateAngleZ = ageInTicks;
/* 95 */     this.leftWing.rotateAngleZ = -ageInTicks;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelChicken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */