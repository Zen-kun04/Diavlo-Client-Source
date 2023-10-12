/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class GenLayerBiomeEdge
/*     */   extends GenLayer
/*     */ {
/*     */   public GenLayerBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_) {
/*   9 */     super(p_i45475_1_);
/*  10 */     this.parent = p_i45475_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  15 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  16 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  18 */     for (int i = 0; i < areaHeight; i++) {
/*     */       
/*  20 */       for (int j = 0; j < areaWidth; j++) {
/*     */         
/*  22 */         initChunkSeed((j + areaX), (i + areaY));
/*  23 */         int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
/*     */         
/*  25 */         if (!replaceBiomeEdgeIfNecessary(aint, aint1, j, i, areaWidth, k, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) && !replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) && !replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) && !replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID))
/*     */         {
/*  27 */           if (k == BiomeGenBase.desert.biomeID) {
/*     */             
/*  29 */             int l1 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/*  30 */             int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/*  31 */             int j2 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/*  32 */             int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/*     */             
/*  34 */             if (l1 != BiomeGenBase.icePlains.biomeID && i2 != BiomeGenBase.icePlains.biomeID && j2 != BiomeGenBase.icePlains.biomeID && k2 != BiomeGenBase.icePlains.biomeID)
/*     */             {
/*  36 */               aint1[j + i * areaWidth] = k;
/*     */             }
/*     */             else
/*     */             {
/*  40 */               aint1[j + i * areaWidth] = BiomeGenBase.extremeHillsPlus.biomeID;
/*     */             }
/*     */           
/*  43 */           } else if (k == BiomeGenBase.swampland.biomeID) {
/*     */             
/*  45 */             int l = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/*  46 */             int i1 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/*  47 */             int j1 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/*  48 */             int k1 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/*     */             
/*  50 */             if (l != BiomeGenBase.desert.biomeID && i1 != BiomeGenBase.desert.biomeID && j1 != BiomeGenBase.desert.biomeID && k1 != BiomeGenBase.desert.biomeID && l != BiomeGenBase.coldTaiga.biomeID && i1 != BiomeGenBase.coldTaiga.biomeID && j1 != BiomeGenBase.coldTaiga.biomeID && k1 != BiomeGenBase.coldTaiga.biomeID && l != BiomeGenBase.icePlains.biomeID && i1 != BiomeGenBase.icePlains.biomeID && j1 != BiomeGenBase.icePlains.biomeID && k1 != BiomeGenBase.icePlains.biomeID) {
/*     */               
/*  52 */               if (l != BiomeGenBase.jungle.biomeID && k1 != BiomeGenBase.jungle.biomeID && i1 != BiomeGenBase.jungle.biomeID && j1 != BiomeGenBase.jungle.biomeID)
/*     */               {
/*  54 */                 aint1[j + i * areaWidth] = k;
/*     */               }
/*     */               else
/*     */               {
/*  58 */                 aint1[j + i * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/*  63 */               aint1[j + i * areaWidth] = BiomeGenBase.plains.biomeID;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  68 */             aint1[j + i * areaWidth] = k;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     return aint1;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean replaceBiomeEdgeIfNecessary(int[] p_151636_1_, int[] p_151636_2_, int p_151636_3_, int p_151636_4_, int p_151636_5_, int p_151636_6_, int p_151636_7_, int p_151636_8_) {
/*  79 */     if (!biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_))
/*     */     {
/*  81 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  85 */     int i = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
/*  86 */     int j = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
/*  87 */     int k = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
/*  88 */     int l = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];
/*     */     
/*  90 */     if (canBiomesBeNeighbors(i, p_151636_7_) && canBiomesBeNeighbors(j, p_151636_7_) && canBiomesBeNeighbors(k, p_151636_7_) && canBiomesBeNeighbors(l, p_151636_7_)) {
/*     */       
/*  92 */       p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_6_;
/*     */     }
/*     */     else {
/*     */       
/*  96 */       p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_8_;
/*     */     } 
/*     */     
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_) {
/* 105 */     if (p_151635_6_ != p_151635_7_)
/*     */     {
/* 107 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 111 */     int i = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
/* 112 */     int j = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
/* 113 */     int k = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
/* 114 */     int l = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];
/*     */     
/* 116 */     if (biomesEqualOrMesaPlateau(i, p_151635_7_) && biomesEqualOrMesaPlateau(j, p_151635_7_) && biomesEqualOrMesaPlateau(k, p_151635_7_) && biomesEqualOrMesaPlateau(l, p_151635_7_)) {
/*     */       
/* 118 */       p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
/*     */     }
/*     */     else {
/*     */       
/* 122 */       p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
/*     */     } 
/*     */     
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_) {
/* 131 */     if (biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_))
/*     */     {
/* 133 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 137 */     BiomeGenBase biomegenbase = BiomeGenBase.getBiome(p_151634_1_);
/* 138 */     BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(p_151634_2_);
/*     */     
/* 140 */     if (biomegenbase != null && biomegenbase1 != null) {
/*     */       
/* 142 */       BiomeGenBase.TempCategory biomegenbase$tempcategory = biomegenbase.getTempCategory();
/* 143 */       BiomeGenBase.TempCategory biomegenbase$tempcategory1 = biomegenbase1.getTempCategory();
/* 144 */       return (biomegenbase$tempcategory == biomegenbase$tempcategory1 || biomegenbase$tempcategory == BiomeGenBase.TempCategory.MEDIUM || biomegenbase$tempcategory1 == BiomeGenBase.TempCategory.MEDIUM);
/*     */     } 
/*     */ 
/*     */     
/* 148 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerBiomeEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */