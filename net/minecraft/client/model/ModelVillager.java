/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class ModelVillager
/*    */   extends ModelBase
/*    */ {
/*    */   public ModelRenderer villagerHead;
/*    */   public ModelRenderer villagerBody;
/*    */   public ModelRenderer villagerArms;
/*    */   public ModelRenderer rightVillagerLeg;
/*    */   public ModelRenderer leftVillagerLeg;
/*    */   public ModelRenderer villagerNose;
/*    */   
/*    */   public ModelVillager(float p_i1163_1_) {
/* 17 */     this(p_i1163_1_, 0.0F, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModelVillager(float p_i1164_1_, float p_i1164_2_, int p_i1164_3_, int p_i1164_4_) {
/* 22 */     this.villagerHead = (new ModelRenderer(this)).setTextureSize(p_i1164_3_, p_i1164_4_);
/* 23 */     this.villagerHead.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
/* 24 */     this.villagerHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, p_i1164_1_);
/* 25 */     this.villagerNose = (new ModelRenderer(this)).setTextureSize(p_i1164_3_, p_i1164_4_);
/* 26 */     this.villagerNose.setRotationPoint(0.0F, p_i1164_2_ - 2.0F, 0.0F);
/* 27 */     this.villagerNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, p_i1164_1_);
/* 28 */     this.villagerHead.addChild(this.villagerNose);
/* 29 */     this.villagerBody = (new ModelRenderer(this)).setTextureSize(p_i1164_3_, p_i1164_4_);
/* 30 */     this.villagerBody.setRotationPoint(0.0F, 0.0F + p_i1164_2_, 0.0F);
/* 31 */     this.villagerBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, p_i1164_1_);
/* 32 */     this.villagerBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, p_i1164_1_ + 0.5F);
/* 33 */     this.villagerArms = (new ModelRenderer(this)).setTextureSize(p_i1164_3_, p_i1164_4_);
/* 34 */     this.villagerArms.setRotationPoint(0.0F, 0.0F + p_i1164_2_ + 2.0F, 0.0F);
/* 35 */     this.villagerArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, p_i1164_1_);
/* 36 */     this.villagerArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, p_i1164_1_);
/* 37 */     this.villagerArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, p_i1164_1_);
/* 38 */     this.rightVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(p_i1164_3_, p_i1164_4_);
/* 39 */     this.rightVillagerLeg.setRotationPoint(-2.0F, 12.0F + p_i1164_2_, 0.0F);
/* 40 */     this.rightVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i1164_1_);
/* 41 */     this.leftVillagerLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(p_i1164_3_, p_i1164_4_);
/* 42 */     this.leftVillagerLeg.mirror = true;
/* 43 */     this.leftVillagerLeg.setRotationPoint(2.0F, 12.0F + p_i1164_2_, 0.0F);
/* 44 */     this.leftVillagerLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i1164_1_);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 49 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/* 50 */     this.villagerHead.render(scale);
/* 51 */     this.villagerBody.render(scale);
/* 52 */     this.rightVillagerLeg.render(scale);
/* 53 */     this.leftVillagerLeg.render(scale);
/* 54 */     this.villagerArms.render(scale);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 59 */     this.villagerHead.rotateAngleY = netHeadYaw / 57.295776F;
/* 60 */     this.villagerHead.rotateAngleX = headPitch / 57.295776F;
/* 61 */     this.villagerArms.rotationPointY = 3.0F;
/* 62 */     this.villagerArms.rotationPointZ = -1.0F;
/* 63 */     this.villagerArms.rotateAngleX = -0.75F;
/* 64 */     this.rightVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
/* 65 */     this.leftVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount * 0.5F;
/* 66 */     this.rightVillagerLeg.rotateAngleY = 0.0F;
/* 67 */     this.leftVillagerLeg.rotateAngleY = 0.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */