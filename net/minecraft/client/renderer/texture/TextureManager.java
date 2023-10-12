/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.CustomGuis;
/*     */ import net.optifine.EmissiveTextures;
/*     */ import net.optifine.RandomEntities;
/*     */ import net.optifine.shaders.ShadersTex;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class TextureManager
/*     */   implements ITickable, IResourceManagerReloadListener
/*     */ {
/*  28 */   private static final Logger logger = LogManager.getLogger();
/*  29 */   private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps.newHashMap();
/*  30 */   private final List<ITickable> listTickables = Lists.newArrayList();
/*  31 */   private final Map<String, Integer> mapTextureCounters = Maps.newHashMap();
/*     */   
/*     */   private IResourceManager theResourceManager;
/*     */   private ITextureObject boundTexture;
/*     */   private ResourceLocation boundTextureLocation;
/*     */   
/*     */   public TextureManager(IResourceManager resourceManager) {
/*  38 */     this.theResourceManager = resourceManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public void bindTexture(ResourceLocation resource) {
/*  43 */     if (Config.isRandomEntities())
/*     */     {
/*  45 */       resource = RandomEntities.getTextureLocation(resource);
/*     */     }
/*     */     
/*  48 */     if (Config.isCustomGuis())
/*     */     {
/*  50 */       resource = CustomGuis.getTextureLocation(resource);
/*     */     }
/*     */     
/*  53 */     ITextureObject itextureobject = this.mapTextureObjects.get(resource);
/*     */     
/*  55 */     if (EmissiveTextures.isActive())
/*     */     {
/*  57 */       itextureobject = EmissiveTextures.getEmissiveTexture(itextureobject, this.mapTextureObjects);
/*     */     }
/*     */     
/*  60 */     if (itextureobject == null) {
/*     */       
/*  62 */       itextureobject = new SimpleTexture(resource);
/*  63 */       loadTexture(resource, itextureobject);
/*     */     } 
/*     */     
/*  66 */     if (Config.isShaders()) {
/*     */       
/*  68 */       ShadersTex.bindTexture(itextureobject);
/*     */     }
/*     */     else {
/*     */       
/*  72 */       TextureUtil.bindTexture(itextureobject.getGlTextureId());
/*     */     } 
/*     */     
/*  75 */     this.boundTexture = itextureobject;
/*  76 */     this.boundTextureLocation = resource;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
/*  81 */     if (loadTexture(textureLocation, textureObj)) {
/*     */       
/*  83 */       this.listTickables.add(textureObj);
/*  84 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
/*  94 */     boolean flag = true;
/*     */ 
/*     */     
/*     */     try {
/*  98 */       textureObj.loadTexture(this.theResourceManager);
/*     */     }
/* 100 */     catch (IOException ioexception) {
/*     */       
/* 102 */       logger.warn("Failed to load texture: " + textureLocation, ioexception);
/* 103 */       textureObj = TextureUtil.missingTexture;
/* 104 */       this.mapTextureObjects.put(textureLocation, textureObj);
/* 105 */       flag = false;
/*     */     }
/* 107 */     catch (Throwable throwable) {
/*     */       
/* 109 */       final ITextureObject textureObjf = textureObj;
/* 110 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
/* 111 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
/* 112 */       crashreportcategory.addCrashSection("Resource location", textureLocation);
/* 113 */       crashreportcategory.addCrashSectionCallable("Texture object class", new Callable<String>()
/*     */           {
/*     */             public String call() throws Exception
/*     */             {
/* 117 */               return textureObjf.getClass().getName();
/*     */             }
/*     */           });
/* 120 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */     
/* 123 */     this.mapTextureObjects.put(textureLocation, textureObj);
/* 124 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public ITextureObject getTexture(ResourceLocation textureLocation) {
/* 129 */     return this.mapTextureObjects.get(textureLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
/* 134 */     if (name.equals("logo"))
/*     */     {
/* 136 */       texture = Config.getMojangLogoTexture(texture);
/*     */     }
/*     */     
/* 139 */     Integer integer = this.mapTextureCounters.get(name);
/*     */     
/* 141 */     if (integer == null) {
/*     */       
/* 143 */       integer = Integer.valueOf(1);
/*     */     }
/*     */     else {
/*     */       
/* 147 */       integer = Integer.valueOf(integer.intValue() + 1);
/*     */     } 
/*     */     
/* 150 */     this.mapTextureCounters.put(name, integer);
/* 151 */     ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] { name, integer }));
/* 152 */     loadTexture(resourcelocation, texture);
/* 153 */     return resourcelocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 158 */     for (ITickable itickable : this.listTickables)
/*     */     {
/* 160 */       itickable.tick();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteTexture(ResourceLocation textureLocation) {
/* 166 */     ITextureObject itextureobject = getTexture(textureLocation);
/*     */     
/* 168 */     if (itextureobject != null) {
/*     */       
/* 170 */       this.mapTextureObjects.remove(textureLocation);
/* 171 */       TextureUtil.deleteTexture(itextureobject.getGlTextureId());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResourceManagerReload(IResourceManager resourceManager) {
/* 177 */     Config.dbg("*** Reloading textures ***");
/* 178 */     Config.log("Resource packs: " + Config.getResourcePackNames());
/* 179 */     Iterator<ResourceLocation> iterator = this.mapTextureObjects.keySet().iterator();
/*     */     
/* 181 */     while (iterator.hasNext()) {
/*     */       
/* 183 */       ResourceLocation resourcelocation = iterator.next();
/* 184 */       String s = resourcelocation.getResourcePath();
/*     */       
/* 186 */       if (s.startsWith("mcpatcher/") || s.startsWith("optifine/") || EmissiveTextures.isEmissive(resourcelocation)) {
/*     */         
/* 188 */         ITextureObject itextureobject = this.mapTextureObjects.get(resourcelocation);
/*     */         
/* 190 */         if (itextureobject instanceof AbstractTexture) {
/*     */           
/* 192 */           AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
/* 193 */           abstracttexture.deleteGlTexture();
/*     */         } 
/*     */         
/* 196 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     EmissiveTextures.update();
/*     */     
/* 202 */     for (Object o : new HashSet(this.mapTextureObjects.entrySet())) {
/*     */       
/* 204 */       Map.Entry<ResourceLocation, ITextureObject> entry = (Map.Entry<ResourceLocation, ITextureObject>)o;
/* 205 */       loadTexture(entry.getKey(), entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reloadBannerTextures() {
/* 211 */     for (Object o : new HashSet(this.mapTextureObjects.entrySet())) {
/*     */       
/* 213 */       Map.Entry<ResourceLocation, ITextureObject> entry = (Map.Entry<ResourceLocation, ITextureObject>)o;
/* 214 */       ResourceLocation resourcelocation = entry.getKey();
/* 215 */       ITextureObject itextureobject = entry.getValue();
/*     */       
/* 217 */       if (itextureobject instanceof LayeredColorMaskTexture)
/*     */       {
/* 219 */         loadTexture(resourcelocation, itextureobject);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ITextureObject getBoundTexture() {
/* 226 */     return this.boundTexture;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getBoundTextureLocation() {
/* 231 */     return this.boundTextureLocation;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\TextureManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */