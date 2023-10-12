/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.item.EntityExpBottle;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemExpBottle extends Item {
/*    */   public ItemExpBottle() {
/* 13 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasEffect(ItemStack stack) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 23 */     if (!playerIn.capabilities.isCreativeMode)
/*    */     {
/* 25 */       itemStackIn.stackSize--;
/*    */     }
/*    */     
/* 28 */     worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*    */     
/* 30 */     if (!worldIn.isRemote)
/*    */     {
/* 32 */       worldIn.spawnEntityInWorld((Entity)new EntityExpBottle(worldIn, (EntityLivingBase)playerIn));
/*    */     }
/*    */     
/* 35 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 36 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemExpBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */