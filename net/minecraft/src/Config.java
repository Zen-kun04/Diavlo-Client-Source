/*      */ package net.minecraft.src;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.lang.reflect.Array;
/*      */ import java.net.URI;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.imageio.ImageIO;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*      */ import net.minecraft.client.resources.DefaultResourcePack;
/*      */ import net.minecraft.client.resources.IResource;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourcePack;
/*      */ import net.minecraft.client.resources.ResourcePackRepository;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.FrameTimer;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Util;
/*      */ import net.optifine.VersionCheckThread;
/*      */ import net.optifine.config.GlVersion;
/*      */ import net.optifine.gui.GuiMessage;
/*      */ import net.optifine.reflect.Reflector;
/*      */ import net.optifine.shaders.Shaders;
/*      */ import net.optifine.util.DisplayModeComparator;
/*      */ import net.optifine.util.PropertiesOrdered;
/*      */ import org.apache.commons.io.IOUtils;
/*      */ import org.lwjgl.LWJGLException;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.DisplayMode;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GLContext;
/*      */ import org.lwjgl.opengl.PixelFormat;
/*      */ 
/*      */ public class Config {
/*      */   public static final String OF_NAME = "OptiFine";
/*      */   public static final String MC_VERSION = "1.8.9";
/*   58 */   private static String build = null; public static final String OF_EDITION = "HD_U"; public static final String OF_RELEASE = "M6_pre2"; public static final String VERSION = "OptiFine_1.8.9_HD_U_M6_pre2";
/*   59 */   private static String newRelease = null;
/*      */   private static boolean notify64BitJava = false;
/*   61 */   public static String openGlVersion = null;
/*   62 */   public static String openGlRenderer = null;
/*   63 */   public static String openGlVendor = null;
/*   64 */   public static String[] openGlExtensions = null;
/*   65 */   public static GlVersion glVersion = null;
/*   66 */   public static GlVersion glslVersion = null;
/*   67 */   public static int minecraftVersionInt = -1;
/*      */   public static boolean fancyFogAvailable = false;
/*      */   public static boolean occlusionAvailable = false;
/*   70 */   private static GameSettings gameSettings = null;
/*   71 */   private static Minecraft minecraft = Minecraft.getMinecraft();
/*      */   private static boolean initialized = false;
/*   73 */   private static Thread minecraftThread = null;
/*   74 */   private static DisplayMode desktopDisplayMode = null;
/*   75 */   private static DisplayMode[] displayModes = null;
/*   76 */   private static int antialiasingLevel = 0;
/*   77 */   private static int availableProcessors = 0;
/*      */   public static boolean zoomMode = false;
/*      */   public static boolean zoomSmoothCamera = false;
/*   80 */   private static int texturePackClouds = 0;
/*      */   public static boolean waterOpacityChanged = false;
/*      */   private static boolean fullscreenModeChecked = false;
/*      */   private static boolean desktopModeChecked = false;
/*   84 */   private static DefaultResourcePack defaultResourcePackLazy = null;
/*   85 */   public static final Float DEF_ALPHA_FUNC_LEVEL = Float.valueOf(0.1F);
/*   86 */   private static final Logger LOGGER = LogManager.getLogger();
/*   87 */   public static final boolean logDetail = System.getProperty("log.detail", "false").equals("true");
/*   88 */   private static String mcDebugLast = null;
/*   89 */   private static int fpsMinLast = 0;
/*      */   
/*      */   public static float renderPartialTicks;
/*      */   
/*      */   public static String getVersion() {
/*   94 */     return "OptiFine_1.8.9_HD_U_M6_pre2";
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getVersionDebug() {
/*   99 */     StringBuffer stringbuffer = new StringBuffer(32);
/*      */     
/*  101 */     if (isDynamicLights()) {
/*      */       
/*  103 */       stringbuffer.append("DL: ");
/*  104 */       stringbuffer.append(String.valueOf(DynamicLights.getCount()));
/*  105 */       stringbuffer.append(", ");
/*      */     } 
/*      */     
/*  108 */     stringbuffer.append("OptiFine_1.8.9_HD_U_M6_pre2");
/*  109 */     String s = Shaders.getShaderPackName();
/*      */     
/*  111 */     if (s != null) {
/*      */       
/*  113 */       stringbuffer.append(", ");
/*  114 */       stringbuffer.append(s);
/*      */     } 
/*      */     
/*  117 */     return stringbuffer.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void initGameSettings(GameSettings p_initGameSettings_0_) {
/*  122 */     if (gameSettings == null) {
/*      */       
/*  124 */       gameSettings = p_initGameSettings_0_;
/*  125 */       desktopDisplayMode = Display.getDesktopDisplayMode();
/*  126 */       updateAvailableProcessors();
/*  127 */       ReflectorForge.putLaunchBlackboard("optifine.ForgeSplashCompatible", Boolean.TRUE);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void initDisplay() {
/*  133 */     checkInitialized();
/*  134 */     antialiasingLevel = gameSettings.ofAaLevel;
/*  135 */     checkDisplaySettings();
/*  136 */     checkDisplayMode();
/*  137 */     minecraftThread = Thread.currentThread();
/*  138 */     updateThreadPriorities();
/*  139 */     Shaders.startup(Minecraft.getMinecraft());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkInitialized() {
/*  144 */     if (!initialized)
/*      */     {
/*  146 */       if (Display.isCreated()) {
/*      */         
/*  148 */         initialized = true;
/*  149 */         checkOpenGlCaps();
/*  150 */         startVersionCheckThread();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void checkOpenGlCaps() {
/*  157 */     log("");
/*  158 */     log(getVersion());
/*  159 */     log("Build: " + getBuild());
/*  160 */     log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
/*  161 */     log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
/*  162 */     log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
/*  163 */     log("LWJGL: " + Sys.getVersion());
/*  164 */     openGlVersion = GL11.glGetString(7938);
/*  165 */     openGlRenderer = GL11.glGetString(7937);
/*  166 */     openGlVendor = GL11.glGetString(7936);
/*  167 */     log("OpenGL: " + openGlRenderer + ", version " + openGlVersion + ", " + openGlVendor);
/*  168 */     log("OpenGL Version: " + getOpenGlVersionString());
/*      */     
/*  170 */     if (!(GLContext.getCapabilities()).OpenGL12)
/*      */     {
/*  172 */       log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
/*      */     }
/*      */     
/*  175 */     fancyFogAvailable = (GLContext.getCapabilities()).GL_NV_fog_distance;
/*      */     
/*  177 */     if (!fancyFogAvailable)
/*      */     {
/*  179 */       log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
/*      */     }
/*      */     
/*  182 */     occlusionAvailable = (GLContext.getCapabilities()).GL_ARB_occlusion_query;
/*      */     
/*  184 */     if (!occlusionAvailable)
/*      */     {
/*  186 */       log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
/*      */     }
/*      */     
/*  189 */     int i = TextureUtils.getGLMaximumTextureSize();
/*  190 */     dbg("Maximum texture size: " + i + "x" + i);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getBuild() {
/*  195 */     if (build == null) {
/*      */       
/*      */       try {
/*      */         
/*  199 */         InputStream inputstream = Config.class.getResourceAsStream("/buildof.txt");
/*      */         
/*  201 */         if (inputstream == null)
/*      */         {
/*  203 */           return null;
/*      */         }
/*      */         
/*  206 */         build = readLines(inputstream)[0];
/*      */       }
/*  208 */       catch (Exception exception) {
/*      */         
/*  210 */         warn("" + exception.getClass().getName() + ": " + exception.getMessage());
/*  211 */         build = "";
/*      */       } 
/*      */     }
/*      */     
/*  215 */     return build;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFancyFogAvailable() {
/*  220 */     return fancyFogAvailable;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOcclusionAvailable() {
/*  225 */     return occlusionAvailable;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getMinecraftVersionInt() {
/*  230 */     if (minecraftVersionInt < 0) {
/*      */       
/*  232 */       String[] astring = tokenize("1.8.9", ".");
/*  233 */       int i = 0;
/*      */       
/*  235 */       if (astring.length > 0)
/*      */       {
/*  237 */         i += 10000 * parseInt(astring[0], 0);
/*      */       }
/*      */       
/*  240 */       if (astring.length > 1)
/*      */       {
/*  242 */         i += 100 * parseInt(astring[1], 0);
/*      */       }
/*      */       
/*  245 */       if (astring.length > 2)
/*      */       {
/*  247 */         i += 1 * parseInt(astring[2], 0);
/*      */       }
/*      */       
/*  250 */       minecraftVersionInt = i;
/*      */     } 
/*      */     
/*  253 */     return minecraftVersionInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getOpenGlVersionString() {
/*  258 */     GlVersion glversion = getGlVersion();
/*  259 */     String s = "" + glversion.getMajor() + "." + glversion.getMinor() + "." + glversion.getRelease();
/*  260 */     return s;
/*      */   }
/*      */ 
/*      */   
/*      */   private static GlVersion getGlVersionLwjgl() {
/*  265 */     return (GLContext.getCapabilities()).OpenGL44 ? new GlVersion(4, 4) : ((GLContext.getCapabilities()).OpenGL43 ? new GlVersion(4, 3) : ((GLContext.getCapabilities()).OpenGL42 ? new GlVersion(4, 2) : ((GLContext.getCapabilities()).OpenGL41 ? new GlVersion(4, 1) : ((GLContext.getCapabilities()).OpenGL40 ? new GlVersion(4, 0) : ((GLContext.getCapabilities()).OpenGL33 ? new GlVersion(3, 3) : ((GLContext.getCapabilities()).OpenGL32 ? new GlVersion(3, 2) : ((GLContext.getCapabilities()).OpenGL31 ? new GlVersion(3, 1) : ((GLContext.getCapabilities()).OpenGL30 ? new GlVersion(3, 0) : ((GLContext.getCapabilities()).OpenGL21 ? new GlVersion(2, 1) : ((GLContext.getCapabilities()).OpenGL20 ? new GlVersion(2, 0) : ((GLContext.getCapabilities()).OpenGL15 ? new GlVersion(1, 5) : ((GLContext.getCapabilities()).OpenGL14 ? new GlVersion(1, 4) : ((GLContext.getCapabilities()).OpenGL13 ? new GlVersion(1, 3) : ((GLContext.getCapabilities()).OpenGL12 ? new GlVersion(1, 2) : ((GLContext.getCapabilities()).OpenGL11 ? new GlVersion(1, 1) : new GlVersion(1, 0))))))))))))))));
/*      */   }
/*      */ 
/*      */   
/*      */   public static GlVersion getGlVersion() {
/*  270 */     if (glVersion == null) {
/*      */       
/*  272 */       String s = GL11.glGetString(7938);
/*  273 */       glVersion = parseGlVersion(s, (GlVersion)null);
/*      */       
/*  275 */       if (glVersion == null)
/*      */       {
/*  277 */         glVersion = getGlVersionLwjgl();
/*      */       }
/*      */       
/*  280 */       if (glVersion == null)
/*      */       {
/*  282 */         glVersion = new GlVersion(1, 0);
/*      */       }
/*      */     } 
/*      */     
/*  286 */     return glVersion;
/*      */   }
/*      */ 
/*      */   
/*      */   public static GlVersion getGlslVersion() {
/*  291 */     if (glslVersion == null) {
/*      */       
/*  293 */       String s = GL11.glGetString(35724);
/*  294 */       glslVersion = parseGlVersion(s, (GlVersion)null);
/*      */       
/*  296 */       if (glslVersion == null)
/*      */       {
/*  298 */         glslVersion = new GlVersion(1, 10);
/*      */       }
/*      */     } 
/*      */     
/*  302 */     return glslVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static GlVersion parseGlVersion(String p_parseGlVersion_0_, GlVersion p_parseGlVersion_1_) {
/*      */     try {
/*  309 */       if (p_parseGlVersion_0_ == null)
/*      */       {
/*  311 */         return p_parseGlVersion_1_;
/*      */       }
/*      */ 
/*      */       
/*  315 */       Pattern pattern = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?(.+)?");
/*  316 */       Matcher matcher = pattern.matcher(p_parseGlVersion_0_);
/*      */       
/*  318 */       if (!matcher.matches())
/*      */       {
/*  320 */         return p_parseGlVersion_1_;
/*      */       }
/*      */ 
/*      */       
/*  324 */       int i = Integer.parseInt(matcher.group(1));
/*  325 */       int j = Integer.parseInt(matcher.group(2));
/*  326 */       int k = (matcher.group(4) != null) ? Integer.parseInt(matcher.group(4)) : 0;
/*  327 */       String s = matcher.group(5);
/*  328 */       return new GlVersion(i, j, k, s);
/*      */ 
/*      */     
/*      */     }
/*  332 */     catch (Exception exception) {
/*      */       
/*  334 */       exception.printStackTrace();
/*  335 */       return p_parseGlVersion_1_;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static String[] getOpenGlExtensions() {
/*  341 */     if (openGlExtensions == null)
/*      */     {
/*  343 */       openGlExtensions = detectOpenGlExtensions();
/*      */     }
/*      */     
/*  346 */     return openGlExtensions;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] detectOpenGlExtensions() {
/*      */     try {
/*  353 */       GlVersion glversion = getGlVersion();
/*      */       
/*  355 */       if (glversion.getMajor() >= 3) {
/*      */         
/*  357 */         int i = GL11.glGetInteger(33309);
/*      */         
/*  359 */         if (i > 0)
/*      */         {
/*  361 */           String[] astring = new String[i];
/*      */           
/*  363 */           for (int j = 0; j < i; j++)
/*      */           {
/*  365 */             astring[j] = GL30.glGetStringi(7939, j);
/*      */           }
/*      */           
/*  368 */           return astring;
/*      */         }
/*      */       
/*      */       } 
/*  372 */     } catch (Exception exception1) {
/*      */       
/*  374 */       exception1.printStackTrace();
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  379 */       String s = GL11.glGetString(7939);
/*  380 */       String[] astring1 = s.split(" ");
/*  381 */       return astring1;
/*      */     }
/*  383 */     catch (Exception exception) {
/*      */       
/*  385 */       exception.printStackTrace();
/*  386 */       return new String[0];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void updateThreadPriorities() {
/*  392 */     updateAvailableProcessors();
/*  393 */     int i = 8;
/*      */     
/*  395 */     if (isSingleProcessor()) {
/*      */       
/*  397 */       if (isSmoothWorld())
/*      */       {
/*  399 */         minecraftThread.setPriority(10);
/*  400 */         setThreadPriority("Server thread", 1);
/*      */       }
/*      */       else
/*      */       {
/*  404 */         minecraftThread.setPriority(5);
/*  405 */         setThreadPriority("Server thread", 5);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  410 */       minecraftThread.setPriority(10);
/*  411 */       setThreadPriority("Server thread", 5);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setThreadPriority(String p_setThreadPriority_0_, int p_setThreadPriority_1_) {
/*      */     try {
/*  419 */       ThreadGroup threadgroup = Thread.currentThread().getThreadGroup();
/*      */       
/*  421 */       if (threadgroup == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  426 */       int i = (threadgroup.activeCount() + 10) * 2;
/*  427 */       Thread[] athread = new Thread[i];
/*  428 */       threadgroup.enumerate(athread, false);
/*      */       
/*  430 */       for (int j = 0; j < athread.length; j++)
/*      */       {
/*  432 */         Thread thread = athread[j];
/*      */         
/*  434 */         if (thread != null && thread.getName().startsWith(p_setThreadPriority_0_))
/*      */         {
/*  436 */           thread.setPriority(p_setThreadPriority_1_);
/*      */         }
/*      */       }
/*      */     
/*  440 */     } catch (Throwable throwable) {
/*      */       
/*  442 */       warn(throwable.getClass().getName() + ": " + throwable.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMinecraftThread() {
/*  448 */     return (Thread.currentThread() == minecraftThread);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void startVersionCheckThread() {
/*  453 */     VersionCheckThread versioncheckthread = new VersionCheckThread();
/*  454 */     versioncheckthread.start();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMipmaps() {
/*  459 */     return (gameSettings.mipmapLevels > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getMipmapLevels() {
/*  464 */     return gameSettings.mipmapLevels;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getMipmapType() {
/*  469 */     switch (gameSettings.ofMipmapType) {
/*      */       
/*      */       case 0:
/*  472 */         return 9986;
/*      */       
/*      */       case 1:
/*  475 */         return 9986;
/*      */       
/*      */       case 2:
/*  478 */         if (isMultiTexture())
/*      */         {
/*  480 */           return 9985;
/*      */         }
/*      */         
/*  483 */         return 9986;
/*      */       
/*      */       case 3:
/*  486 */         if (isMultiTexture())
/*      */         {
/*  488 */           return 9987;
/*      */         }
/*      */         
/*  491 */         return 9986;
/*      */     } 
/*      */     
/*  494 */     return 9986;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUseAlphaFunc() {
/*  500 */     float f = getAlphaFuncLevel();
/*  501 */     return (f > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F);
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getAlphaFuncLevel() {
/*  506 */     return DEF_ALPHA_FUNC_LEVEL.floatValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFogFancy() {
/*  511 */     return !isFancyFogAvailable() ? false : ((gameSettings.ofFogType == 2));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFogFast() {
/*  516 */     return (gameSettings.ofFogType == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFogOff() {
/*  521 */     return (gameSettings.ofFogType == 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFogOn() {
/*  526 */     return (gameSettings.ofFogType != 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getFogStart() {
/*  531 */     return gameSettings.ofFogStart;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void detail(String p_detail_0_) {
/*  536 */     if (logDetail)
/*      */     {
/*  538 */       LOGGER.info("[OptiFine] " + p_detail_0_);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void dbg(String p_dbg_0_) {
/*  544 */     LOGGER.info("[OptiFine] " + p_dbg_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void warn(String p_warn_0_) {
/*  549 */     LOGGER.warn("[OptiFine] " + p_warn_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void error(String p_error_0_) {
/*  554 */     LOGGER.error("[OptiFine] " + p_error_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void log(String p_log_0_) {
/*  559 */     dbg(p_log_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getUpdatesPerFrame() {
/*  564 */     return gameSettings.ofChunkUpdates;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDynamicUpdates() {
/*  569 */     return gameSettings.ofChunkUpdatesDynamic;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRainFancy() {
/*  574 */     return (gameSettings.ofRain == 0) ? gameSettings.fancyGraphics : ((gameSettings.ofRain == 2));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRainOff() {
/*  579 */     return (gameSettings.ofRain == 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCloudsFancy() {
/*  584 */     return (gameSettings.ofClouds != 0) ? ((gameSettings.ofClouds == 2)) : ((isShaders() && !Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isFancy() : ((texturePackClouds != 0) ? ((texturePackClouds == 2)) : gameSettings.fancyGraphics));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCloudsOff() {
/*  589 */     return (gameSettings.ofClouds != 0) ? ((gameSettings.ofClouds == 3)) : ((isShaders() && !Shaders.shaderPackClouds.isDefault()) ? Shaders.shaderPackClouds.isOff() : ((texturePackClouds != 0) ? ((texturePackClouds == 3)) : false));
/*      */   }
/*      */ 
/*      */   
/*      */   public static void updateTexturePackClouds() {
/*  594 */     texturePackClouds = 0;
/*  595 */     IResourceManager iresourcemanager = getResourceManager();
/*      */     
/*  597 */     if (iresourcemanager != null) {
/*      */       
/*      */       try {
/*      */         
/*  601 */         InputStream inputstream = iresourcemanager.getResource(new ResourceLocation("mcpatcher/color.properties")).getInputStream();
/*      */         
/*  603 */         if (inputstream == null) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  608 */         PropertiesOrdered propertiesOrdered = new PropertiesOrdered();
/*  609 */         propertiesOrdered.load(inputstream);
/*  610 */         inputstream.close();
/*  611 */         String s = propertiesOrdered.getProperty("clouds");
/*      */         
/*  613 */         if (s == null) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  618 */         dbg("Texture pack clouds: " + s);
/*  619 */         s = s.toLowerCase();
/*      */         
/*  621 */         if (s.equals("fast"))
/*      */         {
/*  623 */           texturePackClouds = 1;
/*      */         }
/*      */         
/*  626 */         if (s.equals("fancy"))
/*      */         {
/*  628 */           texturePackClouds = 2;
/*      */         }
/*      */         
/*  631 */         if (s.equals("off"))
/*      */         {
/*  633 */           texturePackClouds = 3;
/*      */         }
/*      */       }
/*  636 */       catch (Exception exception) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ModelManager getModelManager() {
/*  645 */     return (minecraft.getRenderItem()).modelManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTreesFancy() {
/*  650 */     return (gameSettings.ofTrees == 0) ? gameSettings.fancyGraphics : ((gameSettings.ofTrees != 1));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTreesSmart() {
/*  655 */     return (gameSettings.ofTrees == 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCullFacesLeaves() {
/*  660 */     return (gameSettings.ofTrees == 0) ? (!gameSettings.fancyGraphics) : ((gameSettings.ofTrees == 4));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDroppedItemsFancy() {
/*  665 */     return (gameSettings.ofDroppedItems == 0) ? gameSettings.fancyGraphics : ((gameSettings.ofDroppedItems == 2));
/*      */   }
/*      */ 
/*      */   
/*      */   public static int limit(int p_limit_0_, int p_limit_1_, int p_limit_2_) {
/*  670 */     return (p_limit_0_ < p_limit_1_) ? p_limit_1_ : ((p_limit_0_ > p_limit_2_) ? p_limit_2_ : p_limit_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static float limit(float p_limit_0_, float p_limit_1_, float p_limit_2_) {
/*  675 */     return (p_limit_0_ < p_limit_1_) ? p_limit_1_ : ((p_limit_0_ > p_limit_2_) ? p_limit_2_ : p_limit_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static double limit(double p_limit_0_, double p_limit_2_, double p_limit_4_) {
/*  680 */     return (p_limit_0_ < p_limit_2_) ? p_limit_2_ : ((p_limit_0_ > p_limit_4_) ? p_limit_4_ : p_limit_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static float limitTo1(float p_limitTo1_0_) {
/*  685 */     return (p_limitTo1_0_ < 0.0F) ? 0.0F : ((p_limitTo1_0_ > 1.0F) ? 1.0F : p_limitTo1_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedWater() {
/*  690 */     return (gameSettings.ofAnimatedWater != 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isGeneratedWater() {
/*  695 */     return (gameSettings.ofAnimatedWater == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedPortal() {
/*  700 */     return gameSettings.ofAnimatedPortal;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedLava() {
/*  705 */     return (gameSettings.ofAnimatedLava != 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isGeneratedLava() {
/*  710 */     return (gameSettings.ofAnimatedLava == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedFire() {
/*  715 */     return gameSettings.ofAnimatedFire;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedRedstone() {
/*  720 */     return gameSettings.ofAnimatedRedstone;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedExplosion() {
/*  725 */     return gameSettings.ofAnimatedExplosion;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedFlame() {
/*  730 */     return gameSettings.ofAnimatedFlame;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedSmoke() {
/*  735 */     return gameSettings.ofAnimatedSmoke;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isVoidParticles() {
/*  740 */     return gameSettings.ofVoidParticles;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isWaterParticles() {
/*  745 */     return gameSettings.ofWaterParticles;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRainSplash() {
/*  750 */     return gameSettings.ofRainSplash;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isPortalParticles() {
/*  755 */     return gameSettings.ofPortalParticles;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isPotionParticles() {
/*  760 */     return gameSettings.ofPotionParticles;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFireworkParticles() {
/*  765 */     return gameSettings.ofFireworkParticles;
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getAmbientOcclusionLevel() {
/*  770 */     return (isShaders() && Shaders.aoLevel >= 0.0F) ? Shaders.aoLevel : gameSettings.ofAoLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String listToString(List p_listToString_0_) {
/*  775 */     return listToString(p_listToString_0_, ", ");
/*      */   }
/*      */ 
/*      */   
/*      */   public static String listToString(List p_listToString_0_, String p_listToString_1_) {
/*  780 */     if (p_listToString_0_ == null)
/*      */     {
/*  782 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  786 */     StringBuffer stringbuffer = new StringBuffer(p_listToString_0_.size() * 5);
/*      */     
/*  788 */     for (int i = 0; i < p_listToString_0_.size(); i++) {
/*      */       
/*  790 */       Object object = p_listToString_0_.get(i);
/*      */       
/*  792 */       if (i > 0)
/*      */       {
/*  794 */         stringbuffer.append(p_listToString_1_);
/*      */       }
/*      */       
/*  797 */       stringbuffer.append(String.valueOf(object));
/*      */     } 
/*      */     
/*  800 */     return stringbuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToString(Object[] p_arrayToString_0_) {
/*  806 */     return arrayToString(p_arrayToString_0_, ", ");
/*      */   }
/*      */ 
/*      */   
/*      */   public static String arrayToString(Object[] p_arrayToString_0_, String p_arrayToString_1_) {
/*  811 */     if (p_arrayToString_0_ == null)
/*      */     {
/*  813 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  817 */     StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
/*      */     
/*  819 */     for (int i = 0; i < p_arrayToString_0_.length; i++) {
/*      */       
/*  821 */       Object object = p_arrayToString_0_[i];
/*      */       
/*  823 */       if (i > 0)
/*      */       {
/*  825 */         stringbuffer.append(p_arrayToString_1_);
/*      */       }
/*      */       
/*  828 */       stringbuffer.append(String.valueOf(object));
/*      */     } 
/*      */     
/*  831 */     return stringbuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToString(int[] p_arrayToString_0_) {
/*  837 */     return arrayToString(p_arrayToString_0_, ", ");
/*      */   }
/*      */ 
/*      */   
/*      */   public static String arrayToString(int[] p_arrayToString_0_, String p_arrayToString_1_) {
/*  842 */     if (p_arrayToString_0_ == null)
/*      */     {
/*  844 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  848 */     StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
/*      */     
/*  850 */     for (int i = 0; i < p_arrayToString_0_.length; i++) {
/*      */       
/*  852 */       int j = p_arrayToString_0_[i];
/*      */       
/*  854 */       if (i > 0)
/*      */       {
/*  856 */         stringbuffer.append(p_arrayToString_1_);
/*      */       }
/*      */       
/*  859 */       stringbuffer.append(String.valueOf(j));
/*      */     } 
/*      */     
/*  862 */     return stringbuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToString(float[] p_arrayToString_0_) {
/*  868 */     return arrayToString(p_arrayToString_0_, ", ");
/*      */   }
/*      */ 
/*      */   
/*      */   public static String arrayToString(float[] p_arrayToString_0_, String p_arrayToString_1_) {
/*  873 */     if (p_arrayToString_0_ == null)
/*      */     {
/*  875 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  879 */     StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
/*      */     
/*  881 */     for (int i = 0; i < p_arrayToString_0_.length; i++) {
/*      */       
/*  883 */       float f = p_arrayToString_0_[i];
/*      */       
/*  885 */       if (i > 0)
/*      */       {
/*  887 */         stringbuffer.append(p_arrayToString_1_);
/*      */       }
/*      */       
/*  890 */       stringbuffer.append(String.valueOf(f));
/*      */     } 
/*      */     
/*  893 */     return stringbuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Minecraft getMinecraft() {
/*  899 */     return minecraft;
/*      */   }
/*      */ 
/*      */   
/*      */   public static TextureManager getTextureManager() {
/*  904 */     return minecraft.getTextureManager();
/*      */   }
/*      */ 
/*      */   
/*      */   public static IResourceManager getResourceManager() {
/*  909 */     return minecraft.getResourceManager();
/*      */   }
/*      */ 
/*      */   
/*      */   public static InputStream getResourceStream(ResourceLocation p_getResourceStream_0_) throws IOException {
/*  914 */     return getResourceStream(minecraft.getResourceManager(), p_getResourceStream_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static InputStream getResourceStream(IResourceManager p_getResourceStream_0_, ResourceLocation p_getResourceStream_1_) throws IOException {
/*  919 */     IResource iresource = p_getResourceStream_0_.getResource(p_getResourceStream_1_);
/*  920 */     return (iresource == null) ? null : iresource.getInputStream();
/*      */   }
/*      */ 
/*      */   
/*      */   public static IResource getResource(ResourceLocation p_getResource_0_) throws IOException {
/*  925 */     return minecraft.getResourceManager().getResource(p_getResource_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean hasResource(ResourceLocation p_hasResource_0_) {
/*  930 */     if (p_hasResource_0_ == null)
/*      */     {
/*  932 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  936 */     IResourcePack iresourcepack = getDefiningResourcePack(p_hasResource_0_);
/*  937 */     return (iresourcepack != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasResource(IResourceManager p_hasResource_0_, ResourceLocation p_hasResource_1_) {
/*      */     try {
/*  945 */       IResource iresource = p_hasResource_0_.getResource(p_hasResource_1_);
/*  946 */       return (iresource != null);
/*      */     }
/*  948 */     catch (IOException var3) {
/*      */       
/*  950 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static IResourcePack[] getResourcePacks() {
/*  956 */     ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
/*  957 */     List list = resourcepackrepository.getRepositoryEntries();
/*  958 */     List<IResourcePack> list1 = new ArrayList();
/*      */     
/*  960 */     for (Object o : list) {
/*      */       
/*  962 */       ResourcePackRepository.Entry resourcepackrepository$entry = (ResourcePackRepository.Entry)o;
/*  963 */       list1.add(resourcepackrepository$entry.getResourcePack());
/*      */     } 
/*      */     
/*  966 */     if (resourcepackrepository.getResourcePackInstance() != null)
/*      */     {
/*  968 */       list1.add(resourcepackrepository.getResourcePackInstance());
/*      */     }
/*      */     
/*  971 */     IResourcePack[] airesourcepack = list1.<IResourcePack>toArray(new IResourcePack[list1.size()]);
/*  972 */     return airesourcepack;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getResourcePackNames() {
/*  977 */     if (minecraft.getResourcePackRepository() == null)
/*      */     {
/*  979 */       return "";
/*      */     }
/*      */ 
/*      */     
/*  983 */     IResourcePack[] airesourcepack = getResourcePacks();
/*      */     
/*  985 */     if (airesourcepack.length <= 0)
/*      */     {
/*  987 */       return getDefaultResourcePack().getPackName();
/*      */     }
/*      */ 
/*      */     
/*  991 */     String[] astring = new String[airesourcepack.length];
/*      */     
/*  993 */     for (int i = 0; i < airesourcepack.length; i++)
/*      */     {
/*  995 */       astring[i] = airesourcepack[i].getPackName();
/*      */     }
/*      */     
/*  998 */     String s = arrayToString((Object[])astring);
/*  999 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DefaultResourcePack getDefaultResourcePack() {
/* 1006 */     if (defaultResourcePackLazy == null) {
/*      */       
/* 1008 */       Minecraft minecraft = Minecraft.getMinecraft();
/* 1009 */       defaultResourcePackLazy = (DefaultResourcePack)Reflector.getFieldValue(minecraft, Reflector.Minecraft_defaultResourcePack);
/*      */       
/* 1011 */       if (defaultResourcePackLazy == null) {
/*      */         
/* 1013 */         ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
/*      */         
/* 1015 */         if (resourcepackrepository != null)
/*      */         {
/* 1017 */           defaultResourcePackLazy = (DefaultResourcePack)resourcepackrepository.rprDefaultResourcePack;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1022 */     return defaultResourcePackLazy;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFromDefaultResourcePack(ResourceLocation p_isFromDefaultResourcePack_0_) {
/* 1027 */     IResourcePack iresourcepack = getDefiningResourcePack(p_isFromDefaultResourcePack_0_);
/* 1028 */     return (iresourcepack == getDefaultResourcePack());
/*      */   }
/*      */ 
/*      */   
/*      */   public static IResourcePack getDefiningResourcePack(ResourceLocation p_getDefiningResourcePack_0_) {
/* 1033 */     ResourcePackRepository resourcepackrepository = minecraft.getResourcePackRepository();
/* 1034 */     IResourcePack iresourcepack = resourcepackrepository.getResourcePackInstance();
/*      */     
/* 1036 */     if (iresourcepack != null && iresourcepack.resourceExists(p_getDefiningResourcePack_0_))
/*      */     {
/* 1038 */       return iresourcepack;
/*      */     }
/*      */ 
/*      */     
/* 1042 */     List<ResourcePackRepository.Entry> list = resourcepackrepository.repositoryEntries;
/*      */     
/* 1044 */     for (int i = list.size() - 1; i >= 0; i--) {
/*      */       
/* 1046 */       ResourcePackRepository.Entry resourcepackrepository$entry = list.get(i);
/* 1047 */       IResourcePack iresourcepack1 = resourcepackrepository$entry.getResourcePack();
/*      */       
/* 1049 */       if (iresourcepack1.resourceExists(p_getDefiningResourcePack_0_))
/*      */       {
/* 1051 */         return iresourcepack1;
/*      */       }
/*      */     } 
/*      */     
/* 1055 */     if (getDefaultResourcePack().resourceExists(p_getDefiningResourcePack_0_))
/*      */     {
/* 1057 */       return (IResourcePack)getDefaultResourcePack();
/*      */     }
/*      */ 
/*      */     
/* 1061 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RenderGlobal getRenderGlobal() {
/* 1068 */     return minecraft.renderGlobal;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isBetterGrass() {
/* 1073 */     return (gameSettings.ofBetterGrass != 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isBetterGrassFancy() {
/* 1078 */     return (gameSettings.ofBetterGrass == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isWeatherEnabled() {
/* 1083 */     return gameSettings.ofWeather;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSkyEnabled() {
/* 1088 */     return gameSettings.ofSky;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSunMoonEnabled() {
/* 1093 */     return gameSettings.ofSunMoon;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSunTexture() {
/* 1098 */     return !isSunMoonEnabled() ? false : ((!isShaders() || Shaders.isSun()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMoonTexture() {
/* 1103 */     return !isSunMoonEnabled() ? false : ((!isShaders() || Shaders.isMoon()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isVignetteEnabled() {
/* 1108 */     return (isShaders() && !Shaders.isVignette()) ? false : ((gameSettings.ofVignette == 0) ? gameSettings.fancyGraphics : ((gameSettings.ofVignette == 2)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isStarsEnabled() {
/* 1113 */     return gameSettings.ofStars;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void sleep(long p_sleep_0_) {
/*      */     try {
/* 1120 */       Thread.sleep(p_sleep_0_);
/*      */     }
/* 1122 */     catch (InterruptedException interruptedexception) {
/*      */       
/* 1124 */       interruptedexception.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTimeDayOnly() {
/* 1130 */     return (gameSettings.ofTime == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTimeDefault() {
/* 1135 */     return (gameSettings.ofTime == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTimeNightOnly() {
/* 1140 */     return (gameSettings.ofTime == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isClearWater() {
/* 1145 */     return gameSettings.ofClearWater;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getAnisotropicFilterLevel() {
/* 1150 */     return gameSettings.ofAfLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnisotropicFiltering() {
/* 1155 */     return (getAnisotropicFilterLevel() > 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getAntialiasingLevel() {
/* 1160 */     return antialiasingLevel;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAntialiasing() {
/* 1165 */     return (getAntialiasingLevel() > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAntialiasingConfigured() {
/* 1170 */     return ((getGameSettings()).ofAaLevel > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMultiTexture() {
/* 1175 */     return (getAnisotropicFilterLevel() > 1) ? true : ((getAntialiasingLevel() > 0));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean between(int p_between_0_, int p_between_1_, int p_between_2_) {
/* 1180 */     return (p_between_0_ >= p_between_1_ && p_between_0_ <= p_between_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean between(float p_between_0_, float p_between_1_, float p_between_2_) {
/* 1185 */     return (p_between_0_ >= p_between_1_ && p_between_0_ <= p_between_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDrippingWaterLava() {
/* 1190 */     return gameSettings.ofDrippingWaterLava;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isBetterSnow() {
/* 1195 */     return gameSettings.ofBetterSnow;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Dimension getFullscreenDimension() {
/* 1200 */     if (desktopDisplayMode == null)
/*      */     {
/* 1202 */       return null;
/*      */     }
/* 1204 */     if (gameSettings == null)
/*      */     {
/* 1206 */       return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
/*      */     }
/*      */ 
/*      */     
/* 1210 */     String s = gameSettings.ofFullscreenMode;
/*      */     
/* 1212 */     if (s.equals("Default"))
/*      */     {
/* 1214 */       return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
/*      */     }
/*      */ 
/*      */     
/* 1218 */     String[] astring = tokenize(s, " x");
/* 1219 */     return (astring.length < 2) ? new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight()) : new Dimension(parseInt(astring[0], -1), parseInt(astring[1], -1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int parseInt(String p_parseInt_0_, int p_parseInt_1_) {
/*      */     try {
/* 1228 */       if (p_parseInt_0_ == null)
/*      */       {
/* 1230 */         return p_parseInt_1_;
/*      */       }
/*      */ 
/*      */       
/* 1234 */       p_parseInt_0_ = p_parseInt_0_.trim();
/* 1235 */       return Integer.parseInt(p_parseInt_0_);
/*      */     
/*      */     }
/* 1238 */     catch (NumberFormatException var3) {
/*      */       
/* 1240 */       return p_parseInt_1_;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static float parseFloat(String p_parseFloat_0_, float p_parseFloat_1_) {
/*      */     try {
/* 1248 */       if (p_parseFloat_0_ == null)
/*      */       {
/* 1250 */         return p_parseFloat_1_;
/*      */       }
/*      */ 
/*      */       
/* 1254 */       p_parseFloat_0_ = p_parseFloat_0_.trim();
/* 1255 */       return Float.parseFloat(p_parseFloat_0_);
/*      */     
/*      */     }
/* 1258 */     catch (NumberFormatException var3) {
/*      */       
/* 1260 */       return p_parseFloat_1_;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean parseBoolean(String p_parseBoolean_0_, boolean p_parseBoolean_1_) {
/*      */     try {
/* 1268 */       if (p_parseBoolean_0_ == null)
/*      */       {
/* 1270 */         return p_parseBoolean_1_;
/*      */       }
/*      */ 
/*      */       
/* 1274 */       p_parseBoolean_0_ = p_parseBoolean_0_.trim();
/* 1275 */       return Boolean.parseBoolean(p_parseBoolean_0_);
/*      */     
/*      */     }
/* 1278 */     catch (NumberFormatException var3) {
/*      */       
/* 1280 */       return p_parseBoolean_1_;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Boolean parseBoolean(String p_parseBoolean_0_, Boolean p_parseBoolean_1_) {
/*      */     try {
/* 1288 */       if (p_parseBoolean_0_ == null)
/*      */       {
/* 1290 */         return p_parseBoolean_1_;
/*      */       }
/*      */ 
/*      */       
/* 1294 */       p_parseBoolean_0_ = p_parseBoolean_0_.trim().toLowerCase();
/* 1295 */       return p_parseBoolean_0_.equals("true") ? Boolean.TRUE : (p_parseBoolean_0_.equals("false") ? Boolean.FALSE : p_parseBoolean_1_);
/*      */     
/*      */     }
/* 1298 */     catch (NumberFormatException var3) {
/*      */       
/* 1300 */       return p_parseBoolean_1_;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static String[] tokenize(String p_tokenize_0_, String p_tokenize_1_) {
/* 1306 */     StringTokenizer stringtokenizer = new StringTokenizer(p_tokenize_0_, p_tokenize_1_);
/* 1307 */     List<String> list = new ArrayList();
/*      */     
/* 1309 */     while (stringtokenizer.hasMoreTokens()) {
/*      */       
/* 1311 */       String s = stringtokenizer.nextToken();
/* 1312 */       list.add(s);
/*      */     } 
/*      */     
/* 1315 */     String[] astring = list.<String>toArray(new String[list.size()]);
/* 1316 */     return astring;
/*      */   }
/*      */ 
/*      */   
/*      */   public static DisplayMode getDesktopDisplayMode() {
/* 1321 */     return desktopDisplayMode;
/*      */   }
/*      */ 
/*      */   
/*      */   public static DisplayMode[] getDisplayModes() {
/* 1326 */     if (displayModes == null) {
/*      */       
/*      */       try {
/*      */         
/* 1330 */         DisplayMode[] adisplaymode = Display.getAvailableDisplayModes();
/* 1331 */         Set<Dimension> set = getDisplayModeDimensions(adisplaymode);
/* 1332 */         List<DisplayMode> list = new ArrayList();
/*      */         
/* 1334 */         for (Dimension dimension : set) {
/*      */           
/* 1336 */           DisplayMode[] adisplaymode1 = getDisplayModes(adisplaymode, dimension);
/* 1337 */           DisplayMode displaymode = getDisplayMode(adisplaymode1, desktopDisplayMode);
/*      */           
/* 1339 */           if (displaymode != null)
/*      */           {
/* 1341 */             list.add(displaymode);
/*      */           }
/*      */         } 
/*      */         
/* 1345 */         DisplayMode[] adisplaymode2 = list.<DisplayMode>toArray(new DisplayMode[list.size()]);
/* 1346 */         Arrays.sort(adisplaymode2, (Comparator<? super DisplayMode>)new DisplayModeComparator());
/* 1347 */         return adisplaymode2;
/*      */       }
/* 1349 */       catch (Exception exception) {
/*      */         
/* 1351 */         exception.printStackTrace();
/* 1352 */         displayModes = new DisplayMode[] { desktopDisplayMode };
/*      */       } 
/*      */     }
/*      */     
/* 1356 */     return displayModes;
/*      */   }
/*      */ 
/*      */   
/*      */   public static DisplayMode getLargestDisplayMode() {
/* 1361 */     DisplayMode[] adisplaymode = getDisplayModes();
/*      */     
/* 1363 */     if (adisplaymode != null && adisplaymode.length >= 1) {
/*      */       
/* 1365 */       DisplayMode displaymode = adisplaymode[adisplaymode.length - 1];
/* 1366 */       return (desktopDisplayMode.getWidth() > displaymode.getWidth()) ? desktopDisplayMode : ((desktopDisplayMode.getWidth() == displaymode.getWidth() && desktopDisplayMode.getHeight() > displaymode.getHeight()) ? desktopDisplayMode : displaymode);
/*      */     } 
/*      */ 
/*      */     
/* 1370 */     return desktopDisplayMode;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Set<Dimension> getDisplayModeDimensions(DisplayMode[] p_getDisplayModeDimensions_0_) {
/* 1376 */     Set<Dimension> set = new HashSet<>();
/*      */     
/* 1378 */     for (int i = 0; i < p_getDisplayModeDimensions_0_.length; i++) {
/*      */       
/* 1380 */       DisplayMode displaymode = p_getDisplayModeDimensions_0_[i];
/* 1381 */       Dimension dimension = new Dimension(displaymode.getWidth(), displaymode.getHeight());
/* 1382 */       set.add(dimension);
/*      */     } 
/*      */     
/* 1385 */     return set;
/*      */   }
/*      */ 
/*      */   
/*      */   private static DisplayMode[] getDisplayModes(DisplayMode[] p_getDisplayModes_0_, Dimension p_getDisplayModes_1_) {
/* 1390 */     List<DisplayMode> list = new ArrayList();
/*      */     
/* 1392 */     for (int i = 0; i < p_getDisplayModes_0_.length; i++) {
/*      */       
/* 1394 */       DisplayMode displaymode = p_getDisplayModes_0_[i];
/*      */       
/* 1396 */       if (displaymode.getWidth() == p_getDisplayModes_1_.getWidth() && displaymode.getHeight() == p_getDisplayModes_1_.getHeight())
/*      */       {
/* 1398 */         list.add(displaymode);
/*      */       }
/*      */     } 
/*      */     
/* 1402 */     DisplayMode[] adisplaymode = list.<DisplayMode>toArray(new DisplayMode[list.size()]);
/* 1403 */     return adisplaymode;
/*      */   }
/*      */ 
/*      */   
/*      */   private static DisplayMode getDisplayMode(DisplayMode[] p_getDisplayMode_0_, DisplayMode p_getDisplayMode_1_) {
/* 1408 */     if (p_getDisplayMode_1_ != null)
/*      */     {
/* 1410 */       for (int i = 0; i < p_getDisplayMode_0_.length; i++) {
/*      */         
/* 1412 */         DisplayMode displaymode = p_getDisplayMode_0_[i];
/*      */         
/* 1414 */         if (displaymode.getBitsPerPixel() == p_getDisplayMode_1_.getBitsPerPixel() && displaymode.getFrequency() == p_getDisplayMode_1_.getFrequency())
/*      */         {
/* 1416 */           return displaymode;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1421 */     if (p_getDisplayMode_0_.length <= 0)
/*      */     {
/* 1423 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1427 */     Arrays.sort(p_getDisplayMode_0_, (Comparator<? super DisplayMode>)new DisplayModeComparator());
/* 1428 */     return p_getDisplayMode_0_[p_getDisplayMode_0_.length - 1];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getDisplayModeNames() {
/* 1434 */     DisplayMode[] adisplaymode = getDisplayModes();
/* 1435 */     String[] astring = new String[adisplaymode.length];
/*      */     
/* 1437 */     for (int i = 0; i < adisplaymode.length; i++) {
/*      */       
/* 1439 */       DisplayMode displaymode = adisplaymode[i];
/* 1440 */       String s = "" + displaymode.getWidth() + "x" + displaymode.getHeight();
/* 1441 */       astring[i] = s;
/*      */     } 
/*      */     
/* 1444 */     return astring;
/*      */   }
/*      */ 
/*      */   
/*      */   public static DisplayMode getDisplayMode(Dimension p_getDisplayMode_0_) throws LWJGLException {
/* 1449 */     DisplayMode[] adisplaymode = getDisplayModes();
/*      */     
/* 1451 */     for (int i = 0; i < adisplaymode.length; i++) {
/*      */       
/* 1453 */       DisplayMode displaymode = adisplaymode[i];
/*      */       
/* 1455 */       if (displaymode.getWidth() == p_getDisplayMode_0_.width && displaymode.getHeight() == p_getDisplayMode_0_.height)
/*      */       {
/* 1457 */         return displaymode;
/*      */       }
/*      */     } 
/*      */     
/* 1461 */     return desktopDisplayMode;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedTerrain() {
/* 1466 */     return gameSettings.ofAnimatedTerrain;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAnimatedTextures() {
/* 1471 */     return gameSettings.ofAnimatedTextures;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSwampColors() {
/* 1476 */     return gameSettings.ofSwampColors;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRandomEntities() {
/* 1481 */     return gameSettings.ofRandomEntities;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkGlError(String p_checkGlError_0_) {
/* 1486 */     int i = GlStateManager.glGetError();
/*      */     
/* 1488 */     if (i != 0 && GlErrors.isEnabled(i)) {
/*      */       
/* 1490 */       String s = getGlErrorString(i);
/* 1491 */       String s1 = String.format("OpenGL error: %s (%s), at: %s", new Object[] { Integer.valueOf(i), s, p_checkGlError_0_ });
/* 1492 */       error(s1);
/*      */       
/* 1494 */       if (isShowGlErrors() && TimedEvent.isActive("ShowGlError", 10000L)) {
/*      */         
/* 1496 */         String s2 = I18n.format("of.message.openglError", new Object[] { Integer.valueOf(i), s });
/* 1497 */         minecraft.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentText(s2));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSmoothBiomes() {
/* 1504 */     return gameSettings.ofSmoothBiomes;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCustomColors() {
/* 1509 */     return gameSettings.ofCustomColors;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCustomSky() {
/* 1514 */     return gameSettings.ofCustomSky;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCustomFonts() {
/* 1519 */     return gameSettings.ofCustomFonts;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isShowCapes() {
/* 1524 */     return gameSettings.ofShowCapes;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isConnectedTextures() {
/* 1529 */     return (gameSettings.ofConnectedTextures != 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNaturalTextures() {
/* 1534 */     return gameSettings.ofNaturalTextures;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isEmissiveTextures() {
/* 1539 */     return gameSettings.ofEmissiveTextures;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isConnectedTexturesFancy() {
/* 1544 */     return (gameSettings.ofConnectedTextures == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFastRender() {
/* 1549 */     return gameSettings.ofFastRender;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isTranslucentBlocksFancy() {
/* 1554 */     return (gameSettings.ofTranslucentBlocks == 0) ? gameSettings.fancyGraphics : ((gameSettings.ofTranslucentBlocks == 2));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isShaders() {
/* 1559 */     return Shaders.shaderPackLoaded;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String[] readLines(File p_readLines_0_) throws IOException {
/* 1564 */     FileInputStream fileinputstream = new FileInputStream(p_readLines_0_);
/* 1565 */     return readLines(fileinputstream);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String[] readLines(InputStream p_readLines_0_) throws IOException {
/* 1570 */     List<String> list = new ArrayList();
/* 1571 */     InputStreamReader inputstreamreader = new InputStreamReader(p_readLines_0_, "ASCII");
/* 1572 */     BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
/*      */ 
/*      */     
/*      */     while (true) {
/* 1576 */       String s = bufferedreader.readLine();
/*      */       
/* 1578 */       if (s == null) {
/*      */         
/* 1580 */         String[] astring = (String[])list.toArray((Object[])new String[list.size()]);
/* 1581 */         return astring;
/*      */       } 
/*      */       
/* 1584 */       list.add(s);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static String readFile(File p_readFile_0_) throws IOException {
/* 1590 */     FileInputStream fileinputstream = new FileInputStream(p_readFile_0_);
/* 1591 */     return readInputStream(fileinputstream, "ASCII");
/*      */   }
/*      */ 
/*      */   
/*      */   public static String readInputStream(InputStream p_readInputStream_0_) throws IOException {
/* 1596 */     return readInputStream(p_readInputStream_0_, "ASCII");
/*      */   }
/*      */ 
/*      */   
/*      */   public static String readInputStream(InputStream p_readInputStream_0_, String p_readInputStream_1_) throws IOException {
/* 1601 */     InputStreamReader inputstreamreader = new InputStreamReader(p_readInputStream_0_, p_readInputStream_1_);
/* 1602 */     BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
/* 1603 */     StringBuffer stringbuffer = new StringBuffer();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1607 */       String s = bufferedreader.readLine();
/*      */       
/* 1609 */       if (s == null)
/*      */       {
/* 1611 */         return stringbuffer.toString();
/*      */       }
/*      */       
/* 1614 */       stringbuffer.append(s);
/* 1615 */       stringbuffer.append("\n");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] readAll(InputStream p_readAll_0_) throws IOException {
/* 1621 */     ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
/* 1622 */     byte[] abyte = new byte[1024];
/*      */ 
/*      */     
/*      */     while (true) {
/* 1626 */       int i = p_readAll_0_.read(abyte);
/*      */       
/* 1628 */       if (i < 0) {
/*      */         
/* 1630 */         p_readAll_0_.close();
/* 1631 */         byte[] abyte1 = bytearrayoutputstream.toByteArray();
/* 1632 */         return abyte1;
/*      */       } 
/*      */       
/* 1635 */       bytearrayoutputstream.write(abyte, 0, i);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static GameSettings getGameSettings() {
/* 1641 */     return gameSettings;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getNewRelease() {
/* 1646 */     return newRelease;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setNewRelease(String p_setNewRelease_0_) {
/* 1651 */     newRelease = p_setNewRelease_0_;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int compareRelease(String p_compareRelease_0_, String p_compareRelease_1_) {
/* 1656 */     String[] astring = splitRelease(p_compareRelease_0_);
/* 1657 */     String[] astring1 = splitRelease(p_compareRelease_1_);
/* 1658 */     String s = astring[0];
/* 1659 */     String s1 = astring1[0];
/*      */     
/* 1661 */     if (!s.equals(s1))
/*      */     {
/* 1663 */       return s.compareTo(s1);
/*      */     }
/*      */ 
/*      */     
/* 1667 */     int i = parseInt(astring[1], -1);
/* 1668 */     int j = parseInt(astring1[1], -1);
/*      */     
/* 1670 */     if (i != j)
/*      */     {
/* 1672 */       return i - j;
/*      */     }
/*      */ 
/*      */     
/* 1676 */     String s2 = astring[2];
/* 1677 */     String s3 = astring1[2];
/*      */     
/* 1679 */     if (!s2.equals(s3)) {
/*      */       
/* 1681 */       if (s2.isEmpty())
/*      */       {
/* 1683 */         return 1;
/*      */       }
/*      */       
/* 1686 */       if (s3.isEmpty())
/*      */       {
/* 1688 */         return -1;
/*      */       }
/*      */     } 
/*      */     
/* 1692 */     return s2.compareTo(s3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String[] splitRelease(String p_splitRelease_0_) {
/* 1699 */     if (p_splitRelease_0_ != null && p_splitRelease_0_.length() > 0) {
/*      */       
/* 1701 */       Pattern pattern = Pattern.compile("([A-Z])([0-9]+)(.*)");
/* 1702 */       Matcher matcher = pattern.matcher(p_splitRelease_0_);
/*      */       
/* 1704 */       if (!matcher.matches())
/*      */       {
/* 1706 */         return new String[] { "", "", "" };
/*      */       }
/*      */ 
/*      */       
/* 1710 */       String s = normalize(matcher.group(1));
/* 1711 */       String s1 = normalize(matcher.group(2));
/* 1712 */       String s2 = normalize(matcher.group(3));
/* 1713 */       return new String[] { s, s1, s2 };
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1718 */     return new String[] { "", "", "" };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int intHash(int p_intHash_0_) {
/* 1724 */     p_intHash_0_ = p_intHash_0_ ^ 0x3D ^ p_intHash_0_ >> 16;
/* 1725 */     p_intHash_0_ += p_intHash_0_ << 3;
/* 1726 */     p_intHash_0_ ^= p_intHash_0_ >> 4;
/* 1727 */     p_intHash_0_ *= 668265261;
/* 1728 */     p_intHash_0_ ^= p_intHash_0_ >> 15;
/* 1729 */     return p_intHash_0_;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getRandom(BlockPos p_getRandom_0_, int p_getRandom_1_) {
/* 1734 */     int i = intHash(p_getRandom_1_ + 37);
/* 1735 */     i = intHash(i + p_getRandom_0_.getX());
/* 1736 */     i = intHash(i + p_getRandom_0_.getZ());
/* 1737 */     i = intHash(i + p_getRandom_0_.getY());
/* 1738 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getAvailableProcessors() {
/* 1743 */     return availableProcessors;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void updateAvailableProcessors() {
/* 1748 */     availableProcessors = Runtime.getRuntime().availableProcessors();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSingleProcessor() {
/* 1753 */     return (getAvailableProcessors() <= 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSmoothWorld() {
/* 1758 */     return gameSettings.ofSmoothWorld;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isLazyChunkLoading() {
/* 1763 */     return gameSettings.ofLazyChunkLoading;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDynamicFov() {
/* 1768 */     return gameSettings.ofDynamicFov;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isAlternateBlocks() {
/* 1773 */     return gameSettings.ofAlternateBlocks;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getChunkViewDistance() {
/* 1778 */     if (gameSettings == null)
/*      */     {
/* 1780 */       return 10;
/*      */     }
/*      */ 
/*      */     
/* 1784 */     int i = gameSettings.renderDistanceChunks;
/* 1785 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equals(Object p_equals_0_, Object p_equals_1_) {
/* 1791 */     return (p_equals_0_ == p_equals_1_) ? true : ((p_equals_0_ == null) ? false : p_equals_0_.equals(p_equals_1_));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean equalsOne(Object p_equalsOne_0_, Object[] p_equalsOne_1_) {
/* 1796 */     if (p_equalsOne_1_ == null)
/*      */     {
/* 1798 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1802 */     for (int i = 0; i < p_equalsOne_1_.length; i++) {
/*      */       
/* 1804 */       Object object = p_equalsOne_1_[i];
/*      */       
/* 1806 */       if (equals(p_equalsOne_0_, object))
/*      */       {
/* 1808 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1812 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equalsOne(int p_equalsOne_0_, int[] p_equalsOne_1_) {
/* 1818 */     for (int i = 0; i < p_equalsOne_1_.length; i++) {
/*      */       
/* 1820 */       if (p_equalsOne_1_[i] == p_equalsOne_0_)
/*      */       {
/* 1822 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1826 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSameOne(Object p_isSameOne_0_, Object[] p_isSameOne_1_) {
/* 1831 */     if (p_isSameOne_1_ == null)
/*      */     {
/* 1833 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1837 */     for (int i = 0; i < p_isSameOne_1_.length; i++) {
/*      */       
/* 1839 */       Object object = p_isSameOne_1_[i];
/*      */       
/* 1841 */       if (p_isSameOne_0_ == object)
/*      */       {
/* 1843 */         return true;
/*      */       }
/*      */     } 
/*      */     
/* 1847 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String normalize(String p_normalize_0_) {
/* 1853 */     return (p_normalize_0_ == null) ? "" : p_normalize_0_;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkDisplaySettings() {
/* 1858 */     int i = getAntialiasingLevel();
/*      */     
/* 1860 */     if (i > 0) {
/*      */       
/* 1862 */       DisplayMode displaymode = Display.getDisplayMode();
/* 1863 */       dbg("FSAA Samples: " + i);
/*      */ 
/*      */       
/*      */       try {
/* 1867 */         Display.destroy();
/* 1868 */         Display.setDisplayMode(displaymode);
/* 1869 */         Display.create((new PixelFormat()).withDepthBits(24).withSamples(i));
/*      */         
/* 1871 */         if (Util.getOSType() == Util.EnumOS.WINDOWS)
/*      */         {
/* 1873 */           Display.setResizable(false);
/* 1874 */           Display.setResizable(true);
/*      */         }
/*      */       
/* 1877 */       } catch (LWJGLException lwjglexception2) {
/*      */         
/* 1879 */         warn("Error setting FSAA: " + i + "x");
/* 1880 */         lwjglexception2.printStackTrace();
/*      */ 
/*      */         
/*      */         try {
/* 1884 */           Display.setDisplayMode(displaymode);
/* 1885 */           Display.create((new PixelFormat()).withDepthBits(24));
/*      */           
/* 1887 */           if (Util.getOSType() == Util.EnumOS.WINDOWS)
/*      */           {
/* 1889 */             Display.setResizable(false);
/* 1890 */             Display.setResizable(true);
/*      */           }
/*      */         
/* 1893 */         } catch (LWJGLException lwjglexception1) {
/*      */           
/* 1895 */           lwjglexception1.printStackTrace();
/*      */ 
/*      */           
/*      */           try {
/* 1899 */             Display.setDisplayMode(displaymode);
/* 1900 */             Display.create();
/*      */             
/* 1902 */             if (Util.getOSType() == Util.EnumOS.WINDOWS)
/*      */             {
/* 1904 */               Display.setResizable(false);
/* 1905 */               Display.setResizable(true);
/*      */             }
/*      */           
/* 1908 */           } catch (LWJGLException lwjglexception) {
/*      */             
/* 1910 */             lwjglexception.printStackTrace();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1915 */       if (!Minecraft.isRunningOnMac && getDefaultResourcePack() != null) {
/*      */         
/* 1917 */         InputStream inputstream = null;
/* 1918 */         InputStream inputstream1 = null;
/*      */ 
/*      */         
/*      */         try {
/* 1922 */           inputstream = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_16x16.png"));
/* 1923 */           inputstream1 = getDefaultResourcePack().getInputStreamAssets(new ResourceLocation("icons/icon_32x32.png"));
/*      */           
/* 1925 */           if (inputstream != null && inputstream1 != null)
/*      */           {
/* 1927 */             Display.setIcon(new ByteBuffer[] { readIconImage(inputstream), readIconImage(inputstream1) });
/*      */           }
/*      */         }
/* 1930 */         catch (IOException ioexception) {
/*      */           
/* 1932 */           warn("Error setting window icon: " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
/*      */         }
/*      */         finally {
/*      */           
/* 1936 */           IOUtils.closeQuietly(inputstream);
/* 1937 */           IOUtils.closeQuietly(inputstream1);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static ByteBuffer readIconImage(InputStream p_readIconImage_0_) throws IOException {
/* 1945 */     BufferedImage bufferedimage = ImageIO.read(p_readIconImage_0_);
/* 1946 */     int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[])null, 0, bufferedimage.getWidth());
/* 1947 */     ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
/*      */     
/* 1949 */     for (int i : aint)
/*      */     {
/* 1951 */       bytebuffer.putInt(i << 8 | i >> 24 & 0xFF);
/*      */     }
/*      */     
/* 1954 */     bytebuffer.flip();
/* 1955 */     return bytebuffer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkDisplayMode() {
/*      */     try {
/* 1962 */       if (minecraft.isFullScreen()) {
/*      */         
/* 1964 */         if (fullscreenModeChecked) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 1969 */         fullscreenModeChecked = true;
/* 1970 */         desktopModeChecked = false;
/* 1971 */         DisplayMode displaymode = Display.getDisplayMode();
/* 1972 */         Dimension dimension = getFullscreenDimension();
/*      */         
/* 1974 */         if (dimension == null) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 1979 */         if (displaymode.getWidth() == dimension.width && displaymode.getHeight() == dimension.height) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 1984 */         DisplayMode displaymode1 = getDisplayMode(dimension);
/*      */         
/* 1986 */         if (displaymode1 == null) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 1991 */         Display.setDisplayMode(displaymode1);
/* 1992 */         minecraft.displayWidth = Display.getDisplayMode().getWidth();
/* 1993 */         minecraft.displayHeight = Display.getDisplayMode().getHeight();
/*      */         
/* 1995 */         if (minecraft.displayWidth <= 0)
/*      */         {
/* 1997 */           minecraft.displayWidth = 1;
/*      */         }
/*      */         
/* 2000 */         if (minecraft.displayHeight <= 0)
/*      */         {
/* 2002 */           minecraft.displayHeight = 1;
/*      */         }
/*      */         
/* 2005 */         if (minecraft.currentScreen != null) {
/*      */           
/* 2007 */           ScaledResolution scaledresolution = new ScaledResolution(minecraft);
/* 2008 */           int i = scaledresolution.getScaledWidth();
/* 2009 */           int j = scaledresolution.getScaledHeight();
/* 2010 */           minecraft.currentScreen.setWorldAndResolution(minecraft, i, j);
/*      */         } 
/*      */         
/* 2013 */         updateFramebufferSize();
/* 2014 */         Display.setFullscreen(true);
/* 2015 */         minecraft.gameSettings.updateVSync();
/* 2016 */         GlStateManager.enableTexture2D();
/*      */       }
/*      */       else {
/*      */         
/* 2020 */         if (desktopModeChecked) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/* 2025 */         desktopModeChecked = true;
/* 2026 */         fullscreenModeChecked = false;
/* 2027 */         minecraft.gameSettings.updateVSync();
/* 2028 */         Display.update();
/* 2029 */         GlStateManager.enableTexture2D();
/*      */         
/* 2031 */         if (Util.getOSType() == Util.EnumOS.WINDOWS)
/*      */         {
/* 2033 */           Display.setResizable(false);
/* 2034 */           Display.setResizable(true);
/*      */         }
/*      */       
/*      */       } 
/* 2038 */     } catch (Exception exception) {
/*      */       
/* 2040 */       exception.printStackTrace();
/* 2041 */       gameSettings.ofFullscreenMode = "Default";
/* 2042 */       gameSettings.saveOfOptions();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void updateFramebufferSize() {
/* 2048 */     minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
/*      */     
/* 2050 */     if (minecraft.entityRenderer != null)
/*      */     {
/* 2052 */       minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
/*      */     }
/*      */     
/* 2055 */     minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object[] addObjectToArray(Object[] p_addObjectToArray_0_, Object p_addObjectToArray_1_) {
/* 2060 */     if (p_addObjectToArray_0_ == null)
/*      */     {
/* 2062 */       throw new NullPointerException("The given array is NULL");
/*      */     }
/*      */ 
/*      */     
/* 2066 */     int i = p_addObjectToArray_0_.length;
/* 2067 */     int j = i + 1;
/* 2068 */     Object[] aobject = (Object[])Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), j);
/* 2069 */     System.arraycopy(p_addObjectToArray_0_, 0, aobject, 0, i);
/* 2070 */     aobject[i] = p_addObjectToArray_1_;
/* 2071 */     return aobject;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[] addObjectToArray(Object[] p_addObjectToArray_0_, Object p_addObjectToArray_1_, int p_addObjectToArray_2_) {
/* 2077 */     List<Object> list = new ArrayList(Arrays.asList(p_addObjectToArray_0_));
/* 2078 */     list.add(p_addObjectToArray_2_, p_addObjectToArray_1_);
/* 2079 */     Object[] aobject = (Object[])Array.newInstance(p_addObjectToArray_0_.getClass().getComponentType(), list.size());
/* 2080 */     return list.toArray(aobject);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object[] addObjectsToArray(Object[] p_addObjectsToArray_0_, Object[] p_addObjectsToArray_1_) {
/* 2085 */     if (p_addObjectsToArray_0_ == null)
/*      */     {
/* 2087 */       throw new NullPointerException("The given array is NULL");
/*      */     }
/* 2089 */     if (p_addObjectsToArray_1_.length == 0)
/*      */     {
/* 2091 */       return p_addObjectsToArray_0_;
/*      */     }
/*      */ 
/*      */     
/* 2095 */     int i = p_addObjectsToArray_0_.length;
/* 2096 */     int j = i + p_addObjectsToArray_1_.length;
/* 2097 */     Object[] aobject = (Object[])Array.newInstance(p_addObjectsToArray_0_.getClass().getComponentType(), j);
/* 2098 */     System.arraycopy(p_addObjectsToArray_0_, 0, aobject, 0, i);
/* 2099 */     System.arraycopy(p_addObjectsToArray_1_, 0, aobject, i, p_addObjectsToArray_1_.length);
/* 2100 */     return aobject;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[] removeObjectFromArray(Object[] p_removeObjectFromArray_0_, Object p_removeObjectFromArray_1_) {
/* 2106 */     List list = new ArrayList(Arrays.asList(p_removeObjectFromArray_0_));
/* 2107 */     list.remove(p_removeObjectFromArray_1_);
/* 2108 */     Object[] aobject = collectionToArray(list, p_removeObjectFromArray_0_.getClass().getComponentType());
/* 2109 */     return aobject;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object[] collectionToArray(Collection p_collectionToArray_0_, Class<?> p_collectionToArray_1_) {
/* 2114 */     if (p_collectionToArray_0_ == null)
/*      */     {
/* 2116 */       return null;
/*      */     }
/* 2118 */     if (p_collectionToArray_1_ == null)
/*      */     {
/* 2120 */       return null;
/*      */     }
/* 2122 */     if (p_collectionToArray_1_.isPrimitive())
/*      */     {
/* 2124 */       throw new IllegalArgumentException("Can not make arrays with primitive elements (int, double), element class: " + p_collectionToArray_1_);
/*      */     }
/*      */ 
/*      */     
/* 2128 */     Object[] aobject = (Object[])Array.newInstance(p_collectionToArray_1_, p_collectionToArray_0_.size());
/* 2129 */     return p_collectionToArray_0_.toArray(aobject);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCustomItems() {
/* 2135 */     return gameSettings.ofCustomItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawFps() {
/* 2140 */     int i = Minecraft.getDebugFPS();
/* 2141 */     String s = getUpdates(minecraft.debug);
/* 2142 */     int j = minecraft.renderGlobal.getCountActiveRenderers();
/* 2143 */     int k = minecraft.renderGlobal.getCountEntitiesRendered();
/* 2144 */     int l = minecraft.renderGlobal.getCountTileEntitiesRendered();
/* 2145 */     String s1 = "" + i + "/" + getFpsMin() + " fps, C: " + j + ", E: " + k + "+" + l + ", U: " + s;
/* 2146 */     minecraft.fontRendererObj.drawString(s1, 2.0D, 2.0D, -2039584);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getFpsMin() {
/* 2151 */     if (minecraft.debug == mcDebugLast)
/*      */     {
/* 2153 */       return fpsMinLast;
/*      */     }
/*      */ 
/*      */     
/* 2157 */     mcDebugLast = minecraft.debug;
/* 2158 */     FrameTimer frametimer = minecraft.getFrameTimer();
/* 2159 */     long[] along = frametimer.getFrames();
/* 2160 */     int i = frametimer.getIndex();
/* 2161 */     int j = frametimer.getLastIndex();
/*      */     
/* 2163 */     if (i == j)
/*      */     {
/* 2165 */       return fpsMinLast;
/*      */     }
/*      */ 
/*      */     
/* 2169 */     int k = Minecraft.getDebugFPS();
/*      */     
/* 2171 */     if (k <= 0)
/*      */     {
/* 2173 */       k = 1;
/*      */     }
/*      */     
/* 2176 */     long l = (long)(1.0D / k * 1.0E9D);
/* 2177 */     long i1 = l;
/* 2178 */     long j1 = 0L;
/*      */     int k1;
/* 2180 */     for (k1 = MathHelper.normalizeAngle(i - 1, along.length); k1 != j && j1 < 1.0E9D; k1 = MathHelper.normalizeAngle(k1 - 1, along.length)) {
/*      */       
/* 2182 */       long l1 = along[k1];
/*      */       
/* 2184 */       if (l1 > i1)
/*      */       {
/* 2186 */         i1 = l1;
/*      */       }
/*      */       
/* 2189 */       j1 += l1;
/*      */     } 
/*      */     
/* 2192 */     double d0 = i1 / 1.0E9D;
/* 2193 */     fpsMinLast = (int)(1.0D / d0);
/* 2194 */     return fpsMinLast;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getUpdates(String p_getUpdates_0_) {
/* 2201 */     int i = p_getUpdates_0_.indexOf('(');
/*      */     
/* 2203 */     if (i < 0)
/*      */     {
/* 2205 */       return "";
/*      */     }
/*      */ 
/*      */     
/* 2209 */     int j = p_getUpdates_0_.indexOf(' ', i);
/* 2210 */     return (j < 0) ? "" : p_getUpdates_0_.substring(i + 1, j);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getBitsOs() {
/* 2216 */     String s = System.getenv("ProgramFiles(X86)");
/* 2217 */     return (s != null) ? 64 : 32;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBitsJre() {
/* 2222 */     String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
/*      */     
/* 2224 */     for (int i = 0; i < astring.length; i++) {
/*      */       
/* 2226 */       String s = astring[i];
/* 2227 */       String s1 = System.getProperty(s);
/*      */       
/* 2229 */       if (s1 != null && s1.contains("64"))
/*      */       {
/* 2231 */         return 64;
/*      */       }
/*      */     } 
/*      */     
/* 2235 */     return 32;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNotify64BitJava() {
/* 2240 */     return notify64BitJava;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setNotify64BitJava(boolean p_setNotify64BitJava_0_) {
/* 2245 */     notify64BitJava = p_setNotify64BitJava_0_;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isConnectedModels() {
/* 2250 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void showGuiMessage(String p_showGuiMessage_0_, String p_showGuiMessage_1_) {
/* 2255 */     GuiMessage guimessage = new GuiMessage(minecraft.currentScreen, p_showGuiMessage_0_, p_showGuiMessage_1_);
/* 2256 */     minecraft.displayGuiScreen((GuiScreen)guimessage);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] addIntToArray(int[] p_addIntToArray_0_, int p_addIntToArray_1_) {
/* 2261 */     return addIntsToArray(p_addIntToArray_0_, new int[] { p_addIntToArray_1_ });
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] addIntsToArray(int[] p_addIntsToArray_0_, int[] p_addIntsToArray_1_) {
/* 2266 */     if (p_addIntsToArray_0_ != null && p_addIntsToArray_1_ != null) {
/*      */       
/* 2268 */       int i = p_addIntsToArray_0_.length;
/* 2269 */       int j = i + p_addIntsToArray_1_.length;
/* 2270 */       int[] aint = new int[j];
/* 2271 */       System.arraycopy(p_addIntsToArray_0_, 0, aint, 0, i);
/*      */       
/* 2273 */       for (int k = 0; k < p_addIntsToArray_1_.length; k++)
/*      */       {
/* 2275 */         aint[k + i] = p_addIntsToArray_1_[k];
/*      */       }
/*      */       
/* 2278 */       return aint;
/*      */     } 
/*      */ 
/*      */     
/* 2282 */     throw new NullPointerException("The given array is NULL");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DynamicTexture getMojangLogoTexture(DynamicTexture p_getMojangLogoTexture_0_) {
/*      */     try {
/* 2290 */       ResourceLocation resourcelocation = new ResourceLocation("textures/gui/title/mojang.png");
/* 2291 */       InputStream inputstream = getResourceStream(resourcelocation);
/*      */       
/* 2293 */       if (inputstream == null)
/*      */       {
/* 2295 */         return p_getMojangLogoTexture_0_;
/*      */       }
/*      */ 
/*      */       
/* 2299 */       BufferedImage bufferedimage = ImageIO.read(inputstream);
/*      */       
/* 2301 */       if (bufferedimage == null)
/*      */       {
/* 2303 */         return p_getMojangLogoTexture_0_;
/*      */       }
/*      */ 
/*      */       
/* 2307 */       DynamicTexture dynamictexture = new DynamicTexture(bufferedimage);
/* 2308 */       return dynamictexture;
/*      */ 
/*      */     
/*      */     }
/* 2312 */     catch (Exception exception) {
/*      */       
/* 2314 */       warn(exception.getClass().getName() + ": " + exception.getMessage());
/* 2315 */       return p_getMojangLogoTexture_0_;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void writeFile(File p_writeFile_0_, String p_writeFile_1_) throws IOException {
/* 2321 */     FileOutputStream fileoutputstream = new FileOutputStream(p_writeFile_0_);
/* 2322 */     byte[] abyte = p_writeFile_1_.getBytes("ASCII");
/* 2323 */     fileoutputstream.write(abyte);
/* 2324 */     fileoutputstream.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public static TextureMap getTextureMap() {
/* 2329 */     return getMinecraft().getTextureMapBlocks();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDynamicLights() {
/* 2334 */     return (gameSettings.ofDynamicLights != 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDynamicLightsFast() {
/* 2339 */     return (gameSettings.ofDynamicLights == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDynamicHandLight() {
/* 2344 */     return !isDynamicLights() ? false : (isShaders() ? Shaders.isDynamicHandLight() : true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCustomEntityModels() {
/* 2349 */     return gameSettings.ofCustomEntityModels;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCustomGuis() {
/* 2354 */     return gameSettings.ofCustomGuis;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getScreenshotSize() {
/* 2359 */     return gameSettings.ofScreenshotSize;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] toPrimitive(Integer[] p_toPrimitive_0_) {
/* 2364 */     if (p_toPrimitive_0_ == null)
/*      */     {
/* 2366 */       return null;
/*      */     }
/* 2368 */     if (p_toPrimitive_0_.length == 0)
/*      */     {
/* 2370 */       return new int[0];
/*      */     }
/*      */ 
/*      */     
/* 2374 */     int[] aint = new int[p_toPrimitive_0_.length];
/*      */     
/* 2376 */     for (int i = 0; i < aint.length; i++)
/*      */     {
/* 2378 */       aint[i] = p_toPrimitive_0_[i].intValue();
/*      */     }
/*      */     
/* 2381 */     return aint;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isRenderRegions() {
/* 2387 */     return gameSettings.ofRenderRegions;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isVbo() {
/* 2392 */     return OpenGlHelper.useVbo();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSmoothFps() {
/* 2397 */     return gameSettings.ofSmoothFps;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean openWebLink(URI p_openWebLink_0_) {
/*      */     try {
/* 2404 */       Desktop.getDesktop().browse(p_openWebLink_0_);
/* 2405 */       return true;
/*      */     }
/* 2407 */     catch (Exception exception) {
/*      */       
/* 2409 */       warn("Error opening link: " + p_openWebLink_0_);
/* 2410 */       warn(exception.getClass().getName() + ": " + exception.getMessage());
/* 2411 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isShowGlErrors() {
/* 2417 */     return gameSettings.ofShowGlErrors;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String arrayToString(boolean[] p_arrayToString_0_, String p_arrayToString_1_) {
/* 2422 */     if (p_arrayToString_0_ == null)
/*      */     {
/* 2424 */       return "";
/*      */     }
/*      */ 
/*      */     
/* 2428 */     StringBuffer stringbuffer = new StringBuffer(p_arrayToString_0_.length * 5);
/*      */     
/* 2430 */     for (int i = 0; i < p_arrayToString_0_.length; i++) {
/*      */       
/* 2432 */       boolean flag = p_arrayToString_0_[i];
/*      */       
/* 2434 */       if (i > 0)
/*      */       {
/* 2436 */         stringbuffer.append(p_arrayToString_1_);
/*      */       }
/*      */       
/* 2439 */       stringbuffer.append(String.valueOf(flag));
/*      */     } 
/*      */     
/* 2442 */     return stringbuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isIntegratedServerRunning() {
/* 2448 */     return (minecraft.getIntegratedServer() == null) ? false : minecraft.isIntegratedServerRunning();
/*      */   }
/*      */ 
/*      */   
/*      */   public static IntBuffer createDirectIntBuffer(int p_createDirectIntBuffer_0_) {
/* 2453 */     return GLAllocation.createDirectByteBuffer(p_createDirectIntBuffer_0_ << 2).asIntBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getGlErrorString(int p_getGlErrorString_0_) {
/* 2458 */     switch (p_getGlErrorString_0_) {
/*      */       
/*      */       case 0:
/* 2461 */         return "No error";
/*      */       
/*      */       case 1280:
/* 2464 */         return "Invalid enum";
/*      */       
/*      */       case 1281:
/* 2467 */         return "Invalid value";
/*      */       
/*      */       case 1282:
/* 2470 */         return "Invalid operation";
/*      */       
/*      */       case 1283:
/* 2473 */         return "Stack overflow";
/*      */       
/*      */       case 1284:
/* 2476 */         return "Stack underflow";
/*      */       
/*      */       case 1285:
/* 2479 */         return "Out of memory";
/*      */       
/*      */       case 1286:
/* 2482 */         return "Invalid framebuffer operation";
/*      */     } 
/*      */     
/* 2485 */     return "Unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isTrue(Boolean p_isTrue_0_) {
/* 2491 */     return (p_isTrue_0_ != null && p_isTrue_0_.booleanValue());
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isQuadsToTriangles() {
/* 2496 */     return !isShaders() ? false : (!Shaders.canRenderQuads());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkNull(Object p_checkNull_0_, String p_checkNull_1_) throws NullPointerException {
/* 2501 */     if (p_checkNull_0_ == null)
/*      */     {
/* 2503 */       throw new NullPointerException(p_checkNull_1_);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\src\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */