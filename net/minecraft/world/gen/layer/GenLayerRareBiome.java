/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRareBiome
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRareBiome(long p_i45478_1_, GenLayer p_i45478_3_) {
/*  9 */     super(p_i45478_1_);
/* 10 */     this.parent = p_i45478_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 15 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/* 16 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 18 */     for (int i = 0; i < areaHeight; i++) {
/*    */       
/* 20 */       for (int j = 0; j < areaWidth; j++) {
/*    */         
/* 22 */         initChunkSeed((j + areaX), (i + areaY));
/* 23 */         int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
/*    */         
/* 25 */         if (nextInt(57) == 0) {
/*    */           
/* 27 */           if (k == BiomeGenBase.plains.biomeID)
/*    */           {
/* 29 */             aint1[j + i * areaWidth] = BiomeGenBase.plains.biomeID + 128;
/*    */           }
/*    */           else
/*    */           {
/* 33 */             aint1[j + i * areaWidth] = k;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 38 */           aint1[j + i * areaWidth] = k;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 43 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerRareBiome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */