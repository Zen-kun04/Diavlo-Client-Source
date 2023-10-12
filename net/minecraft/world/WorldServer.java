/*      */ package net.minecraft.world;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockEventData;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.EntityTracker;
/*      */ import net.minecraft.entity.EnumCreatureType;
/*      */ import net.minecraft.entity.effect.EntityLightningBolt;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.server.S19PacketEntityStatus;
/*      */ import net.minecraft.network.play.server.S24PacketBlockAction;
/*      */ import net.minecraft.network.play.server.S27PacketExplosion;
/*      */ import net.minecraft.network.play.server.S2APacketParticles;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.ScoreboardSaveData;
/*      */ import net.minecraft.scoreboard.ServerScoreboard;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.management.PlayerManager;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.IProgressUpdate;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.WeightedRandom;
/*      */ import net.minecraft.util.WeightedRandomChestContent;
/*      */ import net.minecraft.village.VillageCollection;
/*      */ import net.minecraft.village.VillageSiege;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
/*      */ import net.minecraft.world.chunk.storage.IChunkLoader;
/*      */ import net.minecraft.world.gen.ChunkProviderServer;
/*      */ import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
/*      */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.MapStorage;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ public class WorldServer
/*      */   extends World
/*      */   implements IThreadListener
/*      */ {
/*   76 */   private static final Logger logger = LogManager.getLogger();
/*      */   private final MinecraftServer mcServer;
/*      */   private final EntityTracker theEntityTracker;
/*      */   private final PlayerManager thePlayerManager;
/*   80 */   private final Set<NextTickListEntry> pendingTickListEntriesHashSet = Sets.newHashSet();
/*   81 */   private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new TreeSet<>();
/*   82 */   private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();
/*      */   public ChunkProviderServer theChunkProviderServer;
/*      */   public boolean disableLevelSaving;
/*      */   private boolean allPlayersSleeping;
/*      */   private int updateEntityTick;
/*      */   private final Teleporter worldTeleporter;
/*   88 */   private final SpawnerAnimals mobSpawner = new SpawnerAnimals();
/*   89 */   protected final VillageSiege villageSiege = new VillageSiege(this);
/*   90 */   private ServerBlockEventList[] blockEventQueue = new ServerBlockEventList[] { new ServerBlockEventList(), new ServerBlockEventList() };
/*      */   private int blockEventCacheIndex;
/*   92 */   private static final List<WeightedRandomChestContent> bonusChestContent = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10) });
/*   93 */   private List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();
/*      */ 
/*      */   
/*      */   public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn) {
/*   97 */     super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), profilerIn, false);
/*   98 */     this.mcServer = server;
/*   99 */     this.theEntityTracker = new EntityTracker(this);
/*  100 */     this.thePlayerManager = new PlayerManager(this);
/*  101 */     this.provider.registerWorld(this);
/*  102 */     this.chunkProvider = createChunkProvider();
/*  103 */     this.worldTeleporter = new Teleporter(this);
/*  104 */     calculateInitialSkylight();
/*  105 */     calculateInitialWeather();
/*  106 */     getWorldBorder().setSize(server.getMaxWorldSize());
/*      */   }
/*      */ 
/*      */   
/*      */   public World init() {
/*  111 */     this.mapStorage = new MapStorage(this.saveHandler);
/*  112 */     String s = VillageCollection.fileNameForProvider(this.provider);
/*  113 */     VillageCollection villagecollection = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, s);
/*      */     
/*  115 */     if (villagecollection == null) {
/*      */       
/*  117 */       this.villageCollectionObj = new VillageCollection(this);
/*  118 */       this.mapStorage.setData(s, (WorldSavedData)this.villageCollectionObj);
/*      */     }
/*      */     else {
/*      */       
/*  122 */       this.villageCollectionObj = villagecollection;
/*  123 */       this.villageCollectionObj.setWorldsForAll(this);
/*      */     } 
/*      */     
/*  126 */     this.worldScoreboard = (Scoreboard)new ServerScoreboard(this.mcServer);
/*  127 */     ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");
/*      */     
/*  129 */     if (scoreboardsavedata == null) {
/*      */       
/*  131 */       scoreboardsavedata = new ScoreboardSaveData();
/*  132 */       this.mapStorage.setData("scoreboard", (WorldSavedData)scoreboardsavedata);
/*      */     } 
/*      */     
/*  135 */     scoreboardsavedata.setScoreboard(this.worldScoreboard);
/*  136 */     ((ServerScoreboard)this.worldScoreboard).func_96547_a(scoreboardsavedata);
/*  137 */     getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
/*  138 */     getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
/*  139 */     getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
/*  140 */     getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
/*  141 */     getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
/*      */     
/*  143 */     if (this.worldInfo.getBorderLerpTime() > 0L) {
/*      */       
/*  145 */       getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
/*      */     }
/*      */     else {
/*      */       
/*  149 */       getWorldBorder().setTransition(this.worldInfo.getBorderSize());
/*      */     } 
/*      */     
/*  152 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  157 */     super.tick();
/*      */     
/*  159 */     if (getWorldInfo().isHardcoreModeEnabled() && getDifficulty() != EnumDifficulty.HARD)
/*      */     {
/*  161 */       getWorldInfo().setDifficulty(EnumDifficulty.HARD);
/*      */     }
/*      */     
/*  164 */     this.provider.getWorldChunkManager().cleanupCache();
/*      */     
/*  166 */     if (areAllPlayersAsleep()) {
/*      */       
/*  168 */       if (getGameRules().getBoolean("doDaylightCycle")) {
/*      */         
/*  170 */         long i = this.worldInfo.getWorldTime() + 24000L;
/*  171 */         this.worldInfo.setWorldTime(i - i % 24000L);
/*      */       } 
/*      */       
/*  174 */       wakeAllPlayers();
/*      */     } 
/*      */     
/*  177 */     this.theProfiler.startSection("mobSpawner");
/*      */     
/*  179 */     if (getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
/*      */     {
/*  181 */       this.mobSpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, (this.worldInfo.getWorldTotalTime() % 400L == 0L));
/*      */     }
/*      */     
/*  184 */     this.theProfiler.endStartSection("chunkSource");
/*  185 */     this.chunkProvider.unloadQueuedChunks();
/*  186 */     int j = calculateSkylightSubtracted(1.0F);
/*      */     
/*  188 */     if (j != getSkylightSubtracted())
/*      */     {
/*  190 */       setSkylightSubtracted(j);
/*      */     }
/*      */     
/*  193 */     this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
/*      */     
/*  195 */     if (getGameRules().getBoolean("doDaylightCycle"))
/*      */     {
/*  197 */       this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
/*      */     }
/*      */     
/*  200 */     this.theProfiler.endStartSection("tickPending");
/*  201 */     tickUpdates(false);
/*  202 */     this.theProfiler.endStartSection("tickBlocks");
/*  203 */     updateBlocks();
/*  204 */     this.theProfiler.endStartSection("chunkMap");
/*  205 */     this.thePlayerManager.updatePlayerInstances();
/*  206 */     this.theProfiler.endStartSection("village");
/*  207 */     this.villageCollectionObj.tick();
/*  208 */     this.villageSiege.tick();
/*  209 */     this.theProfiler.endStartSection("portalForcer");
/*  210 */     this.worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
/*  211 */     this.theProfiler.endSection();
/*  212 */     sendQueuedBlockEvents();
/*      */   }
/*      */ 
/*      */   
/*      */   public BiomeGenBase.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType creatureType, BlockPos pos) {
/*  217 */     List<BiomeGenBase.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
/*  218 */     return (list != null && !list.isEmpty()) ? (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(this.rand, list) : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canCreatureTypeSpawnHere(EnumCreatureType creatureType, BiomeGenBase.SpawnListEntry spawnListEntry, BlockPos pos) {
/*  223 */     List<BiomeGenBase.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
/*  224 */     return (list != null && !list.isEmpty()) ? list.contains(spawnListEntry) : false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAllPlayersSleepingFlag() {
/*  229 */     this.allPlayersSleeping = false;
/*      */     
/*  231 */     if (!this.playerEntities.isEmpty()) {
/*      */       
/*  233 */       int i = 0;
/*  234 */       int j = 0;
/*      */       
/*  236 */       for (EntityPlayer entityplayer : this.playerEntities) {
/*      */         
/*  238 */         if (entityplayer.isSpectator()) {
/*      */           
/*  240 */           i++; continue;
/*      */         } 
/*  242 */         if (entityplayer.isPlayerSleeping())
/*      */         {
/*  244 */           j++;
/*      */         }
/*      */       } 
/*      */       
/*  248 */       this.allPlayersSleeping = (j > 0 && j >= this.playerEntities.size() - i);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void wakeAllPlayers() {
/*  254 */     this.allPlayersSleeping = false;
/*      */     
/*  256 */     for (EntityPlayer entityplayer : this.playerEntities) {
/*      */       
/*  258 */       if (entityplayer.isPlayerSleeping())
/*      */       {
/*  260 */         entityplayer.wakeUpPlayer(false, false, true);
/*      */       }
/*      */     } 
/*      */     
/*  264 */     resetRainAndThunder();
/*      */   }
/*      */ 
/*      */   
/*      */   private void resetRainAndThunder() {
/*  269 */     this.worldInfo.setRainTime(0);
/*  270 */     this.worldInfo.setRaining(false);
/*  271 */     this.worldInfo.setThunderTime(0);
/*  272 */     this.worldInfo.setThundering(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean areAllPlayersAsleep() {
/*  277 */     if (this.allPlayersSleeping && !this.isRemote) {
/*      */       
/*  279 */       for (EntityPlayer entityplayer : this.playerEntities) {
/*      */         
/*  281 */         if (entityplayer.isSpectator() || !entityplayer.isPlayerFullyAsleep())
/*      */         {
/*  283 */           return false;
/*      */         }
/*      */       } 
/*      */       
/*  287 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  291 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInitialSpawnLocation() {
/*  297 */     if (this.worldInfo.getSpawnY() <= 0)
/*      */     {
/*  299 */       this.worldInfo.setSpawnY(getSeaLevel() + 1);
/*      */     }
/*      */     
/*  302 */     int i = this.worldInfo.getSpawnX();
/*  303 */     int j = this.worldInfo.getSpawnZ();
/*  304 */     int k = 0;
/*      */     
/*  306 */     while (getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.air) {
/*      */       
/*  308 */       i += this.rand.nextInt(8) - this.rand.nextInt(8);
/*  309 */       j += this.rand.nextInt(8) - this.rand.nextInt(8);
/*  310 */       k++;
/*      */       
/*  312 */       if (k == 10000) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  318 */     this.worldInfo.setSpawnX(i);
/*  319 */     this.worldInfo.setSpawnZ(j);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateBlocks() {
/*  324 */     super.updateBlocks();
/*      */     
/*  326 */     if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
/*      */       
/*  328 */       for (ChunkCoordIntPair chunkcoordintpair1 : this.activeChunkSet)
/*      */       {
/*  330 */         getChunkFromChunkCoords(chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos).func_150804_b(false);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  335 */       int i = 0;
/*  336 */       int j = 0;
/*      */       
/*  338 */       for (ChunkCoordIntPair chunkcoordintpair : this.activeChunkSet) {
/*      */         
/*  340 */         int k = chunkcoordintpair.chunkXPos * 16;
/*  341 */         int l = chunkcoordintpair.chunkZPos * 16;
/*  342 */         this.theProfiler.startSection("getChunk");
/*  343 */         Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
/*  344 */         playMoodSoundAndCheckLight(k, l, chunk);
/*  345 */         this.theProfiler.endStartSection("tickChunk");
/*  346 */         chunk.func_150804_b(false);
/*  347 */         this.theProfiler.endStartSection("thunder");
/*      */         
/*  349 */         if (this.rand.nextInt(100000) == 0 && isRaining() && isThundering()) {
/*      */           
/*  351 */           this.updateLCG = this.updateLCG * 3 + 1013904223;
/*  352 */           int i1 = this.updateLCG >> 2;
/*  353 */           BlockPos blockpos = adjustPosToNearbyEntity(new BlockPos(k + (i1 & 0xF), 0, l + (i1 >> 8 & 0xF)));
/*      */           
/*  355 */           if (isRainingAt(blockpos))
/*      */           {
/*  357 */             addWeatherEffect((Entity)new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ()));
/*      */           }
/*      */         } 
/*      */         
/*  361 */         this.theProfiler.endStartSection("iceandsnow");
/*      */         
/*  363 */         if (this.rand.nextInt(16) == 0) {
/*      */           
/*  365 */           this.updateLCG = this.updateLCG * 3 + 1013904223;
/*  366 */           int k2 = this.updateLCG >> 2;
/*  367 */           BlockPos blockpos2 = getPrecipitationHeight(new BlockPos(k + (k2 & 0xF), 0, l + (k2 >> 8 & 0xF)));
/*  368 */           BlockPos blockpos1 = blockpos2.down();
/*      */           
/*  370 */           if (canBlockFreezeNoWater(blockpos1))
/*      */           {
/*  372 */             setBlockState(blockpos1, Blocks.ice.getDefaultState());
/*      */           }
/*      */           
/*  375 */           if (isRaining() && canSnowAt(blockpos2, true))
/*      */           {
/*  377 */             setBlockState(blockpos2, Blocks.snow_layer.getDefaultState());
/*      */           }
/*      */           
/*  380 */           if (isRaining() && getBiomeGenForCoords(blockpos1).canRain())
/*      */           {
/*  382 */             getBlockState(blockpos1).getBlock().fillWithRain(this, blockpos1);
/*      */           }
/*      */         } 
/*      */         
/*  386 */         this.theProfiler.endStartSection("tickBlocks");
/*  387 */         int l2 = getGameRules().getInt("randomTickSpeed");
/*      */         
/*  389 */         if (l2 > 0)
/*      */         {
/*  391 */           for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
/*      */             
/*  393 */             if (extendedblockstorage != null && extendedblockstorage.getNeedsRandomTick())
/*      */             {
/*  395 */               for (int j1 = 0; j1 < l2; j1++) {
/*      */                 
/*  397 */                 this.updateLCG = this.updateLCG * 3 + 1013904223;
/*  398 */                 int k1 = this.updateLCG >> 2;
/*  399 */                 int l1 = k1 & 0xF;
/*  400 */                 int i2 = k1 >> 8 & 0xF;
/*  401 */                 int j2 = k1 >> 16 & 0xF;
/*  402 */                 j++;
/*  403 */                 IBlockState iblockstate = extendedblockstorage.get(l1, j2, i2);
/*  404 */                 Block block = iblockstate.getBlock();
/*      */                 
/*  406 */                 if (block.getTickRandomly()) {
/*      */                   
/*  408 */                   i++;
/*  409 */                   block.randomTick(this, new BlockPos(l1 + k, j2 + extendedblockstorage.getYLocation(), i2 + l), iblockstate, this.rand);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */           } 
/*      */         }
/*      */         
/*  416 */         this.theProfiler.endSection();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected BlockPos adjustPosToNearbyEntity(BlockPos pos) {
/*  423 */     BlockPos blockpos = getPrecipitationHeight(pos);
/*  424 */     AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), getHeight(), blockpos.getZ()))).expand(3.0D, 3.0D, 3.0D);
/*  425 */     List<EntityLivingBase> list = getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new Predicate<EntityLivingBase>()
/*      */         {
/*      */           public boolean apply(EntityLivingBase p_apply_1_)
/*      */           {
/*  429 */             return (p_apply_1_ != null && p_apply_1_.isEntityAlive() && WorldServer.this.canSeeSky(p_apply_1_.getPosition()));
/*      */           }
/*      */         });
/*  432 */     return !list.isEmpty() ? ((EntityLivingBase)list.get(this.rand.nextInt(list.size()))).getPosition() : blockpos;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockTickPending(BlockPos pos, Block blockType) {
/*  437 */     NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockType);
/*  438 */     return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
/*      */   }
/*      */ 
/*      */   
/*      */   public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {
/*  443 */     updateBlockTick(pos, blockIn, delay, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {
/*  448 */     NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
/*  449 */     int i = 0;
/*      */     
/*  451 */     if (this.scheduledUpdatesAreImmediate && blockIn.getMaterial() != Material.air) {
/*      */       
/*  453 */       if (blockIn.requiresUpdates()) {
/*      */         
/*  455 */         i = 8;
/*      */         
/*  457 */         if (isAreaLoaded(nextticklistentry.position.add(-i, -i, -i), nextticklistentry.position.add(i, i, i))) {
/*      */           
/*  459 */           IBlockState iblockstate = getBlockState(nextticklistentry.position);
/*      */           
/*  461 */           if (iblockstate.getBlock().getMaterial() != Material.air && iblockstate.getBlock() == nextticklistentry.getBlock())
/*      */           {
/*  463 */             iblockstate.getBlock().updateTick(this, nextticklistentry.position, iblockstate, this.rand);
/*      */           }
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  470 */       delay = 1;
/*      */     } 
/*      */     
/*  473 */     if (isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
/*      */       
/*  475 */       if (blockIn.getMaterial() != Material.air) {
/*      */         
/*  477 */         nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
/*  478 */         nextticklistentry.setPriority(priority);
/*      */       } 
/*      */       
/*  481 */       if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
/*      */         
/*  483 */         this.pendingTickListEntriesHashSet.add(nextticklistentry);
/*  484 */         this.pendingTickListEntriesTreeSet.add(nextticklistentry);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {
/*  491 */     NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
/*  492 */     nextticklistentry.setPriority(priority);
/*      */     
/*  494 */     if (blockIn.getMaterial() != Material.air)
/*      */     {
/*  496 */       nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
/*      */     }
/*      */     
/*  499 */     if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
/*      */       
/*  501 */       this.pendingTickListEntriesHashSet.add(nextticklistentry);
/*  502 */       this.pendingTickListEntriesTreeSet.add(nextticklistentry);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateEntities() {
/*  508 */     if (this.playerEntities.isEmpty()) {
/*      */       
/*  510 */       if (this.updateEntityTick++ >= 1200)
/*      */       {
/*      */         return;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  517 */       resetUpdateEntityTick();
/*      */     } 
/*      */     
/*  520 */     super.updateEntities();
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetUpdateEntityTick() {
/*  525 */     this.updateEntityTick = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean tickUpdates(boolean p_72955_1_) {
/*  530 */     if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */     {
/*  532 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  536 */     int i = this.pendingTickListEntriesTreeSet.size();
/*      */     
/*  538 */     if (i != this.pendingTickListEntriesHashSet.size())
/*      */     {
/*  540 */       throw new IllegalStateException("TickNextTick list out of synch");
/*      */     }
/*      */ 
/*      */     
/*  544 */     if (i > 1000)
/*      */     {
/*  546 */       i = 1000;
/*      */     }
/*      */     
/*  549 */     this.theProfiler.startSection("cleaning");
/*      */     
/*  551 */     for (int j = 0; j < i; j++) {
/*      */       
/*  553 */       NextTickListEntry nextticklistentry = this.pendingTickListEntriesTreeSet.first();
/*      */       
/*  555 */       if (!p_72955_1_ && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  560 */       this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
/*  561 */       this.pendingTickListEntriesHashSet.remove(nextticklistentry);
/*  562 */       this.pendingTickListEntriesThisTick.add(nextticklistentry);
/*      */     } 
/*      */     
/*  565 */     this.theProfiler.endSection();
/*  566 */     this.theProfiler.startSection("ticking");
/*  567 */     Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();
/*      */     
/*  569 */     while (iterator.hasNext()) {
/*      */       
/*  571 */       NextTickListEntry nextticklistentry1 = iterator.next();
/*  572 */       iterator.remove();
/*  573 */       int k = 0;
/*      */       
/*  575 */       if (isAreaLoaded(nextticklistentry1.position.add(-k, -k, -k), nextticklistentry1.position.add(k, k, k))) {
/*      */         
/*  577 */         IBlockState iblockstate = getBlockState(nextticklistentry1.position);
/*      */         
/*  579 */         if (iblockstate.getBlock().getMaterial() != Material.air && Block.isEqualTo(iblockstate.getBlock(), nextticklistentry1.getBlock())) {
/*      */           
/*      */           try {
/*      */             
/*  583 */             iblockstate.getBlock().updateTick(this, nextticklistentry1.position, iblockstate, this.rand);
/*      */           }
/*  585 */           catch (Throwable throwable) {
/*      */             
/*  587 */             CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
/*  588 */             CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
/*  589 */             CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position, iblockstate);
/*  590 */             throw new ReportedException(crashreport);
/*      */           } 
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*  596 */       scheduleUpdate(nextticklistentry1.position, nextticklistentry1.getBlock(), 0);
/*      */     } 
/*      */ 
/*      */     
/*  600 */     this.theProfiler.endSection();
/*  601 */     this.pendingTickListEntriesThisTick.clear();
/*  602 */     return !this.pendingTickListEntriesTreeSet.isEmpty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_) {
/*  609 */     ChunkCoordIntPair chunkcoordintpair = chunkIn.getChunkCoordIntPair();
/*  610 */     int i = (chunkcoordintpair.chunkXPos << 4) - 2;
/*  611 */     int j = i + 16 + 2;
/*  612 */     int k = (chunkcoordintpair.chunkZPos << 4) - 2;
/*  613 */     int l = k + 16 + 2;
/*  614 */     return func_175712_a(new StructureBoundingBox(i, 0, k, j, 256, l), p_72920_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_) {
/*  619 */     List<NextTickListEntry> list = null;
/*      */     
/*  621 */     for (int i = 0; i < 2; i++) {
/*      */       Iterator<NextTickListEntry> iterator;
/*      */ 
/*      */       
/*  625 */       if (i == 0) {
/*      */         
/*  627 */         iterator = this.pendingTickListEntriesTreeSet.iterator();
/*      */       }
/*      */       else {
/*      */         
/*  631 */         iterator = this.pendingTickListEntriesThisTick.iterator();
/*      */       } 
/*      */       
/*  634 */       while (iterator.hasNext()) {
/*      */         
/*  636 */         NextTickListEntry nextticklistentry = iterator.next();
/*  637 */         BlockPos blockpos = nextticklistentry.position;
/*      */         
/*  639 */         if (blockpos.getX() >= structureBB.minX && blockpos.getX() < structureBB.maxX && blockpos.getZ() >= structureBB.minZ && blockpos.getZ() < structureBB.maxZ) {
/*      */           
/*  641 */           if (p_175712_2_) {
/*      */             
/*  643 */             this.pendingTickListEntriesHashSet.remove(nextticklistentry);
/*  644 */             iterator.remove();
/*      */           } 
/*      */           
/*  647 */           if (list == null)
/*      */           {
/*  649 */             list = Lists.newArrayList();
/*      */           }
/*      */           
/*  652 */           list.add(nextticklistentry);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  657 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
/*  662 */     if (!canSpawnAnimals() && (entityIn instanceof net.minecraft.entity.passive.EntityAnimal || entityIn instanceof net.minecraft.entity.passive.EntityWaterMob))
/*      */     {
/*  664 */       entityIn.setDead();
/*      */     }
/*      */     
/*  667 */     if (!canSpawnNPCs() && entityIn instanceof net.minecraft.entity.INpc)
/*      */     {
/*  669 */       entityIn.setDead();
/*      */     }
/*      */     
/*  672 */     super.updateEntityWithOptionalForce(entityIn, forceUpdate);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canSpawnNPCs() {
/*  677 */     return this.mcServer.getCanSpawnNPCs();
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canSpawnAnimals() {
/*  682 */     return this.mcServer.getCanSpawnAnimals();
/*      */   }
/*      */ 
/*      */   
/*      */   protected IChunkProvider createChunkProvider() {
/*  687 */     IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);
/*  688 */     this.theChunkProviderServer = new ChunkProviderServer(this, ichunkloader, this.provider.createChunkGenerator());
/*  689 */     return (IChunkProvider)this.theChunkProviderServer;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<TileEntity> getTileEntitiesIn(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/*  694 */     List<TileEntity> list = Lists.newArrayList();
/*      */     
/*  696 */     for (int i = 0; i < this.loadedTileEntityList.size(); i++) {
/*      */       
/*  698 */       TileEntity tileentity = this.loadedTileEntityList.get(i);
/*  699 */       BlockPos blockpos = tileentity.getPos();
/*      */       
/*  701 */       if (blockpos.getX() >= minX && blockpos.getY() >= minY && blockpos.getZ() >= minZ && blockpos.getX() < maxX && blockpos.getY() < maxY && blockpos.getZ() < maxZ)
/*      */       {
/*  703 */         list.add(tileentity);
/*      */       }
/*      */     } 
/*      */     
/*  707 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockModifiable(EntityPlayer player, BlockPos pos) {
/*  712 */     return (!this.mcServer.isBlockProtected(this, pos, player) && getWorldBorder().contains(pos));
/*      */   }
/*      */ 
/*      */   
/*      */   public void initialize(WorldSettings settings) {
/*  717 */     if (!this.worldInfo.isInitialized()) {
/*      */ 
/*      */       
/*      */       try {
/*  721 */         createSpawnPosition(settings);
/*      */         
/*  723 */         if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */         {
/*  725 */           setDebugWorldSettings();
/*      */         }
/*      */         
/*  728 */         super.initialize(settings);
/*      */       }
/*  730 */       catch (Throwable throwable) {
/*      */         
/*  732 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");
/*      */ 
/*      */         
/*      */         try {
/*  736 */           addWorldInfoToCrashReport(crashreport);
/*      */         }
/*  738 */         catch (Throwable throwable1) {}
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  743 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */       
/*  746 */       this.worldInfo.setServerInitialized(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setDebugWorldSettings() {
/*  752 */     this.worldInfo.setMapFeaturesEnabled(false);
/*  753 */     this.worldInfo.setAllowCommands(true);
/*  754 */     this.worldInfo.setRaining(false);
/*  755 */     this.worldInfo.setThundering(false);
/*  756 */     this.worldInfo.setCleanWeatherTime(1000000000);
/*  757 */     this.worldInfo.setWorldTime(6000L);
/*  758 */     this.worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
/*  759 */     this.worldInfo.setHardcore(false);
/*  760 */     this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
/*  761 */     this.worldInfo.setDifficultyLocked(true);
/*  762 */     getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
/*      */   }
/*      */ 
/*      */   
/*      */   private void createSpawnPosition(WorldSettings settings) {
/*  767 */     if (!this.provider.canRespawnHere()) {
/*      */       
/*  769 */       this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
/*      */     }
/*  771 */     else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
/*      */       
/*  773 */       this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
/*      */     }
/*      */     else {
/*      */       
/*  777 */       this.findingSpawnPoint = true;
/*  778 */       WorldChunkManager worldchunkmanager = this.provider.getWorldChunkManager();
/*  779 */       List<BiomeGenBase> list = worldchunkmanager.getBiomesToSpawnIn();
/*  780 */       Random random = new Random(getSeed());
/*  781 */       BlockPos blockpos = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
/*  782 */       int i = 0;
/*  783 */       int j = this.provider.getAverageGroundLevel();
/*  784 */       int k = 0;
/*      */       
/*  786 */       if (blockpos != null) {
/*      */         
/*  788 */         i = blockpos.getX();
/*  789 */         k = blockpos.getZ();
/*      */       }
/*      */       else {
/*      */         
/*  793 */         logger.warn("Unable to find spawn biome");
/*      */       } 
/*      */       
/*  796 */       int l = 0;
/*      */       
/*  798 */       while (!this.provider.canCoordinateBeSpawn(i, k)) {
/*      */         
/*  800 */         i += random.nextInt(64) - random.nextInt(64);
/*  801 */         k += random.nextInt(64) - random.nextInt(64);
/*  802 */         l++;
/*      */         
/*  804 */         if (l == 1000) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  810 */       this.worldInfo.setSpawn(new BlockPos(i, j, k));
/*  811 */       this.findingSpawnPoint = false;
/*      */       
/*  813 */       if (settings.isBonusChestEnabled())
/*      */       {
/*  815 */         createBonusChest();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void createBonusChest() {
/*  822 */     WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(bonusChestContent, 10);
/*      */     
/*  824 */     for (int i = 0; i < 10; i++) {
/*      */       
/*  826 */       int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
/*  827 */       int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
/*  828 */       BlockPos blockpos = getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();
/*      */       
/*  830 */       if (worldgeneratorbonuschest.generate(this, this.rand, blockpos)) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos getSpawnCoordinate() {
/*  839 */     return this.provider.getSpawnCoordinate();
/*      */   }
/*      */ 
/*      */   
/*      */   public void saveAllChunks(boolean p_73044_1_, IProgressUpdate progressCallback) throws MinecraftException {
/*  844 */     if (this.chunkProvider.canSave()) {
/*      */       
/*  846 */       if (progressCallback != null)
/*      */       {
/*  848 */         progressCallback.displaySavingString("Saving level");
/*      */       }
/*      */       
/*  851 */       saveLevel();
/*      */       
/*  853 */       if (progressCallback != null)
/*      */       {
/*  855 */         progressCallback.displayLoadingString("Saving chunks");
/*      */       }
/*      */       
/*  858 */       this.chunkProvider.saveChunks(p_73044_1_, progressCallback);
/*      */       
/*  860 */       for (Chunk chunk : Lists.newArrayList(this.theChunkProviderServer.func_152380_a())) {
/*      */         
/*  862 */         if (chunk != null && !this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition))
/*      */         {
/*  864 */           this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void saveChunkData() {
/*  872 */     if (this.chunkProvider.canSave())
/*      */     {
/*  874 */       this.chunkProvider.saveExtraData();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void saveLevel() throws MinecraftException {
/*  880 */     checkSessionLock();
/*  881 */     this.worldInfo.setBorderSize(getWorldBorder().getDiameter());
/*  882 */     this.worldInfo.getBorderCenterX(getWorldBorder().getCenterX());
/*  883 */     this.worldInfo.getBorderCenterZ(getWorldBorder().getCenterZ());
/*  884 */     this.worldInfo.setBorderSafeZone(getWorldBorder().getDamageBuffer());
/*  885 */     this.worldInfo.setBorderDamagePerBlock(getWorldBorder().getDamageAmount());
/*  886 */     this.worldInfo.setBorderWarningDistance(getWorldBorder().getWarningDistance());
/*  887 */     this.worldInfo.setBorderWarningTime(getWorldBorder().getWarningTime());
/*  888 */     this.worldInfo.setBorderLerpTarget(getWorldBorder().getTargetSize());
/*  889 */     this.worldInfo.setBorderLerpTime(getWorldBorder().getTimeUntilTarget());
/*  890 */     this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getConfigurationManager().getHostPlayerData());
/*  891 */     this.mapStorage.saveAllData();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEntityAdded(Entity entityIn) {
/*  896 */     super.onEntityAdded(entityIn);
/*  897 */     this.entitiesById.addKey(entityIn.getEntityId(), entityIn);
/*  898 */     this.entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
/*  899 */     Entity[] aentity = entityIn.getParts();
/*      */     
/*  901 */     if (aentity != null)
/*      */     {
/*  903 */       for (int i = 0; i < aentity.length; i++)
/*      */       {
/*  905 */         this.entitiesById.addKey(aentity[i].getEntityId(), aentity[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEntityRemoved(Entity entityIn) {
/*  912 */     super.onEntityRemoved(entityIn);
/*  913 */     this.entitiesById.removeObject(entityIn.getEntityId());
/*  914 */     this.entitiesByUuid.remove(entityIn.getUniqueID());
/*  915 */     Entity[] aentity = entityIn.getParts();
/*      */     
/*  917 */     if (aentity != null)
/*      */     {
/*  919 */       for (int i = 0; i < aentity.length; i++)
/*      */       {
/*  921 */         this.entitiesById.removeObject(aentity[i].getEntityId());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addWeatherEffect(Entity entityIn) {
/*  928 */     if (super.addWeatherEffect(entityIn)) {
/*      */       
/*  930 */       this.mcServer.getConfigurationManager().sendToAllNear(entityIn.posX, entityIn.posY, entityIn.posZ, 512.0D, this.provider.getDimensionId(), (Packet)new S2CPacketSpawnGlobalEntity(entityIn));
/*  931 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  935 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEntityState(Entity entityIn, byte state) {
/*  941 */     getEntityTracker().func_151248_b(entityIn, (Packet)new S19PacketEntityStatus(entityIn, state));
/*      */   }
/*      */ 
/*      */   
/*      */   public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
/*  946 */     Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
/*  947 */     explosion.doExplosionA();
/*  948 */     explosion.doExplosionB(false);
/*      */     
/*  950 */     if (!isSmoking)
/*      */     {
/*  952 */       explosion.clearAffectedBlockPositions();
/*      */     }
/*      */     
/*  955 */     for (EntityPlayer entityplayer : this.playerEntities) {
/*      */       
/*  957 */       if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
/*      */       {
/*  959 */         ((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacket((Packet)new S27PacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
/*      */       }
/*      */     } 
/*      */     
/*  963 */     return explosion;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
/*  968 */     BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);
/*      */     
/*  970 */     for (BlockEventData blockeventdata1 : this.blockEventQueue[this.blockEventCacheIndex]) {
/*      */       
/*  972 */       if (blockeventdata1.equals(blockeventdata)) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  978 */     this.blockEventQueue[this.blockEventCacheIndex].add(blockeventdata);
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendQueuedBlockEvents() {
/*  983 */     while (!this.blockEventQueue[this.blockEventCacheIndex].isEmpty()) {
/*      */       
/*  985 */       int i = this.blockEventCacheIndex;
/*  986 */       this.blockEventCacheIndex ^= 0x1;
/*      */       
/*  988 */       for (BlockEventData blockeventdata : this.blockEventQueue[i]) {
/*      */         
/*  990 */         if (fireBlockEvent(blockeventdata))
/*      */         {
/*  992 */           this.mcServer.getConfigurationManager().sendToAllNear(blockeventdata.getPosition().getX(), blockeventdata.getPosition().getY(), blockeventdata.getPosition().getZ(), 64.0D, this.provider.getDimensionId(), (Packet)new S24PacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
/*      */         }
/*      */       } 
/*      */       
/*  996 */       this.blockEventQueue[i].clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean fireBlockEvent(BlockEventData event) {
/* 1002 */     IBlockState iblockstate = getBlockState(event.getPosition());
/* 1003 */     return (iblockstate.getBlock() == event.getBlock()) ? iblockstate.getBlock().onBlockEventReceived(this, event.getPosition(), iblockstate, event.getEventID(), event.getEventParameter()) : false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() {
/* 1008 */     this.saveHandler.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateWeather() {
/* 1013 */     boolean flag = isRaining();
/* 1014 */     super.updateWeather();
/*      */     
/* 1016 */     if (this.prevRainingStrength != this.rainingStrength)
/*      */     {
/* 1018 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension((Packet)new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
/*      */     }
/*      */     
/* 1021 */     if (this.prevThunderingStrength != this.thunderingStrength)
/*      */     {
/* 1023 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension((Packet)new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
/*      */     }
/*      */     
/* 1026 */     if (flag != isRaining()) {
/*      */       
/* 1028 */       if (flag) {
/*      */         
/* 1030 */         this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(2, 0.0F));
/*      */       }
/*      */       else {
/*      */         
/* 1034 */         this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(1, 0.0F));
/*      */       } 
/*      */       
/* 1037 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(7, this.rainingStrength));
/* 1038 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(8, this.thunderingStrength));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getRenderDistanceChunks() {
/* 1044 */     return this.mcServer.getConfigurationManager().getViewDistance();
/*      */   }
/*      */ 
/*      */   
/*      */   public MinecraftServer getMinecraftServer() {
/* 1049 */     return this.mcServer;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityTracker getEntityTracker() {
/* 1054 */     return this.theEntityTracker;
/*      */   }
/*      */ 
/*      */   
/*      */   public PlayerManager getPlayerManager() {
/* 1059 */     return this.thePlayerManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public Teleporter getDefaultTeleporter() {
/* 1064 */     return this.worldTeleporter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments) {
/* 1069 */     spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, xOffset, yOffset, zOffset, particleSpeed, particleArguments);
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnParticle(EnumParticleTypes particleType, boolean longDistance, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments) {
/* 1074 */     S2APacketParticles s2APacketParticles = new S2APacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, particleArguments);
/*      */     
/* 1076 */     for (int i = 0; i < this.playerEntities.size(); i++) {
/*      */       
/* 1078 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntities.get(i);
/* 1079 */       BlockPos blockpos = entityplayermp.getPosition();
/* 1080 */       double d0 = blockpos.distanceSq(xCoord, yCoord, zCoord);
/*      */       
/* 1082 */       if (d0 <= 256.0D || (longDistance && d0 <= 65536.0D))
/*      */       {
/* 1084 */         entityplayermp.playerNetServerHandler.sendPacket((Packet)s2APacketParticles);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getEntityFromUuid(UUID uuid) {
/* 1091 */     return this.entitiesByUuid.get(uuid);
/*      */   }
/*      */ 
/*      */   
/*      */   public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
/* 1096 */     return this.mcServer.addScheduledTask(runnableToSchedule);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCallingFromMinecraftThread() {
/* 1101 */     return this.mcServer.isCallingFromMinecraftThread();
/*      */   }
/*      */   
/*      */   static class ServerBlockEventList extends ArrayList<BlockEventData> {
/*      */     private ServerBlockEventList() {}
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\WorldServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */