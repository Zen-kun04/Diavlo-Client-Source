/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockRedstoneDiode
/*     */   extends BlockDirectional {
/*     */   protected final boolean isRepeaterPowered;
/*     */   
/*     */   protected BlockRedstoneDiode(boolean powered) {
/*  21 */     super(Material.circuits);
/*  22 */     this.isRepeaterPowered = powered;
/*  23 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  28 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  33 */     return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) ? super.canPlaceBlockAt(worldIn, pos) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos) {
/*  38 */     return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  47 */     if (!isLocked((IBlockAccess)worldIn, pos, state)) {
/*     */       
/*  49 */       boolean flag = shouldBePowered(worldIn, pos, state);
/*     */       
/*  51 */       if (this.isRepeaterPowered && !flag) {
/*     */         
/*  53 */         worldIn.setBlockState(pos, getUnpoweredState(state), 2);
/*     */       }
/*  55 */       else if (!this.isRepeaterPowered) {
/*     */         
/*  57 */         worldIn.setBlockState(pos, getPoweredState(state), 2);
/*     */         
/*  59 */         if (!flag)
/*     */         {
/*  61 */           worldIn.updateBlockTick(pos, getPoweredState(state).getBlock(), getTickDelay(state), -1);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/*  69 */     return (side.getAxis() != EnumFacing.Axis.Y);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPowered(IBlockState state) {
/*  74 */     return this.isRepeaterPowered;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/*  79 */     return getWeakPower(worldIn, pos, state, side);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/*  84 */     return !isPowered(state) ? 0 : ((state.getValue((IProperty)FACING) == side) ? getActiveSignal(worldIn, pos, state) : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  89 */     if (canBlockStay(worldIn, pos)) {
/*     */       
/*  91 */       updateState(worldIn, pos, state);
/*     */     }
/*     */     else {
/*     */       
/*  95 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  96 */       worldIn.setBlockToAir(pos);
/*     */       
/*  98 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/* 100 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
/* 107 */     if (!isLocked((IBlockAccess)worldIn, pos, state)) {
/*     */       
/* 109 */       boolean flag = shouldBePowered(worldIn, pos, state);
/*     */       
/* 111 */       if (((this.isRepeaterPowered && !flag) || (!this.isRepeaterPowered && flag)) && !worldIn.isBlockTickPending(pos, this)) {
/*     */         
/* 113 */         int i = -1;
/*     */         
/* 115 */         if (isFacingTowardsRepeater(worldIn, pos, state)) {
/*     */           
/* 117 */           i = -3;
/*     */         }
/* 119 */         else if (this.isRepeaterPowered) {
/*     */           
/* 121 */           i = -2;
/*     */         } 
/*     */         
/* 124 */         worldIn.updateBlockTick(pos, this, getDelay(state), i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
/* 131 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
/* 136 */     return (calculateInputStrength(worldIn, pos, state) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
/* 141 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 142 */     BlockPos blockpos = pos.offset(enumfacing);
/* 143 */     int i = worldIn.getRedstonePower(blockpos, enumfacing);
/*     */     
/* 145 */     if (i >= 15)
/*     */     {
/* 147 */       return i;
/*     */     }
/*     */ 
/*     */     
/* 151 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 152 */     return Math.max(i, (iblockstate.getBlock() == Blocks.redstone_wire) ? ((Integer)iblockstate.getValue((IProperty)BlockRedstoneWire.POWER)).intValue() : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
/* 158 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 159 */     EnumFacing enumfacing1 = enumfacing.rotateY();
/* 160 */     EnumFacing enumfacing2 = enumfacing.rotateYCCW();
/* 161 */     return Math.max(getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 166 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 167 */     Block block = iblockstate.getBlock();
/* 168 */     return canPowerSide(block) ? ((block == Blocks.redstone_wire) ? ((Integer)iblockstate.getValue((IProperty)BlockRedstoneWire.POWER)).intValue() : worldIn.getStrongPower(pos, side)) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 173 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 178 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 183 */     if (shouldBePowered(worldIn, pos, state))
/*     */     {
/* 185 */       worldIn.scheduleUpdate(pos, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 191 */     notifyNeighbors(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
/* 196 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 197 */     BlockPos blockpos = pos.offset(enumfacing.getOpposite());
/* 198 */     worldIn.notifyBlockOfStateChange(blockpos, this);
/* 199 */     worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
/* 204 */     if (this.isRepeaterPowered)
/*     */     {
/* 206 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/* 208 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */     
/* 212 */     super.onBlockDestroyedByPlayer(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canPowerSide(Block blockIn) {
/* 222 */     return blockIn.canProvidePower();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
/* 227 */     return 15;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isRedstoneRepeaterBlockID(Block blockIn) {
/* 232 */     return (Blocks.unpowered_repeater.isAssociated(blockIn) || Blocks.unpowered_comparator.isAssociated(blockIn));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAssociated(Block other) {
/* 237 */     return (other == getPoweredState(getDefaultState()).getBlock() || other == getUnpoweredState(getDefaultState()).getBlock());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state) {
/* 242 */     EnumFacing enumfacing = ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite();
/* 243 */     BlockPos blockpos = pos.offset(enumfacing);
/* 244 */     return isRedstoneRepeaterBlockID(worldIn.getBlockState(blockpos).getBlock()) ? ((worldIn.getBlockState(blockpos).getValue((IProperty)FACING) != enumfacing)) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getTickDelay(IBlockState state) {
/* 249 */     return getDelay(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int getDelay(IBlockState paramIBlockState);
/*     */   
/*     */   protected abstract IBlockState getPoweredState(IBlockState paramIBlockState);
/*     */   
/*     */   protected abstract IBlockState getUnpoweredState(IBlockState paramIBlockState);
/*     */   
/*     */   public boolean isAssociatedBlock(Block other) {
/* 260 */     return isAssociated(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 265 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedstoneDiode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */