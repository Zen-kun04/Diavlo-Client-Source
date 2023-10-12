/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSoup
/*    */   extends ItemFood
/*    */ {
/*    */   public ItemSoup(int healAmount) {
/* 11 */     super(healAmount, false);
/* 12 */     setMaxStackSize(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
/* 17 */     super.onItemUseFinish(stack, worldIn, playerIn);
/* 18 */     return new ItemStack(Items.bowl);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSoup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */