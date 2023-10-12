/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerAddSnow
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerAddSnow(long p_i2121_1_, GenLayer p_i2121_3_) {
/*  7 */     super(p_i2121_1_);
/*  8 */     this.parent = p_i2121_3_;
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
/* 24 */         int k1 = aint[j1 + 1 + (i1 + 1) * k];
/* 25 */         initChunkSeed((j1 + areaX), (i1 + areaY));
/*    */         
/* 27 */         if (k1 == 0) {
/*    */           
/* 29 */           aint1[j1 + i1 * areaWidth] = 0;
/*    */         }
/*    */         else {
/*    */           
/* 33 */           int l1 = nextInt(6);
/*    */           
/* 35 */           if (l1 == 0) {
/*    */             
/* 37 */             l1 = 4;
/*    */           }
/* 39 */           else if (l1 <= 1) {
/*    */             
/* 41 */             l1 = 3;
/*    */           }
/*    */           else {
/*    */             
/* 45 */             l1 = 1;
/*    */           } 
/*    */           
/* 48 */           aint1[j1 + i1 * areaWidth] = l1;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerAddSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */