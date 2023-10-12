/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenJungle;
/*     */ 
/*     */ 
/*     */ public class GenLayerShore
/*     */   extends GenLayer
/*     */ {
/*     */   public GenLayerShore(long p_i2130_1_, GenLayer p_i2130_3_) {
/*  11 */     super(p_i2130_1_);
/*  12 */     this.parent = p_i2130_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  17 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  18 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  20 */     for (int i = 0; i < areaHeight; i++) {
/*     */       
/*  22 */       for (int j = 0; j < areaWidth; j++) {
/*     */         
/*  24 */         initChunkSeed((j + areaX), (i + areaY));
/*  25 */         int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
/*  26 */         BiomeGenBase biomegenbase = BiomeGenBase.getBiome(k);
/*     */         
/*  28 */         if (k == BiomeGenBase.mushroomIsland.biomeID) {
/*     */           
/*  30 */           int j2 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/*  31 */           int i3 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/*  32 */           int l3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/*  33 */           int k4 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/*     */           
/*  35 */           if (j2 != BiomeGenBase.ocean.biomeID && i3 != BiomeGenBase.ocean.biomeID && l3 != BiomeGenBase.ocean.biomeID && k4 != BiomeGenBase.ocean.biomeID)
/*     */           {
/*  37 */             aint1[j + i * areaWidth] = k;
/*     */           }
/*     */           else
/*     */           {
/*  41 */             aint1[j + i * areaWidth] = BiomeGenBase.mushroomIslandShore.biomeID;
/*     */           }
/*     */         
/*  44 */         } else if (biomegenbase != null && biomegenbase.getBiomeClass() == BiomeGenJungle.class) {
/*     */           
/*  46 */           int i2 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/*  47 */           int l2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/*  48 */           int k3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/*  49 */           int j4 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/*     */           
/*  51 */           if (func_151631_c(i2) && func_151631_c(l2) && func_151631_c(k3) && func_151631_c(j4)) {
/*     */             
/*  53 */             if (!isBiomeOceanic(i2) && !isBiomeOceanic(l2) && !isBiomeOceanic(k3) && !isBiomeOceanic(j4))
/*     */             {
/*  55 */               aint1[j + i * areaWidth] = k;
/*     */             }
/*     */             else
/*     */             {
/*  59 */               aint1[j + i * areaWidth] = BiomeGenBase.beach.biomeID;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  64 */             aint1[j + i * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
/*     */           }
/*     */         
/*  67 */         } else if (k != BiomeGenBase.extremeHills.biomeID && k != BiomeGenBase.extremeHillsPlus.biomeID && k != BiomeGenBase.extremeHillsEdge.biomeID) {
/*     */           
/*  69 */           if (biomegenbase != null && biomegenbase.isSnowyBiome()) {
/*     */             
/*  71 */             func_151632_a(aint, aint1, j, i, areaWidth, k, BiomeGenBase.coldBeach.biomeID);
/*     */           }
/*  73 */           else if (k != BiomeGenBase.mesa.biomeID && k != BiomeGenBase.mesaPlateau_F.biomeID) {
/*     */             
/*  75 */             if (k != BiomeGenBase.ocean.biomeID && k != BiomeGenBase.deepOcean.biomeID && k != BiomeGenBase.river.biomeID && k != BiomeGenBase.swampland.biomeID) {
/*     */               
/*  77 */               int l1 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/*  78 */               int k2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/*  79 */               int j3 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/*  80 */               int i4 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/*     */               
/*  82 */               if (!isBiomeOceanic(l1) && !isBiomeOceanic(k2) && !isBiomeOceanic(j3) && !isBiomeOceanic(i4))
/*     */               {
/*  84 */                 aint1[j + i * areaWidth] = k;
/*     */               }
/*     */               else
/*     */               {
/*  88 */                 aint1[j + i * areaWidth] = BiomeGenBase.beach.biomeID;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/*  93 */               aint1[j + i * areaWidth] = k;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  98 */             int l = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/*  99 */             int i1 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/* 100 */             int j1 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/* 101 */             int k1 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/*     */             
/* 103 */             if (!isBiomeOceanic(l) && !isBiomeOceanic(i1) && !isBiomeOceanic(j1) && !isBiomeOceanic(k1)) {
/*     */               
/* 105 */               if (func_151633_d(l) && func_151633_d(i1) && func_151633_d(j1) && func_151633_d(k1))
/*     */               {
/* 107 */                 aint1[j + i * areaWidth] = k;
/*     */               }
/*     */               else
/*     */               {
/* 111 */                 aint1[j + i * areaWidth] = BiomeGenBase.desert.biomeID;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 116 */               aint1[j + i * areaWidth] = k;
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/* 122 */           func_151632_a(aint, aint1, j, i, areaWidth, k, BiomeGenBase.stoneBeach.biomeID);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     return aint1;
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_) {
/* 132 */     if (isBiomeOceanic(p_151632_6_)) {
/*     */       
/* 134 */       p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
/*     */     }
/*     */     else {
/*     */       
/* 138 */       int i = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2)];
/* 139 */       int j = p_151632_1_[p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
/* 140 */       int k = p_151632_1_[p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2)];
/* 141 */       int l = p_151632_1_[p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2)];
/*     */       
/* 143 */       if (!isBiomeOceanic(i) && !isBiomeOceanic(j) && !isBiomeOceanic(k) && !isBiomeOceanic(l)) {
/*     */         
/* 145 */         p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_6_;
/*     */       }
/*     */       else {
/*     */         
/* 149 */         p_151632_2_[p_151632_3_ + p_151632_4_ * p_151632_5_] = p_151632_7_;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_151631_c(int p_151631_1_) {
/* 156 */     return (BiomeGenBase.getBiome(p_151631_1_) != null && BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == BiomeGenJungle.class) ? true : ((p_151631_1_ == BiomeGenBase.jungleEdge.biomeID || p_151631_1_ == BiomeGenBase.jungle.biomeID || p_151631_1_ == BiomeGenBase.jungleHills.biomeID || p_151631_1_ == BiomeGenBase.forest.biomeID || p_151631_1_ == BiomeGenBase.taiga.biomeID || isBiomeOceanic(p_151631_1_)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_151633_d(int p_151633_1_) {
/* 161 */     return BiomeGenBase.getBiome(p_151633_1_) instanceof net.minecraft.world.biome.BiomeGenMesa;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerShore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */