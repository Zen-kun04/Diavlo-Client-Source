/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemGlassBottle
/*    */   extends Item
/*    */ {
/*    */   public ItemGlassBottle() {
/* 16 */     setCreativeTab(CreativeTabs.tabBrewing);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 21 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
/*    */     
/* 23 */     if (movingobjectposition == null)
/*    */     {
/* 25 */       return itemStackIn;
/*    */     }
/*    */ 
/*    */     
/* 29 */     if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*    */       
/* 31 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*    */       
/* 33 */       if (!worldIn.isBlockModifiable(playerIn, blockpos))
/*    */       {
/* 35 */         return itemStackIn;
/*    */       }
/*    */       
/* 38 */       if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
/*    */       {
/* 40 */         return itemStackIn;
/*    */       }
/*    */       
/* 43 */       if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.water) {
/*    */         
/* 45 */         itemStackIn.stackSize--;
/* 46 */         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*    */         
/* 48 */         if (itemStackIn.stackSize <= 0)
/*    */         {
/* 50 */           return new ItemStack(Items.potionitem);
/*    */         }
/*    */         
/* 53 */         if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.potionitem)))
/*    */         {
/* 55 */           playerIn.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     return itemStackIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemGlassBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */