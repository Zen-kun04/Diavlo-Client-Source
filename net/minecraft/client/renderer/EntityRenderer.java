/*      */ package net.minecraft.client.renderer;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.gson.JsonSyntaxException;
/*      */ import java.io.IOException;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockBed;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.entity.AbstractClientPlayer;
/*      */ import net.minecraft.client.gui.GuiChat;
/*      */ import net.minecraft.client.gui.GuiMainMenu;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.gui.MapItemRenderer;
/*      */ import net.minecraft.client.gui.ScaledResolution;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.particle.EffectRenderer;
/*      */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*      */ import net.minecraft.client.renderer.culling.ClippingHelper;
/*      */ import net.minecraft.client.renderer.culling.ClippingHelperImpl;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.culling.ICamera;
/*      */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.resources.I18n;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.settings.GameSettings;
/*      */ import net.minecraft.client.shader.ShaderGroup;
/*      */ import net.minecraft.client.shader.ShaderLinkHelper;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.boss.BossStatus;
/*      */ import net.minecraft.entity.passive.EntityAnimal;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.event.ClickEvent;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.server.integrated.IntegratedServer;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.ChatStyle;
/*      */ import net.minecraft.util.EntitySelectors;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.MouseFilter;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.optifine.CustomColors;
/*      */ import net.optifine.GlErrors;
/*      */ import net.optifine.Lagometer;
/*      */ import net.optifine.RandomEntities;
/*      */ import net.optifine.gui.GuiChatOF;
/*      */ import net.optifine.reflect.Reflector;
/*      */ import net.optifine.reflect.ReflectorForge;
/*      */ import net.optifine.reflect.ReflectorResolver;
/*      */ import net.optifine.shaders.Shaders;
/*      */ import net.optifine.shaders.ShadersRender;
/*      */ import net.optifine.util.MemoryMonitor;
/*      */ import net.optifine.util.TextureUtils;
/*      */ import net.optifine.util.TimedEvent;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.input.Mouse;
/*      */ import org.lwjgl.opengl.Display;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GLContext;
/*      */ import org.lwjgl.util.glu.Project;
/*      */ 
/*      */ 
/*      */ public class EntityRenderer
/*      */   implements IResourceManagerReloadListener
/*      */ {
/*  102 */   private static final Logger logger = LogManager.getLogger();
/*  103 */   private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
/*  104 */   private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
/*      */   public static boolean anaglyphEnable;
/*      */   public static int anaglyphField;
/*      */   private Minecraft mc;
/*      */   private final IResourceManager resourceManager;
/*  109 */   private Random random = new Random();
/*      */   private float farPlaneDistance;
/*      */   public ItemRenderer itemRenderer;
/*      */   private final MapItemRenderer theMapItemRenderer;
/*      */   private int rendererUpdateCount;
/*      */   private Entity pointedEntity;
/*  115 */   private MouseFilter mouseFilterXAxis = new MouseFilter();
/*  116 */   private MouseFilter mouseFilterYAxis = new MouseFilter();
/*  117 */   private float thirdPersonDistance = 4.0F;
/*  118 */   private float thirdPersonDistanceTemp = 4.0F;
/*      */   private float smoothCamYaw;
/*      */   private float smoothCamPitch;
/*      */   private float smoothCamFilterX;
/*      */   private float smoothCamFilterY;
/*      */   private float smoothCamPartialTicks;
/*      */   private float fovModifierHand;
/*      */   private float fovModifierHandPrev;
/*      */   private float bossColorModifier;
/*      */   private float bossColorModifierPrev;
/*      */   private boolean cloudFog;
/*      */   private boolean renderHand = true;
/*      */   private boolean drawBlockOutline = true;
/*  131 */   private long prevFrameTime = Minecraft.getSystemTime();
/*      */   private long renderEndNanoTime;
/*      */   private final DynamicTexture lightmapTexture;
/*      */   private final int[] lightmapColors;
/*      */   private final ResourceLocation locationLightMap;
/*      */   private boolean lightmapUpdateNeeded;
/*      */   private float torchFlickerX;
/*      */   private float torchFlickerDX;
/*      */   private int rainSoundCounter;
/*  140 */   private float[] rainXCoords = new float[1024];
/*  141 */   private float[] rainYCoords = new float[1024];
/*  142 */   private FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
/*      */   public float fogColorRed;
/*      */   public float fogColorGreen;
/*      */   public float fogColorBlue;
/*      */   private float fogColor2;
/*      */   private float fogColor1;
/*  148 */   private int debugViewDirection = 0;
/*      */   private boolean debugView = false;
/*  150 */   private double cameraZoom = 1.0D;
/*      */   private double cameraYaw;
/*      */   private double cameraPitch;
/*      */   private ShaderGroup theShaderGroup;
/*  154 */   private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[] { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };
/*  155 */   public static final int shaderCount = shaderResourceLocations.length;
/*      */   private int shaderIndex;
/*      */   private boolean useShader;
/*      */   public int frameCount;
/*      */   private boolean initialized = false;
/*  160 */   private World updatedWorld = null;
/*      */   private boolean showDebugInfo = false;
/*      */   public boolean fogStandard = false;
/*  163 */   private float clipDistance = 128.0F;
/*  164 */   private long lastServerTime = 0L;
/*  165 */   private int lastServerTicks = 0;
/*  166 */   private int serverWaitTime = 0;
/*  167 */   private int serverWaitTimeCurrent = 0;
/*  168 */   private float avgServerTimeDiff = 0.0F;
/*  169 */   private float avgServerTickDiff = 0.0F;
/*  170 */   private ShaderGroup[] fxaaShaders = new ShaderGroup[10];
/*      */   
/*      */   private boolean loadVisibleChunks = false;
/*      */   
/*      */   public EntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
/*  175 */     this.shaderIndex = shaderCount;
/*  176 */     this.useShader = false;
/*  177 */     this.frameCount = 0;
/*  178 */     this.mc = mcIn;
/*  179 */     this.resourceManager = resourceManagerIn;
/*  180 */     this.itemRenderer = mcIn.getItemRenderer();
/*  181 */     this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
/*  182 */     this.lightmapTexture = new DynamicTexture(16, 16);
/*  183 */     this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
/*  184 */     this.lightmapColors = this.lightmapTexture.getTextureData();
/*  185 */     this.theShaderGroup = null;
/*      */     
/*  187 */     for (int i = 0; i < 32; i++) {
/*      */       
/*  189 */       for (int j = 0; j < 32; j++) {
/*      */         
/*  191 */         float f = (j - 16);
/*  192 */         float f1 = (i - 16);
/*  193 */         float f2 = MathHelper.sqrt_float(f * f + f1 * f1);
/*  194 */         this.rainXCoords[i << 5 | j] = -f1 / f2;
/*  195 */         this.rainYCoords[i << 5 | j] = f / f2;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isShaderActive() {
/*  202 */     return (OpenGlHelper.shadersSupported && this.theShaderGroup != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopUseShader() {
/*  207 */     if (this.theShaderGroup != null)
/*      */     {
/*  209 */       this.theShaderGroup.deleteShaderGroup();
/*      */     }
/*      */     
/*  212 */     this.theShaderGroup = null;
/*  213 */     this.shaderIndex = shaderCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public void switchUseShader() {
/*  218 */     this.useShader = !this.useShader;
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadEntityShader(Entity entityIn) {
/*  223 */     if (OpenGlHelper.shadersSupported) {
/*      */       
/*  225 */       if (this.theShaderGroup != null)
/*      */       {
/*  227 */         this.theShaderGroup.deleteShaderGroup();
/*      */       }
/*      */       
/*  230 */       this.theShaderGroup = null;
/*      */       
/*  232 */       if (entityIn instanceof net.minecraft.entity.monster.EntityCreeper) {
/*      */         
/*  234 */         loadShader(new ResourceLocation("shaders/post/creeper.json"));
/*      */       }
/*  236 */       else if (entityIn instanceof net.minecraft.entity.monster.EntitySpider) {
/*      */         
/*  238 */         loadShader(new ResourceLocation("shaders/post/spider.json"));
/*      */       }
/*  240 */       else if (entityIn instanceof net.minecraft.entity.monster.EntityEnderman) {
/*      */         
/*  242 */         loadShader(new ResourceLocation("shaders/post/invert.json"));
/*      */       }
/*  244 */       else if (Reflector.ForgeHooksClient_loadEntityShader.exists()) {
/*      */         
/*  246 */         Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, new Object[] { entityIn, this });
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void activateNextShader() {
/*  253 */     if (OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
/*      */       
/*  255 */       if (this.theShaderGroup != null)
/*      */       {
/*  257 */         this.theShaderGroup.deleteShaderGroup();
/*      */       }
/*      */       
/*  260 */       this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);
/*      */       
/*  262 */       if (this.shaderIndex != shaderCount) {
/*      */         
/*  264 */         loadShader(shaderResourceLocations[this.shaderIndex]);
/*      */       }
/*      */       else {
/*      */         
/*  268 */         this.theShaderGroup = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadShader(ResourceLocation resourceLocationIn) {
/*  275 */     if (OpenGlHelper.isFramebufferEnabled()) {
/*      */       
/*      */       try {
/*      */         
/*  279 */         this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), resourceLocationIn);
/*  280 */         this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
/*  281 */         this.useShader = true;
/*      */       }
/*  283 */       catch (IOException ioexception) {
/*      */         
/*  285 */         logger.warn("Failed to load shader: " + resourceLocationIn, ioexception);
/*  286 */         this.shaderIndex = shaderCount;
/*  287 */         this.useShader = false;
/*      */       }
/*  289 */       catch (JsonSyntaxException jsonsyntaxexception) {
/*      */         
/*  291 */         logger.warn("Failed to load shader: " + resourceLocationIn, (Throwable)jsonsyntaxexception);
/*  292 */         this.shaderIndex = shaderCount;
/*  293 */         this.useShader = false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager) {
/*  300 */     if (this.theShaderGroup != null)
/*      */     {
/*  302 */       this.theShaderGroup.deleteShaderGroup();
/*      */     }
/*      */     
/*  305 */     this.theShaderGroup = null;
/*      */     
/*  307 */     if (this.shaderIndex != shaderCount) {
/*      */       
/*  309 */       loadShader(shaderResourceLocations[this.shaderIndex]);
/*      */     }
/*      */     else {
/*      */       
/*  313 */       loadEntityShader(this.mc.getRenderViewEntity());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRenderer() {
/*  319 */     if (OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null)
/*      */     {
/*  321 */       ShaderLinkHelper.setNewStaticShaderLinkHelper();
/*      */     }
/*      */     
/*  324 */     updateFovModifierHand();
/*  325 */     updateTorchFlicker();
/*  326 */     this.fogColor2 = this.fogColor1;
/*  327 */     this.thirdPersonDistanceTemp = this.thirdPersonDistance;
/*      */     
/*  329 */     if (this.mc.gameSettings.smoothCamera) {
/*      */       
/*  331 */       float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
/*  332 */       float f1 = f * f * f * 8.0F;
/*  333 */       this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f1);
/*  334 */       this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f1);
/*  335 */       this.smoothCamPartialTicks = 0.0F;
/*  336 */       this.smoothCamYaw = 0.0F;
/*  337 */       this.smoothCamPitch = 0.0F;
/*      */     }
/*      */     else {
/*      */       
/*  341 */       this.smoothCamFilterX = 0.0F;
/*  342 */       this.smoothCamFilterY = 0.0F;
/*  343 */       this.mouseFilterXAxis.reset();
/*  344 */       this.mouseFilterYAxis.reset();
/*      */     } 
/*      */     
/*  347 */     if (this.mc.getRenderViewEntity() == null)
/*      */     {
/*  349 */       this.mc.setRenderViewEntity((Entity)this.mc.thePlayer);
/*      */     }
/*      */     
/*  352 */     Entity entity = this.mc.getRenderViewEntity();
/*  353 */     double d2 = entity.posX;
/*  354 */     double d0 = entity.posY + entity.getEyeHeight();
/*  355 */     double d1 = entity.posZ;
/*  356 */     float f2 = this.mc.theWorld.getLightBrightness(new BlockPos(d2, d0, d1));
/*  357 */     float f3 = this.mc.gameSettings.renderDistanceChunks / 16.0F;
/*  358 */     f3 = MathHelper.clamp_float(f3, 0.0F, 1.0F);
/*  359 */     float f4 = f2 * (1.0F - f3) + f3;
/*  360 */     this.fogColor1 += (f4 - this.fogColor1) * 0.1F;
/*  361 */     this.rendererUpdateCount++;
/*  362 */     this.itemRenderer.updateEquippedItem();
/*  363 */     addRainParticles();
/*  364 */     this.bossColorModifierPrev = this.bossColorModifier;
/*      */     
/*  366 */     if (BossStatus.hasColorModifier) {
/*      */       
/*  368 */       this.bossColorModifier += 0.05F;
/*      */       
/*  370 */       if (this.bossColorModifier > 1.0F)
/*      */       {
/*  372 */         this.bossColorModifier = 1.0F;
/*      */       }
/*      */       
/*  375 */       BossStatus.hasColorModifier = false;
/*      */     }
/*  377 */     else if (this.bossColorModifier > 0.0F) {
/*      */       
/*  379 */       this.bossColorModifier -= 0.0125F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public ShaderGroup getShaderGroup() {
/*  385 */     return this.theShaderGroup;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateShaderGroupSize(int width, int height) {
/*  390 */     if (OpenGlHelper.shadersSupported) {
/*      */       
/*  392 */       if (this.theShaderGroup != null)
/*      */       {
/*  394 */         this.theShaderGroup.createBindFramebuffers(width, height);
/*      */       }
/*      */       
/*  397 */       this.mc.renderGlobal.createBindEntityOutlineFbs(width, height);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void getMouseOver(float partialTicks) {
/*  403 */     Entity entity = this.mc.getRenderViewEntity();
/*      */     
/*  405 */     if (entity != null && this.mc.theWorld != null) {
/*      */       
/*  407 */       this.mc.mcProfiler.startSection("pick");
/*  408 */       this.mc.pointedEntity = null;
/*  409 */       double d0 = this.mc.playerController.getBlockReachDistance();
/*  410 */       this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
/*  411 */       double d1 = d0;
/*  412 */       Vec3 vec3 = entity.getPositionEyes(partialTicks);
/*  413 */       boolean flag = false;
/*  414 */       int i = 3;
/*      */       
/*  416 */       if (this.mc.playerController.extendedReach()) {
/*      */         
/*  418 */         d0 = 6.0D;
/*  419 */         d1 = 6.0D;
/*      */       }
/*  421 */       else if (d0 > 3.0D) {
/*      */         
/*  423 */         flag = true;
/*      */       } 
/*      */       
/*  426 */       if (this.mc.objectMouseOver != null)
/*      */       {
/*  428 */         d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
/*      */       }
/*      */       
/*  431 */       Vec3 vec31 = entity.getLook(partialTicks);
/*  432 */       Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
/*  433 */       this.pointedEntity = null;
/*  434 */       Vec3 vec33 = null;
/*  435 */       float f = 1.0F;
/*  436 */       List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
/*      */             {
/*      */               public boolean apply(Entity p_apply_1_)
/*      */               {
/*  440 */                 return p_apply_1_.canBeCollidedWith();
/*      */               }
/*      */             }));
/*  443 */       double d2 = d1;
/*      */       
/*  445 */       for (int j = 0; j < list.size(); j++) {
/*      */         
/*  447 */         Entity entity1 = list.get(j);
/*  448 */         float f1 = entity1.getCollisionBorderSize();
/*  449 */         AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
/*  450 */         MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
/*      */         
/*  452 */         if (axisalignedbb.isVecInside(vec3)) {
/*      */           
/*  454 */           if (d2 >= 0.0D)
/*      */           {
/*  456 */             this.pointedEntity = entity1;
/*  457 */             vec33 = (movingobjectposition == null) ? vec3 : movingobjectposition.hitVec;
/*  458 */             d2 = 0.0D;
/*      */           }
/*      */         
/*  461 */         } else if (movingobjectposition != null) {
/*      */           
/*  463 */           double d3 = vec3.distanceTo(movingobjectposition.hitVec);
/*      */           
/*  465 */           if (d3 < d2 || d2 == 0.0D) {
/*      */             
/*  467 */             boolean flag1 = false;
/*      */             
/*  469 */             if (Reflector.ForgeEntity_canRiderInteract.exists())
/*      */             {
/*  471 */               flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
/*      */             }
/*      */             
/*  474 */             if (!flag1 && entity1 == entity.ridingEntity) {
/*      */               
/*  476 */               if (d2 == 0.0D)
/*      */               {
/*  478 */                 this.pointedEntity = entity1;
/*  479 */                 vec33 = movingobjectposition.hitVec;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/*  484 */               this.pointedEntity = entity1;
/*  485 */               vec33 = movingobjectposition.hitVec;
/*  486 */               d2 = d3;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  492 */       if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D) {
/*      */         
/*  494 */         this.pointedEntity = null;
/*  495 */         this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing)null, new BlockPos(vec33));
/*      */       } 
/*      */       
/*  498 */       if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
/*      */         
/*  500 */         this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
/*      */         
/*  502 */         if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof net.minecraft.entity.item.EntityItemFrame)
/*      */         {
/*  504 */           this.mc.pointedEntity = this.pointedEntity;
/*      */         }
/*      */       } 
/*      */       
/*  508 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateFovModifierHand() {
/*  514 */     float f = 1.0F;
/*      */     
/*  516 */     if (this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
/*      */       
/*  518 */       AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)this.mc.getRenderViewEntity();
/*  519 */       f = abstractclientplayer.getFovModifier();
/*      */     } 
/*      */     
/*  522 */     this.fovModifierHandPrev = this.fovModifierHand;
/*  523 */     this.fovModifierHand += (f - this.fovModifierHand) * 0.5F;
/*      */     
/*  525 */     if (this.fovModifierHand > 1.5F)
/*      */     {
/*  527 */       this.fovModifierHand = 1.5F;
/*      */     }
/*      */     
/*  530 */     if (this.fovModifierHand < 0.1F)
/*      */     {
/*  532 */       this.fovModifierHand = 0.1F;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private float getFOVModifier(float partialTicks, boolean useFOVSetting) {
/*  538 */     if (this.debugView)
/*      */     {
/*  540 */       return 90.0F;
/*      */     }
/*      */ 
/*      */     
/*  544 */     Entity entity = this.mc.getRenderViewEntity();
/*  545 */     float f = 70.0F;
/*      */     
/*  547 */     if (useFOVSetting) {
/*      */       
/*  549 */       f = this.mc.gameSettings.fovSetting;
/*      */       
/*  551 */       if (Config.isDynamicFov())
/*      */       {
/*  553 */         f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
/*      */       }
/*      */     } 
/*      */     
/*  557 */     boolean flag = false;
/*      */     
/*  559 */     if (this.mc.currentScreen == null) {
/*      */       
/*  561 */       GameSettings gamesettings = this.mc.gameSettings;
/*  562 */       flag = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
/*      */     } 
/*      */     
/*  565 */     if (flag) {
/*      */       
/*  567 */       if (!Config.zoomMode) {
/*      */         
/*  569 */         Config.zoomMode = true;
/*  570 */         Config.zoomSmoothCamera = this.mc.gameSettings.smoothCamera;
/*  571 */         this.mc.gameSettings.smoothCamera = true;
/*  572 */         this.mc.renderGlobal.displayListEntitiesDirty = true;
/*      */       } 
/*      */       
/*  575 */       if (Config.zoomMode)
/*      */       {
/*  577 */         f /= 4.0F;
/*      */       }
/*      */     }
/*  580 */     else if (Config.zoomMode) {
/*      */       
/*  582 */       Config.zoomMode = false;
/*  583 */       this.mc.gameSettings.smoothCamera = Config.zoomSmoothCamera;
/*  584 */       this.mouseFilterXAxis = new MouseFilter();
/*  585 */       this.mouseFilterYAxis = new MouseFilter();
/*  586 */       this.mc.renderGlobal.displayListEntitiesDirty = true;
/*      */     } 
/*      */     
/*  589 */     if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0F) {
/*      */       
/*  591 */       float f1 = ((EntityLivingBase)entity).deathTime + partialTicks;
/*  592 */       f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
/*      */     } 
/*      */     
/*  595 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint((World)this.mc.theWorld, entity, partialTicks);
/*      */     
/*  597 */     if (block.getMaterial() == Material.water)
/*      */     {
/*  599 */       f = f * 60.0F / 70.0F;
/*      */     }
/*      */     
/*  602 */     return Reflector.ForgeHooksClient_getFOVModifier.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, new Object[] { this, entity, block, Float.valueOf(partialTicks), Float.valueOf(f) }) : f;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void hurtCameraEffect(float partialTicks) {
/*  608 */     if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
/*      */       
/*  610 */       EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
/*  611 */       float f = entitylivingbase.hurtTime - partialTicks;
/*      */       
/*  613 */       if (entitylivingbase.getHealth() <= 0.0F) {
/*      */         
/*  615 */         float f1 = entitylivingbase.deathTime + partialTicks;
/*  616 */         GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
/*      */       } 
/*      */       
/*  619 */       if (f < 0.0F) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  624 */       f /= entitylivingbase.maxHurtTime;
/*  625 */       f = MathHelper.sin(f * f * f * f * 3.1415927F);
/*  626 */       float f2 = entitylivingbase.attackedAtYaw;
/*  627 */       GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
/*  628 */       GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
/*  629 */       GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setupViewBobbing(float partialTicks) {
/*  635 */     if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
/*      */       
/*  637 */       EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
/*  638 */       float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
/*  639 */       float f1 = -(entityplayer.distanceWalkedModified + f * partialTicks);
/*  640 */       float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * partialTicks;
/*  641 */       float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * partialTicks;
/*  642 */       GlStateManager.translate(MathHelper.sin(f1 * 3.1415927F) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * 3.1415927F) * f2), 0.0F);
/*  643 */       GlStateManager.rotate(MathHelper.sin(f1 * 3.1415927F) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
/*  644 */       GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * 3.1415927F - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
/*  645 */       GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void orientCamera(float partialTicks) {
/*  651 */     Entity entity = this.mc.getRenderViewEntity();
/*  652 */     float f = entity.getEyeHeight();
/*  653 */     double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/*  654 */     double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
/*  655 */     double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/*      */     
/*  657 */     if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
/*      */       
/*  659 */       f = (float)(f + 1.0D);
/*  660 */       GlStateManager.translate(0.0F, 0.3F, 0.0F);
/*      */       
/*  662 */       if (!this.mc.gameSettings.debugCamEnable)
/*      */       {
/*  664 */         BlockPos blockpos = new BlockPos(entity);
/*  665 */         IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
/*  666 */         Block block = iblockstate.getBlock();
/*      */         
/*  668 */         if (Reflector.ForgeHooksClient_orientBedCamera.exists()) {
/*      */           
/*  670 */           Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[] { this.mc.theWorld, blockpos, iblockstate, entity });
/*      */         }
/*  672 */         else if (block == Blocks.bed) {
/*      */           
/*  674 */           int j = ((EnumFacing)iblockstate.getValue((IProperty)BlockBed.FACING)).getHorizontalIndex();
/*  675 */           GlStateManager.rotate((j * 90), 0.0F, 1.0F, 0.0F);
/*      */         } 
/*      */         
/*  678 */         GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
/*  679 */         GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
/*      */       }
/*      */     
/*  682 */     } else if (this.mc.gameSettings.thirdPersonView > 0) {
/*      */       
/*  684 */       double d3 = (this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * partialTicks);
/*      */       
/*  686 */       if (this.mc.gameSettings.debugCamEnable)
/*      */       {
/*  688 */         GlStateManager.translate(0.0F, 0.0F, (float)-d3);
/*      */       }
/*      */       else
/*      */       {
/*  692 */         float f1 = entity.rotationYaw;
/*  693 */         float f2 = entity.rotationPitch;
/*      */         
/*  695 */         if (this.mc.gameSettings.thirdPersonView == 2)
/*      */         {
/*  697 */           f2 += 180.0F;
/*      */         }
/*      */         
/*  700 */         double d4 = (-MathHelper.sin(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F)) * d3;
/*  701 */         double d5 = (MathHelper.cos(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F)) * d3;
/*  702 */         double d6 = -MathHelper.sin(f2 / 180.0F * 3.1415927F) * d3;
/*      */         
/*  704 */         for (int i = 0; i < 8; i++) {
/*      */           
/*  706 */           float f3 = ((i & 0x1) * 2 - 1);
/*  707 */           float f4 = ((i >> 1 & 0x1) * 2 - 1);
/*  708 */           float f5 = ((i >> 2 & 0x1) * 2 - 1);
/*  709 */           f3 *= 0.1F;
/*  710 */           f4 *= 0.1F;
/*  711 */           f5 *= 0.1F;
/*  712 */           MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(new Vec3(d0 + f3, d1 + f4, d2 + f5), new Vec3(d0 - d4 + f3 + f5, d1 - d6 + f4, d2 - d5 + f5));
/*      */           
/*  714 */           if (movingobjectposition != null) {
/*      */             
/*  716 */             double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));
/*      */             
/*  718 */             if (d7 < d3)
/*      */             {
/*  720 */               d3 = d7;
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/*  725 */         if (this.mc.gameSettings.thirdPersonView == 2)
/*      */         {
/*  727 */           GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*      */         }
/*      */         
/*  730 */         GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
/*  731 */         GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
/*  732 */         GlStateManager.translate(0.0F, 0.0F, (float)-d3);
/*  733 */         GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
/*  734 */         GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  739 */       GlStateManager.translate(0.0F, 0.0F, -0.1F);
/*      */     } 
/*      */     
/*  742 */     if (Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
/*      */       
/*  744 */       if (!this.mc.gameSettings.debugCamEnable)
/*      */       {
/*  746 */         float f6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F;
/*  747 */         float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
/*  748 */         float f8 = 0.0F;
/*      */         
/*  750 */         if (entity instanceof EntityAnimal) {
/*      */           
/*  752 */           EntityAnimal entityanimal1 = (EntityAnimal)entity;
/*  753 */           f6 = entityanimal1.prevRotationYawHead + (entityanimal1.rotationYawHead - entityanimal1.prevRotationYawHead) * partialTicks + 180.0F;
/*      */         } 
/*      */         
/*  756 */         Block block1 = ActiveRenderInfo.getBlockAtEntityViewpoint((World)this.mc.theWorld, entity, partialTicks);
/*  757 */         Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, new Object[] { this, entity, block1, Float.valueOf(partialTicks), Float.valueOf(f6), Float.valueOf(f7), Float.valueOf(f8) });
/*  758 */         Reflector.postForgeBusEvent(object);
/*  759 */         f8 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_roll, f8);
/*  760 */         f7 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_pitch, f7);
/*  761 */         f6 = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_CameraSetup_yaw, f6);
/*  762 */         GlStateManager.rotate(f8, 0.0F, 0.0F, 1.0F);
/*  763 */         GlStateManager.rotate(f7, 1.0F, 0.0F, 0.0F);
/*  764 */         GlStateManager.rotate(f6, 0.0F, 1.0F, 0.0F);
/*      */       }
/*      */     
/*  767 */     } else if (!this.mc.gameSettings.debugCamEnable) {
/*      */       
/*  769 */       GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
/*      */       
/*  771 */       if (entity instanceof EntityAnimal) {
/*      */         
/*  773 */         EntityAnimal entityanimal = (EntityAnimal)entity;
/*  774 */         GlStateManager.rotate(entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
/*      */       }
/*      */       else {
/*      */         
/*  778 */         GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, 1.0F, 0.0F);
/*      */       } 
/*      */     } 
/*      */     
/*  782 */     GlStateManager.translate(0.0F, -f, 0.0F);
/*  783 */     d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/*  784 */     d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
/*  785 */     d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/*  786 */     this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setupCameraTransform(float partialTicks, int pass) {
/*  791 */     this.farPlaneDistance = (this.mc.gameSettings.renderDistanceChunks * 16);
/*      */     
/*  793 */     if (Config.isFogFancy())
/*      */     {
/*  795 */       this.farPlaneDistance *= 0.95F;
/*      */     }
/*      */     
/*  798 */     if (Config.isFogFast())
/*      */     {
/*  800 */       this.farPlaneDistance *= 0.83F;
/*      */     }
/*      */     
/*  803 */     GlStateManager.matrixMode(5889);
/*  804 */     GlStateManager.loadIdentity();
/*  805 */     float f = 0.07F;
/*      */     
/*  807 */     if (this.mc.gameSettings.anaglyph)
/*      */     {
/*  809 */       GlStateManager.translate(-(pass * 2 - 1) * f, 0.0F, 0.0F);
/*      */     }
/*      */     
/*  812 */     this.clipDistance = this.farPlaneDistance * 2.0F;
/*      */     
/*  814 */     if (this.clipDistance < 173.0F)
/*      */     {
/*  816 */       this.clipDistance = 173.0F;
/*      */     }
/*      */     
/*  819 */     if (this.cameraZoom != 1.0D) {
/*      */       
/*  821 */       GlStateManager.translate((float)this.cameraYaw, (float)-this.cameraPitch, 0.0F);
/*  822 */       GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
/*      */     } 
/*      */     
/*  825 */     Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/*  826 */     GlStateManager.matrixMode(5888);
/*  827 */     GlStateManager.loadIdentity();
/*      */     
/*  829 */     if (this.mc.gameSettings.anaglyph)
/*      */     {
/*  831 */       GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
/*      */     }
/*      */     
/*  834 */     hurtCameraEffect(partialTicks);
/*      */     
/*  836 */     if (this.mc.gameSettings.viewBobbing)
/*      */     {
/*  838 */       setupViewBobbing(partialTicks);
/*      */     }
/*      */     
/*  841 */     float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
/*      */     
/*  843 */     if (f1 > 0.0F) {
/*      */       
/*  845 */       int i = 20;
/*      */       
/*  847 */       if (this.mc.thePlayer.isPotionActive(Potion.confusion))
/*      */       {
/*  849 */         i = 7;
/*      */       }
/*      */       
/*  852 */       float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
/*  853 */       f2 *= f2;
/*  854 */       GlStateManager.rotate((this.rendererUpdateCount + partialTicks) * i, 0.0F, 1.0F, 1.0F);
/*  855 */       GlStateManager.scale(1.0F / f2, 1.0F, 1.0F);
/*  856 */       GlStateManager.rotate(-(this.rendererUpdateCount + partialTicks) * i, 0.0F, 1.0F, 1.0F);
/*      */     } 
/*      */     
/*  859 */     orientCamera(partialTicks);
/*      */     
/*  861 */     if (this.debugView)
/*      */     {
/*  863 */       switch (this.debugViewDirection) {
/*      */         
/*      */         case 0:
/*  866 */           GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/*      */           break;
/*      */         
/*      */         case 1:
/*  870 */           GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*      */           break;
/*      */         
/*      */         case 2:
/*  874 */           GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
/*      */           break;
/*      */         
/*      */         case 3:
/*  878 */           GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/*      */           break;
/*      */         
/*      */         case 4:
/*  882 */           GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/*      */           break;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void renderHand(float partialTicks, int xOffset) {
/*  889 */     renderHand(partialTicks, xOffset, true, true, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderHand(float p_renderHand_1_, int p_renderHand_2_, boolean p_renderHand_3_, boolean p_renderHand_4_, boolean p_renderHand_5_) {
/*  894 */     if (!this.debugView) {
/*      */       
/*  896 */       GlStateManager.matrixMode(5889);
/*  897 */       GlStateManager.loadIdentity();
/*  898 */       float f = 0.07F;
/*      */       
/*  900 */       if (this.mc.gameSettings.anaglyph)
/*      */       {
/*  902 */         GlStateManager.translate(-(p_renderHand_2_ * 2 - 1) * f, 0.0F, 0.0F);
/*      */       }
/*      */       
/*  905 */       if (Config.isShaders())
/*      */       {
/*  907 */         Shaders.applyHandDepth();
/*      */       }
/*      */       
/*  910 */       Project.gluPerspective(getFOVModifier(p_renderHand_1_, false), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
/*  911 */       GlStateManager.matrixMode(5888);
/*  912 */       GlStateManager.loadIdentity();
/*      */       
/*  914 */       if (this.mc.gameSettings.anaglyph)
/*      */       {
/*  916 */         GlStateManager.translate((p_renderHand_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
/*      */       }
/*      */       
/*  919 */       boolean flag = false;
/*      */       
/*  921 */       if (p_renderHand_3_) {
/*      */         
/*  923 */         GlStateManager.pushMatrix();
/*  924 */         hurtCameraEffect(p_renderHand_1_);
/*      */         
/*  926 */         if (this.mc.gameSettings.viewBobbing)
/*      */         {
/*  928 */           setupViewBobbing(p_renderHand_1_);
/*      */         }
/*      */         
/*  931 */         flag = (this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping());
/*  932 */         boolean flag1 = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, p_renderHand_1_, p_renderHand_2_);
/*      */         
/*  934 */         if (flag1 && this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
/*      */           
/*  936 */           enableLightmap();
/*      */           
/*  938 */           if (Config.isShaders()) {
/*      */             
/*  940 */             ShadersRender.renderItemFP(this.itemRenderer, p_renderHand_1_, p_renderHand_5_);
/*      */           }
/*      */           else {
/*      */             
/*  944 */             this.itemRenderer.renderItemInFirstPerson(p_renderHand_1_);
/*      */           } 
/*      */           
/*  947 */           disableLightmap();
/*      */         } 
/*      */         
/*  950 */         GlStateManager.popMatrix();
/*      */       } 
/*      */       
/*  953 */       if (!p_renderHand_4_) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  958 */       disableLightmap();
/*      */       
/*  960 */       if (this.mc.gameSettings.thirdPersonView == 0 && !flag) {
/*      */         
/*  962 */         this.itemRenderer.renderOverlays(p_renderHand_1_);
/*  963 */         hurtCameraEffect(p_renderHand_1_);
/*      */       } 
/*      */       
/*  966 */       if (this.mc.gameSettings.viewBobbing)
/*      */       {
/*  968 */         setupViewBobbing(p_renderHand_1_);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void disableLightmap() {
/*  975 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/*  976 */     GlStateManager.disableTexture2D();
/*  977 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*      */     
/*  979 */     if (Config.isShaders())
/*      */     {
/*  981 */       Shaders.disableLightmap();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void enableLightmap() {
/*  987 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/*  988 */     GlStateManager.matrixMode(5890);
/*  989 */     GlStateManager.loadIdentity();
/*  990 */     float f = 0.00390625F;
/*  991 */     GlStateManager.scale(f, f, f);
/*  992 */     GlStateManager.translate(8.0F, 8.0F, 8.0F);
/*  993 */     GlStateManager.matrixMode(5888);
/*  994 */     this.mc.getTextureManager().bindTexture(this.locationLightMap);
/*  995 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  996 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  997 */     GL11.glTexParameteri(3553, 10242, 33071);
/*  998 */     GL11.glTexParameteri(3553, 10243, 33071);
/*  999 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1000 */     GlStateManager.enableTexture2D();
/* 1001 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*      */     
/* 1003 */     if (Config.isShaders())
/*      */     {
/* 1005 */       Shaders.enableLightmap();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateTorchFlicker() {
/* 1011 */     this.torchFlickerDX = (float)(this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
/* 1012 */     this.torchFlickerDX = (float)(this.torchFlickerDX * 0.9D);
/* 1013 */     this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
/* 1014 */     this.lightmapUpdateNeeded = true;
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateLightmap(float partialTicks) {
/* 1019 */     if (this.lightmapUpdateNeeded) {
/*      */       
/* 1021 */       this.mc.mcProfiler.startSection("lightTex");
/* 1022 */       WorldClient worldClient = this.mc.theWorld;
/*      */       
/* 1024 */       if (worldClient != null) {
/*      */         
/* 1026 */         if (Config.isCustomColors() && CustomColors.updateLightmap((World)worldClient, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision), partialTicks)) {
/*      */           
/* 1028 */           this.lightmapTexture.updateDynamicTexture();
/* 1029 */           this.lightmapUpdateNeeded = false;
/* 1030 */           this.mc.mcProfiler.endSection();
/*      */           
/*      */           return;
/*      */         } 
/* 1034 */         float f = worldClient.getSunBrightness(1.0F);
/* 1035 */         float f1 = f * 0.95F + 0.05F;
/*      */         
/* 1037 */         for (int i = 0; i < 256; i++) {
/*      */           
/* 1039 */           float f2 = ((World)worldClient).provider.getLightBrightnessTable()[i / 16] * f1;
/* 1040 */           float f3 = ((World)worldClient).provider.getLightBrightnessTable()[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);
/*      */           
/* 1042 */           if (worldClient.getLastLightningBolt() > 0)
/*      */           {
/* 1044 */             f2 = ((World)worldClient).provider.getLightBrightnessTable()[i / 16];
/*      */           }
/*      */           
/* 1047 */           float f4 = f2 * (f * 0.65F + 0.35F);
/* 1048 */           float f5 = f2 * (f * 0.65F + 0.35F);
/* 1049 */           float f6 = f3 * ((f3 * 0.6F + 0.4F) * 0.6F + 0.4F);
/* 1050 */           float f7 = f3 * (f3 * f3 * 0.6F + 0.4F);
/* 1051 */           float f8 = f4 + f3;
/* 1052 */           float f9 = f5 + f6;
/* 1053 */           float f10 = f2 + f7;
/* 1054 */           f8 = f8 * 0.96F + 0.03F;
/* 1055 */           f9 = f9 * 0.96F + 0.03F;
/* 1056 */           f10 = f10 * 0.96F + 0.03F;
/*      */           
/* 1058 */           if (this.bossColorModifier > 0.0F) {
/*      */             
/* 1060 */             float f11 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
/* 1061 */             f8 = f8 * (1.0F - f11) + f8 * 0.7F * f11;
/* 1062 */             f9 = f9 * (1.0F - f11) + f9 * 0.6F * f11;
/* 1063 */             f10 = f10 * (1.0F - f11) + f10 * 0.6F * f11;
/*      */           } 
/*      */           
/* 1066 */           if (((World)worldClient).provider.getDimensionId() == 1) {
/*      */             
/* 1068 */             f8 = 0.22F + f3 * 0.75F;
/* 1069 */             f9 = 0.28F + f6 * 0.75F;
/* 1070 */             f10 = 0.25F + f7 * 0.75F;
/*      */           } 
/*      */           
/* 1073 */           if (this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
/*      */             
/* 1075 */             float f15 = getNightVisionBrightness((EntityLivingBase)this.mc.thePlayer, partialTicks);
/* 1076 */             float f12 = 1.0F / f8;
/*      */             
/* 1078 */             if (f12 > 1.0F / f9)
/*      */             {
/* 1080 */               f12 = 1.0F / f9;
/*      */             }
/*      */             
/* 1083 */             if (f12 > 1.0F / f10)
/*      */             {
/* 1085 */               f12 = 1.0F / f10;
/*      */             }
/*      */             
/* 1088 */             f8 = f8 * (1.0F - f15) + f8 * f12 * f15;
/* 1089 */             f9 = f9 * (1.0F - f15) + f9 * f12 * f15;
/* 1090 */             f10 = f10 * (1.0F - f15) + f10 * f12 * f15;
/*      */           } 
/*      */           
/* 1093 */           if (f8 > 1.0F)
/*      */           {
/* 1095 */             f8 = 1.0F;
/*      */           }
/*      */           
/* 1098 */           if (f9 > 1.0F)
/*      */           {
/* 1100 */             f9 = 1.0F;
/*      */           }
/*      */           
/* 1103 */           if (f10 > 1.0F)
/*      */           {
/* 1105 */             f10 = 1.0F;
/*      */           }
/*      */           
/* 1108 */           float f16 = this.mc.gameSettings.gammaSetting;
/* 1109 */           float f17 = 1.0F - f8;
/* 1110 */           float f13 = 1.0F - f9;
/* 1111 */           float f14 = 1.0F - f10;
/* 1112 */           f17 = 1.0F - f17 * f17 * f17 * f17;
/* 1113 */           f13 = 1.0F - f13 * f13 * f13 * f13;
/* 1114 */           f14 = 1.0F - f14 * f14 * f14 * f14;
/* 1115 */           f8 = f8 * (1.0F - f16) + f17 * f16;
/* 1116 */           f9 = f9 * (1.0F - f16) + f13 * f16;
/* 1117 */           f10 = f10 * (1.0F - f16) + f14 * f16;
/* 1118 */           f8 = f8 * 0.96F + 0.03F;
/* 1119 */           f9 = f9 * 0.96F + 0.03F;
/* 1120 */           f10 = f10 * 0.96F + 0.03F;
/*      */           
/* 1122 */           if (f8 > 1.0F)
/*      */           {
/* 1124 */             f8 = 1.0F;
/*      */           }
/*      */           
/* 1127 */           if (f9 > 1.0F)
/*      */           {
/* 1129 */             f9 = 1.0F;
/*      */           }
/*      */           
/* 1132 */           if (f10 > 1.0F)
/*      */           {
/* 1134 */             f10 = 1.0F;
/*      */           }
/*      */           
/* 1137 */           if (f8 < 0.0F)
/*      */           {
/* 1139 */             f8 = 0.0F;
/*      */           }
/*      */           
/* 1142 */           if (f9 < 0.0F)
/*      */           {
/* 1144 */             f9 = 0.0F;
/*      */           }
/*      */           
/* 1147 */           if (f10 < 0.0F)
/*      */           {
/* 1149 */             f10 = 0.0F;
/*      */           }
/*      */           
/* 1152 */           int j = 255;
/* 1153 */           int k = (int)(f8 * 255.0F);
/* 1154 */           int l = (int)(f9 * 255.0F);
/* 1155 */           int i1 = (int)(f10 * 255.0F);
/* 1156 */           this.lightmapColors[i] = j << 24 | k << 16 | l << 8 | i1;
/*      */         } 
/*      */         
/* 1159 */         this.lightmapTexture.updateDynamicTexture();
/* 1160 */         this.lightmapUpdateNeeded = false;
/* 1161 */         this.mc.mcProfiler.endSection();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public float getNightVisionBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks) {
/* 1168 */     int i = entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration();
/* 1169 */     return (i > 200) ? 1.0F : (0.7F + MathHelper.sin((i - partialTicks) * 3.1415927F * 0.2F) * 0.3F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCameraAndRender(float partialTicks, long nanoTime) {
/* 1174 */     Config.renderPartialTicks = partialTicks;
/* 1175 */     frameInit();
/* 1176 */     boolean flag = Display.isActive();
/*      */     
/* 1178 */     if (!flag && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
/*      */       
/* 1180 */       if (Minecraft.getSystemTime() - this.prevFrameTime > 500L)
/*      */       {
/* 1182 */         this.mc.displayInGameMenu();
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1187 */       this.prevFrameTime = Minecraft.getSystemTime();
/*      */     } 
/*      */     
/* 1190 */     this.mc.mcProfiler.startSection("mouse");
/*      */     
/* 1192 */     if (flag && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
/*      */       
/* 1194 */       Mouse.setGrabbed(false);
/* 1195 */       Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
/* 1196 */       Mouse.setGrabbed(true);
/*      */     } 
/*      */     
/* 1199 */     if (this.mc.inGameHasFocus && flag) {
/*      */       
/* 1201 */       this.mc.mouseHelper.mouseXYChange();
/* 1202 */       float f = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
/* 1203 */       float f1 = f * f * f * 8.0F;
/* 1204 */       float f2 = this.mc.mouseHelper.deltaX * f1;
/* 1205 */       float f3 = this.mc.mouseHelper.deltaY * f1;
/* 1206 */       int i = 1;
/*      */       
/* 1208 */       if (this.mc.gameSettings.invertMouse)
/*      */       {
/* 1210 */         i = -1;
/*      */       }
/*      */       
/* 1213 */       if (this.mc.gameSettings.smoothCamera) {
/*      */         
/* 1215 */         this.smoothCamYaw += f2;
/* 1216 */         this.smoothCamPitch += f3;
/* 1217 */         float f4 = partialTicks - this.smoothCamPartialTicks;
/* 1218 */         this.smoothCamPartialTicks = partialTicks;
/* 1219 */         f2 = this.smoothCamFilterX * f4;
/* 1220 */         f3 = this.smoothCamFilterY * f4;
/* 1221 */         this.mc.thePlayer.setAngles(f2, f3 * i);
/*      */       }
/*      */       else {
/*      */         
/* 1225 */         this.smoothCamYaw = 0.0F;
/* 1226 */         this.smoothCamPitch = 0.0F;
/* 1227 */         this.mc.thePlayer.setAngles(f2, f3 * i);
/*      */       } 
/*      */     } 
/*      */     
/* 1231 */     this.mc.mcProfiler.endSection();
/*      */     
/* 1233 */     if (!this.mc.skipRenderWorld) {
/*      */       
/* 1235 */       anaglyphEnable = this.mc.gameSettings.anaglyph;
/* 1236 */       final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/* 1237 */       int i1 = scaledresolution.getScaledWidth();
/* 1238 */       int j1 = scaledresolution.getScaledHeight();
/* 1239 */       final int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
/* 1240 */       final int l1 = j1 - Mouse.getY() * j1 / this.mc.displayHeight - 1;
/* 1241 */       int i2 = this.mc.gameSettings.limitFramerate;
/*      */       
/* 1243 */       if (this.mc.theWorld != null) {
/*      */         
/* 1245 */         this.mc.mcProfiler.startSection("level");
/* 1246 */         int j = Math.min(Minecraft.getDebugFPS(), i2);
/* 1247 */         j = Math.max(j, 60);
/* 1248 */         long k = System.nanoTime() - nanoTime;
/* 1249 */         long l = Math.max((1000000000 / j / 4) - k, 0L);
/* 1250 */         renderWorld(partialTicks, System.nanoTime() + l);
/*      */         
/* 1252 */         if (OpenGlHelper.shadersSupported) {
/*      */           
/* 1254 */           this.mc.renderGlobal.renderEntityOutlineFramebuffer();
/*      */           
/* 1256 */           if (this.theShaderGroup != null && this.useShader) {
/*      */             
/* 1258 */             GlStateManager.matrixMode(5890);
/* 1259 */             GlStateManager.pushMatrix();
/* 1260 */             GlStateManager.loadIdentity();
/* 1261 */             this.theShaderGroup.loadShaderGroup(partialTicks);
/* 1262 */             GlStateManager.popMatrix();
/*      */           } 
/*      */           
/* 1265 */           this.mc.getFramebuffer().bindFramebuffer(true);
/*      */         } 
/*      */         
/* 1268 */         this.renderEndNanoTime = System.nanoTime();
/* 1269 */         this.mc.mcProfiler.endStartSection("gui");
/*      */         
/* 1271 */         if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
/*      */           
/* 1273 */           GlStateManager.alphaFunc(516, 0.1F);
/* 1274 */           this.mc.ingameGUI.renderGameOverlay(partialTicks);
/*      */           
/* 1276 */           if (this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo)
/*      */           {
/* 1278 */             Config.drawFps();
/*      */           }
/*      */           
/* 1281 */           if (this.mc.gameSettings.showDebugInfo)
/*      */           {
/* 1283 */             Lagometer.showLagometer(scaledresolution);
/*      */           }
/*      */         } 
/*      */         
/* 1287 */         this.mc.mcProfiler.endSection();
/*      */       }
/*      */       else {
/*      */         
/* 1291 */         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/* 1292 */         GlStateManager.matrixMode(5889);
/* 1293 */         GlStateManager.loadIdentity();
/* 1294 */         GlStateManager.matrixMode(5888);
/* 1295 */         GlStateManager.loadIdentity();
/* 1296 */         setupOverlayRendering();
/* 1297 */         this.renderEndNanoTime = System.nanoTime();
/* 1298 */         TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
/* 1299 */         TileEntityRendererDispatcher.instance.fontRenderer = this.mc.fontRendererObj;
/*      */       } 
/*      */       
/* 1302 */       if (this.mc.currentScreen != null) {
/*      */         
/* 1304 */         GlStateManager.clear(256);
/*      */ 
/*      */         
/*      */         try {
/* 1308 */           if (Reflector.ForgeHooksClient_drawScreen.exists())
/*      */           {
/* 1310 */             Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[] { this.mc.currentScreen, Integer.valueOf(k1), Integer.valueOf(l1), Float.valueOf(partialTicks) });
/*      */           }
/*      */           else
/*      */           {
/* 1314 */             this.mc.currentScreen.drawScreen(k1, l1, partialTicks);
/*      */           }
/*      */         
/* 1317 */         } catch (Throwable throwable) {
/*      */           
/* 1319 */           CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
/* 1320 */           CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
/* 1321 */           crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>()
/*      */               {
/*      */                 public String call() throws Exception
/*      */                 {
/* 1325 */                   return EntityRenderer.this.mc.currentScreen.getClass().getCanonicalName();
/*      */                 }
/*      */               });
/* 1328 */           crashreportcategory.addCrashSectionCallable("Mouse location", new Callable<String>()
/*      */               {
/*      */                 public String call() throws Exception
/*      */                 {
/* 1332 */                   return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] { Integer.valueOf(this.val$k1), Integer.valueOf(this.val$l1), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY()) });
/*      */                 }
/*      */               });
/* 1335 */           crashreportcategory.addCrashSectionCallable("Screen size", new Callable<String>()
/*      */               {
/*      */                 public String call() throws Exception
/*      */                 {
/* 1339 */                   return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] { Integer.valueOf(this.val$scaledresolution.getScaledWidth()), Integer.valueOf(this.val$scaledresolution.getScaledHeight()), Integer.valueOf((EntityRenderer.access$000(this.this$0)).displayWidth), Integer.valueOf((EntityRenderer.access$000(this.this$0)).displayHeight), Integer.valueOf(this.val$scaledresolution.getScaleFactor()) });
/*      */                 }
/*      */               });
/* 1342 */           throw new ReportedException(crashreport);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1347 */     frameFinish();
/* 1348 */     waitForServerThread();
/* 1349 */     MemoryMonitor.update();
/* 1350 */     Lagometer.updateLagometer();
/*      */     
/* 1352 */     if (this.mc.gameSettings.ofProfiler)
/*      */     {
/* 1354 */       this.mc.gameSettings.showDebugProfilerChart = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isDrawBlockOutline() {
/* 1360 */     if (!this.drawBlockOutline)
/*      */     {
/* 1362 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1366 */     Entity entity = this.mc.getRenderViewEntity();
/* 1367 */     boolean flag = (entity instanceof EntityPlayer && !this.mc.gameSettings.hideGUI);
/*      */     
/* 1369 */     if (flag && !((EntityPlayer)entity).capabilities.allowEdit) {
/*      */       
/* 1371 */       ItemStack itemstack = ((EntityPlayer)entity).getCurrentEquippedItem();
/*      */       
/* 1373 */       if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*      */         
/* 1375 */         BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/* 1376 */         IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
/* 1377 */         Block block = iblockstate.getBlock();
/*      */         
/* 1379 */         if (this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
/*      */           
/* 1381 */           flag = (ReflectorForge.blockHasTileEntity(iblockstate) && this.mc.theWorld.getTileEntity(blockpos) instanceof net.minecraft.inventory.IInventory);
/*      */         }
/*      */         else {
/*      */           
/* 1385 */           flag = (itemstack != null && (itemstack.canDestroy(block) || itemstack.canPlaceOn(block)));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1390 */     return flag;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderWorldDirections(float partialTicks) {
/* 1396 */     if (this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
/*      */       
/* 1398 */       Entity entity = this.mc.getRenderViewEntity();
/* 1399 */       GlStateManager.enableBlend();
/* 1400 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1401 */       GL11.glLineWidth(1.0F);
/* 1402 */       GlStateManager.disableTexture2D();
/* 1403 */       GlStateManager.depthMask(false);
/* 1404 */       GlStateManager.pushMatrix();
/* 1405 */       GlStateManager.matrixMode(5888);
/* 1406 */       GlStateManager.loadIdentity();
/* 1407 */       orientCamera(partialTicks);
/* 1408 */       GlStateManager.translate(0.0F, entity.getEyeHeight(), 0.0F);
/* 1409 */       RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
/* 1410 */       RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
/* 1411 */       RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
/* 1412 */       GlStateManager.popMatrix();
/* 1413 */       GlStateManager.depthMask(true);
/* 1414 */       GlStateManager.enableTexture2D();
/* 1415 */       GlStateManager.disableBlend();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderWorld(float partialTicks, long finishTimeNano) {
/* 1421 */     updateLightmap(partialTicks);
/*      */     
/* 1423 */     if (this.mc.getRenderViewEntity() == null)
/*      */     {
/* 1425 */       this.mc.setRenderViewEntity((Entity)this.mc.thePlayer);
/*      */     }
/*      */     
/* 1428 */     getMouseOver(partialTicks);
/*      */     
/* 1430 */     if (Config.isShaders())
/*      */     {
/* 1432 */       Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
/*      */     }
/*      */     
/* 1435 */     GlStateManager.enableDepth();
/* 1436 */     GlStateManager.enableAlpha();
/* 1437 */     GlStateManager.alphaFunc(516, 0.1F);
/* 1438 */     this.mc.mcProfiler.startSection("center");
/*      */     
/* 1440 */     if (this.mc.gameSettings.anaglyph) {
/*      */       
/* 1442 */       anaglyphField = 0;
/* 1443 */       GlStateManager.colorMask(false, true, true, false);
/* 1444 */       renderWorldPass(0, partialTicks, finishTimeNano);
/* 1445 */       anaglyphField = 1;
/* 1446 */       GlStateManager.colorMask(true, false, false, false);
/* 1447 */       renderWorldPass(1, partialTicks, finishTimeNano);
/* 1448 */       GlStateManager.colorMask(true, true, true, false);
/*      */     }
/*      */     else {
/*      */       
/* 1452 */       renderWorldPass(2, partialTicks, finishTimeNano);
/*      */     } 
/*      */     
/* 1455 */     this.mc.mcProfiler.endSection();
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
/* 1460 */     boolean flag = Config.isShaders();
/*      */     
/* 1462 */     if (flag)
/*      */     {
/* 1464 */       Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
/*      */     }
/*      */     
/* 1467 */     RenderGlobal renderglobal = this.mc.renderGlobal;
/* 1468 */     EffectRenderer effectrenderer = this.mc.effectRenderer;
/* 1469 */     boolean flag1 = isDrawBlockOutline();
/* 1470 */     GlStateManager.enableCull();
/* 1471 */     this.mc.mcProfiler.endStartSection("clear");
/*      */     
/* 1473 */     if (flag) {
/*      */       
/* 1475 */       Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/*      */     }
/*      */     else {
/*      */       
/* 1479 */       GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
/*      */     } 
/*      */     
/* 1482 */     updateFogColor(partialTicks);
/* 1483 */     GlStateManager.clear(16640);
/*      */     
/* 1485 */     if (flag)
/*      */     {
/* 1487 */       Shaders.clearRenderBuffer();
/*      */     }
/*      */     
/* 1490 */     this.mc.mcProfiler.endStartSection("camera");
/* 1491 */     setupCameraTransform(partialTicks, pass);
/*      */     
/* 1493 */     if (flag)
/*      */     {
/* 1495 */       Shaders.setCamera(partialTicks);
/*      */     }
/*      */     
/* 1498 */     ActiveRenderInfo.updateRenderInfo((EntityPlayer)this.mc.thePlayer, (this.mc.gameSettings.thirdPersonView == 2));
/* 1499 */     this.mc.mcProfiler.endStartSection("frustum");
/* 1500 */     ClippingHelper clippinghelper = ClippingHelperImpl.getInstance();
/* 1501 */     this.mc.mcProfiler.endStartSection("culling");
/* 1502 */     clippinghelper.disabled = (Config.isShaders() && !Shaders.isFrustumCulling());
/* 1503 */     Frustum frustum = new Frustum(clippinghelper);
/* 1504 */     Entity entity = this.mc.getRenderViewEntity();
/* 1505 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 1506 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 1507 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/*      */     
/* 1509 */     if (flag) {
/*      */       
/* 1511 */       ShadersRender.setFrustrumPosition((ICamera)frustum, d0, d1, d2);
/*      */     }
/*      */     else {
/*      */       
/* 1515 */       frustum.setPosition(d0, d1, d2);
/*      */     } 
/*      */     
/* 1518 */     if ((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
/*      */       
/* 1520 */       setupFog(-1, partialTicks);
/* 1521 */       this.mc.mcProfiler.endStartSection("sky");
/* 1522 */       GlStateManager.matrixMode(5889);
/* 1523 */       GlStateManager.loadIdentity();
/* 1524 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/* 1525 */       GlStateManager.matrixMode(5888);
/*      */       
/* 1527 */       if (flag)
/*      */       {
/* 1529 */         Shaders.beginSky();
/*      */       }
/*      */       
/* 1532 */       renderglobal.renderSky(partialTicks, pass);
/*      */       
/* 1534 */       if (flag)
/*      */       {
/* 1536 */         Shaders.endSky();
/*      */       }
/*      */       
/* 1539 */       GlStateManager.matrixMode(5889);
/* 1540 */       GlStateManager.loadIdentity();
/* 1541 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/* 1542 */       GlStateManager.matrixMode(5888);
/*      */     }
/*      */     else {
/*      */       
/* 1546 */       GlStateManager.disableBlend();
/*      */     } 
/*      */     
/* 1549 */     setupFog(0, partialTicks);
/* 1550 */     GlStateManager.shadeModel(7425);
/*      */     
/* 1552 */     if (entity.posY + entity.getEyeHeight() < 128.0D + (this.mc.gameSettings.ofCloudsHeight * 128.0F))
/*      */     {
/* 1554 */       renderCloudsCheck(renderglobal, partialTicks, pass);
/*      */     }
/*      */     
/* 1557 */     this.mc.mcProfiler.endStartSection("prepareterrain");
/* 1558 */     setupFog(0, partialTicks);
/* 1559 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 1560 */     RenderHelper.disableStandardItemLighting();
/* 1561 */     this.mc.mcProfiler.endStartSection("terrain_setup");
/* 1562 */     checkLoadVisibleChunks(entity, partialTicks, (ICamera)frustum, this.mc.thePlayer.isSpectator());
/*      */     
/* 1564 */     if (flag) {
/*      */       
/* 1566 */       ShadersRender.setupTerrain(renderglobal, entity, partialTicks, (ICamera)frustum, this.frameCount++, this.mc.thePlayer.isSpectator());
/*      */     }
/*      */     else {
/*      */       
/* 1570 */       renderglobal.setupTerrain(entity, partialTicks, (ICamera)frustum, this.frameCount++, this.mc.thePlayer.isSpectator());
/*      */     } 
/*      */     
/* 1573 */     if (pass == 0 || pass == 2) {
/*      */       
/* 1575 */       this.mc.mcProfiler.endStartSection("updatechunks");
/* 1576 */       Lagometer.timerChunkUpload.start();
/* 1577 */       this.mc.renderGlobal.updateChunks(finishTimeNano);
/* 1578 */       Lagometer.timerChunkUpload.end();
/*      */     } 
/*      */     
/* 1581 */     this.mc.mcProfiler.endStartSection("terrain");
/* 1582 */     Lagometer.timerTerrain.start();
/*      */     
/* 1584 */     if (this.mc.gameSettings.ofSmoothFps && pass > 0) {
/*      */       
/* 1586 */       this.mc.mcProfiler.endStartSection("finish");
/* 1587 */       GL11.glFinish();
/* 1588 */       this.mc.mcProfiler.endStartSection("terrain");
/*      */     } 
/*      */     
/* 1591 */     GlStateManager.matrixMode(5888);
/* 1592 */     GlStateManager.pushMatrix();
/* 1593 */     GlStateManager.disableAlpha();
/*      */     
/* 1595 */     if (flag)
/*      */     {
/* 1597 */       ShadersRender.beginTerrainSolid();
/*      */     }
/*      */     
/* 1600 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, partialTicks, pass, entity);
/* 1601 */     GlStateManager.enableAlpha();
/*      */     
/* 1603 */     if (flag)
/*      */     {
/* 1605 */       ShadersRender.beginTerrainCutoutMipped();
/*      */     }
/*      */     
/* 1608 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, (this.mc.gameSettings.mipmapLevels > 0));
/* 1609 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, partialTicks, pass, entity);
/* 1610 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/* 1611 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/*      */     
/* 1613 */     if (flag)
/*      */     {
/* 1615 */       ShadersRender.beginTerrainCutout();
/*      */     }
/*      */     
/* 1618 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, partialTicks, pass, entity);
/* 1619 */     this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/*      */     
/* 1621 */     if (flag)
/*      */     {
/* 1623 */       ShadersRender.endTerrain();
/*      */     }
/*      */     
/* 1626 */     Lagometer.timerTerrain.end();
/* 1627 */     GlStateManager.shadeModel(7424);
/* 1628 */     GlStateManager.alphaFunc(516, 0.1F);
/*      */     
/* 1630 */     if (!this.debugView) {
/*      */       
/* 1632 */       GlStateManager.matrixMode(5888);
/* 1633 */       GlStateManager.popMatrix();
/* 1634 */       GlStateManager.pushMatrix();
/* 1635 */       RenderHelper.enableStandardItemLighting();
/* 1636 */       this.mc.mcProfiler.endStartSection("entities");
/*      */       
/* 1638 */       if (Reflector.ForgeHooksClient_setRenderPass.exists())
/*      */       {
/* 1640 */         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(0) });
/*      */       }
/*      */       
/* 1643 */       renderglobal.renderEntities(entity, (ICamera)frustum, partialTicks);
/*      */       
/* 1645 */       if (Reflector.ForgeHooksClient_setRenderPass.exists())
/*      */       {
/* 1647 */         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(-1) });
/*      */       }
/*      */       
/* 1650 */       RenderHelper.disableStandardItemLighting();
/* 1651 */       disableLightmap();
/* 1652 */       GlStateManager.matrixMode(5888);
/* 1653 */       GlStateManager.popMatrix();
/* 1654 */       GlStateManager.pushMatrix();
/*      */       
/* 1656 */       if (this.mc.objectMouseOver != null && entity.isInsideOfMaterial(Material.water) && flag1) {
/*      */         
/* 1658 */         EntityPlayer entityplayer = (EntityPlayer)entity;
/* 1659 */         GlStateManager.disableAlpha();
/* 1660 */         this.mc.mcProfiler.endStartSection("outline");
/* 1661 */         renderglobal.drawSelectionBox(entityplayer, this.mc.objectMouseOver, 0, partialTicks);
/* 1662 */         GlStateManager.enableAlpha();
/*      */       } 
/*      */     } 
/*      */     
/* 1666 */     GlStateManager.matrixMode(5888);
/* 1667 */     GlStateManager.popMatrix();
/*      */     
/* 1669 */     if (flag1 && this.mc.objectMouseOver != null && !entity.isInsideOfMaterial(Material.water)) {
/*      */       
/* 1671 */       EntityPlayer entityplayer1 = (EntityPlayer)entity;
/* 1672 */       GlStateManager.disableAlpha();
/* 1673 */       this.mc.mcProfiler.endStartSection("outline");
/*      */       
/* 1675 */       if ((!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[] { renderglobal, entityplayer1, this.mc.objectMouseOver, Integer.valueOf(0), entityplayer1.getHeldItem(), Float.valueOf(partialTicks) })) && !this.mc.gameSettings.hideGUI)
/*      */       {
/* 1677 */         renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, partialTicks);
/*      */       }
/* 1679 */       GlStateManager.enableAlpha();
/*      */     } 
/*      */     
/* 1682 */     if (!renderglobal.damagedBlocks.isEmpty()) {
/*      */       
/* 1684 */       this.mc.mcProfiler.endStartSection("destroyProgress");
/* 1685 */       GlStateManager.enableBlend();
/* 1686 */       GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/* 1687 */       this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
/* 1688 */       renderglobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), entity, partialTicks);
/* 1689 */       this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
/* 1690 */       GlStateManager.disableBlend();
/*      */     } 
/*      */     
/* 1693 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1694 */     GlStateManager.disableBlend();
/*      */     
/* 1696 */     if (!this.debugView) {
/*      */       
/* 1698 */       enableLightmap();
/* 1699 */       this.mc.mcProfiler.endStartSection("litParticles");
/*      */       
/* 1701 */       if (flag)
/*      */       {
/* 1703 */         Shaders.beginLitParticles();
/*      */       }
/*      */       
/* 1706 */       effectrenderer.renderLitParticles(entity, partialTicks);
/* 1707 */       RenderHelper.disableStandardItemLighting();
/* 1708 */       setupFog(0, partialTicks);
/* 1709 */       this.mc.mcProfiler.endStartSection("particles");
/*      */       
/* 1711 */       if (flag)
/*      */       {
/* 1713 */         Shaders.beginParticles();
/*      */       }
/*      */       
/* 1716 */       effectrenderer.renderParticles(entity, partialTicks);
/*      */       
/* 1718 */       if (flag)
/*      */       {
/* 1720 */         Shaders.endParticles();
/*      */       }
/*      */       
/* 1723 */       disableLightmap();
/*      */     } 
/*      */     
/* 1726 */     GlStateManager.depthMask(false);
/*      */     
/* 1728 */     if (Config.isShaders())
/*      */     {
/* 1730 */       GlStateManager.depthMask(Shaders.isRainDepth());
/*      */     }
/*      */     
/* 1733 */     GlStateManager.enableCull();
/* 1734 */     this.mc.mcProfiler.endStartSection("weather");
/*      */     
/* 1736 */     if (flag)
/*      */     {
/* 1738 */       Shaders.beginWeather();
/*      */     }
/*      */     
/* 1741 */     renderRainSnow(partialTicks);
/*      */     
/* 1743 */     if (flag)
/*      */     {
/* 1745 */       Shaders.endWeather();
/*      */     }
/*      */     
/* 1748 */     GlStateManager.depthMask(true);
/* 1749 */     renderglobal.renderWorldBorder(entity, partialTicks);
/*      */     
/* 1751 */     if (flag) {
/*      */       
/* 1753 */       ShadersRender.renderHand0(this, partialTicks, pass);
/* 1754 */       Shaders.preWater();
/*      */     } 
/*      */     
/* 1757 */     GlStateManager.disableBlend();
/* 1758 */     GlStateManager.enableCull();
/* 1759 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1760 */     GlStateManager.alphaFunc(516, 0.1F);
/* 1761 */     setupFog(0, partialTicks);
/* 1762 */     GlStateManager.enableBlend();
/* 1763 */     GlStateManager.depthMask(false);
/* 1764 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 1765 */     GlStateManager.shadeModel(7425);
/* 1766 */     this.mc.mcProfiler.endStartSection("translucent");
/*      */     
/* 1768 */     if (flag)
/*      */     {
/* 1770 */       Shaders.beginWater();
/*      */     }
/*      */     
/* 1773 */     renderglobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, partialTicks, pass, entity);
/*      */     
/* 1775 */     if (flag)
/*      */     {
/* 1777 */       Shaders.endWater();
/*      */     }
/*      */     
/* 1780 */     if (Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
/*      */       
/* 1782 */       RenderHelper.enableStandardItemLighting();
/* 1783 */       this.mc.mcProfiler.endStartSection("entities");
/* 1784 */       Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(1) });
/* 1785 */       this.mc.renderGlobal.renderEntities(entity, (ICamera)frustum, partialTicks);
/* 1786 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1787 */       Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[] { Integer.valueOf(-1) });
/* 1788 */       RenderHelper.disableStandardItemLighting();
/*      */     } 
/*      */     
/* 1791 */     GlStateManager.shadeModel(7424);
/* 1792 */     GlStateManager.depthMask(true);
/* 1793 */     GlStateManager.enableCull();
/* 1794 */     GlStateManager.disableBlend();
/* 1795 */     GlStateManager.disableFog();
/*      */     
/* 1797 */     if (entity.posY + entity.getEyeHeight() >= 128.0D + (this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
/*      */       
/* 1799 */       this.mc.mcProfiler.endStartSection("aboveClouds");
/* 1800 */       renderCloudsCheck(renderglobal, partialTicks, pass);
/*      */     } 
/*      */     
/* 1803 */     if (Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
/*      */       
/* 1805 */       this.mc.mcProfiler.endStartSection("forge_render_last");
/* 1806 */       Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[] { renderglobal, Float.valueOf(partialTicks) });
/*      */     } 
/*      */     
/* 1809 */     this.mc.mcProfiler.endStartSection("hand");
/*      */     
/* 1811 */     if (this.renderHand && !Shaders.isShadowPass) {
/*      */       
/* 1813 */       if (flag) {
/*      */         
/* 1815 */         ShadersRender.renderHand1(this, partialTicks, pass);
/* 1816 */         Shaders.renderCompositeFinal();
/*      */       } 
/*      */       
/* 1819 */       GlStateManager.clear(256);
/*      */       
/* 1821 */       if (flag) {
/*      */         
/* 1823 */         ShadersRender.renderFPOverlay(this, partialTicks, pass);
/*      */       }
/*      */       else {
/*      */         
/* 1827 */         renderHand(partialTicks, pass);
/*      */       } 
/*      */       
/* 1830 */       renderWorldDirections(partialTicks);
/*      */     } 
/*      */     
/* 1833 */     if (flag)
/*      */     {
/* 1835 */       Shaders.endRender();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderCloudsCheck(RenderGlobal renderGlobalIn, float partialTicks, int pass) {
/* 1841 */     if (this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
/*      */       
/* 1843 */       this.mc.mcProfiler.endStartSection("clouds");
/* 1844 */       GlStateManager.matrixMode(5889);
/* 1845 */       GlStateManager.loadIdentity();
/* 1846 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
/* 1847 */       GlStateManager.matrixMode(5888);
/* 1848 */       GlStateManager.pushMatrix();
/* 1849 */       setupFog(0, partialTicks);
/* 1850 */       renderGlobalIn.renderClouds(partialTicks, pass);
/* 1851 */       GlStateManager.disableFog();
/* 1852 */       GlStateManager.popMatrix();
/* 1853 */       GlStateManager.matrixMode(5889);
/* 1854 */       GlStateManager.loadIdentity();
/* 1855 */       Project.gluPerspective(getFOVModifier(partialTicks, true), this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.clipDistance);
/* 1856 */       GlStateManager.matrixMode(5888);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addRainParticles() {
/* 1862 */     float f = this.mc.theWorld.getRainStrength(1.0F);
/*      */     
/* 1864 */     if (!Config.isRainFancy())
/*      */     {
/* 1866 */       f /= 2.0F;
/*      */     }
/*      */     
/* 1869 */     if (f != 0.0F && Config.isRainSplash()) {
/*      */       
/* 1871 */       this.random.setSeed(this.rendererUpdateCount * 312987231L);
/* 1872 */       Entity entity = this.mc.getRenderViewEntity();
/* 1873 */       WorldClient worldClient = this.mc.theWorld;
/* 1874 */       BlockPos blockpos = new BlockPos(entity);
/* 1875 */       int i = 10;
/* 1876 */       double d0 = 0.0D;
/* 1877 */       double d1 = 0.0D;
/* 1878 */       double d2 = 0.0D;
/* 1879 */       int j = 0;
/* 1880 */       int k = (int)(100.0F * f * f);
/*      */       
/* 1882 */       if (this.mc.gameSettings.particleSetting == 1) {
/*      */         
/* 1884 */         k >>= 1;
/*      */       }
/* 1886 */       else if (this.mc.gameSettings.particleSetting == 2) {
/*      */         
/* 1888 */         k = 0;
/*      */       } 
/*      */       
/* 1891 */       for (int l = 0; l < k; l++) {
/*      */         
/* 1893 */         BlockPos blockpos1 = worldClient.getPrecipitationHeight(blockpos.add(this.random.nextInt(i) - this.random.nextInt(i), 0, this.random.nextInt(i) - this.random.nextInt(i)));
/* 1894 */         BiomeGenBase biomegenbase = worldClient.getBiomeGenForCoords(blockpos1);
/* 1895 */         BlockPos blockpos2 = blockpos1.down();
/* 1896 */         Block block = worldClient.getBlockState(blockpos2).getBlock();
/*      */         
/* 1898 */         if (blockpos1.getY() <= blockpos.getY() + i && blockpos1.getY() >= blockpos.getY() - i && biomegenbase.canRain() && biomegenbase.getFloatTemperature(blockpos1) >= 0.15F) {
/*      */           
/* 1900 */           double d3 = this.random.nextDouble();
/* 1901 */           double d4 = this.random.nextDouble();
/*      */           
/* 1903 */           if (block.getMaterial() == Material.lava) {
/*      */             
/* 1905 */             this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, blockpos1.getX() + d3, (blockpos1.getY() + 0.1F) - block.getBlockBoundsMinY(), blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*      */           }
/* 1907 */           else if (block.getMaterial() != Material.air) {
/*      */             
/* 1909 */             block.setBlockBoundsBasedOnState((IBlockAccess)worldClient, blockpos2);
/* 1910 */             j++;
/*      */             
/* 1912 */             if (this.random.nextInt(j) == 0) {
/*      */               
/* 1914 */               d0 = blockpos2.getX() + d3;
/* 1915 */               d1 = (blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY() - 1.0D;
/* 1916 */               d2 = blockpos2.getZ() + d4;
/*      */             } 
/*      */             
/* 1919 */             this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, blockpos2.getX() + d3, (blockpos2.getY() + 0.1F) + block.getBlockBoundsMaxY(), blockpos2.getZ() + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1924 */       if (j > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
/*      */         
/* 1926 */         this.rainSoundCounter = 0;
/*      */         
/* 1928 */         if (d1 > (blockpos.getY() + 1) && worldClient.getPrecipitationHeight(blockpos).getY() > MathHelper.floor_float(blockpos.getY())) {
/*      */           
/* 1930 */           this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
/*      */         }
/*      */         else {
/*      */           
/* 1934 */           this.mc.theWorld.playSound(d0, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void renderRainSnow(float partialTicks) {
/* 1942 */     if (Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
/*      */       
/* 1944 */       WorldProvider worldprovider = this.mc.theWorld.provider;
/* 1945 */       Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
/*      */       
/* 1947 */       if (object != null) {
/*      */         
/* 1949 */         Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.mc.theWorld, this.mc });
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1954 */     float f5 = this.mc.theWorld.getRainStrength(partialTicks);
/*      */     
/* 1956 */     if (f5 > 0.0F) {
/*      */       
/* 1958 */       if (Config.isRainOff()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1963 */       enableLightmap();
/* 1964 */       Entity entity = this.mc.getRenderViewEntity();
/* 1965 */       WorldClient worldClient = this.mc.theWorld;
/* 1966 */       int i = MathHelper.floor_double(entity.posX);
/* 1967 */       int j = MathHelper.floor_double(entity.posY);
/* 1968 */       int k = MathHelper.floor_double(entity.posZ);
/* 1969 */       Tessellator tessellator = Tessellator.getInstance();
/* 1970 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1971 */       GlStateManager.disableCull();
/* 1972 */       GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 1973 */       GlStateManager.enableBlend();
/* 1974 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1975 */       GlStateManager.alphaFunc(516, 0.1F);
/* 1976 */       double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 1977 */       double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 1978 */       double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 1979 */       int l = MathHelper.floor_double(d1);
/* 1980 */       int i1 = 5;
/*      */       
/* 1982 */       if (Config.isRainFancy())
/*      */       {
/* 1984 */         i1 = 10;
/*      */       }
/*      */       
/* 1987 */       int j1 = -1;
/* 1988 */       float f = this.rendererUpdateCount + partialTicks;
/* 1989 */       worldrenderer.setTranslation(-d0, -d1, -d2);
/* 1990 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1991 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*      */       
/* 1993 */       for (int k1 = k - i1; k1 <= k + i1; k1++) {
/*      */         
/* 1995 */         for (int l1 = i - i1; l1 <= i + i1; l1++) {
/*      */           
/* 1997 */           int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
/* 1998 */           double d3 = this.rainXCoords[i2] * 0.5D;
/* 1999 */           double d4 = this.rainYCoords[i2] * 0.5D;
/* 2000 */           blockpos$mutableblockpos.set(l1, 0, k1);
/* 2001 */           BiomeGenBase biomegenbase = worldClient.getBiomeGenForCoords((BlockPos)blockpos$mutableblockpos);
/*      */           
/* 2003 */           if (biomegenbase.canRain() || biomegenbase.getEnableSnow()) {
/*      */             
/* 2005 */             int j2 = worldClient.getPrecipitationHeight((BlockPos)blockpos$mutableblockpos).getY();
/* 2006 */             int k2 = j - i1;
/* 2007 */             int l2 = j + i1;
/*      */             
/* 2009 */             if (k2 < j2)
/*      */             {
/* 2011 */               k2 = j2;
/*      */             }
/*      */             
/* 2014 */             if (l2 < j2)
/*      */             {
/* 2016 */               l2 = j2;
/*      */             }
/*      */             
/* 2019 */             int i3 = j2;
/*      */             
/* 2021 */             if (j2 < l)
/*      */             {
/* 2023 */               i3 = l;
/*      */             }
/*      */             
/* 2026 */             if (k2 != l2) {
/*      */               
/* 2028 */               this.random.setSeed((l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
/* 2029 */               blockpos$mutableblockpos.set(l1, k2, k1);
/* 2030 */               float f1 = biomegenbase.getFloatTemperature((BlockPos)blockpos$mutableblockpos);
/*      */               
/* 2032 */               if (worldClient.getWorldChunkManager().getTemperatureAtHeight(f1, j2) >= 0.15F) {
/*      */                 
/* 2034 */                 if (j1 != 0) {
/*      */                   
/* 2036 */                   if (j1 >= 0)
/*      */                   {
/* 2038 */                     tessellator.draw();
/*      */                   }
/*      */                   
/* 2041 */                   j1 = 0;
/* 2042 */                   this.mc.getTextureManager().bindTexture(locationRainPng);
/* 2043 */                   worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
/*      */                 } 
/*      */                 
/* 2046 */                 double d5 = ((this.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 0x1F) + partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
/* 2047 */                 double d6 = (l1 + 0.5F) - entity.posX;
/* 2048 */                 double d7 = (k1 + 0.5F) - entity.posZ;
/* 2049 */                 float f2 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / i1;
/* 2050 */                 float f3 = ((1.0F - f2 * f2) * 0.5F + 0.5F) * f5;
/* 2051 */                 blockpos$mutableblockpos.set(l1, i3, k1);
/* 2052 */                 int j3 = worldClient.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0);
/* 2053 */                 int k3 = j3 >> 16 & 0xFFFF;
/* 2054 */                 int l3 = j3 & 0xFFFF;
/* 2055 */                 worldrenderer.pos(l1 - d3 + 0.5D, k2, k1 - d4 + 0.5D).tex(0.0D, k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
/* 2056 */                 worldrenderer.pos(l1 + d3 + 0.5D, k2, k1 + d4 + 0.5D).tex(1.0D, k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
/* 2057 */                 worldrenderer.pos(l1 + d3 + 0.5D, l2, k1 + d4 + 0.5D).tex(1.0D, l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
/* 2058 */                 worldrenderer.pos(l1 - d3 + 0.5D, l2, k1 - d4 + 0.5D).tex(0.0D, l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f3).lightmap(k3, l3).endVertex();
/*      */               }
/*      */               else {
/*      */                 
/* 2062 */                 if (j1 != 1) {
/*      */                   
/* 2064 */                   if (j1 >= 0)
/*      */                   {
/* 2066 */                     tessellator.draw();
/*      */                   }
/*      */                   
/* 2069 */                   j1 = 1;
/* 2070 */                   this.mc.getTextureManager().bindTexture(locationSnowPng);
/* 2071 */                   worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
/*      */                 } 
/*      */                 
/* 2074 */                 double d8 = (((this.rendererUpdateCount & 0x1FF) + partialTicks) / 512.0F);
/* 2075 */                 double d9 = this.random.nextDouble() + f * 0.01D * (float)this.random.nextGaussian();
/* 2076 */                 double d10 = this.random.nextDouble() + (f * (float)this.random.nextGaussian()) * 0.001D;
/* 2077 */                 double d11 = (l1 + 0.5F) - entity.posX;
/* 2078 */                 double d12 = (k1 + 0.5F) - entity.posZ;
/* 2079 */                 float f6 = MathHelper.sqrt_double(d11 * d11 + d12 * d12) / i1;
/* 2080 */                 float f4 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f5;
/* 2081 */                 blockpos$mutableblockpos.set(l1, i3, k1);
/* 2082 */                 int i4 = (worldClient.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
/* 2083 */                 int j4 = i4 >> 16 & 0xFFFF;
/* 2084 */                 int k4 = i4 & 0xFFFF;
/* 2085 */                 worldrenderer.pos(l1 - d3 + 0.5D, k2, k1 - d4 + 0.5D).tex(0.0D + d9, k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
/* 2086 */                 worldrenderer.pos(l1 + d3 + 0.5D, k2, k1 + d4 + 0.5D).tex(1.0D + d9, k2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
/* 2087 */                 worldrenderer.pos(l1 + d3 + 0.5D, l2, k1 + d4 + 0.5D).tex(1.0D + d9, l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
/* 2088 */                 worldrenderer.pos(l1 - d3 + 0.5D, l2, k1 - d4 + 0.5D).tex(0.0D + d9, l2 * 0.25D + d8 + d10).color(1.0F, 1.0F, 1.0F, f4).lightmap(j4, k4).endVertex();
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2095 */       if (j1 >= 0)
/*      */       {
/* 2097 */         tessellator.draw();
/*      */       }
/*      */       
/* 2100 */       worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 2101 */       GlStateManager.enableCull();
/* 2102 */       GlStateManager.disableBlend();
/* 2103 */       GlStateManager.alphaFunc(516, 0.1F);
/* 2104 */       disableLightmap();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setupOverlayRendering() {
/* 2110 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/* 2111 */     GlStateManager.clear(256);
/* 2112 */     GlStateManager.matrixMode(5889);
/* 2113 */     GlStateManager.loadIdentity();
/* 2114 */     GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
/* 2115 */     GlStateManager.matrixMode(5888);
/* 2116 */     GlStateManager.loadIdentity();
/* 2117 */     GlStateManager.translate(0.0F, 0.0F, -2000.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateFogColor(float partialTicks) {
/* 2122 */     WorldClient worldClient = this.mc.theWorld;
/* 2123 */     Entity entity = this.mc.getRenderViewEntity();
/* 2124 */     float f = 0.25F + 0.75F * this.mc.gameSettings.renderDistanceChunks / 32.0F;
/* 2125 */     f = 1.0F - (float)Math.pow(f, 0.25D);
/* 2126 */     Vec3 vec3 = worldClient.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
/* 2127 */     vec3 = CustomColors.getWorldSkyColor(vec3, (World)worldClient, this.mc.getRenderViewEntity(), partialTicks);
/* 2128 */     float f1 = (float)vec3.xCoord;
/* 2129 */     float f2 = (float)vec3.yCoord;
/* 2130 */     float f3 = (float)vec3.zCoord;
/* 2131 */     Vec3 vec31 = worldClient.getFogColor(partialTicks);
/* 2132 */     vec31 = CustomColors.getWorldFogColor(vec31, (World)worldClient, this.mc.getRenderViewEntity(), partialTicks);
/* 2133 */     this.fogColorRed = (float)vec31.xCoord;
/* 2134 */     this.fogColorGreen = (float)vec31.yCoord;
/* 2135 */     this.fogColorBlue = (float)vec31.zCoord;
/*      */     
/* 2137 */     if (this.mc.gameSettings.renderDistanceChunks >= 4) {
/*      */       
/* 2139 */       double d0 = -1.0D;
/* 2140 */       Vec3 vec32 = (MathHelper.sin(worldClient.getCelestialAngleRadians(partialTicks)) > 0.0F) ? new Vec3(d0, 0.0D, 0.0D) : new Vec3(1.0D, 0.0D, 0.0D);
/* 2141 */       float f5 = (float)entity.getLook(partialTicks).dotProduct(vec32);
/*      */       
/* 2143 */       if (f5 < 0.0F)
/*      */       {
/* 2145 */         f5 = 0.0F;
/*      */       }
/*      */       
/* 2148 */       if (f5 > 0.0F) {
/*      */         
/* 2150 */         float[] afloat = ((World)worldClient).provider.calcSunriseSunsetColors(worldClient.getCelestialAngle(partialTicks), partialTicks);
/*      */         
/* 2152 */         if (afloat != null) {
/*      */           
/* 2154 */           f5 *= afloat[3];
/* 2155 */           this.fogColorRed = this.fogColorRed * (1.0F - f5) + afloat[0] * f5;
/* 2156 */           this.fogColorGreen = this.fogColorGreen * (1.0F - f5) + afloat[1] * f5;
/* 2157 */           this.fogColorBlue = this.fogColorBlue * (1.0F - f5) + afloat[2] * f5;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2162 */     this.fogColorRed += (f1 - this.fogColorRed) * f;
/* 2163 */     this.fogColorGreen += (f2 - this.fogColorGreen) * f;
/* 2164 */     this.fogColorBlue += (f3 - this.fogColorBlue) * f;
/* 2165 */     float f8 = worldClient.getRainStrength(partialTicks);
/*      */     
/* 2167 */     if (f8 > 0.0F) {
/*      */       
/* 2169 */       float f4 = 1.0F - f8 * 0.5F;
/* 2170 */       float f10 = 1.0F - f8 * 0.4F;
/* 2171 */       this.fogColorRed *= f4;
/* 2172 */       this.fogColorGreen *= f4;
/* 2173 */       this.fogColorBlue *= f10;
/*      */     } 
/*      */     
/* 2176 */     float f9 = worldClient.getThunderStrength(partialTicks);
/*      */     
/* 2178 */     if (f9 > 0.0F) {
/*      */       
/* 2180 */       float f11 = 1.0F - f9 * 0.5F;
/* 2181 */       this.fogColorRed *= f11;
/* 2182 */       this.fogColorGreen *= f11;
/* 2183 */       this.fogColorBlue *= f11;
/*      */     } 
/*      */     
/* 2186 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint((World)this.mc.theWorld, entity, partialTicks);
/*      */     
/* 2188 */     if (this.cloudFog) {
/*      */       
/* 2190 */       Vec3 vec33 = worldClient.getCloudColour(partialTicks);
/* 2191 */       this.fogColorRed = (float)vec33.xCoord;
/* 2192 */       this.fogColorGreen = (float)vec33.yCoord;
/* 2193 */       this.fogColorBlue = (float)vec33.zCoord;
/*      */     }
/* 2195 */     else if (block.getMaterial() == Material.water) {
/*      */       
/* 2197 */       float f12 = EnchantmentHelper.getRespiration(entity) * 0.2F;
/* 2198 */       f12 = Config.limit(f12, 0.0F, 0.6F);
/*      */       
/* 2200 */       if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing))
/*      */       {
/* 2202 */         f12 = f12 * 0.3F + 0.6F;
/*      */       }
/*      */       
/* 2205 */       this.fogColorRed = 0.02F + f12;
/* 2206 */       this.fogColorGreen = 0.02F + f12;
/* 2207 */       this.fogColorBlue = 0.2F + f12;
/* 2208 */       Vec3 vec35 = CustomColors.getUnderwaterColor((IBlockAccess)this.mc.theWorld, (this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity()).posY + 1.0D, (this.mc.getRenderViewEntity()).posZ);
/*      */       
/* 2210 */       if (vec35 != null)
/*      */       {
/* 2212 */         this.fogColorRed = (float)vec35.xCoord;
/* 2213 */         this.fogColorGreen = (float)vec35.yCoord;
/* 2214 */         this.fogColorBlue = (float)vec35.zCoord;
/*      */       }
/*      */     
/* 2217 */     } else if (block.getMaterial() == Material.lava) {
/*      */       
/* 2219 */       this.fogColorRed = 0.6F;
/* 2220 */       this.fogColorGreen = 0.1F;
/* 2221 */       this.fogColorBlue = 0.0F;
/* 2222 */       Vec3 vec34 = CustomColors.getUnderlavaColor((IBlockAccess)this.mc.theWorld, (this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity()).posY + 1.0D, (this.mc.getRenderViewEntity()).posZ);
/*      */       
/* 2224 */       if (vec34 != null) {
/*      */         
/* 2226 */         this.fogColorRed = (float)vec34.xCoord;
/* 2227 */         this.fogColorGreen = (float)vec34.yCoord;
/* 2228 */         this.fogColorBlue = (float)vec34.zCoord;
/*      */       } 
/*      */     } 
/*      */     
/* 2232 */     float f13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
/* 2233 */     this.fogColorRed *= f13;
/* 2234 */     this.fogColorGreen *= f13;
/* 2235 */     this.fogColorBlue *= f13;
/* 2236 */     double d1 = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) * ((World)worldClient).provider.getVoidFogYFactor();
/*      */     
/* 2238 */     if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
/*      */       
/* 2240 */       int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
/*      */       
/* 2242 */       if (i < 20) {
/*      */         
/* 2244 */         d1 *= (1.0F - i / 20.0F);
/*      */       }
/*      */       else {
/*      */         
/* 2248 */         d1 = 0.0D;
/*      */       } 
/*      */     } 
/*      */     
/* 2252 */     if (d1 < 1.0D) {
/*      */       
/* 2254 */       if (d1 < 0.0D)
/*      */       {
/* 2256 */         d1 = 0.0D;
/*      */       }
/*      */       
/* 2259 */       d1 *= d1;
/* 2260 */       this.fogColorRed = (float)(this.fogColorRed * d1);
/* 2261 */       this.fogColorGreen = (float)(this.fogColorGreen * d1);
/* 2262 */       this.fogColorBlue = (float)(this.fogColorBlue * d1);
/*      */     } 
/*      */     
/* 2265 */     if (this.bossColorModifier > 0.0F) {
/*      */       
/* 2267 */       float f14 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
/* 2268 */       this.fogColorRed = this.fogColorRed * (1.0F - f14) + this.fogColorRed * 0.7F * f14;
/* 2269 */       this.fogColorGreen = this.fogColorGreen * (1.0F - f14) + this.fogColorGreen * 0.6F * f14;
/* 2270 */       this.fogColorBlue = this.fogColorBlue * (1.0F - f14) + this.fogColorBlue * 0.6F * f14;
/*      */     } 
/*      */     
/* 2273 */     if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.nightVision)) {
/*      */       
/* 2275 */       float f15 = getNightVisionBrightness((EntityLivingBase)entity, partialTicks);
/* 2276 */       float f6 = 1.0F / this.fogColorRed;
/*      */       
/* 2278 */       if (f6 > 1.0F / this.fogColorGreen)
/*      */       {
/* 2280 */         f6 = 1.0F / this.fogColorGreen;
/*      */       }
/*      */       
/* 2283 */       if (f6 > 1.0F / this.fogColorBlue)
/*      */       {
/* 2285 */         f6 = 1.0F / this.fogColorBlue;
/*      */       }
/*      */       
/* 2288 */       if (Float.isInfinite(f6))
/*      */       {
/* 2290 */         f6 = Math.nextAfter(f6, 0.0D);
/*      */       }
/*      */       
/* 2293 */       this.fogColorRed = this.fogColorRed * (1.0F - f15) + this.fogColorRed * f6 * f15;
/* 2294 */       this.fogColorGreen = this.fogColorGreen * (1.0F - f15) + this.fogColorGreen * f6 * f15;
/* 2295 */       this.fogColorBlue = this.fogColorBlue * (1.0F - f15) + this.fogColorBlue * f6 * f15;
/*      */     } 
/*      */     
/* 2298 */     if (this.mc.gameSettings.anaglyph) {
/*      */       
/* 2300 */       float f16 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
/* 2301 */       float f17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
/* 2302 */       float f7 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
/* 2303 */       this.fogColorRed = f16;
/* 2304 */       this.fogColorGreen = f17;
/* 2305 */       this.fogColorBlue = f7;
/*      */     } 
/*      */     
/* 2308 */     if (Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
/*      */       
/* 2310 */       Object object = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[] { this, entity, block, Float.valueOf(partialTicks), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue) });
/* 2311 */       Reflector.postForgeBusEvent(object);
/* 2312 */       this.fogColorRed = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
/* 2313 */       this.fogColorGreen = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
/* 2314 */       this.fogColorBlue = Reflector.getFieldValueFloat(object, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
/*      */     } 
/*      */     
/* 2317 */     Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setupFog(int startCoords, float partialTicks) {
/* 2322 */     this.fogStandard = false;
/* 2323 */     Entity entity = this.mc.getRenderViewEntity();
/* 2324 */     boolean flag = false;
/*      */     
/* 2326 */     if (entity instanceof EntityPlayer)
/*      */     {
/* 2328 */       flag = ((EntityPlayer)entity).capabilities.isCreativeMode;
/*      */     }
/*      */     
/* 2331 */     GL11.glFog(2918, setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
/* 2332 */     GL11.glNormal3f(0.0F, -1.0F, 0.0F);
/* 2333 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 2334 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint((World)this.mc.theWorld, entity, partialTicks);
/* 2335 */     float f = -1.0F;
/*      */     
/* 2337 */     if (Reflector.ForgeHooksClient_getFogDensity.exists())
/*      */     {
/* 2339 */       f = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, new Object[] { this, entity, block, Float.valueOf(partialTicks), Float.valueOf(0.1F) });
/*      */     }
/*      */     
/* 2342 */     if (f >= 0.0F) {
/*      */       
/* 2344 */       GlStateManager.setFogDensity(f);
/*      */     }
/* 2346 */     else if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.blindness)) {
/*      */       
/* 2348 */       float f4 = 5.0F;
/* 2349 */       int i = ((EntityLivingBase)entity).getActivePotionEffect(Potion.blindness).getDuration();
/*      */       
/* 2351 */       if (i < 20)
/*      */       {
/* 2353 */         f4 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - i / 20.0F);
/*      */       }
/*      */       
/* 2356 */       GlStateManager.setFog(9729);
/*      */       
/* 2358 */       if (startCoords == -1) {
/*      */         
/* 2360 */         GlStateManager.setFogStart(0.0F);
/* 2361 */         GlStateManager.setFogEnd(f4 * 0.8F);
/*      */       }
/*      */       else {
/*      */         
/* 2365 */         GlStateManager.setFogStart(f4 * 0.25F);
/* 2366 */         GlStateManager.setFogEnd(f4);
/*      */       } 
/*      */       
/* 2369 */       if ((GLContext.getCapabilities()).GL_NV_fog_distance && Config.isFogFancy())
/*      */       {
/* 2371 */         GL11.glFogi(34138, 34139);
/*      */       }
/*      */     }
/* 2374 */     else if (this.cloudFog) {
/*      */       
/* 2376 */       GlStateManager.setFog(2048);
/* 2377 */       GlStateManager.setFogDensity(0.1F);
/*      */     }
/* 2379 */     else if (block.getMaterial() == Material.water) {
/*      */       
/* 2381 */       GlStateManager.setFog(2048);
/* 2382 */       float f1 = Config.isClearWater() ? 0.02F : 0.1F;
/*      */       
/* 2384 */       if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(Potion.waterBreathing))
/*      */       {
/* 2386 */         GlStateManager.setFogDensity(0.01F);
/*      */       }
/*      */       else
/*      */       {
/* 2390 */         float f2 = 0.1F - EnchantmentHelper.getRespiration(entity) * 0.03F;
/* 2391 */         GlStateManager.setFogDensity(Config.limit(f2, 0.0F, f1));
/*      */       }
/*      */     
/* 2394 */     } else if (block.getMaterial() == Material.lava) {
/*      */       
/* 2396 */       GlStateManager.setFog(2048);
/* 2397 */       GlStateManager.setFogDensity(2.0F);
/*      */     }
/*      */     else {
/*      */       
/* 2401 */       float f3 = this.farPlaneDistance;
/* 2402 */       this.fogStandard = true;
/* 2403 */       GlStateManager.setFog(9729);
/*      */       
/* 2405 */       if (startCoords == -1) {
/*      */         
/* 2407 */         GlStateManager.setFogStart(0.0F);
/* 2408 */         GlStateManager.setFogEnd(f3);
/*      */       }
/*      */       else {
/*      */         
/* 2412 */         GlStateManager.setFogStart(f3 * Config.getFogStart());
/* 2413 */         GlStateManager.setFogEnd(f3);
/*      */       } 
/*      */       
/* 2416 */       if ((GLContext.getCapabilities()).GL_NV_fog_distance) {
/*      */         
/* 2418 */         if (Config.isFogFancy())
/*      */         {
/* 2420 */           GL11.glFogi(34138, 34139);
/*      */         }
/*      */         
/* 2423 */         if (Config.isFogFast())
/*      */         {
/* 2425 */           GL11.glFogi(34138, 34140);
/*      */         }
/*      */       } 
/*      */       
/* 2429 */       if (this.mc.theWorld.provider.doesXZShowFog((int)entity.posX, (int)entity.posZ)) {
/*      */         
/* 2431 */         GlStateManager.setFogStart(f3 * 0.05F);
/* 2432 */         GlStateManager.setFogEnd(f3);
/*      */       } 
/*      */       
/* 2435 */       if (Reflector.ForgeHooksClient_onFogRender.exists())
/*      */       {
/* 2437 */         Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[] { this, entity, block, Float.valueOf(partialTicks), Integer.valueOf(startCoords), Float.valueOf(f3) });
/*      */       }
/*      */     } 
/*      */     
/* 2441 */     GlStateManager.enableColorMaterial();
/* 2442 */     GlStateManager.enableFog();
/* 2443 */     GlStateManager.colorMaterial(1028, 4608);
/*      */   }
/*      */ 
/*      */   
/*      */   private FloatBuffer setFogColorBuffer(float red, float green, float blue, float alpha) {
/* 2448 */     if (Config.isShaders())
/*      */     {
/* 2450 */       Shaders.setFogColor(red, green, blue);
/*      */     }
/*      */     
/* 2453 */     this.fogColorBuffer.clear();
/* 2454 */     this.fogColorBuffer.put(red).put(green).put(blue).put(alpha);
/* 2455 */     this.fogColorBuffer.flip();
/* 2456 */     return this.fogColorBuffer;
/*      */   }
/*      */ 
/*      */   
/*      */   public MapItemRenderer getMapItemRenderer() {
/* 2461 */     return this.theMapItemRenderer;
/*      */   }
/*      */ 
/*      */   
/*      */   private void waitForServerThread() {
/* 2466 */     this.serverWaitTimeCurrent = 0;
/*      */     
/* 2468 */     if (Config.isSmoothWorld() && Config.isSingleProcessor()) {
/*      */       
/* 2470 */       if (this.mc.isIntegratedServerRunning()) {
/*      */         
/* 2472 */         IntegratedServer integratedserver = this.mc.getIntegratedServer();
/*      */         
/* 2474 */         if (integratedserver != null) {
/*      */           
/* 2476 */           boolean flag = this.mc.isGamePaused();
/*      */           
/* 2478 */           if (!flag && !(this.mc.currentScreen instanceof net.minecraft.client.gui.GuiDownloadTerrain)) {
/*      */             
/* 2480 */             if (this.serverWaitTime > 0) {
/*      */               
/* 2482 */               Lagometer.timerServer.start();
/* 2483 */               Config.sleep(this.serverWaitTime);
/* 2484 */               Lagometer.timerServer.end();
/* 2485 */               this.serverWaitTimeCurrent = this.serverWaitTime;
/*      */             } 
/*      */             
/* 2488 */             long i = System.nanoTime() / 1000000L;
/*      */             
/* 2490 */             if (this.lastServerTime != 0L && this.lastServerTicks != 0) {
/*      */               
/* 2492 */               long j = i - this.lastServerTime;
/*      */               
/* 2494 */               if (j < 0L) {
/*      */                 
/* 2496 */                 this.lastServerTime = i;
/* 2497 */                 j = 0L;
/*      */               } 
/*      */               
/* 2500 */               if (j >= 50L)
/*      */               {
/* 2502 */                 this.lastServerTime = i;
/* 2503 */                 int k = integratedserver.getTickCounter();
/* 2504 */                 int l = k - this.lastServerTicks;
/*      */                 
/* 2506 */                 if (l < 0) {
/*      */                   
/* 2508 */                   this.lastServerTicks = k;
/* 2509 */                   l = 0;
/*      */                 } 
/*      */                 
/* 2512 */                 if (l < 1 && this.serverWaitTime < 100)
/*      */                 {
/* 2514 */                   this.serverWaitTime += 2;
/*      */                 }
/*      */                 
/* 2517 */                 if (l > 1 && this.serverWaitTime > 0)
/*      */                 {
/* 2519 */                   this.serverWaitTime--;
/*      */                 }
/*      */                 
/* 2522 */                 this.lastServerTicks = k;
/*      */               }
/*      */             
/*      */             } else {
/*      */               
/* 2527 */               this.lastServerTime = i;
/* 2528 */               this.lastServerTicks = integratedserver.getTickCounter();
/* 2529 */               this.avgServerTickDiff = 1.0F;
/* 2530 */               this.avgServerTimeDiff = 50.0F;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2535 */             if (this.mc.currentScreen instanceof net.minecraft.client.gui.GuiDownloadTerrain)
/*      */             {
/* 2537 */               Config.sleep(20L);
/*      */             }
/*      */             
/* 2540 */             this.lastServerTime = 0L;
/* 2541 */             this.lastServerTicks = 0;
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 2548 */       this.lastServerTime = 0L;
/* 2549 */       this.lastServerTicks = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void frameInit() {
/* 2555 */     GlErrors.frameStart();
/*      */     
/* 2557 */     if (!this.initialized) {
/*      */       
/* 2559 */       ReflectorResolver.resolve();
/* 2560 */       TextureUtils.registerResourceListener();
/*      */       
/* 2562 */       if (Config.getBitsOs() == 64 && Config.getBitsJre() == 32)
/*      */       {
/* 2564 */         Config.setNotify64BitJava(true);
/*      */       }
/*      */       
/* 2567 */       this.initialized = true;
/*      */     } 
/*      */     
/* 2570 */     Config.checkDisplayMode();
/* 2571 */     WorldClient worldClient = this.mc.theWorld;
/*      */     
/* 2573 */     if (worldClient != null) {
/*      */       
/* 2575 */       if (Config.getNewRelease() != null) {
/*      */         
/* 2577 */         String s = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
/* 2578 */         String s1 = s + " " + Config.getNewRelease();
/* 2579 */         ChatComponentText chatcomponenttext = new ChatComponentText(I18n.format("of.message.newVersion", new Object[] { "§n" + s1 + "§r" }));
/* 2580 */         chatcomponenttext.setChatStyle((new ChatStyle()).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://optifine.net/downloads")));
/* 2581 */         this.mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)chatcomponenttext);
/* 2582 */         Config.setNewRelease((String)null);
/*      */       } 
/*      */       
/* 2585 */       if (Config.isNotify64BitJava()) {
/*      */         
/* 2587 */         Config.setNotify64BitJava(false);
/* 2588 */         ChatComponentText chatcomponenttext1 = new ChatComponentText(I18n.format("of.message.java64Bit", new Object[0]));
/* 2589 */         this.mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)chatcomponenttext1);
/*      */       } 
/*      */     } 
/*      */     
/* 2593 */     if (this.mc.currentScreen instanceof GuiMainMenu)
/*      */     {
/* 2595 */       updateMainMenu((GuiMainMenu)this.mc.currentScreen);
/*      */     }
/*      */     
/* 2598 */     if (this.updatedWorld != worldClient) {
/*      */       
/* 2600 */       RandomEntities.worldChanged(this.updatedWorld, (World)worldClient);
/* 2601 */       Config.updateThreadPriorities();
/* 2602 */       this.lastServerTime = 0L;
/* 2603 */       this.lastServerTicks = 0;
/* 2604 */       this.updatedWorld = (World)worldClient;
/*      */     } 
/*      */     
/* 2607 */     if (!setFxaaShader(Shaders.configAntialiasingLevel))
/*      */     {
/* 2609 */       Shaders.configAntialiasingLevel = 0;
/*      */     }
/*      */     
/* 2612 */     if (this.mc.currentScreen != null && this.mc.currentScreen.getClass() == GuiChat.class)
/*      */     {
/* 2614 */       this.mc.displayGuiScreen((GuiScreen)new GuiChatOF((GuiChat)this.mc.currentScreen));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void frameFinish() {
/* 2620 */     if (this.mc.theWorld != null && Config.isShowGlErrors() && TimedEvent.isActive("CheckGlErrorFrameFinish", 10000L)) {
/*      */       
/* 2622 */       int i = GlStateManager.glGetError();
/*      */       
/* 2624 */       if (i != 0 && GlErrors.isEnabled(i)) {
/*      */         
/* 2626 */         String s = Config.getGlErrorString(i);
/* 2627 */         ChatComponentText chatcomponenttext = new ChatComponentText(I18n.format("of.message.openglError", new Object[] { Integer.valueOf(i), s }));
/* 2628 */         this.mc.ingameGUI.getChatGUI().printChatMessage((IChatComponent)chatcomponenttext);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateMainMenu(GuiMainMenu p_updateMainMenu_1_) {
/*      */     try {
/* 2637 */       String s = null;
/* 2638 */       Calendar calendar = Calendar.getInstance();
/* 2639 */       calendar.setTime(new Date());
/* 2640 */       int i = calendar.get(5);
/* 2641 */       int j = calendar.get(2) + 1;
/*      */       
/* 2643 */       if (i == 8 && j == 4)
/*      */       {
/* 2645 */         s = "Happy birthday, OptiFine!";
/*      */       }
/*      */       
/* 2648 */       if (i == 14 && j == 8)
/*      */       {
/* 2650 */         s = "Happy birthday, sp614x!";
/*      */       }
/*      */       
/* 2653 */       if (s == null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 2658 */       Reflector.setFieldValue(p_updateMainMenu_1_, Reflector.GuiMainMenu_splashText, s);
/*      */     }
/* 2660 */     catch (Throwable throwable) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setFxaaShader(int p_setFxaaShader_1_) {
/* 2668 */     if (!OpenGlHelper.isFramebufferEnabled())
/*      */     {
/* 2670 */       return false;
/*      */     }
/* 2672 */     if (this.theShaderGroup != null && this.theShaderGroup != this.fxaaShaders[2] && this.theShaderGroup != this.fxaaShaders[4])
/*      */     {
/* 2674 */       return true;
/*      */     }
/* 2676 */     if (p_setFxaaShader_1_ != 2 && p_setFxaaShader_1_ != 4) {
/*      */       
/* 2678 */       if (this.theShaderGroup == null)
/*      */       {
/* 2680 */         return true;
/*      */       }
/*      */ 
/*      */       
/* 2684 */       this.theShaderGroup.deleteShaderGroup();
/* 2685 */       this.theShaderGroup = null;
/* 2686 */       return true;
/*      */     } 
/*      */     
/* 2689 */     if (this.theShaderGroup != null && this.theShaderGroup == this.fxaaShaders[p_setFxaaShader_1_])
/*      */     {
/* 2691 */       return true;
/*      */     }
/* 2693 */     if (this.mc.theWorld == null)
/*      */     {
/* 2695 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 2699 */     loadShader(new ResourceLocation("shaders/post/fxaa_of_" + p_setFxaaShader_1_ + "x.json"));
/* 2700 */     this.fxaaShaders[p_setFxaaShader_1_] = this.theShaderGroup;
/* 2701 */     return this.useShader;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkLoadVisibleChunks(Entity p_checkLoadVisibleChunks_1_, float p_checkLoadVisibleChunks_2_, ICamera p_checkLoadVisibleChunks_3_, boolean p_checkLoadVisibleChunks_4_) {
/* 2707 */     int i = 201435902;
/*      */     
/* 2709 */     if (this.loadVisibleChunks) {
/*      */       
/* 2711 */       this.loadVisibleChunks = false;
/* 2712 */       loadAllVisibleChunks(p_checkLoadVisibleChunks_1_, p_checkLoadVisibleChunks_2_, p_checkLoadVisibleChunks_3_, p_checkLoadVisibleChunks_4_);
/* 2713 */       this.mc.ingameGUI.getChatGUI().deleteChatLine(i);
/*      */     } 
/*      */     
/* 2716 */     if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(38)) {
/*      */       
/* 2718 */       if (this.mc.currentScreen != null) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 2723 */       this.loadVisibleChunks = true;
/* 2724 */       ChatComponentText chatcomponenttext = new ChatComponentText(I18n.format("of.message.loadingVisibleChunks", new Object[0]));
/* 2725 */       this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((IChatComponent)chatcomponenttext, i);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void loadAllVisibleChunks(Entity p_loadAllVisibleChunks_1_, double p_loadAllVisibleChunks_2_, ICamera p_loadAllVisibleChunks_4_, boolean p_loadAllVisibleChunks_5_) {
/* 2731 */     int i = this.mc.gameSettings.ofChunkUpdates;
/* 2732 */     boolean flag = this.mc.gameSettings.ofLazyChunkLoading;
/*      */ 
/*      */     
/*      */     try {
/* 2736 */       this.mc.gameSettings.ofChunkUpdates = 1000;
/* 2737 */       this.mc.gameSettings.ofLazyChunkLoading = false;
/* 2738 */       RenderGlobal renderglobal = Config.getRenderGlobal();
/* 2739 */       int j = renderglobal.getCountLoadedChunks();
/* 2740 */       long k = System.currentTimeMillis();
/* 2741 */       Config.dbg("Loading visible chunks");
/* 2742 */       long l = System.currentTimeMillis() + 5000L;
/* 2743 */       int i1 = 0;
/* 2744 */       boolean flag1 = false;
/*      */ 
/*      */       
/*      */       do {
/* 2748 */         flag1 = false;
/*      */         
/* 2750 */         for (int j1 = 0; j1 < 100; j1++) {
/*      */           
/* 2752 */           renderglobal.displayListEntitiesDirty = true;
/* 2753 */           renderglobal.setupTerrain(p_loadAllVisibleChunks_1_, p_loadAllVisibleChunks_2_, p_loadAllVisibleChunks_4_, this.frameCount++, p_loadAllVisibleChunks_5_);
/*      */           
/* 2755 */           if (!renderglobal.hasNoChunkUpdates())
/*      */           {
/* 2757 */             flag1 = true;
/*      */           }
/*      */           
/* 2760 */           i1 += renderglobal.getCountChunksToUpdate();
/*      */           
/* 2762 */           while (!renderglobal.hasNoChunkUpdates())
/*      */           {
/* 2764 */             renderglobal.updateChunks(System.nanoTime() + 1000000000L);
/*      */           }
/*      */           
/* 2767 */           i1 -= renderglobal.getCountChunksToUpdate();
/*      */           
/* 2769 */           if (!flag1) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/* 2775 */         if (renderglobal.getCountLoadedChunks() != j) {
/*      */           
/* 2777 */           flag1 = true;
/* 2778 */           j = renderglobal.getCountLoadedChunks();
/*      */         } 
/*      */         
/* 2781 */         if (System.currentTimeMillis() <= l)
/*      */           continue; 
/* 2783 */         Config.log("Chunks loaded: " + i1);
/* 2784 */         l = System.currentTimeMillis() + 5000L;
/*      */       
/*      */       }
/* 2787 */       while (flag1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2793 */       Config.log("Chunks loaded: " + i1);
/* 2794 */       Config.log("Finished loading visible chunks");
/* 2795 */       RenderChunk.renderChunksUpdated = 0;
/*      */     }
/*      */     finally {
/*      */       
/* 2799 */       this.mc.gameSettings.ofChunkUpdates = i;
/* 2800 */       this.mc.gameSettings.ofLazyChunkLoading = flag;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\EntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */