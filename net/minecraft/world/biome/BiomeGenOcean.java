/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.ChunkPrimer;
/*    */ 
/*    */ public class BiomeGenOcean
/*    */   extends BiomeGenBase
/*    */ {
/*    */   public BiomeGenOcean(int id) {
/* 11 */     super(id);
/* 12 */     this.spawnableCreatureList.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public BiomeGenBase.TempCategory getTempCategory() {
/* 17 */     return BiomeGenBase.TempCategory.OCEAN;
/*    */   }
/*    */ 
/*    */   
/*    */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
/* 22 */     super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\biome\BiomeGenOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */