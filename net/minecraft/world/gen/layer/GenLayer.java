/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ 
/*     */ 
/*     */ public abstract class GenLayer
/*     */ {
/*     */   private long worldGenSeed;
/*     */   protected GenLayer parent;
/*     */   private long chunkSeed;
/*     */   protected long baseSeed;
/*     */   
/*     */   public static GenLayer[] initializeAllBiomeGenerators(long seed, WorldType p_180781_2_, String p_180781_3_) {
/*  20 */     GenLayer genlayer = new GenLayerIsland(1L);
/*  21 */     genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
/*  22 */     GenLayerAddIsland genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
/*  23 */     GenLayerZoom genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
/*  24 */     GenLayerAddIsland genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
/*  25 */     genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
/*  26 */     genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
/*  27 */     GenLayerRemoveTooMuchOcean genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
/*  28 */     GenLayerAddSnow genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
/*  29 */     GenLayerAddIsland genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
/*  30 */     GenLayerEdge genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
/*  31 */     genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
/*  32 */     genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
/*  33 */     GenLayerZoom genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
/*  34 */     genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
/*  35 */     GenLayerAddIsland genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
/*  36 */     GenLayerAddMushroomIsland genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
/*  37 */     GenLayerDeepOcean genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
/*  38 */     GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
/*  39 */     ChunkProviderSettings chunkprovidersettings = null;
/*  40 */     int i = 4;
/*  41 */     int j = i;
/*     */     
/*  43 */     if (p_180781_2_ == WorldType.CUSTOMIZED && p_180781_3_.length() > 0) {
/*     */       
/*  45 */       chunkprovidersettings = ChunkProviderSettings.Factory.jsonToFactory(p_180781_3_).func_177864_b();
/*  46 */       i = chunkprovidersettings.biomeSize;
/*  47 */       j = chunkprovidersettings.riverSize;
/*     */     } 
/*     */     
/*  50 */     if (p_180781_2_ == WorldType.LARGE_BIOMES)
/*     */     {
/*  52 */       i = 6;
/*     */     }
/*     */     
/*  55 */     GenLayer lvt_8_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
/*  56 */     GenLayerRiverInit genlayerriverinit = new GenLayerRiverInit(100L, lvt_8_1_);
/*  57 */     GenLayerBiome lvt_9_1_ = new GenLayerBiome(200L, genlayer4, p_180781_2_, p_180781_3_);
/*  58 */     GenLayer genlayer6 = GenLayerZoom.magnify(1000L, lvt_9_1_, 2);
/*  59 */     GenLayerBiomeEdge genlayerbiomeedge = new GenLayerBiomeEdge(1000L, genlayer6);
/*  60 */     GenLayer lvt_10_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
/*  61 */     GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_10_1_);
/*  62 */     GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
/*  63 */     genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
/*  64 */     GenLayerRiver genlayerriver = new GenLayerRiver(1L, genlayer5);
/*  65 */     GenLayerSmooth genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
/*  66 */     genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);
/*     */     
/*  68 */     for (int k = 0; k < i; k++) {
/*     */       
/*  70 */       genlayerhills = new GenLayerZoom((1000 + k), genlayerhills);
/*     */       
/*  72 */       if (k == 0)
/*     */       {
/*  74 */         genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
/*     */       }
/*     */       
/*  77 */       if (k == 1 || i == 1)
/*     */       {
/*  79 */         genlayerhills = new GenLayerShore(1000L, genlayerhills);
/*     */       }
/*     */     } 
/*     */     
/*  83 */     GenLayerSmooth genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
/*  84 */     GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
/*  85 */     GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
/*  86 */     genlayerrivermix.initWorldGenSeed(seed);
/*  87 */     genlayer3.initWorldGenSeed(seed);
/*  88 */     return new GenLayer[] { genlayerrivermix, genlayer3, genlayerrivermix };
/*     */   }
/*     */ 
/*     */   
/*     */   public GenLayer(long p_i2125_1_) {
/*  93 */     this.baseSeed = p_i2125_1_;
/*  94 */     this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
/*  95 */     this.baseSeed += p_i2125_1_;
/*  96 */     this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
/*  97 */     this.baseSeed += p_i2125_1_;
/*  98 */     this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
/*  99 */     this.baseSeed += p_i2125_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initWorldGenSeed(long seed) {
/* 104 */     this.worldGenSeed = seed;
/*     */     
/* 106 */     if (this.parent != null)
/*     */     {
/* 108 */       this.parent.initWorldGenSeed(seed);
/*     */     }
/*     */     
/* 111 */     this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
/* 112 */     this.worldGenSeed += this.baseSeed;
/* 113 */     this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
/* 114 */     this.worldGenSeed += this.baseSeed;
/* 115 */     this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
/* 116 */     this.worldGenSeed += this.baseSeed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initChunkSeed(long p_75903_1_, long p_75903_3_) {
/* 121 */     this.chunkSeed = this.worldGenSeed;
/* 122 */     this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
/* 123 */     this.chunkSeed += p_75903_1_;
/* 124 */     this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
/* 125 */     this.chunkSeed += p_75903_3_;
/* 126 */     this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
/* 127 */     this.chunkSeed += p_75903_1_;
/* 128 */     this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
/* 129 */     this.chunkSeed += p_75903_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int nextInt(int p_75902_1_) {
/* 134 */     int i = (int)((this.chunkSeed >> 24L) % p_75902_1_);
/*     */     
/* 136 */     if (i < 0)
/*     */     {
/* 138 */       i += p_75902_1_;
/*     */     }
/*     */     
/* 141 */     this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
/* 142 */     this.chunkSeed += this.worldGenSeed;
/* 143 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int[] getInts(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   
/*     */   protected static boolean biomesEqualOrMesaPlateau(int biomeIDA, int biomeIDB) {
/* 150 */     if (biomeIDA == biomeIDB)
/*     */     {
/* 152 */       return true;
/*     */     }
/* 154 */     if (biomeIDA != BiomeGenBase.mesaPlateau_F.biomeID && biomeIDA != BiomeGenBase.mesaPlateau.biomeID) {
/*     */       
/* 156 */       final BiomeGenBase biomegenbase = BiomeGenBase.getBiome(biomeIDA);
/* 157 */       final BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(biomeIDB);
/*     */ 
/*     */       
/*     */       try {
/* 161 */         return (biomegenbase != null && biomegenbase1 != null) ? biomegenbase.isEqualTo(biomegenbase1) : false;
/*     */       }
/* 163 */       catch (Throwable throwable) {
/*     */         
/* 165 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
/* 166 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Biomes being compared");
/* 167 */         crashreportcategory.addCrashSection("Biome A ID", Integer.valueOf(biomeIDA));
/* 168 */         crashreportcategory.addCrashSection("Biome B ID", Integer.valueOf(biomeIDB));
/* 169 */         crashreportcategory.addCrashSectionCallable("Biome A", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 173 */                 return String.valueOf(biomegenbase);
/*     */               }
/*     */             });
/* 176 */         crashreportcategory.addCrashSectionCallable("Biome B", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 180 */                 return String.valueOf(biomegenbase1);
/*     */               }
/*     */             });
/* 183 */         throw new ReportedException(crashreport);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 188 */     return (biomeIDB == BiomeGenBase.mesaPlateau_F.biomeID || biomeIDB == BiomeGenBase.mesaPlateau.biomeID);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isBiomeOceanic(int p_151618_0_) {
/* 194 */     return (p_151618_0_ == BiomeGenBase.ocean.biomeID || p_151618_0_ == BiomeGenBase.deepOcean.biomeID || p_151618_0_ == BiomeGenBase.frozenOcean.biomeID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int selectRandom(int... p_151619_1_) {
/* 199 */     return p_151619_1_[nextInt(p_151619_1_.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   protected int selectModeOrRandom(int p_151617_1_, int p_151617_2_, int p_151617_3_, int p_151617_4_) {
/* 204 */     return (p_151617_2_ == p_151617_3_ && p_151617_3_ == p_151617_4_) ? p_151617_2_ : ((p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_3_) ? p_151617_1_ : ((p_151617_1_ == p_151617_2_ && p_151617_1_ == p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_3_ && p_151617_1_ == p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_2_ && p_151617_3_ != p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_3_ && p_151617_2_ != p_151617_4_) ? p_151617_1_ : ((p_151617_1_ == p_151617_4_ && p_151617_2_ != p_151617_3_) ? p_151617_1_ : ((p_151617_2_ == p_151617_3_ && p_151617_1_ != p_151617_4_) ? p_151617_2_ : ((p_151617_2_ == p_151617_4_ && p_151617_1_ != p_151617_3_) ? p_151617_2_ : ((p_151617_3_ == p_151617_4_ && p_151617_1_ != p_151617_2_) ? p_151617_3_ : selectRandom(new int[] { p_151617_1_, p_151617_2_, p_151617_3_, p_151617_4_ }))))))))));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */