/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStairs extends Block {
/*  30 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
/*  31 */   public static final PropertyEnum<EnumHalf> HALF = PropertyEnum.create("half", EnumHalf.class);
/*  32 */   public static final PropertyEnum<EnumShape> SHAPE = PropertyEnum.create("shape", EnumShape.class);
/*  33 */   private static final int[][] field_150150_a = new int[][] { { 4, 5 }, { 5, 7 }, { 6, 7 }, { 4, 6 }, { 0, 1 }, { 1, 3 }, { 2, 3 }, { 0, 2 } };
/*     */   
/*     */   private final Block modelBlock;
/*     */   private final IBlockState modelState;
/*     */   private boolean hasRaytraced;
/*     */   private int rayTracePass;
/*     */   
/*     */   protected BlockStairs(IBlockState modelState) {
/*  41 */     super((modelState.getBlock()).blockMaterial);
/*  42 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)HALF, EnumHalf.BOTTOM).withProperty((IProperty)SHAPE, EnumShape.STRAIGHT));
/*  43 */     this.modelBlock = modelState.getBlock();
/*  44 */     this.modelState = modelState;
/*  45 */     setHardness(this.modelBlock.blockHardness);
/*  46 */     setResistance(this.modelBlock.blockResistance / 3.0F);
/*  47 */     setStepSound(this.modelBlock.stepSound);
/*  48 */     setLightOpacity(255);
/*  49 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
/*  54 */     if (this.hasRaytraced) {
/*     */       
/*  56 */       setBlockBounds(0.5F * (this.rayTracePass % 2), 0.5F * (this.rayTracePass / 4 % 2), 0.5F * (this.rayTracePass / 2 % 2), 0.5F + 0.5F * (this.rayTracePass % 2), 0.5F + 0.5F * (this.rayTracePass / 4 % 2), 0.5F + 0.5F * (this.rayTracePass / 2 % 2));
/*     */     }
/*     */     else {
/*     */       
/*  60 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaseCollisionBounds(IBlockAccess worldIn, BlockPos pos) {
/*  76 */     if (worldIn.getBlockState(pos).getValue((IProperty)HALF) == EnumHalf.TOP) {
/*     */       
/*  78 */       setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/*     */     else {
/*     */       
/*  82 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBlockStairs(Block blockIn) {
/*  88 */     return blockIn instanceof BlockStairs;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isSameStair(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
/*  93 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  94 */     Block block = iblockstate.getBlock();
/*  95 */     return (isBlockStairs(block) && iblockstate.getValue((IProperty)HALF) == state.getValue((IProperty)HALF) && iblockstate.getValue((IProperty)FACING) == state.getValue((IProperty)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_176307_f(IBlockAccess blockAccess, BlockPos pos) {
/* 100 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 101 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/* 102 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue((IProperty)HALF);
/* 103 */     boolean flag = (blockstairs$enumhalf == EnumHalf.TOP);
/*     */     
/* 105 */     if (enumfacing == EnumFacing.EAST) {
/*     */       
/* 107 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.east());
/* 108 */       Block block = iblockstate1.getBlock();
/*     */       
/* 110 */       if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue((IProperty)HALF))
/*     */       {
/* 112 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
/*     */         
/* 114 */         if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 116 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 119 */         if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 121 */           return flag ? 2 : 1;
/*     */         }
/*     */       }
/*     */     
/* 125 */     } else if (enumfacing == EnumFacing.WEST) {
/*     */       
/* 127 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
/* 128 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 130 */       if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue((IProperty)HALF))
/*     */       {
/* 132 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue((IProperty)FACING);
/*     */         
/* 134 */         if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 136 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 139 */         if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 141 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     
/* 145 */     } else if (enumfacing == EnumFacing.SOUTH) {
/*     */       
/* 147 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.south());
/* 148 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 150 */       if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue((IProperty)HALF))
/*     */       {
/* 152 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue((IProperty)FACING);
/*     */         
/* 154 */         if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate))
/*     */         {
/* 156 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 159 */         if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate))
/*     */         {
/* 161 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     
/* 165 */     } else if (enumfacing == EnumFacing.NORTH) {
/*     */       
/* 167 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
/* 168 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 170 */       if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue((IProperty)HALF)) {
/*     */         
/* 172 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue((IProperty)FACING);
/*     */         
/* 174 */         if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate))
/*     */         {
/* 176 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 179 */         if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate))
/*     */         {
/* 181 */           return flag ? 2 : 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_176305_g(IBlockAccess blockAccess, BlockPos pos) {
/* 191 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 192 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/* 193 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue((IProperty)HALF);
/* 194 */     boolean flag = (blockstairs$enumhalf == EnumHalf.TOP);
/*     */     
/* 196 */     if (enumfacing == EnumFacing.EAST) {
/*     */       
/* 198 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.west());
/* 199 */       Block block = iblockstate1.getBlock();
/*     */       
/* 201 */       if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue((IProperty)HALF))
/*     */       {
/* 203 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
/*     */         
/* 205 */         if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 207 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 210 */         if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 212 */           return flag ? 2 : 1;
/*     */         }
/*     */       }
/*     */     
/* 216 */     } else if (enumfacing == EnumFacing.WEST) {
/*     */       
/* 218 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
/* 219 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 221 */       if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue((IProperty)HALF))
/*     */       {
/* 223 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue((IProperty)FACING);
/*     */         
/* 225 */         if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 227 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 230 */         if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 232 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     
/* 236 */     } else if (enumfacing == EnumFacing.SOUTH) {
/*     */       
/* 238 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.north());
/* 239 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 241 */       if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue((IProperty)HALF))
/*     */       {
/* 243 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue((IProperty)FACING);
/*     */         
/* 245 */         if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate))
/*     */         {
/* 247 */           return flag ? 2 : 1;
/*     */         }
/*     */         
/* 250 */         if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate))
/*     */         {
/* 252 */           return flag ? 1 : 2;
/*     */         }
/*     */       }
/*     */     
/* 256 */     } else if (enumfacing == EnumFacing.NORTH) {
/*     */       
/* 258 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
/* 259 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 261 */       if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue((IProperty)HALF)) {
/*     */         
/* 263 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue((IProperty)FACING);
/*     */         
/* 265 */         if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate))
/*     */         {
/* 267 */           return flag ? 1 : 2;
/*     */         }
/*     */         
/* 270 */         if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate))
/*     */         {
/* 272 */           return flag ? 2 : 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_176306_h(IBlockAccess blockAccess, BlockPos pos) {
/* 282 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 283 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/* 284 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue((IProperty)HALF);
/* 285 */     boolean flag = (blockstairs$enumhalf == EnumHalf.TOP);
/* 286 */     float f = 0.5F;
/* 287 */     float f1 = 1.0F;
/*     */     
/* 289 */     if (flag) {
/*     */       
/* 291 */       f = 0.0F;
/* 292 */       f1 = 0.5F;
/*     */     } 
/*     */     
/* 295 */     float f2 = 0.0F;
/* 296 */     float f3 = 1.0F;
/* 297 */     float f4 = 0.0F;
/* 298 */     float f5 = 0.5F;
/* 299 */     boolean flag1 = true;
/*     */     
/* 301 */     if (enumfacing == EnumFacing.EAST) {
/*     */       
/* 303 */       f2 = 0.5F;
/* 304 */       f5 = 1.0F;
/* 305 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.east());
/* 306 */       Block block = iblockstate1.getBlock();
/*     */       
/* 308 */       if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue((IProperty)HALF)) {
/*     */         
/* 310 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
/*     */         
/* 312 */         if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 314 */           f5 = 0.5F;
/* 315 */           flag1 = false;
/*     */         }
/* 317 */         else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 319 */           f4 = 0.5F;
/* 320 */           flag1 = false;
/*     */         }
/*     */       
/*     */       } 
/* 324 */     } else if (enumfacing == EnumFacing.WEST) {
/*     */       
/* 326 */       f3 = 0.5F;
/* 327 */       f5 = 1.0F;
/* 328 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.west());
/* 329 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 331 */       if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue((IProperty)HALF)) {
/*     */         
/* 333 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue((IProperty)FACING);
/*     */         
/* 335 */         if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 337 */           f5 = 0.5F;
/* 338 */           flag1 = false;
/*     */         }
/* 340 */         else if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 342 */           f4 = 0.5F;
/* 343 */           flag1 = false;
/*     */         }
/*     */       
/*     */       } 
/* 347 */     } else if (enumfacing == EnumFacing.SOUTH) {
/*     */       
/* 349 */       f4 = 0.5F;
/* 350 */       f5 = 1.0F;
/* 351 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.south());
/* 352 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 354 */       if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue((IProperty)HALF)) {
/*     */         
/* 356 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue((IProperty)FACING);
/*     */         
/* 358 */         if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate))
/*     */         {
/* 360 */           f3 = 0.5F;
/* 361 */           flag1 = false;
/*     */         }
/* 363 */         else if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate))
/*     */         {
/* 365 */           f2 = 0.5F;
/* 366 */           flag1 = false;
/*     */         }
/*     */       
/*     */       } 
/* 370 */     } else if (enumfacing == EnumFacing.NORTH) {
/*     */       
/* 372 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.north());
/* 373 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 375 */       if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue((IProperty)HALF)) {
/*     */         
/* 377 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue((IProperty)FACING);
/*     */         
/* 379 */         if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
/*     */           
/* 381 */           f3 = 0.5F;
/* 382 */           flag1 = false;
/*     */         }
/* 384 */         else if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
/*     */           
/* 386 */           f2 = 0.5F;
/* 387 */           flag1 = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 392 */     setBlockBounds(f2, f, f4, f3, f1, f5);
/* 393 */     return flag1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_176304_i(IBlockAccess blockAccess, BlockPos pos) {
/* 398 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 399 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
/* 400 */     EnumHalf blockstairs$enumhalf = (EnumHalf)iblockstate.getValue((IProperty)HALF);
/* 401 */     boolean flag = (blockstairs$enumhalf == EnumHalf.TOP);
/* 402 */     float f = 0.5F;
/* 403 */     float f1 = 1.0F;
/*     */     
/* 405 */     if (flag) {
/*     */       
/* 407 */       f = 0.0F;
/* 408 */       f1 = 0.5F;
/*     */     } 
/*     */     
/* 411 */     float f2 = 0.0F;
/* 412 */     float f3 = 0.5F;
/* 413 */     float f4 = 0.5F;
/* 414 */     float f5 = 1.0F;
/* 415 */     boolean flag1 = false;
/*     */     
/* 417 */     if (enumfacing == EnumFacing.EAST) {
/*     */       
/* 419 */       IBlockState iblockstate1 = blockAccess.getBlockState(pos.west());
/* 420 */       Block block = iblockstate1.getBlock();
/*     */       
/* 422 */       if (isBlockStairs(block) && blockstairs$enumhalf == iblockstate1.getValue((IProperty)HALF)) {
/*     */         
/* 424 */         EnumFacing enumfacing1 = (EnumFacing)iblockstate1.getValue((IProperty)FACING);
/*     */         
/* 426 */         if (enumfacing1 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 428 */           f4 = 0.0F;
/* 429 */           f5 = 0.5F;
/* 430 */           flag1 = true;
/*     */         }
/* 432 */         else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 434 */           f4 = 0.5F;
/* 435 */           f5 = 1.0F;
/* 436 */           flag1 = true;
/*     */         }
/*     */       
/*     */       } 
/* 440 */     } else if (enumfacing == EnumFacing.WEST) {
/*     */       
/* 442 */       IBlockState iblockstate2 = blockAccess.getBlockState(pos.east());
/* 443 */       Block block1 = iblockstate2.getBlock();
/*     */       
/* 445 */       if (isBlockStairs(block1) && blockstairs$enumhalf == iblockstate2.getValue((IProperty)HALF)) {
/*     */         
/* 447 */         f2 = 0.5F;
/* 448 */         f3 = 1.0F;
/* 449 */         EnumFacing enumfacing2 = (EnumFacing)iblockstate2.getValue((IProperty)FACING);
/*     */         
/* 451 */         if (enumfacing2 == EnumFacing.NORTH && !isSameStair(blockAccess, pos.north(), iblockstate))
/*     */         {
/* 453 */           f4 = 0.0F;
/* 454 */           f5 = 0.5F;
/* 455 */           flag1 = true;
/*     */         }
/* 457 */         else if (enumfacing2 == EnumFacing.SOUTH && !isSameStair(blockAccess, pos.south(), iblockstate))
/*     */         {
/* 459 */           f4 = 0.5F;
/* 460 */           f5 = 1.0F;
/* 461 */           flag1 = true;
/*     */         }
/*     */       
/*     */       } 
/* 465 */     } else if (enumfacing == EnumFacing.SOUTH) {
/*     */       
/* 467 */       IBlockState iblockstate3 = blockAccess.getBlockState(pos.north());
/* 468 */       Block block2 = iblockstate3.getBlock();
/*     */       
/* 470 */       if (isBlockStairs(block2) && blockstairs$enumhalf == iblockstate3.getValue((IProperty)HALF)) {
/*     */         
/* 472 */         f4 = 0.0F;
/* 473 */         f5 = 0.5F;
/* 474 */         EnumFacing enumfacing3 = (EnumFacing)iblockstate3.getValue((IProperty)FACING);
/*     */         
/* 476 */         if (enumfacing3 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate))
/*     */         {
/* 478 */           flag1 = true;
/*     */         }
/* 480 */         else if (enumfacing3 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate))
/*     */         {
/* 482 */           f2 = 0.5F;
/* 483 */           f3 = 1.0F;
/* 484 */           flag1 = true;
/*     */         }
/*     */       
/*     */       } 
/* 488 */     } else if (enumfacing == EnumFacing.NORTH) {
/*     */       
/* 490 */       IBlockState iblockstate4 = blockAccess.getBlockState(pos.south());
/* 491 */       Block block3 = iblockstate4.getBlock();
/*     */       
/* 493 */       if (isBlockStairs(block3) && blockstairs$enumhalf == iblockstate4.getValue((IProperty)HALF)) {
/*     */         
/* 495 */         EnumFacing enumfacing4 = (EnumFacing)iblockstate4.getValue((IProperty)FACING);
/*     */         
/* 497 */         if (enumfacing4 == EnumFacing.WEST && !isSameStair(blockAccess, pos.west(), iblockstate)) {
/*     */           
/* 499 */           flag1 = true;
/*     */         }
/* 501 */         else if (enumfacing4 == EnumFacing.EAST && !isSameStair(blockAccess, pos.east(), iblockstate)) {
/*     */           
/* 503 */           f2 = 0.5F;
/* 504 */           f3 = 1.0F;
/* 505 */           flag1 = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 510 */     if (flag1)
/*     */     {
/* 512 */       setBlockBounds(f2, f, f4, f3, f1, f5);
/*     */     }
/*     */     
/* 515 */     return flag1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
/* 520 */     setBaseCollisionBounds((IBlockAccess)worldIn, pos);
/* 521 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/* 522 */     boolean flag = func_176306_h((IBlockAccess)worldIn, pos);
/* 523 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     
/* 525 */     if (flag && func_176304_i((IBlockAccess)worldIn, pos))
/*     */     {
/* 527 */       super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */     }
/*     */     
/* 530 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 535 */     this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
/* 540 */     this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
/* 545 */     this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
/* 550 */     return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getExplosionResistance(Entity exploder) {
/* 555 */     return this.modelBlock.getExplosionResistance(exploder);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 560 */     return this.modelBlock.getBlockLayer();
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/* 565 */     return this.modelBlock.tickRate(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/* 570 */     return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
/* 575 */     return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCollidable() {
/* 580 */     return this.modelBlock.isCollidable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
/* 585 */     return this.modelBlock.canCollideCheck(state, hitIfLiquid);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 590 */     return this.modelBlock.canPlaceBlockAt(worldIn, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 595 */     onNeighborBlockChange(worldIn, pos, this.modelState, Blocks.air);
/* 596 */     this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 601 */     this.modelBlock.breakBlock(worldIn, pos, this.modelState);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
/* 606 */     this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 611 */     this.modelBlock.updateTick(worldIn, pos, state, rand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 616 */     return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
/* 621 */     this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/* 626 */     return this.modelBlock.getMapColor(this.modelState);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 631 */     IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
/* 632 */     iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)SHAPE, EnumShape.STRAIGHT);
/* 633 */     return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D)) ? iblockstate.withProperty((IProperty)HALF, EnumHalf.BOTTOM) : iblockstate.withProperty((IProperty)HALF, EnumHalf.TOP);
/*     */   }
/*     */ 
/*     */   
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
/* 638 */     MovingObjectPosition[] amovingobjectposition = new MovingObjectPosition[8];
/* 639 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 640 */     int i = ((EnumFacing)iblockstate.getValue((IProperty)FACING)).getHorizontalIndex();
/* 641 */     boolean flag = (iblockstate.getValue((IProperty)HALF) == EnumHalf.TOP);
/* 642 */     int[] aint = field_150150_a[i + (flag ? 4 : 0)];
/* 643 */     this.hasRaytraced = true;
/*     */     
/* 645 */     for (int j = 0; j < 8; j++) {
/*     */       
/* 647 */       this.rayTracePass = j;
/*     */       
/* 649 */       if (Arrays.binarySearch(aint, j) < 0)
/*     */       {
/* 651 */         amovingobjectposition[j] = super.collisionRayTrace(worldIn, pos, start, end);
/*     */       }
/*     */     } 
/*     */     
/* 655 */     for (int k : aint)
/*     */     {
/* 657 */       amovingobjectposition[k] = null;
/*     */     }
/*     */     
/* 660 */     MovingObjectPosition movingobjectposition1 = null;
/* 661 */     double d1 = 0.0D;
/*     */     
/* 663 */     for (MovingObjectPosition movingobjectposition : amovingobjectposition) {
/*     */       
/* 665 */       if (movingobjectposition != null) {
/*     */         
/* 667 */         double d0 = movingobjectposition.hitVec.squareDistanceTo(end);
/*     */         
/* 669 */         if (d0 > d1) {
/*     */           
/* 671 */           movingobjectposition1 = movingobjectposition;
/* 672 */           d1 = d0;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 677 */     return movingobjectposition1;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 682 */     IBlockState iblockstate = getDefaultState().withProperty((IProperty)HALF, ((meta & 0x4) > 0) ? EnumHalf.TOP : EnumHalf.BOTTOM);
/* 683 */     iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.getFront(5 - (meta & 0x3)));
/* 684 */     return iblockstate;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 689 */     int i = 0;
/*     */     
/* 691 */     if (state.getValue((IProperty)HALF) == EnumHalf.TOP)
/*     */     {
/* 693 */       i |= 0x4;
/*     */     }
/*     */     
/* 696 */     i |= 5 - ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
/* 697 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/* 702 */     if (func_176306_h(worldIn, pos)) {
/*     */       
/* 704 */       switch (func_176305_g(worldIn, pos)) {
/*     */         
/*     */         case 0:
/* 707 */           state = state.withProperty((IProperty)SHAPE, EnumShape.STRAIGHT);
/*     */           break;
/*     */         
/*     */         case 1:
/* 711 */           state = state.withProperty((IProperty)SHAPE, EnumShape.INNER_RIGHT);
/*     */           break;
/*     */         
/*     */         case 2:
/* 715 */           state = state.withProperty((IProperty)SHAPE, EnumShape.INNER_LEFT);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } else {
/* 720 */       switch (func_176307_f(worldIn, pos)) {
/*     */         
/*     */         case 0:
/* 723 */           state = state.withProperty((IProperty)SHAPE, EnumShape.STRAIGHT);
/*     */           break;
/*     */         
/*     */         case 1:
/* 727 */           state = state.withProperty((IProperty)SHAPE, EnumShape.OUTER_RIGHT);
/*     */           break;
/*     */         
/*     */         case 2:
/* 731 */           state = state.withProperty((IProperty)SHAPE, EnumShape.OUTER_LEFT);
/*     */           break;
/*     */       } 
/*     */     } 
/* 735 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 740 */     return new BlockState(this, new IProperty[] { (IProperty)FACING, (IProperty)HALF, (IProperty)SHAPE });
/*     */   }
/*     */   
/*     */   public enum EnumHalf
/*     */     implements IStringSerializable {
/* 745 */     TOP("top"),
/* 746 */     BOTTOM("bottom");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     EnumHalf(String name) {
/* 752 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 757 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 762 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EnumShape
/*     */     implements IStringSerializable {
/* 768 */     STRAIGHT("straight"),
/* 769 */     INNER_LEFT("inner_left"),
/* 770 */     INNER_RIGHT("inner_right"),
/* 771 */     OUTER_LEFT("outer_left"),
/* 772 */     OUTER_RIGHT("outer_right");
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     EnumShape(String name) {
/* 778 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 783 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 788 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockStairs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */