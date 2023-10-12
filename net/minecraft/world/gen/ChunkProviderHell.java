/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFalling;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockHelper;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.feature.WorldGenFire;
/*     */ import net.minecraft.world.gen.feature.WorldGenGlowStone1;
/*     */ import net.minecraft.world.gen.feature.WorldGenGlowStone2;
/*     */ import net.minecraft.world.gen.feature.WorldGenHellLava;
/*     */ import net.minecraft.world.gen.feature.WorldGenMinable;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ import net.minecraft.world.gen.structure.MapGenNetherBridge;
/*     */ 
/*     */ public class ChunkProviderHell
/*     */   implements IChunkProvider {
/*     */   private final World worldObj;
/*  33 */   private double[] slowsandNoise = new double[256]; private final boolean field_177466_i; private final Random hellRNG;
/*  34 */   private double[] gravelNoise = new double[256];
/*  35 */   private double[] netherrackExclusivityNoise = new double[256];
/*     */   private double[] noiseField;
/*     */   private final NoiseGeneratorOctaves netherNoiseGen1;
/*     */   private final NoiseGeneratorOctaves netherNoiseGen2;
/*     */   private final NoiseGeneratorOctaves netherNoiseGen3;
/*     */   private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
/*     */   private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
/*     */   public final NoiseGeneratorOctaves netherNoiseGen6;
/*     */   public final NoiseGeneratorOctaves netherNoiseGen7;
/*  44 */   private final WorldGenFire field_177470_t = new WorldGenFire();
/*  45 */   private final WorldGenGlowStone1 field_177469_u = new WorldGenGlowStone1();
/*  46 */   private final WorldGenGlowStone2 field_177468_v = new WorldGenGlowStone2();
/*  47 */   private final WorldGenerator field_177467_w = (WorldGenerator)new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, (Predicate)BlockHelper.forBlock(Blocks.netherrack));
/*  48 */   private final WorldGenHellLava field_177473_x = new WorldGenHellLava((Block)Blocks.flowing_lava, true);
/*  49 */   private final WorldGenHellLava field_177472_y = new WorldGenHellLava((Block)Blocks.flowing_lava, false);
/*  50 */   private final GeneratorBushFeature field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
/*  51 */   private final GeneratorBushFeature field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
/*  52 */   private final MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
/*  53 */   private final MapGenBase netherCaveGenerator = new MapGenCavesHell();
/*     */   
/*     */   double[] noiseData1;
/*     */   double[] noiseData2;
/*     */   double[] noiseData3;
/*     */   double[] noiseData4;
/*     */   double[] noiseData5;
/*     */   
/*     */   public ChunkProviderHell(World worldIn, boolean p_i45637_2_, long seed) {
/*  62 */     this.worldObj = worldIn;
/*  63 */     this.field_177466_i = p_i45637_2_;
/*  64 */     this.hellRNG = new Random(seed);
/*  65 */     this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
/*  66 */     this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
/*  67 */     this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
/*  68 */     this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
/*  69 */     this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
/*  70 */     this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
/*  71 */     this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
/*  72 */     worldIn.setSeaLevel(63);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_180515_a(int p_180515_1_, int p_180515_2_, ChunkPrimer p_180515_3_) {
/*  77 */     int i = 4;
/*  78 */     int j = this.worldObj.getSeaLevel() / 2 + 1;
/*  79 */     int k = i + 1;
/*  80 */     int l = 17;
/*  81 */     int i1 = i + 1;
/*  82 */     this.noiseField = initializeNoiseField(this.noiseField, p_180515_1_ * i, 0, p_180515_2_ * i, k, l, i1);
/*     */     
/*  84 */     for (int j1 = 0; j1 < i; j1++) {
/*     */       
/*  86 */       for (int k1 = 0; k1 < i; k1++) {
/*     */         
/*  88 */         for (int l1 = 0; l1 < 16; l1++) {
/*     */           
/*  90 */           double d0 = 0.125D;
/*  91 */           double d1 = this.noiseField[((j1 + 0) * i1 + k1 + 0) * l + l1 + 0];
/*  92 */           double d2 = this.noiseField[((j1 + 0) * i1 + k1 + 1) * l + l1 + 0];
/*  93 */           double d3 = this.noiseField[((j1 + 1) * i1 + k1 + 0) * l + l1 + 0];
/*  94 */           double d4 = this.noiseField[((j1 + 1) * i1 + k1 + 1) * l + l1 + 0];
/*  95 */           double d5 = (this.noiseField[((j1 + 0) * i1 + k1 + 0) * l + l1 + 1] - d1) * d0;
/*  96 */           double d6 = (this.noiseField[((j1 + 0) * i1 + k1 + 1) * l + l1 + 1] - d2) * d0;
/*  97 */           double d7 = (this.noiseField[((j1 + 1) * i1 + k1 + 0) * l + l1 + 1] - d3) * d0;
/*  98 */           double d8 = (this.noiseField[((j1 + 1) * i1 + k1 + 1) * l + l1 + 1] - d4) * d0;
/*     */           
/* 100 */           for (int i2 = 0; i2 < 8; i2++) {
/*     */             
/* 102 */             double d9 = 0.25D;
/* 103 */             double d10 = d1;
/* 104 */             double d11 = d2;
/* 105 */             double d12 = (d3 - d1) * d9;
/* 106 */             double d13 = (d4 - d2) * d9;
/*     */             
/* 108 */             for (int j2 = 0; j2 < 4; j2++) {
/*     */               
/* 110 */               double d14 = 0.25D;
/* 111 */               double d15 = d10;
/* 112 */               double d16 = (d11 - d10) * d14;
/*     */               
/* 114 */               for (int k2 = 0; k2 < 4; k2++) {
/*     */                 
/* 116 */                 IBlockState iblockstate = null;
/*     */                 
/* 118 */                 if (l1 * 8 + i2 < j)
/*     */                 {
/* 120 */                   iblockstate = Blocks.lava.getDefaultState();
/*     */                 }
/*     */                 
/* 123 */                 if (d15 > 0.0D)
/*     */                 {
/* 125 */                   iblockstate = Blocks.netherrack.getDefaultState();
/*     */                 }
/*     */                 
/* 128 */                 int l2 = j2 + j1 * 4;
/* 129 */                 int i3 = i2 + l1 * 8;
/* 130 */                 int j3 = k2 + k1 * 4;
/* 131 */                 p_180515_3_.setBlockState(l2, i3, j3, iblockstate);
/* 132 */                 d15 += d16;
/*     */               } 
/*     */               
/* 135 */               d10 += d12;
/* 136 */               d11 += d13;
/*     */             } 
/*     */             
/* 139 */             d1 += d5;
/* 140 */             d2 += d6;
/* 141 */             d3 += d7;
/* 142 */             d4 += d8;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_180516_b(int p_180516_1_, int p_180516_2_, ChunkPrimer p_180516_3_) {
/* 151 */     int i = this.worldObj.getSeaLevel() + 1;
/* 152 */     double d0 = 0.03125D;
/* 153 */     this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0, d0, 1.0D);
/* 154 */     this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_180516_1_ * 16, 109, p_180516_2_ * 16, 16, 1, 16, d0, 1.0D, d0);
/* 155 */     this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);
/*     */     
/* 157 */     for (int j = 0; j < 16; j++) {
/*     */       
/* 159 */       for (int k = 0; k < 16; k++) {
/*     */         
/* 161 */         boolean flag = (this.slowsandNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D);
/* 162 */         boolean flag1 = (this.gravelNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D);
/* 163 */         int l = (int)(this.netherrackExclusivityNoise[j + k * 16] / 3.0D + 3.0D + this.hellRNG.nextDouble() * 0.25D);
/* 164 */         int i1 = -1;
/* 165 */         IBlockState iblockstate = Blocks.netherrack.getDefaultState();
/* 166 */         IBlockState iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */         
/* 168 */         for (int j1 = 127; j1 >= 0; j1--) {
/*     */           
/* 170 */           if (j1 < 127 - this.hellRNG.nextInt(5) && j1 > this.hellRNG.nextInt(5)) {
/*     */             
/* 172 */             IBlockState iblockstate2 = p_180516_3_.getBlockState(k, j1, j);
/*     */             
/* 174 */             if (iblockstate2.getBlock() != null && iblockstate2.getBlock().getMaterial() != Material.air) {
/*     */               
/* 176 */               if (iblockstate2.getBlock() == Blocks.netherrack)
/*     */               {
/* 178 */                 if (i1 == -1) {
/*     */                   
/* 180 */                   if (l <= 0) {
/*     */                     
/* 182 */                     iblockstate = null;
/* 183 */                     iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */                   }
/* 185 */                   else if (j1 >= i - 4 && j1 <= i + 1) {
/*     */                     
/* 187 */                     iblockstate = Blocks.netherrack.getDefaultState();
/* 188 */                     iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */                     
/* 190 */                     if (flag1) {
/*     */                       
/* 192 */                       iblockstate = Blocks.gravel.getDefaultState();
/* 193 */                       iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */                     } 
/*     */                     
/* 196 */                     if (flag) {
/*     */                       
/* 198 */                       iblockstate = Blocks.soul_sand.getDefaultState();
/* 199 */                       iblockstate1 = Blocks.soul_sand.getDefaultState();
/*     */                     } 
/*     */                   } 
/*     */                   
/* 203 */                   if (j1 < i && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air))
/*     */                   {
/* 205 */                     iblockstate = Blocks.lava.getDefaultState();
/*     */                   }
/*     */                   
/* 208 */                   i1 = l;
/*     */                   
/* 210 */                   if (j1 >= i - 1)
/*     */                   {
/* 212 */                     p_180516_3_.setBlockState(k, j1, j, iblockstate);
/*     */                   }
/*     */                   else
/*     */                   {
/* 216 */                     p_180516_3_.setBlockState(k, j1, j, iblockstate1);
/*     */                   }
/*     */                 
/* 219 */                 } else if (i1 > 0) {
/*     */                   
/* 221 */                   i1--;
/* 222 */                   p_180516_3_.setBlockState(k, j1, j, iblockstate1);
/*     */                 }
/*     */               
/*     */               }
/*     */             } else {
/*     */               
/* 228 */               i1 = -1;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 233 */             p_180516_3_.setBlockState(k, j1, j, Blocks.bedrock.getDefaultState());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/* 242 */     this.hellRNG.setSeed(x * 341873128712L + z * 132897987541L);
/* 243 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/* 244 */     func_180515_a(x, z, chunkprimer);
/* 245 */     func_180516_b(x, z, chunkprimer);
/* 246 */     this.netherCaveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     
/* 248 */     if (this.field_177466_i)
/*     */     {
/* 250 */       this.genNetherBridge.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 253 */     Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
/* 254 */     BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, x * 16, z * 16, 16, 16);
/* 255 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 257 */     for (int i = 0; i < abyte.length; i++)
/*     */     {
/* 259 */       abyte[i] = (byte)(abiomegenbase[i]).biomeID;
/*     */     }
/*     */     
/* 262 */     chunk.resetRelightChecks();
/* 263 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_, int p_73164_5_, int p_73164_6_, int p_73164_7_) {
/* 268 */     if (p_73164_1_ == null)
/*     */     {
/* 270 */       p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
/*     */     }
/*     */     
/* 273 */     double d0 = 684.412D;
/* 274 */     double d1 = 2053.236D;
/* 275 */     this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0D, 0.0D, 1.0D);
/* 276 */     this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0D, 0.0D, 100.0D);
/* 277 */     this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0 / 80.0D, d1 / 60.0D, d0 / 80.0D);
/* 278 */     this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
/* 279 */     this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
/* 280 */     int i = 0;
/* 281 */     double[] adouble = new double[p_73164_6_];
/*     */     
/* 283 */     for (int j = 0; j < p_73164_6_; j++) {
/*     */       
/* 285 */       adouble[j] = Math.cos(j * Math.PI * 6.0D / p_73164_6_) * 2.0D;
/* 286 */       double d2 = j;
/*     */       
/* 288 */       if (j > p_73164_6_ / 2)
/*     */       {
/* 290 */         d2 = (p_73164_6_ - 1 - j);
/*     */       }
/*     */       
/* 293 */       if (d2 < 4.0D) {
/*     */         
/* 295 */         d2 = 4.0D - d2;
/* 296 */         adouble[j] = adouble[j] - d2 * d2 * d2 * 10.0D;
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     for (int l = 0; l < p_73164_5_; l++) {
/*     */       
/* 302 */       for (int i1 = 0; i1 < p_73164_7_; i1++) {
/*     */         
/* 304 */         double d3 = 0.0D;
/*     */         
/* 306 */         for (int k = 0; k < p_73164_6_; k++) {
/*     */           
/* 308 */           double d4 = 0.0D;
/* 309 */           double d5 = adouble[k];
/* 310 */           double d6 = this.noiseData2[i] / 512.0D;
/* 311 */           double d7 = this.noiseData3[i] / 512.0D;
/* 312 */           double d8 = (this.noiseData1[i] / 10.0D + 1.0D) / 2.0D;
/*     */           
/* 314 */           if (d8 < 0.0D) {
/*     */             
/* 316 */             d4 = d6;
/*     */           }
/* 318 */           else if (d8 > 1.0D) {
/*     */             
/* 320 */             d4 = d7;
/*     */           }
/*     */           else {
/*     */             
/* 324 */             d4 = d6 + (d7 - d6) * d8;
/*     */           } 
/*     */           
/* 327 */           d4 -= d5;
/*     */           
/* 329 */           if (k > p_73164_6_ - 4) {
/*     */             
/* 331 */             double d9 = ((k - p_73164_6_ - 4) / 3.0F);
/* 332 */             d4 = d4 * (1.0D - d9) + -10.0D * d9;
/*     */           } 
/*     */           
/* 335 */           if (k < d3) {
/*     */             
/* 337 */             double d10 = (d3 - k) / 4.0D;
/* 338 */             d10 = MathHelper.clamp_double(d10, 0.0D, 1.0D);
/* 339 */             d4 = d4 * (1.0D - d10) + -10.0D * d10;
/*     */           } 
/*     */           
/* 342 */           p_73164_1_[i] = d4;
/* 343 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 348 */     return p_73164_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/* 353 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {
/* 358 */     BlockFalling.fallInstantly = true;
/* 359 */     BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);
/* 360 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);
/* 361 */     this.genNetherBridge.generateStructure(this.worldObj, this.hellRNG, chunkcoordintpair);
/*     */     
/* 363 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 365 */       this.field_177472_y.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 368 */     for (int j = 0; j < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; j++)
/*     */     {
/* 370 */       this.field_177470_t.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 373 */     for (int k = 0; k < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); k++)
/*     */     {
/* 375 */       this.field_177469_u.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 378 */     for (int l = 0; l < 10; l++)
/*     */     {
/* 380 */       this.field_177468_v.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 383 */     if (this.hellRNG.nextBoolean())
/*     */     {
/* 385 */       this.field_177471_z.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 388 */     if (this.hellRNG.nextBoolean())
/*     */     {
/* 390 */       this.field_177465_A.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 393 */     for (int i1 = 0; i1 < 16; i1++)
/*     */     {
/* 395 */       this.field_177467_w.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
/*     */     }
/*     */     
/* 398 */     for (int j1 = 0; j1 < 16; j1++)
/*     */     {
/* 400 */       this.field_177473_x.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
/*     */     }
/*     */     
/* 403 */     BlockFalling.fallInstantly = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/* 408 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/* 413 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {}
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 422 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 432 */     return "HellRandomLevelSource";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 437 */     if (creatureType == EnumCreatureType.MONSTER) {
/*     */       
/* 439 */       if (this.genNetherBridge.func_175795_b(pos))
/*     */       {
/* 441 */         return this.genNetherBridge.getSpawnList();
/*     */       }
/*     */       
/* 444 */       if (this.genNetherBridge.isPositionInStructure(this.worldObj, pos) && this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.nether_brick)
/*     */       {
/* 446 */         return this.genNetherBridge.getSpawnList();
/*     */       }
/*     */     } 
/*     */     
/* 450 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
/* 451 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 456 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 461 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {
/* 466 */     this.genNetherBridge.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 471 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\ChunkProviderHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */