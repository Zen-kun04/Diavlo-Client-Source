/*    */ package net.minecraft.item;
/*    */ 
/*    */ public class ItemBook
/*    */   extends Item
/*    */ {
/*    */   public boolean isItemTool(ItemStack stack) {
/*  7 */     return (stack.stackSize == 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getItemEnchantability() {
/* 12 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */