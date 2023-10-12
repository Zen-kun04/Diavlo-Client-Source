/*      */ package net.optifine.shaders;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.renderer.GlStateManager;
/*      */ import net.minecraft.client.renderer.WorldRenderer;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.World;
/*      */ import net.optifine.config.ConnectedParser;
/*      */ import net.optifine.shaders.config.EnumShaderOption;
/*      */ import net.optifine.shaders.config.MacroState;
/*      */ import net.optifine.shaders.config.PropertyDefaultTrueFalse;
/*      */ import net.optifine.shaders.config.RenderScale;
/*      */ import net.optifine.shaders.config.ScreenShaderOptions;
/*      */ import net.optifine.shaders.config.ShaderLine;
/*      */ import net.optifine.shaders.config.ShaderOption;
/*      */ import net.optifine.shaders.config.ShaderPackParser;
/*      */ import net.optifine.shaders.config.ShaderParser;
/*      */ import net.optifine.shaders.uniform.ShaderUniform1f;
/*      */ import net.optifine.shaders.uniform.ShaderUniform1i;
/*      */ import net.optifine.shaders.uniform.ShaderUniform2i;
/*      */ import net.optifine.shaders.uniform.ShaderUniform3f;
/*      */ import net.optifine.shaders.uniform.ShaderUniformM4;
/*      */ import net.optifine.texture.InternalFormat;
/*      */ import net.optifine.texture.PixelFormat;
/*      */ import net.optifine.texture.PixelType;
/*      */ import net.optifine.texture.TextureType;
/*      */ import net.optifine.util.PropertiesOrdered;
/*      */ import net.optifine.util.StrUtils;
/*      */ import org.lwjgl.opengl.ARBShaderObjects;
/*      */ import org.lwjgl.opengl.EXTFramebufferObject;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GL20;
/*      */ import org.lwjgl.util.vector.Vector4f;
/*      */ 
/*      */ public class Shaders {
/*   59 */   static Minecraft mc = Minecraft.getMinecraft();
/*      */   static EntityRenderer entityRenderer;
/*      */   public static boolean isInitializedOnce = false;
/*      */   public static boolean isShaderPackInitialized = false;
/*      */   public static ContextCapabilities capabilities;
/*      */   public static String glVersionString;
/*      */   public static String glVendorString;
/*      */   public static String glRendererString;
/*      */   public static boolean hasGlGenMipmap = false;
/*   68 */   public static int countResetDisplayLists = 0;
/*   69 */   private static int renderDisplayWidth = 0;
/*   70 */   private static int renderDisplayHeight = 0;
/*   71 */   public static int renderWidth = 0;
/*   72 */   public static int renderHeight = 0;
/*      */   public static boolean isRenderingWorld = false;
/*      */   public static boolean isRenderingSky = false;
/*      */   public static boolean isCompositeRendered = false;
/*      */   public static boolean isRenderingDfb = false;
/*      */   public static boolean isShadowPass = false;
/*      */   public static boolean isEntitiesGlowing = false;
/*      */   public static boolean isSleeping;
/*      */   private static boolean isRenderingFirstPersonHand;
/*      */   private static boolean isHandRenderedMain;
/*      */   private static boolean isHandRenderedOff;
/*      */   private static boolean skipRenderHandMain;
/*      */   private static boolean skipRenderHandOff;
/*      */   public static boolean renderItemKeepDepthMask = false;
/*      */   public static boolean itemToRenderMainTranslucent = false;
/*      */   public static boolean itemToRenderOffTranslucent = false;
/*   88 */   static float[] sunPosition = new float[4];
/*   89 */   static float[] moonPosition = new float[4];
/*   90 */   static float[] shadowLightPosition = new float[4];
/*   91 */   static float[] upPosition = new float[4];
/*   92 */   static float[] shadowLightPositionVector = new float[4];
/*   93 */   static float[] upPosModelView = new float[] { 0.0F, 100.0F, 0.0F, 0.0F };
/*   94 */   static float[] sunPosModelView = new float[] { 0.0F, 100.0F, 0.0F, 0.0F };
/*   95 */   static float[] moonPosModelView = new float[] { 0.0F, -100.0F, 0.0F, 0.0F };
/*   96 */   private static float[] tempMat = new float[16];
/*      */   static float clearColorR;
/*      */   static float clearColorG;
/*      */   static float clearColorB;
/*      */   static float skyColorR;
/*      */   static float skyColorG;
/*      */   static float skyColorB;
/*  103 */   static long worldTime = 0L;
/*  104 */   static long lastWorldTime = 0L;
/*  105 */   static long diffWorldTime = 0L;
/*  106 */   static float celestialAngle = 0.0F;
/*  107 */   static float sunAngle = 0.0F;
/*  108 */   static float shadowAngle = 0.0F;
/*  109 */   static int moonPhase = 0;
/*  110 */   static long systemTime = 0L;
/*  111 */   static long lastSystemTime = 0L;
/*  112 */   static long diffSystemTime = 0L;
/*  113 */   static int frameCounter = 0;
/*  114 */   static float frameTime = 0.0F;
/*  115 */   static float frameTimeCounter = 0.0F;
/*  116 */   static int systemTimeInt32 = 0;
/*  117 */   static float rainStrength = 0.0F;
/*  118 */   static float wetness = 0.0F;
/*  119 */   public static float wetnessHalfLife = 600.0F;
/*  120 */   public static float drynessHalfLife = 200.0F;
/*  121 */   public static float eyeBrightnessHalflife = 10.0F;
/*      */   static boolean usewetness = false;
/*  123 */   static int isEyeInWater = 0;
/*  124 */   static int eyeBrightness = 0;
/*  125 */   static float eyeBrightnessFadeX = 0.0F;
/*  126 */   static float eyeBrightnessFadeY = 0.0F;
/*  127 */   static float eyePosY = 0.0F;
/*  128 */   static float centerDepth = 0.0F;
/*  129 */   static float centerDepthSmooth = 0.0F;
/*  130 */   static float centerDepthSmoothHalflife = 1.0F;
/*      */   static boolean centerDepthSmoothEnabled = false;
/*  132 */   static int superSamplingLevel = 1;
/*  133 */   static float nightVision = 0.0F;
/*  134 */   static float blindness = 0.0F;
/*      */   static boolean lightmapEnabled = false;
/*      */   static boolean fogEnabled = true;
/*  137 */   public static int entityAttrib = 10;
/*  138 */   public static int midTexCoordAttrib = 11;
/*  139 */   public static int tangentAttrib = 12;
/*      */   public static boolean useEntityAttrib = false;
/*      */   public static boolean useMidTexCoordAttrib = false;
/*      */   public static boolean useTangentAttrib = false;
/*      */   public static boolean progUseEntityAttrib = false;
/*      */   public static boolean progUseMidTexCoordAttrib = false;
/*      */   public static boolean progUseTangentAttrib = false;
/*      */   private static boolean progArbGeometryShader4 = false;
/*  147 */   private static int progMaxVerticesOut = 3;
/*      */   private static boolean hasGeometryShaders = false;
/*  149 */   public static int atlasSizeX = 0;
/*  150 */   public static int atlasSizeY = 0;
/*  151 */   private static ShaderUniforms shaderUniforms = new ShaderUniforms();
/*  152 */   public static ShaderUniform4f uniform_entityColor = shaderUniforms.make4f("entityColor");
/*  153 */   public static ShaderUniform1i uniform_entityId = shaderUniforms.make1i("entityId");
/*  154 */   public static ShaderUniform1i uniform_blockEntityId = shaderUniforms.make1i("blockEntityId");
/*  155 */   public static ShaderUniform1i uniform_texture = shaderUniforms.make1i("texture");
/*  156 */   public static ShaderUniform1i uniform_lightmap = shaderUniforms.make1i("lightmap");
/*  157 */   public static ShaderUniform1i uniform_normals = shaderUniforms.make1i("normals");
/*  158 */   public static ShaderUniform1i uniform_specular = shaderUniforms.make1i("specular");
/*  159 */   public static ShaderUniform1i uniform_shadow = shaderUniforms.make1i("shadow");
/*  160 */   public static ShaderUniform1i uniform_watershadow = shaderUniforms.make1i("watershadow");
/*  161 */   public static ShaderUniform1i uniform_shadowtex0 = shaderUniforms.make1i("shadowtex0");
/*  162 */   public static ShaderUniform1i uniform_shadowtex1 = shaderUniforms.make1i("shadowtex1");
/*  163 */   public static ShaderUniform1i uniform_depthtex0 = shaderUniforms.make1i("depthtex0");
/*  164 */   public static ShaderUniform1i uniform_depthtex1 = shaderUniforms.make1i("depthtex1");
/*  165 */   public static ShaderUniform1i uniform_shadowcolor = shaderUniforms.make1i("shadowcolor");
/*  166 */   public static ShaderUniform1i uniform_shadowcolor0 = shaderUniforms.make1i("shadowcolor0");
/*  167 */   public static ShaderUniform1i uniform_shadowcolor1 = shaderUniforms.make1i("shadowcolor1");
/*  168 */   public static ShaderUniform1i uniform_noisetex = shaderUniforms.make1i("noisetex");
/*  169 */   public static ShaderUniform1i uniform_gcolor = shaderUniforms.make1i("gcolor");
/*  170 */   public static ShaderUniform1i uniform_gdepth = shaderUniforms.make1i("gdepth");
/*  171 */   public static ShaderUniform1i uniform_gnormal = shaderUniforms.make1i("gnormal");
/*  172 */   public static ShaderUniform1i uniform_composite = shaderUniforms.make1i("composite");
/*  173 */   public static ShaderUniform1i uniform_gaux1 = shaderUniforms.make1i("gaux1");
/*  174 */   public static ShaderUniform1i uniform_gaux2 = shaderUniforms.make1i("gaux2");
/*  175 */   public static ShaderUniform1i uniform_gaux3 = shaderUniforms.make1i("gaux3");
/*  176 */   public static ShaderUniform1i uniform_gaux4 = shaderUniforms.make1i("gaux4");
/*  177 */   public static ShaderUniform1i uniform_colortex0 = shaderUniforms.make1i("colortex0");
/*  178 */   public static ShaderUniform1i uniform_colortex1 = shaderUniforms.make1i("colortex1");
/*  179 */   public static ShaderUniform1i uniform_colortex2 = shaderUniforms.make1i("colortex2");
/*  180 */   public static ShaderUniform1i uniform_colortex3 = shaderUniforms.make1i("colortex3");
/*  181 */   public static ShaderUniform1i uniform_colortex4 = shaderUniforms.make1i("colortex4");
/*  182 */   public static ShaderUniform1i uniform_colortex5 = shaderUniforms.make1i("colortex5");
/*  183 */   public static ShaderUniform1i uniform_colortex6 = shaderUniforms.make1i("colortex6");
/*  184 */   public static ShaderUniform1i uniform_colortex7 = shaderUniforms.make1i("colortex7");
/*  185 */   public static ShaderUniform1i uniform_gdepthtex = shaderUniforms.make1i("gdepthtex");
/*  186 */   public static ShaderUniform1i uniform_depthtex2 = shaderUniforms.make1i("depthtex2");
/*  187 */   public static ShaderUniform1i uniform_tex = shaderUniforms.make1i("tex");
/*  188 */   public static ShaderUniform1i uniform_heldItemId = shaderUniforms.make1i("heldItemId");
/*  189 */   public static ShaderUniform1i uniform_heldBlockLightValue = shaderUniforms.make1i("heldBlockLightValue");
/*  190 */   public static ShaderUniform1i uniform_heldItemId2 = shaderUniforms.make1i("heldItemId2");
/*  191 */   public static ShaderUniform1i uniform_heldBlockLightValue2 = shaderUniforms.make1i("heldBlockLightValue2");
/*  192 */   public static ShaderUniform1i uniform_fogMode = shaderUniforms.make1i("fogMode");
/*  193 */   public static ShaderUniform1f uniform_fogDensity = shaderUniforms.make1f("fogDensity");
/*  194 */   public static ShaderUniform3f uniform_fogColor = shaderUniforms.make3f("fogColor");
/*  195 */   public static ShaderUniform3f uniform_skyColor = shaderUniforms.make3f("skyColor");
/*  196 */   public static ShaderUniform1i uniform_worldTime = shaderUniforms.make1i("worldTime");
/*  197 */   public static ShaderUniform1i uniform_worldDay = shaderUniforms.make1i("worldDay");
/*  198 */   public static ShaderUniform1i uniform_moonPhase = shaderUniforms.make1i("moonPhase");
/*  199 */   public static ShaderUniform1i uniform_frameCounter = shaderUniforms.make1i("frameCounter");
/*  200 */   public static ShaderUniform1f uniform_frameTime = shaderUniforms.make1f("frameTime");
/*  201 */   public static ShaderUniform1f uniform_frameTimeCounter = shaderUniforms.make1f("frameTimeCounter");
/*  202 */   public static ShaderUniform1f uniform_sunAngle = shaderUniforms.make1f("sunAngle");
/*  203 */   public static ShaderUniform1f uniform_shadowAngle = shaderUniforms.make1f("shadowAngle");
/*  204 */   public static ShaderUniform1f uniform_rainStrength = shaderUniforms.make1f("rainStrength");
/*  205 */   public static ShaderUniform1f uniform_aspectRatio = shaderUniforms.make1f("aspectRatio");
/*  206 */   public static ShaderUniform1f uniform_viewWidth = shaderUniforms.make1f("viewWidth");
/*  207 */   public static ShaderUniform1f uniform_viewHeight = shaderUniforms.make1f("viewHeight");
/*  208 */   public static ShaderUniform1f uniform_near = shaderUniforms.make1f("near");
/*  209 */   public static ShaderUniform1f uniform_far = shaderUniforms.make1f("far");
/*  210 */   public static ShaderUniform3f uniform_sunPosition = shaderUniforms.make3f("sunPosition");
/*  211 */   public static ShaderUniform3f uniform_moonPosition = shaderUniforms.make3f("moonPosition");
/*  212 */   public static ShaderUniform3f uniform_shadowLightPosition = shaderUniforms.make3f("shadowLightPosition");
/*  213 */   public static ShaderUniform3f uniform_upPosition = shaderUniforms.make3f("upPosition");
/*  214 */   public static ShaderUniform3f uniform_previousCameraPosition = shaderUniforms.make3f("previousCameraPosition");
/*  215 */   public static ShaderUniform3f uniform_cameraPosition = shaderUniforms.make3f("cameraPosition");
/*  216 */   public static ShaderUniformM4 uniform_gbufferModelView = shaderUniforms.makeM4("gbufferModelView");
/*  217 */   public static ShaderUniformM4 uniform_gbufferModelViewInverse = shaderUniforms.makeM4("gbufferModelViewInverse");
/*  218 */   public static ShaderUniformM4 uniform_gbufferPreviousProjection = shaderUniforms.makeM4("gbufferPreviousProjection");
/*  219 */   public static ShaderUniformM4 uniform_gbufferProjection = shaderUniforms.makeM4("gbufferProjection");
/*  220 */   public static ShaderUniformM4 uniform_gbufferProjectionInverse = shaderUniforms.makeM4("gbufferProjectionInverse");
/*  221 */   public static ShaderUniformM4 uniform_gbufferPreviousModelView = shaderUniforms.makeM4("gbufferPreviousModelView");
/*  222 */   public static ShaderUniformM4 uniform_shadowProjection = shaderUniforms.makeM4("shadowProjection");
/*  223 */   public static ShaderUniformM4 uniform_shadowProjectionInverse = shaderUniforms.makeM4("shadowProjectionInverse");
/*  224 */   public static ShaderUniformM4 uniform_shadowModelView = shaderUniforms.makeM4("shadowModelView");
/*  225 */   public static ShaderUniformM4 uniform_shadowModelViewInverse = shaderUniforms.makeM4("shadowModelViewInverse");
/*  226 */   public static ShaderUniform1f uniform_wetness = shaderUniforms.make1f("wetness");
/*  227 */   public static ShaderUniform1f uniform_eyeAltitude = shaderUniforms.make1f("eyeAltitude");
/*  228 */   public static ShaderUniform2i uniform_eyeBrightness = shaderUniforms.make2i("eyeBrightness");
/*  229 */   public static ShaderUniform2i uniform_eyeBrightnessSmooth = shaderUniforms.make2i("eyeBrightnessSmooth");
/*  230 */   public static ShaderUniform2i uniform_terrainTextureSize = shaderUniforms.make2i("terrainTextureSize");
/*  231 */   public static ShaderUniform1i uniform_terrainIconSize = shaderUniforms.make1i("terrainIconSize");
/*  232 */   public static ShaderUniform1i uniform_isEyeInWater = shaderUniforms.make1i("isEyeInWater");
/*  233 */   public static ShaderUniform1f uniform_nightVision = shaderUniforms.make1f("nightVision");
/*  234 */   public static ShaderUniform1f uniform_blindness = shaderUniforms.make1f("blindness");
/*  235 */   public static ShaderUniform1f uniform_screenBrightness = shaderUniforms.make1f("screenBrightness");
/*  236 */   public static ShaderUniform1i uniform_hideGUI = shaderUniforms.make1i("hideGUI");
/*  237 */   public static ShaderUniform1f uniform_centerDepthSmooth = shaderUniforms.make1f("centerDepthSmooth");
/*  238 */   public static ShaderUniform2i uniform_atlasSize = shaderUniforms.make2i("atlasSize");
/*  239 */   public static ShaderUniform4i uniform_blendFunc = shaderUniforms.make4i("blendFunc");
/*  240 */   public static ShaderUniform1i uniform_instanceId = shaderUniforms.make1i("instanceId");
/*      */   static double previousCameraPositionX;
/*      */   static double previousCameraPositionY;
/*      */   static double previousCameraPositionZ;
/*      */   static double cameraPositionX;
/*      */   static double cameraPositionY;
/*      */   static double cameraPositionZ;
/*      */   static int cameraOffsetX;
/*      */   static int cameraOffsetZ;
/*  249 */   static int shadowPassInterval = 0;
/*      */   public static boolean needResizeShadow = false;
/*  251 */   static int shadowMapWidth = 1024;
/*  252 */   static int shadowMapHeight = 1024;
/*  253 */   static int spShadowMapWidth = 1024;
/*  254 */   static int spShadowMapHeight = 1024;
/*  255 */   static float shadowMapFOV = 90.0F;
/*  256 */   static float shadowMapHalfPlane = 160.0F;
/*      */   static boolean shadowMapIsOrtho = true;
/*  258 */   static float shadowDistanceRenderMul = -1.0F;
/*  259 */   static int shadowPassCounter = 0;
/*      */   static int preShadowPassThirdPersonView;
/*      */   public static boolean shouldSkipDefaultShadow = false;
/*      */   static boolean waterShadowEnabled = false;
/*      */   static final int MaxDrawBuffers = 8;
/*      */   static final int MaxColorBuffers = 8;
/*      */   static final int MaxDepthBuffers = 3;
/*      */   static final int MaxShadowColorBuffers = 8;
/*      */   static final int MaxShadowDepthBuffers = 2;
/*  268 */   static int usedColorBuffers = 0;
/*  269 */   static int usedDepthBuffers = 0;
/*  270 */   static int usedShadowColorBuffers = 0;
/*  271 */   static int usedShadowDepthBuffers = 0;
/*  272 */   static int usedColorAttachs = 0;
/*  273 */   static int usedDrawBuffers = 0;
/*  274 */   static int dfb = 0;
/*  275 */   static int sfb = 0;
/*  276 */   private static int[] gbuffersFormat = new int[8];
/*  277 */   public static boolean[] gbuffersClear = new boolean[8];
/*  278 */   public static Vector4f[] gbuffersClearColor = new Vector4f[8];
/*  279 */   private static Programs programs = new Programs();
/*  280 */   public static final Program ProgramNone = programs.getProgramNone();
/*  281 */   public static final Program ProgramShadow = programs.makeShadow("shadow", ProgramNone);
/*  282 */   public static final Program ProgramShadowSolid = programs.makeShadow("shadow_solid", ProgramShadow);
/*  283 */   public static final Program ProgramShadowCutout = programs.makeShadow("shadow_cutout", ProgramShadow);
/*  284 */   public static final Program ProgramBasic = programs.makeGbuffers("gbuffers_basic", ProgramNone);
/*  285 */   public static final Program ProgramTextured = programs.makeGbuffers("gbuffers_textured", ProgramBasic);
/*  286 */   public static final Program ProgramTexturedLit = programs.makeGbuffers("gbuffers_textured_lit", ProgramTextured);
/*  287 */   public static final Program ProgramSkyBasic = programs.makeGbuffers("gbuffers_skybasic", ProgramBasic);
/*  288 */   public static final Program ProgramSkyTextured = programs.makeGbuffers("gbuffers_skytextured", ProgramTextured);
/*  289 */   public static final Program ProgramClouds = programs.makeGbuffers("gbuffers_clouds", ProgramTextured);
/*  290 */   public static final Program ProgramTerrain = programs.makeGbuffers("gbuffers_terrain", ProgramTexturedLit);
/*  291 */   public static final Program ProgramTerrainSolid = programs.makeGbuffers("gbuffers_terrain_solid", ProgramTerrain);
/*  292 */   public static final Program ProgramTerrainCutoutMip = programs.makeGbuffers("gbuffers_terrain_cutout_mip", ProgramTerrain);
/*  293 */   public static final Program ProgramTerrainCutout = programs.makeGbuffers("gbuffers_terrain_cutout", ProgramTerrain);
/*  294 */   public static final Program ProgramDamagedBlock = programs.makeGbuffers("gbuffers_damagedblock", ProgramTerrain);
/*  295 */   public static final Program ProgramBlock = programs.makeGbuffers("gbuffers_block", ProgramTerrain);
/*  296 */   public static final Program ProgramBeaconBeam = programs.makeGbuffers("gbuffers_beaconbeam", ProgramTextured);
/*  297 */   public static final Program ProgramItem = programs.makeGbuffers("gbuffers_item", ProgramTexturedLit);
/*  298 */   public static final Program ProgramEntities = programs.makeGbuffers("gbuffers_entities", ProgramTexturedLit);
/*  299 */   public static final Program ProgramEntitiesGlowing = programs.makeGbuffers("gbuffers_entities_glowing", ProgramEntities);
/*  300 */   public static final Program ProgramArmorGlint = programs.makeGbuffers("gbuffers_armor_glint", ProgramTextured);
/*  301 */   public static final Program ProgramSpiderEyes = programs.makeGbuffers("gbuffers_spidereyes", ProgramTextured);
/*  302 */   public static final Program ProgramHand = programs.makeGbuffers("gbuffers_hand", ProgramTexturedLit);
/*  303 */   public static final Program ProgramWeather = programs.makeGbuffers("gbuffers_weather", ProgramTexturedLit);
/*  304 */   public static final Program ProgramDeferredPre = programs.makeVirtual("deferred_pre");
/*  305 */   public static final Program[] ProgramsDeferred = programs.makeDeferreds("deferred", 16);
/*  306 */   public static final Program ProgramDeferred = ProgramsDeferred[0];
/*  307 */   public static final Program ProgramWater = programs.makeGbuffers("gbuffers_water", ProgramTerrain);
/*  308 */   public static final Program ProgramHandWater = programs.makeGbuffers("gbuffers_hand_water", ProgramHand);
/*  309 */   public static final Program ProgramCompositePre = programs.makeVirtual("composite_pre");
/*  310 */   public static final Program[] ProgramsComposite = programs.makeComposites("composite", 16);
/*  311 */   public static final Program ProgramComposite = ProgramsComposite[0];
/*  312 */   public static final Program ProgramFinal = programs.makeComposite("final");
/*  313 */   public static final int ProgramCount = programs.getCount();
/*  314 */   public static final Program[] ProgramsAll = programs.getPrograms();
/*  315 */   public static Program activeProgram = ProgramNone;
/*  316 */   public static int activeProgramID = 0;
/*  317 */   private static ProgramStack programStack = new ProgramStack();
/*      */   private static boolean hasDeferredPrograms = false;
/*  319 */   static IntBuffer activeDrawBuffers = null;
/*  320 */   private static int activeCompositeMipmapSetting = 0;
/*  321 */   public static Properties loadedShaders = null;
/*  322 */   public static Properties shadersConfig = null;
/*  323 */   public static ITextureObject defaultTexture = null;
/*  324 */   public static boolean[] shadowHardwareFilteringEnabled = new boolean[2];
/*  325 */   public static boolean[] shadowMipmapEnabled = new boolean[2];
/*  326 */   public static boolean[] shadowFilterNearest = new boolean[2];
/*  327 */   public static boolean[] shadowColorMipmapEnabled = new boolean[8];
/*  328 */   public static boolean[] shadowColorFilterNearest = new boolean[8];
/*      */   public static boolean configTweakBlockDamage = false;
/*      */   public static boolean configCloudShadow = false;
/*  331 */   public static float configHandDepthMul = 0.125F;
/*  332 */   public static float configRenderResMul = 1.0F;
/*  333 */   public static float configShadowResMul = 1.0F;
/*  334 */   public static int configTexMinFilB = 0;
/*  335 */   public static int configTexMinFilN = 0;
/*  336 */   public static int configTexMinFilS = 0;
/*  337 */   public static int configTexMagFilB = 0;
/*  338 */   public static int configTexMagFilN = 0;
/*  339 */   public static int configTexMagFilS = 0;
/*      */   public static boolean configShadowClipFrustrum = true;
/*      */   public static boolean configNormalMap = true;
/*      */   public static boolean configSpecularMap = true;
/*  343 */   public static PropertyDefaultTrueFalse configOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
/*  344 */   public static PropertyDefaultTrueFalse configOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
/*  345 */   public static int configAntialiasingLevel = 0;
/*      */   public static final int texMinFilRange = 3;
/*      */   public static final int texMagFilRange = 2;
/*  348 */   public static final String[] texMinFilDesc = new String[] { "Nearest", "Nearest-Nearest", "Nearest-Linear" };
/*  349 */   public static final String[] texMagFilDesc = new String[] { "Nearest", "Linear" };
/*  350 */   public static final int[] texMinFilValue = new int[] { 9728, 9984, 9986 };
/*  351 */   public static final int[] texMagFilValue = new int[] { 9728, 9729 };
/*  352 */   private static IShaderPack shaderPack = null;
/*      */   public static boolean shaderPackLoaded = false;
/*      */   public static String currentShaderName;
/*      */   public static final String SHADER_PACK_NAME_NONE = "OFF";
/*      */   public static final String SHADER_PACK_NAME_DEFAULT = "(internal)";
/*      */   public static final String SHADER_PACKS_DIR_NAME = "shaderpacks";
/*      */   public static final String OPTIONS_FILE_NAME = "optionsshaders.txt";
/*      */   public static final File shaderPacksDir;
/*      */   static File configFile;
/*  361 */   private static ShaderOption[] shaderPackOptions = null;
/*  362 */   private static Set<String> shaderPackOptionSliders = null;
/*  363 */   static ShaderProfile[] shaderPackProfiles = null;
/*  364 */   static Map<String, ScreenShaderOptions> shaderPackGuiScreens = null;
/*  365 */   static Map<String, IExpressionBool> shaderPackProgramConditions = new HashMap<>();
/*      */   public static final String PATH_SHADERS_PROPERTIES = "/shaders/shaders.properties";
/*  367 */   public static PropertyDefaultFastFancyOff shaderPackClouds = new PropertyDefaultFastFancyOff("clouds", "Clouds", 0);
/*  368 */   public static PropertyDefaultTrueFalse shaderPackOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
/*  369 */   public static PropertyDefaultTrueFalse shaderPackOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
/*  370 */   public static PropertyDefaultTrueFalse shaderPackDynamicHandLight = new PropertyDefaultTrueFalse("dynamicHandLight", "Dynamic Hand Light", 0);
/*  371 */   public static PropertyDefaultTrueFalse shaderPackShadowTranslucent = new PropertyDefaultTrueFalse("shadowTranslucent", "Shadow Translucent", 0);
/*  372 */   public static PropertyDefaultTrueFalse shaderPackUnderwaterOverlay = new PropertyDefaultTrueFalse("underwaterOverlay", "Underwater Overlay", 0);
/*  373 */   public static PropertyDefaultTrueFalse shaderPackSun = new PropertyDefaultTrueFalse("sun", "Sun", 0);
/*  374 */   public static PropertyDefaultTrueFalse shaderPackMoon = new PropertyDefaultTrueFalse("moon", "Moon", 0);
/*  375 */   public static PropertyDefaultTrueFalse shaderPackVignette = new PropertyDefaultTrueFalse("vignette", "Vignette", 0);
/*  376 */   public static PropertyDefaultTrueFalse shaderPackBackFaceSolid = new PropertyDefaultTrueFalse("backFace.solid", "Back-face Solid", 0);
/*  377 */   public static PropertyDefaultTrueFalse shaderPackBackFaceCutout = new PropertyDefaultTrueFalse("backFace.cutout", "Back-face Cutout", 0);
/*  378 */   public static PropertyDefaultTrueFalse shaderPackBackFaceCutoutMipped = new PropertyDefaultTrueFalse("backFace.cutoutMipped", "Back-face Cutout Mipped", 0);
/*  379 */   public static PropertyDefaultTrueFalse shaderPackBackFaceTranslucent = new PropertyDefaultTrueFalse("backFace.translucent", "Back-face Translucent", 0);
/*  380 */   public static PropertyDefaultTrueFalse shaderPackRainDepth = new PropertyDefaultTrueFalse("rain.depth", "Rain Depth", 0);
/*  381 */   public static PropertyDefaultTrueFalse shaderPackBeaconBeamDepth = new PropertyDefaultTrueFalse("beacon.beam.depth", "Rain Depth", 0);
/*  382 */   public static PropertyDefaultTrueFalse shaderPackSeparateAo = new PropertyDefaultTrueFalse("separateAo", "Separate AO", 0);
/*  383 */   public static PropertyDefaultTrueFalse shaderPackFrustumCulling = new PropertyDefaultTrueFalse("frustum.culling", "Frustum Culling", 0);
/*  384 */   private static Map<String, String> shaderPackResources = new HashMap<>();
/*  385 */   private static World currentWorld = null;
/*  386 */   private static List<Integer> shaderPackDimensions = new ArrayList<>();
/*  387 */   private static ICustomTexture[] customTexturesGbuffers = null;
/*  388 */   private static ICustomTexture[] customTexturesComposite = null;
/*  389 */   private static ICustomTexture[] customTexturesDeferred = null;
/*  390 */   private static String noiseTexturePath = null;
/*  391 */   private static CustomUniforms customUniforms = null;
/*      */   private static final int STAGE_GBUFFERS = 0;
/*      */   private static final int STAGE_COMPOSITE = 1;
/*      */   private static final int STAGE_DEFERRED = 2;
/*  395 */   private static final String[] STAGE_NAMES = new String[] { "gbuffers", "composite", "deferred" };
/*      */   public static final boolean enableShadersOption = true;
/*      */   private static final boolean enableShadersDebug = true;
/*  398 */   public static final boolean saveFinalShaders = System.getProperty("shaders.debug.save", "false").equals("true");
/*  399 */   public static float blockLightLevel05 = 0.5F;
/*  400 */   public static float blockLightLevel06 = 0.6F;
/*  401 */   public static float blockLightLevel08 = 0.8F;
/*  402 */   public static float aoLevel = -1.0F;
/*  403 */   public static float sunPathRotation = 0.0F;
/*  404 */   public static float shadowAngleInterval = 0.0F;
/*  405 */   public static int fogMode = 0;
/*  406 */   public static float fogDensity = 0.0F;
/*      */   public static float fogColorR;
/*      */   public static float fogColorG;
/*      */   public static float fogColorB;
/*  410 */   public static float shadowIntervalSize = 2.0F;
/*  411 */   public static int terrainIconSize = 16;
/*  412 */   public static int[] terrainTextureSize = new int[2];
/*      */   private static ICustomTexture noiseTexture;
/*      */   private static boolean noiseTextureEnabled = false;
/*  415 */   private static int noiseTextureResolution = 256;
/*  416 */   static final int[] colorTextureImageUnit = new int[] { 0, 1, 2, 3, 7, 8, 9, 10 };
/*  417 */   private static final int bigBufferSize = (285 + 8 * ProgramCount) * 4;
/*  418 */   private static final ByteBuffer bigBuffer = (ByteBuffer)BufferUtils.createByteBuffer(bigBufferSize).limit(0);
/*  419 */   static final float[] faProjection = new float[16];
/*  420 */   static final float[] faProjectionInverse = new float[16];
/*  421 */   static final float[] faModelView = new float[16];
/*  422 */   static final float[] faModelViewInverse = new float[16];
/*  423 */   static final float[] faShadowProjection = new float[16];
/*  424 */   static final float[] faShadowProjectionInverse = new float[16];
/*  425 */   static final float[] faShadowModelView = new float[16];
/*  426 */   static final float[] faShadowModelViewInverse = new float[16];
/*  427 */   static final FloatBuffer projection = nextFloatBuffer(16);
/*  428 */   static final FloatBuffer projectionInverse = nextFloatBuffer(16);
/*  429 */   static final FloatBuffer modelView = nextFloatBuffer(16);
/*  430 */   static final FloatBuffer modelViewInverse = nextFloatBuffer(16);
/*  431 */   static final FloatBuffer shadowProjection = nextFloatBuffer(16);
/*  432 */   static final FloatBuffer shadowProjectionInverse = nextFloatBuffer(16);
/*  433 */   static final FloatBuffer shadowModelView = nextFloatBuffer(16);
/*  434 */   static final FloatBuffer shadowModelViewInverse = nextFloatBuffer(16);
/*  435 */   static final FloatBuffer previousProjection = nextFloatBuffer(16);
/*  436 */   static final FloatBuffer previousModelView = nextFloatBuffer(16);
/*  437 */   static final FloatBuffer tempMatrixDirectBuffer = nextFloatBuffer(16);
/*  438 */   static final FloatBuffer tempDirectFloatBuffer = nextFloatBuffer(16);
/*  439 */   static final IntBuffer dfbColorTextures = nextIntBuffer(16);
/*  440 */   static final IntBuffer dfbDepthTextures = nextIntBuffer(3);
/*  441 */   static final IntBuffer sfbColorTextures = nextIntBuffer(8);
/*  442 */   static final IntBuffer sfbDepthTextures = nextIntBuffer(2);
/*  443 */   static final IntBuffer dfbDrawBuffers = nextIntBuffer(8);
/*  444 */   static final IntBuffer sfbDrawBuffers = nextIntBuffer(8);
/*  445 */   static final IntBuffer drawBuffersNone = (IntBuffer)nextIntBuffer(8).limit(0);
/*  446 */   static final IntBuffer drawBuffersColorAtt0 = (IntBuffer)nextIntBuffer(8).put(36064).position(0).limit(1);
/*  447 */   static final FlipTextures dfbColorTexturesFlip = new FlipTextures(dfbColorTextures, 8);
/*      */   static Map<Block, Integer> mapBlockToEntityData;
/*  449 */   private static final String[] formatNames = new String[] { "R8", "RG8", "RGB8", "RGBA8", "R8_SNORM", "RG8_SNORM", "RGB8_SNORM", "RGBA8_SNORM", "R16", "RG16", "RGB16", "RGBA16", "R16_SNORM", "RG16_SNORM", "RGB16_SNORM", "RGBA16_SNORM", "R16F", "RG16F", "RGB16F", "RGBA16F", "R32F", "RG32F", "RGB32F", "RGBA32F", "R32I", "RG32I", "RGB32I", "RGBA32I", "R32UI", "RG32UI", "RGB32UI", "RGBA32UI", "R3_G3_B2", "RGB5_A1", "RGB10_A2", "R11F_G11F_B10F", "RGB9_E5" };
/*  450 */   private static final int[] formatIds = new int[] { 33321, 33323, 32849, 32856, 36756, 36757, 36758, 36759, 33322, 33324, 32852, 32859, 36760, 36761, 36762, 36763, 33325, 33327, 34843, 34842, 33326, 33328, 34837, 34836, 33333, 33339, 36227, 36226, 33334, 33340, 36209, 36208, 10768, 32855, 32857, 35898, 35901 };
/*  451 */   private static final Pattern patternLoadEntityDataMap = Pattern.compile("\\s*([\\w:]+)\\s*=\\s*([-]?\\d+)\\s*");
/*  452 */   public static int[] entityData = new int[32];
/*  453 */   public static int entityDataIndex = 0;
/*      */ 
/*      */   
/*      */   private static ByteBuffer nextByteBuffer(int size) {
/*  457 */     ByteBuffer bytebuffer = bigBuffer;
/*  458 */     int i = bytebuffer.limit();
/*  459 */     bytebuffer.position(i).limit(i + size);
/*  460 */     return bytebuffer.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public static IntBuffer nextIntBuffer(int size) {
/*  465 */     ByteBuffer bytebuffer = bigBuffer;
/*  466 */     int i = bytebuffer.limit();
/*  467 */     bytebuffer.position(i).limit(i + size * 4);
/*  468 */     return bytebuffer.asIntBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   private static FloatBuffer nextFloatBuffer(int size) {
/*  473 */     ByteBuffer bytebuffer = bigBuffer;
/*  474 */     int i = bytebuffer.limit();
/*  475 */     bytebuffer.position(i).limit(i + size * 4);
/*  476 */     return bytebuffer.asFloatBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   private static IntBuffer[] nextIntBufferArray(int count, int size) {
/*  481 */     IntBuffer[] aintbuffer = new IntBuffer[count];
/*      */     
/*  483 */     for (int i = 0; i < count; i++)
/*      */     {
/*  485 */       aintbuffer[i] = nextIntBuffer(size);
/*      */     }
/*      */     
/*  488 */     return aintbuffer;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadConfig() {
/*  493 */     SMCLog.info("Load shaders configuration.");
/*      */ 
/*      */     
/*      */     try {
/*  497 */       if (!shaderPacksDir.exists())
/*      */       {
/*  499 */         shaderPacksDir.mkdir();
/*      */       }
/*      */     }
/*  502 */     catch (Exception var8) {
/*      */       
/*  504 */       SMCLog.severe("Failed to open the shaderpacks directory: " + shaderPacksDir);
/*      */     } 
/*      */     
/*  507 */     shadersConfig = (Properties)new PropertiesOrdered();
/*  508 */     shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), "");
/*      */     
/*  510 */     if (configFile.exists()) {
/*      */       
/*      */       try {
/*      */         
/*  514 */         FileReader filereader = new FileReader(configFile);
/*  515 */         shadersConfig.load(filereader);
/*  516 */         filereader.close();
/*      */       }
/*  518 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  524 */     if (!configFile.exists()) {
/*      */       
/*      */       try {
/*      */         
/*  528 */         storeConfig();
/*      */       }
/*  530 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  536 */     EnumShaderOption[] aenumshaderoption = EnumShaderOption.values();
/*      */     
/*  538 */     for (int i = 0; i < aenumshaderoption.length; i++) {
/*      */       
/*  540 */       EnumShaderOption enumshaderoption = aenumshaderoption[i];
/*  541 */       String s = enumshaderoption.getPropertyKey();
/*  542 */       String s1 = enumshaderoption.getValueDefault();
/*  543 */       String s2 = shadersConfig.getProperty(s, s1);
/*  544 */       setEnumShaderOption(enumshaderoption, s2);
/*      */     } 
/*      */     
/*  547 */     loadShaderPack();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setEnumShaderOption(EnumShaderOption eso, String str) {
/*  552 */     if (str == null)
/*      */     {
/*  554 */       str = eso.getValueDefault();
/*      */     }
/*      */     
/*  557 */     switch (eso) {
/*      */       
/*      */       case GBUFFERS:
/*  560 */         configAntialiasingLevel = Config.parseInt(str, 0);
/*      */         return;
/*      */       
/*      */       case DEFERRED:
/*  564 */         configNormalMap = Config.parseBoolean(str, true);
/*      */         return;
/*      */       
/*      */       case COMPOSITE:
/*  568 */         configSpecularMap = Config.parseBoolean(str, true);
/*      */         return;
/*      */       
/*      */       case SHADOW:
/*  572 */         configRenderResMul = Config.parseFloat(str, 1.0F);
/*      */         return;
/*      */       
/*      */       case null:
/*  576 */         configShadowResMul = Config.parseFloat(str, 1.0F);
/*      */         return;
/*      */       
/*      */       case null:
/*  580 */         configHandDepthMul = Config.parseFloat(str, 0.125F);
/*      */         return;
/*      */       
/*      */       case null:
/*  584 */         configCloudShadow = Config.parseBoolean(str, true);
/*      */         return;
/*      */       
/*      */       case null:
/*  588 */         configOldHandLight.setPropertyValue(str);
/*      */         return;
/*      */       
/*      */       case null:
/*  592 */         configOldLighting.setPropertyValue(str);
/*      */         return;
/*      */       
/*      */       case null:
/*  596 */         currentShaderName = str;
/*      */         return;
/*      */       
/*      */       case null:
/*  600 */         configTweakBlockDamage = Config.parseBoolean(str, true);
/*      */         return;
/*      */       
/*      */       case null:
/*  604 */         configShadowClipFrustrum = Config.parseBoolean(str, true);
/*      */         return;
/*      */       
/*      */       case null:
/*  608 */         configTexMinFilB = Config.parseInt(str, 0);
/*      */         return;
/*      */       
/*      */       case null:
/*  612 */         configTexMinFilN = Config.parseInt(str, 0);
/*      */         return;
/*      */       
/*      */       case null:
/*  616 */         configTexMinFilS = Config.parseInt(str, 0);
/*      */         return;
/*      */       
/*      */       case null:
/*  620 */         configTexMagFilB = Config.parseInt(str, 0);
/*      */         return;
/*      */       
/*      */       case null:
/*  624 */         configTexMagFilB = Config.parseInt(str, 0);
/*      */         return;
/*      */       
/*      */       case null:
/*  628 */         configTexMagFilB = Config.parseInt(str, 0);
/*      */         return;
/*      */     } 
/*      */     
/*  632 */     throw new IllegalArgumentException("Unknown option: " + eso);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void storeConfig() {
/*  638 */     SMCLog.info("Save shaders configuration.");
/*      */     
/*  640 */     if (shadersConfig == null)
/*      */     {
/*  642 */       shadersConfig = (Properties)new PropertiesOrdered();
/*      */     }
/*      */     
/*  645 */     EnumShaderOption[] aenumshaderoption = EnumShaderOption.values();
/*      */     
/*  647 */     for (int i = 0; i < aenumshaderoption.length; i++) {
/*      */       
/*  649 */       EnumShaderOption enumshaderoption = aenumshaderoption[i];
/*  650 */       String s = enumshaderoption.getPropertyKey();
/*  651 */       String s1 = getEnumShaderOption(enumshaderoption);
/*  652 */       shadersConfig.setProperty(s, s1);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  657 */       FileWriter filewriter = new FileWriter(configFile);
/*  658 */       shadersConfig.store(filewriter, (String)null);
/*  659 */       filewriter.close();
/*      */     }
/*  661 */     catch (Exception exception) {
/*      */       
/*  663 */       SMCLog.severe("Error saving configuration: " + exception.getClass().getName() + ": " + exception.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getEnumShaderOption(EnumShaderOption eso) {
/*  669 */     switch (eso) {
/*      */       
/*      */       case GBUFFERS:
/*  672 */         return Integer.toString(configAntialiasingLevel);
/*      */       
/*      */       case DEFERRED:
/*  675 */         return Boolean.toString(configNormalMap);
/*      */       
/*      */       case COMPOSITE:
/*  678 */         return Boolean.toString(configSpecularMap);
/*      */       
/*      */       case SHADOW:
/*  681 */         return Float.toString(configRenderResMul);
/*      */       
/*      */       case null:
/*  684 */         return Float.toString(configShadowResMul);
/*      */       
/*      */       case null:
/*  687 */         return Float.toString(configHandDepthMul);
/*      */       
/*      */       case null:
/*  690 */         return Boolean.toString(configCloudShadow);
/*      */       
/*      */       case null:
/*  693 */         return configOldHandLight.getPropertyValue();
/*      */       
/*      */       case null:
/*  696 */         return configOldLighting.getPropertyValue();
/*      */       
/*      */       case null:
/*  699 */         return currentShaderName;
/*      */       
/*      */       case null:
/*  702 */         return Boolean.toString(configTweakBlockDamage);
/*      */       
/*      */       case null:
/*  705 */         return Boolean.toString(configShadowClipFrustrum);
/*      */       
/*      */       case null:
/*  708 */         return Integer.toString(configTexMinFilB);
/*      */       
/*      */       case null:
/*  711 */         return Integer.toString(configTexMinFilN);
/*      */       
/*      */       case null:
/*  714 */         return Integer.toString(configTexMinFilS);
/*      */       
/*      */       case null:
/*  717 */         return Integer.toString(configTexMagFilB);
/*      */       
/*      */       case null:
/*  720 */         return Integer.toString(configTexMagFilB);
/*      */       
/*      */       case null:
/*  723 */         return Integer.toString(configTexMagFilB);
/*      */     } 
/*      */     
/*  726 */     throw new IllegalArgumentException("Unknown option: " + eso);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setShaderPack(String par1name) {
/*  732 */     currentShaderName = par1name;
/*  733 */     shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), par1name);
/*  734 */     loadShaderPack();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadShaderPack() {
/*  739 */     boolean flag = shaderPackLoaded;
/*  740 */     boolean flag1 = isOldLighting();
/*      */     
/*  742 */     if (mc.renderGlobal != null)
/*      */     {
/*  744 */       mc.renderGlobal.pauseChunkUpdates();
/*      */     }
/*      */     
/*  747 */     shaderPackLoaded = false;
/*      */     
/*  749 */     if (shaderPack != null) {
/*      */       
/*  751 */       shaderPack.close();
/*  752 */       shaderPack = null;
/*  753 */       shaderPackResources.clear();
/*  754 */       shaderPackDimensions.clear();
/*  755 */       shaderPackOptions = null;
/*  756 */       shaderPackOptionSliders = null;
/*  757 */       shaderPackProfiles = null;
/*  758 */       shaderPackGuiScreens = null;
/*  759 */       shaderPackProgramConditions.clear();
/*  760 */       shaderPackClouds.resetValue();
/*  761 */       shaderPackOldHandLight.resetValue();
/*  762 */       shaderPackDynamicHandLight.resetValue();
/*  763 */       shaderPackOldLighting.resetValue();
/*  764 */       resetCustomTextures();
/*  765 */       noiseTexturePath = null;
/*      */     } 
/*      */     
/*  768 */     boolean flag2 = false;
/*      */     
/*  770 */     if (Config.isAntialiasing()) {
/*      */       
/*  772 */       SMCLog.info("Shaders can not be loaded, Antialiasing is enabled: " + Config.getAntialiasingLevel() + "x");
/*  773 */       flag2 = true;
/*      */     } 
/*      */     
/*  776 */     if (Config.isAnisotropicFiltering()) {
/*      */       
/*  778 */       SMCLog.info("Shaders can not be loaded, Anisotropic Filtering is enabled: " + Config.getAnisotropicFilterLevel() + "x");
/*  779 */       flag2 = true;
/*      */     } 
/*      */     
/*  782 */     if (Config.isFastRender()) {
/*      */       
/*  784 */       SMCLog.info("Shaders can not be loaded, Fast Render is enabled.");
/*  785 */       flag2 = true;
/*      */     } 
/*      */     
/*  788 */     String s = shadersConfig.getProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), "(internal)");
/*      */     
/*  790 */     if (!flag2) {
/*      */       
/*  792 */       shaderPack = getShaderPack(s);
/*  793 */       shaderPackLoaded = (shaderPack != null);
/*      */     } 
/*      */     
/*  796 */     if (shaderPackLoaded) {
/*      */       
/*  798 */       SMCLog.info("Loaded shaderpack: " + getShaderPackName());
/*      */     }
/*      */     else {
/*      */       
/*  802 */       SMCLog.info("No shaderpack loaded.");
/*  803 */       shaderPack = new ShaderPackNone();
/*      */     } 
/*      */     
/*  806 */     if (saveFinalShaders)
/*      */     {
/*  808 */       clearDirectory(new File(shaderPacksDir, "debug"));
/*      */     }
/*      */     
/*  811 */     loadShaderPackResources();
/*  812 */     loadShaderPackDimensions();
/*  813 */     shaderPackOptions = loadShaderPackOptions();
/*  814 */     loadShaderPackProperties();
/*  815 */     boolean flag3 = (shaderPackLoaded != flag);
/*  816 */     boolean flag4 = (isOldLighting() != flag1);
/*      */     
/*  818 */     if (flag3 || flag4) {
/*      */       
/*  820 */       DefaultVertexFormats.updateVertexFormats();
/*      */       
/*  822 */       if (Reflector.LightUtil.exists()) {
/*      */         
/*  824 */         Reflector.LightUtil_itemConsumer.setValue(null);
/*  825 */         Reflector.LightUtil_tessellator.setValue(null);
/*      */       } 
/*      */       
/*  828 */       updateBlockLightLevel();
/*      */     } 
/*      */     
/*  831 */     if (mc.getResourcePackRepository() != null)
/*      */     {
/*  833 */       CustomBlockLayers.update();
/*      */     }
/*      */     
/*  836 */     if (mc.renderGlobal != null)
/*      */     {
/*  838 */       mc.renderGlobal.resumeChunkUpdates();
/*      */     }
/*      */     
/*  841 */     if ((flag3 || flag4) && mc.getResourceManager() != null)
/*      */     {
/*  843 */       mc.scheduleResourcesRefresh();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static IShaderPack getShaderPack(String name) {
/*  849 */     if (name == null)
/*      */     {
/*  851 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  855 */     name = name.trim();
/*      */     
/*  857 */     if (!name.isEmpty() && !name.equals("OFF")) {
/*      */       
/*  859 */       if (name.equals("(internal)"))
/*      */       {
/*  861 */         return new ShaderPackDefault();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  867 */         File file1 = new File(shaderPacksDir, name);
/*  868 */         return file1.isDirectory() ? new ShaderPackFolder(name, file1) : ((file1.isFile() && name.toLowerCase().endsWith(".zip")) ? new ShaderPackZip(name, file1) : null);
/*      */       }
/*  870 */       catch (Exception exception) {
/*      */         
/*  872 */         exception.printStackTrace();
/*  873 */         return null;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  879 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IShaderPack getShaderPack() {
/*  886 */     return shaderPack;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void loadShaderPackDimensions() {
/*  891 */     shaderPackDimensions.clear();
/*      */     
/*  893 */     for (int i = -128; i <= 128; i++) {
/*      */       
/*  895 */       String s = "/shaders/world" + i;
/*      */       
/*  897 */       if (shaderPack.hasDirectory(s))
/*      */       {
/*  899 */         shaderPackDimensions.add(Integer.valueOf(i));
/*      */       }
/*      */     } 
/*      */     
/*  903 */     if (shaderPackDimensions.size() > 0) {
/*      */       
/*  905 */       Integer[] ainteger = shaderPackDimensions.<Integer>toArray(new Integer[shaderPackDimensions.size()]);
/*  906 */       Config.dbg("[Shaders] Worlds: " + Config.arrayToString((Object[])ainteger));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void loadShaderPackProperties() {
/*  912 */     shaderPackClouds.resetValue();
/*  913 */     shaderPackOldHandLight.resetValue();
/*  914 */     shaderPackDynamicHandLight.resetValue();
/*  915 */     shaderPackOldLighting.resetValue();
/*  916 */     shaderPackShadowTranslucent.resetValue();
/*  917 */     shaderPackUnderwaterOverlay.resetValue();
/*  918 */     shaderPackSun.resetValue();
/*  919 */     shaderPackMoon.resetValue();
/*  920 */     shaderPackVignette.resetValue();
/*  921 */     shaderPackBackFaceSolid.resetValue();
/*  922 */     shaderPackBackFaceCutout.resetValue();
/*  923 */     shaderPackBackFaceCutoutMipped.resetValue();
/*  924 */     shaderPackBackFaceTranslucent.resetValue();
/*  925 */     shaderPackRainDepth.resetValue();
/*  926 */     shaderPackBeaconBeamDepth.resetValue();
/*  927 */     shaderPackSeparateAo.resetValue();
/*  928 */     shaderPackFrustumCulling.resetValue();
/*  929 */     BlockAliases.reset();
/*  930 */     ItemAliases.reset();
/*  931 */     EntityAliases.reset();
/*  932 */     customUniforms = null;
/*      */     
/*  934 */     for (int i = 0; i < ProgramsAll.length; i++) {
/*      */       
/*  936 */       Program program = ProgramsAll[i];
/*  937 */       program.resetProperties();
/*      */     } 
/*      */     
/*  940 */     if (shaderPack != null) {
/*      */       
/*  942 */       BlockAliases.update(shaderPack);
/*  943 */       ItemAliases.update(shaderPack);
/*  944 */       EntityAliases.update(shaderPack);
/*  945 */       String s = "/shaders/shaders.properties";
/*      */ 
/*      */       
/*      */       try {
/*  949 */         InputStream inputstream = shaderPack.getResourceAsStream(s);
/*      */         
/*  951 */         if (inputstream == null) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  956 */         inputstream = MacroProcessor.process(inputstream, s);
/*  957 */         PropertiesOrdered propertiesOrdered = new PropertiesOrdered();
/*  958 */         propertiesOrdered.load(inputstream);
/*  959 */         inputstream.close();
/*  960 */         shaderPackClouds.loadFrom((Properties)propertiesOrdered);
/*  961 */         shaderPackOldHandLight.loadFrom((Properties)propertiesOrdered);
/*  962 */         shaderPackDynamicHandLight.loadFrom((Properties)propertiesOrdered);
/*  963 */         shaderPackOldLighting.loadFrom((Properties)propertiesOrdered);
/*  964 */         shaderPackShadowTranslucent.loadFrom((Properties)propertiesOrdered);
/*  965 */         shaderPackUnderwaterOverlay.loadFrom((Properties)propertiesOrdered);
/*  966 */         shaderPackSun.loadFrom((Properties)propertiesOrdered);
/*  967 */         shaderPackVignette.loadFrom((Properties)propertiesOrdered);
/*  968 */         shaderPackMoon.loadFrom((Properties)propertiesOrdered);
/*  969 */         shaderPackBackFaceSolid.loadFrom((Properties)propertiesOrdered);
/*  970 */         shaderPackBackFaceCutout.loadFrom((Properties)propertiesOrdered);
/*  971 */         shaderPackBackFaceCutoutMipped.loadFrom((Properties)propertiesOrdered);
/*  972 */         shaderPackBackFaceTranslucent.loadFrom((Properties)propertiesOrdered);
/*  973 */         shaderPackRainDepth.loadFrom((Properties)propertiesOrdered);
/*  974 */         shaderPackBeaconBeamDepth.loadFrom((Properties)propertiesOrdered);
/*  975 */         shaderPackSeparateAo.loadFrom((Properties)propertiesOrdered);
/*  976 */         shaderPackFrustumCulling.loadFrom((Properties)propertiesOrdered);
/*  977 */         shaderPackOptionSliders = ShaderPackParser.parseOptionSliders((Properties)propertiesOrdered, shaderPackOptions);
/*  978 */         shaderPackProfiles = ShaderPackParser.parseProfiles((Properties)propertiesOrdered, shaderPackOptions);
/*  979 */         shaderPackGuiScreens = ShaderPackParser.parseGuiScreens((Properties)propertiesOrdered, shaderPackProfiles, shaderPackOptions);
/*  980 */         shaderPackProgramConditions = ShaderPackParser.parseProgramConditions((Properties)propertiesOrdered, shaderPackOptions);
/*  981 */         customTexturesGbuffers = loadCustomTextures((Properties)propertiesOrdered, 0);
/*  982 */         customTexturesComposite = loadCustomTextures((Properties)propertiesOrdered, 1);
/*  983 */         customTexturesDeferred = loadCustomTextures((Properties)propertiesOrdered, 2);
/*  984 */         noiseTexturePath = propertiesOrdered.getProperty("texture.noise");
/*      */         
/*  986 */         if (noiseTexturePath != null)
/*      */         {
/*  988 */           noiseTextureEnabled = true;
/*      */         }
/*      */         
/*  991 */         customUniforms = ShaderPackParser.parseCustomUniforms((Properties)propertiesOrdered);
/*  992 */         ShaderPackParser.parseAlphaStates((Properties)propertiesOrdered);
/*  993 */         ShaderPackParser.parseBlendStates((Properties)propertiesOrdered);
/*  994 */         ShaderPackParser.parseRenderScales((Properties)propertiesOrdered);
/*  995 */         ShaderPackParser.parseBuffersFlip((Properties)propertiesOrdered);
/*      */       }
/*  997 */       catch (IOException var3) {
/*      */         
/*  999 */         Config.warn("[Shaders] Error reading: " + s);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static ICustomTexture[] loadCustomTextures(Properties props, int stage) {
/* 1006 */     String s = "texture." + STAGE_NAMES[stage] + ".";
/* 1007 */     Set<Object> set = props.keySet();
/* 1008 */     List<ICustomTexture> list = new ArrayList<>();
/*      */     
/* 1010 */     for (Object o : set) {
/*      */       
/* 1012 */       String s1 = (String)o;
/* 1013 */       if (s1.startsWith(s)) {
/*      */         
/* 1015 */         String s2 = StrUtils.removePrefix(s1, s);
/* 1016 */         s2 = StrUtils.removeSuffix(s2, new String[] { ".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9" });
/* 1017 */         String s3 = props.getProperty(s1).trim();
/* 1018 */         int i = getTextureIndex(stage, s2);
/*      */         
/* 1020 */         if (i < 0) {
/*      */           
/* 1022 */           SMCLog.warning("Invalid texture name: " + s1);
/*      */           
/*      */           continue;
/*      */         } 
/* 1026 */         ICustomTexture icustomtexture = loadCustomTexture(i, s3);
/*      */         
/* 1028 */         if (icustomtexture != null) {
/*      */           
/* 1030 */           SMCLog.info("Custom texture: " + s1 + " = " + s3);
/* 1031 */           list.add(icustomtexture);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1037 */     if (list.size() <= 0)
/*      */     {
/* 1039 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1043 */     ICustomTexture[] aicustomtexture = list.<ICustomTexture>toArray(new ICustomTexture[list.size()]);
/* 1044 */     return aicustomtexture;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ICustomTexture loadCustomTexture(int textureUnit, String path) {
/* 1050 */     if (path == null)
/*      */     {
/* 1052 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1056 */     path = path.trim();
/* 1057 */     return (path.indexOf(':') >= 0) ? loadCustomTextureLocation(textureUnit, path) : ((path.indexOf(' ') >= 0) ? loadCustomTextureRaw(textureUnit, path) : loadCustomTextureShaders(textureUnit, path));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ICustomTexture loadCustomTextureLocation(int textureUnit, String path) {
/* 1063 */     String s = path.trim();
/* 1064 */     int i = 0;
/*      */     
/* 1066 */     if (s.startsWith("minecraft:textures/")) {
/*      */       
/* 1068 */       s = StrUtils.addSuffixCheck(s, ".png");
/*      */       
/* 1070 */       if (s.endsWith("_n.png")) {
/*      */         
/* 1072 */         s = StrUtils.replaceSuffix(s, "_n.png", ".png");
/* 1073 */         i = 1;
/*      */       }
/* 1075 */       else if (s.endsWith("_s.png")) {
/*      */         
/* 1077 */         s = StrUtils.replaceSuffix(s, "_s.png", ".png");
/* 1078 */         i = 2;
/*      */       } 
/*      */     } 
/*      */     
/* 1082 */     ResourceLocation resourcelocation = new ResourceLocation(s);
/* 1083 */     CustomTextureLocation customtexturelocation = new CustomTextureLocation(textureUnit, resourcelocation, i);
/* 1084 */     return customtexturelocation;
/*      */   }
/*      */ 
/*      */   
/*      */   private static ICustomTexture loadCustomTextureRaw(int textureUnit, String line) {
/* 1089 */     ConnectedParser connectedparser = new ConnectedParser("Shaders");
/* 1090 */     String[] astring = Config.tokenize(line, " ");
/* 1091 */     Deque<String> deque = new ArrayDeque<>(Arrays.asList(astring));
/* 1092 */     String s = deque.poll();
/* 1093 */     TextureType texturetype = (TextureType)connectedparser.parseEnum(deque.poll(), (Enum[])TextureType.values(), "texture type");
/*      */     
/* 1095 */     if (texturetype == null) {
/*      */       
/* 1097 */       SMCLog.warning("Invalid raw texture type: " + line);
/* 1098 */       return null;
/*      */     } 
/*      */ 
/*      */     
/* 1102 */     InternalFormat internalformat = (InternalFormat)connectedparser.parseEnum(deque.poll(), (Enum[])InternalFormat.values(), "internal format");
/*      */     
/* 1104 */     if (internalformat == null) {
/*      */       
/* 1106 */       SMCLog.warning("Invalid raw texture internal format: " + line);
/* 1107 */       return null;
/*      */     } 
/*      */ 
/*      */     
/* 1111 */     int i = 0;
/* 1112 */     int j = 0;
/* 1113 */     int k = 0;
/*      */     
/* 1115 */     switch (texturetype) {
/*      */       
/*      */       case GBUFFERS:
/* 1118 */         i = connectedparser.parseInt(deque.poll(), -1);
/*      */         break;
/*      */       
/*      */       case DEFERRED:
/* 1122 */         i = connectedparser.parseInt(deque.poll(), -1);
/* 1123 */         j = connectedparser.parseInt(deque.poll(), -1);
/*      */         break;
/*      */       
/*      */       case COMPOSITE:
/* 1127 */         i = connectedparser.parseInt(deque.poll(), -1);
/* 1128 */         j = connectedparser.parseInt(deque.poll(), -1);
/* 1129 */         k = connectedparser.parseInt(deque.poll(), -1);
/*      */         break;
/*      */       
/*      */       case SHADOW:
/* 1133 */         i = connectedparser.parseInt(deque.poll(), -1);
/* 1134 */         j = connectedparser.parseInt(deque.poll(), -1);
/*      */         break;
/*      */       
/*      */       default:
/* 1138 */         SMCLog.warning("Invalid raw texture type: " + texturetype);
/* 1139 */         return null;
/*      */     } 
/*      */     
/* 1142 */     if (i >= 0 && j >= 0 && k >= 0) {
/*      */       
/* 1144 */       PixelFormat pixelformat = (PixelFormat)connectedparser.parseEnum(deque.poll(), (Enum[])PixelFormat.values(), "pixel format");
/*      */       
/* 1146 */       if (pixelformat == null) {
/*      */         
/* 1148 */         SMCLog.warning("Invalid raw texture pixel format: " + line);
/* 1149 */         return null;
/*      */       } 
/*      */ 
/*      */       
/* 1153 */       PixelType pixeltype = (PixelType)connectedparser.parseEnum(deque.poll(), (Enum[])PixelType.values(), "pixel type");
/*      */       
/* 1155 */       if (pixeltype == null) {
/*      */         
/* 1157 */         SMCLog.warning("Invalid raw texture pixel type: " + line);
/* 1158 */         return null;
/*      */       } 
/* 1160 */       if (!deque.isEmpty()) {
/*      */         
/* 1162 */         SMCLog.warning("Invalid raw texture, too many parameters: " + line);
/* 1163 */         return null;
/*      */       } 
/*      */ 
/*      */       
/* 1167 */       return loadCustomTextureRaw(textureUnit, line, s, texturetype, internalformat, i, j, k, pixelformat, pixeltype);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1173 */     SMCLog.warning("Invalid raw texture size: " + line);
/* 1174 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ICustomTexture loadCustomTextureRaw(int textureUnit, String line, String path, TextureType type, InternalFormat internalFormat, int width, int height, int depth, PixelFormat pixelFormat, PixelType pixelType) {
/*      */     try {
/* 1184 */       String s = "shaders/" + StrUtils.removePrefix(path, "/");
/* 1185 */       InputStream inputstream = shaderPack.getResourceAsStream(s);
/*      */       
/* 1187 */       if (inputstream == null) {
/*      */         
/* 1189 */         SMCLog.warning("Raw texture not found: " + path);
/* 1190 */         return null;
/*      */       } 
/*      */ 
/*      */       
/* 1194 */       byte[] abyte = Config.readAll(inputstream);
/* 1195 */       IOUtils.closeQuietly(inputstream);
/* 1196 */       ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(abyte.length);
/* 1197 */       bytebuffer.put(abyte);
/* 1198 */       bytebuffer.flip();
/* 1199 */       TextureMetadataSection texturemetadatasection = SimpleShaderTexture.loadTextureMetadataSection(s, new TextureMetadataSection(true, true, new ArrayList()));
/* 1200 */       CustomTextureRaw customtextureraw = new CustomTextureRaw(type, internalFormat, width, height, depth, pixelFormat, pixelType, bytebuffer, textureUnit, texturemetadatasection.getTextureBlur(), texturemetadatasection.getTextureClamp());
/* 1201 */       return customtextureraw;
/*      */     
/*      */     }
/* 1204 */     catch (IOException ioexception) {
/*      */       
/* 1206 */       SMCLog.warning("Error loading raw texture: " + path);
/* 1207 */       SMCLog.warning("" + ioexception.getClass().getName() + ": " + ioexception.getMessage());
/* 1208 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static ICustomTexture loadCustomTextureShaders(int textureUnit, String path) {
/* 1214 */     path = path.trim();
/*      */     
/* 1216 */     if (path.indexOf('.') < 0)
/*      */     {
/* 1218 */       path = path + ".png";
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1223 */       String s = "shaders/" + StrUtils.removePrefix(path, "/");
/* 1224 */       InputStream inputstream = shaderPack.getResourceAsStream(s);
/*      */       
/* 1226 */       if (inputstream == null) {
/*      */         
/* 1228 */         SMCLog.warning("Texture not found: " + path);
/* 1229 */         return null;
/*      */       } 
/*      */ 
/*      */       
/* 1233 */       IOUtils.closeQuietly(inputstream);
/* 1234 */       SimpleShaderTexture simpleshadertexture = new SimpleShaderTexture(s);
/* 1235 */       simpleshadertexture.loadTexture(mc.getResourceManager());
/* 1236 */       CustomTexture customtexture = new CustomTexture(textureUnit, s, (ITextureObject)simpleshadertexture);
/* 1237 */       return customtexture;
/*      */     
/*      */     }
/* 1240 */     catch (IOException ioexception) {
/*      */       
/* 1242 */       SMCLog.warning("Error loading texture: " + path);
/* 1243 */       SMCLog.warning("" + ioexception.getClass().getName() + ": " + ioexception.getMessage());
/* 1244 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getTextureIndex(int stage, String name) {
/* 1250 */     if (stage == 0) {
/*      */       
/* 1252 */       if (name.equals("texture"))
/*      */       {
/* 1254 */         return 0;
/*      */       }
/*      */       
/* 1257 */       if (name.equals("lightmap"))
/*      */       {
/* 1259 */         return 1;
/*      */       }
/*      */       
/* 1262 */       if (name.equals("normals"))
/*      */       {
/* 1264 */         return 2;
/*      */       }
/*      */       
/* 1267 */       if (name.equals("specular"))
/*      */       {
/* 1269 */         return 3;
/*      */       }
/*      */       
/* 1272 */       if (name.equals("shadowtex0") || name.equals("watershadow"))
/*      */       {
/* 1274 */         return 4;
/*      */       }
/*      */       
/* 1277 */       if (name.equals("shadow"))
/*      */       {
/* 1279 */         return waterShadowEnabled ? 5 : 4;
/*      */       }
/*      */       
/* 1282 */       if (name.equals("shadowtex1"))
/*      */       {
/* 1284 */         return 5;
/*      */       }
/*      */       
/* 1287 */       if (name.equals("depthtex0"))
/*      */       {
/* 1289 */         return 6;
/*      */       }
/*      */       
/* 1292 */       if (name.equals("gaux1"))
/*      */       {
/* 1294 */         return 7;
/*      */       }
/*      */       
/* 1297 */       if (name.equals("gaux2"))
/*      */       {
/* 1299 */         return 8;
/*      */       }
/*      */       
/* 1302 */       if (name.equals("gaux3"))
/*      */       {
/* 1304 */         return 9;
/*      */       }
/*      */       
/* 1307 */       if (name.equals("gaux4"))
/*      */       {
/* 1309 */         return 10;
/*      */       }
/*      */       
/* 1312 */       if (name.equals("depthtex1"))
/*      */       {
/* 1314 */         return 12;
/*      */       }
/*      */       
/* 1317 */       if (name.equals("shadowcolor0") || name.equals("shadowcolor"))
/*      */       {
/* 1319 */         return 13;
/*      */       }
/*      */       
/* 1322 */       if (name.equals("shadowcolor1"))
/*      */       {
/* 1324 */         return 14;
/*      */       }
/*      */       
/* 1327 */       if (name.equals("noisetex"))
/*      */       {
/* 1329 */         return 15;
/*      */       }
/*      */     } 
/*      */     
/* 1333 */     if (stage == 1 || stage == 2) {
/*      */       
/* 1335 */       if (name.equals("colortex0") || name.equals("colortex0"))
/*      */       {
/* 1337 */         return 0;
/*      */       }
/*      */       
/* 1340 */       if (name.equals("colortex1") || name.equals("gdepth"))
/*      */       {
/* 1342 */         return 1;
/*      */       }
/*      */       
/* 1345 */       if (name.equals("colortex2") || name.equals("gnormal"))
/*      */       {
/* 1347 */         return 2;
/*      */       }
/*      */       
/* 1350 */       if (name.equals("colortex3") || name.equals("composite"))
/*      */       {
/* 1352 */         return 3;
/*      */       }
/*      */       
/* 1355 */       if (name.equals("shadowtex0") || name.equals("watershadow"))
/*      */       {
/* 1357 */         return 4;
/*      */       }
/*      */       
/* 1360 */       if (name.equals("shadow"))
/*      */       {
/* 1362 */         return waterShadowEnabled ? 5 : 4;
/*      */       }
/*      */       
/* 1365 */       if (name.equals("shadowtex1"))
/*      */       {
/* 1367 */         return 5;
/*      */       }
/*      */       
/* 1370 */       if (name.equals("depthtex0") || name.equals("gdepthtex"))
/*      */       {
/* 1372 */         return 6;
/*      */       }
/*      */       
/* 1375 */       if (name.equals("colortex4") || name.equals("gaux1"))
/*      */       {
/* 1377 */         return 7;
/*      */       }
/*      */       
/* 1380 */       if (name.equals("colortex5") || name.equals("gaux2"))
/*      */       {
/* 1382 */         return 8;
/*      */       }
/*      */       
/* 1385 */       if (name.equals("colortex6") || name.equals("gaux3"))
/*      */       {
/* 1387 */         return 9;
/*      */       }
/*      */       
/* 1390 */       if (name.equals("colortex7") || name.equals("gaux4"))
/*      */       {
/* 1392 */         return 10;
/*      */       }
/*      */       
/* 1395 */       if (name.equals("depthtex1"))
/*      */       {
/* 1397 */         return 11;
/*      */       }
/*      */       
/* 1400 */       if (name.equals("depthtex2"))
/*      */       {
/* 1402 */         return 12;
/*      */       }
/*      */       
/* 1405 */       if (name.equals("shadowcolor0") || name.equals("shadowcolor"))
/*      */       {
/* 1407 */         return 13;
/*      */       }
/*      */       
/* 1410 */       if (name.equals("shadowcolor1"))
/*      */       {
/* 1412 */         return 14;
/*      */       }
/*      */       
/* 1415 */       if (name.equals("noisetex"))
/*      */       {
/* 1417 */         return 15;
/*      */       }
/*      */     } 
/*      */     
/* 1421 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void bindCustomTextures(ICustomTexture[] cts) {
/* 1426 */     if (cts != null)
/*      */     {
/* 1428 */       for (int i = 0; i < cts.length; i++) {
/*      */         
/* 1430 */         ICustomTexture icustomtexture = cts[i];
/* 1431 */         GlStateManager.setActiveTexture(33984 + icustomtexture.getTextureUnit());
/* 1432 */         int j = icustomtexture.getTextureId();
/* 1433 */         int k = icustomtexture.getTarget();
/*      */         
/* 1435 */         if (k == 3553) {
/*      */           
/* 1437 */           GlStateManager.bindTexture(j);
/*      */         }
/*      */         else {
/*      */           
/* 1441 */           GL11.glBindTexture(k, j);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void resetCustomTextures() {
/* 1449 */     deleteCustomTextures(customTexturesGbuffers);
/* 1450 */     deleteCustomTextures(customTexturesComposite);
/* 1451 */     deleteCustomTextures(customTexturesDeferred);
/* 1452 */     customTexturesGbuffers = null;
/* 1453 */     customTexturesComposite = null;
/* 1454 */     customTexturesDeferred = null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void deleteCustomTextures(ICustomTexture[] cts) {
/* 1459 */     if (cts != null)
/*      */     {
/* 1461 */       for (int i = 0; i < cts.length; i++) {
/*      */         
/* 1463 */         ICustomTexture icustomtexture = cts[i];
/* 1464 */         icustomtexture.deleteTexture();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static ShaderOption[] getShaderPackOptions(String screenName) {
/* 1471 */     ShaderOption[] ashaderoption = (ShaderOption[])shaderPackOptions.clone();
/*      */     
/* 1473 */     if (shaderPackGuiScreens == null) {
/*      */       
/* 1475 */       if (shaderPackProfiles != null) {
/*      */         
/* 1477 */         ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderPackProfiles, ashaderoption);
/* 1478 */         ashaderoption = (ShaderOption[])Config.addObjectToArray((Object[])ashaderoption, shaderoptionprofile, 0);
/*      */       } 
/*      */       
/* 1481 */       ashaderoption = getVisibleOptions(ashaderoption);
/* 1482 */       return ashaderoption;
/*      */     } 
/*      */ 
/*      */     
/* 1486 */     String s = (screenName != null) ? ("screen." + screenName) : "screen";
/* 1487 */     ScreenShaderOptions screenshaderoptions = shaderPackGuiScreens.get(s);
/*      */     
/* 1489 */     if (screenshaderoptions == null)
/*      */     {
/* 1491 */       return new ShaderOption[0];
/*      */     }
/*      */ 
/*      */     
/* 1495 */     ShaderOption[] ashaderoption1 = screenshaderoptions.getShaderOptions();
/* 1496 */     List<ShaderOption> list = new ArrayList<>();
/*      */     
/* 1498 */     for (int i = 0; i < ashaderoption1.length; i++) {
/*      */       
/* 1500 */       ShaderOption shaderoption = ashaderoption1[i];
/*      */       
/* 1502 */       if (shaderoption == null) {
/*      */         
/* 1504 */         list.add((ShaderOption)null);
/*      */       }
/* 1506 */       else if (shaderoption instanceof net.optifine.shaders.config.ShaderOptionRest) {
/*      */         
/* 1508 */         ShaderOption[] ashaderoption2 = getShaderOptionsRest(shaderPackGuiScreens, ashaderoption);
/* 1509 */         list.addAll(Arrays.asList(ashaderoption2));
/*      */       }
/*      */       else {
/*      */         
/* 1513 */         list.add(shaderoption);
/*      */       } 
/*      */     } 
/*      */     
/* 1517 */     ShaderOption[] ashaderoption3 = list.<ShaderOption>toArray(new ShaderOption[list.size()]);
/* 1518 */     return ashaderoption3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getShaderPackColumns(String screenName, int def) {
/* 1525 */     String s = (screenName != null) ? ("screen." + screenName) : "screen";
/*      */     
/* 1527 */     if (shaderPackGuiScreens == null)
/*      */     {
/* 1529 */       return def;
/*      */     }
/*      */ 
/*      */     
/* 1533 */     ScreenShaderOptions screenshaderoptions = shaderPackGuiScreens.get(s);
/* 1534 */     return (screenshaderoptions == null) ? def : screenshaderoptions.getColumns();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ShaderOption[] getShaderOptionsRest(Map<String, ScreenShaderOptions> mapScreens, ShaderOption[] ops) {
/* 1540 */     Set<String> set = new HashSet<>();
/*      */     
/* 1542 */     for (String s : mapScreens.keySet()) {
/*      */       
/* 1544 */       ScreenShaderOptions screenshaderoptions = mapScreens.get(s);
/* 1545 */       ShaderOption[] ashaderoption = screenshaderoptions.getShaderOptions();
/*      */       
/* 1547 */       for (int i = 0; i < ashaderoption.length; i++) {
/*      */         
/* 1549 */         ShaderOption shaderoption = ashaderoption[i];
/*      */         
/* 1551 */         if (shaderoption != null)
/*      */         {
/* 1553 */           set.add(shaderoption.getName());
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1558 */     List<ShaderOption> list = new ArrayList<>();
/*      */     
/* 1560 */     for (int j = 0; j < ops.length; j++) {
/*      */       
/* 1562 */       ShaderOption shaderoption1 = ops[j];
/*      */       
/* 1564 */       if (shaderoption1.isVisible()) {
/*      */         
/* 1566 */         String s1 = shaderoption1.getName();
/*      */         
/* 1568 */         if (!set.contains(s1))
/*      */         {
/* 1570 */           list.add(shaderoption1);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1575 */     ShaderOption[] ashaderoption1 = list.<ShaderOption>toArray(new ShaderOption[list.size()]);
/* 1576 */     return ashaderoption1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static ShaderOption getShaderOption(String name) {
/* 1581 */     return ShaderUtils.getShaderOption(name, shaderPackOptions);
/*      */   }
/*      */ 
/*      */   
/*      */   public static ShaderOption[] getShaderPackOptions() {
/* 1586 */     return shaderPackOptions;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isShaderPackOptionSlider(String name) {
/* 1591 */     return (shaderPackOptionSliders == null) ? false : shaderPackOptionSliders.contains(name);
/*      */   }
/*      */ 
/*      */   
/*      */   private static ShaderOption[] getVisibleOptions(ShaderOption[] ops) {
/* 1596 */     List<ShaderOption> list = new ArrayList<>();
/*      */     
/* 1598 */     for (int i = 0; i < ops.length; i++) {
/*      */       
/* 1600 */       ShaderOption shaderoption = ops[i];
/*      */       
/* 1602 */       if (shaderoption.isVisible())
/*      */       {
/* 1604 */         list.add(shaderoption);
/*      */       }
/*      */     } 
/*      */     
/* 1608 */     ShaderOption[] ashaderoption = list.<ShaderOption>toArray(new ShaderOption[list.size()]);
/* 1609 */     return ashaderoption;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void saveShaderPackOptions() {
/* 1614 */     saveShaderPackOptions(shaderPackOptions, shaderPack);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void saveShaderPackOptions(ShaderOption[] sos, IShaderPack sp) {
/* 1619 */     PropertiesOrdered propertiesOrdered = new PropertiesOrdered();
/*      */     
/* 1621 */     if (shaderPackOptions != null)
/*      */     {
/* 1623 */       for (int i = 0; i < sos.length; i++) {
/*      */         
/* 1625 */         ShaderOption shaderoption = sos[i];
/*      */         
/* 1627 */         if (shaderoption.isChanged() && shaderoption.isEnabled())
/*      */         {
/* 1629 */           propertiesOrdered.setProperty(shaderoption.getName(), shaderoption.getValue());
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1636 */       saveOptionProperties(sp, (Properties)propertiesOrdered);
/*      */     }
/* 1638 */     catch (IOException ioexception) {
/*      */       
/* 1640 */       Config.warn("[Shaders] Error saving configuration for " + shaderPack.getName());
/* 1641 */       ioexception.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void saveOptionProperties(IShaderPack sp, Properties props) throws IOException {
/* 1647 */     String s = "shaderpacks/" + sp.getName() + ".txt";
/* 1648 */     File file1 = new File((Minecraft.getMinecraft()).mcDataDir, s);
/*      */     
/* 1650 */     if (props.isEmpty()) {
/*      */       
/* 1652 */       file1.delete();
/*      */     }
/*      */     else {
/*      */       
/* 1656 */       FileOutputStream fileoutputstream = new FileOutputStream(file1);
/* 1657 */       props.store(fileoutputstream, (String)null);
/* 1658 */       fileoutputstream.flush();
/* 1659 */       fileoutputstream.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ShaderOption[] loadShaderPackOptions() {
/*      */     try {
/* 1667 */       String[] astring = programs.getProgramNames();
/* 1668 */       ShaderOption[] ashaderoption = ShaderPackParser.parseShaderPackOptions(shaderPack, astring, shaderPackDimensions);
/* 1669 */       Properties properties = loadOptionProperties(shaderPack);
/*      */       
/* 1671 */       for (int i = 0; i < ashaderoption.length; i++) {
/*      */         
/* 1673 */         ShaderOption shaderoption = ashaderoption[i];
/* 1674 */         String s = properties.getProperty(shaderoption.getName());
/*      */         
/* 1676 */         if (s != null) {
/*      */           
/* 1678 */           shaderoption.resetValue();
/*      */           
/* 1680 */           if (!shaderoption.setValue(s))
/*      */           {
/* 1682 */             Config.warn("[Shaders] Invalid value, option: " + shaderoption.getName() + ", value: " + s);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1687 */       return ashaderoption;
/*      */     }
/* 1689 */     catch (IOException ioexception) {
/*      */       
/* 1691 */       Config.warn("[Shaders] Error reading configuration for " + shaderPack.getName());
/* 1692 */       ioexception.printStackTrace();
/* 1693 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Properties loadOptionProperties(IShaderPack sp) throws IOException {
/* 1699 */     PropertiesOrdered propertiesOrdered = new PropertiesOrdered();
/* 1700 */     String s = "shaderpacks/" + sp.getName() + ".txt";
/* 1701 */     File file1 = new File((Minecraft.getMinecraft()).mcDataDir, s);
/*      */     
/* 1703 */     if (file1.exists() && file1.isFile() && file1.canRead()) {
/*      */       
/* 1705 */       FileInputStream fileinputstream = new FileInputStream(file1);
/* 1706 */       propertiesOrdered.load(fileinputstream);
/* 1707 */       fileinputstream.close();
/* 1708 */       return (Properties)propertiesOrdered;
/*      */     } 
/*      */ 
/*      */     
/* 1712 */     return (Properties)propertiesOrdered;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static ShaderOption[] getChangedOptions(ShaderOption[] ops) {
/* 1718 */     List<ShaderOption> list = new ArrayList<>();
/*      */     
/* 1720 */     for (int i = 0; i < ops.length; i++) {
/*      */       
/* 1722 */       ShaderOption shaderoption = ops[i];
/*      */       
/* 1724 */       if (shaderoption.isEnabled() && shaderoption.isChanged())
/*      */       {
/* 1726 */         list.add(shaderoption);
/*      */       }
/*      */     } 
/*      */     
/* 1730 */     ShaderOption[] ashaderoption = list.<ShaderOption>toArray(new ShaderOption[list.size()]);
/* 1731 */     return ashaderoption;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String applyOptions(String line, ShaderOption[] ops) {
/* 1736 */     if (ops != null && ops.length > 0) {
/*      */       
/* 1738 */       for (int i = 0; i < ops.length; i++) {
/*      */         
/* 1740 */         ShaderOption shaderoption = ops[i];
/*      */         
/* 1742 */         if (shaderoption.matchesLine(line)) {
/*      */           
/* 1744 */           line = shaderoption.getSourceLine();
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1749 */       return line;
/*      */     } 
/*      */ 
/*      */     
/* 1753 */     return line;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static ArrayList listOfShaders() {
/* 1759 */     ArrayList<String> arraylist = new ArrayList<>();
/* 1760 */     arraylist.add("OFF");
/* 1761 */     arraylist.add("(internal)");
/* 1762 */     int i = arraylist.size();
/*      */ 
/*      */     
/*      */     try {
/* 1766 */       if (!shaderPacksDir.exists())
/*      */       {
/* 1768 */         shaderPacksDir.mkdir();
/*      */       }
/*      */       
/* 1771 */       File[] afile = shaderPacksDir.listFiles();
/*      */       
/* 1773 */       for (int j = 0; j < afile.length; j++) {
/*      */         
/* 1775 */         File file1 = afile[j];
/* 1776 */         String s = file1.getName();
/*      */         
/* 1778 */         if (file1.isDirectory()) {
/*      */           
/* 1780 */           if (!s.equals("debug"))
/*      */           {
/* 1782 */             File file2 = new File(file1, "shaders");
/*      */             
/* 1784 */             if (file2.exists() && file2.isDirectory())
/*      */             {
/* 1786 */               arraylist.add(s);
/*      */             }
/*      */           }
/*      */         
/* 1790 */         } else if (file1.isFile() && s.toLowerCase().endsWith(".zip")) {
/*      */           
/* 1792 */           arraylist.add(s);
/*      */         }
/*      */       
/*      */       } 
/* 1796 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1801 */     List<String> list = arraylist.subList(i, arraylist.size());
/* 1802 */     Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
/* 1803 */     return arraylist;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int checkFramebufferStatus(String location) {
/* 1808 */     int i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
/*      */     
/* 1810 */     if (i != 36053)
/*      */     {
/* 1812 */       System.err.format("FramebufferStatus 0x%04X at %s\n", new Object[] { Integer.valueOf(i), location });
/*      */     }
/*      */     
/* 1815 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int checkGLError(String location) {
/* 1820 */     int i = GlStateManager.glGetError();
/*      */     
/* 1822 */     if (i != 0 && GlErrors.isEnabled(i)) {
/*      */       
/* 1824 */       String s = Config.getGlErrorString(i);
/* 1825 */       String s1 = getErrorInfo(i, location);
/* 1826 */       String s2 = String.format("OpenGL error: %s (%s)%s, at: %s", new Object[] { Integer.valueOf(i), s, s1, location });
/* 1827 */       SMCLog.severe(s2);
/*      */       
/* 1829 */       if (Config.isShowGlErrors() && TimedEvent.isActive("ShowGlErrorShaders", 10000L)) {
/*      */         
/* 1831 */         String s3 = I18n.format("of.message.openglError", new Object[] { Integer.valueOf(i), s });
/* 1832 */         printChat(s3);
/*      */       } 
/*      */     } 
/*      */     
/* 1836 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getErrorInfo(int errorCode, String location) {
/* 1841 */     StringBuilder stringbuilder = new StringBuilder();
/*      */     
/* 1843 */     if (errorCode == 1286) {
/*      */       
/* 1845 */       int i = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
/* 1846 */       String s = getFramebufferStatusText(i);
/* 1847 */       String s1 = ", fbStatus: " + i + " (" + s + ")";
/* 1848 */       stringbuilder.append(s1);
/*      */     } 
/*      */     
/* 1851 */     String s2 = activeProgram.getName();
/*      */     
/* 1853 */     if (s2.isEmpty())
/*      */     {
/* 1855 */       s2 = "none";
/*      */     }
/*      */     
/* 1858 */     stringbuilder.append(", program: " + s2);
/* 1859 */     Program program = getProgramById(activeProgramID);
/*      */     
/* 1861 */     if (program != activeProgram) {
/*      */       
/* 1863 */       String s3 = program.getName();
/*      */       
/* 1865 */       if (s3.isEmpty())
/*      */       {
/* 1867 */         s3 = "none";
/*      */       }
/*      */       
/* 1870 */       stringbuilder.append(" (" + s3 + ")");
/*      */     } 
/*      */     
/* 1873 */     if (location.equals("setDrawBuffers"))
/*      */     {
/* 1875 */       stringbuilder.append(", drawBuffers: " + activeProgram.getDrawBufSettings());
/*      */     }
/*      */     
/* 1878 */     return stringbuilder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private static Program getProgramById(int programID) {
/* 1883 */     for (int i = 0; i < ProgramsAll.length; i++) {
/*      */       
/* 1885 */       Program program = ProgramsAll[i];
/*      */       
/* 1887 */       if (program.getId() == programID)
/*      */       {
/* 1889 */         return program;
/*      */       }
/*      */     } 
/*      */     
/* 1893 */     return ProgramNone;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getFramebufferStatusText(int fbStatusCode) {
/* 1898 */     switch (fbStatusCode) {
/*      */       
/*      */       case 33305:
/* 1901 */         return "Undefined";
/*      */       
/*      */       case 36053:
/* 1904 */         return "Complete";
/*      */       
/*      */       case 36054:
/* 1907 */         return "Incomplete attachment";
/*      */       
/*      */       case 36055:
/* 1910 */         return "Incomplete missing attachment";
/*      */       
/*      */       case 36059:
/* 1913 */         return "Incomplete draw buffer";
/*      */       
/*      */       case 36060:
/* 1916 */         return "Incomplete read buffer";
/*      */       
/*      */       case 36061:
/* 1919 */         return "Unsupported";
/*      */       
/*      */       case 36182:
/* 1922 */         return "Incomplete multisample";
/*      */       
/*      */       case 36264:
/* 1925 */         return "Incomplete layer targets";
/*      */     } 
/*      */     
/* 1928 */     return "Unknown";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void printChat(String str) {
/* 1934 */     mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentText(str));
/*      */   }
/*      */ 
/*      */   
/*      */   private static void printChatAndLogError(String str) {
/* 1939 */     SMCLog.severe(str);
/* 1940 */     mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentText(str));
/*      */   }
/*      */ 
/*      */   
/*      */   public static void printIntBuffer(String title, IntBuffer buf) {
/* 1945 */     StringBuilder stringbuilder = new StringBuilder(128);
/* 1946 */     stringbuilder.append(title).append(" [pos ").append(buf.position()).append(" lim ").append(buf.limit()).append(" cap ").append(buf.capacity()).append(" :");
/* 1947 */     int i = buf.limit();
/*      */     
/* 1949 */     for (int j = 0; j < i; j++)
/*      */     {
/* 1951 */       stringbuilder.append(" ").append(buf.get(j));
/*      */     }
/*      */     
/* 1954 */     stringbuilder.append("]");
/* 1955 */     SMCLog.info(stringbuilder.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void startup(Minecraft mc) {
/* 1960 */     checkShadersModInstalled();
/* 1961 */     mc = mc;
/* 1962 */     mc = Minecraft.getMinecraft();
/* 1963 */     capabilities = GLContext.getCapabilities();
/* 1964 */     glVersionString = GL11.glGetString(7938);
/* 1965 */     glVendorString = GL11.glGetString(7936);
/* 1966 */     glRendererString = GL11.glGetString(7937);
/* 1967 */     SMCLog.info("OpenGL Version: " + glVersionString);
/* 1968 */     SMCLog.info("Vendor:  " + glVendorString);
/* 1969 */     SMCLog.info("Renderer: " + glRendererString);
/* 1970 */     SMCLog.info("Capabilities: " + (capabilities.OpenGL20 ? " 2.0 " : " - ") + (capabilities.OpenGL21 ? " 2.1 " : " - ") + (capabilities.OpenGL30 ? " 3.0 " : " - ") + (capabilities.OpenGL32 ? " 3.2 " : " - ") + (capabilities.OpenGL40 ? " 4.0 " : " - "));
/* 1971 */     SMCLog.info("GL_MAX_DRAW_BUFFERS: " + GL11.glGetInteger(34852));
/* 1972 */     SMCLog.info("GL_MAX_COLOR_ATTACHMENTS_EXT: " + GL11.glGetInteger(36063));
/* 1973 */     SMCLog.info("GL_MAX_TEXTURE_IMAGE_UNITS: " + GL11.glGetInteger(34930));
/* 1974 */     hasGlGenMipmap = capabilities.OpenGL30;
/* 1975 */     loadConfig();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void updateBlockLightLevel() {
/* 1980 */     if (isOldLighting()) {
/*      */       
/* 1982 */       blockLightLevel05 = 0.5F;
/* 1983 */       blockLightLevel06 = 0.6F;
/* 1984 */       blockLightLevel08 = 0.8F;
/*      */     }
/*      */     else {
/*      */       
/* 1988 */       blockLightLevel05 = 1.0F;
/* 1989 */       blockLightLevel06 = 1.0F;
/* 1990 */       blockLightLevel08 = 1.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOldHandLight() {
/* 1996 */     return !configOldHandLight.isDefault() ? configOldHandLight.isTrue() : (!shaderPackOldHandLight.isDefault() ? shaderPackOldHandLight.isTrue() : true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isDynamicHandLight() {
/* 2001 */     return !shaderPackDynamicHandLight.isDefault() ? shaderPackDynamicHandLight.isTrue() : true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOldLighting() {
/* 2006 */     return !configOldLighting.isDefault() ? configOldLighting.isTrue() : (!shaderPackOldLighting.isDefault() ? shaderPackOldLighting.isTrue() : true);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRenderShadowTranslucent() {
/* 2011 */     return !shaderPackShadowTranslucent.isFalse();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isUnderwaterOverlay() {
/* 2016 */     return !shaderPackUnderwaterOverlay.isFalse();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSun() {
/* 2021 */     return !shaderPackSun.isFalse();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMoon() {
/* 2026 */     return !shaderPackMoon.isFalse();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isVignette() {
/* 2031 */     return !shaderPackVignette.isFalse();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRenderBackFace(EnumWorldBlockLayer blockLayerIn) {
/* 2036 */     switch (blockLayerIn) {
/*      */       
/*      */       case GBUFFERS:
/* 2039 */         return shaderPackBackFaceSolid.isTrue();
/*      */       
/*      */       case DEFERRED:
/* 2042 */         return shaderPackBackFaceCutout.isTrue();
/*      */       
/*      */       case COMPOSITE:
/* 2045 */         return shaderPackBackFaceCutoutMipped.isTrue();
/*      */       
/*      */       case SHADOW:
/* 2048 */         return shaderPackBackFaceTranslucent.isTrue();
/*      */     } 
/*      */     
/* 2051 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isRainDepth() {
/* 2057 */     return shaderPackRainDepth.isTrue();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isBeaconBeamDepth() {
/* 2062 */     return shaderPackBeaconBeamDepth.isTrue();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSeparateAo() {
/* 2067 */     return shaderPackSeparateAo.isTrue();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFrustumCulling() {
/* 2072 */     return !shaderPackFrustumCulling.isFalse();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void init() {
/*      */     boolean flag;
/* 2079 */     if (!isInitializedOnce) {
/*      */       
/* 2081 */       isInitializedOnce = true;
/* 2082 */       flag = true;
/*      */     }
/*      */     else {
/*      */       
/* 2086 */       flag = false;
/*      */     } 
/*      */     
/* 2089 */     if (!isShaderPackInitialized) {
/*      */       
/* 2091 */       checkGLError("Shaders.init pre");
/*      */       
/* 2093 */       if (getShaderPackName() != null);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2098 */       if (!capabilities.OpenGL20)
/*      */       {
/* 2100 */         printChatAndLogError("No OpenGL 2.0");
/*      */       }
/*      */       
/* 2103 */       if (!capabilities.GL_EXT_framebuffer_object)
/*      */       {
/* 2105 */         printChatAndLogError("No EXT_framebuffer_object");
/*      */       }
/*      */       
/* 2108 */       dfbDrawBuffers.position(0).limit(8);
/* 2109 */       dfbColorTextures.position(0).limit(16);
/* 2110 */       dfbDepthTextures.position(0).limit(3);
/* 2111 */       sfbDrawBuffers.position(0).limit(8);
/* 2112 */       sfbDepthTextures.position(0).limit(2);
/* 2113 */       sfbColorTextures.position(0).limit(8);
/* 2114 */       usedColorBuffers = 4;
/* 2115 */       usedDepthBuffers = 1;
/* 2116 */       usedShadowColorBuffers = 0;
/* 2117 */       usedShadowDepthBuffers = 0;
/* 2118 */       usedColorAttachs = 1;
/* 2119 */       usedDrawBuffers = 1;
/* 2120 */       Arrays.fill(gbuffersFormat, 6408);
/* 2121 */       Arrays.fill(gbuffersClear, true);
/* 2122 */       Arrays.fill((Object[])gbuffersClearColor, (Object)null);
/* 2123 */       Arrays.fill(shadowHardwareFilteringEnabled, false);
/* 2124 */       Arrays.fill(shadowMipmapEnabled, false);
/* 2125 */       Arrays.fill(shadowFilterNearest, false);
/* 2126 */       Arrays.fill(shadowColorMipmapEnabled, false);
/* 2127 */       Arrays.fill(shadowColorFilterNearest, false);
/* 2128 */       centerDepthSmoothEnabled = false;
/* 2129 */       noiseTextureEnabled = false;
/* 2130 */       sunPathRotation = 0.0F;
/* 2131 */       shadowIntervalSize = 2.0F;
/* 2132 */       shadowMapWidth = 1024;
/* 2133 */       shadowMapHeight = 1024;
/* 2134 */       spShadowMapWidth = 1024;
/* 2135 */       spShadowMapHeight = 1024;
/* 2136 */       shadowMapFOV = 90.0F;
/* 2137 */       shadowMapHalfPlane = 160.0F;
/* 2138 */       shadowMapIsOrtho = true;
/* 2139 */       shadowDistanceRenderMul = -1.0F;
/* 2140 */       aoLevel = -1.0F;
/* 2141 */       useEntityAttrib = false;
/* 2142 */       useMidTexCoordAttrib = false;
/* 2143 */       useTangentAttrib = false;
/* 2144 */       waterShadowEnabled = false;
/* 2145 */       hasGeometryShaders = false;
/* 2146 */       updateBlockLightLevel();
/* 2147 */       Smoother.resetValues();
/* 2148 */       shaderUniforms.reset();
/*      */       
/* 2150 */       if (customUniforms != null)
/*      */       {
/* 2152 */         customUniforms.reset();
/*      */       }
/*      */       
/* 2155 */       ShaderProfile shaderprofile = ShaderUtils.detectProfile(shaderPackProfiles, shaderPackOptions, false);
/* 2156 */       String s = "";
/*      */       
/* 2158 */       if (currentWorld != null) {
/*      */         
/* 2160 */         int i = currentWorld.provider.getDimensionId();
/*      */         
/* 2162 */         if (shaderPackDimensions.contains(Integer.valueOf(i)))
/*      */         {
/* 2164 */           s = "world" + i + "/";
/*      */         }
/*      */       } 
/*      */       
/* 2168 */       for (int k = 0; k < ProgramsAll.length; k++) {
/*      */         
/* 2170 */         Program program = ProgramsAll[k];
/* 2171 */         program.resetId();
/* 2172 */         program.resetConfiguration();
/*      */         
/* 2174 */         if (program.getProgramStage() != ProgramStage.NONE) {
/*      */           
/* 2176 */           String s1 = program.getName();
/* 2177 */           String s2 = s + s1;
/* 2178 */           boolean flag1 = true;
/*      */           
/* 2180 */           if (shaderPackProgramConditions.containsKey(s2))
/*      */           {
/* 2182 */             flag1 = (flag1 && ((IExpressionBool)shaderPackProgramConditions.get(s2)).eval());
/*      */           }
/*      */           
/* 2185 */           if (shaderprofile != null)
/*      */           {
/* 2187 */             flag1 = (flag1 && !shaderprofile.isProgramDisabled(s2));
/*      */           }
/*      */           
/* 2190 */           if (!flag1) {
/*      */             
/* 2192 */             SMCLog.info("Program disabled: " + s2);
/* 2193 */             s1 = "<disabled>";
/* 2194 */             s2 = s + s1;
/*      */           } 
/*      */           
/* 2197 */           String s3 = "/shaders/" + s2;
/* 2198 */           String s4 = s3 + ".vsh";
/* 2199 */           String s5 = s3 + ".gsh";
/* 2200 */           String s6 = s3 + ".fsh";
/* 2201 */           setupProgram(program, s4, s5, s6);
/* 2202 */           int j = program.getId();
/*      */           
/* 2204 */           if (j > 0)
/*      */           {
/* 2206 */             SMCLog.info("Program loaded: " + s2);
/*      */           }
/*      */           
/* 2209 */           initDrawBuffers(program);
/* 2210 */           updateToggleBuffers(program);
/*      */         } 
/*      */       } 
/*      */       
/* 2214 */       hasDeferredPrograms = false;
/*      */       
/* 2216 */       for (int l = 0; l < ProgramsDeferred.length; l++) {
/*      */         
/* 2218 */         if (ProgramsDeferred[l].getId() != 0) {
/*      */           
/* 2220 */           hasDeferredPrograms = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 2225 */       usedColorAttachs = usedColorBuffers;
/* 2226 */       shadowPassInterval = (usedShadowDepthBuffers > 0) ? 1 : 0;
/* 2227 */       shouldSkipDefaultShadow = (usedShadowDepthBuffers > 0);
/* 2228 */       SMCLog.info("usedColorBuffers: " + usedColorBuffers);
/* 2229 */       SMCLog.info("usedDepthBuffers: " + usedDepthBuffers);
/* 2230 */       SMCLog.info("usedShadowColorBuffers: " + usedShadowColorBuffers);
/* 2231 */       SMCLog.info("usedShadowDepthBuffers: " + usedShadowDepthBuffers);
/* 2232 */       SMCLog.info("usedColorAttachs: " + usedColorAttachs);
/* 2233 */       SMCLog.info("usedDrawBuffers: " + usedDrawBuffers);
/* 2234 */       dfbDrawBuffers.position(0).limit(usedDrawBuffers);
/* 2235 */       dfbColorTextures.position(0).limit(usedColorBuffers * 2);
/* 2236 */       dfbColorTexturesFlip.reset();
/*      */       
/* 2238 */       for (int i1 = 0; i1 < usedDrawBuffers; i1++)
/*      */       {
/* 2240 */         dfbDrawBuffers.put(i1, 36064 + i1);
/*      */       }
/*      */       
/* 2243 */       int j1 = GL11.glGetInteger(34852);
/*      */       
/* 2245 */       if (usedDrawBuffers > j1)
/*      */       {
/* 2247 */         printChatAndLogError("[Shaders] Error: Not enough draw buffers, needed: " + usedDrawBuffers + ", available: " + j1);
/*      */       }
/*      */       
/* 2250 */       sfbDrawBuffers.position(0).limit(usedShadowColorBuffers);
/*      */       
/* 2252 */       for (int k1 = 0; k1 < usedShadowColorBuffers; k1++)
/*      */       {
/* 2254 */         sfbDrawBuffers.put(k1, 36064 + k1);
/*      */       }
/*      */       
/* 2257 */       for (int l1 = 0; l1 < ProgramsAll.length; l1++) {
/*      */         
/* 2259 */         Program program1 = ProgramsAll[l1];
/*      */         
/*      */         Program program2;
/* 2262 */         for (program2 = program1; program2.getId() == 0 && program2.getProgramBackup() != program2; program2 = program2.getProgramBackup());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2267 */         if (program2 != program1 && program1 != ProgramShadow)
/*      */         {
/* 2269 */           program1.copyFrom(program2);
/*      */         }
/*      */       } 
/*      */       
/* 2273 */       resize();
/* 2274 */       resizeShadow();
/*      */       
/* 2276 */       if (noiseTextureEnabled)
/*      */       {
/* 2278 */         setupNoiseTexture();
/*      */       }
/*      */       
/* 2281 */       if (defaultTexture == null)
/*      */       {
/* 2283 */         defaultTexture = ShadersTex.createDefaultTexture();
/*      */       }
/*      */       
/* 2286 */       GlStateManager.pushMatrix();
/* 2287 */       GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
/* 2288 */       preCelestialRotate();
/* 2289 */       postCelestialRotate();
/* 2290 */       GlStateManager.popMatrix();
/* 2291 */       isShaderPackInitialized = true;
/* 2292 */       loadEntityDataMap();
/* 2293 */       resetDisplayLists();
/*      */       
/* 2295 */       if (!flag);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2300 */       checkGLError("Shaders.init");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initDrawBuffers(Program p) {
/* 2306 */     int i = GL11.glGetInteger(34852);
/* 2307 */     Arrays.fill(p.getToggleColorTextures(), false);
/*      */     
/* 2309 */     if (p == ProgramFinal) {
/*      */       
/* 2311 */       p.setDrawBuffers((IntBuffer)null);
/*      */     }
/* 2313 */     else if (p.getId() == 0) {
/*      */       
/* 2315 */       if (p == ProgramShadow)
/*      */       {
/* 2317 */         p.setDrawBuffers(drawBuffersNone);
/*      */       }
/*      */       else
/*      */       {
/* 2321 */         p.setDrawBuffers(drawBuffersColorAtt0);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 2326 */       String s = p.getDrawBufSettings();
/*      */       
/* 2328 */       if (s == null) {
/*      */         
/* 2330 */         if (p != ProgramShadow && p != ProgramShadowSolid && p != ProgramShadowCutout)
/*      */         {
/* 2332 */           p.setDrawBuffers(dfbDrawBuffers);
/* 2333 */           usedDrawBuffers = usedColorBuffers;
/* 2334 */           Arrays.fill(p.getToggleColorTextures(), 0, usedColorBuffers, true);
/*      */         }
/*      */         else
/*      */         {
/* 2338 */           p.setDrawBuffers(sfbDrawBuffers);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 2343 */         IntBuffer intbuffer = p.getDrawBuffersBuffer();
/* 2344 */         int j = s.length();
/* 2345 */         usedDrawBuffers = Math.max(usedDrawBuffers, j);
/* 2346 */         j = Math.min(j, i);
/* 2347 */         p.setDrawBuffers(intbuffer);
/* 2348 */         intbuffer.limit(j);
/*      */         
/* 2350 */         for (int k = 0; k < j; k++) {
/*      */           
/* 2352 */           int l = getDrawBuffer(p, s, k);
/* 2353 */           intbuffer.put(k, l);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getDrawBuffer(Program p, String str, int ic) {
/* 2361 */     int i = 0;
/*      */     
/* 2363 */     if (ic >= str.length())
/*      */     {
/* 2365 */       return i;
/*      */     }
/*      */ 
/*      */     
/* 2369 */     int j = str.charAt(ic) - 48;
/*      */     
/* 2371 */     if (p == ProgramShadow) {
/*      */       
/* 2373 */       if (j >= 0 && j <= 1) {
/*      */         
/* 2375 */         i = j + 36064;
/* 2376 */         usedShadowColorBuffers = Math.max(usedShadowColorBuffers, j);
/*      */       } 
/*      */       
/* 2379 */       return i;
/*      */     } 
/*      */ 
/*      */     
/* 2383 */     if (j >= 0 && j <= 7) {
/*      */       
/* 2385 */       p.getToggleColorTextures()[j] = true;
/* 2386 */       i = j + 36064;
/* 2387 */       usedColorAttachs = Math.max(usedColorAttachs, j);
/* 2388 */       usedColorBuffers = Math.max(usedColorBuffers, j);
/*      */     } 
/*      */     
/* 2391 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateToggleBuffers(Program p) {
/* 2398 */     boolean[] aboolean = p.getToggleColorTextures();
/* 2399 */     Boolean[] aboolean1 = p.getBuffersFlip();
/*      */     
/* 2401 */     for (int i = 0; i < aboolean1.length; i++) {
/*      */       
/* 2403 */       Boolean obool = aboolean1[i];
/*      */       
/* 2405 */       if (obool != null)
/*      */       {
/* 2407 */         aboolean[i] = obool.booleanValue();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void resetDisplayLists() {
/* 2414 */     SMCLog.info("Reset model renderers");
/* 2415 */     countResetDisplayLists++;
/* 2416 */     SMCLog.info("Reset world renderers");
/* 2417 */     mc.renderGlobal.loadRenderers();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setupProgram(Program program, String vShaderPath, String gShaderPath, String fShaderPath) {
/* 2422 */     checkGLError("pre setupProgram");
/* 2423 */     int i = ARBShaderObjects.glCreateProgramObjectARB();
/* 2424 */     checkGLError("create");
/*      */     
/* 2426 */     if (i != 0) {
/*      */       
/* 2428 */       progUseEntityAttrib = false;
/* 2429 */       progUseMidTexCoordAttrib = false;
/* 2430 */       progUseTangentAttrib = false;
/* 2431 */       int j = createVertShader(program, vShaderPath);
/* 2432 */       int k = createGeomShader(program, gShaderPath);
/* 2433 */       int l = createFragShader(program, fShaderPath);
/* 2434 */       checkGLError("create");
/*      */       
/* 2436 */       if (j == 0 && k == 0 && l == 0) {
/*      */         
/* 2438 */         ARBShaderObjects.glDeleteObjectARB(i);
/* 2439 */         i = 0;
/* 2440 */         program.resetId();
/*      */       }
/*      */       else {
/*      */         
/* 2444 */         if (j != 0) {
/*      */           
/* 2446 */           ARBShaderObjects.glAttachObjectARB(i, j);
/* 2447 */           checkGLError("attach");
/*      */         } 
/*      */         
/* 2450 */         if (k != 0) {
/*      */           
/* 2452 */           ARBShaderObjects.glAttachObjectARB(i, k);
/* 2453 */           checkGLError("attach");
/*      */           
/* 2455 */           if (progArbGeometryShader4) {
/*      */             
/* 2457 */             ARBGeometryShader4.glProgramParameteriARB(i, 36315, 4);
/* 2458 */             ARBGeometryShader4.glProgramParameteriARB(i, 36316, 5);
/* 2459 */             ARBGeometryShader4.glProgramParameteriARB(i, 36314, progMaxVerticesOut);
/* 2460 */             checkGLError("arbGeometryShader4");
/*      */           } 
/*      */           
/* 2463 */           hasGeometryShaders = true;
/*      */         } 
/*      */         
/* 2466 */         if (l != 0) {
/*      */           
/* 2468 */           ARBShaderObjects.glAttachObjectARB(i, l);
/* 2469 */           checkGLError("attach");
/*      */         } 
/*      */         
/* 2472 */         if (progUseEntityAttrib) {
/*      */           
/* 2474 */           ARBVertexShader.glBindAttribLocationARB(i, entityAttrib, "mc_Entity");
/* 2475 */           checkGLError("mc_Entity");
/*      */         } 
/*      */         
/* 2478 */         if (progUseMidTexCoordAttrib) {
/*      */           
/* 2480 */           ARBVertexShader.glBindAttribLocationARB(i, midTexCoordAttrib, "mc_midTexCoord");
/* 2481 */           checkGLError("mc_midTexCoord");
/*      */         } 
/*      */         
/* 2484 */         if (progUseTangentAttrib) {
/*      */           
/* 2486 */           ARBVertexShader.glBindAttribLocationARB(i, tangentAttrib, "at_tangent");
/* 2487 */           checkGLError("at_tangent");
/*      */         } 
/*      */         
/* 2490 */         ARBShaderObjects.glLinkProgramARB(i);
/*      */         
/* 2492 */         if (GL20.glGetProgrami(i, 35714) != 1)
/*      */         {
/* 2494 */           SMCLog.severe("Error linking program: " + i + " (" + program.getName() + ")");
/*      */         }
/*      */         
/* 2497 */         printLogInfo(i, program.getName());
/*      */         
/* 2499 */         if (j != 0) {
/*      */           
/* 2501 */           ARBShaderObjects.glDetachObjectARB(i, j);
/* 2502 */           ARBShaderObjects.glDeleteObjectARB(j);
/*      */         } 
/*      */         
/* 2505 */         if (k != 0) {
/*      */           
/* 2507 */           ARBShaderObjects.glDetachObjectARB(i, k);
/* 2508 */           ARBShaderObjects.glDeleteObjectARB(k);
/*      */         } 
/*      */         
/* 2511 */         if (l != 0) {
/*      */           
/* 2513 */           ARBShaderObjects.glDetachObjectARB(i, l);
/* 2514 */           ARBShaderObjects.glDeleteObjectARB(l);
/*      */         } 
/*      */         
/* 2517 */         program.setId(i);
/* 2518 */         program.setRef(i);
/* 2519 */         useProgram(program);
/* 2520 */         ARBShaderObjects.glValidateProgramARB(i);
/* 2521 */         useProgram(ProgramNone);
/* 2522 */         printLogInfo(i, program.getName());
/* 2523 */         int i1 = GL20.glGetProgrami(i, 35715);
/*      */         
/* 2525 */         if (i1 != 1) {
/*      */           
/* 2527 */           String s = "\"";
/* 2528 */           printChatAndLogError("[Shaders] Error: Invalid program " + s + program.getName() + s);
/* 2529 */           ARBShaderObjects.glDeleteObjectARB(i);
/* 2530 */           i = 0;
/* 2531 */           program.resetId();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int createVertShader(Program program, String filename) {
/* 2539 */     int i = ARBShaderObjects.glCreateShaderObjectARB(35633);
/*      */     
/* 2541 */     if (i == 0)
/*      */     {
/* 2543 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 2547 */     StringBuilder stringbuilder = new StringBuilder(131072);
/* 2548 */     BufferedReader bufferedreader = null;
/*      */ 
/*      */     
/*      */     try {
/* 2552 */       bufferedreader = new BufferedReader(getShaderReader(filename));
/*      */     }
/* 2554 */     catch (Exception var10) {
/*      */       
/* 2556 */       ARBShaderObjects.glDeleteObjectARB(i);
/* 2557 */       return 0;
/*      */     } 
/*      */     
/* 2560 */     ShaderOption[] ashaderoption = getChangedOptions(shaderPackOptions);
/* 2561 */     List<String> list = new ArrayList<>();
/*      */     
/* 2563 */     if (bufferedreader != null) {
/*      */       
/*      */       try {
/*      */         
/* 2567 */         bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, shaderPack, 0, list, 0);
/* 2568 */         MacroState macrostate = new MacroState();
/*      */ 
/*      */         
/*      */         while (true) {
/* 2572 */           String s = bufferedreader.readLine();
/*      */           
/* 2574 */           if (s == null) {
/*      */             
/* 2576 */             bufferedreader.close();
/*      */             
/*      */             break;
/*      */           } 
/* 2580 */           s = applyOptions(s, ashaderoption);
/* 2581 */           stringbuilder.append(s).append('\n');
/*      */           
/* 2583 */           if (macrostate.processLine(s)) {
/*      */             
/* 2585 */             ShaderLine shaderline = ShaderParser.parseLine(s);
/*      */             
/* 2587 */             if (shaderline != null) {
/*      */               
/* 2589 */               if (shaderline.isAttribute("mc_Entity")) {
/*      */                 
/* 2591 */                 useEntityAttrib = true;
/* 2592 */                 progUseEntityAttrib = true;
/*      */               }
/* 2594 */               else if (shaderline.isAttribute("mc_midTexCoord")) {
/*      */                 
/* 2596 */                 useMidTexCoordAttrib = true;
/* 2597 */                 progUseMidTexCoordAttrib = true;
/*      */               }
/* 2599 */               else if (shaderline.isAttribute("at_tangent")) {
/*      */                 
/* 2601 */                 useTangentAttrib = true;
/* 2602 */                 progUseTangentAttrib = true;
/*      */               } 
/*      */               
/* 2605 */               if (shaderline.isConstInt("countInstances"))
/*      */               {
/* 2607 */                 program.setCountInstances(shaderline.getValueInt());
/* 2608 */                 SMCLog.info("countInstances: " + program.getCountInstances());
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/*      */         } 
/* 2614 */       } catch (Exception exception) {
/*      */         
/* 2616 */         SMCLog.severe("Couldn't read " + filename + "!");
/* 2617 */         exception.printStackTrace();
/* 2618 */         ARBShaderObjects.glDeleteObjectARB(i);
/* 2619 */         return 0;
/*      */       } 
/*      */     }
/*      */     
/* 2623 */     if (saveFinalShaders)
/*      */     {
/* 2625 */       saveShader(filename, stringbuilder.toString());
/*      */     }
/*      */     
/* 2628 */     ARBShaderObjects.glShaderSourceARB(i, stringbuilder);
/* 2629 */     ARBShaderObjects.glCompileShaderARB(i);
/*      */     
/* 2631 */     if (GL20.glGetShaderi(i, 35713) != 1)
/*      */     {
/* 2633 */       SMCLog.severe("Error compiling vertex shader: " + filename);
/*      */     }
/*      */     
/* 2636 */     printShaderLogInfo(i, filename, list);
/* 2637 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int createGeomShader(Program program, String filename) {
/* 2643 */     int i = ARBShaderObjects.glCreateShaderObjectARB(36313);
/*      */     
/* 2645 */     if (i == 0)
/*      */     {
/* 2647 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 2651 */     StringBuilder stringbuilder = new StringBuilder(131072);
/* 2652 */     BufferedReader bufferedreader = null;
/*      */ 
/*      */     
/*      */     try {
/* 2656 */       bufferedreader = new BufferedReader(getShaderReader(filename));
/*      */     }
/* 2658 */     catch (Exception var11) {
/*      */       
/* 2660 */       ARBShaderObjects.glDeleteObjectARB(i);
/* 2661 */       return 0;
/*      */     } 
/*      */     
/* 2664 */     ShaderOption[] ashaderoption = getChangedOptions(shaderPackOptions);
/* 2665 */     List<String> list = new ArrayList<>();
/* 2666 */     progArbGeometryShader4 = false;
/* 2667 */     progMaxVerticesOut = 3;
/*      */     
/* 2669 */     if (bufferedreader != null) {
/*      */       
/*      */       try {
/*      */         
/* 2673 */         bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, shaderPack, 0, list, 0);
/* 2674 */         MacroState macrostate = new MacroState();
/*      */ 
/*      */         
/*      */         while (true) {
/* 2678 */           String s = bufferedreader.readLine();
/*      */           
/* 2680 */           if (s == null) {
/*      */             
/* 2682 */             bufferedreader.close();
/*      */             
/*      */             break;
/*      */           } 
/* 2686 */           s = applyOptions(s, ashaderoption);
/* 2687 */           stringbuilder.append(s).append('\n');
/*      */           
/* 2689 */           if (macrostate.processLine(s)) {
/*      */             
/* 2691 */             ShaderLine shaderline = ShaderParser.parseLine(s);
/*      */             
/* 2693 */             if (shaderline != null)
/*      */             {
/* 2695 */               if (shaderline.isExtension("GL_ARB_geometry_shader4")) {
/*      */                 
/* 2697 */                 String s1 = Config.normalize(shaderline.getValue());
/*      */                 
/* 2699 */                 if (s1.equals("enable") || s1.equals("require") || s1.equals("warn"))
/*      */                 {
/* 2701 */                   progArbGeometryShader4 = true;
/*      */                 }
/*      */               } 
/*      */               
/* 2705 */               if (shaderline.isConstInt("maxVerticesOut"))
/*      */               {
/* 2707 */                 progMaxVerticesOut = shaderline.getValueInt();
/*      */               }
/*      */             }
/*      */           
/*      */           } 
/*      */         } 
/* 2713 */       } catch (Exception exception) {
/*      */         
/* 2715 */         SMCLog.severe("Couldn't read " + filename + "!");
/* 2716 */         exception.printStackTrace();
/* 2717 */         ARBShaderObjects.glDeleteObjectARB(i);
/* 2718 */         return 0;
/*      */       } 
/*      */     }
/*      */     
/* 2722 */     if (saveFinalShaders)
/*      */     {
/* 2724 */       saveShader(filename, stringbuilder.toString());
/*      */     }
/*      */     
/* 2727 */     ARBShaderObjects.glShaderSourceARB(i, stringbuilder);
/* 2728 */     ARBShaderObjects.glCompileShaderARB(i);
/*      */     
/* 2730 */     if (GL20.glGetShaderi(i, 35713) != 1)
/*      */     {
/* 2732 */       SMCLog.severe("Error compiling geometry shader: " + filename);
/*      */     }
/*      */     
/* 2735 */     printShaderLogInfo(i, filename, list);
/* 2736 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int createFragShader(Program program, String filename) {
/* 2742 */     int i = ARBShaderObjects.glCreateShaderObjectARB(35632);
/*      */     
/* 2744 */     if (i == 0)
/*      */     {
/* 2746 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 2750 */     StringBuilder stringbuilder = new StringBuilder(131072);
/* 2751 */     BufferedReader bufferedreader = null;
/*      */ 
/*      */     
/*      */     try {
/* 2755 */       bufferedreader = new BufferedReader(getShaderReader(filename));
/*      */     }
/* 2757 */     catch (Exception var14) {
/*      */       
/* 2759 */       ARBShaderObjects.glDeleteObjectARB(i);
/* 2760 */       return 0;
/*      */     } 
/*      */     
/* 2763 */     ShaderOption[] ashaderoption = getChangedOptions(shaderPackOptions);
/* 2764 */     List<String> list = new ArrayList<>();
/*      */     
/* 2766 */     if (bufferedreader != null) {
/*      */       
/*      */       try {
/*      */         
/* 2770 */         bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, shaderPack, 0, list, 0);
/* 2771 */         MacroState macrostate = new MacroState();
/*      */ 
/*      */         
/*      */         while (true) {
/* 2775 */           String s = bufferedreader.readLine();
/*      */           
/* 2777 */           if (s == null) {
/*      */             
/* 2779 */             bufferedreader.close();
/*      */             
/*      */             break;
/*      */           } 
/* 2783 */           s = applyOptions(s, ashaderoption);
/* 2784 */           stringbuilder.append(s).append('\n');
/*      */           
/* 2786 */           if (macrostate.processLine(s))
/*      */           {
/* 2788 */             ShaderLine shaderline = ShaderParser.parseLine(s);
/*      */             
/* 2790 */             if (shaderline != null)
/*      */             {
/* 2792 */               if (shaderline.isUniform()) {
/*      */                 
/* 2794 */                 String s6 = shaderline.getName();
/*      */                 
/*      */                 int l1;
/* 2797 */                 if ((l1 = ShaderParser.getShadowDepthIndex(s6)) >= 0) {
/*      */                   
/* 2799 */                   usedShadowDepthBuffers = Math.max(usedShadowDepthBuffers, l1 + 1); continue;
/*      */                 } 
/* 2801 */                 if ((l1 = ShaderParser.getShadowColorIndex(s6)) >= 0) {
/*      */                   
/* 2803 */                   usedShadowColorBuffers = Math.max(usedShadowColorBuffers, l1 + 1); continue;
/*      */                 } 
/* 2805 */                 if ((l1 = ShaderParser.getDepthIndex(s6)) >= 0) {
/*      */                   
/* 2807 */                   usedDepthBuffers = Math.max(usedDepthBuffers, l1 + 1); continue;
/*      */                 } 
/* 2809 */                 if (s6.equals("gdepth") && gbuffersFormat[1] == 6408) {
/*      */                   
/* 2811 */                   gbuffersFormat[1] = 34836; continue;
/*      */                 } 
/* 2813 */                 if ((l1 = ShaderParser.getColorIndex(s6)) >= 0) {
/*      */                   
/* 2815 */                   usedColorBuffers = Math.max(usedColorBuffers, l1 + 1); continue;
/*      */                 } 
/* 2817 */                 if (s6.equals("centerDepthSmooth"))
/*      */                 {
/* 2819 */                   centerDepthSmoothEnabled = true; } 
/*      */                 continue;
/*      */               } 
/* 2822 */               if (!shaderline.isConstInt("shadowMapResolution") && !shaderline.isProperty("SHADOWRES")) {
/*      */                 
/* 2824 */                 if (!shaderline.isConstFloat("shadowMapFov") && !shaderline.isProperty("SHADOWFOV")) {
/*      */                   
/* 2826 */                   if (!shaderline.isConstFloat("shadowDistance") && !shaderline.isProperty("SHADOWHPL")) {
/*      */                     
/* 2828 */                     if (shaderline.isConstFloat("shadowDistanceRenderMul")) {
/*      */                       
/* 2830 */                       shadowDistanceRenderMul = shaderline.getValueFloat();
/* 2831 */                       SMCLog.info("Shadow distance render mul: " + shadowDistanceRenderMul); continue;
/*      */                     } 
/* 2833 */                     if (shaderline.isConstFloat("shadowIntervalSize")) {
/*      */                       
/* 2835 */                       shadowIntervalSize = shaderline.getValueFloat();
/* 2836 */                       SMCLog.info("Shadow map interval size: " + shadowIntervalSize); continue;
/*      */                     } 
/* 2838 */                     if (shaderline.isConstBool("generateShadowMipmap", true)) {
/*      */                       
/* 2840 */                       Arrays.fill(shadowMipmapEnabled, true);
/* 2841 */                       SMCLog.info("Generate shadow mipmap"); continue;
/*      */                     } 
/* 2843 */                     if (shaderline.isConstBool("generateShadowColorMipmap", true)) {
/*      */                       
/* 2845 */                       Arrays.fill(shadowColorMipmapEnabled, true);
/* 2846 */                       SMCLog.info("Generate shadow color mipmap"); continue;
/*      */                     } 
/* 2848 */                     if (shaderline.isConstBool("shadowHardwareFiltering", true)) {
/*      */                       
/* 2850 */                       Arrays.fill(shadowHardwareFilteringEnabled, true);
/* 2851 */                       SMCLog.info("Hardware shadow filtering enabled."); continue;
/*      */                     } 
/* 2853 */                     if (shaderline.isConstBool("shadowHardwareFiltering0", true)) {
/*      */                       
/* 2855 */                       shadowHardwareFilteringEnabled[0] = true;
/* 2856 */                       SMCLog.info("shadowHardwareFiltering0"); continue;
/*      */                     } 
/* 2858 */                     if (shaderline.isConstBool("shadowHardwareFiltering1", true)) {
/*      */                       
/* 2860 */                       shadowHardwareFilteringEnabled[1] = true;
/* 2861 */                       SMCLog.info("shadowHardwareFiltering1"); continue;
/*      */                     } 
/* 2863 */                     if (shaderline.isConstBool("shadowtex0Mipmap", "shadowtexMipmap", true)) {
/*      */                       
/* 2865 */                       shadowMipmapEnabled[0] = true;
/* 2866 */                       SMCLog.info("shadowtex0Mipmap"); continue;
/*      */                     } 
/* 2868 */                     if (shaderline.isConstBool("shadowtex1Mipmap", true)) {
/*      */                       
/* 2870 */                       shadowMipmapEnabled[1] = true;
/* 2871 */                       SMCLog.info("shadowtex1Mipmap"); continue;
/*      */                     } 
/* 2873 */                     if (shaderline.isConstBool("shadowcolor0Mipmap", "shadowColor0Mipmap", true)) {
/*      */                       
/* 2875 */                       shadowColorMipmapEnabled[0] = true;
/* 2876 */                       SMCLog.info("shadowcolor0Mipmap"); continue;
/*      */                     } 
/* 2878 */                     if (shaderline.isConstBool("shadowcolor1Mipmap", "shadowColor1Mipmap", true)) {
/*      */                       
/* 2880 */                       shadowColorMipmapEnabled[1] = true;
/* 2881 */                       SMCLog.info("shadowcolor1Mipmap"); continue;
/*      */                     } 
/* 2883 */                     if (shaderline.isConstBool("shadowtex0Nearest", "shadowtexNearest", "shadow0MinMagNearest", true)) {
/*      */                       
/* 2885 */                       shadowFilterNearest[0] = true;
/* 2886 */                       SMCLog.info("shadowtex0Nearest"); continue;
/*      */                     } 
/* 2888 */                     if (shaderline.isConstBool("shadowtex1Nearest", "shadow1MinMagNearest", true)) {
/*      */                       
/* 2890 */                       shadowFilterNearest[1] = true;
/* 2891 */                       SMCLog.info("shadowtex1Nearest"); continue;
/*      */                     } 
/* 2893 */                     if (shaderline.isConstBool("shadowcolor0Nearest", "shadowColor0Nearest", "shadowColor0MinMagNearest", true)) {
/*      */                       
/* 2895 */                       shadowColorFilterNearest[0] = true;
/* 2896 */                       SMCLog.info("shadowcolor0Nearest"); continue;
/*      */                     } 
/* 2898 */                     if (shaderline.isConstBool("shadowcolor1Nearest", "shadowColor1Nearest", "shadowColor1MinMagNearest", true)) {
/*      */                       
/* 2900 */                       shadowColorFilterNearest[1] = true;
/* 2901 */                       SMCLog.info("shadowcolor1Nearest"); continue;
/*      */                     } 
/* 2903 */                     if (!shaderline.isConstFloat("wetnessHalflife") && !shaderline.isProperty("WETNESSHL")) {
/*      */                       
/* 2905 */                       if (!shaderline.isConstFloat("drynessHalflife") && !shaderline.isProperty("DRYNESSHL")) {
/*      */                         
/* 2907 */                         if (shaderline.isConstFloat("eyeBrightnessHalflife")) {
/*      */                           
/* 2909 */                           eyeBrightnessHalflife = shaderline.getValueFloat();
/* 2910 */                           SMCLog.info("Eye brightness halflife: " + eyeBrightnessHalflife); continue;
/*      */                         } 
/* 2912 */                         if (shaderline.isConstFloat("centerDepthHalflife")) {
/*      */                           
/* 2914 */                           centerDepthSmoothHalflife = shaderline.getValueFloat();
/* 2915 */                           SMCLog.info("Center depth halflife: " + centerDepthSmoothHalflife); continue;
/*      */                         } 
/* 2917 */                         if (shaderline.isConstFloat("sunPathRotation")) {
/*      */                           
/* 2919 */                           sunPathRotation = shaderline.getValueFloat();
/* 2920 */                           SMCLog.info("Sun path rotation: " + sunPathRotation); continue;
/*      */                         } 
/* 2922 */                         if (shaderline.isConstFloat("ambientOcclusionLevel")) {
/*      */                           
/* 2924 */                           aoLevel = Config.limit(shaderline.getValueFloat(), 0.0F, 1.0F);
/* 2925 */                           SMCLog.info("AO Level: " + aoLevel); continue;
/*      */                         } 
/* 2927 */                         if (shaderline.isConstInt("superSamplingLevel")) {
/*      */                           
/* 2929 */                           int i1 = shaderline.getValueInt();
/*      */                           
/* 2931 */                           if (i1 > 1) {
/*      */                             
/* 2933 */                             SMCLog.info("Super sampling level: " + i1 + "x");
/* 2934 */                             superSamplingLevel = i1;
/*      */                             
/*      */                             continue;
/*      */                           } 
/* 2938 */                           superSamplingLevel = 1;
/*      */                           continue;
/*      */                         } 
/* 2941 */                         if (shaderline.isConstInt("noiseTextureResolution")) {
/*      */                           
/* 2943 */                           noiseTextureResolution = shaderline.getValueInt();
/* 2944 */                           noiseTextureEnabled = true;
/* 2945 */                           SMCLog.info("Noise texture enabled");
/* 2946 */                           SMCLog.info("Noise texture resolution: " + noiseTextureResolution); continue;
/*      */                         } 
/* 2948 */                         if (shaderline.isConstIntSuffix("Format")) {
/*      */                           
/* 2950 */                           String s5 = StrUtils.removeSuffix(shaderline.getName(), "Format");
/* 2951 */                           String s7 = shaderline.getValue();
/* 2952 */                           int i2 = getBufferIndexFromString(s5);
/* 2953 */                           int l = getTextureFormatFromString(s7);
/*      */                           
/* 2955 */                           if (i2 >= 0 && l != 0) {
/*      */                             
/* 2957 */                             gbuffersFormat[i2] = l;
/* 2958 */                             SMCLog.info("%s format: %s", new Object[] { s5, s7 });
/*      */                           }  continue;
/*      */                         } 
/* 2961 */                         if (shaderline.isConstBoolSuffix("Clear", false)) {
/*      */                           
/* 2963 */                           if (ShaderParser.isComposite(filename) || ShaderParser.isDeferred(filename)) {
/*      */                             
/* 2965 */                             String s4 = StrUtils.removeSuffix(shaderline.getName(), "Clear");
/* 2966 */                             int k1 = getBufferIndexFromString(s4);
/*      */                             
/* 2968 */                             if (k1 >= 0) {
/*      */                               
/* 2970 */                               gbuffersClear[k1] = false;
/* 2971 */                               SMCLog.info("%s clear disabled", new Object[] { s4 });
/*      */                             } 
/*      */                           }  continue;
/*      */                         } 
/* 2975 */                         if (shaderline.isConstVec4Suffix("ClearColor")) {
/*      */                           
/* 2977 */                           if (ShaderParser.isComposite(filename) || ShaderParser.isDeferred(filename)) {
/*      */                             
/* 2979 */                             String s3 = StrUtils.removeSuffix(shaderline.getName(), "ClearColor");
/* 2980 */                             int j1 = getBufferIndexFromString(s3);
/*      */                             
/* 2982 */                             if (j1 >= 0) {
/*      */                               
/* 2984 */                               Vector4f vector4f = shaderline.getValueVec4();
/*      */                               
/* 2986 */                               if (vector4f != null) {
/*      */                                 
/* 2988 */                                 gbuffersClearColor[j1] = vector4f;
/* 2989 */                                 SMCLog.info("%s clear color: %s %s %s %s", new Object[] { s3, Float.valueOf(vector4f.getX()), Float.valueOf(vector4f.getY()), Float.valueOf(vector4f.getZ()), Float.valueOf(vector4f.getW()) });
/*      */                                 
/*      */                                 continue;
/*      */                               } 
/* 2993 */                               SMCLog.warning("Invalid color value: " + shaderline.getValue());
/*      */                             } 
/*      */                           } 
/*      */                           continue;
/*      */                         } 
/* 2998 */                         if (shaderline.isProperty("GAUX4FORMAT", "RGBA32F")) {
/*      */                           
/* 3000 */                           gbuffersFormat[7] = 34836;
/* 3001 */                           SMCLog.info("gaux4 format : RGB32AF"); continue;
/*      */                         } 
/* 3003 */                         if (shaderline.isProperty("GAUX4FORMAT", "RGB32F")) {
/*      */                           
/* 3005 */                           gbuffersFormat[7] = 34837;
/* 3006 */                           SMCLog.info("gaux4 format : RGB32F"); continue;
/*      */                         } 
/* 3008 */                         if (shaderline.isProperty("GAUX4FORMAT", "RGB16")) {
/*      */                           
/* 3010 */                           gbuffersFormat[7] = 32852;
/* 3011 */                           SMCLog.info("gaux4 format : RGB16"); continue;
/*      */                         } 
/* 3013 */                         if (shaderline.isConstBoolSuffix("MipmapEnabled", true)) {
/*      */                           
/* 3015 */                           if (ShaderParser.isComposite(filename) || ShaderParser.isDeferred(filename) || ShaderParser.isFinal(filename)) {
/*      */                             
/* 3017 */                             String s2 = StrUtils.removeSuffix(shaderline.getName(), "MipmapEnabled");
/* 3018 */                             int j = getBufferIndexFromString(s2);
/*      */                             
/* 3020 */                             if (j >= 0) {
/*      */                               
/* 3022 */                               int k = program.getCompositeMipmapSetting();
/* 3023 */                               k |= 1 << j;
/* 3024 */                               program.setCompositeMipmapSetting(k);
/* 3025 */                               SMCLog.info("%s mipmap enabled", new Object[] { s2 });
/*      */                             } 
/*      */                           }  continue;
/*      */                         } 
/* 3029 */                         if (shaderline.isProperty("DRAWBUFFERS")) {
/*      */                           
/* 3031 */                           String s1 = shaderline.getValue();
/*      */                           
/* 3033 */                           if (ShaderParser.isValidDrawBuffers(s1)) {
/*      */                             
/* 3035 */                             program.setDrawBufSettings(s1);
/*      */                             
/*      */                             continue;
/*      */                           } 
/* 3039 */                           SMCLog.warning("Invalid draw buffers: " + s1);
/*      */                         } 
/*      */                         
/*      */                         continue;
/*      */                       } 
/*      */                       
/* 3045 */                       drynessHalfLife = shaderline.getValueFloat();
/* 3046 */                       SMCLog.info("Dryness halflife: " + drynessHalfLife);
/*      */                       
/*      */                       continue;
/*      */                     } 
/*      */                     
/* 3051 */                     wetnessHalfLife = shaderline.getValueFloat();
/* 3052 */                     SMCLog.info("Wetness halflife: " + wetnessHalfLife);
/*      */                     
/*      */                     continue;
/*      */                   } 
/*      */                   
/* 3057 */                   shadowMapHalfPlane = shaderline.getValueFloat();
/* 3058 */                   shadowMapIsOrtho = true;
/* 3059 */                   SMCLog.info("Shadow map distance: " + shadowMapHalfPlane);
/*      */                   
/*      */                   continue;
/*      */                 } 
/*      */                 
/* 3064 */                 shadowMapFOV = shaderline.getValueFloat();
/* 3065 */                 shadowMapIsOrtho = false;
/* 3066 */                 SMCLog.info("Shadow map field of view: " + shadowMapFOV);
/*      */                 
/*      */                 continue;
/*      */               } 
/*      */               
/* 3071 */               spShadowMapWidth = spShadowMapHeight = shaderline.getValueInt();
/* 3072 */               shadowMapWidth = shadowMapHeight = Math.round(spShadowMapWidth * configShadowResMul);
/* 3073 */               SMCLog.info("Shadow map resolution: " + spShadowMapWidth);
/*      */             }
/*      */           
/*      */           }
/*      */         
/*      */         } 
/* 3079 */       } catch (Exception exception) {
/*      */         
/* 3081 */         SMCLog.severe("Couldn't read " + filename + "!");
/* 3082 */         exception.printStackTrace();
/* 3083 */         ARBShaderObjects.glDeleteObjectARB(i);
/* 3084 */         return 0;
/*      */       } 
/*      */     }
/*      */     
/* 3088 */     if (saveFinalShaders)
/*      */     {
/* 3090 */       saveShader(filename, stringbuilder.toString());
/*      */     }
/*      */     
/* 3093 */     ARBShaderObjects.glShaderSourceARB(i, stringbuilder);
/* 3094 */     ARBShaderObjects.glCompileShaderARB(i);
/*      */     
/* 3096 */     if (GL20.glGetShaderi(i, 35713) != 1)
/*      */     {
/* 3098 */       SMCLog.severe("Error compiling fragment shader: " + filename);
/*      */     }
/*      */     
/* 3101 */     printShaderLogInfo(i, filename, list);
/* 3102 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Reader getShaderReader(String filename) {
/* 3108 */     return new InputStreamReader(shaderPack.getResourceAsStream(filename));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void saveShader(String filename, String code) {
/*      */     try {
/* 3115 */       File file1 = new File(shaderPacksDir, "debug/" + filename);
/* 3116 */       file1.getParentFile().mkdirs();
/* 3117 */       Config.writeFile(file1, code);
/*      */     }
/* 3119 */     catch (IOException ioexception) {
/*      */       
/* 3121 */       Config.warn("Error saving: " + filename);
/* 3122 */       ioexception.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void clearDirectory(File dir) {
/* 3128 */     if (dir.exists())
/*      */     {
/* 3130 */       if (dir.isDirectory()) {
/*      */         
/* 3132 */         File[] afile = dir.listFiles();
/*      */         
/* 3134 */         if (afile != null)
/*      */         {
/* 3136 */           for (int i = 0; i < afile.length; i++) {
/*      */             
/* 3138 */             File file1 = afile[i];
/*      */             
/* 3140 */             if (file1.isDirectory())
/*      */             {
/* 3142 */               clearDirectory(file1);
/*      */             }
/*      */             
/* 3145 */             file1.delete();
/*      */           } 
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean printLogInfo(int obj, String name) {
/* 3154 */     IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
/* 3155 */     ARBShaderObjects.glGetObjectParameterARB(obj, 35716, intbuffer);
/* 3156 */     int i = intbuffer.get();
/*      */     
/* 3158 */     if (i > 1) {
/*      */       
/* 3160 */       ByteBuffer bytebuffer = BufferUtils.createByteBuffer(i);
/* 3161 */       intbuffer.flip();
/* 3162 */       ARBShaderObjects.glGetInfoLogARB(obj, intbuffer, bytebuffer);
/* 3163 */       byte[] abyte = new byte[i];
/* 3164 */       bytebuffer.get(abyte);
/*      */       
/* 3166 */       if (abyte[i - 1] == 0)
/*      */       {
/* 3168 */         abyte[i - 1] = 10;
/*      */       }
/*      */       
/* 3171 */       String s = new String(abyte, Charsets.US_ASCII);
/* 3172 */       s = StrUtils.trim(s, " \n\r\t");
/* 3173 */       SMCLog.info("Info log: " + name + "\n" + s);
/* 3174 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 3178 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean printShaderLogInfo(int shader, String name, List<String> listFiles) {
/* 3184 */     IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
/* 3185 */     int i = GL20.glGetShaderi(shader, 35716);
/*      */     
/* 3187 */     if (i <= 1)
/*      */     {
/* 3189 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 3193 */     for (int j = 0; j < listFiles.size(); j++) {
/*      */       
/* 3195 */       String s = listFiles.get(j);
/* 3196 */       SMCLog.info("File: " + (j + 1) + " = " + s);
/*      */     } 
/*      */     
/* 3199 */     String s1 = GL20.glGetShaderInfoLog(shader, i);
/* 3200 */     s1 = StrUtils.trim(s1, " \n\r\t");
/* 3201 */     SMCLog.info("Shader info log: " + name + "\n" + s1);
/* 3202 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setDrawBuffers(IntBuffer drawBuffers) {
/* 3208 */     if (drawBuffers == null)
/*      */     {
/* 3210 */       drawBuffers = drawBuffersNone;
/*      */     }
/*      */     
/* 3213 */     if (activeDrawBuffers != drawBuffers) {
/*      */       
/* 3215 */       activeDrawBuffers = drawBuffers;
/* 3216 */       GL20.glDrawBuffers(drawBuffers);
/* 3217 */       checkGLError("setDrawBuffers");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void useProgram(Program program) {
/* 3223 */     checkGLError("pre-useProgram");
/*      */     
/* 3225 */     if (isShadowPass) {
/*      */       
/* 3227 */       program = ProgramShadow;
/*      */     }
/* 3229 */     else if (isEntitiesGlowing) {
/*      */       
/* 3231 */       program = ProgramEntitiesGlowing;
/*      */     } 
/*      */     
/* 3234 */     if (activeProgram != program) {
/*      */       
/* 3236 */       updateAlphaBlend(activeProgram, program);
/* 3237 */       activeProgram = program;
/* 3238 */       int i = program.getId();
/* 3239 */       activeProgramID = i;
/* 3240 */       ARBShaderObjects.glUseProgramObjectARB(i);
/*      */       
/* 3242 */       if (checkGLError("useProgram") != 0) {
/*      */         
/* 3244 */         program.setId(0);
/* 3245 */         i = program.getId();
/* 3246 */         activeProgramID = i;
/* 3247 */         ARBShaderObjects.glUseProgramObjectARB(i);
/*      */       } 
/*      */       
/* 3250 */       shaderUniforms.setProgram(i);
/*      */       
/* 3252 */       if (customUniforms != null)
/*      */       {
/* 3254 */         customUniforms.setProgram(i);
/*      */       }
/*      */       
/* 3257 */       if (i != 0) {
/*      */         
/* 3259 */         IntBuffer intbuffer = program.getDrawBuffers();
/*      */         
/* 3261 */         if (isRenderingDfb)
/*      */         {
/* 3263 */           setDrawBuffers(intbuffer);
/*      */         }
/*      */         
/* 3266 */         activeCompositeMipmapSetting = program.getCompositeMipmapSetting();
/*      */         
/* 3268 */         switch (program.getProgramStage()) {
/*      */           
/*      */           case GBUFFERS:
/* 3271 */             setProgramUniform1i(uniform_texture, 0);
/* 3272 */             setProgramUniform1i(uniform_lightmap, 1);
/* 3273 */             setProgramUniform1i(uniform_normals, 2);
/* 3274 */             setProgramUniform1i(uniform_specular, 3);
/* 3275 */             setProgramUniform1i(uniform_shadow, waterShadowEnabled ? 5 : 4);
/* 3276 */             setProgramUniform1i(uniform_watershadow, 4);
/* 3277 */             setProgramUniform1i(uniform_shadowtex0, 4);
/* 3278 */             setProgramUniform1i(uniform_shadowtex1, 5);
/* 3279 */             setProgramUniform1i(uniform_depthtex0, 6);
/*      */             
/* 3281 */             if (customTexturesGbuffers != null || hasDeferredPrograms) {
/*      */               
/* 3283 */               setProgramUniform1i(uniform_gaux1, 7);
/* 3284 */               setProgramUniform1i(uniform_gaux2, 8);
/* 3285 */               setProgramUniform1i(uniform_gaux3, 9);
/* 3286 */               setProgramUniform1i(uniform_gaux4, 10);
/*      */             } 
/*      */             
/* 3289 */             setProgramUniform1i(uniform_depthtex1, 11);
/* 3290 */             setProgramUniform1i(uniform_shadowcolor, 13);
/* 3291 */             setProgramUniform1i(uniform_shadowcolor0, 13);
/* 3292 */             setProgramUniform1i(uniform_shadowcolor1, 14);
/* 3293 */             setProgramUniform1i(uniform_noisetex, 15);
/*      */             break;
/*      */           
/*      */           case DEFERRED:
/*      */           case COMPOSITE:
/* 3298 */             setProgramUniform1i(uniform_gcolor, 0);
/* 3299 */             setProgramUniform1i(uniform_gdepth, 1);
/* 3300 */             setProgramUniform1i(uniform_gnormal, 2);
/* 3301 */             setProgramUniform1i(uniform_composite, 3);
/* 3302 */             setProgramUniform1i(uniform_gaux1, 7);
/* 3303 */             setProgramUniform1i(uniform_gaux2, 8);
/* 3304 */             setProgramUniform1i(uniform_gaux3, 9);
/* 3305 */             setProgramUniform1i(uniform_gaux4, 10);
/* 3306 */             setProgramUniform1i(uniform_colortex0, 0);
/* 3307 */             setProgramUniform1i(uniform_colortex1, 1);
/* 3308 */             setProgramUniform1i(uniform_colortex2, 2);
/* 3309 */             setProgramUniform1i(uniform_colortex3, 3);
/* 3310 */             setProgramUniform1i(uniform_colortex4, 7);
/* 3311 */             setProgramUniform1i(uniform_colortex5, 8);
/* 3312 */             setProgramUniform1i(uniform_colortex6, 9);
/* 3313 */             setProgramUniform1i(uniform_colortex7, 10);
/* 3314 */             setProgramUniform1i(uniform_shadow, waterShadowEnabled ? 5 : 4);
/* 3315 */             setProgramUniform1i(uniform_watershadow, 4);
/* 3316 */             setProgramUniform1i(uniform_shadowtex0, 4);
/* 3317 */             setProgramUniform1i(uniform_shadowtex1, 5);
/* 3318 */             setProgramUniform1i(uniform_gdepthtex, 6);
/* 3319 */             setProgramUniform1i(uniform_depthtex0, 6);
/* 3320 */             setProgramUniform1i(uniform_depthtex1, 11);
/* 3321 */             setProgramUniform1i(uniform_depthtex2, 12);
/* 3322 */             setProgramUniform1i(uniform_shadowcolor, 13);
/* 3323 */             setProgramUniform1i(uniform_shadowcolor0, 13);
/* 3324 */             setProgramUniform1i(uniform_shadowcolor1, 14);
/* 3325 */             setProgramUniform1i(uniform_noisetex, 15);
/*      */             break;
/*      */           
/*      */           case SHADOW:
/* 3329 */             setProgramUniform1i(uniform_tex, 0);
/* 3330 */             setProgramUniform1i(uniform_texture, 0);
/* 3331 */             setProgramUniform1i(uniform_lightmap, 1);
/* 3332 */             setProgramUniform1i(uniform_normals, 2);
/* 3333 */             setProgramUniform1i(uniform_specular, 3);
/* 3334 */             setProgramUniform1i(uniform_shadow, waterShadowEnabled ? 5 : 4);
/* 3335 */             setProgramUniform1i(uniform_watershadow, 4);
/* 3336 */             setProgramUniform1i(uniform_shadowtex0, 4);
/* 3337 */             setProgramUniform1i(uniform_shadowtex1, 5);
/*      */             
/* 3339 */             if (customTexturesGbuffers != null) {
/*      */               
/* 3341 */               setProgramUniform1i(uniform_gaux1, 7);
/* 3342 */               setProgramUniform1i(uniform_gaux2, 8);
/* 3343 */               setProgramUniform1i(uniform_gaux3, 9);
/* 3344 */               setProgramUniform1i(uniform_gaux4, 10);
/*      */             } 
/*      */             
/* 3347 */             setProgramUniform1i(uniform_shadowcolor, 13);
/* 3348 */             setProgramUniform1i(uniform_shadowcolor0, 13);
/* 3349 */             setProgramUniform1i(uniform_shadowcolor1, 14);
/* 3350 */             setProgramUniform1i(uniform_noisetex, 15);
/*      */             break;
/*      */         } 
/* 3353 */         ItemStack itemstack = (mc.thePlayer != null) ? mc.thePlayer.getHeldItem() : null;
/* 3354 */         Item item = (itemstack != null) ? itemstack.getItem() : null;
/* 3355 */         int j = -1;
/* 3356 */         Block block = null;
/*      */         
/* 3358 */         if (item != null) {
/*      */           
/* 3360 */           j = Item.itemRegistry.getIDForObject(item);
/* 3361 */           block = (Block)Block.blockRegistry.getObjectById(j);
/* 3362 */           j = ItemAliases.getItemAliasId(j);
/*      */         } 
/*      */         
/* 3365 */         int k = (block != null) ? block.getLightValue() : 0;
/* 3366 */         setProgramUniform1i(uniform_heldItemId, j);
/* 3367 */         setProgramUniform1i(uniform_heldBlockLightValue, k);
/* 3368 */         setProgramUniform1i(uniform_fogMode, fogEnabled ? fogMode : 0);
/* 3369 */         setProgramUniform1f(uniform_fogDensity, fogEnabled ? fogDensity : 0.0F);
/* 3370 */         setProgramUniform3f(uniform_fogColor, fogColorR, fogColorG, fogColorB);
/* 3371 */         setProgramUniform3f(uniform_skyColor, skyColorR, skyColorG, skyColorB);
/* 3372 */         setProgramUniform1i(uniform_worldTime, (int)(worldTime % 24000L));
/* 3373 */         setProgramUniform1i(uniform_worldDay, (int)(worldTime / 24000L));
/* 3374 */         setProgramUniform1i(uniform_moonPhase, moonPhase);
/* 3375 */         setProgramUniform1i(uniform_frameCounter, frameCounter);
/* 3376 */         setProgramUniform1f(uniform_frameTime, frameTime);
/* 3377 */         setProgramUniform1f(uniform_frameTimeCounter, frameTimeCounter);
/* 3378 */         setProgramUniform1f(uniform_sunAngle, sunAngle);
/* 3379 */         setProgramUniform1f(uniform_shadowAngle, shadowAngle);
/* 3380 */         setProgramUniform1f(uniform_rainStrength, rainStrength);
/* 3381 */         setProgramUniform1f(uniform_aspectRatio, renderWidth / renderHeight);
/* 3382 */         setProgramUniform1f(uniform_viewWidth, renderWidth);
/* 3383 */         setProgramUniform1f(uniform_viewHeight, renderHeight);
/* 3384 */         setProgramUniform1f(uniform_near, 0.05F);
/* 3385 */         setProgramUniform1f(uniform_far, (mc.gameSettings.renderDistanceChunks * 16));
/* 3386 */         setProgramUniform3f(uniform_sunPosition, sunPosition[0], sunPosition[1], sunPosition[2]);
/* 3387 */         setProgramUniform3f(uniform_moonPosition, moonPosition[0], moonPosition[1], moonPosition[2]);
/* 3388 */         setProgramUniform3f(uniform_shadowLightPosition, shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
/* 3389 */         setProgramUniform3f(uniform_upPosition, upPosition[0], upPosition[1], upPosition[2]);
/* 3390 */         setProgramUniform3f(uniform_previousCameraPosition, (float)previousCameraPositionX, (float)previousCameraPositionY, (float)previousCameraPositionZ);
/* 3391 */         setProgramUniform3f(uniform_cameraPosition, (float)cameraPositionX, (float)cameraPositionY, (float)cameraPositionZ);
/* 3392 */         setProgramUniformMatrix4ARB(uniform_gbufferModelView, false, modelView);
/* 3393 */         setProgramUniformMatrix4ARB(uniform_gbufferModelViewInverse, false, modelViewInverse);
/* 3394 */         setProgramUniformMatrix4ARB(uniform_gbufferPreviousProjection, false, previousProjection);
/* 3395 */         setProgramUniformMatrix4ARB(uniform_gbufferProjection, false, projection);
/* 3396 */         setProgramUniformMatrix4ARB(uniform_gbufferProjectionInverse, false, projectionInverse);
/* 3397 */         setProgramUniformMatrix4ARB(uniform_gbufferPreviousModelView, false, previousModelView);
/*      */         
/* 3399 */         if (usedShadowDepthBuffers > 0) {
/*      */           
/* 3401 */           setProgramUniformMatrix4ARB(uniform_shadowProjection, false, shadowProjection);
/* 3402 */           setProgramUniformMatrix4ARB(uniform_shadowProjectionInverse, false, shadowProjectionInverse);
/* 3403 */           setProgramUniformMatrix4ARB(uniform_shadowModelView, false, shadowModelView);
/* 3404 */           setProgramUniformMatrix4ARB(uniform_shadowModelViewInverse, false, shadowModelViewInverse);
/*      */         } 
/*      */         
/* 3407 */         setProgramUniform1f(uniform_wetness, wetness);
/* 3408 */         setProgramUniform1f(uniform_eyeAltitude, eyePosY);
/* 3409 */         setProgramUniform2i(uniform_eyeBrightness, eyeBrightness & 0xFFFF, eyeBrightness >> 16);
/* 3410 */         setProgramUniform2i(uniform_eyeBrightnessSmooth, Math.round(eyeBrightnessFadeX), Math.round(eyeBrightnessFadeY));
/* 3411 */         setProgramUniform2i(uniform_terrainTextureSize, terrainTextureSize[0], terrainTextureSize[1]);
/* 3412 */         setProgramUniform1i(uniform_terrainIconSize, terrainIconSize);
/* 3413 */         setProgramUniform1i(uniform_isEyeInWater, isEyeInWater);
/* 3414 */         setProgramUniform1f(uniform_nightVision, nightVision);
/* 3415 */         setProgramUniform1f(uniform_blindness, blindness);
/* 3416 */         setProgramUniform1f(uniform_screenBrightness, mc.gameSettings.gammaSetting);
/* 3417 */         setProgramUniform1i(uniform_hideGUI, mc.gameSettings.hideGUI ? 1 : 0);
/* 3418 */         setProgramUniform1f(uniform_centerDepthSmooth, centerDepthSmooth);
/* 3419 */         setProgramUniform2i(uniform_atlasSize, atlasSizeX, atlasSizeY);
/*      */         
/* 3421 */         if (customUniforms != null)
/*      */         {
/* 3423 */           customUniforms.update();
/*      */         }
/*      */         
/* 3426 */         checkGLError("end useProgram");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void updateAlphaBlend(Program programOld, Program programNew) {
/* 3433 */     if (programOld.getAlphaState() != null)
/*      */     {
/* 3435 */       GlStateManager.unlockAlpha();
/*      */     }
/*      */     
/* 3438 */     if (programOld.getBlendState() != null)
/*      */     {
/* 3440 */       GlStateManager.unlockBlend();
/*      */     }
/*      */     
/* 3443 */     GlAlphaState glalphastate = programNew.getAlphaState();
/*      */     
/* 3445 */     if (glalphastate != null)
/*      */     {
/* 3447 */       GlStateManager.lockAlpha(glalphastate);
/*      */     }
/*      */     
/* 3450 */     GlBlendState glblendstate = programNew.getBlendState();
/*      */     
/* 3452 */     if (glblendstate != null)
/*      */     {
/* 3454 */       GlStateManager.lockBlend(glblendstate);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setProgramUniform1i(ShaderUniform1i su, int value) {
/* 3460 */     su.setValue(value);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setProgramUniform2i(ShaderUniform2i su, int i0, int i1) {
/* 3465 */     su.setValue(i0, i1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setProgramUniform1f(ShaderUniform1f su, float value) {
/* 3470 */     su.setValue(value);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setProgramUniform3f(ShaderUniform3f su, float f0, float f1, float f2) {
/* 3475 */     su.setValue(f0, f1, f2);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setProgramUniformMatrix4ARB(ShaderUniformM4 su, boolean transpose, FloatBuffer matrix) {
/* 3480 */     su.setValue(transpose, matrix);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBufferIndexFromString(String name) {
/* 3485 */     return (!name.equals("colortex0") && !name.equals("gcolor")) ? ((!name.equals("colortex1") && !name.equals("gdepth")) ? ((!name.equals("colortex2") && !name.equals("gnormal")) ? ((!name.equals("colortex3") && !name.equals("composite")) ? ((!name.equals("colortex4") && !name.equals("gaux1")) ? ((!name.equals("colortex5") && !name.equals("gaux2")) ? ((!name.equals("colortex6") && !name.equals("gaux3")) ? ((!name.equals("colortex7") && !name.equals("gaux4")) ? -1 : 7) : 6) : 5) : 4) : 3) : 2) : 1) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getTextureFormatFromString(String par) {
/* 3490 */     par = par.trim();
/*      */     
/* 3492 */     for (int i = 0; i < formatNames.length; i++) {
/*      */       
/* 3494 */       String s = formatNames[i];
/*      */       
/* 3496 */       if (par.equals(s))
/*      */       {
/* 3498 */         return formatIds[i];
/*      */       }
/*      */     } 
/*      */     
/* 3502 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setupNoiseTexture() {
/* 3507 */     if (noiseTexture == null && noiseTexturePath != null)
/*      */     {
/* 3509 */       noiseTexture = loadCustomTexture(15, noiseTexturePath);
/*      */     }
/*      */     
/* 3512 */     if (noiseTexture == null)
/*      */     {
/* 3514 */       noiseTexture = new HFNoiseTexture(noiseTextureResolution, noiseTextureResolution);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void loadEntityDataMap() {
/* 3520 */     mapBlockToEntityData = new IdentityHashMap<>(300);
/*      */     
/* 3522 */     if (mapBlockToEntityData.isEmpty())
/*      */     {
/* 3524 */       for (ResourceLocation resourcelocation : Block.blockRegistry.getKeys()) {
/*      */         
/* 3526 */         Block block = (Block)Block.blockRegistry.getObject(resourcelocation);
/* 3527 */         int i = Block.blockRegistry.getIDForObject(block);
/* 3528 */         mapBlockToEntityData.put(block, Integer.valueOf(i));
/*      */       } 
/*      */     }
/*      */     
/* 3532 */     BufferedReader bufferedreader = null;
/*      */ 
/*      */     
/*      */     try {
/* 3536 */       bufferedreader = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream("/mc_Entity_x.txt")));
/*      */     }
/* 3538 */     catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3543 */     if (bufferedreader != null) {
/*      */       try {
/*      */         String s1;
/*      */ 
/*      */ 
/*      */         
/* 3549 */         while ((s1 = bufferedreader.readLine()) != null)
/*      */         {
/* 3551 */           Matcher matcher = patternLoadEntityDataMap.matcher(s1);
/*      */           
/* 3553 */           if (matcher.matches()) {
/*      */             
/* 3555 */             String s2 = matcher.group(1);
/* 3556 */             String s = matcher.group(2);
/* 3557 */             int j = Integer.parseInt(s);
/* 3558 */             Block block1 = Block.getBlockFromName(s2);
/*      */             
/* 3560 */             if (block1 != null) {
/*      */               
/* 3562 */               mapBlockToEntityData.put(block1, Integer.valueOf(j));
/*      */               
/*      */               continue;
/*      */             } 
/* 3566 */             SMCLog.warning("Unknown block name %s", new Object[] { s2 });
/*      */             
/*      */             continue;
/*      */           } 
/*      */           
/* 3571 */           SMCLog.warning("unmatched %s\n", new Object[] { s1 });
/*      */         }
/*      */       
/*      */       }
/* 3575 */       catch (Exception var9) {
/*      */         
/* 3577 */         SMCLog.warning("Error parsing mc_Entity_x.txt");
/*      */       } 
/*      */     }
/*      */     
/* 3581 */     if (bufferedreader != null) {
/*      */       
/*      */       try {
/*      */         
/* 3585 */         bufferedreader.close();
/*      */       }
/* 3587 */       catch (Exception exception) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static IntBuffer fillIntBufferZero(IntBuffer buf) {
/* 3596 */     int i = buf.limit();
/*      */     
/* 3598 */     for (int j = buf.position(); j < i; j++)
/*      */     {
/* 3600 */       buf.put(j, 0);
/*      */     }
/*      */     
/* 3603 */     return buf;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void uninit() {
/* 3608 */     if (isShaderPackInitialized) {
/*      */       
/* 3610 */       checkGLError("Shaders.uninit pre");
/*      */       
/* 3612 */       for (int i = 0; i < ProgramsAll.length; i++) {
/*      */         
/* 3614 */         Program program = ProgramsAll[i];
/*      */         
/* 3616 */         if (program.getRef() != 0) {
/*      */           
/* 3618 */           ARBShaderObjects.glDeleteObjectARB(program.getRef());
/* 3619 */           checkGLError("del programRef");
/*      */         } 
/*      */         
/* 3622 */         program.setRef(0);
/* 3623 */         program.setId(0);
/* 3624 */         program.setDrawBufSettings((String)null);
/* 3625 */         program.setDrawBuffers((IntBuffer)null);
/* 3626 */         program.setCompositeMipmapSetting(0);
/*      */       } 
/*      */       
/* 3629 */       hasDeferredPrograms = false;
/*      */       
/* 3631 */       if (dfb != 0) {
/*      */         
/* 3633 */         EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
/* 3634 */         dfb = 0;
/* 3635 */         checkGLError("del dfb");
/*      */       } 
/*      */       
/* 3638 */       if (sfb != 0) {
/*      */         
/* 3640 */         EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
/* 3641 */         sfb = 0;
/* 3642 */         checkGLError("del sfb");
/*      */       } 
/*      */       
/* 3645 */       if (dfbDepthTextures != null) {
/*      */         
/* 3647 */         GlStateManager.deleteTextures(dfbDepthTextures);
/* 3648 */         fillIntBufferZero(dfbDepthTextures);
/* 3649 */         checkGLError("del dfbDepthTextures");
/*      */       } 
/*      */       
/* 3652 */       if (dfbColorTextures != null) {
/*      */         
/* 3654 */         GlStateManager.deleteTextures(dfbColorTextures);
/* 3655 */         fillIntBufferZero(dfbColorTextures);
/* 3656 */         checkGLError("del dfbTextures");
/*      */       } 
/*      */       
/* 3659 */       if (sfbDepthTextures != null) {
/*      */         
/* 3661 */         GlStateManager.deleteTextures(sfbDepthTextures);
/* 3662 */         fillIntBufferZero(sfbDepthTextures);
/* 3663 */         checkGLError("del shadow depth");
/*      */       } 
/*      */       
/* 3666 */       if (sfbColorTextures != null) {
/*      */         
/* 3668 */         GlStateManager.deleteTextures(sfbColorTextures);
/* 3669 */         fillIntBufferZero(sfbColorTextures);
/* 3670 */         checkGLError("del shadow color");
/*      */       } 
/*      */       
/* 3673 */       if (dfbDrawBuffers != null)
/*      */       {
/* 3675 */         fillIntBufferZero(dfbDrawBuffers);
/*      */       }
/*      */       
/* 3678 */       if (noiseTexture != null) {
/*      */         
/* 3680 */         noiseTexture.deleteTexture();
/* 3681 */         noiseTexture = null;
/*      */       } 
/*      */       
/* 3684 */       SMCLog.info("Uninit");
/* 3685 */       shadowPassInterval = 0;
/* 3686 */       shouldSkipDefaultShadow = false;
/* 3687 */       isShaderPackInitialized = false;
/* 3688 */       checkGLError("Shaders.uninit");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void scheduleResize() {
/* 3694 */     renderDisplayHeight = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void scheduleResizeShadow() {
/* 3699 */     needResizeShadow = true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void resize() {
/* 3704 */     renderDisplayWidth = mc.displayWidth;
/* 3705 */     renderDisplayHeight = mc.displayHeight;
/* 3706 */     renderWidth = Math.round(renderDisplayWidth * configRenderResMul);
/* 3707 */     renderHeight = Math.round(renderDisplayHeight * configRenderResMul);
/* 3708 */     setupFrameBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void resizeShadow() {
/* 3713 */     needResizeShadow = false;
/* 3714 */     shadowMapWidth = Math.round(spShadowMapWidth * configShadowResMul);
/* 3715 */     shadowMapHeight = Math.round(spShadowMapHeight * configShadowResMul);
/* 3716 */     setupShadowFrameBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setupFrameBuffer() {
/* 3721 */     if (dfb != 0) {
/*      */       
/* 3723 */       EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
/* 3724 */       GlStateManager.deleteTextures(dfbDepthTextures);
/* 3725 */       GlStateManager.deleteTextures(dfbColorTextures);
/*      */     } 
/*      */     
/* 3728 */     dfb = EXTFramebufferObject.glGenFramebuffersEXT();
/* 3729 */     GL11.glGenTextures((IntBuffer)dfbDepthTextures.clear().limit(usedDepthBuffers));
/* 3730 */     GL11.glGenTextures((IntBuffer)dfbColorTextures.clear().limit(16));
/* 3731 */     dfbDepthTextures.position(0);
/* 3732 */     dfbColorTextures.position(0);
/* 3733 */     EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
/* 3734 */     GL20.glDrawBuffers(0);
/* 3735 */     GL11.glReadBuffer(0);
/*      */     
/* 3737 */     for (int i = 0; i < usedDepthBuffers; i++) {
/*      */       
/* 3739 */       GlStateManager.bindTexture(dfbDepthTextures.get(i));
/* 3740 */       GL11.glTexParameteri(3553, 10242, 33071);
/* 3741 */       GL11.glTexParameteri(3553, 10243, 33071);
/* 3742 */       GL11.glTexParameteri(3553, 10241, 9728);
/* 3743 */       GL11.glTexParameteri(3553, 10240, 9728);
/* 3744 */       GL11.glTexParameteri(3553, 34891, 6409);
/* 3745 */       GL11.glTexImage2D(3553, 0, 6402, renderWidth, renderHeight, 0, 6402, 5126, (FloatBuffer)null);
/*      */     } 
/*      */     
/* 3748 */     EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, dfbDepthTextures.get(0), 0);
/* 3749 */     GL20.glDrawBuffers(dfbDrawBuffers);
/* 3750 */     GL11.glReadBuffer(0);
/* 3751 */     checkGLError("FT d");
/*      */     
/* 3753 */     for (int k = 0; k < usedColorBuffers; k++) {
/*      */       
/* 3755 */       GlStateManager.bindTexture(dfbColorTexturesFlip.getA(k));
/* 3756 */       GL11.glTexParameteri(3553, 10242, 33071);
/* 3757 */       GL11.glTexParameteri(3553, 10243, 33071);
/* 3758 */       GL11.glTexParameteri(3553, 10241, 9729);
/* 3759 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 3760 */       GL11.glTexImage2D(3553, 0, gbuffersFormat[k], renderWidth, renderHeight, 0, getPixelFormat(gbuffersFormat[k]), 33639, (ByteBuffer)null);
/* 3761 */       EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + k, 3553, dfbColorTexturesFlip.getA(k), 0);
/* 3762 */       checkGLError("FT c");
/*      */     } 
/*      */     
/* 3765 */     for (int l = 0; l < usedColorBuffers; l++) {
/*      */       
/* 3767 */       GlStateManager.bindTexture(dfbColorTexturesFlip.getB(l));
/* 3768 */       GL11.glTexParameteri(3553, 10242, 33071);
/* 3769 */       GL11.glTexParameteri(3553, 10243, 33071);
/* 3770 */       GL11.glTexParameteri(3553, 10241, 9729);
/* 3771 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 3772 */       GL11.glTexImage2D(3553, 0, gbuffersFormat[l], renderWidth, renderHeight, 0, getPixelFormat(gbuffersFormat[l]), 33639, (ByteBuffer)null);
/* 3773 */       checkGLError("FT ca");
/*      */     } 
/*      */     
/* 3776 */     int i1 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
/*      */     
/* 3778 */     if (i1 == 36058) {
/*      */       
/* 3780 */       printChatAndLogError("[Shaders] Error: Failed framebuffer incomplete formats");
/*      */       
/* 3782 */       for (int j = 0; j < usedColorBuffers; j++) {
/*      */         
/* 3784 */         GlStateManager.bindTexture(dfbColorTexturesFlip.getA(j));
/* 3785 */         GL11.glTexImage2D(3553, 0, 6408, renderWidth, renderHeight, 0, 32993, 33639, (ByteBuffer)null);
/* 3786 */         EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + j, 3553, dfbColorTexturesFlip.getA(j), 0);
/* 3787 */         checkGLError("FT c");
/*      */       } 
/*      */       
/* 3790 */       i1 = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
/*      */       
/* 3792 */       if (i1 == 36053)
/*      */       {
/* 3794 */         SMCLog.info("complete");
/*      */       }
/*      */     } 
/*      */     
/* 3798 */     GlStateManager.bindTexture(0);
/*      */     
/* 3800 */     if (i1 != 36053) {
/*      */       
/* 3802 */       printChatAndLogError("[Shaders] Error: Failed creating framebuffer! (Status " + i1 + ")");
/*      */     }
/*      */     else {
/*      */       
/* 3806 */       SMCLog.info("Framebuffer created.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getPixelFormat(int internalFormat) {
/* 3812 */     switch (internalFormat) {
/*      */       
/*      */       case 33333:
/*      */       case 33334:
/*      */       case 33339:
/*      */       case 33340:
/*      */       case 36208:
/*      */       case 36209:
/*      */       case 36226:
/*      */       case 36227:
/* 3822 */         return 36251;
/*      */     } 
/*      */     
/* 3825 */     return 32993;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setupShadowFrameBuffer() {
/* 3831 */     if (usedShadowDepthBuffers != 0) {
/*      */       
/* 3833 */       if (sfb != 0) {
/*      */         
/* 3835 */         EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
/* 3836 */         GlStateManager.deleteTextures(sfbDepthTextures);
/* 3837 */         GlStateManager.deleteTextures(sfbColorTextures);
/*      */       } 
/*      */       
/* 3840 */       sfb = EXTFramebufferObject.glGenFramebuffersEXT();
/* 3841 */       EXTFramebufferObject.glBindFramebufferEXT(36160, sfb);
/* 3842 */       GL11.glDrawBuffer(0);
/* 3843 */       GL11.glReadBuffer(0);
/* 3844 */       GL11.glGenTextures((IntBuffer)sfbDepthTextures.clear().limit(usedShadowDepthBuffers));
/* 3845 */       GL11.glGenTextures((IntBuffer)sfbColorTextures.clear().limit(usedShadowColorBuffers));
/* 3846 */       sfbDepthTextures.position(0);
/* 3847 */       sfbColorTextures.position(0);
/*      */       
/* 3849 */       for (int i = 0; i < usedShadowDepthBuffers; i++) {
/*      */         
/* 3851 */         GlStateManager.bindTexture(sfbDepthTextures.get(i));
/* 3852 */         GL11.glTexParameterf(3553, 10242, 33071.0F);
/* 3853 */         GL11.glTexParameterf(3553, 10243, 33071.0F);
/* 3854 */         int j = shadowFilterNearest[i] ? 9728 : 9729;
/* 3855 */         GL11.glTexParameteri(3553, 10241, j);
/* 3856 */         GL11.glTexParameteri(3553, 10240, j);
/*      */         
/* 3858 */         if (shadowHardwareFilteringEnabled[i])
/*      */         {
/* 3860 */           GL11.glTexParameteri(3553, 34892, 34894);
/*      */         }
/*      */         
/* 3863 */         GL11.glTexImage2D(3553, 0, 6402, shadowMapWidth, shadowMapHeight, 0, 6402, 5126, (FloatBuffer)null);
/*      */       } 
/*      */       
/* 3866 */       EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, sfbDepthTextures.get(0), 0);
/* 3867 */       checkGLError("FT sd");
/*      */       
/* 3869 */       for (int k = 0; k < usedShadowColorBuffers; k++) {
/*      */         
/* 3871 */         GlStateManager.bindTexture(sfbColorTextures.get(k));
/* 3872 */         GL11.glTexParameterf(3553, 10242, 33071.0F);
/* 3873 */         GL11.glTexParameterf(3553, 10243, 33071.0F);
/* 3874 */         int i1 = shadowColorFilterNearest[k] ? 9728 : 9729;
/* 3875 */         GL11.glTexParameteri(3553, 10241, i1);
/* 3876 */         GL11.glTexParameteri(3553, 10240, i1);
/* 3877 */         GL11.glTexImage2D(3553, 0, 6408, shadowMapWidth, shadowMapHeight, 0, 32993, 33639, (ByteBuffer)null);
/* 3878 */         EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + k, 3553, sfbColorTextures.get(k), 0);
/* 3879 */         checkGLError("FT sc");
/*      */       } 
/*      */       
/* 3882 */       GlStateManager.bindTexture(0);
/*      */       
/* 3884 */       if (usedShadowColorBuffers > 0)
/*      */       {
/* 3886 */         GL20.glDrawBuffers(sfbDrawBuffers);
/*      */       }
/*      */       
/* 3889 */       int l = EXTFramebufferObject.glCheckFramebufferStatusEXT(36160);
/*      */       
/* 3891 */       if (l != 36053) {
/*      */         
/* 3893 */         printChatAndLogError("[Shaders] Error: Failed creating shadow framebuffer! (Status " + l + ")");
/*      */       }
/*      */       else {
/*      */         
/* 3897 */         SMCLog.info("Shadow framebuffer created.");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginRender(Minecraft minecraft, float partialTicks, long finishTimeNano) {
/* 3904 */     checkGLError("pre beginRender");
/* 3905 */     checkWorldChanged((World)mc.theWorld);
/* 3906 */     mc = minecraft;
/* 3907 */     mc.mcProfiler.startSection("init");
/* 3908 */     entityRenderer = mc.entityRenderer;
/*      */     
/* 3910 */     if (!isShaderPackInitialized) {
/*      */       
/*      */       try {
/*      */         
/* 3914 */         init();
/*      */       }
/* 3916 */       catch (IllegalStateException illegalstateexception) {
/*      */         
/* 3918 */         if (Config.normalize(illegalstateexception.getMessage()).equals("Function is not supported")) {
/*      */           
/* 3920 */           printChatAndLogError("[Shaders] Error: " + illegalstateexception.getMessage());
/* 3921 */           illegalstateexception.printStackTrace();
/* 3922 */           setShaderPack("OFF");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     }
/* 3928 */     if (mc.displayWidth != renderDisplayWidth || mc.displayHeight != renderDisplayHeight)
/*      */     {
/* 3930 */       resize();
/*      */     }
/*      */     
/* 3933 */     if (needResizeShadow)
/*      */     {
/* 3935 */       resizeShadow();
/*      */     }
/*      */     
/* 3938 */     worldTime = mc.theWorld.getWorldTime();
/* 3939 */     diffWorldTime = (worldTime - lastWorldTime) % 24000L;
/*      */     
/* 3941 */     if (diffWorldTime < 0L)
/*      */     {
/* 3943 */       diffWorldTime += 24000L;
/*      */     }
/*      */     
/* 3946 */     lastWorldTime = worldTime;
/* 3947 */     moonPhase = mc.theWorld.getMoonPhase();
/* 3948 */     frameCounter++;
/*      */     
/* 3950 */     if (frameCounter >= 720720)
/*      */     {
/* 3952 */       frameCounter = 0;
/*      */     }
/*      */     
/* 3955 */     systemTime = System.currentTimeMillis();
/*      */     
/* 3957 */     if (lastSystemTime == 0L)
/*      */     {
/* 3959 */       lastSystemTime = systemTime;
/*      */     }
/*      */     
/* 3962 */     diffSystemTime = systemTime - lastSystemTime;
/* 3963 */     lastSystemTime = systemTime;
/* 3964 */     frameTime = (float)diffSystemTime / 1000.0F;
/* 3965 */     frameTimeCounter += frameTime;
/* 3966 */     frameTimeCounter %= 3600.0F;
/* 3967 */     rainStrength = minecraft.theWorld.getRainStrength(partialTicks);
/* 3968 */     float f = (float)diffSystemTime * 0.01F;
/* 3969 */     float f1 = (float)Math.exp(Math.log(0.5D) * f / ((wetness < rainStrength) ? drynessHalfLife : wetnessHalfLife));
/* 3970 */     wetness = wetness * f1 + rainStrength * (1.0F - f1);
/* 3971 */     Entity entity = mc.getRenderViewEntity();
/*      */     
/* 3973 */     if (entity != null) {
/*      */       
/* 3975 */       isSleeping = (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping());
/* 3976 */       eyePosY = (float)entity.posY * partialTicks + (float)entity.lastTickPosY * (1.0F - partialTicks);
/* 3977 */       eyeBrightness = entity.getBrightnessForRender(partialTicks);
/* 3978 */       f1 = (float)diffSystemTime * 0.01F;
/* 3979 */       float f2 = (float)Math.exp(Math.log(0.5D) * f1 / eyeBrightnessHalflife);
/* 3980 */       eyeBrightnessFadeX = eyeBrightnessFadeX * f2 + (eyeBrightness & 0xFFFF) * (1.0F - f2);
/* 3981 */       eyeBrightnessFadeY = eyeBrightnessFadeY * f2 + (eyeBrightness >> 16) * (1.0F - f2);
/* 3982 */       Block block = ActiveRenderInfo.getBlockAtEntityViewpoint((World)mc.theWorld, entity, partialTicks);
/* 3983 */       Material material = block.getMaterial();
/*      */       
/* 3985 */       if (material == Material.water) {
/*      */         
/* 3987 */         isEyeInWater = 1;
/*      */       }
/* 3989 */       else if (material == Material.lava) {
/*      */         
/* 3991 */         isEyeInWater = 2;
/*      */       }
/*      */       else {
/*      */         
/* 3995 */         isEyeInWater = 0;
/*      */       } 
/*      */       
/* 3998 */       if (mc.thePlayer != null) {
/*      */         
/* 4000 */         nightVision = 0.0F;
/*      */         
/* 4002 */         if (mc.thePlayer.isPotionActive(Potion.nightVision))
/*      */         {
/* 4004 */           nightVision = (Config.getMinecraft()).entityRenderer.getNightVisionBrightness((EntityLivingBase)mc.thePlayer, partialTicks);
/*      */         }
/*      */         
/* 4007 */         blindness = 0.0F;
/*      */         
/* 4009 */         if (mc.thePlayer.isPotionActive(Potion.blindness)) {
/*      */           
/* 4011 */           int i = mc.thePlayer.getActivePotionEffect(Potion.blindness).getDuration();
/* 4012 */           blindness = Config.limit(i / 20.0F, 0.0F, 1.0F);
/*      */         } 
/*      */       } 
/*      */       
/* 4016 */       Vec3 vec3 = mc.theWorld.getSkyColor(entity, partialTicks);
/* 4017 */       vec3 = CustomColors.getWorldSkyColor(vec3, currentWorld, entity, partialTicks);
/* 4018 */       skyColorR = (float)vec3.xCoord;
/* 4019 */       skyColorG = (float)vec3.yCoord;
/* 4020 */       skyColorB = (float)vec3.zCoord;
/*      */     } 
/*      */     
/* 4023 */     isRenderingWorld = true;
/* 4024 */     isCompositeRendered = false;
/* 4025 */     isShadowPass = false;
/* 4026 */     isHandRenderedMain = false;
/* 4027 */     isHandRenderedOff = false;
/* 4028 */     skipRenderHandMain = false;
/* 4029 */     skipRenderHandOff = false;
/* 4030 */     bindGbuffersTextures();
/* 4031 */     previousCameraPositionX = cameraPositionX;
/* 4032 */     previousCameraPositionY = cameraPositionY;
/* 4033 */     previousCameraPositionZ = cameraPositionZ;
/* 4034 */     previousProjection.position(0);
/* 4035 */     projection.position(0);
/* 4036 */     previousProjection.put(projection);
/* 4037 */     previousProjection.position(0);
/* 4038 */     projection.position(0);
/* 4039 */     previousModelView.position(0);
/* 4040 */     modelView.position(0);
/* 4041 */     previousModelView.put(modelView);
/* 4042 */     previousModelView.position(0);
/* 4043 */     modelView.position(0);
/* 4044 */     checkGLError("beginRender");
/* 4045 */     ShadersRender.renderShadowMap(entityRenderer, 0, partialTicks, finishTimeNano);
/* 4046 */     mc.mcProfiler.endSection();
/* 4047 */     EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
/*      */     
/* 4049 */     for (int j = 0; j < usedColorBuffers; j++)
/*      */     {
/* 4051 */       EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + j, 3553, dfbColorTexturesFlip.getA(j), 0);
/*      */     }
/*      */     
/* 4054 */     checkGLError("end beginRender");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void bindGbuffersTextures() {
/* 4059 */     if (usedShadowDepthBuffers >= 1) {
/*      */       
/* 4061 */       GlStateManager.setActiveTexture(33988);
/* 4062 */       GlStateManager.bindTexture(sfbDepthTextures.get(0));
/*      */       
/* 4064 */       if (usedShadowDepthBuffers >= 2) {
/*      */         
/* 4066 */         GlStateManager.setActiveTexture(33989);
/* 4067 */         GlStateManager.bindTexture(sfbDepthTextures.get(1));
/*      */       } 
/*      */     } 
/*      */     
/* 4071 */     GlStateManager.setActiveTexture(33984);
/*      */     
/* 4073 */     for (int i = 0; i < usedColorBuffers; i++) {
/*      */       
/* 4075 */       GlStateManager.bindTexture(dfbColorTexturesFlip.getA(i));
/* 4076 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 4077 */       GL11.glTexParameteri(3553, 10241, 9729);
/* 4078 */       GlStateManager.bindTexture(dfbColorTexturesFlip.getB(i));
/* 4079 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 4080 */       GL11.glTexParameteri(3553, 10241, 9729);
/*      */     } 
/*      */     
/* 4083 */     GlStateManager.bindTexture(0);
/*      */     
/* 4085 */     for (int j = 0; j < 4 && 4 + j < usedColorBuffers; j++) {
/*      */       
/* 4087 */       GlStateManager.setActiveTexture(33991 + j);
/* 4088 */       GlStateManager.bindTexture(dfbColorTexturesFlip.getA(4 + j));
/*      */     } 
/*      */     
/* 4091 */     GlStateManager.setActiveTexture(33990);
/* 4092 */     GlStateManager.bindTexture(dfbDepthTextures.get(0));
/*      */     
/* 4094 */     if (usedDepthBuffers >= 2) {
/*      */       
/* 4096 */       GlStateManager.setActiveTexture(33995);
/* 4097 */       GlStateManager.bindTexture(dfbDepthTextures.get(1));
/*      */       
/* 4099 */       if (usedDepthBuffers >= 3) {
/*      */         
/* 4101 */         GlStateManager.setActiveTexture(33996);
/* 4102 */         GlStateManager.bindTexture(dfbDepthTextures.get(2));
/*      */       } 
/*      */     } 
/*      */     
/* 4106 */     for (int k = 0; k < usedShadowColorBuffers; k++) {
/*      */       
/* 4108 */       GlStateManager.setActiveTexture(33997 + k);
/* 4109 */       GlStateManager.bindTexture(sfbColorTextures.get(k));
/*      */     } 
/*      */     
/* 4112 */     if (noiseTextureEnabled) {
/*      */       
/* 4114 */       GlStateManager.setActiveTexture(33984 + noiseTexture.getTextureUnit());
/* 4115 */       GlStateManager.bindTexture(noiseTexture.getTextureId());
/*      */     } 
/*      */     
/* 4118 */     bindCustomTextures(customTexturesGbuffers);
/* 4119 */     GlStateManager.setActiveTexture(33984);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkWorldChanged(World world) {
/* 4124 */     if (currentWorld != world) {
/*      */       
/* 4126 */       World oldworld = currentWorld;
/* 4127 */       currentWorld = world;
/* 4128 */       setCameraOffset(mc.getRenderViewEntity());
/* 4129 */       int i = getDimensionId(oldworld);
/* 4130 */       int j = getDimensionId(world);
/*      */       
/* 4132 */       if (j != i) {
/*      */         
/* 4134 */         boolean flag = shaderPackDimensions.contains(Integer.valueOf(i));
/* 4135 */         boolean flag1 = shaderPackDimensions.contains(Integer.valueOf(j));
/*      */         
/* 4137 */         if (flag || flag1)
/*      */         {
/* 4139 */           uninit();
/*      */         }
/*      */       } 
/*      */       
/* 4143 */       Smoother.resetValues();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getDimensionId(World world) {
/* 4149 */     return (world == null) ? Integer.MIN_VALUE : world.provider.getDimensionId();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginRenderPass(int pass, float partialTicks, long finishTimeNano) {
/* 4154 */     if (!isShadowPass) {
/*      */       
/* 4156 */       EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
/* 4157 */       GL11.glViewport(0, 0, renderWidth, renderHeight);
/* 4158 */       activeDrawBuffers = null;
/* 4159 */       ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
/* 4160 */       useProgram(ProgramTextured);
/* 4161 */       checkGLError("end beginRenderPass");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setViewport(int vx, int vy, int vw, int vh) {
/* 4167 */     GlStateManager.colorMask(true, true, true, true);
/*      */     
/* 4169 */     if (isShadowPass) {
/*      */       
/* 4171 */       GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
/*      */     }
/*      */     else {
/*      */       
/* 4175 */       GL11.glViewport(0, 0, renderWidth, renderHeight);
/* 4176 */       EXTFramebufferObject.glBindFramebufferEXT(36160, dfb);
/* 4177 */       isRenderingDfb = true;
/* 4178 */       GlStateManager.enableCull();
/* 4179 */       GlStateManager.enableDepth();
/* 4180 */       setDrawBuffers(drawBuffersNone);
/* 4181 */       useProgram(ProgramTextured);
/* 4182 */       checkGLError("beginRenderPass");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogMode(int value) {
/* 4188 */     fogMode = value;
/*      */     
/* 4190 */     if (fogEnabled)
/*      */     {
/* 4192 */       setProgramUniform1i(uniform_fogMode, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogColor(float r, float g, float b) {
/* 4198 */     fogColorR = r;
/* 4199 */     fogColorG = g;
/* 4200 */     fogColorB = b;
/* 4201 */     setProgramUniform3f(uniform_fogColor, fogColorR, fogColorG, fogColorB);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setClearColor(float red, float green, float blue, float alpha) {
/* 4206 */     GlStateManager.clearColor(red, green, blue, alpha);
/* 4207 */     clearColorR = red;
/* 4208 */     clearColorG = green;
/* 4209 */     clearColorB = blue;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clearRenderBuffer() {
/* 4214 */     if (isShadowPass) {
/*      */       
/* 4216 */       checkGLError("shadow clear pre");
/* 4217 */       EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, sfbDepthTextures.get(0), 0);
/* 4218 */       GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 4219 */       GL20.glDrawBuffers(ProgramShadow.getDrawBuffers());
/* 4220 */       checkFramebufferStatus("shadow clear");
/* 4221 */       GL11.glClear(16640);
/* 4222 */       checkGLError("shadow clear");
/*      */     }
/*      */     else {
/*      */       
/* 4226 */       checkGLError("clear pre");
/*      */       
/* 4228 */       if (gbuffersClear[0]) {
/*      */         
/* 4230 */         Vector4f vector4f = gbuffersClearColor[0];
/*      */         
/* 4232 */         if (vector4f != null)
/*      */         {
/* 4234 */           GL11.glClearColor(vector4f.getX(), vector4f.getY(), vector4f.getZ(), vector4f.getW());
/*      */         }
/*      */         
/* 4237 */         if (dfbColorTexturesFlip.isChanged(0)) {
/*      */           
/* 4239 */           EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, dfbColorTexturesFlip.getB(0), 0);
/* 4240 */           GL20.glDrawBuffers(36064);
/* 4241 */           GL11.glClear(16384);
/* 4242 */           EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, dfbColorTexturesFlip.getA(0), 0);
/*      */         } 
/*      */         
/* 4245 */         GL20.glDrawBuffers(36064);
/* 4246 */         GL11.glClear(16384);
/*      */       } 
/*      */       
/* 4249 */       if (gbuffersClear[1]) {
/*      */         
/* 4251 */         GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
/* 4252 */         Vector4f vector4f2 = gbuffersClearColor[1];
/*      */         
/* 4254 */         if (vector4f2 != null)
/*      */         {
/* 4256 */           GL11.glClearColor(vector4f2.getX(), vector4f2.getY(), vector4f2.getZ(), vector4f2.getW());
/*      */         }
/*      */         
/* 4259 */         if (dfbColorTexturesFlip.isChanged(1)) {
/*      */           
/* 4261 */           EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36065, 3553, dfbColorTexturesFlip.getB(1), 0);
/* 4262 */           GL20.glDrawBuffers(36065);
/* 4263 */           GL11.glClear(16384);
/* 4264 */           EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36065, 3553, dfbColorTexturesFlip.getA(1), 0);
/*      */         } 
/*      */         
/* 4267 */         GL20.glDrawBuffers(36065);
/* 4268 */         GL11.glClear(16384);
/*      */       } 
/*      */       
/* 4271 */       for (int i = 2; i < usedColorBuffers; i++) {
/*      */         
/* 4273 */         if (gbuffersClear[i]) {
/*      */           
/* 4275 */           GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
/* 4276 */           Vector4f vector4f1 = gbuffersClearColor[i];
/*      */           
/* 4278 */           if (vector4f1 != null)
/*      */           {
/* 4280 */             GL11.glClearColor(vector4f1.getX(), vector4f1.getY(), vector4f1.getZ(), vector4f1.getW());
/*      */           }
/*      */           
/* 4283 */           if (dfbColorTexturesFlip.isChanged(i)) {
/*      */             
/* 4285 */             EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + i, 3553, dfbColorTexturesFlip.getB(i), 0);
/* 4286 */             GL20.glDrawBuffers(36064 + i);
/* 4287 */             GL11.glClear(16384);
/* 4288 */             EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + i, 3553, dfbColorTexturesFlip.getA(i), 0);
/*      */           } 
/*      */           
/* 4291 */           GL20.glDrawBuffers(36064 + i);
/* 4292 */           GL11.glClear(16384);
/*      */         } 
/*      */       } 
/*      */       
/* 4296 */       setDrawBuffers(dfbDrawBuffers);
/* 4297 */       checkFramebufferStatus("clear");
/* 4298 */       checkGLError("clear");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setCamera(float partialTicks) {
/* 4304 */     Entity entity = mc.getRenderViewEntity();
/* 4305 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 4306 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 4307 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 4308 */     updateCameraOffset(entity);
/* 4309 */     cameraPositionX = d0 - cameraOffsetX;
/* 4310 */     cameraPositionY = d1;
/* 4311 */     cameraPositionZ = d2 - cameraOffsetZ;
/* 4312 */     GL11.glGetFloat(2983, (FloatBuffer)projection.position(0));
/* 4313 */     SMath.invertMat4FBFA((FloatBuffer)projectionInverse.position(0), (FloatBuffer)projection.position(0), faProjectionInverse, faProjection);
/* 4314 */     projection.position(0);
/* 4315 */     projectionInverse.position(0);
/* 4316 */     GL11.glGetFloat(2982, (FloatBuffer)modelView.position(0));
/* 4317 */     SMath.invertMat4FBFA((FloatBuffer)modelViewInverse.position(0), (FloatBuffer)modelView.position(0), faModelViewInverse, faModelView);
/* 4318 */     modelView.position(0);
/* 4319 */     modelViewInverse.position(0);
/* 4320 */     checkGLError("setCamera");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void updateCameraOffset(Entity viewEntity) {
/* 4325 */     double d0 = Math.abs(cameraPositionX - previousCameraPositionX);
/* 4326 */     double d1 = Math.abs(cameraPositionZ - previousCameraPositionZ);
/* 4327 */     double d2 = Math.abs(cameraPositionX);
/* 4328 */     double d3 = Math.abs(cameraPositionZ);
/*      */     
/* 4330 */     if (d0 > 1000.0D || d1 > 1000.0D || d2 > 1000000.0D || d3 > 1000000.0D)
/*      */     {
/* 4332 */       setCameraOffset(viewEntity);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setCameraOffset(Entity viewEntity) {
/* 4338 */     if (viewEntity == null) {
/*      */       
/* 4340 */       cameraOffsetX = 0;
/* 4341 */       cameraOffsetZ = 0;
/*      */     }
/*      */     else {
/*      */       
/* 4345 */       cameraOffsetX = (int)viewEntity.posX / 1000 * 1000;
/* 4346 */       cameraOffsetZ = (int)viewEntity.posZ / 1000 * 1000;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setCameraShadow(float partialTicks) {
/* 4352 */     Entity entity = mc.getRenderViewEntity();
/* 4353 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 4354 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 4355 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 4356 */     updateCameraOffset(entity);
/* 4357 */     cameraPositionX = d0 - cameraOffsetX;
/* 4358 */     cameraPositionY = d1;
/* 4359 */     cameraPositionZ = d2 - cameraOffsetZ;
/* 4360 */     GL11.glGetFloat(2983, (FloatBuffer)projection.position(0));
/* 4361 */     SMath.invertMat4FBFA((FloatBuffer)projectionInverse.position(0), (FloatBuffer)projection.position(0), faProjectionInverse, faProjection);
/* 4362 */     projection.position(0);
/* 4363 */     projectionInverse.position(0);
/* 4364 */     GL11.glGetFloat(2982, (FloatBuffer)modelView.position(0));
/* 4365 */     SMath.invertMat4FBFA((FloatBuffer)modelViewInverse.position(0), (FloatBuffer)modelView.position(0), faModelViewInverse, faModelView);
/* 4366 */     modelView.position(0);
/* 4367 */     modelViewInverse.position(0);
/* 4368 */     GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
/* 4369 */     GL11.glMatrixMode(5889);
/* 4370 */     GL11.glLoadIdentity();
/*      */     
/* 4372 */     if (shadowMapIsOrtho) {
/*      */       
/* 4374 */       GL11.glOrtho(-shadowMapHalfPlane, shadowMapHalfPlane, -shadowMapHalfPlane, shadowMapHalfPlane, 0.05000000074505806D, 256.0D);
/*      */     }
/*      */     else {
/*      */       
/* 4378 */       GLU.gluPerspective(shadowMapFOV, shadowMapWidth / shadowMapHeight, 0.05F, 256.0F);
/*      */     } 
/*      */     
/* 4381 */     GL11.glMatrixMode(5888);
/* 4382 */     GL11.glLoadIdentity();
/* 4383 */     GL11.glTranslatef(0.0F, 0.0F, -100.0F);
/* 4384 */     GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
/* 4385 */     celestialAngle = mc.theWorld.getCelestialAngle(partialTicks);
/* 4386 */     sunAngle = (celestialAngle < 0.75F) ? (celestialAngle + 0.25F) : (celestialAngle - 0.75F);
/* 4387 */     float f = celestialAngle * -360.0F;
/* 4388 */     float f1 = (shadowAngleInterval > 0.0F) ? (f % shadowAngleInterval - shadowAngleInterval * 0.5F) : 0.0F;
/*      */     
/* 4390 */     if (sunAngle <= 0.5D) {
/*      */       
/* 4392 */       GL11.glRotatef(f - f1, 0.0F, 0.0F, 1.0F);
/* 4393 */       GL11.glRotatef(sunPathRotation, 1.0F, 0.0F, 0.0F);
/* 4394 */       shadowAngle = sunAngle;
/*      */     }
/*      */     else {
/*      */       
/* 4398 */       GL11.glRotatef(f + 180.0F - f1, 0.0F, 0.0F, 1.0F);
/* 4399 */       GL11.glRotatef(sunPathRotation, 1.0F, 0.0F, 0.0F);
/* 4400 */       shadowAngle = sunAngle - 0.5F;
/*      */     } 
/*      */     
/* 4403 */     if (shadowMapIsOrtho) {
/*      */       
/* 4405 */       float f2 = shadowIntervalSize;
/* 4406 */       float f3 = f2 / 2.0F;
/* 4407 */       GL11.glTranslatef((float)d0 % f2 - f3, (float)d1 % f2 - f3, (float)d2 % f2 - f3);
/*      */     } 
/*      */     
/* 4410 */     float f9 = sunAngle * 6.2831855F;
/* 4411 */     float f10 = (float)Math.cos(f9);
/* 4412 */     float f4 = (float)Math.sin(f9);
/* 4413 */     float f5 = sunPathRotation * 6.2831855F;
/* 4414 */     float f6 = f10;
/* 4415 */     float f7 = f4 * (float)Math.cos(f5);
/* 4416 */     float f8 = f4 * (float)Math.sin(f5);
/*      */     
/* 4418 */     if (sunAngle > 0.5D) {
/*      */       
/* 4420 */       f6 = -f10;
/* 4421 */       f7 = -f7;
/* 4422 */       f8 = -f8;
/*      */     } 
/*      */     
/* 4425 */     shadowLightPositionVector[0] = f6;
/* 4426 */     shadowLightPositionVector[1] = f7;
/* 4427 */     shadowLightPositionVector[2] = f8;
/* 4428 */     shadowLightPositionVector[3] = 0.0F;
/* 4429 */     GL11.glGetFloat(2983, (FloatBuffer)shadowProjection.position(0));
/* 4430 */     SMath.invertMat4FBFA((FloatBuffer)shadowProjectionInverse.position(0), (FloatBuffer)shadowProjection.position(0), faShadowProjectionInverse, faShadowProjection);
/* 4431 */     shadowProjection.position(0);
/* 4432 */     shadowProjectionInverse.position(0);
/* 4433 */     GL11.glGetFloat(2982, (FloatBuffer)shadowModelView.position(0));
/* 4434 */     SMath.invertMat4FBFA((FloatBuffer)shadowModelViewInverse.position(0), (FloatBuffer)shadowModelView.position(0), faShadowModelViewInverse, faShadowModelView);
/* 4435 */     shadowModelView.position(0);
/* 4436 */     shadowModelViewInverse.position(0);
/* 4437 */     setProgramUniformMatrix4ARB(uniform_gbufferProjection, false, projection);
/* 4438 */     setProgramUniformMatrix4ARB(uniform_gbufferProjectionInverse, false, projectionInverse);
/* 4439 */     setProgramUniformMatrix4ARB(uniform_gbufferPreviousProjection, false, previousProjection);
/* 4440 */     setProgramUniformMatrix4ARB(uniform_gbufferModelView, false, modelView);
/* 4441 */     setProgramUniformMatrix4ARB(uniform_gbufferModelViewInverse, false, modelViewInverse);
/* 4442 */     setProgramUniformMatrix4ARB(uniform_gbufferPreviousModelView, false, previousModelView);
/* 4443 */     setProgramUniformMatrix4ARB(uniform_shadowProjection, false, shadowProjection);
/* 4444 */     setProgramUniformMatrix4ARB(uniform_shadowProjectionInverse, false, shadowProjectionInverse);
/* 4445 */     setProgramUniformMatrix4ARB(uniform_shadowModelView, false, shadowModelView);
/* 4446 */     setProgramUniformMatrix4ARB(uniform_shadowModelViewInverse, false, shadowModelViewInverse);
/* 4447 */     mc.gameSettings.thirdPersonView = 1;
/* 4448 */     checkGLError("setCamera");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void preCelestialRotate() {
/* 4453 */     GL11.glRotatef(sunPathRotation * 1.0F, 0.0F, 0.0F, 1.0F);
/* 4454 */     checkGLError("preCelestialRotate");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void postCelestialRotate() {
/* 4459 */     FloatBuffer floatbuffer = tempMatrixDirectBuffer;
/* 4460 */     floatbuffer.clear();
/* 4461 */     GL11.glGetFloat(2982, floatbuffer);
/* 4462 */     floatbuffer.get(tempMat, 0, 16);
/* 4463 */     SMath.multiplyMat4xVec4(sunPosition, tempMat, sunPosModelView);
/* 4464 */     SMath.multiplyMat4xVec4(moonPosition, tempMat, moonPosModelView);
/* 4465 */     System.arraycopy((shadowAngle == sunAngle) ? sunPosition : moonPosition, 0, shadowLightPosition, 0, 3);
/* 4466 */     setProgramUniform3f(uniform_sunPosition, sunPosition[0], sunPosition[1], sunPosition[2]);
/* 4467 */     setProgramUniform3f(uniform_moonPosition, moonPosition[0], moonPosition[1], moonPosition[2]);
/* 4468 */     setProgramUniform3f(uniform_shadowLightPosition, shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
/*      */     
/* 4470 */     if (customUniforms != null)
/*      */     {
/* 4472 */       customUniforms.update();
/*      */     }
/*      */     
/* 4475 */     checkGLError("postCelestialRotate");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setUpPosition() {
/* 4480 */     FloatBuffer floatbuffer = tempMatrixDirectBuffer;
/* 4481 */     floatbuffer.clear();
/* 4482 */     GL11.glGetFloat(2982, floatbuffer);
/* 4483 */     floatbuffer.get(tempMat, 0, 16);
/* 4484 */     SMath.multiplyMat4xVec4(upPosition, tempMat, upPosModelView);
/* 4485 */     setProgramUniform3f(uniform_upPosition, upPosition[0], upPosition[1], upPosition[2]);
/*      */     
/* 4487 */     if (customUniforms != null)
/*      */     {
/* 4489 */       customUniforms.update();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void genCompositeMipmap() {
/* 4495 */     if (hasGlGenMipmap) {
/*      */       
/* 4497 */       for (int i = 0; i < usedColorBuffers; i++) {
/*      */         
/* 4499 */         if ((activeCompositeMipmapSetting & 1 << i) != 0) {
/*      */           
/* 4501 */           GlStateManager.setActiveTexture(33984 + colorTextureImageUnit[i]);
/* 4502 */           GL11.glTexParameteri(3553, 10241, 9987);
/* 4503 */           GL30.glGenerateMipmap(3553);
/*      */         } 
/*      */       } 
/*      */       
/* 4507 */       GlStateManager.setActiveTexture(33984);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawComposite() {
/* 4513 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 4514 */     drawCompositeQuad();
/* 4515 */     int i = activeProgram.getCountInstances();
/*      */     
/* 4517 */     if (i > 1) {
/*      */       
/* 4519 */       for (int j = 1; j < i; j++) {
/*      */         
/* 4521 */         uniform_instanceId.setValue(j);
/* 4522 */         drawCompositeQuad();
/*      */       } 
/*      */       
/* 4525 */       uniform_instanceId.setValue(0);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void drawCompositeQuad() {
/* 4531 */     if (!canRenderQuads()) {
/*      */       
/* 4533 */       GL11.glBegin(5);
/* 4534 */       GL11.glTexCoord2f(0.0F, 0.0F);
/* 4535 */       GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 4536 */       GL11.glTexCoord2f(1.0F, 0.0F);
/* 4537 */       GL11.glVertex3f(1.0F, 0.0F, 0.0F);
/* 4538 */       GL11.glTexCoord2f(0.0F, 1.0F);
/* 4539 */       GL11.glVertex3f(0.0F, 1.0F, 0.0F);
/* 4540 */       GL11.glTexCoord2f(1.0F, 1.0F);
/* 4541 */       GL11.glVertex3f(1.0F, 1.0F, 0.0F);
/* 4542 */       GL11.glEnd();
/*      */     }
/*      */     else {
/*      */       
/* 4546 */       GL11.glBegin(7);
/* 4547 */       GL11.glTexCoord2f(0.0F, 0.0F);
/* 4548 */       GL11.glVertex3f(0.0F, 0.0F, 0.0F);
/* 4549 */       GL11.glTexCoord2f(1.0F, 0.0F);
/* 4550 */       GL11.glVertex3f(1.0F, 0.0F, 0.0F);
/* 4551 */       GL11.glTexCoord2f(1.0F, 1.0F);
/* 4552 */       GL11.glVertex3f(1.0F, 1.0F, 0.0F);
/* 4553 */       GL11.glTexCoord2f(0.0F, 1.0F);
/* 4554 */       GL11.glVertex3f(0.0F, 1.0F, 0.0F);
/* 4555 */       GL11.glEnd();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void renderDeferred() {
/* 4561 */     if (!isShadowPass) {
/*      */       
/* 4563 */       boolean flag = checkBufferFlip(ProgramDeferredPre);
/*      */       
/* 4565 */       if (hasDeferredPrograms) {
/*      */         
/* 4567 */         checkGLError("pre-render Deferred");
/* 4568 */         renderComposites(ProgramsDeferred, false);
/* 4569 */         flag = true;
/*      */       } 
/*      */       
/* 4572 */       if (flag) {
/*      */         
/* 4574 */         bindGbuffersTextures();
/*      */         
/* 4576 */         for (int i = 0; i < usedColorBuffers; i++)
/*      */         {
/* 4578 */           EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + i, 3553, dfbColorTexturesFlip.getA(i), 0);
/*      */         }
/*      */         
/* 4581 */         if (ProgramWater.getDrawBuffers() != null) {
/*      */           
/* 4583 */           setDrawBuffers(ProgramWater.getDrawBuffers());
/*      */         }
/*      */         else {
/*      */           
/* 4587 */           setDrawBuffers(dfbDrawBuffers);
/*      */         } 
/*      */         
/* 4590 */         GlStateManager.setActiveTexture(33984);
/* 4591 */         mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void renderCompositeFinal() {
/* 4598 */     if (!isShadowPass) {
/*      */       
/* 4600 */       checkBufferFlip(ProgramCompositePre);
/* 4601 */       checkGLError("pre-render CompositeFinal");
/* 4602 */       renderComposites(ProgramsComposite, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean checkBufferFlip(Program program) {
/* 4608 */     boolean flag = false;
/* 4609 */     Boolean[] aboolean = program.getBuffersFlip();
/*      */     
/* 4611 */     for (int i = 0; i < usedColorBuffers; i++) {
/*      */       
/* 4613 */       if (Config.isTrue(aboolean[i])) {
/*      */         
/* 4615 */         dfbColorTexturesFlip.flip(i);
/* 4616 */         flag = true;
/*      */       } 
/*      */     } 
/*      */     
/* 4620 */     return flag;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void renderComposites(Program[] ps, boolean renderFinal) {
/* 4625 */     if (!isShadowPass) {
/*      */       
/* 4627 */       GL11.glPushMatrix();
/* 4628 */       GL11.glLoadIdentity();
/* 4629 */       GL11.glMatrixMode(5889);
/* 4630 */       GL11.glPushMatrix();
/* 4631 */       GL11.glLoadIdentity();
/* 4632 */       GL11.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
/* 4633 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 4634 */       GlStateManager.enableTexture2D();
/* 4635 */       GlStateManager.disableAlpha();
/* 4636 */       GlStateManager.disableBlend();
/* 4637 */       GlStateManager.enableDepth();
/* 4638 */       GlStateManager.depthFunc(519);
/* 4639 */       GlStateManager.depthMask(false);
/* 4640 */       GlStateManager.disableLighting();
/*      */       
/* 4642 */       if (usedShadowDepthBuffers >= 1) {
/*      */         
/* 4644 */         GlStateManager.setActiveTexture(33988);
/* 4645 */         GlStateManager.bindTexture(sfbDepthTextures.get(0));
/*      */         
/* 4647 */         if (usedShadowDepthBuffers >= 2) {
/*      */           
/* 4649 */           GlStateManager.setActiveTexture(33989);
/* 4650 */           GlStateManager.bindTexture(sfbDepthTextures.get(1));
/*      */         } 
/*      */       } 
/*      */       
/* 4654 */       for (int i = 0; i < usedColorBuffers; i++) {
/*      */         
/* 4656 */         GlStateManager.setActiveTexture(33984 + colorTextureImageUnit[i]);
/* 4657 */         GlStateManager.bindTexture(dfbColorTexturesFlip.getA(i));
/*      */       } 
/*      */       
/* 4660 */       GlStateManager.setActiveTexture(33990);
/* 4661 */       GlStateManager.bindTexture(dfbDepthTextures.get(0));
/*      */       
/* 4663 */       if (usedDepthBuffers >= 2) {
/*      */         
/* 4665 */         GlStateManager.setActiveTexture(33995);
/* 4666 */         GlStateManager.bindTexture(dfbDepthTextures.get(1));
/*      */         
/* 4668 */         if (usedDepthBuffers >= 3) {
/*      */           
/* 4670 */           GlStateManager.setActiveTexture(33996);
/* 4671 */           GlStateManager.bindTexture(dfbDepthTextures.get(2));
/*      */         } 
/*      */       } 
/*      */       
/* 4675 */       for (int k = 0; k < usedShadowColorBuffers; k++) {
/*      */         
/* 4677 */         GlStateManager.setActiveTexture(33997 + k);
/* 4678 */         GlStateManager.bindTexture(sfbColorTextures.get(k));
/*      */       } 
/*      */       
/* 4681 */       if (noiseTextureEnabled) {
/*      */         
/* 4683 */         GlStateManager.setActiveTexture(33984 + noiseTexture.getTextureUnit());
/* 4684 */         GlStateManager.bindTexture(noiseTexture.getTextureId());
/*      */       } 
/*      */       
/* 4687 */       if (renderFinal) {
/*      */         
/* 4689 */         bindCustomTextures(customTexturesComposite);
/*      */       }
/*      */       else {
/*      */         
/* 4693 */         bindCustomTextures(customTexturesDeferred);
/*      */       } 
/*      */       
/* 4696 */       GlStateManager.setActiveTexture(33984);
/*      */       
/* 4698 */       for (int l = 0; l < usedColorBuffers; l++)
/*      */       {
/* 4700 */         EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + l, 3553, dfbColorTexturesFlip.getB(l), 0);
/*      */       }
/*      */       
/* 4703 */       EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36096, 3553, dfbDepthTextures.get(0), 0);
/* 4704 */       GL20.glDrawBuffers(dfbDrawBuffers);
/* 4705 */       checkGLError("pre-composite");
/*      */       
/* 4707 */       for (int i1 = 0; i1 < ps.length; i1++) {
/*      */         
/* 4709 */         Program program = ps[i1];
/*      */         
/* 4711 */         if (program.getId() != 0) {
/*      */           
/* 4713 */           useProgram(program);
/* 4714 */           checkGLError(program.getName());
/*      */           
/* 4716 */           if (activeCompositeMipmapSetting != 0)
/*      */           {
/* 4718 */             genCompositeMipmap();
/*      */           }
/*      */           
/* 4721 */           preDrawComposite();
/* 4722 */           drawComposite();
/* 4723 */           postDrawComposite();
/*      */           
/* 4725 */           for (int j = 0; j < usedColorBuffers; j++) {
/*      */             
/* 4727 */             if (program.getToggleColorTextures()[j]) {
/*      */               
/* 4729 */               dfbColorTexturesFlip.flip(j);
/* 4730 */               GlStateManager.setActiveTexture(33984 + colorTextureImageUnit[j]);
/* 4731 */               GlStateManager.bindTexture(dfbColorTexturesFlip.getA(j));
/* 4732 */               EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064 + j, 3553, dfbColorTexturesFlip.getB(j), 0);
/*      */             } 
/*      */           } 
/*      */           
/* 4736 */           GlStateManager.setActiveTexture(33984);
/*      */         } 
/*      */       } 
/*      */       
/* 4740 */       checkGLError("composite");
/*      */       
/* 4742 */       if (renderFinal) {
/*      */         
/* 4744 */         renderFinal();
/* 4745 */         isCompositeRendered = true;
/*      */       } 
/*      */       
/* 4748 */       GlStateManager.enableLighting();
/* 4749 */       GlStateManager.enableTexture2D();
/* 4750 */       GlStateManager.enableAlpha();
/* 4751 */       GlStateManager.enableBlend();
/* 4752 */       GlStateManager.depthFunc(515);
/* 4753 */       GlStateManager.depthMask(true);
/* 4754 */       GL11.glPopMatrix();
/* 4755 */       GL11.glMatrixMode(5888);
/* 4756 */       GL11.glPopMatrix();
/* 4757 */       useProgram(ProgramNone);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void preDrawComposite() {
/* 4763 */     RenderScale renderscale = activeProgram.getRenderScale();
/*      */     
/* 4765 */     if (renderscale != null) {
/*      */       
/* 4767 */       int i = (int)(renderWidth * renderscale.getOffsetX());
/* 4768 */       int j = (int)(renderHeight * renderscale.getOffsetY());
/* 4769 */       int k = (int)(renderWidth * renderscale.getScale());
/* 4770 */       int l = (int)(renderHeight * renderscale.getScale());
/* 4771 */       GL11.glViewport(i, j, k, l);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void postDrawComposite() {
/* 4777 */     RenderScale renderscale = activeProgram.getRenderScale();
/*      */     
/* 4779 */     if (renderscale != null)
/*      */     {
/* 4781 */       GL11.glViewport(0, 0, renderWidth, renderHeight);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void renderFinal() {
/* 4787 */     isRenderingDfb = false;
/* 4788 */     mc.getFramebuffer().bindFramebuffer(true);
/* 4789 */     OpenGlHelper.glFramebufferTexture2D(OpenGlHelper.GL_FRAMEBUFFER, OpenGlHelper.GL_COLOR_ATTACHMENT0, 3553, (mc.getFramebuffer()).framebufferTexture, 0);
/* 4790 */     GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
/*      */     
/* 4792 */     if (EntityRenderer.anaglyphEnable) {
/*      */       
/* 4794 */       boolean flag = (EntityRenderer.anaglyphField != 0);
/* 4795 */       GlStateManager.colorMask(flag, !flag, !flag, true);
/*      */     } 
/*      */     
/* 4798 */     GlStateManager.depthMask(true);
/* 4799 */     GL11.glClearColor(clearColorR, clearColorG, clearColorB, 1.0F);
/* 4800 */     GL11.glClear(16640);
/* 4801 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 4802 */     GlStateManager.enableTexture2D();
/* 4803 */     GlStateManager.disableAlpha();
/* 4804 */     GlStateManager.disableBlend();
/* 4805 */     GlStateManager.enableDepth();
/* 4806 */     GlStateManager.depthFunc(519);
/* 4807 */     GlStateManager.depthMask(false);
/* 4808 */     checkGLError("pre-final");
/* 4809 */     useProgram(ProgramFinal);
/* 4810 */     checkGLError("final");
/*      */     
/* 4812 */     if (activeCompositeMipmapSetting != 0)
/*      */     {
/* 4814 */       genCompositeMipmap();
/*      */     }
/*      */     
/* 4817 */     drawComposite();
/* 4818 */     checkGLError("renderCompositeFinal");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endRender() {
/* 4823 */     if (isShadowPass) {
/*      */       
/* 4825 */       checkGLError("shadow endRender");
/*      */     }
/*      */     else {
/*      */       
/* 4829 */       if (!isCompositeRendered)
/*      */       {
/* 4831 */         renderCompositeFinal();
/*      */       }
/*      */       
/* 4834 */       isRenderingWorld = false;
/* 4835 */       GlStateManager.colorMask(true, true, true, true);
/* 4836 */       useProgram(ProgramNone);
/* 4837 */       RenderHelper.disableStandardItemLighting();
/* 4838 */       checkGLError("endRender end");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginSky() {
/* 4844 */     isRenderingSky = true;
/* 4845 */     fogEnabled = true;
/* 4846 */     setDrawBuffers(dfbDrawBuffers);
/* 4847 */     useProgram(ProgramSkyTextured);
/* 4848 */     pushEntity(-2, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setSkyColor(Vec3 v3color) {
/* 4853 */     skyColorR = (float)v3color.xCoord;
/* 4854 */     skyColorG = (float)v3color.yCoord;
/* 4855 */     skyColorB = (float)v3color.zCoord;
/* 4856 */     setProgramUniform3f(uniform_skyColor, skyColorR, skyColorG, skyColorB);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawHorizon() {
/* 4861 */     WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
/* 4862 */     float f = (mc.gameSettings.renderDistanceChunks * 16);
/* 4863 */     double d0 = f * 0.9238D;
/* 4864 */     double d1 = f * 0.3826D;
/* 4865 */     double d2 = -d1;
/* 4866 */     double d3 = -d0;
/* 4867 */     double d4 = 16.0D;
/* 4868 */     double d5 = -cameraPositionY;
/* 4869 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/* 4870 */     worldrenderer.pos(d2, d5, d3).endVertex();
/* 4871 */     worldrenderer.pos(d2, d4, d3).endVertex();
/* 4872 */     worldrenderer.pos(d3, d4, d2).endVertex();
/* 4873 */     worldrenderer.pos(d3, d5, d2).endVertex();
/* 4874 */     worldrenderer.pos(d3, d5, d2).endVertex();
/* 4875 */     worldrenderer.pos(d3, d4, d2).endVertex();
/* 4876 */     worldrenderer.pos(d3, d4, d1).endVertex();
/* 4877 */     worldrenderer.pos(d3, d5, d1).endVertex();
/* 4878 */     worldrenderer.pos(d3, d5, d1).endVertex();
/* 4879 */     worldrenderer.pos(d3, d4, d1).endVertex();
/* 4880 */     worldrenderer.pos(d2, d4, d0).endVertex();
/* 4881 */     worldrenderer.pos(d2, d5, d0).endVertex();
/* 4882 */     worldrenderer.pos(d2, d5, d0).endVertex();
/* 4883 */     worldrenderer.pos(d2, d4, d0).endVertex();
/* 4884 */     worldrenderer.pos(d1, d4, d0).endVertex();
/* 4885 */     worldrenderer.pos(d1, d5, d0).endVertex();
/* 4886 */     worldrenderer.pos(d1, d5, d0).endVertex();
/* 4887 */     worldrenderer.pos(d1, d4, d0).endVertex();
/* 4888 */     worldrenderer.pos(d0, d4, d1).endVertex();
/* 4889 */     worldrenderer.pos(d0, d5, d1).endVertex();
/* 4890 */     worldrenderer.pos(d0, d5, d1).endVertex();
/* 4891 */     worldrenderer.pos(d0, d4, d1).endVertex();
/* 4892 */     worldrenderer.pos(d0, d4, d2).endVertex();
/* 4893 */     worldrenderer.pos(d0, d5, d2).endVertex();
/* 4894 */     worldrenderer.pos(d0, d5, d2).endVertex();
/* 4895 */     worldrenderer.pos(d0, d4, d2).endVertex();
/* 4896 */     worldrenderer.pos(d1, d4, d3).endVertex();
/* 4897 */     worldrenderer.pos(d1, d5, d3).endVertex();
/* 4898 */     worldrenderer.pos(d1, d5, d3).endVertex();
/* 4899 */     worldrenderer.pos(d1, d4, d3).endVertex();
/* 4900 */     worldrenderer.pos(d2, d4, d3).endVertex();
/* 4901 */     worldrenderer.pos(d2, d5, d3).endVertex();
/* 4902 */     worldrenderer.pos(d3, d5, d3).endVertex();
/* 4903 */     worldrenderer.pos(d3, d5, d0).endVertex();
/* 4904 */     worldrenderer.pos(d0, d5, d0).endVertex();
/* 4905 */     worldrenderer.pos(d0, d5, d3).endVertex();
/* 4906 */     Tessellator.getInstance().draw();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void preSkyList() {
/* 4911 */     setUpPosition();
/* 4912 */     GL11.glColor3f(fogColorR, fogColorG, fogColorB);
/* 4913 */     drawHorizon();
/* 4914 */     GL11.glColor3f(skyColorR, skyColorG, skyColorB);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endSky() {
/* 4919 */     isRenderingSky = false;
/* 4920 */     setDrawBuffers(dfbDrawBuffers);
/* 4921 */     useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
/* 4922 */     popEntity();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginUpdateChunks() {
/* 4927 */     checkGLError("beginUpdateChunks1");
/* 4928 */     checkFramebufferStatus("beginUpdateChunks1");
/*      */     
/* 4930 */     if (!isShadowPass)
/*      */     {
/* 4932 */       useProgram(ProgramTerrain);
/*      */     }
/*      */     
/* 4935 */     checkGLError("beginUpdateChunks2");
/* 4936 */     checkFramebufferStatus("beginUpdateChunks2");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endUpdateChunks() {
/* 4941 */     checkGLError("endUpdateChunks1");
/* 4942 */     checkFramebufferStatus("endUpdateChunks1");
/*      */     
/* 4944 */     if (!isShadowPass)
/*      */     {
/* 4946 */       useProgram(ProgramTerrain);
/*      */     }
/*      */     
/* 4949 */     checkGLError("endUpdateChunks2");
/* 4950 */     checkFramebufferStatus("endUpdateChunks2");
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean shouldRenderClouds(GameSettings gs) {
/* 4955 */     if (!shaderPackLoaded)
/*      */     {
/* 4957 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 4961 */     checkGLError("shouldRenderClouds");
/* 4962 */     return isShadowPass ? configCloudShadow : ((gs.clouds > 0));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void beginClouds() {
/* 4968 */     fogEnabled = true;
/* 4969 */     pushEntity(-3, 0);
/* 4970 */     useProgram(ProgramClouds);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endClouds() {
/* 4975 */     disableFog();
/* 4976 */     popEntity();
/* 4977 */     useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginEntities() {
/* 4982 */     if (isRenderingWorld)
/*      */     {
/* 4984 */       useProgram(ProgramEntities);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void nextEntity(Entity entity) {
/* 4990 */     if (isRenderingWorld) {
/*      */       
/* 4992 */       useProgram(ProgramEntities);
/* 4993 */       setEntityId(entity);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setEntityId(Entity entity) {
/* 4999 */     if (uniform_entityId.isDefined()) {
/*      */       
/* 5001 */       int i = EntityUtils.getEntityIdByClass(entity);
/* 5002 */       int j = EntityAliases.getEntityAliasId(i);
/*      */       
/* 5004 */       if (j >= 0)
/*      */       {
/* 5006 */         i = j;
/*      */       }
/*      */       
/* 5009 */       uniform_entityId.setValue(i);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginSpiderEyes() {
/* 5015 */     if (isRenderingWorld && ProgramSpiderEyes.getId() != ProgramNone.getId()) {
/*      */       
/* 5017 */       useProgram(ProgramSpiderEyes);
/* 5018 */       GlStateManager.enableAlpha();
/* 5019 */       GlStateManager.alphaFunc(516, 0.0F);
/* 5020 */       GlStateManager.blendFunc(770, 771);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endSpiderEyes() {
/* 5026 */     if (isRenderingWorld && ProgramSpiderEyes.getId() != ProgramNone.getId()) {
/*      */       
/* 5028 */       useProgram(ProgramEntities);
/* 5029 */       GlStateManager.disableAlpha();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endEntities() {
/* 5035 */     if (isRenderingWorld) {
/*      */       
/* 5037 */       setEntityId((Entity)null);
/* 5038 */       useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginEntitiesGlowing() {
/* 5044 */     if (isRenderingWorld)
/*      */     {
/* 5046 */       isEntitiesGlowing = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endEntitiesGlowing() {
/* 5052 */     if (isRenderingWorld)
/*      */     {
/* 5054 */       isEntitiesGlowing = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setEntityColor(float r, float g, float b, float a) {
/* 5060 */     if (isRenderingWorld && !isShadowPass)
/*      */     {
/* 5062 */       uniform_entityColor.setValue(r, g, b, a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginLivingDamage() {
/* 5068 */     if (isRenderingWorld) {
/*      */       
/* 5070 */       ShadersTex.bindTexture(defaultTexture);
/*      */       
/* 5072 */       if (!isShadowPass)
/*      */       {
/* 5074 */         setDrawBuffers(drawBuffersColorAtt0);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endLivingDamage() {
/* 5081 */     if (isRenderingWorld && !isShadowPass)
/*      */     {
/* 5083 */       setDrawBuffers(ProgramEntities.getDrawBuffers());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginBlockEntities() {
/* 5089 */     if (isRenderingWorld) {
/*      */       
/* 5091 */       checkGLError("beginBlockEntities");
/* 5092 */       useProgram(ProgramBlock);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void nextBlockEntity(TileEntity tileEntity) {
/* 5098 */     if (isRenderingWorld) {
/*      */       
/* 5100 */       checkGLError("nextBlockEntity");
/* 5101 */       useProgram(ProgramBlock);
/* 5102 */       setBlockEntityId(tileEntity);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setBlockEntityId(TileEntity tileEntity) {
/* 5108 */     if (uniform_blockEntityId.isDefined()) {
/*      */       
/* 5110 */       int i = getBlockEntityId(tileEntity);
/* 5111 */       uniform_blockEntityId.setValue(i);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getBlockEntityId(TileEntity tileEntity) {
/* 5117 */     if (tileEntity == null)
/*      */     {
/* 5119 */       return -1;
/*      */     }
/*      */ 
/*      */     
/* 5123 */     Block block = tileEntity.getBlockType();
/*      */     
/* 5125 */     if (block == null)
/*      */     {
/* 5127 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 5131 */     int i = Block.getIdFromBlock(block);
/* 5132 */     int j = tileEntity.getBlockMetadata();
/* 5133 */     int k = BlockAliases.getBlockAliasId(i, j);
/*      */     
/* 5135 */     if (k >= 0)
/*      */     {
/* 5137 */       i = k;
/*      */     }
/*      */     
/* 5140 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void endBlockEntities() {
/* 5147 */     if (isRenderingWorld) {
/*      */       
/* 5149 */       checkGLError("endBlockEntities");
/* 5150 */       setBlockEntityId((TileEntity)null);
/* 5151 */       useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
/* 5152 */       ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginLitParticles() {
/* 5158 */     useProgram(ProgramTexturedLit);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginParticles() {
/* 5163 */     useProgram(ProgramTextured);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endParticles() {
/* 5168 */     useProgram(ProgramTexturedLit);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void readCenterDepth() {
/* 5173 */     if (!isShadowPass && centerDepthSmoothEnabled) {
/*      */       
/* 5175 */       tempDirectFloatBuffer.clear();
/* 5176 */       GL11.glReadPixels(renderWidth / 2, renderHeight / 2, 1, 1, 6402, 5126, tempDirectFloatBuffer);
/* 5177 */       centerDepth = tempDirectFloatBuffer.get(0);
/* 5178 */       float f = (float)diffSystemTime * 0.01F;
/* 5179 */       float f1 = (float)Math.exp(Math.log(0.5D) * f / centerDepthSmoothHalflife);
/* 5180 */       centerDepthSmooth = centerDepthSmooth * f1 + centerDepth * (1.0F - f1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginWeather() {
/* 5186 */     if (!isShadowPass) {
/*      */       
/* 5188 */       if (usedDepthBuffers >= 3) {
/*      */         
/* 5190 */         GlStateManager.setActiveTexture(33996);
/* 5191 */         GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, renderWidth, renderHeight);
/* 5192 */         GlStateManager.setActiveTexture(33984);
/*      */       } 
/*      */       
/* 5195 */       GlStateManager.enableDepth();
/* 5196 */       GlStateManager.enableBlend();
/* 5197 */       GlStateManager.blendFunc(770, 771);
/* 5198 */       GlStateManager.enableAlpha();
/* 5199 */       useProgram(ProgramWeather);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endWeather() {
/* 5205 */     GlStateManager.disableBlend();
/* 5206 */     useProgram(ProgramTexturedLit);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void preWater() {
/* 5211 */     if (usedDepthBuffers >= 2) {
/*      */       
/* 5213 */       GlStateManager.setActiveTexture(33995);
/* 5214 */       checkGLError("pre copy depth");
/* 5215 */       GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, renderWidth, renderHeight);
/* 5216 */       checkGLError("copy depth");
/* 5217 */       GlStateManager.setActiveTexture(33984);
/*      */     } 
/*      */     
/* 5220 */     ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginWater() {
/* 5225 */     if (isRenderingWorld)
/*      */     {
/* 5227 */       if (!isShadowPass) {
/*      */         
/* 5229 */         renderDeferred();
/* 5230 */         useProgram(ProgramWater);
/* 5231 */         GlStateManager.enableBlend();
/* 5232 */         GlStateManager.depthMask(true);
/*      */       }
/*      */       else {
/*      */         
/* 5236 */         GlStateManager.depthMask(true);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endWater() {
/* 5243 */     if (isRenderingWorld) {
/*      */       
/* 5245 */       if (isShadowPass);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5250 */       useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void applyHandDepth() {
/* 5256 */     if (configHandDepthMul != 1.0D)
/*      */     {
/* 5258 */       GL11.glScaled(1.0D, 1.0D, configHandDepthMul);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginHand(boolean translucent) {
/* 5264 */     GL11.glMatrixMode(5888);
/* 5265 */     GL11.glPushMatrix();
/* 5266 */     GL11.glMatrixMode(5889);
/* 5267 */     GL11.glPushMatrix();
/* 5268 */     GL11.glMatrixMode(5888);
/*      */     
/* 5270 */     if (translucent) {
/*      */       
/* 5272 */       useProgram(ProgramHandWater);
/*      */     }
/*      */     else {
/*      */       
/* 5276 */       useProgram(ProgramHand);
/*      */     } 
/*      */     
/* 5279 */     checkGLError("beginHand");
/* 5280 */     checkFramebufferStatus("beginHand");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endHand() {
/* 5285 */     checkGLError("pre endHand");
/* 5286 */     checkFramebufferStatus("pre endHand");
/* 5287 */     GL11.glMatrixMode(5889);
/* 5288 */     GL11.glPopMatrix();
/* 5289 */     GL11.glMatrixMode(5888);
/* 5290 */     GL11.glPopMatrix();
/* 5291 */     GlStateManager.blendFunc(770, 771);
/* 5292 */     checkGLError("endHand");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginFPOverlay() {
/* 5297 */     GlStateManager.disableLighting();
/* 5298 */     GlStateManager.disableBlend();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void endFPOverlay() {}
/*      */ 
/*      */   
/*      */   public static void glEnableWrapper(int cap) {
/* 5307 */     GL11.glEnable(cap);
/*      */     
/* 5309 */     if (cap == 3553) {
/*      */       
/* 5311 */       enableTexture2D();
/*      */     }
/* 5313 */     else if (cap == 2912) {
/*      */       
/* 5315 */       enableFog();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glDisableWrapper(int cap) {
/* 5321 */     GL11.glDisable(cap);
/*      */     
/* 5323 */     if (cap == 3553) {
/*      */       
/* 5325 */       disableTexture2D();
/*      */     }
/* 5327 */     else if (cap == 2912) {
/*      */       
/* 5329 */       disableFog();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sglEnableT2D(int cap) {
/* 5335 */     GL11.glEnable(cap);
/* 5336 */     enableTexture2D();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sglDisableT2D(int cap) {
/* 5341 */     GL11.glDisable(cap);
/* 5342 */     disableTexture2D();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sglEnableFog(int cap) {
/* 5347 */     GL11.glEnable(cap);
/* 5348 */     enableFog();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sglDisableFog(int cap) {
/* 5353 */     GL11.glDisable(cap);
/* 5354 */     disableFog();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableTexture2D() {
/* 5359 */     if (isRenderingSky) {
/*      */       
/* 5361 */       useProgram(ProgramSkyTextured);
/*      */     }
/* 5363 */     else if (activeProgram == ProgramBasic) {
/*      */       
/* 5365 */       useProgram(lightmapEnabled ? ProgramTexturedLit : ProgramTextured);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableTexture2D() {
/* 5371 */     if (isRenderingSky) {
/*      */       
/* 5373 */       useProgram(ProgramSkyBasic);
/*      */     }
/* 5375 */     else if (activeProgram == ProgramTextured || activeProgram == ProgramTexturedLit) {
/*      */       
/* 5377 */       useProgram(ProgramBasic);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pushProgram() {
/* 5383 */     programStack.push(activeProgram);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void popProgram() {
/* 5388 */     Program program = programStack.pop();
/* 5389 */     useProgram(program);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginLeash() {
/* 5394 */     pushProgram();
/* 5395 */     useProgram(ProgramBasic);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endLeash() {
/* 5400 */     popProgram();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableFog() {
/* 5405 */     fogEnabled = true;
/* 5406 */     setProgramUniform1i(uniform_fogMode, fogMode);
/* 5407 */     setProgramUniform1f(uniform_fogDensity, fogDensity);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableFog() {
/* 5412 */     fogEnabled = false;
/* 5413 */     setProgramUniform1i(uniform_fogMode, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogDensity(float value) {
/* 5418 */     fogDensity = value;
/*      */     
/* 5420 */     if (fogEnabled)
/*      */     {
/* 5422 */       setProgramUniform1f(uniform_fogDensity, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void sglFogi(int pname, int param) {
/* 5428 */     GL11.glFogi(pname, param);
/*      */     
/* 5430 */     if (pname == 2917) {
/*      */       
/* 5432 */       fogMode = param;
/*      */       
/* 5434 */       if (fogEnabled)
/*      */       {
/* 5436 */         setProgramUniform1i(uniform_fogMode, fogMode);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableLightmap() {
/* 5443 */     lightmapEnabled = true;
/*      */     
/* 5445 */     if (activeProgram == ProgramTextured)
/*      */     {
/* 5447 */       useProgram(ProgramTexturedLit);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableLightmap() {
/* 5453 */     lightmapEnabled = false;
/*      */     
/* 5455 */     if (activeProgram == ProgramTexturedLit)
/*      */     {
/* 5457 */       useProgram(ProgramTextured);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getEntityData() {
/* 5463 */     return entityData[entityDataIndex * 2];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getEntityData2() {
/* 5468 */     return entityData[entityDataIndex * 2 + 1];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int setEntityData1(int data1) {
/* 5473 */     entityData[entityDataIndex * 2] = entityData[entityDataIndex * 2] & 0xFFFF | data1 << 16;
/* 5474 */     return data1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int setEntityData2(int data2) {
/* 5479 */     entityData[entityDataIndex * 2 + 1] = entityData[entityDataIndex * 2 + 1] & 0xFFFF0000 | data2 & 0xFFFF;
/* 5480 */     return data2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pushEntity(int data0, int data1) {
/* 5485 */     entityDataIndex++;
/* 5486 */     entityData[entityDataIndex * 2] = data0 & 0xFFFF | data1 << 16;
/* 5487 */     entityData[entityDataIndex * 2 + 1] = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pushEntity(int data0) {
/* 5492 */     entityDataIndex++;
/* 5493 */     entityData[entityDataIndex * 2] = data0 & 0xFFFF;
/* 5494 */     entityData[entityDataIndex * 2 + 1] = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pushEntity(Block block) {
/* 5499 */     entityDataIndex++;
/* 5500 */     int i = block.getRenderType();
/* 5501 */     entityData[entityDataIndex * 2] = Block.blockRegistry.getIDForObject(block) & 0xFFFF | i << 16;
/* 5502 */     entityData[entityDataIndex * 2 + 1] = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void popEntity() {
/* 5507 */     entityData[entityDataIndex * 2] = 0;
/* 5508 */     entityData[entityDataIndex * 2 + 1] = 0;
/* 5509 */     entityDataIndex--;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mcProfilerEndSection() {
/* 5514 */     mc.mcProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getShaderPackName() {
/* 5519 */     return (shaderPack == null) ? null : ((shaderPack instanceof ShaderPackNone) ? null : shaderPack.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public static InputStream getShaderPackResourceStream(String path) {
/* 5524 */     return (shaderPack == null) ? null : shaderPack.getResourceAsStream(path);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void nextAntialiasingLevel(boolean forward) {
/* 5529 */     if (forward) {
/*      */       
/* 5531 */       configAntialiasingLevel += 2;
/*      */       
/* 5533 */       if (configAntialiasingLevel > 4)
/*      */       {
/* 5535 */         configAntialiasingLevel = 0;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 5540 */       configAntialiasingLevel -= 2;
/*      */       
/* 5542 */       if (configAntialiasingLevel < 0)
/*      */       {
/* 5544 */         configAntialiasingLevel = 4;
/*      */       }
/*      */     } 
/*      */     
/* 5548 */     configAntialiasingLevel = configAntialiasingLevel / 2 * 2;
/* 5549 */     configAntialiasingLevel = Config.limit(configAntialiasingLevel, 0, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkShadersModInstalled() {
/*      */     try {
/* 5556 */       Class<?> clazz = Class.forName("shadersmod.transform.SMCClassTransformer");
/*      */     }
/* 5558 */     catch (Throwable var1) {
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/* 5563 */     throw new RuntimeException("Shaders Mod detected. Please remove it, OptiFine has built-in support for shaders.");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void resourcesReloaded() {
/* 5568 */     loadShaderPackResources();
/*      */     
/* 5570 */     if (shaderPackLoaded) {
/*      */       
/* 5572 */       BlockAliases.resourcesReloaded();
/* 5573 */       ItemAliases.resourcesReloaded();
/* 5574 */       EntityAliases.resourcesReloaded();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void loadShaderPackResources() {
/* 5580 */     shaderPackResources = new HashMap<>();
/*      */     
/* 5582 */     if (shaderPackLoaded) {
/*      */       
/* 5584 */       List<String> list = new ArrayList<>();
/* 5585 */       String s = "/shaders/lang/";
/* 5586 */       String s1 = "en_US";
/* 5587 */       String s2 = ".lang";
/* 5588 */       list.add(s + s1 + s2);
/*      */       
/* 5590 */       if (!(Config.getGameSettings()).language.equals(s1))
/*      */       {
/* 5592 */         list.add(s + (Config.getGameSettings()).language + s2);
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/* 5597 */         for (String s3 : list) {
/*      */           
/* 5599 */           InputStream inputstream = shaderPack.getResourceAsStream(s3);
/*      */           
/* 5601 */           if (inputstream != null) {
/*      */             
/* 5603 */             PropertiesOrdered propertiesOrdered = new PropertiesOrdered();
/* 5604 */             Lang.loadLocaleData(inputstream, (Map)propertiesOrdered);
/* 5605 */             inputstream.close();
/*      */             
/* 5607 */             for (Object o : propertiesOrdered.keySet())
/*      */             {
/* 5609 */               String s4 = (String)o;
/* 5610 */               String s5 = propertiesOrdered.getProperty(s4);
/* 5611 */               shaderPackResources.put(s4, s5);
/*      */             }
/*      */           
/*      */           } 
/*      */         } 
/* 5616 */       } catch (IOException ioexception) {
/*      */         
/* 5618 */         ioexception.printStackTrace();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static String translate(String key, String def) {
/* 5625 */     String s = shaderPackResources.get(key);
/* 5626 */     return (s == null) ? def : s;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isProgramPath(String path) {
/* 5631 */     if (path == null)
/*      */     {
/* 5633 */       return false;
/*      */     }
/* 5635 */     if (path.length() <= 0)
/*      */     {
/* 5637 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 5641 */     int i = path.lastIndexOf("/");
/*      */     
/* 5643 */     if (i >= 0)
/*      */     {
/* 5645 */       path = path.substring(i + 1);
/*      */     }
/*      */     
/* 5648 */     Program program = getProgram(path);
/* 5649 */     return (program != null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Program getProgram(String name) {
/* 5655 */     return programs.getProgram(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setItemToRenderMain(ItemStack itemToRenderMain) {
/* 5660 */     itemToRenderMainTranslucent = isTranslucentBlock(itemToRenderMain);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setItemToRenderOff(ItemStack itemToRenderOff) {
/* 5665 */     itemToRenderOffTranslucent = isTranslucentBlock(itemToRenderOff);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isItemToRenderMainTranslucent() {
/* 5670 */     return itemToRenderMainTranslucent;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isItemToRenderOffTranslucent() {
/* 5675 */     return itemToRenderOffTranslucent;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isBothHandsRendered() {
/* 5680 */     return (isHandRenderedMain && isHandRenderedOff);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isTranslucentBlock(ItemStack stack) {
/* 5685 */     if (stack == null)
/*      */     {
/* 5687 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 5691 */     Item item = stack.getItem();
/*      */     
/* 5693 */     if (item == null)
/*      */     {
/* 5695 */       return false;
/*      */     }
/* 5697 */     if (!(item instanceof ItemBlock))
/*      */     {
/* 5699 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 5703 */     ItemBlock itemblock = (ItemBlock)item;
/* 5704 */     Block block = itemblock.getBlock();
/*      */     
/* 5706 */     if (block == null)
/*      */     {
/* 5708 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 5712 */     EnumWorldBlockLayer enumworldblocklayer = block.getBlockLayer();
/* 5713 */     return (enumworldblocklayer == EnumWorldBlockLayer.TRANSLUCENT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSkipRenderHand() {
/* 5721 */     return skipRenderHandMain;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRenderBothHands() {
/* 5726 */     return (!skipRenderHandMain && !skipRenderHandOff);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setSkipRenderHands(boolean skipMain, boolean skipOff) {
/* 5731 */     skipRenderHandMain = skipMain;
/* 5732 */     skipRenderHandOff = skipOff;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setHandsRendered(boolean handMain, boolean handOff) {
/* 5737 */     isHandRenderedMain = handMain;
/* 5738 */     isHandRenderedOff = handOff;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isHandRenderedMain() {
/* 5743 */     return isHandRenderedMain;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isHandRenderedOff() {
/* 5748 */     return isHandRenderedOff;
/*      */   }
/*      */ 
/*      */   
/*      */   public static float getShadowRenderDistance() {
/* 5753 */     return (shadowDistanceRenderMul < 0.0F) ? -1.0F : (shadowMapHalfPlane * shadowDistanceRenderMul);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setRenderingFirstPersonHand(boolean flag) {
/* 5758 */     isRenderingFirstPersonHand = flag;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isRenderingFirstPersonHand() {
/* 5763 */     return isRenderingFirstPersonHand;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void beginBeacon() {
/* 5768 */     if (isRenderingWorld)
/*      */     {
/* 5770 */       useProgram(ProgramBeaconBeam);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void endBeacon() {
/* 5776 */     if (isRenderingWorld)
/*      */     {
/* 5778 */       useProgram(ProgramBlock);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static World getCurrentWorld() {
/* 5784 */     return currentWorld;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BlockPos getCameraPosition() {
/* 5789 */     return new BlockPos(cameraPositionX, cameraPositionY, cameraPositionZ);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isCustomUniforms() {
/* 5794 */     return (customUniforms != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean canRenderQuads() {
/* 5799 */     return hasGeometryShaders ? capabilities.GL_NV_geometry_shader4 : true;
/*      */   }
/*      */ 
/*      */   
/*      */   static {
/* 5804 */     shaderPacksDir = new File((Minecraft.getMinecraft()).mcDataDir, "shaderpacks");
/* 5805 */     configFile = new File((Minecraft.getMinecraft()).mcDataDir, "optionsshaders.txt");
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\Shaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */