/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.BlockStandingSign;
/*    */ import net.minecraft.block.BlockWallSign;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntitySign;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSign
/*    */   extends Item {
/*    */   public ItemSign() {
/* 19 */     this.maxStackSize = 16;
/* 20 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 25 */     if (side == EnumFacing.DOWN)
/*    */     {
/* 27 */       return false;
/*    */     }
/* 29 */     if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 35 */     pos = pos.offset(side);
/*    */     
/* 37 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 39 */       return false;
/*    */     }
/* 41 */     if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, pos))
/*    */     {
/* 43 */       return false;
/*    */     }
/* 45 */     if (worldIn.isRemote)
/*    */     {
/* 47 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 51 */     if (side == EnumFacing.UP) {
/*    */       
/* 53 */       int i = MathHelper.floor_double(((playerIn.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 0xF;
/* 54 */       worldIn.setBlockState(pos, Blocks.standing_sign.getDefaultState().withProperty((IProperty)BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
/*    */     }
/*    */     else {
/*    */       
/* 58 */       worldIn.setBlockState(pos, Blocks.wall_sign.getDefaultState().withProperty((IProperty)BlockWallSign.FACING, (Comparable)side), 3);
/*    */     } 
/*    */     
/* 61 */     stack.stackSize--;
/* 62 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*    */     
/* 64 */     if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack))
/*    */     {
/* 66 */       playerIn.openEditSign((TileEntitySign)tileentity);
/*    */     }
/*    */     
/* 69 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */