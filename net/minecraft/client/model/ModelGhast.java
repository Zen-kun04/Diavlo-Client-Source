/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelGhast
/*    */   extends ModelBase {
/*    */   ModelRenderer body;
/* 11 */   ModelRenderer[] tentacles = new ModelRenderer[9];
/*    */ 
/*    */   
/*    */   public ModelGhast() {
/* 15 */     int i = -16;
/* 16 */     this.body = new ModelRenderer(this, 0, 0);
/* 17 */     this.body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
/* 18 */     this.body.rotationPointY += (24 + i);
/* 19 */     Random random = new Random(1660L);
/*    */     
/* 21 */     for (int j = 0; j < this.tentacles.length; j++) {
/*    */       
/* 23 */       this.tentacles[j] = new ModelRenderer(this, 0, 0);
/* 24 */       float f = (((j % 3) - (j / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
/* 25 */       float f1 = ((j / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
/* 26 */       int k = random.nextInt(7) + 8;
/* 27 */       this.tentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, k, 2);
/* 28 */       (this.tentacles[j]).rotationPointX = f;
/* 29 */       (this.tentacles[j]).rotationPointZ = f1;
/* 30 */       (this.tentacles[j]).rotationPointY = (31 + i);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 36 */     for (int i = 0; i < this.tentacles.length; i++)
/*    */     {
/* 38 */       (this.tentacles[i]).rotateAngleX = 0.2F * MathHelper.sin(ageInTicks * 0.3F + i) + 0.4F;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 44 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 45 */     GlStateManager.pushMatrix();
/* 46 */     GlStateManager.translate(0.0F, 0.6F, 0.0F);
/* 47 */     this.body.render(scale);
/*    */     
/* 49 */     for (ModelRenderer modelrenderer : this.tentacles)
/*    */     {
/* 51 */       modelrenderer.render(scale);
/*    */     }
/*    */     
/* 54 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */