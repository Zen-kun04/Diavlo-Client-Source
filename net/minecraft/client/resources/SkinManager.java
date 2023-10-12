/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.InsecureTextureException;
/*     */ import com.mojang.authlib.minecraft.MinecraftProfileTexture;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.IImageBuffer;
/*     */ import net.minecraft.client.renderer.ImageBufferDownload;
/*     */ import net.minecraft.client.renderer.ThreadDownloadImageData;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class SkinManager
/*     */ {
/*  29 */   private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   private final File skinCacheDir;
/*     */   private final MinecraftSessionService sessionService;
/*     */   private final LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> skinCacheLoader;
/*     */   
/*     */   public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService) {
/*  37 */     this.textureManager = textureManagerInstance;
/*  38 */     this.skinCacheDir = skinCacheDirectory;
/*  39 */     this.sessionService = sessionService;
/*  40 */     this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>()
/*     */         {
/*     */           public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(GameProfile p_load_1_) throws Exception
/*     */           {
/*  44 */             return Minecraft.getMinecraft().getSessionService().getTextures(p_load_1_, false);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, MinecraftProfileTexture.Type p_152792_2_) {
/*  51 */     return loadSkin(profileTexture, p_152792_2_, (SkinAvailableCallback)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type p_152789_2_, final SkinAvailableCallback skinAvailableCallback) {
/*  56 */     final ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
/*  57 */     ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);
/*     */     
/*  59 */     if (itextureobject != null) {
/*     */       
/*  61 */       if (skinAvailableCallback != null)
/*     */       {
/*  63 */         skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  68 */       File file1 = new File(this.skinCacheDir, (profileTexture.getHash().length() > 2) ? profileTexture.getHash().substring(0, 2) : "xx");
/*  69 */       File file2 = new File(file1, profileTexture.getHash());
/*  70 */       final ImageBufferDownload iimagebuffer = (p_152789_2_ == MinecraftProfileTexture.Type.SKIN) ? new ImageBufferDownload() : null;
/*  71 */       ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer()
/*     */           {
/*     */             public BufferedImage parseUserSkin(BufferedImage image)
/*     */             {
/*  75 */               if (iimagebuffer != null)
/*     */               {
/*  77 */                 image = iimagebuffer.parseUserSkin(image);
/*     */               }
/*     */               
/*  80 */               return image;
/*     */             }
/*     */             
/*     */             public void skinAvailable() {
/*  84 */               if (iimagebuffer != null)
/*     */               {
/*  86 */                 iimagebuffer.skinAvailable();
/*     */               }
/*     */               
/*  89 */               if (skinAvailableCallback != null)
/*     */               {
/*  91 */                 skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture);
/*     */               }
/*     */             }
/*     */           });
/*  95 */       this.textureManager.loadTexture(resourcelocation, (ITextureObject)threaddownloadimagedata);
/*     */     } 
/*     */     
/*  98 */     return resourcelocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadProfileTextures(final GameProfile profile, final SkinAvailableCallback skinAvailableCallback, final boolean requireSecure) {
/* 103 */     THREAD_POOL.submit(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 107 */             final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Maps.newHashMap();
/*     */ 
/*     */             
/*     */             try {
/* 111 */               map.putAll(SkinManager.this.sessionService.getTextures(profile, requireSecure));
/*     */             }
/* 113 */             catch (InsecureTextureException insecureTextureException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 118 */             if (map.isEmpty() && profile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
/*     */               
/* 120 */               profile.getProperties().clear();
/* 121 */               profile.getProperties().putAll((Multimap)Minecraft.getMinecraft().getProfileProperties());
/* 122 */               map.putAll(SkinManager.this.sessionService.getTextures(profile, false));
/*     */             } 
/*     */             
/* 125 */             Minecraft.getMinecraft().addScheduledTask(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/* 129 */                     if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
/*     */                     {
/* 131 */                       SkinManager.this.loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, skinAvailableCallback);
/*     */                     }
/*     */                     
/* 134 */                     if (map.containsKey(MinecraftProfileTexture.Type.CAPE))
/*     */                     {
/* 136 */                       SkinManager.this.loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, skinAvailableCallback);
/*     */                     }
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile profile) {
/* 146 */     return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)this.skinCacheLoader.getUnchecked(profile);
/*     */   }
/*     */   
/*     */   public static interface SkinAvailableCallback {
/*     */     void skinAvailable(MinecraftProfileTexture.Type param1Type, ResourceLocation param1ResourceLocation, MinecraftProfileTexture param1MinecraftProfileTexture);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\SkinManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */