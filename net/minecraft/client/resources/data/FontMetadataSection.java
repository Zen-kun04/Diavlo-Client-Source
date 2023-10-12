/*    */ package net.minecraft.client.resources.data;
/*    */ 
/*    */ public class FontMetadataSection
/*    */   implements IMetadataSection
/*    */ {
/*    */   private final float[] charWidths;
/*    */   private final float[] charLefts;
/*    */   private final float[] charSpacings;
/*    */   
/*    */   public FontMetadataSection(float[] p_i1310_1_, float[] p_i1310_2_, float[] p_i1310_3_) {
/* 11 */     this.charWidths = p_i1310_1_;
/* 12 */     this.charLefts = p_i1310_2_;
/* 13 */     this.charSpacings = p_i1310_3_;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\data\FontMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */