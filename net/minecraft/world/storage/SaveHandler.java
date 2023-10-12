/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.MinecraftException;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SaveHandler implements ISaveHandler, IPlayerFileData {
/*  21 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final File worldDirectory;
/*     */   private final File playersDirectory;
/*     */   private final File mapDataDir;
/*  25 */   private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
/*     */   
/*     */   private final String saveDirectoryName;
/*     */   
/*     */   public SaveHandler(File savesDirectory, String directoryName, boolean playersDirectoryIn) {
/*  30 */     this.worldDirectory = new File(savesDirectory, directoryName);
/*  31 */     this.worldDirectory.mkdirs();
/*  32 */     this.playersDirectory = new File(this.worldDirectory, "playerdata");
/*  33 */     this.mapDataDir = new File(this.worldDirectory, "data");
/*  34 */     this.mapDataDir.mkdirs();
/*  35 */     this.saveDirectoryName = directoryName;
/*     */     
/*  37 */     if (playersDirectoryIn)
/*     */     {
/*  39 */       this.playersDirectory.mkdirs();
/*     */     }
/*     */     
/*  42 */     setSessionLock();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setSessionLock() {
/*     */     try {
/*  49 */       File file1 = new File(this.worldDirectory, "session.lock");
/*  50 */       DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
/*     */ 
/*     */       
/*     */       try {
/*  54 */         dataoutputstream.writeLong(this.initializationTime);
/*     */       }
/*     */       finally {
/*     */         
/*  58 */         dataoutputstream.close();
/*     */       }
/*     */     
/*  61 */     } catch (IOException ioexception) {
/*     */       
/*  63 */       ioexception.printStackTrace();
/*  64 */       throw new RuntimeException("Failed to check session lock, aborting");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public File getWorldDirectory() {
/*  70 */     return this.worldDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkSessionLock() throws MinecraftException {
/*     */     try {
/*  77 */       File file1 = new File(this.worldDirectory, "session.lock");
/*  78 */       DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
/*     */ 
/*     */       
/*     */       try {
/*  82 */         if (datainputstream.readLong() != this.initializationTime)
/*     */         {
/*  84 */           throw new MinecraftException("The save is being accessed from another location, aborting");
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/*  89 */         datainputstream.close();
/*     */       }
/*     */     
/*  92 */     } catch (IOException var7) {
/*     */       
/*  94 */       throw new MinecraftException("Failed to check session lock, aborting");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IChunkLoader getChunkLoader(WorldProvider provider) {
/* 100 */     throw new RuntimeException("Old Chunk Storage is no longer supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldInfo loadWorldInfo() {
/* 105 */     File file1 = new File(this.worldDirectory, "level.dat");
/*     */     
/* 107 */     if (file1.exists()) {
/*     */       
/*     */       try {
/*     */         
/* 111 */         NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file1));
/* 112 */         NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
/* 113 */         return new WorldInfo(nbttagcompound3);
/*     */       }
/* 115 */       catch (Exception exception1) {
/*     */         
/* 117 */         exception1.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 121 */     file1 = new File(this.worldDirectory, "level.dat_old");
/*     */     
/* 123 */     if (file1.exists()) {
/*     */       
/*     */       try {
/*     */         
/* 127 */         NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
/* 128 */         NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
/* 129 */         return new WorldInfo(nbttagcompound1);
/*     */       }
/* 131 */       catch (Exception exception) {
/*     */         
/* 133 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {
/* 142 */     NBTTagCompound nbttagcompound = worldInformation.cloneNBTCompound(tagCompound);
/* 143 */     NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 144 */     nbttagcompound1.setTag("Data", (NBTBase)nbttagcompound);
/*     */ 
/*     */     
/*     */     try {
/* 148 */       File file1 = new File(this.worldDirectory, "level.dat_new");
/* 149 */       File file2 = new File(this.worldDirectory, "level.dat_old");
/* 150 */       File file3 = new File(this.worldDirectory, "level.dat");
/* 151 */       CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));
/*     */       
/* 153 */       if (file2.exists())
/*     */       {
/* 155 */         file2.delete();
/*     */       }
/*     */       
/* 158 */       file3.renameTo(file2);
/*     */       
/* 160 */       if (file3.exists())
/*     */       {
/* 162 */         file3.delete();
/*     */       }
/*     */       
/* 165 */       file1.renameTo(file3);
/*     */       
/* 167 */       if (file1.exists())
/*     */       {
/* 169 */         file1.delete();
/*     */       }
/*     */     }
/* 172 */     catch (Exception exception) {
/*     */       
/* 174 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveWorldInfo(WorldInfo worldInformation) {
/* 180 */     NBTTagCompound nbttagcompound = worldInformation.getNBTTagCompound();
/* 181 */     NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 182 */     nbttagcompound1.setTag("Data", (NBTBase)nbttagcompound);
/*     */ 
/*     */     
/*     */     try {
/* 186 */       File file1 = new File(this.worldDirectory, "level.dat_new");
/* 187 */       File file2 = new File(this.worldDirectory, "level.dat_old");
/* 188 */       File file3 = new File(this.worldDirectory, "level.dat");
/* 189 */       CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));
/*     */       
/* 191 */       if (file2.exists())
/*     */       {
/* 193 */         file2.delete();
/*     */       }
/*     */       
/* 196 */       file3.renameTo(file2);
/*     */       
/* 198 */       if (file3.exists())
/*     */       {
/* 200 */         file3.delete();
/*     */       }
/*     */       
/* 203 */       file1.renameTo(file3);
/*     */       
/* 205 */       if (file1.exists())
/*     */       {
/* 207 */         file1.delete();
/*     */       }
/*     */     }
/* 210 */     catch (Exception exception) {
/*     */       
/* 212 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writePlayerData(EntityPlayer player) {
/*     */     try {
/* 220 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 221 */       player.writeToNBT(nbttagcompound);
/* 222 */       File file1 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat.tmp");
/* 223 */       File file2 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat");
/* 224 */       CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));
/*     */       
/* 226 */       if (file2.exists())
/*     */       {
/* 228 */         file2.delete();
/*     */       }
/*     */       
/* 231 */       file1.renameTo(file2);
/*     */     }
/* 233 */     catch (Exception var5) {
/*     */       
/* 235 */       logger.warn("Failed to save player data for " + player.getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound readPlayerData(EntityPlayer player) {
/* 241 */     NBTTagCompound nbttagcompound = null;
/*     */ 
/*     */     
/*     */     try {
/* 245 */       File file1 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat");
/*     */       
/* 247 */       if (file1.exists() && file1.isFile())
/*     */       {
/* 249 */         nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
/*     */       }
/*     */     }
/* 252 */     catch (Exception var4) {
/*     */       
/* 254 */       logger.warn("Failed to load player data for " + player.getName());
/*     */     } 
/*     */     
/* 257 */     if (nbttagcompound != null)
/*     */     {
/* 259 */       player.readFromNBT(nbttagcompound);
/*     */     }
/*     */     
/* 262 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public IPlayerFileData getPlayerNBTManager() {
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getAvailablePlayerDat() {
/* 272 */     String[] astring = this.playersDirectory.list();
/*     */     
/* 274 */     if (astring == null)
/*     */     {
/* 276 */       astring = new String[0];
/*     */     }
/*     */     
/* 279 */     for (int i = 0; i < astring.length; i++) {
/*     */       
/* 281 */       if (astring[i].endsWith(".dat"))
/*     */       {
/* 283 */         astring[i] = astring[i].substring(0, astring[i].length() - 4);
/*     */       }
/*     */     } 
/*     */     
/* 287 */     return astring;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {}
/*     */ 
/*     */   
/*     */   public File getMapFileFromName(String mapName) {
/* 296 */     return new File(this.mapDataDir, mapName + ".dat");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWorldDirectoryName() {
/* 301 */     return this.saveDirectoryName;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\SaveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */