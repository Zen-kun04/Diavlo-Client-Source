/*     */ package net.optifine.util;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.ITickableTextureObject;
/*     */ import net.minecraft.client.renderer.texture.SimpleTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.resources.IReloadableResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.BetterGrass;
/*     */ import net.optifine.BetterSnow;
/*     */ import net.optifine.CustomBlockLayers;
/*     */ import net.optifine.CustomColors;
/*     */ import net.optifine.CustomGuis;
/*     */ import net.optifine.CustomItems;
/*     */ import net.optifine.CustomLoadingScreens;
/*     */ import net.optifine.CustomPanorama;
/*     */ import net.optifine.CustomSky;
/*     */ import net.optifine.Lang;
/*     */ import net.optifine.NaturalTextures;
/*     */ import net.optifine.RandomEntities;
/*     */ import net.optifine.SmartLeaves;
/*     */ import net.optifine.TextureAnimations;
/*     */ import net.optifine.entity.model.CustomEntityModels;
/*     */ import net.optifine.shaders.MultiTexID;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextureUtils
/*     */ {
/*     */   public static final String texGrassTop = "grass_top";
/*     */   public static final String texStone = "stone";
/*     */   public static final String texDirt = "dirt";
/*     */   public static final String texCoarseDirt = "coarse_dirt";
/*     */   public static final String texGrassSide = "grass_side";
/*     */   public static final String texStoneslabSide = "stone_slab_side";
/*     */   public static final String texStoneslabTop = "stone_slab_top";
/*     */   public static final String texBedrock = "bedrock";
/*     */   public static final String texSand = "sand";
/*     */   public static final String texGravel = "gravel";
/*     */   public static final String texLogOak = "log_oak";
/*     */   public static final String texLogBigOak = "log_big_oak";
/*     */   public static final String texLogAcacia = "log_acacia";
/*     */   public static final String texLogSpruce = "log_spruce";
/*     */   public static final String texLogBirch = "log_birch";
/*     */   public static final String texLogJungle = "log_jungle";
/*     */   public static final String texLogOakTop = "log_oak_top";
/*     */   public static final String texLogBigOakTop = "log_big_oak_top";
/*     */   public static final String texLogAcaciaTop = "log_acacia_top";
/*     */   public static final String texLogSpruceTop = "log_spruce_top";
/*     */   public static final String texLogBirchTop = "log_birch_top";
/*     */   public static final String texLogJungleTop = "log_jungle_top";
/*     */   public static final String texLeavesOak = "leaves_oak";
/*     */   public static final String texLeavesBigOak = "leaves_big_oak";
/*     */   public static final String texLeavesAcacia = "leaves_acacia";
/*     */   public static final String texLeavesBirch = "leaves_birch";
/*     */   public static final String texLeavesSpuce = "leaves_spruce";
/*     */   public static final String texLeavesJungle = "leaves_jungle";
/*     */   public static final String texGoldOre = "gold_ore";
/*     */   public static final String texIronOre = "iron_ore";
/*     */   public static final String texCoalOre = "coal_ore";
/*     */   public static final String texObsidian = "obsidian";
/*     */   public static final String texGrassSideOverlay = "grass_side_overlay";
/*     */   public static final String texSnow = "snow";
/*     */   public static final String texGrassSideSnowed = "grass_side_snowed";
/*     */   public static final String texMyceliumSide = "mycelium_side";
/*     */   public static final String texMyceliumTop = "mycelium_top";
/*     */   public static final String texDiamondOre = "diamond_ore";
/*     */   public static final String texRedstoneOre = "redstone_ore";
/*     */   public static final String texLapisOre = "lapis_ore";
/*     */   public static final String texCactusSide = "cactus_side";
/*     */   public static final String texClay = "clay";
/* 124 */   private static IntBuffer staticBuffer = GLAllocation.createDirectIntBuffer(256); public static final String texFarmlandWet = "farmland_wet"; public static final String texFarmlandDry = "farmland_dry"; public static final String texNetherrack = "netherrack"; public static final String texSoulSand = "soul_sand"; public static final String texGlowstone = "glowstone"; public static final String texLeavesSpruce = "leaves_spruce"; public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque"; public static final String texEndStone = "end_stone"; public static final String texSandstoneTop = "sandstone_top"; public static final String texSandstoneBottom = "sandstone_bottom"; public static final String texRedstoneLampOff = "redstone_lamp_off"; public static final String texRedstoneLampOn = "redstone_lamp_on"; public static final String texWaterStill = "water_still"; public static final String texWaterFlow = "water_flow"; public static final String texLavaStill = "lava_still"; public static final String texLavaFlow = "lava_flow"; public static final String texFireLayer0 = "fire_layer_0"; public static final String texFireLayer1 = "fire_layer_1"; public static final String texPortal = "portal"; public static final String texGlass = "glass"; public static final String texGlassPaneTop = "glass_pane_top"; public static final String texCompass = "compass"; public static final String texClock = "clock"; public static TextureAtlasSprite iconGrassTop; public static TextureAtlasSprite iconGrassSide; public static TextureAtlasSprite iconGrassSideOverlay; public static TextureAtlasSprite iconSnow; public static TextureAtlasSprite iconGrassSideSnowed; public static TextureAtlasSprite iconMyceliumSide; public static TextureAtlasSprite iconMyceliumTop; public static TextureAtlasSprite iconWaterStill; public static TextureAtlasSprite iconWaterFlow; public static TextureAtlasSprite iconLavaStill; public static TextureAtlasSprite iconLavaFlow; public static TextureAtlasSprite iconPortal; public static TextureAtlasSprite iconFireLayer0; public static TextureAtlasSprite iconFireLayer1; public static TextureAtlasSprite iconGlass; public static TextureAtlasSprite iconGlassPaneTop; public static TextureAtlasSprite iconCompass; public static TextureAtlasSprite iconClock; public static final String SPRITE_PREFIX_BLOCKS = "minecraft:blocks/";
/*     */   public static final String SPRITE_PREFIX_ITEMS = "minecraft:items/";
/*     */   
/*     */   public static void update() {
/* 128 */     TextureMap texturemap = getTextureMapBlocks();
/*     */     
/* 130 */     if (texturemap != null) {
/*     */       
/* 132 */       String s = "minecraft:blocks/";
/* 133 */       iconGrassTop = texturemap.getSpriteSafe(s + "grass_top");
/* 134 */       iconGrassSide = texturemap.getSpriteSafe(s + "grass_side");
/* 135 */       iconGrassSideOverlay = texturemap.getSpriteSafe(s + "grass_side_overlay");
/* 136 */       iconSnow = texturemap.getSpriteSafe(s + "snow");
/* 137 */       iconGrassSideSnowed = texturemap.getSpriteSafe(s + "grass_side_snowed");
/* 138 */       iconMyceliumSide = texturemap.getSpriteSafe(s + "mycelium_side");
/* 139 */       iconMyceliumTop = texturemap.getSpriteSafe(s + "mycelium_top");
/* 140 */       iconWaterStill = texturemap.getSpriteSafe(s + "water_still");
/* 141 */       iconWaterFlow = texturemap.getSpriteSafe(s + "water_flow");
/* 142 */       iconLavaStill = texturemap.getSpriteSafe(s + "lava_still");
/* 143 */       iconLavaFlow = texturemap.getSpriteSafe(s + "lava_flow");
/* 144 */       iconFireLayer0 = texturemap.getSpriteSafe(s + "fire_layer_0");
/* 145 */       iconFireLayer1 = texturemap.getSpriteSafe(s + "fire_layer_1");
/* 146 */       iconPortal = texturemap.getSpriteSafe(s + "portal");
/* 147 */       iconGlass = texturemap.getSpriteSafe(s + "glass");
/* 148 */       iconGlassPaneTop = texturemap.getSpriteSafe(s + "glass_pane_top");
/* 149 */       String s1 = "minecraft:items/";
/* 150 */       iconCompass = texturemap.getSpriteSafe(s1 + "compass");
/* 151 */       iconClock = texturemap.getSpriteSafe(s1 + "clock");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static BufferedImage fixTextureDimensions(String name, BufferedImage bi) {
/* 157 */     if (name.startsWith("/mob/zombie") || name.startsWith("/mob/pigzombie")) {
/*     */       
/* 159 */       int i = bi.getWidth();
/* 160 */       int j = bi.getHeight();
/*     */       
/* 162 */       if (i == j * 2) {
/*     */         
/* 164 */         BufferedImage bufferedimage = new BufferedImage(i, j * 2, 2);
/* 165 */         Graphics2D graphics2d = bufferedimage.createGraphics();
/* 166 */         graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/* 167 */         graphics2d.drawImage(bi, 0, 0, i, j, (ImageObserver)null);
/* 168 */         return bufferedimage;
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     return bi;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ceilPowerOfTwo(int val) {
/*     */     int i;
/* 179 */     for (i = 1; i < val; i *= 2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getPowerOfTwo(int val) {
/* 189 */     int i = 1;
/*     */     
/*     */     int j;
/* 192 */     for (j = 0; i < val; j++)
/*     */     {
/* 194 */       i *= 2;
/*     */     }
/*     */     
/* 197 */     return j;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int twoToPower(int power) {
/* 202 */     int i = 1;
/*     */     
/* 204 */     for (int j = 0; j < power; j++)
/*     */     {
/* 206 */       i *= 2;
/*     */     }
/*     */     
/* 209 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ITextureObject getTexture(ResourceLocation loc) {
/* 214 */     ITextureObject itextureobject = Config.getTextureManager().getTexture(loc);
/*     */     
/* 216 */     if (itextureobject != null)
/*     */     {
/* 218 */       return itextureobject;
/*     */     }
/* 220 */     if (!Config.hasResource(loc))
/*     */     {
/* 222 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 226 */     SimpleTexture simpletexture = new SimpleTexture(loc);
/* 227 */     Config.getTextureManager().loadTexture(loc, (ITextureObject)simpletexture);
/* 228 */     return (ITextureObject)simpletexture;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void resourcesReloaded(IResourceManager rm) {
/* 234 */     if (getTextureMapBlocks() != null) {
/*     */       
/* 236 */       Config.dbg("*** Reloading custom textures ***");
/* 237 */       CustomSky.reset();
/* 238 */       TextureAnimations.reset();
/* 239 */       update();
/* 240 */       NaturalTextures.update();
/* 241 */       BetterGrass.update();
/* 242 */       BetterSnow.update();
/* 243 */       TextureAnimations.update();
/* 244 */       CustomColors.update();
/* 245 */       CustomSky.update();
/* 246 */       RandomEntities.update();
/* 247 */       CustomItems.updateModels();
/* 248 */       CustomEntityModels.update();
/* 249 */       Shaders.resourcesReloaded();
/* 250 */       Lang.resourcesReloaded();
/* 251 */       Config.updateTexturePackClouds();
/* 252 */       SmartLeaves.updateLeavesModels();
/* 253 */       CustomPanorama.update();
/* 254 */       CustomGuis.update();
/* 255 */       LayerMooshroomMushroom.update();
/* 256 */       CustomLoadingScreens.update();
/* 257 */       CustomBlockLayers.update();
/* 258 */       Config.getTextureManager().tick();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static TextureMap getTextureMapBlocks() {
/* 264 */     return Minecraft.getMinecraft().getTextureMapBlocks();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void registerResourceListener() {
/* 269 */     IResourceManager iresourcemanager = Config.getResourceManager();
/*     */     
/* 271 */     if (iresourcemanager instanceof IReloadableResourceManager) {
/*     */       
/* 273 */       IReloadableResourceManager ireloadableresourcemanager = (IReloadableResourceManager)iresourcemanager;
/* 274 */       IResourceManagerReloadListener iresourcemanagerreloadlistener = new IResourceManagerReloadListener()
/*     */         {
/*     */           public void onResourceManagerReload(IResourceManager var1)
/*     */           {
/* 278 */             TextureUtils.resourcesReloaded(var1);
/*     */           }
/*     */         };
/* 281 */       ireloadableresourcemanager.registerReloadListener(iresourcemanagerreloadlistener);
/*     */     } 
/*     */     
/* 284 */     ITickableTextureObject itickabletextureobject = new ITickableTextureObject()
/*     */       {
/*     */         public void tick()
/*     */         {
/* 288 */           TextureAnimations.updateAnimations();
/*     */         }
/*     */ 
/*     */         
/*     */         public void loadTexture(IResourceManager var1) throws IOException {}
/*     */         
/*     */         public int getGlTextureId() {
/* 295 */           return 0;
/*     */         }
/*     */ 
/*     */         
/*     */         public void setBlurMipmap(boolean p_174936_1, boolean p_174936_2) {}
/*     */ 
/*     */         
/*     */         public void restoreLastBlurMipmap() {}
/*     */         
/*     */         public MultiTexID getMultiTexID() {
/* 305 */           return null;
/*     */         }
/*     */       };
/* 308 */     ResourceLocation resourcelocation = new ResourceLocation("optifine/TickableTextures");
/* 309 */     Config.getTextureManager().loadTickableTexture(resourcelocation, itickabletextureobject);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResourceLocation fixResourceLocation(ResourceLocation loc, String basePath) {
/* 314 */     if (!loc.getResourceDomain().equals("minecraft"))
/*     */     {
/* 316 */       return loc;
/*     */     }
/*     */ 
/*     */     
/* 320 */     String s = loc.getResourcePath();
/* 321 */     String s1 = fixResourcePath(s, basePath);
/*     */     
/* 323 */     if (s1 != s)
/*     */     {
/* 325 */       loc = new ResourceLocation(loc.getResourceDomain(), s1);
/*     */     }
/*     */     
/* 328 */     return loc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String fixResourcePath(String path, String basePath) {
/* 334 */     String s = "assets/minecraft/";
/*     */     
/* 336 */     if (path.startsWith(s)) {
/*     */       
/* 338 */       path = path.substring(s.length());
/* 339 */       return path;
/*     */     } 
/* 341 */     if (path.startsWith("./")) {
/*     */       
/* 343 */       path = path.substring(2);
/*     */       
/* 345 */       if (!basePath.endsWith("/"))
/*     */       {
/* 347 */         basePath = basePath + "/";
/*     */       }
/*     */       
/* 350 */       path = basePath + path;
/* 351 */       return path;
/*     */     } 
/*     */ 
/*     */     
/* 355 */     if (path.startsWith("/~"))
/*     */     {
/* 357 */       path = path.substring(1);
/*     */     }
/*     */     
/* 360 */     String s1 = "mcpatcher/";
/*     */     
/* 362 */     if (path.startsWith("~/")) {
/*     */       
/* 364 */       path = path.substring(2);
/* 365 */       path = s1 + path;
/* 366 */       return path;
/*     */     } 
/* 368 */     if (path.startsWith("/")) {
/*     */       
/* 370 */       path = s1 + path.substring(1);
/* 371 */       return path;
/*     */     } 
/*     */ 
/*     */     
/* 375 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getBasePath(String path) {
/* 382 */     int i = path.lastIndexOf('/');
/* 383 */     return (i < 0) ? "" : path.substring(0, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void applyAnisotropicLevel() {
/* 388 */     if ((GLContext.getCapabilities()).GL_EXT_texture_filter_anisotropic) {
/*     */       
/* 390 */       float f = GL11.glGetFloat(34047);
/* 391 */       float f1 = Config.getAnisotropicFilterLevel();
/* 392 */       f1 = Math.min(f1, f);
/* 393 */       GL11.glTexParameterf(3553, 34046, f1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bindTexture(int glTexId) {
/* 399 */     GlStateManager.bindTexture(glTexId);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPowerOfTwo(int x) {
/* 404 */     int i = MathHelper.roundUpToPowerOfTwo(x);
/* 405 */     return (i == x);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BufferedImage scaleImage(BufferedImage bi, int w2) {
/* 410 */     int i = bi.getWidth();
/* 411 */     int j = bi.getHeight();
/* 412 */     int k = j * w2 / i;
/* 413 */     BufferedImage bufferedimage = new BufferedImage(w2, k, 2);
/* 414 */     Graphics2D graphics2d = bufferedimage.createGraphics();
/* 415 */     Object object = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/*     */     
/* 417 */     if (w2 < i || w2 % i != 0)
/*     */     {
/* 419 */       object = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
/*     */     }
/*     */     
/* 422 */     graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, object);
/* 423 */     graphics2d.drawImage(bi, 0, 0, w2, k, (ImageObserver)null);
/* 424 */     return bufferedimage;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int scaleToGrid(int size, int sizeGrid) {
/* 429 */     if (size == sizeGrid)
/*     */     {
/* 431 */       return size;
/*     */     }
/*     */ 
/*     */     
/*     */     int i;
/*     */     
/* 437 */     for (i = size / sizeGrid * sizeGrid; i < size; i += sizeGrid);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 442 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int scaleToMin(int size, int sizeMin) {
/* 448 */     if (size >= sizeMin)
/*     */     {
/* 450 */       return size;
/*     */     }
/*     */ 
/*     */     
/*     */     int i;
/*     */     
/* 456 */     for (i = sizeMin / size * size; i < sizeMin; i += size);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dimension getImageSize(InputStream in, String suffix) {
/* 467 */     Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix(suffix);
/*     */ 
/*     */ 
/*     */     
/* 471 */     while (iterator.hasNext()) {
/*     */       Dimension dimension;
/* 473 */       ImageReader imagereader = iterator.next();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 478 */         ImageInputStream imageinputstream = ImageIO.createImageInputStream(in);
/* 479 */         imagereader.setInput(imageinputstream);
/* 480 */         int i = imagereader.getWidth(imagereader.getMinIndex());
/* 481 */         int j = imagereader.getHeight(imagereader.getMinIndex());
/* 482 */         dimension = new Dimension(i, j);
/*     */       }
/* 484 */       catch (IOException var11) {
/*     */         
/*     */         continue;
/*     */       }
/*     */       finally {
/*     */         
/* 490 */         imagereader.dispose();
/*     */       } 
/*     */       
/* 493 */       return dimension;
/*     */     } 
/*     */     
/* 496 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dbgMipmaps(TextureAtlasSprite textureatlassprite) {
/* 502 */     int[][] aint = textureatlassprite.getFrameTextureData(0);
/*     */     
/* 504 */     for (int i = 0; i < aint.length; i++) {
/*     */       
/* 506 */       int[] aint1 = aint[i];
/*     */       
/* 508 */       if (aint1 == null) {
/*     */         
/* 510 */         Config.dbg("" + i + ": " + aint1);
/*     */       }
/*     */       else {
/*     */         
/* 514 */         Config.dbg("" + i + ": " + aint1.length);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void saveGlTexture(String name, int textureId, int mipmapLevels, int width, int height) {
/* 521 */     bindTexture(textureId);
/* 522 */     GL11.glPixelStorei(3333, 1);
/* 523 */     GL11.glPixelStorei(3317, 1);
/* 524 */     File file1 = new File(name);
/* 525 */     File file2 = file1.getParentFile();
/*     */     
/* 527 */     if (file2 != null)
/*     */     {
/* 529 */       file2.mkdirs();
/*     */     }
/*     */     
/* 532 */     for (int i = 0; i < 16; i++) {
/*     */       
/* 534 */       File file3 = new File(name + "_" + i + ".png");
/* 535 */       file3.delete();
/*     */     } 
/*     */     
/* 538 */     for (int i1 = 0; i1 <= mipmapLevels; i1++) {
/*     */       
/* 540 */       File file4 = new File(name + "_" + i1 + ".png");
/* 541 */       int j = width >> i1;
/* 542 */       int k = height >> i1;
/* 543 */       int l = j * k;
/* 544 */       IntBuffer intbuffer = BufferUtils.createIntBuffer(l);
/* 545 */       int[] aint = new int[l];
/* 546 */       GL11.glGetTexImage(3553, i1, 32993, 33639, intbuffer);
/* 547 */       intbuffer.get(aint);
/* 548 */       BufferedImage bufferedimage = new BufferedImage(j, k, 2);
/* 549 */       bufferedimage.setRGB(0, 0, j, k, aint, 0, j);
/*     */ 
/*     */       
/*     */       try {
/* 553 */         ImageIO.write(bufferedimage, "png", file4);
/* 554 */         Config.dbg("Exported: " + file4);
/*     */       }
/* 556 */       catch (Exception exception) {
/*     */         
/* 558 */         Config.warn("Error writing: " + file4);
/* 559 */         Config.warn("" + exception.getClass().getName() + ": " + exception.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void generateCustomMipmaps(TextureAtlasSprite tas, int mipmaps) {
/* 566 */     int i = tas.getIconWidth();
/* 567 */     int j = tas.getIconHeight();
/*     */     
/* 569 */     if (tas.getFrameCount() < 1) {
/*     */       
/* 571 */       List<int[][]> list = (List)new ArrayList<>();
/* 572 */       int[][] aint = new int[mipmaps + 1][];
/* 573 */       int[] aint1 = new int[i * j];
/* 574 */       aint[0] = aint1;
/* 575 */       list.add(aint);
/* 576 */       tas.setFramesTextureData(list);
/*     */     } 
/*     */     
/* 579 */     List<int[][]> list1 = (List)new ArrayList<>();
/* 580 */     int l = tas.getFrameCount();
/*     */     
/* 582 */     for (int i1 = 0; i1 < l; i1++) {
/*     */       
/* 584 */       int[] aint2 = getFrameData(tas, i1, 0);
/*     */       
/* 586 */       if (aint2 == null || aint2.length < 1)
/*     */       {
/* 588 */         aint2 = new int[i * j];
/*     */       }
/*     */       
/* 591 */       if (aint2.length != i * j) {
/*     */         
/* 593 */         int k = (int)Math.round(Math.sqrt(aint2.length));
/*     */         
/* 595 */         if (k * k != aint2.length) {
/*     */           
/* 597 */           aint2 = new int[1];
/* 598 */           k = 1;
/*     */         } 
/*     */         
/* 601 */         BufferedImage bufferedimage = new BufferedImage(k, k, 2);
/* 602 */         bufferedimage.setRGB(0, 0, k, k, aint2, 0, k);
/* 603 */         BufferedImage bufferedimage1 = scaleImage(bufferedimage, i);
/* 604 */         int[] aint3 = new int[i * j];
/* 605 */         bufferedimage1.getRGB(0, 0, i, j, aint3, 0, i);
/* 606 */         aint2 = aint3;
/*     */       } 
/*     */       
/* 609 */       int[][] aint4 = new int[mipmaps + 1][];
/* 610 */       aint4[0] = aint2;
/* 611 */       list1.add(aint4);
/*     */     } 
/*     */     
/* 614 */     tas.setFramesTextureData(list1);
/* 615 */     tas.generateMipmaps(mipmaps);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] getFrameData(TextureAtlasSprite tas, int frame, int level) {
/* 620 */     List<int[][]> list = tas.getFramesTextureData();
/*     */     
/* 622 */     if (list.size() <= frame)
/*     */     {
/* 624 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 628 */     int[][] aint = list.get(frame);
/*     */     
/* 630 */     if (aint != null && aint.length > level) {
/*     */       
/* 632 */       int[] aint1 = aint[level];
/* 633 */       return aint1;
/*     */     } 
/*     */ 
/*     */     
/* 637 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGLMaximumTextureSize() {
/* 644 */     for (int i = 65536; i > 0; i >>= 1) {
/*     */       
/* 646 */       GlStateManager.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, (IntBuffer)null);
/* 647 */       int j = GL11.glGetError();
/* 648 */       int k = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);
/*     */       
/* 650 */       if (k != 0)
/*     */       {
/* 652 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 656 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\TextureUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */