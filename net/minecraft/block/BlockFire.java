/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFire
/*     */   extends Block
/*     */ {
/*  25 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
/*  26 */   public static final PropertyBool FLIP = PropertyBool.create("flip");
/*  27 */   public static final PropertyBool ALT = PropertyBool.create("alt");
/*  28 */   public static final PropertyBool NORTH = PropertyBool.create("north");
/*  29 */   public static final PropertyBool EAST = PropertyBool.create("east");
/*  30 */   public static final PropertyBool SOUTH = PropertyBool.create("south");
/*  31 */   public static final PropertyBool WEST = PropertyBool.create("west");
/*  32 */   public static final PropertyInteger UPPER = PropertyInteger.create("upper", 0, 2);
/*  33 */   private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
/*  34 */   private final Map<Block, Integer> flammabilities = Maps.newIdentityHashMap();
/*     */ 
/*     */   
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
/*  38 */     int i = pos.getX();
/*  39 */     int j = pos.getY();
/*  40 */     int k = pos.getZ();
/*     */     
/*  42 */     if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !Blocks.fire.canCatchFire(worldIn, pos.down())) {
/*     */       
/*  44 */       boolean flag = ((i + j + k & 0x1) == 1);
/*  45 */       boolean flag1 = ((i / 2 + j / 2 + k / 2 & 0x1) == 1);
/*  46 */       int l = 0;
/*     */       
/*  48 */       if (canCatchFire(worldIn, pos.up()))
/*     */       {
/*  50 */         l = flag ? 1 : 2;
/*     */       }
/*     */       
/*  53 */       return state.withProperty((IProperty)NORTH, Boolean.valueOf(canCatchFire(worldIn, pos.north()))).withProperty((IProperty)EAST, Boolean.valueOf(canCatchFire(worldIn, pos.east()))).withProperty((IProperty)SOUTH, Boolean.valueOf(canCatchFire(worldIn, pos.south()))).withProperty((IProperty)WEST, Boolean.valueOf(canCatchFire(worldIn, pos.west()))).withProperty((IProperty)UPPER, Integer.valueOf(l)).withProperty((IProperty)FLIP, Boolean.valueOf(flag1)).withProperty((IProperty)ALT, Boolean.valueOf(flag));
/*     */     } 
/*     */ 
/*     */     
/*  57 */     return getDefaultState();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockFire() {
/*  63 */     super(Material.fire);
/*  64 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)).withProperty((IProperty)FLIP, Boolean.valueOf(false)).withProperty((IProperty)ALT, Boolean.valueOf(false)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)).withProperty((IProperty)UPPER, Integer.valueOf(0)));
/*  65 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init() {
/*  70 */     Blocks.fire.setFireInfo(Blocks.planks, 5, 20);
/*  71 */     Blocks.fire.setFireInfo(Blocks.double_wooden_slab, 5, 20);
/*  72 */     Blocks.fire.setFireInfo(Blocks.wooden_slab, 5, 20);
/*  73 */     Blocks.fire.setFireInfo(Blocks.oak_fence_gate, 5, 20);
/*  74 */     Blocks.fire.setFireInfo(Blocks.spruce_fence_gate, 5, 20);
/*  75 */     Blocks.fire.setFireInfo(Blocks.birch_fence_gate, 5, 20);
/*  76 */     Blocks.fire.setFireInfo(Blocks.jungle_fence_gate, 5, 20);
/*  77 */     Blocks.fire.setFireInfo(Blocks.dark_oak_fence_gate, 5, 20);
/*  78 */     Blocks.fire.setFireInfo(Blocks.acacia_fence_gate, 5, 20);
/*  79 */     Blocks.fire.setFireInfo(Blocks.oak_fence, 5, 20);
/*  80 */     Blocks.fire.setFireInfo(Blocks.spruce_fence, 5, 20);
/*  81 */     Blocks.fire.setFireInfo(Blocks.birch_fence, 5, 20);
/*  82 */     Blocks.fire.setFireInfo(Blocks.jungle_fence, 5, 20);
/*  83 */     Blocks.fire.setFireInfo(Blocks.dark_oak_fence, 5, 20);
/*  84 */     Blocks.fire.setFireInfo(Blocks.acacia_fence, 5, 20);
/*  85 */     Blocks.fire.setFireInfo(Blocks.oak_stairs, 5, 20);
/*  86 */     Blocks.fire.setFireInfo(Blocks.birch_stairs, 5, 20);
/*  87 */     Blocks.fire.setFireInfo(Blocks.spruce_stairs, 5, 20);
/*  88 */     Blocks.fire.setFireInfo(Blocks.jungle_stairs, 5, 20);
/*  89 */     Blocks.fire.setFireInfo(Blocks.log, 5, 5);
/*  90 */     Blocks.fire.setFireInfo(Blocks.log2, 5, 5);
/*  91 */     Blocks.fire.setFireInfo(Blocks.leaves, 30, 60);
/*  92 */     Blocks.fire.setFireInfo(Blocks.leaves2, 30, 60);
/*  93 */     Blocks.fire.setFireInfo(Blocks.bookshelf, 30, 20);
/*  94 */     Blocks.fire.setFireInfo(Blocks.tnt, 15, 100);
/*  95 */     Blocks.fire.setFireInfo(Blocks.tallgrass, 60, 100);
/*  96 */     Blocks.fire.setFireInfo(Blocks.double_plant, 60, 100);
/*  97 */     Blocks.fire.setFireInfo(Blocks.yellow_flower, 60, 100);
/*  98 */     Blocks.fire.setFireInfo(Blocks.red_flower, 60, 100);
/*  99 */     Blocks.fire.setFireInfo(Blocks.deadbush, 60, 100);
/* 100 */     Blocks.fire.setFireInfo(Blocks.wool, 30, 60);
/* 101 */     Blocks.fire.setFireInfo(Blocks.vine, 15, 100);
/* 102 */     Blocks.fire.setFireInfo(Blocks.coal_block, 5, 5);
/* 103 */     Blocks.fire.setFireInfo(Blocks.hay_block, 60, 20);
/* 104 */     Blocks.fire.setFireInfo(Blocks.carpet, 60, 20);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFireInfo(Block blockIn, int encouragement, int flammability) {
/* 109 */     this.encouragements.put(blockIn, Integer.valueOf(encouragement));
/* 110 */     this.flammabilities.put(blockIn, Integer.valueOf(flammability));
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/* 130 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/* 135 */     return 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 140 */     if (worldIn.getGameRules().getBoolean("doFireTick")) {
/*     */       
/* 142 */       if (!canPlaceBlockAt(worldIn, pos))
/*     */       {
/* 144 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       
/* 147 */       Block block = worldIn.getBlockState(pos.down()).getBlock();
/* 148 */       boolean flag = (block == Blocks.netherrack);
/*     */       
/* 150 */       if (worldIn.provider instanceof net.minecraft.world.WorldProviderEnd && block == Blocks.bedrock)
/*     */       {
/* 152 */         flag = true;
/*     */       }
/*     */       
/* 155 */       if (!flag && worldIn.isRaining() && canDie(worldIn, pos)) {
/*     */         
/* 157 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       else {
/*     */         
/* 161 */         int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */         
/* 163 */         if (i < 15) {
/*     */           
/* 165 */           state = state.withProperty((IProperty)AGE, Integer.valueOf(i + rand.nextInt(3) / 2));
/* 166 */           worldIn.setBlockState(pos, state, 4);
/*     */         } 
/*     */         
/* 169 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + rand.nextInt(10));
/*     */         
/* 171 */         if (!flag) {
/*     */           
/* 173 */           if (!canNeighborCatchFire(worldIn, pos)) {
/*     */             
/* 175 */             if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) || i > 3)
/*     */             {
/* 177 */               worldIn.setBlockToAir(pos);
/*     */             }
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/* 183 */           if (!canCatchFire((IBlockAccess)worldIn, pos.down()) && i == 15 && rand.nextInt(4) == 0) {
/*     */             
/* 185 */             worldIn.setBlockToAir(pos);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 190 */         boolean flag1 = worldIn.isBlockinHighHumidity(pos);
/* 191 */         int j = 0;
/*     */         
/* 193 */         if (flag1)
/*     */         {
/* 195 */           j = -50;
/*     */         }
/*     */         
/* 198 */         catchOnFire(worldIn, pos.east(), 300 + j, rand, i);
/* 199 */         catchOnFire(worldIn, pos.west(), 300 + j, rand, i);
/* 200 */         catchOnFire(worldIn, pos.down(), 250 + j, rand, i);
/* 201 */         catchOnFire(worldIn, pos.up(), 250 + j, rand, i);
/* 202 */         catchOnFire(worldIn, pos.north(), 300 + j, rand, i);
/* 203 */         catchOnFire(worldIn, pos.south(), 300 + j, rand, i);
/*     */         
/* 205 */         for (int k = -1; k <= 1; k++) {
/*     */           
/* 207 */           for (int l = -1; l <= 1; l++) {
/*     */             
/* 209 */             for (int i1 = -1; i1 <= 4; i1++) {
/*     */               
/* 211 */               if (k != 0 || i1 != 0 || l != 0) {
/*     */                 
/* 213 */                 int j1 = 100;
/*     */                 
/* 215 */                 if (i1 > 1)
/*     */                 {
/* 217 */                   j1 += (i1 - 1) * 100;
/*     */                 }
/*     */                 
/* 220 */                 BlockPos blockpos = pos.add(k, i1, l);
/* 221 */                 int k1 = getNeighborEncouragement(worldIn, blockpos);
/*     */                 
/* 223 */                 if (k1 > 0) {
/*     */                   
/* 225 */                   int l1 = (k1 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (i + 30);
/*     */                   
/* 227 */                   if (flag1)
/*     */                   {
/* 229 */                     l1 /= 2;
/*     */                   }
/*     */                   
/* 232 */                   if (l1 > 0 && rand.nextInt(j1) <= l1 && (!worldIn.isRaining() || !canDie(worldIn, blockpos))) {
/*     */                     
/* 234 */                     int i2 = i + rand.nextInt(5) / 4;
/*     */                     
/* 236 */                     if (i2 > 15)
/*     */                     {
/* 238 */                       i2 = 15;
/*     */                     }
/*     */                     
/* 241 */                     worldIn.setBlockState(blockpos, state.withProperty((IProperty)AGE, Integer.valueOf(i2)), 3);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDie(World worldIn, BlockPos pos) {
/* 254 */     return (worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdates() {
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getFlammability(Block blockIn) {
/* 264 */     Integer integer = this.flammabilities.get(blockIn);
/* 265 */     return (integer == null) ? 0 : integer.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private int getEncouragement(Block blockIn) {
/* 270 */     Integer integer = this.encouragements.get(blockIn);
/* 271 */     return (integer == null) ? 0 : integer.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private void catchOnFire(World worldIn, BlockPos pos, int chance, Random random, int age) {
/* 276 */     int i = getFlammability(worldIn.getBlockState(pos).getBlock());
/*     */     
/* 278 */     if (random.nextInt(chance) < i) {
/*     */       
/* 280 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */       
/* 282 */       if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
/*     */         
/* 284 */         int j = age + random.nextInt(5) / 4;
/*     */         
/* 286 */         if (j > 15)
/*     */         {
/* 288 */           j = 15;
/*     */         }
/*     */         
/* 291 */         worldIn.setBlockState(pos, getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(j)), 3);
/*     */       }
/*     */       else {
/*     */         
/* 295 */         worldIn.setBlockToAir(pos);
/*     */       } 
/*     */       
/* 298 */       if (iblockstate.getBlock() == Blocks.tnt)
/*     */       {
/* 300 */         Blocks.tnt.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty((IProperty)BlockTNT.EXPLODE, Boolean.valueOf(true)));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
/* 307 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */       
/* 309 */       if (canCatchFire((IBlockAccess)worldIn, pos.offset(enumfacing)))
/*     */       {
/* 311 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getNeighborEncouragement(World worldIn, BlockPos pos) {
/* 320 */     if (!worldIn.isAirBlock(pos))
/*     */     {
/* 322 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 326 */     int i = 0;
/*     */     
/* 328 */     for (EnumFacing enumfacing : EnumFacing.values())
/*     */     {
/* 330 */       i = Math.max(getEncouragement(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
/*     */     }
/*     */     
/* 333 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCollidable() {
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos) {
/* 344 */     return (getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 349 */     return (World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) || canNeighborCatchFire(worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 354 */     if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && !canNeighborCatchFire(worldIn, pos))
/*     */     {
/* 356 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 362 */     if (worldIn.provider.getDimensionId() > 0 || !Blocks.portal.func_176548_d(worldIn, pos))
/*     */     {
/* 364 */       if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && !canNeighborCatchFire(worldIn, pos)) {
/*     */         
/* 366 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       else {
/*     */         
/* 370 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + worldIn.rand.nextInt(10));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 377 */     if (rand.nextInt(24) == 0)
/*     */     {
/* 379 */       worldIn.playSound((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
/*     */     }
/*     */     
/* 382 */     if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down()) && !Blocks.fire.canCatchFire((IBlockAccess)worldIn, pos.down())) {
/*     */       
/* 384 */       if (Blocks.fire.canCatchFire((IBlockAccess)worldIn, pos.west()))
/*     */       {
/* 386 */         for (int j = 0; j < 2; j++) {
/*     */           
/* 388 */           double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612D;
/* 389 */           double d8 = pos.getY() + rand.nextDouble();
/* 390 */           double d13 = pos.getZ() + rand.nextDouble();
/* 391 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         } 
/*     */       }
/*     */       
/* 395 */       if (Blocks.fire.canCatchFire((IBlockAccess)worldIn, pos.east()))
/*     */       {
/* 397 */         for (int k = 0; k < 2; k++) {
/*     */           
/* 399 */           double d4 = (pos.getX() + 1) - rand.nextDouble() * 0.10000000149011612D;
/* 400 */           double d9 = pos.getY() + rand.nextDouble();
/* 401 */           double d14 = pos.getZ() + rand.nextDouble();
/* 402 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         } 
/*     */       }
/*     */       
/* 406 */       if (Blocks.fire.canCatchFire((IBlockAccess)worldIn, pos.north()))
/*     */       {
/* 408 */         for (int l = 0; l < 2; l++) {
/*     */           
/* 410 */           double d5 = pos.getX() + rand.nextDouble();
/* 411 */           double d10 = pos.getY() + rand.nextDouble();
/* 412 */           double d15 = pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
/* 413 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         } 
/*     */       }
/*     */       
/* 417 */       if (Blocks.fire.canCatchFire((IBlockAccess)worldIn, pos.south()))
/*     */       {
/* 419 */         for (int i1 = 0; i1 < 2; i1++) {
/*     */           
/* 421 */           double d6 = pos.getX() + rand.nextDouble();
/* 422 */           double d11 = pos.getY() + rand.nextDouble();
/* 423 */           double d16 = (pos.getZ() + 1) - rand.nextDouble() * 0.10000000149011612D;
/* 424 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         } 
/*     */       }
/*     */       
/* 428 */       if (Blocks.fire.canCatchFire((IBlockAccess)worldIn, pos.up()))
/*     */       {
/* 430 */         for (int j1 = 0; j1 < 2; j1++)
/*     */         {
/* 432 */           double d7 = pos.getX() + rand.nextDouble();
/* 433 */           double d12 = (pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
/* 434 */           double d17 = pos.getZ() + rand.nextDouble();
/* 435 */           worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       
/*     */       }
/*     */     } else {
/*     */       
/* 441 */       for (int i = 0; i < 3; i++) {
/*     */         
/* 443 */         double d0 = pos.getX() + rand.nextDouble();
/* 444 */         double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
/* 445 */         double d2 = pos.getZ() + rand.nextDouble();
/* 446 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MapColor getMapColor(IBlockState state) {
/* 453 */     return MapColor.tntColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 458 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 463 */     return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 468 */     return ((Integer)state.getValue((IProperty)AGE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 473 */     return new BlockState(this, new IProperty[] { (IProperty)AGE, (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST, (IProperty)UPPER, (IProperty)FLIP, (IProperty)ALT });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */