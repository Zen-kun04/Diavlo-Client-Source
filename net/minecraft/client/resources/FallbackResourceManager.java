/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class FallbackResourceManager
/*     */   implements IResourceManager {
/*  18 */   private static final Logger logger = LogManager.getLogger();
/*  19 */   protected final List<IResourcePack> resourcePacks = Lists.newArrayList();
/*     */   
/*     */   private final IMetadataSerializer frmMetadataSerializer;
/*     */   
/*     */   public FallbackResourceManager(IMetadataSerializer frmMetadataSerializerIn) {
/*  24 */     this.frmMetadataSerializer = frmMetadataSerializerIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addResourcePack(IResourcePack resourcePack) {
/*  29 */     this.resourcePacks.add(resourcePack);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getResourceDomains() {
/*  34 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IResource getResource(ResourceLocation location) throws IOException {
/*  39 */     IResourcePack iresourcepack = null;
/*  40 */     ResourceLocation resourcelocation = getLocationMcmeta(location);
/*     */     
/*  42 */     for (int i = this.resourcePacks.size() - 1; i >= 0; i--) {
/*     */       
/*  44 */       IResourcePack iresourcepack1 = this.resourcePacks.get(i);
/*     */       
/*  46 */       if (iresourcepack == null && iresourcepack1.resourceExists(resourcelocation))
/*     */       {
/*  48 */         iresourcepack = iresourcepack1;
/*     */       }
/*     */       
/*  51 */       if (iresourcepack1.resourceExists(location)) {
/*     */         
/*  53 */         InputStream inputstream = null;
/*     */         
/*  55 */         if (iresourcepack != null)
/*     */         {
/*  57 */           inputstream = getInputStream(resourcelocation, iresourcepack);
/*     */         }
/*     */         
/*  60 */         return new SimpleResource(iresourcepack1.getPackName(), location, getInputStream(location, iresourcepack1), inputstream, this.frmMetadataSerializer);
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     throw new FileNotFoundException(location.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   protected InputStream getInputStream(ResourceLocation location, IResourcePack resourcePack) throws IOException {
/*  69 */     InputStream inputstream = resourcePack.getInputStream(location);
/*  70 */     return logger.isDebugEnabled() ? new InputStreamLeakedResourceLogger(inputstream, location, resourcePack.getPackName()) : inputstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IResource> getAllResources(ResourceLocation location) throws IOException {
/*  75 */     List<IResource> list = Lists.newArrayList();
/*  76 */     ResourceLocation resourcelocation = getLocationMcmeta(location);
/*     */     
/*  78 */     for (IResourcePack iresourcepack : this.resourcePacks) {
/*     */       
/*  80 */       if (iresourcepack.resourceExists(location)) {
/*     */         
/*  82 */         InputStream inputstream = iresourcepack.resourceExists(resourcelocation) ? getInputStream(resourcelocation, iresourcepack) : null;
/*  83 */         list.add(new SimpleResource(iresourcepack.getPackName(), location, getInputStream(location, iresourcepack), inputstream, this.frmMetadataSerializer));
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     if (list.isEmpty())
/*     */     {
/*  89 */       throw new FileNotFoundException(location.toString());
/*     */     }
/*     */ 
/*     */     
/*  93 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static ResourceLocation getLocationMcmeta(ResourceLocation location) {
/*  99 */     return new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".mcmeta");
/*     */   }
/*     */   
/*     */   static class InputStreamLeakedResourceLogger
/*     */     extends InputStream
/*     */   {
/*     */     private final InputStream inputStream;
/*     */     private final String message;
/*     */     private boolean isClosed = false;
/*     */     
/*     */     public InputStreamLeakedResourceLogger(InputStream p_i46093_1_, ResourceLocation location, String resourcePack) {
/* 110 */       this.inputStream = p_i46093_1_;
/* 111 */       ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
/* 112 */       (new Exception()).printStackTrace(new PrintStream(bytearrayoutputstream));
/* 113 */       this.message = "Leaked resource: '" + location + "' loaded from pack: '" + resourcePack + "'\n" + bytearrayoutputstream.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 118 */       this.inputStream.close();
/* 119 */       this.isClosed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void finalize() throws Throwable {
/* 124 */       if (!this.isClosed)
/*     */       {
/* 126 */         FallbackResourceManager.logger.warn(this.message);
/*     */       }
/*     */       
/* 129 */       super.finalize();
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 134 */       return this.inputStream.read();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\FallbackResourceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */