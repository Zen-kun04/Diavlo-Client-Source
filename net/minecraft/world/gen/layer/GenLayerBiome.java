/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ 
/*     */ public class GenLayerBiome
/*     */   extends GenLayer {
/*   9 */   private BiomeGenBase[] field_151623_c = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna, BiomeGenBase.savanna, BiomeGenBase.plains };
/*  10 */   private BiomeGenBase[] field_151621_d = new BiomeGenBase[] { BiomeGenBase.forest, BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.plains, BiomeGenBase.birchForest, BiomeGenBase.swampland };
/*  11 */   private BiomeGenBase[] field_151622_e = new BiomeGenBase[] { BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.taiga, BiomeGenBase.plains };
/*  12 */   private BiomeGenBase[] field_151620_f = new BiomeGenBase[] { BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.coldTaiga };
/*     */   
/*     */   private final ChunkProviderSettings field_175973_g;
/*     */   
/*     */   public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, String p_i45560_5_) {
/*  17 */     super(p_i45560_1_);
/*  18 */     this.parent = p_i45560_3_;
/*     */     
/*  20 */     if (p_i45560_4_ == WorldType.DEFAULT_1_1) {
/*     */       
/*  22 */       this.field_151623_c = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga };
/*  23 */       this.field_175973_g = null;
/*     */     }
/*  25 */     else if (p_i45560_4_ == WorldType.CUSTOMIZED) {
/*     */       
/*  27 */       this.field_175973_g = ChunkProviderSettings.Factory.jsonToFactory(p_i45560_5_).func_177864_b();
/*     */     }
/*     */     else {
/*     */       
/*  31 */       this.field_175973_g = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  37 */     int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
/*  38 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  40 */     for (int i = 0; i < areaHeight; i++) {
/*     */       
/*  42 */       for (int j = 0; j < areaWidth; j++) {
/*     */         
/*  44 */         initChunkSeed((j + areaX), (i + areaY));
/*  45 */         int k = aint[j + i * areaWidth];
/*  46 */         int l = (k & 0xF00) >> 8;
/*  47 */         k &= 0xFFFFF0FF;
/*     */         
/*  49 */         if (this.field_175973_g != null && this.field_175973_g.fixedBiome >= 0) {
/*     */           
/*  51 */           aint1[j + i * areaWidth] = this.field_175973_g.fixedBiome;
/*     */         }
/*  53 */         else if (isBiomeOceanic(k)) {
/*     */           
/*  55 */           aint1[j + i * areaWidth] = k;
/*     */         }
/*  57 */         else if (k == BiomeGenBase.mushroomIsland.biomeID) {
/*     */           
/*  59 */           aint1[j + i * areaWidth] = k;
/*     */         }
/*  61 */         else if (k == 1) {
/*     */           
/*  63 */           if (l > 0) {
/*     */             
/*  65 */             if (nextInt(3) == 0)
/*     */             {
/*  67 */               aint1[j + i * areaWidth] = BiomeGenBase.mesaPlateau.biomeID;
/*     */             }
/*     */             else
/*     */             {
/*  71 */               aint1[j + i * areaWidth] = BiomeGenBase.mesaPlateau_F.biomeID;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/*  76 */             aint1[j + i * areaWidth] = (this.field_151623_c[nextInt(this.field_151623_c.length)]).biomeID;
/*     */           }
/*     */         
/*  79 */         } else if (k == 2) {
/*     */           
/*  81 */           if (l > 0)
/*     */           {
/*  83 */             aint1[j + i * areaWidth] = BiomeGenBase.jungle.biomeID;
/*     */           }
/*     */           else
/*     */           {
/*  87 */             aint1[j + i * areaWidth] = (this.field_151621_d[nextInt(this.field_151621_d.length)]).biomeID;
/*     */           }
/*     */         
/*  90 */         } else if (k == 3) {
/*     */           
/*  92 */           if (l > 0)
/*     */           {
/*  94 */             aint1[j + i * areaWidth] = BiomeGenBase.megaTaiga.biomeID;
/*     */           }
/*     */           else
/*     */           {
/*  98 */             aint1[j + i * areaWidth] = (this.field_151622_e[nextInt(this.field_151622_e.length)]).biomeID;
/*     */           }
/*     */         
/* 101 */         } else if (k == 4) {
/*     */           
/* 103 */           aint1[j + i * areaWidth] = (this.field_151620_f[nextInt(this.field_151620_f.length)]).biomeID;
/*     */         }
/*     */         else {
/*     */           
/* 107 */           aint1[j + i * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     return aint1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerBiome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */