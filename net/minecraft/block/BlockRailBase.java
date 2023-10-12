/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockRailBase
/*     */   extends Block
/*     */ {
/*     */   protected final boolean isPowered;
/*     */   
/*     */   public static boolean isRailBlock(World worldIn, BlockPos pos) {
/*  26 */     return isRailBlock(worldIn.getBlockState(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isRailBlock(IBlockState state) {
/*  31 */     Block block = state.getBlock();
/*  32 */     return (block == Blocks.rail || block == Blocks.golden_rail || block == Blocks.detector_rail || block == Blocks.activator_rail);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockRailBase(boolean isPowered) {
/*  37 */     super(Material.circuits);
/*  38 */     this.isPowered = isPowered;
/*  39 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
/*  40 */     setCreativeTab(CreativeTabs.tabTransport);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/*  45 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
/*  55 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*  56 */     return super.collisionRayTrace(worldIn, pos, start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  61 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  62 */     EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() == this) ? (EnumRailDirection)iblockstate.getValue(getShapeProperty()) : null;
/*     */     
/*  64 */     if (blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending()) {
/*     */       
/*  66 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
/*     */     }
/*     */     else {
/*     */       
/*  70 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*  81 */     return World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  86 */     if (!worldIn.isRemote) {
/*     */       
/*  88 */       state = func_176564_a(worldIn, pos, state, true);
/*     */       
/*  90 */       if (this.isPowered)
/*     */       {
/*  92 */         onNeighborBlockChange(worldIn, pos, state, this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  99 */     if (!worldIn.isRemote) {
/*     */       
/* 101 */       EnumRailDirection blockrailbase$enumraildirection = (EnumRailDirection)state.getValue(getShapeProperty());
/* 102 */       boolean flag = false;
/*     */       
/* 104 */       if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()))
/*     */       {
/* 106 */         flag = true;
/*     */       }
/*     */       
/* 109 */       if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.east())) {
/*     */         
/* 111 */         flag = true;
/*     */       }
/* 113 */       else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.west())) {
/*     */         
/* 115 */         flag = true;
/*     */       }
/* 117 */       else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.north())) {
/*     */         
/* 119 */         flag = true;
/*     */       }
/* 121 */       else if (blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.south())) {
/*     */         
/* 123 */         flag = true;
/*     */       } 
/*     */       
/* 126 */       if (flag) {
/*     */         
/* 128 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 129 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       else {
/*     */         
/* 133 */         onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {}
/*     */ 
/*     */   
/*     */   protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_) {
/* 144 */     return worldIn.isRemote ? p_176564_3_ : (new Rail(worldIn, p_176564_2_, p_176564_3_)).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMobilityFlag() {
/* 149 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 154 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 159 */     super.breakBlock(worldIn, pos, state);
/*     */     
/* 161 */     if (((EnumRailDirection)state.getValue(getShapeProperty())).isAscending())
/*     */     {
/* 163 */       worldIn.notifyNeighborsOfStateChange(pos.up(), this);
/*     */     }
/*     */     
/* 166 */     if (this.isPowered) {
/*     */       
/* 168 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 169 */       worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract IProperty<EnumRailDirection> getShapeProperty();
/*     */   
/*     */   public enum EnumRailDirection
/*     */     implements IStringSerializable {
/* 177 */     NORTH_SOUTH(0, "north_south"),
/* 178 */     EAST_WEST(1, "east_west"),
/* 179 */     ASCENDING_EAST(2, "ascending_east"),
/* 180 */     ASCENDING_WEST(3, "ascending_west"),
/* 181 */     ASCENDING_NORTH(4, "ascending_north"),
/* 182 */     ASCENDING_SOUTH(5, "ascending_south"),
/* 183 */     SOUTH_EAST(6, "south_east"),
/* 184 */     SOUTH_WEST(7, "south_west"),
/* 185 */     NORTH_WEST(8, "north_west"),
/* 186 */     NORTH_EAST(9, "north_east");
/*     */     
/* 188 */     private static final EnumRailDirection[] META_LOOKUP = new EnumRailDirection[(values()).length];
/*     */ 
/*     */     
/*     */     private final int meta;
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     EnumRailDirection(int meta, String name) {
/*     */       this.meta = meta;
/*     */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMetadata() {
/*     */       return this.meta;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*     */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAscending() {
/*     */       return (this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST);
/*     */     }
/*     */ 
/*     */     
/*     */     public static EnumRailDirection byMetadata(int meta) {
/*     */       if (meta < 0 || meta >= META_LOOKUP.length) {
/*     */         meta = 0;
/*     */       }
/*     */       return META_LOOKUP[meta];
/*     */     }
/*     */ 
/*     */     
/*     */     static {
/* 229 */       for (EnumRailDirection blockrailbase$enumraildirection : values())
/*     */       {
/* 231 */         META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection; } 
/*     */     }
/*     */     
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public class Rail
/*     */   {
/*     */     private final World world;
/*     */     private final BlockPos pos;
/* 243 */     private final List<BlockPos> field_150657_g = Lists.newArrayList();
/*     */     private final BlockRailBase block;
/*     */     
/*     */     public Rail(World worldIn, BlockPos pos, IBlockState state) {
/* 247 */       this.world = worldIn;
/* 248 */       this.pos = pos;
/* 249 */       this.state = state;
/* 250 */       this.block = (BlockRailBase)state.getBlock();
/* 251 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(BlockRailBase.this.getShapeProperty());
/* 252 */       this.isPowered = this.block.isPowered;
/* 253 */       func_180360_a(blockrailbase$enumraildirection);
/*     */     }
/*     */     private IBlockState state; private final boolean isPowered;
/*     */     
/*     */     private void func_180360_a(BlockRailBase.EnumRailDirection p_180360_1_) {
/* 258 */       this.field_150657_g.clear();
/*     */       
/* 260 */       switch (p_180360_1_) {
/*     */         
/*     */         case NORTH_SOUTH:
/* 263 */           this.field_150657_g.add(this.pos.north());
/* 264 */           this.field_150657_g.add(this.pos.south());
/*     */           break;
/*     */         
/*     */         case EAST_WEST:
/* 268 */           this.field_150657_g.add(this.pos.west());
/* 269 */           this.field_150657_g.add(this.pos.east());
/*     */           break;
/*     */         
/*     */         case ASCENDING_EAST:
/* 273 */           this.field_150657_g.add(this.pos.west());
/* 274 */           this.field_150657_g.add(this.pos.east().up());
/*     */           break;
/*     */         
/*     */         case ASCENDING_WEST:
/* 278 */           this.field_150657_g.add(this.pos.west().up());
/* 279 */           this.field_150657_g.add(this.pos.east());
/*     */           break;
/*     */         
/*     */         case ASCENDING_NORTH:
/* 283 */           this.field_150657_g.add(this.pos.north().up());
/* 284 */           this.field_150657_g.add(this.pos.south());
/*     */           break;
/*     */         
/*     */         case ASCENDING_SOUTH:
/* 288 */           this.field_150657_g.add(this.pos.north());
/* 289 */           this.field_150657_g.add(this.pos.south().up());
/*     */           break;
/*     */         
/*     */         case SOUTH_EAST:
/* 293 */           this.field_150657_g.add(this.pos.east());
/* 294 */           this.field_150657_g.add(this.pos.south());
/*     */           break;
/*     */         
/*     */         case SOUTH_WEST:
/* 298 */           this.field_150657_g.add(this.pos.west());
/* 299 */           this.field_150657_g.add(this.pos.south());
/*     */           break;
/*     */         
/*     */         case NORTH_WEST:
/* 303 */           this.field_150657_g.add(this.pos.west());
/* 304 */           this.field_150657_g.add(this.pos.north());
/*     */           break;
/*     */         
/*     */         case NORTH_EAST:
/* 308 */           this.field_150657_g.add(this.pos.east());
/* 309 */           this.field_150657_g.add(this.pos.north());
/*     */           break;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void func_150651_b() {
/* 315 */       for (int i = 0; i < this.field_150657_g.size(); i++) {
/*     */         
/* 317 */         Rail blockrailbase$rail = findRailAt(this.field_150657_g.get(i));
/*     */         
/* 319 */         if (blockrailbase$rail != null && blockrailbase$rail.func_150653_a(this)) {
/*     */           
/* 321 */           this.field_150657_g.set(i, blockrailbase$rail.pos);
/*     */         }
/*     */         else {
/*     */           
/* 325 */           this.field_150657_g.remove(i--);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean hasRailAt(BlockPos pos) {
/* 332 */       return (BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up()) || BlockRailBase.isRailBlock(this.world, pos.down()));
/*     */     }
/*     */ 
/*     */     
/*     */     private Rail findRailAt(BlockPos pos) {
/* 337 */       IBlockState iblockstate = this.world.getBlockState(pos);
/*     */       
/* 339 */       if (BlockRailBase.isRailBlock(iblockstate)) {
/*     */         
/* 341 */         BlockRailBase.this.getClass(); return new Rail(this.world, pos, iblockstate);
/*     */       } 
/*     */ 
/*     */       
/* 345 */       BlockPos lvt_2_1_ = pos.up();
/* 346 */       iblockstate = this.world.getBlockState(lvt_2_1_);
/*     */       
/* 348 */       if (BlockRailBase.isRailBlock(iblockstate)) {
/*     */         
/* 350 */         BlockRailBase.this.getClass(); return new Rail(this.world, lvt_2_1_, iblockstate);
/*     */       } 
/*     */ 
/*     */       
/* 354 */       lvt_2_1_ = pos.down();
/* 355 */       iblockstate = this.world.getBlockState(lvt_2_1_);
/* 356 */       BlockRailBase.this.getClass(); return BlockRailBase.isRailBlock(iblockstate) ? new Rail(this.world, lvt_2_1_, iblockstate) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean func_150653_a(Rail p_150653_1_) {
/* 363 */       return func_180363_c(p_150653_1_.pos);
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean func_180363_c(BlockPos p_180363_1_) {
/* 368 */       for (int i = 0; i < this.field_150657_g.size(); i++) {
/*     */         
/* 370 */         BlockPos blockpos = this.field_150657_g.get(i);
/*     */         
/* 372 */         if (blockpos.getX() == p_180363_1_.getX() && blockpos.getZ() == p_180363_1_.getZ())
/*     */         {
/* 374 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 378 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int countAdjacentRails() {
/* 383 */       int i = 0;
/*     */       
/* 385 */       for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */         
/* 387 */         if (hasRailAt(this.pos.offset(enumfacing)))
/*     */         {
/* 389 */           i++;
/*     */         }
/*     */       } 
/*     */       
/* 393 */       return i;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean func_150649_b(Rail rail) {
/* 398 */       return (func_150653_a(rail) || this.field_150657_g.size() != 2);
/*     */     }
/*     */ 
/*     */     
/*     */     private void func_150645_c(Rail p_150645_1_) {
/* 403 */       this.field_150657_g.add(p_150645_1_.pos);
/* 404 */       BlockPos blockpos = this.pos.north();
/* 405 */       BlockPos blockpos1 = this.pos.south();
/* 406 */       BlockPos blockpos2 = this.pos.west();
/* 407 */       BlockPos blockpos3 = this.pos.east();
/* 408 */       boolean flag = func_180363_c(blockpos);
/* 409 */       boolean flag1 = func_180363_c(blockpos1);
/* 410 */       boolean flag2 = func_180363_c(blockpos2);
/* 411 */       boolean flag3 = func_180363_c(blockpos3);
/* 412 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
/*     */       
/* 414 */       if (flag || flag1)
/*     */       {
/* 416 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 419 */       if (flag2 || flag3)
/*     */       {
/* 421 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
/*     */       }
/*     */       
/* 424 */       if (!this.isPowered) {
/*     */         
/* 426 */         if (flag1 && flag3 && !flag && !flag2)
/*     */         {
/* 428 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */         }
/*     */         
/* 431 */         if (flag1 && flag2 && !flag && !flag3)
/*     */         {
/* 433 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */         }
/*     */         
/* 436 */         if (flag && flag2 && !flag1 && !flag3)
/*     */         {
/* 438 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */         }
/*     */         
/* 441 */         if (flag && flag3 && !flag1 && !flag2)
/*     */         {
/* 443 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */         }
/*     */       } 
/*     */       
/* 447 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
/*     */         
/* 449 */         if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
/*     */         {
/* 451 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
/*     */         }
/*     */         
/* 454 */         if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
/*     */         {
/* 456 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
/*     */         }
/*     */       } 
/*     */       
/* 460 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
/*     */         
/* 462 */         if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
/*     */         {
/* 464 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
/*     */         }
/*     */         
/* 467 */         if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
/*     */         {
/* 469 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
/*     */         }
/*     */       } 
/*     */       
/* 473 */       if (blockrailbase$enumraildirection == null)
/*     */       {
/* 475 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 478 */       this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
/* 479 */       this.world.setBlockState(this.pos, this.state, 3);
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean func_180361_d(BlockPos p_180361_1_) {
/* 484 */       Rail blockrailbase$rail = findRailAt(p_180361_1_);
/*     */       
/* 486 */       if (blockrailbase$rail == null)
/*     */       {
/* 488 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 492 */       blockrailbase$rail.func_150651_b();
/* 493 */       return blockrailbase$rail.func_150649_b(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Rail func_180364_a(boolean p_180364_1_, boolean p_180364_2_) {
/* 499 */       BlockPos blockpos = this.pos.north();
/* 500 */       BlockPos blockpos1 = this.pos.south();
/* 501 */       BlockPos blockpos2 = this.pos.west();
/* 502 */       BlockPos blockpos3 = this.pos.east();
/* 503 */       boolean flag = func_180361_d(blockpos);
/* 504 */       boolean flag1 = func_180361_d(blockpos1);
/* 505 */       boolean flag2 = func_180361_d(blockpos2);
/* 506 */       boolean flag3 = func_180361_d(blockpos3);
/* 507 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
/*     */       
/* 509 */       if ((flag || flag1) && !flag2 && !flag3)
/*     */       {
/* 511 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 514 */       if ((flag2 || flag3) && !flag && !flag1)
/*     */       {
/* 516 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
/*     */       }
/*     */       
/* 519 */       if (!this.isPowered) {
/*     */         
/* 521 */         if (flag1 && flag3 && !flag && !flag2)
/*     */         {
/* 523 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */         }
/*     */         
/* 526 */         if (flag1 && flag2 && !flag && !flag3)
/*     */         {
/* 528 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */         }
/*     */         
/* 531 */         if (flag && flag2 && !flag1 && !flag3)
/*     */         {
/* 533 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */         }
/*     */         
/* 536 */         if (flag && flag3 && !flag1 && !flag2)
/*     */         {
/* 538 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */         }
/*     */       } 
/*     */       
/* 542 */       if (blockrailbase$enumraildirection == null) {
/*     */         
/* 544 */         if (flag || flag1)
/*     */         {
/* 546 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */         }
/*     */         
/* 549 */         if (flag2 || flag3)
/*     */         {
/* 551 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
/*     */         }
/*     */         
/* 554 */         if (!this.isPowered)
/*     */         {
/* 556 */           if (p_180364_1_) {
/*     */             
/* 558 */             if (flag1 && flag3)
/*     */             {
/* 560 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */             }
/*     */             
/* 563 */             if (flag2 && flag1)
/*     */             {
/* 565 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */             }
/*     */             
/* 568 */             if (flag3 && flag)
/*     */             {
/* 570 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */             }
/*     */             
/* 573 */             if (flag && flag2)
/*     */             {
/* 575 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 580 */             if (flag && flag2)
/*     */             {
/* 582 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */             }
/*     */             
/* 585 */             if (flag3 && flag)
/*     */             {
/* 587 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */             }
/*     */             
/* 590 */             if (flag2 && flag1)
/*     */             {
/* 592 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */             }
/*     */             
/* 595 */             if (flag1 && flag3)
/*     */             {
/* 597 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 603 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
/*     */         
/* 605 */         if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
/*     */         {
/* 607 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
/*     */         }
/*     */         
/* 610 */         if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
/*     */         {
/* 612 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
/*     */         }
/*     */       } 
/*     */       
/* 616 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
/*     */         
/* 618 */         if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
/*     */         {
/* 620 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
/*     */         }
/*     */         
/* 623 */         if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
/*     */         {
/* 625 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
/*     */         }
/*     */       } 
/*     */       
/* 629 */       if (blockrailbase$enumraildirection == null)
/*     */       {
/* 631 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 634 */       func_180360_a(blockrailbase$enumraildirection);
/* 635 */       this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
/*     */       
/* 637 */       if (p_180364_2_ || this.world.getBlockState(this.pos) != this.state) {
/*     */         
/* 639 */         this.world.setBlockState(this.pos, this.state, 3);
/*     */         
/* 641 */         for (int i = 0; i < this.field_150657_g.size(); i++) {
/*     */           
/* 643 */           Rail blockrailbase$rail = findRailAt(this.field_150657_g.get(i));
/*     */           
/* 645 */           if (blockrailbase$rail != null) {
/*     */             
/* 647 */             blockrailbase$rail.func_150651_b();
/*     */             
/* 649 */             if (blockrailbase$rail.func_150649_b(this))
/*     */             {
/* 651 */               blockrailbase$rail.func_150645_c(this);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 657 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IBlockState getBlockState() {
/* 662 */       return this.state;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRailBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */