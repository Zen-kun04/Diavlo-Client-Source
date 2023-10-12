/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GenLayerHills
/*     */   extends GenLayer {
/*   9 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private GenLayer field_151628_d;
/*     */   
/*     */   public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_) {
/*  14 */     super(p_i45479_1_);
/*  15 */     this.parent = p_i45479_3_;
/*  16 */     this.field_151628_d = p_i45479_4_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  21 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  22 */     int[] aint1 = this.field_151628_d.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  23 */     int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  25 */     for (int i = 0; i < areaHeight; i++) {
/*     */       
/*  27 */       for (int j = 0; j < areaWidth; j++) {
/*     */         
/*  29 */         initChunkSeed((j + areaX), (i + areaY));
/*  30 */         int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
/*  31 */         int l = aint1[j + 1 + (i + 1) * (areaWidth + 2)];
/*  32 */         boolean flag = ((l - 2) % 29 == 0);
/*     */         
/*  34 */         if (k > 255)
/*     */         {
/*  36 */           logger.debug("old! " + k);
/*     */         }
/*     */         
/*  39 */         if (k != 0 && l >= 2 && (l - 2) % 29 == 1 && k < 128) {
/*     */           
/*  41 */           if (BiomeGenBase.getBiome(k + 128) != null)
/*     */           {
/*  43 */             aint2[j + i * areaWidth] = k + 128;
/*     */           }
/*     */           else
/*     */           {
/*  47 */             aint2[j + i * areaWidth] = k;
/*     */           }
/*     */         
/*  50 */         } else if (nextInt(3) != 0 && !flag) {
/*     */           
/*  52 */           aint2[j + i * areaWidth] = k;
/*     */         }
/*     */         else {
/*     */           
/*  56 */           int i1 = k;
/*     */           
/*  58 */           if (k == BiomeGenBase.desert.biomeID) {
/*     */             
/*  60 */             i1 = BiomeGenBase.desertHills.biomeID;
/*     */           }
/*  62 */           else if (k == BiomeGenBase.forest.biomeID) {
/*     */             
/*  64 */             i1 = BiomeGenBase.forestHills.biomeID;
/*     */           }
/*  66 */           else if (k == BiomeGenBase.birchForest.biomeID) {
/*     */             
/*  68 */             i1 = BiomeGenBase.birchForestHills.biomeID;
/*     */           }
/*  70 */           else if (k == BiomeGenBase.roofedForest.biomeID) {
/*     */             
/*  72 */             i1 = BiomeGenBase.plains.biomeID;
/*     */           }
/*  74 */           else if (k == BiomeGenBase.taiga.biomeID) {
/*     */             
/*  76 */             i1 = BiomeGenBase.taigaHills.biomeID;
/*     */           }
/*  78 */           else if (k == BiomeGenBase.megaTaiga.biomeID) {
/*     */             
/*  80 */             i1 = BiomeGenBase.megaTaigaHills.biomeID;
/*     */           }
/*  82 */           else if (k == BiomeGenBase.coldTaiga.biomeID) {
/*     */             
/*  84 */             i1 = BiomeGenBase.coldTaigaHills.biomeID;
/*     */           }
/*  86 */           else if (k == BiomeGenBase.plains.biomeID) {
/*     */             
/*  88 */             if (nextInt(3) == 0)
/*     */             {
/*  90 */               i1 = BiomeGenBase.forestHills.biomeID;
/*     */             }
/*     */             else
/*     */             {
/*  94 */               i1 = BiomeGenBase.forest.biomeID;
/*     */             }
/*     */           
/*  97 */           } else if (k == BiomeGenBase.icePlains.biomeID) {
/*     */             
/*  99 */             i1 = BiomeGenBase.iceMountains.biomeID;
/*     */           }
/* 101 */           else if (k == BiomeGenBase.jungle.biomeID) {
/*     */             
/* 103 */             i1 = BiomeGenBase.jungleHills.biomeID;
/*     */           }
/* 105 */           else if (k == BiomeGenBase.ocean.biomeID) {
/*     */             
/* 107 */             i1 = BiomeGenBase.deepOcean.biomeID;
/*     */           }
/* 109 */           else if (k == BiomeGenBase.extremeHills.biomeID) {
/*     */             
/* 111 */             i1 = BiomeGenBase.extremeHillsPlus.biomeID;
/*     */           }
/* 113 */           else if (k == BiomeGenBase.savanna.biomeID) {
/*     */             
/* 115 */             i1 = BiomeGenBase.savannaPlateau.biomeID;
/*     */           }
/* 117 */           else if (biomesEqualOrMesaPlateau(k, BiomeGenBase.mesaPlateau_F.biomeID)) {
/*     */             
/* 119 */             i1 = BiomeGenBase.mesa.biomeID;
/*     */           }
/* 121 */           else if (k == BiomeGenBase.deepOcean.biomeID && nextInt(3) == 0) {
/*     */             
/* 123 */             int j1 = nextInt(2);
/*     */             
/* 125 */             if (j1 == 0) {
/*     */               
/* 127 */               i1 = BiomeGenBase.plains.biomeID;
/*     */             }
/*     */             else {
/*     */               
/* 131 */               i1 = BiomeGenBase.forest.biomeID;
/*     */             } 
/*     */           } 
/*     */           
/* 135 */           if (flag && i1 != k)
/*     */           {
/* 137 */             if (BiomeGenBase.getBiome(i1 + 128) != null) {
/*     */               
/* 139 */               i1 += 128;
/*     */             }
/*     */             else {
/*     */               
/* 143 */               i1 = k;
/*     */             } 
/*     */           }
/*     */           
/* 147 */           if (i1 == k) {
/*     */             
/* 149 */             aint2[j + i * areaWidth] = k;
/*     */           }
/*     */           else {
/*     */             
/* 153 */             int k2 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
/* 154 */             int k1 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
/* 155 */             int l1 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
/* 156 */             int i2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];
/* 157 */             int j2 = 0;
/*     */             
/* 159 */             if (biomesEqualOrMesaPlateau(k2, k))
/*     */             {
/* 161 */               j2++;
/*     */             }
/*     */             
/* 164 */             if (biomesEqualOrMesaPlateau(k1, k))
/*     */             {
/* 166 */               j2++;
/*     */             }
/*     */             
/* 169 */             if (biomesEqualOrMesaPlateau(l1, k))
/*     */             {
/* 171 */               j2++;
/*     */             }
/*     */             
/* 174 */             if (biomesEqualOrMesaPlateau(i2, k))
/*     */             {
/* 176 */               j2++;
/*     */             }
/*     */             
/* 179 */             if (j2 >= 3) {
/*     */               
/* 181 */               aint2[j + i * areaWidth] = i1;
/*     */             }
/*     */             else {
/*     */               
/* 185 */               aint2[j + i * areaWidth] = k;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 192 */     return aint2;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerHills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */