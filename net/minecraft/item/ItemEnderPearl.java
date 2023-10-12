/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.item.EntityEnderPearl;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemEnderPearl extends Item {
/*    */   public ItemEnderPearl() {
/* 13 */     this.maxStackSize = 16;
/* 14 */     setCreativeTab(CreativeTabs.tabMisc);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 19 */     if (playerIn.capabilities.isCreativeMode)
/*    */     {
/* 21 */       return itemStackIn;
/*    */     }
/*    */ 
/*    */     
/* 25 */     itemStackIn.stackSize--;
/* 26 */     worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/*    */     
/* 28 */     if (!worldIn.isRemote)
/*    */     {
/* 30 */       worldIn.spawnEntityInWorld((Entity)new EntityEnderPearl(worldIn, (EntityLivingBase)playerIn));
/*    */     }
/*    */     
/* 33 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 34 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemEnderPearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */