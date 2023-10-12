/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerIsland
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerIsland(long p_i2124_1_) {
/*  7 */     super(p_i2124_1_);
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/* 12 */     int[] aint = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 14 */     for (int i = 0; i < areaHeight; i++) {
/*    */       
/* 16 */       for (int j = 0; j < areaWidth; j++) {
/*    */         
/* 18 */         initChunkSeed((areaX + j), (areaY + i));
/* 19 */         aint[j + i * areaWidth] = (nextInt(10) == 0) ? 1 : 0;
/*    */       } 
/*    */     } 
/*    */     
/* 23 */     if (areaX > -areaWidth && areaX <= 0 && areaY > -areaHeight && areaY <= 0)
/*    */     {
/* 25 */       aint[-areaX + -areaY * areaWidth] = 1;
/*    */     }
/*    */     
/* 28 */     return aint;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */