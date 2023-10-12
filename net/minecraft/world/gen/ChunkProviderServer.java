/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.LongHashMap;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.MinecraftException;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.EmptyChunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ChunkProviderServer
/*     */   implements IChunkProvider {
/*  30 */   private static final Logger logger = LogManager.getLogger();
/*  31 */   private Set<Long> droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
/*     */   private Chunk dummyChunk;
/*     */   private IChunkProvider serverChunkGenerator;
/*     */   private IChunkLoader chunkLoader;
/*     */   public boolean chunkLoadOverride = true;
/*  36 */   private LongHashMap<Chunk> id2ChunkMap = new LongHashMap();
/*  37 */   private List<Chunk> loadedChunks = Lists.newArrayList();
/*     */   
/*     */   private WorldServer worldObj;
/*     */   
/*     */   public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_, IChunkProvider p_i1520_3_) {
/*  42 */     this.dummyChunk = (Chunk)new EmptyChunk((World)p_i1520_1_, 0, 0);
/*  43 */     this.worldObj = p_i1520_1_;
/*  44 */     this.chunkLoader = p_i1520_2_;
/*  45 */     this.serverChunkGenerator = p_i1520_3_;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean chunkExists(int x, int z) {
/*  50 */     return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Chunk> func_152380_a() {
/*  55 */     return this.loadedChunks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropChunk(int x, int z) {
/*  60 */     if (this.worldObj.provider.canRespawnHere()) {
/*     */       
/*  62 */       if (!this.worldObj.isSpawnChunk(x, z))
/*     */       {
/*  64 */         this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(x, z)));
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  69 */       this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(x, z)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unloadAllChunks() {
/*  75 */     for (Chunk chunk : this.loadedChunks)
/*     */     {
/*  77 */       dropChunk(chunk.xPosition, chunk.zPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk loadChunk(int chunkX, int chunkZ) {
/*  83 */     long i = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
/*  84 */     this.droppedChunksSet.remove(Long.valueOf(i));
/*  85 */     Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(i);
/*     */     
/*  87 */     if (chunk == null) {
/*     */       
/*  89 */       chunk = loadChunkFromFile(chunkX, chunkZ);
/*     */       
/*  91 */       if (chunk == null)
/*     */       {
/*  93 */         if (this.serverChunkGenerator == null) {
/*     */           
/*  95 */           chunk = this.dummyChunk;
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 101 */             chunk = this.serverChunkGenerator.provideChunk(chunkX, chunkZ);
/*     */           }
/* 103 */           catch (Throwable throwable) {
/*     */             
/* 105 */             CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
/* 106 */             CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
/* 107 */             crashreportcategory.addCrashSection("Location", String.format("%d,%d", new Object[] { Integer.valueOf(chunkX), Integer.valueOf(chunkZ) }));
/* 108 */             crashreportcategory.addCrashSection("Position hash", Long.valueOf(i));
/* 109 */             crashreportcategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
/* 110 */             throw new ReportedException(crashreport);
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 115 */       this.id2ChunkMap.add(i, chunk);
/* 116 */       this.loadedChunks.add(chunk);
/* 117 */       chunk.onChunkLoad();
/* 118 */       chunk.populateChunk(this, this, chunkX, chunkZ);
/*     */     } 
/*     */     
/* 121 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(int x, int z) {
/* 126 */     Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
/* 127 */     return (chunk == null) ? ((!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride) ? this.dummyChunk : loadChunk(x, z)) : chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   private Chunk loadChunkFromFile(int x, int z) {
/* 132 */     if (this.chunkLoader == null)
/*     */     {
/* 134 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 140 */       Chunk chunk = this.chunkLoader.loadChunk((World)this.worldObj, x, z);
/*     */       
/* 142 */       if (chunk != null) {
/*     */         
/* 144 */         chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
/*     */         
/* 146 */         if (this.serverChunkGenerator != null)
/*     */         {
/* 148 */           this.serverChunkGenerator.recreateStructures(chunk, x, z);
/*     */         }
/*     */       } 
/*     */       
/* 152 */       return chunk;
/*     */     }
/* 154 */     catch (Exception exception) {
/*     */       
/* 156 */       logger.error("Couldn't load chunk", exception);
/* 157 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveChunkExtraData(Chunk chunkIn) {
/* 164 */     if (this.chunkLoader != null) {
/*     */       
/*     */       try {
/*     */         
/* 168 */         this.chunkLoader.saveExtraChunkData((World)this.worldObj, chunkIn);
/*     */       }
/* 170 */       catch (Exception exception) {
/*     */         
/* 172 */         logger.error("Couldn't save entities", exception);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveChunkData(Chunk chunkIn) {
/* 179 */     if (this.chunkLoader != null) {
/*     */       
/*     */       try {
/*     */         
/* 183 */         chunkIn.setLastSaveTime(this.worldObj.getTotalWorldTime());
/* 184 */         this.chunkLoader.saveChunk((World)this.worldObj, chunkIn);
/*     */       }
/* 186 */       catch (IOException ioexception) {
/*     */         
/* 188 */         logger.error("Couldn't save chunk", ioexception);
/*     */       }
/* 190 */       catch (MinecraftException minecraftexception) {
/*     */         
/* 192 */         logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)minecraftexception);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void populate(IChunkProvider chunkProvider, int x, int z) {
/* 199 */     Chunk chunk = provideChunk(x, z);
/*     */     
/* 201 */     if (!chunk.isTerrainPopulated()) {
/*     */       
/* 203 */       chunk.func_150809_p();
/*     */       
/* 205 */       if (this.serverChunkGenerator != null) {
/*     */         
/* 207 */         this.serverChunkGenerator.populate(chunkProvider, x, z);
/* 208 */         chunk.setChunkModified();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
/* 215 */     if (this.serverChunkGenerator != null && this.serverChunkGenerator.populateChunk(chunkProvider, chunkIn, x, z)) {
/*     */       
/* 217 */       Chunk chunk = provideChunk(x, z);
/* 218 */       chunk.setChunkModified();
/* 219 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
/* 229 */     int i = 0;
/* 230 */     List<Chunk> list = Lists.newArrayList(this.loadedChunks);
/*     */     
/* 232 */     for (int j = 0; j < list.size(); j++) {
/*     */       
/* 234 */       Chunk chunk = list.get(j);
/*     */       
/* 236 */       if (saveAllChunks)
/*     */       {
/* 238 */         saveChunkExtraData(chunk);
/*     */       }
/*     */       
/* 241 */       if (chunk.needsSaving(saveAllChunks)) {
/*     */         
/* 243 */         saveChunkData(chunk);
/* 244 */         chunk.setModified(false);
/* 245 */         i++;
/*     */         
/* 247 */         if (i == 24 && !saveAllChunks)
/*     */         {
/* 249 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveExtraData() {
/* 259 */     if (this.chunkLoader != null)
/*     */     {
/* 261 */       this.chunkLoader.saveExtraData();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean unloadQueuedChunks() {
/* 267 */     if (!this.worldObj.disableLevelSaving) {
/*     */       
/* 269 */       for (int i = 0; i < 100; i++) {
/*     */         
/* 271 */         if (!this.droppedChunksSet.isEmpty()) {
/*     */           
/* 273 */           Long olong = this.droppedChunksSet.iterator().next();
/* 274 */           Chunk chunk = (Chunk)this.id2ChunkMap.getValueByKey(olong.longValue());
/*     */           
/* 276 */           if (chunk != null) {
/*     */             
/* 278 */             chunk.onChunkUnload();
/* 279 */             saveChunkData(chunk);
/* 280 */             saveChunkExtraData(chunk);
/* 281 */             this.id2ChunkMap.remove(olong.longValue());
/* 282 */             this.loadedChunks.remove(chunk);
/*     */           } 
/*     */           
/* 285 */           this.droppedChunksSet.remove(olong);
/*     */         } 
/*     */       } 
/*     */       
/* 289 */       if (this.chunkLoader != null)
/*     */       {
/* 291 */         this.chunkLoader.chunkTick();
/*     */       }
/*     */     } 
/*     */     
/* 295 */     return this.serverChunkGenerator.unloadQueuedChunks();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSave() {
/* 300 */     return !this.worldObj.disableLevelSaving;
/*     */   }
/*     */ 
/*     */   
/*     */   public String makeString() {
/* 305 */     return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
/* 310 */     return this.serverChunkGenerator.getPossibleCreatures(creatureType, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
/* 315 */     return this.serverChunkGenerator.getStrongholdGen(worldIn, structureName, position);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunkCount() {
/* 320 */     return this.id2ChunkMap.getNumHashElements();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreateStructures(Chunk chunkIn, int x, int z) {}
/*     */ 
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn) {
/* 329 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\ChunkProviderServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */