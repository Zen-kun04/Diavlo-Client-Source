/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemDoor;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.util.WeightedRandomChestContent;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StructureComponent
/*     */ {
/*     */   protected StructureBoundingBox boundingBox;
/*     */   protected EnumFacing coordBaseMode;
/*     */   protected int componentType;
/*     */   
/*     */   public StructureComponent() {}
/*     */   
/*     */   protected StructureComponent(int type) {
/*  33 */     this.componentType = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound createStructureBaseNBT() {
/*  38 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  39 */     nbttagcompound.setString("id", MapGenStructureIO.getStructureComponentName(this));
/*  40 */     nbttagcompound.setTag("BB", (NBTBase)this.boundingBox.toNBTTagIntArray());
/*  41 */     nbttagcompound.setInteger("O", (this.coordBaseMode == null) ? -1 : this.coordBaseMode.getHorizontalIndex());
/*  42 */     nbttagcompound.setInteger("GD", this.componentType);
/*  43 */     writeStructureToNBT(nbttagcompound);
/*  44 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void writeStructureToNBT(NBTTagCompound paramNBTTagCompound);
/*     */   
/*     */   public void readStructureBaseNBT(World worldIn, NBTTagCompound tagCompound) {
/*  51 */     if (tagCompound.hasKey("BB"))
/*     */     {
/*  53 */       this.boundingBox = new StructureBoundingBox(tagCompound.getIntArray("BB"));
/*     */     }
/*     */     
/*  56 */     int i = tagCompound.getInteger("O");
/*  57 */     this.coordBaseMode = (i == -1) ? null : EnumFacing.getHorizontal(i);
/*  58 */     this.componentType = tagCompound.getInteger("GD");
/*  59 */     readStructureFromNBT(tagCompound);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void readStructureFromNBT(NBTTagCompound paramNBTTagCompound);
/*     */ 
/*     */   
/*     */   public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {}
/*     */ 
/*     */   
/*     */   public abstract boolean addComponentParts(World paramWorld, Random paramRandom, StructureBoundingBox paramStructureBoundingBox);
/*     */   
/*     */   public StructureBoundingBox getBoundingBox() {
/*  72 */     return this.boundingBox;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComponentType() {
/*  77 */     return this.componentType;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StructureComponent findIntersecting(List<StructureComponent> listIn, StructureBoundingBox boundingboxIn) {
/*  82 */     for (StructureComponent structurecomponent : listIn) {
/*     */       
/*  84 */       if (structurecomponent.getBoundingBox() != null && structurecomponent.getBoundingBox().intersectsWith(boundingboxIn))
/*     */       {
/*  86 */         return structurecomponent;
/*     */       }
/*     */     } 
/*     */     
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getBoundingBoxCenter() {
/*  95 */     return new BlockPos(this.boundingBox.getCenter());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox boundingboxIn) {
/* 100 */     int i = Math.max(this.boundingBox.minX - 1, boundingboxIn.minX);
/* 101 */     int j = Math.max(this.boundingBox.minY - 1, boundingboxIn.minY);
/* 102 */     int k = Math.max(this.boundingBox.minZ - 1, boundingboxIn.minZ);
/* 103 */     int l = Math.min(this.boundingBox.maxX + 1, boundingboxIn.maxX);
/* 104 */     int i1 = Math.min(this.boundingBox.maxY + 1, boundingboxIn.maxY);
/* 105 */     int j1 = Math.min(this.boundingBox.maxZ + 1, boundingboxIn.maxZ);
/* 106 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 108 */     for (int k1 = i; k1 <= l; k1++) {
/*     */       
/* 110 */       for (int l1 = k; l1 <= j1; l1++) {
/*     */         
/* 112 */         if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, j, l1)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 114 */           return true;
/*     */         }
/*     */         
/* 117 */         if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, i1, l1)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 119 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 124 */     for (int i2 = i; i2 <= l; i2++) {
/*     */       
/* 126 */       for (int k2 = j; k2 <= i1; k2++) {
/*     */         
/* 128 */         if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(i2, k2, k)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 130 */           return true;
/*     */         }
/*     */         
/* 133 */         if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(i2, k2, j1)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 135 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     for (int j2 = k; j2 <= j1; j2++) {
/*     */       
/* 142 */       for (int l2 = j; l2 <= i1; l2++) {
/*     */         
/* 144 */         if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(i, l2, j2)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 146 */           return true;
/*     */         }
/*     */         
/* 149 */         if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(l, l2, j2)).getBlock().getMaterial().isLiquid())
/*     */         {
/* 151 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getXWithOffset(int x, int z) {
/* 161 */     if (this.coordBaseMode == null)
/*     */     {
/* 163 */       return x;
/*     */     }
/*     */ 
/*     */     
/* 167 */     switch (this.coordBaseMode) {
/*     */       
/*     */       case NORTH:
/*     */       case SOUTH:
/* 171 */         return this.boundingBox.minX + x;
/*     */       
/*     */       case WEST:
/* 174 */         return this.boundingBox.maxX - z;
/*     */       
/*     */       case EAST:
/* 177 */         return this.boundingBox.minX + z;
/*     */     } 
/*     */     
/* 180 */     return x;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getYWithOffset(int y) {
/* 187 */     return (this.coordBaseMode == null) ? y : (y + this.boundingBox.minY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getZWithOffset(int x, int z) {
/* 192 */     if (this.coordBaseMode == null)
/*     */     {
/* 194 */       return z;
/*     */     }
/*     */ 
/*     */     
/* 198 */     switch (this.coordBaseMode) {
/*     */       
/*     */       case NORTH:
/* 201 */         return this.boundingBox.maxZ - z;
/*     */       
/*     */       case SOUTH:
/* 204 */         return this.boundingBox.minZ + z;
/*     */       
/*     */       case WEST:
/*     */       case EAST:
/* 208 */         return this.boundingBox.minZ + x;
/*     */     } 
/*     */     
/* 211 */     return z;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMetadataWithOffset(Block blockIn, int meta) {
/* 218 */     if (blockIn == Blocks.rail) {
/*     */       
/* 220 */       if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST)
/*     */       {
/* 222 */         if (meta == 1)
/*     */         {
/* 224 */           return 0;
/*     */         }
/*     */         
/* 227 */         return 1;
/*     */       }
/*     */     
/* 230 */     } else if (blockIn instanceof net.minecraft.block.BlockDoor) {
/*     */       
/* 232 */       if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */       {
/* 234 */         if (meta == 0)
/*     */         {
/* 236 */           return 2;
/*     */         }
/*     */         
/* 239 */         if (meta == 2)
/*     */         {
/* 241 */           return 0;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 246 */         if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 248 */           return meta + 1 & 0x3;
/*     */         }
/*     */         
/* 251 */         if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 253 */           return meta + 3 & 0x3;
/*     */         }
/*     */       }
/*     */     
/* 257 */     } else if (blockIn != Blocks.stone_stairs && blockIn != Blocks.oak_stairs && blockIn != Blocks.nether_brick_stairs && blockIn != Blocks.stone_brick_stairs && blockIn != Blocks.sandstone_stairs) {
/*     */       
/* 259 */       if (blockIn == Blocks.ladder) {
/*     */         
/* 261 */         if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */         {
/* 263 */           if (meta == EnumFacing.NORTH.getIndex())
/*     */           {
/* 265 */             return EnumFacing.SOUTH.getIndex();
/*     */           }
/*     */           
/* 268 */           if (meta == EnumFacing.SOUTH.getIndex())
/*     */           {
/* 270 */             return EnumFacing.NORTH.getIndex();
/*     */           }
/*     */         }
/* 273 */         else if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 275 */           if (meta == EnumFacing.NORTH.getIndex())
/*     */           {
/* 277 */             return EnumFacing.WEST.getIndex();
/*     */           }
/*     */           
/* 280 */           if (meta == EnumFacing.SOUTH.getIndex())
/*     */           {
/* 282 */             return EnumFacing.EAST.getIndex();
/*     */           }
/*     */           
/* 285 */           if (meta == EnumFacing.WEST.getIndex())
/*     */           {
/* 287 */             return EnumFacing.NORTH.getIndex();
/*     */           }
/*     */           
/* 290 */           if (meta == EnumFacing.EAST.getIndex())
/*     */           {
/* 292 */             return EnumFacing.SOUTH.getIndex();
/*     */           }
/*     */         }
/* 295 */         else if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 297 */           if (meta == EnumFacing.NORTH.getIndex())
/*     */           {
/* 299 */             return EnumFacing.EAST.getIndex();
/*     */           }
/*     */           
/* 302 */           if (meta == EnumFacing.SOUTH.getIndex())
/*     */           {
/* 304 */             return EnumFacing.WEST.getIndex();
/*     */           }
/*     */           
/* 307 */           if (meta == EnumFacing.WEST.getIndex())
/*     */           {
/* 309 */             return EnumFacing.NORTH.getIndex();
/*     */           }
/*     */           
/* 312 */           if (meta == EnumFacing.EAST.getIndex())
/*     */           {
/* 314 */             return EnumFacing.SOUTH.getIndex();
/*     */           }
/*     */         }
/*     */       
/* 318 */       } else if (blockIn == Blocks.stone_button) {
/*     */         
/* 320 */         if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */         {
/* 322 */           if (meta == 3)
/*     */           {
/* 324 */             return 4;
/*     */           }
/*     */           
/* 327 */           if (meta == 4)
/*     */           {
/* 329 */             return 3;
/*     */           }
/*     */         }
/* 332 */         else if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 334 */           if (meta == 3)
/*     */           {
/* 336 */             return 1;
/*     */           }
/*     */           
/* 339 */           if (meta == 4)
/*     */           {
/* 341 */             return 2;
/*     */           }
/*     */           
/* 344 */           if (meta == 2)
/*     */           {
/* 346 */             return 3;
/*     */           }
/*     */           
/* 349 */           if (meta == 1)
/*     */           {
/* 351 */             return 4;
/*     */           }
/*     */         }
/* 354 */         else if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 356 */           if (meta == 3)
/*     */           {
/* 358 */             return 2;
/*     */           }
/*     */           
/* 361 */           if (meta == 4)
/*     */           {
/* 363 */             return 1;
/*     */           }
/*     */           
/* 366 */           if (meta == 2)
/*     */           {
/* 368 */             return 3;
/*     */           }
/*     */           
/* 371 */           if (meta == 1)
/*     */           {
/* 373 */             return 4;
/*     */           }
/*     */         }
/*     */       
/* 377 */       } else if (blockIn != Blocks.tripwire_hook && !(blockIn instanceof net.minecraft.block.BlockDirectional)) {
/*     */         
/* 379 */         if (blockIn == Blocks.piston || blockIn == Blocks.sticky_piston || blockIn == Blocks.lever || blockIn == Blocks.dispenser)
/*     */         {
/* 381 */           if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */           {
/* 383 */             if (meta == EnumFacing.NORTH.getIndex() || meta == EnumFacing.SOUTH.getIndex())
/*     */             {
/* 385 */               return EnumFacing.getFront(meta).getOpposite().getIndex();
/*     */             }
/*     */           }
/* 388 */           else if (this.coordBaseMode == EnumFacing.WEST)
/*     */           {
/* 390 */             if (meta == EnumFacing.NORTH.getIndex())
/*     */             {
/* 392 */               return EnumFacing.WEST.getIndex();
/*     */             }
/*     */             
/* 395 */             if (meta == EnumFacing.SOUTH.getIndex())
/*     */             {
/* 397 */               return EnumFacing.EAST.getIndex();
/*     */             }
/*     */             
/* 400 */             if (meta == EnumFacing.WEST.getIndex())
/*     */             {
/* 402 */               return EnumFacing.NORTH.getIndex();
/*     */             }
/*     */             
/* 405 */             if (meta == EnumFacing.EAST.getIndex())
/*     */             {
/* 407 */               return EnumFacing.SOUTH.getIndex();
/*     */             }
/*     */           }
/* 410 */           else if (this.coordBaseMode == EnumFacing.EAST)
/*     */           {
/* 412 */             if (meta == EnumFacing.NORTH.getIndex())
/*     */             {
/* 414 */               return EnumFacing.EAST.getIndex();
/*     */             }
/*     */             
/* 417 */             if (meta == EnumFacing.SOUTH.getIndex())
/*     */             {
/* 419 */               return EnumFacing.WEST.getIndex();
/*     */             }
/*     */             
/* 422 */             if (meta == EnumFacing.WEST.getIndex())
/*     */             {
/* 424 */               return EnumFacing.NORTH.getIndex();
/*     */             }
/*     */             
/* 427 */             if (meta == EnumFacing.EAST.getIndex())
/*     */             {
/* 429 */               return EnumFacing.SOUTH.getIndex();
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       } else {
/*     */         
/* 436 */         EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
/*     */         
/* 438 */         if (this.coordBaseMode == EnumFacing.SOUTH)
/*     */         {
/* 440 */           if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH)
/*     */           {
/* 442 */             return enumfacing.getOpposite().getHorizontalIndex();
/*     */           }
/*     */         }
/* 445 */         else if (this.coordBaseMode == EnumFacing.WEST)
/*     */         {
/* 447 */           if (enumfacing == EnumFacing.NORTH)
/*     */           {
/* 449 */             return EnumFacing.WEST.getHorizontalIndex();
/*     */           }
/*     */           
/* 452 */           if (enumfacing == EnumFacing.SOUTH)
/*     */           {
/* 454 */             return EnumFacing.EAST.getHorizontalIndex();
/*     */           }
/*     */           
/* 457 */           if (enumfacing == EnumFacing.WEST)
/*     */           {
/* 459 */             return EnumFacing.NORTH.getHorizontalIndex();
/*     */           }
/*     */           
/* 462 */           if (enumfacing == EnumFacing.EAST)
/*     */           {
/* 464 */             return EnumFacing.SOUTH.getHorizontalIndex();
/*     */           }
/*     */         }
/* 467 */         else if (this.coordBaseMode == EnumFacing.EAST)
/*     */         {
/* 469 */           if (enumfacing == EnumFacing.NORTH)
/*     */           {
/* 471 */             return EnumFacing.EAST.getHorizontalIndex();
/*     */           }
/*     */           
/* 474 */           if (enumfacing == EnumFacing.SOUTH)
/*     */           {
/* 476 */             return EnumFacing.WEST.getHorizontalIndex();
/*     */           }
/*     */           
/* 479 */           if (enumfacing == EnumFacing.WEST)
/*     */           {
/* 481 */             return EnumFacing.NORTH.getHorizontalIndex();
/*     */           }
/*     */           
/* 484 */           if (enumfacing == EnumFacing.EAST)
/*     */           {
/* 486 */             return EnumFacing.SOUTH.getHorizontalIndex();
/*     */           }
/*     */         }
/*     */       
/*     */       } 
/* 491 */     } else if (this.coordBaseMode == EnumFacing.SOUTH) {
/*     */       
/* 493 */       if (meta == 2)
/*     */       {
/* 495 */         return 3;
/*     */       }
/*     */       
/* 498 */       if (meta == 3)
/*     */       {
/* 500 */         return 2;
/*     */       }
/*     */     }
/* 503 */     else if (this.coordBaseMode == EnumFacing.WEST) {
/*     */       
/* 505 */       if (meta == 0)
/*     */       {
/* 507 */         return 2;
/*     */       }
/*     */       
/* 510 */       if (meta == 1)
/*     */       {
/* 512 */         return 3;
/*     */       }
/*     */       
/* 515 */       if (meta == 2)
/*     */       {
/* 517 */         return 0;
/*     */       }
/*     */       
/* 520 */       if (meta == 3)
/*     */       {
/* 522 */         return 1;
/*     */       }
/*     */     }
/* 525 */     else if (this.coordBaseMode == EnumFacing.EAST) {
/*     */       
/* 527 */       if (meta == 0)
/*     */       {
/* 529 */         return 2;
/*     */       }
/*     */       
/* 532 */       if (meta == 1)
/*     */       {
/* 534 */         return 3;
/*     */       }
/*     */       
/* 537 */       if (meta == 2)
/*     */       {
/* 539 */         return 1;
/*     */       }
/*     */       
/* 542 */       if (meta == 3)
/*     */       {
/* 544 */         return 0;
/*     */       }
/*     */     } 
/*     */     
/* 548 */     return meta;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
/* 553 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 555 */     if (boundingboxIn.isVecInside((Vec3i)blockpos))
/*     */     {
/* 557 */       worldIn.setBlockState(blockpos, blockstateIn, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected IBlockState getBlockStateFromPos(World worldIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
/* 563 */     int i = getXWithOffset(x, z);
/* 564 */     int j = getYWithOffset(y);
/* 565 */     int k = getZWithOffset(x, z);
/* 566 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 567 */     return !boundingboxIn.isVecInside((Vec3i)blockpos) ? Blocks.air.getDefaultState() : worldIn.getBlockState(blockpos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillWithAir(World worldIn, StructureBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/* 572 */     for (int i = minY; i <= maxY; i++) {
/*     */       
/* 574 */       for (int j = minX; j <= maxX; j++) {
/*     */         
/* 576 */         for (int k = minZ; k <= maxZ; k++)
/*     */         {
/* 578 */           setBlockState(worldIn, Blocks.air.getDefaultState(), j, i, k, structurebb);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly) {
/* 586 */     for (int i = yMin; i <= yMax; i++) {
/*     */       
/* 588 */       for (int j = xMin; j <= xMax; j++) {
/*     */         
/* 590 */         for (int k = zMin; k <= zMax; k++) {
/*     */           
/* 592 */           if (!existingOnly || getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air)
/*     */           {
/* 594 */             if (i != yMin && i != yMax && j != xMin && j != xMax && k != zMin && k != zMax) {
/*     */               
/* 596 */               setBlockState(worldIn, insideBlockState, j, i, k, boundingboxIn);
/*     */             }
/*     */             else {
/*     */               
/* 600 */               setBlockState(worldIn, boundaryBlockState, j, i, k, boundingboxIn);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean alwaysReplace, Random rand, BlockSelector blockselector) {
/* 610 */     for (int i = minY; i <= maxY; i++) {
/*     */       
/* 612 */       for (int j = minX; j <= maxX; j++) {
/*     */         
/* 614 */         for (int k = minZ; k <= maxZ; k++) {
/*     */           
/* 616 */           if (!alwaysReplace || getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air) {
/*     */             
/* 618 */             blockselector.selectBlocks(rand, j, i, k, (i == minY || i == maxY || j == minX || j == maxX || k == minZ || k == maxZ));
/* 619 */             setBlockState(worldIn, blockselector.getBlockState(), j, i, k, boundingboxIn);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_175805_a(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstate1, IBlockState blockstate2, boolean p_175805_13_) {
/* 628 */     for (int i = minY; i <= maxY; i++) {
/*     */       
/* 630 */       for (int j = minX; j <= maxX; j++) {
/*     */         
/* 632 */         for (int k = minZ; k <= maxZ; k++) {
/*     */           
/* 634 */           if (rand.nextFloat() <= chance && (!p_175805_13_ || getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air))
/*     */           {
/* 636 */             if (i != minY && i != maxY && j != minX && j != maxX && k != minZ && k != maxZ) {
/*     */               
/* 638 */               setBlockState(worldIn, blockstate2, j, i, k, boundingboxIn);
/*     */             }
/*     */             else {
/*     */               
/* 642 */               setBlockState(worldIn, blockstate1, j, i, k, boundingboxIn);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomlyPlaceBlock(World worldIn, StructureBoundingBox boundingboxIn, Random rand, float chance, int x, int y, int z, IBlockState blockstateIn) {
/* 652 */     if (rand.nextFloat() < chance)
/*     */     {
/* 654 */       setBlockState(worldIn, blockstateIn, x, y, z, boundingboxIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomlyRareFillWithBlocks(World worldIn, StructureBoundingBox boundingboxIn, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockstateIn, boolean p_180777_10_) {
/* 660 */     float f = (maxX - minX + 1);
/* 661 */     float f1 = (maxY - minY + 1);
/* 662 */     float f2 = (maxZ - minZ + 1);
/* 663 */     float f3 = minX + f / 2.0F;
/* 664 */     float f4 = minZ + f2 / 2.0F;
/*     */     
/* 666 */     for (int i = minY; i <= maxY; i++) {
/*     */       
/* 668 */       float f5 = (i - minY) / f1;
/*     */       
/* 670 */       for (int j = minX; j <= maxX; j++) {
/*     */         
/* 672 */         float f6 = (j - f3) / f * 0.5F;
/*     */         
/* 674 */         for (int k = minZ; k <= maxZ; k++) {
/*     */           
/* 676 */           float f7 = (k - f4) / f2 * 0.5F;
/*     */           
/* 678 */           if (!p_180777_10_ || getBlockStateFromPos(worldIn, j, i, k, boundingboxIn).getBlock().getMaterial() != Material.air) {
/*     */             
/* 680 */             float f8 = f6 * f6 + f5 * f5 + f7 * f7;
/*     */             
/* 682 */             if (f8 <= 1.05F)
/*     */             {
/* 684 */               setBlockState(worldIn, blockstateIn, j, i, k, boundingboxIn);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearCurrentPositionBlocksUpwards(World worldIn, int x, int y, int z, StructureBoundingBox structurebb) {
/* 694 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 696 */     if (structurebb.isVecInside((Vec3i)blockpos))
/*     */     {
/* 698 */       while (!worldIn.isAirBlock(blockpos) && blockpos.getY() < 255) {
/*     */         
/* 700 */         worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
/* 701 */         blockpos = blockpos.up();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
/* 708 */     int i = getXWithOffset(x, z);
/* 709 */     int j = getYWithOffset(y);
/* 710 */     int k = getZWithOffset(x, z);
/*     */     
/* 712 */     if (boundingboxIn.isVecInside((Vec3i)new BlockPos(i, j, k)))
/*     */     {
/* 714 */       while ((worldIn.isAirBlock(new BlockPos(i, j, k)) || worldIn.getBlockState(new BlockPos(i, j, k)).getBlock().getMaterial().isLiquid()) && j > 1) {
/*     */         
/* 716 */         worldIn.setBlockState(new BlockPos(i, j, k), blockstateIn, 2);
/* 717 */         j--;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, List<WeightedRandomChestContent> listIn, int max) {
/* 724 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 726 */     if (boundingBoxIn.isVecInside((Vec3i)blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.chest) {
/*     */       
/* 728 */       IBlockState iblockstate = Blocks.chest.getDefaultState();
/* 729 */       worldIn.setBlockState(blockpos, Blocks.chest.correctFacing(worldIn, blockpos, iblockstate), 2);
/* 730 */       TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */       
/* 732 */       if (tileentity instanceof net.minecraft.tileentity.TileEntityChest)
/*     */       {
/* 734 */         WeightedRandomChestContent.generateChestContents(rand, listIn, (IInventory)tileentity, max);
/*     */       }
/*     */       
/* 737 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 741 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean generateDispenserContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, int meta, List<WeightedRandomChestContent> listIn, int max) {
/* 747 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 749 */     if (boundingBoxIn.isVecInside((Vec3i)blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.dispenser) {
/*     */       
/* 751 */       worldIn.setBlockState(blockpos, Blocks.dispenser.getStateFromMeta(getMetadataWithOffset(Blocks.dispenser, meta)), 2);
/* 752 */       TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */       
/* 754 */       if (tileentity instanceof TileEntityDispenser)
/*     */       {
/* 756 */         WeightedRandomChestContent.generateDispenserContents(rand, listIn, (TileEntityDispenser)tileentity, max);
/*     */       }
/*     */       
/* 759 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 763 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void placeDoorCurrentPosition(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, EnumFacing facing) {
/* 769 */     BlockPos blockpos = new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*     */     
/* 771 */     if (boundingBoxIn.isVecInside((Vec3i)blockpos))
/*     */     {
/* 773 */       ItemDoor.placeDoor(worldIn, blockpos, facing.rotateYCCW(), Blocks.oak_door);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_181138_a(int p_181138_1_, int p_181138_2_, int p_181138_3_) {
/* 779 */     this.boundingBox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
/*     */   }
/*     */   
/*     */   public static abstract class BlockSelector
/*     */   {
/* 784 */     protected IBlockState blockstate = Blocks.air.getDefaultState();
/*     */ 
/*     */     
/*     */     public abstract void selectBlocks(Random param1Random, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean);
/*     */     
/*     */     public IBlockState getBlockState() {
/* 790 */       return this.blockstate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\StructureComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */