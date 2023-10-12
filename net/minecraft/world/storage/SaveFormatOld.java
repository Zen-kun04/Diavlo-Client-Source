/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.AnvilConverterException;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SaveFormatOld
/*     */   implements ISaveFormat {
/*  17 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   protected final File savesDirectory;
/*     */   
/*     */   public SaveFormatOld(File savesDirectoryIn) {
/*  22 */     if (!savesDirectoryIn.exists())
/*     */     {
/*  24 */       savesDirectoryIn.mkdirs();
/*     */     }
/*     */     
/*  27 */     this.savesDirectory = savesDirectoryIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  32 */     return "Old Format";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SaveFormatComparator> getSaveList() throws AnvilConverterException {
/*  37 */     List<SaveFormatComparator> list = Lists.newArrayList();
/*     */     
/*  39 */     for (int i = 0; i < 5; i++) {
/*     */       
/*  41 */       String s = "World" + (i + 1);
/*  42 */       WorldInfo worldinfo = getWorldInfo(s);
/*     */       
/*  44 */       if (worldinfo != null)
/*     */       {
/*  46 */         list.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), worldinfo.getGameType(), false, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushCache() {}
/*     */ 
/*     */   
/*     */   public WorldInfo getWorldInfo(String saveName) {
/*  59 */     File file1 = new File(this.savesDirectory, saveName);
/*     */     
/*  61 */     if (!file1.exists())
/*     */     {
/*  63 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  67 */     File file2 = new File(file1, "level.dat");
/*     */     
/*  69 */     if (file2.exists()) {
/*     */       
/*     */       try {
/*     */         
/*  73 */         NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file2));
/*  74 */         NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
/*  75 */         return new WorldInfo(nbttagcompound3);
/*     */       }
/*  77 */       catch (Exception exception1) {
/*     */         
/*  79 */         logger.error("Exception reading " + file2, exception1);
/*     */       } 
/*     */     }
/*     */     
/*  83 */     file2 = new File(file1, "level.dat_old");
/*     */     
/*  85 */     if (file2.exists()) {
/*     */       
/*     */       try {
/*     */         
/*  89 */         NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
/*  90 */         NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
/*  91 */         return new WorldInfo(nbttagcompound1);
/*     */       }
/*  93 */       catch (Exception exception) {
/*     */         
/*  95 */         logger.error("Exception reading " + file2, exception);
/*     */       } 
/*     */     }
/*     */     
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renameWorld(String dirName, String newName) {
/* 105 */     File file1 = new File(this.savesDirectory, dirName);
/*     */     
/* 107 */     if (file1.exists()) {
/*     */       
/* 109 */       File file2 = new File(file1, "level.dat");
/*     */       
/* 111 */       if (file2.exists()) {
/*     */         
/*     */         try {
/*     */           
/* 115 */           NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
/* 116 */           NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
/* 117 */           nbttagcompound1.setString("LevelName", newName);
/* 118 */           CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
/*     */         }
/* 120 */         catch (Exception exception) {
/*     */           
/* 122 */           exception.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNewLevelIdAcceptable(String saveName) {
/* 130 */     File file1 = new File(this.savesDirectory, saveName);
/*     */     
/* 132 */     if (file1.exists())
/*     */     {
/* 134 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 140 */       file1.mkdir();
/* 141 */       file1.delete();
/* 142 */       return true;
/*     */     }
/* 144 */     catch (Throwable throwable) {
/*     */       
/* 146 */       logger.warn("Couldn't make new level", throwable);
/* 147 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteWorldDirectory(String saveName) {
/* 154 */     File file1 = new File(this.savesDirectory, saveName);
/*     */     
/* 156 */     if (!file1.exists())
/*     */     {
/* 158 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 162 */     logger.info("Deleting level " + saveName);
/*     */     
/* 164 */     for (int i = 1; i <= 5; i++) {
/*     */       
/* 166 */       logger.info("Attempt " + i + "...");
/*     */       
/* 168 */       if (deleteFiles(file1.listFiles())) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 173 */       logger.warn("Unsuccessful in deleting contents.");
/*     */       
/* 175 */       if (i < 5) {
/*     */         
/*     */         try {
/*     */           
/* 179 */           Thread.sleep(500L);
/*     */         }
/* 181 */         catch (InterruptedException interruptedException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     return file1.delete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean deleteFiles(File[] files) {
/* 194 */     for (int i = 0; i < files.length; i++) {
/*     */       
/* 196 */       File file1 = files[i];
/* 197 */       logger.debug("Deleting " + file1);
/*     */       
/* 199 */       if (file1.isDirectory() && !deleteFiles(file1.listFiles())) {
/*     */         
/* 201 */         logger.warn("Couldn't delete directory " + file1);
/* 202 */         return false;
/*     */       } 
/*     */       
/* 205 */       if (!file1.delete()) {
/*     */         
/* 207 */         logger.warn("Couldn't delete file " + file1);
/* 208 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata) {
/* 217 */     return new SaveHandler(this.savesDirectory, saveName, storePlayerdata);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConvertible(String saveName) {
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOldMapFormat(String saveName) {
/* 227 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean convertMapFormat(String filename, IProgressUpdate progressCallback) {
/* 232 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canLoadWorld(String saveName) {
/* 237 */     File file1 = new File(this.savesDirectory, saveName);
/* 238 */     return file1.isDirectory();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\SaveFormatOld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */