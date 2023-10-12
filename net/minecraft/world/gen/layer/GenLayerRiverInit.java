/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerRiverInit
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
/*  7 */     super(p_i2127_1_);
/*  8 */     this.parent = p_i2127_3_;
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 13 */     int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
/* 14 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 16 */     for (int i = 0; i < areaHeight; i++) {
/*    */       
/* 18 */       for (int j = 0; j < areaWidth; j++) {
/*    */         
/* 20 */         initChunkSeed((j + areaX), (i + areaY));
/* 21 */         aint1[j + i * areaWidth] = (aint[j + i * areaWidth] > 0) ? (nextInt(299999) + 2) : 0;
/*    */       } 
/*    */     } 
/*    */     
/* 25 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerRiverInit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */