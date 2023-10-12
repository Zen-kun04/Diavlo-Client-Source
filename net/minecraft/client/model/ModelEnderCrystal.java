/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class ModelEnderCrystal
/*    */   extends ModelBase {
/*    */   private ModelRenderer cube;
/*  9 */   private ModelRenderer glass = new ModelRenderer(this, "glass");
/*    */   
/*    */   private ModelRenderer base;
/*    */   
/*    */   public ModelEnderCrystal(float p_i1170_1_, boolean p_i1170_2_) {
/* 14 */     this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
/* 15 */     this.cube = new ModelRenderer(this, "cube");
/* 16 */     this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
/*    */     
/* 18 */     if (p_i1170_2_) {
/*    */       
/* 20 */       this.base = new ModelRenderer(this, "base");
/* 21 */       this.base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 27 */     GlStateManager.pushMatrix();
/* 28 */     GlStateManager.scale(2.0F, 2.0F, 2.0F);
/* 29 */     GlStateManager.translate(0.0F, -0.5F, 0.0F);
/*    */     
/* 31 */     if (this.base != null)
/*    */     {
/* 33 */       this.base.render(scale);
/*    */     }
/*    */     
/* 36 */     GlStateManager.rotate(p_78088_3_, 0.0F, 1.0F, 0.0F);
/* 37 */     GlStateManager.translate(0.0F, 0.8F + p_78088_4_, 0.0F);
/* 38 */     GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
/* 39 */     this.glass.render(scale);
/* 40 */     float f = 0.875F;
/* 41 */     GlStateManager.scale(f, f, f);
/* 42 */     GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
/* 43 */     GlStateManager.rotate(p_78088_3_, 0.0F, 1.0F, 0.0F);
/* 44 */     this.glass.render(scale);
/* 45 */     GlStateManager.scale(f, f, f);
/* 46 */     GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
/* 47 */     GlStateManager.rotate(p_78088_3_, 0.0F, 1.0F, 0.0F);
/* 48 */     this.cube.render(scale);
/* 49 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */