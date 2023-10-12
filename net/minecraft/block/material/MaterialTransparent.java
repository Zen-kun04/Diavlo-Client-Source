/*    */ package net.minecraft.block.material;
/*    */ 
/*    */ public class MaterialTransparent
/*    */   extends Material
/*    */ {
/*    */   public MaterialTransparent(MapColor color) {
/*  7 */     super(color);
/*  8 */     setReplaceable();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSolid() {
/* 13 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean blocksLight() {
/* 18 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean blocksMovement() {
/* 23 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\material\MaterialTransparent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */