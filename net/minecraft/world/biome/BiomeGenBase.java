/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.monster.EntitySlime;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityBat;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntityRabbit;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.world.ColorizerFoliage;
/*     */ import net.minecraft.world.ColorizerGrass;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.NoiseGeneratorPerlin;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenDoublePlant;
/*     */ import net.minecraft.world.gen.feature.WorldGenSwamp;
/*     */ import net.minecraft.world.gen.feature.WorldGenTallGrass;
/*     */ import net.minecraft.world.gen.feature.WorldGenTrees;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class BiomeGenBase {
/*  53 */   private static final Logger logger = LogManager.getLogger();
/*  54 */   protected static final Height height_Default = new Height(0.1F, 0.2F);
/*  55 */   protected static final Height height_ShallowWaters = new Height(-0.5F, 0.0F);
/*  56 */   protected static final Height height_Oceans = new Height(-1.0F, 0.1F);
/*  57 */   protected static final Height height_DeepOceans = new Height(-1.8F, 0.1F);
/*  58 */   protected static final Height height_LowPlains = new Height(0.125F, 0.05F);
/*  59 */   protected static final Height height_MidPlains = new Height(0.2F, 0.2F);
/*  60 */   protected static final Height height_LowHills = new Height(0.45F, 0.3F);
/*  61 */   protected static final Height height_HighPlateaus = new Height(1.5F, 0.025F);
/*  62 */   protected static final Height height_MidHills = new Height(1.0F, 0.5F);
/*  63 */   protected static final Height height_Shores = new Height(0.0F, 0.025F);
/*  64 */   protected static final Height height_RockyWaters = new Height(0.1F, 0.8F);
/*  65 */   protected static final Height height_LowIslands = new Height(0.2F, 0.3F);
/*  66 */   protected static final Height height_PartiallySubmerged = new Height(-0.2F, 0.1F);
/*  67 */   private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
/*  68 */   public static final Set<BiomeGenBase> explorationBiomesList = Sets.newHashSet();
/*  69 */   public static final Map<String, BiomeGenBase> BIOME_ID_MAP = Maps.newHashMap();
/*  70 */   public static final BiomeGenBase ocean = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
/*  71 */   public static final BiomeGenBase plains = (new BiomeGenPlains(1)).setColor(9286496).setBiomeName("Plains");
/*  72 */   public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowPlains);
/*  73 */   public static final BiomeGenBase extremeHills = (new BiomeGenHills(3, false)).setColor(6316128).setBiomeName("Extreme Hills").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
/*  74 */   public static final BiomeGenBase forest = (new BiomeGenForest(4, 0)).setColor(353825).setBiomeName("Forest");
/*  75 */   public static final BiomeGenBase taiga = (new BiomeGenTaiga(5, 0)).setColor(747097).setBiomeName("Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_MidPlains);
/*  76 */   public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setColor(522674).setBiomeName("Swampland").setFillerBlockMetadata(9154376).setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8F, 0.9F);
/*  77 */   public static final BiomeGenBase river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
/*  78 */   public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
/*  79 */   public static final BiomeGenBase sky = (new BiomeGenEnd(9)).setColor(8421631).setBiomeName("The End").setDisableRain();
/*  80 */   public static final BiomeGenBase frozenOcean = (new BiomeGenOcean(10)).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans).setTemperatureRainfall(0.0F, 0.5F);
/*  81 */   public static final BiomeGenBase frozenRiver = (new BiomeGenRiver(11)).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters).setTemperatureRainfall(0.0F, 0.5F);
/*  82 */   public static final BiomeGenBase icePlains = (new BiomeGenSnow(12, false)).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(height_LowPlains);
/*  83 */   public static final BiomeGenBase iceMountains = (new BiomeGenSnow(13, false)).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills).setTemperatureRainfall(0.0F, 0.5F);
/*  84 */   public static final BiomeGenBase mushroomIsland = (new BiomeGenMushroomIsland(14)).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_LowIslands);
/*  85 */   public static final BiomeGenBase mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_Shores);
/*  86 */   public static final BiomeGenBase beach = (new BiomeGenBeach(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setHeight(height_Shores);
/*  87 */   public static final BiomeGenBase desertHills = (new BiomeGenDesert(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowHills);
/*  88 */   public static final BiomeGenBase forestHills = (new BiomeGenForest(18, 0)).setColor(2250012).setBiomeName("ForestHills").setHeight(height_LowHills);
/*  89 */   public static final BiomeGenBase taigaHills = (new BiomeGenTaiga(19, 0)).setColor(1456435).setBiomeName("TaigaHills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_LowHills);
/*  90 */   public static final BiomeGenBase extremeHillsEdge = (new BiomeGenHills(20, true)).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2F, 0.3F);
/*  91 */   public static final BiomeGenBase jungle = (new BiomeGenJungle(21, false)).setColor(5470985).setBiomeName("Jungle").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F);
/*  92 */   public static final BiomeGenBase jungleHills = (new BiomeGenJungle(22, false)).setColor(2900485).setBiomeName("JungleHills").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.9F).setHeight(height_LowHills);
/*  93 */   public static final BiomeGenBase jungleEdge = (new BiomeGenJungle(23, true)).setColor(6458135).setBiomeName("JungleEdge").setFillerBlockMetadata(5470985).setTemperatureRainfall(0.95F, 0.8F);
/*  94 */   public static final BiomeGenBase deepOcean = (new BiomeGenOcean(24)).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
/*  95 */   public static final BiomeGenBase stoneBeach = (new BiomeGenStoneBeach(25)).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2F, 0.3F).setHeight(height_RockyWaters);
/*  96 */   public static final BiomeGenBase coldBeach = (new BiomeGenBeach(26)).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05F, 0.3F).setHeight(height_Shores).setEnableSnow();
/*  97 */   public static final BiomeGenBase birchForest = (new BiomeGenForest(27, 2)).setBiomeName("Birch Forest").setColor(3175492);
/*  98 */   public static final BiomeGenBase birchForestHills = (new BiomeGenForest(28, 2)).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills);
/*  99 */   public static final BiomeGenBase roofedForest = (new BiomeGenForest(29, 3)).setColor(4215066).setBiomeName("Roofed Forest");
/* 100 */   public static final BiomeGenBase coldTaiga = (new BiomeGenTaiga(30, 0)).setColor(3233098).setBiomeName("Cold Taiga").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_MidPlains).func_150563_c(16777215);
/* 101 */   public static final BiomeGenBase coldTaigaHills = (new BiomeGenTaiga(31, 0)).setColor(2375478).setBiomeName("Cold Taiga Hills").setFillerBlockMetadata(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_LowHills).func_150563_c(16777215);
/* 102 */   public static final BiomeGenBase megaTaiga = (new BiomeGenTaiga(32, 1)).setColor(5858897).setBiomeName("Mega Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_MidPlains);
/* 103 */   public static final BiomeGenBase megaTaigaHills = (new BiomeGenTaiga(33, 1)).setColor(4542270).setBiomeName("Mega Taiga Hills").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_LowHills);
/* 104 */   public static final BiomeGenBase extremeHillsPlus = (new BiomeGenHills(34, true)).setColor(5271632).setBiomeName("Extreme Hills+").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
/* 105 */   public static final BiomeGenBase savanna = (new BiomeGenSavanna(35)).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2F, 0.0F).setDisableRain().setHeight(height_LowPlains);
/* 106 */   public static final BiomeGenBase savannaPlateau = (new BiomeGenSavanna(36)).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0F, 0.0F).setDisableRain().setHeight(height_HighPlateaus);
/* 107 */   public static final BiomeGenBase mesa = (new BiomeGenMesa(37, false, false)).setColor(14238997).setBiomeName("Mesa");
/* 108 */   public static final BiomeGenBase mesaPlateau_F = (new BiomeGenMesa(38, false, true)).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus);
/* 109 */   public static final BiomeGenBase mesaPlateau = (new BiomeGenMesa(39, false, false)).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus);
/* 110 */   public static final BiomeGenBase field_180279_ad = ocean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public IBlockState topBlock = Blocks.grass.getDefaultState();
/* 118 */   public IBlockState fillerBlock = Blocks.dirt.getDefaultState();
/* 119 */   public int fillerBlockMetadata = 5169201;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BiomeGenBase(int id) {
/* 139 */     this.minHeight = height_Default.rootHeight;
/* 140 */     this.maxHeight = height_Default.variation;
/* 141 */     this.temperature = 0.5F;
/* 142 */     this.rainfall = 0.5F;
/* 143 */     this.waterColorMultiplier = 16777215;
/* 144 */     this.spawnableMonsterList = Lists.newArrayList();
/* 145 */     this.spawnableCreatureList = Lists.newArrayList();
/* 146 */     this.spawnableWaterCreatureList = Lists.newArrayList();
/* 147 */     this.spawnableCaveCreatureList = Lists.newArrayList();
/* 148 */     this.enableRain = true;
/* 149 */     this.worldGeneratorTrees = new WorldGenTrees(false);
/* 150 */     this.worldGeneratorBigTree = new WorldGenBigTree(false);
/* 151 */     this.worldGeneratorSwamp = new WorldGenSwamp();
/* 152 */     this.biomeID = id;
/* 153 */     biomeList[id] = this;
/* 154 */     this.theBiomeDecorator = createBiomeDecorator();
/* 155 */     this.spawnableCreatureList.add(new SpawnListEntry((Class)EntitySheep.class, 12, 4, 4));
/* 156 */     this.spawnableCreatureList.add(new SpawnListEntry((Class)EntityRabbit.class, 10, 3, 3));
/* 157 */     this.spawnableCreatureList.add(new SpawnListEntry((Class)EntityPig.class, 10, 4, 4));
/* 158 */     this.spawnableCreatureList.add(new SpawnListEntry((Class)EntityChicken.class, 10, 4, 4));
/* 159 */     this.spawnableCreatureList.add(new SpawnListEntry((Class)EntityCow.class, 8, 4, 4));
/* 160 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntitySpider.class, 100, 4, 4));
/* 161 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntityZombie.class, 100, 4, 4));
/* 162 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntitySkeleton.class, 100, 4, 4));
/* 163 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntityCreeper.class, 100, 4, 4));
/* 164 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntitySlime.class, 100, 4, 4));
/* 165 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntityEnderman.class, 10, 1, 4));
/* 166 */     this.spawnableMonsterList.add(new SpawnListEntry((Class)EntityWitch.class, 5, 1, 1));
/* 167 */     this.spawnableWaterCreatureList.add(new SpawnListEntry((Class)EntitySquid.class, 10, 4, 4));
/* 168 */     this.spawnableCaveCreatureList.add(new SpawnListEntry((Class)EntityBat.class, 10, 8, 8));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeDecorator createBiomeDecorator() {
/* 173 */     return new BiomeDecorator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setTemperatureRainfall(float temperatureIn, float rainfallIn) {
/* 178 */     if (temperatureIn > 0.1F && temperatureIn < 0.2F)
/*     */     {
/* 180 */       throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
/*     */     }
/*     */ 
/*     */     
/* 184 */     this.temperature = temperatureIn;
/* 185 */     this.rainfall = rainfallIn;
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BiomeGenBase setHeight(Height heights) {
/* 192 */     this.minHeight = heights.rootHeight;
/* 193 */     this.maxHeight = heights.variation;
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setDisableRain() {
/* 199 */     this.enableRain = false;
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldGenAbstractTree genBigTreeChance(Random rand) {
/* 205 */     return (rand.nextInt(10) == 0) ? (WorldGenAbstractTree)this.worldGeneratorBigTree : (WorldGenAbstractTree)this.worldGeneratorTrees;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldGenerator getRandomWorldGenForGrass(Random rand) {
/* 210 */     return (WorldGenerator)new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) {
/* 215 */     return (rand.nextInt(3) > 0) ? BlockFlower.EnumFlowerType.DANDELION : BlockFlower.EnumFlowerType.POPPY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setEnableSnow() {
/* 220 */     this.enableSnow = true;
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setBiomeName(String name) {
/* 226 */     this.biomeName = name;
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setFillerBlockMetadata(int meta) {
/* 232 */     this.fillerBlockMetadata = meta;
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase setColor(int colorIn) {
/* 238 */     func_150557_a(colorIn, false);
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase func_150563_c(int p_150563_1_) {
/* 244 */     this.field_150609_ah = p_150563_1_;
/* 245 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase func_150557_a(int colorIn, boolean p_150557_2_) {
/* 250 */     this.color = colorIn;
/*     */     
/* 252 */     if (p_150557_2_) {
/*     */       
/* 254 */       this.field_150609_ah = (colorIn & 0xFEFEFE) >> 1;
/*     */     }
/*     */     else {
/*     */       
/* 258 */       this.field_150609_ah = colorIn;
/*     */     } 
/*     */     
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkyColorByTemp(float p_76731_1_) {
/* 266 */     p_76731_1_ /= 3.0F;
/* 267 */     p_76731_1_ = MathHelper.clamp_float(p_76731_1_, -1.0F, 1.0F);
/* 268 */     return MathHelper.hsvToRGB(0.62222224F - p_76731_1_ * 0.05F, 0.5F + p_76731_1_ * 0.1F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
/* 273 */     switch (creatureType) {
/*     */       
/*     */       case MONSTER:
/* 276 */         return this.spawnableMonsterList;
/*     */       
/*     */       case CREATURE:
/* 279 */         return this.spawnableCreatureList;
/*     */       
/*     */       case WATER_CREATURE:
/* 282 */         return this.spawnableWaterCreatureList;
/*     */       
/*     */       case AMBIENT:
/* 285 */         return this.spawnableCaveCreatureList;
/*     */     } 
/*     */     
/* 288 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getEnableSnow() {
/* 294 */     return isSnowyBiome();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRain() {
/* 299 */     return isSnowyBiome() ? false : this.enableRain;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHighHumidity() {
/* 304 */     return (this.rainfall > 0.85F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSpawningChance() {
/* 309 */     return 0.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getIntRainfall() {
/* 314 */     return (int)(this.rainfall * 65536.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getFloatRainfall() {
/* 319 */     return this.rainfall;
/*     */   }
/*     */ 
/*     */   
/*     */   public final float getFloatTemperature(BlockPos pos) {
/* 324 */     if (pos.getY() > 64) {
/*     */       
/* 326 */       float f = (float)(temperatureNoise.func_151601_a(pos.getX() * 1.0D / 8.0D, pos.getZ() * 1.0D / 8.0D) * 4.0D);
/* 327 */       return this.temperature - (f + pos.getY() - 64.0F) * 0.05F / 30.0F;
/*     */     } 
/*     */ 
/*     */     
/* 331 */     return this.temperature;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos) {
/* 337 */     this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getGrassColorAtPos(BlockPos pos) {
/* 342 */     double d0 = MathHelper.clamp_float(getFloatTemperature(pos), 0.0F, 1.0F);
/* 343 */     double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
/* 344 */     return ColorizerGrass.getGrassColor(d0, d1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFoliageColorAtPos(BlockPos pos) {
/* 349 */     double d0 = MathHelper.clamp_float(getFloatTemperature(pos), 0.0F, 1.0F);
/* 350 */     double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
/* 351 */     return ColorizerFoliage.getFoliageColor(d0, d1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSnowyBiome() {
/* 356 */     return this.enableSnow;
/*     */   }
/*     */ 
/*     */   
/*     */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
/* 361 */     generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void generateBiomeTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
/* 366 */     int i = worldIn.getSeaLevel();
/* 367 */     IBlockState iblockstate = this.topBlock;
/* 368 */     IBlockState iblockstate1 = this.fillerBlock;
/* 369 */     int j = -1;
/* 370 */     int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
/* 371 */     int l = x & 0xF;
/* 372 */     int i1 = z & 0xF;
/* 373 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 375 */     for (int j1 = 255; j1 >= 0; j1--) {
/*     */       
/* 377 */       if (j1 <= rand.nextInt(5)) {
/*     */         
/* 379 */         chunkPrimerIn.setBlockState(i1, j1, l, Blocks.bedrock.getDefaultState());
/*     */       }
/*     */       else {
/*     */         
/* 383 */         IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);
/*     */         
/* 385 */         if (iblockstate2.getBlock().getMaterial() == Material.air) {
/*     */           
/* 387 */           j = -1;
/*     */         }
/* 389 */         else if (iblockstate2.getBlock() == Blocks.stone) {
/*     */           
/* 391 */           if (j == -1) {
/*     */             
/* 393 */             if (k <= 0) {
/*     */               
/* 395 */               iblockstate = null;
/* 396 */               iblockstate1 = Blocks.stone.getDefaultState();
/*     */             }
/* 398 */             else if (j1 >= i - 4 && j1 <= i + 1) {
/*     */               
/* 400 */               iblockstate = this.topBlock;
/* 401 */               iblockstate1 = this.fillerBlock;
/*     */             } 
/*     */             
/* 404 */             if (j1 < i && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air))
/*     */             {
/* 406 */               if (getFloatTemperature((BlockPos)blockpos$mutableblockpos.set(x, j1, z)) < 0.15F) {
/*     */                 
/* 408 */                 iblockstate = Blocks.ice.getDefaultState();
/*     */               }
/*     */               else {
/*     */                 
/* 412 */                 iblockstate = Blocks.water.getDefaultState();
/*     */               } 
/*     */             }
/*     */             
/* 416 */             j = k;
/*     */             
/* 418 */             if (j1 >= i - 1)
/*     */             {
/* 420 */               chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
/*     */             }
/* 422 */             else if (j1 < i - 7 - k)
/*     */             {
/* 424 */               iblockstate = null;
/* 425 */               iblockstate1 = Blocks.stone.getDefaultState();
/* 426 */               chunkPrimerIn.setBlockState(i1, j1, l, Blocks.gravel.getDefaultState());
/*     */             }
/*     */             else
/*     */             {
/* 430 */               chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
/*     */             }
/*     */           
/* 433 */           } else if (j > 0) {
/*     */             
/* 435 */             j--;
/* 436 */             chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
/*     */             
/* 438 */             if (j == 0 && iblockstate1.getBlock() == Blocks.sand) {
/*     */               
/* 440 */               j = rand.nextInt(4) + Math.max(0, j1 - 63);
/* 441 */               iblockstate1 = (iblockstate1.getValue((IProperty)BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND) ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase createMutation() {
/* 451 */     return createMutatedBiome(this.biomeID + 128);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 456 */     return new BiomeGenMutated(p_180277_1_, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<? extends BiomeGenBase> getBiomeClass() {
/* 461 */     return (Class)getClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEqualTo(BiomeGenBase biome) {
/* 466 */     return (biome == this) ? true : ((biome == null) ? false : ((getBiomeClass() == biome.getBiomeClass())));
/*     */   }
/*     */ 
/*     */   
/*     */   public TempCategory getTempCategory() {
/* 471 */     return (this.temperature < 0.2D) ? TempCategory.COLD : ((this.temperature < 1.0D) ? TempCategory.MEDIUM : TempCategory.WARM);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BiomeGenBase[] getBiomeGenArray() {
/* 476 */     return biomeList;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BiomeGenBase getBiome(int id) {
/* 481 */     return getBiomeFromBiomeList(id, (BiomeGenBase)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BiomeGenBase getBiomeFromBiomeList(int biomeId, BiomeGenBase biome) {
/* 486 */     if (biomeId >= 0 && biomeId <= biomeList.length) {
/*     */       
/* 488 */       BiomeGenBase biomegenbase = biomeList[biomeId];
/* 489 */       return (biomegenbase == null) ? biome : biomegenbase;
/*     */     } 
/*     */ 
/*     */     
/* 493 */     logger.warn("Biome ID is out of bounds: " + biomeId + ", defaulting to 0 (Ocean)");
/* 494 */     return ocean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 500 */     plains.createMutation();
/* 501 */     desert.createMutation();
/* 502 */     forest.createMutation();
/* 503 */     taiga.createMutation();
/* 504 */     swampland.createMutation();
/* 505 */     icePlains.createMutation();
/* 506 */     jungle.createMutation();
/* 507 */     jungleEdge.createMutation();
/* 508 */     coldTaiga.createMutation();
/* 509 */     savanna.createMutation();
/* 510 */     savannaPlateau.createMutation();
/* 511 */     mesa.createMutation();
/* 512 */     mesaPlateau_F.createMutation();
/* 513 */     mesaPlateau.createMutation();
/* 514 */     birchForest.createMutation();
/* 515 */     birchForestHills.createMutation();
/* 516 */     roofedForest.createMutation();
/* 517 */     megaTaiga.createMutation();
/* 518 */     extremeHills.createMutation();
/* 519 */     extremeHillsPlus.createMutation();
/* 520 */     megaTaiga.createMutatedBiome(megaTaigaHills.biomeID + 128).setBiomeName("Redwood Taiga Hills M");
/*     */     
/* 522 */     for (BiomeGenBase biomegenbase : biomeList) {
/*     */       
/* 524 */       if (biomegenbase != null) {
/*     */         
/* 526 */         if (BIOME_ID_MAP.containsKey(biomegenbase.biomeName))
/*     */         {
/* 528 */           throw new Error("Biome \"" + biomegenbase.biomeName + "\" is defined as both ID " + ((BiomeGenBase)BIOME_ID_MAP.get(biomegenbase.biomeName)).biomeID + " and " + biomegenbase.biomeID);
/*     */         }
/*     */         
/* 531 */         BIOME_ID_MAP.put(biomegenbase.biomeName, biomegenbase);
/*     */         
/* 533 */         if (biomegenbase.biomeID < 128)
/*     */         {
/* 535 */           explorationBiomesList.add(biomegenbase);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 540 */     explorationBiomesList.remove(hell);
/* 541 */     explorationBiomesList.remove(sky);
/* 542 */     explorationBiomesList.remove(frozenOcean);
/* 543 */     explorationBiomesList.remove(extremeHillsEdge);
/* 544 */   } protected static final NoiseGeneratorPerlin temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
/* 545 */   protected static final NoiseGeneratorPerlin GRASS_COLOR_NOISE = new NoiseGeneratorPerlin(new Random(2345L), 1);
/* 546 */   protected static final WorldGenDoublePlant DOUBLE_PLANT_GENERATOR = new WorldGenDoublePlant(); public String biomeName; public int color; public int field_150609_ah; public float minHeight; public float maxHeight; public float temperature; public float rainfall; public int waterColorMultiplier; public BiomeDecorator theBiomeDecorator; protected List<SpawnListEntry> spawnableMonsterList; protected List<SpawnListEntry> spawnableCreatureList; protected List<SpawnListEntry> spawnableWaterCreatureList; protected List<SpawnListEntry> spawnableCaveCreatureList; protected boolean enableSnow;
/*     */   protected boolean enableRain;
/*     */   public final int biomeID;
/*     */   protected WorldGenTrees worldGeneratorTrees;
/*     */   protected WorldGenBigTree worldGeneratorBigTree;
/*     */   protected WorldGenSwamp worldGeneratorSwamp;
/*     */   
/*     */   public static class Height { public float rootHeight;
/*     */     
/*     */     public Height(float rootHeightIn, float variationIn) {
/* 556 */       this.rootHeight = rootHeightIn;
/* 557 */       this.variation = variationIn;
/*     */     }
/*     */     public float variation;
/*     */     
/*     */     public Height attenuate() {
/* 562 */       return new Height(this.rootHeight * 0.8F, this.variation * 0.6F);
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class SpawnListEntry
/*     */     extends WeightedRandom.Item
/*     */   {
/*     */     public Class<? extends EntityLiving> entityClass;
/*     */     public int minGroupCount;
/*     */     public int maxGroupCount;
/*     */     
/*     */     public SpawnListEntry(Class<? extends EntityLiving> entityclassIn, int weight, int groupCountMin, int groupCountMax) {
/* 574 */       super(weight);
/* 575 */       this.entityClass = entityclassIn;
/* 576 */       this.minGroupCount = groupCountMin;
/* 577 */       this.maxGroupCount = groupCountMax;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 582 */       return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum TempCategory
/*     */   {
/* 588 */     OCEAN,
/* 589 */     COLD,
/* 590 */     MEDIUM,
/* 591 */     WARM;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeGenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */