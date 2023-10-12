/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ public class GenLayerEdge
/*     */   extends GenLayer
/*     */ {
/*     */   private final Mode field_151627_c;
/*     */   
/*     */   public GenLayerEdge(long p_i45474_1_, GenLayer p_i45474_3_, Mode p_i45474_4_) {
/*   9 */     super(p_i45474_1_);
/*  10 */     this.parent = p_i45474_3_;
/*  11 */     this.field_151627_c = p_i45474_4_;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
/*  16 */     switch (this.field_151627_c) {
/*     */ 
/*     */       
/*     */       default:
/*  20 */         return getIntsCoolWarm(areaX, areaY, areaWidth, areaHeight);
/*     */       
/*     */       case HEAT_ICE:
/*  23 */         return getIntsHeatIce(areaX, areaY, areaWidth, areaHeight);
/*     */       case SPECIAL:
/*     */         break;
/*  26 */     }  return getIntsSpecial(areaX, areaY, areaWidth, areaHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int[] getIntsCoolWarm(int p_151626_1_, int p_151626_2_, int p_151626_3_, int p_151626_4_) {
/*  32 */     int i = p_151626_1_ - 1;
/*  33 */     int j = p_151626_2_ - 1;
/*  34 */     int k = 1 + p_151626_3_ + 1;
/*  35 */     int l = 1 + p_151626_4_ + 1;
/*  36 */     int[] aint = this.parent.getInts(i, j, k, l);
/*  37 */     int[] aint1 = IntCache.getIntCache(p_151626_3_ * p_151626_4_);
/*     */     
/*  39 */     for (int i1 = 0; i1 < p_151626_4_; i1++) {
/*     */       
/*  41 */       for (int j1 = 0; j1 < p_151626_3_; j1++) {
/*     */         
/*  43 */         initChunkSeed((j1 + p_151626_1_), (i1 + p_151626_2_));
/*  44 */         int k1 = aint[j1 + 1 + (i1 + 1) * k];
/*     */         
/*  46 */         if (k1 == 1) {
/*     */           
/*  48 */           int l1 = aint[j1 + 1 + (i1 + 1 - 1) * k];
/*  49 */           int i2 = aint[j1 + 1 + 1 + (i1 + 1) * k];
/*  50 */           int j2 = aint[j1 + 1 - 1 + (i1 + 1) * k];
/*  51 */           int k2 = aint[j1 + 1 + (i1 + 1 + 1) * k];
/*  52 */           boolean flag = (l1 == 3 || i2 == 3 || j2 == 3 || k2 == 3);
/*  53 */           boolean flag1 = (l1 == 4 || i2 == 4 || j2 == 4 || k2 == 4);
/*     */           
/*  55 */           if (flag || flag1)
/*     */           {
/*  57 */             k1 = 2;
/*     */           }
/*     */         } 
/*     */         
/*  61 */         aint1[j1 + i1 * p_151626_3_] = k1;
/*     */       } 
/*     */     } 
/*     */     
/*  65 */     return aint1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] getIntsHeatIce(int p_151624_1_, int p_151624_2_, int p_151624_3_, int p_151624_4_) {
/*  70 */     int i = p_151624_1_ - 1;
/*  71 */     int j = p_151624_2_ - 1;
/*  72 */     int k = 1 + p_151624_3_ + 1;
/*  73 */     int l = 1 + p_151624_4_ + 1;
/*  74 */     int[] aint = this.parent.getInts(i, j, k, l);
/*  75 */     int[] aint1 = IntCache.getIntCache(p_151624_3_ * p_151624_4_);
/*     */     
/*  77 */     for (int i1 = 0; i1 < p_151624_4_; i1++) {
/*     */       
/*  79 */       for (int j1 = 0; j1 < p_151624_3_; j1++) {
/*     */         
/*  81 */         int k1 = aint[j1 + 1 + (i1 + 1) * k];
/*     */         
/*  83 */         if (k1 == 4) {
/*     */           
/*  85 */           int l1 = aint[j1 + 1 + (i1 + 1 - 1) * k];
/*  86 */           int i2 = aint[j1 + 1 + 1 + (i1 + 1) * k];
/*  87 */           int j2 = aint[j1 + 1 - 1 + (i1 + 1) * k];
/*  88 */           int k2 = aint[j1 + 1 + (i1 + 1 + 1) * k];
/*  89 */           boolean flag = (l1 == 2 || i2 == 2 || j2 == 2 || k2 == 2);
/*  90 */           boolean flag1 = (l1 == 1 || i2 == 1 || j2 == 1 || k2 == 1);
/*     */           
/*  92 */           if (flag1 || flag)
/*     */           {
/*  94 */             k1 = 3;
/*     */           }
/*     */         } 
/*     */         
/*  98 */         aint1[j1 + i1 * p_151624_3_] = k1;
/*     */       } 
/*     */     } 
/*     */     
/* 102 */     return aint1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] getIntsSpecial(int p_151625_1_, int p_151625_2_, int p_151625_3_, int p_151625_4_) {
/* 107 */     int[] aint = this.parent.getInts(p_151625_1_, p_151625_2_, p_151625_3_, p_151625_4_);
/* 108 */     int[] aint1 = IntCache.getIntCache(p_151625_3_ * p_151625_4_);
/*     */     
/* 110 */     for (int i = 0; i < p_151625_4_; i++) {
/*     */       
/* 112 */       for (int j = 0; j < p_151625_3_; j++) {
/*     */         
/* 114 */         initChunkSeed((j + p_151625_1_), (i + p_151625_2_));
/* 115 */         int k = aint[j + i * p_151625_3_];
/*     */         
/* 117 */         if (k != 0 && nextInt(13) == 0)
/*     */         {
/* 119 */           k |= 1 + nextInt(15) << 8 & 0xF00;
/*     */         }
/*     */         
/* 122 */         aint1[j + i * p_151625_3_] = k;
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     return aint1;
/*     */   }
/*     */   
/*     */   public enum Mode
/*     */   {
/* 131 */     COOL_WARM,
/* 132 */     HEAT_ICE,
/* 133 */     SPECIAL;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\layer\GenLayerEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */