/*      */ package net.minecraft.server;
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.ListenableFutureTask;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.GameProfileRepository;
/*      */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*      */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.handler.codec.base64.Base64;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.net.Proxy;
/*      */ import java.security.KeyPair;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Queue;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.FutureTask;
/*      */ import javax.imageio.ImageIO;
/*      */ import net.minecraft.command.CommandBase;
/*      */ import net.minecraft.command.CommandResultStats;
/*      */ import net.minecraft.command.ICommandManager;
/*      */ import net.minecraft.command.ICommandSender;
/*      */ import net.minecraft.command.ServerCommandManager;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.network.NetworkSystem;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.ServerStatusResponse;
/*      */ import net.minecraft.network.play.server.S03PacketTimeUpdate;
/*      */ import net.minecraft.profiler.IPlayerUsage;
/*      */ import net.minecraft.profiler.PlayerUsageSnooper;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.management.PlayerProfileCache;
/*      */ import net.minecraft.server.management.ServerConfigurationManager;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.IProgressUpdate;
/*      */ import net.minecraft.util.IThreadListener;
/*      */ import net.minecraft.util.ITickable;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.IWorldAccess;
/*      */ import net.minecraft.world.MinecraftException;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldManager;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.WorldServerMulti;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.WorldType;
/*      */ import net.minecraft.world.chunk.storage.AnvilSaveConverter;
/*      */ import net.minecraft.world.demo.DemoWorldServer;
/*      */ import net.minecraft.world.storage.ISaveFormat;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public abstract class MinecraftServer implements Runnable, ICommandSender, IThreadListener, IPlayerUsage {
/*   82 */   private static final Logger logger = LogManager.getLogger();
/*   83 */   public static final File USER_CACHE_FILE = new File("usercache.json");
/*      */   private static MinecraftServer mcServer;
/*      */   private final ISaveFormat anvilConverterForAnvilFile;
/*   86 */   private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
/*      */   private final File anvilFile;
/*   88 */   private final List<ITickable> playersOnline = Lists.newArrayList();
/*      */   protected final ICommandManager commandManager;
/*   90 */   public final Profiler theProfiler = new Profiler();
/*      */   private final NetworkSystem networkSystem;
/*   92 */   private final ServerStatusResponse statusResponse = new ServerStatusResponse();
/*   93 */   private final Random random = new Random();
/*   94 */   private int serverPort = -1;
/*      */   public WorldServer[] worldServers;
/*      */   private ServerConfigurationManager serverConfigManager;
/*      */   private boolean serverRunning = true;
/*      */   private boolean serverStopped;
/*      */   private int tickCounter;
/*      */   protected final Proxy serverProxy;
/*      */   public String currentTask;
/*      */   public int percentDone;
/*      */   private boolean onlineMode;
/*      */   private boolean canSpawnAnimals;
/*      */   private boolean canSpawnNPCs;
/*      */   private boolean pvpEnabled;
/*      */   private boolean allowFlight;
/*      */   private String motd;
/*      */   private int buildLimit;
/*  110 */   private int maxPlayerIdleMinutes = 0;
/*  111 */   public final long[] tickTimeArray = new long[100];
/*      */   public long[][] timeOfLastDimensionTick;
/*      */   private KeyPair serverKeyPair;
/*      */   private String serverOwner;
/*      */   private String folderName;
/*      */   private String worldName;
/*      */   private boolean isDemo;
/*      */   private boolean enableBonusChest;
/*      */   private boolean worldIsBeingDeleted;
/*  120 */   private String resourcePackUrl = "";
/*  121 */   private String resourcePackHash = "";
/*      */   private boolean serverIsRunning;
/*      */   private long timeOfLastWarning;
/*      */   private String userMessage;
/*      */   private boolean startProfiling;
/*      */   private boolean isGamemodeForced;
/*      */   private final YggdrasilAuthenticationService authService;
/*      */   private final MinecraftSessionService sessionService;
/*  129 */   private long nanoTimeSinceStatusRefresh = 0L;
/*      */   private final GameProfileRepository profileRepo;
/*      */   private final PlayerProfileCache profileCache;
/*  132 */   protected final Queue<FutureTask<?>> futureTaskQueue = Queues.newArrayDeque();
/*      */   private Thread serverThread;
/*  134 */   private long currentTime = getCurrentTimeMillis();
/*      */ 
/*      */   
/*      */   public MinecraftServer(Proxy proxy, File workDir) {
/*  138 */     this.serverProxy = proxy;
/*  139 */     mcServer = this;
/*  140 */     this.anvilFile = null;
/*  141 */     this.networkSystem = null;
/*  142 */     this.profileCache = new PlayerProfileCache(this, workDir);
/*  143 */     this.commandManager = null;
/*  144 */     this.anvilConverterForAnvilFile = null;
/*  145 */     this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
/*  146 */     this.sessionService = this.authService.createMinecraftSessionService();
/*  147 */     this.profileRepo = this.authService.createProfileRepository();
/*      */   }
/*      */ 
/*      */   
/*      */   public MinecraftServer(File workDir, Proxy proxy, File profileCacheDir) {
/*  152 */     this.serverProxy = proxy;
/*  153 */     mcServer = this;
/*  154 */     this.anvilFile = workDir;
/*  155 */     this.networkSystem = new NetworkSystem(this);
/*  156 */     this.profileCache = new PlayerProfileCache(this, profileCacheDir);
/*  157 */     this.commandManager = (ICommandManager)createNewCommandManager();
/*  158 */     this.anvilConverterForAnvilFile = (ISaveFormat)new AnvilSaveConverter(workDir);
/*  159 */     this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
/*  160 */     this.sessionService = this.authService.createMinecraftSessionService();
/*  161 */     this.profileRepo = this.authService.createProfileRepository();
/*      */   }
/*      */ 
/*      */   
/*      */   protected ServerCommandManager createNewCommandManager() {
/*  166 */     return new ServerCommandManager();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void convertMapIfNeeded(String worldNameIn) {
/*  173 */     if (getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
/*      */       
/*  175 */       logger.info("Converting map!");
/*  176 */       setUserMessage("menu.convertingLevel");
/*  177 */       getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate()
/*      */           {
/*  179 */             private long startTime = System.currentTimeMillis();
/*      */ 
/*      */             
/*      */             public void displaySavingString(String message) {}
/*      */ 
/*      */             
/*      */             public void resetProgressAndMessage(String message) {}
/*      */             
/*      */             public void setLoadingProgress(int progress) {
/*  188 */               if (System.currentTimeMillis() - this.startTime >= 1000L) {
/*      */                 
/*  190 */                 this.startTime = System.currentTimeMillis();
/*  191 */                 MinecraftServer.logger.info("Converting... " + progress + "%");
/*      */               } 
/*      */             }
/*      */ 
/*      */             
/*      */             public void setDoneWorking() {}
/*      */ 
/*      */             
/*      */             public void displayLoadingString(String message) {}
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected synchronized void setUserMessage(String message) {
/*  206 */     this.userMessage = message;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getUserMessage() {
/*  211 */     return this.userMessage;
/*      */   }
/*      */   
/*      */   protected void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String worldNameIn2) {
/*      */     WorldSettings worldsettings;
/*  216 */     convertMapIfNeeded(saveName);
/*  217 */     setUserMessage("menu.loadingLevel");
/*  218 */     this.worldServers = new WorldServer[3];
/*  219 */     this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
/*  220 */     ISaveHandler isavehandler = this.anvilConverterForAnvilFile.getSaveLoader(saveName, true);
/*  221 */     setResourcePackFromWorld(getFolderName(), isavehandler);
/*  222 */     WorldInfo worldinfo = isavehandler.loadWorldInfo();
/*      */ 
/*      */     
/*  225 */     if (worldinfo == null) {
/*      */       
/*  227 */       if (isDemo()) {
/*      */         
/*  229 */         worldsettings = DemoWorldServer.demoWorldSettings;
/*      */       }
/*      */       else {
/*      */         
/*  233 */         worldsettings = new WorldSettings(seed, getGameType(), canStructuresSpawn(), isHardcore(), type);
/*  234 */         worldsettings.setWorldName(worldNameIn2);
/*      */         
/*  236 */         if (this.enableBonusChest)
/*      */         {
/*  238 */           worldsettings.enableBonusChest();
/*      */         }
/*      */       } 
/*      */       
/*  242 */       worldinfo = new WorldInfo(worldsettings, worldNameIn);
/*      */     }
/*      */     else {
/*      */       
/*  246 */       worldinfo.setWorldName(worldNameIn);
/*  247 */       worldsettings = new WorldSettings(worldinfo);
/*      */     } 
/*      */     
/*  250 */     for (int i = 0; i < this.worldServers.length; i++) {
/*      */       
/*  252 */       int j = 0;
/*      */       
/*  254 */       if (i == 1)
/*      */       {
/*  256 */         j = -1;
/*      */       }
/*      */       
/*  259 */       if (i == 2)
/*      */       {
/*  261 */         j = 1;
/*      */       }
/*      */       
/*  264 */       if (i == 0) {
/*      */         
/*  266 */         if (isDemo()) {
/*      */           
/*  268 */           this.worldServers[i] = (WorldServer)(new DemoWorldServer(this, isavehandler, worldinfo, j, this.theProfiler)).init();
/*      */         }
/*      */         else {
/*      */           
/*  272 */           this.worldServers[i] = (WorldServer)(new WorldServer(this, isavehandler, worldinfo, j, this.theProfiler)).init();
/*      */         } 
/*      */         
/*  275 */         this.worldServers[i].initialize(worldsettings);
/*      */       }
/*      */       else {
/*      */         
/*  279 */         this.worldServers[i] = (WorldServer)(new WorldServerMulti(this, isavehandler, j, this.worldServers[0], this.theProfiler)).init();
/*      */       } 
/*      */       
/*  282 */       this.worldServers[i].addWorldAccess((IWorldAccess)new WorldManager(this, this.worldServers[i]));
/*      */       
/*  284 */       if (!isSinglePlayer())
/*      */       {
/*  286 */         this.worldServers[i].getWorldInfo().setGameType(getGameType());
/*      */       }
/*      */     } 
/*      */     
/*  290 */     this.serverConfigManager.setPlayerManager(this.worldServers);
/*  291 */     setDifficultyForAllWorlds(getDifficulty());
/*  292 */     initialWorldChunkLoad();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initialWorldChunkLoad() {
/*  297 */     int i = 16;
/*  298 */     int j = 4;
/*  299 */     int k = 192;
/*  300 */     int l = 625;
/*  301 */     int i1 = 0;
/*  302 */     setUserMessage("menu.generatingTerrain");
/*  303 */     int j1 = 0;
/*  304 */     logger.info("Preparing start region for level " + j1);
/*  305 */     WorldServer worldserver = this.worldServers[j1];
/*  306 */     BlockPos blockpos = worldserver.getSpawnPoint();
/*  307 */     long k1 = getCurrentTimeMillis();
/*      */     
/*  309 */     for (int l1 = -192; l1 <= 192 && isServerRunning(); l1 += 16) {
/*      */       
/*  311 */       for (int i2 = -192; i2 <= 192 && isServerRunning(); i2 += 16) {
/*      */         
/*  313 */         long j2 = getCurrentTimeMillis();
/*      */         
/*  315 */         if (j2 - k1 > 1000L) {
/*      */           
/*  317 */           outputPercentRemaining("Preparing spawn area", i1 * 100 / 625);
/*  318 */           k1 = j2;
/*      */         } 
/*      */         
/*  321 */         i1++;
/*  322 */         worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
/*      */       } 
/*      */     } 
/*      */     
/*  326 */     clearCurrentTask();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn) {
/*  331 */     File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");
/*      */     
/*  333 */     if (file1.isFile())
/*      */     {
/*  335 */       setResourcePack("level://" + worldNameIn + "/" + file1.getName(), "");
/*      */     }
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
/*      */   protected void outputPercentRemaining(String message, int percent) {
/*  355 */     this.currentTask = message;
/*  356 */     this.percentDone = percent;
/*  357 */     logger.info(message + ": " + percent + "%");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void clearCurrentTask() {
/*  362 */     this.currentTask = null;
/*  363 */     this.percentDone = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void saveAllWorlds(boolean dontLog) {
/*  368 */     if (!this.worldIsBeingDeleted)
/*      */     {
/*  370 */       for (WorldServer worldserver : this.worldServers) {
/*      */         
/*  372 */         if (worldserver != null) {
/*      */           
/*  374 */           if (!dontLog)
/*      */           {
/*  376 */             logger.info("Saving chunks for level '" + worldserver.getWorldInfo().getWorldName() + "'/" + worldserver.provider.getDimensionName());
/*      */           }
/*      */ 
/*      */           
/*      */           try {
/*  381 */             worldserver.saveAllChunks(true, (IProgressUpdate)null);
/*      */           }
/*  383 */           catch (MinecraftException minecraftexception) {
/*      */             
/*  385 */             logger.warn(minecraftexception.getMessage());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopServer() {
/*  394 */     if (!this.worldIsBeingDeleted) {
/*      */       
/*  396 */       logger.info("Stopping server");
/*      */       
/*  398 */       if (getNetworkSystem() != null)
/*      */       {
/*  400 */         getNetworkSystem().terminateEndpoints();
/*      */       }
/*      */       
/*  403 */       if (this.serverConfigManager != null) {
/*      */         
/*  405 */         logger.info("Saving players");
/*  406 */         this.serverConfigManager.saveAllPlayerData();
/*  407 */         this.serverConfigManager.removeAllPlayers();
/*      */       } 
/*      */       
/*  410 */       if (this.worldServers != null) {
/*      */         
/*  412 */         logger.info("Saving worlds");
/*  413 */         saveAllWorlds(false);
/*      */         
/*  415 */         for (int i = 0; i < this.worldServers.length; i++) {
/*      */           
/*  417 */           WorldServer worldserver = this.worldServers[i];
/*  418 */           worldserver.flush();
/*      */         } 
/*      */       } 
/*      */       
/*  422 */       if (this.usageSnooper.isSnooperRunning())
/*      */       {
/*  424 */         this.usageSnooper.stopSnooper();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isServerRunning() {
/*  431 */     return this.serverRunning;
/*      */   }
/*      */ 
/*      */   
/*      */   public void initiateShutdown() {
/*  436 */     this.serverRunning = false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setInstance() {
/*  441 */     mcServer = this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*      */     try {
/*  448 */       if (startServer()) {
/*      */         
/*  450 */         this.currentTime = getCurrentTimeMillis();
/*  451 */         long i = 0L;
/*  452 */         this.statusResponse.setServerDescription((IChatComponent)new ChatComponentText(this.motd));
/*  453 */         this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8.9", 47));
/*  454 */         addFaviconToStatusResponse(this.statusResponse);
/*      */         
/*  456 */         while (this.serverRunning)
/*      */         {
/*  458 */           long k = getCurrentTimeMillis();
/*  459 */           long j = k - this.currentTime;
/*      */           
/*  461 */           if (j > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
/*      */             
/*  463 */             logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[] { Long.valueOf(j), Long.valueOf(j / 50L) });
/*  464 */             j = 2000L;
/*  465 */             this.timeOfLastWarning = this.currentTime;
/*      */           } 
/*      */           
/*  468 */           if (j < 0L) {
/*      */             
/*  470 */             logger.warn("Time ran backwards! Did the system time change?");
/*  471 */             j = 0L;
/*      */           } 
/*      */           
/*  474 */           i += j;
/*  475 */           this.currentTime = k;
/*      */           
/*  477 */           if (this.worldServers[0].areAllPlayersAsleep()) {
/*      */             
/*  479 */             tick();
/*  480 */             i = 0L;
/*      */           }
/*      */           else {
/*      */             
/*  484 */             while (i > 50L) {
/*      */               
/*  486 */               i -= 50L;
/*  487 */               tick();
/*      */             } 
/*      */           } 
/*      */           
/*  491 */           Thread.sleep(Math.max(1L, 50L - i));
/*  492 */           this.serverIsRunning = true;
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  497 */         finalTick((CrashReport)null);
/*      */       }
/*      */     
/*  500 */     } catch (Throwable throwable1) {
/*      */       
/*  502 */       logger.error("Encountered an unexpected exception", throwable1);
/*  503 */       CrashReport crashreport = null;
/*      */       
/*  505 */       if (throwable1 instanceof ReportedException) {
/*      */         
/*  507 */         crashreport = addServerInfoToCrashReport(((ReportedException)throwable1).getCrashReport());
/*      */       }
/*      */       else {
/*      */         
/*  511 */         crashreport = addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
/*      */       } 
/*      */       
/*  514 */       File file1 = new File(new File(getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
/*      */       
/*  516 */       if (crashreport.saveToFile(file1)) {
/*      */         
/*  518 */         logger.error("This crash report has been saved to: " + file1.getAbsolutePath());
/*      */       }
/*      */       else {
/*      */         
/*  522 */         logger.error("We were unable to save this crash report to disk.");
/*      */       } 
/*      */       
/*  525 */       finalTick(crashreport);
/*      */     } finally {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  531 */         this.serverStopped = true;
/*  532 */         stopServer();
/*      */       }
/*  534 */       catch (Throwable throwable) {
/*      */         
/*  536 */         logger.error("Exception stopping the server", throwable);
/*      */       }
/*      */       finally {
/*      */         
/*  540 */         systemExitNow();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addFaviconToStatusResponse(ServerStatusResponse response) {
/*  547 */     File file1 = getFile("server-icon.png");
/*      */     
/*  549 */     if (file1.isFile()) {
/*      */       
/*  551 */       ByteBuf bytebuf = Unpooled.buffer();
/*      */ 
/*      */       
/*      */       try {
/*  555 */         BufferedImage bufferedimage = ImageIO.read(file1);
/*  556 */         Validate.validState((bufferedimage.getWidth() == 64), "Must be 64 pixels wide", new Object[0]);
/*  557 */         Validate.validState((bufferedimage.getHeight() == 64), "Must be 64 pixels high", new Object[0]);
/*  558 */         ImageIO.write(bufferedimage, "PNG", (OutputStream)new ByteBufOutputStream(bytebuf));
/*  559 */         ByteBuf bytebuf1 = Base64.encode(bytebuf);
/*  560 */         response.setFavicon("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
/*      */       }
/*  562 */       catch (Exception exception) {
/*      */         
/*  564 */         logger.error("Couldn't load server icon", exception);
/*      */       }
/*      */       finally {
/*      */         
/*  568 */         bytebuf.release();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public File getDataDirectory() {
/*  575 */     return new File(".");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalTick(CrashReport report) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected void systemExitNow() {}
/*      */ 
/*      */   
/*      */   public void tick() {
/*  588 */     long i = System.nanoTime();
/*  589 */     this.tickCounter++;
/*      */     
/*  591 */     if (this.startProfiling) {
/*      */       
/*  593 */       this.startProfiling = false;
/*  594 */       this.theProfiler.profilingEnabled = true;
/*  595 */       this.theProfiler.clearProfiling();
/*      */     } 
/*      */     
/*  598 */     this.theProfiler.startSection("root");
/*  599 */     updateTimeLightAndEntities();
/*      */     
/*  601 */     if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
/*      */       
/*  603 */       this.nanoTimeSinceStatusRefresh = i;
/*  604 */       this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(getMaxPlayers(), getCurrentPlayerCount()));
/*  605 */       GameProfile[] agameprofile = new GameProfile[Math.min(getCurrentPlayerCount(), 12)];
/*  606 */       int j = MathHelper.getRandomIntegerInRange(this.random, 0, getCurrentPlayerCount() - agameprofile.length);
/*      */       
/*  608 */       for (int k = 0; k < agameprofile.length; k++)
/*      */       {
/*  610 */         agameprofile[k] = ((EntityPlayerMP)this.serverConfigManager.getPlayerList().get(j + k)).getGameProfile();
/*      */       }
/*      */       
/*  613 */       Collections.shuffle(Arrays.asList((Object[])agameprofile));
/*  614 */       this.statusResponse.getPlayerCountData().setPlayers(agameprofile);
/*      */     } 
/*      */     
/*  617 */     if (this.tickCounter % 900 == 0) {
/*      */       
/*  619 */       this.theProfiler.startSection("save");
/*  620 */       this.serverConfigManager.saveAllPlayerData();
/*  621 */       saveAllWorlds(true);
/*  622 */       this.theProfiler.endSection();
/*      */     } 
/*      */     
/*  625 */     this.theProfiler.startSection("tallying");
/*  626 */     this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - i;
/*  627 */     this.theProfiler.endSection();
/*  628 */     this.theProfiler.startSection("snooper");
/*      */     
/*  630 */     if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100)
/*      */     {
/*  632 */       this.usageSnooper.startSnooper();
/*      */     }
/*      */     
/*  635 */     if (this.tickCounter % 6000 == 0)
/*      */     {
/*  637 */       this.usageSnooper.addMemoryStatsToSnooper();
/*      */     }
/*      */     
/*  640 */     this.theProfiler.endSection();
/*  641 */     this.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTimeLightAndEntities() {
/*  646 */     this.theProfiler.startSection("jobs");
/*      */     
/*  648 */     synchronized (this.futureTaskQueue) {
/*      */       
/*  650 */       while (!this.futureTaskQueue.isEmpty())
/*      */       {
/*  652 */         Util.runTask(this.futureTaskQueue.poll(), logger);
/*      */       }
/*      */     } 
/*      */     
/*  656 */     this.theProfiler.endStartSection("levels");
/*      */     
/*  658 */     for (int j = 0; j < this.worldServers.length; j++) {
/*      */       
/*  660 */       long i = System.nanoTime();
/*      */       
/*  662 */       if (j == 0 || getAllowNether()) {
/*      */         
/*  664 */         WorldServer worldserver = this.worldServers[j];
/*  665 */         this.theProfiler.startSection(worldserver.getWorldInfo().getWorldName());
/*      */         
/*  667 */         if (this.tickCounter % 20 == 0) {
/*      */           
/*  669 */           this.theProfiler.startSection("timeSync");
/*  670 */           this.serverConfigManager.sendPacketToAllPlayersInDimension((Packet)new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimensionId());
/*  671 */           this.theProfiler.endSection();
/*      */         } 
/*      */         
/*  674 */         this.theProfiler.startSection("tick");
/*      */ 
/*      */         
/*      */         try {
/*  678 */           worldserver.tick();
/*      */         }
/*  680 */         catch (Throwable throwable1) {
/*      */           
/*  682 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
/*  683 */           worldserver.addWorldInfoToCrashReport(crashreport);
/*  684 */           throw new ReportedException(crashreport);
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/*  689 */           worldserver.updateEntities();
/*      */         }
/*  691 */         catch (Throwable throwable) {
/*      */           
/*  693 */           CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
/*  694 */           worldserver.addWorldInfoToCrashReport(crashreport1);
/*  695 */           throw new ReportedException(crashreport1);
/*      */         } 
/*      */         
/*  698 */         this.theProfiler.endSection();
/*  699 */         this.theProfiler.startSection("tracker");
/*  700 */         worldserver.getEntityTracker().updateTrackedEntities();
/*  701 */         this.theProfiler.endSection();
/*  702 */         this.theProfiler.endSection();
/*      */       } 
/*      */       
/*  705 */       this.timeOfLastDimensionTick[j][this.tickCounter % 100] = System.nanoTime() - i;
/*      */     } 
/*      */     
/*  708 */     this.theProfiler.endStartSection("connection");
/*  709 */     getNetworkSystem().networkTick();
/*  710 */     this.theProfiler.endStartSection("players");
/*  711 */     this.serverConfigManager.onTick();
/*  712 */     this.theProfiler.endStartSection("tickables");
/*      */     
/*  714 */     for (int k = 0; k < this.playersOnline.size(); k++)
/*      */     {
/*  716 */       ((ITickable)this.playersOnline.get(k)).update();
/*      */     }
/*      */     
/*  719 */     this.theProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAllowNether() {
/*  724 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void startServerThread() {
/*  729 */     this.serverThread = new Thread(this, "Server thread");
/*  730 */     this.serverThread.start();
/*      */   }
/*      */ 
/*      */   
/*      */   public File getFile(String fileName) {
/*  735 */     return new File(getDataDirectory(), fileName);
/*      */   }
/*      */ 
/*      */   
/*      */   public void logWarning(String msg) {
/*  740 */     logger.warn(msg);
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldServer worldServerForDimension(int dimension) {
/*  745 */     return (dimension == -1) ? this.worldServers[1] : ((dimension == 1) ? this.worldServers[2] : this.worldServers[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getMinecraftVersion() {
/*  750 */     return "1.8.9";
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCurrentPlayerCount() {
/*  755 */     return this.serverConfigManager.getCurrentPlayerCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxPlayers() {
/*  760 */     return this.serverConfigManager.getMaxPlayers();
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getAllUsernames() {
/*  765 */     return this.serverConfigManager.getAllUsernames();
/*      */   }
/*      */ 
/*      */   
/*      */   public GameProfile[] getGameProfiles() {
/*  770 */     return this.serverConfigManager.getAllProfiles();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getServerModName() {
/*  775 */     return "vanilla";
/*      */   }
/*      */ 
/*      */   
/*      */   public CrashReport addServerInfoToCrashReport(CrashReport report) {
/*  780 */     report.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/*  784 */             return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
/*      */           }
/*      */         });
/*      */     
/*  788 */     if (this.serverConfigManager != null)
/*      */     {
/*  790 */       report.getCategory().addCrashSectionCallable("Player Count", new Callable<String>()
/*      */           {
/*      */             public String call()
/*      */             {
/*  794 */               return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.getPlayerList();
/*      */             }
/*      */           });
/*      */     }
/*      */     
/*  799 */     return report;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> getTabCompletions(ICommandSender sender, String input, BlockPos pos) {
/*  804 */     List<String> list = Lists.newArrayList();
/*      */     
/*  806 */     if (input.startsWith("/")) {
/*      */       
/*  808 */       input = input.substring(1);
/*  809 */       boolean flag = !input.contains(" ");
/*  810 */       List<String> list1 = this.commandManager.getTabCompletionOptions(sender, input, pos);
/*      */       
/*  812 */       if (list1 != null)
/*      */       {
/*  814 */         for (String s2 : list1) {
/*      */           
/*  816 */           if (flag) {
/*      */             
/*  818 */             list.add("/" + s2);
/*      */             
/*      */             continue;
/*      */           } 
/*  822 */           list.add(s2);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  827 */       return list;
/*      */     } 
/*      */ 
/*      */     
/*  831 */     String[] astring = input.split(" ", -1);
/*  832 */     String s = astring[astring.length - 1];
/*      */     
/*  834 */     for (String s1 : this.serverConfigManager.getAllUsernames()) {
/*      */       
/*  836 */       if (CommandBase.doesStringStartWith(s, s1))
/*      */       {
/*  838 */         list.add(s1);
/*      */       }
/*      */     } 
/*      */     
/*  842 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static MinecraftServer getServer() {
/*  848 */     return mcServer;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAnvilFileSet() {
/*  853 */     return (this.anvilFile != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/*  858 */     return "Server";
/*      */   }
/*      */ 
/*      */   
/*      */   public void addChatMessage(IChatComponent component) {
/*  863 */     logger.info(component.getUnformattedText());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
/*  868 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public ICommandManager getCommandManager() {
/*  873 */     return this.commandManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyPair getKeyPair() {
/*  878 */     return this.serverKeyPair;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getServerOwner() {
/*  883 */     return this.serverOwner;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setServerOwner(String owner) {
/*  888 */     this.serverOwner = owner;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSinglePlayer() {
/*  893 */     return (this.serverOwner != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getFolderName() {
/*  898 */     return this.folderName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFolderName(String name) {
/*  903 */     this.folderName = name;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWorldName(String p_71246_1_) {
/*  908 */     this.worldName = p_71246_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getWorldName() {
/*  913 */     return this.worldName;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setKeyPair(KeyPair keyPair) {
/*  918 */     this.serverKeyPair = keyPair;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
/*  923 */     for (int i = 0; i < this.worldServers.length; i++) {
/*      */       
/*  925 */       WorldServer worldServer = this.worldServers[i];
/*      */       
/*  927 */       if (worldServer != null)
/*      */       {
/*  929 */         if (worldServer.getWorldInfo().isHardcoreModeEnabled()) {
/*      */           
/*  931 */           worldServer.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
/*  932 */           worldServer.setAllowedSpawnTypes(true, true);
/*      */         }
/*  934 */         else if (isSinglePlayer()) {
/*      */           
/*  936 */           worldServer.getWorldInfo().setDifficulty(difficulty);
/*  937 */           worldServer.setAllowedSpawnTypes((worldServer.getDifficulty() != EnumDifficulty.PEACEFUL), true);
/*      */         }
/*      */         else {
/*      */           
/*  941 */           worldServer.getWorldInfo().setDifficulty(difficulty);
/*  942 */           worldServer.setAllowedSpawnTypes(allowSpawnMonsters(), this.canSpawnAnimals);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean allowSpawnMonsters() {
/*  950 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDemo() {
/*  955 */     return this.isDemo;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDemo(boolean demo) {
/*  960 */     this.isDemo = demo;
/*      */   }
/*      */ 
/*      */   
/*      */   public void canCreateBonusChest(boolean enable) {
/*  965 */     this.enableBonusChest = enable;
/*      */   }
/*      */ 
/*      */   
/*      */   public ISaveFormat getActiveAnvilConverter() {
/*  970 */     return this.anvilConverterForAnvilFile;
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteWorldAndStopServer() {
/*  975 */     this.worldIsBeingDeleted = true;
/*  976 */     getActiveAnvilConverter().flushCache();
/*      */     
/*  978 */     for (int i = 0; i < this.worldServers.length; i++) {
/*      */       
/*  980 */       WorldServer worldserver = this.worldServers[i];
/*      */       
/*  982 */       if (worldserver != null)
/*      */       {
/*  984 */         worldserver.flush();
/*      */       }
/*      */     } 
/*      */     
/*  988 */     getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].getSaveHandler().getWorldDirectoryName());
/*  989 */     initiateShutdown();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getResourcePackUrl() {
/*  994 */     return this.resourcePackUrl;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getResourcePackHash() {
/*  999 */     return this.resourcePackHash;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setResourcePack(String url, String hash) {
/* 1004 */     this.resourcePackUrl = url;
/* 1005 */     this.resourcePackHash = hash;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
/* 1010 */     playerSnooper.addClientStat("whitelist_enabled", Boolean.valueOf(false));
/* 1011 */     playerSnooper.addClientStat("whitelist_count", Integer.valueOf(0));
/*      */     
/* 1013 */     if (this.serverConfigManager != null) {
/*      */       
/* 1015 */       playerSnooper.addClientStat("players_current", Integer.valueOf(getCurrentPlayerCount()));
/* 1016 */       playerSnooper.addClientStat("players_max", Integer.valueOf(getMaxPlayers()));
/* 1017 */       playerSnooper.addClientStat("players_seen", Integer.valueOf((this.serverConfigManager.getAvailablePlayerDat()).length));
/*      */     } 
/*      */     
/* 1020 */     playerSnooper.addClientStat("uses_auth", Boolean.valueOf(this.onlineMode));
/* 1021 */     playerSnooper.addClientStat("gui_state", getGuiEnabled() ? "enabled" : "disabled");
/* 1022 */     playerSnooper.addClientStat("run_time", Long.valueOf((getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
/* 1023 */     playerSnooper.addClientStat("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
/* 1024 */     int i = 0;
/*      */     
/* 1026 */     if (this.worldServers != null)
/*      */     {
/* 1028 */       for (int j = 0; j < this.worldServers.length; j++) {
/*      */         
/* 1030 */         if (this.worldServers[j] != null) {
/*      */           
/* 1032 */           WorldServer worldserver = this.worldServers[j];
/* 1033 */           WorldInfo worldinfo = worldserver.getWorldInfo();
/* 1034 */           playerSnooper.addClientStat("world[" + i + "][dimension]", Integer.valueOf(worldserver.provider.getDimensionId()));
/* 1035 */           playerSnooper.addClientStat("world[" + i + "][mode]", worldinfo.getGameType());
/* 1036 */           playerSnooper.addClientStat("world[" + i + "][difficulty]", worldserver.getDifficulty());
/* 1037 */           playerSnooper.addClientStat("world[" + i + "][hardcore]", Boolean.valueOf(worldinfo.isHardcoreModeEnabled()));
/* 1038 */           playerSnooper.addClientStat("world[" + i + "][generator_name]", worldinfo.getTerrainType().getWorldTypeName());
/* 1039 */           playerSnooper.addClientStat("world[" + i + "][generator_version]", Integer.valueOf(worldinfo.getTerrainType().getGeneratorVersion()));
/* 1040 */           playerSnooper.addClientStat("world[" + i + "][height]", Integer.valueOf(this.buildLimit));
/* 1041 */           playerSnooper.addClientStat("world[" + i + "][chunks_loaded]", Integer.valueOf(worldserver.getChunkProvider().getLoadedChunkCount()));
/* 1042 */           i++;
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/* 1047 */     playerSnooper.addClientStat("worlds", Integer.valueOf(i));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper) {
/* 1052 */     playerSnooper.addStatToSnooper("singleplayer", Boolean.valueOf(isSinglePlayer()));
/* 1053 */     playerSnooper.addStatToSnooper("server_brand", getServerModName());
/* 1054 */     playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
/* 1055 */     playerSnooper.addStatToSnooper("dedicated", Boolean.valueOf(isDedicatedServer()));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSnooperEnabled() {
/* 1060 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isServerInOnlineMode() {
/* 1067 */     return this.onlineMode;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOnlineMode(boolean online) {
/* 1072 */     this.onlineMode = online;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getCanSpawnAnimals() {
/* 1077 */     return this.canSpawnAnimals;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCanSpawnAnimals(boolean spawnAnimals) {
/* 1082 */     this.canSpawnAnimals = spawnAnimals;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getCanSpawnNPCs() {
/* 1087 */     return this.canSpawnNPCs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCanSpawnNPCs(boolean spawnNpcs) {
/* 1094 */     this.canSpawnNPCs = spawnNpcs;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPVPEnabled() {
/* 1099 */     return this.pvpEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAllowPvp(boolean allowPvp) {
/* 1104 */     this.pvpEnabled = allowPvp;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFlightAllowed() {
/* 1109 */     return this.allowFlight;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAllowFlight(boolean allow) {
/* 1114 */     this.allowFlight = allow;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMOTD() {
/* 1121 */     return this.motd;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMOTD(String motdIn) {
/* 1126 */     this.motd = motdIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBuildLimit() {
/* 1131 */     return this.buildLimit;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBuildLimit(int maxBuildHeight) {
/* 1136 */     this.buildLimit = maxBuildHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isServerStopped() {
/* 1141 */     return this.serverStopped;
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerConfigurationManager getConfigurationManager() {
/* 1146 */     return this.serverConfigManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setConfigManager(ServerConfigurationManager configManager) {
/* 1151 */     this.serverConfigManager = configManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGameType(WorldSettings.GameType gameMode) {
/* 1156 */     for (int i = 0; i < this.worldServers.length; i++)
/*      */     {
/* 1158 */       (getServer()).worldServers[i].getWorldInfo().setGameType(gameMode);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public NetworkSystem getNetworkSystem() {
/* 1164 */     return this.networkSystem;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean serverIsInRunLoop() {
/* 1169 */     return this.serverIsRunning;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getGuiEnabled() {
/* 1174 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTickCounter() {
/* 1181 */     return this.tickCounter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void enableProfiling() {
/* 1186 */     this.startProfiling = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public PlayerUsageSnooper getPlayerUsageSnooper() {
/* 1191 */     return this.usageSnooper;
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getPosition() {
/* 1196 */     return BlockPos.ORIGIN;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getPositionVector() {
/* 1201 */     return new Vec3(0.0D, 0.0D, 0.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public World getEntityWorld() {
/* 1206 */     return (World)this.worldServers[0];
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getCommandSenderEntity() {
/* 1211 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSpawnProtectionSize() {
/* 1216 */     return 16;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn) {
/* 1221 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getForceGamemode() {
/* 1226 */     return this.isGamemodeForced;
/*      */   }
/*      */ 
/*      */   
/*      */   public Proxy getServerProxy() {
/* 1231 */     return this.serverProxy;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long getCurrentTimeMillis() {
/* 1236 */     return System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxPlayerIdleMinutes() {
/* 1241 */     return this.maxPlayerIdleMinutes;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPlayerIdleTimeout(int idleTimeout) {
/* 1246 */     this.maxPlayerIdleMinutes = idleTimeout;
/*      */   }
/*      */ 
/*      */   
/*      */   public IChatComponent getDisplayName() {
/* 1251 */     return (IChatComponent)new ChatComponentText(getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAnnouncingPlayerAchievements() {
/* 1256 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public MinecraftSessionService getMinecraftSessionService() {
/* 1261 */     return this.sessionService;
/*      */   }
/*      */ 
/*      */   
/*      */   public GameProfileRepository getGameProfileRepository() {
/* 1266 */     return this.profileRepo;
/*      */   }
/*      */ 
/*      */   
/*      */   public PlayerProfileCache getPlayerProfileCache() {
/* 1271 */     return this.profileCache;
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerStatusResponse getServerStatusResponse() {
/* 1276 */     return this.statusResponse;
/*      */   }
/*      */ 
/*      */   
/*      */   public void refreshStatusNextTick() {
/* 1281 */     this.nanoTimeSinceStatusRefresh = 0L;
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getEntityFromUuid(UUID uuid) {
/* 1286 */     for (WorldServer worldserver : this.worldServers) {
/*      */       
/* 1288 */       if (worldserver != null) {
/*      */         
/* 1290 */         Entity entity = worldserver.getEntityFromUuid(uuid);
/*      */         
/* 1292 */         if (entity != null)
/*      */         {
/* 1294 */           return entity;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1299 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean sendCommandFeedback() {
/* 1304 */     return (getServer()).worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCommandStat(CommandResultStats.Type type, int amount) {}
/*      */ 
/*      */   
/*      */   public int getMaxWorldSize() {
/* 1313 */     return 29999984;
/*      */   }
/*      */ 
/*      */   
/*      */   public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable) {
/* 1318 */     Validate.notNull(callable);
/*      */     
/* 1320 */     if (!isCallingFromMinecraftThread() && !isServerStopped()) {
/*      */       
/* 1322 */       ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callable);
/*      */       
/* 1324 */       synchronized (this.futureTaskQueue) {
/*      */         
/* 1326 */         this.futureTaskQueue.add(listenablefuturetask);
/* 1327 */         return (ListenableFuture<V>)listenablefuturetask;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1334 */       return Futures.immediateFuture(callable.call());
/*      */     }
/* 1336 */     catch (Exception exception) {
/*      */       
/* 1338 */       return (ListenableFuture<V>)Futures.immediateFailedCheckedFuture(exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
/* 1345 */     Validate.notNull(runnableToSchedule);
/* 1346 */     return callFromMainThread(Executors.callable(runnableToSchedule));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCallingFromMinecraftThread() {
/* 1351 */     return (Thread.currentThread() == this.serverThread);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNetworkCompressionTreshold() {
/* 1356 */     return 256;
/*      */   }
/*      */   
/*      */   protected abstract boolean startServer() throws IOException;
/*      */   
/*      */   public abstract boolean canStructuresSpawn();
/*      */   
/*      */   public abstract WorldSettings.GameType getGameType();
/*      */   
/*      */   public abstract EnumDifficulty getDifficulty();
/*      */   
/*      */   public abstract boolean isHardcore();
/*      */   
/*      */   public abstract int getOpPermissionLevel();
/*      */   
/*      */   public abstract boolean shouldBroadcastRconToOps();
/*      */   
/*      */   public abstract boolean shouldBroadcastConsoleToOps();
/*      */   
/*      */   public abstract boolean isDedicatedServer();
/*      */   
/*      */   public abstract boolean shouldUseNativeTransport();
/*      */   
/*      */   public abstract boolean isCommandBlockEnabled();
/*      */   
/*      */   public abstract String shareToLAN(WorldSettings.GameType paramGameType, boolean paramBoolean);
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\MinecraftServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */