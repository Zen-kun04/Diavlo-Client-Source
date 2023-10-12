/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockDirt;
/*    */ import net.minecraft.block.BlockDoublePlant;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.ChunkPrimer;
/*    */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*    */ import net.minecraft.world.gen.feature.WorldGenSavannaTree;
/*    */ 
/*    */ public class BiomeGenSavanna extends BiomeGenBase {
/* 16 */   private static final WorldGenSavannaTree field_150627_aC = new WorldGenSavannaTree(false);
/*    */ 
/*    */   
/*    */   protected BiomeGenSavanna(int id) {
/* 20 */     super(id);
/* 21 */     this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry((Class)EntityHorse.class, 1, 2, 6));
/* 22 */     this.theBiomeDecorator.treesPerChunk = 1;
/* 23 */     this.theBiomeDecorator.flowersPerChunk = 4;
/* 24 */     this.theBiomeDecorator.grassPerChunk = 20;
/*    */   }
/*    */ 
/*    */   
/*    */   public WorldGenAbstractTree genBigTreeChance(Random rand) {
/* 29 */     return (rand.nextInt(5) > 0) ? (WorldGenAbstractTree)field_150627_aC : (WorldGenAbstractTree)this.worldGeneratorTrees;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_) {
/* 34 */     BiomeGenBase biomegenbase = new Mutated(p_180277_1_, this);
/* 35 */     biomegenbase.temperature = (this.temperature + 1.0F) * 0.5F;
/* 36 */     biomegenbase.minHeight = this.minHeight * 0.5F + 0.3F;
/* 37 */     biomegenbase.maxHeight = this.maxHeight * 0.5F + 1.2F;
/* 38 */     return biomegenbase;
/*    */   }
/*    */ 
/*    */   
/*    */   public void decorate(World worldIn, Random rand, BlockPos pos) {
/* 43 */     DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
/*    */     
/* 45 */     for (int i = 0; i < 7; i++) {
/*    */       
/* 47 */       int j = rand.nextInt(16) + 8;
/* 48 */       int k = rand.nextInt(16) + 8;
/* 49 */       int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
/* 50 */       DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
/*    */     } 
/*    */     
/* 53 */     super.decorate(worldIn, rand, pos);
/*    */   }
/*    */   
/*    */   public static class Mutated
/*    */     extends BiomeGenMutated
/*    */   {
/*    */     public Mutated(int p_i45382_1_, BiomeGenBase p_i45382_2_) {
/* 60 */       super(p_i45382_1_, p_i45382_2_);
/* 61 */       this.theBiomeDecorator.treesPerChunk = 2;
/* 62 */       this.theBiomeDecorator.flowersPerChunk = 2;
/* 63 */       this.theBiomeDecorator.grassPerChunk = 5;
/*    */     }
/*    */ 
/*    */     
/*    */     public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
/* 68 */       this.topBlock = Blocks.grass.getDefaultState();
/* 69 */       this.fillerBlock = Blocks.dirt.getDefaultState();
/*    */       
/* 71 */       if (noiseVal > 1.75D) {
/*    */         
/* 73 */         this.topBlock = Blocks.stone.getDefaultState();
/* 74 */         this.fillerBlock = Blocks.stone.getDefaultState();
/*    */       }
/* 76 */       else if (noiseVal > -0.5D) {
/*    */         
/* 78 */         this.topBlock = Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.COARSE_DIRT);
/*    */       } 
/*    */       
/* 81 */       generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
/*    */     }
/*    */ 
/*    */     
/*    */     public void decorate(World worldIn, Random rand, BlockPos pos) {
/* 86 */       this.theBiomeDecorator.decorate(worldIn, rand, this, pos);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeGenSavanna.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */