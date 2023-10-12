/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockLiquid;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemLilyPad
/*    */   extends ItemColored {
/*    */   public ItemLilyPad(Block block) {
/* 18 */     super(block, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 23 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
/*    */     
/* 25 */     if (movingobjectposition == null)
/*    */     {
/* 27 */       return itemStackIn;
/*    */     }
/*    */ 
/*    */     
/* 31 */     if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*    */       
/* 33 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*    */       
/* 35 */       if (!worldIn.isBlockModifiable(playerIn, blockpos))
/*    */       {
/* 37 */         return itemStackIn;
/*    */       }
/*    */       
/* 40 */       if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
/*    */       {
/* 42 */         return itemStackIn;
/*    */       }
/*    */       
/* 45 */       BlockPos blockpos1 = blockpos.up();
/* 46 */       IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*    */       
/* 48 */       if (iblockstate.getBlock().getMaterial() == Material.water && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0 && worldIn.isAirBlock(blockpos1)) {
/*    */         
/* 50 */         worldIn.setBlockState(blockpos1, Blocks.waterlily.getDefaultState());
/*    */         
/* 52 */         if (!playerIn.capabilities.isCreativeMode)
/*    */         {
/* 54 */           itemStackIn.stackSize--;
/*    */         }
/*    */         
/* 57 */         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*    */       } 
/*    */     } 
/*    */     
/* 61 */     return itemStackIn;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/* 67 */     return Blocks.waterlily.getRenderColor(Blocks.waterlily.getStateFromMeta(stack.getMetadata()));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemLilyPad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */