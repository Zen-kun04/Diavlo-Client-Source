/*    */ package net.minecraft.block;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyDirection;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockWallSign extends BlockSign {
/* 14 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*    */ 
/*    */   
/*    */   public BlockWallSign() {
/* 18 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 24 */     EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
/* 25 */     float f = 0.28125F;
/* 26 */     float f1 = 0.78125F;
/* 27 */     float f2 = 0.0F;
/* 28 */     float f3 = 1.0F;
/* 29 */     float f4 = 0.125F;
/* 30 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*    */     
/* 32 */     switch (enumfacing) {
/*    */       
/*    */       case NORTH:
/* 35 */         setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
/*    */         break;
/*    */       
/*    */       case SOUTH:
/* 39 */         setBlockBounds(f2, f, 0.0F, f3, f1, f4);
/*    */         break;
/*    */       
/*    */       case WEST:
/* 43 */         setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
/*    */         break;
/*    */       
/*    */       case EAST:
/* 47 */         setBlockBounds(0.0F, f, f2, f4, f1, f3);
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 53 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*    */     
/* 55 */     if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid()) {
/*    */       
/* 57 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 58 */       worldIn.setBlockToAir(pos);
/*    */     } 
/*    */     
/* 61 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getStateFromMeta(int meta) {
/* 66 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*    */     
/* 68 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*    */     {
/* 70 */       enumfacing = EnumFacing.NORTH;
/*    */     }
/*    */     
/* 73 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetaFromState(IBlockState state) {
/* 78 */     return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createBlockState() {
/* 83 */     return new BlockState(this, new IProperty[] { (IProperty)FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockWallSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */