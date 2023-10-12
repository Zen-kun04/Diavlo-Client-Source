/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockBed;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemBed extends Item {
/*    */   public ItemBed() {
/* 18 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 23 */     if (worldIn.isRemote)
/*    */     {
/* 25 */       return true;
/*    */     }
/* 27 */     if (side != EnumFacing.UP)
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 33 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 34 */     Block block = iblockstate.getBlock();
/* 35 */     boolean flag = block.isReplaceable(worldIn, pos);
/*    */     
/* 37 */     if (!flag)
/*    */     {
/* 39 */       pos = pos.up();
/*    */     }
/*    */     
/* 42 */     int i = MathHelper.floor_double((playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3;
/* 43 */     EnumFacing enumfacing = EnumFacing.getHorizontal(i);
/* 44 */     BlockPos blockpos = pos.offset(enumfacing);
/*    */     
/* 46 */     if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos, side, stack)) {
/*    */       
/* 48 */       boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
/* 49 */       boolean flag2 = (flag || worldIn.isAirBlock(pos));
/* 50 */       boolean flag3 = (flag1 || worldIn.isAirBlock(blockpos));
/*    */       
/* 52 */       if (flag2 && flag3 && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, blockpos.down())) {
/*    */         
/* 54 */         IBlockState iblockstate1 = Blocks.bed.getDefaultState().withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty((IProperty)BlockBed.FACING, (Comparable)enumfacing).withProperty((IProperty)BlockBed.PART, (Comparable)BlockBed.EnumPartType.FOOT);
/*    */         
/* 56 */         if (worldIn.setBlockState(pos, iblockstate1, 3)) {
/*    */           
/* 58 */           IBlockState iblockstate2 = iblockstate1.withProperty((IProperty)BlockBed.PART, (Comparable)BlockBed.EnumPartType.HEAD);
/* 59 */           worldIn.setBlockState(blockpos, iblockstate2, 3);
/*    */         } 
/*    */         
/* 62 */         stack.stackSize--;
/* 63 */         return true;
/*    */       } 
/*    */ 
/*    */       
/* 67 */       return false;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */