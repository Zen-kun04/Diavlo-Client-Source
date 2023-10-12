/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.gen.layer.GenLayer;
/*     */ import net.minecraft.world.gen.layer.IntCache;
/*     */ 
/*     */ 
/*     */ public class WorldChunkManager
/*     */ {
/*     */   private GenLayer genBiomes;
/*     */   private GenLayer biomeIndexLayer;
/*     */   private BiomeCache biomeCache;
/*     */   private List<BiomeGenBase> biomesToSpawnIn;
/*     */   private String generatorOptions;
/*     */   
/*     */   protected WorldChunkManager() {
/*  25 */     this.biomeCache = new BiomeCache(this);
/*  26 */     this.generatorOptions = "";
/*  27 */     this.biomesToSpawnIn = Lists.newArrayList();
/*  28 */     this.biomesToSpawnIn.add(BiomeGenBase.forest);
/*  29 */     this.biomesToSpawnIn.add(BiomeGenBase.plains);
/*  30 */     this.biomesToSpawnIn.add(BiomeGenBase.taiga);
/*  31 */     this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
/*  32 */     this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
/*  33 */     this.biomesToSpawnIn.add(BiomeGenBase.jungle);
/*  34 */     this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldChunkManager(long seed, WorldType worldTypeIn, String options) {
/*  39 */     this();
/*  40 */     this.generatorOptions = options;
/*  41 */     GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, worldTypeIn, options);
/*  42 */     this.genBiomes = agenlayer[0];
/*  43 */     this.biomeIndexLayer = agenlayer[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldChunkManager(World worldIn) {
/*  48 */     this(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase> getBiomesToSpawnIn() {
/*  53 */     return this.biomesToSpawnIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeGenBase getBiomeGenerator(BlockPos pos) {
/*  58 */     return getBiomeGenerator(pos, (BiomeGenBase)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeGenBase getBiomeGenerator(BlockPos pos, BiomeGenBase biomeGenBaseIn) {
/*  63 */     return this.biomeCache.func_180284_a(pos.getX(), pos.getZ(), biomeGenBaseIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
/*  68 */     IntCache.resetIntCache();
/*     */     
/*  70 */     if (listToReuse == null || listToReuse.length < width * length)
/*     */     {
/*  72 */       listToReuse = new float[width * length];
/*     */     }
/*     */     
/*  75 */     int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);
/*     */     
/*  77 */     for (int i = 0; i < width * length; i++) {
/*     */ 
/*     */       
/*     */       try {
/*  81 */         float f = BiomeGenBase.getBiomeFromBiomeList(aint[i], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0F;
/*     */         
/*  83 */         if (f > 1.0F)
/*     */         {
/*  85 */           f = 1.0F;
/*     */         }
/*     */         
/*  88 */         listToReuse[i] = f;
/*     */       }
/*  90 */       catch (Throwable throwable) {
/*     */         
/*  92 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
/*  93 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
/*  94 */         crashreportcategory.addCrashSection("biome id", Integer.valueOf(i));
/*  95 */         crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
/*  96 */         crashreportcategory.addCrashSection("x", Integer.valueOf(x));
/*  97 */         crashreportcategory.addCrashSection("z", Integer.valueOf(z));
/*  98 */         crashreportcategory.addCrashSection("w", Integer.valueOf(width));
/*  99 */         crashreportcategory.addCrashSection("h", Integer.valueOf(length));
/* 100 */         throw new ReportedException(crashreport);
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     return listToReuse;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_) {
/* 109 */     return p_76939_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height) {
/* 114 */     IntCache.resetIntCache();
/*     */     
/* 116 */     if (biomes == null || biomes.length < width * height)
/*     */     {
/* 118 */       biomes = new BiomeGenBase[width * height];
/*     */     }
/*     */     
/* 121 */     int[] aint = this.genBiomes.getInts(x, z, width, height);
/*     */ 
/*     */     
/*     */     try {
/* 125 */       for (int i = 0; i < width * height; i++)
/*     */       {
/* 127 */         biomes[i] = BiomeGenBase.getBiomeFromBiomeList(aint[i], BiomeGenBase.field_180279_ad);
/*     */       }
/*     */       
/* 130 */       return biomes;
/*     */     }
/* 132 */     catch (Throwable throwable) {
/*     */       
/* 134 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
/* 135 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
/* 136 */       crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
/* 137 */       crashreportcategory.addCrashSection("x", Integer.valueOf(x));
/* 138 */       crashreportcategory.addCrashSection("z", Integer.valueOf(z));
/* 139 */       crashreportcategory.addCrashSection("w", Integer.valueOf(width));
/* 140 */       crashreportcategory.addCrashSection("h", Integer.valueOf(height));
/* 141 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth) {
/* 147 */     return getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
/* 152 */     IntCache.resetIntCache();
/*     */     
/* 154 */     if (listToReuse == null || listToReuse.length < width * length)
/*     */     {
/* 156 */       listToReuse = new BiomeGenBase[width * length];
/*     */     }
/*     */     
/* 159 */     if (cacheFlag && width == 16 && length == 16 && (x & 0xF) == 0 && (z & 0xF) == 0) {
/*     */       
/* 161 */       BiomeGenBase[] abiomegenbase = this.biomeCache.getCachedBiomes(x, z);
/* 162 */       System.arraycopy(abiomegenbase, 0, listToReuse, 0, width * length);
/* 163 */       return listToReuse;
/*     */     } 
/*     */ 
/*     */     
/* 167 */     int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);
/*     */     
/* 169 */     for (int i = 0; i < width * length; i++)
/*     */     {
/* 171 */       listToReuse[i] = BiomeGenBase.getBiomeFromBiomeList(aint[i], BiomeGenBase.field_180279_ad);
/*     */     }
/*     */     
/* 174 */     return listToReuse;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List<BiomeGenBase> p_76940_4_) {
/* 180 */     IntCache.resetIntCache();
/* 181 */     int i = p_76940_1_ - p_76940_3_ >> 2;
/* 182 */     int j = p_76940_2_ - p_76940_3_ >> 2;
/* 183 */     int k = p_76940_1_ + p_76940_3_ >> 2;
/* 184 */     int l = p_76940_2_ + p_76940_3_ >> 2;
/* 185 */     int i1 = k - i + 1;
/* 186 */     int j1 = l - j + 1;
/* 187 */     int[] aint = this.genBiomes.getInts(i, j, i1, j1);
/*     */ 
/*     */     
/*     */     try {
/* 191 */       for (int k1 = 0; k1 < i1 * j1; k1++) {
/*     */         
/* 193 */         BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k1]);
/*     */         
/* 195 */         if (!p_76940_4_.contains(biomegenbase))
/*     */         {
/* 197 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 201 */       return true;
/*     */     }
/* 203 */     catch (Throwable throwable) {
/*     */       
/* 205 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
/* 206 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
/* 207 */       crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
/* 208 */       crashreportcategory.addCrashSection("x", Integer.valueOf(p_76940_1_));
/* 209 */       crashreportcategory.addCrashSection("z", Integer.valueOf(p_76940_2_));
/* 210 */       crashreportcategory.addCrashSection("radius", Integer.valueOf(p_76940_3_));
/* 211 */       crashreportcategory.addCrashSection("allowed", p_76940_4_);
/* 212 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos findBiomePosition(int x, int z, int range, List<BiomeGenBase> biomes, Random random) {
/* 218 */     IntCache.resetIntCache();
/* 219 */     int i = x - range >> 2;
/* 220 */     int j = z - range >> 2;
/* 221 */     int k = x + range >> 2;
/* 222 */     int l = z + range >> 2;
/* 223 */     int i1 = k - i + 1;
/* 224 */     int j1 = l - j + 1;
/* 225 */     int[] aint = this.genBiomes.getInts(i, j, i1, j1);
/* 226 */     BlockPos blockpos = null;
/* 227 */     int k1 = 0;
/*     */     
/* 229 */     for (int l1 = 0; l1 < i1 * j1; l1++) {
/*     */       
/* 231 */       int i2 = i + l1 % i1 << 2;
/* 232 */       int j2 = j + l1 / i1 << 2;
/* 233 */       BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[l1]);
/*     */       
/* 235 */       if (biomes.contains(biomegenbase) && (blockpos == null || random.nextInt(k1 + 1) == 0)) {
/*     */         
/* 237 */         blockpos = new BlockPos(i2, 0, j2);
/* 238 */         k1++;
/*     */       } 
/*     */     } 
/*     */     
/* 242 */     return blockpos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanupCache() {
/* 247 */     this.biomeCache.cleanupCache();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\WorldChunkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */