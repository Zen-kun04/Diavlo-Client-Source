/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.inventory.InventoryLargeChest;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockChest extends BlockContainer {
/*  31 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*     */   
/*     */   public final int chestType;
/*     */   
/*     */   protected BlockChest(int type) {
/*  36 */     super(Material.wood);
/*  37 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
/*  38 */     this.chestType = type;
/*  39 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  40 */     setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  45 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/*  55 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  60 */     if (worldIn.getBlockState(pos.north()).getBlock() == this) {
/*     */       
/*  62 */       setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
/*     */     }
/*  64 */     else if (worldIn.getBlockState(pos.south()).getBlock() == this) {
/*     */       
/*  66 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
/*     */     }
/*  68 */     else if (worldIn.getBlockState(pos.west()).getBlock() == this) {
/*     */       
/*  70 */       setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
/*     */     }
/*  72 */     else if (worldIn.getBlockState(pos.east()).getBlock() == this) {
/*     */       
/*  74 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
/*     */     }
/*     */     else {
/*     */       
/*  78 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  84 */     checkForSurroundingChests(worldIn, pos, state);
/*     */     
/*  86 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/*  88 */       BlockPos blockpos = pos.offset(enumfacing);
/*  89 */       IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */       
/*  91 */       if (iblockstate.getBlock() == this)
/*     */       {
/*  93 */         checkForSurroundingChests(worldIn, blockpos, iblockstate);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 100 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 105 */     EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3).getOpposite();
/* 106 */     state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
/* 107 */     BlockPos blockpos = pos.north();
/* 108 */     BlockPos blockpos1 = pos.south();
/* 109 */     BlockPos blockpos2 = pos.west();
/* 110 */     BlockPos blockpos3 = pos.east();
/* 111 */     boolean flag = (this == worldIn.getBlockState(blockpos).getBlock());
/* 112 */     boolean flag1 = (this == worldIn.getBlockState(blockpos1).getBlock());
/* 113 */     boolean flag2 = (this == worldIn.getBlockState(blockpos2).getBlock());
/* 114 */     boolean flag3 = (this == worldIn.getBlockState(blockpos3).getBlock());
/*     */     
/* 116 */     if (!flag && !flag1 && !flag2 && !flag3) {
/*     */       
/* 118 */       worldIn.setBlockState(pos, state, 3);
/*     */     }
/* 120 */     else if (enumfacing.getAxis() != EnumFacing.Axis.X || (!flag && !flag1)) {
/*     */       
/* 122 */       if (enumfacing.getAxis() == EnumFacing.Axis.Z && (flag2 || flag3))
/*     */       {
/* 124 */         if (flag2) {
/*     */           
/* 126 */           worldIn.setBlockState(blockpos2, state, 3);
/*     */         }
/*     */         else {
/*     */           
/* 130 */           worldIn.setBlockState(blockpos3, state, 3);
/*     */         } 
/*     */         
/* 133 */         worldIn.setBlockState(pos, state, 3);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 138 */       if (flag) {
/*     */         
/* 140 */         worldIn.setBlockState(blockpos, state, 3);
/*     */       }
/*     */       else {
/*     */         
/* 144 */         worldIn.setBlockState(blockpos1, state, 3);
/*     */       } 
/*     */       
/* 147 */       worldIn.setBlockState(pos, state, 3);
/*     */     } 
/*     */     
/* 150 */     if (stack.hasDisplayName()) {
/*     */       
/* 152 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 154 */       if (tileentity instanceof TileEntityChest)
/*     */       {
/* 156 */         ((TileEntityChest)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState checkForSurroundingChests(World worldIn, BlockPos pos, IBlockState state) {
/* 163 */     if (worldIn.isRemote)
/*     */     {
/* 165 */       return state;
/*     */     }
/*     */ 
/*     */     
/* 169 */     IBlockState iblockstate = worldIn.getBlockState(pos.north());
/* 170 */     IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
/* 171 */     IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
/* 172 */     IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
/* 173 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/* 174 */     Block block = iblockstate.getBlock();
/* 175 */     Block block1 = iblockstate1.getBlock();
/* 176 */     Block block2 = iblockstate2.getBlock();
/* 177 */     Block block3 = iblockstate3.getBlock();
/*     */     
/* 179 */     if (block != this && block1 != this) {
/*     */       
/* 181 */       boolean flag = block.isFullBlock();
/* 182 */       boolean flag1 = block1.isFullBlock();
/*     */       
/* 184 */       if (block2 == this || block3 == this) {
/*     */         EnumFacing enumfacing2;
/* 186 */         BlockPos blockpos1 = (block2 == this) ? pos.west() : pos.east();
/* 187 */         IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.north());
/* 188 */         IBlockState iblockstate7 = worldIn.getBlockState(blockpos1.south());
/* 189 */         enumfacing = EnumFacing.SOUTH;
/*     */ 
/*     */         
/* 192 */         if (block2 == this) {
/*     */           
/* 194 */           enumfacing2 = (EnumFacing)iblockstate2.getValue((IProperty)FACING);
/*     */         }
/*     */         else {
/*     */           
/* 198 */           enumfacing2 = (EnumFacing)iblockstate3.getValue((IProperty)FACING);
/*     */         } 
/*     */         
/* 201 */         if (enumfacing2 == EnumFacing.NORTH)
/*     */         {
/* 203 */           enumfacing = EnumFacing.NORTH;
/*     */         }
/*     */         
/* 206 */         Block block6 = iblockstate6.getBlock();
/* 207 */         Block block7 = iblockstate7.getBlock();
/*     */         
/* 209 */         if ((flag || block6.isFullBlock()) && !flag1 && !block7.isFullBlock())
/*     */         {
/* 211 */           enumfacing = EnumFacing.SOUTH;
/*     */         }
/*     */         
/* 214 */         if ((flag1 || block7.isFullBlock()) && !flag && !block6.isFullBlock())
/*     */         {
/* 216 */           enumfacing = EnumFacing.NORTH;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       EnumFacing enumfacing1;
/*     */       
/* 222 */       BlockPos blockpos = (block == this) ? pos.north() : pos.south();
/* 223 */       IBlockState iblockstate4 = worldIn.getBlockState(blockpos.west());
/* 224 */       IBlockState iblockstate5 = worldIn.getBlockState(blockpos.east());
/* 225 */       enumfacing = EnumFacing.EAST;
/*     */ 
/*     */       
/* 228 */       if (block == this) {
/*     */         
/* 230 */         enumfacing1 = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/*     */       }
/*     */       else {
/*     */         
/* 234 */         enumfacing1 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
/*     */       } 
/*     */       
/* 237 */       if (enumfacing1 == EnumFacing.WEST)
/*     */       {
/* 239 */         enumfacing = EnumFacing.WEST;
/*     */       }
/*     */       
/* 242 */       Block block4 = iblockstate4.getBlock();
/* 243 */       Block block5 = iblockstate5.getBlock();
/*     */       
/* 245 */       if ((block2.isFullBlock() || block4.isFullBlock()) && !block3.isFullBlock() && !block5.isFullBlock())
/*     */       {
/* 247 */         enumfacing = EnumFacing.EAST;
/*     */       }
/*     */       
/* 250 */       if ((block3.isFullBlock() || block5.isFullBlock()) && !block2.isFullBlock() && !block4.isFullBlock())
/*     */       {
/* 252 */         enumfacing = EnumFacing.WEST;
/*     */       }
/*     */     } 
/*     */     
/* 256 */     state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
/* 257 */     worldIn.setBlockState(pos, state, 3);
/* 258 */     return state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state) {
/* 264 */     EnumFacing enumfacing = null;
/*     */     
/* 266 */     for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 268 */       IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing1));
/*     */       
/* 270 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 272 */         return state;
/*     */       }
/*     */       
/* 275 */       if (iblockstate.getBlock().isFullBlock()) {
/*     */         
/* 277 */         if (enumfacing != null) {
/*     */           
/* 279 */           enumfacing = null;
/*     */           
/*     */           break;
/*     */         } 
/* 283 */         enumfacing = enumfacing1;
/*     */       } 
/*     */     } 
/*     */     
/* 287 */     if (enumfacing != null)
/*     */     {
/* 289 */       return state.withProperty((IProperty)FACING, (Comparable)enumfacing.getOpposite());
/*     */     }
/*     */ 
/*     */     
/* 293 */     EnumFacing enumfacing2 = (EnumFacing)state.getValue((IProperty)FACING);
/*     */     
/* 295 */     if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
/*     */     {
/* 297 */       enumfacing2 = enumfacing2.getOpposite();
/*     */     }
/*     */     
/* 300 */     if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
/*     */     {
/* 302 */       enumfacing2 = enumfacing2.rotateY();
/*     */     }
/*     */     
/* 305 */     if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
/*     */     {
/* 307 */       enumfacing2 = enumfacing2.getOpposite();
/*     */     }
/*     */     
/* 310 */     return state.withProperty((IProperty)FACING, (Comparable)enumfacing2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 316 */     int i = 0;
/* 317 */     BlockPos blockpos = pos.west();
/* 318 */     BlockPos blockpos1 = pos.east();
/* 319 */     BlockPos blockpos2 = pos.north();
/* 320 */     BlockPos blockpos3 = pos.south();
/*     */     
/* 322 */     if (worldIn.getBlockState(blockpos).getBlock() == this) {
/*     */       
/* 324 */       if (isDoubleChest(worldIn, blockpos))
/*     */       {
/* 326 */         return false;
/*     */       }
/*     */       
/* 329 */       i++;
/*     */     } 
/*     */     
/* 332 */     if (worldIn.getBlockState(blockpos1).getBlock() == this) {
/*     */       
/* 334 */       if (isDoubleChest(worldIn, blockpos1))
/*     */       {
/* 336 */         return false;
/*     */       }
/*     */       
/* 339 */       i++;
/*     */     } 
/*     */     
/* 342 */     if (worldIn.getBlockState(blockpos2).getBlock() == this) {
/*     */       
/* 344 */       if (isDoubleChest(worldIn, blockpos2))
/*     */       {
/* 346 */         return false;
/*     */       }
/*     */       
/* 349 */       i++;
/*     */     } 
/*     */     
/* 352 */     if (worldIn.getBlockState(blockpos3).getBlock() == this) {
/*     */       
/* 354 */       if (isDoubleChest(worldIn, blockpos3))
/*     */       {
/* 356 */         return false;
/*     */       }
/*     */       
/* 359 */       i++;
/*     */     } 
/*     */     
/* 362 */     return (i <= 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isDoubleChest(World worldIn, BlockPos pos) {
/* 367 */     if (worldIn.getBlockState(pos).getBlock() != this)
/*     */     {
/* 369 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 373 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 375 */       if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this)
/*     */       {
/* 377 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 381 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 387 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/* 388 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 390 */     if (tileentity instanceof TileEntityChest)
/*     */     {
/* 392 */       tileentity.updateContainingBlockInfo();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 398 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 400 */     if (tileentity instanceof IInventory) {
/*     */       
/* 402 */       InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
/* 403 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     } 
/*     */     
/* 406 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 411 */     if (worldIn.isRemote)
/*     */     {
/* 413 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 417 */     ILockableContainer ilockablecontainer = getLockableContainer(worldIn, pos);
/*     */     
/* 419 */     if (ilockablecontainer != null) {
/*     */       
/* 421 */       playerIn.displayGUIChest((IInventory)ilockablecontainer);
/*     */       
/* 423 */       if (this.chestType == 0) {
/*     */         
/* 425 */         playerIn.triggerAchievement(StatList.field_181723_aa);
/*     */       }
/* 427 */       else if (this.chestType == 1) {
/*     */         
/* 429 */         playerIn.triggerAchievement(StatList.field_181737_U);
/*     */       } 
/*     */     } 
/*     */     
/* 433 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ILockableContainer getLockableContainer(World worldIn, BlockPos pos) {
/*     */     InventoryLargeChest inventoryLargeChest;
/* 439 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 441 */     if (!(tileentity instanceof TileEntityChest))
/*     */     {
/* 443 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 447 */     TileEntityChest tileEntityChest = (TileEntityChest)tileentity;
/*     */     
/* 449 */     if (isBlocked(worldIn, pos))
/*     */     {
/* 451 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 455 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 457 */       BlockPos blockpos = pos.offset(enumfacing);
/* 458 */       Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */       
/* 460 */       if (block == this) {
/*     */         
/* 462 */         if (isBlocked(worldIn, blockpos))
/*     */         {
/* 464 */           return null;
/*     */         }
/*     */         
/* 467 */         TileEntity tileentity1 = worldIn.getTileEntity(blockpos);
/*     */         
/* 469 */         if (tileentity1 instanceof TileEntityChest) {
/*     */           
/* 471 */           if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH) {
/*     */             
/* 473 */             inventoryLargeChest = new InventoryLargeChest("container.chestDouble", (ILockableContainer)tileEntityChest, (ILockableContainer)tileentity1);
/*     */             
/*     */             continue;
/*     */           } 
/* 477 */           inventoryLargeChest = new InventoryLargeChest("container.chestDouble", (ILockableContainer)tileentity1, (ILockableContainer)inventoryLargeChest);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 483 */     return (ILockableContainer)inventoryLargeChest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 490 */     return (TileEntity)new TileEntityChest();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 495 */     return (this.chestType == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 500 */     if (!canProvidePower())
/*     */     {
/* 502 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 506 */     int i = 0;
/* 507 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 509 */     if (tileentity instanceof TileEntityChest)
/*     */     {
/* 511 */       i = ((TileEntityChest)tileentity).numPlayersUsing;
/*     */     }
/*     */     
/* 514 */     return MathHelper.clamp_int(i, 0, 15);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 520 */     return (side == EnumFacing.UP) ? getWeakPower(worldIn, pos, state, side) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isBlocked(World worldIn, BlockPos pos) {
/* 525 */     return (isBelowSolidBlock(worldIn, pos) || isOcelotSittingOnChest(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
/* 530 */     return worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
/* 535 */     for (Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), (pos.getY() + 1), pos.getZ(), (pos.getX() + 1), (pos.getY() + 2), (pos.getZ() + 1)))) {
/*     */       
/* 537 */       EntityOcelot entityocelot = (EntityOcelot)entity;
/*     */       
/* 539 */       if (entityocelot.isSitting())
/*     */       {
/* 541 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 545 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/* 550 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/* 555 */     return Container.calcRedstoneFromInventory((IInventory)getLockableContainer(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 560 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */     
/* 562 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */     {
/* 564 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 567 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 572 */     return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 577 */     return new BlockState(this, new IProperty[] { (IProperty)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */