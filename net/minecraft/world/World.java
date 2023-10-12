/*      */ package net.minecraft.world;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockLiquid;
/*      */ import net.minecraft.block.BlockSlab;
/*      */ import net.minecraft.block.BlockSnow;
/*      */ import net.minecraft.block.BlockStairs;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EntitySelectors;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.ITickable;
/*      */ import net.minecraft.util.IntHashMap;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.village.VillageCollection;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.MapStorage;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import rip.diavlo.base.viaversion.viamcp.fixes.FixedSoundEngine;
/*      */ 
/*      */ public abstract class World
/*      */   implements IBlockAccess {
/*   59 */   private int seaLevel = 63;
/*      */   protected boolean scheduledUpdatesAreImmediate;
/*   61 */   public final List<Entity> loadedEntityList = Lists.newArrayList();
/*   62 */   protected final List<Entity> unloadedEntityList = Lists.newArrayList();
/*   63 */   public final List<TileEntity> loadedTileEntityList = Lists.newArrayList();
/*   64 */   public final List<TileEntity> tickableTileEntities = Lists.newArrayList();
/*   65 */   private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
/*   66 */   private final List<TileEntity> tileEntitiesToBeRemoved = Lists.newArrayList();
/*   67 */   public final List<EntityPlayer> playerEntities = Lists.newArrayList();
/*   68 */   public final List<Entity> weatherEffects = Lists.newArrayList();
/*   69 */   protected final IntHashMap<Entity> entitiesById = new IntHashMap();
/*   70 */   private long cloudColour = 16777215L;
/*      */   private int skylightSubtracted;
/*   72 */   protected int updateLCG = (new Random()).nextInt();
/*   73 */   protected final int DIST_HASH_MAGIC = 1013904223;
/*      */   protected float prevRainingStrength;
/*      */   protected float rainingStrength;
/*      */   protected float prevThunderingStrength;
/*      */   protected float thunderingStrength;
/*      */   private int lastLightningBolt;
/*   79 */   public final Random rand = new Random();
/*      */   public final WorldProvider provider;
/*   81 */   protected List<IWorldAccess> worldAccesses = Lists.newArrayList();
/*      */   protected IChunkProvider chunkProvider;
/*      */   protected final ISaveHandler saveHandler;
/*      */   protected WorldInfo worldInfo;
/*      */   protected boolean findingSpawnPoint;
/*      */   protected MapStorage mapStorage;
/*      */   protected VillageCollection villageCollectionObj;
/*      */   public final Profiler theProfiler;
/*   89 */   private final Calendar theCalendar = Calendar.getInstance();
/*   90 */   protected Scoreboard worldScoreboard = new Scoreboard();
/*      */   public final boolean isRemote;
/*   92 */   protected Set<ChunkCoordIntPair> activeChunkSet = Sets.newHashSet();
/*      */   
/*      */   private int ambientTickCountdown;
/*      */   protected boolean spawnHostileMobs;
/*      */   protected boolean spawnPeacefulMobs;
/*      */   private boolean processingLoadedTiles;
/*      */   private final WorldBorder worldBorder;
/*      */   int[] lightUpdateBlockList;
/*      */   
/*      */   protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
/*  102 */     this.ambientTickCountdown = this.rand.nextInt(12000);
/*  103 */     this.spawnHostileMobs = true;
/*  104 */     this.spawnPeacefulMobs = true;
/*  105 */     this.lightUpdateBlockList = new int[32768];
/*  106 */     this.saveHandler = saveHandlerIn;
/*  107 */     this.theProfiler = profilerIn;
/*  108 */     this.worldInfo = info;
/*  109 */     this.provider = providerIn;
/*  110 */     this.isRemote = client;
/*  111 */     this.worldBorder = providerIn.getWorldBorder();
/*      */   }
/*      */ 
/*      */   
/*      */   public World init() {
/*  116 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
/*  121 */     if (isBlockLoaded(pos)) {
/*      */       
/*  123 */       Chunk chunk = getChunkFromBlockCoords(pos);
/*      */ 
/*      */       
/*      */       try {
/*  127 */         return chunk.getBiome(pos, this.provider.getWorldChunkManager());
/*      */       }
/*  129 */       catch (Throwable throwable) {
/*      */         
/*  131 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
/*  132 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
/*  133 */         crashreportcategory.addCrashSectionCallable("Location", new Callable<String>()
/*      */             {
/*      */               public String call() throws Exception
/*      */               {
/*  137 */                 return CrashReportCategory.getCoordinateInfo(pos);
/*      */               }
/*      */             });
/*  140 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  145 */     return this.provider.getWorldChunkManager().getBiomeGenerator(pos, BiomeGenBase.plains);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public WorldChunkManager getWorldChunkManager() {
/*  151 */     return this.provider.getWorldChunkManager();
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract IChunkProvider createChunkProvider();
/*      */   
/*      */   public void initialize(WorldSettings settings) {
/*  158 */     this.worldInfo.setServerInitialized(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInitialSpawnLocation() {
/*  163 */     setSpawnPoint(new BlockPos(8, 64, 8));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Block getGroundAboveSeaLevel(BlockPos pos) {
/*      */     BlockPos blockpos;
/*  170 */     for (blockpos = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ()); !isAirBlock(blockpos.up()); blockpos = blockpos.up());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  175 */     return getBlockState(blockpos).getBlock();
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isValid(BlockPos pos) {
/*  180 */     return (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAirBlock(BlockPos pos) {
/*  185 */     return (getBlockState(pos).getBlock().getMaterial() == Material.air);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockLoaded(BlockPos pos) {
/*  190 */     return isBlockLoaded(pos, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty) {
/*  195 */     return !isValid(pos) ? false : isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos center, int radius) {
/*  200 */     return isAreaLoaded(center, radius, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty) {
/*  205 */     return isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos from, BlockPos to) {
/*  210 */     return isAreaLoaded(from, to, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty) {
/*  215 */     return isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAreaLoaded(StructureBoundingBox box) {
/*  220 */     return isAreaLoaded(box, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty) {
/*  225 */     return isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty) {
/*  230 */     if (yEnd >= 0 && yStart < 256) {
/*      */       
/*  232 */       xStart >>= 4;
/*  233 */       zStart >>= 4;
/*  234 */       xEnd >>= 4;
/*  235 */       zEnd >>= 4;
/*      */       
/*  237 */       for (int i = xStart; i <= xEnd; i++) {
/*      */         
/*  239 */         for (int j = zStart; j <= zEnd; j++) {
/*      */           
/*  241 */           if (!isChunkLoaded(i, j, allowEmpty))
/*      */           {
/*  243 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  248 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  252 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
/*  258 */     return (this.chunkProvider.chunkExists(x, z) && (allowEmpty || !this.chunkProvider.provideChunk(x, z).isEmpty()));
/*      */   }
/*      */ 
/*      */   
/*      */   public Chunk getChunkFromBlockCoords(BlockPos pos) {
/*  263 */     return getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
/*  268 */     return this.chunkProvider.provideChunk(chunkX, chunkZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
/*  273 */     if (!isValid(pos))
/*      */     {
/*  275 */       return false;
/*      */     }
/*  277 */     if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
/*      */     {
/*  279 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  283 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  284 */     Block block = newState.getBlock();
/*  285 */     IBlockState iblockstate = chunk.setBlockState(pos, newState);
/*      */     
/*  287 */     if (iblockstate == null)
/*      */     {
/*  289 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  293 */     Block block1 = iblockstate.getBlock();
/*      */     
/*  295 */     if (block.getLightOpacity() != block1.getLightOpacity() || block.getLightValue() != block1.getLightValue()) {
/*      */       
/*  297 */       this.theProfiler.startSection("checkLight");
/*  298 */       checkLight(pos);
/*  299 */       this.theProfiler.endSection();
/*      */     } 
/*      */     
/*  302 */     if ((flags & 0x2) != 0 && (!this.isRemote || (flags & 0x4) == 0) && chunk.isPopulated())
/*      */     {
/*  304 */       markBlockForUpdate(pos);
/*      */     }
/*      */     
/*  307 */     if (!this.isRemote && (flags & 0x1) != 0) {
/*      */       
/*  309 */       notifyNeighborsRespectDebug(pos, iblockstate.getBlock());
/*      */       
/*  311 */       if (block.hasComparatorInputOverride())
/*      */       {
/*  313 */         updateComparatorOutputLevel(pos, block);
/*      */       }
/*      */     } 
/*      */     
/*  317 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setBlockToAir(BlockPos pos) {
/*  324 */     return setBlockState(pos, Blocks.air.getDefaultState(), 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
/*  329 */     return FixedSoundEngine.destroyBlock(this, pos, dropBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setBlockState(BlockPos pos, IBlockState state) {
/*  356 */     return setBlockState(pos, state, 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void markBlockForUpdate(BlockPos pos) {
/*  361 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  363 */       ((IWorldAccess)this.worldAccesses.get(i)).markBlockForUpdate(pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType) {
/*  369 */     if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
/*      */     {
/*  371 */       notifyNeighborsOfStateChange(pos, blockType);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void markBlocksDirtyVertical(int x1, int z1, int x2, int z2) {
/*  377 */     if (x2 > z2) {
/*      */       
/*  379 */       int i = z2;
/*  380 */       z2 = x2;
/*  381 */       x2 = i;
/*      */     } 
/*      */     
/*  384 */     if (!this.provider.getHasNoSky())
/*      */     {
/*  386 */       for (int j = x2; j <= z2; j++)
/*      */       {
/*  388 */         checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));
/*      */       }
/*      */     }
/*      */     
/*  392 */     markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax) {
/*  397 */     markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
/*      */   }
/*      */ 
/*      */   
/*      */   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
/*  402 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  404 */       ((IWorldAccess)this.worldAccesses.get(i)).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType) {
/*  410 */     notifyBlockOfStateChange(pos.west(), blockType);
/*  411 */     notifyBlockOfStateChange(pos.east(), blockType);
/*  412 */     notifyBlockOfStateChange(pos.down(), blockType);
/*  413 */     notifyBlockOfStateChange(pos.up(), blockType);
/*  414 */     notifyBlockOfStateChange(pos.north(), blockType);
/*  415 */     notifyBlockOfStateChange(pos.south(), blockType);
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide) {
/*  420 */     if (skipSide != EnumFacing.WEST)
/*      */     {
/*  422 */       notifyBlockOfStateChange(pos.west(), blockType);
/*      */     }
/*      */     
/*  425 */     if (skipSide != EnumFacing.EAST)
/*      */     {
/*  427 */       notifyBlockOfStateChange(pos.east(), blockType);
/*      */     }
/*      */     
/*  430 */     if (skipSide != EnumFacing.DOWN)
/*      */     {
/*  432 */       notifyBlockOfStateChange(pos.down(), blockType);
/*      */     }
/*      */     
/*  435 */     if (skipSide != EnumFacing.UP)
/*      */     {
/*  437 */       notifyBlockOfStateChange(pos.up(), blockType);
/*      */     }
/*      */     
/*  440 */     if (skipSide != EnumFacing.NORTH)
/*      */     {
/*  442 */       notifyBlockOfStateChange(pos.north(), blockType);
/*      */     }
/*      */     
/*  445 */     if (skipSide != EnumFacing.SOUTH)
/*      */     {
/*  447 */       notifyBlockOfStateChange(pos.south(), blockType);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyBlockOfStateChange(BlockPos pos, final Block blockIn) {
/*  453 */     if (!this.isRemote) {
/*      */       
/*  455 */       IBlockState iblockstate = getBlockState(pos);
/*      */ 
/*      */       
/*      */       try {
/*  459 */         iblockstate.getBlock().onNeighborBlockChange(this, pos, iblockstate, blockIn);
/*      */       }
/*  461 */       catch (Throwable throwable) {
/*      */         
/*  463 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
/*  464 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
/*  465 */         crashreportcategory.addCrashSectionCallable("Source block type", new Callable<String>()
/*      */             {
/*      */               
/*      */               public String call() throws Exception
/*      */               {
/*      */                 try {
/*  471 */                   return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getIdFromBlock(this.val$blockIn)), this.val$blockIn.getUnlocalizedName(), this.val$blockIn.getClass().getCanonicalName() });
/*      */                 }
/*  473 */                 catch (Throwable var2) {
/*      */                   
/*  475 */                   return "ID #" + Block.getIdFromBlock(blockIn);
/*      */                 } 
/*      */               }
/*      */             });
/*  479 */         CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
/*  480 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockTickPending(BlockPos pos, Block blockType) {
/*  487 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canSeeSky(BlockPos pos) {
/*  492 */     return getChunkFromBlockCoords(pos).canSeeSky(pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBlockSeeSky(BlockPos pos) {
/*  497 */     if (pos.getY() >= getSeaLevel())
/*      */     {
/*  499 */       return canSeeSky(pos);
/*      */     }
/*      */ 
/*      */     
/*  503 */     BlockPos blockpos = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ());
/*      */     
/*  505 */     if (!canSeeSky(blockpos))
/*      */     {
/*  507 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  511 */     for (blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down()) {
/*      */       
/*  513 */       Block block = getBlockState(blockpos).getBlock();
/*      */       
/*  515 */       if (block.getLightOpacity() > 0 && !block.getMaterial().isLiquid())
/*      */       {
/*  517 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  521 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLight(BlockPos pos) {
/*  528 */     if (pos.getY() < 0)
/*      */     {
/*  530 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  534 */     if (pos.getY() >= 256)
/*      */     {
/*  536 */       pos = new BlockPos(pos.getX(), 255, pos.getZ());
/*      */     }
/*      */     
/*  539 */     return getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLightFromNeighbors(BlockPos pos) {
/*  545 */     return getLight(pos, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLight(BlockPos pos, boolean checkNeighbors) {
/*  550 */     if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
/*      */       
/*  552 */       if (checkNeighbors && getBlockState(pos).getBlock().getUseNeighborBrightness()) {
/*      */         
/*  554 */         int i1 = getLight(pos.up(), false);
/*  555 */         int i = getLight(pos.east(), false);
/*  556 */         int j = getLight(pos.west(), false);
/*  557 */         int k = getLight(pos.south(), false);
/*  558 */         int l = getLight(pos.north(), false);
/*      */         
/*  560 */         if (i > i1)
/*      */         {
/*  562 */           i1 = i;
/*      */         }
/*      */         
/*  565 */         if (j > i1)
/*      */         {
/*  567 */           i1 = j;
/*      */         }
/*      */         
/*  570 */         if (k > i1)
/*      */         {
/*  572 */           i1 = k;
/*      */         }
/*      */         
/*  575 */         if (l > i1)
/*      */         {
/*  577 */           i1 = l;
/*      */         }
/*      */         
/*  580 */         return i1;
/*      */       } 
/*  582 */       if (pos.getY() < 0)
/*      */       {
/*  584 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*  588 */       if (pos.getY() >= 256)
/*      */       {
/*  590 */         pos = new BlockPos(pos.getX(), 255, pos.getZ());
/*      */       }
/*      */       
/*  593 */       Chunk chunk = getChunkFromBlockCoords(pos);
/*  594 */       return chunk.getLightSubtracted(pos, this.skylightSubtracted);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  599 */     return 15;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos getHeight(BlockPos pos) {
/*      */     int i;
/*  607 */     if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
/*      */       
/*  609 */       if (isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, true))
/*      */       {
/*  611 */         i = getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
/*      */       }
/*      */       else
/*      */       {
/*  615 */         i = 0;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  620 */       i = getSeaLevel() + 1;
/*      */     } 
/*      */     
/*  623 */     return new BlockPos(pos.getX(), i, pos.getZ());
/*      */   }
/*      */ 
/*      */   
/*      */   public int getChunksLowestHorizon(int x, int z) {
/*  628 */     if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
/*      */       
/*  630 */       if (!isChunkLoaded(x >> 4, z >> 4, true))
/*      */       {
/*  632 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*  636 */       Chunk chunk = getChunkFromChunkCoords(x >> 4, z >> 4);
/*  637 */       return chunk.getLowestHeight();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  642 */     return getSeaLevel() + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos) {
/*  648 */     if (this.provider.getHasNoSky() && type == EnumSkyBlock.SKY)
/*      */     {
/*  650 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  654 */     if (pos.getY() < 0)
/*      */     {
/*  656 */       pos = new BlockPos(pos.getX(), 0, pos.getZ());
/*      */     }
/*      */     
/*  659 */     if (!isValid(pos))
/*      */     {
/*  661 */       return type.defaultLightValue;
/*      */     }
/*  663 */     if (!isBlockLoaded(pos))
/*      */     {
/*  665 */       return type.defaultLightValue;
/*      */     }
/*  667 */     if (getBlockState(pos).getBlock().getUseNeighborBrightness()) {
/*      */       
/*  669 */       int i1 = getLightFor(type, pos.up());
/*  670 */       int i = getLightFor(type, pos.east());
/*  671 */       int j = getLightFor(type, pos.west());
/*  672 */       int k = getLightFor(type, pos.south());
/*  673 */       int l = getLightFor(type, pos.north());
/*      */       
/*  675 */       if (i > i1)
/*      */       {
/*  677 */         i1 = i;
/*      */       }
/*      */       
/*  680 */       if (j > i1)
/*      */       {
/*  682 */         i1 = j;
/*      */       }
/*      */       
/*  685 */       if (k > i1)
/*      */       {
/*  687 */         i1 = k;
/*      */       }
/*      */       
/*  690 */       if (l > i1)
/*      */       {
/*  692 */         i1 = l;
/*      */       }
/*      */       
/*  695 */       return i1;
/*      */     } 
/*      */ 
/*      */     
/*  699 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  700 */     return chunk.getLightFor(type, pos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLightFor(EnumSkyBlock type, BlockPos pos) {
/*  707 */     if (pos.getY() < 0)
/*      */     {
/*  709 */       pos = new BlockPos(pos.getX(), 0, pos.getZ());
/*      */     }
/*      */     
/*  712 */     if (!isValid(pos))
/*      */     {
/*  714 */       return type.defaultLightValue;
/*      */     }
/*  716 */     if (!isBlockLoaded(pos))
/*      */     {
/*  718 */       return type.defaultLightValue;
/*      */     }
/*      */ 
/*      */     
/*  722 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  723 */     return chunk.getLightFor(type, pos);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue) {
/*  729 */     if (isValid(pos))
/*      */     {
/*  731 */       if (isBlockLoaded(pos)) {
/*      */         
/*  733 */         Chunk chunk = getChunkFromBlockCoords(pos);
/*  734 */         chunk.setLightFor(type, pos, lightValue);
/*  735 */         notifyLightSet(pos);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyLightSet(BlockPos pos) {
/*  742 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  744 */       ((IWorldAccess)this.worldAccesses.get(i)).notifyLightSet(pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCombinedLight(BlockPos pos, int lightValue) {
/*  750 */     int i = getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
/*  751 */     int j = getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
/*      */     
/*  753 */     if (j < lightValue)
/*      */     {
/*  755 */       j = lightValue;
/*      */     }
/*      */     
/*  758 */     return i << 20 | j << 4;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getLightBrightness(BlockPos pos) {
/*  763 */     return this.provider.getLightBrightnessTable()[getLightFromNeighbors(pos)];
/*      */   }
/*      */ 
/*      */   
/*      */   public IBlockState getBlockState(BlockPos pos) {
/*  768 */     if (!isValid(pos))
/*      */     {
/*  770 */       return Blocks.air.getDefaultState();
/*      */     }
/*      */ 
/*      */     
/*  774 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*  775 */     return chunk.getBlockState(pos);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDaytime() {
/*  781 */     return (this.skylightSubtracted < 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public MovingObjectPosition rayTraceBlocks(Vec3 p_72933_1_, Vec3 p_72933_2_) {
/*  786 */     return rayTraceBlocks(p_72933_1_, p_72933_2_, false, false, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public MovingObjectPosition rayTraceBlocks(Vec3 start, Vec3 end, boolean stopOnLiquid) {
/*  791 */     return rayTraceBlocks(start, end, stopOnLiquid, false, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public MovingObjectPosition rayTraceBlocks(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
/*  796 */     if (!Double.isNaN(vec31.xCoord) && !Double.isNaN(vec31.yCoord) && !Double.isNaN(vec31.zCoord)) {
/*      */       
/*  798 */       if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord)) {
/*      */         
/*  800 */         int i = MathHelper.floor_double(vec32.xCoord);
/*  801 */         int j = MathHelper.floor_double(vec32.yCoord);
/*  802 */         int k = MathHelper.floor_double(vec32.zCoord);
/*  803 */         int l = MathHelper.floor_double(vec31.xCoord);
/*  804 */         int i1 = MathHelper.floor_double(vec31.yCoord);
/*  805 */         int j1 = MathHelper.floor_double(vec31.zCoord);
/*  806 */         BlockPos blockpos = new BlockPos(l, i1, j1);
/*  807 */         IBlockState iblockstate = getBlockState(blockpos);
/*  808 */         Block block = iblockstate.getBlock();
/*      */         
/*  810 */         if ((!ignoreBlockWithoutBoundingBox || block.getCollisionBoundingBox(this, blockpos, iblockstate) != null) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
/*      */           
/*  812 */           MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, blockpos, vec31, vec32);
/*      */           
/*  814 */           if (movingobjectposition != null)
/*      */           {
/*  816 */             return movingobjectposition;
/*      */           }
/*      */         } 
/*      */         
/*  820 */         MovingObjectPosition movingobjectposition2 = null;
/*  821 */         int k1 = 200;
/*      */         
/*  823 */         while (k1-- >= 0) {
/*      */           EnumFacing enumfacing;
/*  825 */           if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord))
/*      */           {
/*  827 */             return null;
/*      */           }
/*      */           
/*  830 */           if (l == i && i1 == j && j1 == k)
/*      */           {
/*  832 */             return returnLastUncollidableBlock ? movingobjectposition2 : null;
/*      */           }
/*      */           
/*  835 */           boolean flag2 = true;
/*  836 */           boolean flag = true;
/*  837 */           boolean flag1 = true;
/*  838 */           double d0 = 999.0D;
/*  839 */           double d1 = 999.0D;
/*  840 */           double d2 = 999.0D;
/*      */           
/*  842 */           if (i > l) {
/*      */             
/*  844 */             d0 = l + 1.0D;
/*      */           }
/*  846 */           else if (i < l) {
/*      */             
/*  848 */             d0 = l + 0.0D;
/*      */           }
/*      */           else {
/*      */             
/*  852 */             flag2 = false;
/*      */           } 
/*      */           
/*  855 */           if (j > i1) {
/*      */             
/*  857 */             d1 = i1 + 1.0D;
/*      */           }
/*  859 */           else if (j < i1) {
/*      */             
/*  861 */             d1 = i1 + 0.0D;
/*      */           }
/*      */           else {
/*      */             
/*  865 */             flag = false;
/*      */           } 
/*      */           
/*  868 */           if (k > j1) {
/*      */             
/*  870 */             d2 = j1 + 1.0D;
/*      */           }
/*  872 */           else if (k < j1) {
/*      */             
/*  874 */             d2 = j1 + 0.0D;
/*      */           }
/*      */           else {
/*      */             
/*  878 */             flag1 = false;
/*      */           } 
/*      */           
/*  881 */           double d3 = 999.0D;
/*  882 */           double d4 = 999.0D;
/*  883 */           double d5 = 999.0D;
/*  884 */           double d6 = vec32.xCoord - vec31.xCoord;
/*  885 */           double d7 = vec32.yCoord - vec31.yCoord;
/*  886 */           double d8 = vec32.zCoord - vec31.zCoord;
/*      */           
/*  888 */           if (flag2)
/*      */           {
/*  890 */             d3 = (d0 - vec31.xCoord) / d6;
/*      */           }
/*      */           
/*  893 */           if (flag)
/*      */           {
/*  895 */             d4 = (d1 - vec31.yCoord) / d7;
/*      */           }
/*      */           
/*  898 */           if (flag1)
/*      */           {
/*  900 */             d5 = (d2 - vec31.zCoord) / d8;
/*      */           }
/*      */           
/*  903 */           if (d3 == -0.0D)
/*      */           {
/*  905 */             d3 = -1.0E-4D;
/*      */           }
/*      */           
/*  908 */           if (d4 == -0.0D)
/*      */           {
/*  910 */             d4 = -1.0E-4D;
/*      */           }
/*      */           
/*  913 */           if (d5 == -0.0D)
/*      */           {
/*  915 */             d5 = -1.0E-4D;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  920 */           if (d3 < d4 && d3 < d5) {
/*      */             
/*  922 */             enumfacing = (i > l) ? EnumFacing.WEST : EnumFacing.EAST;
/*  923 */             vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
/*      */           }
/*  925 */           else if (d4 < d5) {
/*      */             
/*  927 */             enumfacing = (j > i1) ? EnumFacing.DOWN : EnumFacing.UP;
/*  928 */             vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
/*      */           }
/*      */           else {
/*      */             
/*  932 */             enumfacing = (k > j1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
/*  933 */             vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
/*      */           } 
/*      */           
/*  936 */           l = MathHelper.floor_double(vec31.xCoord) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
/*  937 */           i1 = MathHelper.floor_double(vec31.yCoord) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
/*  938 */           j1 = MathHelper.floor_double(vec31.zCoord) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
/*  939 */           blockpos = new BlockPos(l, i1, j1);
/*  940 */           IBlockState iblockstate1 = getBlockState(blockpos);
/*  941 */           Block block1 = iblockstate1.getBlock();
/*      */           
/*  943 */           if (!ignoreBlockWithoutBoundingBox || block1.getCollisionBoundingBox(this, blockpos, iblockstate1) != null) {
/*      */             
/*  945 */             if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
/*      */               
/*  947 */               MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, blockpos, vec31, vec32);
/*      */               
/*  949 */               if (movingobjectposition1 != null)
/*      */               {
/*  951 */                 return movingobjectposition1;
/*      */               }
/*      */               
/*      */               continue;
/*      */             } 
/*  956 */             movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  961 */         return returnLastUncollidableBlock ? movingobjectposition2 : null;
/*      */       } 
/*      */ 
/*      */       
/*  965 */       return null;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  970 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSoundAtEntity(Entity entityIn, String name, float volume, float pitch) {
/*  976 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  978 */       ((IWorldAccess)this.worldAccesses.get(i)).playSound(name, entityIn.posX, entityIn.posY, entityIn.posZ, volume, pitch);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSoundToNearExcept(EntityPlayer player, String name, float volume, float pitch) {
/*  984 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  986 */       ((IWorldAccess)this.worldAccesses.get(i)).playSoundToNearExcept(player, name, player.posX, player.posY, player.posZ, volume, pitch);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
/*  992 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/*  994 */       ((IWorldAccess)this.worldAccesses.get(i)).playSound(soundName, x, y, z, volume, pitch);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay) {}
/*      */ 
/*      */   
/*      */   public void playRecord(BlockPos pos, String name) {
/* 1004 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1006 */       ((IWorldAccess)this.worldAccesses.get(i)).playRecord(name, pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175688_14_) {
/* 1012 */     spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175688_14_);
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnParticle(EnumParticleTypes particleType, boolean p_175682_2_, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175682_15_) {
/* 1017 */     spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() | p_175682_2_, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175682_15_);
/*      */   }
/*      */ 
/*      */   
/*      */   private void spawnParticle(int particleID, boolean p_175720_2_, double xCood, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175720_15_) {
/* 1022 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1024 */       ((IWorldAccess)this.worldAccesses.get(i)).spawnParticle(particleID, p_175720_2_, xCood, yCoord, zCoord, xOffset, yOffset, zOffset, p_175720_15_);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addWeatherEffect(Entity entityIn) {
/* 1030 */     this.weatherEffects.add(entityIn);
/* 1031 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean spawnEntityInWorld(Entity entityIn) {
/* 1036 */     int i = MathHelper.floor_double(entityIn.posX / 16.0D);
/* 1037 */     int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
/* 1038 */     boolean flag = entityIn.forceSpawn;
/*      */     
/* 1040 */     if (entityIn instanceof EntityPlayer)
/*      */     {
/* 1042 */       flag = true;
/*      */     }
/*      */     
/* 1045 */     if (!flag && !isChunkLoaded(i, j, true))
/*      */     {
/* 1047 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1051 */     if (entityIn instanceof EntityPlayer) {
/*      */       
/* 1053 */       EntityPlayer entityplayer = (EntityPlayer)entityIn;
/* 1054 */       this.playerEntities.add(entityplayer);
/* 1055 */       updateAllPlayersSleepingFlag();
/*      */     } 
/*      */     
/* 1058 */     getChunkFromChunkCoords(i, j).addEntity(entityIn);
/* 1059 */     this.loadedEntityList.add(entityIn);
/* 1060 */     onEntityAdded(entityIn);
/* 1061 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void onEntityAdded(Entity entityIn) {
/* 1067 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1069 */       ((IWorldAccess)this.worldAccesses.get(i)).onEntityAdded(entityIn);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onEntityRemoved(Entity entityIn) {
/* 1075 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 1077 */       ((IWorldAccess)this.worldAccesses.get(i)).onEntityRemoved(entityIn);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeEntity(Entity entityIn) {
/* 1083 */     if (entityIn.riddenByEntity != null)
/*      */     {
/* 1085 */       entityIn.riddenByEntity.mountEntity((Entity)null);
/*      */     }
/*      */     
/* 1088 */     if (entityIn.ridingEntity != null)
/*      */     {
/* 1090 */       entityIn.mountEntity((Entity)null);
/*      */     }
/*      */     
/* 1093 */     entityIn.setDead();
/*      */     
/* 1095 */     if (entityIn instanceof EntityPlayer) {
/*      */       
/* 1097 */       this.playerEntities.remove(entityIn);
/* 1098 */       updateAllPlayersSleepingFlag();
/* 1099 */       onEntityRemoved(entityIn);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removePlayerEntityDangerously(Entity entityIn) {
/* 1105 */     entityIn.setDead();
/*      */     
/* 1107 */     if (entityIn instanceof EntityPlayer) {
/*      */       
/* 1109 */       this.playerEntities.remove(entityIn);
/* 1110 */       updateAllPlayersSleepingFlag();
/*      */     } 
/*      */     
/* 1113 */     int i = entityIn.chunkCoordX;
/* 1114 */     int j = entityIn.chunkCoordZ;
/*      */     
/* 1116 */     if (entityIn.addedToChunk && isChunkLoaded(i, j, true))
/*      */     {
/* 1118 */       getChunkFromChunkCoords(i, j).removeEntity(entityIn);
/*      */     }
/*      */     
/* 1121 */     this.loadedEntityList.remove(entityIn);
/* 1122 */     onEntityRemoved(entityIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addWorldAccess(IWorldAccess worldAccess) {
/* 1127 */     this.worldAccesses.add(worldAccess);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeWorldAccess(IWorldAccess worldAccess) {
/* 1132 */     this.worldAccesses.remove(worldAccess);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entityIn, AxisAlignedBB bb) {
/* 1137 */     List<AxisAlignedBB> list = Lists.newArrayList();
/* 1138 */     int i = MathHelper.floor_double(bb.minX);
/* 1139 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1140 */     int k = MathHelper.floor_double(bb.minY);
/* 1141 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1142 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1143 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 1144 */     WorldBorder worldborder = getWorldBorder();
/* 1145 */     boolean flag = entityIn.isOutsideBorder();
/* 1146 */     boolean flag1 = isInsideBorder(worldborder, entityIn);
/* 1147 */     IBlockState iblockstate = Blocks.stone.getDefaultState();
/* 1148 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1150 */     for (int k1 = i; k1 < j; k1++) {
/*      */       
/* 1152 */       for (int l1 = i1; l1 < j1; l1++) {
/*      */         
/* 1154 */         if (isBlockLoaded((BlockPos)blockpos$mutableblockpos.set(k1, 64, l1)))
/*      */         {
/* 1156 */           for (int i2 = k - 1; i2 < l; i2++) {
/*      */             
/* 1158 */             blockpos$mutableblockpos.set(k1, i2, l1);
/*      */             
/* 1160 */             if (flag && flag1) {
/*      */               
/* 1162 */               entityIn.setOutsideBorder(false);
/*      */             }
/* 1164 */             else if (!flag && !flag1) {
/*      */               
/* 1166 */               entityIn.setOutsideBorder(true);
/*      */             } 
/*      */             
/* 1169 */             IBlockState iblockstate1 = iblockstate;
/*      */             
/* 1171 */             if (worldborder.contains((BlockPos)blockpos$mutableblockpos) || !flag1)
/*      */             {
/* 1173 */               iblockstate1 = getBlockState((BlockPos)blockpos$mutableblockpos);
/*      */             }
/*      */             
/* 1176 */             iblockstate1.getBlock().addCollisionBoxesToList(this, (BlockPos)blockpos$mutableblockpos, iblockstate1, bb, list, entityIn);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1182 */     double d0 = 0.25D;
/* 1183 */     List<Entity> list1 = getEntitiesWithinAABBExcludingEntity(entityIn, bb.expand(d0, d0, d0));
/*      */     
/* 1185 */     for (int j2 = 0; j2 < list1.size(); j2++) {
/*      */       
/* 1187 */       if (entityIn.riddenByEntity != list1 && entityIn.ridingEntity != list1) {
/*      */         
/* 1189 */         AxisAlignedBB axisalignedbb = ((Entity)list1.get(j2)).getCollisionBoundingBox();
/*      */         
/* 1191 */         if (axisalignedbb != null && axisalignedbb.intersectsWith(bb))
/*      */         {
/* 1193 */           list.add(axisalignedbb);
/*      */         }
/*      */         
/* 1196 */         axisalignedbb = entityIn.getCollisionBox(list1.get(j2));
/*      */         
/* 1198 */         if (axisalignedbb != null && axisalignedbb.intersectsWith(bb))
/*      */         {
/* 1200 */           list.add(axisalignedbb);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1205 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInsideBorder(WorldBorder worldBorderIn, Entity entityIn) {
/* 1210 */     double d0 = worldBorderIn.minX();
/* 1211 */     double d1 = worldBorderIn.minZ();
/* 1212 */     double d2 = worldBorderIn.maxX();
/* 1213 */     double d3 = worldBorderIn.maxZ();
/*      */     
/* 1215 */     if (entityIn.isOutsideBorder()) {
/*      */       
/* 1217 */       d0++;
/* 1218 */       d1++;
/* 1219 */       d2--;
/* 1220 */       d3--;
/*      */     }
/*      */     else {
/*      */       
/* 1224 */       d0--;
/* 1225 */       d1--;
/* 1226 */       d2++;
/* 1227 */       d3++;
/*      */     } 
/*      */     
/* 1230 */     return (entityIn.posX > d0 && entityIn.posX < d2 && entityIn.posZ > d1 && entityIn.posZ < d3);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB bb) {
/* 1235 */     List<AxisAlignedBB> list = Lists.newArrayList();
/* 1236 */     int i = MathHelper.floor_double(bb.minX);
/* 1237 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1238 */     int k = MathHelper.floor_double(bb.minY);
/* 1239 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1240 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1241 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 1242 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1244 */     for (int k1 = i; k1 < j; k1++) {
/*      */       
/* 1246 */       for (int l1 = i1; l1 < j1; l1++) {
/*      */         
/* 1248 */         if (isBlockLoaded((BlockPos)blockpos$mutableblockpos.set(k1, 64, l1)))
/*      */         {
/* 1250 */           for (int i2 = k - 1; i2 < l; i2++) {
/*      */             IBlockState iblockstate;
/* 1252 */             blockpos$mutableblockpos.set(k1, i2, l1);
/*      */ 
/*      */             
/* 1255 */             if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) {
/*      */               
/* 1257 */               iblockstate = getBlockState((BlockPos)blockpos$mutableblockpos);
/*      */             }
/*      */             else {
/*      */               
/* 1261 */               iblockstate = Blocks.bedrock.getDefaultState();
/*      */             } 
/*      */             
/* 1264 */             iblockstate.getBlock().addCollisionBoxesToList(this, (BlockPos)blockpos$mutableblockpos, iblockstate, bb, list, (Entity)null);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1270 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public int calculateSkylightSubtracted(float p_72967_1_) {
/* 1275 */     float f = getCelestialAngle(p_72967_1_);
/* 1276 */     float f1 = 1.0F - MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 1277 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1278 */     f1 = 1.0F - f1;
/* 1279 */     f1 = (float)(f1 * (1.0D - (getRainStrength(p_72967_1_) * 5.0F) / 16.0D));
/* 1280 */     f1 = (float)(f1 * (1.0D - (getThunderStrength(p_72967_1_) * 5.0F) / 16.0D));
/* 1281 */     f1 = 1.0F - f1;
/* 1282 */     return (int)(f1 * 11.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSunBrightness(float p_72971_1_) {
/* 1287 */     float f = getCelestialAngle(p_72971_1_);
/* 1288 */     float f1 = 1.0F - MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.2F;
/* 1289 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1290 */     f1 = 1.0F - f1;
/* 1291 */     f1 = (float)(f1 * (1.0D - (getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
/* 1292 */     f1 = (float)(f1 * (1.0D - (getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
/* 1293 */     return f1 * 0.8F + 0.2F;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getSkyColor(Entity entityIn, float partialTicks) {
/* 1298 */     float f = getCelestialAngle(partialTicks);
/* 1299 */     float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 1300 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1301 */     int i = MathHelper.floor_double(entityIn.posX);
/* 1302 */     int j = MathHelper.floor_double(entityIn.posY);
/* 1303 */     int k = MathHelper.floor_double(entityIn.posZ);
/* 1304 */     BlockPos blockpos = new BlockPos(i, j, k);
/* 1305 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(blockpos);
/* 1306 */     float f2 = biomegenbase.getFloatTemperature(blockpos);
/* 1307 */     int l = biomegenbase.getSkyColorByTemp(f2);
/* 1308 */     float f3 = (l >> 16 & 0xFF) / 255.0F;
/* 1309 */     float f4 = (l >> 8 & 0xFF) / 255.0F;
/* 1310 */     float f5 = (l & 0xFF) / 255.0F;
/* 1311 */     f3 *= f1;
/* 1312 */     f4 *= f1;
/* 1313 */     f5 *= f1;
/* 1314 */     float f6 = getRainStrength(partialTicks);
/*      */     
/* 1316 */     if (f6 > 0.0F) {
/*      */       
/* 1318 */       float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
/* 1319 */       float f8 = 1.0F - f6 * 0.75F;
/* 1320 */       f3 = f3 * f8 + f7 * (1.0F - f8);
/* 1321 */       f4 = f4 * f8 + f7 * (1.0F - f8);
/* 1322 */       f5 = f5 * f8 + f7 * (1.0F - f8);
/*      */     } 
/*      */     
/* 1325 */     float f10 = getThunderStrength(partialTicks);
/*      */     
/* 1327 */     if (f10 > 0.0F) {
/*      */       
/* 1329 */       float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
/* 1330 */       float f9 = 1.0F - f10 * 0.75F;
/* 1331 */       f3 = f3 * f9 + f11 * (1.0F - f9);
/* 1332 */       f4 = f4 * f9 + f11 * (1.0F - f9);
/* 1333 */       f5 = f5 * f9 + f11 * (1.0F - f9);
/*      */     } 
/*      */     
/* 1336 */     if (this.lastLightningBolt > 0) {
/*      */       
/* 1338 */       float f12 = this.lastLightningBolt - partialTicks;
/*      */       
/* 1340 */       if (f12 > 1.0F)
/*      */       {
/* 1342 */         f12 = 1.0F;
/*      */       }
/*      */       
/* 1345 */       f12 *= 0.45F;
/* 1346 */       f3 = f3 * (1.0F - f12) + 0.8F * f12;
/* 1347 */       f4 = f4 * (1.0F - f12) + 0.8F * f12;
/* 1348 */       f5 = f5 * (1.0F - f12) + 1.0F * f12;
/*      */     } 
/*      */     
/* 1351 */     return new Vec3(f3, f4, f5);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCelestialAngle(float partialTicks) {
/* 1356 */     return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMoonPhase() {
/* 1361 */     return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCurrentMoonPhaseFactor() {
/* 1366 */     return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
/*      */   }
/*      */ 
/*      */   
/*      */   public float getCelestialAngleRadians(float partialTicks) {
/* 1371 */     float f = getCelestialAngle(partialTicks);
/* 1372 */     return f * 3.1415927F * 2.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getCloudColour(float partialTicks) {
/* 1377 */     float f = getCelestialAngle(partialTicks);
/* 1378 */     float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
/* 1379 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1380 */     float f2 = (float)(this.cloudColour >> 16L & 0xFFL) / 255.0F;
/* 1381 */     float f3 = (float)(this.cloudColour >> 8L & 0xFFL) / 255.0F;
/* 1382 */     float f4 = (float)(this.cloudColour & 0xFFL) / 255.0F;
/* 1383 */     float f5 = getRainStrength(partialTicks);
/*      */     
/* 1385 */     if (f5 > 0.0F) {
/*      */       
/* 1387 */       float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
/* 1388 */       float f7 = 1.0F - f5 * 0.95F;
/* 1389 */       f2 = f2 * f7 + f6 * (1.0F - f7);
/* 1390 */       f3 = f3 * f7 + f6 * (1.0F - f7);
/* 1391 */       f4 = f4 * f7 + f6 * (1.0F - f7);
/*      */     } 
/*      */     
/* 1394 */     f2 *= f1 * 0.9F + 0.1F;
/* 1395 */     f3 *= f1 * 0.9F + 0.1F;
/* 1396 */     f4 *= f1 * 0.85F + 0.15F;
/* 1397 */     float f9 = getThunderStrength(partialTicks);
/*      */     
/* 1399 */     if (f9 > 0.0F) {
/*      */       
/* 1401 */       float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
/* 1402 */       float f8 = 1.0F - f9 * 0.95F;
/* 1403 */       f2 = f2 * f8 + f10 * (1.0F - f8);
/* 1404 */       f3 = f3 * f8 + f10 * (1.0F - f8);
/* 1405 */       f4 = f4 * f8 + f10 * (1.0F - f8);
/*      */     } 
/*      */     
/* 1408 */     return new Vec3(f2, f3, f4);
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getFogColor(float partialTicks) {
/* 1413 */     float f = getCelestialAngle(partialTicks);
/* 1414 */     return this.provider.getFogColor(f, partialTicks);
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getPrecipitationHeight(BlockPos pos) {
/* 1419 */     return getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getTopSolidOrLiquidBlock(BlockPos pos) {
/* 1424 */     Chunk chunk = getChunkFromBlockCoords(pos);
/*      */     
/*      */     BlockPos blockpos;
/*      */     
/* 1428 */     for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1) {
/*      */       
/* 1430 */       BlockPos blockpos1 = blockpos.down();
/* 1431 */       Material material = chunk.getBlock(blockpos1).getMaterial();
/*      */       
/* 1433 */       if (material.blocksMovement() && material != Material.leaves) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1439 */     return blockpos;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getStarBrightness(float partialTicks) {
/* 1444 */     float f = getCelestialAngle(partialTicks);
/* 1445 */     float f1 = 1.0F - MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.25F;
/* 1446 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 1447 */     return f1 * f1 * 0.5F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {}
/*      */ 
/*      */   
/*      */   public void updateEntities() {
/* 1464 */     this.theProfiler.startSection("entities");
/* 1465 */     this.theProfiler.startSection("global");
/*      */     
/* 1467 */     for (int i = 0; i < this.weatherEffects.size(); i++) {
/*      */       
/* 1469 */       Entity entity = this.weatherEffects.get(i);
/*      */ 
/*      */       
/*      */       try {
/* 1473 */         entity.ticksExisted++;
/* 1474 */         entity.onUpdate();
/*      */       }
/* 1476 */       catch (Throwable throwable2) {
/*      */         
/* 1478 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
/* 1479 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
/*      */         
/* 1481 */         if (entity == null) {
/*      */           
/* 1483 */           crashreportcategory.addCrashSection("Entity", "~~NULL~~");
/*      */         }
/*      */         else {
/*      */           
/* 1487 */           entity.addEntityCrashInfo(crashreportcategory);
/*      */         } 
/*      */         
/* 1490 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */       
/* 1493 */       if (entity.isDead)
/*      */       {
/* 1495 */         this.weatherEffects.remove(i--);
/*      */       }
/*      */     } 
/*      */     
/* 1499 */     this.theProfiler.endStartSection("remove");
/* 1500 */     this.loadedEntityList.removeAll(this.unloadedEntityList);
/*      */     
/* 1502 */     for (int k = 0; k < this.unloadedEntityList.size(); k++) {
/*      */       
/* 1504 */       Entity entity1 = this.unloadedEntityList.get(k);
/* 1505 */       int j = entity1.chunkCoordX;
/* 1506 */       int l1 = entity1.chunkCoordZ;
/*      */       
/* 1508 */       if (entity1.addedToChunk && isChunkLoaded(j, l1, true))
/*      */       {
/* 1510 */         getChunkFromChunkCoords(j, l1).removeEntity(entity1);
/*      */       }
/*      */     } 
/*      */     
/* 1514 */     for (int l = 0; l < this.unloadedEntityList.size(); l++)
/*      */     {
/* 1516 */       onEntityRemoved(this.unloadedEntityList.get(l));
/*      */     }
/*      */     
/* 1519 */     this.unloadedEntityList.clear();
/* 1520 */     this.theProfiler.endStartSection("regular");
/*      */     
/* 1522 */     for (int i1 = 0; i1 < this.loadedEntityList.size(); i1++) {
/*      */       
/* 1524 */       Entity entity2 = this.loadedEntityList.get(i1);
/*      */       
/* 1526 */       if (entity2.ridingEntity != null) {
/*      */         
/* 1528 */         if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/* 1533 */         entity2.ridingEntity.riddenByEntity = null;
/* 1534 */         entity2.ridingEntity = null;
/*      */       } 
/*      */       
/* 1537 */       this.theProfiler.startSection("tick");
/*      */       
/* 1539 */       if (!entity2.isDead) {
/*      */         
/*      */         try {
/*      */           
/* 1543 */           updateEntity(entity2);
/*      */         }
/* 1545 */         catch (Throwable throwable1) {
/*      */           
/* 1547 */           CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
/* 1548 */           CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
/* 1549 */           entity2.addEntityCrashInfo(crashreportcategory2);
/* 1550 */           throw new ReportedException(crashreport1);
/*      */         } 
/*      */       }
/*      */       
/* 1554 */       this.theProfiler.endSection();
/* 1555 */       this.theProfiler.startSection("remove");
/*      */       
/* 1557 */       if (entity2.isDead) {
/*      */         
/* 1559 */         int k1 = entity2.chunkCoordX;
/* 1560 */         int i2 = entity2.chunkCoordZ;
/*      */         
/* 1562 */         if (entity2.addedToChunk && isChunkLoaded(k1, i2, true))
/*      */         {
/* 1564 */           getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
/*      */         }
/*      */         
/* 1567 */         this.loadedEntityList.remove(i1--);
/* 1568 */         onEntityRemoved(entity2);
/*      */       } 
/*      */       
/* 1571 */       this.theProfiler.endSection();
/*      */       continue;
/*      */     } 
/* 1574 */     this.theProfiler.endStartSection("blockEntities");
/* 1575 */     this.processingLoadedTiles = true;
/* 1576 */     Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();
/*      */     
/* 1578 */     while (iterator.hasNext()) {
/*      */       
/* 1580 */       TileEntity tileentity = iterator.next();
/*      */       
/* 1582 */       if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
/*      */         
/* 1584 */         BlockPos blockpos = tileentity.getPos();
/*      */         
/* 1586 */         if (isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos)) {
/*      */           
/*      */           try {
/*      */             
/* 1590 */             ((ITickable)tileentity).update();
/*      */           }
/* 1592 */           catch (Throwable throwable) {
/*      */             
/* 1594 */             CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
/* 1595 */             CrashReportCategory crashreportcategory1 = crashreport2.makeCategory("Block entity being ticked");
/* 1596 */             tileentity.addInfoToCrashReport(crashreportcategory1);
/* 1597 */             throw new ReportedException(crashreport2);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 1602 */       if (tileentity.isInvalid()) {
/*      */         
/* 1604 */         iterator.remove();
/* 1605 */         this.loadedTileEntityList.remove(tileentity);
/*      */         
/* 1607 */         if (isBlockLoaded(tileentity.getPos()))
/*      */         {
/* 1609 */           getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1614 */     this.processingLoadedTiles = false;
/*      */     
/* 1616 */     if (!this.tileEntitiesToBeRemoved.isEmpty()) {
/*      */       
/* 1618 */       this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
/* 1619 */       this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
/* 1620 */       this.tileEntitiesToBeRemoved.clear();
/*      */     } 
/*      */     
/* 1623 */     this.theProfiler.endStartSection("pendingBlockEntities");
/*      */     
/* 1625 */     if (!this.addedTileEntityList.isEmpty()) {
/*      */       
/* 1627 */       for (int j1 = 0; j1 < this.addedTileEntityList.size(); j1++) {
/*      */         
/* 1629 */         TileEntity tileentity1 = this.addedTileEntityList.get(j1);
/*      */         
/* 1631 */         if (!tileentity1.isInvalid()) {
/*      */           
/* 1633 */           if (!this.loadedTileEntityList.contains(tileentity1))
/*      */           {
/* 1635 */             addTileEntity(tileentity1);
/*      */           }
/*      */           
/* 1638 */           if (isBlockLoaded(tileentity1.getPos()))
/*      */           {
/* 1640 */             getChunkFromBlockCoords(tileentity1.getPos()).addTileEntity(tileentity1.getPos(), tileentity1);
/*      */           }
/*      */           
/* 1643 */           markBlockForUpdate(tileentity1.getPos());
/*      */         } 
/*      */       } 
/*      */       
/* 1647 */       this.addedTileEntityList.clear();
/*      */     } 
/*      */     
/* 1650 */     this.theProfiler.endSection();
/* 1651 */     this.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addTileEntity(TileEntity tile) {
/* 1656 */     boolean flag = this.loadedTileEntityList.add(tile);
/*      */     
/* 1658 */     if (flag && tile instanceof ITickable)
/*      */     {
/* 1660 */       this.tickableTileEntities.add(tile);
/*      */     }
/*      */     
/* 1663 */     return flag;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addTileEntities(Collection<TileEntity> tileEntityCollection) {
/* 1668 */     if (this.processingLoadedTiles) {
/*      */       
/* 1670 */       this.addedTileEntityList.addAll(tileEntityCollection);
/*      */     }
/*      */     else {
/*      */       
/* 1674 */       for (TileEntity tileentity : tileEntityCollection) {
/*      */         
/* 1676 */         this.loadedTileEntityList.add(tileentity);
/*      */         
/* 1678 */         if (tileentity instanceof ITickable)
/*      */         {
/* 1680 */           this.tickableTileEntities.add(tileentity);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateEntity(Entity ent) {
/* 1688 */     updateEntityWithOptionalForce(ent, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
/* 1693 */     int i = MathHelper.floor_double(entityIn.posX);
/* 1694 */     int j = MathHelper.floor_double(entityIn.posZ);
/* 1695 */     int k = 32;
/*      */     
/* 1697 */     if (!forceUpdate || isAreaLoaded(i - k, 0, j - k, i + k, 0, j + k, true)) {
/*      */       
/* 1699 */       entityIn.lastTickPosX = entityIn.posX;
/* 1700 */       entityIn.lastTickPosY = entityIn.posY;
/* 1701 */       entityIn.lastTickPosZ = entityIn.posZ;
/* 1702 */       entityIn.prevRotationYaw = entityIn.rotationYaw;
/* 1703 */       entityIn.prevRotationPitch = entityIn.rotationPitch;
/*      */       
/* 1705 */       if (forceUpdate && entityIn.addedToChunk) {
/*      */         
/* 1707 */         entityIn.ticksExisted++;
/*      */         
/* 1709 */         if (entityIn.ridingEntity != null) {
/*      */           
/* 1711 */           entityIn.updateRidden();
/*      */         }
/*      */         else {
/*      */           
/* 1715 */           entityIn.onUpdate();
/*      */         } 
/*      */       } 
/*      */       
/* 1719 */       this.theProfiler.startSection("chunkCheck");
/*      */       
/* 1721 */       if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX))
/*      */       {
/* 1723 */         entityIn.posX = entityIn.lastTickPosX;
/*      */       }
/*      */       
/* 1726 */       if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY))
/*      */       {
/* 1728 */         entityIn.posY = entityIn.lastTickPosY;
/*      */       }
/*      */       
/* 1731 */       if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ))
/*      */       {
/* 1733 */         entityIn.posZ = entityIn.lastTickPosZ;
/*      */       }
/*      */       
/* 1736 */       if (Double.isNaN(entityIn.rotationPitch) || Double.isInfinite(entityIn.rotationPitch))
/*      */       {
/* 1738 */         entityIn.rotationPitch = entityIn.prevRotationPitch;
/*      */       }
/*      */       
/* 1741 */       if (Double.isNaN(entityIn.rotationYaw) || Double.isInfinite(entityIn.rotationYaw))
/*      */       {
/* 1743 */         entityIn.rotationYaw = entityIn.prevRotationYaw;
/*      */       }
/*      */       
/* 1746 */       int l = MathHelper.floor_double(entityIn.posX / 16.0D);
/* 1747 */       int i1 = MathHelper.floor_double(entityIn.posY / 16.0D);
/* 1748 */       int j1 = MathHelper.floor_double(entityIn.posZ / 16.0D);
/*      */       
/* 1750 */       if (!entityIn.addedToChunk || entityIn.chunkCoordX != l || entityIn.chunkCoordY != i1 || entityIn.chunkCoordZ != j1) {
/*      */         
/* 1752 */         if (entityIn.addedToChunk && isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true))
/*      */         {
/* 1754 */           getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
/*      */         }
/*      */         
/* 1757 */         if (isChunkLoaded(l, j1, true)) {
/*      */           
/* 1759 */           entityIn.addedToChunk = true;
/* 1760 */           getChunkFromChunkCoords(l, j1).addEntity(entityIn);
/*      */         }
/*      */         else {
/*      */           
/* 1764 */           entityIn.addedToChunk = false;
/*      */         } 
/*      */       } 
/*      */       
/* 1768 */       this.theProfiler.endSection();
/*      */       
/* 1770 */       if (forceUpdate && entityIn.addedToChunk && entityIn.riddenByEntity != null)
/*      */       {
/* 1772 */         if (!entityIn.riddenByEntity.isDead && entityIn.riddenByEntity.ridingEntity == entityIn) {
/*      */           
/* 1774 */           updateEntity(entityIn.riddenByEntity);
/*      */         }
/*      */         else {
/*      */           
/* 1778 */           entityIn.riddenByEntity.ridingEntity = null;
/* 1779 */           entityIn.riddenByEntity = null;
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean checkNoEntityCollision(AxisAlignedBB bb) {
/* 1787 */     return checkNoEntityCollision(bb, (Entity)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean checkNoEntityCollision(AxisAlignedBB bb, Entity entityIn) {
/* 1792 */     List<Entity> list = getEntitiesWithinAABBExcludingEntity((Entity)null, bb);
/*      */     
/* 1794 */     for (int i = 0; i < list.size(); i++) {
/*      */       
/* 1796 */       Entity entity = list.get(i);
/*      */       
/* 1798 */       if (!entity.isDead && entity.preventEntitySpawning && entity != entityIn && (entityIn == null || (entityIn.ridingEntity != entity && entityIn.riddenByEntity != entity)))
/*      */       {
/* 1800 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1804 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean checkBlockCollision(AxisAlignedBB bb) {
/* 1809 */     int i = MathHelper.floor_double(bb.minX);
/* 1810 */     int j = MathHelper.floor_double(bb.maxX);
/* 1811 */     int k = MathHelper.floor_double(bb.minY);
/* 1812 */     int l = MathHelper.floor_double(bb.maxY);
/* 1813 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1814 */     int j1 = MathHelper.floor_double(bb.maxZ);
/* 1815 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1817 */     for (int k1 = i; k1 <= j; k1++) {
/*      */       
/* 1819 */       for (int l1 = k; l1 <= l; l1++) {
/*      */         
/* 1821 */         for (int i2 = i1; i2 <= j1; i2++) {
/*      */           
/* 1823 */           Block block = getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, l1, i2)).getBlock();
/*      */           
/* 1825 */           if (block.getMaterial() != Material.air)
/*      */           {
/* 1827 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1833 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAnyLiquid(AxisAlignedBB bb) {
/* 1838 */     int i = MathHelper.floor_double(bb.minX);
/* 1839 */     int j = MathHelper.floor_double(bb.maxX);
/* 1840 */     int k = MathHelper.floor_double(bb.minY);
/* 1841 */     int l = MathHelper.floor_double(bb.maxY);
/* 1842 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1843 */     int j1 = MathHelper.floor_double(bb.maxZ);
/* 1844 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1846 */     for (int k1 = i; k1 <= j; k1++) {
/*      */       
/* 1848 */       for (int l1 = k; l1 <= l; l1++) {
/*      */         
/* 1850 */         for (int i2 = i1; i2 <= j1; i2++) {
/*      */           
/* 1852 */           Block block = getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, l1, i2)).getBlock();
/*      */           
/* 1854 */           if (block.getMaterial().isLiquid())
/*      */           {
/* 1856 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1862 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFlammableWithin(AxisAlignedBB bb) {
/* 1867 */     int i = MathHelper.floor_double(bb.minX);
/* 1868 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1869 */     int k = MathHelper.floor_double(bb.minY);
/* 1870 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1871 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1872 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/*      */     
/* 1874 */     if (isAreaLoaded(i, k, i1, j, l, j1, true)) {
/*      */       
/* 1876 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */       
/* 1878 */       for (int k1 = i; k1 < j; k1++) {
/*      */         
/* 1880 */         for (int l1 = k; l1 < l; l1++) {
/*      */           
/* 1882 */           for (int i2 = i1; i2 < j1; i2++) {
/*      */             
/* 1884 */             Block block = getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, l1, i2)).getBlock();
/*      */             
/* 1886 */             if (block == Blocks.fire || block == Blocks.flowing_lava || block == Blocks.lava)
/*      */             {
/* 1888 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1895 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn) {
/* 1900 */     int i = MathHelper.floor_double(bb.minX);
/* 1901 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1902 */     int k = MathHelper.floor_double(bb.minY);
/* 1903 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1904 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1905 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/*      */     
/* 1907 */     if (!isAreaLoaded(i, k, i1, j, l, j1, true))
/*      */     {
/* 1909 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1913 */     boolean flag = false;
/* 1914 */     Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
/* 1915 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1917 */     for (int k1 = i; k1 < j; k1++) {
/*      */       
/* 1919 */       for (int l1 = k; l1 < l; l1++) {
/*      */         
/* 1921 */         for (int i2 = i1; i2 < j1; i2++) {
/*      */           
/* 1923 */           blockpos$mutableblockpos.set(k1, l1, i2);
/* 1924 */           IBlockState iblockstate = getBlockState((BlockPos)blockpos$mutableblockpos);
/* 1925 */           Block block = iblockstate.getBlock();
/*      */           
/* 1927 */           if (block.getMaterial() == materialIn) {
/*      */             
/* 1929 */             double d0 = ((l1 + 1) - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue()));
/*      */             
/* 1931 */             if (l >= d0) {
/*      */               
/* 1933 */               flag = true;
/* 1934 */               vec3 = block.modifyAcceleration(this, (BlockPos)blockpos$mutableblockpos, entityIn, vec3);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1941 */     if (vec3.lengthVector() > 0.0D && entityIn.isPushedByWater()) {
/*      */       
/* 1943 */       vec3 = vec3.normalize();
/* 1944 */       double d1 = 0.014D;
/* 1945 */       entityIn.motionX += vec3.xCoord * d1;
/* 1946 */       entityIn.motionY += vec3.yCoord * d1;
/* 1947 */       entityIn.motionZ += vec3.zCoord * d1;
/*      */     } 
/*      */     
/* 1950 */     return flag;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn) {
/* 1956 */     int i = MathHelper.floor_double(bb.minX);
/* 1957 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1958 */     int k = MathHelper.floor_double(bb.minY);
/* 1959 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1960 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1961 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 1962 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1964 */     for (int k1 = i; k1 < j; k1++) {
/*      */       
/* 1966 */       for (int l1 = k; l1 < l; l1++) {
/*      */         
/* 1968 */         for (int i2 = i1; i2 < j1; i2++) {
/*      */           
/* 1970 */           if (getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, l1, i2)).getBlock().getMaterial() == materialIn)
/*      */           {
/* 1972 */             return true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1978 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAABBInMaterial(AxisAlignedBB bb, Material materialIn) {
/* 1983 */     int i = MathHelper.floor_double(bb.minX);
/* 1984 */     int j = MathHelper.floor_double(bb.maxX + 1.0D);
/* 1985 */     int k = MathHelper.floor_double(bb.minY);
/* 1986 */     int l = MathHelper.floor_double(bb.maxY + 1.0D);
/* 1987 */     int i1 = MathHelper.floor_double(bb.minZ);
/* 1988 */     int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
/* 1989 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */     
/* 1991 */     for (int k1 = i; k1 < j; k1++) {
/*      */       
/* 1993 */       for (int l1 = k; l1 < l; l1++) {
/*      */         
/* 1995 */         for (int i2 = i1; i2 < j1; i2++) {
/*      */           
/* 1997 */           IBlockState iblockstate = getBlockState((BlockPos)blockpos$mutableblockpos.set(k1, l1, i2));
/* 1998 */           Block block = iblockstate.getBlock();
/*      */           
/* 2000 */           if (block.getMaterial() == materialIn) {
/*      */             
/* 2002 */             int j2 = ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue();
/* 2003 */             double d0 = (l1 + 1);
/*      */             
/* 2005 */             if (j2 < 8)
/*      */             {
/* 2007 */               d0 = (l1 + 1) - j2 / 8.0D;
/*      */             }
/*      */             
/* 2010 */             if (d0 >= bb.minY)
/*      */             {
/* 2012 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2019 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public Explosion createExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
/* 2024 */     return newExplosion(entityIn, x, y, z, strength, false, isSmoking);
/*      */   }
/*      */ 
/*      */   
/*      */   public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
/* 2029 */     Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
/* 2030 */     explosion.doExplosionA();
/* 2031 */     explosion.doExplosionB(true);
/* 2032 */     return explosion;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getBlockDensity(Vec3 vec, AxisAlignedBB bb) {
/* 2037 */     double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
/* 2038 */     double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
/* 2039 */     double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
/* 2040 */     double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
/* 2041 */     double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
/*      */     
/* 2043 */     if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
/*      */       
/* 2045 */       int i = 0;
/* 2046 */       int j = 0;
/*      */       float f;
/* 2048 */       for (f = 0.0F; f <= 1.0F; f = (float)(f + d0)) {
/*      */         float f1;
/* 2050 */         for (f1 = 0.0F; f1 <= 1.0F; f1 = (float)(f1 + d1)) {
/*      */           float f2;
/* 2052 */           for (f2 = 0.0F; f2 <= 1.0F; f2 = (float)(f2 + d2)) {
/*      */             
/* 2054 */             double d5 = bb.minX + (bb.maxX - bb.minX) * f;
/* 2055 */             double d6 = bb.minY + (bb.maxY - bb.minY) * f1;
/* 2056 */             double d7 = bb.minZ + (bb.maxZ - bb.minZ) * f2;
/*      */             
/* 2058 */             if (rayTraceBlocks(new Vec3(d5 + d3, d6, d7 + d4), vec) == null)
/*      */             {
/* 2060 */               i++;
/*      */             }
/*      */             
/* 2063 */             j++;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2068 */       return i / j;
/*      */     } 
/*      */ 
/*      */     
/* 2072 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean extinguishFire(EntityPlayer player, BlockPos pos, EnumFacing side) {
/* 2078 */     pos = pos.offset(side);
/*      */     
/* 2080 */     if (getBlockState(pos).getBlock() == Blocks.fire) {
/*      */       
/* 2082 */       playAuxSFXAtEntity(player, 1004, pos, 0);
/* 2083 */       setBlockToAir(pos);
/* 2084 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2088 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDebugLoadedEntities() {
/* 2094 */     return "All: " + this.loadedEntityList.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getProviderName() {
/* 2099 */     return this.chunkProvider.makeString();
/*      */   }
/*      */ 
/*      */   
/*      */   public TileEntity getTileEntity(BlockPos pos) {
/* 2104 */     if (!isValid(pos))
/*      */     {
/* 2106 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 2110 */     TileEntity tileentity = null;
/*      */     
/* 2112 */     if (this.processingLoadedTiles)
/*      */     {
/* 2114 */       for (int i = 0; i < this.addedTileEntityList.size(); i++) {
/*      */         
/* 2116 */         TileEntity tileentity1 = this.addedTileEntityList.get(i);
/*      */         
/* 2118 */         if (!tileentity1.isInvalid() && tileentity1.getPos().equals(pos)) {
/*      */           
/* 2120 */           tileentity = tileentity1;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 2126 */     if (tileentity == null)
/*      */     {
/* 2128 */       tileentity = getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
/*      */     }
/*      */     
/* 2131 */     if (tileentity == null)
/*      */     {
/* 2133 */       for (int j = 0; j < this.addedTileEntityList.size(); j++) {
/*      */         
/* 2135 */         TileEntity tileentity2 = this.addedTileEntityList.get(j);
/*      */         
/* 2137 */         if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos)) {
/*      */           
/* 2139 */           tileentity = tileentity2;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 2145 */     return tileentity;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTileEntity(BlockPos pos, TileEntity tileEntityIn) {
/* 2151 */     if (tileEntityIn != null && !tileEntityIn.isInvalid())
/*      */     {
/* 2153 */       if (this.processingLoadedTiles) {
/*      */         
/* 2155 */         tileEntityIn.setPos(pos);
/* 2156 */         Iterator<TileEntity> iterator = this.addedTileEntityList.iterator();
/*      */         
/* 2158 */         while (iterator.hasNext()) {
/*      */           
/* 2160 */           TileEntity tileentity = iterator.next();
/*      */           
/* 2162 */           if (tileentity.getPos().equals(pos)) {
/*      */             
/* 2164 */             tileentity.invalidate();
/* 2165 */             iterator.remove();
/*      */           } 
/*      */         } 
/*      */         
/* 2169 */         this.addedTileEntityList.add(tileEntityIn);
/*      */       }
/*      */       else {
/*      */         
/* 2173 */         addTileEntity(tileEntityIn);
/* 2174 */         getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeTileEntity(BlockPos pos) {
/* 2181 */     TileEntity tileentity = getTileEntity(pos);
/*      */     
/* 2183 */     if (tileentity != null && this.processingLoadedTiles) {
/*      */       
/* 2185 */       tileentity.invalidate();
/* 2186 */       this.addedTileEntityList.remove(tileentity);
/*      */     }
/*      */     else {
/*      */       
/* 2190 */       if (tileentity != null) {
/*      */         
/* 2192 */         this.addedTileEntityList.remove(tileentity);
/* 2193 */         this.loadedTileEntityList.remove(tileentity);
/* 2194 */         this.tickableTileEntities.remove(tileentity);
/*      */       } 
/*      */       
/* 2197 */       getChunkFromBlockCoords(pos).removeTileEntity(pos);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void markTileEntityForRemoval(TileEntity tileEntityIn) {
/* 2203 */     this.tileEntitiesToBeRemoved.add(tileEntityIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockFullCube(BlockPos pos) {
/* 2208 */     IBlockState iblockstate = getBlockState(pos);
/* 2209 */     AxisAlignedBB axisalignedbb = iblockstate.getBlock().getCollisionBoundingBox(this, pos, iblockstate);
/* 2210 */     return (axisalignedbb != null && axisalignedbb.getAverageEdgeLength() >= 1.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean doesBlockHaveSolidTopSurface(IBlockAccess blockAccess, BlockPos pos) {
/* 2215 */     IBlockState iblockstate = blockAccess.getBlockState(pos);
/* 2216 */     Block block = iblockstate.getBlock();
/* 2217 */     return (block.getMaterial().isOpaque() && block.isFullCube()) ? true : ((block instanceof BlockStairs) ? ((iblockstate.getValue((IProperty)BlockStairs.HALF) == BlockStairs.EnumHalf.TOP)) : ((block instanceof BlockSlab) ? ((iblockstate.getValue((IProperty)BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP)) : ((block instanceof net.minecraft.block.BlockHopper) ? true : ((block instanceof BlockSnow) ? ((((Integer)iblockstate.getValue((IProperty)BlockSnow.LAYERS)).intValue() == 7)) : false))));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockNormalCube(BlockPos pos, boolean _default) {
/* 2222 */     if (!isValid(pos))
/*      */     {
/* 2224 */       return _default;
/*      */     }
/*      */ 
/*      */     
/* 2228 */     Chunk chunk = this.chunkProvider.provideChunk(pos);
/*      */     
/* 2230 */     if (chunk.isEmpty())
/*      */     {
/* 2232 */       return _default;
/*      */     }
/*      */ 
/*      */     
/* 2236 */     Block block = getBlockState(pos).getBlock();
/* 2237 */     return (block.getMaterial().isOpaque() && block.isFullCube());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void calculateInitialSkylight() {
/* 2244 */     int i = calculateSkylightSubtracted(1.0F);
/*      */     
/* 2246 */     if (i != this.skylightSubtracted)
/*      */     {
/* 2248 */       this.skylightSubtracted = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAllowedSpawnTypes(boolean hostile, boolean peaceful) {
/* 2254 */     this.spawnHostileMobs = hostile;
/* 2255 */     this.spawnPeacefulMobs = peaceful;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/* 2260 */     updateWeather();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void calculateInitialWeather() {
/* 2265 */     if (this.worldInfo.isRaining()) {
/*      */       
/* 2267 */       this.rainingStrength = 1.0F;
/*      */       
/* 2269 */       if (this.worldInfo.isThundering())
/*      */       {
/* 2271 */         this.thunderingStrength = 1.0F;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateWeather() {
/* 2278 */     if (!this.provider.getHasNoSky())
/*      */     {
/* 2280 */       if (!this.isRemote) {
/*      */         
/* 2282 */         int i = this.worldInfo.getCleanWeatherTime();
/*      */         
/* 2284 */         if (i > 0) {
/*      */           
/* 2286 */           i--;
/* 2287 */           this.worldInfo.setCleanWeatherTime(i);
/* 2288 */           this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
/* 2289 */           this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
/*      */         } 
/*      */         
/* 2292 */         int j = this.worldInfo.getThunderTime();
/*      */         
/* 2294 */         if (j <= 0) {
/*      */           
/* 2296 */           if (this.worldInfo.isThundering())
/*      */           {
/* 2298 */             this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
/*      */           }
/*      */           else
/*      */           {
/* 2302 */             this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2307 */           j--;
/* 2308 */           this.worldInfo.setThunderTime(j);
/*      */           
/* 2310 */           if (j <= 0)
/*      */           {
/* 2312 */             this.worldInfo.setThundering(!this.worldInfo.isThundering());
/*      */           }
/*      */         } 
/*      */         
/* 2316 */         this.prevThunderingStrength = this.thunderingStrength;
/*      */         
/* 2318 */         if (this.worldInfo.isThundering()) {
/*      */           
/* 2320 */           this.thunderingStrength = (float)(this.thunderingStrength + 0.01D);
/*      */         }
/*      */         else {
/*      */           
/* 2324 */           this.thunderingStrength = (float)(this.thunderingStrength - 0.01D);
/*      */         } 
/*      */         
/* 2327 */         this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0F, 1.0F);
/* 2328 */         int k = this.worldInfo.getRainTime();
/*      */         
/* 2330 */         if (k <= 0) {
/*      */           
/* 2332 */           if (this.worldInfo.isRaining())
/*      */           {
/* 2334 */             this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
/*      */           }
/*      */           else
/*      */           {
/* 2338 */             this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2343 */           k--;
/* 2344 */           this.worldInfo.setRainTime(k);
/*      */           
/* 2346 */           if (k <= 0)
/*      */           {
/* 2348 */             this.worldInfo.setRaining(!this.worldInfo.isRaining());
/*      */           }
/*      */         } 
/*      */         
/* 2352 */         this.prevRainingStrength = this.rainingStrength;
/*      */         
/* 2354 */         if (this.worldInfo.isRaining()) {
/*      */           
/* 2356 */           this.rainingStrength = (float)(this.rainingStrength + 0.01D);
/*      */         }
/*      */         else {
/*      */           
/* 2360 */           this.rainingStrength = (float)(this.rainingStrength - 0.01D);
/*      */         } 
/*      */         
/* 2363 */         this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setActivePlayerChunksAndCheckLight() {
/* 2370 */     this.activeChunkSet.clear();
/* 2371 */     this.theProfiler.startSection("buildList");
/*      */     
/* 2373 */     for (int i = 0; i < this.playerEntities.size(); i++) {
/*      */       
/* 2375 */       EntityPlayer entityplayer = this.playerEntities.get(i);
/* 2376 */       int j = MathHelper.floor_double(entityplayer.posX / 16.0D);
/* 2377 */       int k = MathHelper.floor_double(entityplayer.posZ / 16.0D);
/* 2378 */       int l = getRenderDistanceChunks();
/*      */       
/* 2380 */       for (int i1 = -l; i1 <= l; i1++) {
/*      */         
/* 2382 */         for (int j1 = -l; j1 <= l; j1++)
/*      */         {
/* 2384 */           this.activeChunkSet.add(new ChunkCoordIntPair(i1 + j, j1 + k));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2389 */     this.theProfiler.endSection();
/*      */     
/* 2391 */     if (this.ambientTickCountdown > 0)
/*      */     {
/* 2393 */       this.ambientTickCountdown--;
/*      */     }
/*      */     
/* 2396 */     this.theProfiler.startSection("playerCheckLight");
/*      */     
/* 2398 */     if (!this.playerEntities.isEmpty()) {
/*      */       
/* 2400 */       int k1 = this.rand.nextInt(this.playerEntities.size());
/* 2401 */       EntityPlayer entityplayer1 = this.playerEntities.get(k1);
/* 2402 */       int l1 = MathHelper.floor_double(entityplayer1.posX) + this.rand.nextInt(11) - 5;
/* 2403 */       int i2 = MathHelper.floor_double(entityplayer1.posY) + this.rand.nextInt(11) - 5;
/* 2404 */       int j2 = MathHelper.floor_double(entityplayer1.posZ) + this.rand.nextInt(11) - 5;
/* 2405 */       checkLight(new BlockPos(l1, i2, j2));
/*      */     } 
/*      */     
/* 2408 */     this.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract int getRenderDistanceChunks();
/*      */   
/*      */   protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn) {
/* 2415 */     this.theProfiler.endStartSection("moodSound");
/*      */     
/* 2417 */     if (this.ambientTickCountdown == 0 && !this.isRemote) {
/*      */       
/* 2419 */       this.updateLCG = this.updateLCG * 3 + 1013904223;
/* 2420 */       int i = this.updateLCG >> 2;
/* 2421 */       int j = i & 0xF;
/* 2422 */       int k = i >> 8 & 0xF;
/* 2423 */       int l = i >> 16 & 0xFF;
/* 2424 */       BlockPos blockpos = new BlockPos(j, l, k);
/* 2425 */       Block block = chunkIn.getBlock(blockpos);
/* 2426 */       j += p_147467_1_;
/* 2427 */       k += p_147467_2_;
/*      */       
/* 2429 */       if (block.getMaterial() == Material.air && getLight(blockpos) <= this.rand.nextInt(8) && getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
/*      */         
/* 2431 */         EntityPlayer entityplayer = getClosestPlayer(j + 0.5D, l + 0.5D, k + 0.5D, 8.0D);
/*      */         
/* 2433 */         if (entityplayer != null && entityplayer.getDistanceSq(j + 0.5D, l + 0.5D, k + 0.5D) > 4.0D) {
/*      */           
/* 2435 */           playSoundEffect(j + 0.5D, l + 0.5D, k + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
/* 2436 */           this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2441 */     this.theProfiler.endStartSection("checkLight");
/* 2442 */     chunkIn.enqueueRelightChecks();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateBlocks() {
/* 2447 */     setActivePlayerChunksAndCheckLight();
/*      */   }
/*      */ 
/*      */   
/*      */   public void forceBlockUpdateTick(Block blockType, BlockPos pos, Random random) {
/* 2452 */     this.scheduledUpdatesAreImmediate = true;
/* 2453 */     blockType.updateTick(this, pos, getBlockState(pos), random);
/* 2454 */     this.scheduledUpdatesAreImmediate = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBlockFreezeWater(BlockPos pos) {
/* 2459 */     return canBlockFreeze(pos, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBlockFreezeNoWater(BlockPos pos) {
/* 2464 */     return canBlockFreeze(pos, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj) {
/* 2469 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(pos);
/* 2470 */     float f = biomegenbase.getFloatTemperature(pos);
/*      */     
/* 2472 */     if (f > 0.15F)
/*      */     {
/* 2474 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2478 */     if (pos.getY() >= 0 && pos.getY() < 256 && getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
/*      */       
/* 2480 */       IBlockState iblockstate = getBlockState(pos);
/* 2481 */       Block block = iblockstate.getBlock();
/*      */       
/* 2483 */       if ((block == Blocks.water || block == Blocks.flowing_water) && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0) {
/*      */         
/* 2485 */         if (!noWaterAdj)
/*      */         {
/* 2487 */           return true;
/*      */         }
/*      */         
/* 2490 */         boolean flag = (isWater(pos.west()) && isWater(pos.east()) && isWater(pos.north()) && isWater(pos.south()));
/*      */         
/* 2492 */         if (!flag)
/*      */         {
/* 2494 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2499 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isWater(BlockPos pos) {
/* 2505 */     return (getBlockState(pos).getBlock().getMaterial() == Material.water);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canSnowAt(BlockPos pos, boolean checkLight) {
/* 2510 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(pos);
/* 2511 */     float f = biomegenbase.getFloatTemperature(pos);
/*      */     
/* 2513 */     if (f > 0.15F)
/*      */     {
/* 2515 */       return false;
/*      */     }
/* 2517 */     if (!checkLight)
/*      */     {
/* 2519 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 2523 */     if (pos.getY() >= 0 && pos.getY() < 256 && getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
/*      */       
/* 2525 */       Block block = getBlockState(pos).getBlock();
/*      */       
/* 2527 */       if (block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, pos))
/*      */       {
/* 2529 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 2533 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkLight(BlockPos pos) {
/* 2539 */     boolean flag = false;
/*      */     
/* 2541 */     if (!this.provider.getHasNoSky())
/*      */     {
/* 2543 */       flag |= checkLightFor(EnumSkyBlock.SKY, pos);
/*      */     }
/*      */     
/* 2546 */     flag |= checkLightFor(EnumSkyBlock.BLOCK, pos);
/* 2547 */     return flag;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getRawLight(BlockPos pos, EnumSkyBlock lightType) {
/* 2552 */     if (lightType == EnumSkyBlock.SKY && canSeeSky(pos))
/*      */     {
/* 2554 */       return 15;
/*      */     }
/*      */ 
/*      */     
/* 2558 */     Block block = getBlockState(pos).getBlock();
/* 2559 */     int i = (lightType == EnumSkyBlock.SKY) ? 0 : block.getLightValue();
/* 2560 */     int j = block.getLightOpacity();
/*      */     
/* 2562 */     if (j >= 15 && block.getLightValue() > 0)
/*      */     {
/* 2564 */       j = 1;
/*      */     }
/*      */     
/* 2567 */     if (j < 1)
/*      */     {
/* 2569 */       j = 1;
/*      */     }
/*      */     
/* 2572 */     if (j >= 15)
/*      */     {
/* 2574 */       return 0;
/*      */     }
/* 2576 */     if (i >= 14)
/*      */     {
/* 2578 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2582 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*      */       
/* 2584 */       BlockPos blockpos = pos.offset(enumfacing);
/* 2585 */       int k = getLightFor(lightType, blockpos) - j;
/*      */       
/* 2587 */       if (k > i)
/*      */       {
/* 2589 */         i = k;
/*      */       }
/*      */       
/* 2592 */       if (i >= 14)
/*      */       {
/* 2594 */         return i;
/*      */       }
/*      */     } 
/*      */     
/* 2598 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos) {
/* 2605 */     if (!isAreaLoaded(pos, 17, false))
/*      */     {
/* 2607 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 2611 */     int i = 0;
/* 2612 */     int j = 0;
/* 2613 */     this.theProfiler.startSection("getBrightness");
/* 2614 */     int k = getLightFor(lightType, pos);
/* 2615 */     int l = getRawLight(pos, lightType);
/* 2616 */     int i1 = pos.getX();
/* 2617 */     int j1 = pos.getY();
/* 2618 */     int k1 = pos.getZ();
/*      */     
/* 2620 */     if (l > k) {
/*      */       
/* 2622 */       this.lightUpdateBlockList[j++] = 133152;
/*      */     }
/* 2624 */     else if (l < k) {
/*      */       
/* 2626 */       this.lightUpdateBlockList[j++] = 0x20820 | k << 18;
/*      */       
/* 2628 */       while (i < j) {
/*      */         
/* 2630 */         int l1 = this.lightUpdateBlockList[i++];
/* 2631 */         int i2 = (l1 & 0x3F) - 32 + i1;
/* 2632 */         int j2 = (l1 >> 6 & 0x3F) - 32 + j1;
/* 2633 */         int k2 = (l1 >> 12 & 0x3F) - 32 + k1;
/* 2634 */         int l2 = l1 >> 18 & 0xF;
/* 2635 */         BlockPos blockpos = new BlockPos(i2, j2, k2);
/* 2636 */         int i3 = getLightFor(lightType, blockpos);
/*      */         
/* 2638 */         if (i3 == l2) {
/*      */           
/* 2640 */           setLightFor(lightType, blockpos, 0);
/*      */           
/* 2642 */           if (l2 > 0) {
/*      */             
/* 2644 */             int j3 = MathHelper.abs_int(i2 - i1);
/* 2645 */             int k3 = MathHelper.abs_int(j2 - j1);
/* 2646 */             int l3 = MathHelper.abs_int(k2 - k1);
/*      */             
/* 2648 */             if (j3 + k3 + l3 < 17) {
/*      */               
/* 2650 */               BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */               
/* 2652 */               for (EnumFacing enumfacing : EnumFacing.values()) {
/*      */                 
/* 2654 */                 int i4 = i2 + enumfacing.getFrontOffsetX();
/* 2655 */                 int j4 = j2 + enumfacing.getFrontOffsetY();
/* 2656 */                 int k4 = k2 + enumfacing.getFrontOffsetZ();
/* 2657 */                 blockpos$mutableblockpos.set(i4, j4, k4);
/* 2658 */                 int l4 = Math.max(1, getBlockState((BlockPos)blockpos$mutableblockpos).getBlock().getLightOpacity());
/* 2659 */                 i3 = getLightFor(lightType, (BlockPos)blockpos$mutableblockpos);
/*      */                 
/* 2661 */                 if (i3 == l2 - l4 && j < this.lightUpdateBlockList.length)
/*      */                 {
/* 2663 */                   this.lightUpdateBlockList[j++] = i4 - i1 + 32 | j4 - j1 + 32 << 6 | k4 - k1 + 32 << 12 | l2 - l4 << 18;
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2671 */       i = 0;
/*      */     } 
/*      */     
/* 2674 */     this.theProfiler.endSection();
/* 2675 */     this.theProfiler.startSection("checkedPosition < toCheckCount");
/*      */     
/* 2677 */     while (i < j) {
/*      */       
/* 2679 */       int i5 = this.lightUpdateBlockList[i++];
/* 2680 */       int j5 = (i5 & 0x3F) - 32 + i1;
/* 2681 */       int k5 = (i5 >> 6 & 0x3F) - 32 + j1;
/* 2682 */       int l5 = (i5 >> 12 & 0x3F) - 32 + k1;
/* 2683 */       BlockPos blockpos1 = new BlockPos(j5, k5, l5);
/* 2684 */       int i6 = getLightFor(lightType, blockpos1);
/* 2685 */       int j6 = getRawLight(blockpos1, lightType);
/*      */       
/* 2687 */       if (j6 != i6) {
/*      */         
/* 2689 */         setLightFor(lightType, blockpos1, j6);
/*      */         
/* 2691 */         if (j6 > i6) {
/*      */           
/* 2693 */           int k6 = Math.abs(j5 - i1);
/* 2694 */           int l6 = Math.abs(k5 - j1);
/* 2695 */           int i7 = Math.abs(l5 - k1);
/* 2696 */           boolean flag = (j < this.lightUpdateBlockList.length - 6);
/*      */           
/* 2698 */           if (k6 + l6 + i7 < 17 && flag) {
/*      */             
/* 2700 */             if (getLightFor(lightType, blockpos1.west()) < j6)
/*      */             {
/* 2702 */               this.lightUpdateBlockList[j++] = j5 - 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
/*      */             }
/*      */             
/* 2705 */             if (getLightFor(lightType, blockpos1.east()) < j6)
/*      */             {
/* 2707 */               this.lightUpdateBlockList[j++] = j5 + 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
/*      */             }
/*      */             
/* 2710 */             if (getLightFor(lightType, blockpos1.down()) < j6)
/*      */             {
/* 2712 */               this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
/*      */             }
/*      */             
/* 2715 */             if (getLightFor(lightType, blockpos1.up()) < j6)
/*      */             {
/* 2717 */               this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 + 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
/*      */             }
/*      */             
/* 2720 */             if (getLightFor(lightType, blockpos1.north()) < j6)
/*      */             {
/* 2722 */               this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - 1 - k1 + 32 << 12);
/*      */             }
/*      */             
/* 2725 */             if (getLightFor(lightType, blockpos1.south()) < j6)
/*      */             {
/* 2727 */               this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 + 1 - k1 + 32 << 12);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2734 */     this.theProfiler.endSection();
/* 2735 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean tickUpdates(boolean p_72955_1_) {
/* 2741 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_) {
/* 2746 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_) {
/* 2751 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Entity> getEntitiesWithinAABBExcludingEntity(Entity entityIn, AxisAlignedBB bb) {
/* 2756 */     return getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Entity> getEntitiesInAABBexcluding(Entity entityIn, AxisAlignedBB boundingBox, Predicate<? super Entity> predicate) {
/* 2761 */     List<Entity> list = Lists.newArrayList();
/* 2762 */     int i = MathHelper.floor_double((boundingBox.minX - 2.0D) / 16.0D);
/* 2763 */     int j = MathHelper.floor_double((boundingBox.maxX + 2.0D) / 16.0D);
/* 2764 */     int k = MathHelper.floor_double((boundingBox.minZ - 2.0D) / 16.0D);
/* 2765 */     int l = MathHelper.floor_double((boundingBox.maxZ + 2.0D) / 16.0D);
/*      */     
/* 2767 */     for (int i1 = i; i1 <= j; i1++) {
/*      */       
/* 2769 */       for (int j1 = k; j1 <= l; j1++) {
/*      */         
/* 2771 */         if (isChunkLoaded(i1, j1, true))
/*      */         {
/* 2773 */           getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2778 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> List<T> getEntities(Class<? extends T> entityType, Predicate<? super T> filter) {
/* 2783 */     List<T> list = Lists.newArrayList();
/*      */     
/* 2785 */     for (Entity entity : this.loadedEntityList) {
/*      */       
/* 2787 */       if (entityType.isAssignableFrom(entity.getClass()) && filter.apply(entity))
/*      */       {
/* 2789 */         list.add((T)entity);
/*      */       }
/*      */     } 
/*      */     
/* 2793 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> List<T> getPlayers(Class<? extends T> playerType, Predicate<? super T> filter) {
/* 2798 */     List<T> list = Lists.newArrayList();
/*      */     
/* 2800 */     for (Entity entity : this.playerEntities) {
/*      */       
/* 2802 */       if (playerType.isAssignableFrom(entity.getClass()) && filter.apply(entity))
/*      */       {
/* 2804 */         list.add((T)entity);
/*      */       }
/*      */     } 
/*      */     
/* 2808 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> classEntity, AxisAlignedBB bb) {
/* 2813 */     return getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, Predicate<? super T> filter) {
/* 2818 */     int i = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
/* 2819 */     int j = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
/* 2820 */     int k = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
/* 2821 */     int l = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
/* 2822 */     List<T> list = Lists.newArrayList();
/*      */     
/* 2824 */     for (int i1 = i; i1 <= j; i1++) {
/*      */       
/* 2826 */       for (int j1 = k; j1 <= l; j1++) {
/*      */         
/* 2828 */         if (isChunkLoaded(i1, j1, true))
/*      */         {
/* 2830 */           getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAAAB(clazz, aabb, list, filter);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2835 */     return list;
/*      */   }
/*      */   
/*      */   public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> entityType, AxisAlignedBB aabb, T closestTo) {
/*      */     Entity entity;
/* 2840 */     List<T> list = getEntitiesWithinAABB(entityType, aabb);
/* 2841 */     T t = null;
/* 2842 */     double d0 = Double.MAX_VALUE;
/*      */     
/* 2844 */     for (int i = 0; i < list.size(); i++) {
/*      */       
/* 2846 */       Entity entity1 = (Entity)list.get(i);
/*      */       
/* 2848 */       if (entity1 != closestTo && EntitySelectors.NOT_SPECTATING.apply(entity1)) {
/*      */         
/* 2850 */         double d1 = closestTo.getDistanceSqToEntity(entity1);
/*      */         
/* 2852 */         if (d1 <= d0) {
/*      */           
/* 2854 */           entity = entity1;
/* 2855 */           d0 = d1;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2860 */     return (T)entity;
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getEntityByID(int id) {
/* 2865 */     return (Entity)this.entitiesById.lookup(id);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Entity> getLoadedEntityList() {
/* 2870 */     return this.loadedEntityList;
/*      */   }
/*      */ 
/*      */   
/*      */   public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {
/* 2875 */     if (isBlockLoaded(pos))
/*      */     {
/* 2877 */       getChunkFromBlockCoords(pos).setChunkModified();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int countEntities(Class<?> entityType) {
/* 2883 */     int i = 0;
/*      */     
/* 2885 */     for (Entity entity : this.loadedEntityList) {
/*      */       
/* 2887 */       if ((!(entity instanceof EntityLiving) || !((EntityLiving)entity).isNoDespawnRequired()) && entityType.isAssignableFrom(entity.getClass()))
/*      */       {
/* 2889 */         i++;
/*      */       }
/*      */     } 
/*      */     
/* 2893 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadEntities(Collection<Entity> entityCollection) {
/* 2898 */     this.loadedEntityList.addAll(entityCollection);
/*      */     
/* 2900 */     for (Entity entity : entityCollection)
/*      */     {
/* 2902 */       onEntityAdded(entity);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void unloadEntities(Collection<Entity> entityCollection) {
/* 2908 */     this.unloadedEntityList.addAll(entityCollection);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBlockBePlaced(Block blockIn, BlockPos pos, boolean p_175716_3_, EnumFacing side, Entity entityIn, ItemStack itemStackIn) {
/* 2913 */     Block block = getBlockState(pos).getBlock();
/* 2914 */     AxisAlignedBB axisalignedbb = p_175716_3_ ? null : blockIn.getCollisionBoundingBox(this, pos, blockIn.getDefaultState());
/* 2915 */     return (axisalignedbb != null && !checkNoEntityCollision(axisalignedbb, entityIn)) ? false : ((block.getMaterial() == Material.circuits && blockIn == Blocks.anvil) ? true : ((block.getMaterial().isReplaceable() && blockIn.canReplace(this, pos, side, itemStackIn))));
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSeaLevel() {
/* 2920 */     return this.seaLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSeaLevel(int p_181544_1_) {
/* 2925 */     this.seaLevel = p_181544_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getStrongPower(BlockPos pos, EnumFacing direction) {
/* 2930 */     IBlockState iblockstate = getBlockState(pos);
/* 2931 */     return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldType getWorldType() {
/* 2936 */     return this.worldInfo.getTerrainType();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getStrongPower(BlockPos pos) {
/* 2941 */     int i = 0;
/* 2942 */     i = Math.max(i, getStrongPower(pos.down(), EnumFacing.DOWN));
/*      */     
/* 2944 */     if (i >= 15)
/*      */     {
/* 2946 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2950 */     i = Math.max(i, getStrongPower(pos.up(), EnumFacing.UP));
/*      */     
/* 2952 */     if (i >= 15)
/*      */     {
/* 2954 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2958 */     i = Math.max(i, getStrongPower(pos.north(), EnumFacing.NORTH));
/*      */     
/* 2960 */     if (i >= 15)
/*      */     {
/* 2962 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2966 */     i = Math.max(i, getStrongPower(pos.south(), EnumFacing.SOUTH));
/*      */     
/* 2968 */     if (i >= 15)
/*      */     {
/* 2970 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2974 */     i = Math.max(i, getStrongPower(pos.west(), EnumFacing.WEST));
/*      */     
/* 2976 */     if (i >= 15)
/*      */     {
/* 2978 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2982 */     i = Math.max(i, getStrongPower(pos.east(), EnumFacing.EAST));
/* 2983 */     return (i >= 15) ? i : i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSidePowered(BlockPos pos, EnumFacing side) {
/* 2993 */     return (getRedstonePower(pos, side) > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRedstonePower(BlockPos pos, EnumFacing facing) {
/* 2998 */     IBlockState iblockstate = getBlockState(pos);
/* 2999 */     Block block = iblockstate.getBlock();
/* 3000 */     return block.isNormalCube() ? getStrongPower(pos) : block.getWeakPower(this, pos, iblockstate, facing);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockPowered(BlockPos pos) {
/* 3005 */     return (getRedstonePower(pos.down(), EnumFacing.DOWN) > 0) ? true : ((getRedstonePower(pos.up(), EnumFacing.UP) > 0) ? true : ((getRedstonePower(pos.north(), EnumFacing.NORTH) > 0) ? true : ((getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0) ? true : ((getRedstonePower(pos.west(), EnumFacing.WEST) > 0) ? true : ((getRedstonePower(pos.east(), EnumFacing.EAST) > 0))))));
/*      */   }
/*      */ 
/*      */   
/*      */   public int isBlockIndirectlyGettingPowered(BlockPos pos) {
/* 3010 */     int i = 0;
/*      */     
/* 3012 */     for (EnumFacing enumfacing : EnumFacing.values()) {
/*      */       
/* 3014 */       int j = getRedstonePower(pos.offset(enumfacing), enumfacing);
/*      */       
/* 3016 */       if (j >= 15)
/*      */       {
/* 3018 */         return 15;
/*      */       }
/*      */       
/* 3021 */       if (j > i)
/*      */       {
/* 3023 */         i = j;
/*      */       }
/*      */     } 
/*      */     
/* 3027 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance) {
/* 3032 */     return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer getClosestPlayer(double x, double y, double z, double distance) {
/* 3037 */     double d0 = -1.0D;
/* 3038 */     EntityPlayer entityplayer = null;
/*      */     
/* 3040 */     for (int i = 0; i < this.playerEntities.size(); i++) {
/*      */       
/* 3042 */       EntityPlayer entityplayer1 = this.playerEntities.get(i);
/*      */       
/* 3044 */       if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1)) {
/*      */         
/* 3046 */         double d1 = entityplayer1.getDistanceSq(x, y, z);
/*      */         
/* 3048 */         if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0)) {
/*      */           
/* 3050 */           d0 = d1;
/* 3051 */           entityplayer = entityplayer1;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 3056 */     return entityplayer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range) {
/* 3061 */     for (int i = 0; i < this.playerEntities.size(); i++) {
/*      */       
/* 3063 */       EntityPlayer entityplayer = this.playerEntities.get(i);
/*      */       
/* 3065 */       if (EntitySelectors.NOT_SPECTATING.apply(entityplayer)) {
/*      */         
/* 3067 */         double d0 = entityplayer.getDistanceSq(x, y, z);
/*      */         
/* 3069 */         if (range < 0.0D || d0 < range * range)
/*      */         {
/* 3071 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3076 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer getPlayerEntityByName(String name) {
/* 3081 */     for (int i = 0; i < this.playerEntities.size(); i++) {
/*      */       
/* 3083 */       EntityPlayer entityplayer = this.playerEntities.get(i);
/*      */       
/* 3085 */       if (name.equals(entityplayer.getName()))
/*      */       {
/* 3087 */         return entityplayer;
/*      */       }
/*      */     } 
/*      */     
/* 3091 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityPlayer getPlayerEntityByUUID(UUID uuid) {
/* 3096 */     for (int i = 0; i < this.playerEntities.size(); i++) {
/*      */       
/* 3098 */       EntityPlayer entityplayer = this.playerEntities.get(i);
/*      */       
/* 3100 */       if (uuid.equals(entityplayer.getUniqueID()))
/*      */       {
/* 3102 */         return entityplayer;
/*      */       }
/*      */     } 
/*      */     
/* 3106 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendQuittingDisconnectingPacket() {}
/*      */ 
/*      */   
/*      */   public void checkSessionLock() throws MinecraftException {
/* 3115 */     this.saveHandler.checkSessionLock();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTotalWorldTime(long worldTime) {
/* 3120 */     this.worldInfo.setWorldTotalTime(worldTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getSeed() {
/* 3125 */     return this.worldInfo.getSeed();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getTotalWorldTime() {
/* 3130 */     return this.worldInfo.getWorldTotalTime();
/*      */   }
/*      */ 
/*      */   
/*      */   public long getWorldTime() {
/* 3135 */     return this.worldInfo.getWorldTime();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWorldTime(long time) {
/* 3140 */     this.worldInfo.setWorldTime(time);
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getSpawnPoint() {
/* 3145 */     BlockPos blockpos = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
/*      */     
/* 3147 */     if (!getWorldBorder().contains(blockpos))
/*      */     {
/* 3149 */       blockpos = getHeight(new BlockPos(getWorldBorder().getCenterX(), 0.0D, getWorldBorder().getCenterZ()));
/*      */     }
/*      */     
/* 3152 */     return blockpos;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpawnPoint(BlockPos pos) {
/* 3157 */     this.worldInfo.setSpawn(pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public void joinEntityInSurroundings(Entity entityIn) {
/* 3162 */     int i = MathHelper.floor_double(entityIn.posX / 16.0D);
/* 3163 */     int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
/* 3164 */     int k = 2;
/*      */     
/* 3166 */     for (int l = i - k; l <= i + k; l++) {
/*      */       
/* 3168 */       for (int i1 = j - k; i1 <= j + k; i1++)
/*      */       {
/* 3170 */         getChunkFromChunkCoords(l, i1);
/*      */       }
/*      */     } 
/*      */     
/* 3174 */     if (!this.loadedEntityList.contains(entityIn))
/*      */     {
/* 3176 */       this.loadedEntityList.add(entityIn);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockModifiable(EntityPlayer player, BlockPos pos) {
/* 3182 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEntityState(Entity entityIn, byte state) {}
/*      */ 
/*      */   
/*      */   public IChunkProvider getChunkProvider() {
/* 3191 */     return this.chunkProvider;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
/* 3196 */     blockIn.onBlockEventReceived(this, pos, getBlockState(pos), eventID, eventParam);
/*      */   }
/*      */ 
/*      */   
/*      */   public ISaveHandler getSaveHandler() {
/* 3201 */     return this.saveHandler;
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldInfo getWorldInfo() {
/* 3206 */     return this.worldInfo;
/*      */   }
/*      */ 
/*      */   
/*      */   public GameRules getGameRules() {
/* 3211 */     return this.worldInfo.getGameRulesInstance();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateAllPlayersSleepingFlag() {}
/*      */ 
/*      */   
/*      */   public float getThunderStrength(float delta) {
/* 3220 */     return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * getRainStrength(delta);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setThunderStrength(float strength) {
/* 3225 */     this.prevThunderingStrength = strength;
/* 3226 */     this.thunderingStrength = strength;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRainStrength(float delta) {
/* 3231 */     return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRainStrength(float strength) {
/* 3236 */     this.prevRainingStrength = strength;
/* 3237 */     this.rainingStrength = strength;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isThundering() {
/* 3242 */     return (getThunderStrength(1.0F) > 0.9D);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRaining() {
/* 3247 */     return (getRainStrength(1.0F) > 0.2D);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRainingAt(BlockPos strikePosition) {
/* 3252 */     if (!isRaining())
/*      */     {
/* 3254 */       return false;
/*      */     }
/* 3256 */     if (!canSeeSky(strikePosition))
/*      */     {
/* 3258 */       return false;
/*      */     }
/* 3260 */     if (getPrecipitationHeight(strikePosition).getY() > strikePosition.getY())
/*      */     {
/* 3262 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 3266 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(strikePosition);
/* 3267 */     return biomegenbase.getEnableSnow() ? false : (canSnowAt(strikePosition, false) ? false : biomegenbase.canRain());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlockinHighHumidity(BlockPos pos) {
/* 3273 */     BiomeGenBase biomegenbase = getBiomeGenForCoords(pos);
/* 3274 */     return biomegenbase.isHighHumidity();
/*      */   }
/*      */ 
/*      */   
/*      */   public MapStorage getMapStorage() {
/* 3279 */     return this.mapStorage;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemData(String dataID, WorldSavedData worldSavedDataIn) {
/* 3284 */     this.mapStorage.setData(dataID, worldSavedDataIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldSavedData loadItemData(Class<? extends WorldSavedData> clazz, String dataID) {
/* 3289 */     return this.mapStorage.loadData(clazz, dataID);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUniqueDataId(String key) {
/* 3294 */     return this.mapStorage.getUniqueDataId(key);
/*      */   }
/*      */ 
/*      */   
/*      */   public void playBroadcastSound(int p_175669_1_, BlockPos pos, int p_175669_3_) {
/* 3299 */     for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */     {
/* 3301 */       ((IWorldAccess)this.worldAccesses.get(i)).broadcastSound(p_175669_1_, pos, p_175669_3_);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void playAuxSFX(int p_175718_1_, BlockPos pos, int p_175718_3_) {
/* 3307 */     playAuxSFXAtEntity((EntityPlayer)null, p_175718_1_, pos, p_175718_3_);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playAuxSFXAtEntity(EntityPlayer player, int sfxType, BlockPos pos, int p_180498_4_) {
/*      */     try {
/* 3314 */       for (int i = 0; i < this.worldAccesses.size(); i++)
/*      */       {
/* 3316 */         ((IWorldAccess)this.worldAccesses.get(i)).playAuxSFX(player, sfxType, pos, p_180498_4_);
/*      */       }
/*      */     }
/* 3319 */     catch (Throwable throwable) {
/*      */       
/* 3321 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Playing level event");
/* 3322 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Level event being played");
/* 3323 */       crashreportcategory.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
/* 3324 */       crashreportcategory.addCrashSection("Event source", player);
/* 3325 */       crashreportcategory.addCrashSection("Event type", Integer.valueOf(sfxType));
/* 3326 */       crashreportcategory.addCrashSection("Event data", Integer.valueOf(p_180498_4_));
/* 3327 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHeight() {
/* 3333 */     return 256;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getActualHeight() {
/* 3338 */     return this.provider.getHasNoSky() ? 128 : 256;
/*      */   }
/*      */ 
/*      */   
/*      */   public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_) {
/* 3343 */     long i = p_72843_1_ * 341873128712L + p_72843_2_ * 132897987541L + getWorldInfo().getSeed() + p_72843_3_;
/* 3344 */     this.rand.setSeed(i);
/* 3345 */     return this.rand;
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getStrongholdPos(String name, BlockPos pos) {
/* 3350 */     return getChunkProvider().getStrongholdGen(this, name, pos);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean extendedLevelsInChunkCache() {
/* 3355 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getHorizon() {
/* 3360 */     return (this.worldInfo.getTerrainType() == WorldType.FLAT) ? 0.0D : 63.0D;
/*      */   }
/*      */ 
/*      */   
/*      */   public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
/* 3365 */     CrashReportCategory crashreportcategory = report.makeCategoryDepth("Affected level", 1);
/* 3366 */     crashreportcategory.addCrashSection("Level name", (this.worldInfo == null) ? "????" : this.worldInfo.getWorldName());
/* 3367 */     crashreportcategory.addCrashSectionCallable("All players", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 3371 */             return World.this.playerEntities.size() + " total; " + World.this.playerEntities.toString();
/*      */           }
/*      */         });
/* 3374 */     crashreportcategory.addCrashSectionCallable("Chunk stats", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 3378 */             return World.this.chunkProvider.makeString();
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*      */     try {
/* 3384 */       this.worldInfo.addToCrashReport(crashreportcategory);
/*      */     }
/* 3386 */     catch (Throwable throwable) {
/*      */       
/* 3388 */       crashreportcategory.addCrashSectionThrowable("Level Data Unobtainable", throwable);
/*      */     } 
/*      */     
/* 3391 */     return crashreportcategory;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
/* 3396 */     for (int i = 0; i < this.worldAccesses.size(); i++) {
/*      */       
/* 3398 */       IWorldAccess iworldaccess = this.worldAccesses.get(i);
/* 3399 */       iworldaccess.sendBlockBreakProgress(breakerId, pos, progress);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Calendar getCurrentDate() {
/* 3405 */     if (getTotalWorldTime() % 600L == 0L)
/*      */     {
/* 3407 */       this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
/*      */     }
/*      */     
/* 3410 */     return this.theCalendar;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund) {}
/*      */ 
/*      */   
/*      */   public Scoreboard getScoreboard() {
/* 3419 */     return this.worldScoreboard;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateComparatorOutputLevel(BlockPos pos, Block blockIn) {
/* 3424 */     for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*      */       
/* 3426 */       BlockPos blockpos = pos.offset(enumfacing);
/*      */       
/* 3428 */       if (isBlockLoaded(blockpos)) {
/*      */         
/* 3430 */         IBlockState iblockstate = getBlockState(blockpos);
/*      */         
/* 3432 */         if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
/*      */           
/* 3434 */           iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn); continue;
/*      */         } 
/* 3436 */         if (iblockstate.getBlock().isNormalCube()) {
/*      */           
/* 3438 */           blockpos = blockpos.offset(enumfacing);
/* 3439 */           iblockstate = getBlockState(blockpos);
/*      */           
/* 3441 */           if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock()))
/*      */           {
/* 3443 */             iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
/* 3452 */     long i = 0L;
/* 3453 */     float f = 0.0F;
/*      */     
/* 3455 */     if (isBlockLoaded(pos)) {
/*      */       
/* 3457 */       f = getCurrentMoonPhaseFactor();
/* 3458 */       i = getChunkFromBlockCoords(pos).getInhabitedTime();
/*      */     } 
/*      */     
/* 3461 */     return new DifficultyInstance(getDifficulty(), getWorldTime(), i, f);
/*      */   }
/*      */ 
/*      */   
/*      */   public EnumDifficulty getDifficulty() {
/* 3466 */     return getWorldInfo().getDifficulty();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSkylightSubtracted() {
/* 3471 */     return this.skylightSubtracted;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSkylightSubtracted(int newSkylightSubtracted) {
/* 3476 */     this.skylightSubtracted = newSkylightSubtracted;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLastLightningBolt() {
/* 3481 */     return this.lastLightningBolt;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLastLightningBolt(int lastLightningBoltIn) {
/* 3486 */     this.lastLightningBolt = lastLightningBoltIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFindingSpawnPoint() {
/* 3491 */     return this.findingSpawnPoint;
/*      */   }
/*      */ 
/*      */   
/*      */   public VillageCollection getVillageCollection() {
/* 3496 */     return this.villageCollectionObj;
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldBorder getWorldBorder() {
/* 3501 */     return this.worldBorder;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSpawnChunk(int x, int z) {
/* 3506 */     BlockPos blockpos = getSpawnPoint();
/* 3507 */     int i = x * 16 + 8 - blockpos.getX();
/* 3508 */     int j = z * 16 + 8 - blockpos.getZ();
/* 3509 */     int k = 128;
/* 3510 */     return (i >= -k && i <= k && j >= -k && j <= k);
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\World.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */