/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class BlockContainer
/*    */   extends Block
/*    */   implements ITileEntityProvider {
/*    */   protected BlockContainer(Material materialIn) {
/* 15 */     this(materialIn, materialIn.getMaterialMapColor());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockContainer(Material p_i46402_1_, MapColor p_i46402_2_) {
/* 20 */     super(p_i46402_1_, p_i46402_2_);
/* 21 */     this.isBlockContainer = true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isInvalidNeighbor(World p_181086_1_, BlockPos p_181086_2_, EnumFacing p_181086_3_) {
/* 26 */     return (p_181086_1_.getBlockState(p_181086_2_.offset(p_181086_3_)).getBlock().getMaterial() == Material.cactus);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasInvalidNeighbor(World p_181087_1_, BlockPos p_181087_2_) {
/* 31 */     return (isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.NORTH) || isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.SOUTH) || isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.WEST) || isInvalidNeighbor(p_181087_1_, p_181087_2_, EnumFacing.EAST));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRenderType() {
/* 36 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 41 */     super.breakBlock(worldIn, pos, state);
/* 42 */     worldIn.removeTileEntity(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
/* 47 */     super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
/* 48 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 49 */     return (tileentity == null) ? false : tileentity.receiveClientEvent(eventID, eventParam);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */