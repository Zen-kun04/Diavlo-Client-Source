/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.ChunkPrimer;
/*    */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*    */ 
/*    */ public class BiomeGenMutated
/*    */   extends BiomeGenBase
/*    */ {
/*    */   protected BiomeGenBase baseBiome;
/*    */   
/*    */   public BiomeGenMutated(int id, BiomeGenBase biome) {
/* 16 */     super(id);
/* 17 */     this.baseBiome = biome;
/* 18 */     func_150557_a(biome.color, true);
/* 19 */     this.biomeName = biome.biomeName + " M";
/* 20 */     this.topBlock = biome.topBlock;
/* 21 */     this.fillerBlock = biome.fillerBlock;
/* 22 */     this.fillerBlockMetadata = biome.fillerBlockMetadata;
/* 23 */     this.minHeight = biome.minHeight;
/* 24 */     this.maxHeight = biome.maxHeight;
/* 25 */     this.temperature = biome.temperature;
/* 26 */     this.rainfall = biome.rainfall;
/* 27 */     this.waterColorMultiplier = biome.waterColorMultiplier;
/* 28 */     this.enableSnow = biome.enableSnow;
/* 29 */     this.enableRain = biome.enableRain;
/* 30 */     this.spawnableCreatureList = Lists.newArrayList(biome.spawnableCreatureList);
/* 31 */     this.spawnableMonsterList = Lists.newArrayList(biome.spawnableMonsterList);
/* 32 */     this.spawnableCaveCreatureList = Lists.newArrayList(biome.spawnableCaveCreatureList);
/* 33 */     this.spawnableWaterCreatureList = Lists.newArrayList(biome.spawnableWaterCreatureList);
/* 34 */     this.temperature = biome.temperature;
/* 35 */     this.rainfall = biome.rainfall;
/* 36 */     this.minHeight = biome.minHeight + 0.1F;
/* 37 */     this.maxHeight = biome.maxHeight + 0.2F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void decorate(World worldIn, Random rand, BlockPos pos) {
/* 42 */     this.baseBiome.theBiomeDecorator.decorate(worldIn, rand, this, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
/* 47 */     this.baseBiome.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getSpawningChance() {
/* 52 */     return this.baseBiome.getSpawningChance();
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldGenAbstractTree genBigTreeChance(Random rand) {
/* 57 */     return this.baseBiome.genBigTreeChance(rand);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFoliageColorAtPos(BlockPos pos) {
/* 62 */     return this.baseBiome.getFoliageColorAtPos(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getGrassColorAtPos(BlockPos pos) {
/* 67 */     return this.baseBiome.getGrassColorAtPos(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends BiomeGenBase> getBiomeClass() {
/* 72 */     return this.baseBiome.getBiomeClass();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEqualTo(BiomeGenBase biome) {
/* 77 */     return this.baseBiome.isEqualTo(biome);
/*    */   }
/*    */ 
/*    */   
/*    */   public BiomeGenBase.TempCategory getTempCategory() {
/* 82 */     return this.baseBiome.getTempCategory();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeGenMutated.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */