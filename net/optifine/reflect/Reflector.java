/*      */ package net.optifine.reflect;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Map;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.gui.GuiEnchantment;
/*      */ import net.minecraft.client.gui.GuiHopper;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.model.ModelBanner;
/*      */ import net.minecraft.client.model.ModelBase;
/*      */ import net.minecraft.client.model.ModelBat;
/*      */ import net.minecraft.client.model.ModelChest;
/*      */ import net.minecraft.client.model.ModelOcelot;
/*      */ import net.minecraft.client.model.ModelRenderer;
/*      */ import net.minecraft.client.model.ModelSkeletonHead;
/*      */ import net.minecraft.client.model.ModelWitch;
/*      */ import net.minecraft.client.model.ModelWolf;
/*      */ import net.minecraft.client.renderer.EntityRenderer;
/*      */ import net.minecraft.client.renderer.entity.RenderBoat;
/*      */ import net.minecraft.client.renderer.entity.RenderManager;
/*      */ import net.minecraft.client.renderer.entity.RenderMinecart;
/*      */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*      */ import net.minecraft.client.renderer.tileentity.RenderItemFrame;
/*      */ import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
/*      */ import net.minecraft.client.resources.DefaultResourcePack;
/*      */ import net.minecraft.client.resources.model.ModelManager;
/*      */ import net.minecraft.client.resources.model.ModelRotation;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.passive.EntityVillager;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntityBeacon;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.world.ChunkCache;
/*      */ import net.minecraft.world.ChunkCoordIntPair;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraftforge.common.property.IUnlistedProperty;
/*      */ import net.optifine.Log;
/*      */ import net.optifine.util.ArrayUtils;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class Reflector {
/*   59 */   private static final Logger LOGGER = LogManager.getLogger();
/*   60 */   private static boolean logForge = logEntry("*** Reflector Forge ***");
/*   61 */   public static ReflectorClass BetterFoliageClient = new ReflectorClass("mods.betterfoliage.client.BetterFoliageClient");
/*   62 */   public static ReflectorClass BlamingTransformer = new ReflectorClass("net.minecraftforge.fml.common.asm.transformers.BlamingTransformer");
/*   63 */   public static ReflectorMethod BlamingTransformer_onCrash = new ReflectorMethod(BlamingTransformer, "onCrash");
/*   64 */   public static ReflectorClass ChunkWatchEvent_UnWatch = new ReflectorClass("net.minecraftforge.event.world.ChunkWatchEvent$UnWatch");
/*   65 */   public static ReflectorConstructor ChunkWatchEvent_UnWatch_Constructor = new ReflectorConstructor(ChunkWatchEvent_UnWatch, new Class[] { ChunkCoordIntPair.class, EntityPlayerMP.class });
/*   66 */   public static ReflectorClass CoreModManager = new ReflectorClass("net.minecraftforge.fml.relauncher.CoreModManager");
/*   67 */   public static ReflectorMethod CoreModManager_onCrash = new ReflectorMethod(CoreModManager, "onCrash");
/*   68 */   public static ReflectorClass DimensionManager = new ReflectorClass("net.minecraftforge.common.DimensionManager");
/*   69 */   public static ReflectorMethod DimensionManager_createProviderFor = new ReflectorMethod(DimensionManager, "createProviderFor");
/*   70 */   public static ReflectorMethod DimensionManager_getStaticDimensionIDs = new ReflectorMethod(DimensionManager, "getStaticDimensionIDs");
/*   71 */   public static ReflectorClass DrawScreenEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre");
/*   72 */   public static ReflectorConstructor DrawScreenEvent_Pre_Constructor = new ReflectorConstructor(DrawScreenEvent_Pre, new Class[] { GuiScreen.class, int.class, int.class, float.class });
/*   73 */   public static ReflectorClass DrawScreenEvent_Post = new ReflectorClass("net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Post");
/*   74 */   public static ReflectorConstructor DrawScreenEvent_Post_Constructor = new ReflectorConstructor(DrawScreenEvent_Post, new Class[] { GuiScreen.class, int.class, int.class, float.class });
/*   75 */   public static ReflectorClass EntityViewRenderEvent_CameraSetup = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$CameraSetup");
/*   76 */   public static ReflectorConstructor EntityViewRenderEvent_CameraSetup_Constructor = new ReflectorConstructor(EntityViewRenderEvent_CameraSetup, new Class[] { EntityRenderer.class, Entity.class, Block.class, double.class, float.class, float.class, float.class });
/*   77 */   public static ReflectorField EntityViewRenderEvent_CameraSetup_yaw = new ReflectorField(EntityViewRenderEvent_CameraSetup, "yaw");
/*   78 */   public static ReflectorField EntityViewRenderEvent_CameraSetup_pitch = new ReflectorField(EntityViewRenderEvent_CameraSetup, "pitch");
/*   79 */   public static ReflectorField EntityViewRenderEvent_CameraSetup_roll = new ReflectorField(EntityViewRenderEvent_CameraSetup, "roll");
/*   80 */   public static ReflectorClass EntityViewRenderEvent_FogColors = new ReflectorClass("net.minecraftforge.client.event.EntityViewRenderEvent$FogColors");
/*   81 */   public static ReflectorConstructor EntityViewRenderEvent_FogColors_Constructor = new ReflectorConstructor(EntityViewRenderEvent_FogColors, new Class[] { EntityRenderer.class, Entity.class, Block.class, double.class, float.class, float.class, float.class });
/*   82 */   public static ReflectorField EntityViewRenderEvent_FogColors_red = new ReflectorField(EntityViewRenderEvent_FogColors, "red");
/*   83 */   public static ReflectorField EntityViewRenderEvent_FogColors_green = new ReflectorField(EntityViewRenderEvent_FogColors, "green");
/*   84 */   public static ReflectorField EntityViewRenderEvent_FogColors_blue = new ReflectorField(EntityViewRenderEvent_FogColors, "blue");
/*   85 */   public static ReflectorClass Event = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event");
/*   86 */   public static ReflectorMethod Event_isCanceled = new ReflectorMethod(Event, "isCanceled");
/*   87 */   public static ReflectorClass EventBus = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.EventBus");
/*   88 */   public static ReflectorMethod EventBus_post = new ReflectorMethod(EventBus, "post");
/*   89 */   public static ReflectorClass Event_Result = new ReflectorClass("net.minecraftforge.fml.common.eventhandler.Event$Result");
/*   90 */   public static ReflectorField Event_Result_DENY = new ReflectorField(Event_Result, "DENY");
/*   91 */   public static ReflectorField Event_Result_ALLOW = new ReflectorField(Event_Result, "ALLOW");
/*   92 */   public static ReflectorField Event_Result_DEFAULT = new ReflectorField(Event_Result, "DEFAULT");
/*   93 */   public static ReflectorClass ExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.ExtendedBlockState");
/*   94 */   public static ReflectorConstructor ExtendedBlockState_Constructor = new ReflectorConstructor(ExtendedBlockState, new Class[] { Block.class, IProperty[].class, IUnlistedProperty[].class });
/*   95 */   public static ReflectorClass FMLClientHandler = new ReflectorClass("net.minecraftforge.fml.client.FMLClientHandler");
/*   96 */   public static ReflectorMethod FMLClientHandler_instance = new ReflectorMethod(FMLClientHandler, "instance");
/*   97 */   public static ReflectorMethod FMLClientHandler_handleLoadingScreen = new ReflectorMethod(FMLClientHandler, "handleLoadingScreen");
/*   98 */   public static ReflectorMethod FMLClientHandler_isLoading = new ReflectorMethod(FMLClientHandler, "isLoading");
/*   99 */   public static ReflectorMethod FMLClientHandler_trackBrokenTexture = new ReflectorMethod(FMLClientHandler, "trackBrokenTexture");
/*  100 */   public static ReflectorMethod FMLClientHandler_trackMissingTexture = new ReflectorMethod(FMLClientHandler, "trackMissingTexture");
/*  101 */   public static ReflectorClass FMLCommonHandler = new ReflectorClass("net.minecraftforge.fml.common.FMLCommonHandler");
/*  102 */   public static ReflectorMethod FMLCommonHandler_callFuture = new ReflectorMethod(FMLCommonHandler, "callFuture");
/*  103 */   public static ReflectorMethod FMLCommonHandler_enhanceCrashReport = new ReflectorMethod(FMLCommonHandler, "enhanceCrashReport");
/*  104 */   public static ReflectorMethod FMLCommonHandler_getBrandings = new ReflectorMethod(FMLCommonHandler, "getBrandings");
/*  105 */   public static ReflectorMethod FMLCommonHandler_handleServerAboutToStart = new ReflectorMethod(FMLCommonHandler, "handleServerAboutToStart");
/*  106 */   public static ReflectorMethod FMLCommonHandler_handleServerStarting = new ReflectorMethod(FMLCommonHandler, "handleServerStarting");
/*  107 */   public static ReflectorMethod FMLCommonHandler_instance = new ReflectorMethod(FMLCommonHandler, "instance");
/*  108 */   public static ReflectorClass ForgeBiome = new ReflectorClass(BiomeGenBase.class);
/*  109 */   public static ReflectorMethod ForgeBiome_getWaterColorMultiplier = new ReflectorMethod(ForgeBiome, "getWaterColorMultiplier");
/*  110 */   public static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
/*  111 */   public static ReflectorMethod ForgeBlock_addDestroyEffects = new ReflectorMethod(ForgeBlock, "addDestroyEffects");
/*  112 */   public static ReflectorMethod ForgeBlock_addHitEffects = new ReflectorMethod(ForgeBlock, "addHitEffects");
/*  113 */   public static ReflectorMethod ForgeBlock_canCreatureSpawn = new ReflectorMethod(ForgeBlock, "canCreatureSpawn");
/*  114 */   public static ReflectorMethod ForgeBlock_canRenderInLayer = new ReflectorMethod(ForgeBlock, "canRenderInLayer", new Class[] { EnumWorldBlockLayer.class });
/*  115 */   public static ReflectorMethod ForgeBlock_doesSideBlockRendering = new ReflectorMethod(ForgeBlock, "doesSideBlockRendering");
/*  116 */   public static ReflectorMethod ForgeBlock_getBedDirection = new ReflectorMethod(ForgeBlock, "getBedDirection");
/*  117 */   public static ReflectorMethod ForgeBlock_getExtendedState = new ReflectorMethod(ForgeBlock, "getExtendedState");
/*  118 */   public static ReflectorMethod ForgeBlock_getLightOpacity = new ReflectorMethod(ForgeBlock, "getLightOpacity", new Class[] { IBlockAccess.class, BlockPos.class });
/*  119 */   public static ReflectorMethod ForgeBlock_getLightValue = new ReflectorMethod(ForgeBlock, "getLightValue", new Class[] { IBlockAccess.class, BlockPos.class });
/*  120 */   public static ReflectorMethod ForgeBlock_hasTileEntity = new ReflectorMethod(ForgeBlock, "hasTileEntity", new Class[] { IBlockState.class });
/*  121 */   public static ReflectorMethod ForgeBlock_isAir = new ReflectorMethod(ForgeBlock, "isAir");
/*  122 */   public static ReflectorMethod ForgeBlock_isBed = new ReflectorMethod(ForgeBlock, "isBed");
/*  123 */   public static ReflectorMethod ForgeBlock_isBedFoot = new ReflectorMethod(ForgeBlock, "isBedFoot");
/*  124 */   public static ReflectorMethod ForgeBlock_isSideSolid = new ReflectorMethod(ForgeBlock, "isSideSolid");
/*  125 */   public static ReflectorClass ForgeChunkCache = new ReflectorClass(ChunkCache.class);
/*  126 */   public static ReflectorMethod ForgeChunkCache_isSideSolid = new ReflectorMethod(ForgeChunkCache, "isSideSolid");
/*  127 */   public static ReflectorClass ForgeEntity = new ReflectorClass(Entity.class);
/*  128 */   public static ReflectorMethod ForgeEntity_canRiderInteract = new ReflectorMethod(ForgeEntity, "canRiderInteract");
/*  129 */   public static ReflectorField ForgeEntity_captureDrops = new ReflectorField(ForgeEntity, "captureDrops");
/*  130 */   public static ReflectorField ForgeEntity_capturedDrops = new ReflectorField(ForgeEntity, "capturedDrops");
/*  131 */   public static ReflectorMethod ForgeEntity_shouldRenderInPass = new ReflectorMethod(ForgeEntity, "shouldRenderInPass");
/*  132 */   public static ReflectorMethod ForgeEntity_shouldRiderSit = new ReflectorMethod(ForgeEntity, "shouldRiderSit");
/*  133 */   public static ReflectorClass ForgeEventFactory = new ReflectorClass("net.minecraftforge.event.ForgeEventFactory");
/*  134 */   public static ReflectorMethod ForgeEventFactory_canEntityDespawn = new ReflectorMethod(ForgeEventFactory, "canEntityDespawn");
/*  135 */   public static ReflectorMethod ForgeEventFactory_canEntitySpawn = new ReflectorMethod(ForgeEventFactory, "canEntitySpawn");
/*  136 */   public static ReflectorMethod ForgeEventFactory_doSpecialSpawn = new ReflectorMethod(ForgeEventFactory, "doSpecialSpawn", new Class[] { EntityLiving.class, World.class, float.class, float.class, float.class });
/*  137 */   public static ReflectorMethod ForgeEventFactory_getMaxSpawnPackSize = new ReflectorMethod(ForgeEventFactory, "getMaxSpawnPackSize");
/*  138 */   public static ReflectorMethod ForgeEventFactory_renderBlockOverlay = new ReflectorMethod(ForgeEventFactory, "renderBlockOverlay");
/*  139 */   public static ReflectorMethod ForgeEventFactory_renderFireOverlay = new ReflectorMethod(ForgeEventFactory, "renderFireOverlay");
/*  140 */   public static ReflectorMethod ForgeEventFactory_renderWaterOverlay = new ReflectorMethod(ForgeEventFactory, "renderWaterOverlay");
/*  141 */   public static ReflectorClass ForgeHooks = new ReflectorClass("net.minecraftforge.common.ForgeHooks");
/*  142 */   public static ReflectorMethod ForgeHooks_onLivingAttack = new ReflectorMethod(ForgeHooks, "onLivingAttack");
/*  143 */   public static ReflectorMethod ForgeHooks_onLivingDeath = new ReflectorMethod(ForgeHooks, "onLivingDeath");
/*  144 */   public static ReflectorMethod ForgeHooks_onLivingDrops = new ReflectorMethod(ForgeHooks, "onLivingDrops");
/*  145 */   public static ReflectorMethod ForgeHooks_onLivingFall = new ReflectorMethod(ForgeHooks, "onLivingFall");
/*  146 */   public static ReflectorMethod ForgeHooks_onLivingHurt = new ReflectorMethod(ForgeHooks, "onLivingHurt");
/*  147 */   public static ReflectorMethod ForgeHooks_onLivingJump = new ReflectorMethod(ForgeHooks, "onLivingJump");
/*  148 */   public static ReflectorMethod ForgeHooks_onLivingSetAttackTarget = new ReflectorMethod(ForgeHooks, "onLivingSetAttackTarget");
/*  149 */   public static ReflectorMethod ForgeHooks_onLivingUpdate = new ReflectorMethod(ForgeHooks, "onLivingUpdate");
/*  150 */   public static ReflectorClass ForgeHooksClient = new ReflectorClass("net.minecraftforge.client.ForgeHooksClient");
/*  151 */   public static ReflectorMethod ForgeHooksClient_applyTransform = new ReflectorMethod(ForgeHooksClient, "applyTransform", new Class[] { Matrix4f.class, Optional.class });
/*  152 */   public static ReflectorMethod ForgeHooksClient_dispatchRenderLast = new ReflectorMethod(ForgeHooksClient, "dispatchRenderLast");
/*  153 */   public static ReflectorMethod ForgeHooksClient_drawScreen = new ReflectorMethod(ForgeHooksClient, "drawScreen");
/*  154 */   public static ReflectorMethod ForgeHooksClient_fillNormal = new ReflectorMethod(ForgeHooksClient, "fillNormal");
/*  155 */   public static ReflectorMethod ForgeHooksClient_handleCameraTransforms = new ReflectorMethod(ForgeHooksClient, "handleCameraTransforms");
/*  156 */   public static ReflectorMethod ForgeHooksClient_getArmorModel = new ReflectorMethod(ForgeHooksClient, "getArmorModel");
/*  157 */   public static ReflectorMethod ForgeHooksClient_getArmorTexture = new ReflectorMethod(ForgeHooksClient, "getArmorTexture");
/*  158 */   public static ReflectorMethod ForgeHooksClient_getFogDensity = new ReflectorMethod(ForgeHooksClient, "getFogDensity");
/*  159 */   public static ReflectorMethod ForgeHooksClient_getFOVModifier = new ReflectorMethod(ForgeHooksClient, "getFOVModifier");
/*  160 */   public static ReflectorMethod ForgeHooksClient_getMatrix = new ReflectorMethod(ForgeHooksClient, "getMatrix", new Class[] { ModelRotation.class });
/*  161 */   public static ReflectorMethod ForgeHooksClient_getOffsetFOV = new ReflectorMethod(ForgeHooksClient, "getOffsetFOV");
/*  162 */   public static ReflectorMethod ForgeHooksClient_loadEntityShader = new ReflectorMethod(ForgeHooksClient, "loadEntityShader");
/*  163 */   public static ReflectorMethod ForgeHooksClient_onDrawBlockHighlight = new ReflectorMethod(ForgeHooksClient, "onDrawBlockHighlight");
/*  164 */   public static ReflectorMethod ForgeHooksClient_onFogRender = new ReflectorMethod(ForgeHooksClient, "onFogRender");
/*  165 */   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPre = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPre");
/*  166 */   public static ReflectorMethod ForgeHooksClient_onTextureStitchedPost = new ReflectorMethod(ForgeHooksClient, "onTextureStitchedPost");
/*  167 */   public static ReflectorMethod ForgeHooksClient_orientBedCamera = new ReflectorMethod(ForgeHooksClient, "orientBedCamera");
/*  168 */   public static ReflectorMethod ForgeHooksClient_putQuadColor = new ReflectorMethod(ForgeHooksClient, "putQuadColor");
/*  169 */   public static ReflectorMethod ForgeHooksClient_renderFirstPersonHand = new ReflectorMethod(ForgeHooksClient, "renderFirstPersonHand");
/*  170 */   public static ReflectorMethod ForgeHooksClient_renderMainMenu = new ReflectorMethod(ForgeHooksClient, "renderMainMenu");
/*  171 */   public static ReflectorMethod ForgeHooksClient_setRenderLayer = new ReflectorMethod(ForgeHooksClient, "setRenderLayer");
/*  172 */   public static ReflectorMethod ForgeHooksClient_setRenderPass = new ReflectorMethod(ForgeHooksClient, "setRenderPass");
/*  173 */   public static ReflectorMethod ForgeHooksClient_transform = new ReflectorMethod(ForgeHooksClient, "transform");
/*  174 */   public static ReflectorClass ForgeItem = new ReflectorClass(Item.class);
/*  175 */   public static ReflectorField ForgeItem_delegate = new ReflectorField(ForgeItem, "delegate");
/*  176 */   public static ReflectorMethod ForgeItem_getDurabilityForDisplay = new ReflectorMethod(ForgeItem, "getDurabilityForDisplay");
/*  177 */   public static ReflectorMethod ForgeItem_getModel = new ReflectorMethod(ForgeItem, "getModel");
/*  178 */   public static ReflectorMethod ForgeItem_onEntitySwing = new ReflectorMethod(ForgeItem, "onEntitySwing");
/*  179 */   public static ReflectorMethod ForgeItem_shouldCauseReequipAnimation = new ReflectorMethod(ForgeItem, "shouldCauseReequipAnimation");
/*  180 */   public static ReflectorMethod ForgeItem_showDurabilityBar = new ReflectorMethod(ForgeItem, "showDurabilityBar");
/*  181 */   public static ReflectorClass ForgeModContainer = new ReflectorClass("net.minecraftforge.common.ForgeModContainer");
/*  182 */   public static ReflectorField ForgeModContainer_forgeLightPipelineEnabled = new ReflectorField(ForgeModContainer, "forgeLightPipelineEnabled");
/*  183 */   public static ReflectorClass ForgePotionEffect = new ReflectorClass(PotionEffect.class);
/*  184 */   public static ReflectorMethod ForgePotionEffect_isCurativeItem = new ReflectorMethod(ForgePotionEffect, "isCurativeItem");
/*  185 */   public static ReflectorClass ForgeTileEntity = new ReflectorClass(TileEntity.class);
/*  186 */   public static ReflectorMethod ForgeTileEntity_canRenderBreaking = new ReflectorMethod(ForgeTileEntity, "canRenderBreaking");
/*  187 */   public static ReflectorMethod ForgeTileEntity_getRenderBoundingBox = new ReflectorMethod(ForgeTileEntity, "getRenderBoundingBox");
/*  188 */   public static ReflectorMethod ForgeTileEntity_hasFastRenderer = new ReflectorMethod(ForgeTileEntity, "hasFastRenderer");
/*  189 */   public static ReflectorMethod ForgeTileEntity_shouldRenderInPass = new ReflectorMethod(ForgeTileEntity, "shouldRenderInPass");
/*  190 */   public static ReflectorClass ForgeVertexFormatElementEnumUseage = new ReflectorClass(VertexFormatElement.EnumUsage.class);
/*  191 */   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_preDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "preDraw");
/*  192 */   public static ReflectorMethod ForgeVertexFormatElementEnumUseage_postDraw = new ReflectorMethod(ForgeVertexFormatElementEnumUseage, "postDraw");
/*  193 */   public static ReflectorClass ForgeWorld = new ReflectorClass(World.class);
/*  194 */   public static ReflectorMethod ForgeWorld_countEntities = new ReflectorMethod(ForgeWorld, "countEntities", new Class[] { EnumCreatureType.class, boolean.class });
/*  195 */   public static ReflectorMethod ForgeWorld_getPerWorldStorage = new ReflectorMethod(ForgeWorld, "getPerWorldStorage");
/*  196 */   public static ReflectorClass ForgeWorldProvider = new ReflectorClass(WorldProvider.class);
/*  197 */   public static ReflectorMethod ForgeWorldProvider_getCloudRenderer = new ReflectorMethod(ForgeWorldProvider, "getCloudRenderer");
/*  198 */   public static ReflectorMethod ForgeWorldProvider_getSkyRenderer = new ReflectorMethod(ForgeWorldProvider, "getSkyRenderer");
/*  199 */   public static ReflectorMethod ForgeWorldProvider_getWeatherRenderer = new ReflectorMethod(ForgeWorldProvider, "getWeatherRenderer");
/*  200 */   public static ReflectorMethod ForgeWorldProvider_getSaveFolder = new ReflectorMethod(ForgeWorldProvider, "getSaveFolder");
/*  201 */   public static ReflectorClass GuiModList = new ReflectorClass("net.minecraftforge.fml.client.GuiModList");
/*  202 */   public static ReflectorConstructor GuiModList_Constructor = new ReflectorConstructor(GuiModList, new Class[] { GuiScreen.class });
/*  203 */   public static ReflectorClass IColoredBakedQuad = new ReflectorClass("net.minecraftforge.client.model.IColoredBakedQuad");
/*  204 */   public static ReflectorClass IExtendedBlockState = new ReflectorClass("net.minecraftforge.common.property.IExtendedBlockState");
/*  205 */   public static ReflectorMethod IExtendedBlockState_getClean = new ReflectorMethod(IExtendedBlockState, "getClean");
/*  206 */   public static ReflectorClass IModel = new ReflectorClass("net.minecraftforge.client.model.IModel");
/*  207 */   public static ReflectorMethod IModel_getTextures = new ReflectorMethod(IModel, "getTextures");
/*  208 */   public static ReflectorClass IRenderHandler = new ReflectorClass("net.minecraftforge.client.IRenderHandler");
/*  209 */   public static ReflectorMethod IRenderHandler_render = new ReflectorMethod(IRenderHandler, "render");
/*  210 */   public static ReflectorClass ItemModelMesherForge = new ReflectorClass("net.minecraftforge.client.ItemModelMesherForge");
/*  211 */   public static ReflectorConstructor ItemModelMesherForge_Constructor = new ReflectorConstructor(ItemModelMesherForge, new Class[] { ModelManager.class });
/*  212 */   public static ReflectorClass Launch = new ReflectorClass("net.minecraft.launchwrapper.Launch");
/*  213 */   public static ReflectorField Launch_blackboard = new ReflectorField(Launch, "blackboard");
/*  214 */   public static ReflectorClass LightUtil = new ReflectorClass("net.minecraftforge.client.model.pipeline.LightUtil");
/*  215 */   public static ReflectorField LightUtil_itemConsumer = new ReflectorField(LightUtil, "itemConsumer");
/*  216 */   public static ReflectorMethod LightUtil_putBakedQuad = new ReflectorMethod(LightUtil, "putBakedQuad");
/*  217 */   public static ReflectorMethod LightUtil_renderQuadColor = new ReflectorMethod(LightUtil, "renderQuadColor");
/*  218 */   public static ReflectorField LightUtil_tessellator = new ReflectorField(LightUtil, "tessellator");
/*  219 */   public static ReflectorClass Loader = new ReflectorClass("net.minecraftforge.fml.common.Loader");
/*  220 */   public static ReflectorMethod Loader_getActiveModList = new ReflectorMethod(Loader, "getActiveModList");
/*  221 */   public static ReflectorMethod Loader_instance = new ReflectorMethod(Loader, "instance");
/*  222 */   public static ReflectorClass MinecraftForge = new ReflectorClass("net.minecraftforge.common.MinecraftForge");
/*  223 */   public static ReflectorField MinecraftForge_EVENT_BUS = new ReflectorField(MinecraftForge, "EVENT_BUS");
/*  224 */   public static ReflectorClass MinecraftForgeClient = new ReflectorClass("net.minecraftforge.client.MinecraftForgeClient");
/*  225 */   public static ReflectorMethod MinecraftForgeClient_getRenderPass = new ReflectorMethod(MinecraftForgeClient, "getRenderPass");
/*  226 */   public static ReflectorMethod MinecraftForgeClient_onRebuildChunk = new ReflectorMethod(MinecraftForgeClient, "onRebuildChunk");
/*  227 */   public static ReflectorClass ModContainer = new ReflectorClass("net.minecraftforge.fml.common.ModContainer");
/*  228 */   public static ReflectorMethod ModContainer_getModId = new ReflectorMethod(ModContainer, "getModId");
/*  229 */   public static ReflectorClass ModelLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader");
/*  230 */   public static ReflectorField ModelLoader_stateModels = new ReflectorField(ModelLoader, "stateModels");
/*  231 */   public static ReflectorMethod ModelLoader_onRegisterItems = new ReflectorMethod(ModelLoader, "onRegisterItems");
/*  232 */   public static ReflectorMethod ModelLoader_getInventoryVariant = new ReflectorMethod(ModelLoader, "getInventoryVariant");
/*  233 */   public static ReflectorField ModelLoader_textures = new ReflectorField(ModelLoader, "textures");
/*  234 */   public static ReflectorClass ModelLoader_VanillaLoader = new ReflectorClass("net.minecraftforge.client.model.ModelLoader$VanillaLoader");
/*  235 */   public static ReflectorField ModelLoader_VanillaLoader_INSTANCE = new ReflectorField(ModelLoader_VanillaLoader, "instance");
/*  236 */   public static ReflectorMethod ModelLoader_VanillaLoader_loadModel = new ReflectorMethod(ModelLoader_VanillaLoader, "loadModel");
/*  237 */   public static ReflectorClass RenderBlockOverlayEvent_OverlayType = new ReflectorClass("net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType");
/*  238 */   public static ReflectorField RenderBlockOverlayEvent_OverlayType_BLOCK = new ReflectorField(RenderBlockOverlayEvent_OverlayType, "BLOCK");
/*  239 */   public static ReflectorClass RenderingRegistry = new ReflectorClass("net.minecraftforge.fml.client.registry.RenderingRegistry");
/*  240 */   public static ReflectorMethod RenderingRegistry_loadEntityRenderers = new ReflectorMethod(RenderingRegistry, "loadEntityRenderers", new Class[] { RenderManager.class, Map.class });
/*  241 */   public static ReflectorClass RenderItemInFrameEvent = new ReflectorClass("net.minecraftforge.client.event.RenderItemInFrameEvent");
/*  242 */   public static ReflectorConstructor RenderItemInFrameEvent_Constructor = new ReflectorConstructor(RenderItemInFrameEvent, new Class[] { EntityItemFrame.class, RenderItemFrame.class });
/*  243 */   public static ReflectorClass RenderLivingEvent_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Pre");
/*  244 */   public static ReflectorConstructor RenderLivingEvent_Pre_Constructor = new ReflectorConstructor(RenderLivingEvent_Pre, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, double.class, double.class, double.class });
/*  245 */   public static ReflectorClass RenderLivingEvent_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Post");
/*  246 */   public static ReflectorConstructor RenderLivingEvent_Post_Constructor = new ReflectorConstructor(RenderLivingEvent_Post, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, double.class, double.class, double.class });
/*  247 */   public static ReflectorClass RenderLivingEvent_Specials_Pre = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre");
/*  248 */   public static ReflectorConstructor RenderLivingEvent_Specials_Pre_Constructor = new ReflectorConstructor(RenderLivingEvent_Specials_Pre, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, double.class, double.class, double.class });
/*  249 */   public static ReflectorClass RenderLivingEvent_Specials_Post = new ReflectorClass("net.minecraftforge.client.event.RenderLivingEvent$Specials$Post");
/*  250 */   public static ReflectorConstructor RenderLivingEvent_Specials_Post_Constructor = new ReflectorConstructor(RenderLivingEvent_Specials_Post, new Class[] { EntityLivingBase.class, RendererLivingEntity.class, double.class, double.class, double.class });
/*  251 */   public static ReflectorClass SplashScreen = new ReflectorClass("net.minecraftforge.fml.client.SplashProgress");
/*  252 */   public static ReflectorClass WorldEvent_Load = new ReflectorClass("net.minecraftforge.event.world.WorldEvent$Load");
/*  253 */   public static ReflectorConstructor WorldEvent_Load_Constructor = new ReflectorConstructor(WorldEvent_Load, new Class[] { World.class });
/*  254 */   private static boolean logVanilla = logEntry("*** Reflector Vanilla ***");
/*  255 */   public static ReflectorClass ChunkProviderClient = new ReflectorClass(ChunkProviderClient.class);
/*  256 */   public static ReflectorField ChunkProviderClient_chunkMapping = new ReflectorField(ChunkProviderClient, LongHashMap.class);
/*  257 */   public static ReflectorClass EntityVillager = new ReflectorClass(EntityVillager.class);
/*  258 */   public static ReflectorField EntityVillager_careerId = new ReflectorField(new FieldLocatorTypes(EntityVillager.class, new Class[0], int.class, new Class[] { int.class, boolean.class, boolean.class, InventoryBasic.class }, "EntityVillager.careerId"));
/*  259 */   public static ReflectorField EntityVillager_careerLevel = new ReflectorField(new FieldLocatorTypes(EntityVillager.class, new Class[] { int.class }, int.class, new Class[] { boolean.class, boolean.class, InventoryBasic.class }, "EntityVillager.careerLevel"));
/*  260 */   public static ReflectorClass GuiBeacon = new ReflectorClass(GuiBeacon.class);
/*  261 */   public static ReflectorField GuiBeacon_tileBeacon = new ReflectorField(GuiBeacon, IInventory.class);
/*  262 */   public static ReflectorClass GuiBrewingStand = new ReflectorClass(GuiBrewingStand.class);
/*  263 */   public static ReflectorField GuiBrewingStand_tileBrewingStand = new ReflectorField(GuiBrewingStand, IInventory.class);
/*  264 */   public static ReflectorClass GuiChest = new ReflectorClass(GuiChest.class);
/*  265 */   public static ReflectorField GuiChest_lowerChestInventory = new ReflectorField(GuiChest, IInventory.class, 1);
/*  266 */   public static ReflectorClass GuiEnchantment = new ReflectorClass(GuiEnchantment.class);
/*  267 */   public static ReflectorField GuiEnchantment_nameable = new ReflectorField(GuiEnchantment, IWorldNameable.class);
/*  268 */   public static ReflectorClass GuiFurnace = new ReflectorClass(GuiFurnace.class);
/*  269 */   public static ReflectorField GuiFurnace_tileFurnace = new ReflectorField(GuiFurnace, IInventory.class);
/*  270 */   public static ReflectorClass GuiHopper = new ReflectorClass(GuiHopper.class);
/*  271 */   public static ReflectorField GuiHopper_hopperInventory = new ReflectorField(GuiHopper, IInventory.class, 1);
/*  272 */   public static ReflectorClass GuiMainMenu = new ReflectorClass(GuiMainMenu.class);
/*  273 */   public static ReflectorField GuiMainMenu_splashText = new ReflectorField(GuiMainMenu, String.class);
/*  274 */   public static ReflectorClass Minecraft = new ReflectorClass(Minecraft.class);
/*  275 */   public static ReflectorField Minecraft_defaultResourcePack = new ReflectorField(Minecraft, DefaultResourcePack.class);
/*  276 */   public static ReflectorClass ModelHumanoidHead = new ReflectorClass(ModelHumanoidHead.class);
/*  277 */   public static ReflectorField ModelHumanoidHead_head = new ReflectorField(ModelHumanoidHead, ModelRenderer.class);
/*  278 */   public static ReflectorClass ModelBat = new ReflectorClass(ModelBat.class);
/*  279 */   public static ReflectorFields ModelBat_ModelRenderers = new ReflectorFields(ModelBat, ModelRenderer.class, 6);
/*  280 */   public static ReflectorClass ModelBlaze = new ReflectorClass(ModelBlaze.class);
/*  281 */   public static ReflectorField ModelBlaze_blazeHead = new ReflectorField(ModelBlaze, ModelRenderer.class);
/*  282 */   public static ReflectorField ModelBlaze_blazeSticks = new ReflectorField(ModelBlaze, ModelRenderer[].class);
/*  283 */   public static ReflectorClass ModelBlock = new ReflectorClass(ModelBlock.class);
/*  284 */   public static ReflectorField ModelBlock_parentLocation = new ReflectorField(ModelBlock, ResourceLocation.class);
/*  285 */   public static ReflectorField ModelBlock_textures = new ReflectorField(ModelBlock, Map.class);
/*  286 */   public static ReflectorClass ModelDragon = new ReflectorClass(ModelDragon.class);
/*  287 */   public static ReflectorFields ModelDragon_ModelRenderers = new ReflectorFields(ModelDragon, ModelRenderer.class, 12);
/*  288 */   public static ReflectorClass ModelEnderCrystal = new ReflectorClass(ModelEnderCrystal.class);
/*  289 */   public static ReflectorFields ModelEnderCrystal_ModelRenderers = new ReflectorFields(ModelEnderCrystal, ModelRenderer.class, 3);
/*  290 */   public static ReflectorClass RenderEnderCrystal = new ReflectorClass(RenderEnderCrystal.class);
/*  291 */   public static ReflectorField RenderEnderCrystal_modelEnderCrystal = new ReflectorField(RenderEnderCrystal, ModelBase.class, 0);
/*  292 */   public static ReflectorClass ModelEnderMite = new ReflectorClass(ModelEnderMite.class);
/*  293 */   public static ReflectorField ModelEnderMite_bodyParts = new ReflectorField(ModelEnderMite, ModelRenderer[].class);
/*  294 */   public static ReflectorClass ModelGhast = new ReflectorClass(ModelGhast.class);
/*  295 */   public static ReflectorField ModelGhast_body = new ReflectorField(ModelGhast, ModelRenderer.class);
/*  296 */   public static ReflectorField ModelGhast_tentacles = new ReflectorField(ModelGhast, ModelRenderer[].class);
/*  297 */   public static ReflectorClass ModelGuardian = new ReflectorClass(ModelGuardian.class);
/*  298 */   public static ReflectorField ModelGuardian_body = new ReflectorField(ModelGuardian, ModelRenderer.class, 0);
/*  299 */   public static ReflectorField ModelGuardian_eye = new ReflectorField(ModelGuardian, ModelRenderer.class, 1);
/*  300 */   public static ReflectorField ModelGuardian_spines = new ReflectorField(ModelGuardian, ModelRenderer[].class, 0);
/*  301 */   public static ReflectorField ModelGuardian_tail = new ReflectorField(ModelGuardian, ModelRenderer[].class, 1);
/*  302 */   public static ReflectorClass ModelHorse = new ReflectorClass(ModelHorse.class);
/*  303 */   public static ReflectorFields ModelHorse_ModelRenderers = new ReflectorFields(ModelHorse, ModelRenderer.class, 39);
/*  304 */   public static ReflectorClass RenderLeashKnot = new ReflectorClass(RenderLeashKnot.class);
/*  305 */   public static ReflectorField RenderLeashKnot_leashKnotModel = new ReflectorField(RenderLeashKnot, ModelLeashKnot.class);
/*  306 */   public static ReflectorClass ModelMagmaCube = new ReflectorClass(ModelMagmaCube.class);
/*  307 */   public static ReflectorField ModelMagmaCube_core = new ReflectorField(ModelMagmaCube, ModelRenderer.class);
/*  308 */   public static ReflectorField ModelMagmaCube_segments = new ReflectorField(ModelMagmaCube, ModelRenderer[].class);
/*  309 */   public static ReflectorClass ModelOcelot = new ReflectorClass(ModelOcelot.class);
/*  310 */   public static ReflectorFields ModelOcelot_ModelRenderers = new ReflectorFields(ModelOcelot, ModelRenderer.class, 8);
/*  311 */   public static ReflectorClass ModelRabbit = new ReflectorClass(ModelRabbit.class);
/*  312 */   public static ReflectorFields ModelRabbit_renderers = new ReflectorFields(ModelRabbit, ModelRenderer.class, 12);
/*  313 */   public static ReflectorClass ModelSilverfish = new ReflectorClass(ModelSilverfish.class);
/*  314 */   public static ReflectorField ModelSilverfish_bodyParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 0);
/*  315 */   public static ReflectorField ModelSilverfish_wingParts = new ReflectorField(ModelSilverfish, ModelRenderer[].class, 1);
/*  316 */   public static ReflectorClass ModelSlime = new ReflectorClass(ModelSlime.class);
/*  317 */   public static ReflectorFields ModelSlime_ModelRenderers = new ReflectorFields(ModelSlime, ModelRenderer.class, 4);
/*  318 */   public static ReflectorClass ModelSquid = new ReflectorClass(ModelSquid.class);
/*  319 */   public static ReflectorField ModelSquid_body = new ReflectorField(ModelSquid, ModelRenderer.class);
/*  320 */   public static ReflectorField ModelSquid_tentacles = new ReflectorField(ModelSquid, ModelRenderer[].class);
/*  321 */   public static ReflectorClass ModelWitch = new ReflectorClass(ModelWitch.class);
/*  322 */   public static ReflectorField ModelWitch_mole = new ReflectorField(ModelWitch, ModelRenderer.class, 0);
/*  323 */   public static ReflectorField ModelWitch_hat = new ReflectorField(ModelWitch, ModelRenderer.class, 1);
/*  324 */   public static ReflectorClass ModelWither = new ReflectorClass(ModelWither.class);
/*  325 */   public static ReflectorField ModelWither_bodyParts = new ReflectorField(ModelWither, ModelRenderer[].class, 0);
/*  326 */   public static ReflectorField ModelWither_heads = new ReflectorField(ModelWither, ModelRenderer[].class, 1);
/*  327 */   public static ReflectorClass ModelWolf = new ReflectorClass(ModelWolf.class);
/*  328 */   public static ReflectorField ModelWolf_tail = new ReflectorField(ModelWolf, ModelRenderer.class, 6);
/*  329 */   public static ReflectorField ModelWolf_mane = new ReflectorField(ModelWolf, ModelRenderer.class, 7);
/*  330 */   public static ReflectorClass OptiFineClassTransformer = new ReflectorClass("optifine.OptiFineClassTransformer");
/*  331 */   public static ReflectorField OptiFineClassTransformer_instance = new ReflectorField(OptiFineClassTransformer, "instance");
/*  332 */   public static ReflectorMethod OptiFineClassTransformer_getOptiFineResource = new ReflectorMethod(OptiFineClassTransformer, "getOptiFineResource");
/*  333 */   public static ReflectorClass RenderBoat = new ReflectorClass(RenderBoat.class);
/*  334 */   public static ReflectorField RenderBoat_modelBoat = new ReflectorField(RenderBoat, ModelBase.class);
/*  335 */   public static ReflectorClass RenderMinecart = new ReflectorClass(RenderMinecart.class);
/*  336 */   public static ReflectorField RenderMinecart_modelMinecart = new ReflectorField(RenderMinecart, ModelBase.class);
/*  337 */   public static ReflectorClass RenderWitherSkull = new ReflectorClass(RenderWitherSkull.class);
/*  338 */   public static ReflectorField RenderWitherSkull_model = new ReflectorField(RenderWitherSkull, ModelSkeletonHead.class);
/*  339 */   public static ReflectorClass TileEntityBannerRenderer = new ReflectorClass(TileEntityBannerRenderer.class);
/*  340 */   public static ReflectorField TileEntityBannerRenderer_bannerModel = new ReflectorField(TileEntityBannerRenderer, ModelBanner.class);
/*  341 */   public static ReflectorClass TileEntityBeacon = new ReflectorClass(TileEntityBeacon.class);
/*  342 */   public static ReflectorField TileEntityBeacon_customName = new ReflectorField(TileEntityBeacon, String.class);
/*  343 */   public static ReflectorClass TileEntityBrewingStand = new ReflectorClass(TileEntityBrewingStand.class);
/*  344 */   public static ReflectorField TileEntityBrewingStand_customName = new ReflectorField(TileEntityBrewingStand, String.class);
/*  345 */   public static ReflectorClass TileEntityChestRenderer = new ReflectorClass(TileEntityChestRenderer.class);
/*  346 */   public static ReflectorField TileEntityChestRenderer_simpleChest = new ReflectorField(TileEntityChestRenderer, ModelChest.class, 0);
/*  347 */   public static ReflectorField TileEntityChestRenderer_largeChest = new ReflectorField(TileEntityChestRenderer, ModelChest.class, 1);
/*  348 */   public static ReflectorClass TileEntityEnchantmentTable = new ReflectorClass(TileEntityEnchantmentTable.class);
/*  349 */   public static ReflectorField TileEntityEnchantmentTable_customName = new ReflectorField(TileEntityEnchantmentTable, String.class);
/*  350 */   public static ReflectorClass TileEntityEnchantmentTableRenderer = new ReflectorClass(TileEntityEnchantmentTableRenderer.class);
/*  351 */   public static ReflectorField TileEntityEnchantmentTableRenderer_modelBook = new ReflectorField(TileEntityEnchantmentTableRenderer, ModelBook.class);
/*  352 */   public static ReflectorClass TileEntityEnderChestRenderer = new ReflectorClass(TileEntityEnderChestRenderer.class);
/*  353 */   public static ReflectorField TileEntityEnderChestRenderer_modelChest = new ReflectorField(TileEntityEnderChestRenderer, ModelChest.class);
/*  354 */   public static ReflectorClass TileEntityFurnace = new ReflectorClass(TileEntityFurnace.class);
/*  355 */   public static ReflectorField TileEntityFurnace_customName = new ReflectorField(TileEntityFurnace, String.class);
/*  356 */   public static ReflectorClass TileEntitySignRenderer = new ReflectorClass(TileEntitySignRenderer.class);
/*  357 */   public static ReflectorField TileEntitySignRenderer_model = new ReflectorField(TileEntitySignRenderer, ModelSign.class);
/*  358 */   public static ReflectorClass TileEntitySkullRenderer = new ReflectorClass(TileEntitySkullRenderer.class);
/*  359 */   public static ReflectorField TileEntitySkullRenderer_skeletonHead = new ReflectorField(TileEntitySkullRenderer, ModelSkeletonHead.class, 0);
/*  360 */   public static ReflectorField TileEntitySkullRenderer_humanoidHead = new ReflectorField(TileEntitySkullRenderer, ModelSkeletonHead.class, 1);
/*      */ 
/*      */ 
/*      */   
/*      */   public static void callVoid(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  366 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  368 */       if (method == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  373 */       method.invoke(null, params);
/*      */     }
/*  375 */     catch (Throwable throwable) {
/*      */       
/*  377 */       handleException(throwable, null, refMethod, params);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean callBoolean(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  385 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  387 */       if (method == null)
/*      */       {
/*  389 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  393 */       Boolean obool = (Boolean)method.invoke(null, params);
/*  394 */       return obool.booleanValue();
/*      */     
/*      */     }
/*  397 */     catch (Throwable throwable) {
/*      */       
/*  399 */       handleException(throwable, null, refMethod, params);
/*  400 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int callInt(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  408 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  410 */       if (method == null)
/*      */       {
/*  412 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*  416 */       Integer integer = (Integer)method.invoke(null, params);
/*  417 */       return integer.intValue();
/*      */     
/*      */     }
/*  420 */     catch (Throwable throwable) {
/*      */       
/*  422 */       handleException(throwable, null, refMethod, params);
/*  423 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static float callFloat(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  431 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  433 */       if (method == null)
/*      */       {
/*  435 */         return 0.0F;
/*      */       }
/*      */ 
/*      */       
/*  439 */       Float f = (Float)method.invoke(null, params);
/*  440 */       return f.floatValue();
/*      */     
/*      */     }
/*  443 */     catch (Throwable throwable) {
/*      */       
/*  445 */       handleException(throwable, null, refMethod, params);
/*  446 */       return 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static double callDouble(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  454 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  456 */       if (method == null)
/*      */       {
/*  458 */         return 0.0D;
/*      */       }
/*      */ 
/*      */       
/*  462 */       Double d0 = (Double)method.invoke(null, params);
/*  463 */       return d0.doubleValue();
/*      */     
/*      */     }
/*  466 */     catch (Throwable throwable) {
/*      */       
/*  468 */       handleException(throwable, null, refMethod, params);
/*  469 */       return 0.0D;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String callString(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  477 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  479 */       if (method == null)
/*      */       {
/*  481 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  485 */       String s = (String)method.invoke(null, params);
/*  486 */       return s;
/*      */     
/*      */     }
/*  489 */     catch (Throwable throwable) {
/*      */       
/*  491 */       handleException(throwable, null, refMethod, params);
/*  492 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object call(ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  500 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  502 */       if (method == null)
/*      */       {
/*  504 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  508 */       Object object = method.invoke(null, params);
/*  509 */       return object;
/*      */     
/*      */     }
/*  512 */     catch (Throwable throwable) {
/*      */       
/*  514 */       handleException(throwable, null, refMethod, params);
/*  515 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void callVoid(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  523 */       if (obj == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  528 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  530 */       if (method == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  535 */       method.invoke(obj, params);
/*      */     }
/*  537 */     catch (Throwable throwable) {
/*      */       
/*  539 */       handleException(throwable, obj, refMethod, params);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean callBoolean(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  547 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  549 */       if (method == null)
/*      */       {
/*  551 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  555 */       Boolean obool = (Boolean)method.invoke(obj, params);
/*  556 */       return obool.booleanValue();
/*      */     
/*      */     }
/*  559 */     catch (Throwable throwable) {
/*      */       
/*  561 */       handleException(throwable, obj, refMethod, params);
/*  562 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int callInt(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  570 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  572 */       if (method == null)
/*      */       {
/*  574 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*  578 */       Integer integer = (Integer)method.invoke(obj, params);
/*  579 */       return integer.intValue();
/*      */     
/*      */     }
/*  582 */     catch (Throwable throwable) {
/*      */       
/*  584 */       handleException(throwable, obj, refMethod, params);
/*  585 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static float callFloat(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  593 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  595 */       if (method == null)
/*      */       {
/*  597 */         return 0.0F;
/*      */       }
/*      */ 
/*      */       
/*  601 */       Float f = (Float)method.invoke(obj, params);
/*  602 */       return f.floatValue();
/*      */     
/*      */     }
/*  605 */     catch (Throwable throwable) {
/*      */       
/*  607 */       handleException(throwable, obj, refMethod, params);
/*  608 */       return 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static double callDouble(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  616 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  618 */       if (method == null)
/*      */       {
/*  620 */         return 0.0D;
/*      */       }
/*      */ 
/*      */       
/*  624 */       Double d0 = (Double)method.invoke(obj, params);
/*  625 */       return d0.doubleValue();
/*      */     
/*      */     }
/*  628 */     catch (Throwable throwable) {
/*      */       
/*  630 */       handleException(throwable, obj, refMethod, params);
/*  631 */       return 0.0D;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static String callString(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  639 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  641 */       if (method == null)
/*      */       {
/*  643 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  647 */       String s = (String)method.invoke(obj, params);
/*  648 */       return s;
/*      */     
/*      */     }
/*  651 */     catch (Throwable throwable) {
/*      */       
/*  653 */       handleException(throwable, obj, refMethod, params);
/*  654 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object call(Object obj, ReflectorMethod refMethod, Object... params) {
/*      */     try {
/*  662 */       Method method = refMethod.getTargetMethod();
/*      */       
/*  664 */       if (method == null)
/*      */       {
/*  666 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  670 */       Object object = method.invoke(obj, params);
/*  671 */       return object;
/*      */     
/*      */     }
/*  674 */     catch (Throwable throwable) {
/*      */       
/*  676 */       handleException(throwable, obj, refMethod, params);
/*  677 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object getFieldValue(ReflectorField refField) {
/*  683 */     return getFieldValue((Object)null, refField);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object getFieldValue(Object obj, ReflectorField refField) {
/*      */     try {
/*  690 */       Field field = refField.getTargetField();
/*      */       
/*  692 */       if (field == null)
/*      */       {
/*  694 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  698 */       Object object = field.get(obj);
/*  699 */       return object;
/*      */     
/*      */     }
/*  702 */     catch (Throwable throwable) {
/*      */       
/*  704 */       Log.error("", throwable);
/*  705 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean getFieldValueBoolean(ReflectorField refField, boolean def) {
/*      */     try {
/*  713 */       Field field = refField.getTargetField();
/*      */       
/*  715 */       if (field == null)
/*      */       {
/*  717 */         return def;
/*      */       }
/*      */ 
/*      */       
/*  721 */       boolean flag = field.getBoolean(null);
/*  722 */       return flag;
/*      */     
/*      */     }
/*  725 */     catch (Throwable throwable) {
/*      */       
/*  727 */       Log.error("", throwable);
/*  728 */       return def;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean getFieldValueBoolean(Object obj, ReflectorField refField, boolean def) {
/*      */     try {
/*  736 */       Field field = refField.getTargetField();
/*      */       
/*  738 */       if (field == null)
/*      */       {
/*  740 */         return def;
/*      */       }
/*      */ 
/*      */       
/*  744 */       boolean flag = field.getBoolean(obj);
/*  745 */       return flag;
/*      */     
/*      */     }
/*  748 */     catch (Throwable throwable) {
/*      */       
/*  750 */       Log.error("", throwable);
/*  751 */       return def;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object getFieldValue(ReflectorFields refFields, int index) {
/*  757 */     ReflectorField reflectorfield = refFields.getReflectorField(index);
/*  758 */     return (reflectorfield == null) ? null : getFieldValue(reflectorfield);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Object getFieldValue(Object obj, ReflectorFields refFields, int index) {
/*  763 */     ReflectorField reflectorfield = refFields.getReflectorField(index);
/*  764 */     return (reflectorfield == null) ? null : getFieldValue(obj, reflectorfield);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static float getFieldValueFloat(Object obj, ReflectorField refField, float def) {
/*      */     try {
/*  771 */       Field field = refField.getTargetField();
/*      */       
/*  773 */       if (field == null)
/*      */       {
/*  775 */         return def;
/*      */       }
/*      */ 
/*      */       
/*  779 */       float f = field.getFloat(obj);
/*  780 */       return f;
/*      */     
/*      */     }
/*  783 */     catch (Throwable throwable) {
/*      */       
/*  785 */       Log.error("", throwable);
/*  786 */       return def;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getFieldValueInt(Object obj, ReflectorField refField, int def) {
/*      */     try {
/*  794 */       Field field = refField.getTargetField();
/*      */       
/*  796 */       if (field == null)
/*      */       {
/*  798 */         return def;
/*      */       }
/*      */ 
/*      */       
/*  802 */       int i = field.getInt(obj);
/*  803 */       return i;
/*      */     
/*      */     }
/*  806 */     catch (Throwable throwable) {
/*      */       
/*  808 */       Log.error("", throwable);
/*  809 */       return def;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getFieldValueLong(Object obj, ReflectorField refField, long def) {
/*      */     try {
/*  817 */       Field field = refField.getTargetField();
/*      */       
/*  819 */       if (field == null)
/*      */       {
/*  821 */         return def;
/*      */       }
/*      */ 
/*      */       
/*  825 */       long i = field.getLong(obj);
/*  826 */       return i;
/*      */     
/*      */     }
/*  829 */     catch (Throwable throwable) {
/*      */       
/*  831 */       Log.error("", throwable);
/*  832 */       return def;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean setFieldValue(ReflectorField refField, Object value) {
/*  838 */     return setFieldValue(null, refField, value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean setFieldValue(Object obj, ReflectorField refField, Object value) {
/*      */     try {
/*  845 */       Field field = refField.getTargetField();
/*      */       
/*  847 */       if (field == null)
/*      */       {
/*  849 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  853 */       field.set(obj, value);
/*  854 */       return true;
/*      */     
/*      */     }
/*  857 */     catch (Throwable throwable) {
/*      */       
/*  859 */       Log.error("", throwable);
/*  860 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean setFieldValueInt(ReflectorField refField, int value) {
/*  866 */     return setFieldValueInt(null, refField, value);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean setFieldValueInt(Object obj, ReflectorField refField, int value) {
/*      */     try {
/*  873 */       Field field = refField.getTargetField();
/*      */       
/*  875 */       if (field == null)
/*      */       {
/*  877 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  881 */       field.setInt(obj, value);
/*  882 */       return true;
/*      */     
/*      */     }
/*  885 */     catch (Throwable throwable) {
/*      */       
/*  887 */       Log.error("", throwable);
/*  888 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean postForgeBusEvent(ReflectorConstructor constr, Object... params) {
/*  894 */     Object object = newInstance(constr, params);
/*  895 */     return (object == null) ? false : postForgeBusEvent(object);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean postForgeBusEvent(Object event) {
/*  900 */     if (event == null)
/*      */     {
/*  902 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  906 */     Object object = getFieldValue(MinecraftForge_EVENT_BUS);
/*      */     
/*  908 */     if (object == null)
/*      */     {
/*  910 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  914 */     Object object1 = call(object, EventBus_post, new Object[] { event });
/*      */     
/*  916 */     if (!(object1 instanceof Boolean))
/*      */     {
/*  918 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  922 */     Boolean obool = (Boolean)object1;
/*  923 */     return obool.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object newInstance(ReflectorConstructor constr, Object... params) {
/*  931 */     Constructor constructor = constr.getTargetConstructor();
/*      */     
/*  933 */     if (constructor == null)
/*      */     {
/*  935 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  941 */       Object object = constructor.newInstance(params);
/*  942 */       return object;
/*      */     }
/*  944 */     catch (Throwable throwable) {
/*      */       
/*  946 */       handleException(throwable, constr, params);
/*  947 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean matchesTypes(Class[] pTypes, Class[] cTypes) {
/*  954 */     if (pTypes.length != cTypes.length)
/*      */     {
/*  956 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  960 */     for (int i = 0; i < cTypes.length; i++) {
/*      */       
/*  962 */       Class oclass = pTypes[i];
/*  963 */       Class oclass1 = cTypes[i];
/*      */       
/*  965 */       if (oclass != oclass1)
/*      */       {
/*  967 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  971 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void dbgCall(boolean isStatic, String callType, ReflectorMethod refMethod, Object[] params, Object retVal) {
/*  977 */     String s = refMethod.getTargetMethod().getDeclaringClass().getName();
/*  978 */     String s1 = refMethod.getTargetMethod().getName();
/*  979 */     String s2 = "";
/*      */     
/*  981 */     if (isStatic)
/*      */     {
/*  983 */       s2 = " static";
/*      */     }
/*      */     
/*  986 */     Log.dbg(callType + s2 + " " + s + "." + s1 + "(" + ArrayUtils.arrayToString(params) + ") => " + retVal);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void dbgCallVoid(boolean isStatic, String callType, ReflectorMethod refMethod, Object[] params) {
/*  991 */     String s = refMethod.getTargetMethod().getDeclaringClass().getName();
/*  992 */     String s1 = refMethod.getTargetMethod().getName();
/*  993 */     String s2 = "";
/*      */     
/*  995 */     if (isStatic)
/*      */     {
/*  997 */       s2 = " static";
/*      */     }
/*      */     
/* 1000 */     Log.dbg(callType + s2 + " " + s + "." + s1 + "(" + ArrayUtils.arrayToString(params) + ")");
/*      */   }
/*      */ 
/*      */   
/*      */   private static void dbgFieldValue(boolean isStatic, String accessType, ReflectorField refField, Object val) {
/* 1005 */     String s = refField.getTargetField().getDeclaringClass().getName();
/* 1006 */     String s1 = refField.getTargetField().getName();
/* 1007 */     String s2 = "";
/*      */     
/* 1009 */     if (isStatic)
/*      */     {
/* 1011 */       s2 = " static";
/*      */     }
/*      */     
/* 1014 */     Log.dbg(accessType + s2 + " " + s + "." + s1 + " => " + val);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handleException(Throwable e, Object obj, ReflectorMethod refMethod, Object[] params) {
/* 1019 */     if (e instanceof java.lang.reflect.InvocationTargetException) {
/*      */       
/* 1021 */       Throwable throwable = e.getCause();
/*      */       
/* 1023 */       if (throwable instanceof RuntimeException) {
/*      */         
/* 1025 */         RuntimeException runtimeexception = (RuntimeException)throwable;
/* 1026 */         throw runtimeexception;
/*      */       } 
/*      */ 
/*      */       
/* 1030 */       Log.error("", e);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1035 */       Log.warn("*** Exception outside of method ***");
/* 1036 */       Log.warn("Method deactivated: " + refMethod.getTargetMethod());
/* 1037 */       refMethod.deactivate();
/*      */       
/* 1039 */       if (e instanceof IllegalArgumentException) {
/*      */         
/* 1041 */         Log.warn("*** IllegalArgumentException ***");
/* 1042 */         Log.warn("Method: " + refMethod.getTargetMethod());
/* 1043 */         Log.warn("Object: " + obj);
/* 1044 */         Log.warn("Parameter classes: " + ArrayUtils.arrayToString(getClasses(params)));
/* 1045 */         Log.warn("Parameters: " + ArrayUtils.arrayToString(params));
/*      */       } 
/*      */       
/* 1048 */       Log.warn("", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void handleException(Throwable e, ReflectorConstructor refConstr, Object[] params) {
/* 1054 */     if (e instanceof java.lang.reflect.InvocationTargetException) {
/*      */       
/* 1056 */       Log.error("", e);
/*      */     }
/*      */     else {
/*      */       
/* 1060 */       Log.warn("*** Exception outside of constructor ***");
/* 1061 */       Log.warn("Constructor deactivated: " + refConstr.getTargetConstructor());
/* 1062 */       refConstr.deactivate();
/*      */       
/* 1064 */       if (e instanceof IllegalArgumentException) {
/*      */         
/* 1066 */         Log.warn("*** IllegalArgumentException ***");
/* 1067 */         Log.warn("Constructor: " + refConstr.getTargetConstructor());
/* 1068 */         Log.warn("Parameter classes: " + ArrayUtils.arrayToString(getClasses(params)));
/* 1069 */         Log.warn("Parameters: " + ArrayUtils.arrayToString(params));
/*      */       } 
/*      */       
/* 1072 */       Log.warn("", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static Object[] getClasses(Object[] objs) {
/* 1078 */     if (objs == null)
/*      */     {
/* 1080 */       return (Object[])new Class[0];
/*      */     }
/*      */ 
/*      */     
/* 1084 */     Class[] aclass = new Class[objs.length];
/*      */     
/* 1086 */     for (int i = 0; i < aclass.length; i++) {
/*      */       
/* 1088 */       Object object = objs[i];
/*      */       
/* 1090 */       if (object != null)
/*      */       {
/* 1092 */         aclass[i] = object.getClass();
/*      */       }
/*      */     } 
/*      */     
/* 1096 */     return (Object[])aclass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ReflectorField[] getReflectorFields(ReflectorClass parentClass, Class fieldType, int count) {
/* 1102 */     ReflectorField[] areflectorfield = new ReflectorField[count];
/*      */     
/* 1104 */     for (int i = 0; i < areflectorfield.length; i++)
/*      */     {
/* 1106 */       areflectorfield[i] = new ReflectorField(parentClass, fieldType, i);
/*      */     }
/*      */     
/* 1109 */     return areflectorfield;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean logEntry(String str) {
/* 1114 */     LOGGER.info("[OptiFine] " + str);
/* 1115 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean registerResolvable(final String str) {
/* 1120 */     IResolvable iresolvable = new IResolvable()
/*      */       {
/*      */         public void resolve()
/*      */         {
/* 1124 */           Reflector.LOGGER.info("[OptiFine] " + str);
/*      */         }
/*      */       };
/* 1127 */     ReflectorResolver.register(iresolvable);
/* 1128 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\reflect\Reflector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */