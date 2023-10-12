/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockPistonStructureHelper;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityPiston;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPistonBase
/*     */   extends Block {
/*  27 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  28 */   public static final PropertyBool EXTENDED = PropertyBool.create("extended");
/*     */   
/*     */   private final boolean isSticky;
/*     */   
/*     */   public BlockPistonBase(boolean isSticky) {
/*  33 */     super(Material.piston);
/*  34 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)EXTENDED, Boolean.valueOf(false)));
/*  35 */     this.isSticky = isSticky;
/*  36 */     setStepSound(soundTypePiston);
/*  37 */     setHardness(0.5F);
/*  38 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  43 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/*  48 */     worldIn.setBlockState(pos, state.withProperty((IProperty)FACING, (Comparable)getFacingFromEntity(worldIn, pos, placer)), 2);
/*     */     
/*  50 */     if (!worldIn.isRemote)
/*     */     {
/*  52 */       checkForMove(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  58 */     if (!worldIn.isRemote)
/*     */     {
/*  60 */       checkForMove(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  66 */     if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null)
/*     */     {
/*  68 */       checkForMove(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/*  74 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacingFromEntity(worldIn, pos, placer)).withProperty((IProperty)EXTENDED, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkForMove(World worldIn, BlockPos pos, IBlockState state) {
/*  79 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*  80 */     boolean flag = shouldBeExtended(worldIn, pos, enumfacing);
/*     */     
/*  82 */     if (flag && !((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue()) {
/*     */       
/*  84 */       if ((new BlockPistonStructureHelper(worldIn, pos, enumfacing, true)).canMove())
/*     */       {
/*  86 */         worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
/*     */       }
/*     */     }
/*  89 */     else if (!flag && ((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue()) {
/*     */       
/*  91 */       worldIn.setBlockState(pos, state.withProperty((IProperty)EXTENDED, Boolean.valueOf(false)), 2);
/*  92 */       worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing) {
/*  98 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */       
/* 100 */       if (enumfacing != facing && worldIn.isSidePowered(pos.offset(enumfacing), enumfacing))
/*     */       {
/* 102 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 106 */     if (worldIn.isSidePowered(pos, EnumFacing.DOWN))
/*     */     {
/* 108 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 112 */     BlockPos blockpos = pos.up();
/*     */     
/* 114 */     for (EnumFacing enumfacing1 : EnumFacing.values()) {
/*     */       
/* 116 */       if (enumfacing1 != EnumFacing.DOWN && worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1))
/*     */       {
/* 118 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam) {
/* 128 */     EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */     
/* 130 */     if (!worldIn.isRemote) {
/*     */       
/* 132 */       boolean flag = shouldBeExtended(worldIn, pos, enumfacing);
/*     */       
/* 134 */       if (flag && eventID == 1) {
/*     */         
/* 136 */         worldIn.setBlockState(pos, state.withProperty((IProperty)EXTENDED, Boolean.valueOf(true)), 2);
/* 137 */         return false;
/*     */       } 
/*     */       
/* 140 */       if (!flag && eventID == 0)
/*     */       {
/* 142 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 146 */     if (eventID == 0) {
/*     */       
/* 148 */       if (!doMove(worldIn, pos, enumfacing, true))
/*     */       {
/* 150 */         return false;
/*     */       }
/*     */       
/* 153 */       worldIn.setBlockState(pos, state.withProperty((IProperty)EXTENDED, Boolean.valueOf(true)), 2);
/* 154 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
/*     */     }
/* 156 */     else if (eventID == 1) {
/*     */       
/* 158 */       TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
/*     */       
/* 160 */       if (tileentity1 instanceof TileEntityPiston)
/*     */       {
/* 162 */         ((TileEntityPiston)tileentity1).clearPistonTileEntity();
/*     */       }
/*     */       
/* 165 */       worldIn.setBlockState(pos, Blocks.piston_extension.getDefaultState().withProperty((IProperty)BlockPistonMoving.FACING, (Comparable)enumfacing).withProperty((IProperty)BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
/* 166 */       worldIn.setTileEntity(pos, BlockPistonMoving.newTileEntity(getStateFromMeta(eventParam), enumfacing, false, true));
/*     */       
/* 168 */       if (this.isSticky) {
/*     */         
/* 170 */         BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
/* 171 */         Block block = worldIn.getBlockState(blockpos).getBlock();
/* 172 */         boolean flag1 = false;
/*     */         
/* 174 */         if (block == Blocks.piston_extension) {
/*     */           
/* 176 */           TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */           
/* 178 */           if (tileentity instanceof TileEntityPiston) {
/*     */             
/* 180 */             TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity;
/*     */             
/* 182 */             if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
/*     */               
/* 184 */               tileentitypiston.clearPistonTileEntity();
/* 185 */               flag1 = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 190 */         if (!flag1 && block.getMaterial() != Material.air && canPush(block, worldIn, blockpos, enumfacing.getOpposite(), false) && (block.getMobilityFlag() == 0 || block == Blocks.piston || block == Blocks.sticky_piston))
/*     */         {
/* 192 */           doMove(worldIn, pos, enumfacing, false);
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 197 */         worldIn.setBlockToAir(pos.offset(enumfacing));
/*     */       } 
/*     */       
/* 200 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.in", 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
/*     */     } 
/*     */     
/* 203 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/* 208 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/* 210 */     if (iblockstate.getBlock() == this && ((Boolean)iblockstate.getValue((IProperty)EXTENDED)).booleanValue()) {
/*     */       
/* 212 */       float f = 0.25F;
/* 213 */       EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/*     */       
/* 215 */       if (enumfacing != null)
/*     */       {
/* 217 */         switch (enumfacing) {
/*     */           
/*     */           case DOWN:
/* 220 */             setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */             break;
/*     */           
/*     */           case UP:
/* 224 */             setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
/*     */             break;
/*     */           
/*     */           case NORTH:
/* 228 */             setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
/*     */             break;
/*     */           
/*     */           case SOUTH:
/* 232 */             setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
/*     */             break;
/*     */           
/*     */           case WEST:
/* 236 */             setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */             break;
/*     */           
/*     */           case EAST:
/* 240 */             setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
/*     */             break;
/*     */         } 
/*     */       
/*     */       }
/*     */     } else {
/* 246 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsForItemRender() {
/* 252 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/* 257 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 258 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 263 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/* 264 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/* 269 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacing(int meta) {
/* 274 */     int i = meta & 0x7;
/* 275 */     return (i > 5) ? null : EnumFacing.getFront(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn) {
/* 280 */     if (MathHelper.abs((float)entityIn.posX - clickedBlock.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - clickedBlock.getZ()) < 2.0F) {
/*     */       
/* 282 */       double d0 = entityIn.posY + entityIn.getEyeHeight();
/*     */       
/* 284 */       if (d0 - clickedBlock.getY() > 2.0D)
/*     */       {
/* 286 */         return EnumFacing.UP;
/*     */       }
/*     */       
/* 289 */       if (clickedBlock.getY() - d0 > 0.0D)
/*     */       {
/* 291 */         return EnumFacing.DOWN;
/*     */       }
/*     */     } 
/*     */     
/* 295 */     return entityIn.getHorizontalFacing().getOpposite();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canPush(Block blockIn, World worldIn, BlockPos pos, EnumFacing direction, boolean allowDestroy) {
/* 300 */     if (blockIn == Blocks.obsidian)
/*     */     {
/* 302 */       return false;
/*     */     }
/* 304 */     if (!worldIn.getWorldBorder().contains(pos))
/*     */     {
/* 306 */       return false;
/*     */     }
/* 308 */     if (pos.getY() >= 0 && (direction != EnumFacing.DOWN || pos.getY() != 0)) {
/*     */       
/* 310 */       if (pos.getY() <= worldIn.getHeight() - 1 && (direction != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1)) {
/*     */         
/* 312 */         if (blockIn != Blocks.piston && blockIn != Blocks.sticky_piston) {
/*     */           
/* 314 */           if (blockIn.getBlockHardness(worldIn, pos) == -1.0F)
/*     */           {
/* 316 */             return false;
/*     */           }
/*     */           
/* 319 */           if (blockIn.getMobilityFlag() == 2)
/*     */           {
/* 321 */             return false;
/*     */           }
/*     */           
/* 324 */           if (blockIn.getMobilityFlag() == 1)
/*     */           {
/* 326 */             if (!allowDestroy)
/*     */             {
/* 328 */               return false;
/*     */             }
/*     */             
/* 331 */             return true;
/*     */           }
/*     */         
/* 334 */         } else if (((Boolean)worldIn.getBlockState(pos).getValue((IProperty)EXTENDED)).booleanValue()) {
/*     */           
/* 336 */           return false;
/*     */         } 
/*     */         
/* 339 */         return !(blockIn instanceof ITileEntityProvider);
/*     */       } 
/*     */ 
/*     */       
/* 343 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 348 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending) {
/* 354 */     if (!extending)
/*     */     {
/* 356 */       worldIn.setBlockToAir(pos.offset(direction));
/*     */     }
/*     */     
/* 359 */     BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
/* 360 */     List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
/* 361 */     List<BlockPos> list1 = blockpistonstructurehelper.getBlocksToDestroy();
/*     */     
/* 363 */     if (!blockpistonstructurehelper.canMove())
/*     */     {
/* 365 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 369 */     int i = list.size() + list1.size();
/* 370 */     Block[] ablock = new Block[i];
/* 371 */     EnumFacing enumfacing = extending ? direction : direction.getOpposite();
/*     */     
/* 373 */     for (int j = list1.size() - 1; j >= 0; j--) {
/*     */       
/* 375 */       BlockPos blockpos = list1.get(j);
/* 376 */       Block block = worldIn.getBlockState(blockpos).getBlock();
/* 377 */       block.dropBlockAsItem(worldIn, blockpos, worldIn.getBlockState(blockpos), 0);
/* 378 */       worldIn.setBlockToAir(blockpos);
/* 379 */       i--;
/* 380 */       ablock[i] = block;
/*     */     } 
/*     */     
/* 383 */     for (int k = list.size() - 1; k >= 0; k--) {
/*     */       
/* 385 */       BlockPos blockpos2 = list.get(k);
/* 386 */       IBlockState iblockstate = worldIn.getBlockState(blockpos2);
/* 387 */       Block block1 = iblockstate.getBlock();
/* 388 */       block1.getMetaFromState(iblockstate);
/* 389 */       worldIn.setBlockToAir(blockpos2);
/* 390 */       blockpos2 = blockpos2.offset(enumfacing);
/* 391 */       worldIn.setBlockState(blockpos2, Blocks.piston_extension.getDefaultState().withProperty((IProperty)FACING, (Comparable)direction), 4);
/* 392 */       worldIn.setTileEntity(blockpos2, BlockPistonMoving.newTileEntity(iblockstate, direction, extending, false));
/* 393 */       i--;
/* 394 */       ablock[i] = block1;
/*     */     } 
/*     */     
/* 397 */     BlockPos blockpos1 = pos.offset(direction);
/*     */     
/* 399 */     if (extending) {
/*     */       
/* 401 */       BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
/* 402 */       IBlockState iblockstate1 = Blocks.piston_head.getDefaultState().withProperty((IProperty)BlockPistonExtension.FACING, (Comparable)direction).withProperty((IProperty)BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
/* 403 */       IBlockState iblockstate2 = Blocks.piston_extension.getDefaultState().withProperty((IProperty)BlockPistonMoving.FACING, (Comparable)direction).withProperty((IProperty)BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
/* 404 */       worldIn.setBlockState(blockpos1, iblockstate2, 4);
/* 405 */       worldIn.setTileEntity(blockpos1, BlockPistonMoving.newTileEntity(iblockstate1, direction, true, false));
/*     */     } 
/*     */     
/* 408 */     for (int l = list1.size() - 1; l >= 0; l--)
/*     */     {
/* 410 */       worldIn.notifyNeighborsOfStateChange(list1.get(l), ablock[i++]);
/*     */     }
/*     */     
/* 413 */     for (int i1 = list.size() - 1; i1 >= 0; i1--)
/*     */     {
/* 415 */       worldIn.notifyNeighborsOfStateChange(list.get(i1), ablock[i++]);
/*     */     }
/*     */     
/* 418 */     if (extending) {
/*     */       
/* 420 */       worldIn.notifyNeighborsOfStateChange(blockpos1, Blocks.piston_head);
/* 421 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/*     */     } 
/*     */     
/* 424 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IBlockState getStateForEntityRender(IBlockState state) {
/* 430 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.UP);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 435 */     return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)EXTENDED, Boolean.valueOf(((meta & 0x8) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 440 */     int i = 0;
/* 441 */     i |= ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/*     */     
/* 443 */     if (((Boolean)state.getValue((IProperty)EXTENDED)).booleanValue())
/*     */     {
/* 445 */       i |= 0x8;
/*     */     }
/*     */     
/* 448 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 453 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)EXTENDED });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPistonBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */