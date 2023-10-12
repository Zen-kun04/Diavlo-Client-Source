/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelBlaze
/*    */   extends ModelBase {
/*  8 */   private ModelRenderer[] blazeSticks = new ModelRenderer[12];
/*    */   
/*    */   private ModelRenderer blazeHead;
/*    */   
/*    */   public ModelBlaze() {
/* 13 */     for (int i = 0; i < this.blazeSticks.length; i++) {
/*    */       
/* 15 */       this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
/* 16 */       this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
/*    */     } 
/*    */     
/* 19 */     this.blazeHead = new ModelRenderer(this, 0, 0);
/* 20 */     this.blazeHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 25 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 26 */     this.blazeHead.render(scale);
/*    */     
/* 28 */     for (int i = 0; i < this.blazeSticks.length; i++)
/*    */     {
/* 30 */       this.blazeSticks[i].render(scale);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 36 */     float f = ageInTicks * 3.1415927F * -0.1F;
/*    */     
/* 38 */     for (int i = 0; i < 4; i++) {
/*    */       
/* 40 */       (this.blazeSticks[i]).rotationPointY = -2.0F + MathHelper.cos(((i * 2) + ageInTicks) * 0.25F);
/* 41 */       (this.blazeSticks[i]).rotationPointX = MathHelper.cos(f) * 9.0F;
/* 42 */       (this.blazeSticks[i]).rotationPointZ = MathHelper.sin(f) * 9.0F;
/* 43 */       f++;
/*    */     } 
/*    */     
/* 46 */     f = 0.7853982F + ageInTicks * 3.1415927F * 0.03F;
/*    */     
/* 48 */     for (int j = 4; j < 8; j++) {
/*    */       
/* 50 */       (this.blazeSticks[j]).rotationPointY = 2.0F + MathHelper.cos(((j * 2) + ageInTicks) * 0.25F);
/* 51 */       (this.blazeSticks[j]).rotationPointX = MathHelper.cos(f) * 7.0F;
/* 52 */       (this.blazeSticks[j]).rotationPointZ = MathHelper.sin(f) * 7.0F;
/* 53 */       f++;
/*    */     } 
/*    */     
/* 56 */     f = 0.47123894F + ageInTicks * 3.1415927F * -0.05F;
/*    */     
/* 58 */     for (int k = 8; k < 12; k++) {
/*    */       
/* 60 */       (this.blazeSticks[k]).rotationPointY = 11.0F + MathHelper.cos((k * 1.5F + ageInTicks) * 0.5F);
/* 61 */       (this.blazeSticks[k]).rotationPointX = MathHelper.cos(f) * 5.0F;
/* 62 */       (this.blazeSticks[k]).rotationPointZ = MathHelper.sin(f) * 5.0F;
/* 63 */       f++;
/*    */     } 
/*    */     
/* 66 */     this.blazeHead.rotateAngleY = netHeadYaw / 57.295776F;
/* 67 */     this.blazeHead.rotateAngleX = headPitch / 57.295776F;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */