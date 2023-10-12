/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemPiston
/*    */   extends ItemBlock
/*    */ {
/*    */   public ItemPiston(Block block) {
/*  9 */     super(block);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 14 */     return 7;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemPiston.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */