/*      */ package net.minecraft.client;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.ListenableFutureTask;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.authlib.properties.PropertyMap;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.Proxy;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.imageio.ImageIO;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.client.audio.MusicTicker;
/*      */ import net.minecraft.client.audio.SoundHandler;
/*      */ import net.minecraft.client.entity.EntityPlayerSP;
/*      */ import net.minecraft.client.gui.FontRenderer;
/*      */ import net.minecraft.client.gui.GuiGameOver;
/*      */ import net.minecraft.client.gui.GuiMainMenu;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.gui.achievement.GuiAchievement;
/*      */ import net.minecraft.client.main.GameConfiguration;
/*      */ import net.minecraft.client.multiplayer.ServerData;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.network.NetHandlerPlayClient;
/*      */ import net.minecraft.client.renderer.BlockRendererDispatcher;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.ItemRenderer;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.RenderGlobal;
/*      */ import net.minecraft.client.renderer.Tessellator;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*      */ import net.minecraft.client.renderer.entity.RenderItem;
/*      */ import net.minecraft.client.renderer.entity.RenderManager;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.DefaultResourcePack;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.resources.IResourcePack;
/*      */ import net.minecraft.client.resources.LanguageManager;
/*      */ import net.minecraft.client.resources.ResourcePackRepository;
/*      */ import net.minecraft.client.resources.SkinManager;
/*      */ import net.minecraft.client.resources.data.IMetadataSectionSerializer;
/*      */ import net.minecraft.client.resources.data.IMetadataSerializer;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.settings.KeyBinding;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.item.EntityItemFrame;
/*      */ import net.minecraft.entity.item.EntityMinecart;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.Bootstrap;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.network.NetworkManager;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*      */ import net.minecraft.profiler.PlayerUsageSnooper;
/*      */ import net.minecraft.profiler.Profiler;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.integrated.IntegratedServer;
/*      */ import net.minecraft.stats.IStatStringFormat;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.FrameTimer;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MouseHelper;
/*      */ import net.minecraft.util.MovementInput;
/*      */ import net.minecraft.util.MovementInputFromOptions;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Session;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.storage.ISaveFormat;
/*      */ import net.minecraft.world.storage.ISaveHandler;
/*      */ import net.minecraft.world.storage.WorldInfo;
/*      */ import org.apache.commons.io.IOUtils;
/*      */ import org.apache.commons.lang3.Validate;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.Sys;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.ContextCapabilities;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.DisplayMode;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import rip.diavlo.base.Client;
/*      */ import rip.diavlo.base.events.other.KeyEvent;
/*      */ 
/*      */ public class Minecraft implements IThreadListener, IPlayerUsage {
/*  120 */   private static final Logger logger = LogManager.getLogger();
/*  121 */   private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
/*  122 */   public static final boolean isRunningOnMac = (Util.getOSType() == Util.EnumOS.OSX);
/*  123 */   public static byte[] memoryReserve = new byte[10485760];
/*  124 */   private static final List<DisplayMode> macDisplayModes = Lists.newArrayList((Object[])new DisplayMode[] { new DisplayMode(2560, 1600), new DisplayMode(2880, 1800) });
/*      */   private final File fileResourcepacks;
/*      */   private final PropertyMap twitchDetails;
/*      */   private final PropertyMap profileProperties;
/*      */   private ServerData currentServerData;
/*      */   private TextureManager renderEngine;
/*      */   private static Minecraft theMinecraft;
/*      */   public PlayerControllerMP playerController;
/*      */   private boolean fullscreen;
/*      */   private boolean enableGLErrorChecking = true;
/*      */   private boolean hasCrashed;
/*      */   private CrashReport crashReporter;
/*      */   public int displayWidth;
/*      */   public int displayHeight;
/*      */   private boolean connectedToRealms = false;
/*  139 */   public Timer timer = new Timer(20.0F);
/*  140 */   private PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getCurrentTimeMillis());
/*      */   public WorldClient theWorld;
/*      */   public RenderGlobal renderGlobal;
/*      */   private RenderManager renderManager;
/*      */   private RenderItem renderItem;
/*      */   private ItemRenderer itemRenderer;
/*      */   public EntityPlayerSP thePlayer;
/*      */   private Entity renderViewEntity;
/*      */   public Entity pointedEntity;
/*      */   public EffectRenderer effectRenderer;
/*      */   public Session session;
/*      */   private boolean isGamePaused;
/*      */   public FontRenderer fontRendererObj;
/*      */   public FontRenderer standardGalacticFontRenderer;
/*      */   public GuiScreen currentScreen;
/*      */   public LoadingScreenRenderer loadingScreen;
/*      */   public EntityRenderer entityRenderer;
/*      */   private int leftClickCounter;
/*      */   private int tempDisplayWidth;
/*      */   private int tempDisplayHeight;
/*      */   private IntegratedServer theIntegratedServer;
/*      */   public GuiAchievement guiAchievement;
/*      */   public GuiIngame ingameGUI;
/*      */   public boolean skipRenderWorld;
/*      */   public MovingObjectPosition objectMouseOver;
/*      */   public GameSettings gameSettings;
/*      */   public MouseHelper mouseHelper;
/*      */   public final File mcDataDir;
/*      */   private final File fileAssets;
/*      */   private final String launchedVersion;
/*      */   private final Proxy proxy;
/*      */   private ISaveFormat saveLoader;
/*      */   private static int debugFPS;
/*      */   private int rightClickDelayTimer;
/*      */   private String serverName;
/*      */   private int serverPort;
/*      */   public boolean inGameHasFocus;
/*  177 */   long systemTime = getSystemTime();
/*      */   private int joinPlayerCounter;
/*  179 */   public final FrameTimer frameTimer = new FrameTimer();
/*  180 */   long startNanoTime = System.nanoTime();
/*      */   private final boolean jvm64bit;
/*      */   private final boolean isDemo;
/*      */   private NetworkManager myNetworkManager;
/*      */   private boolean integratedServerIsRunning;
/*  185 */   public final Profiler mcProfiler = new Profiler();
/*  186 */   private long debugCrashKeyPressTime = -1L;
/*      */   private IReloadableResourceManager mcResourceManager;
/*  188 */   private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
/*  189 */   private final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
/*      */   private final DefaultResourcePack mcDefaultResourcePack;
/*      */   private ResourcePackRepository mcResourcePackRepository;
/*      */   private LanguageManager mcLanguageManager;
/*      */   private Framebuffer framebufferMc;
/*      */   private TextureMap textureMapBlocks;
/*      */   private SoundHandler mcSoundHandler;
/*      */   private MusicTicker mcMusicTicker;
/*      */   private ResourceLocation mojangLogo;
/*      */   private final MinecraftSessionService sessionService;
/*      */   private SkinManager skinManager;
/*  200 */   private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
/*  201 */   private long field_175615_aJ = 0L;
/*  202 */   private final Thread mcThread = Thread.currentThread();
/*      */   private ModelManager modelManager;
/*      */   private BlockRendererDispatcher blockRenderDispatcher;
/*      */   volatile boolean running = true;
/*  206 */   public String debug = "";
/*      */   public boolean field_175613_B = false;
/*      */   public boolean field_175614_C = false;
/*      */   public boolean field_175611_D = false;
/*      */   public boolean renderChunksMany = true;
/*  211 */   long debugUpdateTime = getSystemTime();
/*      */   int fpsCounter;
/*  213 */   long prevFrameTime = -1L;
/*  214 */   private String debugProfilerName = "root";
/*      */ 
/*      */   
/*      */   public Minecraft(GameConfiguration gameConfig) {
/*  218 */     theMinecraft = this;
/*  219 */     this.mcDataDir = gameConfig.folderInfo.mcDataDir;
/*  220 */     this.fileAssets = gameConfig.folderInfo.assetsDir;
/*  221 */     this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
/*  222 */     this.launchedVersion = gameConfig.gameInfo.version;
/*  223 */     this.twitchDetails = gameConfig.userInfo.userProperties;
/*  224 */     this.profileProperties = gameConfig.userInfo.profileProperties;
/*  225 */     this.mcDefaultResourcePack = new DefaultResourcePack((new ResourceIndex(gameConfig.folderInfo.assetsDir, gameConfig.folderInfo.assetIndex)).getResourceMap());
/*  226 */     this.proxy = (gameConfig.userInfo.proxy == null) ? Proxy.NO_PROXY : gameConfig.userInfo.proxy;
/*  227 */     this.sessionService = (new YggdrasilAuthenticationService(gameConfig.userInfo.proxy, UUID.randomUUID().toString())).createMinecraftSessionService();
/*  228 */     this.session = gameConfig.userInfo.session;
/*  229 */     logger.info("Setting user: " + this.session.getUsername());
/*  230 */     logger.info("(Session ID is " + this.session.getSessionID() + ")");
/*  231 */     this.isDemo = gameConfig.gameInfo.isDemo;
/*  232 */     this.displayWidth = (gameConfig.displayInfo.width > 0) ? gameConfig.displayInfo.width : 1;
/*  233 */     this.displayHeight = (gameConfig.displayInfo.height > 0) ? gameConfig.displayInfo.height : 1;
/*  234 */     this.tempDisplayWidth = gameConfig.displayInfo.width;
/*  235 */     this.tempDisplayHeight = gameConfig.displayInfo.height;
/*  236 */     this.fullscreen = gameConfig.displayInfo.fullscreen;
/*  237 */     this.jvm64bit = isJvm64bit();
/*  238 */     this.theIntegratedServer = new IntegratedServer(this);
/*      */     
/*  240 */     if (gameConfig.serverInfo.serverName != null) {
/*      */       
/*  242 */       this.serverName = gameConfig.serverInfo.serverName;
/*  243 */       this.serverPort = gameConfig.serverInfo.serverPort;
/*      */     } 
/*      */     
/*  246 */     ImageIO.setUseCache(false);
/*  247 */     Bootstrap.register();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*  253 */     this.running = true;
/*      */ 
/*      */     
/*      */     try {
/*  257 */       startGame();
/*      */     }
/*  259 */     catch (Throwable throwable) {
/*      */       
/*  261 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
/*  262 */       crashreport.makeCategory("Initialization");
/*  263 */       displayCrashReport(addGraphicsAndWorldToCrashReport(crashreport));
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  271 */       while (this.running)
/*      */       {
/*  273 */         if (!this.hasCrashed || this.crashReporter == null) {
/*      */ 
/*      */           
/*      */           try {
/*  277 */             runGameLoop();
/*      */           }
/*  279 */           catch (OutOfMemoryError var10) {
/*      */             
/*  281 */             freeMemory();
/*  282 */             displayGuiScreen((GuiScreen)new GuiMemoryErrorScreen());
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*  287 */         displayCrashReport(this.crashReporter);
/*      */       }
/*      */     
/*      */     }
/*  291 */     catch (MinecraftError var12) {
/*      */ 
/*      */     
/*      */     }
/*  295 */     catch (ReportedException reportedexception) {
/*      */       
/*  297 */       addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
/*  298 */       freeMemory();
/*  299 */       logger.fatal("Reported exception thrown!", (Throwable)reportedexception);
/*  300 */       displayCrashReport(reportedexception.getCrashReport());
/*      */     
/*      */     }
/*  303 */     catch (Throwable throwable1) {
/*      */       
/*  305 */       CrashReport crashreport1 = addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
/*  306 */       freeMemory();
/*  307 */       logger.fatal("Unreported exception thrown!", throwable1);
/*  308 */       displayCrashReport(crashreport1);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  313 */       shutdownMinecraftApplet();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void startGame() throws LWJGLException, IOException {
/*  322 */     this.gameSettings = new GameSettings(this, this.mcDataDir);
/*  323 */     this.defaultResourcePacks.add(this.mcDefaultResourcePack);
/*  324 */     startTimerHackThread();
/*      */     
/*  326 */     if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
/*      */       
/*  328 */       this.displayWidth = this.gameSettings.overrideWidth;
/*  329 */       this.displayHeight = this.gameSettings.overrideHeight;
/*      */     } 
/*      */     
/*  332 */     logger.info("LWJGL Version: " + Sys.getVersion());
/*  333 */     setWindowIcon();
/*  334 */     setInitialDisplayMode();
/*  335 */     createDisplay();
/*  336 */     OpenGlHelper.initializeTextures();
/*  337 */     this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true);
/*  338 */     this.framebufferMc.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
/*  339 */     registerMetadataSerializers();
/*  340 */     this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), (IResourcePack)this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
/*  341 */     this.mcResourceManager = (IReloadableResourceManager)new SimpleReloadableResourceManager(this.metadataSerializer_);
/*  342 */     this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
/*  343 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.mcLanguageManager);
/*  344 */     refreshResources();
/*  345 */     this.renderEngine = new TextureManager((IResourceManager)this.mcResourceManager);
/*  346 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.renderEngine);
/*  347 */     drawSplashScreen(this.renderEngine);
/*  348 */     this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
/*  349 */     this.saveLoader = (ISaveFormat)new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
/*  350 */     this.mcSoundHandler = new SoundHandler((IResourceManager)this.mcResourceManager, this.gameSettings);
/*  351 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.mcSoundHandler);
/*  352 */     this.mcMusicTicker = new MusicTicker(this);
/*  353 */     this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
/*      */     
/*  355 */     if (this.gameSettings.language != null) {
/*      */       
/*  357 */       this.fontRendererObj.setUnicodeFlag(isUnicode());
/*  358 */       this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
/*      */     } 
/*      */     
/*  361 */     this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
/*  362 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.fontRendererObj);
/*  363 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.standardGalacticFontRenderer);
/*  364 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)new GrassColorReloadListener());
/*  365 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)new FoliageColorReloadListener());
/*  366 */     AchievementList.openInventory.setStatStringFormatter(new IStatStringFormat()
/*      */         {
/*      */           
/*      */           public String formatString(String str)
/*      */           {
/*      */             try {
/*  372 */               return String.format(str, new Object[] { GameSettings.getKeyDisplayString(this.this$0.gameSettings.keyBindInventory.getKeyCode()) });
/*      */             }
/*  374 */             catch (Exception exception) {
/*      */               
/*  376 */               return "Error: " + exception.getLocalizedMessage();
/*      */             } 
/*      */           }
/*      */         });
/*  380 */     this.mouseHelper = new MouseHelper();
/*  381 */     checkGLError("Pre startup");
/*  382 */     GlStateManager.enableTexture2D();
/*  383 */     GlStateManager.shadeModel(7425);
/*  384 */     GlStateManager.clearDepth(1.0D);
/*  385 */     GlStateManager.enableDepth();
/*  386 */     GlStateManager.depthFunc(515);
/*  387 */     GlStateManager.enableAlpha();
/*  388 */     GlStateManager.alphaFunc(516, 0.1F);
/*  389 */     GlStateManager.cullFace(1029);
/*  390 */     GlStateManager.matrixMode(5889);
/*  391 */     GlStateManager.loadIdentity();
/*  392 */     GlStateManager.matrixMode(5888);
/*  393 */     checkGLError("Startup");
/*  394 */     this.textureMapBlocks = new TextureMap("textures");
/*  395 */     this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
/*  396 */     this.renderEngine.loadTickableTexture(TextureMap.locationBlocksTexture, (ITickableTextureObject)this.textureMapBlocks);
/*  397 */     this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/*  398 */     this.textureMapBlocks.setBlurMipmapDirect(false, (this.gameSettings.mipmapLevels > 0));
/*  399 */     this.modelManager = new ModelManager(this.textureMapBlocks);
/*  400 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.modelManager);
/*  401 */     this.renderItem = new RenderItem(this.renderEngine, this.modelManager);
/*  402 */     this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
/*  403 */     this.itemRenderer = new ItemRenderer(this);
/*  404 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.renderItem);
/*  405 */     this.entityRenderer = new EntityRenderer(this, (IResourceManager)this.mcResourceManager);
/*  406 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.entityRenderer);
/*  407 */     this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.gameSettings);
/*  408 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.blockRenderDispatcher);
/*  409 */     this.renderGlobal = new RenderGlobal(this);
/*  410 */     this.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)this.renderGlobal);
/*  411 */     this.guiAchievement = new GuiAchievement(this);
/*  412 */     GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
/*  413 */     this.effectRenderer = new EffectRenderer((World)this.theWorld, this.renderEngine);
/*  414 */     checkGLError("Post startup");
/*  415 */     this.ingameGUI = new GuiIngame(this);
/*      */ 
/*      */ 
/*      */     
/*  419 */     Client.getInstance().onStartup();
/*      */     
/*  421 */     if (this.serverName != null) {
/*      */       
/*  423 */       displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)new GuiMainMenu(), this, this.serverName, this.serverPort));
/*      */     }
/*      */     else {
/*      */       
/*  427 */       displayGuiScreen((GuiScreen)new GuiMainMenu());
/*      */     } 
/*      */     
/*  430 */     this.renderEngine.deleteTexture(this.mojangLogo);
/*  431 */     this.mojangLogo = null;
/*  432 */     this.loadingScreen = new LoadingScreenRenderer(this);
/*      */     
/*  434 */     if (this.gameSettings.fullScreen && !this.fullscreen)
/*      */     {
/*  436 */       toggleFullscreen();
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  441 */       Display.setVSyncEnabled(this.gameSettings.enableVsync);
/*      */     }
/*  443 */     catch (OpenGLException var2) {
/*      */       
/*  445 */       this.gameSettings.enableVsync = false;
/*  446 */       this.gameSettings.saveOptions();
/*      */     } 
/*      */     
/*  449 */     this.renderGlobal.makeEntityOutlineShader();
/*      */   }
/*      */ 
/*      */   
/*      */   private void registerMetadataSerializers() {
/*  454 */     this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
/*  455 */     this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new FontMetadataSectionSerializer(), FontMetadataSection.class);
/*  456 */     this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
/*  457 */     this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new PackMetadataSectionSerializer(), PackMetadataSection.class);
/*  458 */     this.metadataSerializer_.registerMetadataSectionType((IMetadataSectionSerializer)new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
/*      */   }
/*      */ 
/*      */   
/*      */   private void createDisplay() throws LWJGLException {
/*  463 */     Display.setResizable(true);
/*  464 */     Display.setTitle("Loading...");
/*      */ 
/*      */     
/*      */     try {
/*  468 */       Display.create((new PixelFormat()).withDepthBits(24));
/*      */     }
/*  470 */     catch (LWJGLException lwjglexception) {
/*      */       
/*  472 */       logger.error("Couldn't set pixel format", (Throwable)lwjglexception);
/*      */ 
/*      */       
/*      */       try {
/*  476 */         Thread.sleep(1000L);
/*      */       }
/*  478 */       catch (InterruptedException interruptedException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  483 */       if (this.fullscreen)
/*      */       {
/*  485 */         updateDisplayMode();
/*      */       }
/*      */       
/*  488 */       Display.create();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setInitialDisplayMode() throws LWJGLException {
/*  494 */     if (this.fullscreen) {
/*      */       
/*  496 */       Display.setFullscreen(true);
/*  497 */       DisplayMode displaymode = Display.getDisplayMode();
/*  498 */       this.displayWidth = Math.max(1, displaymode.getWidth());
/*  499 */       this.displayHeight = Math.max(1, displaymode.getHeight());
/*      */     }
/*      */     else {
/*      */       
/*  503 */       Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setWindowIcon() {
/*  509 */     Util.EnumOS util$enumos = Util.getOSType();
/*      */     
/*  511 */     if (util$enumos != Util.EnumOS.OSX) {
/*      */       
/*  513 */       InputStream inputstream = null;
/*  514 */       InputStream inputstream1 = null;
/*      */ 
/*      */       
/*      */       try {
/*  518 */         inputstream = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
/*  519 */         inputstream1 = this.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
/*      */         
/*  521 */         if (inputstream != null && inputstream1 != null)
/*      */         {
/*  523 */           Display.setIcon(new ByteBuffer[] { readImageToBuffer(inputstream), readImageToBuffer(inputstream1) });
/*      */         }
/*      */       }
/*  526 */       catch (IOException ioexception) {
/*      */         
/*  528 */         logger.error("Couldn't set icon", ioexception);
/*      */       }
/*      */       finally {
/*      */         
/*  532 */         IOUtils.closeQuietly(inputstream);
/*  533 */         IOUtils.closeQuietly(inputstream1);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isJvm64bit() {
/*  540 */     String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
/*      */     
/*  542 */     for (String s : astring) {
/*      */       
/*  544 */       String s1 = System.getProperty(s);
/*      */       
/*  546 */       if (s1 != null && s1.contains("64"))
/*      */       {
/*  548 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  552 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public Framebuffer getFramebuffer() {
/*  557 */     return this.framebufferMc;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getVersion() {
/*  562 */     return this.launchedVersion;
/*      */   }
/*      */ 
/*      */   
/*      */   private void startTimerHackThread() {
/*  567 */     Thread thread = new Thread("Timer hack thread")
/*      */       {
/*      */         public void run()
/*      */         {
/*  571 */           while (Minecraft.this.running) {
/*      */ 
/*      */             
/*      */             try {
/*  575 */               Thread.sleep(2147483647L);
/*      */             }
/*  577 */             catch (InterruptedException interruptedException) {}
/*      */           } 
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */     
/*  584 */     thread.setDaemon(true);
/*  585 */     thread.start();
/*      */   }
/*      */ 
/*      */   
/*      */   public void crashed(CrashReport crash) {
/*  590 */     this.hasCrashed = true;
/*  591 */     this.crashReporter = crash;
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayCrashReport(CrashReport crashReportIn) {
/*  596 */     File file1 = new File((getMinecraft()).mcDataDir, "crash-reports");
/*  597 */     File file2 = new File(file1, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
/*  598 */     Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());
/*      */     
/*  600 */     if (crashReportIn.getFile() != null) {
/*      */       
/*  602 */       Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
/*  603 */       System.exit(-1);
/*      */     }
/*  605 */     else if (crashReportIn.saveToFile(file2)) {
/*      */       
/*  607 */       Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
/*  608 */       System.exit(-1);
/*      */     }
/*      */     else {
/*      */       
/*  612 */       Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
/*  613 */       System.exit(-2);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUnicode() {
/*  619 */     return (this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont);
/*      */   }
/*      */ 
/*      */   
/*      */   public void refreshResources() {
/*  624 */     List<IResourcePack> list = Lists.newArrayList(this.defaultResourcePacks);
/*      */     
/*  626 */     for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries())
/*      */     {
/*  628 */       list.add(resourcepackrepository$entry.getResourcePack());
/*      */     }
/*      */     
/*  631 */     if (this.mcResourcePackRepository.getResourcePackInstance() != null)
/*      */     {
/*  633 */       list.add(this.mcResourcePackRepository.getResourcePackInstance());
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  638 */       this.mcResourceManager.reloadResources(list);
/*      */     }
/*  640 */     catch (RuntimeException runtimeexception) {
/*      */       
/*  642 */       logger.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
/*  643 */       list.clear();
/*  644 */       list.addAll(this.defaultResourcePacks);
/*  645 */       this.mcResourcePackRepository.setRepositories(Collections.emptyList());
/*  646 */       this.mcResourceManager.reloadResources(list);
/*  647 */       this.gameSettings.resourcePacks.clear();
/*  648 */       this.gameSettings.incompatibleResourcePacks.clear();
/*  649 */       this.gameSettings.saveOptions();
/*      */     } 
/*      */     
/*  652 */     this.mcLanguageManager.parseLanguageMetadata(list);
/*      */     
/*  654 */     if (this.renderGlobal != null)
/*      */     {
/*  656 */       this.renderGlobal.loadRenderers();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
/*  662 */     BufferedImage bufferedimage = ImageIO.read(imageStream);
/*  663 */     int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
/*  664 */     ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
/*      */     
/*  666 */     for (int i : aint)
/*      */     {
/*  668 */       bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
/*      */     }
/*      */     
/*  671 */     bytebuffer.flip();
/*  672 */     return bytebuffer;
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateDisplayMode() throws LWJGLException {
/*  677 */     Set<DisplayMode> set = Sets.newHashSet();
/*  678 */     Collections.addAll(set, Display.getAvailableDisplayModes());
/*  679 */     DisplayMode displaymode = Display.getDesktopDisplayMode();
/*      */     
/*  681 */     if (!set.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX)
/*      */     {
/*      */ 
/*      */       
/*  685 */       for (DisplayMode displaymode1 : macDisplayModes) {
/*      */         
/*  687 */         boolean flag = true;
/*      */         
/*  689 */         for (DisplayMode displaymode2 : set) {
/*      */           
/*  691 */           if (displaymode2.getBitsPerPixel() == 32 && displaymode2.getWidth() == displaymode1.getWidth() && displaymode2.getHeight() == displaymode1.getHeight()) {
/*      */             
/*  693 */             flag = false;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*  698 */         if (!flag) {
/*      */           
/*  700 */           Iterator<DisplayMode> iterator = set.iterator();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  705 */           while (iterator.hasNext()) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  710 */             DisplayMode displaymode3 = iterator.next();
/*      */             
/*  712 */             if (displaymode3.getBitsPerPixel() == 32 && displaymode3.getWidth() == displaymode1.getWidth() / 2 && displaymode3.getHeight() == displaymode1.getHeight() / 2)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  718 */               displaymode = displaymode3; } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*  723 */     Display.setDisplayMode(displaymode);
/*  724 */     this.displayWidth = displaymode.getWidth();
/*  725 */     this.displayHeight = displaymode.getHeight();
/*      */   }
/*      */ 
/*      */   
/*      */   private void drawSplashScreen(TextureManager textureManagerInstance) throws LWJGLException {
/*  730 */     ScaledResolution scaledresolution = new ScaledResolution(this);
/*  731 */     int i = scaledresolution.getScaleFactor();
/*  732 */     Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i, true);
/*  733 */     framebuffer.bindFramebuffer(false);
/*  734 */     GlStateManager.matrixMode(5889);
/*  735 */     GlStateManager.loadIdentity();
/*  736 */     GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
/*  737 */     GlStateManager.matrixMode(5888);
/*  738 */     GlStateManager.loadIdentity();
/*  739 */     GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/*  740 */     GlStateManager.disableLighting();
/*  741 */     GlStateManager.disableFog();
/*  742 */     GlStateManager.disableDepth();
/*  743 */     GlStateManager.enableTexture2D();
/*  744 */     InputStream inputstream = null;
/*      */ 
/*      */     
/*      */     try {
/*  748 */       inputstream = this.mcDefaultResourcePack.getInputStream(locationMojangPng);
/*  749 */       this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream)));
/*  750 */       textureManagerInstance.bindTexture(this.mojangLogo);
/*      */     }
/*  752 */     catch (IOException ioexception) {
/*      */       
/*  754 */       logger.error("Unable to load logo: " + locationMojangPng, ioexception);
/*      */     }
/*      */     finally {
/*      */       
/*  758 */       IOUtils.closeQuietly(inputstream);
/*      */     } 
/*      */     
/*  761 */     Tessellator tessellator = Tessellator.getInstance();
/*  762 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*  763 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*  764 */     worldrenderer.pos(0.0D, this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/*  765 */     worldrenderer.pos(this.displayWidth, this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/*  766 */     worldrenderer.pos(this.displayWidth, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/*  767 */     worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/*  768 */     tessellator.draw();
/*  769 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  770 */     int j = 256;
/*  771 */     int k = 256;
/*  772 */     draw((scaledresolution.getScaledWidth() - j) / 2, (scaledresolution.getScaledHeight() - k) / 2, 0, 0, j, k, 255, 255, 255, 255);
/*  773 */     GlStateManager.disableLighting();
/*  774 */     GlStateManager.disableFog();
/*  775 */     framebuffer.unbindFramebuffer();
/*  776 */     framebuffer.framebufferRender(scaledresolution.getScaledWidth() * i, scaledresolution.getScaledHeight() * i);
/*  777 */     GlStateManager.enableAlpha();
/*  778 */     GlStateManager.alphaFunc(516, 0.1F);
/*  779 */     updateDisplay();
/*      */   }
/*      */ 
/*      */   
/*      */   public void draw(int posX, int posY, int texU, int texV, int width, int height, int red, int green, int blue, int alpha) {
/*  784 */     float f = 0.00390625F;
/*  785 */     float f1 = 0.00390625F;
/*  786 */     WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
/*  787 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*  788 */     worldrenderer.pos(posX, (posY + height), 0.0D).tex((texU * f), ((texV + height) * f1)).color(red, green, blue, alpha).endVertex();
/*  789 */     worldrenderer.pos((posX + width), (posY + height), 0.0D).tex(((texU + width) * f), ((texV + height) * f1)).color(red, green, blue, alpha).endVertex();
/*  790 */     worldrenderer.pos((posX + width), posY, 0.0D).tex(((texU + width) * f), (texV * f1)).color(red, green, blue, alpha).endVertex();
/*  791 */     worldrenderer.pos(posX, posY, 0.0D).tex((texU * f), (texV * f1)).color(red, green, blue, alpha).endVertex();
/*  792 */     Tessellator.getInstance().draw();
/*      */   }
/*      */ 
/*      */   
/*      */   public ISaveFormat getSaveLoader() {
/*  797 */     return this.saveLoader;
/*      */   }
/*      */   public void displayGuiScreen(GuiScreen guiScreenIn) {
/*      */     GuiMainMenu guiMainMenu;
/*      */     GuiGameOver guiGameOver;
/*  802 */     if (this.currentScreen != null)
/*      */     {
/*  804 */       this.currentScreen.onGuiClosed();
/*      */     }
/*      */     
/*  807 */     if (guiScreenIn == null && this.theWorld == null) {
/*      */       
/*  809 */       guiMainMenu = new GuiMainMenu();
/*      */     }
/*  811 */     else if (guiMainMenu == null && this.thePlayer.getHealth() <= 0.0F) {
/*      */       
/*  813 */       guiGameOver = new GuiGameOver();
/*      */     } 
/*      */     
/*  816 */     if (guiGameOver instanceof GuiMainMenu) {
/*      */       
/*  818 */       this.gameSettings.showDebugInfo = false;
/*  819 */       this.ingameGUI.getChatGUI().clearChatMessages();
/*      */     } 
/*      */     
/*  822 */     this.currentScreen = (GuiScreen)guiGameOver;
/*      */     
/*  824 */     if (guiGameOver != null) {
/*      */       
/*  826 */       setIngameNotInFocus();
/*  827 */       ScaledResolution scaledresolution = new ScaledResolution(this);
/*  828 */       int i = scaledresolution.getScaledWidth();
/*  829 */       int j = scaledresolution.getScaledHeight();
/*  830 */       guiGameOver.setWorldAndResolution(this, i, j);
/*  831 */       this.skipRenderWorld = false;
/*      */     }
/*      */     else {
/*      */       
/*  835 */       this.mcSoundHandler.resumeSounds();
/*  836 */       setIngameFocus();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkGLError(String message) {
/*  842 */     if (this.enableGLErrorChecking) {
/*      */       
/*  844 */       int i = GL11.glGetError();
/*      */       
/*  846 */       if (i != 0) {
/*      */         
/*  848 */         String s = GLU.gluErrorString(i);
/*  849 */         logger.error("########## GL ERROR ##########");
/*  850 */         logger.error("@ " + message);
/*  851 */         logger.error(i + ": " + s);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void shutdownMinecraftApplet() {
/*      */     try {
/*  860 */       logger.info("Stopping!");
/*      */ 
/*      */       
/*      */       try {
/*  864 */         loadWorld((WorldClient)null);
/*      */       }
/*  866 */       catch (Throwable throwable) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  871 */       this.mcSoundHandler.unloadSounds();
/*      */     }
/*      */     finally {
/*      */       
/*  875 */       Display.destroy();
/*      */       
/*  877 */       if (!this.hasCrashed)
/*      */       {
/*  879 */         System.exit(0);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void runGameLoop() throws IOException {
/*  887 */     long i = System.nanoTime();
/*  888 */     this.mcProfiler.startSection("root");
/*      */     
/*  890 */     if (Display.isCreated() && Display.isCloseRequested())
/*      */     {
/*  892 */       shutdown();
/*      */     }
/*      */     
/*  895 */     if (this.isGamePaused && this.theWorld != null) {
/*      */       
/*  897 */       float f = this.timer.renderPartialTicks;
/*  898 */       this.timer.updateTimer();
/*  899 */       this.timer.renderPartialTicks = f;
/*      */     }
/*      */     else {
/*      */       
/*  903 */       this.timer.updateTimer();
/*      */     } 
/*      */     
/*  906 */     this.mcProfiler.startSection("scheduledExecutables");
/*      */     
/*  908 */     synchronized (this.scheduledTasks) {
/*      */       
/*  910 */       while (!this.scheduledTasks.isEmpty())
/*      */       {
/*  912 */         Util.runTask(this.scheduledTasks.poll(), logger);
/*      */       }
/*      */     } 
/*      */     
/*  916 */     this.mcProfiler.endSection();
/*  917 */     long l = System.nanoTime();
/*  918 */     this.mcProfiler.startSection("tick");
/*      */     
/*  920 */     for (int j = 0; j < this.timer.elapsedTicks; j++)
/*      */     {
/*  922 */       runTick();
/*      */     }
/*      */     
/*  925 */     this.mcProfiler.endStartSection("preRenderErrors");
/*  926 */     long i1 = System.nanoTime() - l;
/*  927 */     checkGLError("Pre render");
/*  928 */     this.mcProfiler.endStartSection("sound");
/*  929 */     this.mcSoundHandler.setListener((EntityPlayer)this.thePlayer, this.timer.renderPartialTicks);
/*  930 */     this.mcProfiler.endSection();
/*  931 */     this.mcProfiler.startSection("render");
/*  932 */     GlStateManager.pushMatrix();
/*  933 */     GlStateManager.clear(16640);
/*  934 */     this.framebufferMc.bindFramebuffer(true);
/*  935 */     this.mcProfiler.startSection("display");
/*  936 */     GlStateManager.enableTexture2D();
/*      */     
/*  938 */     if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock())
/*      */     {
/*  940 */       this.gameSettings.thirdPersonView = 0;
/*      */     }
/*      */     
/*  943 */     this.mcProfiler.endSection();
/*      */     
/*  945 */     if (!this.skipRenderWorld) {
/*      */       
/*  947 */       this.mcProfiler.endStartSection("gameRenderer");
/*  948 */       this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks, i);
/*  949 */       this.mcProfiler.endSection();
/*      */     } 
/*      */     
/*  952 */     this.mcProfiler.endSection();
/*      */     
/*  954 */     if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
/*      */       
/*  956 */       if (!this.mcProfiler.profilingEnabled)
/*      */       {
/*  958 */         this.mcProfiler.clearProfiling();
/*      */       }
/*      */       
/*  961 */       this.mcProfiler.profilingEnabled = true;
/*  962 */       displayDebugInfo(i1);
/*      */     }
/*      */     else {
/*      */       
/*  966 */       this.mcProfiler.profilingEnabled = false;
/*  967 */       this.prevFrameTime = System.nanoTime();
/*      */     } 
/*      */     
/*  970 */     this.framebufferMc.unbindFramebuffer();
/*  971 */     GlStateManager.popMatrix();
/*  972 */     GlStateManager.pushMatrix();
/*  973 */     this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
/*  974 */     GlStateManager.popMatrix();
/*  975 */     this.mcProfiler.startSection("root");
/*  976 */     updateDisplay();
/*  977 */     Thread.yield();
/*  978 */     checkGLError("Post render");
/*  979 */     this.fpsCounter++;
/*  980 */     this.isGamePaused = (isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic());
/*  981 */     long k = System.nanoTime();
/*  982 */     this.frameTimer.addFrame(k - this.startNanoTime);
/*  983 */     this.startNanoTime = k;
/*      */     
/*  985 */     while (getSystemTime() >= this.debugUpdateTime + 1000L) {
/*      */       
/*  987 */       debugFPS = this.fpsCounter;
/*  988 */       this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", new Object[] { Integer.valueOf(debugFPS), Integer.valueOf(RenderChunk.renderChunksUpdated), (RenderChunk.renderChunksUpdated != 1) ? "s" : "", (this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate), this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", (this.gameSettings.clouds == 0) ? "" : ((this.gameSettings.clouds == 1) ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "" });
/*  989 */       RenderChunk.renderChunksUpdated = 0;
/*  990 */       this.debugUpdateTime += 1000L;
/*  991 */       this.fpsCounter = 0;
/*  992 */       this.usageSnooper.addMemoryStatsToSnooper();
/*      */       
/*  994 */       if (!this.usageSnooper.isSnooperRunning())
/*      */       {
/*  996 */         this.usageSnooper.startSnooper();
/*      */       }
/*      */     } 
/*      */     
/* 1000 */     if (isFramerateLimitBelowMax()) {
/*      */       
/* 1002 */       this.mcProfiler.startSection("fpslimit_wait");
/* 1003 */       Display.sync(getLimitFramerate());
/* 1004 */       this.mcProfiler.endSection();
/*      */     } 
/*      */     
/* 1007 */     this.mcProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDisplay() {
/* 1012 */     this.mcProfiler.startSection("display_update");
/* 1013 */     Display.update();
/* 1014 */     this.mcProfiler.endSection();
/* 1015 */     checkWindowResize();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkWindowResize() {
/* 1020 */     if (!this.fullscreen && Display.wasResized()) {
/*      */       
/* 1022 */       int i = this.displayWidth;
/* 1023 */       int j = this.displayHeight;
/* 1024 */       this.displayWidth = Display.getWidth();
/* 1025 */       this.displayHeight = Display.getHeight();
/*      */       
/* 1027 */       if (this.displayWidth != i || this.displayHeight != j) {
/*      */         
/* 1029 */         if (this.displayWidth <= 0)
/*      */         {
/* 1031 */           this.displayWidth = 1;
/*      */         }
/*      */         
/* 1034 */         if (this.displayHeight <= 0)
/*      */         {
/* 1036 */           this.displayHeight = 1;
/*      */         }
/*      */         
/* 1039 */         resize(this.displayWidth, this.displayHeight);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLimitFramerate() {
/* 1046 */     return (this.theWorld == null && this.currentScreen != null) ? 30 : this.gameSettings.limitFramerate;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFramerateLimitBelowMax() {
/* 1051 */     return (getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void freeMemory() {
/*      */     try {
/* 1058 */       memoryReserve = new byte[0];
/* 1059 */       this.renderGlobal.deleteAllDisplayLists();
/*      */     }
/* 1061 */     catch (Throwable throwable) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1068 */       loadWorld((WorldClient)null);
/*      */     }
/* 1070 */     catch (Throwable throwable) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateDebugProfilerName(int keyCount) {
/* 1079 */     List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
/*      */     
/* 1081 */     if (list != null && !list.isEmpty()) {
/*      */       
/* 1083 */       Profiler.Result profiler$result = list.remove(0);
/*      */       
/* 1085 */       if (keyCount == 0) {
/*      */         
/* 1087 */         if (profiler$result.field_76331_c.length() > 0)
/*      */         {
/* 1089 */           int i = this.debugProfilerName.lastIndexOf(".");
/*      */           
/* 1091 */           if (i >= 0)
/*      */           {
/* 1093 */             this.debugProfilerName = this.debugProfilerName.substring(0, i);
/*      */           }
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1099 */         keyCount--;
/*      */         
/* 1101 */         if (keyCount < list.size() && !((Profiler.Result)list.get(keyCount)).field_76331_c.equals("unspecified")) {
/*      */           
/* 1103 */           if (this.debugProfilerName.length() > 0)
/*      */           {
/* 1105 */             this.debugProfilerName += ".";
/*      */           }
/*      */           
/* 1108 */           this.debugProfilerName += ((Profiler.Result)list.get(keyCount)).field_76331_c;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void displayDebugInfo(long elapsedTicksTime) {
/* 1116 */     if (this.mcProfiler.profilingEnabled) {
/*      */       
/* 1118 */       List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
/* 1119 */       Profiler.Result profiler$result = list.remove(0);
/* 1120 */       GlStateManager.clear(256);
/* 1121 */       GlStateManager.matrixMode(5889);
/* 1122 */       GlStateManager.enableColorMaterial();
/* 1123 */       GlStateManager.loadIdentity();
/* 1124 */       GlStateManager.ortho(0.0D, this.displayWidth, this.displayHeight, 0.0D, 1000.0D, 3000.0D);
/* 1125 */       GlStateManager.matrixMode(5888);
/* 1126 */       GlStateManager.loadIdentity();
/* 1127 */       GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/* 1128 */       GL11.glLineWidth(1.0F);
/* 1129 */       GlStateManager.disableTexture2D();
/* 1130 */       Tessellator tessellator = Tessellator.getInstance();
/* 1131 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1132 */       int i = 160;
/* 1133 */       int j = this.displayWidth - i - 10;
/* 1134 */       int k = this.displayHeight - i * 2;
/* 1135 */       GlStateManager.enableBlend();
/* 1136 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 1137 */       worldrenderer.pos((j - i * 1.1F), (k - i * 0.6F - 16.0F), 0.0D).color(200, 0, 0, 0).endVertex();
/* 1138 */       worldrenderer.pos((j - i * 1.1F), (k + i * 2), 0.0D).color(200, 0, 0, 0).endVertex();
/* 1139 */       worldrenderer.pos((j + i * 1.1F), (k + i * 2), 0.0D).color(200, 0, 0, 0).endVertex();
/* 1140 */       worldrenderer.pos((j + i * 1.1F), (k - i * 0.6F - 16.0F), 0.0D).color(200, 0, 0, 0).endVertex();
/* 1141 */       tessellator.draw();
/* 1142 */       GlStateManager.disableBlend();
/* 1143 */       double d0 = 0.0D;
/*      */       
/* 1145 */       for (int l = 0; l < list.size(); l++) {
/*      */         
/* 1147 */         Profiler.Result profiler$result1 = list.get(l);
/* 1148 */         int i1 = MathHelper.floor_double(profiler$result1.field_76332_a / 4.0D) + 1;
/* 1149 */         worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
/* 1150 */         int j1 = profiler$result1.getColor();
/* 1151 */         int k1 = j1 >> 16 & 0xFF;
/* 1152 */         int l1 = j1 >> 8 & 0xFF;
/* 1153 */         int i2 = j1 & 0xFF;
/* 1154 */         worldrenderer.pos(j, k, 0.0D).color(k1, l1, i2, 255).endVertex();
/*      */         
/* 1156 */         for (int j2 = i1; j2 >= 0; j2--) {
/*      */           
/* 1158 */           float f = (float)((d0 + profiler$result1.field_76332_a * j2 / i1) * Math.PI * 2.0D / 100.0D);
/* 1159 */           float f1 = MathHelper.sin(f) * i;
/* 1160 */           float f2 = MathHelper.cos(f) * i * 0.5F;
/* 1161 */           worldrenderer.pos((j + f1), (k - f2), 0.0D).color(k1, l1, i2, 255).endVertex();
/*      */         } 
/*      */         
/* 1164 */         tessellator.draw();
/* 1165 */         worldrenderer.begin(5, DefaultVertexFormats.POSITION_COLOR);
/*      */         
/* 1167 */         for (int i3 = i1; i3 >= 0; i3--) {
/*      */           
/* 1169 */           float f3 = (float)((d0 + profiler$result1.field_76332_a * i3 / i1) * Math.PI * 2.0D / 100.0D);
/* 1170 */           float f4 = MathHelper.sin(f3) * i;
/* 1171 */           float f5 = MathHelper.cos(f3) * i * 0.5F;
/* 1172 */           worldrenderer.pos((j + f4), (k - f5), 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
/* 1173 */           worldrenderer.pos((j + f4), (k - f5 + 10.0F), 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
/*      */         } 
/*      */         
/* 1176 */         tessellator.draw();
/* 1177 */         d0 += profiler$result1.field_76332_a;
/*      */       } 
/*      */       
/* 1180 */       DecimalFormat decimalformat = new DecimalFormat("##0.00");
/* 1181 */       GlStateManager.enableTexture2D();
/* 1182 */       String s = "";
/*      */       
/* 1184 */       if (!profiler$result.field_76331_c.equals("unspecified"))
/*      */       {
/* 1186 */         s = s + "[0] ";
/*      */       }
/*      */       
/* 1189 */       if (profiler$result.field_76331_c.length() == 0) {
/*      */         
/* 1191 */         s = s + "ROOT ";
/*      */       }
/*      */       else {
/*      */         
/* 1195 */         s = s + profiler$result.field_76331_c + " ";
/*      */       } 
/*      */       
/* 1198 */       int l2 = 16777215;
/* 1199 */       this.fontRendererObj.drawStringWithShadow(s, (j - i), (k - i / 2 - 16), l2);
/* 1200 */       this.fontRendererObj.drawStringWithShadow(s = decimalformat.format(profiler$result.field_76330_b) + "%", (j + i - this.fontRendererObj.getStringWidth(s)), (k - i / 2 - 16), l2);
/*      */       
/* 1202 */       for (int k2 = 0; k2 < list.size(); k2++) {
/*      */         
/* 1204 */         Profiler.Result profiler$result2 = list.get(k2);
/* 1205 */         String s1 = "";
/*      */         
/* 1207 */         if (profiler$result2.field_76331_c.equals("unspecified")) {
/*      */           
/* 1209 */           s1 = s1 + "[?] ";
/*      */         }
/*      */         else {
/*      */           
/* 1213 */           s1 = s1 + "[" + (k2 + 1) + "] ";
/*      */         } 
/*      */         
/* 1216 */         s1 = s1 + profiler$result2.field_76331_c;
/* 1217 */         this.fontRendererObj.drawStringWithShadow(s1, (j - i), (k + i / 2 + k2 * 8 + 20), profiler$result2.getColor());
/* 1218 */         this.fontRendererObj.drawStringWithShadow(s1 = decimalformat.format(profiler$result2.field_76332_a) + "%", (j + i - 50 - this.fontRendererObj.getStringWidth(s1)), (k + i / 2 + k2 * 8 + 20), profiler$result2.getColor());
/* 1219 */         this.fontRendererObj.drawStringWithShadow(s1 = decimalformat.format(profiler$result2.field_76330_b) + "%", (j + i - this.fontRendererObj.getStringWidth(s1)), (k + i / 2 + k2 * 8 + 20), profiler$result2.getColor());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void shutdown() {
/* 1226 */     this.running = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIngameFocus() {
/* 1231 */     if (Display.isActive())
/*      */     {
/* 1233 */       if (!this.inGameHasFocus) {
/*      */         
/* 1235 */         this.inGameHasFocus = true;
/* 1236 */         this.mouseHelper.grabMouseCursor();
/* 1237 */         displayGuiScreen((GuiScreen)null);
/* 1238 */         this.leftClickCounter = 10000;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIngameNotInFocus() {
/* 1245 */     if (this.inGameHasFocus) {
/*      */       
/* 1247 */       KeyBinding.unPressAllKeys();
/* 1248 */       this.inGameHasFocus = false;
/* 1249 */       this.mouseHelper.ungrabMouseCursor();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void displayInGameMenu() {
/* 1255 */     if (this.currentScreen == null) {
/*      */       
/* 1257 */       displayGuiScreen((GuiScreen)new GuiIngameMenu());
/*      */       
/* 1259 */       if (isSingleplayer() && !this.theIntegratedServer.getPublic())
/*      */       {
/* 1261 */         this.mcSoundHandler.pauseSounds();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendClickBlockToController(boolean leftClick) {
/* 1268 */     if (!leftClick)
/*      */     {
/* 1270 */       this.leftClickCounter = 0;
/*      */     }
/*      */     
/* 1273 */     if (this.leftClickCounter <= 0 && !this.thePlayer.isUsingItem())
/*      */     {
/* 1275 */       if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*      */         
/* 1277 */         BlockPos blockpos = this.objectMouseOver.getBlockPos();
/*      */         
/* 1279 */         if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air && this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit))
/*      */         {
/* 1281 */           this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
/* 1282 */           this.thePlayer.swingItem();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1287 */         this.playerController.resetBlockRemoving();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void clickMouse() {
/* 1294 */     if (this.leftClickCounter <= 0) {
/*      */       
/* 1296 */       AttackOrder.sendConditionalSwing(this.objectMouseOver);
/* 1297 */       this.thePlayer.swingItem();
/*      */       
/* 1299 */       if (this.objectMouseOver == null) {
/*      */         
/* 1301 */         logger.error("Null returned as 'hitResult', this shouldn't happen!");
/*      */         
/* 1303 */         if (this.playerController.isNotCreative())
/*      */         {
/* 1305 */           this.leftClickCounter = 10;
/*      */         }
/*      */       } else {
/*      */         BlockPos blockpos;
/*      */         
/* 1310 */         switch (this.objectMouseOver.typeOfHit) {
/*      */ 
/*      */           
/*      */           case FURNACE:
/* 1314 */             AttackOrder.sendFixedAttack((EntityPlayer)this.thePlayer, this.objectMouseOver.entityHit);
/*      */             return;
/*      */           
/*      */           case CHEST:
/* 1318 */             blockpos = this.objectMouseOver.getBlockPos();
/*      */             
/* 1320 */             if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
/*      */               
/* 1322 */               this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
/*      */               return;
/*      */             } 
/*      */             break;
/*      */         } 
/*      */         
/* 1328 */         if (this.playerController.isNotCreative())
/*      */         {
/* 1330 */           this.leftClickCounter = 10;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void rightClickMouse() {
/* 1340 */     if (!this.playerController.getIsHittingBlock()) {
/*      */       
/* 1342 */       this.rightClickDelayTimer = 4;
/* 1343 */       boolean flag = true;
/* 1344 */       ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();
/*      */       
/* 1346 */       if (this.objectMouseOver == null) {
/*      */         
/* 1348 */         logger.warn("Null returned as 'hitResult', this shouldn't happen!");
/*      */       } else {
/*      */         BlockPos blockpos;
/*      */         
/* 1352 */         switch (this.objectMouseOver.typeOfHit) {
/*      */           
/*      */           case FURNACE:
/* 1355 */             if (this.playerController.isPlayerRightClickingOnEntity((EntityPlayer)this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver)) {
/*      */               
/* 1357 */               flag = false; break;
/*      */             } 
/* 1359 */             if (this.playerController.interactWithEntitySendPacket((EntityPlayer)this.thePlayer, this.objectMouseOver.entityHit))
/*      */             {
/* 1361 */               flag = false;
/*      */             }
/*      */             break;
/*      */ 
/*      */           
/*      */           case CHEST:
/* 1367 */             blockpos = this.objectMouseOver.getBlockPos();
/*      */             
/* 1369 */             if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air) {
/*      */               
/* 1371 */               int i = (itemstack != null) ? itemstack.stackSize : 0;
/*      */               
/* 1373 */               if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec)) {
/*      */                 
/* 1375 */                 flag = false;
/* 1376 */                 this.thePlayer.swingItem();
/*      */               } 
/*      */               
/* 1379 */               if (itemstack == null) {
/*      */                 return;
/*      */               }
/*      */ 
/*      */               
/* 1384 */               if (itemstack.stackSize == 0) {
/*      */                 
/* 1386 */                 this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null; break;
/*      */               } 
/* 1388 */               if (itemstack.stackSize != i || this.playerController.isInCreativeMode())
/*      */               {
/* 1390 */                 this.entityRenderer.itemRenderer.resetEquippedProgress();
/*      */               }
/*      */             } 
/*      */             break;
/*      */         } 
/*      */       } 
/* 1396 */       if (flag) {
/*      */         
/* 1398 */         ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();
/*      */         
/* 1400 */         if (itemstack1 != null && this.playerController.sendUseItem((EntityPlayer)this.thePlayer, (World)this.theWorld, itemstack1))
/*      */         {
/* 1402 */           this.entityRenderer.itemRenderer.resetEquippedProgress2();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toggleFullscreen() {
/*      */     try {
/* 1412 */       this.fullscreen = !this.fullscreen;
/* 1413 */       this.gameSettings.fullScreen = this.fullscreen;
/*      */       
/* 1415 */       if (this.fullscreen) {
/*      */         
/* 1417 */         updateDisplayMode();
/* 1418 */         this.displayWidth = Display.getDisplayMode().getWidth();
/* 1419 */         this.displayHeight = Display.getDisplayMode().getHeight();
/*      */         
/* 1421 */         if (this.displayWidth <= 0)
/*      */         {
/* 1423 */           this.displayWidth = 1;
/*      */         }
/*      */         
/* 1426 */         if (this.displayHeight <= 0)
/*      */         {
/* 1428 */           this.displayHeight = 1;
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1433 */         Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
/* 1434 */         this.displayWidth = this.tempDisplayWidth;
/* 1435 */         this.displayHeight = this.tempDisplayHeight;
/*      */         
/* 1437 */         if (this.displayWidth <= 0)
/*      */         {
/* 1439 */           this.displayWidth = 1;
/*      */         }
/*      */         
/* 1442 */         if (this.displayHeight <= 0)
/*      */         {
/* 1444 */           this.displayHeight = 1;
/*      */         }
/*      */       } 
/*      */       
/* 1448 */       if (this.currentScreen != null) {
/*      */         
/* 1450 */         resize(this.displayWidth, this.displayHeight);
/*      */       }
/*      */       else {
/*      */         
/* 1454 */         updateFramebufferSize();
/*      */       } 
/*      */       
/* 1457 */       Display.setFullscreen(this.fullscreen);
/* 1458 */       Display.setVSyncEnabled(this.gameSettings.enableVsync);
/* 1459 */       updateDisplay();
/*      */     }
/* 1461 */     catch (Exception exception) {
/*      */       
/* 1463 */       logger.error("Couldn't toggle fullscreen", exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void resize(int width, int height) {
/* 1469 */     this.displayWidth = Math.max(1, width);
/* 1470 */     this.displayHeight = Math.max(1, height);
/*      */     
/* 1472 */     if (this.currentScreen != null) {
/*      */       
/* 1474 */       ScaledResolution scaledresolution = new ScaledResolution(this);
/* 1475 */       this.currentScreen.onResize(this, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
/*      */     } 
/*      */     
/* 1478 */     this.loadingScreen = new LoadingScreenRenderer(this);
/* 1479 */     updateFramebufferSize();
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateFramebufferSize() {
/* 1484 */     this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
/*      */     
/* 1486 */     if (this.entityRenderer != null)
/*      */     {
/* 1488 */       this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public MusicTicker getMusicTicker() {
/* 1494 */     return this.mcMusicTicker;
/*      */   }
/*      */ 
/*      */   
/*      */   public void runTick() throws IOException {
/* 1499 */     if (this.rightClickDelayTimer > 0)
/*      */     {
/* 1501 */       this.rightClickDelayTimer--;
/*      */     }
/*      */     
/* 1504 */     this.mcProfiler.startSection("gui");
/*      */     
/* 1506 */     if (!this.isGamePaused)
/*      */     {
/* 1508 */       this.ingameGUI.updateTick();
/*      */     }
/*      */     
/* 1511 */     this.mcProfiler.endSection();
/* 1512 */     this.entityRenderer.getMouseOver(1.0F);
/* 1513 */     this.mcProfiler.startSection("gameMode");
/*      */     
/* 1515 */     if (!this.isGamePaused && this.theWorld != null)
/*      */     {
/* 1517 */       this.playerController.updateController();
/*      */     }
/*      */     
/* 1520 */     this.mcProfiler.endStartSection("textures");
/*      */     
/* 1522 */     if (!this.isGamePaused)
/*      */     {
/* 1524 */       this.renderEngine.tick();
/*      */     }
/*      */     
/* 1527 */     if (this.currentScreen == null && this.thePlayer != null) {
/*      */       
/* 1529 */       if (this.thePlayer.getHealth() <= 0.0F)
/*      */       {
/* 1531 */         displayGuiScreen((GuiScreen)null);
/*      */       }
/* 1533 */       else if (this.thePlayer.isPlayerSleeping() && this.theWorld != null)
/*      */       {
/* 1535 */         displayGuiScreen((GuiScreen)new GuiSleepMP());
/*      */       }
/*      */     
/* 1538 */     } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
/*      */       
/* 1540 */       displayGuiScreen((GuiScreen)null);
/*      */     } 
/*      */     
/* 1543 */     if (this.currentScreen != null)
/*      */     {
/* 1545 */       this.leftClickCounter = 10000;
/*      */     }
/*      */     
/* 1548 */     if (this.currentScreen != null) {
/*      */ 
/*      */       
/*      */       try {
/* 1552 */         this.currentScreen.handleInput();
/*      */       }
/* 1554 */       catch (Throwable throwable1) {
/*      */         
/* 1556 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
/* 1557 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
/* 1558 */         crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>()
/*      */             {
/*      */               public String call() throws Exception
/*      */               {
/* 1562 */                 return Minecraft.this.currentScreen.getClass().getCanonicalName();
/*      */               }
/*      */             });
/* 1565 */         throw new ReportedException(crashreport);
/*      */       } 
/*      */       
/* 1568 */       if (this.currentScreen != null) {
/*      */         
/*      */         try {
/*      */           
/* 1572 */           this.currentScreen.updateScreen();
/*      */         }
/* 1574 */         catch (Throwable throwable) {
/*      */           
/* 1576 */           CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Ticking screen");
/* 1577 */           CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
/* 1578 */           crashreportcategory1.addCrashSectionCallable("Screen name", new Callable<String>()
/*      */               {
/*      */                 public String call() throws Exception
/*      */                 {
/* 1582 */                   return Minecraft.this.currentScreen.getClass().getCanonicalName();
/*      */                 }
/*      */               });
/* 1585 */           throw new ReportedException(crashreport1);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1590 */     if (this.currentScreen == null || this.currentScreen.allowUserInput) {
/*      */       
/* 1592 */       this.mcProfiler.endStartSection("mouse");
/*      */       
/* 1594 */       while (Mouse.next()) {
/*      */         
/* 1596 */         int i = Mouse.getEventButton();
/* 1597 */         KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
/*      */         
/* 1599 */         if (Mouse.getEventButtonState())
/*      */         {
/* 1601 */           if (this.thePlayer.isSpectator() && i == 2) {
/*      */             
/* 1603 */             this.ingameGUI.getSpectatorGui().func_175261_b();
/*      */           }
/*      */           else {
/*      */             
/* 1607 */             KeyBinding.onTick(i - 100);
/*      */           } 
/*      */         }
/*      */         
/* 1611 */         long i1 = getSystemTime() - this.systemTime;
/*      */         
/* 1613 */         if (i1 <= 200L) {
/*      */           
/* 1615 */           int j = Mouse.getEventDWheel();
/*      */           
/* 1617 */           if (j != 0)
/*      */           {
/* 1619 */             if (this.thePlayer.isSpectator()) {
/*      */               
/* 1621 */               j = (j < 0) ? -1 : 1;
/*      */               
/* 1623 */               if (this.ingameGUI.getSpectatorGui().func_175262_a())
/*      */               {
/* 1625 */                 this.ingameGUI.getSpectatorGui().func_175259_b(-j);
/*      */               }
/*      */               else
/*      */               {
/* 1629 */                 float f = MathHelper.clamp_float(this.thePlayer.capabilities.getFlySpeed() + j * 0.005F, 0.0F, 0.2F);
/* 1630 */                 this.thePlayer.capabilities.setFlySpeed(f);
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 1635 */               this.thePlayer.inventory.changeCurrentItem(j);
/*      */             } 
/*      */           }
/*      */           
/* 1639 */           if (this.currentScreen == null) {
/*      */             
/* 1641 */             if (!this.inGameHasFocus && Mouse.getEventButtonState())
/*      */             {
/* 1643 */               setIngameFocus(); } 
/*      */             continue;
/*      */           } 
/* 1646 */           if (this.currentScreen != null)
/*      */           {
/* 1648 */             this.currentScreen.handleMouseInput();
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1653 */       if (this.leftClickCounter > 0)
/*      */       {
/* 1655 */         this.leftClickCounter--;
/*      */       }
/*      */       
/* 1658 */       this.mcProfiler.endStartSection("keyboard");
/*      */       
/* 1660 */       while (Keyboard.next()) {
/*      */         
/* 1662 */         int k = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + 256) : Keyboard.getEventKey();
/* 1663 */         KeyBinding.setKeyBindState(k, Keyboard.getEventKeyState());
/*      */         
/* 1665 */         if (Keyboard.getEventKeyState()) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1670 */           KeyEvent event = new KeyEvent(k);
/* 1671 */           Client.getInstance().getEventBus().post(event);
/*      */           
/* 1673 */           KeyBinding.onTick(k);
/*      */         } 
/*      */         
/* 1676 */         if (this.debugCrashKeyPressTime > 0L) {
/*      */           
/* 1678 */           if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L)
/*      */           {
/* 1680 */             throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
/*      */           }
/*      */           
/* 1683 */           if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61))
/*      */           {
/* 1685 */             this.debugCrashKeyPressTime = -1L;
/*      */           }
/*      */         }
/* 1688 */         else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
/*      */           
/* 1690 */           this.debugCrashKeyPressTime = getSystemTime();
/*      */         } 
/*      */         
/* 1693 */         dispatchKeypresses();
/*      */         
/* 1695 */         if (Keyboard.getEventKeyState()) {
/*      */           
/* 1697 */           if (k == 62 && this.entityRenderer != null)
/*      */           {
/* 1699 */             this.entityRenderer.switchUseShader();
/*      */           }
/*      */           
/* 1702 */           if (this.currentScreen != null) {
/*      */             
/* 1704 */             this.currentScreen.handleKeyboardInput();
/*      */           }
/*      */           else {
/*      */             
/* 1708 */             if (k == 1)
/*      */             {
/* 1710 */               displayInGameMenu();
/*      */             }
/*      */             
/* 1713 */             if (k == 32 && Keyboard.isKeyDown(61) && this.ingameGUI != null)
/*      */             {
/* 1715 */               this.ingameGUI.getChatGUI().clearChatMessages();
/*      */             }
/*      */             
/* 1718 */             if (k == 31 && Keyboard.isKeyDown(61))
/*      */             {
/* 1720 */               refreshResources();
/*      */             }
/*      */             
/* 1723 */             if (k != 17 || Keyboard.isKeyDown(61));
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1728 */             if (k != 18 || Keyboard.isKeyDown(61));
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1733 */             if (k != 47 || Keyboard.isKeyDown(61));
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1738 */             if (k != 38 || Keyboard.isKeyDown(61));
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1743 */             if (k != 22 || Keyboard.isKeyDown(61));
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1748 */             if (k == 20 && Keyboard.isKeyDown(61))
/*      */             {
/* 1750 */               refreshResources();
/*      */             }
/*      */             
/* 1753 */             if (k == 33 && Keyboard.isKeyDown(61))
/*      */             {
/* 1755 */               this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
/*      */             }
/*      */             
/* 1758 */             if (k == 30 && Keyboard.isKeyDown(61))
/*      */             {
/* 1760 */               this.renderGlobal.loadRenderers();
/*      */             }
/*      */             
/* 1763 */             if (k == 35 && Keyboard.isKeyDown(61)) {
/*      */               
/* 1765 */               this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
/* 1766 */               this.gameSettings.saveOptions();
/*      */             } 
/*      */             
/* 1769 */             if (k == 48 && Keyboard.isKeyDown(61))
/*      */             {
/* 1771 */               this.renderManager.setDebugBoundingBox(!this.renderManager.isDebugBoundingBox());
/*      */             }
/*      */             
/* 1774 */             if (k == 25 && Keyboard.isKeyDown(61)) {
/*      */               
/* 1776 */               this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
/* 1777 */               this.gameSettings.saveOptions();
/*      */             } 
/*      */             
/* 1780 */             if (k == 59)
/*      */             {
/* 1782 */               this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
/*      */             }
/*      */             
/* 1785 */             if (k == 61) {
/*      */               
/* 1787 */               this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
/* 1788 */               this.gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
/* 1789 */               this.gameSettings.showLagometer = GuiScreen.isAltKeyDown();
/*      */             } 
/*      */             
/* 1792 */             if (this.gameSettings.keyBindTogglePerspective.isPressed()) {
/*      */               
/* 1794 */               this.gameSettings.thirdPersonView++;
/*      */               
/* 1796 */               if (this.gameSettings.thirdPersonView > 2)
/*      */               {
/* 1798 */                 this.gameSettings.thirdPersonView = 0;
/*      */               }
/*      */               
/* 1801 */               if (this.gameSettings.thirdPersonView == 0) {
/*      */                 
/* 1803 */                 this.entityRenderer.loadEntityShader(getRenderViewEntity());
/*      */               }
/* 1805 */               else if (this.gameSettings.thirdPersonView == 1) {
/*      */                 
/* 1807 */                 this.entityRenderer.loadEntityShader((Entity)null);
/*      */               } 
/*      */               
/* 1810 */               this.renderGlobal.setDisplayListEntitiesDirty();
/*      */             } 
/*      */             
/* 1813 */             if (this.gameSettings.keyBindSmoothCamera.isPressed())
/*      */             {
/* 1815 */               this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
/*      */             }
/*      */           } 
/*      */           
/* 1819 */           if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart) {
/*      */             
/* 1821 */             if (k == 11)
/*      */             {
/* 1823 */               updateDebugProfilerName(0);
/*      */             }
/*      */             
/* 1826 */             for (int j1 = 0; j1 < 9; j1++) {
/*      */               
/* 1828 */               if (k == 2 + j1)
/*      */               {
/* 1830 */                 updateDebugProfilerName(j1 + 1);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1837 */       for (int l = 0; l < 9; l++) {
/*      */         
/* 1839 */         if (this.gameSettings.keyBindsHotbar[l].isPressed())
/*      */         {
/* 1841 */           if (this.thePlayer.isSpectator()) {
/*      */             
/* 1843 */             this.ingameGUI.getSpectatorGui().func_175260_a(l);
/*      */           }
/*      */           else {
/*      */             
/* 1847 */             this.thePlayer.inventory.currentItem = l;
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 1852 */       boolean flag = (this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN);
/*      */       
/* 1854 */       while (this.gameSettings.keyBindInventory.isPressed()) {
/*      */         
/* 1856 */         if (this.playerController.isRidingHorse()) {
/*      */           
/* 1858 */           this.thePlayer.sendHorseInventory();
/*      */           
/*      */           continue;
/*      */         } 
/* 1862 */         getNetHandler().addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
/* 1863 */         displayGuiScreen((GuiScreen)new GuiInventory((EntityPlayer)this.thePlayer));
/*      */       } 
/*      */ 
/*      */       
/* 1867 */       while (this.gameSettings.keyBindDrop.isPressed()) {
/*      */         
/* 1869 */         if (!this.thePlayer.isSpectator())
/*      */         {
/* 1871 */           this.thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
/*      */         }
/*      */       } 
/*      */       
/* 1875 */       while (this.gameSettings.keyBindChat.isPressed() && flag)
/*      */       {
/* 1877 */         displayGuiScreen((GuiScreen)new GuiChat());
/*      */       }
/*      */       
/* 1880 */       if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && flag)
/*      */       {
/* 1882 */         displayGuiScreen((GuiScreen)new GuiChat("/"));
/*      */       }
/*      */       
/* 1885 */       if (this.thePlayer.isUsingItem()) {
/*      */         
/* 1887 */         if (!this.gameSettings.keyBindUseItem.isKeyDown())
/*      */         {
/* 1889 */           this.playerController.onStoppedUsingItem((EntityPlayer)this.thePlayer);
/*      */         }
/*      */         
/* 1892 */         while (this.gameSettings.keyBindAttack.isPressed());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1897 */         while (this.gameSettings.keyBindUseItem.isPressed());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1902 */         while (this.gameSettings.keyBindPickBlock.isPressed());
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1909 */         while (this.gameSettings.keyBindAttack.isPressed())
/*      */         {
/* 1911 */           clickMouse();
/*      */         }
/*      */         
/* 1914 */         while (this.gameSettings.keyBindUseItem.isPressed())
/*      */         {
/* 1916 */           rightClickMouse();
/*      */         }
/*      */         
/* 1919 */         while (this.gameSettings.keyBindPickBlock.isPressed())
/*      */         {
/* 1921 */           middleClickMouse();
/*      */         }
/*      */       } 
/*      */       
/* 1925 */       if (this.gameSettings.keyBindUseItem.isKeyDown() && (this.rightClickDelayTimer == 0 || FastPlace.toggled) && !this.thePlayer.isUsingItem())
/*      */       {
/* 1927 */         rightClickMouse();
/*      */       }
/*      */       
/* 1930 */       sendClickBlockToController((this.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus));
/*      */     } 
/*      */     
/* 1933 */     if (this.theWorld != null) {
/*      */       
/* 1935 */       if (this.thePlayer != null) {
/*      */         
/* 1937 */         this.joinPlayerCounter++;
/*      */         
/* 1939 */         if (this.joinPlayerCounter == 30) {
/*      */           
/* 1941 */           this.joinPlayerCounter = 0;
/* 1942 */           this.theWorld.joinEntityInSurroundings((Entity)this.thePlayer);
/*      */         } 
/*      */       } 
/*      */       
/* 1946 */       this.mcProfiler.endStartSection("gameRenderer");
/*      */       
/* 1948 */       if (!this.isGamePaused)
/*      */       {
/* 1950 */         this.entityRenderer.updateRenderer();
/*      */       }
/*      */       
/* 1953 */       this.mcProfiler.endStartSection("levelRenderer");
/*      */       
/* 1955 */       if (!this.isGamePaused)
/*      */       {
/* 1957 */         this.renderGlobal.updateClouds();
/*      */       }
/*      */       
/* 1960 */       this.mcProfiler.endStartSection("level");
/*      */       
/* 1962 */       if (!this.isGamePaused)
/*      */       {
/* 1964 */         if (this.theWorld.getLastLightningBolt() > 0)
/*      */         {
/* 1966 */           this.theWorld.setLastLightningBolt(this.theWorld.getLastLightningBolt() - 1);
/*      */         }
/*      */         
/* 1969 */         this.theWorld.updateEntities();
/*      */       }
/*      */     
/* 1972 */     } else if (this.entityRenderer.isShaderActive()) {
/*      */       
/* 1974 */       this.entityRenderer.stopUseShader();
/*      */     } 
/*      */     
/* 1977 */     if (!this.isGamePaused) {
/*      */       
/* 1979 */       this.mcMusicTicker.update();
/* 1980 */       this.mcSoundHandler.update();
/*      */     } 
/*      */     
/* 1983 */     if (this.theWorld != null) {
/*      */       
/* 1985 */       if (!this.isGamePaused) {
/*      */         
/* 1987 */         this.theWorld.setAllowedSpawnTypes((this.theWorld.getDifficulty() != EnumDifficulty.PEACEFUL), true);
/*      */ 
/*      */         
/*      */         try {
/* 1991 */           this.theWorld.tick();
/*      */         }
/* 1993 */         catch (Throwable throwable2) {
/*      */           
/* 1995 */           CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");
/*      */           
/* 1997 */           if (this.theWorld == null) {
/*      */             
/* 1999 */             CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
/* 2000 */             crashreportcategory2.addCrashSection("Problem", "Level is null!");
/*      */           }
/*      */           else {
/*      */             
/* 2004 */             this.theWorld.addWorldInfoToCrashReport(crashreport2);
/*      */           } 
/*      */           
/* 2007 */           throw new ReportedException(crashreport2);
/*      */         } 
/*      */       } 
/*      */       
/* 2011 */       this.mcProfiler.endStartSection("animateTick");
/*      */       
/* 2013 */       if (!this.isGamePaused && this.theWorld != null)
/*      */       {
/* 2015 */         this.theWorld.doVoidFogParticles(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
/*      */       }
/*      */       
/* 2018 */       this.mcProfiler.endStartSection("particles");
/*      */       
/* 2020 */       if (!this.isGamePaused)
/*      */       {
/* 2022 */         this.effectRenderer.updateEffects();
/*      */       }
/*      */     }
/* 2025 */     else if (this.myNetworkManager != null) {
/*      */       
/* 2027 */       this.mcProfiler.endStartSection("pendingConnection");
/* 2028 */       this.myNetworkManager.processReceivedPackets();
/*      */     } 
/*      */     
/* 2031 */     this.mcProfiler.endSection();
/* 2032 */     this.systemTime = getSystemTime();
/*      */   }
/*      */ 
/*      */   
/*      */   public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
/* 2037 */     loadWorld((WorldClient)null);
/* 2038 */     ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
/* 2039 */     WorldInfo worldinfo = isavehandler.loadWorldInfo();
/*      */     
/* 2041 */     if (worldinfo == null && worldSettingsIn != null) {
/*      */       
/* 2043 */       worldinfo = new WorldInfo(worldSettingsIn, folderName);
/* 2044 */       isavehandler.saveWorldInfo(worldinfo);
/*      */     } 
/*      */     
/* 2047 */     if (worldSettingsIn == null)
/*      */     {
/* 2049 */       worldSettingsIn = new WorldSettings(worldinfo);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 2054 */       this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn);
/* 2055 */       this.theIntegratedServer.startServerThread();
/* 2056 */       this.integratedServerIsRunning = true;
/*      */     }
/* 2058 */     catch (Throwable throwable) {
/*      */       
/* 2060 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
/* 2061 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
/* 2062 */       crashreportcategory.addCrashSection("Level ID", folderName);
/* 2063 */       crashreportcategory.addCrashSection("Level Name", worldName);
/* 2064 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */     
/* 2067 */     this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
/*      */     
/* 2069 */     while (!this.theIntegratedServer.serverIsInRunLoop()) {
/*      */       
/* 2071 */       String s = this.theIntegratedServer.getUserMessage();
/*      */       
/* 2073 */       if (s != null) {
/*      */         
/* 2075 */         this.loadingScreen.displayLoadingString(I18n.format(s, new Object[0]));
/*      */       }
/*      */       else {
/*      */         
/* 2079 */         this.loadingScreen.displayLoadingString("");
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/* 2084 */         Thread.sleep(200L);
/*      */       }
/* 2086 */       catch (InterruptedException interruptedException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2092 */     displayGuiScreen((GuiScreen)null);
/* 2093 */     SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
/* 2094 */     NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
/* 2095 */     networkmanager.setNetHandler((INetHandler)new NetHandlerLoginClient(networkmanager, this, (GuiScreen)null));
/* 2096 */     networkmanager.sendPacket((Packet)new C00Handshake(47, socketaddress.toString(), 0, EnumConnectionState.LOGIN));
/* 2097 */     networkmanager.sendPacket((Packet)new C00PacketLoginStart(getSession().getProfile()));
/* 2098 */     this.myNetworkManager = networkmanager;
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadWorld(WorldClient worldClientIn) {
/* 2103 */     loadWorld(worldClientIn, "");
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadWorld(WorldClient worldClientIn, String loadingMessage) {
/* 2108 */     if (worldClientIn == null) {
/*      */       
/* 2110 */       NetHandlerPlayClient nethandlerplayclient = getNetHandler();
/*      */       
/* 2112 */       if (nethandlerplayclient != null)
/*      */       {
/* 2114 */         nethandlerplayclient.cleanup();
/*      */       }
/*      */       
/* 2117 */       if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet()) {
/*      */         
/* 2119 */         this.theIntegratedServer.initiateShutdown();
/* 2120 */         this.theIntegratedServer.setStaticInstance();
/*      */       } 
/*      */       
/* 2123 */       this.theIntegratedServer = null;
/* 2124 */       this.guiAchievement.clearAchievements();
/* 2125 */       this.entityRenderer.getMapItemRenderer().clearLoadedMaps();
/*      */     } 
/*      */     
/* 2128 */     this.renderViewEntity = null;
/* 2129 */     this.myNetworkManager = null;
/*      */     
/* 2131 */     if (this.loadingScreen != null) {
/*      */       
/* 2133 */       this.loadingScreen.resetProgressAndMessage(loadingMessage);
/* 2134 */       this.loadingScreen.displayLoadingString("");
/*      */     } 
/*      */     
/* 2137 */     if (worldClientIn == null && this.theWorld != null) {
/*      */       
/* 2139 */       this.mcResourcePackRepository.clearResourcePack();
/* 2140 */       this.ingameGUI.resetPlayersOverlayFooterHeader();
/* 2141 */       setServerData((ServerData)null);
/* 2142 */       this.integratedServerIsRunning = false;
/*      */     } 
/*      */     
/* 2145 */     this.mcSoundHandler.stopSounds();
/* 2146 */     this.theWorld = worldClientIn;
/*      */     
/* 2148 */     if (worldClientIn != null) {
/*      */       
/* 2150 */       if (this.renderGlobal != null)
/*      */       {
/* 2152 */         this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
/*      */       }
/*      */       
/* 2155 */       if (this.effectRenderer != null)
/*      */       {
/* 2157 */         this.effectRenderer.clearEffects((World)worldClientIn);
/*      */       }
/*      */       
/* 2160 */       if (this.thePlayer == null) {
/*      */         
/* 2162 */         this.thePlayer = this.playerController.func_178892_a((World)worldClientIn, new StatFileWriter());
/* 2163 */         this.playerController.flipPlayer((EntityPlayer)this.thePlayer);
/*      */       } 
/*      */       
/* 2166 */       this.thePlayer.preparePlayerToSpawn();
/* 2167 */       worldClientIn.spawnEntityInWorld((Entity)this.thePlayer);
/* 2168 */       this.thePlayer.movementInput = (MovementInput)new MovementInputFromOptions(this.gameSettings);
/* 2169 */       this.playerController.setPlayerCapabilities((EntityPlayer)this.thePlayer);
/* 2170 */       this.renderViewEntity = (Entity)this.thePlayer;
/*      */     }
/*      */     else {
/*      */       
/* 2174 */       this.saveLoader.flushCache();
/* 2175 */       this.thePlayer = null;
/*      */     } 
/*      */     
/* 2178 */     this.systemTime = 0L;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDimensionAndSpawnPlayer(int dimension) {
/* 2183 */     this.theWorld.setInitialSpawnLocation();
/* 2184 */     this.theWorld.removeAllEntities();
/* 2185 */     int i = 0;
/* 2186 */     String s = null;
/*      */     
/* 2188 */     if (this.thePlayer != null) {
/*      */       
/* 2190 */       i = this.thePlayer.getEntityId();
/* 2191 */       this.theWorld.removeEntity((Entity)this.thePlayer);
/* 2192 */       s = this.thePlayer.getClientBrand();
/*      */     } 
/*      */     
/* 2195 */     this.renderViewEntity = null;
/* 2196 */     EntityPlayerSP entityplayersp = this.thePlayer;
/* 2197 */     this.thePlayer = this.playerController.func_178892_a((World)this.theWorld, (this.thePlayer == null) ? new StatFileWriter() : this.thePlayer.getStatFileWriter());
/* 2198 */     this.thePlayer.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
/* 2199 */     this.thePlayer.dimension = dimension;
/* 2200 */     this.renderViewEntity = (Entity)this.thePlayer;
/* 2201 */     this.thePlayer.preparePlayerToSpawn();
/* 2202 */     this.thePlayer.setClientBrand(s);
/* 2203 */     this.theWorld.spawnEntityInWorld((Entity)this.thePlayer);
/* 2204 */     this.playerController.flipPlayer((EntityPlayer)this.thePlayer);
/* 2205 */     this.thePlayer.movementInput = (MovementInput)new MovementInputFromOptions(this.gameSettings);
/* 2206 */     this.thePlayer.setEntityId(i);
/* 2207 */     this.playerController.setPlayerCapabilities((EntityPlayer)this.thePlayer);
/* 2208 */     this.thePlayer.setReducedDebug(entityplayersp.hasReducedDebug());
/*      */     
/* 2210 */     if (this.currentScreen instanceof GuiGameOver)
/*      */     {
/* 2212 */       displayGuiScreen((GuiScreen)null);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isDemo() {
/* 2218 */     return this.isDemo;
/*      */   }
/*      */ 
/*      */   
/*      */   public NetHandlerPlayClient getNetHandler() {
/* 2223 */     return (this.thePlayer != null) ? this.thePlayer.sendQueue : null;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isGuiEnabled() {
/* 2228 */     return (theMinecraft == null || !theMinecraft.gameSettings.hideGUI);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFancyGraphicsEnabled() {
/* 2233 */     return (theMinecraft != null && theMinecraft.gameSettings.fancyGraphics);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAmbientOcclusionEnabled() {
/* 2238 */     return (theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void middleClickMouse() {
/* 2243 */     if (this.objectMouseOver != null) {
/*      */       Item item;
/* 2245 */       boolean flag = this.thePlayer.capabilities.isCreativeMode;
/* 2246 */       int i = 0;
/* 2247 */       boolean flag1 = false;
/* 2248 */       TileEntity tileentity = null;
/*      */ 
/*      */       
/* 2251 */       if (this.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*      */         
/* 2253 */         BlockPos blockpos = this.objectMouseOver.getBlockPos();
/* 2254 */         Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */         
/* 2256 */         if (block.getMaterial() == Material.air) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 2261 */         item = block.getItem((World)this.theWorld, blockpos);
/*      */         
/* 2263 */         if (item == null) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 2268 */         if (flag && GuiScreen.isCtrlKeyDown())
/*      */         {
/* 2270 */           tileentity = this.theWorld.getTileEntity(blockpos);
/*      */         }
/*      */         
/* 2273 */         Block block1 = (item instanceof net.minecraft.item.ItemBlock && !block.isFlowerPot()) ? Block.getBlockFromItem(item) : block;
/* 2274 */         i = block1.getDamageValue((World)this.theWorld, blockpos);
/* 2275 */         flag1 = item.getHasSubtypes();
/*      */       }
/*      */       else {
/*      */         
/* 2279 */         if (this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || this.objectMouseOver.entityHit == null || !flag) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 2284 */         if (this.objectMouseOver.entityHit instanceof net.minecraft.entity.item.EntityPainting) {
/*      */           
/* 2286 */           item = Items.painting;
/*      */         }
/* 2288 */         else if (this.objectMouseOver.entityHit instanceof net.minecraft.entity.EntityLeashKnot) {
/*      */           
/* 2290 */           item = Items.lead;
/*      */         }
/* 2292 */         else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
/*      */           
/* 2294 */           EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.entityHit;
/* 2295 */           ItemStack itemstack = entityitemframe.getDisplayedItem();
/*      */           
/* 2297 */           if (itemstack == null)
/*      */           {
/* 2299 */             item = Items.item_frame;
/*      */           }
/*      */           else
/*      */           {
/* 2303 */             item = itemstack.getItem();
/* 2304 */             i = itemstack.getMetadata();
/* 2305 */             flag1 = true;
/*      */           }
/*      */         
/* 2308 */         } else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
/*      */           
/* 2310 */           EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.entityHit;
/*      */           
/* 2312 */           switch (entityminecart.getMinecartType()) {
/*      */             
/*      */             case FURNACE:
/* 2315 */               item = Items.furnace_minecart;
/*      */               break;
/*      */             
/*      */             case CHEST:
/* 2319 */               item = Items.chest_minecart;
/*      */               break;
/*      */             
/*      */             case TNT:
/* 2323 */               item = Items.tnt_minecart;
/*      */               break;
/*      */             
/*      */             case HOPPER:
/* 2327 */               item = Items.hopper_minecart;
/*      */               break;
/*      */             
/*      */             case COMMAND_BLOCK:
/* 2331 */               item = Items.command_block_minecart;
/*      */               break;
/*      */             
/*      */             default:
/* 2335 */               item = Items.minecart;
/*      */               break;
/*      */           } 
/* 2338 */         } else if (this.objectMouseOver.entityHit instanceof net.minecraft.entity.item.EntityBoat) {
/*      */           
/* 2340 */           item = Items.boat;
/*      */         }
/* 2342 */         else if (this.objectMouseOver.entityHit instanceof net.minecraft.entity.item.EntityArmorStand) {
/*      */           
/* 2344 */           ItemArmorStand itemArmorStand = Items.armor_stand;
/*      */         }
/*      */         else {
/*      */           
/* 2348 */           item = Items.spawn_egg;
/* 2349 */           i = EntityList.getEntityID(this.objectMouseOver.entityHit);
/* 2350 */           flag1 = true;
/*      */           
/* 2352 */           if (!EntityList.entityEggs.containsKey(Integer.valueOf(i))) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2359 */       InventoryPlayer inventoryplayer = this.thePlayer.inventory;
/*      */       
/* 2361 */       if (tileentity == null) {
/*      */         
/* 2363 */         inventoryplayer.setCurrentItem(item, i, flag1, flag);
/*      */       }
/*      */       else {
/*      */         
/* 2367 */         ItemStack itemstack1 = pickBlockWithNBT(item, i, tileentity);
/* 2368 */         inventoryplayer.setInventorySlotContents(inventoryplayer.currentItem, itemstack1);
/*      */       } 
/*      */       
/* 2371 */       if (flag) {
/*      */         
/* 2373 */         int j = this.thePlayer.inventoryContainer.inventorySlots.size() - 9 + inventoryplayer.currentItem;
/* 2374 */         this.playerController.sendSlotPacket(inventoryplayer.getStackInSlot(inventoryplayer.currentItem), j);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private ItemStack pickBlockWithNBT(Item itemIn, int meta, TileEntity tileEntityIn) {
/* 2381 */     ItemStack itemstack = new ItemStack(itemIn, 1, meta);
/* 2382 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 2383 */     tileEntityIn.writeToNBT(nbttagcompound);
/*      */     
/* 2385 */     if (itemIn == Items.skull && nbttagcompound.hasKey("Owner")) {
/*      */       
/* 2387 */       NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
/* 2388 */       NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/* 2389 */       nbttagcompound3.setTag("SkullOwner", (NBTBase)nbttagcompound2);
/* 2390 */       itemstack.setTagCompound(nbttagcompound3);
/* 2391 */       return itemstack;
/*      */     } 
/*      */ 
/*      */     
/* 2395 */     itemstack.setTagInfo("BlockEntityTag", (NBTBase)nbttagcompound);
/* 2396 */     NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 2397 */     NBTTagList nbttaglist = new NBTTagList();
/* 2398 */     nbttaglist.appendTag((NBTBase)new NBTTagString("(+NBT)"));
/* 2399 */     nbttagcompound1.setTag("Lore", (NBTBase)nbttaglist);
/* 2400 */     itemstack.setTagInfo("display", (NBTBase)nbttagcompound1);
/* 2401 */     return itemstack;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash) {
/* 2407 */     theCrash.getCategory().addCrashSectionCallable("Launched Version", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2411 */             return Minecraft.this.launchedVersion;
/*      */           }
/*      */         });
/* 2414 */     theCrash.getCategory().addCrashSectionCallable("LWJGL", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 2418 */             return Sys.getVersion();
/*      */           }
/*      */         });
/* 2421 */     theCrash.getCategory().addCrashSectionCallable("OpenGL", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 2425 */             return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
/*      */           }
/*      */         });
/* 2428 */     theCrash.getCategory().addCrashSectionCallable("GL Caps", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 2432 */             return OpenGlHelper.getLogText();
/*      */           }
/*      */         });
/* 2435 */     theCrash.getCategory().addCrashSectionCallable("Using VBOs", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 2439 */             return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
/*      */           }
/*      */         });
/* 2442 */     theCrash.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2446 */             String s = ClientBrandRetriever.getClientModName();
/* 2447 */             return !s.equals("vanilla") ? ("Definitely; Client brand changed to '" + s + "'") : ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.");
/*      */           }
/*      */         });
/* 2450 */     theCrash.getCategory().addCrashSectionCallable("Type", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2454 */             return "Client (map_client.txt)";
/*      */           }
/*      */         });
/* 2457 */     theCrash.getCategory().addCrashSectionCallable("Resource Packs", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2461 */             StringBuilder stringbuilder = new StringBuilder();
/*      */             
/* 2463 */             for (String s : Minecraft.this.gameSettings.resourcePacks) {
/*      */               
/* 2465 */               if (stringbuilder.length() > 0)
/*      */               {
/* 2467 */                 stringbuilder.append(", ");
/*      */               }
/*      */               
/* 2470 */               stringbuilder.append(s);
/*      */               
/* 2472 */               if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(s))
/*      */               {
/* 2474 */                 stringbuilder.append(" (incompatible)");
/*      */               }
/*      */             } 
/*      */             
/* 2478 */             return stringbuilder.toString();
/*      */           }
/*      */         });
/* 2481 */     theCrash.getCategory().addCrashSectionCallable("Current Language", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2485 */             return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
/*      */           }
/*      */         });
/* 2488 */     theCrash.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>()
/*      */         {
/*      */           public String call() throws Exception
/*      */           {
/* 2492 */             return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
/*      */           }
/*      */         });
/* 2495 */     theCrash.getCategory().addCrashSectionCallable("CPU", new Callable<String>()
/*      */         {
/*      */           public String call()
/*      */           {
/* 2499 */             return OpenGlHelper.getCpu();
/*      */           }
/*      */         });
/*      */     
/* 2503 */     if (this.theWorld != null)
/*      */     {
/* 2505 */       this.theWorld.addWorldInfoToCrashReport(theCrash);
/*      */     }
/*      */     
/* 2508 */     return theCrash;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Minecraft getMinecraft() {
/* 2513 */     return theMinecraft;
/*      */   }
/*      */ 
/*      */   
/*      */   public ListenableFuture<Object> scheduleResourcesRefresh() {
/* 2518 */     return addScheduledTask(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/* 2522 */             Minecraft.this.refreshResources();
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
/* 2529 */     playerSnooper.addClientStat("fps", Integer.valueOf(debugFPS));
/* 2530 */     playerSnooper.addClientStat("vsync_enabled", Boolean.valueOf(this.gameSettings.enableVsync));
/* 2531 */     playerSnooper.addClientStat("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
/* 2532 */     playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
/* 2533 */     playerSnooper.addClientStat("run_time", Long.valueOf((MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
/* 2534 */     playerSnooper.addClientStat("current_action", getCurrentAction());
/* 2535 */     String s = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) ? "little" : "big";
/* 2536 */     playerSnooper.addClientStat("endianness", s);
/* 2537 */     playerSnooper.addClientStat("resource_packs", Integer.valueOf(this.mcResourcePackRepository.getRepositoryEntries().size()));
/* 2538 */     int i = 0;
/*      */     
/* 2540 */     for (ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries())
/*      */     {
/* 2542 */       playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
/*      */     }
/*      */     
/* 2545 */     if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null)
/*      */     {
/* 2547 */       playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private String getCurrentAction() {
/* 2553 */     return (this.theIntegratedServer != null) ? (this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer") : ((this.currentServerData != null) ? (this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer") : "out_of_game");
/*      */   }
/*      */ 
/*      */   
/*      */   public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper) {
/* 2558 */     playerSnooper.addStatToSnooper("opengl_version", GL11.glGetString(7938));
/* 2559 */     playerSnooper.addStatToSnooper("opengl_vendor", GL11.glGetString(7936));
/* 2560 */     playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
/* 2561 */     playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
/* 2562 */     ContextCapabilities contextcapabilities = GLContext.getCapabilities();
/* 2563 */     playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_arrays_of_arrays));
/* 2564 */     playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", Boolean.valueOf(contextcapabilities.GL_ARB_base_instance));
/* 2565 */     playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", Boolean.valueOf(contextcapabilities.GL_ARB_blend_func_extended));
/* 2566 */     playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_clear_buffer_object));
/* 2567 */     playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_color_buffer_float));
/* 2568 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", Boolean.valueOf(contextcapabilities.GL_ARB_compatibility));
/* 2569 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", Boolean.valueOf(contextcapabilities.GL_ARB_compressed_texture_pixel_storage));
/* 2570 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
/* 2571 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
/* 2572 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
/* 2573 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
/* 2574 */     playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_compute_shader));
/* 2575 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_buffer));
/* 2576 */     playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", Boolean.valueOf(contextcapabilities.GL_ARB_copy_image));
/* 2577 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_buffer_float));
/* 2578 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_clamp));
/* 2579 */     playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", Boolean.valueOf(contextcapabilities.GL_ARB_depth_texture));
/* 2580 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers));
/* 2581 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_buffers_blend));
/* 2582 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_elements_base_vertex));
/* 2583 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_indirect));
/* 2584 */     playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_ARB_draw_instanced));
/* 2585 */     playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_attrib_location));
/* 2586 */     playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", Boolean.valueOf(contextcapabilities.GL_ARB_explicit_uniform_location));
/* 2587 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_layer_viewport));
/* 2588 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program));
/* 2589 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_shader));
/* 2590 */     playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program_shadow));
/* 2591 */     playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_object));
/* 2592 */     playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_sRGB));
/* 2593 */     playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_ARB_geometry_shader4));
/* 2594 */     playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", Boolean.valueOf(contextcapabilities.GL_ARB_gpu_shader5));
/* 2595 */     playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_pixel));
/* 2596 */     playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", Boolean.valueOf(contextcapabilities.GL_ARB_half_float_vertex));
/* 2597 */     playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", Boolean.valueOf(contextcapabilities.GL_ARB_instanced_arrays));
/* 2598 */     playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_alignment));
/* 2599 */     playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", Boolean.valueOf(contextcapabilities.GL_ARB_map_buffer_range));
/* 2600 */     playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", Boolean.valueOf(contextcapabilities.GL_ARB_multisample));
/* 2601 */     playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", Boolean.valueOf(contextcapabilities.GL_ARB_multitexture));
/* 2602 */     playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", Boolean.valueOf(contextcapabilities.GL_ARB_occlusion_query2));
/* 2603 */     playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_pixel_buffer_object));
/* 2604 */     playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_seamless_cube_map));
/* 2605 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_objects));
/* 2606 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_stencil_export));
/* 2607 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_texture_lod));
/* 2608 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow));
/* 2609 */     playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", Boolean.valueOf(contextcapabilities.GL_ARB_shadow_ambient));
/* 2610 */     playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", Boolean.valueOf(contextcapabilities.GL_ARB_stencil_texturing));
/* 2611 */     playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", Boolean.valueOf(contextcapabilities.GL_ARB_sync));
/* 2612 */     playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_tessellation_shader));
/* 2613 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_border_clamp));
/* 2614 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_buffer_object));
/* 2615 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map));
/* 2616 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map_array));
/* 2617 */     playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_non_power_of_two));
/* 2618 */     playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_uniform_buffer_object));
/* 2619 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_blend));
/* 2620 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_buffer_object));
/* 2621 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_program));
/* 2622 */     playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_shader));
/* 2623 */     playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", Boolean.valueOf(contextcapabilities.GL_EXT_bindable_uniform));
/* 2624 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_equation_separate));
/* 2625 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_func_separate));
/* 2626 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_minmax));
/* 2627 */     playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", Boolean.valueOf(contextcapabilities.GL_EXT_blend_subtract));
/* 2628 */     playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", Boolean.valueOf(contextcapabilities.GL_EXT_draw_instanced));
/* 2629 */     playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_multisample));
/* 2630 */     playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_object));
/* 2631 */     playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_framebuffer_sRGB));
/* 2632 */     playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_geometry_shader4));
/* 2633 */     playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_program_parameters));
/* 2634 */     playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", Boolean.valueOf(contextcapabilities.GL_EXT_gpu_shader4));
/* 2635 */     playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", Boolean.valueOf(contextcapabilities.GL_EXT_multi_draw_arrays));
/* 2636 */     playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", Boolean.valueOf(contextcapabilities.GL_EXT_packed_depth_stencil));
/* 2637 */     playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", Boolean.valueOf(contextcapabilities.GL_EXT_paletted_texture));
/* 2638 */     playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", Boolean.valueOf(contextcapabilities.GL_EXT_rescale_normal));
/* 2639 */     playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", Boolean.valueOf(contextcapabilities.GL_EXT_separate_shader_objects));
/* 2640 */     playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", Boolean.valueOf(contextcapabilities.GL_EXT_shader_image_load_store));
/* 2641 */     playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", Boolean.valueOf(contextcapabilities.GL_EXT_shadow_funcs));
/* 2642 */     playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", Boolean.valueOf(contextcapabilities.GL_EXT_shared_texture_palette));
/* 2643 */     playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_clear_tag));
/* 2644 */     playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_two_side));
/* 2645 */     playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", Boolean.valueOf(contextcapabilities.GL_EXT_stencil_wrap));
/* 2646 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_3d));
/* 2647 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_array));
/* 2648 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_buffer_object));
/* 2649 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_integer));
/* 2650 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_lod_bias));
/* 2651 */     playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", Boolean.valueOf(contextcapabilities.GL_EXT_texture_sRGB));
/* 2652 */     playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_shader));
/* 2653 */     playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", Boolean.valueOf(contextcapabilities.GL_EXT_vertex_weighting));
/* 2654 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", Integer.valueOf(GL11.glGetInteger(35658)));
/* 2655 */     GL11.glGetError();
/* 2656 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", Integer.valueOf(GL11.glGetInteger(35657)));
/* 2657 */     GL11.glGetError();
/* 2658 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", Integer.valueOf(GL11.glGetInteger(34921)));
/* 2659 */     GL11.glGetError();
/* 2660 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35660)));
/* 2661 */     GL11.glGetError();
/* 2662 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(34930)));
/* 2663 */     GL11.glGetError();
/* 2664 */     playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", Integer.valueOf(GL11.glGetInteger(35071)));
/* 2665 */     GL11.glGetError();
/* 2666 */     playerSnooper.addStatToSnooper("gl_max_texture_size", Integer.valueOf(getGLMaximumTextureSize()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getGLMaximumTextureSize() {
/* 2671 */     for (int i = 16384; i > 0; i >>= 1) {
/*      */       
/* 2673 */       GL11.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, (ByteBuffer)null);
/* 2674 */       int j = GL11.glGetTexLevelParameteri(32868, 0, 4096);
/*      */       
/* 2676 */       if (j != 0)
/*      */       {
/* 2678 */         return i;
/*      */       }
/*      */     } 
/*      */     
/* 2682 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSnooperEnabled() {
/* 2687 */     return this.gameSettings.snooperEnabled;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setServerData(ServerData serverDataIn) {
/* 2692 */     this.currentServerData = serverDataIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public ServerData getCurrentServerData() {
/* 2697 */     return this.currentServerData;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isIntegratedServerRunning() {
/* 2702 */     return this.integratedServerIsRunning;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSingleplayer() {
/* 2707 */     return (this.integratedServerIsRunning && this.theIntegratedServer != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public IntegratedServer getIntegratedServer() {
/* 2712 */     return this.theIntegratedServer;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void stopIntegratedServer() {
/* 2717 */     if (theMinecraft != null) {
/*      */       
/* 2719 */       IntegratedServer integratedserver = theMinecraft.getIntegratedServer();
/*      */       
/* 2721 */       if (integratedserver != null)
/*      */       {
/* 2723 */         integratedserver.stopServer();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public PlayerUsageSnooper getPlayerUsageSnooper() {
/* 2730 */     return this.usageSnooper;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long getSystemTime() {
/* 2735 */     return Sys.getTime() * 1000L / Sys.getTimerResolution();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFullScreen() {
/* 2740 */     return this.fullscreen;
/*      */   }
/*      */ 
/*      */   
/*      */   public Session getSession() {
/* 2745 */     return this.session;
/*      */   }
/*      */ 
/*      */   
/*      */   public PropertyMap getTwitchDetails() {
/* 2750 */     return this.twitchDetails;
/*      */   }
/*      */ 
/*      */   
/*      */   public PropertyMap getProfileProperties() {
/* 2755 */     if (this.profileProperties.isEmpty()) {
/*      */       
/* 2757 */       GameProfile gameprofile = getSessionService().fillProfileProperties(this.session.getProfile(), false);
/* 2758 */       this.profileProperties.putAll((Multimap)gameprofile.getProperties());
/*      */     } 
/*      */     
/* 2761 */     return this.profileProperties;
/*      */   }
/*      */ 
/*      */   
/*      */   public Proxy getProxy() {
/* 2766 */     return this.proxy;
/*      */   }
/*      */ 
/*      */   
/*      */   public TextureManager getTextureManager() {
/* 2771 */     return this.renderEngine;
/*      */   }
/*      */ 
/*      */   
/*      */   public IResourceManager getResourceManager() {
/* 2776 */     return (IResourceManager)this.mcResourceManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public ResourcePackRepository getResourcePackRepository() {
/* 2781 */     return this.mcResourcePackRepository;
/*      */   }
/*      */ 
/*      */   
/*      */   public LanguageManager getLanguageManager() {
/* 2786 */     return this.mcLanguageManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public TextureMap getTextureMapBlocks() {
/* 2791 */     return this.textureMapBlocks;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isJava64bit() {
/* 2796 */     return this.jvm64bit;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isGamePaused() {
/* 2801 */     return this.isGamePaused;
/*      */   }
/*      */ 
/*      */   
/*      */   public SoundHandler getSoundHandler() {
/* 2806 */     return this.mcSoundHandler;
/*      */   }
/*      */ 
/*      */   
/*      */   public MusicTicker.MusicType getAmbientMusicType() {
/* 2811 */     return (this.thePlayer != null) ? ((this.thePlayer.worldObj.provider instanceof net.minecraft.world.WorldProviderHell) ? MusicTicker.MusicType.NETHER : ((this.thePlayer.worldObj.provider instanceof net.minecraft.world.WorldProviderEnd) ? ((BossStatus.bossName != null && BossStatus.statusBarTime > 0) ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END) : ((this.thePlayer.capabilities.isCreativeMode && this.thePlayer.capabilities.allowFlying) ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME))) : MusicTicker.MusicType.MENU;
/*      */   }
/*      */   
/*      */   public void dispatchKeypresses() {
/* 2815 */     int i = (Keyboard.getEventKey() == 0) ? Keyboard.getEventCharacter() : Keyboard.getEventKey();
/*      */     
/* 2817 */     if (i != 0 && !Keyboard.isRepeatEvent() && (
/* 2818 */       !(this.currentScreen instanceof GuiControls) || ((GuiControls)this.currentScreen).time <= getSystemTime() - 20L) && 
/* 2819 */       Keyboard.getEventKeyState()) {
/* 2820 */       if (i == this.gameSettings.keyBindFullscreen.getKeyCode()) {
/* 2821 */         toggleFullscreen();
/* 2822 */       } else if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
/* 2823 */         this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MinecraftSessionService getSessionService() {
/* 2832 */     return this.sessionService;
/*      */   }
/*      */ 
/*      */   
/*      */   public SkinManager getSkinManager() {
/* 2837 */     return this.skinManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public Entity getRenderViewEntity() {
/* 2842 */     return this.renderViewEntity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRenderViewEntity(Entity viewingEntity) {
/* 2847 */     this.renderViewEntity = viewingEntity;
/* 2848 */     this.entityRenderer.loadEntityShader(viewingEntity);
/*      */   }
/*      */ 
/*      */   
/*      */   public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
/* 2853 */     Validate.notNull(callableToSchedule);
/*      */     
/* 2855 */     if (!isCallingFromMinecraftThread()) {
/*      */       
/* 2857 */       ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
/*      */       
/* 2859 */       synchronized (this.scheduledTasks) {
/*      */         
/* 2861 */         this.scheduledTasks.add(listenablefuturetask);
/* 2862 */         return (ListenableFuture<V>)listenablefuturetask;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2869 */       return Futures.immediateFuture(callableToSchedule.call());
/*      */     }
/* 2871 */     catch (Exception exception) {
/*      */       
/* 2873 */       return (ListenableFuture<V>)Futures.immediateFailedCheckedFuture(exception);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
/* 2880 */     Validate.notNull(runnableToSchedule);
/* 2881 */     return addScheduledTask(Executors.callable(runnableToSchedule));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCallingFromMinecraftThread() {
/* 2886 */     return (Thread.currentThread() == this.mcThread);
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockRendererDispatcher getBlockRendererDispatcher() {
/* 2891 */     return this.blockRenderDispatcher;
/*      */   }
/*      */ 
/*      */   
/*      */   public RenderManager getRenderManager() {
/* 2896 */     return this.renderManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public RenderItem getRenderItem() {
/* 2901 */     return this.renderItem;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemRenderer getItemRenderer() {
/* 2906 */     return this.itemRenderer;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getDebugFPS() {
/* 2911 */     return debugFPS * 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public FrameTimer getFrameTimer() {
/* 2916 */     return this.frameTimer;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, String> getSessionInfo() {
/* 2921 */     Map<String, String> map = Maps.newHashMap();
/* 2922 */     map.put("X-Minecraft-Username", getMinecraft().getSession().getUsername());
/* 2923 */     map.put("X-Minecraft-UUID", getMinecraft().getSession().getPlayerID());
/* 2924 */     map.put("X-Minecraft-Version", "1.8.9");
/* 2925 */     return map;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isConnectedToRealms() {
/* 2930 */     return this.connectedToRealms;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setConnectedToRealms(boolean isConnected) {
/* 2935 */     this.connectedToRealms = isConnected;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\Minecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */