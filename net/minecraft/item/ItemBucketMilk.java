/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemBucketMilk
/*    */   extends Item
/*    */ {
/*    */   public ItemBucketMilk() {
/* 13 */     setMaxStackSize(1);
/* 14 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
/* 19 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 21 */       stack.stackSize--;
/*    */     }
/*    */     
/* 24 */     if (!worldIn.isRemote)
/*    */     {
/* 26 */       playerIn.clearActivePotions();
/*    */     }
/*    */     
/* 29 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 30 */     return (stack.stackSize <= 0) ? new ItemStack(Items.bucket) : stack;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxItemUseDuration(ItemStack stack) {
/* 35 */     return 32;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumAction getItemUseAction(ItemStack stack) {
/* 40 */     return EnumAction.DRINK;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 45 */     playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/* 46 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBucketMilk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */