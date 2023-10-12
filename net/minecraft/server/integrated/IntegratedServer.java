/*     */ package net.minecraft.server.integrated;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.Futures;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import net.minecraft.client.ClientBrandRetriever;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ThreadLanServerPing;
/*     */ import net.minecraft.command.ServerCommandManager;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketThreadUtil;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.profiler.PlayerUsageSnooper;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.CryptManager;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.IWorldAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldManager;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.WorldServerMulti;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.demo.DemoWorldServer;
/*     */ import net.minecraft.world.storage.ISaveHandler;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ import net.optifine.ClearWater;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class IntegratedServer extends MinecraftServer {
/*  45 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final Minecraft mc;
/*     */   private final WorldSettings theWorldSettings;
/*     */   private boolean isGamePaused;
/*     */   private boolean isPublic;
/*     */   private ThreadLanServerPing lanServerPing;
/*  51 */   private long ticksSaveLast = 0L;
/*  52 */   public World difficultyUpdateWorld = null;
/*  53 */   public BlockPos difficultyUpdatePos = null;
/*  54 */   public DifficultyInstance difficultyLast = null;
/*     */ 
/*     */   
/*     */   public IntegratedServer(Minecraft mcIn) {
/*  58 */     super(mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
/*  59 */     this.mc = mcIn;
/*  60 */     this.theWorldSettings = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegratedServer(Minecraft mcIn, String folderName, String worldName, WorldSettings settings) {
/*  65 */     super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
/*  66 */     setServerOwner(mcIn.getSession().getUsername());
/*  67 */     setFolderName(folderName);
/*  68 */     setWorldName(worldName);
/*  69 */     setDemo(mcIn.isDemo());
/*  70 */     canCreateBonusChest(settings.isBonusChestEnabled());
/*  71 */     setBuildLimit(256);
/*  72 */     setConfigManager(new IntegratedPlayerList(this));
/*  73 */     this.mc = mcIn;
/*  74 */     this.theWorldSettings = isDemo() ? DemoWorldServer.demoWorldSettings : settings;
/*  75 */     ISaveHandler isavehandler = getActiveAnvilConverter().getSaveLoader(folderName, false);
/*  76 */     WorldInfo worldinfo = isavehandler.loadWorldInfo();
/*     */     
/*  78 */     if (worldinfo != null) {
/*     */       
/*  80 */       NBTTagCompound nbttagcompound = worldinfo.getPlayerNBTTagCompound();
/*     */       
/*  82 */       if (nbttagcompound != null && nbttagcompound.hasKey("Dimension")) {
/*     */         
/*  84 */         int i = nbttagcompound.getInteger("Dimension");
/*  85 */         PacketThreadUtil.lastDimensionId = i;
/*  86 */         this.mc.loadingScreen.setLoadingProgress(-1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ServerCommandManager createNewCommandManager() {
/*  93 */     return new IntegratedServerCommandManager();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String worldNameIn2) {
/*  98 */     convertMapIfNeeded(saveName);
/*  99 */     boolean flag = Reflector.DimensionManager.exists();
/*     */     
/* 101 */     if (!flag) {
/*     */       
/* 103 */       this.worldServers = new WorldServer[3];
/* 104 */       this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
/*     */     } 
/*     */     
/* 107 */     ISaveHandler isavehandler = getActiveAnvilConverter().getSaveLoader(saveName, true);
/* 108 */     setResourcePackFromWorld(getFolderName(), isavehandler);
/* 109 */     WorldInfo worldinfo = isavehandler.loadWorldInfo();
/*     */     
/* 111 */     if (worldinfo == null) {
/*     */       
/* 113 */       worldinfo = new WorldInfo(this.theWorldSettings, worldNameIn);
/*     */     }
/*     */     else {
/*     */       
/* 117 */       worldinfo.setWorldName(worldNameIn);
/*     */     } 
/*     */     
/* 120 */     if (flag) {
/*     */       
/* 122 */       WorldServer worldserver = isDemo() ? (WorldServer)(new DemoWorldServer(this, isavehandler, worldinfo, 0, this.theProfiler)).init() : (WorldServer)(new WorldServer(this, isavehandler, worldinfo, 0, this.theProfiler)).init();
/* 123 */       worldserver.initialize(this.theWorldSettings);
/* 124 */       Integer[] ainteger = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
/* 125 */       Integer[] ainteger1 = ainteger;
/* 126 */       int i = ainteger.length;
/*     */       
/* 128 */       for (int j = 0; j < i; j++) {
/*     */         
/* 130 */         int k = ainteger1[j].intValue();
/* 131 */         WorldServer worldserver1 = (k == 0) ? worldserver : (WorldServer)(new WorldServerMulti(this, isavehandler, k, worldserver, this.theProfiler)).init();
/* 132 */         worldserver1.addWorldAccess((IWorldAccess)new WorldManager(this, worldserver1));
/*     */         
/* 134 */         if (!isSinglePlayer())
/*     */         {
/* 136 */           worldserver1.getWorldInfo().setGameType(getGameType());
/*     */         }
/*     */         
/* 139 */         if (Reflector.EventBus.exists())
/*     */         {
/* 141 */           Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[] { worldserver1 });
/*     */         }
/*     */       } 
/*     */       
/* 145 */       getConfigurationManager().setPlayerManager(new WorldServer[] { worldserver });
/*     */       
/* 147 */       if (worldserver.getWorldInfo().getDifficulty() == null)
/*     */       {
/* 149 */         setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 154 */       for (int l = 0; l < this.worldServers.length; l++) {
/*     */         
/* 156 */         int i1 = 0;
/*     */         
/* 158 */         if (l == 1)
/*     */         {
/* 160 */           i1 = -1;
/*     */         }
/*     */         
/* 163 */         if (l == 2)
/*     */         {
/* 165 */           i1 = 1;
/*     */         }
/*     */         
/* 168 */         if (l == 0) {
/*     */           
/* 170 */           if (isDemo()) {
/*     */             
/* 172 */             this.worldServers[l] = (WorldServer)(new DemoWorldServer(this, isavehandler, worldinfo, i1, this.theProfiler)).init();
/*     */           }
/*     */           else {
/*     */             
/* 176 */             this.worldServers[l] = (WorldServer)(new WorldServer(this, isavehandler, worldinfo, i1, this.theProfiler)).init();
/*     */           } 
/*     */           
/* 179 */           this.worldServers[l].initialize(this.theWorldSettings);
/*     */         }
/*     */         else {
/*     */           
/* 183 */           this.worldServers[l] = (WorldServer)(new WorldServerMulti(this, isavehandler, i1, this.worldServers[0], this.theProfiler)).init();
/*     */         } 
/*     */         
/* 186 */         this.worldServers[l].addWorldAccess((IWorldAccess)new WorldManager(this, this.worldServers[l]));
/*     */       } 
/*     */       
/* 189 */       getConfigurationManager().setPlayerManager(this.worldServers);
/*     */       
/* 191 */       if (this.worldServers[0].getWorldInfo().getDifficulty() == null)
/*     */       {
/* 193 */         setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
/*     */       }
/*     */     } 
/*     */     
/* 197 */     initialWorldChunkLoad();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean startServer() throws IOException {
/* 202 */     logger.info("Starting integrated minecraft server version 1.9");
/* 203 */     setOnlineMode(true);
/* 204 */     setCanSpawnAnimals(true);
/* 205 */     setCanSpawnNPCs(true);
/* 206 */     setAllowPvp(true);
/* 207 */     setAllowFlight(true);
/* 208 */     logger.info("Generating keypair");
/* 209 */     setKeyPair(CryptManager.generateKeyPair());
/*     */     
/* 211 */     if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
/*     */       
/* 213 */       Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
/*     */       
/* 215 */       if (!Reflector.callBoolean(object, Reflector.FMLCommonHandler_handleServerAboutToStart, new Object[] { this }))
/*     */       {
/* 217 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 221 */     loadAllWorlds(getFolderName(), getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
/* 222 */     setMOTD(getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
/*     */     
/* 224 */     if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
/*     */       
/* 226 */       Object object1 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
/*     */       
/* 228 */       if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == boolean.class)
/*     */       {
/* 230 */         return Reflector.callBoolean(object1, Reflector.FMLCommonHandler_handleServerStarting, new Object[] { this });
/*     */       }
/*     */       
/* 233 */       Reflector.callVoid(object1, Reflector.FMLCommonHandler_handleServerStarting, new Object[] { this });
/*     */     } 
/*     */     
/* 236 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 241 */     onTick();
/* 242 */     boolean flag = this.isGamePaused;
/* 243 */     this.isGamePaused = (Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused());
/*     */     
/* 245 */     if (!flag && this.isGamePaused) {
/*     */       
/* 247 */       logger.info("Saving and pausing game...");
/* 248 */       getConfigurationManager().saveAllPlayerData();
/* 249 */       saveAllWorlds(false);
/*     */     } 
/*     */     
/* 252 */     if (this.isGamePaused) {
/*     */       
/* 254 */       synchronized (this.futureTaskQueue)
/*     */       {
/* 256 */         while (!this.futureTaskQueue.isEmpty())
/*     */         {
/* 258 */           Util.runTask(this.futureTaskQueue.poll(), logger);
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 264 */       super.tick();
/*     */       
/* 266 */       if (this.mc.gameSettings.renderDistanceChunks != getConfigurationManager().getViewDistance()) {
/*     */         
/* 268 */         logger.info("Changing view distance to {}, from {}", new Object[] { Integer.valueOf(this.mc.gameSettings.renderDistanceChunks), Integer.valueOf(getConfigurationManager().getViewDistance()) });
/* 269 */         getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
/*     */       } 
/*     */       
/* 272 */       if (this.mc.theWorld != null) {
/*     */         
/* 274 */         WorldInfo worldinfo1 = this.worldServers[0].getWorldInfo();
/* 275 */         WorldInfo worldinfo = this.mc.theWorld.getWorldInfo();
/*     */         
/* 277 */         if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty()) {
/*     */           
/* 279 */           logger.info("Changing difficulty to {}, from {}", new Object[] { worldinfo.getDifficulty(), worldinfo1.getDifficulty() });
/* 280 */           setDifficultyForAllWorlds(worldinfo.getDifficulty());
/*     */         }
/* 282 */         else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
/*     */           
/* 284 */           logger.info("Locking difficulty to {}", new Object[] { worldinfo.getDifficulty() });
/*     */           
/* 286 */           for (WorldServer worldserver : this.worldServers) {
/*     */             
/* 288 */             if (worldserver != null)
/*     */             {
/* 290 */               worldserver.getWorldInfo().setDifficultyLocked(true);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canStructuresSpawn() {
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSettings.GameType getGameType() {
/* 305 */     return this.theWorldSettings.getGameType();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumDifficulty getDifficulty() {
/* 310 */     return (this.mc.theWorld == null) ? this.mc.gameSettings.difficulty : this.mc.theWorld.getWorldInfo().getDifficulty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHardcore() {
/* 315 */     return this.theWorldSettings.getHardcoreEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldBroadcastRconToOps() {
/* 320 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldBroadcastConsoleToOps() {
/* 325 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveAllWorlds(boolean dontLog) {
/* 330 */     if (dontLog) {
/*     */       
/* 332 */       int i = getTickCounter();
/* 333 */       int j = this.mc.gameSettings.ofAutoSaveTicks;
/*     */       
/* 335 */       if (i < this.ticksSaveLast + j) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 340 */       this.ticksSaveLast = i;
/*     */     } 
/*     */     
/* 343 */     super.saveAllWorlds(dontLog);
/*     */   }
/*     */ 
/*     */   
/*     */   public File getDataDirectory() {
/* 348 */     return this.mc.mcDataDir;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 353 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldUseNativeTransport() {
/* 358 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalTick(CrashReport report) {
/* 363 */     this.mc.crashed(report);
/*     */   }
/*     */ 
/*     */   
/*     */   public CrashReport addServerInfoToCrashReport(CrashReport report) {
/* 368 */     report = super.addServerInfoToCrashReport(report);
/* 369 */     report.getCategory().addCrashSectionCallable("Type", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 373 */             return "Integrated Server (map_client.txt)";
/*     */           }
/*     */         });
/* 376 */     report.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>()
/*     */         {
/*     */           public String call() throws Exception
/*     */           {
/* 380 */             String s = ClientBrandRetriever.getClientModName();
/*     */             
/* 382 */             if (!s.equals("vanilla"))
/*     */             {
/* 384 */               return "Definitely; Client brand changed to '" + s + "'";
/*     */             }
/*     */ 
/*     */             
/* 388 */             s = IntegratedServer.this.getServerModName();
/* 389 */             return !s.equals("vanilla") ? ("Definitely; Server brand changed to '" + s + "'") : ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
/*     */           }
/*     */         });
/*     */     
/* 393 */     return report;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
/* 398 */     super.setDifficultyForAllWorlds(difficulty);
/*     */     
/* 400 */     if (this.mc.theWorld != null)
/*     */     {
/* 402 */       this.mc.theWorld.getWorldInfo().setDifficulty(difficulty);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
/* 408 */     super.addServerStatsToSnooper(playerSnooper);
/* 409 */     playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSnooperEnabled() {
/* 414 */     return Minecraft.getMinecraft().isSnooperEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String shareToLAN(WorldSettings.GameType type, boolean allowCheats) {
/*     */     try {
/* 421 */       int i = -1;
/*     */ 
/*     */       
/*     */       try {
/* 425 */         i = HttpUtil.getSuitableLanPort();
/*     */       }
/* 427 */       catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 432 */       if (i <= 0)
/*     */       {
/* 434 */         i = 25564;
/*     */       }
/*     */       
/* 437 */       getNetworkSystem().addLanEndpoint((InetAddress)null, i);
/* 438 */       logger.info("Started on " + i);
/* 439 */       this.isPublic = true;
/* 440 */       this.lanServerPing = new ThreadLanServerPing(getMOTD(), i + "");
/* 441 */       this.lanServerPing.start();
/* 442 */       getConfigurationManager().setGameType(type);
/* 443 */       getConfigurationManager().setCommandsAllowedForAll(allowCheats);
/* 444 */       return i + "";
/*     */     }
/* 446 */     catch (IOException var6) {
/*     */       
/* 448 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopServer() {
/* 454 */     super.stopServer();
/*     */     
/* 456 */     if (this.lanServerPing != null) {
/*     */       
/* 458 */       this.lanServerPing.interrupt();
/* 459 */       this.lanServerPing = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void initiateShutdown() {
/* 465 */     if (!Reflector.MinecraftForge.exists() || isServerRunning())
/*     */     {
/* 467 */       Futures.getUnchecked((Future)addScheduledTask(new Runnable()
/*     */             {
/*     */               public void run()
/*     */               {
/* 471 */                 for (EntityPlayerMP entityplayermp : Lists.newArrayList(IntegratedServer.this.getConfigurationManager().getPlayerList()))
/*     */                 {
/* 473 */                   IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityplayermp);
/*     */                 }
/*     */               }
/*     */             }));
/*     */     }
/*     */     
/* 479 */     super.initiateShutdown();
/*     */     
/* 481 */     if (this.lanServerPing != null) {
/*     */       
/* 483 */       this.lanServerPing.interrupt();
/* 484 */       this.lanServerPing = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStaticInstance() {
/* 490 */     setInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getPublic() {
/* 495 */     return this.isPublic;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setGameType(WorldSettings.GameType gameMode) {
/* 500 */     getConfigurationManager().setGameType(gameMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCommandBlockEnabled() {
/* 505 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOpPermissionLevel() {
/* 510 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   private void onTick() {
/* 515 */     for (WorldServer worldserver : Arrays.<WorldServer>asList(this.worldServers))
/*     */     {
/* 517 */       onTick(worldserver);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public DifficultyInstance getDifficultyAsync(World p_getDifficultyAsync_1_, BlockPos p_getDifficultyAsync_2_) {
/* 523 */     this.difficultyUpdateWorld = p_getDifficultyAsync_1_;
/* 524 */     this.difficultyUpdatePos = p_getDifficultyAsync_2_;
/* 525 */     return this.difficultyLast;
/*     */   }
/*     */ 
/*     */   
/*     */   private void onTick(WorldServer p_onTick_1_) {
/* 530 */     if (!Config.isTimeDefault())
/*     */     {
/* 532 */       fixWorldTime(p_onTick_1_);
/*     */     }
/*     */     
/* 535 */     if (!Config.isWeatherEnabled())
/*     */     {
/* 537 */       fixWorldWeather(p_onTick_1_);
/*     */     }
/*     */     
/* 540 */     if (Config.waterOpacityChanged) {
/*     */       
/* 542 */       Config.waterOpacityChanged = false;
/* 543 */       ClearWater.updateWaterOpacity(Config.getGameSettings(), (World)p_onTick_1_);
/*     */     } 
/*     */     
/* 546 */     if (this.difficultyUpdateWorld == p_onTick_1_ && this.difficultyUpdatePos != null) {
/*     */       
/* 548 */       this.difficultyLast = p_onTick_1_.getDifficultyForLocation(this.difficultyUpdatePos);
/* 549 */       this.difficultyUpdateWorld = null;
/* 550 */       this.difficultyUpdatePos = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixWorldWeather(WorldServer p_fixWorldWeather_1_) {
/* 556 */     WorldInfo worldinfo = p_fixWorldWeather_1_.getWorldInfo();
/*     */     
/* 558 */     if (worldinfo.isRaining() || worldinfo.isThundering()) {
/*     */       
/* 560 */       worldinfo.setRainTime(0);
/* 561 */       worldinfo.setRaining(false);
/* 562 */       p_fixWorldWeather_1_.setRainStrength(0.0F);
/* 563 */       worldinfo.setThunderTime(0);
/* 564 */       worldinfo.setThundering(false);
/* 565 */       p_fixWorldWeather_1_.setThunderStrength(0.0F);
/* 566 */       getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(2, 0.0F));
/* 567 */       getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(7, 0.0F));
/* 568 */       getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(8, 0.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixWorldTime(WorldServer p_fixWorldTime_1_) {
/* 574 */     WorldInfo worldinfo = p_fixWorldTime_1_.getWorldInfo();
/*     */     
/* 576 */     if (worldinfo.getGameType().getID() == 1) {
/*     */       
/* 578 */       long i = p_fixWorldTime_1_.getWorldTime();
/* 579 */       long j = i % 24000L;
/*     */       
/* 581 */       if (Config.isTimeDayOnly()) {
/*     */         
/* 583 */         if (j <= 1000L)
/*     */         {
/* 585 */           p_fixWorldTime_1_.setWorldTime(i - j + 1001L);
/*     */         }
/*     */         
/* 588 */         if (j >= 11000L)
/*     */         {
/* 590 */           p_fixWorldTime_1_.setWorldTime(i - j + 24001L);
/*     */         }
/*     */       } 
/*     */       
/* 594 */       if (Config.isTimeNightOnly()) {
/*     */         
/* 596 */         if (j <= 14000L)
/*     */         {
/* 598 */           p_fixWorldTime_1_.setWorldTime(i - j + 14001L);
/*     */         }
/*     */         
/* 601 */         if (j >= 22000L)
/*     */         {
/* 603 */           p_fixWorldTime_1_.setWorldTime(i - j + 24000L + 14001L);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\integrated\IntegratedServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */