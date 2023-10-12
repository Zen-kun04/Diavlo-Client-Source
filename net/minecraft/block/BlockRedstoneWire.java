/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneWire
/*     */   extends Block {
/*  30 */   public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
/*  31 */   public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create("east", EnumAttachPosition.class);
/*  32 */   public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
/*  33 */   public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create("west", EnumAttachPosition.class);
/*  34 */   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
/*     */   private boolean canProvidePower = true;
/*  36 */   private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
/*     */ 
/*     */   
/*     */   public BlockRedstoneWire() {
/*  40 */     super(Material.circuits);
/*  41 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, EnumAttachPosition.NONE).withProperty((IProperty)EAST, EnumAttachPosition.NONE).withProperty((IProperty)SOUTH, EnumAttachPosition.NONE).withProperty((IProperty)WEST, EnumAttachPosition.NONE).withProperty((IProperty)POWER, Integer.valueOf(0)));
/*  42 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  47 */     state = state.withProperty((IProperty)WEST, getAttachPosition(worldIn, pos, EnumFacing.WEST));
/*  48 */     state = state.withProperty((IProperty)EAST, getAttachPosition(worldIn, pos, EnumFacing.EAST));
/*  49 */     state = state.withProperty((IProperty)NORTH, getAttachPosition(worldIn, pos, EnumFacing.NORTH));
/*  50 */     state = state.withProperty((IProperty)SOUTH, getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
/*  51 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction) {
/*  56 */     BlockPos blockpos = pos.offset(direction);
/*  57 */     Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
/*     */     
/*  59 */     if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (block.isBlockNormalCube() || !canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))) {
/*     */       
/*  61 */       Block block1 = worldIn.getBlockState(pos.up()).getBlock();
/*  62 */       return (!block1.isBlockNormalCube() && block.isBlockNormalCube() && canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return EnumAttachPosition.SIDE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  72 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/*  87 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  88 */     return (iblockstate.getBlock() != this) ? super.colorMultiplier(worldIn, pos, renderPass) : colorMultiplier(((Integer)iblockstate.getValue((IProperty)POWER)).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  93 */     return (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone);
/*     */   }
/*     */ 
/*     */   
/*     */   private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state) {
/*  98 */     state = calculateCurrentChanges(worldIn, pos, pos, state);
/*  99 */     List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
/* 100 */     this.blocksNeedingUpdate.clear();
/*     */     
/* 102 */     for (BlockPos blockpos : list)
/*     */     {
/* 104 */       worldIn.notifyNeighborsOfStateChange(blockpos, this);
/*     */     }
/*     */     
/* 107 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state) {
/* 112 */     IBlockState iblockstate = state;
/* 113 */     int i = ((Integer)state.getValue((IProperty)POWER)).intValue();
/* 114 */     int j = 0;
/* 115 */     j = getMaxCurrentStrength(worldIn, pos2, j);
/* 116 */     this.canProvidePower = false;
/* 117 */     int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
/* 118 */     this.canProvidePower = true;
/*     */     
/* 120 */     if (k > 0 && k > j - 1)
/*     */     {
/* 122 */       j = k;
/*     */     }
/*     */     
/* 125 */     int l = 0;
/*     */     
/* 127 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 129 */       BlockPos blockpos = pos1.offset(enumfacing);
/* 130 */       boolean flag = (blockpos.getX() != pos2.getX() || blockpos.getZ() != pos2.getZ());
/*     */       
/* 132 */       if (flag)
/*     */       {
/* 134 */         l = getMaxCurrentStrength(worldIn, blockpos, l);
/*     */       }
/*     */       
/* 137 */       if (worldIn.getBlockState(blockpos).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()) {
/*     */         
/* 139 */         if (flag && pos1.getY() >= pos2.getY())
/*     */         {
/* 141 */           l = getMaxCurrentStrength(worldIn, blockpos.up(), l); } 
/*     */         continue;
/*     */       } 
/* 144 */       if (!worldIn.getBlockState(blockpos).getBlock().isNormalCube() && flag && pos1.getY() <= pos2.getY())
/*     */       {
/* 146 */         l = getMaxCurrentStrength(worldIn, blockpos.down(), l);
/*     */       }
/*     */     } 
/*     */     
/* 150 */     if (l > j) {
/*     */       
/* 152 */       j = l - 1;
/*     */     }
/* 154 */     else if (j > 0) {
/*     */       
/* 156 */       j--;
/*     */     }
/*     */     else {
/*     */       
/* 160 */       j = 0;
/*     */     } 
/*     */     
/* 163 */     if (k > j - 1)
/*     */     {
/* 165 */       j = k;
/*     */     }
/*     */     
/* 168 */     if (i != j) {
/*     */       
/* 170 */       state = state.withProperty((IProperty)POWER, Integer.valueOf(j));
/*     */       
/* 172 */       if (worldIn.getBlockState(pos1) == iblockstate)
/*     */       {
/* 174 */         worldIn.setBlockState(pos1, state, 2);
/*     */       }
/*     */       
/* 177 */       this.blocksNeedingUpdate.add(pos1);
/*     */       
/* 179 */       for (EnumFacing enumfacing1 : EnumFacing.values())
/*     */       {
/* 181 */         this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
/*     */       }
/*     */     } 
/*     */     
/* 185 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
/* 190 */     if (worldIn.getBlockState(pos).getBlock() == this) {
/*     */       
/* 192 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/*     */       
/* 194 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/* 196 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 203 */     if (!worldIn.isRemote) {
/*     */       
/* 205 */       updateSurroundingRedstone(worldIn, pos, state);
/*     */       
/* 207 */       for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
/*     */       {
/* 209 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */       
/* 212 */       for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 214 */         notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
/*     */       }
/*     */       
/* 217 */       for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
/*     */         
/* 219 */         BlockPos blockpos = pos.offset(enumfacing2);
/*     */         
/* 221 */         if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
/*     */           
/* 223 */           notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
/*     */           
/*     */           continue;
/*     */         } 
/* 227 */         notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 235 */     super.breakBlock(worldIn, pos, state);
/*     */     
/* 237 */     if (!worldIn.isRemote) {
/*     */       
/* 239 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/* 241 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */       
/* 244 */       updateSurroundingRedstone(worldIn, pos, state);
/*     */       
/* 246 */       for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 248 */         notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
/*     */       }
/*     */       
/* 251 */       for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
/*     */         
/* 253 */         BlockPos blockpos = pos.offset(enumfacing2);
/*     */         
/* 255 */         if (worldIn.getBlockState(blockpos).getBlock().isNormalCube()) {
/*     */           
/* 257 */           notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
/*     */           
/*     */           continue;
/*     */         } 
/* 261 */         notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength) {
/* 269 */     if (worldIn.getBlockState(pos).getBlock() != this)
/*     */     {
/* 271 */       return strength;
/*     */     }
/*     */ 
/*     */     
/* 275 */     int i = ((Integer)worldIn.getBlockState(pos).getValue((IProperty)POWER)).intValue();
/* 276 */     return (i > strength) ? i : strength;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 282 */     if (!worldIn.isRemote)
/*     */     {
/* 284 */       if (canPlaceBlockAt(worldIn, pos)) {
/*     */         
/* 286 */         updateSurroundingRedstone(worldIn, pos, state);
/*     */       }
/*     */       else {
/*     */         
/* 290 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 291 */         worldIn.setBlockToAir(pos);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 298 */     return Items.redstone;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 303 */     return !this.canProvidePower ? 0 : getWeakPower(worldIn, pos, state, side);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 308 */     if (!this.canProvidePower)
/*     */     {
/* 310 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 314 */     int i = ((Integer)state.getValue((IProperty)POWER)).intValue();
/*     */     
/* 316 */     if (i == 0)
/*     */     {
/* 318 */       return 0;
/*     */     }
/* 320 */     if (side == EnumFacing.UP)
/*     */     {
/* 322 */       return i;
/*     */     }
/*     */ 
/*     */     
/* 326 */     EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);
/*     */     
/* 328 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 330 */       if (func_176339_d(worldIn, pos, enumfacing))
/*     */       {
/* 332 */         enumset.add(enumfacing);
/*     */       }
/*     */     } 
/*     */     
/* 336 */     if (side.getAxis().isHorizontal() && enumset.isEmpty())
/*     */     {
/* 338 */       return i;
/*     */     }
/* 340 */     if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))
/*     */     {
/* 342 */       return i;
/*     */     }
/*     */ 
/*     */     
/* 346 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/* 354 */     BlockPos blockpos = pos.offset(side);
/* 355 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 356 */     Block block = iblockstate.getBlock();
/* 357 */     boolean flag = block.isNormalCube();
/* 358 */     boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
/* 359 */     return (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up())) ? true : (canConnectTo(iblockstate, side) ? true : ((block == Blocks.powered_repeater && iblockstate.getValue((IProperty)BlockRedstoneDiode.FACING) == side) ? true : ((!flag && canConnectUpwardsTo(worldIn, blockpos.down())))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos) {
/* 364 */     return canConnectUpwardsTo(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean canConnectUpwardsTo(IBlockState state) {
/* 369 */     return canConnectTo(state, (EnumFacing)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean canConnectTo(IBlockState blockState, EnumFacing side) {
/* 374 */     Block block = blockState.getBlock();
/*     */     
/* 376 */     if (block == Blocks.redstone_wire)
/*     */     {
/* 378 */       return true;
/*     */     }
/* 380 */     if (Blocks.unpowered_repeater.isAssociated(block)) {
/*     */       
/* 382 */       EnumFacing enumfacing = (EnumFacing)blockState.getValue((IProperty)BlockRedstoneRepeater.FACING);
/* 383 */       return (enumfacing == side || enumfacing.getOpposite() == side);
/*     */     } 
/*     */ 
/*     */     
/* 387 */     return (block.canProvidePower() && side != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 393 */     return this.canProvidePower;
/*     */   }
/*     */ 
/*     */   
/*     */   private int colorMultiplier(int powerLevel) {
/* 398 */     float f = powerLevel / 15.0F;
/* 399 */     float f1 = f * 0.6F + 0.4F;
/*     */     
/* 401 */     if (powerLevel == 0)
/*     */     {
/* 403 */       f1 = 0.3F;
/*     */     }
/*     */     
/* 406 */     float f2 = f * f * 0.7F - 0.5F;
/* 407 */     float f3 = f * f * 0.6F - 0.7F;
/*     */     
/* 409 */     if (f2 < 0.0F)
/*     */     {
/* 411 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 414 */     if (f3 < 0.0F)
/*     */     {
/* 416 */       f3 = 0.0F;
/*     */     }
/*     */     
/* 419 */     int i = MathHelper.clamp_int((int)(f1 * 255.0F), 0, 255);
/* 420 */     int j = MathHelper.clamp_int((int)(f2 * 255.0F), 0, 255);
/* 421 */     int k = MathHelper.clamp_int((int)(f3 * 255.0F), 0, 255);
/* 422 */     return 0xFF000000 | i << 16 | j << 8 | k;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 427 */     int i = ((Integer)state.getValue((IProperty)POWER)).intValue();
/*     */     
/* 429 */     if (i != 0) {
/*     */       
/* 431 */       double d0 = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
/* 432 */       double d1 = (pos.getY() + 0.0625F);
/* 433 */       double d2 = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
/* 434 */       float f = i / 15.0F;
/* 435 */       float f1 = f * 0.6F + 0.4F;
/* 436 */       float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
/* 437 */       float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
/* 438 */       worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, f1, f2, f3, new int[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 444 */     return Items.redstone;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 449 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 454 */     return getDefaultState().withProperty((IProperty)POWER, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 459 */     return ((Integer)state.getValue((IProperty)POWER)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 464 */     return new BlockState(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST, (IProperty)POWER });
/*     */   }
/*     */   
/*     */   enum EnumAttachPosition
/*     */     implements IStringSerializable {
/* 469 */     UP("up"),
/* 470 */     SIDE("side"),
/* 471 */     NONE("none");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     EnumAttachPosition(String name) {
/* 477 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 482 */       return getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 487 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedstoneWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */