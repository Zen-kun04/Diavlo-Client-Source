/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerZoom
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerZoom(long p_i2134_1_, GenLayer p_i2134_3_) {
/*  7 */     super(p_i2134_1_);
/*  8 */     this.parent = p_i2134_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 13 */     int i = areaX >> 1;
/* 14 */     int j = areaY >> 1;
/* 15 */     int k = (areaWidth >> 1) + 2;
/* 16 */     int l = (areaHeight >> 1) + 2;
/* 17 */     int[] aint = this.parent.getInts(i, j, k, l);
/* 18 */     int i1 = k - 1 << 1;
/* 19 */     int j1 = l - 1 << 1;
/* 20 */     int[] aint1 = IntCache.getIntCache(i1 * j1);
/*    */     
/* 22 */     for (int k1 = 0; k1 < l - 1; k1++) {
/*    */       
/* 24 */       int l1 = (k1 << 1) * i1;
/* 25 */       int i2 = 0;
/* 26 */       int j2 = aint[i2 + 0 + (k1 + 0) * k];
/*    */       
/* 28 */       for (int k2 = aint[i2 + 0 + (k1 + 1) * k]; i2 < k - 1; i2++) {
/*    */         
/* 30 */         initChunkSeed((i2 + i << 1), (k1 + j << 1));
/* 31 */         int l2 = aint[i2 + 1 + (k1 + 0) * k];
/* 32 */         int i3 = aint[i2 + 1 + (k1 + 1) * k];
/* 33 */         aint1[l1] = j2;
/* 34 */         aint1[l1++ + i1] = selectRandom2(j2, k2);
/* 35 */         aint1[l1] = selectRandom2(j2, l2);
/* 36 */         aint1[l1++ + i1] = selectModeOrRandom(j2, l2, k2, i3);
/* 37 */         j2 = l2;
/* 38 */         k2 = i3;
/*    */       } 
/*    */     } 
/*    */     
/* 42 */     int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 44 */     for (int j3 = 0; j3 < areaHeight; j3++)
/*    */     {
/* 46 */       System.arraycopy(aint1, (j3 + (areaY & 0x1)) * i1 + (areaX & 0x1), aint2, j3 * areaWidth, areaWidth);
/*    */     }
/*    */     
/* 49 */     return aint2;
/*    */   }
/*    */ 
/*    */   
/*    */   public static GenLayer magnify(long p_75915_0_, GenLayer p_75915_2_, int p_75915_3_) {
/* 54 */     GenLayer genlayer = p_75915_2_;
/*    */     
/* 56 */     for (int i = 0; i < p_75915_3_; i++)
/*    */     {
/* 58 */       genlayer = new GenLayerZoom(p_75915_0_ + i, genlayer);
/*    */     }
/*    */     
/* 61 */     return genlayer;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int selectRandom2(int p_selectRandom2_1_, int p_selectRandom2_2_) {
/* 66 */     int i = nextInt(2);
/* 67 */     return (i == 0) ? p_selectRandom2_1_ : p_selectRandom2_2_;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */