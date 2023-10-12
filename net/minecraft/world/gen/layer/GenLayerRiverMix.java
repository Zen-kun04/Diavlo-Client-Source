/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRiverMix
/*    */   extends GenLayer
/*    */ {
/*    */   private GenLayer biomePatternGeneratorChain;
/*    */   private GenLayer riverPatternGeneratorChain;
/*    */   
/*    */   public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_) {
/* 12 */     super(p_i2129_1_);
/* 13 */     this.biomePatternGeneratorChain = p_i2129_3_;
/* 14 */     this.riverPatternGeneratorChain = p_i2129_4_;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initWorldGenSeed(long seed) {
/* 19 */     this.biomePatternGeneratorChain.initWorldGenSeed(seed);
/* 20 */     this.riverPatternGeneratorChain.initWorldGenSeed(seed);
/* 21 */     super.initWorldGenSeed(seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 26 */     int[] aint = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
/* 27 */     int[] aint1 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
/* 28 */     int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 30 */     for (int i = 0; i < areaWidth * areaHeight; i++) {
/*    */       
/* 32 */       if (aint[i] != BiomeGenBase.ocean.biomeID && aint[i] != BiomeGenBase.deepOcean.biomeID) {
/*    */         
/* 34 */         if (aint1[i] == BiomeGenBase.river.biomeID) {
/*    */           
/* 36 */           if (aint[i] == BiomeGenBase.icePlains.biomeID)
/*    */           {
/* 38 */             aint2[i] = BiomeGenBase.frozenRiver.biomeID;
/*    */           }
/* 40 */           else if (aint[i] != BiomeGenBase.mushroomIsland.biomeID && aint[i] != BiomeGenBase.mushroomIslandShore.biomeID)
/*    */           {
/* 42 */             aint2[i] = aint1[i] & 0xFF;
/*    */           }
/*    */           else
/*    */           {
/* 46 */             aint2[i] = BiomeGenBase.mushroomIslandShore.biomeID;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 51 */           aint2[i] = aint[i];
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 56 */         aint2[i] = aint[i];
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     return aint2;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerRiverMix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */