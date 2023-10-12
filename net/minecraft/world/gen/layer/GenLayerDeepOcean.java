/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerDeepOcean
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerDeepOcean(long p_i45472_1_, GenLayer p_i45472_3_) {
/*  9 */     super(p_i45472_1_);
/* 10 */     this.parent = p_i45472_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 15 */     int i = areaX - 1;
/* 16 */     int j = areaY - 1;
/* 17 */     int k = areaWidth + 2;
/* 18 */     int l = areaHeight + 2;
/* 19 */     int[] aint = this.parent.getInts(i, j, k, l);
/* 20 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 22 */     for (int i1 = 0; i1 < areaHeight; i1++) {
/*    */       
/* 24 */       for (int j1 = 0; j1 < areaWidth; j1++) {
/*    */         
/* 26 */         int k1 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
/* 27 */         int l1 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
/* 28 */         int i2 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
/* 29 */         int j2 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];
/* 30 */         int k2 = aint[j1 + 1 + (i1 + 1) * k];
/* 31 */         int l2 = 0;
/*    */         
/* 33 */         if (k1 == 0)
/*    */         {
/* 35 */           l2++;
/*    */         }
/*    */         
/* 38 */         if (l1 == 0)
/*    */         {
/* 40 */           l2++;
/*    */         }
/*    */         
/* 43 */         if (i2 == 0)
/*    */         {
/* 45 */           l2++;
/*    */         }
/*    */         
/* 48 */         if (j2 == 0)
/*    */         {
/* 50 */           l2++;
/*    */         }
/*    */         
/* 53 */         if (k2 == 0 && l2 > 3) {
/*    */           
/* 55 */           aint1[j1 + i1 * areaWidth] = BiomeGenBase.deepOcean.biomeID;
/*    */         }
/*    */         else {
/*    */           
/* 59 */           aint1[j1 + i1 * areaWidth] = k2;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerDeepOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */