/*     */ package net.optifine.shaders;
/*     */ 
/*     */ import net.minecraft.client.renderer.culling.ClippingHelper;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ClippingHelperShadow
/*     */   extends ClippingHelper {
/*   8 */   private static ClippingHelperShadow instance = new ClippingHelperShadow();
/*   9 */   float[] frustumTest = new float[6];
/*  10 */   float[][] shadowClipPlanes = new float[10][4];
/*     */   int shadowClipPlaneCount;
/*  12 */   float[] matInvMP = new float[16];
/*  13 */   float[] vecIntersection = new float[4];
/*     */ 
/*     */   
/*     */   public boolean isBoxInFrustum(double x1, double y1, double z1, double x2, double y2, double z2) {
/*  17 */     for (int i = 0; i < this.shadowClipPlaneCount; i++) {
/*     */       
/*  19 */       float[] afloat = this.shadowClipPlanes[i];
/*     */       
/*  21 */       if (dot4(afloat, x1, y1, z1) <= 0.0D && dot4(afloat, x2, y1, z1) <= 0.0D && dot4(afloat, x1, y2, z1) <= 0.0D && dot4(afloat, x2, y2, z1) <= 0.0D && dot4(afloat, x1, y1, z2) <= 0.0D && dot4(afloat, x2, y1, z2) <= 0.0D && dot4(afloat, x1, y2, z2) <= 0.0D && dot4(afloat, x2, y2, z2) <= 0.0D)
/*     */       {
/*  23 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  27 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private double dot4(float[] plane, double x, double y, double z) {
/*  32 */     return plane[0] * x + plane[1] * y + plane[2] * z + plane[3];
/*     */   }
/*     */ 
/*     */   
/*     */   private double dot3(float[] vecA, float[] vecB) {
/*  37 */     return vecA[0] * vecB[0] + vecA[1] * vecB[1] + vecA[2] * vecB[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public static ClippingHelper getInstance() {
/*  42 */     instance.init();
/*  43 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   private void normalizePlane(float[] plane) {
/*  48 */     float f = MathHelper.sqrt_float(plane[0] * plane[0] + plane[1] * plane[1] + plane[2] * plane[2]);
/*  49 */     plane[0] = plane[0] / f;
/*  50 */     plane[1] = plane[1] / f;
/*  51 */     plane[2] = plane[2] / f;
/*  52 */     plane[3] = plane[3] / f;
/*     */   }
/*     */ 
/*     */   
/*     */   private void normalize3(float[] plane) {
/*  57 */     float f = MathHelper.sqrt_float(plane[0] * plane[0] + plane[1] * plane[1] + plane[2] * plane[2]);
/*     */     
/*  59 */     if (f == 0.0F)
/*     */     {
/*  61 */       f = 1.0F;
/*     */     }
/*     */     
/*  64 */     plane[0] = plane[0] / f;
/*  65 */     plane[1] = plane[1] / f;
/*  66 */     plane[2] = plane[2] / f;
/*     */   }
/*     */ 
/*     */   
/*     */   private void assignPlane(float[] plane, float a, float b, float c, float d) {
/*  71 */     float f = (float)Math.sqrt((a * a + b * b + c * c));
/*  72 */     plane[0] = a / f;
/*  73 */     plane[1] = b / f;
/*  74 */     plane[2] = c / f;
/*  75 */     plane[3] = d / f;
/*     */   }
/*     */ 
/*     */   
/*     */   private void copyPlane(float[] dst, float[] src) {
/*  80 */     dst[0] = src[0];
/*  81 */     dst[1] = src[1];
/*  82 */     dst[2] = src[2];
/*  83 */     dst[3] = src[3];
/*     */   }
/*     */ 
/*     */   
/*     */   private void cross3(float[] out, float[] a, float[] b) {
/*  88 */     out[0] = a[1] * b[2] - a[2] * b[1];
/*  89 */     out[1] = a[2] * b[0] - a[0] * b[2];
/*  90 */     out[2] = a[0] * b[1] - a[1] * b[0];
/*     */   }
/*     */ 
/*     */   
/*     */   private void addShadowClipPlane(float[] plane) {
/*  95 */     copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], plane);
/*     */   }
/*     */ 
/*     */   
/*     */   private float length(float x, float y, float z) {
/* 100 */     return (float)Math.sqrt((x * x + y * y + z * z));
/*     */   }
/*     */ 
/*     */   
/*     */   private float distance(float x1, float y1, float z1, float x2, float y2, float z2) {
/* 105 */     return length(x1 - x2, y1 - y2, z1 - z2);
/*     */   }
/*     */ 
/*     */   
/*     */   private void makeShadowPlane(float[] shadowPlane, float[] positivePlane, float[] negativePlane, float[] vecSun) {
/* 110 */     cross3(this.vecIntersection, positivePlane, negativePlane);
/* 111 */     cross3(shadowPlane, this.vecIntersection, vecSun);
/* 112 */     normalize3(shadowPlane);
/* 113 */     float f = (float)dot3(positivePlane, negativePlane);
/* 114 */     float f1 = (float)dot3(shadowPlane, negativePlane);
/* 115 */     float f2 = distance(shadowPlane[0], shadowPlane[1], shadowPlane[2], negativePlane[0] * f1, negativePlane[1] * f1, negativePlane[2] * f1);
/* 116 */     float f3 = distance(positivePlane[0], positivePlane[1], positivePlane[2], negativePlane[0] * f, negativePlane[1] * f, negativePlane[2] * f);
/* 117 */     float f4 = f2 / f3;
/* 118 */     float f5 = (float)dot3(shadowPlane, positivePlane);
/* 119 */     float f6 = distance(shadowPlane[0], shadowPlane[1], shadowPlane[2], positivePlane[0] * f5, positivePlane[1] * f5, positivePlane[2] * f5);
/* 120 */     float f7 = distance(negativePlane[0], negativePlane[1], negativePlane[2], positivePlane[0] * f, positivePlane[1] * f, positivePlane[2] * f);
/* 121 */     float f8 = f6 / f7;
/* 122 */     shadowPlane[3] = positivePlane[3] * f4 + negativePlane[3] * f8;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 127 */     float[] afloat = this.projectionMatrix;
/* 128 */     float[] afloat1 = this.modelviewMatrix;
/* 129 */     float[] afloat2 = this.clippingMatrix;
/* 130 */     System.arraycopy(Shaders.faProjection, 0, afloat, 0, 16);
/* 131 */     System.arraycopy(Shaders.faModelView, 0, afloat1, 0, 16);
/* 132 */     SMath.multiplyMat4xMat4(afloat2, afloat1, afloat);
/* 133 */     assignPlane(this.frustum[0], afloat2[3] - afloat2[0], afloat2[7] - afloat2[4], afloat2[11] - afloat2[8], afloat2[15] - afloat2[12]);
/* 134 */     assignPlane(this.frustum[1], afloat2[3] + afloat2[0], afloat2[7] + afloat2[4], afloat2[11] + afloat2[8], afloat2[15] + afloat2[12]);
/* 135 */     assignPlane(this.frustum[2], afloat2[3] + afloat2[1], afloat2[7] + afloat2[5], afloat2[11] + afloat2[9], afloat2[15] + afloat2[13]);
/* 136 */     assignPlane(this.frustum[3], afloat2[3] - afloat2[1], afloat2[7] - afloat2[5], afloat2[11] - afloat2[9], afloat2[15] - afloat2[13]);
/* 137 */     assignPlane(this.frustum[4], afloat2[3] - afloat2[2], afloat2[7] - afloat2[6], afloat2[11] - afloat2[10], afloat2[15] - afloat2[14]);
/* 138 */     assignPlane(this.frustum[5], afloat2[3] + afloat2[2], afloat2[7] + afloat2[6], afloat2[11] + afloat2[10], afloat2[15] + afloat2[14]);
/* 139 */     float[] afloat3 = Shaders.shadowLightPositionVector;
/* 140 */     float f = (float)dot3(this.frustum[0], afloat3);
/* 141 */     float f1 = (float)dot3(this.frustum[1], afloat3);
/* 142 */     float f2 = (float)dot3(this.frustum[2], afloat3);
/* 143 */     float f3 = (float)dot3(this.frustum[3], afloat3);
/* 144 */     float f4 = (float)dot3(this.frustum[4], afloat3);
/* 145 */     float f5 = (float)dot3(this.frustum[5], afloat3);
/* 146 */     this.shadowClipPlaneCount = 0;
/*     */     
/* 148 */     if (f >= 0.0F) {
/*     */       
/* 150 */       copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0]);
/*     */       
/* 152 */       if (f > 0.0F) {
/*     */         
/* 154 */         if (f2 < 0.0F)
/*     */         {
/* 156 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[2], afloat3);
/*     */         }
/*     */         
/* 159 */         if (f3 < 0.0F)
/*     */         {
/* 161 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[3], afloat3);
/*     */         }
/*     */         
/* 164 */         if (f4 < 0.0F)
/*     */         {
/* 166 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[4], afloat3);
/*     */         }
/*     */         
/* 169 */         if (f5 < 0.0F)
/*     */         {
/* 171 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[0], this.frustum[5], afloat3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 176 */     if (f1 >= 0.0F) {
/*     */       
/* 178 */       copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1]);
/*     */       
/* 180 */       if (f1 > 0.0F) {
/*     */         
/* 182 */         if (f2 < 0.0F)
/*     */         {
/* 184 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[2], afloat3);
/*     */         }
/*     */         
/* 187 */         if (f3 < 0.0F)
/*     */         {
/* 189 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[3], afloat3);
/*     */         }
/*     */         
/* 192 */         if (f4 < 0.0F)
/*     */         {
/* 194 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[4], afloat3);
/*     */         }
/*     */         
/* 197 */         if (f5 < 0.0F)
/*     */         {
/* 199 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[1], this.frustum[5], afloat3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 204 */     if (f2 >= 0.0F) {
/*     */       
/* 206 */       copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2]);
/*     */       
/* 208 */       if (f2 > 0.0F) {
/*     */         
/* 210 */         if (f < 0.0F)
/*     */         {
/* 212 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[0], afloat3);
/*     */         }
/*     */         
/* 215 */         if (f1 < 0.0F)
/*     */         {
/* 217 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[1], afloat3);
/*     */         }
/*     */         
/* 220 */         if (f4 < 0.0F)
/*     */         {
/* 222 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[4], afloat3);
/*     */         }
/*     */         
/* 225 */         if (f5 < 0.0F)
/*     */         {
/* 227 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[2], this.frustum[5], afloat3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     if (f3 >= 0.0F) {
/*     */       
/* 234 */       copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3]);
/*     */       
/* 236 */       if (f3 > 0.0F) {
/*     */         
/* 238 */         if (f < 0.0F)
/*     */         {
/* 240 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[0], afloat3);
/*     */         }
/*     */         
/* 243 */         if (f1 < 0.0F)
/*     */         {
/* 245 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[1], afloat3);
/*     */         }
/*     */         
/* 248 */         if (f4 < 0.0F)
/*     */         {
/* 250 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[4], afloat3);
/*     */         }
/*     */         
/* 253 */         if (f5 < 0.0F)
/*     */         {
/* 255 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[3], this.frustum[5], afloat3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     if (f4 >= 0.0F) {
/*     */       
/* 262 */       copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4]);
/*     */       
/* 264 */       if (f4 > 0.0F) {
/*     */         
/* 266 */         if (f < 0.0F)
/*     */         {
/* 268 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[0], afloat3);
/*     */         }
/*     */         
/* 271 */         if (f1 < 0.0F)
/*     */         {
/* 273 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[1], afloat3);
/*     */         }
/*     */         
/* 276 */         if (f2 < 0.0F)
/*     */         {
/* 278 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[2], afloat3);
/*     */         }
/*     */         
/* 281 */         if (f3 < 0.0F)
/*     */         {
/* 283 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[4], this.frustum[3], afloat3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 288 */     if (f5 >= 0.0F) {
/*     */       
/* 290 */       copyPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5]);
/*     */       
/* 292 */       if (f5 > 0.0F) {
/*     */         
/* 294 */         if (f < 0.0F)
/*     */         {
/* 296 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[0], afloat3);
/*     */         }
/*     */         
/* 299 */         if (f1 < 0.0F)
/*     */         {
/* 301 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[1], afloat3);
/*     */         }
/*     */         
/* 304 */         if (f2 < 0.0F)
/*     */         {
/* 306 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[2], afloat3);
/*     */         }
/*     */         
/* 309 */         if (f3 < 0.0F)
/*     */         {
/* 311 */           makeShadowPlane(this.shadowClipPlanes[this.shadowClipPlaneCount++], this.frustum[5], this.frustum[3], afloat3);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\ClippingHelperShadow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */