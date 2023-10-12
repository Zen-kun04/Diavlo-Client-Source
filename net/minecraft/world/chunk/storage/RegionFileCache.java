/*    */ package net.minecraft.world.chunk.storage;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RegionFileCache
/*    */ {
/* 12 */   private static final Map<File, RegionFile> regionsByFilename = Maps.newHashMap();
/*    */ 
/*    */   
/*    */   public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ) {
/* 16 */     File file1 = new File(worldDir, "region");
/* 17 */     File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
/* 18 */     RegionFile regionfile = regionsByFilename.get(file2);
/*    */     
/* 20 */     if (regionfile != null)
/*    */     {
/* 22 */       return regionfile;
/*    */     }
/*    */ 
/*    */     
/* 26 */     if (!file1.exists())
/*    */     {
/* 28 */       file1.mkdirs();
/*    */     }
/*    */     
/* 31 */     if (regionsByFilename.size() >= 256)
/*    */     {
/* 33 */       clearRegionFileReferences();
/*    */     }
/*    */     
/* 36 */     RegionFile regionfile1 = new RegionFile(file2);
/* 37 */     regionsByFilename.put(file2, regionfile1);
/* 38 */     return regionfile1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static synchronized void clearRegionFileReferences() {
/* 44 */     for (RegionFile regionfile : regionsByFilename.values()) {
/*    */ 
/*    */       
/*    */       try {
/* 48 */         if (regionfile != null)
/*    */         {
/* 50 */           regionfile.close();
/*    */         }
/*    */       }
/* 53 */       catch (IOException ioexception) {
/*    */         
/* 55 */         ioexception.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     regionsByFilename.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ) {
/* 64 */     RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
/* 65 */     return regionfile.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
/*    */   }
/*    */ 
/*    */   
/*    */   public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ) {
/* 70 */     RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
/* 71 */     return regionfile.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\storage\RegionFileCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */