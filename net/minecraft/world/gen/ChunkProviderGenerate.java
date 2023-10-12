/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFalling;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.SpawnerAnimals;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.feature.WorldGenDungeons;
/*     */ import net.minecraft.world.gen.feature.WorldGenLakes;
/*     */ import net.minecraft.world.gen.structure.MapGenMineshaft;
/*     */ import net.minecraft.world.gen.structure.MapGenScatteredFeature;
/*     */ import net.minecraft.world.gen.structure.MapGenStronghold;
/*     */ import net.minecraft.world.gen.structure.MapGenVillage;
/*     */ import net.minecraft.world.gen.structure.StructureOceanMonument;
/*     */ 
/*     */ public class ChunkProviderGenerate
/*     */   implements IChunkProvider {
/*     */   private Random rand;
/*     */   private NoiseGeneratorOctaves field_147431_j;
/*     */   private NoiseGeneratorOctaves field_147432_k;
/*     */   private NoiseGeneratorOctaves field_147429_l;
/*     */   private NoiseGeneratorPerlin field_147430_m;
/*     */   public NoiseGeneratorOctaves noiseGen5;
/*     */   public NoiseGeneratorOctaves noiseGen6;
/*     */   public NoiseGeneratorOctaves mobSpawnerNoise;
/*     */   private World worldObj;
/*     */   private final boolean mapFeaturesEnabled;
/*     */   private WorldType field_177475_o;
/*     */   private final double[] field_147434_q;
/*     */   private final float[] parabolicField;
/*     */   private ChunkProviderSettings settings;
/*  44 */   private Block oceanBlockTmpl = (Block)Blocks.water;
/*  45 */   private double[] stoneNoise = new double[256];
/*  46 */   private MapGenBase caveGenerator = new MapGenCaves();
/*  47 */   private MapGenStronghold strongholdGenerator = new MapGenStronghold();
/*  48 */   private MapGenVillage villageGenerator = new MapGenVillage();
/*  49 */   private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
/*  50 */   private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
/*  51 */   private MapGenBase ravineGenerator = new MapGenRavine();
/*  52 */   private StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
/*     */   
/*     */   private BiomeGenBase[] biomesForGeneration;
/*     */   double[] mainNoiseArray;
/*     */   double[] lowerLimitNoiseArray;
/*     */   double[] upperLimitNoiseArray;
/*     */   double[] depthNoiseArray;
/*     */   
/*     */   public ChunkProviderGenerate(World worldIn, long seed, boolean generateStructures, String structuresJson) {
/*  61 */     this.worldObj = worldIn;
/*  62 */     this.mapFeaturesEnabled = generateStructures;
/*  63 */     this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
/*  64 */     this.rand = new Random(seed);
/*  65 */     this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
/*  66 */     this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
/*  67 */     this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
/*  68 */     this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
/*  69 */     this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
/*  70 */     this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
/*  71 */     this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
/*  72 */     this.field_147434_q = new double[825];
/*  73 */     this.parabolicField = new float[25];
/*     */     
/*  75 */     for (int i = -2; i <= 2; i++) {
/*     */       
/*  77 */       for (int j = -2; j <= 2; j++) {
/*     */         
/*  79 */         float f = 10.0F / MathHelper.sqrt_float((i * i + j * j) + 0.2F);
/*  80 */         this.parabolicField[i + 2 + (j + 2) * 5] = f;
/*     */       } 
/*     */     } 
/*     */     
/*  84 */     if (structuresJson != null) {
/*     */       
/*  86 */       this.settings = ChunkProviderSettings.Factory.jsonToFactory(structuresJson).func_177864_b();
/*  87 */       this.oceanBlockTmpl = this.settings.useLavaOceans ? (Block)Blocks.lava : (Block)Blocks.water;
/*  88 */       worldIn.setSeaLevel(this.settings.seaLevel);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
/*  94 */     this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
/*  95 */     func_147423_a(x * 4, 0, z * 4);
/*     */     
/*  97 */     for (int i = 0; i < 4; i++) {
/*     */       
/*  99 */       int j = i * 5;
/* 100 */       int k = (i + 1) * 5;
/*     */       
/* 102 */       for (int l = 0; l < 4; l++) {
/*     */         
/* 104 */         int i1 = (j + l) * 33;
/* 105 */         int j1 = (j + l + 1) * 33;
/* 106 */         int k1 = (k + l) * 33;
/* 107 */         int l1 = (k + l + 1) * 33;
/*     */         
/* 109 */         for (int i2 = 0; i2 < 32; i2++) {
/*     */           
/* 111 */           double d0 = 0.125D;
/* 112 */           double d1 = this.field_147434_q[i1 + i2];
/* 113 */           double d2 = this.field_147434_q[j1 + i2];
/* 114 */           double d3 = this.field_147434_q[k1 + i2];
/* 115 */           double d4 = this.field_147434_q[l1 + i2];
/* 116 */           double d5 = (this.field_147434_q[i1 + i2 + 1] - d1) * d0;
/* 117 */           double d6 = (this.field_147434_q[j1 + i2 + 1] - d2) * d0;
/* 118 */           double d7 = (this.field_147434_q[k1 + i2 + 1] - d3) * d0;
/* 119 */           double d8 = (this.field_147434_q[l1 + i2 + 1] - d4) * d0;
/*     */           
/* 121 */           for (int j2 = 0; j2 < 8; j2++) {
/*     */             
/* 123 */             double d9 = 0.25D;
/* 124 */             double d10 = d1;
/* 125 */             double d11 = d2;
/* 126 */             double d12 = (d3 - d1) * d9;
/* 127 */             double d13 = (d4 - d2) * d9;
/*     */             
/* 129 */             for (int k2 = 0; k2 < 4; k2++) {
/*     */               
/* 131 */               double d14 = 0.25D;
/* 132 */               double d16 = (d11 - d10) * d14;
/* 133 */               double lvt_45_1_ = d10 - d16;
/*     */               
/* 135 */               for (int l2 = 0; l2 < 4; l2++) {
/*     */                 
/* 137 */                 if ((lvt_45_1_ += d16) > 0.0D) {
/*     */                   
/* 139 */                   primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, Blocks.stone.getDefaultState());
/*     */                 }
/* 141 */                 else if (i2 * 8 + j2 < this.settings.seaLevel) {
/*     */                   
/* 143 */                   primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, this.oceanBlockTmpl.getDefaultState());
/*     */                 } 
/*     */               } 
/*     */               
/* 147 */               d10 += d12;
/* 148 */               d11 += d13;
/*     */             } 
/*     */             
/* 151 */             d1 += d5;
/* 152 */             d2 += d6;
/* 153 */             d3 += d7;
/* 154 */             d4 += d8;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceBlocksForBiome(int x, int z, ChunkPrimer primer, BiomeGenBase[] biomeGens) {
/* 163 */     double d0 = 0.03125D;
/* 164 */     this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, (x * 16), (z * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
/*     */     
/* 166 */     for (int i = 0; i < 16; i++) {
/*     */       
/* 168 */       for (int j = 0; j < 16; j++) {
/*     */         
/* 170 */         BiomeGenBase biomegenbase = biomeGens[j + i * 16];
/* 171 */         biomegenbase.genTerrainBlocks(this.worldObj, this.rand, primer, x * 16 + i, z * 16 + j, this.stoneNoise[j + i * 16]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/* 178 */     this.rand.setSeed(x * 341873128712L + z * 132897987541L);
/* 179 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/* 180 */     setBlocksInChunk(x, z, chunkprimer);
/* 181 */     this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
/* 182 */     replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);
/*     */     
/* 184 */     if (this.settings.useCaves)
/*     */     {
/* 186 */       this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 189 */     if (this.settings.useRavines)
/*     */     {
/* 191 */       this.ravineGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 194 */     if (this.settings.useMineShafts && this.mapFeaturesEnabled)
/*     */     {
/* 196 */       this.mineshaftGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 199 */     if (this.settings.useVillages && this.mapFeaturesEnabled)
/*     */     {
/* 201 */       this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 204 */     if (this.settings.useStrongholds && this.mapFeaturesEnabled)
/*     */     {
/* 206 */       this.strongholdGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 209 */     if (this.settings.useTemples && this.mapFeaturesEnabled)
/*     */     {
/* 211 */       this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 214 */     if (this.settings.useMonuments && this.mapFeaturesEnabled)
/*     */     {
/* 216 */       this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 219 */     Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
/* 220 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 222 */     for (int i = 0; i < abyte.length; i++)
/*     */     {
/* 224 */       abyte[i] = (byte)(this.biomesForGeneration[i]).biomeID;
/*     */     }
/*     */     
/* 227 */     chunk.generateSkylightMap();
/* 228 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_147423_a(int x, int y, int z) {
/* 233 */     this.depthNoiseArray = this.noiseGen6.generateNoiseOctaves(this.depthNoiseArray, x, z, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent);
/* 234 */     float f = this.settings.coordinateScale;
/* 235 */     float f1 = this.settings.heightScale;
/* 236 */     this.mainNoiseArray = this.field_147429_l.generateNoiseOctaves(this.mainNoiseArray, x, y, z, 5, 33, 5, (f / this.settings.mainNoiseScaleX), (f1 / this.settings.mainNoiseScaleY), (f / this.settings.mainNoiseScaleZ));
/* 237 */     this.lowerLimitNoiseArray = this.field_147431_j.generateNoiseOctaves(this.lowerLimitNoiseArray, x, y, z, 5, 33, 5, f, f1, f);
/* 238 */     this.upperLimitNoiseArray = this.field_147432_k.generateNoiseOctaves(this.upperLimitNoiseArray, x, y, z, 5, 33, 5, f, f1, f);
/* 239 */     z = 0;
/* 240 */     x = 0;
/* 241 */     int i = 0;
/* 242 */     int j = 0;
/*     */     
/* 244 */     for (int k = 0; k < 5; k++) {
/*     */       
/* 246 */       for (int l = 0; l < 5; l++) {
/*     */         
/* 248 */         float f2 = 0.0F;
/* 249 */         float f3 = 0.0F;
/* 250 */         float f4 = 0.0F;
/* 251 */         int i1 = 2;
/* 252 */         BiomeGenBase biomegenbase = this.biomesForGeneration[k + 2 + (l + 2) * 10];
/*     */         
/* 254 */         for (int j1 = -i1; j1 <= i1; j1++) {
/*     */           
/* 256 */           for (int k1 = -i1; k1 <= i1; k1++) {
/*     */             
/* 258 */             BiomeGenBase biomegenbase1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
/* 259 */             float f5 = this.settings.biomeDepthOffSet + biomegenbase1.minHeight * this.settings.biomeDepthWeight;
/* 260 */             float f6 = this.settings.biomeScaleOffset + biomegenbase1.maxHeight * this.settings.biomeScaleWeight;
/*     */             
/* 262 */             if (this.field_177475_o == WorldType.AMPLIFIED && f5 > 0.0F) {
/*     */               
/* 264 */               f5 = 1.0F + f5 * 2.0F;
/* 265 */               f6 = 1.0F + f6 * 4.0F;
/*     */             } 
/*     */             
/* 268 */             float f7 = this.parabolicField[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);
/*     */             
/* 270 */             if (biomegenbase1.minHeight > biomegenbase.minHeight)
/*     */             {
/* 272 */               f7 /= 2.0F;
/*     */             }
/*     */             
/* 275 */             f2 += f6 * f7;
/* 276 */             f3 += f5 * f7;
/* 277 */             f4 += f7;
/*     */           } 
/*     */         } 
/*     */         
/* 281 */         f2 /= f4;
/* 282 */         f3 /= f4;
/* 283 */         f2 = f2 * 0.9F + 0.1F;
/* 284 */         f3 = (f3 * 4.0F - 1.0F) / 8.0F;
/* 285 */         double d7 = this.depthNoiseArray[j] / 8000.0D;
/*     */         
/* 287 */         if (d7 < 0.0D)
/*     */         {
/* 289 */           d7 = -d7 * 0.3D;
/*     */         }
/*     */         
/* 292 */         d7 = d7 * 3.0D - 2.0D;
/*     */         
/* 294 */         if (d7 < 0.0D) {
/*     */           
/* 296 */           d7 /= 2.0D;
/*     */           
/* 298 */           if (d7 < -1.0D)
/*     */           {
/* 300 */             d7 = -1.0D;
/*     */           }
/*     */           
/* 303 */           d7 /= 1.4D;
/* 304 */           d7 /= 2.0D;
/*     */         }
/*     */         else {
/*     */           
/* 308 */           if (d7 > 1.0D)
/*     */           {
/* 310 */             d7 = 1.0D;
/*     */           }
/*     */           
/* 313 */           d7 /= 8.0D;
/*     */         } 
/*     */         
/* 316 */         j++;
/* 317 */         double d8 = f3;
/* 318 */         double d9 = f2;
/* 319 */         d8 += d7 * 0.2D;
/* 320 */         d8 = d8 * this.settings.baseSize / 8.0D;
/* 321 */         double d0 = this.settings.baseSize + d8 * 4.0D;
/*     */         
/* 323 */         for (int l1 = 0; l1 < 33; l1++) {
/*     */           
/* 325 */           double d1 = (l1 - d0) * this.settings.stretchY * 128.0D / 256.0D / d9;
/*     */           
/* 327 */           if (d1 < 0.0D)
/*     */           {
/* 329 */             d1 *= 4.0D;
/*     */           }
/*     */           
/* 332 */           double d2 = this.lowerLimitNoiseArray[i] / this.settings.lowerLimitScale;
/* 333 */           double d3 = this.upperLimitNoiseArray[i] / this.settings.upperLimitScale;
/* 334 */           double d4 = (this.mainNoiseArray[i] / 10.0D + 1.0D) / 2.0D;
/* 335 */           double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;
/*     */           
/* 337 */           if (l1 > 29) {
/*     */             
/* 339 */             double d6 = ((l1 - 29) / 3.0F);
/* 340 */             d5 = d5 * (1.0D - d6) + -10.0D * d6;
/*     */           } 
/*     */           
/* 343 */           this.field_147434_q[i] = d5;
/* 344 */           i++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/* 352 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {
/* 357 */     BlockFalling.fallInstantly = true;
/* 358 */     int i = x * 16;
/* 359 */     int j = z * 16;
/* 360 */     BlockPos blockpos = new BlockPos(i, 0, j);
/* 361 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
/* 362 */     this.rand.setSeed(this.worldObj.getSeed());
/* 363 */     long k = this.rand.nextLong() / 2L * 2L + 1L;
/* 364 */     long l = this.rand.nextLong() / 2L * 2L + 1L;
/* 365 */     this.rand.setSeed(x * k + z * l ^ this.worldObj.getSeed());
/* 366 */     boolean flag = false;
/* 367 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);
/*     */     
/* 369 */     if (this.settings.useMineShafts && this.mapFeaturesEnabled)
/*     */     {
/* 371 */       this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 374 */     if (this.settings.useVillages && this.mapFeaturesEnabled)
/*     */     {
/* 376 */       flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 379 */     if (this.settings.useStrongholds && this.mapFeaturesEnabled)
/*     */     {
/* 381 */       this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 384 */     if (this.settings.useTemples && this.mapFeaturesEnabled)
/*     */     {
/* 386 */       this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 389 */     if (this.settings.useMonuments && this.mapFeaturesEnabled)
/*     */     {
/* 391 */       this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 394 */     if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
/*     */       
/* 396 */       int i1 = this.rand.nextInt(16) + 8;
/* 397 */       int j1 = this.rand.nextInt(256);
/* 398 */       int k1 = this.rand.nextInt(16) + 8;
/* 399 */       (new WorldGenLakes((Block)Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(i1, j1, k1));
/*     */     } 
/*     */     
/* 402 */     if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
/*     */       
/* 404 */       int i2 = this.rand.nextInt(16) + 8;
/* 405 */       int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
/* 406 */       int k3 = this.rand.nextInt(16) + 8;
/*     */       
/* 408 */       if (l2 < this.worldObj.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
/*     */       {
/* 410 */         (new WorldGenLakes((Block)Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(i2, l2, k3));
/*     */       }
/*     */     } 
/*     */     
/* 414 */     if (this.settings.useDungeons)
/*     */     {
/* 416 */       for (int j2 = 0; j2 < this.settings.dungeonChance; j2++) {
/*     */         
/* 418 */         int i3 = this.rand.nextInt(16) + 8;
/* 419 */         int l3 = this.rand.nextInt(256);
/* 420 */         int l1 = this.rand.nextInt(16) + 8;
/* 421 */         (new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(i3, l3, l1));
/*     */       } 
/*     */     }
/*     */     
/* 425 */     biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
/* 426 */     SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, i + 8, j + 8, 16, 16, this.rand);
/* 427 */     blockpos = blockpos.add(8, 0, 8);
/*     */     
/* 429 */     for (int k2 = 0; k2 < 16; k2++) {
/*     */       
/* 431 */       for (int j3 = 0; j3 < 16; j3++) {
/*     */         
/* 433 */         BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k2, 0, j3));
/* 434 */         BlockPos blockpos2 = blockpos1.down();
/*     */         
/* 436 */         if (this.worldObj.canBlockFreezeWater(blockpos2))
/*     */         {
/* 438 */           this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
/*     */         }
/*     */         
/* 441 */         if (this.worldObj.canSnowAt(blockpos1, true))
/*     */         {
/* 443 */           this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 448 */     BlockFalling.fallInstantly = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/* 453 */     boolean flag = false;
/*     */     
/* 455 */     if (this.settings.useMonuments && this.mapFeaturesEnabled && chunkIn.getInhabitedTime() < 3600L)
/*     */     {
/* 457 */       flag |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(x, z));
/*     */     }
/*     */     
/* 460 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/* 465 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {}
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 474 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/* 479 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 484 */     return "RandomLevelSource";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 489 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
/*     */     
/* 491 */     if (this.mapFeaturesEnabled) {
/*     */       
/* 493 */       if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(pos))
/*     */       {
/* 495 */         return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
/*     */       }
/*     */       
/* 498 */       if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.isPositionInStructure(this.worldObj, pos))
/*     */       {
/* 500 */         return this.oceanMonumentGenerator.getScatteredFeatureSpawnList();
/*     */       }
/*     */     } 
/*     */     
/* 504 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 509 */     return ("Stronghold".equals(structureName) && this.strongholdGenerator != null) ? this.strongholdGenerator.getClosestStrongholdPos(worldIn, position) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 514 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {
/* 519 */     if (this.settings.useMineShafts && this.mapFeaturesEnabled)
/*     */     {
/* 521 */       this.mineshaftGenerator.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */     }
/*     */     
/* 524 */     if (this.settings.useVillages && this.mapFeaturesEnabled)
/*     */     {
/* 526 */       this.villageGenerator.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */     }
/*     */     
/* 529 */     if (this.settings.useStrongholds && this.mapFeaturesEnabled)
/*     */     {
/* 531 */       this.strongholdGenerator.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */     }
/*     */     
/* 534 */     if (this.settings.useTemples && this.mapFeaturesEnabled)
/*     */     {
/* 536 */       this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */     }
/*     */     
/* 539 */     if (this.settings.useMonuments && this.mapFeaturesEnabled)
/*     */     {
/* 541 */       this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, (ChunkPrimer)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 547 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\ChunkProviderGenerate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */