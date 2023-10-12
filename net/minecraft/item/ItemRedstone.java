/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemRedstone
/*    */   extends Item
/*    */ {
/*    */   public ItemRedstone() {
/* 16 */     setCreativeTab(CreativeTabs.tabRedstone);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 21 */     boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
/* 22 */     BlockPos blockpos = flag ? pos : pos.offset(side);
/*    */     
/* 24 */     if (!playerIn.canPlayerEdit(blockpos, side, stack))
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*    */     
/* 32 */     if (!worldIn.canBlockBePlaced(block, blockpos, false, side, (Entity)null, stack))
/*    */     {
/* 34 */       return false;
/*    */     }
/* 36 */     if (Blocks.redstone_wire.canPlaceBlockAt(worldIn, blockpos)) {
/*    */       
/* 38 */       stack.stackSize--;
/* 39 */       worldIn.setBlockState(blockpos, Blocks.redstone_wire.getDefaultState());
/* 40 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 44 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemRedstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */