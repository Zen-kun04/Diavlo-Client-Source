/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ public class NoiseGeneratorImproved
/*     */   extends NoiseGenerator {
/*     */   private int[] permutations;
/*     */   public double xCoord;
/*     */   public double yCoord;
/*     */   public double zCoord;
/*  11 */   private static final double[] field_152381_e = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
/*  12 */   private static final double[] field_152382_f = new double[] { 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D };
/*  13 */   private static final double[] field_152383_g = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
/*  14 */   private static final double[] field_152384_h = new double[] { 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, -1.0D, 0.0D };
/*  15 */   private static final double[] field_152385_i = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D, -1.0D, -1.0D, 1.0D, 1.0D, -1.0D, -1.0D, 0.0D, 1.0D, 0.0D, -1.0D };
/*     */ 
/*     */   
/*     */   public NoiseGeneratorImproved() {
/*  19 */     this(new Random());
/*     */   }
/*     */ 
/*     */   
/*     */   public NoiseGeneratorImproved(Random p_i45469_1_) {
/*  24 */     this.permutations = new int[512];
/*  25 */     this.xCoord = p_i45469_1_.nextDouble() * 256.0D;
/*  26 */     this.yCoord = p_i45469_1_.nextDouble() * 256.0D;
/*  27 */     this.zCoord = p_i45469_1_.nextDouble() * 256.0D;
/*     */     
/*  29 */     for (int i = 0; i < 256; this.permutations[i] = i++);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  34 */     for (int l = 0; l < 256; l++) {
/*     */       
/*  36 */       int j = p_i45469_1_.nextInt(256 - l) + l;
/*  37 */       int k = this.permutations[l];
/*  38 */       this.permutations[l] = this.permutations[j];
/*  39 */       this.permutations[j] = k;
/*  40 */       this.permutations[l + 256] = this.permutations[l];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final double lerp(double p_76311_1_, double p_76311_3_, double p_76311_5_) {
/*  46 */     return p_76311_3_ + p_76311_1_ * (p_76311_5_ - p_76311_3_);
/*     */   }
/*     */ 
/*     */   
/*     */   public final double func_76309_a(int p_76309_1_, double p_76309_2_, double p_76309_4_) {
/*  51 */     int i = p_76309_1_ & 0xF;
/*  52 */     return field_152384_h[i] * p_76309_2_ + field_152385_i[i] * p_76309_4_;
/*     */   }
/*     */ 
/*     */   
/*     */   public final double grad(int p_76310_1_, double p_76310_2_, double p_76310_4_, double p_76310_6_) {
/*  57 */     int i = p_76310_1_ & 0xF;
/*  58 */     return field_152381_e[i] * p_76310_2_ + field_152382_f[i] * p_76310_4_ + field_152383_g[i] * p_76310_6_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void populateNoiseArray(double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale, double noiseScale) {
/*  63 */     if (ySize == 1) {
/*     */       
/*  65 */       int i5 = 0;
/*  66 */       int j5 = 0;
/*  67 */       int j = 0;
/*  68 */       int k5 = 0;
/*  69 */       double d14 = 0.0D;
/*  70 */       double d15 = 0.0D;
/*  71 */       int l5 = 0;
/*  72 */       double d16 = 1.0D / noiseScale;
/*     */       
/*  74 */       for (int j2 = 0; j2 < xSize; j2++) {
/*     */         
/*  76 */         double d17 = xOffset + j2 * xScale + this.xCoord;
/*  77 */         int i6 = (int)d17;
/*     */         
/*  79 */         if (d17 < i6)
/*     */         {
/*  81 */           i6--;
/*     */         }
/*     */         
/*  84 */         int k2 = i6 & 0xFF;
/*  85 */         d17 -= i6;
/*  86 */         double d18 = d17 * d17 * d17 * (d17 * (d17 * 6.0D - 15.0D) + 10.0D);
/*     */         
/*  88 */         for (int j6 = 0; j6 < zSize; j6++)
/*     */         {
/*  90 */           double d19 = zOffset + j6 * zScale + this.zCoord;
/*  91 */           int k6 = (int)d19;
/*     */           
/*  93 */           if (d19 < k6)
/*     */           {
/*  95 */             k6--;
/*     */           }
/*     */           
/*  98 */           int l6 = k6 & 0xFF;
/*  99 */           d19 -= k6;
/* 100 */           double d20 = d19 * d19 * d19 * (d19 * (d19 * 6.0D - 15.0D) + 10.0D);
/* 101 */           i5 = this.permutations[k2] + 0;
/* 102 */           j5 = this.permutations[i5] + l6;
/* 103 */           j = this.permutations[k2 + 1] + 0;
/* 104 */           k5 = this.permutations[j] + l6;
/* 105 */           d14 = lerp(d18, func_76309_a(this.permutations[j5], d17, d19), grad(this.permutations[k5], d17 - 1.0D, 0.0D, d19));
/* 106 */           d15 = lerp(d18, grad(this.permutations[j5 + 1], d17, 0.0D, d19 - 1.0D), grad(this.permutations[k5 + 1], d17 - 1.0D, 0.0D, d19 - 1.0D));
/* 107 */           double d21 = lerp(d20, d14, d15);
/* 108 */           int i7 = l5++;
/* 109 */           noiseArray[i7] = noiseArray[i7] + d21 * d16;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 115 */       int i = 0;
/* 116 */       double d0 = 1.0D / noiseScale;
/* 117 */       int k = -1;
/* 118 */       int l = 0;
/* 119 */       int i1 = 0;
/* 120 */       int j1 = 0;
/* 121 */       int k1 = 0;
/* 122 */       int l1 = 0;
/* 123 */       int i2 = 0;
/* 124 */       double d1 = 0.0D;
/* 125 */       double d2 = 0.0D;
/* 126 */       double d3 = 0.0D;
/* 127 */       double d4 = 0.0D;
/*     */       
/* 129 */       for (int l2 = 0; l2 < xSize; l2++) {
/*     */         
/* 131 */         double d5 = xOffset + l2 * xScale + this.xCoord;
/* 132 */         int i3 = (int)d5;
/*     */         
/* 134 */         if (d5 < i3)
/*     */         {
/* 136 */           i3--;
/*     */         }
/*     */         
/* 139 */         int j3 = i3 & 0xFF;
/* 140 */         d5 -= i3;
/* 141 */         double d6 = d5 * d5 * d5 * (d5 * (d5 * 6.0D - 15.0D) + 10.0D);
/*     */         
/* 143 */         for (int k3 = 0; k3 < zSize; k3++) {
/*     */           
/* 145 */           double d7 = zOffset + k3 * zScale + this.zCoord;
/* 146 */           int l3 = (int)d7;
/*     */           
/* 148 */           if (d7 < l3)
/*     */           {
/* 150 */             l3--;
/*     */           }
/*     */           
/* 153 */           int i4 = l3 & 0xFF;
/* 154 */           d7 -= l3;
/* 155 */           double d8 = d7 * d7 * d7 * (d7 * (d7 * 6.0D - 15.0D) + 10.0D);
/*     */           
/* 157 */           for (int j4 = 0; j4 < ySize; j4++) {
/*     */             
/* 159 */             double d9 = yOffset + j4 * yScale + this.yCoord;
/* 160 */             int k4 = (int)d9;
/*     */             
/* 162 */             if (d9 < k4)
/*     */             {
/* 164 */               k4--;
/*     */             }
/*     */             
/* 167 */             int l4 = k4 & 0xFF;
/* 168 */             d9 -= k4;
/* 169 */             double d10 = d9 * d9 * d9 * (d9 * (d9 * 6.0D - 15.0D) + 10.0D);
/*     */             
/* 171 */             if (j4 == 0 || l4 != k) {
/*     */               
/* 173 */               k = l4;
/* 174 */               l = this.permutations[j3] + l4;
/* 175 */               i1 = this.permutations[l] + i4;
/* 176 */               j1 = this.permutations[l + 1] + i4;
/* 177 */               k1 = this.permutations[j3 + 1] + l4;
/* 178 */               l1 = this.permutations[k1] + i4;
/* 179 */               i2 = this.permutations[k1 + 1] + i4;
/* 180 */               d1 = lerp(d6, grad(this.permutations[i1], d5, d9, d7), grad(this.permutations[l1], d5 - 1.0D, d9, d7));
/* 181 */               d2 = lerp(d6, grad(this.permutations[j1], d5, d9 - 1.0D, d7), grad(this.permutations[i2], d5 - 1.0D, d9 - 1.0D, d7));
/* 182 */               d3 = lerp(d6, grad(this.permutations[i1 + 1], d5, d9, d7 - 1.0D), grad(this.permutations[l1 + 1], d5 - 1.0D, d9, d7 - 1.0D));
/* 183 */               d4 = lerp(d6, grad(this.permutations[j1 + 1], d5, d9 - 1.0D, d7 - 1.0D), grad(this.permutations[i2 + 1], d5 - 1.0D, d9 - 1.0D, d7 - 1.0D));
/*     */             } 
/*     */             
/* 186 */             double d11 = lerp(d10, d1, d2);
/* 187 */             double d12 = lerp(d10, d3, d4);
/* 188 */             double d13 = lerp(d8, d11, d12);
/* 189 */             int j7 = i++;
/* 190 */             noiseArray[j7] = noiseArray[j7] + d13 * d0;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\NoiseGeneratorImproved.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */