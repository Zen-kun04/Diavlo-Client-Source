/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.StitcherException;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.data.AnimationMetadataSection;
/*     */ import net.minecraft.client.resources.data.TextureMetadataSection;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.BetterGrass;
/*     */ import net.optifine.ConnectedTextures;
/*     */ import net.optifine.CustomItems;
/*     */ import net.optifine.EmissiveTextures;
/*     */ import net.optifine.SmartAnimations;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.reflect.ReflectorForge;
/*     */ import net.optifine.shaders.ShadersTex;
/*     */ import net.optifine.util.CounterInt;
/*     */ import net.optifine.util.TextureUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class TextureMap
/*     */   extends AbstractTexture
/*     */   implements ITickableTextureObject {
/*  44 */   private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
/*  45 */   private static final Logger logger = LogManager.getLogger();
/*  46 */   public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
/*  47 */   public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
/*     */   
/*     */   private final List<TextureAtlasSprite> listAnimatedSprites;
/*     */   private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
/*     */   private final Map<String, TextureAtlasSprite> mapUploadedSprites;
/*     */   private final String basePath;
/*     */   private final IIconCreator iconCreator;
/*     */   private int mipmapLevels;
/*     */   private final TextureAtlasSprite missingImage;
/*     */   private boolean skipFirst;
/*     */   private TextureAtlasSprite[] iconGrid;
/*     */   private int iconGridSize;
/*     */   private int iconGridCountX;
/*     */   private int iconGridCountY;
/*     */   private double iconGridSizeU;
/*     */   private double iconGridSizeV;
/*     */   private CounterInt counterIndexInMap;
/*     */   public int atlasWidth;
/*     */   public int atlasHeight;
/*     */   private int countAnimationsActive;
/*     */   private int frameCountAnimations;
/*     */   
/*     */   public TextureMap(String p_i46099_1_) {
/*  70 */     this(p_i46099_1_, (IIconCreator)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureMap(String p_i5_1_, boolean p_i5_2_) {
/*  75 */     this(p_i5_1_, (IIconCreator)null, p_i5_2_);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn) {
/*  80 */     this(p_i46100_1_, iconCreatorIn, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureMap(String p_i6_1_, IIconCreator p_i6_2_, boolean p_i6_3_) {
/*  85 */     this.skipFirst = false;
/*  86 */     this.iconGrid = null;
/*  87 */     this.iconGridSize = -1;
/*  88 */     this.iconGridCountX = -1;
/*  89 */     this.iconGridCountY = -1;
/*  90 */     this.iconGridSizeU = -1.0D;
/*  91 */     this.iconGridSizeV = -1.0D;
/*  92 */     this.counterIndexInMap = new CounterInt(0);
/*  93 */     this.atlasWidth = 0;
/*  94 */     this.atlasHeight = 0;
/*  95 */     this.listAnimatedSprites = Lists.newArrayList();
/*  96 */     this.mapRegisteredSprites = Maps.newHashMap();
/*  97 */     this.mapUploadedSprites = Maps.newHashMap();
/*  98 */     this.missingImage = new TextureAtlasSprite("missingno");
/*  99 */     this.basePath = p_i6_1_;
/* 100 */     this.iconCreator = p_i6_2_;
/* 101 */     this.skipFirst = (p_i6_3_ && ENABLE_SKIP);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initMissingImage() {
/* 106 */     int i = getMinSpriteSize();
/* 107 */     int[] aint = getMissingImageData(i);
/* 108 */     this.missingImage.setIconWidth(i);
/* 109 */     this.missingImage.setIconHeight(i);
/* 110 */     int[][] aint1 = new int[this.mipmapLevels + 1][];
/* 111 */     aint1[0] = aint;
/* 112 */     this.missingImage.setFramesTextureData(Lists.newArrayList((Object[])new int[][][] { aint1 }));
/* 113 */     this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadTexture(IResourceManager resourceManager) throws IOException {
/* 118 */     if (this.iconCreator != null)
/*     */     {
/* 120 */       loadSprites(resourceManager, this.iconCreator);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_) {
/* 126 */     this.mapRegisteredSprites.clear();
/* 127 */     this.counterIndexInMap.reset();
/* 128 */     p_174943_2_.registerSprites(this);
/*     */     
/* 130 */     if (this.mipmapLevels >= 4) {
/*     */       
/* 132 */       this.mipmapLevels = detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
/* 133 */       Config.log("Mipmap levels: " + this.mipmapLevels);
/*     */     } 
/*     */     
/* 136 */     initMissingImage();
/* 137 */     deleteGlTexture();
/* 138 */     loadTextureAtlas(resourceManager);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadTextureAtlas(IResourceManager resourceManager) {
/* 143 */     Config.dbg("Multitexture: " + Config.isMultiTexture());
/*     */     
/* 145 */     if (Config.isMultiTexture())
/*     */     {
/* 147 */       for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values())
/*     */       {
/* 149 */         textureatlassprite.deleteSpriteTexture();
/*     */       }
/*     */     }
/*     */     
/* 153 */     ConnectedTextures.updateIcons(this);
/* 154 */     CustomItems.updateIcons(this);
/* 155 */     BetterGrass.updateIcons(this);
/* 156 */     int i2 = TextureUtils.getGLMaximumTextureSize();
/* 157 */     Stitcher stitcher = new Stitcher(i2, i2, true, 0, this.mipmapLevels);
/* 158 */     this.mapUploadedSprites.clear();
/* 159 */     this.listAnimatedSprites.clear();
/* 160 */     int i = Integer.MAX_VALUE;
/* 161 */     Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, new Object[] { this });
/* 162 */     int j = getMinSpriteSize();
/* 163 */     this.iconGridSize = j;
/* 164 */     int k = 1 << this.mipmapLevels;
/* 165 */     int l = 0;
/* 166 */     int i1 = 0;
/* 167 */     Iterator<Map.Entry<String, TextureAtlasSprite>> iterator = this.mapRegisteredSprites.entrySet().iterator();
/*     */ 
/*     */ 
/*     */     
/* 171 */     while (iterator.hasNext()) {
/*     */       
/* 173 */       Map.Entry<String, TextureAtlasSprite> entry = iterator.next();
/*     */       
/* 175 */       if (!this.skipFirst) {
/*     */         
/* 177 */         TextureAtlasSprite textureatlassprite3 = entry.getValue();
/* 178 */         ResourceLocation resourcelocation1 = new ResourceLocation(textureatlassprite3.getIconName());
/* 179 */         ResourceLocation resourcelocation2 = completeResourceLocation(resourcelocation1, 0);
/* 180 */         textureatlassprite3.updateIndexInMap(this.counterIndexInMap);
/*     */         
/* 182 */         if (textureatlassprite3.hasCustomLoader(resourceManager, resourcelocation1)) {
/*     */           
/* 184 */           if (!textureatlassprite3.load(resourceManager, resourcelocation1)) {
/*     */             
/* 186 */             i = Math.min(i, Math.min(textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight()));
/* 187 */             stitcher.addSprite(textureatlassprite3);
/* 188 */             Config.detail("Custom loader (skipped): " + textureatlassprite3);
/* 189 */             i1++;
/*     */           } 
/*     */           
/* 192 */           Config.detail("Custom loader: " + textureatlassprite3);
/* 193 */           l++;
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/*     */         try {
/* 199 */           IResource iresource = resourceManager.getResource(resourcelocation2);
/* 200 */           BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
/* 201 */           abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
/* 202 */           int k3 = abufferedimage[0].getWidth();
/* 203 */           int l3 = abufferedimage[0].getHeight();
/*     */           
/* 205 */           if (k3 < 1 || l3 < 1) {
/*     */             
/* 207 */             Config.warn("Invalid sprite size: " + textureatlassprite3);
/*     */             
/*     */             continue;
/*     */           } 
/* 211 */           if (k3 < j || this.mipmapLevels > 0) {
/*     */             
/* 213 */             int i4 = (this.mipmapLevels > 0) ? TextureUtils.scaleToGrid(k3, j) : TextureUtils.scaleToMin(k3, j);
/*     */             
/* 215 */             if (i4 != k3) {
/*     */               
/* 217 */               if (!TextureUtils.isPowerOfTwo(k3)) {
/*     */                 
/* 219 */                 Config.log("Scaled non power of 2: " + textureatlassprite3.getIconName() + ", " + k3 + " -> " + i4);
/*     */               }
/*     */               else {
/*     */                 
/* 223 */                 Config.log("Scaled too small texture: " + textureatlassprite3.getIconName() + ", " + k3 + " -> " + i4);
/*     */               } 
/*     */               
/* 226 */               int j1 = l3 * i4 / k3;
/* 227 */               abufferedimage[0] = TextureUtils.scaleImage(abufferedimage[0], i4);
/*     */             } 
/*     */           } 
/*     */           
/* 231 */           TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
/*     */           
/* 233 */           if (texturemetadatasection != null) {
/*     */             
/* 235 */             List<Integer> list1 = texturemetadatasection.getListMipmaps();
/*     */             
/* 237 */             if (!list1.isEmpty()) {
/*     */               
/* 239 */               int k1 = abufferedimage[0].getWidth();
/* 240 */               int l1 = abufferedimage[0].getHeight();
/*     */               
/* 242 */               if (MathHelper.roundUpToPowerOfTwo(k1) != k1 || MathHelper.roundUpToPowerOfTwo(l1) != l1)
/*     */               {
/* 244 */                 throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
/*     */               }
/*     */             } 
/*     */             
/* 248 */             Iterator<Integer> iterator1 = list1.iterator();
/*     */             
/* 250 */             while (iterator1.hasNext()) {
/*     */               
/* 252 */               int j4 = ((Integer)iterator1.next()).intValue();
/*     */               
/* 254 */               if (j4 > 0 && j4 < abufferedimage.length - 1 && abufferedimage[j4] == null) {
/*     */                 
/* 256 */                 ResourceLocation resourcelocation = completeResourceLocation(resourcelocation1, j4);
/*     */ 
/*     */                 
/*     */                 try {
/* 260 */                   abufferedimage[j4] = TextureUtil.readBufferedImage(resourceManager.getResource(resourcelocation).getInputStream());
/*     */                 }
/* 262 */                 catch (IOException ioexception) {
/*     */                   
/* 264 */                   logger.error("Unable to load miplevel {} from: {}", new Object[] { Integer.valueOf(j4), resourcelocation, ioexception });
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 270 */           AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
/* 271 */           textureatlassprite3.loadSprite(abufferedimage, animationmetadatasection);
/*     */         }
/* 273 */         catch (RuntimeException runtimeexception) {
/*     */           
/* 275 */           logger.error("Unable to parse metadata from " + resourcelocation2, runtimeexception);
/* 276 */           ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation2, runtimeexception.getMessage());
/*     */           
/*     */           continue;
/* 279 */         } catch (IOException ioexception1) {
/*     */           
/* 281 */           logger.error("Using missing texture, unable to load " + resourcelocation2 + ", " + ioexception1.getClass().getName());
/* 282 */           ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation2);
/*     */           
/*     */           continue;
/*     */         } 
/* 286 */         i = Math.min(i, Math.min(textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight()));
/* 287 */         int j3 = Math.min(Integer.lowestOneBit(textureatlassprite3.getIconWidth()), Integer.lowestOneBit(textureatlassprite3.getIconHeight()));
/*     */         
/* 289 */         if (j3 < k) {
/*     */           
/* 291 */           logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { resourcelocation2, Integer.valueOf(textureatlassprite3.getIconWidth()), Integer.valueOf(textureatlassprite3.getIconHeight()), Integer.valueOf(MathHelper.calculateLogBaseTwo(k)), Integer.valueOf(MathHelper.calculateLogBaseTwo(j3)) });
/* 292 */           k = j3;
/*     */         } 
/*     */         
/* 295 */         stitcher.addSprite(textureatlassprite3);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 300 */     if (l > 0)
/*     */     {
/* 302 */       Config.dbg("Custom loader sprites: " + l);
/*     */     }
/*     */     
/* 305 */     if (i1 > 0)
/*     */     {
/* 307 */       Config.dbg("Custom loader sprites (skipped): " + i1);
/*     */     }
/*     */     
/* 310 */     int j2 = Math.min(i, k);
/* 311 */     int k2 = MathHelper.calculateLogBaseTwo(j2);
/*     */     
/* 313 */     if (k2 < 0)
/*     */     {
/* 315 */       k2 = 0;
/*     */     }
/*     */     
/* 318 */     if (k2 < this.mipmapLevels) {
/*     */       
/* 320 */       logger.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(k2), Integer.valueOf(j2) });
/* 321 */       this.mipmapLevels = k2;
/*     */     } 
/*     */     
/* 324 */     for (TextureAtlasSprite textureatlassprite1 : this.mapRegisteredSprites.values()) {
/*     */       
/* 326 */       if (this.skipFirst) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 333 */         textureatlassprite1.generateMipmaps(this.mipmapLevels);
/*     */       }
/* 335 */       catch (Throwable throwable1) {
/*     */         
/* 337 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
/* 338 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
/* 339 */         crashreportcategory.addCrashSectionCallable("Sprite name", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 343 */                 return textureatlassprite1.getIconName();
/*     */               }
/*     */             });
/* 346 */         crashreportcategory.addCrashSectionCallable("Sprite size", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 350 */                 return textureatlassprite1.getIconWidth() + " x " + textureatlassprite1.getIconHeight();
/*     */               }
/*     */             });
/* 353 */         crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable<String>()
/*     */             {
/*     */               public String call() throws Exception
/*     */               {
/* 357 */                 return textureatlassprite1.getFrameCount() + " frames";
/*     */               }
/*     */             });
/* 360 */         crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
/* 361 */         throw new ReportedException(crashreport);
/*     */       } 
/*     */     } 
/*     */     
/* 365 */     this.missingImage.generateMipmaps(this.mipmapLevels);
/* 366 */     stitcher.addSprite(this.missingImage);
/* 367 */     this.skipFirst = false;
/*     */ 
/*     */     
/*     */     try {
/* 371 */       stitcher.doStitch();
/*     */     }
/* 373 */     catch (StitcherException stitcherexception) {
/*     */       
/* 375 */       throw stitcherexception;
/*     */     } 
/*     */     
/* 378 */     logger.info("Created: {}x{} {}-atlas", new Object[] { Integer.valueOf(stitcher.getCurrentWidth()), Integer.valueOf(stitcher.getCurrentHeight()), this.basePath });
/*     */     
/* 380 */     if (Config.isShaders()) {
/*     */       
/* 382 */       ShadersTex.allocateTextureMap(getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
/*     */     }
/*     */     else {
/*     */       
/* 386 */       TextureUtil.allocateTextureImpl(getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
/*     */     } 
/*     */     
/* 389 */     Map<String, TextureAtlasSprite> map = Maps.newHashMap(this.mapRegisteredSprites);
/*     */     
/* 391 */     for (TextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots()) {
/*     */       
/* 393 */       String s = textureatlassprite2.getIconName();
/* 394 */       map.remove(s);
/* 395 */       this.mapUploadedSprites.put(s, textureatlassprite2);
/*     */ 
/*     */       
/*     */       try {
/* 399 */         if (Config.isShaders())
/*     */         {
/* 401 */           ShadersTex.uploadTexSubForLoadAtlas(this, textureatlassprite2.getIconName(), textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
/*     */         }
/*     */         else
/*     */         {
/* 405 */           TextureUtil.uploadTextureMipmap(textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
/*     */         }
/*     */       
/* 408 */       } catch (Throwable throwable) {
/*     */         
/* 410 */         CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
/* 411 */         CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Texture being stitched together");
/* 412 */         crashreportcategory1.addCrashSection("Atlas path", this.basePath);
/* 413 */         crashreportcategory1.addCrashSection("Sprite", textureatlassprite2);
/* 414 */         throw new ReportedException(crashreport1);
/*     */       } 
/*     */       
/* 417 */       if (textureatlassprite2.hasAnimationMetadata()) {
/*     */         
/* 419 */         textureatlassprite2.setAnimationIndex(this.listAnimatedSprites.size());
/* 420 */         this.listAnimatedSprites.add(textureatlassprite2);
/*     */       } 
/*     */     } 
/*     */     
/* 424 */     for (TextureAtlasSprite textureatlassprite4 : map.values())
/*     */     {
/* 426 */       textureatlassprite4.copyFrom(this.missingImage);
/*     */     }
/*     */     
/* 429 */     Config.log("Animated sprites: " + this.listAnimatedSprites.size());
/*     */     
/* 431 */     if (Config.isMultiTexture()) {
/*     */       
/* 433 */       int l2 = stitcher.getCurrentWidth();
/* 434 */       int i3 = stitcher.getCurrentHeight();
/*     */       
/* 436 */       for (TextureAtlasSprite textureatlassprite5 : stitcher.getStichSlots()) {
/*     */         
/* 438 */         textureatlassprite5.sheetWidth = l2;
/* 439 */         textureatlassprite5.sheetHeight = i3;
/* 440 */         textureatlassprite5.mipmapLevels = this.mipmapLevels;
/* 441 */         TextureAtlasSprite textureatlassprite6 = textureatlassprite5.spriteSingle;
/*     */         
/* 443 */         if (textureatlassprite6 != null) {
/*     */           
/* 445 */           if (textureatlassprite6.getIconWidth() <= 0) {
/*     */             
/* 447 */             textureatlassprite6.setIconWidth(textureatlassprite5.getIconWidth());
/* 448 */             textureatlassprite6.setIconHeight(textureatlassprite5.getIconHeight());
/* 449 */             textureatlassprite6.initSprite(textureatlassprite5.getIconWidth(), textureatlassprite5.getIconHeight(), 0, 0, false);
/* 450 */             textureatlassprite6.clearFramesTextureData();
/* 451 */             List<int[][]> list = textureatlassprite5.getFramesTextureData();
/* 452 */             textureatlassprite6.setFramesTextureData(list);
/* 453 */             textureatlassprite6.setAnimationMetadata(textureatlassprite5.getAnimationMetadata());
/*     */           } 
/*     */           
/* 456 */           textureatlassprite6.sheetWidth = l2;
/* 457 */           textureatlassprite6.sheetHeight = i3;
/* 458 */           textureatlassprite6.mipmapLevels = this.mipmapLevels;
/* 459 */           textureatlassprite6.setAnimationIndex(textureatlassprite5.getAnimationIndex());
/* 460 */           textureatlassprite5.bindSpriteTexture();
/* 461 */           boolean flag1 = false;
/* 462 */           boolean flag = true;
/*     */ 
/*     */           
/*     */           try {
/* 466 */             TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), flag1, flag);
/*     */           }
/* 468 */           catch (Exception exception) {
/*     */             
/* 470 */             Config.dbg("Error uploading sprite single: " + textureatlassprite6 + ", parent: " + textureatlassprite5);
/* 471 */             exception.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 476 */       Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
/*     */     } 
/*     */     
/* 479 */     Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, new Object[] { this });
/* 480 */     updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
/*     */     
/* 482 */     if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
/*     */       
/* 484 */       Config.dbg("Exporting texture map: " + this.basePath);
/* 485 */       TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceLocation completeResourceLocation(ResourceLocation p_completeResourceLocation_1_) {
/* 494 */     return completeResourceLocation(p_completeResourceLocation_1_, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_) {
/* 499 */     return isAbsoluteLocation(location) ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : ((p_147634_2_ == 0) ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] { this.basePath, location.getResourcePath(), ".png" })) : new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] { this.basePath, location.getResourcePath(), Integer.valueOf(p_147634_2_), ".png" })));
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getAtlasSprite(String iconName) {
/* 504 */     TextureAtlasSprite textureatlassprite = this.mapUploadedSprites.get(iconName);
/*     */     
/* 506 */     if (textureatlassprite == null)
/*     */     {
/* 508 */       textureatlassprite = this.missingImage;
/*     */     }
/*     */     
/* 511 */     return textureatlassprite;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAnimations() {
/* 516 */     boolean flag = false;
/* 517 */     boolean flag1 = false;
/* 518 */     TextureUtil.bindTexture(getGlTextureId());
/* 519 */     int i = 0;
/*     */     
/* 521 */     for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
/*     */       
/* 523 */       if (isTerrainAnimationActive(textureatlassprite)) {
/*     */         
/* 525 */         textureatlassprite.updateAnimation();
/*     */         
/* 527 */         if (textureatlassprite.isAnimationActive())
/*     */         {
/* 529 */           i++;
/*     */         }
/*     */         
/* 532 */         if (textureatlassprite.spriteNormal != null)
/*     */         {
/* 534 */           flag = true;
/*     */         }
/*     */         
/* 537 */         if (textureatlassprite.spriteSpecular != null)
/*     */         {
/* 539 */           flag1 = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 544 */     if (Config.isMultiTexture()) {
/*     */       
/* 546 */       for (TextureAtlasSprite textureatlassprite2 : this.listAnimatedSprites) {
/*     */         
/* 548 */         if (isTerrainAnimationActive(textureatlassprite2)) {
/*     */           
/* 550 */           TextureAtlasSprite textureatlassprite1 = textureatlassprite2.spriteSingle;
/*     */           
/* 552 */           if (textureatlassprite1 != null) {
/*     */             
/* 554 */             if (textureatlassprite2 == TextureUtils.iconClock || textureatlassprite2 == TextureUtils.iconCompass)
/*     */             {
/* 556 */               textureatlassprite1.frameCounter = textureatlassprite2.frameCounter;
/*     */             }
/*     */             
/* 559 */             textureatlassprite2.bindSpriteTexture();
/* 560 */             textureatlassprite1.updateAnimation();
/*     */             
/* 562 */             if (textureatlassprite1.isAnimationActive())
/*     */             {
/* 564 */               i++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 570 */       TextureUtil.bindTexture(getGlTextureId());
/*     */     } 
/*     */     
/* 573 */     if (Config.isShaders()) {
/*     */       
/* 575 */       if (flag) {
/*     */         
/* 577 */         TextureUtil.bindTexture((getMultiTexID()).norm);
/*     */         
/* 579 */         for (TextureAtlasSprite textureatlassprite3 : this.listAnimatedSprites) {
/*     */           
/* 581 */           if (textureatlassprite3.spriteNormal != null && isTerrainAnimationActive(textureatlassprite3)) {
/*     */             
/* 583 */             if (textureatlassprite3 == TextureUtils.iconClock || textureatlassprite3 == TextureUtils.iconCompass)
/*     */             {
/* 585 */               textureatlassprite3.spriteNormal.frameCounter = textureatlassprite3.frameCounter;
/*     */             }
/*     */             
/* 588 */             textureatlassprite3.spriteNormal.updateAnimation();
/*     */             
/* 590 */             if (textureatlassprite3.spriteNormal.isAnimationActive())
/*     */             {
/* 592 */               i++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 598 */       if (flag1) {
/*     */         
/* 600 */         TextureUtil.bindTexture((getMultiTexID()).spec);
/*     */         
/* 602 */         for (TextureAtlasSprite textureatlassprite4 : this.listAnimatedSprites) {
/*     */           
/* 604 */           if (textureatlassprite4.spriteSpecular != null && isTerrainAnimationActive(textureatlassprite4)) {
/*     */             
/* 606 */             if (textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass)
/*     */             {
/* 608 */               textureatlassprite4.spriteNormal.frameCounter = textureatlassprite4.frameCounter;
/*     */             }
/*     */             
/* 611 */             textureatlassprite4.spriteSpecular.updateAnimation();
/*     */             
/* 613 */             if (textureatlassprite4.spriteSpecular.isAnimationActive())
/*     */             {
/* 615 */               i++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 621 */       if (flag || flag1)
/*     */       {
/* 623 */         TextureUtil.bindTexture(getGlTextureId());
/*     */       }
/*     */     } 
/*     */     
/* 627 */     int j = (Config.getMinecraft()).entityRenderer.frameCount;
/*     */     
/* 629 */     if (j != this.frameCountAnimations) {
/*     */       
/* 631 */       this.countAnimationsActive = i;
/* 632 */       this.frameCountAnimations = j;
/*     */     } 
/*     */     
/* 635 */     if (SmartAnimations.isActive())
/*     */     {
/* 637 */       SmartAnimations.resetSpritesRendered();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite registerSprite(ResourceLocation location) {
/* 643 */     if (location == null)
/*     */     {
/* 645 */       throw new IllegalArgumentException("Location cannot be null!");
/*     */     }
/*     */ 
/*     */     
/* 649 */     TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());
/*     */     
/* 651 */     if (textureatlassprite == null) {
/*     */       
/* 653 */       textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
/* 654 */       this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
/* 655 */       textureatlassprite.updateIndexInMap(this.counterIndexInMap);
/*     */       
/* 657 */       if (Config.isEmissiveTextures())
/*     */       {
/* 659 */         checkEmissive(location, textureatlassprite);
/*     */       }
/*     */     } 
/*     */     
/* 663 */     return textureatlassprite;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 669 */     updateAnimations();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMipmapLevels(int mipmapLevelsIn) {
/* 674 */     this.mipmapLevels = mipmapLevelsIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getMissingSprite() {
/* 679 */     return this.missingImage;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_) {
/* 684 */     return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_) {
/* 689 */     if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_)) {
/*     */       
/* 691 */       this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
/* 692 */       p_setTextureEntry_2_.updateIndexInMap(this.counterIndexInMap);
/* 693 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 697 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_) {
/* 703 */     return setTextureEntry(p_setTextureEntry_1_.getIconName(), p_setTextureEntry_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBasePath() {
/* 708 */     return this.basePath;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMipmapLevels() {
/* 713 */     return this.mipmapLevels;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
/* 718 */     String s = p_isAbsoluteLocation_1_.getResourcePath();
/* 719 */     return isAbsoluteLocationPath(s);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
/* 724 */     String s = p_isAbsoluteLocationPath_1_.toLowerCase();
/* 725 */     return (s.startsWith("mcpatcher/") || s.startsWith("optifine/"));
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_) {
/* 730 */     ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
/* 731 */     return this.mapRegisteredSprites.get(resourcelocation.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getRegisteredSprite(ResourceLocation p_getRegisteredSprite_1_) {
/* 736 */     return this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_) {
/* 741 */     return (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow) ? ((p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow) ? ((p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1) ? ((p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal) ? Config.isAnimatedPortal() : ((p_isTerrainAnimationActive_1_ != TextureUtils.iconClock && p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass) ? Config.isAnimatedTerrain() : true)) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCountRegisteredSprites() {
/* 746 */     return this.counterIndexInMap.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_) {
/* 751 */     int i = detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
/*     */     
/* 753 */     if (i < 16)
/*     */     {
/* 755 */       i = 16;
/*     */     }
/*     */     
/* 758 */     i = MathHelper.roundUpToPowerOfTwo(i);
/*     */     
/* 760 */     if (i > 16)
/*     */     {
/* 762 */       Config.log("Sprite size: " + i);
/*     */     }
/*     */     
/* 765 */     int j = MathHelper.calculateLogBaseTwo(i);
/*     */     
/* 767 */     if (j < 4)
/*     */     {
/* 769 */       j = 4;
/*     */     }
/*     */     
/* 772 */     return j;
/*     */   }
/*     */ 
/*     */   
/*     */   private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_) {
/* 777 */     Map<Object, Object> map = new HashMap<>();
/*     */     
/* 779 */     for (Object o : p_detectMinimumSpriteSize_1_.entrySet()) {
/*     */       
/* 781 */       Map.Entry entry = (Map.Entry)o;
/* 782 */       TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)entry.getValue();
/* 783 */       ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
/* 784 */       ResourceLocation resourcelocation1 = completeResourceLocation(resourcelocation);
/*     */       
/* 786 */       if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) {
/*     */         
/*     */         try {
/*     */           
/* 790 */           IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation1);
/*     */           
/* 792 */           if (iresource != null) {
/*     */             
/* 794 */             InputStream inputstream = iresource.getInputStream();
/*     */             
/* 796 */             if (inputstream != null)
/*     */             {
/* 798 */               Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
/* 799 */               inputstream.close();
/*     */               
/* 801 */               if (dimension != null)
/*     */               {
/* 803 */                 int i = dimension.width;
/* 804 */                 int j = MathHelper.roundUpToPowerOfTwo(i);
/*     */                 
/* 806 */                 if (!map.containsKey(Integer.valueOf(j))) {
/*     */                   
/* 808 */                   map.put(Integer.valueOf(j), Integer.valueOf(1));
/*     */                   
/*     */                   continue;
/*     */                 } 
/* 812 */                 int k = ((Integer)map.get(Integer.valueOf(j))).intValue();
/* 813 */                 map.put(Integer.valueOf(j), Integer.valueOf(k + 1));
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } 
/* 819 */         } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 826 */     int l = 0;
/* 827 */     Set<?> set = map.keySet();
/* 828 */     Set set1 = new TreeSet(set);
/*     */ 
/*     */     
/* 831 */     for (Iterator<Integer> iterator = set1.iterator(); iterator.hasNext(); l += i) {
/*     */       
/* 833 */       int j1 = ((Integer)iterator.next()).intValue();
/* 834 */       int i = ((Integer)map.get(Integer.valueOf(j1))).intValue();
/*     */     } 
/*     */     
/* 837 */     int i1 = 16;
/* 838 */     int k1 = 0;
/* 839 */     int l1 = l * p_detectMinimumSpriteSize_3_ / 100;
/* 840 */     Iterator<Integer> iterator1 = set1.iterator();
/*     */     
/* 842 */     while (iterator1.hasNext()) {
/*     */       
/* 844 */       int i2 = ((Integer)iterator1.next()).intValue();
/* 845 */       int j2 = ((Integer)map.get(Integer.valueOf(i2))).intValue();
/* 846 */       k1 += j2;
/*     */       
/* 848 */       if (i2 > i1)
/*     */       {
/* 850 */         i1 = i2;
/*     */       }
/*     */       
/* 853 */       if (k1 > l1)
/*     */       {
/* 855 */         return i1;
/*     */       }
/*     */     } 
/*     */     
/* 859 */     return i1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getMinSpriteSize() {
/* 864 */     int i = 1 << this.mipmapLevels;
/*     */     
/* 866 */     if (i < 8)
/*     */     {
/* 868 */       i = 8;
/*     */     }
/*     */     
/* 871 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] getMissingImageData(int p_getMissingImageData_1_) {
/* 876 */     BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
/* 877 */     bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
/* 878 */     BufferedImage bufferedimage1 = TextureUtils.scaleImage(bufferedimage, p_getMissingImageData_1_);
/* 879 */     int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
/* 880 */     bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
/* 881 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTextureBound() {
/* 886 */     int i = GlStateManager.getBoundTexture();
/* 887 */     int j = getGlTextureId();
/* 888 */     return (i == j);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_) {
/* 893 */     this.iconGridCountX = -1;
/* 894 */     this.iconGridCountY = -1;
/* 895 */     this.iconGrid = null;
/*     */     
/* 897 */     if (this.iconGridSize > 0) {
/*     */       
/* 899 */       this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
/* 900 */       this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
/* 901 */       this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
/* 902 */       this.iconGridSizeU = 1.0D / this.iconGridCountX;
/* 903 */       this.iconGridSizeV = 1.0D / this.iconGridCountY;
/*     */       
/* 905 */       for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values()) {
/*     */         
/* 907 */         double d0 = 0.5D / p_updateIconGrid_1_;
/* 908 */         double d1 = 0.5D / p_updateIconGrid_2_;
/* 909 */         double d2 = Math.min(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) + d0;
/* 910 */         double d3 = Math.min(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) + d1;
/* 911 */         double d4 = Math.max(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) - d0;
/* 912 */         double d5 = Math.max(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) - d1;
/* 913 */         int i = (int)(d2 / this.iconGridSizeU);
/* 914 */         int j = (int)(d3 / this.iconGridSizeV);
/* 915 */         int k = (int)(d4 / this.iconGridSizeU);
/* 916 */         int l = (int)(d5 / this.iconGridSizeV);
/*     */         
/* 918 */         for (int i1 = i; i1 <= k; i1++) {
/*     */           
/* 920 */           if (i1 >= 0 && i1 < this.iconGridCountX) {
/*     */             
/* 922 */             for (int j1 = j; j1 <= l; j1++) {
/*     */               
/* 924 */               if (j1 >= 0 && j1 < this.iconGridCountX)
/*     */               {
/* 926 */                 int k1 = j1 * this.iconGridCountX + i1;
/* 927 */                 this.iconGrid[k1] = textureatlassprite;
/*     */               }
/*     */               else
/*     */               {
/* 931 */                 Config.warn("Invalid grid V: " + j1 + ", icon: " + textureatlassprite.getIconName());
/*     */               }
/*     */             
/*     */             } 
/*     */           } else {
/*     */             
/* 937 */             Config.warn("Invalid grid U: " + i1 + ", icon: " + textureatlassprite.getIconName());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_) {
/* 946 */     if (this.iconGrid == null)
/*     */     {
/* 948 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 952 */     int i = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
/* 953 */     int j = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
/* 954 */     int k = j * this.iconGridCountX + i;
/* 955 */     return (k >= 0 && k <= this.iconGrid.length) ? this.iconGrid[k] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkEmissive(ResourceLocation p_checkEmissive_1_, TextureAtlasSprite p_checkEmissive_2_) {
/* 961 */     String s = EmissiveTextures.getSuffixEmissive();
/*     */     
/* 963 */     if (s != null)
/*     */     {
/* 965 */       if (!p_checkEmissive_1_.getResourcePath().endsWith(s)) {
/*     */         
/* 967 */         ResourceLocation resourcelocation = new ResourceLocation(p_checkEmissive_1_.getResourceDomain(), p_checkEmissive_1_.getResourcePath() + s);
/* 968 */         ResourceLocation resourcelocation1 = completeResourceLocation(resourcelocation);
/*     */         
/* 970 */         if (Config.hasResource(resourcelocation1)) {
/*     */           
/* 972 */           TextureAtlasSprite textureatlassprite = registerSprite(resourcelocation);
/* 973 */           textureatlassprite.isEmissive = true;
/* 974 */           p_checkEmissive_2_.spriteEmissive = textureatlassprite;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCountAnimations() {
/* 982 */     return this.listAnimatedSprites.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCountAnimationsActive() {
/* 987 */     return this.countAnimationsActive;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\TextureMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */