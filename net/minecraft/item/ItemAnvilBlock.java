/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemAnvilBlock
/*    */   extends ItemMultiTexture
/*    */ {
/*    */   public ItemAnvilBlock(Block block) {
/*  9 */     super(block, block, new String[] { "intact", "slightlyDamaged", "veryDamaged" });
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 14 */     return damage << 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemAnvilBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */