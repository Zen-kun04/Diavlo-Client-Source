/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeColorHelper;
/*     */ 
/*     */ public abstract class BlockLiquid
/*     */   extends Block {
/*  25 */   public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
/*     */ 
/*     */   
/*     */   protected BlockLiquid(Material materialIn) {
/*  29 */     super(materialIn);
/*  30 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LEVEL, Integer.valueOf(0)));
/*  31 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  32 */     setTickRandomly(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/*  37 */     return (this.blockMaterial != Material.lava);
/*     */   }
/*     */ 
/*     */   
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
/*  42 */     return (this.blockMaterial == Material.water) ? BiomeColorHelper.getWaterColorAtPos(worldIn, pos) : 16777215;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getLiquidHeightPercent(int meta) {
/*  47 */     if (meta >= 8)
/*     */     {
/*  49 */       meta = 0;
/*     */     }
/*     */     
/*  52 */     return (meta + 1) / 9.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLevel(IBlockAccess worldIn, BlockPos pos) {
/*  57 */     return (worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial) ? ((Integer)worldIn.getBlockState(pos).getValue((IProperty)LEVEL)).intValue() : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getEffectiveFlowDecay(IBlockAccess worldIn, BlockPos pos) {
/*  62 */     int i = getLevel(worldIn, pos);
/*  63 */     return (i >= 8) ? 0 : i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
/*  78 */     return (hitIfLiquid && ((Integer)state.getValue((IProperty)LEVEL)).intValue() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/*  83 */     Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
/*  84 */     return (material == this.blockMaterial) ? false : ((side == EnumFacing.UP) ? true : ((material == Material.ice) ? false : super.isBlockSolid(worldIn, pos, side)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
/*  89 */     return (worldIn.getBlockState(pos).getBlock().getMaterial() == this.blockMaterial) ? false : ((side == EnumFacing.UP) ? true : super.shouldSideBeRendered(worldIn, pos, side));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderSides(IBlockAccess blockAccess, BlockPos pos) {
/*  94 */     for (int i = -1; i <= 1; i++) {
/*     */       
/*  96 */       for (int j = -1; j <= 1; j++) {
/*     */         
/*  98 */         IBlockState iblockstate = blockAccess.getBlockState(pos.add(i, 0, j));
/*  99 */         Block block = iblockstate.getBlock();
/* 100 */         Material material = block.getMaterial();
/*     */         
/* 102 */         if (material != this.blockMaterial && !block.isFullBlock())
/*     */         {
/* 104 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/* 119 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/* 129 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getFlowVector(IBlockAccess worldIn, BlockPos pos) {
/* 134 */     Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
/* 135 */     int i = getEffectiveFlowDecay(worldIn, pos);
/*     */     
/* 137 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*     */       
/* 139 */       BlockPos blockpos = pos.offset(enumfacing);
/* 140 */       int j = getEffectiveFlowDecay(worldIn, blockpos);
/*     */       
/* 142 */       if (j < 0) {
/*     */         
/* 144 */         if (!worldIn.getBlockState(blockpos).getBlock().getMaterial().blocksMovement()) {
/*     */           
/* 146 */           j = getEffectiveFlowDecay(worldIn, blockpos.down());
/*     */           
/* 148 */           if (j >= 0) {
/*     */             
/* 150 */             int k = j - i - 8;
/* 151 */             vec3 = vec3.addVector(((blockpos.getX() - pos.getX()) * k), ((blockpos.getY() - pos.getY()) * k), ((blockpos.getZ() - pos.getZ()) * k));
/*     */           } 
/*     */         }  continue;
/*     */       } 
/* 155 */       if (j >= 0) {
/*     */         
/* 157 */         int l = j - i;
/* 158 */         vec3 = vec3.addVector(((blockpos.getX() - pos.getX()) * l), ((blockpos.getY() - pos.getY()) * l), ((blockpos.getZ() - pos.getZ()) * l));
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     if (((Integer)worldIn.getBlockState(pos).getValue((IProperty)LEVEL)).intValue() >= 8)
/*     */     {
/* 164 */       for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
/*     */         
/* 166 */         BlockPos blockpos1 = pos.offset(enumfacing1);
/*     */         
/* 168 */         if (isBlockSolid(worldIn, blockpos1, enumfacing1) || isBlockSolid(worldIn, blockpos1.up(), enumfacing1)) {
/*     */           
/* 170 */           vec3 = vec3.normalize().addVector(0.0D, -6.0D, 0.0D);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 176 */     return vec3.normalize();
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
/* 181 */     return motion.add(getFlowVector((IBlockAccess)worldIn, pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/* 186 */     return (this.blockMaterial == Material.water) ? 5 : ((this.blockMaterial == Material.lava) ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
/* 191 */     int i = worldIn.getCombinedLight(pos, 0);
/* 192 */     int j = worldIn.getCombinedLight(pos.up(), 0);
/* 193 */     int k = i & 0xFF;
/* 194 */     int l = j & 0xFF;
/* 195 */     int i1 = i >> 16 & 0xFF;
/* 196 */     int j1 = j >> 16 & 0xFF;
/* 197 */     return ((k > l) ? k : l) | ((i1 > j1) ? i1 : j1) << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/* 202 */     return (this.blockMaterial == Material.water) ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 207 */     double d0 = pos.getX();
/* 208 */     double d1 = pos.getY();
/* 209 */     double d2 = pos.getZ();
/*     */     
/* 211 */     if (this.blockMaterial == Material.water) {
/*     */       
/* 213 */       int i = ((Integer)state.getValue((IProperty)LEVEL)).intValue();
/*     */       
/* 215 */       if (i > 0 && i < 8) {
/*     */         
/* 217 */         if (rand.nextInt(64) == 0)
/*     */         {
/* 219 */           worldIn.playSound(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() * 1.0F + 0.5F, false);
/*     */         }
/*     */       }
/* 222 */       else if (rand.nextInt(10) == 0) {
/*     */         
/* 224 */         worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, d0 + rand.nextFloat(), d1 + rand.nextFloat(), d2 + rand.nextFloat(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       } 
/*     */     } 
/*     */     
/* 228 */     if (this.blockMaterial == Material.lava && worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube()) {
/*     */       
/* 230 */       if (rand.nextInt(100) == 0) {
/*     */         
/* 232 */         double d8 = d0 + rand.nextFloat();
/* 233 */         double d4 = d1 + this.maxY;
/* 234 */         double d6 = d2 + rand.nextFloat();
/* 235 */         worldIn.spawnParticle(EnumParticleTypes.LAVA, d8, d4, d6, 0.0D, 0.0D, 0.0D, new int[0]);
/* 236 */         worldIn.playSound(d8, d4, d6, "liquid.lavapop", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
/*     */       } 
/*     */       
/* 239 */       if (rand.nextInt(200) == 0)
/*     */       {
/* 241 */         worldIn.playSound(d0, d1, d2, "liquid.lava", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
/*     */       }
/*     */     } 
/*     */     
/* 245 */     if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.down())) {
/*     */       
/* 247 */       Material material = worldIn.getBlockState(pos.down(2)).getBlock().getMaterial();
/*     */       
/* 249 */       if (!material.blocksMovement() && !material.isLiquid()) {
/*     */         
/* 251 */         double d3 = d0 + rand.nextFloat();
/* 252 */         double d5 = d1 - 1.05D;
/* 253 */         double d7 = d2 + rand.nextFloat();
/*     */         
/* 255 */         if (this.blockMaterial == Material.water) {
/*     */           
/* 257 */           worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */         else {
/*     */           
/* 261 */           worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getFlowDirection(IBlockAccess worldIn, BlockPos pos, Material materialIn) {
/* 269 */     Vec3 vec3 = getFlowingBlock(materialIn).getFlowVector(worldIn, pos);
/* 270 */     return (vec3.xCoord == 0.0D && vec3.zCoord == 0.0D) ? -1000.0D : (MathHelper.atan2(vec3.zCoord, vec3.xCoord) - 1.5707963267948966D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/* 275 */     checkForMixing(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 280 */     checkForMixing(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkForMixing(World worldIn, BlockPos pos, IBlockState state) {
/* 285 */     if (this.blockMaterial == Material.lava) {
/*     */       
/* 287 */       boolean flag = false;
/*     */       
/* 289 */       for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */         
/* 291 */         if (enumfacing != EnumFacing.DOWN && worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getMaterial() == Material.water) {
/*     */           
/* 293 */           flag = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 298 */       if (flag) {
/*     */         
/* 300 */         Integer integer = (Integer)state.getValue((IProperty)LEVEL);
/*     */         
/* 302 */         if (integer.intValue() == 0) {
/*     */           
/* 304 */           worldIn.setBlockState(pos, Blocks.obsidian.getDefaultState());
/* 305 */           triggerMixEffects(worldIn, pos);
/* 306 */           return true;
/*     */         } 
/*     */         
/* 309 */         if (integer.intValue() <= 4) {
/*     */           
/* 311 */           worldIn.setBlockState(pos, Blocks.cobblestone.getDefaultState());
/* 312 */           triggerMixEffects(worldIn, pos);
/* 313 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 318 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void triggerMixEffects(World worldIn, BlockPos pos) {
/* 323 */     double d0 = pos.getX();
/* 324 */     double d1 = pos.getY();
/* 325 */     double d2 = pos.getZ();
/* 326 */     worldIn.playSoundEffect(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
/*     */     
/* 328 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 330 */       worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 336 */     return getDefaultState().withProperty((IProperty)LEVEL, Integer.valueOf(meta));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 341 */     return ((Integer)state.getValue((IProperty)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 346 */     return new BlockState(this, new IProperty[] { (IProperty)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockDynamicLiquid getFlowingBlock(Material materialIn) {
/* 351 */     if (materialIn == Material.water)
/*     */     {
/* 353 */       return Blocks.flowing_water;
/*     */     }
/* 355 */     if (materialIn == Material.lava)
/*     */     {
/* 357 */       return Blocks.flowing_lava;
/*     */     }
/*     */ 
/*     */     
/* 361 */     throw new IllegalArgumentException("Invalid material");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BlockStaticLiquid getStaticBlock(Material materialIn) {
/* 367 */     if (materialIn == Material.water)
/*     */     {
/* 369 */       return Blocks.water;
/*     */     }
/* 371 */     if (materialIn == Material.lava)
/*     */     {
/* 373 */       return Blocks.lava;
/*     */     }
/*     */ 
/*     */     
/* 377 */     throw new IllegalArgumentException("Invalid material");
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */