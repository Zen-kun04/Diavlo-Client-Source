/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyEnum;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockRail
/*    */   extends BlockRailBase {
/* 12 */   public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);
/*    */ 
/*    */   
/*    */   protected BlockRail() {
/* 16 */     super(false);
/* 17 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 22 */     if (neighborBlock.canProvidePower() && (new BlockRailBase.Rail(this, worldIn, pos, state)).countAdjacentRails() == 3)
/*    */     {
/* 24 */       func_176564_a(worldIn, pos, state, false);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty() {
/* 30 */     return (IProperty<BlockRailBase.EnumRailDirection>)SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getStateFromMeta(int meta) {
/* 35 */     return getDefaultState().withProperty((IProperty)SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetaFromState(IBlockState state) {
/* 40 */     return ((BlockRailBase.EnumRailDirection)state.getValue((IProperty)SHAPE)).getMetadata();
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createBlockState() {
/* 45 */     return new BlockState(this, new IProperty[] { (IProperty)SHAPE });
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */