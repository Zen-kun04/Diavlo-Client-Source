/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.AnvilConverterException;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.biome.WorldChunkManagerHell;
/*     */ import net.minecraft.world.storage.ISaveHandler;
/*     */ import net.minecraft.world.storage.SaveFormatComparator;
/*     */ import net.minecraft.world.storage.SaveFormatOld;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AnvilSaveConverter extends SaveFormatOld {
/*  30 */   private static final Logger logger = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   public AnvilSaveConverter(File savesDirectoryIn) {
/*  34 */     super(savesDirectoryIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  39 */     return "Anvil";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SaveFormatComparator> getSaveList() throws AnvilConverterException {
/*  44 */     if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
/*     */       
/*  46 */       List<SaveFormatComparator> list = Lists.newArrayList();
/*  47 */       File[] afile = this.savesDirectory.listFiles();
/*     */       
/*  49 */       for (File file1 : afile) {
/*     */         
/*  51 */         if (file1.isDirectory()) {
/*     */           
/*  53 */           String s = file1.getName();
/*  54 */           WorldInfo worldinfo = getWorldInfo(s);
/*     */           
/*  56 */           if (worldinfo != null && (worldinfo.getSaveVersion() == 19132 || worldinfo.getSaveVersion() == 19133)) {
/*     */             
/*  58 */             boolean flag = (worldinfo.getSaveVersion() != getSaveVersion());
/*  59 */             String s1 = worldinfo.getWorldName();
/*     */             
/*  61 */             if (StringUtils.isEmpty(s1))
/*     */             {
/*  63 */               s1 = s;
/*     */             }
/*     */             
/*  66 */             long i = 0L;
/*  67 */             list.add(new SaveFormatComparator(s, s1, worldinfo.getLastTimePlayed(), i, worldinfo.getGameType(), flag, worldinfo.isHardcoreModeEnabled(), worldinfo.areCommandsAllowed()));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  72 */       return list;
/*     */     } 
/*     */ 
/*     */     
/*  76 */     throw new AnvilConverterException("Unable to read or access folder where game worlds are saved!");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSaveVersion() {
/*  82 */     return 19133;
/*     */   }
/*     */ 
/*     */   
/*     */   public void flushCache() {
/*  87 */     RegionFileCache.clearRegionFileReferences();
/*     */   }
/*     */ 
/*     */   
/*     */   public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata) {
/*  92 */     return (ISaveHandler)new AnvilSaveHandler(this.savesDirectory, saveName, storePlayerdata);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConvertible(String saveName) {
/*  97 */     WorldInfo worldinfo = getWorldInfo(saveName);
/*  98 */     return (worldinfo != null && worldinfo.getSaveVersion() == 19132);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOldMapFormat(String saveName) {
/* 103 */     WorldInfo worldinfo = getWorldInfo(saveName);
/* 104 */     return (worldinfo != null && worldinfo.getSaveVersion() != getSaveVersion());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean convertMapFormat(String filename, IProgressUpdate progressCallback) {
/* 109 */     progressCallback.setLoadingProgress(0);
/* 110 */     List<File> list = Lists.newArrayList();
/* 111 */     List<File> list1 = Lists.newArrayList();
/* 112 */     List<File> list2 = Lists.newArrayList();
/* 113 */     File file1 = new File(this.savesDirectory, filename);
/* 114 */     File file2 = new File(file1, "DIM-1");
/* 115 */     File file3 = new File(file1, "DIM1");
/* 116 */     logger.info("Scanning folders...");
/* 117 */     addRegionFilesToCollection(file1, list);
/*     */     
/* 119 */     if (file2.exists())
/*     */     {
/* 121 */       addRegionFilesToCollection(file2, list1);
/*     */     }
/*     */     
/* 124 */     if (file3.exists())
/*     */     {
/* 126 */       addRegionFilesToCollection(file3, list2);
/*     */     }
/*     */     
/* 129 */     int i = list.size() + list1.size() + list2.size();
/* 130 */     logger.info("Total conversion count is " + i);
/* 131 */     WorldInfo worldinfo = getWorldInfo(filename);
/* 132 */     WorldChunkManager worldchunkmanager = null;
/*     */     
/* 134 */     if (worldinfo.getTerrainType() == WorldType.FLAT) {
/*     */       
/* 136 */       WorldChunkManagerHell worldChunkManagerHell = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F);
/*     */     }
/*     */     else {
/*     */       
/* 140 */       worldchunkmanager = new WorldChunkManager(worldinfo.getSeed(), worldinfo.getTerrainType(), worldinfo.getGeneratorOptions());
/*     */     } 
/*     */     
/* 143 */     convertFile(new File(file1, "region"), list, worldchunkmanager, 0, i, progressCallback);
/* 144 */     convertFile(new File(file2, "region"), list1, (WorldChunkManager)new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F), list.size(), i, progressCallback);
/* 145 */     convertFile(new File(file3, "region"), list2, (WorldChunkManager)new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F), list.size() + list1.size(), i, progressCallback);
/* 146 */     worldinfo.setSaveVersion(19133);
/*     */     
/* 148 */     if (worldinfo.getTerrainType() == WorldType.DEFAULT_1_1)
/*     */     {
/* 150 */       worldinfo.setTerrainType(WorldType.DEFAULT);
/*     */     }
/*     */     
/* 153 */     createFile(filename);
/* 154 */     ISaveHandler isavehandler = getSaveLoader(filename, false);
/* 155 */     isavehandler.saveWorldInfo(worldinfo);
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void createFile(String filename) {
/* 161 */     File file1 = new File(this.savesDirectory, filename);
/*     */     
/* 163 */     if (!file1.exists()) {
/*     */       
/* 165 */       logger.warn("Unable to create level.dat_mcr backup");
/*     */     }
/*     */     else {
/*     */       
/* 169 */       File file2 = new File(file1, "level.dat");
/*     */       
/* 171 */       if (!file2.exists()) {
/*     */         
/* 173 */         logger.warn("Unable to create level.dat_mcr backup");
/*     */       }
/*     */       else {
/*     */         
/* 177 */         File file3 = new File(file1, "level.dat_mcr");
/*     */         
/* 179 */         if (!file2.renameTo(file3))
/*     */         {
/* 181 */           logger.warn("Unable to create level.dat_mcr backup");
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void convertFile(File p_75813_1_, Iterable<File> p_75813_2_, WorldChunkManager p_75813_3_, int p_75813_4_, int p_75813_5_, IProgressUpdate p_75813_6_) {
/* 189 */     for (File file1 : p_75813_2_) {
/*     */       
/* 191 */       convertChunks(p_75813_1_, file1, p_75813_3_, p_75813_4_, p_75813_5_, p_75813_6_);
/* 192 */       p_75813_4_++;
/* 193 */       int i = (int)Math.round(100.0D * p_75813_4_ / p_75813_5_);
/* 194 */       p_75813_6_.setLoadingProgress(i);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void convertChunks(File p_75811_1_, File p_75811_2_, WorldChunkManager p_75811_3_, int p_75811_4_, int p_75811_5_, IProgressUpdate progressCallback) {
/*     */     try {
/* 202 */       String s = p_75811_2_.getName();
/* 203 */       RegionFile regionfile = new RegionFile(p_75811_2_);
/* 204 */       RegionFile regionfile1 = new RegionFile(new File(p_75811_1_, s.substring(0, s.length() - ".mcr".length()) + ".mca"));
/*     */       
/* 206 */       for (int i = 0; i < 32; i++) {
/*     */         
/* 208 */         for (int j = 0; j < 32; j++) {
/*     */           
/* 210 */           if (regionfile.isChunkSaved(i, j) && !regionfile1.isChunkSaved(i, j)) {
/*     */             
/* 212 */             DataInputStream datainputstream = regionfile.getChunkDataInputStream(i, j);
/*     */             
/* 214 */             if (datainputstream == null) {
/*     */               
/* 216 */               logger.warn("Failed to fetch input stream");
/*     */             }
/*     */             else {
/*     */               
/* 220 */               NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
/* 221 */               datainputstream.close();
/* 222 */               NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
/* 223 */               ChunkLoader.AnvilConverterData chunkloader$anvilconverterdata = ChunkLoader.load(nbttagcompound1);
/* 224 */               NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/* 225 */               NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/* 226 */               nbttagcompound2.setTag("Level", (NBTBase)nbttagcompound3);
/* 227 */               ChunkLoader.convertToAnvilFormat(chunkloader$anvilconverterdata, nbttagcompound3, p_75811_3_);
/* 228 */               DataOutputStream dataoutputstream = regionfile1.getChunkDataOutputStream(i, j);
/* 229 */               CompressedStreamTools.write(nbttagcompound2, dataoutputstream);
/* 230 */               dataoutputstream.close();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 235 */         int k = (int)Math.round(100.0D * (p_75811_4_ * 1024) / (p_75811_5_ * 1024));
/* 236 */         int l = (int)Math.round(100.0D * ((i + 1) * 32 + p_75811_4_ * 1024) / (p_75811_5_ * 1024));
/*     */         
/* 238 */         if (l > k)
/*     */         {
/* 240 */           progressCallback.setLoadingProgress(l);
/*     */         }
/*     */       } 
/*     */       
/* 244 */       regionfile.close();
/* 245 */       regionfile1.close();
/*     */     }
/* 247 */     catch (IOException ioexception) {
/*     */       
/* 249 */       ioexception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addRegionFilesToCollection(File worldDir, Collection<File> collection) {
/* 255 */     File file1 = new File(worldDir, "region");
/* 256 */     File[] afile = file1.listFiles(new FilenameFilter()
/*     */         {
/*     */           public boolean accept(File p_accept_1_, String p_accept_2_)
/*     */           {
/* 260 */             return p_accept_2_.endsWith(".mcr");
/*     */           }
/*     */         });
/*     */     
/* 264 */     if (afile != null)
/*     */     {
/* 266 */       Collections.addAll(collection, afile);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\storage\AnvilSaveConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */