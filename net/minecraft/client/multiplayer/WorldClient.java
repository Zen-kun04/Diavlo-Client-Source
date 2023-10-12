/*     */ package net.minecraft.client.multiplayer;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.MovingSoundMinecart;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.particle.EntityFX;
/*     */ import net.minecraft.client.particle.EntityFirework;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.profiler.Profiler;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.storage.ISaveHandler;
/*     */ import net.minecraft.world.storage.MapStorage;
/*     */ import net.minecraft.world.storage.SaveHandlerMP;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import net.optifine.CustomGuis;
/*     */ import net.optifine.DynamicLights;
/*     */ import net.optifine.override.PlayerControllerOF;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ public class WorldClient extends World {
/*     */   private NetHandlerPlayClient sendQueue;
/*  47 */   private final Set<Entity> entityList = Sets.newHashSet(); private ChunkProviderClient clientChunkProvider;
/*  48 */   private final Set<Entity> entitySpawnQueue = Sets.newHashSet();
/*  49 */   private final Minecraft mc = Minecraft.getMinecraft();
/*  50 */   private final Set<ChunkCoordIntPair> previousActiveChunkSet = Sets.newHashSet();
/*     */   
/*     */   private boolean playerUpdate = false;
/*     */   
/*     */   public WorldClient(NetHandlerPlayClient netHandler, WorldSettings settings, int dimension, EnumDifficulty difficulty, Profiler profilerIn) {
/*  55 */     super((ISaveHandler)new SaveHandlerMP(), new WorldInfo(settings, "MpServer"), WorldProvider.getProviderForDimension(dimension), profilerIn, true);
/*  56 */     this.sendQueue = netHandler;
/*  57 */     getWorldInfo().setDifficulty(difficulty);
/*  58 */     this.provider.registerWorld(this);
/*  59 */     setSpawnPoint(new BlockPos(8, 64, 8));
/*  60 */     this.chunkProvider = createChunkProvider();
/*  61 */     this.mapStorage = (MapStorage)new SaveDataMemoryStorage();
/*  62 */     calculateInitialSkylight();
/*  63 */     calculateInitialWeather();
/*  64 */     Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[] { this });
/*     */     
/*  66 */     if (this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
/*     */       
/*  68 */       this.mc.playerController = (PlayerControllerMP)new PlayerControllerOF(this.mc, netHandler);
/*  69 */       CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  75 */     super.tick();
/*  76 */     setTotalWorldTime(getTotalWorldTime() + 1L);
/*     */     
/*  78 */     if (getGameRules().getBoolean("doDaylightCycle"))
/*     */     {
/*  80 */       setWorldTime(getWorldTime() + 1L);
/*     */     }
/*     */     
/*  83 */     this.theProfiler.startSection("reEntryProcessing");
/*     */     
/*  85 */     for (int i = 0; i < 10 && !this.entitySpawnQueue.isEmpty(); i++) {
/*     */       
/*  87 */       Entity entity = this.entitySpawnQueue.iterator().next();
/*  88 */       this.entitySpawnQueue.remove(entity);
/*     */       
/*  90 */       if (!this.loadedEntityList.contains(entity))
/*     */       {
/*  92 */         spawnEntityInWorld(entity);
/*     */       }
/*     */     } 
/*     */     
/*  96 */     this.theProfiler.endStartSection("chunkCache");
/*  97 */     this.clientChunkProvider.unloadQueuedChunks();
/*  98 */     this.theProfiler.endStartSection("blocks");
/*  99 */     updateBlocks();
/* 100 */     this.theProfiler.endSection();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateBlockReceiveRegion(int x1, int y1, int z1, int x2, int y2, int z2) {}
/*     */ 
/*     */   
/*     */   protected IChunkProvider createChunkProvider() {
/* 109 */     this.clientChunkProvider = new ChunkProviderClient(this);
/* 110 */     return this.clientChunkProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateBlocks() {
/* 115 */     super.updateBlocks();
/* 116 */     this.previousActiveChunkSet.retainAll(this.activeChunkSet);
/*     */     
/* 118 */     if (this.previousActiveChunkSet.size() == this.activeChunkSet.size())
/*     */     {
/* 120 */       this.previousActiveChunkSet.clear();
/*     */     }
/*     */     
/* 123 */     int i = 0;
/*     */     
/* 125 */     for (ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet) {
/*     */       
/* 127 */       if (!this.previousActiveChunkSet.contains(chunkcoordintpair)) {
/*     */         
/* 129 */         int j = chunkcoordintpair.chunkXPos * 16;
/* 130 */         int k = chunkcoordintpair.chunkZPos * 16;
/* 131 */         this.theProfiler.startSection("getChunk");
/* 132 */         Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
/* 133 */         playMoodSoundAndCheckLight(j, k, chunk);
/* 134 */         this.theProfiler.endSection();
/* 135 */         this.previousActiveChunkSet.add(chunkcoordintpair);
/* 136 */         i++;
/*     */         
/* 138 */         if (i >= 10) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPreChunk(int chuncX, int chuncZ, boolean loadChunk) {
/* 148 */     if (loadChunk) {
/*     */       
/* 150 */       this.clientChunkProvider.loadChunk(chuncX, chuncZ);
/*     */     }
/*     */     else {
/*     */       
/* 154 */       this.clientChunkProvider.unloadChunk(chuncX, chuncZ);
/*     */     } 
/*     */     
/* 157 */     if (!loadChunk)
/*     */     {
/* 159 */       markBlockRangeForRenderUpdate(chuncX * 16, 0, chuncZ * 16, chuncX * 16 + 15, 256, chuncZ * 16 + 15);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean spawnEntityInWorld(Entity entityIn) {
/* 165 */     boolean flag = super.spawnEntityInWorld(entityIn);
/* 166 */     this.entityList.add(entityIn);
/*     */     
/* 168 */     if (!flag) {
/*     */       
/* 170 */       this.entitySpawnQueue.add(entityIn);
/*     */     }
/* 172 */     else if (entityIn instanceof EntityMinecart) {
/*     */       
/* 174 */       this.mc.getSoundHandler().playSound((ISound)new MovingSoundMinecart((EntityMinecart)entityIn));
/*     */     } 
/*     */     
/* 177 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeEntity(Entity entityIn) {
/* 182 */     super.removeEntity(entityIn);
/* 183 */     this.entityList.remove(entityIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEntityAdded(Entity entityIn) {
/* 188 */     super.onEntityAdded(entityIn);
/*     */     
/* 190 */     if (this.entitySpawnQueue.contains(entityIn))
/*     */     {
/* 192 */       this.entitySpawnQueue.remove(entityIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEntityRemoved(Entity entityIn) {
/* 198 */     super.onEntityRemoved(entityIn);
/* 199 */     boolean flag = false;
/*     */     
/* 201 */     if (this.entityList.contains(entityIn))
/*     */     {
/* 203 */       if (entityIn.isEntityAlive()) {
/*     */         
/* 205 */         this.entitySpawnQueue.add(entityIn);
/* 206 */         flag = true;
/*     */       }
/*     */       else {
/*     */         
/* 210 */         this.entityList.remove(entityIn);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntityToWorld(int entityID, Entity entityToSpawn) {
/* 217 */     Entity entity = getEntityByID(entityID);
/*     */     
/* 219 */     if (entity != null)
/*     */     {
/* 221 */       removeEntity(entity);
/*     */     }
/*     */     
/* 224 */     this.entityList.add(entityToSpawn);
/* 225 */     entityToSpawn.setEntityId(entityID);
/*     */     
/* 227 */     if (!spawnEntityInWorld(entityToSpawn))
/*     */     {
/* 229 */       this.entitySpawnQueue.add(entityToSpawn);
/*     */     }
/*     */     
/* 232 */     this.entitiesById.addKey(entityID, entityToSpawn);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getEntityByID(int id) {
/* 237 */     return (id == this.mc.thePlayer.getEntityId()) ? (Entity)this.mc.thePlayer : super.getEntityByID(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity removeEntityFromWorld(int entityID) {
/* 242 */     Entity entity = (Entity)this.entitiesById.removeObject(entityID);
/*     */     
/* 244 */     if (entity != null) {
/*     */       
/* 246 */       this.entityList.remove(entity);
/* 247 */       removeEntity(entity);
/*     */     } 
/*     */     
/* 250 */     return entity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean invalidateRegionAndSetBlock(BlockPos pos, IBlockState state) {
/* 255 */     int i = pos.getX();
/* 256 */     int j = pos.getY();
/* 257 */     int k = pos.getZ();
/* 258 */     invalidateBlockReceiveRegion(i, j, k, i, j, k);
/* 259 */     return super.setBlockState(pos, state, 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuittingDisconnectingPacket() {
/* 264 */     this.sendQueue.getNetworkManager().closeChannel((IChatComponent)new ChatComponentText("Quitting"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateWeather() {}
/*     */ 
/*     */   
/*     */   protected int getRenderDistanceChunks() {
/* 273 */     return this.mc.gameSettings.renderDistanceChunks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doVoidFogParticles(int posX, int posY, int posZ) {
/* 278 */     int i = 16;
/* 279 */     Random random = new Random();
/* 280 */     ItemStack itemstack = this.mc.thePlayer.getHeldItem();
/* 281 */     boolean flag = (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE && itemstack != null && Block.getBlockFromItem(itemstack.getItem()) == Blocks.barrier);
/* 282 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */     
/* 284 */     for (int j = 0; j < 1000; j++) {
/*     */       
/* 286 */       int k = posX + this.rand.nextInt(i) - this.rand.nextInt(i);
/* 287 */       int l = posY + this.rand.nextInt(i) - this.rand.nextInt(i);
/* 288 */       int i1 = posZ + this.rand.nextInt(i) - this.rand.nextInt(i);
/* 289 */       blockpos$mutableblockpos.set(k, l, i1);
/* 290 */       IBlockState iblockstate = getBlockState((BlockPos)blockpos$mutableblockpos);
/* 291 */       iblockstate.getBlock().randomDisplayTick(this, (BlockPos)blockpos$mutableblockpos, iblockstate, random);
/*     */       
/* 293 */       if (flag && iblockstate.getBlock() == Blocks.barrier)
/*     */       {
/* 295 */         spawnParticle(EnumParticleTypes.BARRIER, (k + 0.5F), (l + 0.5F), (i1 + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllEntities() {
/* 302 */     this.loadedEntityList.removeAll(this.unloadedEntityList);
/*     */     
/* 304 */     for (int i = 0; i < this.unloadedEntityList.size(); i++) {
/*     */       
/* 306 */       Entity entity = this.unloadedEntityList.get(i);
/* 307 */       int j = entity.chunkCoordX;
/* 308 */       int k = entity.chunkCoordZ;
/*     */       
/* 310 */       if (entity.addedToChunk && isChunkLoaded(j, k, true))
/*     */       {
/* 312 */         getChunkFromChunkCoords(j, k).removeEntity(entity);
/*     */       }
/*     */     } 
/*     */     
/* 316 */     for (int l = 0; l < this.unloadedEntityList.size(); l++)
/*     */     {
/* 318 */       onEntityRemoved(this.unloadedEntityList.get(l));
/*     */     }
/*     */     
/* 321 */     this.unloadedEntityList.clear();
/*     */     
/* 323 */     for (int i1 = 0; i1 < this.loadedEntityList.size(); i1++) {
/*     */       
/* 325 */       Entity entity1 = this.loadedEntityList.get(i1);
/*     */       
/* 327 */       if (entity1.ridingEntity != null) {
/*     */         
/* 329 */         if (!entity1.ridingEntity.isDead && entity1.ridingEntity.riddenByEntity == entity1) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 334 */         entity1.ridingEntity.riddenByEntity = null;
/* 335 */         entity1.ridingEntity = null;
/*     */       } 
/*     */       
/* 338 */       if (entity1.isDead) {
/*     */         
/* 340 */         int j1 = entity1.chunkCoordX;
/* 341 */         int k1 = entity1.chunkCoordZ;
/*     */         
/* 343 */         if (entity1.addedToChunk && isChunkLoaded(j1, k1, true))
/*     */         {
/* 345 */           getChunkFromChunkCoords(j1, k1).removeEntity(entity1);
/*     */         }
/*     */         
/* 348 */         this.loadedEntityList.remove(i1--);
/* 349 */         onEntityRemoved(entity1);
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
/*     */   
/*     */   public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
/* 356 */     CrashReportCategory crashreportcategory = super.addWorldInfoToCrashReport(report);
/* 357 */     crashreportcategory.addCrashSectionCallable("Forced entities", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/* 361 */             return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
/*     */           }
/*     */         });
/* 364 */     crashreportcategory.addCrashSectionCallable("Retry entities", new Callable<String>()
/*     */         {
/*     */           public String call()
/*     */           {
/* 368 */             return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
/*     */           }
/*     */         });
/* 371 */     crashreportcategory.addCrashSectionCallable("Server brand", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 375 */             return WorldClient.this.mc.thePlayer.getClientBrand();
/*     */           }
/*     */         });
/* 378 */     crashreportcategory.addCrashSectionCallable("Server type", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 382 */             return (WorldClient.this.mc.getIntegratedServer() == null) ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
/*     */           }
/*     */         });
/* 385 */     return crashreportcategory;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playSoundAtPos(BlockPos pos, String soundName, float volume, float pitch, boolean distanceDelay) {
/* 390 */     playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundName, volume, pitch, distanceDelay);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay) {
/* 395 */     double d0 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
/* 396 */     PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
/*     */     
/* 398 */     if (distanceDelay && d0 > 100.0D) {
/*     */       
/* 400 */       double d1 = Math.sqrt(d0) / 40.0D;
/* 401 */       this.mc.getSoundHandler().playDelayedSound((ISound)positionedsoundrecord, (int)(d1 * 20.0D));
/*     */     }
/*     */     else {
/*     */       
/* 405 */       this.mc.getSoundHandler().playSound((ISound)positionedsoundrecord);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund) {
/* 411 */     this.mc.effectRenderer.addEffect((EntityFX)new EntityFirework.StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldScoreboard(Scoreboard scoreboardIn) {
/* 416 */     this.worldScoreboard = scoreboardIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldTime(long time) {
/* 421 */     if (time < 0L) {
/*     */       
/* 423 */       time = -time;
/* 424 */       getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
/*     */     }
/*     */     else {
/*     */       
/* 428 */       getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
/*     */     } 
/*     */     
/* 431 */     super.setWorldTime(time);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCombinedLight(BlockPos pos, int lightValue) {
/* 436 */     int i = super.getCombinedLight(pos, lightValue);
/*     */     
/* 438 */     if (Config.isDynamicLights())
/*     */     {
/* 440 */       i = DynamicLights.getCombinedLight(pos, i);
/*     */     }
/*     */     
/* 443 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
/* 448 */     this.playerUpdate = isPlayerActing();
/* 449 */     boolean flag = super.setBlockState(pos, newState, flags);
/* 450 */     this.playerUpdate = false;
/* 451 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isPlayerActing() {
/* 456 */     if (this.mc.playerController instanceof PlayerControllerOF) {
/*     */       
/* 458 */       PlayerControllerOF playercontrollerof = (PlayerControllerOF)this.mc.playerController;
/* 459 */       return playercontrollerof.isActing();
/*     */     } 
/*     */ 
/*     */     
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPlayerUpdate() {
/* 469 */     return this.playerUpdate;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\WorldClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */