/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.boss.EntityWither;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelWither
/*    */   extends ModelBase
/*    */ {
/*    */   private ModelRenderer[] field_82905_a;
/*    */   private ModelRenderer[] field_82904_b;
/*    */   
/*    */   public ModelWither(float p_i46302_1_) {
/* 15 */     this.textureWidth = 64;
/* 16 */     this.textureHeight = 64;
/* 17 */     this.field_82905_a = new ModelRenderer[3];
/* 18 */     this.field_82905_a[0] = new ModelRenderer(this, 0, 16);
/* 19 */     this.field_82905_a[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, p_i46302_1_);
/* 20 */     this.field_82905_a[1] = (new ModelRenderer(this)).setTextureSize(this.textureWidth, this.textureHeight);
/* 21 */     this.field_82905_a[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
/* 22 */     this.field_82905_a[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, p_i46302_1_);
/* 23 */     this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, p_i46302_1_);
/* 24 */     this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, p_i46302_1_);
/* 25 */     this.field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, p_i46302_1_);
/* 26 */     this.field_82905_a[2] = new ModelRenderer(this, 12, 22);
/* 27 */     this.field_82905_a[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, p_i46302_1_);
/* 28 */     this.field_82904_b = new ModelRenderer[3];
/* 29 */     this.field_82904_b[0] = new ModelRenderer(this, 0, 0);
/* 30 */     this.field_82904_b[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, p_i46302_1_);
/* 31 */     this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
/* 32 */     this.field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
/* 33 */     (this.field_82904_b[1]).rotationPointX = -8.0F;
/* 34 */     (this.field_82904_b[1]).rotationPointY = 4.0F;
/* 35 */     this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
/* 36 */     this.field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
/* 37 */     (this.field_82904_b[2]).rotationPointX = 10.0F;
/* 38 */     (this.field_82904_b[2]).rotationPointY = 4.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 43 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*    */     
/* 45 */     for (ModelRenderer modelrenderer : this.field_82904_b)
/*    */     {
/* 47 */       modelrenderer.render(scale);
/*    */     }
/*    */     
/* 50 */     for (ModelRenderer modelrenderer1 : this.field_82905_a)
/*    */     {
/* 52 */       modelrenderer1.render(scale);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 58 */     float f = MathHelper.cos(ageInTicks * 0.1F);
/* 59 */     (this.field_82905_a[1]).rotateAngleX = (0.065F + 0.05F * f) * 3.1415927F;
/* 60 */     this.field_82905_a[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos((this.field_82905_a[1]).rotateAngleX) * 10.0F, -0.5F + MathHelper.sin((this.field_82905_a[1]).rotateAngleX) * 10.0F);
/* 61 */     (this.field_82905_a[2]).rotateAngleX = (0.265F + 0.1F * f) * 3.1415927F;
/* 62 */     (this.field_82904_b[0]).rotateAngleY = netHeadYaw / 57.295776F;
/* 63 */     (this.field_82904_b[0]).rotateAngleX = headPitch / 57.295776F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/* 68 */     EntityWither entitywither = (EntityWither)entitylivingbaseIn;
/*    */     
/* 70 */     for (int i = 1; i < 3; i++) {
/*    */       
/* 72 */       (this.field_82904_b[i]).rotateAngleY = (entitywither.func_82207_a(i - 1) - entitylivingbaseIn.renderYawOffset) / 57.295776F;
/* 73 */       (this.field_82904_b[i]).rotateAngleX = entitywither.func_82210_r(i - 1) / 57.295776F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */