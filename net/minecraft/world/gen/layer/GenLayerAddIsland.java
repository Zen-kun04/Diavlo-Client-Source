/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerAddIsland
/*    */   extends GenLayer
/*    */ {
/*    */   public GenLayerAddIsland(long p_i2119_1_, GenLayer p_i2119_3_) {
/*  7 */     super(p_i2119_1_);
/*  8 */     this.parent = p_i2119_3_;
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
/* 24 */         int k1 = aint[j1 + 0 + (i1 + 0) * k];
/* 25 */         int l1 = aint[j1 + 2 + (i1 + 0) * k];
/* 26 */         int i2 = aint[j1 + 0 + (i1 + 2) * k];
/* 27 */         int j2 = aint[j1 + 2 + (i1 + 2) * k];
/* 28 */         int k2 = aint[j1 + 1 + (i1 + 1) * k];
/* 29 */         initChunkSeed((j1 + areaX), (i1 + areaY));
/*    */         
/* 31 */         if (k2 != 0 || (k1 == 0 && l1 == 0 && i2 == 0 && j2 == 0)) {
/*    */           
/* 33 */           if (k2 > 0 && (k1 == 0 || l1 == 0 || i2 == 0 || j2 == 0)) {
/*    */             
/* 35 */             if (nextInt(5) == 0) {
/*    */               
/* 37 */               if (k2 == 4)
/*    */               {
/* 39 */                 aint1[j1 + i1 * areaWidth] = 4;
/*    */               }
/*    */               else
/*    */               {
/* 43 */                 aint1[j1 + i1 * areaWidth] = 0;
/*    */               }
/*    */             
/*    */             } else {
/*    */               
/* 48 */               aint1[j1 + i1 * areaWidth] = k2;
/*    */             }
/*    */           
/*    */           } else {
/*    */             
/* 53 */             aint1[j1 + i1 * areaWidth] = k2;
/*    */           }
/*    */         
/*    */         } else {
/*    */           
/* 58 */           int l2 = 1;
/* 59 */           int i3 = 1;
/*    */           
/* 61 */           if (k1 != 0 && nextInt(l2++) == 0)
/*    */           {
/* 63 */             i3 = k1;
/*    */           }
/*    */           
/* 66 */           if (l1 != 0 && nextInt(l2++) == 0)
/*    */           {
/* 68 */             i3 = l1;
/*    */           }
/*    */           
/* 71 */           if (i2 != 0 && nextInt(l2++) == 0)
/*    */           {
/* 73 */             i3 = i2;
/*    */           }
/*    */           
/* 76 */           if (j2 != 0 && nextInt(l2++) == 0)
/*    */           {
/* 78 */             i3 = j2;
/*    */           }
/*    */           
/* 81 */           if (nextInt(3) == 0) {
/*    */             
/* 83 */             aint1[j1 + i1 * areaWidth] = i3;
/*    */           }
/* 85 */           else if (i3 == 4) {
/*    */             
/* 87 */             aint1[j1 + i1 * areaWidth] = 4;
/*    */           }
/*    */           else {
/*    */             
/* 91 */             aint1[j1 + i1 * areaWidth] = 0;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 97 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerAddIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */