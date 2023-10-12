/*    */ package net.minecraft.world.gen;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ public class FlatLayerInfo
/*    */ {
/*    */   private final int field_175902_a;
/*    */   private IBlockState layerMaterial;
/*    */   private int layerCount;
/*    */   private int layerMinimumY;
/*    */   
/*    */   public FlatLayerInfo(int p_i45467_1_, Block p_i45467_2_) {
/* 16 */     this(3, p_i45467_1_, p_i45467_2_);
/*    */   }
/*    */ 
/*    */   
/*    */   public FlatLayerInfo(int p_i45627_1_, int height, Block layerMaterialIn) {
/* 21 */     this.layerCount = 1;
/* 22 */     this.field_175902_a = p_i45627_1_;
/* 23 */     this.layerCount = height;
/* 24 */     this.layerMaterial = layerMaterialIn.getDefaultState();
/*    */   }
/*    */ 
/*    */   
/*    */   public FlatLayerInfo(int p_i45628_1_, int p_i45628_2_, Block p_i45628_3_, int p_i45628_4_) {
/* 29 */     this(p_i45628_1_, p_i45628_2_, p_i45628_3_);
/* 30 */     this.layerMaterial = p_i45628_3_.getStateFromMeta(p_i45628_4_);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLayerCount() {
/* 35 */     return this.layerCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getLayerMaterial() {
/* 40 */     return this.layerMaterial;
/*    */   }
/*    */ 
/*    */   
/*    */   private Block getLayerMaterialBlock() {
/* 45 */     return this.layerMaterial.getBlock();
/*    */   }
/*    */ 
/*    */   
/*    */   private int getFillBlockMeta() {
/* 50 */     return this.layerMaterial.getBlock().getMetaFromState(this.layerMaterial);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinY() {
/* 55 */     return this.layerMinimumY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMinY(int minY) {
/* 60 */     this.layerMinimumY = minY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/*    */     String s;
/* 67 */     if (this.field_175902_a >= 3) {
/*    */       
/* 69 */       ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(getLayerMaterialBlock());
/* 70 */       s = (resourcelocation == null) ? "null" : resourcelocation.toString();
/*    */       
/* 72 */       if (this.layerCount > 1)
/*    */       {
/* 74 */         s = this.layerCount + "*" + s;
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 79 */       s = Integer.toString(Block.getIdFromBlock(getLayerMaterialBlock()));
/*    */       
/* 81 */       if (this.layerCount > 1)
/*    */       {
/* 83 */         s = this.layerCount + "x" + s;
/*    */       }
/*    */     } 
/*    */     
/* 87 */     int i = getFillBlockMeta();
/*    */     
/* 89 */     if (i > 0)
/*    */     {
/* 91 */       s = s + ":" + i;
/*    */     }
/*    */     
/* 94 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\FlatLayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */