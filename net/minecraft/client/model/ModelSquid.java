/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ModelSquid
/*    */   extends ModelBase {
/*    */   ModelRenderer squidBody;
/*  8 */   ModelRenderer[] squidTentacles = new ModelRenderer[8];
/*    */ 
/*    */   
/*    */   public ModelSquid() {
/* 12 */     int i = -16;
/* 13 */     this.squidBody = new ModelRenderer(this, 0, 0);
/* 14 */     this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
/* 15 */     this.squidBody.rotationPointY += (24 + i);
/*    */     
/* 17 */     for (int j = 0; j < this.squidTentacles.length; j++) {
/*    */       
/* 19 */       this.squidTentacles[j] = new ModelRenderer(this, 48, 0);
/* 20 */       double d0 = j * Math.PI * 2.0D / this.squidTentacles.length;
/* 21 */       float f = (float)Math.cos(d0) * 5.0F;
/* 22 */       float f1 = (float)Math.sin(d0) * 5.0F;
/* 23 */       this.squidTentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
/* 24 */       (this.squidTentacles[j]).rotationPointX = f;
/* 25 */       (this.squidTentacles[j]).rotationPointZ = f1;
/* 26 */       (this.squidTentacles[j]).rotationPointY = (31 + i);
/* 27 */       d0 = j * Math.PI * -2.0D / this.squidTentacles.length + 1.5707963267948966D;
/* 28 */       (this.squidTentacles[j]).rotateAngleY = (float)d0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 34 */     for (ModelRenderer modelrenderer : this.squidTentacles)
/*    */     {
/* 36 */       modelrenderer.rotateAngleX = ageInTicks;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 42 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 43 */     this.squidBody.render(scale);
/*    */     
/* 45 */     for (int i = 0; i < this.squidTentacles.length; i++)
/*    */     {
/* 47 */       this.squidTentacles[i].render(scale);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelSquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */