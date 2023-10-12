/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerRemoveTooMuchOcean
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRemoveTooMuchOcean(long p_i45480_1_, GenLayer p_i45480_3_) {
/*  7 */     super(p_i45480_1_);
/*  8 */     this.parent = p_i45480_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 13 */     int i = areaX - 1;
/* 14 */     int j = areaY - 1;
/* 15 */     int k = areaWidth + 2;
/* 16 */     int l = areaHeight + 2;
/* 17 */     int[] aint = this.parent.getInts(i, j, k, l);
/* 18 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 20 */     for (int i1 = 0; i1 < areaHeight; i1++) {
/*    */       
/* 22 */       for (int j1 = 0; j1 < areaWidth; j1++) {
/*    */         
/* 24 */         int k1 = aint[j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2)];
/* 25 */         int l1 = aint[j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2)];
/* 26 */         int i2 = aint[j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2)];
/* 27 */         int j2 = aint[j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2)];
/* 28 */         int k2 = aint[j1 + 1 + (i1 + 1) * k];
/* 29 */         aint1[j1 + i1 * areaWidth] = k2;
/* 30 */         initChunkSeed((j1 + areaX), (i1 + areaY));
/*    */         
/* 32 */         if (k2 == 0 && k1 == 0 && l1 == 0 && i2 == 0 && j2 == 0 && nextInt(2) == 0)
/*    */         {
/* 34 */           aint1[j1 + i1 * areaWidth] = 1;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 39 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerRemoveTooMuchOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */