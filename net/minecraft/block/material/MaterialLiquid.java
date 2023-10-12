/*    */ package net.minecraft.block.material;
/*    */ 
/*    */ public class MaterialLiquid
/*    */   extends Material
/*    */ {
/*    */   public MaterialLiquid(MapColor color) {
/*  7 */     super(color);
/*  8 */     setReplaceable();
/*  9 */     setNoPushMobility();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isLiquid() {
/* 14 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean blocksMovement() {
/* 19 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSolid() {
/* 24 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\material\MaterialLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */