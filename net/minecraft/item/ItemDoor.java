/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockDoor;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemDoor
/*    */   extends Item {
/*    */   private Block block;
/*    */   
/*    */   public ItemDoor(Block block) {
/* 18 */     this.block = block;
/* 19 */     setCreativeTab(CreativeTabs.tabRedstone);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 24 */     if (side != EnumFacing.UP)
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 31 */     Block block = iblockstate.getBlock();
/*    */     
/* 33 */     if (!block.isReplaceable(worldIn, pos))
/*    */     {
/* 35 */       pos = pos.offset(side);
/*    */     }
/*    */     
/* 38 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 40 */       return false;
/*    */     }
/* 42 */     if (!this.block.canPlaceBlockAt(worldIn, pos))
/*    */     {
/* 44 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 48 */     placeDoor(worldIn, pos, EnumFacing.fromAngle(playerIn.rotationYaw), this.block);
/* 49 */     stack.stackSize--;
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door) {
/* 57 */     BlockPos blockpos = pos.offset(facing.rotateY());
/* 58 */     BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
/* 59 */     int i = (worldIn.getBlockState(blockpos1).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).getBlock().isNormalCube() ? 1 : 0);
/* 60 */     int j = (worldIn.getBlockState(blockpos).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube() ? 1 : 0);
/* 61 */     boolean flag = (worldIn.getBlockState(blockpos1).getBlock() == door || worldIn.getBlockState(blockpos1.up()).getBlock() == door);
/* 62 */     boolean flag1 = (worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.up()).getBlock() == door);
/* 63 */     boolean flag2 = false;
/*    */     
/* 65 */     if ((flag && !flag1) || j > i)
/*    */     {
/* 67 */       flag2 = true;
/*    */     }
/*    */     
/* 70 */     BlockPos blockpos2 = pos.up();
/* 71 */     IBlockState iblockstate = door.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)facing).withProperty((IProperty)BlockDoor.HINGE, flag2 ? (Comparable)BlockDoor.EnumHingePosition.RIGHT : (Comparable)BlockDoor.EnumHingePosition.LEFT);
/* 72 */     worldIn.setBlockState(pos, iblockstate.withProperty((IProperty)BlockDoor.HALF, (Comparable)BlockDoor.EnumDoorHalf.LOWER), 2);
/* 73 */     worldIn.setBlockState(blockpos2, iblockstate.withProperty((IProperty)BlockDoor.HALF, (Comparable)BlockDoor.EnumDoorHalf.UPPER), 2);
/* 74 */     worldIn.notifyNeighborsOfStateChange(pos, door);
/* 75 */     worldIn.notifyNeighborsOfStateChange(blockpos2, door);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */