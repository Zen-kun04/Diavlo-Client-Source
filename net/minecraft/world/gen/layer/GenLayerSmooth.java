/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerSmooth
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerSmooth(long p_i2131_1_, GenLayer p_i2131_3_) {
/*  7 */     super(p_i2131_1_);
/*  8 */     this.parent = p_i2131_3_;
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
/* 24 */         int k1 = aint[j1 + 0 + (i1 + 1) * k];
/* 25 */         int l1 = aint[j1 + 2 + (i1 + 1) * k];
/* 26 */         int i2 = aint[j1 + 1 + (i1 + 0) * k];
/* 27 */         int j2 = aint[j1 + 1 + (i1 + 2) * k];
/* 28 */         int k2 = aint[j1 + 1 + (i1 + 1) * k];
/*    */         
/* 30 */         if (k1 == l1 && i2 == j2) {
/*    */           
/* 32 */           initChunkSeed((j1 + areaX), (i1 + areaY));
/*    */           
/* 34 */           if (nextInt(2) == 0)
/*    */           {
/* 36 */             k2 = k1;
/*    */           }
/*    */           else
/*    */           {
/* 40 */             k2 = i2;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 45 */           if (k1 == l1)
/*    */           {
/* 47 */             k2 = k1;
/*    */           }
/*    */           
/* 50 */           if (i2 == j2)
/*    */           {
/* 52 */             k2 = i2;
/*    */           }
/*    */         } 
/*    */         
/* 56 */         aint1[j1 + i1 * areaWidth] = k2;
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerSmooth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */