/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockDirt;
/*     */ import net.minecraft.block.BlockDoublePlant;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenBlockBlob;
/*     */ import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenTaiga1;
/*     */ import net.minecraft.world.gen.feature.WorldGenTaiga2;
/*     */ import net.minecraft.world.gen.feature.WorldGenTallGrass;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ 
/*     */ public class BiomeGenTaiga extends BiomeGenBase {
/*  22 */   private static final WorldGenTaiga1 field_150639_aC = new WorldGenTaiga1();
/*  23 */   private static final WorldGenTaiga2 field_150640_aD = new WorldGenTaiga2(false);
/*  24 */   private static final WorldGenMegaPineTree field_150641_aE = new WorldGenMegaPineTree(false, false);
/*  25 */   private static final WorldGenMegaPineTree field_150642_aF = new WorldGenMegaPineTree(false, true);
/*  26 */   private static final WorldGenBlockBlob field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
/*     */   
/*     */   private int field_150644_aH;
/*     */   
/*     */   public BiomeGenTaiga(int id, int p_i45385_2_) {
/*  31 */     super(id);
/*  32 */     this.field_150644_aH = p_i45385_2_;
/*  33 */     this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry((Class)EntityWolf.class, 8, 4, 4));
/*  34 */     this.theBiomeDecorator.treesPerChunk = 10;
/*     */     
/*  36 */     if (p_i45385_2_ != 1 && p_i45385_2_ != 2) {
/*     */       
/*  38 */       this.theBiomeDecorator.grassPerChunk = 1;
/*  39 */       this.theBiomeDecorator.mushroomsPerChunk = 1;
/*     */     }
/*     */     else {
/*     */       
/*  43 */       this.theBiomeDecorator.grassPerChunk = 7;
/*  44 */       this.theBiomeDecorator.deadBushPerChunk = 1;
/*  45 */       this.theBiomeDecorator.mushroomsPerChunk = 3;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldGenAbstractTree genBigTreeChance(Random rand) {
/*  51 */     return ((this.field_150644_aH == 1 || this.field_150644_aH == 2) && rand.nextInt(3) == 0) ? ((this.field_150644_aH != 2 && rand.nextInt(13) != 0) ? (WorldGenAbstractTree)field_150641_aE : (WorldGenAbstractTree)field_150642_aF) : ((rand.nextInt(3) == 0) ? (WorldGenAbstractTree)field_150639_aC : (WorldGenAbstractTree)field_150640_aD);
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldGenerator getRandomWorldGenForGrass(Random rand) {
/*  56 */     return (rand.nextInt(5) > 0) ? (WorldGenerator)new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : (WorldGenerator)new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos) {
/*  61 */     if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
/*     */       
/*  63 */       int i = rand.nextInt(3);
/*     */       
/*  65 */       for (int j = 0; j < i; j++) {
/*     */         
/*  67 */         int k = rand.nextInt(16) + 8;
/*  68 */         int l = rand.nextInt(16) + 8;
/*  69 */         BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
/*  70 */         field_150643_aG.generate(worldIn, rand, blockpos);
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.FERN);
/*     */     
/*  76 */     for (int i1 = 0; i1 < 7; i1++) {
/*     */       
/*  78 */       int j1 = rand.nextInt(16) + 8;
/*  79 */       int k1 = rand.nextInt(16) + 8;
/*  80 */       int l1 = rand.nextInt(worldIn.getHeight(pos.add(j1, 0, k1)).getY() + 32);
/*  81 */       DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j1, l1, k1));
/*     */     } 
/*     */     
/*  84 */     super.decorate(worldIn, rand, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
/*  89 */     if (this.field_150644_aH == 1 || this.field_150644_aH == 2) {
/*     */       
/*  91 */       this.topBlock = Blocks.grass.getDefaultState();
/*  92 */       this.fillerBlock = Blocks.dirt.getDefaultState();
/*     */       
/*  94 */       if (noiseVal > 1.75D) {
/*     */         
/*  96 */         this.topBlock = Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.COARSE_DIRT);
/*     */       }
/*  98 */       else if (noiseVal > -0.95D) {
/*     */         
/* 100 */         this.topBlock = Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.PODZOL);
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 109 */     return (this.biomeID == BiomeGenBase.megaTaiga.biomeID) ? (new BiomeGenTaiga(p_180277_1_, 2)).func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight)) : super.createMutatedBiome(p_180277_1_);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeGenTaiga.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */