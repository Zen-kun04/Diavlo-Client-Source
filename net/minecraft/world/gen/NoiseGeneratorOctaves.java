/*    */ package net.minecraft.world.gen;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class NoiseGeneratorOctaves
/*    */   extends NoiseGenerator
/*    */ {
/*    */   private NoiseGeneratorImproved[] generatorCollection;
/*    */   private int octaves;
/*    */   
/*    */   public NoiseGeneratorOctaves(Random seed, int octavesIn) {
/* 13 */     this.octaves = octavesIn;
/* 14 */     this.generatorCollection = new NoiseGeneratorImproved[octavesIn];
/*    */     
/* 16 */     for (int i = 0; i < octavesIn; i++)
/*    */     {
/* 18 */       this.generatorCollection[i] = new NoiseGeneratorImproved(seed);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public double[] generateNoiseOctaves(double[] noiseArray, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale) {
/* 24 */     if (noiseArray == null) {
/*    */       
/* 26 */       noiseArray = new double[xSize * ySize * zSize];
/*    */     }
/*    */     else {
/*    */       
/* 30 */       for (int i = 0; i < noiseArray.length; i++)
/*    */       {
/* 32 */         noiseArray[i] = 0.0D;
/*    */       }
/*    */     } 
/*    */     
/* 36 */     double d3 = 1.0D;
/*    */     
/* 38 */     for (int j = 0; j < this.octaves; j++) {
/*    */       
/* 40 */       double d0 = xOffset * d3 * xScale;
/* 41 */       double d1 = yOffset * d3 * yScale;
/* 42 */       double d2 = zOffset * d3 * zScale;
/* 43 */       long k = MathHelper.floor_double_long(d0);
/* 44 */       long l = MathHelper.floor_double_long(d2);
/* 45 */       d0 -= k;
/* 46 */       d2 -= l;
/* 47 */       k %= 16777216L;
/* 48 */       l %= 16777216L;
/* 49 */       d0 += k;
/* 50 */       d2 += l;
/* 51 */       this.generatorCollection[j].populateNoiseArray(noiseArray, d0, d1, d2, xSize, ySize, zSize, xScale * d3, yScale * d3, zScale * d3, d3);
/* 52 */       d3 /= 2.0D;
/*    */     } 
/*    */     
/* 55 */     return noiseArray;
/*    */   }
/*    */ 
/*    */   
/*    */   public double[] generateNoiseOctaves(double[] noiseArray, int xOffset, int zOffset, int xSize, int zSize, double xScale, double zScale, double p_76305_10_) {
/* 60 */     return generateNoiseOctaves(noiseArray, xOffset, 10, zOffset, xSize, 1, zSize, xScale, 1.0D, zScale);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\NoiseGeneratorOctaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */