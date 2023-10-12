/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderDebug;
/*     */ import net.minecraft.world.gen.ChunkProviderFlat;
/*     */ import net.minecraft.world.gen.ChunkProviderGenerate;
/*     */ import net.minecraft.world.gen.FlatGeneratorInfo;
/*     */ 
/*     */ public abstract class WorldProvider
/*     */ {
/*  19 */   public static final float[] moonPhaseFactors = new float[] { 1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F };
/*     */   protected World worldObj;
/*     */   private WorldType terrainType;
/*     */   private String generatorSettings;
/*     */   protected WorldChunkManager worldChunkMgr;
/*     */   protected boolean isHellWorld;
/*     */   protected boolean hasNoSky;
/*  26 */   protected final float[] lightBrightnessTable = new float[16];
/*     */   protected int dimensionId;
/*  28 */   private final float[] colorsSunriseSunset = new float[4];
/*     */ 
/*     */   
/*     */   public final void registerWorld(World worldIn) {
/*  32 */     this.worldObj = worldIn;
/*  33 */     this.terrainType = worldIn.getWorldInfo().getTerrainType();
/*  34 */     this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
/*  35 */     registerWorldChunkManager();
/*  36 */     generateLightBrightnessTable();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateLightBrightnessTable() {
/*  41 */     float f = 0.0F;
/*     */     
/*  43 */     for (int i = 0; i <= 15; i++) {
/*     */       
/*  45 */       float f1 = 1.0F - i / 15.0F;
/*  46 */       this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerWorldChunkManager() {
/*  52 */     WorldType worldtype = this.worldObj.getWorldInfo().getTerrainType();
/*     */     
/*  54 */     if (worldtype == WorldType.FLAT) {
/*     */       
/*  56 */       FlatGeneratorInfo flatgeneratorinfo = FlatGeneratorInfo.createFlatGeneratorFromString(this.worldObj.getWorldInfo().getGeneratorOptions());
/*  57 */       this.worldChunkMgr = (WorldChunkManager)new WorldChunkManagerHell(BiomeGenBase.getBiomeFromBiomeList(flatgeneratorinfo.getBiome(), BiomeGenBase.field_180279_ad), 0.5F);
/*     */     }
/*  59 */     else if (worldtype == WorldType.DEBUG_WORLD) {
/*     */       
/*  61 */       this.worldChunkMgr = (WorldChunkManager)new WorldChunkManagerHell(BiomeGenBase.plains, 0.0F);
/*     */     }
/*     */     else {
/*     */       
/*  65 */       this.worldChunkMgr = new WorldChunkManager(this.worldObj);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IChunkProvider createChunkGenerator() {
/*  71 */     return (this.terrainType == WorldType.FLAT) ? (IChunkProvider)new ChunkProviderFlat(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : ((this.terrainType == WorldType.DEBUG_WORLD) ? (IChunkProvider)new ChunkProviderDebug(this.worldObj) : ((this.terrainType == WorldType.CUSTOMIZED) ? (IChunkProvider)new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings) : (IChunkProvider)new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), this.generatorSettings)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCoordinateBeSpawn(int x, int z) {
/*  76 */     return (this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == Blocks.grass);
/*     */   }
/*     */ 
/*     */   
/*     */   public float calculateCelestialAngle(long worldTime, float partialTicks) {
/*  81 */     int i = (int)(worldTime % 24000L);
/*  82 */     float f = (i + partialTicks) / 24000.0F - 0.25F;
/*     */     
/*  84 */     if (f < 0.0F)
/*     */     {
/*  86 */       f++;
/*     */     }
/*     */     
/*  89 */     if (f > 1.0F)
/*     */     {
/*  91 */       f--;
/*     */     }
/*     */     
/*  94 */     f = 1.0F - (float)((Math.cos(f * Math.PI) + 1.0D) / 2.0D);
/*  95 */     f += (f - f) / 3.0F;
/*  96 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMoonPhase(long worldTime) {
/* 101 */     return (int)(worldTime / 24000L % 8L + 8L) % 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSurfaceWorld() {
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
/* 111 */     float f = 0.4F;
/* 112 */     float f1 = MathHelper.cos(celestialAngle * 3.1415927F * 2.0F) - 0.0F;
/* 113 */     float f2 = -0.0F;
/*     */     
/* 115 */     if (f1 >= f2 - f && f1 <= f2 + f) {
/*     */       
/* 117 */       float f3 = (f1 - f2) / f * 0.5F + 0.5F;
/* 118 */       float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * 3.1415927F)) * 0.99F;
/* 119 */       f4 *= f4;
/* 120 */       this.colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
/* 121 */       this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
/* 122 */       this.colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
/* 123 */       this.colorsSunriseSunset[3] = f4;
/* 124 */       return this.colorsSunriseSunset;
/*     */     } 
/*     */ 
/*     */     
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
/* 134 */     float f = MathHelper.cos(p_76562_1_ * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 135 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 136 */     float f1 = 0.7529412F;
/* 137 */     float f2 = 0.84705883F;
/* 138 */     float f3 = 1.0F;
/* 139 */     f1 *= f * 0.94F + 0.06F;
/* 140 */     f2 *= f * 0.94F + 0.06F;
/* 141 */     f3 *= f * 0.91F + 0.09F;
/* 142 */     return new Vec3(f1, f2, f3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRespawnHere() {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static WorldProvider getProviderForDimension(int dimension) {
/* 152 */     return (dimension == -1) ? new WorldProviderHell() : ((dimension == 0) ? new WorldProviderSurface() : ((dimension == 1) ? new WorldProviderEnd() : null));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getCloudHeight() {
/* 157 */     return 128.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSkyColored() {
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getSpawnCoordinate() {
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAverageGroundLevel() {
/* 172 */     return (this.terrainType == WorldType.FLAT) ? 4 : (this.worldObj.getSeaLevel() + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getVoidFogYFactor() {
/* 177 */     return (this.terrainType == WorldType.FLAT) ? 1.0D : 0.03125D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesXZShowFog(int x, int z) {
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract String getDimensionName();
/*     */   
/*     */   public abstract String getInternalNameSuffix();
/*     */   
/*     */   public WorldChunkManager getWorldChunkManager() {
/* 191 */     return this.worldChunkMgr;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesWaterVaporize() {
/* 196 */     return this.isHellWorld;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getHasNoSky() {
/* 201 */     return this.hasNoSky;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getLightBrightnessTable() {
/* 206 */     return this.lightBrightnessTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDimensionId() {
/* 211 */     return this.dimensionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/* 216 */     return new WorldBorder();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\WorldProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */