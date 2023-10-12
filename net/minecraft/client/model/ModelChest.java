/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ public class ModelChest
/*    */   extends ModelBase {
/*  5 */   public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
/*    */   
/*    */   public ModelRenderer chestBelow;
/*    */   public ModelRenderer chestKnob;
/*    */   
/*    */   public ModelChest() {
/* 11 */     this.chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
/* 12 */     this.chestLid.rotationPointX = 1.0F;
/* 13 */     this.chestLid.rotationPointY = 7.0F;
/* 14 */     this.chestLid.rotationPointZ = 15.0F;
/* 15 */     this.chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
/* 16 */     this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
/* 17 */     this.chestKnob.rotationPointX = 8.0F;
/* 18 */     this.chestKnob.rotationPointY = 7.0F;
/* 19 */     this.chestKnob.rotationPointZ = 15.0F;
/* 20 */     this.chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
/* 21 */     this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
/* 22 */     this.chestBelow.rotationPointX = 1.0F;
/* 23 */     this.chestBelow.rotationPointY = 6.0F;
/* 24 */     this.chestBelow.rotationPointZ = 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderAll() {
/* 29 */     this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
/* 30 */     this.chestLid.render(0.0625F);
/* 31 */     this.chestKnob.render(0.0625F);
/* 32 */     this.chestBelow.render(0.0625F);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */