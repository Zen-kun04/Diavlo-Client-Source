/*      */ package net.minecraft.world.chunk;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Queues;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.ITileEntityProvider;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ClassInheritanceMultiMap;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.world.ChunkCoordIntPair;
/*      */ import net.minecraft.world.EnumSkyBlock;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldType;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.biome.WorldChunkManager;
/*      */ import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
/*      */ import net.minecraft.world.gen.ChunkProviderDebug;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class Chunk {
/*   40 */   private static final Logger logger = LogManager.getLogger();
/*      */   
/*      */   private final ExtendedBlockStorage[] storageArrays;
/*      */   private final byte[] blockBiomeArray;
/*      */   private final int[] precipitationHeightMap;
/*      */   private final boolean[] updateSkylightColumns;
/*      */   private boolean isChunkLoaded;
/*      */   private final World worldObj;
/*      */   private final int[] heightMap;
/*      */   public final int xPosition;
/*      */   public final int zPosition;
/*      */   private boolean isGapLightingUpdated;
/*      */   private final Map<BlockPos, TileEntity> chunkTileEntityMap;
/*      */   private final ClassInheritanceMultiMap<Entity>[] entityLists;
/*      */   private boolean isTerrainPopulated;
/*      */   private boolean isLightPopulated;
/*      */   private boolean field_150815_m;
/*      */   private boolean isModified;
/*      */   private boolean hasEntities;
/*      */   private long lastSaveTime;
/*      */   private int heightMapMinimum;
/*      */   private long inhabitedTime;
/*      */   private int queuedLightChecks;
/*      */   private ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;
/*      */   
/*      */   public Chunk(World worldIn, int x, int z) {
/*   66 */     this.storageArrays = new ExtendedBlockStorage[16];
/*   67 */     this.blockBiomeArray = new byte[256];
/*   68 */     this.precipitationHeightMap = new int[256];
/*   69 */     this.updateSkylightColumns = new boolean[256];
/*   70 */     this.chunkTileEntityMap = Maps.newHashMap();
/*   71 */     this.queuedLightChecks = 4096;
/*   72 */     this.tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
/*   73 */     this.entityLists = (ClassInheritanceMultiMap<Entity>[])new ClassInheritanceMultiMap[16];
/*   74 */     this.worldObj = worldIn;
/*   75 */     this.xPosition = x;
/*   76 */     this.zPosition = z;
/*   77 */     this.heightMap = new int[256];
/*      */     
/*   79 */     for (int i = 0; i < this.entityLists.length; i++)
/*      */     {
/*   81 */       this.entityLists[i] = new ClassInheritanceMultiMap(Entity.class);
/*      */     }
/*      */     
/*   84 */     Arrays.fill(this.precipitationHeightMap, -999);
/*   85 */     Arrays.fill(this.blockBiomeArray, (byte)-1);
/*      */   }
/*      */ 
/*      */   
/*      */   public Chunk(World worldIn, ChunkPrimer primer, int x, int z) {
/*   90 */     this(worldIn, x, z);
/*   91 */     int i = 256;
/*   92 */     boolean flag = !worldIn.provider.getHasNoSky();
/*      */     
/*   94 */     for (int j = 0; j < 16; j++) {
/*      */       
/*   96 */       for (int k = 0; k < 16; k++) {
/*      */         
/*   98 */         for (int l = 0; l < i; l++) {
/*      */           
/*  100 */           int i1 = j * i * 16 | k * i | l;
/*  101 */           IBlockState iblockstate = primer.getBlockState(i1);
/*      */           
/*  103 */           if (iblockstate.getBlock().getMaterial() != Material.air) {
/*      */             
/*  105 */             int j1 = l >> 4;
/*      */             
/*  107 */             if (this.storageArrays[j1] == null)
/*      */             {
/*  109 */               this.storageArrays[j1] = new ExtendedBlockStorage(j1 << 4, flag);
/*      */             }
/*      */             
/*  112 */             this.storageArrays[j1].set(j, l & 0xF, k, iblockstate);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAtLocation(int x, int z) {
/*  121 */     return (x == this.xPosition && z == this.zPosition);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHeight(BlockPos pos) {
/*  126 */     return getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHeightValue(int x, int z) {
/*  131 */     return this.heightMap[z << 4 | x];
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTopFilledSegment() {
/*  136 */     for (int i = this.storageArrays.length - 1; i >= 0; i--) {
/*      */       
/*  138 */       if (this.storageArrays[i] != null)
/*      */       {
/*  140 */         return this.storageArrays[i].getYLocation();
/*      */       }
/*      */     } 
/*      */     
/*  144 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public ExtendedBlockStorage[] getBlockStorageArray() {
/*  149 */     return this.storageArrays;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generateHeightMap() {
/*  154 */     int i = getTopFilledSegment();
/*  155 */     this.heightMapMinimum = Integer.MAX_VALUE;
/*      */     
/*  157 */     for (int j = 0; j < 16; j++) {
/*      */       
/*  159 */       for (int k = 0; k < 16; k++) {
/*      */         
/*  161 */         this.precipitationHeightMap[j + (k << 4)] = -999;
/*      */         
/*  163 */         for (int l = i + 16; l > 0; l--) {
/*      */           
/*  165 */           Block block = getBlock0(j, l - 1, k);
/*      */           
/*  167 */           if (block.getLightOpacity() != 0) {
/*      */             
/*  169 */             this.heightMap[k << 4 | j] = l;
/*      */             
/*  171 */             if (l < this.heightMapMinimum)
/*      */             {
/*  173 */               this.heightMapMinimum = l;
/*      */             }
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  182 */     this.isModified = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void generateSkylightMap() {
/*  187 */     int i = getTopFilledSegment();
/*  188 */     this.heightMapMinimum = Integer.MAX_VALUE;
/*      */     
/*  190 */     for (int j = 0; j < 16; j++) {
/*      */       
/*  192 */       for (int k = 0; k < 16; k++) {
/*      */         
/*  194 */         this.precipitationHeightMap[j + (k << 4)] = -999;
/*      */         
/*  196 */         for (int l = i + 16; l > 0; l--) {
/*      */           
/*  198 */           if (getBlockLightOpacity(j, l - 1, k) != 0) {
/*      */             
/*  200 */             this.heightMap[k << 4 | j] = l;
/*      */             
/*  202 */             if (l < this.heightMapMinimum)
/*      */             {
/*  204 */               this.heightMapMinimum = l;
/*      */             }
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/*  211 */         if (!this.worldObj.provider.getHasNoSky()) {
/*      */           
/*  213 */           int k1 = 15;
/*  214 */           int i1 = i + 16 - 1;
/*      */ 
/*      */           
/*      */           do {
/*  218 */             int j1 = getBlockLightOpacity(j, i1, k);
/*      */             
/*  220 */             if (j1 == 0 && k1 != 15)
/*      */             {
/*  222 */               j1 = 1;
/*      */             }
/*      */             
/*  225 */             k1 -= j1;
/*      */             
/*  227 */             if (k1 <= 0)
/*      */               continue; 
/*  229 */             ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
/*      */             
/*  231 */             if (extendedblockstorage == null)
/*      */               continue; 
/*  233 */             extendedblockstorage.setExtSkylightValue(j, i1 & 0xF, k, k1);
/*  234 */             this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k));
/*      */ 
/*      */ 
/*      */             
/*  238 */             --i1;
/*      */           }
/*  240 */           while (i1 > 0 && k1 > 0);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  249 */     this.isModified = true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void propagateSkylightOcclusion(int x, int z) {
/*  254 */     this.updateSkylightColumns[x + z * 16] = true;
/*  255 */     this.isGapLightingUpdated = true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void recheckGaps(boolean p_150803_1_) {
/*  260 */     this.worldObj.theProfiler.startSection("recheckGaps");
/*      */     
/*  262 */     if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
/*      */       
/*  264 */       for (int i = 0; i < 16; i++) {
/*      */         
/*  266 */         for (int j = 0; j < 16; j++) {
/*      */           
/*  268 */           if (this.updateSkylightColumns[i + j * 16]) {
/*      */             
/*  270 */             this.updateSkylightColumns[i + j * 16] = false;
/*  271 */             int k = getHeightValue(i, j);
/*  272 */             int l = this.xPosition * 16 + i;
/*  273 */             int i1 = this.zPosition * 16 + j;
/*  274 */             int j1 = Integer.MAX_VALUE;
/*      */             
/*  276 */             for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
/*      */             {
/*  278 */               j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l + enumfacing.getFrontOffsetX(), i1 + enumfacing.getFrontOffsetZ()));
/*      */             }
/*      */             
/*  281 */             checkSkylightNeighborHeight(l, i1, j1);
/*      */             
/*  283 */             for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
/*      */             {
/*  285 */               checkSkylightNeighborHeight(l + enumfacing1.getFrontOffsetX(), i1 + enumfacing1.getFrontOffsetZ(), k);
/*      */             }
/*      */             
/*  288 */             if (p_150803_1_) {
/*      */               
/*  290 */               this.worldObj.theProfiler.endSection();
/*      */               
/*      */               return;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  297 */       this.isGapLightingUpdated = false;
/*      */     } 
/*      */     
/*  300 */     this.worldObj.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkSkylightNeighborHeight(int x, int z, int maxValue) {
/*  305 */     int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
/*      */     
/*  307 */     if (i > maxValue) {
/*      */       
/*  309 */       updateSkylightNeighborHeight(x, z, maxValue, i + 1);
/*      */     }
/*  311 */     else if (i < maxValue) {
/*      */       
/*  313 */       updateSkylightNeighborHeight(x, z, i, maxValue + 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateSkylightNeighborHeight(int x, int z, int startY, int endY) {
/*  319 */     if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
/*      */       
/*  321 */       for (int i = startY; i < endY; i++)
/*      */       {
/*  323 */         this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
/*      */       }
/*      */       
/*  326 */       this.isModified = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void relightBlock(int x, int y, int z) {
/*  332 */     int i = this.heightMap[z << 4 | x] & 0xFF;
/*  333 */     int j = i;
/*      */     
/*  335 */     if (y > i)
/*      */     {
/*  337 */       j = y;
/*      */     }
/*      */     
/*  340 */     while (j > 0 && getBlockLightOpacity(x, j - 1, z) == 0)
/*      */     {
/*  342 */       j--;
/*      */     }
/*      */     
/*  345 */     if (j != i) {
/*      */       
/*  347 */       this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, j, i);
/*  348 */       this.heightMap[z << 4 | x] = j;
/*  349 */       int k = this.xPosition * 16 + x;
/*  350 */       int l = this.zPosition * 16 + z;
/*      */       
/*  352 */       if (!this.worldObj.provider.getHasNoSky()) {
/*      */         
/*  354 */         if (j < i) {
/*      */           
/*  356 */           for (int j1 = j; j1 < i; j1++) {
/*      */             
/*  358 */             ExtendedBlockStorage extendedblockstorage2 = this.storageArrays[j1 >> 4];
/*      */             
/*  360 */             if (extendedblockstorage2 != null)
/*      */             {
/*  362 */               extendedblockstorage2.setExtSkylightValue(x, j1 & 0xF, z, 15);
/*  363 */               this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, j1, (this.zPosition << 4) + z));
/*      */             }
/*      */           
/*      */           } 
/*      */         } else {
/*      */           
/*  369 */           for (int i1 = i; i1 < j; i1++) {
/*      */             
/*  371 */             ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
/*      */             
/*  373 */             if (extendedblockstorage != null) {
/*      */               
/*  375 */               extendedblockstorage.setExtSkylightValue(x, i1 & 0xF, z, 0);
/*  376 */               this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, i1, (this.zPosition << 4) + z));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  381 */         int k1 = 15;
/*      */         
/*  383 */         while (j > 0 && k1 > 0) {
/*      */           
/*  385 */           j--;
/*  386 */           int i2 = getBlockLightOpacity(x, j, z);
/*      */           
/*  388 */           if (i2 == 0)
/*      */           {
/*  390 */             i2 = 1;
/*      */           }
/*      */           
/*  393 */           k1 -= i2;
/*      */           
/*  395 */           if (k1 < 0)
/*      */           {
/*  397 */             k1 = 0;
/*      */           }
/*      */           
/*  400 */           ExtendedBlockStorage extendedblockstorage1 = this.storageArrays[j >> 4];
/*      */           
/*  402 */           if (extendedblockstorage1 != null)
/*      */           {
/*  404 */             extendedblockstorage1.setExtSkylightValue(x, j & 0xF, z, k1);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  409 */       int l1 = this.heightMap[z << 4 | x];
/*  410 */       int j2 = i;
/*  411 */       int k2 = l1;
/*      */       
/*  413 */       if (l1 < i) {
/*      */         
/*  415 */         j2 = l1;
/*  416 */         k2 = i;
/*      */       } 
/*      */       
/*  419 */       if (l1 < this.heightMapMinimum)
/*      */       {
/*  421 */         this.heightMapMinimum = l1;
/*      */       }
/*      */       
/*  424 */       if (!this.worldObj.provider.getHasNoSky()) {
/*      */         
/*  426 */         for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
/*      */         {
/*  428 */           updateSkylightNeighborHeight(k + enumfacing.getFrontOffsetX(), l + enumfacing.getFrontOffsetZ(), j2, k2);
/*      */         }
/*      */         
/*  431 */         updateSkylightNeighborHeight(k, l, j2, k2);
/*      */       } 
/*      */       
/*  434 */       this.isModified = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBlockLightOpacity(BlockPos pos) {
/*  440 */     return getBlock(pos).getLightOpacity();
/*      */   }
/*      */ 
/*      */   
/*      */   private int getBlockLightOpacity(int x, int y, int z) {
/*  445 */     return getBlock0(x, y, z).getLightOpacity();
/*      */   }
/*      */ 
/*      */   
/*      */   private Block getBlock0(int x, int y, int z) {
/*  450 */     Block block = Blocks.air;
/*      */     
/*  452 */     if (y >= 0 && y >> 4 < this.storageArrays.length) {
/*      */       
/*  454 */       ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
/*      */       
/*  456 */       if (extendedblockstorage != null) {
/*      */         
/*      */         try {
/*      */           
/*  460 */           block = extendedblockstorage.getBlockByExtId(x, y & 0xF, z);
/*      */         }
/*  462 */         catch (Throwable throwable) {
/*      */           
/*  464 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
/*  465 */           throw new ReportedException(crashreport);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  470 */     return block;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Block getBlock(final int x, final int y, final int z) {
/*      */     try {
/*  477 */       return getBlock0(x & 0xF, y, z & 0xF);
/*      */     }
/*  479 */     catch (ReportedException reportedexception) {
/*      */       
/*  481 */       CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
/*  482 */       crashreportcategory.addCrashSectionCallable("Location", new Callable<String>()
/*      */           {
/*      */             public String call() throws Exception
/*      */             {
/*  486 */               return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));
/*      */             }
/*      */           });
/*  489 */       throw reportedexception;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Block getBlock(final BlockPos pos) {
/*      */     try {
/*  497 */       return getBlock0(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
/*      */     }
/*  499 */     catch (ReportedException reportedexception) {
/*      */       
/*  501 */       CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
/*  502 */       crashreportcategory.addCrashSectionCallable("Location", new Callable<String>()
/*      */           {
/*      */             public String call() throws Exception
/*      */             {
/*  506 */               return CrashReportCategory.getCoordinateInfo(pos);
/*      */             }
/*      */           });
/*  509 */       throw reportedexception;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public IBlockState getBlockState(final BlockPos pos) {
/*  515 */     if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
/*      */       
/*  517 */       IBlockState iblockstate = null;
/*      */       
/*  519 */       if (pos.getY() == 60)
/*      */       {
/*  521 */         iblockstate = Blocks.barrier.getDefaultState();
/*      */       }
/*      */       
/*  524 */       if (pos.getY() == 70)
/*      */       {
/*  526 */         iblockstate = ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
/*      */       }
/*      */       
/*  529 */       return (iblockstate == null) ? Blocks.air.getDefaultState() : iblockstate;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  535 */       if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length) {
/*      */         
/*  537 */         ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
/*      */         
/*  539 */         if (extendedblockstorage != null) {
/*      */           
/*  541 */           int j = pos.getX() & 0xF;
/*  542 */           int k = pos.getY() & 0xF;
/*  543 */           int i = pos.getZ() & 0xF;
/*  544 */           return extendedblockstorage.get(j, k, i);
/*      */         } 
/*      */       } 
/*      */       
/*  548 */       return Blocks.air.getDefaultState();
/*      */     }
/*  550 */     catch (Throwable throwable) {
/*      */       
/*  552 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
/*  553 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
/*  554 */       crashreportcategory.addCrashSectionCallable("Location", new Callable<String>()
/*      */           {
/*      */             public String call() throws Exception
/*      */             {
/*  558 */               return CrashReportCategory.getCoordinateInfo(pos);
/*      */             }
/*      */           });
/*  561 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int getBlockMetadata(int x, int y, int z) {
/*  568 */     if (y >> 4 >= this.storageArrays.length)
/*      */     {
/*  570 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  574 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
/*  575 */     return (extendedblockstorage != null) ? extendedblockstorage.getExtBlockMetadata(x, y & 0xF, z) : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getBlockMetadata(BlockPos pos) {
/*  581 */     return getBlockMetadata(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
/*      */   }
/*      */ 
/*      */   
/*      */   public IBlockState setBlockState(BlockPos pos, IBlockState state) {
/*  586 */     int i = pos.getX() & 0xF;
/*  587 */     int j = pos.getY();
/*  588 */     int k = pos.getZ() & 0xF;
/*  589 */     int l = k << 4 | i;
/*      */     
/*  591 */     if (j >= this.precipitationHeightMap[l] - 1)
/*      */     {
/*  593 */       this.precipitationHeightMap[l] = -999;
/*      */     }
/*      */     
/*  596 */     int i1 = this.heightMap[l];
/*  597 */     IBlockState iblockstate = getBlockState(pos);
/*      */     
/*  599 */     if (iblockstate == state)
/*      */     {
/*  601 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  605 */     Block block = state.getBlock();
/*  606 */     Block block1 = iblockstate.getBlock();
/*  607 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
/*  608 */     boolean flag = false;
/*      */     
/*  610 */     if (extendedblockstorage == null) {
/*      */       
/*  612 */       if (block == Blocks.air)
/*      */       {
/*  614 */         return null;
/*      */       }
/*      */       
/*  617 */       extendedblockstorage = this.storageArrays[j >> 4] = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
/*  618 */       flag = (j >= i1);
/*      */     } 
/*      */     
/*  621 */     extendedblockstorage.set(i, j & 0xF, k, state);
/*      */     
/*  623 */     if (block1 != block)
/*      */     {
/*  625 */       if (!this.worldObj.isRemote) {
/*      */         
/*  627 */         block1.breakBlock(this.worldObj, pos, iblockstate);
/*      */       }
/*  629 */       else if (block1 instanceof ITileEntityProvider) {
/*      */         
/*  631 */         this.worldObj.removeTileEntity(pos);
/*      */       } 
/*      */     }
/*      */     
/*  635 */     if (extendedblockstorage.getBlockByExtId(i, j & 0xF, k) != block)
/*      */     {
/*  637 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  641 */     if (flag) {
/*      */       
/*  643 */       generateSkylightMap();
/*      */     }
/*      */     else {
/*      */       
/*  647 */       int j1 = block.getLightOpacity();
/*  648 */       int k1 = block1.getLightOpacity();
/*      */       
/*  650 */       if (j1 > 0) {
/*      */         
/*  652 */         if (j >= i1)
/*      */         {
/*  654 */           relightBlock(i, j + 1, k);
/*      */         }
/*      */       }
/*  657 */       else if (j == i1 - 1) {
/*      */         
/*  659 */         relightBlock(i, j, k);
/*      */       } 
/*      */       
/*  662 */       if (j1 != k1 && (j1 < k1 || getLightFor(EnumSkyBlock.SKY, pos) > 0 || getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
/*      */       {
/*  664 */         propagateSkylightOcclusion(i, k);
/*      */       }
/*      */     } 
/*      */     
/*  668 */     if (block1 instanceof ITileEntityProvider) {
/*      */       
/*  670 */       TileEntity tileentity = getTileEntity(pos, EnumCreateEntityType.CHECK);
/*      */       
/*  672 */       if (tileentity != null)
/*      */       {
/*  674 */         tileentity.updateContainingBlockInfo();
/*      */       }
/*      */     } 
/*      */     
/*  678 */     if (!this.worldObj.isRemote && block1 != block)
/*      */     {
/*  680 */       block.onBlockAdded(this.worldObj, pos, state);
/*      */     }
/*      */     
/*  683 */     if (block instanceof ITileEntityProvider) {
/*      */       
/*  685 */       TileEntity tileentity1 = getTileEntity(pos, EnumCreateEntityType.CHECK);
/*      */       
/*  687 */       if (tileentity1 == null) {
/*      */         
/*  689 */         tileentity1 = ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, block.getMetaFromState(state));
/*  690 */         this.worldObj.setTileEntity(pos, tileentity1);
/*      */       } 
/*      */       
/*  693 */       if (tileentity1 != null)
/*      */       {
/*  695 */         tileentity1.updateContainingBlockInfo();
/*      */       }
/*      */     } 
/*      */     
/*  699 */     this.isModified = true;
/*  700 */     return iblockstate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos) {
/*  707 */     int i = pos.getX() & 0xF;
/*  708 */     int j = pos.getY();
/*  709 */     int k = pos.getZ() & 0xF;
/*  710 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
/*  711 */     return (extendedblockstorage == null) ? (canSeeSky(pos) ? p_177413_1_.defaultLightValue : 0) : ((p_177413_1_ == EnumSkyBlock.SKY) ? (this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k)) : ((p_177413_1_ == EnumSkyBlock.BLOCK) ? extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k) : p_177413_1_.defaultLightValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {
/*  716 */     int i = pos.getX() & 0xF;
/*  717 */     int j = pos.getY();
/*  718 */     int k = pos.getZ() & 0xF;
/*  719 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
/*      */     
/*  721 */     if (extendedblockstorage == null) {
/*      */       
/*  723 */       extendedblockstorage = this.storageArrays[j >> 4] = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
/*  724 */       generateSkylightMap();
/*      */     } 
/*      */     
/*  727 */     this.isModified = true;
/*      */     
/*  729 */     if (p_177431_1_ == EnumSkyBlock.SKY) {
/*      */       
/*  731 */       if (!this.worldObj.provider.getHasNoSky())
/*      */       {
/*  733 */         extendedblockstorage.setExtSkylightValue(i, j & 0xF, k, value);
/*      */       }
/*      */     }
/*  736 */     else if (p_177431_1_ == EnumSkyBlock.BLOCK) {
/*      */       
/*  738 */       extendedblockstorage.setExtBlocklightValue(i, j & 0xF, k, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLightSubtracted(BlockPos pos, int amount) {
/*  744 */     int i = pos.getX() & 0xF;
/*  745 */     int j = pos.getY();
/*  746 */     int k = pos.getZ() & 0xF;
/*  747 */     ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
/*      */     
/*  749 */     if (extendedblockstorage == null)
/*      */     {
/*  751 */       return (!this.worldObj.provider.getHasNoSky() && amount < EnumSkyBlock.SKY.defaultLightValue) ? (EnumSkyBlock.SKY.defaultLightValue - amount) : 0;
/*      */     }
/*      */ 
/*      */     
/*  755 */     int l = this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
/*  756 */     l -= amount;
/*  757 */     int i1 = extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k);
/*      */     
/*  759 */     if (i1 > l)
/*      */     {
/*  761 */       l = i1;
/*      */     }
/*      */     
/*  764 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addEntity(Entity entityIn) {
/*  770 */     this.hasEntities = true;
/*  771 */     int i = MathHelper.floor_double(entityIn.posX / 16.0D);
/*  772 */     int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
/*      */     
/*  774 */     if (i != this.xPosition || j != this.zPosition) {
/*      */       
/*  776 */       logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, new Object[] { entityIn });
/*  777 */       entityIn.setDead();
/*      */     } 
/*      */     
/*  780 */     int k = MathHelper.floor_double(entityIn.posY / 16.0D);
/*      */     
/*  782 */     if (k < 0)
/*      */     {
/*  784 */       k = 0;
/*      */     }
/*      */     
/*  787 */     if (k >= this.entityLists.length)
/*      */     {
/*  789 */       k = this.entityLists.length - 1;
/*      */     }
/*      */     
/*  792 */     entityIn.addedToChunk = true;
/*  793 */     entityIn.chunkCoordX = this.xPosition;
/*  794 */     entityIn.chunkCoordY = k;
/*  795 */     entityIn.chunkCoordZ = this.zPosition;
/*  796 */     this.entityLists[k].add(entityIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeEntity(Entity entityIn) {
/*  801 */     removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeEntityAtIndex(Entity entityIn, int p_76608_2_) {
/*  806 */     if (p_76608_2_ < 0)
/*      */     {
/*  808 */       p_76608_2_ = 0;
/*      */     }
/*      */     
/*  811 */     if (p_76608_2_ >= this.entityLists.length)
/*      */     {
/*  813 */       p_76608_2_ = this.entityLists.length - 1;
/*      */     }
/*      */     
/*  816 */     this.entityLists[p_76608_2_].remove(entityIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canSeeSky(BlockPos pos) {
/*  821 */     int i = pos.getX() & 0xF;
/*  822 */     int j = pos.getY();
/*  823 */     int k = pos.getZ() & 0xF;
/*  824 */     return (j >= this.heightMap[k << 4 | i]);
/*      */   }
/*      */ 
/*      */   
/*      */   private TileEntity createNewTileEntity(BlockPos pos) {
/*  829 */     Block block = getBlock(pos);
/*  830 */     return !block.hasTileEntity() ? null : ((ITileEntityProvider)block).createNewTileEntity(this.worldObj, getBlockMetadata(pos));
/*      */   }
/*      */ 
/*      */   
/*      */   public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_177424_2_) {
/*  835 */     TileEntity tileentity = this.chunkTileEntityMap.get(pos);
/*      */     
/*  837 */     if (tileentity == null) {
/*      */       
/*  839 */       if (p_177424_2_ == EnumCreateEntityType.IMMEDIATE)
/*      */       {
/*  841 */         tileentity = createNewTileEntity(pos);
/*  842 */         this.worldObj.setTileEntity(pos, tileentity);
/*      */       }
/*  844 */       else if (p_177424_2_ == EnumCreateEntityType.QUEUED)
/*      */       {
/*  846 */         this.tileEntityPosQueue.add(pos);
/*      */       }
/*      */     
/*  849 */     } else if (tileentity.isInvalid()) {
/*      */       
/*  851 */       this.chunkTileEntityMap.remove(pos);
/*  852 */       return null;
/*      */     } 
/*      */     
/*  855 */     return tileentity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addTileEntity(TileEntity tileEntityIn) {
/*  860 */     addTileEntity(tileEntityIn.getPos(), tileEntityIn);
/*      */     
/*  862 */     if (this.isChunkLoaded)
/*      */     {
/*  864 */       this.worldObj.addTileEntity(tileEntityIn);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
/*  870 */     tileEntityIn.setWorldObj(this.worldObj);
/*  871 */     tileEntityIn.setPos(pos);
/*      */     
/*  873 */     if (getBlock(pos) instanceof ITileEntityProvider) {
/*      */       
/*  875 */       if (this.chunkTileEntityMap.containsKey(pos))
/*      */       {
/*  877 */         ((TileEntity)this.chunkTileEntityMap.get(pos)).invalidate();
/*      */       }
/*      */       
/*  880 */       tileEntityIn.validate();
/*  881 */       this.chunkTileEntityMap.put(pos, tileEntityIn);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeTileEntity(BlockPos pos) {
/*  887 */     if (this.isChunkLoaded) {
/*      */       
/*  889 */       TileEntity tileentity = this.chunkTileEntityMap.remove(pos);
/*      */       
/*  891 */       if (tileentity != null)
/*      */       {
/*  893 */         tileentity.invalidate();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onChunkLoad() {
/*  900 */     this.isChunkLoaded = true;
/*  901 */     this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
/*      */     
/*  903 */     for (int i = 0; i < this.entityLists.length; i++) {
/*      */       
/*  905 */       for (Entity entity : this.entityLists[i])
/*      */       {
/*  907 */         entity.onChunkLoad();
/*      */       }
/*      */       
/*  910 */       this.worldObj.loadEntities((Collection)this.entityLists[i]);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onChunkUnload() {
/*  916 */     this.isChunkLoaded = false;
/*      */     
/*  918 */     for (TileEntity tileentity : this.chunkTileEntityMap.values())
/*      */     {
/*  920 */       this.worldObj.markTileEntityForRemoval(tileentity);
/*      */     }
/*      */     
/*  923 */     for (int i = 0; i < this.entityLists.length; i++)
/*      */     {
/*  925 */       this.worldObj.unloadEntities((Collection)this.entityLists[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setChunkModified() {
/*  931 */     this.isModified = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_) {
/*  936 */     int i = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
/*  937 */     int j = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
/*  938 */     i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
/*  939 */     j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
/*      */     
/*  941 */     for (int k = i; k <= j; k++) {
/*      */       
/*  943 */       if (!this.entityLists[k].isEmpty())
/*      */       {
/*  945 */         for (Entity entity : this.entityLists[k]) {
/*      */           
/*  947 */           if (entity.getEntityBoundingBox().intersectsWith(aabb) && entity != entityIn) {
/*      */             
/*  949 */             if (p_177414_4_ == null || p_177414_4_.apply(entity))
/*      */             {
/*  951 */               listToFill.add(entity);
/*      */             }
/*      */             
/*  954 */             Entity[] aentity = entity.getParts();
/*      */             
/*  956 */             if (aentity != null)
/*      */             {
/*  958 */               for (int l = 0; l < aentity.length; l++) {
/*      */                 
/*  960 */                 entity = aentity[l];
/*      */                 
/*  962 */                 if (entity != entityIn && entity.getEntityBoundingBox().intersectsWith(aabb) && (p_177414_4_ == null || p_177414_4_.apply(entity)))
/*      */                 {
/*  964 */                   listToFill.add(entity);
/*      */                 }
/*      */               } 
/*      */             }
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> p_177430_4_) {
/*  976 */     int i = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
/*  977 */     int j = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
/*  978 */     i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
/*  979 */     j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
/*      */     
/*  981 */     for (int k = i; k <= j; k++) {
/*      */       
/*  983 */       for (Entity entity : this.entityLists[k].getByClass(entityClass)) {
/*      */         
/*  985 */         if (entity.getEntityBoundingBox().intersectsWith(aabb) && (p_177430_4_ == null || p_177430_4_.apply(entity)))
/*      */         {
/*  987 */           listToFill.add((T)entity);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean needsSaving(boolean p_76601_1_) {
/*  995 */     if (p_76601_1_) {
/*      */       
/*  997 */       if ((this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime) || this.isModified)
/*      */       {
/*  999 */         return true;
/*      */       }
/*      */     }
/* 1002 */     else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
/*      */       
/* 1004 */       return true;
/*      */     } 
/*      */     
/* 1007 */     return this.isModified;
/*      */   }
/*      */ 
/*      */   
/*      */   public Random getRandomWithSeed(long seed) {
/* 1012 */     return new Random(this.worldObj.getSeed() + (this.xPosition * this.xPosition * 4987142) + (this.xPosition * 5947611) + (this.zPosition * this.zPosition) * 4392871L + (this.zPosition * 389711) ^ seed);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 1017 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void populateChunk(IChunkProvider p_76624_1_, IChunkProvider p_76624_2_, int x, int z) {
/* 1022 */     boolean flag = p_76624_1_.chunkExists(x, z - 1);
/* 1023 */     boolean flag1 = p_76624_1_.chunkExists(x + 1, z);
/* 1024 */     boolean flag2 = p_76624_1_.chunkExists(x, z + 1);
/* 1025 */     boolean flag3 = p_76624_1_.chunkExists(x - 1, z);
/* 1026 */     boolean flag4 = p_76624_1_.chunkExists(x - 1, z - 1);
/* 1027 */     boolean flag5 = p_76624_1_.chunkExists(x + 1, z + 1);
/* 1028 */     boolean flag6 = p_76624_1_.chunkExists(x - 1, z + 1);
/* 1029 */     boolean flag7 = p_76624_1_.chunkExists(x + 1, z - 1);
/*      */     
/* 1031 */     if (flag1 && flag2 && flag5)
/*      */     {
/* 1033 */       if (!this.isTerrainPopulated) {
/*      */         
/* 1035 */         p_76624_1_.populate(p_76624_2_, x, z);
/*      */       }
/*      */       else {
/*      */         
/* 1039 */         p_76624_1_.populateChunk(p_76624_2_, this, x, z);
/*      */       } 
/*      */     }
/*      */     
/* 1043 */     if (flag3 && flag2 && flag6) {
/*      */       
/* 1045 */       Chunk chunk = p_76624_1_.provideChunk(x - 1, z);
/*      */       
/* 1047 */       if (!chunk.isTerrainPopulated) {
/*      */         
/* 1049 */         p_76624_1_.populate(p_76624_2_, x - 1, z);
/*      */       }
/*      */       else {
/*      */         
/* 1053 */         p_76624_1_.populateChunk(p_76624_2_, chunk, x - 1, z);
/*      */       } 
/*      */     } 
/*      */     
/* 1057 */     if (flag && flag1 && flag7) {
/*      */       
/* 1059 */       Chunk chunk1 = p_76624_1_.provideChunk(x, z - 1);
/*      */       
/* 1061 */       if (!chunk1.isTerrainPopulated) {
/*      */         
/* 1063 */         p_76624_1_.populate(p_76624_2_, x, z - 1);
/*      */       }
/*      */       else {
/*      */         
/* 1067 */         p_76624_1_.populateChunk(p_76624_2_, chunk1, x, z - 1);
/*      */       } 
/*      */     } 
/*      */     
/* 1071 */     if (flag4 && flag && flag3) {
/*      */       
/* 1073 */       Chunk chunk2 = p_76624_1_.provideChunk(x - 1, z - 1);
/*      */       
/* 1075 */       if (!chunk2.isTerrainPopulated) {
/*      */         
/* 1077 */         p_76624_1_.populate(p_76624_2_, x - 1, z - 1);
/*      */       }
/*      */       else {
/*      */         
/* 1081 */         p_76624_1_.populateChunk(p_76624_2_, chunk2, x - 1, z - 1);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getPrecipitationHeight(BlockPos pos) {
/* 1088 */     int i = pos.getX() & 0xF;
/* 1089 */     int j = pos.getZ() & 0xF;
/* 1090 */     int k = i | j << 4;
/* 1091 */     BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
/*      */     
/* 1093 */     if (blockpos.getY() == -999) {
/*      */       
/* 1095 */       int l = getTopFilledSegment() + 15;
/* 1096 */       blockpos = new BlockPos(pos.getX(), l, pos.getZ());
/* 1097 */       int i1 = -1;
/*      */       
/* 1099 */       while (blockpos.getY() > 0 && i1 == -1) {
/*      */         
/* 1101 */         Block block = getBlock(blockpos);
/* 1102 */         Material material = block.getMaterial();
/*      */         
/* 1104 */         if (!material.blocksMovement() && !material.isLiquid()) {
/*      */           
/* 1106 */           blockpos = blockpos.down();
/*      */           
/*      */           continue;
/*      */         } 
/* 1110 */         i1 = blockpos.getY() + 1;
/*      */       } 
/*      */ 
/*      */       
/* 1114 */       this.precipitationHeightMap[k] = i1;
/*      */     } 
/*      */     
/* 1117 */     return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
/*      */   }
/*      */ 
/*      */   
/*      */   public void func_150804_b(boolean p_150804_1_) {
/* 1122 */     if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !p_150804_1_)
/*      */     {
/* 1124 */       recheckGaps(this.worldObj.isRemote);
/*      */     }
/*      */     
/* 1127 */     this.field_150815_m = true;
/*      */     
/* 1129 */     if (!this.isLightPopulated && this.isTerrainPopulated)
/*      */     {
/* 1131 */       func_150809_p();
/*      */     }
/*      */     
/* 1134 */     while (!this.tileEntityPosQueue.isEmpty()) {
/*      */       
/* 1136 */       BlockPos blockpos = this.tileEntityPosQueue.poll();
/*      */       
/* 1138 */       if (getTileEntity(blockpos, EnumCreateEntityType.CHECK) == null && getBlock(blockpos).hasTileEntity()) {
/*      */         
/* 1140 */         TileEntity tileentity = createNewTileEntity(blockpos);
/* 1141 */         this.worldObj.setTileEntity(blockpos, tileentity);
/* 1142 */         this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPopulated() {
/* 1149 */     return (this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated);
/*      */   }
/*      */ 
/*      */   
/*      */   public ChunkCoordIntPair getChunkCoordIntPair() {
/* 1154 */     return new ChunkCoordIntPair(this.xPosition, this.zPosition);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAreLevelsEmpty(int startY, int endY) {
/* 1159 */     if (startY < 0)
/*      */     {
/* 1161 */       startY = 0;
/*      */     }
/*      */     
/* 1164 */     if (endY >= 256)
/*      */     {
/* 1166 */       endY = 255;
/*      */     }
/*      */     
/* 1169 */     for (int i = startY; i <= endY; i += 16) {
/*      */       
/* 1171 */       ExtendedBlockStorage extendedblockstorage = this.storageArrays[i >> 4];
/*      */       
/* 1173 */       if (extendedblockstorage != null && !extendedblockstorage.isEmpty())
/*      */       {
/* 1175 */         return false;
/*      */       }
/*      */     } 
/*      */     
/* 1179 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays) {
/* 1184 */     if (this.storageArrays.length != newStorageArrays.length) {
/*      */       
/* 1186 */       logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
/*      */     }
/*      */     else {
/*      */       
/* 1190 */       for (int i = 0; i < this.storageArrays.length; i++)
/*      */       {
/* 1192 */         this.storageArrays[i] = newStorageArrays[i];
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_) {
/* 1199 */     int i = 0;
/* 1200 */     boolean flag = !this.worldObj.provider.getHasNoSky();
/*      */     
/* 1202 */     for (int j = 0; j < this.storageArrays.length; j++) {
/*      */       
/* 1204 */       if ((p_177439_2_ & 1 << j) != 0) {
/*      */         
/* 1206 */         if (this.storageArrays[j] == null)
/*      */         {
/* 1208 */           this.storageArrays[j] = new ExtendedBlockStorage(j << 4, flag);
/*      */         }
/*      */         
/* 1211 */         char[] achar = this.storageArrays[j].getData();
/*      */         
/* 1213 */         for (int k = 0; k < achar.length; k++)
/*      */         {
/* 1215 */           achar[k] = (char)((p_177439_1_[i + 1] & 0xFF) << 8 | p_177439_1_[i] & 0xFF);
/* 1216 */           i += 2;
/*      */         }
/*      */       
/* 1219 */       } else if (p_177439_3_ && this.storageArrays[j] != null) {
/*      */         
/* 1221 */         this.storageArrays[j] = null;
/*      */       } 
/*      */     } 
/*      */     int l;
/* 1225 */     for (l = 0; l < this.storageArrays.length; l++) {
/*      */       
/* 1227 */       if ((p_177439_2_ & 1 << l) != 0 && this.storageArrays[l] != null) {
/*      */         
/* 1229 */         NibbleArray nibblearray = this.storageArrays[l].getBlocklightArray();
/* 1230 */         System.arraycopy(p_177439_1_, i, nibblearray.getData(), 0, (nibblearray.getData()).length);
/* 1231 */         i += (nibblearray.getData()).length;
/*      */       } 
/*      */     } 
/*      */     
/* 1235 */     if (flag)
/*      */     {
/* 1237 */       for (int i1 = 0; i1 < this.storageArrays.length; i1++) {
/*      */         
/* 1239 */         if ((p_177439_2_ & 1 << i1) != 0 && this.storageArrays[i1] != null) {
/*      */           
/* 1241 */           NibbleArray nibblearray1 = this.storageArrays[i1].getSkylightArray();
/* 1242 */           System.arraycopy(p_177439_1_, i, nibblearray1.getData(), 0, (nibblearray1.getData()).length);
/* 1243 */           i += (nibblearray1.getData()).length;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 1248 */     if (p_177439_3_) {
/*      */       
/* 1250 */       System.arraycopy(p_177439_1_, i, this.blockBiomeArray, 0, this.blockBiomeArray.length);
/* 1251 */       l = i + this.blockBiomeArray.length;
/*      */     } 
/*      */     
/* 1254 */     for (int j1 = 0; j1 < this.storageArrays.length; j1++) {
/*      */       
/* 1256 */       if (this.storageArrays[j1] != null && (p_177439_2_ & 1 << j1) != 0)
/*      */       {
/* 1258 */         this.storageArrays[j1].removeInvalidBlocks();
/*      */       }
/*      */     } 
/*      */     
/* 1262 */     this.isLightPopulated = true;
/* 1263 */     this.isTerrainPopulated = true;
/* 1264 */     generateHeightMap();
/*      */     
/* 1266 */     for (TileEntity tileentity : this.chunkTileEntityMap.values())
/*      */     {
/* 1268 */       tileentity.updateContainingBlockInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager) {
/* 1274 */     int i = pos.getX() & 0xF;
/* 1275 */     int j = pos.getZ() & 0xF;
/* 1276 */     int k = this.blockBiomeArray[j << 4 | i] & 0xFF;
/*      */     
/* 1278 */     if (k == 255) {
/*      */       
/* 1280 */       BiomeGenBase biomegenbase = chunkManager.getBiomeGenerator(pos, BiomeGenBase.plains);
/* 1281 */       k = biomegenbase.biomeID;
/* 1282 */       this.blockBiomeArray[j << 4 | i] = (byte)(k & 0xFF);
/*      */     } 
/*      */     
/* 1285 */     BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(k);
/* 1286 */     return (biomegenbase1 == null) ? BiomeGenBase.plains : biomegenbase1;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getBiomeArray() {
/* 1291 */     return this.blockBiomeArray;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBiomeArray(byte[] biomeArray) {
/* 1296 */     if (this.blockBiomeArray.length != biomeArray.length) {
/*      */       
/* 1298 */       logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
/*      */     }
/*      */     else {
/*      */       
/* 1302 */       for (int i = 0; i < this.blockBiomeArray.length; i++)
/*      */       {
/* 1304 */         this.blockBiomeArray[i] = biomeArray[i];
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetRelightChecks() {
/* 1311 */     this.queuedLightChecks = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enqueueRelightChecks() {
/* 1316 */     BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
/*      */     
/* 1318 */     for (int i = 0; i < 8; i++) {
/*      */       
/* 1320 */       if (this.queuedLightChecks >= 4096) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1325 */       int j = this.queuedLightChecks % 16;
/* 1326 */       int k = this.queuedLightChecks / 16 % 16;
/* 1327 */       int l = this.queuedLightChecks / 256;
/* 1328 */       this.queuedLightChecks++;
/*      */       
/* 1330 */       for (int i1 = 0; i1 < 16; i1++) {
/*      */         
/* 1332 */         BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
/* 1333 */         boolean flag = (i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15);
/*      */         
/* 1335 */         if ((this.storageArrays[j] == null && flag) || (this.storageArrays[j] != null && this.storageArrays[j].getBlockByExtId(k, i1, l).getMaterial() == Material.air)) {
/*      */           
/* 1337 */           for (EnumFacing enumfacing : EnumFacing.values()) {
/*      */             
/* 1339 */             BlockPos blockpos2 = blockpos1.offset(enumfacing);
/*      */             
/* 1341 */             if (this.worldObj.getBlockState(blockpos2).getBlock().getLightValue() > 0)
/*      */             {
/* 1343 */               this.worldObj.checkLight(blockpos2);
/*      */             }
/*      */           } 
/*      */           
/* 1347 */           this.worldObj.checkLight(blockpos1);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void func_150809_p() {
/* 1355 */     this.isTerrainPopulated = true;
/* 1356 */     this.isLightPopulated = true;
/* 1357 */     BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
/*      */     
/* 1359 */     if (!this.worldObj.provider.getHasNoSky())
/*      */     {
/* 1361 */       if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.getSeaLevel(), 16))) {
/*      */         int i;
/*      */ 
/*      */         
/* 1365 */         label31: for (i = 0; i < 16; i++) {
/*      */           
/* 1367 */           for (int j = 0; j < 16; j++) {
/*      */             
/* 1369 */             if (!func_150811_f(i, j)) {
/*      */               
/* 1371 */               this.isLightPopulated = false;
/*      */               
/*      */               break label31;
/*      */             } 
/*      */           } 
/*      */         } 
/* 1377 */         if (this.isLightPopulated)
/*      */         {
/* 1379 */           for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
/*      */             
/* 1381 */             int k = (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? 16 : 1;
/* 1382 */             this.worldObj.getChunkFromBlockCoords(blockpos.offset(enumfacing, k)).func_180700_a(enumfacing.getOpposite());
/*      */           } 
/*      */           
/* 1385 */           func_177441_y();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1390 */         this.isLightPopulated = false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void func_177441_y() {
/* 1397 */     for (int i = 0; i < this.updateSkylightColumns.length; i++)
/*      */     {
/* 1399 */       this.updateSkylightColumns[i] = true;
/*      */     }
/*      */     
/* 1402 */     recheckGaps(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private void func_180700_a(EnumFacing facing) {
/* 1407 */     if (this.isTerrainPopulated)
/*      */     {
/* 1409 */       if (facing == EnumFacing.EAST) {
/*      */         
/* 1411 */         for (int i = 0; i < 16; i++)
/*      */         {
/* 1413 */           func_150811_f(15, i);
/*      */         }
/*      */       }
/* 1416 */       else if (facing == EnumFacing.WEST) {
/*      */         
/* 1418 */         for (int j = 0; j < 16; j++)
/*      */         {
/* 1420 */           func_150811_f(0, j);
/*      */         }
/*      */       }
/* 1423 */       else if (facing == EnumFacing.SOUTH) {
/*      */         
/* 1425 */         for (int k = 0; k < 16; k++)
/*      */         {
/* 1427 */           func_150811_f(k, 15);
/*      */         }
/*      */       }
/* 1430 */       else if (facing == EnumFacing.NORTH) {
/*      */         
/* 1432 */         for (int l = 0; l < 16; l++)
/*      */         {
/* 1434 */           func_150811_f(l, 0);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean func_150811_f(int x, int z) {
/* 1442 */     int i = getTopFilledSegment();
/* 1443 */     boolean flag = false;
/* 1444 */     boolean flag1 = false;
/* 1445 */     BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.xPosition << 4) + x, 0, (this.zPosition << 4) + z);
/*      */     
/* 1447 */     for (int j = i + 16 - 1; j > this.worldObj.getSeaLevel() || (j > 0 && !flag1); j--) {
/*      */       
/* 1449 */       blockpos$mutableblockpos.set(blockpos$mutableblockpos.getX(), j, blockpos$mutableblockpos.getZ());
/* 1450 */       int k = getBlockLightOpacity((BlockPos)blockpos$mutableblockpos);
/*      */       
/* 1452 */       if (k == 255 && blockpos$mutableblockpos.getY() < this.worldObj.getSeaLevel())
/*      */       {
/* 1454 */         flag1 = true;
/*      */       }
/*      */       
/* 1457 */       if (!flag && k > 0) {
/*      */         
/* 1459 */         flag = true;
/*      */       }
/* 1461 */       else if (flag && k == 0 && !this.worldObj.checkLight((BlockPos)blockpos$mutableblockpos)) {
/*      */         
/* 1463 */         return false;
/*      */       } 
/*      */     } 
/*      */     
/* 1467 */     for (int l = blockpos$mutableblockpos.getY(); l > 0; l--) {
/*      */       
/* 1469 */       blockpos$mutableblockpos.set(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
/*      */       
/* 1471 */       if (getBlock((BlockPos)blockpos$mutableblockpos).getLightValue() > 0)
/*      */       {
/* 1473 */         this.worldObj.checkLight((BlockPos)blockpos$mutableblockpos);
/*      */       }
/*      */     } 
/*      */     
/* 1477 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLoaded() {
/* 1482 */     return this.isChunkLoaded;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setChunkLoaded(boolean loaded) {
/* 1487 */     this.isChunkLoaded = loaded;
/*      */   }
/*      */ 
/*      */   
/*      */   public World getWorld() {
/* 1492 */     return this.worldObj;
/*      */   }
/*      */ 
/*      */   
/*      */   public int[] getHeightMap() {
/* 1497 */     return this.heightMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHeightMap(int[] newHeightMap) {
/* 1502 */     if (this.heightMap.length != newHeightMap.length) {
/*      */       
/* 1504 */       logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
/*      */     }
/*      */     else {
/*      */       
/* 1508 */       for (int i = 0; i < this.heightMap.length; i++)
/*      */       {
/* 1510 */         this.heightMap[i] = newHeightMap[i];
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<BlockPos, TileEntity> getTileEntityMap() {
/* 1517 */     return this.chunkTileEntityMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
/* 1522 */     return this.entityLists;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTerrainPopulated() {
/* 1527 */     return this.isTerrainPopulated;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTerrainPopulated(boolean terrainPopulated) {
/* 1532 */     this.isTerrainPopulated = terrainPopulated;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLightPopulated() {
/* 1537 */     return this.isLightPopulated;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLightPopulated(boolean lightPopulated) {
/* 1542 */     this.isLightPopulated = lightPopulated;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setModified(boolean modified) {
/* 1547 */     this.isModified = modified;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHasEntities(boolean hasEntitiesIn) {
/* 1552 */     this.hasEntities = hasEntitiesIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLastSaveTime(long saveTime) {
/* 1557 */     this.lastSaveTime = saveTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLowestHeight() {
/* 1562 */     return this.heightMapMinimum;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getInhabitedTime() {
/* 1567 */     return this.inhabitedTime;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInhabitedTime(long newInhabitedTime) {
/* 1572 */     this.inhabitedTime = newInhabitedTime;
/*      */   }
/*      */   
/*      */   public enum EnumCreateEntityType
/*      */   {
/* 1577 */     IMMEDIATE,
/* 1578 */     QUEUED,
/* 1579 */     CHECK;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\Chunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */