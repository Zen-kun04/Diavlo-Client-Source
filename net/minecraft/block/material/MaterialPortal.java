/*    */ package net.minecraft.block.material;
/*    */ 
/*    */ public class MaterialPortal
/*    */   extends Material
/*    */ {
/*    */   public MaterialPortal(MapColor color) {
/*  7 */     super(color);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSolid() {
/* 12 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean blocksLight() {
/* 17 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean blocksMovement() {
/* 22 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\material\MaterialPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */