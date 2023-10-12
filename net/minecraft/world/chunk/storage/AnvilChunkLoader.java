/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.MinecraftException;
/*     */ import net.minecraft.world.NextTickListEntry;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.NibbleArray;
/*     */ import net.minecraft.world.storage.IThreadedFileIO;
/*     */ import net.minecraft.world.storage.ThreadedFileIOBase;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO {
/*  34 */   private static final Logger logger = LogManager.getLogger();
/*  35 */   private Map<ChunkCoordIntPair, NBTTagCompound> chunksToRemove = new ConcurrentHashMap<>();
/*  36 */   private Set<ChunkCoordIntPair> pendingAnvilChunksCoordinates = Collections.newSetFromMap(new ConcurrentHashMap<>());
/*     */   
/*     */   private final File chunkSaveLocation;
/*     */   private boolean field_183014_e = false;
/*     */   
/*     */   public AnvilChunkLoader(File chunkSaveLocationIn) {
/*  42 */     this.chunkSaveLocation = chunkSaveLocationIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Chunk loadChunk(World worldIn, int x, int z) throws IOException {
/*  47 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);
/*  48 */     NBTTagCompound nbttagcompound = this.chunksToRemove.get(chunkcoordintpair);
/*     */     
/*  50 */     if (nbttagcompound == null) {
/*     */       
/*  52 */       DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, x, z);
/*     */       
/*  54 */       if (datainputstream == null)
/*     */       {
/*  56 */         return null;
/*     */       }
/*     */       
/*  59 */       nbttagcompound = CompressedStreamTools.read(datainputstream);
/*     */     } 
/*     */     
/*  62 */     return checkedReadChunkFromNBT(worldIn, x, z, nbttagcompound);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Chunk checkedReadChunkFromNBT(World worldIn, int x, int z, NBTTagCompound p_75822_4_) {
/*  67 */     if (!p_75822_4_.hasKey("Level", 10)) {
/*     */       
/*  69 */       logger.error("Chunk file at " + x + "," + z + " is missing level data, skipping");
/*  70 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     NBTTagCompound nbttagcompound = p_75822_4_.getCompoundTag("Level");
/*     */     
/*  76 */     if (!nbttagcompound.hasKey("Sections", 9)) {
/*     */       
/*  78 */       logger.error("Chunk file at " + x + "," + z + " is missing block data, skipping");
/*  79 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     Chunk chunk = readChunkFromNBT(worldIn, nbttagcompound);
/*     */     
/*  85 */     if (!chunk.isAtLocation(x, z)) {
/*     */       
/*  87 */       logger.error("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
/*  88 */       nbttagcompound.setInteger("xPos", x);
/*  89 */       nbttagcompound.setInteger("zPos", z);
/*  90 */       chunk = readChunkFromNBT(worldIn, nbttagcompound);
/*     */     } 
/*     */     
/*  93 */     return chunk;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveChunk(World worldIn, Chunk chunkIn) throws MinecraftException, IOException {
/* 100 */     worldIn.checkSessionLock();
/*     */ 
/*     */     
/*     */     try {
/* 104 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 105 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 106 */       nbttagcompound.setTag("Level", (NBTBase)nbttagcompound1);
/* 107 */       writeChunkToNBT(chunkIn, worldIn, nbttagcompound1);
/* 108 */       addChunkToPending(chunkIn.getChunkCoordIntPair(), nbttagcompound);
/*     */     }
/* 110 */     catch (Exception exception) {
/*     */       
/* 112 */       logger.error("Failed to save chunk", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addChunkToPending(ChunkCoordIntPair p_75824_1_, NBTTagCompound p_75824_2_) {
/* 118 */     if (!this.pendingAnvilChunksCoordinates.contains(p_75824_1_))
/*     */     {
/* 120 */       this.chunksToRemove.put(p_75824_1_, p_75824_2_);
/*     */     }
/*     */     
/* 123 */     ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
/*     */   }
/*     */   
/*     */   public boolean writeNextIO() {
/*     */     boolean lvt_3_1_;
/* 128 */     if (this.chunksToRemove.isEmpty()) {
/*     */       
/* 130 */       if (this.field_183014_e)
/*     */       {
/* 132 */         logger.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", new Object[] { this.chunkSaveLocation.getName() });
/*     */       }
/*     */       
/* 135 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     ChunkCoordIntPair chunkcoordintpair = this.chunksToRemove.keySet().iterator().next();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 144 */       this.pendingAnvilChunksCoordinates.add(chunkcoordintpair);
/* 145 */       NBTTagCompound nbttagcompound = this.chunksToRemove.remove(chunkcoordintpair);
/*     */       
/* 147 */       if (nbttagcompound != null) {
/*     */         
/*     */         try {
/*     */           
/* 151 */           func_183013_b(chunkcoordintpair, nbttagcompound);
/*     */         }
/* 153 */         catch (Exception exception) {
/*     */           
/* 155 */           logger.error("Failed to save chunk", exception);
/*     */         } 
/*     */       }
/*     */       
/* 159 */       lvt_3_1_ = true;
/*     */     }
/*     */     finally {
/*     */       
/* 163 */       this.pendingAnvilChunksCoordinates.remove(chunkcoordintpair);
/*     */     } 
/*     */     
/* 166 */     return lvt_3_1_;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void func_183013_b(ChunkCoordIntPair p_183013_1_, NBTTagCompound p_183013_2_) throws IOException {
/* 172 */     DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, p_183013_1_.chunkXPos, p_183013_1_.chunkZPos);
/* 173 */     CompressedStreamTools.write(p_183013_2_, dataoutputstream);
/* 174 */     dataoutputstream.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraChunkData(World worldIn, Chunk chunkIn) throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void chunkTick() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveExtraData() {
/*     */     try {
/* 189 */       this.field_183014_e = true;
/*     */ 
/*     */       
/*     */       while (true) {
/* 193 */         if (writeNextIO());
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 201 */       this.field_183014_e = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound p_75820_3_) {
/* 207 */     p_75820_3_.setByte("V", (byte)1);
/* 208 */     p_75820_3_.setInteger("xPos", chunkIn.xPosition);
/* 209 */     p_75820_3_.setInteger("zPos", chunkIn.zPosition);
/* 210 */     p_75820_3_.setLong("LastUpdate", worldIn.getTotalWorldTime());
/* 211 */     p_75820_3_.setIntArray("HeightMap", chunkIn.getHeightMap());
/* 212 */     p_75820_3_.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
/* 213 */     p_75820_3_.setBoolean("LightPopulated", chunkIn.isLightPopulated());
/* 214 */     p_75820_3_.setLong("InhabitedTime", chunkIn.getInhabitedTime());
/* 215 */     ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
/* 216 */     NBTTagList nbttaglist = new NBTTagList();
/* 217 */     boolean flag = !worldIn.provider.getHasNoSky();
/*     */     
/* 219 */     for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage) {
/*     */       
/* 221 */       if (extendedblockstorage != null) {
/*     */         
/* 223 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 224 */         nbttagcompound.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 0xFF));
/* 225 */         byte[] abyte = new byte[(extendedblockstorage.getData()).length];
/* 226 */         NibbleArray nibblearray = new NibbleArray();
/* 227 */         NibbleArray nibblearray1 = null;
/*     */         
/* 229 */         for (int i = 0; i < (extendedblockstorage.getData()).length; i++) {
/*     */           
/* 231 */           char c0 = extendedblockstorage.getData()[i];
/* 232 */           int j = i & 0xF;
/* 233 */           int k = i >> 8 & 0xF;
/* 234 */           int l = i >> 4 & 0xF;
/*     */           
/* 236 */           if (c0 >> 12 != 0) {
/*     */             
/* 238 */             if (nibblearray1 == null)
/*     */             {
/* 240 */               nibblearray1 = new NibbleArray();
/*     */             }
/*     */             
/* 243 */             nibblearray1.set(j, k, l, c0 >> 12);
/*     */           } 
/*     */           
/* 246 */           abyte[i] = (byte)(c0 >> 4 & 0xFF);
/* 247 */           nibblearray.set(j, k, l, c0 & 0xF);
/*     */         } 
/*     */         
/* 250 */         nbttagcompound.setByteArray("Blocks", abyte);
/* 251 */         nbttagcompound.setByteArray("Data", nibblearray.getData());
/*     */         
/* 253 */         if (nibblearray1 != null)
/*     */         {
/* 255 */           nbttagcompound.setByteArray("Add", nibblearray1.getData());
/*     */         }
/*     */         
/* 258 */         nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().getData());
/*     */         
/* 260 */         if (flag) {
/*     */           
/* 262 */           nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().getData());
/*     */         }
/*     */         else {
/*     */           
/* 266 */           nbttagcompound.setByteArray("SkyLight", new byte[(extendedblockstorage.getBlocklightArray().getData()).length]);
/*     */         } 
/*     */         
/* 269 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     p_75820_3_.setTag("Sections", (NBTBase)nbttaglist);
/* 274 */     p_75820_3_.setByteArray("Biomes", chunkIn.getBiomeArray());
/* 275 */     chunkIn.setHasEntities(false);
/* 276 */     NBTTagList nbttaglist1 = new NBTTagList();
/*     */     
/* 278 */     for (int i1 = 0; i1 < (chunkIn.getEntityLists()).length; i1++) {
/*     */       
/* 280 */       for (Entity entity : chunkIn.getEntityLists()[i1]) {
/*     */         
/* 282 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*     */         
/* 284 */         if (entity.writeToNBTOptional(nbttagcompound1)) {
/*     */           
/* 286 */           chunkIn.setHasEntities(true);
/* 287 */           nbttaglist1.appendTag((NBTBase)nbttagcompound1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 292 */     p_75820_3_.setTag("Entities", (NBTBase)nbttaglist1);
/* 293 */     NBTTagList nbttaglist2 = new NBTTagList();
/*     */     
/* 295 */     for (TileEntity tileentity : chunkIn.getTileEntityMap().values()) {
/*     */       
/* 297 */       NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/* 298 */       tileentity.writeToNBT(nbttagcompound2);
/* 299 */       nbttaglist2.appendTag((NBTBase)nbttagcompound2);
/*     */     } 
/*     */     
/* 302 */     p_75820_3_.setTag("TileEntities", (NBTBase)nbttaglist2);
/* 303 */     List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);
/*     */     
/* 305 */     if (list != null) {
/*     */       
/* 307 */       long j1 = worldIn.getTotalWorldTime();
/* 308 */       NBTTagList nbttaglist3 = new NBTTagList();
/*     */       
/* 310 */       for (NextTickListEntry nextticklistentry : list) {
/*     */         
/* 312 */         NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/* 313 */         ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(nextticklistentry.getBlock());
/* 314 */         nbttagcompound3.setString("i", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 315 */         nbttagcompound3.setInteger("x", nextticklistentry.position.getX());
/* 316 */         nbttagcompound3.setInteger("y", nextticklistentry.position.getY());
/* 317 */         nbttagcompound3.setInteger("z", nextticklistentry.position.getZ());
/* 318 */         nbttagcompound3.setInteger("t", (int)(nextticklistentry.scheduledTime - j1));
/* 319 */         nbttagcompound3.setInteger("p", nextticklistentry.priority);
/* 320 */         nbttaglist3.appendTag((NBTBase)nbttagcompound3);
/*     */       } 
/*     */       
/* 323 */       p_75820_3_.setTag("TileTicks", (NBTBase)nbttaglist3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Chunk readChunkFromNBT(World worldIn, NBTTagCompound p_75823_2_) {
/* 329 */     int i = p_75823_2_.getInteger("xPos");
/* 330 */     int j = p_75823_2_.getInteger("zPos");
/* 331 */     Chunk chunk = new Chunk(worldIn, i, j);
/* 332 */     chunk.setHeightMap(p_75823_2_.getIntArray("HeightMap"));
/* 333 */     chunk.setTerrainPopulated(p_75823_2_.getBoolean("TerrainPopulated"));
/* 334 */     chunk.setLightPopulated(p_75823_2_.getBoolean("LightPopulated"));
/* 335 */     chunk.setInhabitedTime(p_75823_2_.getLong("InhabitedTime"));
/* 336 */     NBTTagList nbttaglist = p_75823_2_.getTagList("Sections", 10);
/* 337 */     int k = 16;
/* 338 */     ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[k];
/* 339 */     boolean flag = !worldIn.provider.getHasNoSky();
/*     */     
/* 341 */     for (int l = 0; l < nbttaglist.tagCount(); l++) {
/*     */       
/* 343 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(l);
/* 344 */       int i1 = nbttagcompound.getByte("Y");
/* 345 */       ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i1 << 4, flag);
/* 346 */       byte[] abyte = nbttagcompound.getByteArray("Blocks");
/* 347 */       NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
/* 348 */       NibbleArray nibblearray1 = nbttagcompound.hasKey("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
/* 349 */       char[] achar = new char[abyte.length];
/*     */       
/* 351 */       for (int j1 = 0; j1 < achar.length; j1++) {
/*     */         
/* 353 */         int k1 = j1 & 0xF;
/* 354 */         int l1 = j1 >> 8 & 0xF;
/* 355 */         int i2 = j1 >> 4 & 0xF;
/* 356 */         int j2 = (nibblearray1 != null) ? nibblearray1.get(k1, l1, i2) : 0;
/* 357 */         achar[j1] = (char)(j2 << 12 | (abyte[j1] & 0xFF) << 4 | nibblearray.get(k1, l1, i2));
/*     */       } 
/*     */       
/* 360 */       extendedblockstorage.setData(achar);
/* 361 */       extendedblockstorage.setBlocklightArray(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));
/*     */       
/* 363 */       if (flag)
/*     */       {
/* 365 */         extendedblockstorage.setSkylightArray(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
/*     */       }
/*     */       
/* 368 */       extendedblockstorage.removeInvalidBlocks();
/* 369 */       aextendedblockstorage[i1] = extendedblockstorage;
/*     */     } 
/*     */     
/* 372 */     chunk.setStorageArrays(aextendedblockstorage);
/*     */     
/* 374 */     if (p_75823_2_.hasKey("Biomes", 7))
/*     */     {
/* 376 */       chunk.setBiomeArray(p_75823_2_.getByteArray("Biomes"));
/*     */     }
/*     */     
/* 379 */     NBTTagList nbttaglist1 = p_75823_2_.getTagList("Entities", 10);
/*     */     
/* 381 */     if (nbttaglist1 != null)
/*     */     {
/* 383 */       for (int k2 = 0; k2 < nbttaglist1.tagCount(); k2++) {
/*     */         
/* 385 */         NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(k2);
/* 386 */         Entity entity = EntityList.createEntityFromNBT(nbttagcompound1, worldIn);
/* 387 */         chunk.setHasEntities(true);
/*     */         
/* 389 */         if (entity != null) {
/*     */           
/* 391 */           chunk.addEntity(entity);
/* 392 */           Entity entity1 = entity;
/*     */           
/* 394 */           for (NBTTagCompound nbttagcompound4 = nbttagcompound1; nbttagcompound4.hasKey("Riding", 10); nbttagcompound4 = nbttagcompound4.getCompoundTag("Riding")) {
/*     */             
/* 396 */             Entity entity2 = EntityList.createEntityFromNBT(nbttagcompound4.getCompoundTag("Riding"), worldIn);
/*     */             
/* 398 */             if (entity2 != null) {
/*     */               
/* 400 */               chunk.addEntity(entity2);
/* 401 */               entity1.mountEntity(entity2);
/*     */             } 
/*     */             
/* 404 */             entity1 = entity2;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 410 */     NBTTagList nbttaglist2 = p_75823_2_.getTagList("TileEntities", 10);
/*     */     
/* 412 */     if (nbttaglist2 != null)
/*     */     {
/* 414 */       for (int l2 = 0; l2 < nbttaglist2.tagCount(); l2++) {
/*     */         
/* 416 */         NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(l2);
/* 417 */         TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound2);
/*     */         
/* 419 */         if (tileentity != null)
/*     */         {
/* 421 */           chunk.addTileEntity(tileentity);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 426 */     if (p_75823_2_.hasKey("TileTicks", 9)) {
/*     */       
/* 428 */       NBTTagList nbttaglist3 = p_75823_2_.getTagList("TileTicks", 10);
/*     */       
/* 430 */       if (nbttaglist3 != null)
/*     */       {
/* 432 */         for (int i3 = 0; i3 < nbttaglist3.tagCount(); i3++) {
/*     */           Block block;
/* 434 */           NBTTagCompound nbttagcompound3 = nbttaglist3.getCompoundTagAt(i3);
/*     */ 
/*     */           
/* 437 */           if (nbttagcompound3.hasKey("i", 8)) {
/*     */             
/* 439 */             block = Block.getBlockFromName(nbttagcompound3.getString("i"));
/*     */           }
/*     */           else {
/*     */             
/* 443 */             block = Block.getBlockById(nbttagcompound3.getInteger("i"));
/*     */           } 
/*     */           
/* 446 */           worldIn.scheduleBlockUpdate(new BlockPos(nbttagcompound3.getInteger("x"), nbttagcompound3.getInteger("y"), nbttagcompound3.getInteger("z")), block, nbttagcompound3.getInteger("t"), nbttagcompound3.getInteger("p"));
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 451 */     return chunk;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\storage\AnvilChunkLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */