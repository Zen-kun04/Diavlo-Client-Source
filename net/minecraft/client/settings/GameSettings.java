/*      */ package net.minecraft.client.settings;
/*      */ 
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.gson.Gson;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileWriter;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.audio.SoundCategory;
/*      */ import net.minecraft.client.gui.GuiNewChat;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.renderer.OpenGlHelper;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.client.C15PacketClientSettings;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.World;
/*      */ import net.optifine.ClearWater;
/*      */ import net.optifine.CustomColors;
/*      */ import net.optifine.CustomGuis;
/*      */ import net.optifine.CustomSky;
/*      */ import net.optifine.DynamicLights;
/*      */ import net.optifine.Lang;
/*      */ import net.optifine.NaturalTextures;
/*      */ import net.optifine.RandomEntities;
/*      */ import net.optifine.reflect.Reflector;
/*      */ import net.optifine.shaders.Shaders;
/*      */ import net.optifine.util.KeyUtils;
/*      */ import org.apache.commons.io.IOUtils;
/*      */ import org.apache.commons.lang3.ArrayUtils;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.DisplayMode;
/*      */ 
/*      */ public class GameSettings {
/*   58 */   private static final Logger logger = LogManager.getLogger();
/*   59 */   private static final Gson gson = new Gson();
/*   60 */   private static final ParameterizedType typeListString = new ParameterizedType()
/*      */     {
/*      */       public Type[] getActualTypeArguments()
/*      */       {
/*   64 */         return new Type[] { String.class };
/*      */       }
/*      */       
/*      */       public Type getRawType() {
/*   68 */         return List.class;
/*      */       }
/*      */       
/*      */       public Type getOwnerType() {
/*   72 */         return null;
/*      */       }
/*      */     };
/*   75 */   private static final String[] GUISCALES = new String[] { "options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large" };
/*   76 */   private static final String[] PARTICLES = new String[] { "options.particles.all", "options.particles.decreased", "options.particles.minimal" };
/*   77 */   private static final String[] AMBIENT_OCCLUSIONS = new String[] { "options.ao.off", "options.ao.min", "options.ao.max" };
/*   78 */   private static final String[] STREAM_COMPRESSIONS = new String[] { "options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high" };
/*   79 */   private static final String[] STREAM_CHAT_MODES = new String[] { "options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never" };
/*   80 */   private static final String[] STREAM_CHAT_FILTER_MODES = new String[] { "options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods" };
/*   81 */   private static final String[] STREAM_MIC_MODES = new String[] { "options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk" };
/*   82 */   private static final String[] CLOUDS_TYPES = new String[] { "options.off", "options.graphics.fast", "options.graphics.fancy" };
/*   83 */   public float mouseSensitivity = 0.5F;
/*      */   public boolean invertMouse;
/*   85 */   public int renderDistanceChunks = -1;
/*      */   public boolean viewBobbing = true;
/*      */   public boolean anaglyph;
/*      */   public boolean fboEnable = true;
/*   89 */   public int limitFramerate = 120;
/*   90 */   public int clouds = 2;
/*      */   public boolean fancyGraphics = true;
/*   92 */   public int ambientOcclusion = 2;
/*   93 */   public List<String> resourcePacks = Lists.newArrayList();
/*   94 */   public List<String> incompatibleResourcePacks = Lists.newArrayList();
/*   95 */   public EntityPlayer.EnumChatVisibility chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
/*      */   public boolean chatColours = true;
/*      */   public boolean chatLinks = true;
/*      */   public boolean chatLinksPrompt = true;
/*   99 */   public float chatOpacity = 1.0F;
/*      */   public boolean snooperEnabled = true;
/*      */   public boolean fullScreen;
/*      */   public boolean enableVsync = true;
/*      */   public boolean useVbo = false;
/*      */   public boolean allowBlockAlternatives = true;
/*      */   public boolean reducedDebugInfo = false;
/*      */   public boolean hideServerAddress;
/*      */   public boolean advancedItemTooltips;
/*      */   public boolean pauseOnLostFocus = true;
/*  109 */   private final Set<EnumPlayerModelParts> setModelParts = Sets.newHashSet((Object[])EnumPlayerModelParts.values());
/*      */   public boolean touchscreen;
/*      */   public int overrideWidth;
/*      */   public int overrideHeight;
/*      */   public boolean heldItemTooltips = true;
/*  114 */   public float chatScale = 1.0F;
/*  115 */   public float chatWidth = 1.0F;
/*  116 */   public float chatHeightUnfocused = 0.44366196F;
/*  117 */   public float chatHeightFocused = 1.0F;
/*      */   public boolean showInventoryAchievementHint = true;
/*  119 */   public int mipmapLevels = 4;
/*  120 */   private Map<SoundCategory, Float> mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
/*  121 */   public float streamBytesPerPixel = 0.5F;
/*  122 */   public float streamMicVolume = 1.0F;
/*  123 */   public float streamGameVolume = 1.0F;
/*  124 */   public float streamKbps = 0.5412844F;
/*  125 */   public float streamFps = 0.31690142F;
/*  126 */   public int streamCompression = 1;
/*      */   public boolean streamSendMetadata = true;
/*  128 */   public String streamPreferredServer = "";
/*  129 */   public int streamChatEnabled = 0;
/*  130 */   public int streamChatUserFilter = 0;
/*  131 */   public int streamMicToggleBehavior = 0;
/*      */   public boolean useNativeTransport = true;
/*      */   public boolean entityShadows = true;
/*      */   public boolean realmsNotifications = true;
/*  135 */   public KeyBinding keyBindForward = new KeyBinding("key.forward", 17, "key.categories.movement");
/*  136 */   public KeyBinding keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
/*  137 */   public KeyBinding keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
/*  138 */   public KeyBinding keyBindRight = new KeyBinding("key.right", 32, "key.categories.movement");
/*  139 */   public KeyBinding keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
/*  140 */   public KeyBinding keyBindSneak = new KeyBinding("key.sneak", 42, "key.categories.movement");
/*  141 */   public KeyBinding keyBindSprint = new KeyBinding("key.sprint", 29, "key.categories.movement");
/*  142 */   public KeyBinding keyBindInventory = new KeyBinding("key.inventory", 18, "key.categories.inventory");
/*  143 */   public KeyBinding keyBindUseItem = new KeyBinding("key.use", -99, "key.categories.gameplay");
/*  144 */   public KeyBinding keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
/*  145 */   public KeyBinding keyBindAttack = new KeyBinding("key.attack", -100, "key.categories.gameplay");
/*  146 */   public KeyBinding keyBindPickBlock = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
/*  147 */   public KeyBinding keyBindChat = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
/*  148 */   public KeyBinding keyBindPlayerList = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
/*  149 */   public KeyBinding keyBindCommand = new KeyBinding("key.command", 53, "key.categories.multiplayer");
/*  150 */   public KeyBinding keyBindScreenshot = new KeyBinding("key.screenshot", 60, "key.categories.misc");
/*  151 */   public KeyBinding keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
/*  152 */   public KeyBinding keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
/*  153 */   public KeyBinding keyBindFullscreen = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
/*  154 */   public KeyBinding keyBindSpectatorOutlines = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
/*  155 */   public KeyBinding keyBindStreamStartStop = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
/*  156 */   public KeyBinding keyBindStreamPauseUnpause = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
/*  157 */   public KeyBinding keyBindStreamCommercials = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
/*  158 */   public KeyBinding keyBindStreamToggleMic = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
/*  159 */   public KeyBinding[] keyBindsHotbar = new KeyBinding[] { new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
/*      */   public KeyBinding[] keyBindings;
/*      */   protected Minecraft mc;
/*      */   private File optionsFile;
/*      */   public EnumDifficulty difficulty;
/*      */   public boolean hideGUI;
/*      */   public int thirdPersonView;
/*      */   public boolean showDebugInfo;
/*      */   public boolean showDebugProfilerChart;
/*      */   public boolean showLagometer;
/*      */   public String lastServer;
/*      */   public boolean smoothCamera;
/*      */   public boolean debugCamEnable;
/*      */   public float fovSetting;
/*      */   public float gammaSetting;
/*      */   public float saturation;
/*      */   public int guiScale;
/*      */   public int particleSetting;
/*      */   public String language;
/*      */   public boolean forceUnicodeFont;
/*  179 */   public int ofFogType = 1;
/*  180 */   public float ofFogStart = 0.8F;
/*  181 */   public int ofMipmapType = 0;
/*      */   public boolean ofOcclusionFancy = false;
/*      */   public boolean ofSmoothFps = false;
/*  184 */   public boolean ofSmoothWorld = Config.isSingleProcessor();
/*  185 */   public boolean ofLazyChunkLoading = Config.isSingleProcessor();
/*      */   public boolean ofRenderRegions = false;
/*      */   public boolean ofSmartAnimations = false;
/*  188 */   public float ofAoLevel = 1.0F;
/*  189 */   public int ofAaLevel = 0;
/*  190 */   public int ofAfLevel = 1;
/*  191 */   public int ofClouds = 0;
/*  192 */   public float ofCloudsHeight = 0.0F;
/*  193 */   public int ofTrees = 0;
/*  194 */   public int ofRain = 0;
/*  195 */   public int ofDroppedItems = 0;
/*  196 */   public int ofBetterGrass = 3;
/*  197 */   public int ofAutoSaveTicks = 4000;
/*      */   public boolean ofLagometer = false;
/*      */   public boolean ofProfiler = false;
/*      */   public boolean ofShowFps = false;
/*      */   public boolean ofWeather = true;
/*      */   public boolean ofSky = true;
/*      */   public boolean ofStars = true;
/*      */   public boolean ofSunMoon = true;
/*  205 */   public int ofVignette = 0;
/*  206 */   public int ofChunkUpdates = 1;
/*      */   public boolean ofChunkUpdatesDynamic = false;
/*  208 */   public int ofTime = 0;
/*      */   public boolean ofClearWater = false;
/*      */   public boolean ofBetterSnow = false;
/*  211 */   public String ofFullscreenMode = "Default";
/*      */   public boolean ofSwampColors = true;
/*      */   public boolean ofRandomEntities = true;
/*      */   public boolean ofSmoothBiomes = true;
/*      */   public boolean ofCustomFonts = true;
/*      */   public boolean ofCustomColors = true;
/*      */   public boolean ofCustomSky = true;
/*      */   public boolean ofShowCapes = true;
/*  219 */   public int ofConnectedTextures = 2;
/*      */   public boolean ofCustomItems = true;
/*      */   public boolean ofNaturalTextures = false;
/*      */   public boolean ofEmissiveTextures = true;
/*      */   public boolean ofFastMath = false;
/*      */   public boolean ofFastRender = false;
/*  225 */   public int ofTranslucentBlocks = 0;
/*      */   public boolean ofDynamicFov = true;
/*      */   public boolean ofAlternateBlocks = true;
/*  228 */   public int ofDynamicLights = 3;
/*      */   public boolean ofCustomEntityModels = true;
/*      */   public boolean ofCustomGuis = true;
/*      */   public boolean ofShowGlErrors = true;
/*  232 */   public int ofScreenshotSize = 1;
/*  233 */   public int ofAnimatedWater = 0;
/*  234 */   public int ofAnimatedLava = 0;
/*      */   public boolean ofAnimatedFire = true;
/*      */   public boolean ofAnimatedPortal = true;
/*      */   public boolean ofAnimatedRedstone = true;
/*      */   public boolean ofAnimatedExplosion = true;
/*      */   public boolean ofAnimatedFlame = true;
/*      */   public boolean ofAnimatedSmoke = true;
/*      */   public boolean ofVoidParticles = true;
/*      */   public boolean ofWaterParticles = true;
/*      */   public boolean ofRainSplash = true;
/*      */   public boolean ofPortalParticles = true;
/*      */   public boolean ofPotionParticles = true;
/*      */   public boolean ofFireworkParticles = true;
/*      */   public boolean ofDrippingWaterLava = true;
/*      */   public boolean ofAnimatedTerrain = true;
/*      */   public boolean ofAnimatedTextures = true;
/*      */   public static final int DEFAULT = 0;
/*      */   public static final int FAST = 1;
/*      */   public static final int FANCY = 2;
/*      */   public static final int OFF = 3;
/*      */   public static final int SMART = 4;
/*      */   public static final int ANIM_ON = 0;
/*      */   public static final int ANIM_GENERATED = 1;
/*      */   public static final int ANIM_OFF = 2;
/*      */   public static final String DEFAULT_STR = "Default";
/*  259 */   private static final int[] OF_TREES_VALUES = new int[] { 0, 1, 4, 2 };
/*  260 */   private static final int[] OF_DYNAMIC_LIGHTS = new int[] { 3, 1, 2 };
/*  261 */   private static final String[] KEYS_DYNAMIC_LIGHTS = new String[] { "options.off", "options.graphics.fast", "options.graphics.fancy" };
/*      */   
/*      */   public KeyBinding ofKeyBindZoom;
/*      */   private File optionsFileOF;
/*      */   
/*      */   public GameSettings(Minecraft mcIn, File optionsFileIn) {
/*  267 */     this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines }, (Object[])this.keyBindsHotbar);
/*  268 */     this.difficulty = EnumDifficulty.NORMAL;
/*  269 */     this.lastServer = "";
/*  270 */     this.fovSetting = 70.0F;
/*  271 */     this.language = "en_US";
/*  272 */     this.forceUnicodeFont = false;
/*  273 */     this.mc = mcIn;
/*  274 */     this.optionsFile = new File(optionsFileIn, "options.txt");
/*      */     
/*  276 */     if (mcIn.isJava64bit() && Runtime.getRuntime().maxMemory() >= 1000000000L) {
/*      */       
/*  278 */       Options.RENDER_DISTANCE.setValueMax(32.0F);
/*  279 */       long i = 1000000L;
/*      */       
/*  281 */       if (Runtime.getRuntime().maxMemory() >= 1500L * i)
/*      */       {
/*  283 */         Options.RENDER_DISTANCE.setValueMax(48.0F);
/*      */       }
/*      */       
/*  286 */       if (Runtime.getRuntime().maxMemory() >= 2500L * i)
/*      */       {
/*  288 */         Options.RENDER_DISTANCE.setValueMax(64.0F);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  293 */       Options.RENDER_DISTANCE.setValueMax(16.0F);
/*      */     } 
/*      */     
/*  296 */     this.renderDistanceChunks = mcIn.isJava64bit() ? 12 : 8;
/*  297 */     this.optionsFileOF = new File(optionsFileIn, "optionsof.txt");
/*  298 */     this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
/*  299 */     this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
/*  300 */     this.keyBindings = (KeyBinding[])ArrayUtils.add((Object[])this.keyBindings, this.ofKeyBindZoom);
/*  301 */     KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[] { this.ofKeyBindZoom });
/*  302 */     this.renderDistanceChunks = 8;
/*  303 */     loadOptions();
/*  304 */     Config.initGameSettings(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public GameSettings() {
/*  309 */     this.keyBindings = (KeyBinding[])ArrayUtils.addAll((Object[])new KeyBinding[] { this.keyBindAttack, this.keyBindUseItem, this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindSprint, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindPlayerList, this.keyBindPickBlock, this.keyBindCommand, this.keyBindScreenshot, this.keyBindTogglePerspective, this.keyBindSmoothCamera, this.keyBindStreamStartStop, this.keyBindStreamPauseUnpause, this.keyBindStreamCommercials, this.keyBindStreamToggleMic, this.keyBindFullscreen, this.keyBindSpectatorOutlines }, (Object[])this.keyBindsHotbar);
/*  310 */     this.difficulty = EnumDifficulty.NORMAL;
/*  311 */     this.lastServer = "";
/*  312 */     this.fovSetting = 70.0F;
/*  313 */     this.language = "en_US";
/*  314 */     this.forceUnicodeFont = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getKeyDisplayString(int key) {
/*  319 */     return (key < 0) ? I18n.format("key.mouseButton", new Object[] { Integer.valueOf(key + 101) }) : ((key < 256) ? Keyboard.getKeyName(key) : String.format("%c", new Object[] { Character.valueOf((char)(key - 256)) }).toUpperCase());
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isKeyDown(KeyBinding key) {
/*  324 */     return (key.getKeyCode() == 0) ? false : ((key.getKeyCode() < 0) ? Mouse.isButtonDown(key.getKeyCode() + 100) : Keyboard.isKeyDown(key.getKeyCode()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOptionKeyBinding(KeyBinding key, int keyCode) {
/*  329 */     key.setKeyCode(keyCode);
/*  330 */     saveOptions();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOptionFloatValue(Options settingsOption, float value) {
/*  335 */     setOptionFloatValueOF(settingsOption, value);
/*      */     
/*  337 */     if (settingsOption == Options.SENSITIVITY)
/*      */     {
/*  339 */       this.mouseSensitivity = value;
/*      */     }
/*      */     
/*  342 */     if (settingsOption == Options.FOV)
/*      */     {
/*  344 */       this.fovSetting = value;
/*      */     }
/*      */     
/*  347 */     if (settingsOption == Options.GAMMA)
/*      */     {
/*  349 */       this.gammaSetting = value;
/*      */     }
/*      */     
/*  352 */     if (settingsOption == Options.FRAMERATE_LIMIT) {
/*      */       
/*  354 */       this.limitFramerate = (int)value;
/*  355 */       this.enableVsync = false;
/*      */       
/*  357 */       if (this.limitFramerate <= 0) {
/*      */         
/*  359 */         this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
/*  360 */         this.enableVsync = true;
/*      */       } 
/*      */       
/*  363 */       updateVSync();
/*      */     } 
/*      */     
/*  366 */     if (settingsOption == Options.CHAT_OPACITY) {
/*      */       
/*  368 */       this.chatOpacity = value;
/*  369 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     } 
/*      */     
/*  372 */     if (settingsOption == Options.CHAT_HEIGHT_FOCUSED) {
/*      */       
/*  374 */       this.chatHeightFocused = value;
/*  375 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     } 
/*      */     
/*  378 */     if (settingsOption == Options.CHAT_HEIGHT_UNFOCUSED) {
/*      */       
/*  380 */       this.chatHeightUnfocused = value;
/*  381 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     } 
/*      */     
/*  384 */     if (settingsOption == Options.CHAT_WIDTH) {
/*      */       
/*  386 */       this.chatWidth = value;
/*  387 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     } 
/*      */     
/*  390 */     if (settingsOption == Options.CHAT_SCALE) {
/*      */       
/*  392 */       this.chatScale = value;
/*  393 */       this.mc.ingameGUI.getChatGUI().refreshChat();
/*      */     } 
/*      */     
/*  396 */     if (settingsOption == Options.MIPMAP_LEVELS) {
/*      */       
/*  398 */       int i = this.mipmapLevels;
/*  399 */       this.mipmapLevels = (int)value;
/*      */       
/*  401 */       if (i != value) {
/*      */         
/*  403 */         this.mc.getTextureMapBlocks().setMipmapLevels(this.mipmapLevels);
/*  404 */         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/*  405 */         this.mc.getTextureMapBlocks().setBlurMipmapDirect(false, (this.mipmapLevels > 0));
/*  406 */         this.mc.scheduleResourcesRefresh();
/*      */       } 
/*      */     } 
/*      */     
/*  410 */     if (settingsOption == Options.BLOCK_ALTERNATIVES) {
/*      */       
/*  412 */       this.allowBlockAlternatives = !this.allowBlockAlternatives;
/*  413 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/*  416 */     if (settingsOption == Options.RENDER_DISTANCE) {
/*      */       
/*  418 */       this.renderDistanceChunks = (int)value;
/*  419 */       this.mc.renderGlobal.setDisplayListEntitiesDirty();
/*      */     } 
/*      */     
/*  422 */     if (settingsOption == Options.STREAM_BYTES_PER_PIXEL)
/*      */     {
/*  424 */       this.streamBytesPerPixel = value;
/*      */     }
/*      */     
/*  427 */     if (settingsOption == Options.STREAM_KBPS)
/*      */     {
/*  429 */       this.streamKbps = value;
/*      */     }
/*      */     
/*  432 */     if (settingsOption == Options.STREAM_FPS)
/*      */     {
/*  434 */       this.streamFps = value;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOptionValue(Options settingsOption, int value) {
/*  440 */     setOptionValueOF(settingsOption, value);
/*      */     
/*  442 */     if (settingsOption == Options.INVERT_MOUSE)
/*      */     {
/*  444 */       this.invertMouse = !this.invertMouse;
/*      */     }
/*      */     
/*  447 */     if (settingsOption == Options.GUI_SCALE) {
/*      */       
/*  449 */       this.guiScale += value;
/*      */       
/*  451 */       if (GuiScreen.isShiftKeyDown())
/*      */       {
/*  453 */         this.guiScale = 0;
/*      */       }
/*      */       
/*  456 */       DisplayMode displaymode = Config.getLargestDisplayMode();
/*  457 */       int i = displaymode.getWidth() / 320;
/*  458 */       int j = displaymode.getHeight() / 240;
/*  459 */       int k = Math.min(i, j);
/*      */       
/*  461 */       if (this.guiScale < 0)
/*      */       {
/*  463 */         this.guiScale = k - 1;
/*      */       }
/*      */       
/*  466 */       if (this.mc.isUnicode() && this.guiScale % 2 != 0)
/*      */       {
/*  468 */         this.guiScale += value;
/*      */       }
/*      */       
/*  471 */       if (this.guiScale < 0 || this.guiScale >= k)
/*      */       {
/*  473 */         this.guiScale = 0;
/*      */       }
/*      */     } 
/*      */     
/*  477 */     if (settingsOption == Options.PARTICLES)
/*      */     {
/*  479 */       this.particleSetting = (this.particleSetting + value) % 3;
/*      */     }
/*      */     
/*  482 */     if (settingsOption == Options.VIEW_BOBBING)
/*      */     {
/*  484 */       this.viewBobbing = !this.viewBobbing;
/*      */     }
/*      */     
/*  487 */     if (settingsOption == Options.RENDER_CLOUDS)
/*      */     {
/*  489 */       this.clouds = (this.clouds + value) % 3;
/*      */     }
/*      */     
/*  492 */     if (settingsOption == Options.FORCE_UNICODE_FONT) {
/*      */       
/*  494 */       this.forceUnicodeFont = !this.forceUnicodeFont;
/*  495 */       this.mc.fontRendererObj.setUnicodeFlag((this.mc.getLanguageManager().isCurrentLocaleUnicode() || this.forceUnicodeFont));
/*      */     } 
/*      */     
/*  498 */     if (settingsOption == Options.FBO_ENABLE)
/*      */     {
/*  500 */       this.fboEnable = !this.fboEnable;
/*      */     }
/*      */     
/*  503 */     if (settingsOption == Options.ANAGLYPH) {
/*      */       
/*  505 */       if (!this.anaglyph && Config.isShaders()) {
/*      */         
/*  507 */         Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
/*      */         
/*      */         return;
/*      */       } 
/*  511 */       this.anaglyph = !this.anaglyph;
/*  512 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/*  515 */     if (settingsOption == Options.GRAPHICS) {
/*      */       
/*  517 */       this.fancyGraphics = !this.fancyGraphics;
/*  518 */       updateRenderClouds();
/*  519 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/*  522 */     if (settingsOption == Options.AMBIENT_OCCLUSION) {
/*      */       
/*  524 */       this.ambientOcclusion = (this.ambientOcclusion + value) % 3;
/*  525 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/*  528 */     if (settingsOption == Options.CHAT_VISIBILITY)
/*      */     {
/*  530 */       this.chatVisibility = EntityPlayer.EnumChatVisibility.getEnumChatVisibility((this.chatVisibility.getChatVisibility() + value) % 3);
/*      */     }
/*      */     
/*  533 */     if (settingsOption == Options.STREAM_COMPRESSION)
/*      */     {
/*  535 */       this.streamCompression = (this.streamCompression + value) % 3;
/*      */     }
/*      */     
/*  538 */     if (settingsOption == Options.STREAM_SEND_METADATA)
/*      */     {
/*  540 */       this.streamSendMetadata = !this.streamSendMetadata;
/*      */     }
/*      */     
/*  543 */     if (settingsOption == Options.STREAM_CHAT_ENABLED)
/*      */     {
/*  545 */       this.streamChatEnabled = (this.streamChatEnabled + value) % 3;
/*      */     }
/*      */     
/*  548 */     if (settingsOption == Options.STREAM_CHAT_USER_FILTER)
/*      */     {
/*  550 */       this.streamChatUserFilter = (this.streamChatUserFilter + value) % 3;
/*      */     }
/*      */     
/*  553 */     if (settingsOption == Options.STREAM_MIC_TOGGLE_BEHAVIOR)
/*      */     {
/*  555 */       this.streamMicToggleBehavior = (this.streamMicToggleBehavior + value) % 2;
/*      */     }
/*      */     
/*  558 */     if (settingsOption == Options.CHAT_COLOR)
/*      */     {
/*  560 */       this.chatColours = !this.chatColours;
/*      */     }
/*      */     
/*  563 */     if (settingsOption == Options.CHAT_LINKS)
/*      */     {
/*  565 */       this.chatLinks = !this.chatLinks;
/*      */     }
/*      */     
/*  568 */     if (settingsOption == Options.CHAT_LINKS_PROMPT)
/*      */     {
/*  570 */       this.chatLinksPrompt = !this.chatLinksPrompt;
/*      */     }
/*      */     
/*  573 */     if (settingsOption == Options.SNOOPER_ENABLED)
/*      */     {
/*  575 */       this.snooperEnabled = !this.snooperEnabled;
/*      */     }
/*      */     
/*  578 */     if (settingsOption == Options.TOUCHSCREEN)
/*      */     {
/*  580 */       this.touchscreen = !this.touchscreen;
/*      */     }
/*      */     
/*  583 */     if (settingsOption == Options.USE_FULLSCREEN) {
/*      */       
/*  585 */       this.fullScreen = !this.fullScreen;
/*      */       
/*  587 */       if (this.mc.isFullScreen() != this.fullScreen)
/*      */       {
/*  589 */         this.mc.toggleFullscreen();
/*      */       }
/*      */     } 
/*      */     
/*  593 */     if (settingsOption == Options.ENABLE_VSYNC) {
/*      */       
/*  595 */       this.enableVsync = !this.enableVsync;
/*  596 */       Display.setVSyncEnabled(this.enableVsync);
/*      */     } 
/*      */     
/*  599 */     if (settingsOption == Options.USE_VBO) {
/*      */       
/*  601 */       this.useVbo = !this.useVbo;
/*  602 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/*  605 */     if (settingsOption == Options.BLOCK_ALTERNATIVES) {
/*      */       
/*  607 */       this.allowBlockAlternatives = !this.allowBlockAlternatives;
/*  608 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/*  611 */     if (settingsOption == Options.REDUCED_DEBUG_INFO)
/*      */     {
/*  613 */       this.reducedDebugInfo = !this.reducedDebugInfo;
/*      */     }
/*      */     
/*  616 */     if (settingsOption == Options.ENTITY_SHADOWS)
/*      */     {
/*  618 */       this.entityShadows = !this.entityShadows;
/*      */     }
/*      */     
/*  621 */     if (settingsOption == Options.REALMS_NOTIFICATIONS)
/*      */     {
/*  623 */       this.realmsNotifications = !this.realmsNotifications;
/*      */     }
/*      */     
/*  626 */     saveOptions();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getOptionFloatValue(Options settingOption) {
/*  631 */     float f = getOptionFloatValueOF(settingOption);
/*  632 */     return (f != Float.MAX_VALUE) ? f : ((settingOption == Options.FOV) ? this.fovSetting : ((settingOption == Options.GAMMA) ? this.gammaSetting : ((settingOption == Options.SATURATION) ? this.saturation : ((settingOption == Options.SENSITIVITY) ? this.mouseSensitivity : ((settingOption == Options.CHAT_OPACITY) ? this.chatOpacity : ((settingOption == Options.CHAT_HEIGHT_FOCUSED) ? this.chatHeightFocused : ((settingOption == Options.CHAT_HEIGHT_UNFOCUSED) ? this.chatHeightUnfocused : ((settingOption == Options.CHAT_SCALE) ? this.chatScale : ((settingOption == Options.CHAT_WIDTH) ? this.chatWidth : ((settingOption == Options.FRAMERATE_LIMIT) ? this.limitFramerate : ((settingOption == Options.MIPMAP_LEVELS) ? this.mipmapLevels : ((settingOption == Options.RENDER_DISTANCE) ? this.renderDistanceChunks : ((settingOption == Options.STREAM_BYTES_PER_PIXEL) ? this.streamBytesPerPixel : ((settingOption == Options.STREAM_VOLUME_MIC) ? this.streamMicVolume : ((settingOption == Options.STREAM_VOLUME_SYSTEM) ? this.streamGameVolume : ((settingOption == Options.STREAM_KBPS) ? this.streamKbps : ((settingOption == Options.STREAM_FPS) ? this.streamFps : 0.0F)))))))))))))))));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getOptionOrdinalValue(Options settingOption) {
/*  637 */     switch (settingOption) {
/*      */       
/*      */       case INVERT_MOUSE:
/*  640 */         return this.invertMouse;
/*      */       
/*      */       case VIEW_BOBBING:
/*  643 */         return this.viewBobbing;
/*      */       
/*      */       case ANAGLYPH:
/*  646 */         return this.anaglyph;
/*      */       
/*      */       case FBO_ENABLE:
/*  649 */         return this.fboEnable;
/*      */       
/*      */       case CHAT_COLOR:
/*  652 */         return this.chatColours;
/*      */       
/*      */       case CHAT_LINKS:
/*  655 */         return this.chatLinks;
/*      */       
/*      */       case CHAT_LINKS_PROMPT:
/*  658 */         return this.chatLinksPrompt;
/*      */       
/*      */       case SNOOPER_ENABLED:
/*  661 */         return this.snooperEnabled;
/*      */       
/*      */       case USE_FULLSCREEN:
/*  664 */         return this.fullScreen;
/*      */       
/*      */       case ENABLE_VSYNC:
/*  667 */         return this.enableVsync;
/*      */       
/*      */       case USE_VBO:
/*  670 */         return this.useVbo;
/*      */       
/*      */       case TOUCHSCREEN:
/*  673 */         return this.touchscreen;
/*      */       
/*      */       case STREAM_SEND_METADATA:
/*  676 */         return this.streamSendMetadata;
/*      */       
/*      */       case FORCE_UNICODE_FONT:
/*  679 */         return this.forceUnicodeFont;
/*      */       
/*      */       case BLOCK_ALTERNATIVES:
/*  682 */         return this.allowBlockAlternatives;
/*      */       
/*      */       case REDUCED_DEBUG_INFO:
/*  685 */         return this.reducedDebugInfo;
/*      */       
/*      */       case ENTITY_SHADOWS:
/*  688 */         return this.entityShadows;
/*      */       
/*      */       case REALMS_NOTIFICATIONS:
/*  691 */         return this.realmsNotifications;
/*      */     } 
/*      */     
/*  694 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getTranslation(String[] strArray, int index) {
/*  700 */     if (index < 0 || index >= strArray.length)
/*      */     {
/*  702 */       index = 0;
/*      */     }
/*      */     
/*  705 */     return I18n.format(strArray[index], new Object[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getKeyBinding(Options settingOption) {
/*  710 */     String s = getKeyBindingOF(settingOption);
/*      */     
/*  712 */     if (s != null)
/*      */     {
/*  714 */       return s;
/*      */     }
/*      */ 
/*      */     
/*  718 */     String s1 = I18n.format(settingOption.getEnumString(), new Object[0]) + ": ";
/*      */     
/*  720 */     if (settingOption.getEnumFloat()) {
/*      */       
/*  722 */       float f1 = getOptionFloatValue(settingOption);
/*  723 */       float f = settingOption.normalizeValue(f1);
/*  724 */       return (settingOption == Options.MIPMAP_LEVELS && f1 >= 4.0D) ? (s1 + Lang.get("of.general.max")) : ((settingOption == Options.SENSITIVITY) ? ((f == 0.0F) ? (s1 + I18n.format("options.sensitivity.min", new Object[0])) : ((f == 1.0F) ? (s1 + I18n.format("options.sensitivity.max", new Object[0])) : (s1 + (int)(f * 200.0F) + "%"))) : ((settingOption == Options.FOV) ? ((f1 == 70.0F) ? (s1 + I18n.format("options.fov.min", new Object[0])) : ((f1 == 110.0F) ? (s1 + I18n.format("options.fov.max", new Object[0])) : (s1 + (int)f1))) : ((settingOption == Options.FRAMERATE_LIMIT) ? ((f1 == settingOption.valueMax) ? (s1 + I18n.format("options.framerateLimit.max", new Object[0])) : (s1 + (int)f1 + " fps")) : ((settingOption == Options.RENDER_CLOUDS) ? ((f1 == settingOption.valueMin) ? (s1 + I18n.format("options.cloudHeight.min", new Object[0])) : (s1 + ((int)f1 + 128))) : ((settingOption == Options.GAMMA) ? ((f == 0.0F) ? (s1 + I18n.format("options.gamma.min", new Object[0])) : ((f == 1.0F) ? (s1 + I18n.format("options.gamma.max", new Object[0])) : (s1 + "+" + (int)(f * 100.0F) + "%"))) : ((settingOption == Options.SATURATION) ? (s1 + (int)(f * 400.0F) + "%") : ((settingOption == Options.CHAT_OPACITY) ? (s1 + (int)(f * 90.0F + 10.0F) + "%") : ((settingOption == Options.CHAT_HEIGHT_UNFOCUSED) ? (s1 + GuiNewChat.calculateChatboxHeight(f) + "px") : ((settingOption == Options.CHAT_HEIGHT_FOCUSED) ? (s1 + GuiNewChat.calculateChatboxHeight(f) + "px") : ((settingOption == Options.CHAT_WIDTH) ? (s1 + GuiNewChat.calculateChatboxWidth(f) + "px") : ((settingOption == Options.RENDER_DISTANCE) ? (s1 + (int)f1 + " chunks") : ((settingOption == Options.MIPMAP_LEVELS) ? ((f1 == 0.0F) ? (s1 + I18n.format("options.off", new Object[0])) : (s1 + (int)f1)) : ((f == 0.0F) ? (s1 + I18n.format("options.off", new Object[0])) : (s1 + (int)(f * 100.0F) + "%"))))))))))))));
/*      */     } 
/*  726 */     if (settingOption.getEnumBoolean()) {
/*      */       
/*  728 */       boolean flag = getOptionOrdinalValue(settingOption);
/*  729 */       return flag ? (s1 + I18n.format("options.on", new Object[0])) : (s1 + I18n.format("options.off", new Object[0]));
/*      */     } 
/*  731 */     if (settingOption == Options.GUI_SCALE)
/*      */     {
/*  733 */       return (this.guiScale >= GUISCALES.length) ? (s1 + this.guiScale + "x") : (s1 + getTranslation(GUISCALES, this.guiScale));
/*      */     }
/*  735 */     if (settingOption == Options.CHAT_VISIBILITY)
/*      */     {
/*  737 */       return s1 + I18n.format(this.chatVisibility.getResourceKey(), new Object[0]);
/*      */     }
/*  739 */     if (settingOption == Options.PARTICLES)
/*      */     {
/*  741 */       return s1 + getTranslation(PARTICLES, this.particleSetting);
/*      */     }
/*  743 */     if (settingOption == Options.AMBIENT_OCCLUSION)
/*      */     {
/*  745 */       return s1 + getTranslation(AMBIENT_OCCLUSIONS, this.ambientOcclusion);
/*      */     }
/*  747 */     if (settingOption == Options.STREAM_COMPRESSION)
/*      */     {
/*  749 */       return s1 + getTranslation(STREAM_COMPRESSIONS, this.streamCompression);
/*      */     }
/*  751 */     if (settingOption == Options.STREAM_CHAT_ENABLED)
/*      */     {
/*  753 */       return s1 + getTranslation(STREAM_CHAT_MODES, this.streamChatEnabled);
/*      */     }
/*  755 */     if (settingOption == Options.STREAM_CHAT_USER_FILTER)
/*      */     {
/*  757 */       return s1 + getTranslation(STREAM_CHAT_FILTER_MODES, this.streamChatUserFilter);
/*      */     }
/*  759 */     if (settingOption == Options.STREAM_MIC_TOGGLE_BEHAVIOR)
/*      */     {
/*  761 */       return s1 + getTranslation(STREAM_MIC_MODES, this.streamMicToggleBehavior);
/*      */     }
/*  763 */     if (settingOption == Options.RENDER_CLOUDS)
/*      */     {
/*  765 */       return s1 + getTranslation(CLOUDS_TYPES, this.clouds);
/*      */     }
/*  767 */     if (settingOption == Options.GRAPHICS) {
/*      */       
/*  769 */       if (this.fancyGraphics)
/*      */       {
/*  771 */         return s1 + I18n.format("options.graphics.fancy", new Object[0]);
/*      */       }
/*      */ 
/*      */       
/*  775 */       String s2 = "options.graphics.fast";
/*  776 */       return s1 + I18n.format("options.graphics.fast", new Object[0]);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  781 */     return s1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadOptions() {
/*  788 */     FileInputStream fileinputstream = null;
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
/*      */     try {
/*      */     
/* 1186 */     } catch (Exception exception1) {
/*      */       
/* 1188 */       logger.error("Failed to load options", exception1);
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/* 1193 */       IOUtils.closeQuietly(fileinputstream);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1198 */     loadOfOptions();
/*      */   }
/*      */ 
/*      */   
/*      */   private float parseFloat(String str) {
/* 1203 */     return str.equals("true") ? 1.0F : (str.equals("false") ? 0.0F : Float.parseFloat(str));
/*      */   }
/*      */ 
/*      */   
/*      */   public void saveOptions() {
/* 1208 */     if (Reflector.FMLClientHandler.exists()) {
/*      */       
/* 1210 */       Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
/*      */       
/* 1212 */       if (object != null && Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading, new Object[0])) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1220 */       PrintWriter printwriter = new PrintWriter(new FileWriter(this.optionsFile));
/* 1221 */       printwriter.println("invertYMouse:" + this.invertMouse);
/* 1222 */       printwriter.println("mouseSensitivity:" + this.mouseSensitivity);
/* 1223 */       printwriter.println("fov:" + ((this.fovSetting - 70.0F) / 40.0F));
/* 1224 */       printwriter.println("gamma:" + this.gammaSetting);
/* 1225 */       printwriter.println("saturation:" + this.saturation);
/* 1226 */       printwriter.println("renderDistance:" + this.renderDistanceChunks);
/* 1227 */       printwriter.println("guiScale:" + this.guiScale);
/* 1228 */       printwriter.println("particles:" + this.particleSetting);
/* 1229 */       printwriter.println("bobView:" + this.viewBobbing);
/* 1230 */       printwriter.println("anaglyph3d:" + this.anaglyph);
/* 1231 */       printwriter.println("maxFps:" + this.limitFramerate);
/* 1232 */       printwriter.println("fboEnable:" + this.fboEnable);
/* 1233 */       printwriter.println("difficulty:" + this.difficulty.getDifficultyId());
/* 1234 */       printwriter.println("fancyGraphics:" + this.fancyGraphics);
/* 1235 */       printwriter.println("ao:" + this.ambientOcclusion);
/*      */       
/* 1237 */       switch (this.clouds) {
/*      */         
/*      */         case 0:
/* 1240 */           printwriter.println("renderClouds:false");
/*      */           break;
/*      */         
/*      */         case 1:
/* 1244 */           printwriter.println("renderClouds:fast");
/*      */           break;
/*      */         
/*      */         case 2:
/* 1248 */           printwriter.println("renderClouds:true");
/*      */           break;
/*      */       } 
/* 1251 */       printwriter.println("resourcePacks:" + gson.toJson(this.resourcePacks));
/* 1252 */       printwriter.println("incompatibleResourcePacks:" + gson.toJson(this.incompatibleResourcePacks));
/* 1253 */       printwriter.println("lastServer:" + this.lastServer);
/* 1254 */       printwriter.println("lang:" + this.language);
/* 1255 */       printwriter.println("chatVisibility:" + this.chatVisibility.getChatVisibility());
/* 1256 */       printwriter.println("chatColors:" + this.chatColours);
/* 1257 */       printwriter.println("chatLinks:" + this.chatLinks);
/* 1258 */       printwriter.println("chatLinksPrompt:" + this.chatLinksPrompt);
/* 1259 */       printwriter.println("chatOpacity:" + this.chatOpacity);
/* 1260 */       printwriter.println("snooperEnabled:" + this.snooperEnabled);
/* 1261 */       printwriter.println("fullscreen:" + this.fullScreen);
/* 1262 */       printwriter.println("enableVsync:" + this.enableVsync);
/* 1263 */       printwriter.println("useVbo:" + this.useVbo);
/* 1264 */       printwriter.println("hideServerAddress:" + this.hideServerAddress);
/* 1265 */       printwriter.println("advancedItemTooltips:" + this.advancedItemTooltips);
/* 1266 */       printwriter.println("pauseOnLostFocus:" + this.pauseOnLostFocus);
/* 1267 */       printwriter.println("touchscreen:" + this.touchscreen);
/* 1268 */       printwriter.println("overrideWidth:" + this.overrideWidth);
/* 1269 */       printwriter.println("overrideHeight:" + this.overrideHeight);
/* 1270 */       printwriter.println("heldItemTooltips:" + this.heldItemTooltips);
/* 1271 */       printwriter.println("chatHeightFocused:" + this.chatHeightFocused);
/* 1272 */       printwriter.println("chatHeightUnfocused:" + this.chatHeightUnfocused);
/* 1273 */       printwriter.println("chatScale:" + this.chatScale);
/* 1274 */       printwriter.println("chatWidth:" + this.chatWidth);
/* 1275 */       printwriter.println("showInventoryAchievementHint:" + this.showInventoryAchievementHint);
/* 1276 */       printwriter.println("mipmapLevels:" + this.mipmapLevels);
/* 1277 */       printwriter.println("streamBytesPerPixel:" + this.streamBytesPerPixel);
/* 1278 */       printwriter.println("streamMicVolume:" + this.streamMicVolume);
/* 1279 */       printwriter.println("streamSystemVolume:" + this.streamGameVolume);
/* 1280 */       printwriter.println("streamKbps:" + this.streamKbps);
/* 1281 */       printwriter.println("streamFps:" + this.streamFps);
/* 1282 */       printwriter.println("streamCompression:" + this.streamCompression);
/* 1283 */       printwriter.println("streamSendMetadata:" + this.streamSendMetadata);
/* 1284 */       printwriter.println("streamPreferredServer:" + this.streamPreferredServer);
/* 1285 */       printwriter.println("streamChatEnabled:" + this.streamChatEnabled);
/* 1286 */       printwriter.println("streamChatUserFilter:" + this.streamChatUserFilter);
/* 1287 */       printwriter.println("streamMicToggleBehavior:" + this.streamMicToggleBehavior);
/* 1288 */       printwriter.println("forceUnicodeFont:" + this.forceUnicodeFont);
/* 1289 */       printwriter.println("allowBlockAlternatives:" + this.allowBlockAlternatives);
/* 1290 */       printwriter.println("reducedDebugInfo:" + this.reducedDebugInfo);
/* 1291 */       printwriter.println("useNativeTransport:" + this.useNativeTransport);
/* 1292 */       printwriter.println("entityShadows:" + this.entityShadows);
/* 1293 */       printwriter.println("realmsNotifications:" + this.realmsNotifications);
/*      */       
/* 1295 */       for (KeyBinding keybinding : this.keyBindings)
/*      */       {
/* 1297 */         printwriter.println("key_" + keybinding.getKeyDescription() + ":" + keybinding.getKeyCode());
/*      */       }
/*      */       
/* 1300 */       for (SoundCategory soundcategory : SoundCategory.values())
/*      */       {
/* 1302 */         printwriter.println("soundCategory_" + soundcategory.getCategoryName() + ":" + getSoundLevel(soundcategory));
/*      */       }
/*      */       
/* 1305 */       for (EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values())
/*      */       {
/* 1307 */         printwriter.println("modelPart_" + enumplayermodelparts.getPartName() + ":" + this.setModelParts.contains(enumplayermodelparts));
/*      */       }
/*      */       
/* 1310 */       printwriter.close();
/*      */     }
/* 1312 */     catch (Exception exception) {
/*      */       
/* 1314 */       logger.error("Failed to save options", exception);
/*      */     } 
/*      */     
/* 1317 */     saveOfOptions();
/* 1318 */     sendSettingsToServer();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSoundLevel(SoundCategory sndCategory) {
/* 1323 */     return this.mapSoundLevels.containsKey(sndCategory) ? ((Float)this.mapSoundLevels.get(sndCategory)).floatValue() : 1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSoundLevel(SoundCategory sndCategory, float soundLevel) {
/* 1328 */     this.mc.getSoundHandler().setSoundLevel(sndCategory, soundLevel);
/* 1329 */     this.mapSoundLevels.put(sndCategory, Float.valueOf(soundLevel));
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendSettingsToServer() {
/* 1334 */     if (this.mc.thePlayer != null) {
/*      */       
/* 1336 */       int i = 0;
/*      */       
/* 1338 */       for (EnumPlayerModelParts enumplayermodelparts : this.setModelParts)
/*      */       {
/* 1340 */         i |= enumplayermodelparts.getPartMask();
/*      */       }
/*      */       
/* 1343 */       this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C15PacketClientSettings(this.language, this.renderDistanceChunks, this.chatVisibility, this.chatColours, i));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<EnumPlayerModelParts> getModelParts() {
/* 1349 */     return (Set<EnumPlayerModelParts>)ImmutableSet.copyOf(this.setModelParts);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setModelPartEnabled(EnumPlayerModelParts modelPart, boolean enable) {
/* 1354 */     if (enable) {
/*      */       
/* 1356 */       this.setModelParts.add(modelPart);
/*      */     }
/*      */     else {
/*      */       
/* 1360 */       this.setModelParts.remove(modelPart);
/*      */     } 
/*      */     
/* 1363 */     sendSettingsToServer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void switchModelPartEnabled(EnumPlayerModelParts modelPart) {
/* 1368 */     if (!getModelParts().contains(modelPart)) {
/*      */       
/* 1370 */       this.setModelParts.add(modelPart);
/*      */     }
/*      */     else {
/*      */       
/* 1374 */       this.setModelParts.remove(modelPart);
/*      */     } 
/*      */     
/* 1377 */     sendSettingsToServer();
/*      */   }
/*      */ 
/*      */   
/*      */   public int shouldRenderClouds() {
/* 1382 */     return (this.renderDistanceChunks >= 4) ? this.clouds : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUsingNativeTransport() {
/* 1387 */     return this.useNativeTransport;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setOptionFloatValueOF(Options p_setOptionFloatValueOF_1_, float p_setOptionFloatValueOF_2_) {
/* 1392 */     if (p_setOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT) {
/*      */       
/* 1394 */       this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
/* 1395 */       this.mc.renderGlobal.resetClouds();
/*      */     } 
/*      */     
/* 1398 */     if (p_setOptionFloatValueOF_1_ == Options.AO_LEVEL) {
/*      */       
/* 1400 */       this.ofAoLevel = p_setOptionFloatValueOF_2_;
/* 1401 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1404 */     if (p_setOptionFloatValueOF_1_ == Options.AA_LEVEL) {
/*      */       
/* 1406 */       int i = (int)p_setOptionFloatValueOF_2_;
/*      */       
/* 1408 */       if (i > 0 && Config.isShaders()) {
/*      */         
/* 1410 */         Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
/*      */         
/*      */         return;
/*      */       } 
/* 1414 */       int[] aint = { 0, 2, 4, 6, 8, 12, 16 };
/* 1415 */       this.ofAaLevel = 0;
/*      */       
/* 1417 */       for (int j = 0; j < aint.length; j++) {
/*      */         
/* 1419 */         if (i >= aint[j])
/*      */         {
/* 1421 */           this.ofAaLevel = aint[j];
/*      */         }
/*      */       } 
/*      */       
/* 1425 */       this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
/*      */     } 
/*      */     
/* 1428 */     if (p_setOptionFloatValueOF_1_ == Options.AF_LEVEL) {
/*      */       
/* 1430 */       int k = (int)p_setOptionFloatValueOF_2_;
/*      */       
/* 1432 */       if (k > 1 && Config.isShaders()) {
/*      */         
/* 1434 */         Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
/*      */         
/*      */         return;
/*      */       } 
/* 1438 */       for (this.ofAfLevel = 1; this.ofAfLevel * 2 <= k; this.ofAfLevel *= 2);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1443 */       this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
/* 1444 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/* 1447 */     if (p_setOptionFloatValueOF_1_ == Options.MIPMAP_TYPE) {
/*      */       
/* 1449 */       int l = (int)p_setOptionFloatValueOF_2_;
/* 1450 */       this.ofMipmapType = Config.limit(l, 0, 3);
/* 1451 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/* 1454 */     if (p_setOptionFloatValueOF_1_ == Options.FULLSCREEN_MODE) {
/*      */       
/* 1456 */       int i1 = (int)p_setOptionFloatValueOF_2_ - 1;
/* 1457 */       String[] astring = Config.getDisplayModeNames();
/*      */       
/* 1459 */       if (i1 < 0 || i1 >= astring.length) {
/*      */         
/* 1461 */         this.ofFullscreenMode = "Default";
/*      */         
/*      */         return;
/*      */       } 
/* 1465 */       this.ofFullscreenMode = astring[i1];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private float getOptionFloatValueOF(Options p_getOptionFloatValueOF_1_) {
/* 1471 */     if (p_getOptionFloatValueOF_1_ == Options.CLOUD_HEIGHT)
/*      */     {
/* 1473 */       return this.ofCloudsHeight;
/*      */     }
/* 1475 */     if (p_getOptionFloatValueOF_1_ == Options.AO_LEVEL)
/*      */     {
/* 1477 */       return this.ofAoLevel;
/*      */     }
/* 1479 */     if (p_getOptionFloatValueOF_1_ == Options.AA_LEVEL)
/*      */     {
/* 1481 */       return this.ofAaLevel;
/*      */     }
/* 1483 */     if (p_getOptionFloatValueOF_1_ == Options.AF_LEVEL)
/*      */     {
/* 1485 */       return this.ofAfLevel;
/*      */     }
/* 1487 */     if (p_getOptionFloatValueOF_1_ == Options.MIPMAP_TYPE)
/*      */     {
/* 1489 */       return this.ofMipmapType;
/*      */     }
/* 1491 */     if (p_getOptionFloatValueOF_1_ == Options.FRAMERATE_LIMIT)
/*      */     {
/* 1493 */       return (this.limitFramerate == Options.FRAMERATE_LIMIT.getValueMax() && this.enableVsync) ? 0.0F : this.limitFramerate;
/*      */     }
/* 1495 */     if (p_getOptionFloatValueOF_1_ == Options.FULLSCREEN_MODE) {
/*      */       
/* 1497 */       if (this.ofFullscreenMode.equals("Default"))
/*      */       {
/* 1499 */         return 0.0F;
/*      */       }
/*      */ 
/*      */       
/* 1503 */       List<String> list = Arrays.asList(Config.getDisplayModeNames());
/* 1504 */       int i = list.indexOf(this.ofFullscreenMode);
/* 1505 */       return (i < 0) ? 0.0F : (i + 1);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1510 */     return Float.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void setOptionValueOF(Options p_setOptionValueOF_1_, int p_setOptionValueOF_2_) {
/* 1516 */     if (p_setOptionValueOF_1_ == Options.FOG_FANCY)
/*      */     {
/* 1518 */       switch (this.ofFogType) {
/*      */         
/*      */         case 1:
/* 1521 */           this.ofFogType = 2;
/*      */           
/* 1523 */           if (!Config.isFancyFogAvailable())
/*      */           {
/* 1525 */             this.ofFogType = 3;
/*      */           }
/*      */           break;
/*      */ 
/*      */         
/*      */         case 2:
/* 1531 */           this.ofFogType = 3;
/*      */           break;
/*      */         
/*      */         case 3:
/* 1535 */           this.ofFogType = 1;
/*      */           break;
/*      */         
/*      */         default:
/* 1539 */           this.ofFogType = 1;
/*      */           break;
/*      */       } 
/*      */     }
/* 1543 */     if (p_setOptionValueOF_1_ == Options.FOG_START) {
/*      */       
/* 1545 */       this.ofFogStart += 0.2F;
/*      */       
/* 1547 */       if (this.ofFogStart > 0.81F)
/*      */       {
/* 1549 */         this.ofFogStart = 0.2F;
/*      */       }
/*      */     } 
/*      */     
/* 1553 */     if (p_setOptionValueOF_1_ == Options.SMOOTH_FPS)
/*      */     {
/* 1555 */       this.ofSmoothFps = !this.ofSmoothFps;
/*      */     }
/*      */     
/* 1558 */     if (p_setOptionValueOF_1_ == Options.SMOOTH_WORLD) {
/*      */       
/* 1560 */       this.ofSmoothWorld = !this.ofSmoothWorld;
/* 1561 */       Config.updateThreadPriorities();
/*      */     } 
/*      */     
/* 1564 */     if (p_setOptionValueOF_1_ == Options.CLOUDS) {
/*      */       
/* 1566 */       this.ofClouds++;
/*      */       
/* 1568 */       if (this.ofClouds > 3)
/*      */       {
/* 1570 */         this.ofClouds = 0;
/*      */       }
/*      */       
/* 1573 */       updateRenderClouds();
/* 1574 */       this.mc.renderGlobal.resetClouds();
/*      */     } 
/*      */     
/* 1577 */     if (p_setOptionValueOF_1_ == Options.TREES) {
/*      */       
/* 1579 */       this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
/* 1580 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1583 */     if (p_setOptionValueOF_1_ == Options.DROPPED_ITEMS) {
/*      */       
/* 1585 */       this.ofDroppedItems++;
/*      */       
/* 1587 */       if (this.ofDroppedItems > 2)
/*      */       {
/* 1589 */         this.ofDroppedItems = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1593 */     if (p_setOptionValueOF_1_ == Options.RAIN) {
/*      */       
/* 1595 */       this.ofRain++;
/*      */       
/* 1597 */       if (this.ofRain > 3)
/*      */       {
/* 1599 */         this.ofRain = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1603 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_WATER) {
/*      */       
/* 1605 */       this.ofAnimatedWater++;
/*      */       
/* 1607 */       if (this.ofAnimatedWater == 1)
/*      */       {
/* 1609 */         this.ofAnimatedWater++;
/*      */       }
/*      */       
/* 1612 */       if (this.ofAnimatedWater > 2)
/*      */       {
/* 1614 */         this.ofAnimatedWater = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1618 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_LAVA) {
/*      */       
/* 1620 */       this.ofAnimatedLava++;
/*      */       
/* 1622 */       if (this.ofAnimatedLava == 1)
/*      */       {
/* 1624 */         this.ofAnimatedLava++;
/*      */       }
/*      */       
/* 1627 */       if (this.ofAnimatedLava > 2)
/*      */       {
/* 1629 */         this.ofAnimatedLava = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1633 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_FIRE)
/*      */     {
/* 1635 */       this.ofAnimatedFire = !this.ofAnimatedFire;
/*      */     }
/*      */     
/* 1638 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_PORTAL)
/*      */     {
/* 1640 */       this.ofAnimatedPortal = !this.ofAnimatedPortal;
/*      */     }
/*      */     
/* 1643 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_REDSTONE)
/*      */     {
/* 1645 */       this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
/*      */     }
/*      */     
/* 1648 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_EXPLOSION)
/*      */     {
/* 1650 */       this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
/*      */     }
/*      */     
/* 1653 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_FLAME)
/*      */     {
/* 1655 */       this.ofAnimatedFlame = !this.ofAnimatedFlame;
/*      */     }
/*      */     
/* 1658 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_SMOKE)
/*      */     {
/* 1660 */       this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
/*      */     }
/*      */     
/* 1663 */     if (p_setOptionValueOF_1_ == Options.VOID_PARTICLES)
/*      */     {
/* 1665 */       this.ofVoidParticles = !this.ofVoidParticles;
/*      */     }
/*      */     
/* 1668 */     if (p_setOptionValueOF_1_ == Options.WATER_PARTICLES)
/*      */     {
/* 1670 */       this.ofWaterParticles = !this.ofWaterParticles;
/*      */     }
/*      */     
/* 1673 */     if (p_setOptionValueOF_1_ == Options.PORTAL_PARTICLES)
/*      */     {
/* 1675 */       this.ofPortalParticles = !this.ofPortalParticles;
/*      */     }
/*      */     
/* 1678 */     if (p_setOptionValueOF_1_ == Options.POTION_PARTICLES)
/*      */     {
/* 1680 */       this.ofPotionParticles = !this.ofPotionParticles;
/*      */     }
/*      */     
/* 1683 */     if (p_setOptionValueOF_1_ == Options.FIREWORK_PARTICLES)
/*      */     {
/* 1685 */       this.ofFireworkParticles = !this.ofFireworkParticles;
/*      */     }
/*      */     
/* 1688 */     if (p_setOptionValueOF_1_ == Options.DRIPPING_WATER_LAVA)
/*      */     {
/* 1690 */       this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
/*      */     }
/*      */     
/* 1693 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_TERRAIN)
/*      */     {
/* 1695 */       this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
/*      */     }
/*      */     
/* 1698 */     if (p_setOptionValueOF_1_ == Options.ANIMATED_TEXTURES)
/*      */     {
/* 1700 */       this.ofAnimatedTextures = !this.ofAnimatedTextures;
/*      */     }
/*      */     
/* 1703 */     if (p_setOptionValueOF_1_ == Options.RAIN_SPLASH)
/*      */     {
/* 1705 */       this.ofRainSplash = !this.ofRainSplash;
/*      */     }
/*      */     
/* 1708 */     if (p_setOptionValueOF_1_ == Options.LAGOMETER)
/*      */     {
/* 1710 */       this.ofLagometer = !this.ofLagometer;
/*      */     }
/*      */     
/* 1713 */     if (p_setOptionValueOF_1_ == Options.SHOW_FPS)
/*      */     {
/* 1715 */       this.ofShowFps = !this.ofShowFps;
/*      */     }
/*      */     
/* 1718 */     if (p_setOptionValueOF_1_ == Options.AUTOSAVE_TICKS) {
/*      */       
/* 1720 */       int i = 900;
/* 1721 */       this.ofAutoSaveTicks = Math.max(this.ofAutoSaveTicks / i * i, i);
/* 1722 */       this.ofAutoSaveTicks *= 2;
/*      */       
/* 1724 */       if (this.ofAutoSaveTicks > 32 * i)
/*      */       {
/* 1726 */         this.ofAutoSaveTicks = i;
/*      */       }
/*      */     } 
/*      */     
/* 1730 */     if (p_setOptionValueOF_1_ == Options.BETTER_GRASS) {
/*      */       
/* 1732 */       this.ofBetterGrass++;
/*      */       
/* 1734 */       if (this.ofBetterGrass > 3)
/*      */       {
/* 1736 */         this.ofBetterGrass = 1;
/*      */       }
/*      */       
/* 1739 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1742 */     if (p_setOptionValueOF_1_ == Options.CONNECTED_TEXTURES) {
/*      */       
/* 1744 */       this.ofConnectedTextures++;
/*      */       
/* 1746 */       if (this.ofConnectedTextures > 3)
/*      */       {
/* 1748 */         this.ofConnectedTextures = 1;
/*      */       }
/*      */       
/* 1751 */       if (this.ofConnectedTextures == 2) {
/*      */         
/* 1753 */         this.mc.renderGlobal.loadRenderers();
/*      */       }
/*      */       else {
/*      */         
/* 1757 */         this.mc.refreshResources();
/*      */       } 
/*      */     } 
/*      */     
/* 1761 */     if (p_setOptionValueOF_1_ == Options.WEATHER)
/*      */     {
/* 1763 */       this.ofWeather = !this.ofWeather;
/*      */     }
/*      */     
/* 1766 */     if (p_setOptionValueOF_1_ == Options.SKY)
/*      */     {
/* 1768 */       this.ofSky = !this.ofSky;
/*      */     }
/*      */     
/* 1771 */     if (p_setOptionValueOF_1_ == Options.STARS)
/*      */     {
/* 1773 */       this.ofStars = !this.ofStars;
/*      */     }
/*      */     
/* 1776 */     if (p_setOptionValueOF_1_ == Options.SUN_MOON)
/*      */     {
/* 1778 */       this.ofSunMoon = !this.ofSunMoon;
/*      */     }
/*      */     
/* 1781 */     if (p_setOptionValueOF_1_ == Options.VIGNETTE) {
/*      */       
/* 1783 */       this.ofVignette++;
/*      */       
/* 1785 */       if (this.ofVignette > 2)
/*      */       {
/* 1787 */         this.ofVignette = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1791 */     if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES) {
/*      */       
/* 1793 */       this.ofChunkUpdates++;
/*      */       
/* 1795 */       if (this.ofChunkUpdates > 5)
/*      */       {
/* 1797 */         this.ofChunkUpdates = 1;
/*      */       }
/*      */     } 
/*      */     
/* 1801 */     if (p_setOptionValueOF_1_ == Options.CHUNK_UPDATES_DYNAMIC)
/*      */     {
/* 1803 */       this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
/*      */     }
/*      */     
/* 1806 */     if (p_setOptionValueOF_1_ == Options.TIME) {
/*      */       
/* 1808 */       this.ofTime++;
/*      */       
/* 1810 */       if (this.ofTime > 2)
/*      */       {
/* 1812 */         this.ofTime = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1816 */     if (p_setOptionValueOF_1_ == Options.CLEAR_WATER) {
/*      */       
/* 1818 */       this.ofClearWater = !this.ofClearWater;
/* 1819 */       updateWaterOpacity();
/*      */     } 
/*      */     
/* 1822 */     if (p_setOptionValueOF_1_ == Options.PROFILER)
/*      */     {
/* 1824 */       this.ofProfiler = !this.ofProfiler;
/*      */     }
/*      */     
/* 1827 */     if (p_setOptionValueOF_1_ == Options.BETTER_SNOW) {
/*      */       
/* 1829 */       this.ofBetterSnow = !this.ofBetterSnow;
/* 1830 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1833 */     if (p_setOptionValueOF_1_ == Options.SWAMP_COLORS) {
/*      */       
/* 1835 */       this.ofSwampColors = !this.ofSwampColors;
/* 1836 */       CustomColors.updateUseDefaultGrassFoliageColors();
/* 1837 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1840 */     if (p_setOptionValueOF_1_ == Options.RANDOM_ENTITIES) {
/*      */       
/* 1842 */       this.ofRandomEntities = !this.ofRandomEntities;
/* 1843 */       RandomEntities.update();
/*      */     } 
/*      */     
/* 1846 */     if (p_setOptionValueOF_1_ == Options.SMOOTH_BIOMES) {
/*      */       
/* 1848 */       this.ofSmoothBiomes = !this.ofSmoothBiomes;
/* 1849 */       CustomColors.updateUseDefaultGrassFoliageColors();
/* 1850 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1853 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_FONTS) {
/*      */       
/* 1855 */       this.ofCustomFonts = !this.ofCustomFonts;
/* 1856 */       this.mc.fontRendererObj.onResourceManagerReload(Config.getResourceManager());
/* 1857 */       this.mc.standardGalacticFontRenderer.onResourceManagerReload(Config.getResourceManager());
/*      */     } 
/*      */     
/* 1860 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_COLORS) {
/*      */       
/* 1862 */       this.ofCustomColors = !this.ofCustomColors;
/* 1863 */       CustomColors.update();
/* 1864 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1867 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_ITEMS) {
/*      */       
/* 1869 */       this.ofCustomItems = !this.ofCustomItems;
/* 1870 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/* 1873 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_SKY) {
/*      */       
/* 1875 */       this.ofCustomSky = !this.ofCustomSky;
/* 1876 */       CustomSky.update();
/*      */     } 
/*      */     
/* 1879 */     if (p_setOptionValueOF_1_ == Options.SHOW_CAPES)
/*      */     {
/* 1881 */       this.ofShowCapes = !this.ofShowCapes;
/*      */     }
/*      */     
/* 1884 */     if (p_setOptionValueOF_1_ == Options.NATURAL_TEXTURES) {
/*      */       
/* 1886 */       this.ofNaturalTextures = !this.ofNaturalTextures;
/* 1887 */       NaturalTextures.update();
/* 1888 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1891 */     if (p_setOptionValueOF_1_ == Options.EMISSIVE_TEXTURES) {
/*      */       
/* 1893 */       this.ofEmissiveTextures = !this.ofEmissiveTextures;
/* 1894 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/* 1897 */     if (p_setOptionValueOF_1_ == Options.FAST_MATH) {
/*      */       
/* 1899 */       this.ofFastMath = !this.ofFastMath;
/* 1900 */       MathHelper.fastMath = this.ofFastMath;
/*      */     } 
/*      */     
/* 1903 */     if (p_setOptionValueOF_1_ == Options.FAST_RENDER) {
/*      */       
/* 1905 */       if (!this.ofFastRender && Config.isShaders()) {
/*      */         
/* 1907 */         Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
/*      */         
/*      */         return;
/*      */       } 
/* 1911 */       this.ofFastRender = !this.ofFastRender;
/*      */       
/* 1913 */       if (this.ofFastRender)
/*      */       {
/* 1915 */         this.mc.entityRenderer.stopUseShader();
/*      */       }
/*      */       
/* 1918 */       Config.updateFramebufferSize();
/*      */     } 
/*      */     
/* 1921 */     if (p_setOptionValueOF_1_ == Options.TRANSLUCENT_BLOCKS) {
/*      */       
/* 1923 */       if (this.ofTranslucentBlocks == 0) {
/*      */         
/* 1925 */         this.ofTranslucentBlocks = 1;
/*      */       }
/* 1927 */       else if (this.ofTranslucentBlocks == 1) {
/*      */         
/* 1929 */         this.ofTranslucentBlocks = 2;
/*      */       }
/* 1931 */       else if (this.ofTranslucentBlocks == 2) {
/*      */         
/* 1933 */         this.ofTranslucentBlocks = 0;
/*      */       }
/*      */       else {
/*      */         
/* 1937 */         this.ofTranslucentBlocks = 0;
/*      */       } 
/*      */       
/* 1940 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1943 */     if (p_setOptionValueOF_1_ == Options.LAZY_CHUNK_LOADING)
/*      */     {
/* 1945 */       this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
/*      */     }
/*      */     
/* 1948 */     if (p_setOptionValueOF_1_ == Options.RENDER_REGIONS) {
/*      */       
/* 1950 */       this.ofRenderRegions = !this.ofRenderRegions;
/* 1951 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1954 */     if (p_setOptionValueOF_1_ == Options.SMART_ANIMATIONS) {
/*      */       
/* 1956 */       this.ofSmartAnimations = !this.ofSmartAnimations;
/* 1957 */       this.mc.renderGlobal.loadRenderers();
/*      */     } 
/*      */     
/* 1960 */     if (p_setOptionValueOF_1_ == Options.DYNAMIC_FOV)
/*      */     {
/* 1962 */       this.ofDynamicFov = !this.ofDynamicFov;
/*      */     }
/*      */     
/* 1965 */     if (p_setOptionValueOF_1_ == Options.ALTERNATE_BLOCKS) {
/*      */       
/* 1967 */       this.ofAlternateBlocks = !this.ofAlternateBlocks;
/* 1968 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/* 1971 */     if (p_setOptionValueOF_1_ == Options.DYNAMIC_LIGHTS) {
/*      */       
/* 1973 */       this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
/* 1974 */       DynamicLights.removeLights(this.mc.renderGlobal);
/*      */     } 
/*      */     
/* 1977 */     if (p_setOptionValueOF_1_ == Options.SCREENSHOT_SIZE) {
/*      */       
/* 1979 */       this.ofScreenshotSize++;
/*      */       
/* 1981 */       if (this.ofScreenshotSize > 4)
/*      */       {
/* 1983 */         this.ofScreenshotSize = 1;
/*      */       }
/*      */       
/* 1986 */       if (!OpenGlHelper.isFramebufferEnabled())
/*      */       {
/* 1988 */         this.ofScreenshotSize = 1;
/*      */       }
/*      */     } 
/*      */     
/* 1992 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_ENTITY_MODELS) {
/*      */       
/* 1994 */       this.ofCustomEntityModels = !this.ofCustomEntityModels;
/* 1995 */       this.mc.refreshResources();
/*      */     } 
/*      */     
/* 1998 */     if (p_setOptionValueOF_1_ == Options.CUSTOM_GUIS) {
/*      */       
/* 2000 */       this.ofCustomGuis = !this.ofCustomGuis;
/* 2001 */       CustomGuis.update();
/*      */     } 
/*      */     
/* 2004 */     if (p_setOptionValueOF_1_ == Options.SHOW_GL_ERRORS)
/*      */     {
/* 2006 */       this.ofShowGlErrors = !this.ofShowGlErrors;
/*      */     }
/*      */     
/* 2009 */     if (p_setOptionValueOF_1_ == Options.HELD_ITEM_TOOLTIPS)
/*      */     {
/* 2011 */       this.heldItemTooltips = !this.heldItemTooltips;
/*      */     }
/*      */     
/* 2014 */     if (p_setOptionValueOF_1_ == Options.ADVANCED_TOOLTIPS)
/*      */     {
/* 2016 */       this.advancedItemTooltips = !this.advancedItemTooltips;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private String getKeyBindingOF(Options p_getKeyBindingOF_1_) {
/* 2022 */     String s = I18n.format(p_getKeyBindingOF_1_.getEnumString(), new Object[0]) + ": ";
/*      */     
/* 2024 */     if (s == null)
/*      */     {
/* 2026 */       s = p_getKeyBindingOF_1_.getEnumString();
/*      */     }
/*      */     
/* 2029 */     if (p_getKeyBindingOF_1_ == Options.RENDER_DISTANCE) {
/*      */       
/* 2031 */       int i1 = (int)getOptionFloatValue(p_getKeyBindingOF_1_);
/* 2032 */       String s1 = I18n.format("options.renderDistance.tiny", new Object[0]);
/* 2033 */       int i = 2;
/*      */       
/* 2035 */       if (i1 >= 4) {
/*      */         
/* 2037 */         s1 = I18n.format("options.renderDistance.short", new Object[0]);
/* 2038 */         i = 4;
/*      */       } 
/*      */       
/* 2041 */       if (i1 >= 8) {
/*      */         
/* 2043 */         s1 = I18n.format("options.renderDistance.normal", new Object[0]);
/* 2044 */         i = 8;
/*      */       } 
/*      */       
/* 2047 */       if (i1 >= 16) {
/*      */         
/* 2049 */         s1 = I18n.format("options.renderDistance.far", new Object[0]);
/* 2050 */         i = 16;
/*      */       } 
/*      */       
/* 2053 */       if (i1 >= 32) {
/*      */         
/* 2055 */         s1 = Lang.get("of.options.renderDistance.extreme");
/* 2056 */         i = 32;
/*      */       } 
/*      */       
/* 2059 */       if (i1 >= 48) {
/*      */         
/* 2061 */         s1 = Lang.get("of.options.renderDistance.insane");
/* 2062 */         i = 48;
/*      */       } 
/*      */       
/* 2065 */       if (i1 >= 64) {
/*      */         
/* 2067 */         s1 = Lang.get("of.options.renderDistance.ludicrous");
/* 2068 */         i = 64;
/*      */       } 
/*      */       
/* 2071 */       int j = this.renderDistanceChunks - i;
/* 2072 */       String s2 = s1;
/*      */       
/* 2074 */       if (j > 0)
/*      */       {
/* 2076 */         s2 = s1 + "+";
/*      */       }
/*      */       
/* 2079 */       return s + i1 + " " + s2 + "";
/*      */     } 
/* 2081 */     if (p_getKeyBindingOF_1_ == Options.FOG_FANCY) {
/*      */       
/* 2083 */       switch (this.ofFogType) {
/*      */         
/*      */         case 1:
/* 2086 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2089 */           return s + Lang.getFancy();
/*      */         
/*      */         case 3:
/* 2092 */           return s + Lang.getOff();
/*      */       } 
/*      */       
/* 2095 */       return s + Lang.getOff();
/*      */     } 
/*      */     
/* 2098 */     if (p_getKeyBindingOF_1_ == Options.FOG_START)
/*      */     {
/* 2100 */       return s + this.ofFogStart;
/*      */     }
/* 2102 */     if (p_getKeyBindingOF_1_ == Options.MIPMAP_TYPE) {
/*      */       
/* 2104 */       switch (this.ofMipmapType) {
/*      */         
/*      */         case 0:
/* 2107 */           return s + Lang.get("of.options.mipmap.nearest");
/*      */         
/*      */         case 1:
/* 2110 */           return s + Lang.get("of.options.mipmap.linear");
/*      */         
/*      */         case 2:
/* 2113 */           return s + Lang.get("of.options.mipmap.bilinear");
/*      */         
/*      */         case 3:
/* 2116 */           return s + Lang.get("of.options.mipmap.trilinear");
/*      */       } 
/*      */       
/* 2119 */       return s + "of.options.mipmap.nearest";
/*      */     } 
/*      */     
/* 2122 */     if (p_getKeyBindingOF_1_ == Options.SMOOTH_FPS)
/*      */     {
/* 2124 */       return this.ofSmoothFps ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2126 */     if (p_getKeyBindingOF_1_ == Options.SMOOTH_WORLD)
/*      */     {
/* 2128 */       return this.ofSmoothWorld ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2130 */     if (p_getKeyBindingOF_1_ == Options.CLOUDS) {
/*      */       
/* 2132 */       switch (this.ofClouds) {
/*      */         
/*      */         case 1:
/* 2135 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2138 */           return s + Lang.getFancy();
/*      */         
/*      */         case 3:
/* 2141 */           return s + Lang.getOff();
/*      */       } 
/*      */       
/* 2144 */       return s + Lang.getDefault();
/*      */     } 
/*      */     
/* 2147 */     if (p_getKeyBindingOF_1_ == Options.TREES) {
/*      */       
/* 2149 */       switch (this.ofTrees) {
/*      */         
/*      */         case 1:
/* 2152 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2155 */           return s + Lang.getFancy();
/*      */ 
/*      */         
/*      */         default:
/* 2159 */           return s + Lang.getDefault();
/*      */         case 4:
/*      */           break;
/* 2162 */       }  return s + Lang.get("of.general.smart");
/*      */     } 
/*      */     
/* 2165 */     if (p_getKeyBindingOF_1_ == Options.DROPPED_ITEMS) {
/*      */       
/* 2167 */       switch (this.ofDroppedItems) {
/*      */         
/*      */         case 1:
/* 2170 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2173 */           return s + Lang.getFancy();
/*      */       } 
/*      */       
/* 2176 */       return s + Lang.getDefault();
/*      */     } 
/*      */     
/* 2179 */     if (p_getKeyBindingOF_1_ == Options.RAIN) {
/*      */       
/* 2181 */       switch (this.ofRain) {
/*      */         
/*      */         case 1:
/* 2184 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2187 */           return s + Lang.getFancy();
/*      */         
/*      */         case 3:
/* 2190 */           return s + Lang.getOff();
/*      */       } 
/*      */       
/* 2193 */       return s + Lang.getDefault();
/*      */     } 
/*      */     
/* 2196 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_WATER) {
/*      */       
/* 2198 */       switch (this.ofAnimatedWater) {
/*      */         
/*      */         case 1:
/* 2201 */           return s + Lang.get("of.options.animation.dynamic");
/*      */         
/*      */         case 2:
/* 2204 */           return s + Lang.getOff();
/*      */       } 
/*      */       
/* 2207 */       return s + Lang.getOn();
/*      */     } 
/*      */     
/* 2210 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_LAVA) {
/*      */       
/* 2212 */       switch (this.ofAnimatedLava) {
/*      */         
/*      */         case 1:
/* 2215 */           return s + Lang.get("of.options.animation.dynamic");
/*      */         
/*      */         case 2:
/* 2218 */           return s + Lang.getOff();
/*      */       } 
/*      */       
/* 2221 */       return s + Lang.getOn();
/*      */     } 
/*      */     
/* 2224 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_FIRE)
/*      */     {
/* 2226 */       return this.ofAnimatedFire ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2228 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_PORTAL)
/*      */     {
/* 2230 */       return this.ofAnimatedPortal ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2232 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_REDSTONE)
/*      */     {
/* 2234 */       return this.ofAnimatedRedstone ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2236 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_EXPLOSION)
/*      */     {
/* 2238 */       return this.ofAnimatedExplosion ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2240 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_FLAME)
/*      */     {
/* 2242 */       return this.ofAnimatedFlame ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2244 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_SMOKE)
/*      */     {
/* 2246 */       return this.ofAnimatedSmoke ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2248 */     if (p_getKeyBindingOF_1_ == Options.VOID_PARTICLES)
/*      */     {
/* 2250 */       return this.ofVoidParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2252 */     if (p_getKeyBindingOF_1_ == Options.WATER_PARTICLES)
/*      */     {
/* 2254 */       return this.ofWaterParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2256 */     if (p_getKeyBindingOF_1_ == Options.PORTAL_PARTICLES)
/*      */     {
/* 2258 */       return this.ofPortalParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2260 */     if (p_getKeyBindingOF_1_ == Options.POTION_PARTICLES)
/*      */     {
/* 2262 */       return this.ofPotionParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2264 */     if (p_getKeyBindingOF_1_ == Options.FIREWORK_PARTICLES)
/*      */     {
/* 2266 */       return this.ofFireworkParticles ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2268 */     if (p_getKeyBindingOF_1_ == Options.DRIPPING_WATER_LAVA)
/*      */     {
/* 2270 */       return this.ofDrippingWaterLava ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2272 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_TERRAIN)
/*      */     {
/* 2274 */       return this.ofAnimatedTerrain ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2276 */     if (p_getKeyBindingOF_1_ == Options.ANIMATED_TEXTURES)
/*      */     {
/* 2278 */       return this.ofAnimatedTextures ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2280 */     if (p_getKeyBindingOF_1_ == Options.RAIN_SPLASH)
/*      */     {
/* 2282 */       return this.ofRainSplash ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2284 */     if (p_getKeyBindingOF_1_ == Options.LAGOMETER)
/*      */     {
/* 2286 */       return this.ofLagometer ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2288 */     if (p_getKeyBindingOF_1_ == Options.SHOW_FPS)
/*      */     {
/* 2290 */       return this.ofShowFps ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2292 */     if (p_getKeyBindingOF_1_ == Options.AUTOSAVE_TICKS) {
/*      */       
/* 2294 */       int l = 900;
/* 2295 */       return (this.ofAutoSaveTicks <= l) ? (s + Lang.get("of.options.save.45s")) : ((this.ofAutoSaveTicks <= 2 * l) ? (s + Lang.get("of.options.save.90s")) : ((this.ofAutoSaveTicks <= 4 * l) ? (s + Lang.get("of.options.save.3min")) : ((this.ofAutoSaveTicks <= 8 * l) ? (s + Lang.get("of.options.save.6min")) : ((this.ofAutoSaveTicks <= 16 * l) ? (s + Lang.get("of.options.save.12min")) : (s + Lang.get("of.options.save.24min"))))));
/*      */     } 
/* 2297 */     if (p_getKeyBindingOF_1_ == Options.BETTER_GRASS) {
/*      */       
/* 2299 */       switch (this.ofBetterGrass) {
/*      */         
/*      */         case 1:
/* 2302 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2305 */           return s + Lang.getFancy();
/*      */       } 
/*      */       
/* 2308 */       return s + Lang.getOff();
/*      */     } 
/*      */     
/* 2311 */     if (p_getKeyBindingOF_1_ == Options.CONNECTED_TEXTURES) {
/*      */       
/* 2313 */       switch (this.ofConnectedTextures) {
/*      */         
/*      */         case 1:
/* 2316 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2319 */           return s + Lang.getFancy();
/*      */       } 
/*      */       
/* 2322 */       return s + Lang.getOff();
/*      */     } 
/*      */     
/* 2325 */     if (p_getKeyBindingOF_1_ == Options.WEATHER)
/*      */     {
/* 2327 */       return this.ofWeather ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2329 */     if (p_getKeyBindingOF_1_ == Options.SKY)
/*      */     {
/* 2331 */       return this.ofSky ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2333 */     if (p_getKeyBindingOF_1_ == Options.STARS)
/*      */     {
/* 2335 */       return this.ofStars ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2337 */     if (p_getKeyBindingOF_1_ == Options.SUN_MOON)
/*      */     {
/* 2339 */       return this.ofSunMoon ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2341 */     if (p_getKeyBindingOF_1_ == Options.VIGNETTE) {
/*      */       
/* 2343 */       switch (this.ofVignette) {
/*      */         
/*      */         case 1:
/* 2346 */           return s + Lang.getFast();
/*      */         
/*      */         case 2:
/* 2349 */           return s + Lang.getFancy();
/*      */       } 
/*      */       
/* 2352 */       return s + Lang.getDefault();
/*      */     } 
/*      */     
/* 2355 */     if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES)
/*      */     {
/* 2357 */       return s + this.ofChunkUpdates;
/*      */     }
/* 2359 */     if (p_getKeyBindingOF_1_ == Options.CHUNK_UPDATES_DYNAMIC)
/*      */     {
/* 2361 */       return this.ofChunkUpdatesDynamic ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2363 */     if (p_getKeyBindingOF_1_ == Options.TIME)
/*      */     {
/* 2365 */       return (this.ofTime == 1) ? (s + Lang.get("of.options.time.dayOnly")) : ((this.ofTime == 2) ? (s + Lang.get("of.options.time.nightOnly")) : (s + Lang.getDefault()));
/*      */     }
/* 2367 */     if (p_getKeyBindingOF_1_ == Options.CLEAR_WATER)
/*      */     {
/* 2369 */       return this.ofClearWater ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2371 */     if (p_getKeyBindingOF_1_ == Options.AA_LEVEL) {
/*      */       
/* 2373 */       String s3 = "";
/*      */       
/* 2375 */       if (this.ofAaLevel != Config.getAntialiasingLevel())
/*      */       {
/* 2377 */         s3 = " (" + Lang.get("of.general.restart") + ")";
/*      */       }
/*      */       
/* 2380 */       return (this.ofAaLevel == 0) ? (s + Lang.getOff() + s3) : (s + this.ofAaLevel + s3);
/*      */     } 
/* 2382 */     if (p_getKeyBindingOF_1_ == Options.AF_LEVEL)
/*      */     {
/* 2384 */       return (this.ofAfLevel == 1) ? (s + Lang.getOff()) : (s + this.ofAfLevel);
/*      */     }
/* 2386 */     if (p_getKeyBindingOF_1_ == Options.PROFILER)
/*      */     {
/* 2388 */       return this.ofProfiler ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2390 */     if (p_getKeyBindingOF_1_ == Options.BETTER_SNOW)
/*      */     {
/* 2392 */       return this.ofBetterSnow ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2394 */     if (p_getKeyBindingOF_1_ == Options.SWAMP_COLORS)
/*      */     {
/* 2396 */       return this.ofSwampColors ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2398 */     if (p_getKeyBindingOF_1_ == Options.RANDOM_ENTITIES)
/*      */     {
/* 2400 */       return this.ofRandomEntities ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2402 */     if (p_getKeyBindingOF_1_ == Options.SMOOTH_BIOMES)
/*      */     {
/* 2404 */       return this.ofSmoothBiomes ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2406 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_FONTS)
/*      */     {
/* 2408 */       return this.ofCustomFonts ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2410 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_COLORS)
/*      */     {
/* 2412 */       return this.ofCustomColors ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2414 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_SKY)
/*      */     {
/* 2416 */       return this.ofCustomSky ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2418 */     if (p_getKeyBindingOF_1_ == Options.SHOW_CAPES)
/*      */     {
/* 2420 */       return this.ofShowCapes ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2422 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_ITEMS)
/*      */     {
/* 2424 */       return this.ofCustomItems ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2426 */     if (p_getKeyBindingOF_1_ == Options.NATURAL_TEXTURES)
/*      */     {
/* 2428 */       return this.ofNaturalTextures ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2430 */     if (p_getKeyBindingOF_1_ == Options.EMISSIVE_TEXTURES)
/*      */     {
/* 2432 */       return this.ofEmissiveTextures ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2434 */     if (p_getKeyBindingOF_1_ == Options.FAST_MATH)
/*      */     {
/* 2436 */       return this.ofFastMath ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2438 */     if (p_getKeyBindingOF_1_ == Options.FAST_RENDER)
/*      */     {
/* 2440 */       return this.ofFastRender ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2442 */     if (p_getKeyBindingOF_1_ == Options.TRANSLUCENT_BLOCKS)
/*      */     {
/* 2444 */       return (this.ofTranslucentBlocks == 1) ? (s + Lang.getFast()) : ((this.ofTranslucentBlocks == 2) ? (s + Lang.getFancy()) : (s + Lang.getDefault()));
/*      */     }
/* 2446 */     if (p_getKeyBindingOF_1_ == Options.LAZY_CHUNK_LOADING)
/*      */     {
/* 2448 */       return this.ofLazyChunkLoading ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2450 */     if (p_getKeyBindingOF_1_ == Options.RENDER_REGIONS)
/*      */     {
/* 2452 */       return this.ofRenderRegions ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2454 */     if (p_getKeyBindingOF_1_ == Options.SMART_ANIMATIONS)
/*      */     {
/* 2456 */       return this.ofSmartAnimations ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2458 */     if (p_getKeyBindingOF_1_ == Options.DYNAMIC_FOV)
/*      */     {
/* 2460 */       return this.ofDynamicFov ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2462 */     if (p_getKeyBindingOF_1_ == Options.ALTERNATE_BLOCKS)
/*      */     {
/* 2464 */       return this.ofAlternateBlocks ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2466 */     if (p_getKeyBindingOF_1_ == Options.DYNAMIC_LIGHTS) {
/*      */       
/* 2468 */       int k = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
/* 2469 */       return s + getTranslation(KEYS_DYNAMIC_LIGHTS, k);
/*      */     } 
/* 2471 */     if (p_getKeyBindingOF_1_ == Options.SCREENSHOT_SIZE)
/*      */     {
/* 2473 */       return (this.ofScreenshotSize <= 1) ? (s + Lang.getDefault()) : (s + this.ofScreenshotSize + "x");
/*      */     }
/* 2475 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_ENTITY_MODELS)
/*      */     {
/* 2477 */       return this.ofCustomEntityModels ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2479 */     if (p_getKeyBindingOF_1_ == Options.CUSTOM_GUIS)
/*      */     {
/* 2481 */       return this.ofCustomGuis ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2483 */     if (p_getKeyBindingOF_1_ == Options.SHOW_GL_ERRORS)
/*      */     {
/* 2485 */       return this.ofShowGlErrors ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2487 */     if (p_getKeyBindingOF_1_ == Options.FULLSCREEN_MODE)
/*      */     {
/* 2489 */       return this.ofFullscreenMode.equals("Default") ? (s + Lang.getDefault()) : (s + this.ofFullscreenMode);
/*      */     }
/* 2491 */     if (p_getKeyBindingOF_1_ == Options.HELD_ITEM_TOOLTIPS)
/*      */     {
/* 2493 */       return this.heldItemTooltips ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2495 */     if (p_getKeyBindingOF_1_ == Options.ADVANCED_TOOLTIPS)
/*      */     {
/* 2497 */       return this.advancedItemTooltips ? (s + Lang.getOn()) : (s + Lang.getOff());
/*      */     }
/* 2499 */     if (p_getKeyBindingOF_1_ == Options.FRAMERATE_LIMIT) {
/*      */       
/* 2501 */       float f = getOptionFloatValue(p_getKeyBindingOF_1_);
/* 2502 */       return (f == 0.0F) ? (s + Lang.get("of.options.framerateLimit.vsync")) : ((f == p_getKeyBindingOF_1_.valueMax) ? (s + I18n.format("options.framerateLimit.max", new Object[0])) : (s + (int)f + " fps"));
/*      */     } 
/*      */ 
/*      */     
/* 2506 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void loadOfOptions() {
/*      */     try {
/* 2514 */       File file1 = this.optionsFileOF;
/*      */       
/* 2516 */       if (!file1.exists())
/*      */       {
/* 2518 */         file1 = this.optionsFile;
/*      */       }
/*      */       
/* 2521 */       if (!file1.exists()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 2526 */       BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "UTF-8"));
/* 2527 */       String s = "";
/*      */       
/* 2529 */       while ((s = bufferedreader.readLine()) != null) {
/*      */ 
/*      */         
/*      */         try {
/* 2533 */           String[] astring = s.split(":");
/*      */           
/* 2535 */           if (astring[0].equals("ofRenderDistanceChunks") && astring.length >= 2) {
/*      */             
/* 2537 */             this.renderDistanceChunks = Integer.valueOf(astring[1]).intValue();
/* 2538 */             this.renderDistanceChunks = Config.limit(this.renderDistanceChunks, 2, 1024);
/*      */           } 
/*      */           
/* 2541 */           if (astring[0].equals("ofFogType") && astring.length >= 2) {
/*      */             
/* 2543 */             this.ofFogType = Integer.valueOf(astring[1]).intValue();
/* 2544 */             this.ofFogType = Config.limit(this.ofFogType, 1, 3);
/*      */           } 
/*      */           
/* 2547 */           if (astring[0].equals("ofFogStart") && astring.length >= 2) {
/*      */             
/* 2549 */             this.ofFogStart = Float.valueOf(astring[1]).floatValue();
/*      */             
/* 2551 */             if (this.ofFogStart < 0.2F)
/*      */             {
/* 2553 */               this.ofFogStart = 0.2F;
/*      */             }
/*      */             
/* 2556 */             if (this.ofFogStart > 0.81F)
/*      */             {
/* 2558 */               this.ofFogStart = 0.8F;
/*      */             }
/*      */           } 
/*      */           
/* 2562 */           if (astring[0].equals("ofMipmapType") && astring.length >= 2) {
/*      */             
/* 2564 */             this.ofMipmapType = Integer.valueOf(astring[1]).intValue();
/* 2565 */             this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
/*      */           } 
/*      */           
/* 2568 */           if (astring[0].equals("ofOcclusionFancy") && astring.length >= 2)
/*      */           {
/* 2570 */             this.ofOcclusionFancy = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2573 */           if (astring[0].equals("ofSmoothFps") && astring.length >= 2)
/*      */           {
/* 2575 */             this.ofSmoothFps = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2578 */           if (astring[0].equals("ofSmoothWorld") && astring.length >= 2)
/*      */           {
/* 2580 */             this.ofSmoothWorld = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2583 */           if (astring[0].equals("ofAoLevel") && astring.length >= 2) {
/*      */             
/* 2585 */             this.ofAoLevel = Float.valueOf(astring[1]).floatValue();
/* 2586 */             this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
/*      */           } 
/*      */           
/* 2589 */           if (astring[0].equals("ofClouds") && astring.length >= 2) {
/*      */             
/* 2591 */             this.ofClouds = Integer.valueOf(astring[1]).intValue();
/* 2592 */             this.ofClouds = Config.limit(this.ofClouds, 0, 3);
/* 2593 */             updateRenderClouds();
/*      */           } 
/*      */           
/* 2596 */           if (astring[0].equals("ofCloudsHeight") && astring.length >= 2) {
/*      */             
/* 2598 */             this.ofCloudsHeight = Float.valueOf(astring[1]).floatValue();
/* 2599 */             this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
/*      */           } 
/*      */           
/* 2602 */           if (astring[0].equals("ofTrees") && astring.length >= 2) {
/*      */             
/* 2604 */             this.ofTrees = Integer.valueOf(astring[1]).intValue();
/* 2605 */             this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
/*      */           } 
/*      */           
/* 2608 */           if (astring[0].equals("ofDroppedItems") && astring.length >= 2) {
/*      */             
/* 2610 */             this.ofDroppedItems = Integer.valueOf(astring[1]).intValue();
/* 2611 */             this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
/*      */           } 
/*      */           
/* 2614 */           if (astring[0].equals("ofRain") && astring.length >= 2) {
/*      */             
/* 2616 */             this.ofRain = Integer.valueOf(astring[1]).intValue();
/* 2617 */             this.ofRain = Config.limit(this.ofRain, 0, 3);
/*      */           } 
/*      */           
/* 2620 */           if (astring[0].equals("ofAnimatedWater") && astring.length >= 2) {
/*      */             
/* 2622 */             this.ofAnimatedWater = Integer.valueOf(astring[1]).intValue();
/* 2623 */             this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
/*      */           } 
/*      */           
/* 2626 */           if (astring[0].equals("ofAnimatedLava") && astring.length >= 2) {
/*      */             
/* 2628 */             this.ofAnimatedLava = Integer.valueOf(astring[1]).intValue();
/* 2629 */             this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
/*      */           } 
/*      */           
/* 2632 */           if (astring[0].equals("ofAnimatedFire") && astring.length >= 2)
/*      */           {
/* 2634 */             this.ofAnimatedFire = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2637 */           if (astring[0].equals("ofAnimatedPortal") && astring.length >= 2)
/*      */           {
/* 2639 */             this.ofAnimatedPortal = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2642 */           if (astring[0].equals("ofAnimatedRedstone") && astring.length >= 2)
/*      */           {
/* 2644 */             this.ofAnimatedRedstone = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2647 */           if (astring[0].equals("ofAnimatedExplosion") && astring.length >= 2)
/*      */           {
/* 2649 */             this.ofAnimatedExplosion = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2652 */           if (astring[0].equals("ofAnimatedFlame") && astring.length >= 2)
/*      */           {
/* 2654 */             this.ofAnimatedFlame = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2657 */           if (astring[0].equals("ofAnimatedSmoke") && astring.length >= 2)
/*      */           {
/* 2659 */             this.ofAnimatedSmoke = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2662 */           if (astring[0].equals("ofVoidParticles") && astring.length >= 2)
/*      */           {
/* 2664 */             this.ofVoidParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2667 */           if (astring[0].equals("ofWaterParticles") && astring.length >= 2)
/*      */           {
/* 2669 */             this.ofWaterParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2672 */           if (astring[0].equals("ofPortalParticles") && astring.length >= 2)
/*      */           {
/* 2674 */             this.ofPortalParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2677 */           if (astring[0].equals("ofPotionParticles") && astring.length >= 2)
/*      */           {
/* 2679 */             this.ofPotionParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2682 */           if (astring[0].equals("ofFireworkParticles") && astring.length >= 2)
/*      */           {
/* 2684 */             this.ofFireworkParticles = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2687 */           if (astring[0].equals("ofDrippingWaterLava") && astring.length >= 2)
/*      */           {
/* 2689 */             this.ofDrippingWaterLava = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2692 */           if (astring[0].equals("ofAnimatedTerrain") && astring.length >= 2)
/*      */           {
/* 2694 */             this.ofAnimatedTerrain = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2697 */           if (astring[0].equals("ofAnimatedTextures") && astring.length >= 2)
/*      */           {
/* 2699 */             this.ofAnimatedTextures = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2702 */           if (astring[0].equals("ofRainSplash") && astring.length >= 2)
/*      */           {
/* 2704 */             this.ofRainSplash = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2707 */           if (astring[0].equals("ofLagometer") && astring.length >= 2)
/*      */           {
/* 2709 */             this.ofLagometer = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2712 */           if (astring[0].equals("ofShowFps") && astring.length >= 2)
/*      */           {
/* 2714 */             this.ofShowFps = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2717 */           if (astring[0].equals("ofAutoSaveTicks") && astring.length >= 2) {
/*      */             
/* 2719 */             this.ofAutoSaveTicks = Integer.valueOf(astring[1]).intValue();
/* 2720 */             this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, 40000);
/*      */           } 
/*      */           
/* 2723 */           if (astring[0].equals("ofBetterGrass") && astring.length >= 2) {
/*      */             
/* 2725 */             this.ofBetterGrass = Integer.valueOf(astring[1]).intValue();
/* 2726 */             this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
/*      */           } 
/*      */           
/* 2729 */           if (astring[0].equals("ofConnectedTextures") && astring.length >= 2) {
/*      */             
/* 2731 */             this.ofConnectedTextures = Integer.valueOf(astring[1]).intValue();
/* 2732 */             this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
/*      */           } 
/*      */           
/* 2735 */           if (astring[0].equals("ofWeather") && astring.length >= 2)
/*      */           {
/* 2737 */             this.ofWeather = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2740 */           if (astring[0].equals("ofSky") && astring.length >= 2)
/*      */           {
/* 2742 */             this.ofSky = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2745 */           if (astring[0].equals("ofStars") && astring.length >= 2)
/*      */           {
/* 2747 */             this.ofStars = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2750 */           if (astring[0].equals("ofSunMoon") && astring.length >= 2)
/*      */           {
/* 2752 */             this.ofSunMoon = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2755 */           if (astring[0].equals("ofVignette") && astring.length >= 2) {
/*      */             
/* 2757 */             this.ofVignette = Integer.valueOf(astring[1]).intValue();
/* 2758 */             this.ofVignette = Config.limit(this.ofVignette, 0, 2);
/*      */           } 
/*      */           
/* 2761 */           if (astring[0].equals("ofChunkUpdates") && astring.length >= 2) {
/*      */             
/* 2763 */             this.ofChunkUpdates = Integer.valueOf(astring[1]).intValue();
/* 2764 */             this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
/*      */           } 
/*      */           
/* 2767 */           if (astring[0].equals("ofChunkUpdatesDynamic") && astring.length >= 2)
/*      */           {
/* 2769 */             this.ofChunkUpdatesDynamic = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2772 */           if (astring[0].equals("ofTime") && astring.length >= 2) {
/*      */             
/* 2774 */             this.ofTime = Integer.valueOf(astring[1]).intValue();
/* 2775 */             this.ofTime = Config.limit(this.ofTime, 0, 2);
/*      */           } 
/*      */           
/* 2778 */           if (astring[0].equals("ofClearWater") && astring.length >= 2) {
/*      */             
/* 2780 */             this.ofClearWater = Boolean.valueOf(astring[1]).booleanValue();
/* 2781 */             updateWaterOpacity();
/*      */           } 
/*      */           
/* 2784 */           if (astring[0].equals("ofAaLevel") && astring.length >= 2) {
/*      */             
/* 2786 */             this.ofAaLevel = Integer.valueOf(astring[1]).intValue();
/* 2787 */             this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
/*      */           } 
/*      */           
/* 2790 */           if (astring[0].equals("ofAfLevel") && astring.length >= 2) {
/*      */             
/* 2792 */             this.ofAfLevel = Integer.valueOf(astring[1]).intValue();
/* 2793 */             this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
/*      */           } 
/*      */           
/* 2796 */           if (astring[0].equals("ofProfiler") && astring.length >= 2)
/*      */           {
/* 2798 */             this.ofProfiler = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2801 */           if (astring[0].equals("ofBetterSnow") && astring.length >= 2)
/*      */           {
/* 2803 */             this.ofBetterSnow = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2806 */           if (astring[0].equals("ofSwampColors") && astring.length >= 2)
/*      */           {
/* 2808 */             this.ofSwampColors = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2811 */           if (astring[0].equals("ofRandomEntities") && astring.length >= 2)
/*      */           {
/* 2813 */             this.ofRandomEntities = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2816 */           if (astring[0].equals("ofSmoothBiomes") && astring.length >= 2)
/*      */           {
/* 2818 */             this.ofSmoothBiomes = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2821 */           if (astring[0].equals("ofCustomFonts") && astring.length >= 2)
/*      */           {
/* 2823 */             this.ofCustomFonts = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2826 */           if (astring[0].equals("ofCustomColors") && astring.length >= 2)
/*      */           {
/* 2828 */             this.ofCustomColors = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2831 */           if (astring[0].equals("ofCustomItems") && astring.length >= 2)
/*      */           {
/* 2833 */             this.ofCustomItems = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2836 */           if (astring[0].equals("ofCustomSky") && astring.length >= 2)
/*      */           {
/* 2838 */             this.ofCustomSky = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2841 */           if (astring[0].equals("ofShowCapes") && astring.length >= 2)
/*      */           {
/* 2843 */             this.ofShowCapes = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2846 */           if (astring[0].equals("ofNaturalTextures") && astring.length >= 2)
/*      */           {
/* 2848 */             this.ofNaturalTextures = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2851 */           if (astring[0].equals("ofEmissiveTextures") && astring.length >= 2)
/*      */           {
/* 2853 */             this.ofEmissiveTextures = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2856 */           if (astring[0].equals("ofLazyChunkLoading") && astring.length >= 2)
/*      */           {
/* 2858 */             this.ofLazyChunkLoading = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2861 */           if (astring[0].equals("ofRenderRegions") && astring.length >= 2)
/*      */           {
/* 2863 */             this.ofRenderRegions = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2866 */           if (astring[0].equals("ofSmartAnimations") && astring.length >= 2)
/*      */           {
/* 2868 */             this.ofSmartAnimations = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2871 */           if (astring[0].equals("ofDynamicFov") && astring.length >= 2)
/*      */           {
/* 2873 */             this.ofDynamicFov = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2876 */           if (astring[0].equals("ofAlternateBlocks") && astring.length >= 2)
/*      */           {
/* 2878 */             this.ofAlternateBlocks = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2881 */           if (astring[0].equals("ofDynamicLights") && astring.length >= 2) {
/*      */             
/* 2883 */             this.ofDynamicLights = Integer.valueOf(astring[1]).intValue();
/* 2884 */             this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
/*      */           } 
/*      */           
/* 2887 */           if (astring[0].equals("ofScreenshotSize") && astring.length >= 2) {
/*      */             
/* 2889 */             this.ofScreenshotSize = Integer.valueOf(astring[1]).intValue();
/* 2890 */             this.ofScreenshotSize = Config.limit(this.ofScreenshotSize, 1, 4);
/*      */           } 
/*      */           
/* 2893 */           if (astring[0].equals("ofCustomEntityModels") && astring.length >= 2)
/*      */           {
/* 2895 */             this.ofCustomEntityModels = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2898 */           if (astring[0].equals("ofCustomGuis") && astring.length >= 2)
/*      */           {
/* 2900 */             this.ofCustomGuis = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2903 */           if (astring[0].equals("ofShowGlErrors") && astring.length >= 2)
/*      */           {
/* 2905 */             this.ofShowGlErrors = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2908 */           if (astring[0].equals("ofFullscreenMode") && astring.length >= 2)
/*      */           {
/* 2910 */             this.ofFullscreenMode = astring[1];
/*      */           }
/*      */           
/* 2913 */           if (astring[0].equals("ofFastMath") && astring.length >= 2) {
/*      */             
/* 2915 */             this.ofFastMath = Boolean.valueOf(astring[1]).booleanValue();
/* 2916 */             MathHelper.fastMath = this.ofFastMath;
/*      */           } 
/*      */           
/* 2919 */           if (astring[0].equals("ofFastRender") && astring.length >= 2)
/*      */           {
/* 2921 */             this.ofFastRender = Boolean.valueOf(astring[1]).booleanValue();
/*      */           }
/*      */           
/* 2924 */           if (astring[0].equals("ofTranslucentBlocks") && astring.length >= 2) {
/*      */             
/* 2926 */             this.ofTranslucentBlocks = Integer.valueOf(astring[1]).intValue();
/* 2927 */             this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
/*      */           } 
/*      */           
/* 2930 */           if (astring[0].equals("key_" + this.ofKeyBindZoom.getKeyDescription()))
/*      */           {
/* 2932 */             this.ofKeyBindZoom.setKeyCode(Integer.parseInt(astring[1]));
/*      */           }
/*      */         }
/* 2935 */         catch (Exception exception) {
/*      */           
/* 2937 */           Config.dbg("Skipping bad option: " + s);
/* 2938 */           exception.printStackTrace();
/*      */         } 
/*      */       } 
/*      */       
/* 2942 */       KeyUtils.fixKeyConflicts(this.keyBindings, new KeyBinding[] { this.ofKeyBindZoom });
/* 2943 */       KeyBinding.resetKeyBindingArrayAndHash();
/* 2944 */       bufferedreader.close();
/*      */     }
/* 2946 */     catch (Exception exception1) {
/*      */       
/* 2948 */       Config.warn("Failed to load options");
/* 2949 */       exception1.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveOfOptions() {
/*      */     try {
/* 2957 */       PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), "UTF-8"));
/* 2958 */       printwriter.println("ofFogType:" + this.ofFogType);
/* 2959 */       printwriter.println("ofFogStart:" + this.ofFogStart);
/* 2960 */       printwriter.println("ofMipmapType:" + this.ofMipmapType);
/* 2961 */       printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
/* 2962 */       printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
/* 2963 */       printwriter.println("ofSmoothWorld:" + this.ofSmoothWorld);
/* 2964 */       printwriter.println("ofAoLevel:" + this.ofAoLevel);
/* 2965 */       printwriter.println("ofClouds:" + this.ofClouds);
/* 2966 */       printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
/* 2967 */       printwriter.println("ofTrees:" + this.ofTrees);
/* 2968 */       printwriter.println("ofDroppedItems:" + this.ofDroppedItems);
/* 2969 */       printwriter.println("ofRain:" + this.ofRain);
/* 2970 */       printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
/* 2971 */       printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
/* 2972 */       printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
/* 2973 */       printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
/* 2974 */       printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
/* 2975 */       printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
/* 2976 */       printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
/* 2977 */       printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
/* 2978 */       printwriter.println("ofVoidParticles:" + this.ofVoidParticles);
/* 2979 */       printwriter.println("ofWaterParticles:" + this.ofWaterParticles);
/* 2980 */       printwriter.println("ofPortalParticles:" + this.ofPortalParticles);
/* 2981 */       printwriter.println("ofPotionParticles:" + this.ofPotionParticles);
/* 2982 */       printwriter.println("ofFireworkParticles:" + this.ofFireworkParticles);
/* 2983 */       printwriter.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
/* 2984 */       printwriter.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
/* 2985 */       printwriter.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
/* 2986 */       printwriter.println("ofRainSplash:" + this.ofRainSplash);
/* 2987 */       printwriter.println("ofLagometer:" + this.ofLagometer);
/* 2988 */       printwriter.println("ofShowFps:" + this.ofShowFps);
/* 2989 */       printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
/* 2990 */       printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
/* 2991 */       printwriter.println("ofConnectedTextures:" + this.ofConnectedTextures);
/* 2992 */       printwriter.println("ofWeather:" + this.ofWeather);
/* 2993 */       printwriter.println("ofSky:" + this.ofSky);
/* 2994 */       printwriter.println("ofStars:" + this.ofStars);
/* 2995 */       printwriter.println("ofSunMoon:" + this.ofSunMoon);
/* 2996 */       printwriter.println("ofVignette:" + this.ofVignette);
/* 2997 */       printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
/* 2998 */       printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
/* 2999 */       printwriter.println("ofTime:" + this.ofTime);
/* 3000 */       printwriter.println("ofClearWater:" + this.ofClearWater);
/* 3001 */       printwriter.println("ofAaLevel:" + this.ofAaLevel);
/* 3002 */       printwriter.println("ofAfLevel:" + this.ofAfLevel);
/* 3003 */       printwriter.println("ofProfiler:" + this.ofProfiler);
/* 3004 */       printwriter.println("ofBetterSnow:" + this.ofBetterSnow);
/* 3005 */       printwriter.println("ofSwampColors:" + this.ofSwampColors);
/* 3006 */       printwriter.println("ofRandomEntities:" + this.ofRandomEntities);
/* 3007 */       printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
/* 3008 */       printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
/* 3009 */       printwriter.println("ofCustomColors:" + this.ofCustomColors);
/* 3010 */       printwriter.println("ofCustomItems:" + this.ofCustomItems);
/* 3011 */       printwriter.println("ofCustomSky:" + this.ofCustomSky);
/* 3012 */       printwriter.println("ofShowCapes:" + this.ofShowCapes);
/* 3013 */       printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
/* 3014 */       printwriter.println("ofEmissiveTextures:" + this.ofEmissiveTextures);
/* 3015 */       printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
/* 3016 */       printwriter.println("ofRenderRegions:" + this.ofRenderRegions);
/* 3017 */       printwriter.println("ofSmartAnimations:" + this.ofSmartAnimations);
/* 3018 */       printwriter.println("ofDynamicFov:" + this.ofDynamicFov);
/* 3019 */       printwriter.println("ofAlternateBlocks:" + this.ofAlternateBlocks);
/* 3020 */       printwriter.println("ofDynamicLights:" + this.ofDynamicLights);
/* 3021 */       printwriter.println("ofScreenshotSize:" + this.ofScreenshotSize);
/* 3022 */       printwriter.println("ofCustomEntityModels:" + this.ofCustomEntityModels);
/* 3023 */       printwriter.println("ofCustomGuis:" + this.ofCustomGuis);
/* 3024 */       printwriter.println("ofShowGlErrors:" + this.ofShowGlErrors);
/* 3025 */       printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
/* 3026 */       printwriter.println("ofFastMath:" + this.ofFastMath);
/* 3027 */       printwriter.println("ofFastRender:" + this.ofFastRender);
/* 3028 */       printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
/* 3029 */       printwriter.println("key_" + this.ofKeyBindZoom.getKeyDescription() + ":" + this.ofKeyBindZoom.getKeyCode());
/* 3030 */       printwriter.close();
/*      */     }
/* 3032 */     catch (Exception exception) {
/*      */       
/* 3034 */       Config.warn("Failed to save options");
/* 3035 */       exception.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateRenderClouds() {
/* 3041 */     switch (this.ofClouds) {
/*      */       
/*      */       case 1:
/* 3044 */         this.clouds = 1;
/*      */         return;
/*      */       
/*      */       case 2:
/* 3048 */         this.clouds = 2;
/*      */         return;
/*      */       
/*      */       case 3:
/* 3052 */         this.clouds = 0;
/*      */         return;
/*      */     } 
/*      */     
/* 3056 */     if (this.fancyGraphics) {
/*      */       
/* 3058 */       this.clouds = 2;
/*      */     }
/*      */     else {
/*      */       
/* 3062 */       this.clouds = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetSettings() {
/* 3069 */     this.renderDistanceChunks = 8;
/* 3070 */     this.viewBobbing = true;
/* 3071 */     this.anaglyph = false;
/* 3072 */     this.limitFramerate = (int)Options.FRAMERATE_LIMIT.getValueMax();
/* 3073 */     this.enableVsync = false;
/* 3074 */     updateVSync();
/* 3075 */     this.mipmapLevels = 4;
/* 3076 */     this.fancyGraphics = true;
/* 3077 */     this.ambientOcclusion = 2;
/* 3078 */     this.clouds = 2;
/* 3079 */     this.fovSetting = 70.0F;
/* 3080 */     this.gammaSetting = 0.0F;
/* 3081 */     this.guiScale = 0;
/* 3082 */     this.particleSetting = 0;
/* 3083 */     this.heldItemTooltips = true;
/* 3084 */     this.useVbo = false;
/* 3085 */     this.forceUnicodeFont = false;
/* 3086 */     this.ofFogType = 1;
/* 3087 */     this.ofFogStart = 0.8F;
/* 3088 */     this.ofMipmapType = 0;
/* 3089 */     this.ofOcclusionFancy = false;
/* 3090 */     this.ofSmartAnimations = false;
/* 3091 */     this.ofSmoothFps = false;
/* 3092 */     Config.updateAvailableProcessors();
/* 3093 */     this.ofSmoothWorld = Config.isSingleProcessor();
/* 3094 */     this.ofLazyChunkLoading = false;
/* 3095 */     this.ofRenderRegions = false;
/* 3096 */     this.ofFastMath = false;
/* 3097 */     this.ofFastRender = false;
/* 3098 */     this.ofTranslucentBlocks = 0;
/* 3099 */     this.ofDynamicFov = true;
/* 3100 */     this.ofAlternateBlocks = true;
/* 3101 */     this.ofDynamicLights = 3;
/* 3102 */     this.ofScreenshotSize = 1;
/* 3103 */     this.ofCustomEntityModels = true;
/* 3104 */     this.ofCustomGuis = true;
/* 3105 */     this.ofShowGlErrors = true;
/* 3106 */     this.ofAoLevel = 1.0F;
/* 3107 */     this.ofAaLevel = 0;
/* 3108 */     this.ofAfLevel = 1;
/* 3109 */     this.ofClouds = 0;
/* 3110 */     this.ofCloudsHeight = 0.0F;
/* 3111 */     this.ofTrees = 0;
/* 3112 */     this.ofRain = 0;
/* 3113 */     this.ofBetterGrass = 3;
/* 3114 */     this.ofAutoSaveTicks = 4000;
/* 3115 */     this.ofLagometer = false;
/* 3116 */     this.ofShowFps = false;
/* 3117 */     this.ofProfiler = false;
/* 3118 */     this.ofWeather = true;
/* 3119 */     this.ofSky = true;
/* 3120 */     this.ofStars = true;
/* 3121 */     this.ofSunMoon = true;
/* 3122 */     this.ofVignette = 0;
/* 3123 */     this.ofChunkUpdates = 1;
/* 3124 */     this.ofChunkUpdatesDynamic = false;
/* 3125 */     this.ofTime = 0;
/* 3126 */     this.ofClearWater = false;
/* 3127 */     this.ofBetterSnow = false;
/* 3128 */     this.ofFullscreenMode = "Default";
/* 3129 */     this.ofSwampColors = true;
/* 3130 */     this.ofRandomEntities = true;
/* 3131 */     this.ofSmoothBiomes = true;
/* 3132 */     this.ofCustomFonts = true;
/* 3133 */     this.ofCustomColors = true;
/* 3134 */     this.ofCustomItems = true;
/* 3135 */     this.ofCustomSky = true;
/* 3136 */     this.ofShowCapes = true;
/* 3137 */     this.ofConnectedTextures = 2;
/* 3138 */     this.ofNaturalTextures = false;
/* 3139 */     this.ofEmissiveTextures = true;
/* 3140 */     this.ofAnimatedWater = 0;
/* 3141 */     this.ofAnimatedLava = 0;
/* 3142 */     this.ofAnimatedFire = true;
/* 3143 */     this.ofAnimatedPortal = true;
/* 3144 */     this.ofAnimatedRedstone = true;
/* 3145 */     this.ofAnimatedExplosion = true;
/* 3146 */     this.ofAnimatedFlame = true;
/* 3147 */     this.ofAnimatedSmoke = true;
/* 3148 */     this.ofVoidParticles = true;
/* 3149 */     this.ofWaterParticles = true;
/* 3150 */     this.ofRainSplash = true;
/* 3151 */     this.ofPortalParticles = true;
/* 3152 */     this.ofPotionParticles = true;
/* 3153 */     this.ofFireworkParticles = true;
/* 3154 */     this.ofDrippingWaterLava = true;
/* 3155 */     this.ofAnimatedTerrain = true;
/* 3156 */     this.ofAnimatedTextures = true;
/* 3157 */     Shaders.setShaderPack("OFF");
/* 3158 */     Shaders.configAntialiasingLevel = 0;
/* 3159 */     Shaders.uninit();
/* 3160 */     Shaders.storeConfig();
/* 3161 */     updateWaterOpacity();
/* 3162 */     this.mc.refreshResources();
/* 3163 */     saveOptions();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateVSync() {
/* 3168 */     Display.setVSyncEnabled(this.enableVsync);
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateWaterOpacity() {
/* 3173 */     if (Config.isIntegratedServerRunning())
/*      */     {
/* 3175 */       Config.waterOpacityChanged = true;
/*      */     }
/*      */     
/* 3178 */     ClearWater.updateWaterOpacity(this, (World)this.mc.theWorld);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAllAnimations(boolean p_setAllAnimations_1_) {
/* 3183 */     int i = p_setAllAnimations_1_ ? 0 : 2;
/* 3184 */     this.ofAnimatedWater = i;
/* 3185 */     this.ofAnimatedLava = i;
/* 3186 */     this.ofAnimatedFire = p_setAllAnimations_1_;
/* 3187 */     this.ofAnimatedPortal = p_setAllAnimations_1_;
/* 3188 */     this.ofAnimatedRedstone = p_setAllAnimations_1_;
/* 3189 */     this.ofAnimatedExplosion = p_setAllAnimations_1_;
/* 3190 */     this.ofAnimatedFlame = p_setAllAnimations_1_;
/* 3191 */     this.ofAnimatedSmoke = p_setAllAnimations_1_;
/* 3192 */     this.ofVoidParticles = p_setAllAnimations_1_;
/* 3193 */     this.ofWaterParticles = p_setAllAnimations_1_;
/* 3194 */     this.ofRainSplash = p_setAllAnimations_1_;
/* 3195 */     this.ofPortalParticles = p_setAllAnimations_1_;
/* 3196 */     this.ofPotionParticles = p_setAllAnimations_1_;
/* 3197 */     this.ofFireworkParticles = p_setAllAnimations_1_;
/* 3198 */     this.particleSetting = p_setAllAnimations_1_ ? 0 : 2;
/* 3199 */     this.ofDrippingWaterLava = p_setAllAnimations_1_;
/* 3200 */     this.ofAnimatedTerrain = p_setAllAnimations_1_;
/* 3201 */     this.ofAnimatedTextures = p_setAllAnimations_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int nextValue(int p_nextValue_0_, int[] p_nextValue_1_) {
/* 3206 */     int i = indexOf(p_nextValue_0_, p_nextValue_1_);
/*      */     
/* 3208 */     if (i < 0)
/*      */     {
/* 3210 */       return p_nextValue_1_[0];
/*      */     }
/*      */ 
/*      */     
/* 3214 */     i++;
/*      */     
/* 3216 */     if (i >= p_nextValue_1_.length)
/*      */     {
/* 3218 */       i = 0;
/*      */     }
/*      */     
/* 3221 */     return p_nextValue_1_[i];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int limit(int p_limit_0_, int[] p_limit_1_) {
/* 3227 */     int i = indexOf(p_limit_0_, p_limit_1_);
/* 3228 */     return (i < 0) ? p_limit_1_[0] : p_limit_0_;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int indexOf(int p_indexOf_0_, int[] p_indexOf_1_) {
/* 3233 */     for (int i = 0; i < p_indexOf_1_.length; i++) {
/*      */       
/* 3235 */       if (p_indexOf_1_[i] == p_indexOf_0_)
/*      */       {
/* 3237 */         return i;
/*      */       }
/*      */     } 
/*      */     
/* 3241 */     return -1;
/*      */   }
/*      */   
/*      */   public enum Options
/*      */   {
/* 3246 */     INVERT_MOUSE("options.invertMouse", false, true),
/* 3247 */     SENSITIVITY("options.sensitivity", true, false),
/* 3248 */     FOV("options.fov", true, false, 30.0F, 110.0F, 1.0F),
/* 3249 */     GAMMA("options.gamma", true, false),
/* 3250 */     SATURATION("options.saturation", true, false),
/* 3251 */     RENDER_DISTANCE("options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
/* 3252 */     VIEW_BOBBING("options.viewBobbing", false, true),
/* 3253 */     ANAGLYPH("options.anaglyph", false, true),
/* 3254 */     FRAMERATE_LIMIT("options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
/* 3255 */     FBO_ENABLE("options.fboEnable", false, true),
/* 3256 */     RENDER_CLOUDS("options.renderClouds", false, false),
/* 3257 */     GRAPHICS("options.graphics", false, false),
/* 3258 */     AMBIENT_OCCLUSION("options.ao", false, false),
/* 3259 */     GUI_SCALE("options.guiScale", false, false),
/* 3260 */     PARTICLES("options.particles", false, false),
/* 3261 */     CHAT_VISIBILITY("options.chat.visibility", false, false),
/* 3262 */     CHAT_COLOR("options.chat.color", false, true),
/* 3263 */     CHAT_LINKS("options.chat.links", false, true),
/* 3264 */     CHAT_OPACITY("options.chat.opacity", true, false),
/* 3265 */     CHAT_LINKS_PROMPT("options.chat.links.prompt", false, true),
/* 3266 */     SNOOPER_ENABLED("options.snooper", false, true),
/* 3267 */     USE_FULLSCREEN("options.fullscreen", false, true),
/* 3268 */     ENABLE_VSYNC("options.vsync", false, true),
/* 3269 */     USE_VBO("options.vbo", false, true),
/* 3270 */     TOUCHSCREEN("options.touchscreen", false, true),
/* 3271 */     CHAT_SCALE("options.chat.scale", true, false),
/* 3272 */     CHAT_WIDTH("options.chat.width", true, false),
/* 3273 */     CHAT_HEIGHT_FOCUSED("options.chat.height.focused", true, false),
/* 3274 */     CHAT_HEIGHT_UNFOCUSED("options.chat.height.unfocused", true, false),
/* 3275 */     MIPMAP_LEVELS("options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
/* 3276 */     FORCE_UNICODE_FONT("options.forceUnicodeFont", false, true),
/* 3277 */     STREAM_BYTES_PER_PIXEL("options.stream.bytesPerPixel", true, false),
/* 3278 */     STREAM_VOLUME_MIC("options.stream.micVolumne", true, false),
/* 3279 */     STREAM_VOLUME_SYSTEM("options.stream.systemVolume", true, false),
/* 3280 */     STREAM_KBPS("options.stream.kbps", true, false),
/* 3281 */     STREAM_FPS("options.stream.fps", true, false),
/* 3282 */     STREAM_COMPRESSION("options.stream.compression", false, false),
/* 3283 */     STREAM_SEND_METADATA("options.stream.sendMetadata", false, true),
/* 3284 */     STREAM_CHAT_ENABLED("options.stream.chat.enabled", false, false),
/* 3285 */     STREAM_CHAT_USER_FILTER("options.stream.chat.userFilter", false, false),
/* 3286 */     STREAM_MIC_TOGGLE_BEHAVIOR("options.stream.micToggleBehavior", false, false),
/* 3287 */     BLOCK_ALTERNATIVES("options.blockAlternatives", false, true),
/* 3288 */     REDUCED_DEBUG_INFO("options.reducedDebugInfo", false, true),
/* 3289 */     ENTITY_SHADOWS("options.entityShadows", false, true),
/* 3290 */     REALMS_NOTIFICATIONS("options.realmsNotifications", false, true),
/* 3291 */     FOG_FANCY("of.options.FOG_FANCY", false, false),
/* 3292 */     FOG_START("of.options.FOG_START", false, false),
/* 3293 */     MIPMAP_TYPE("of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
/* 3294 */     SMOOTH_FPS("of.options.SMOOTH_FPS", false, false),
/* 3295 */     CLOUDS("of.options.CLOUDS", false, false),
/* 3296 */     CLOUD_HEIGHT("of.options.CLOUD_HEIGHT", true, false),
/* 3297 */     TREES("of.options.TREES", false, false),
/* 3298 */     RAIN("of.options.RAIN", false, false),
/* 3299 */     ANIMATED_WATER("of.options.ANIMATED_WATER", false, false),
/* 3300 */     ANIMATED_LAVA("of.options.ANIMATED_LAVA", false, false),
/* 3301 */     ANIMATED_FIRE("of.options.ANIMATED_FIRE", false, false),
/* 3302 */     ANIMATED_PORTAL("of.options.ANIMATED_PORTAL", false, false),
/* 3303 */     AO_LEVEL("of.options.AO_LEVEL", true, false),
/* 3304 */     LAGOMETER("of.options.LAGOMETER", false, false),
/* 3305 */     SHOW_FPS("of.options.SHOW_FPS", false, false),
/* 3306 */     AUTOSAVE_TICKS("of.options.AUTOSAVE_TICKS", false, false),
/* 3307 */     BETTER_GRASS("of.options.BETTER_GRASS", false, false),
/* 3308 */     ANIMATED_REDSTONE("of.options.ANIMATED_REDSTONE", false, false),
/* 3309 */     ANIMATED_EXPLOSION("of.options.ANIMATED_EXPLOSION", false, false),
/* 3310 */     ANIMATED_FLAME("of.options.ANIMATED_FLAME", false, false),
/* 3311 */     ANIMATED_SMOKE("of.options.ANIMATED_SMOKE", false, false),
/* 3312 */     WEATHER("of.options.WEATHER", false, false),
/* 3313 */     SKY("of.options.SKY", false, false),
/* 3314 */     STARS("of.options.STARS", false, false),
/* 3315 */     SUN_MOON("of.options.SUN_MOON", false, false),
/* 3316 */     VIGNETTE("of.options.VIGNETTE", false, false),
/* 3317 */     CHUNK_UPDATES("of.options.CHUNK_UPDATES", false, false),
/* 3318 */     CHUNK_UPDATES_DYNAMIC("of.options.CHUNK_UPDATES_DYNAMIC", false, false),
/* 3319 */     TIME("of.options.TIME", false, false),
/* 3320 */     CLEAR_WATER("of.options.CLEAR_WATER", false, false),
/* 3321 */     SMOOTH_WORLD("of.options.SMOOTH_WORLD", false, false),
/* 3322 */     VOID_PARTICLES("of.options.VOID_PARTICLES", false, false),
/* 3323 */     WATER_PARTICLES("of.options.WATER_PARTICLES", false, false),
/* 3324 */     RAIN_SPLASH("of.options.RAIN_SPLASH", false, false),
/* 3325 */     PORTAL_PARTICLES("of.options.PORTAL_PARTICLES", false, false),
/* 3326 */     POTION_PARTICLES("of.options.POTION_PARTICLES", false, false),
/* 3327 */     FIREWORK_PARTICLES("of.options.FIREWORK_PARTICLES", false, false),
/* 3328 */     PROFILER("of.options.PROFILER", false, false),
/* 3329 */     DRIPPING_WATER_LAVA("of.options.DRIPPING_WATER_LAVA", false, false),
/* 3330 */     BETTER_SNOW("of.options.BETTER_SNOW", false, false),
/* 3331 */     FULLSCREEN_MODE("of.options.FULLSCREEN_MODE", true, false, 0.0F, (Config.getDisplayModes()).length, 1.0F),
/* 3332 */     ANIMATED_TERRAIN("of.options.ANIMATED_TERRAIN", false, false),
/* 3333 */     SWAMP_COLORS("of.options.SWAMP_COLORS", false, false),
/* 3334 */     RANDOM_ENTITIES("of.options.RANDOM_ENTITIES", false, false),
/* 3335 */     SMOOTH_BIOMES("of.options.SMOOTH_BIOMES", false, false),
/* 3336 */     CUSTOM_FONTS("of.options.CUSTOM_FONTS", false, false),
/* 3337 */     CUSTOM_COLORS("of.options.CUSTOM_COLORS", false, false),
/* 3338 */     SHOW_CAPES("of.options.SHOW_CAPES", false, false),
/* 3339 */     CONNECTED_TEXTURES("of.options.CONNECTED_TEXTURES", false, false),
/* 3340 */     CUSTOM_ITEMS("of.options.CUSTOM_ITEMS", false, false),
/* 3341 */     AA_LEVEL("of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
/* 3342 */     AF_LEVEL("of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
/* 3343 */     ANIMATED_TEXTURES("of.options.ANIMATED_TEXTURES", false, false),
/* 3344 */     NATURAL_TEXTURES("of.options.NATURAL_TEXTURES", false, false),
/* 3345 */     EMISSIVE_TEXTURES("of.options.EMISSIVE_TEXTURES", false, false),
/* 3346 */     HELD_ITEM_TOOLTIPS("of.options.HELD_ITEM_TOOLTIPS", false, false),
/* 3347 */     DROPPED_ITEMS("of.options.DROPPED_ITEMS", false, false),
/* 3348 */     LAZY_CHUNK_LOADING("of.options.LAZY_CHUNK_LOADING", false, false),
/* 3349 */     CUSTOM_SKY("of.options.CUSTOM_SKY", false, false),
/* 3350 */     FAST_MATH("of.options.FAST_MATH", false, false),
/* 3351 */     FAST_RENDER("of.options.FAST_RENDER", false, false),
/* 3352 */     TRANSLUCENT_BLOCKS("of.options.TRANSLUCENT_BLOCKS", false, false),
/* 3353 */     DYNAMIC_FOV("of.options.DYNAMIC_FOV", false, false),
/* 3354 */     DYNAMIC_LIGHTS("of.options.DYNAMIC_LIGHTS", false, false),
/* 3355 */     ALTERNATE_BLOCKS("of.options.ALTERNATE_BLOCKS", false, false),
/* 3356 */     CUSTOM_ENTITY_MODELS("of.options.CUSTOM_ENTITY_MODELS", false, false),
/* 3357 */     ADVANCED_TOOLTIPS("of.options.ADVANCED_TOOLTIPS", false, false),
/* 3358 */     SCREENSHOT_SIZE("of.options.SCREENSHOT_SIZE", false, false),
/* 3359 */     CUSTOM_GUIS("of.options.CUSTOM_GUIS", false, false),
/* 3360 */     RENDER_REGIONS("of.options.RENDER_REGIONS", false, false),
/* 3361 */     SHOW_GL_ERRORS("of.options.SHOW_GL_ERRORS", false, false),
/* 3362 */     SMART_ANIMATIONS("of.options.SMART_ANIMATIONS", false, false);
/*      */     
/*      */     private final boolean enumFloat;
/*      */     
/*      */     private final boolean enumBoolean;
/*      */     private final String enumString;
/*      */     private final float valueStep;
/*      */     private float valueMin;
/*      */     private float valueMax;
/*      */     
/*      */     public static Options getEnumOptions(int ordinal) {
/* 3373 */       for (Options gamesettings$options : values()) {
/*      */         
/* 3375 */         if (gamesettings$options.returnEnumOrdinal() == ordinal)
/*      */         {
/* 3377 */           return gamesettings$options;
/*      */         }
/*      */       } 
/*      */       
/* 3381 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Options(String str, boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep) {
/* 3391 */       this.enumString = str;
/* 3392 */       this.enumFloat = isFloat;
/* 3393 */       this.enumBoolean = isBoolean;
/* 3394 */       this.valueMin = valMin;
/* 3395 */       this.valueMax = valMax;
/* 3396 */       this.valueStep = valStep;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean getEnumFloat() {
/* 3401 */       return this.enumFloat;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean getEnumBoolean() {
/* 3406 */       return this.enumBoolean;
/*      */     }
/*      */ 
/*      */     
/*      */     public int returnEnumOrdinal() {
/* 3411 */       return ordinal();
/*      */     }
/*      */ 
/*      */     
/*      */     public String getEnumString() {
/* 3416 */       return this.enumString;
/*      */     }
/*      */ 
/*      */     
/*      */     public float getValueMax() {
/* 3421 */       return this.valueMax;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueMax(float value) {
/* 3426 */       this.valueMax = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public float normalizeValue(float value) {
/* 3431 */       return MathHelper.clamp_float((snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
/*      */     }
/*      */ 
/*      */     
/*      */     public float denormalizeValue(float value) {
/* 3436 */       return snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(value, 0.0F, 1.0F));
/*      */     }
/*      */ 
/*      */     
/*      */     public float snapToStepClamp(float value) {
/* 3441 */       value = snapToStep(value);
/* 3442 */       return MathHelper.clamp_float(value, this.valueMin, this.valueMax);
/*      */     }
/*      */ 
/*      */     
/*      */     protected float snapToStep(float value) {
/* 3447 */       if (this.valueStep > 0.0F)
/*      */       {
/* 3449 */         value = this.valueStep * Math.round(value / this.valueStep);
/*      */       }
/*      */       
/* 3452 */       return value;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\settings\GameSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */