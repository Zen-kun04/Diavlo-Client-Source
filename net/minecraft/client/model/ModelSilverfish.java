/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelSilverfish
/*    */   extends ModelBase {
/*  8 */   private ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];
/*    */   private ModelRenderer[] silverfishWings;
/* 10 */   private float[] field_78170_c = new float[7];
/* 11 */   private static final int[][] silverfishBoxLength = new int[][] { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 }, { 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
/* 12 */   private static final int[][] silverfishTexturePositions = new int[][] { { 0, 0 }, { 0, 4 }, { 0, 9 }, { 0, 16 }, { 0, 22 }, { 11, 0 }, { 13, 4 } };
/*    */ 
/*    */   
/*    */   public ModelSilverfish() {
/* 16 */     float f = -3.5F;
/*    */     
/* 18 */     for (int i = 0; i < this.silverfishBodyParts.length; i++) {
/*    */       
/* 20 */       this.silverfishBodyParts[i] = new ModelRenderer(this, silverfishTexturePositions[i][0], silverfishTexturePositions[i][1]);
/* 21 */       this.silverfishBodyParts[i].addBox(silverfishBoxLength[i][0] * -0.5F, 0.0F, silverfishBoxLength[i][2] * -0.5F, silverfishBoxLength[i][0], silverfishBoxLength[i][1], silverfishBoxLength[i][2]);
/* 22 */       this.silverfishBodyParts[i].setRotationPoint(0.0F, (24 - silverfishBoxLength[i][1]), f);
/* 23 */       this.field_78170_c[i] = f;
/*    */       
/* 25 */       if (i < this.silverfishBodyParts.length - 1)
/*    */       {
/* 27 */         f += (silverfishBoxLength[i][2] + silverfishBoxLength[i + 1][2]) * 0.5F;
/*    */       }
/*    */     } 
/*    */     
/* 31 */     this.silverfishWings = new ModelRenderer[3];
/* 32 */     this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
/* 33 */     this.silverfishWings[0].addBox(-5.0F, 0.0F, silverfishBoxLength[2][2] * -0.5F, 10, 8, silverfishBoxLength[2][2]);
/* 34 */     this.silverfishWings[0].setRotationPoint(0.0F, 16.0F, this.field_78170_c[2]);
/* 35 */     this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
/* 36 */     this.silverfishWings[1].addBox(-3.0F, 0.0F, silverfishBoxLength[4][2] * -0.5F, 6, 4, silverfishBoxLength[4][2]);
/* 37 */     this.silverfishWings[1].setRotationPoint(0.0F, 20.0F, this.field_78170_c[4]);
/* 38 */     this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
/* 39 */     this.silverfishWings[2].addBox(-3.0F, 0.0F, silverfishBoxLength[4][2] * -0.5F, 6, 5, silverfishBoxLength[1][2]);
/* 40 */     this.silverfishWings[2].setRotationPoint(0.0F, 19.0F, this.field_78170_c[1]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 45 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*    */     
/* 47 */     for (int i = 0; i < this.silverfishBodyParts.length; i++)
/*    */     {
/* 49 */       this.silverfishBodyParts[i].render(scale);
/*    */     }
/*    */     
/* 52 */     for (int j = 0; j < this.silverfishWings.length; j++)
/*    */     {
/* 54 */       this.silverfishWings[j].render(scale);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 60 */     for (int i = 0; i < this.silverfishBodyParts.length; i++) {
/*    */       
/* 62 */       (this.silverfishBodyParts[i]).rotateAngleY = MathHelper.cos(ageInTicks * 0.9F + i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (1 + Math.abs(i - 2));
/* 63 */       (this.silverfishBodyParts[i]).rotationPointX = MathHelper.sin(ageInTicks * 0.9F + i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * Math.abs(i - 2);
/*    */     } 
/*    */     
/* 66 */     (this.silverfishWings[0]).rotateAngleY = (this.silverfishBodyParts[2]).rotateAngleY;
/* 67 */     (this.silverfishWings[1]).rotateAngleY = (this.silverfishBodyParts[4]).rotateAngleY;
/* 68 */     (this.silverfishWings[1]).rotationPointX = (this.silverfishBodyParts[4]).rotationPointX;
/* 69 */     (this.silverfishWings[2]).rotateAngleY = (this.silverfishBodyParts[1]).rotateAngleY;
/* 70 */     (this.silverfishWings[2]).rotationPointX = (this.silverfishBodyParts[1]).rotationPointX;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelSilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */