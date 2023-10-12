/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityMagmaCube;
/*    */ 
/*    */ public class ModelMagmaCube
/*    */   extends ModelBase {
/*  9 */   ModelRenderer[] segments = new ModelRenderer[8];
/*    */   
/*    */   ModelRenderer core;
/*    */   
/*    */   public ModelMagmaCube() {
/* 14 */     for (int i = 0; i < this.segments.length; i++) {
/*    */       
/* 16 */       int j = 0;
/* 17 */       int k = i;
/*    */       
/* 19 */       if (i == 2) {
/*    */         
/* 21 */         j = 24;
/* 22 */         k = 10;
/*    */       }
/* 24 */       else if (i == 3) {
/*    */         
/* 26 */         j = 24;
/* 27 */         k = 19;
/*    */       } 
/*    */       
/* 30 */       this.segments[i] = new ModelRenderer(this, j, k);
/* 31 */       this.segments[i].addBox(-4.0F, (16 + i), -4.0F, 8, 1, 8);
/*    */     } 
/*    */     
/* 34 */     this.core = new ModelRenderer(this, 0, 16);
/* 35 */     this.core.addBox(-2.0F, 18.0F, -2.0F, 4, 4, 4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/* 40 */     EntityMagmaCube entitymagmacube = (EntityMagmaCube)entitylivingbaseIn;
/* 41 */     float f = entitymagmacube.prevSquishFactor + (entitymagmacube.squishFactor - entitymagmacube.prevSquishFactor) * partialTickTime;
/*    */     
/* 43 */     if (f < 0.0F)
/*    */     {
/* 45 */       f = 0.0F;
/*    */     }
/*    */     
/* 48 */     for (int i = 0; i < this.segments.length; i++)
/*    */     {
/* 50 */       (this.segments[i]).rotationPointY = -(4 - i) * f * 1.7F;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 56 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 57 */     this.core.render(scale);
/*    */     
/* 59 */     for (int i = 0; i < this.segments.length; i++)
/*    */     {
/* 61 */       this.segments[i].render(scale);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelMagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */