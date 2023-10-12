/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockBasePressurePlate
/*     */   extends Block
/*     */ {
/*     */   protected BlockBasePressurePlate(Material materialIn) {
/*  19 */     this(materialIn, materialIn.getMaterialMapColor());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockBasePressurePlate(Material p_i46401_1_, MapColor p_i46401_2_) {
/*  24 */     super(p_i46401_1_, p_i46401_2_);
/*  25 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  26 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  31 */     setBlockBoundsBasedOnState0(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setBlockBoundsBasedOnState0(IBlockState state) {
/*  36 */     boolean flag = (getRedstoneStrength(state) > 0);
/*  37 */     float f = 0.0625F;
/*     */     
/*  39 */     if (flag) {
/*     */       
/*  41 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);
/*     */     }
/*     */     else {
/*     */       
/*  45 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  51 */     return 20;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSpawnInBlock() {
/*  76 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  81 */     return canBePlacedOn(worldIn, pos.down());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  86 */     if (!canBePlacedOn(worldIn, pos.down())) {
/*     */       
/*  88 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  89 */       worldIn.setBlockToAir(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canBePlacedOn(World worldIn, BlockPos pos) {
/*  95 */     return (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos) || worldIn.getBlockState(pos).getBlock() instanceof BlockFence);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 104 */     if (!worldIn.isRemote) {
/*     */       
/* 106 */       int i = getRedstoneStrength(state);
/*     */       
/* 108 */       if (i > 0)
/*     */       {
/* 110 */         updateState(worldIn, pos, state, i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
/* 117 */     if (!worldIn.isRemote) {
/*     */       
/* 119 */       int i = getRedstoneStrength(state);
/*     */       
/* 121 */       if (i == 0)
/*     */       {
/* 123 */         updateState(worldIn, pos, state, i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
/* 130 */     int i = computeRedstoneStrength(worldIn, pos);
/* 131 */     boolean flag = (oldRedstoneStrength > 0);
/* 132 */     boolean flag1 = (i > 0);
/*     */     
/* 134 */     if (oldRedstoneStrength != i) {
/*     */       
/* 136 */       state = setRedstoneStrength(state, i);
/* 137 */       worldIn.setBlockState(pos, state, 2);
/* 138 */       updateNeighbors(worldIn, pos);
/* 139 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */     } 
/*     */     
/* 142 */     if (!flag1 && flag) {
/*     */       
/* 144 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
/*     */     }
/* 146 */     else if (flag1 && !flag) {
/*     */       
/* 148 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
/*     */     } 
/*     */     
/* 151 */     if (flag1)
/*     */     {
/* 153 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected AxisAlignedBB getSensitiveAABB(BlockPos pos) {
/* 159 */     float f = 0.125F;
/* 160 */     return new AxisAlignedBB((pos.getX() + 0.125F), pos.getY(), (pos.getZ() + 0.125F), ((pos.getX() + 1) - 0.125F), pos.getY() + 0.25D, ((pos.getZ() + 1) - 0.125F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 165 */     if (getRedstoneStrength(state) > 0)
/*     */     {
/* 167 */       updateNeighbors(worldIn, pos);
/*     */     }
/*     */     
/* 170 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateNeighbors(World worldIn, BlockPos pos) {
/* 175 */     worldIn.notifyNeighborsOfStateChange(pos, this);
/* 176 */     worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 181 */     return getRedstoneStrength(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 186 */     return (side == EnumFacing.UP) ? getRedstoneStrength(state) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/* 196 */     float f = 0.5F;
/* 197 */     float f1 = 0.125F;
/* 198 */     float f2 = 0.5F;
/* 199 */     setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMobilityFlag() {
/* 204 */     return 1;
/*     */   }
/*     */   
/*     */   protected abstract int computeRedstoneStrength(World paramWorld, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract int getRedstoneStrength(IBlockState paramIBlockState);
/*     */   
/*     */   protected abstract IBlockState setRedstoneStrength(IBlockState paramIBlockState, int paramInt);
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBasePressurePlate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */