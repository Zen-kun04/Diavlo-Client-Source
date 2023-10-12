/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.data.AnimationFrame;
/*     */ import net.minecraft.client.resources.data.AnimationMetadataSection;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.SmartAnimations;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import net.optifine.util.CounterInt;
/*     */ import net.optifine.util.TextureUtils;
/*     */ 
/*     */ public class TextureAtlasSprite
/*     */ {
/*     */   private final String iconName;
/*  26 */   protected List<int[][]> framesTextureData = Lists.newArrayList();
/*     */   protected int[][] interpolatedFrameData;
/*     */   private AnimationMetadataSection animationMetadata;
/*     */   protected boolean rotated;
/*     */   protected int originX;
/*     */   protected int originY;
/*     */   protected int width;
/*     */   protected int height;
/*     */   private float minU;
/*     */   private float maxU;
/*     */   private float minV;
/*     */   private float maxV;
/*     */   protected int frameCounter;
/*     */   protected int tickCounter;
/*  40 */   private static String locationNameClock = "builtin/clock";
/*  41 */   private static String locationNameCompass = "builtin/compass";
/*  42 */   private int indexInMap = -1;
/*     */   public float baseU;
/*     */   public float baseV;
/*     */   public int sheetWidth;
/*     */   public int sheetHeight;
/*  47 */   public int glSpriteTextureId = -1;
/*  48 */   public TextureAtlasSprite spriteSingle = null;
/*     */   public boolean isSpriteSingle = false;
/*  50 */   public int mipmapLevels = 0;
/*  51 */   public TextureAtlasSprite spriteNormal = null;
/*  52 */   public TextureAtlasSprite spriteSpecular = null;
/*     */   public boolean isShadersSprite = false;
/*     */   public boolean isEmissive = false;
/*  55 */   public TextureAtlasSprite spriteEmissive = null;
/*  56 */   private int animationIndex = -1;
/*     */   
/*     */   private boolean animationActive = false;
/*     */   
/*     */   private TextureAtlasSprite(String p_i7_1_, boolean p_i7_2_) {
/*  61 */     this.iconName = p_i7_1_;
/*  62 */     this.isSpriteSingle = p_i7_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite(String spriteName) {
/*  67 */     this.iconName = spriteName;
/*     */     
/*  69 */     if (Config.isMultiTexture())
/*     */     {
/*  71 */       this.spriteSingle = new TextureAtlasSprite(getIconName() + ".spriteSingle", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
/*  77 */     String s = spriteResourceLocation.toString();
/*  78 */     return locationNameClock.equals(s) ? new TextureClock(s) : (locationNameCompass.equals(s) ? new TextureCompass(s) : new TextureAtlasSprite(s));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setLocationNameClock(String clockName) {
/*  83 */     locationNameClock = clockName;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setLocationNameCompass(String compassName) {
/*  88 */     locationNameCompass = compassName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
/*  93 */     this.originX = originInX;
/*  94 */     this.originY = originInY;
/*  95 */     this.rotated = rotatedIn;
/*  96 */     float f = (float)(0.009999999776482582D / inX);
/*  97 */     float f1 = (float)(0.009999999776482582D / inY);
/*  98 */     this.minU = originInX / (float)inX + f;
/*  99 */     this.maxU = (originInX + this.width) / (float)inX - f;
/* 100 */     this.minV = originInY / inY + f1;
/* 101 */     this.maxV = (originInY + this.height) / inY - f1;
/* 102 */     this.baseU = Math.min(this.minU, this.maxU);
/* 103 */     this.baseV = Math.min(this.minV, this.maxV);
/*     */     
/* 105 */     if (this.spriteSingle != null)
/*     */     {
/* 107 */       this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
/*     */     }
/*     */     
/* 110 */     if (this.spriteNormal != null)
/*     */     {
/* 112 */       this.spriteNormal.copyFrom(this);
/*     */     }
/*     */     
/* 115 */     if (this.spriteSpecular != null)
/*     */     {
/* 117 */       this.spriteSpecular.copyFrom(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyFrom(TextureAtlasSprite atlasSpirit) {
/* 123 */     this.originX = atlasSpirit.originX;
/* 124 */     this.originY = atlasSpirit.originY;
/* 125 */     this.width = atlasSpirit.width;
/* 126 */     this.height = atlasSpirit.height;
/* 127 */     this.rotated = atlasSpirit.rotated;
/* 128 */     this.minU = atlasSpirit.minU;
/* 129 */     this.maxU = atlasSpirit.maxU;
/* 130 */     this.minV = atlasSpirit.minV;
/* 131 */     this.maxV = atlasSpirit.maxV;
/*     */     
/* 133 */     if (atlasSpirit != Config.getTextureMap().getMissingSprite())
/*     */     {
/* 135 */       this.indexInMap = atlasSpirit.indexInMap;
/*     */     }
/*     */     
/* 138 */     this.baseU = atlasSpirit.baseU;
/* 139 */     this.baseV = atlasSpirit.baseV;
/* 140 */     this.sheetWidth = atlasSpirit.sheetWidth;
/* 141 */     this.sheetHeight = atlasSpirit.sheetHeight;
/* 142 */     this.glSpriteTextureId = atlasSpirit.glSpriteTextureId;
/* 143 */     this.mipmapLevels = atlasSpirit.mipmapLevels;
/*     */     
/* 145 */     if (this.spriteSingle != null)
/*     */     {
/* 147 */       this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
/*     */     }
/*     */     
/* 150 */     this.animationIndex = atlasSpirit.animationIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOriginX() {
/* 155 */     return this.originX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOriginY() {
/* 160 */     return this.originY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/* 165 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/* 170 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMinU() {
/* 175 */     return this.minU;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMaxU() {
/* 180 */     return this.maxU;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getInterpolatedU(double u) {
/* 185 */     float f = this.maxU - this.minU;
/* 186 */     return this.minU + f * (float)u / 16.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMinV() {
/* 191 */     return this.minV;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getMaxV() {
/* 196 */     return this.maxV;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getInterpolatedV(double v) {
/* 201 */     float f = this.maxV - this.minV;
/* 202 */     return this.minV + f * (float)v / 16.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIconName() {
/* 207 */     return this.iconName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAnimation() {
/* 212 */     if (this.animationMetadata != null) {
/*     */       
/* 214 */       this.animationActive = SmartAnimations.isActive() ? SmartAnimations.isSpriteRendered(this.animationIndex) : true;
/* 215 */       this.tickCounter++;
/*     */       
/* 217 */       if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
/*     */         
/* 219 */         int i = this.animationMetadata.getFrameIndex(this.frameCounter);
/* 220 */         int j = (this.animationMetadata.getFrameCount() == 0) ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
/* 221 */         this.frameCounter = (this.frameCounter + 1) % j;
/* 222 */         this.tickCounter = 0;
/* 223 */         int k = this.animationMetadata.getFrameIndex(this.frameCounter);
/* 224 */         boolean flag = false;
/* 225 */         boolean flag1 = this.isSpriteSingle;
/*     */         
/* 227 */         if (!this.animationActive) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 232 */         if (i != k && k >= 0 && k < this.framesTextureData.size())
/*     */         {
/* 234 */           TextureUtil.uploadTextureMipmap(this.framesTextureData.get(k), this.width, this.height, this.originX, this.originY, flag, flag1);
/*     */         }
/*     */       }
/* 237 */       else if (this.animationMetadata.isInterpolate()) {
/*     */         
/* 239 */         if (!this.animationActive) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 244 */         updateAnimationInterpolated();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateAnimationInterpolated() {
/* 251 */     double d0 = 1.0D - this.tickCounter / this.animationMetadata.getFrameTimeSingle(this.frameCounter);
/* 252 */     int i = this.animationMetadata.getFrameIndex(this.frameCounter);
/* 253 */     int j = (this.animationMetadata.getFrameCount() == 0) ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
/* 254 */     int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
/*     */     
/* 256 */     if (i != k && k >= 0 && k < this.framesTextureData.size()) {
/*     */       
/* 258 */       int[][] aint = this.framesTextureData.get(i);
/* 259 */       int[][] aint1 = this.framesTextureData.get(k);
/*     */       
/* 261 */       if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length)
/*     */       {
/* 263 */         this.interpolatedFrameData = new int[aint.length][];
/*     */       }
/*     */       
/* 266 */       for (int l = 0; l < aint.length; l++) {
/*     */         
/* 268 */         if (this.interpolatedFrameData[l] == null)
/*     */         {
/* 270 */           this.interpolatedFrameData[l] = new int[(aint[l]).length];
/*     */         }
/*     */         
/* 273 */         if (l < aint1.length && (aint1[l]).length == (aint[l]).length)
/*     */         {
/* 275 */           for (int i1 = 0; i1 < (aint[l]).length; i1++) {
/*     */             
/* 277 */             int j1 = aint[l][i1];
/* 278 */             int k1 = aint1[l][i1];
/* 279 */             int l1 = (int)(((j1 & 0xFF0000) >> 16) * d0 + ((k1 & 0xFF0000) >> 16) * (1.0D - d0));
/* 280 */             int i2 = (int)(((j1 & 0xFF00) >> 8) * d0 + ((k1 & 0xFF00) >> 8) * (1.0D - d0));
/* 281 */             int j2 = (int)((j1 & 0xFF) * d0 + (k1 & 0xFF) * (1.0D - d0));
/* 282 */             this.interpolatedFrameData[l][i1] = j1 & 0xFF000000 | l1 << 16 | i2 << 8 | j2;
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 287 */       TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int[][] getFrameTextureData(int index) {
/* 293 */     return this.framesTextureData.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFrameCount() {
/* 298 */     return this.framesTextureData.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIconWidth(int newWidth) {
/* 303 */     this.width = newWidth;
/*     */     
/* 305 */     if (this.spriteSingle != null)
/*     */     {
/* 307 */       this.spriteSingle.setIconWidth(this.width);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIconHeight(int newHeight) {
/* 313 */     this.height = newHeight;
/*     */     
/* 315 */     if (this.spriteSingle != null)
/*     */     {
/* 317 */       this.spriteSingle.setIconHeight(this.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadSprite(BufferedImage[] images, AnimationMetadataSection meta) throws IOException {
/* 323 */     resetSprite();
/* 324 */     int i = images[0].getWidth();
/* 325 */     int j = images[0].getHeight();
/* 326 */     this.width = i;
/* 327 */     this.height = j;
/*     */     
/* 329 */     if (this.spriteSingle != null) {
/*     */       
/* 331 */       this.spriteSingle.width = this.width;
/* 332 */       this.spriteSingle.height = this.height;
/*     */     } 
/*     */     
/* 335 */     int[][] aint = new int[images.length][];
/*     */     
/* 337 */     for (int k = 0; k < images.length; k++) {
/*     */       
/* 339 */       BufferedImage bufferedimage = images[k];
/*     */       
/* 341 */       if (bufferedimage != null) {
/*     */         
/* 343 */         if (this.width >> k != bufferedimage.getWidth())
/*     */         {
/* 345 */           bufferedimage = TextureUtils.scaleImage(bufferedimage, this.width >> k);
/*     */         }
/*     */         
/* 348 */         if (k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k))
/*     */         {
/* 350 */           throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] { Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()), Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k), Integer.valueOf(j >> k) }));
/*     */         }
/*     */         
/* 353 */         aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
/* 354 */         bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
/*     */       } 
/*     */     } 
/*     */     
/* 358 */     if (meta == null) {
/*     */       
/* 360 */       if (j != i)
/*     */       {
/* 362 */         throw new RuntimeException("broken aspect ratio and not an animation");
/*     */       }
/*     */       
/* 365 */       this.framesTextureData.add(aint);
/*     */     }
/*     */     else {
/*     */       
/* 369 */       int j1 = j / i;
/* 370 */       int l1 = i;
/* 371 */       int l = i;
/* 372 */       this.height = this.width;
/*     */       
/* 374 */       if (meta.getFrameCount() > 0) {
/*     */         
/* 376 */         Iterator<Integer> iterator = meta.getFrameIndexSet().iterator();
/*     */         
/* 378 */         while (iterator.hasNext()) {
/*     */           
/* 380 */           int i1 = ((Integer)iterator.next()).intValue();
/*     */           
/* 382 */           if (i1 >= j1)
/*     */           {
/* 384 */             throw new RuntimeException("invalid frameindex " + i1);
/*     */           }
/*     */           
/* 387 */           allocateFrameTextureData(i1);
/* 388 */           this.framesTextureData.set(i1, getFrameTextureData(aint, l1, l, i1));
/*     */         } 
/*     */         
/* 391 */         this.animationMetadata = meta;
/*     */       }
/*     */       else {
/*     */         
/* 395 */         List<AnimationFrame> list = Lists.newArrayList();
/*     */         
/* 397 */         for (int j2 = 0; j2 < j1; j2++) {
/*     */           
/* 399 */           this.framesTextureData.add(getFrameTextureData(aint, l1, l, j2));
/* 400 */           list.add(new AnimationFrame(j2, -1));
/*     */         } 
/*     */         
/* 403 */         this.animationMetadata = new AnimationMetadataSection(list, this.width, this.height, meta.getFrameTime(), meta.isInterpolate());
/*     */       } 
/*     */     } 
/*     */     
/* 407 */     if (!this.isShadersSprite) {
/*     */       
/* 409 */       if (Config.isShaders())
/*     */       {
/* 411 */         loadShadersSprites();
/*     */       }
/*     */       
/* 414 */       for (int k1 = 0; k1 < this.framesTextureData.size(); k1++) {
/*     */         
/* 416 */         int[][] aint1 = this.framesTextureData.get(k1);
/*     */         
/* 418 */         if (aint1 != null && !this.iconName.startsWith("minecraft:blocks/leaves_"))
/*     */         {
/* 420 */           for (int i2 = 0; i2 < aint1.length; i2++) {
/*     */             
/* 422 */             int[] aint2 = aint1[i2];
/* 423 */             fixTransparentColor(aint2);
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 428 */       if (this.spriteSingle != null)
/*     */       {
/* 430 */         this.spriteSingle.loadSprite(images, meta);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateMipmaps(int level) {
/* 437 */     List<int[][]> list = Lists.newArrayList();
/*     */     
/* 439 */     for (int i = 0; i < this.framesTextureData.size(); i++) {
/*     */       
/* 441 */       final int[][] aint = this.framesTextureData.get(i);
/*     */       
/* 443 */       if (aint != null) {
/*     */         
/*     */         try {
/*     */           
/* 447 */           list.add(TextureUtil.generateMipmapData(level, this.width, aint));
/*     */         }
/* 449 */         catch (Throwable throwable) {
/*     */           
/* 451 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
/* 452 */           CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
/* 453 */           crashreportcategory.addCrashSection("Frame index", Integer.valueOf(i));
/* 454 */           crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable<String>()
/*     */               {
/*     */                 public String call() throws Exception
/*     */                 {
/* 458 */                   StringBuilder stringbuilder = new StringBuilder();
/*     */                   
/* 460 */                   for (int[] aint1 : aint) {
/*     */                     
/* 462 */                     if (stringbuilder.length() > 0)
/*     */                     {
/* 464 */                       stringbuilder.append(", ");
/*     */                     }
/*     */                     
/* 467 */                     stringbuilder.append((aint1 == null) ? "null" : Integer.valueOf(aint1.length));
/*     */                   } 
/*     */                   
/* 470 */                   return stringbuilder.toString();
/*     */                 }
/*     */               });
/* 473 */           throw new ReportedException(crashreport);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 478 */     setFramesTextureData(list);
/*     */     
/* 480 */     if (this.spriteSingle != null)
/*     */     {
/* 482 */       this.spriteSingle.generateMipmaps(level);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void allocateFrameTextureData(int index) {
/* 488 */     if (this.framesTextureData.size() <= index)
/*     */     {
/* 490 */       for (int i = this.framesTextureData.size(); i <= index; i++)
/*     */       {
/* 492 */         this.framesTextureData.add((int[][])null);
/*     */       }
/*     */     }
/*     */     
/* 496 */     if (this.spriteSingle != null)
/*     */     {
/* 498 */       this.spriteSingle.allocateFrameTextureData(index);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_) {
/* 504 */     int[][] aint = new int[data.length][];
/*     */     
/* 506 */     for (int i = 0; i < data.length; i++) {
/*     */       
/* 508 */       int[] aint1 = data[i];
/*     */       
/* 510 */       if (aint1 != null) {
/*     */         
/* 512 */         aint[i] = new int[(rows >> i) * (columns >> i)];
/* 513 */         System.arraycopy(aint1, p_147962_3_ * (aint[i]).length, aint[i], 0, (aint[i]).length);
/*     */       } 
/*     */     } 
/*     */     
/* 517 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearFramesTextureData() {
/* 522 */     this.framesTextureData.clear();
/*     */     
/* 524 */     if (this.spriteSingle != null)
/*     */     {
/* 526 */       this.spriteSingle.clearFramesTextureData();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnimationMetadata() {
/* 532 */     return (this.animationMetadata != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFramesTextureData(List<int[][]> newFramesTextureData) {
/* 537 */     this.framesTextureData = newFramesTextureData;
/*     */     
/* 539 */     if (this.spriteSingle != null)
/*     */     {
/* 541 */       this.spriteSingle.setFramesTextureData(newFramesTextureData);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetSprite() {
/* 547 */     this.animationMetadata = null;
/* 548 */     setFramesTextureData(Lists.newArrayList());
/* 549 */     this.frameCounter = 0;
/* 550 */     this.tickCounter = 0;
/*     */     
/* 552 */     if (this.spriteSingle != null)
/*     */     {
/* 554 */       this.spriteSingle.resetSprite();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 560 */     return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomLoader(IResourceManager p_hasCustomLoader_1_, ResourceLocation p_hasCustomLoader_2_) {
/* 565 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean load(IResourceManager p_load_1_, ResourceLocation p_load_2_) {
/* 570 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndexInMap() {
/* 575 */     return this.indexInMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIndexInMap(int p_setIndexInMap_1_) {
/* 580 */     this.indexInMap = p_setIndexInMap_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateIndexInMap(CounterInt p_updateIndexInMap_1_) {
/* 585 */     if (this.indexInMap < 0)
/*     */     {
/* 587 */       this.indexInMap = p_updateIndexInMap_1_.nextValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnimationIndex() {
/* 593 */     return this.animationIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnimationIndex(int p_setAnimationIndex_1_) {
/* 598 */     this.animationIndex = p_setAnimationIndex_1_;
/*     */     
/* 600 */     if (this.spriteNormal != null)
/*     */     {
/* 602 */       this.spriteNormal.setAnimationIndex(p_setAnimationIndex_1_);
/*     */     }
/*     */     
/* 605 */     if (this.spriteSpecular != null)
/*     */     {
/* 607 */       this.spriteSpecular.setAnimationIndex(p_setAnimationIndex_1_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnimationActive() {
/* 613 */     return this.animationActive;
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixTransparentColor(int[] p_fixTransparentColor_1_) {
/* 618 */     if (p_fixTransparentColor_1_ != null) {
/*     */       
/* 620 */       long i = 0L;
/* 621 */       long j = 0L;
/* 622 */       long k = 0L;
/* 623 */       long l = 0L;
/*     */       
/* 625 */       for (int i1 = 0; i1 < p_fixTransparentColor_1_.length; i1++) {
/*     */         
/* 627 */         int j1 = p_fixTransparentColor_1_[i1];
/* 628 */         int k1 = j1 >> 24 & 0xFF;
/*     */         
/* 630 */         if (k1 >= 16) {
/*     */           
/* 632 */           int l1 = j1 >> 16 & 0xFF;
/* 633 */           int i2 = j1 >> 8 & 0xFF;
/* 634 */           int j2 = j1 & 0xFF;
/* 635 */           i += l1;
/* 636 */           j += i2;
/* 637 */           k += j2;
/* 638 */           l++;
/*     */         } 
/*     */       } 
/*     */       
/* 642 */       if (l > 0L) {
/*     */         
/* 644 */         int l2 = (int)(i / l);
/* 645 */         int i3 = (int)(j / l);
/* 646 */         int j3 = (int)(k / l);
/* 647 */         int k3 = l2 << 16 | i3 << 8 | j3;
/*     */         
/* 649 */         for (int l3 = 0; l3 < p_fixTransparentColor_1_.length; l3++) {
/*     */           
/* 651 */           int i4 = p_fixTransparentColor_1_[l3];
/* 652 */           int k2 = i4 >> 24 & 0xFF;
/*     */           
/* 654 */           if (k2 <= 16)
/*     */           {
/* 656 */             p_fixTransparentColor_1_[l3] = k3;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double getSpriteU16(float p_getSpriteU16_1_) {
/* 665 */     float f = this.maxU - this.minU;
/* 666 */     return ((p_getSpriteU16_1_ - this.minU) / f * 16.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getSpriteV16(float p_getSpriteV16_1_) {
/* 671 */     float f = this.maxV - this.minV;
/* 672 */     return ((p_getSpriteV16_1_ - this.minV) / f * 16.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void bindSpriteTexture() {
/* 677 */     if (this.glSpriteTextureId < 0) {
/*     */       
/* 679 */       this.glSpriteTextureId = TextureUtil.glGenTextures();
/* 680 */       TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
/* 681 */       TextureUtils.applyAnisotropicLevel();
/*     */     } 
/*     */     
/* 684 */     TextureUtils.bindTexture(this.glSpriteTextureId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteSpriteTexture() {
/* 689 */     if (this.glSpriteTextureId >= 0) {
/*     */       
/* 691 */       TextureUtil.deleteTexture(this.glSpriteTextureId);
/* 692 */       this.glSpriteTextureId = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float toSingleU(float p_toSingleU_1_) {
/* 698 */     p_toSingleU_1_ -= this.baseU;
/* 699 */     float f = this.sheetWidth / this.width;
/* 700 */     p_toSingleU_1_ *= f;
/* 701 */     return p_toSingleU_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public float toSingleV(float p_toSingleV_1_) {
/* 706 */     p_toSingleV_1_ -= this.baseV;
/* 707 */     float f = this.sheetHeight / this.height;
/* 708 */     p_toSingleV_1_ *= f;
/* 709 */     return p_toSingleV_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<int[][]> getFramesTextureData() {
/* 714 */     List<int[][]> list = (List)new ArrayList<>();
/* 715 */     list.addAll(this.framesTextureData);
/* 716 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnimationMetadataSection getAnimationMetadata() {
/* 721 */     return this.animationMetadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAnimationMetadata(AnimationMetadataSection p_setAnimationMetadata_1_) {
/* 726 */     this.animationMetadata = p_setAnimationMetadata_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadShadersSprites() {
/* 731 */     if (Shaders.configNormalMap) {
/*     */       
/* 733 */       String s = this.iconName + "_n";
/* 734 */       ResourceLocation resourcelocation = new ResourceLocation(s);
/* 735 */       resourcelocation = Config.getTextureMap().completeResourceLocation(resourcelocation);
/*     */       
/* 737 */       if (Config.hasResource(resourcelocation)) {
/*     */         
/* 739 */         this.spriteNormal = new TextureAtlasSprite(s);
/* 740 */         this.spriteNormal.isShadersSprite = true;
/* 741 */         this.spriteNormal.copyFrom(this);
/* 742 */         this.spriteNormal.generateMipmaps(this.mipmapLevels);
/*     */       } 
/*     */     } 
/*     */     
/* 746 */     if (Shaders.configSpecularMap) {
/*     */       
/* 748 */       String s1 = this.iconName + "_s";
/* 749 */       ResourceLocation resourcelocation1 = new ResourceLocation(s1);
/* 750 */       resourcelocation1 = Config.getTextureMap().completeResourceLocation(resourcelocation1);
/*     */       
/* 752 */       if (Config.hasResource(resourcelocation1)) {
/*     */         
/* 754 */         this.spriteSpecular = new TextureAtlasSprite(s1);
/* 755 */         this.spriteSpecular.isShadersSprite = true;
/* 756 */         this.spriteSpecular.copyFrom(this);
/* 757 */         this.spriteSpecular.generateMipmaps(this.mipmapLevels);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\TextureAtlasSprite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */