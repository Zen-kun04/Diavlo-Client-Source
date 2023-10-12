/*     */ package net.minecraft.client.resources;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.common.util.concurrent.FutureCallback;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import com.google.common.util.concurrent.ListenableFuture;
/*     */ import com.google.common.util.concurrent.SettableFuture;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiScreenWorking;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*     */ import net.minecraft.client.resources.data.PackMetadataSection;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.comparator.LastModifiedFileComparator;
/*     */ import org.apache.commons.io.filefilter.IOFileFilter;
/*     */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ResourcePackRepository {
/*  42 */   private static final Logger logger = LogManager.getLogger();
/*  43 */   private static final FileFilter resourcePackFilter = new FileFilter()
/*     */     {
/*     */       public boolean accept(File p_accept_1_)
/*     */       {
/*  47 */         boolean flag = (p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip"));
/*  48 */         boolean flag1 = (p_accept_1_.isDirectory() && (new File(p_accept_1_, "pack.mcmeta")).isFile());
/*  49 */         return (flag || flag1);
/*     */       }
/*     */     };
/*     */   private final File dirResourcepacks;
/*     */   public final IResourcePack rprDefaultResourcePack;
/*     */   private final File dirServerResourcepacks;
/*     */   public final IMetadataSerializer rprMetadataSerializer;
/*     */   private IResourcePack resourcePackInstance;
/*  57 */   private final ReentrantLock lock = new ReentrantLock();
/*     */   private ListenableFuture<Object> downloadingPacks;
/*  59 */   private List<Entry> repositoryEntriesAll = Lists.newArrayList();
/*  60 */   public List<Entry> repositoryEntries = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   public ResourcePackRepository(File dirResourcepacksIn, File dirServerResourcepacksIn, IResourcePack rprDefaultResourcePackIn, IMetadataSerializer rprMetadataSerializerIn, GameSettings settings) {
/*  64 */     this.dirResourcepacks = dirResourcepacksIn;
/*  65 */     this.dirServerResourcepacks = dirServerResourcepacksIn;
/*  66 */     this.rprDefaultResourcePack = rprDefaultResourcePackIn;
/*  67 */     this.rprMetadataSerializer = rprMetadataSerializerIn;
/*  68 */     fixDirResourcepacks();
/*  69 */     updateRepositoryEntriesAll();
/*  70 */     Iterator<String> iterator = settings.resourcePacks.iterator();
/*     */     
/*  72 */     while (iterator.hasNext()) {
/*     */       
/*  74 */       String s = iterator.next();
/*     */       
/*  76 */       for (Entry resourcepackrepository$entry : this.repositoryEntriesAll) {
/*     */         
/*  78 */         if (resourcepackrepository$entry.getResourcePackName().equals(s)) {
/*     */           
/*  80 */           if (resourcepackrepository$entry.func_183027_f() == 1 || settings.incompatibleResourcePacks.contains(resourcepackrepository$entry.getResourcePackName())) {
/*     */             
/*  82 */             this.repositoryEntries.add(resourcepackrepository$entry);
/*     */             
/*     */             break;
/*     */           } 
/*  86 */           iterator.remove();
/*  87 */           logger.warn("Removed selected resource pack {} because it's no longer compatible", new Object[] { resourcepackrepository$entry.getResourcePackName() });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixDirResourcepacks() {
/*  95 */     if (this.dirResourcepacks.exists()) {
/*     */       
/*  97 */       if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs()))
/*     */       {
/*  99 */         logger.warn("Unable to recreate resourcepack folder, it exists but is not a directory: " + this.dirResourcepacks);
/*     */       }
/*     */     }
/* 102 */     else if (!this.dirResourcepacks.mkdirs()) {
/*     */       
/* 104 */       logger.warn("Unable to create resourcepack folder: " + this.dirResourcepacks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<File> getResourcePackFiles() {
/* 110 */     return this.dirResourcepacks.isDirectory() ? Arrays.<File>asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.<File>emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRepositoryEntriesAll() {
/* 115 */     List<Entry> list = Lists.newArrayList();
/*     */     
/* 117 */     for (File file1 : getResourcePackFiles()) {
/*     */       
/* 119 */       Entry resourcepackrepository$entry = new Entry(file1);
/*     */       
/* 121 */       if (!this.repositoryEntriesAll.contains(resourcepackrepository$entry)) {
/*     */ 
/*     */         
/*     */         try {
/* 125 */           resourcepackrepository$entry.updateResourcePack();
/* 126 */           list.add(resourcepackrepository$entry);
/*     */         }
/* 128 */         catch (Exception var61) {
/*     */           
/* 130 */           list.remove(resourcepackrepository$entry);
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 135 */       int i = this.repositoryEntriesAll.indexOf(resourcepackrepository$entry);
/*     */       
/* 137 */       if (i > -1 && i < this.repositoryEntriesAll.size())
/*     */       {
/* 139 */         list.add(this.repositoryEntriesAll.get(i));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 144 */     this.repositoryEntriesAll.removeAll(list);
/*     */     
/* 146 */     for (Entry resourcepackrepository$entry1 : this.repositoryEntriesAll)
/*     */     {
/* 148 */       resourcepackrepository$entry1.closeResourcePack();
/*     */     }
/*     */     
/* 151 */     this.repositoryEntriesAll = list;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entry> getRepositoryEntriesAll() {
/* 156 */     return (List<Entry>)ImmutableList.copyOf(this.repositoryEntriesAll);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entry> getRepositoryEntries() {
/* 161 */     return (List<Entry>)ImmutableList.copyOf(this.repositoryEntries);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRepositories(List<Entry> repositories) {
/* 166 */     this.repositoryEntries.clear();
/* 167 */     this.repositoryEntries.addAll(repositories);
/*     */   }
/*     */ 
/*     */   
/*     */   public File getDirResourcepacks() {
/* 172 */     return this.dirResourcepacks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListenableFuture<Object> downloadResourcePack(String url, String hash) {
/*     */     String s;
/* 179 */     if (hash.matches("^[a-f0-9]{40}$")) {
/*     */       
/* 181 */       s = hash;
/*     */     }
/*     */     else {
/*     */       
/* 185 */       s = "legacy";
/*     */     } 
/*     */     
/* 188 */     final File file1 = new File(this.dirServerResourcepacks, s);
/* 189 */     this.lock.lock();
/*     */ 
/*     */     
/*     */     try {
/* 193 */       clearResourcePack();
/*     */       
/* 195 */       if (file1.exists() && hash.length() == 40) {
/*     */         
/*     */         try {
/*     */           
/* 199 */           String s1 = Hashing.sha1().hashBytes(Files.toByteArray(file1)).toString();
/*     */           
/* 201 */           if (s1.equals(hash)) {
/*     */             
/* 203 */             ListenableFuture<Object> listenablefuture2 = setResourcePackInstance(file1);
/* 204 */             ListenableFuture<Object> listenablefuture3 = listenablefuture2;
/* 205 */             return listenablefuture3;
/*     */           } 
/*     */           
/* 208 */           logger.warn("File " + file1 + " had wrong hash (expected " + hash + ", found " + s1 + "). Deleting it.");
/* 209 */           FileUtils.deleteQuietly(file1);
/*     */         }
/* 211 */         catch (IOException ioexception) {
/*     */           
/* 213 */           logger.warn("File " + file1 + " couldn't be hashed. Deleting it.", ioexception);
/* 214 */           FileUtils.deleteQuietly(file1);
/*     */         } 
/*     */       }
/*     */       
/* 218 */       deleteOldServerResourcesPacks();
/* 219 */       final GuiScreenWorking guiscreenworking = new GuiScreenWorking();
/* 220 */       Map<String, String> map = Minecraft.getSessionInfo();
/* 221 */       final Minecraft minecraft = Minecraft.getMinecraft();
/* 222 */       Futures.getUnchecked((Future)minecraft.addScheduledTask(new Runnable()
/*     */             {
/*     */               public void run()
/*     */               {
/* 226 */                 minecraft.displayGuiScreen((GuiScreen)guiscreenworking);
/*     */               }
/*     */             }));
/* 229 */       final SettableFuture<Object> settablefuture = SettableFuture.create();
/* 230 */       this.downloadingPacks = HttpUtil.downloadResourcePack(file1, url, map, 52428800, (IProgressUpdate)guiscreenworking, minecraft.getProxy());
/* 231 */       Futures.addCallback(this.downloadingPacks, new FutureCallback<Object>()
/*     */           {
/*     */             public void onSuccess(Object p_onSuccess_1_)
/*     */             {
/* 235 */               ResourcePackRepository.this.setResourcePackInstance(file1);
/* 236 */               settablefuture.set(null);
/*     */             }
/*     */             
/*     */             public void onFailure(Throwable p_onFailure_1_) {
/* 240 */               settablefuture.setException(p_onFailure_1_);
/*     */             }
/*     */           });
/* 243 */       ListenableFuture<Object> listenablefuture = this.downloadingPacks;
/* 244 */       ListenableFuture<Object> listenablefuture11 = listenablefuture;
/* 245 */       return listenablefuture11;
/*     */     }
/*     */     finally {
/*     */       
/* 249 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void deleteOldServerResourcesPacks() {
/* 255 */     List<File> list = Lists.newArrayList(FileUtils.listFiles(this.dirServerResourcepacks, TrueFileFilter.TRUE, (IOFileFilter)null));
/* 256 */     Collections.sort(list, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
/* 257 */     int i = 0;
/*     */     
/* 259 */     for (File file1 : list) {
/*     */       
/* 261 */       if (i++ >= 10) {
/*     */         
/* 263 */         logger.info("Deleting old server resource pack " + file1.getName());
/* 264 */         FileUtils.deleteQuietly(file1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ListenableFuture<Object> setResourcePackInstance(File resourceFile) {
/* 271 */     this.resourcePackInstance = new FileResourcePack(resourceFile);
/* 272 */     return Minecraft.getMinecraft().scheduleResourcesRefresh();
/*     */   }
/*     */ 
/*     */   
/*     */   public IResourcePack getResourcePackInstance() {
/* 277 */     return this.resourcePackInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearResourcePack() {
/* 282 */     this.lock.lock();
/*     */ 
/*     */     
/*     */     try {
/* 286 */       if (this.downloadingPacks != null)
/*     */       {
/* 288 */         this.downloadingPacks.cancel(true);
/*     */       }
/*     */       
/* 291 */       this.downloadingPacks = null;
/*     */       
/* 293 */       if (this.resourcePackInstance != null)
/*     */       {
/* 295 */         this.resourcePackInstance = null;
/* 296 */         Minecraft.getMinecraft().scheduleResourcesRefresh();
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 301 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public class Entry
/*     */   {
/*     */     private final File resourcePackFile;
/*     */     private IResourcePack reResourcePack;
/*     */     private PackMetadataSection rePackMetadataSection;
/*     */     private BufferedImage texturePackIcon;
/*     */     private ResourceLocation locationTexturePackIcon;
/*     */     
/*     */     private Entry(File resourcePackFileIn) {
/* 315 */       this.resourcePackFile = resourcePackFileIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateResourcePack() throws IOException {
/* 320 */       this.reResourcePack = this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile);
/* 321 */       this.rePackMetadataSection = this.reResourcePack.<PackMetadataSection>getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");
/*     */ 
/*     */       
/*     */       try {
/* 325 */         this.texturePackIcon = this.reResourcePack.getPackImage();
/*     */       }
/* 327 */       catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 332 */       if (this.texturePackIcon == null)
/*     */       {
/* 334 */         this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
/*     */       }
/*     */       
/* 337 */       closeResourcePack();
/*     */     }
/*     */ 
/*     */     
/*     */     public void bindTexturePackIcon(TextureManager textureManagerIn) {
/* 342 */       if (this.locationTexturePackIcon == null)
/*     */       {
/* 344 */         this.locationTexturePackIcon = textureManagerIn.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
/*     */       }
/*     */       
/* 347 */       textureManagerIn.bindTexture(this.locationTexturePackIcon);
/*     */     }
/*     */ 
/*     */     
/*     */     public void closeResourcePack() {
/* 352 */       if (this.reResourcePack instanceof Closeable)
/*     */       {
/* 354 */         IOUtils.closeQuietly((Closeable)this.reResourcePack);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public IResourcePack getResourcePack() {
/* 360 */       return this.reResourcePack;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getResourcePackName() {
/* 365 */       return this.reResourcePack.getPackName();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getTexturePackDescription() {
/* 370 */       return (this.rePackMetadataSection == null) ? (EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing 'pack' section)") : this.rePackMetadataSection.getPackDescription().getFormattedText();
/*     */     }
/*     */ 
/*     */     
/*     */     public int func_183027_f() {
/* 375 */       return this.rePackMetadataSection.getPackFormat();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object p_equals_1_) {
/* 380 */       return (this == p_equals_1_) ? true : ((p_equals_1_ instanceof Entry) ? toString().equals(p_equals_1_.toString()) : false);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 385 */       return toString().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 390 */       return String.format("%s:%s:%d", new Object[] { this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", Long.valueOf(this.resourcePackFile.lastModified()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\ResourcePackRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */