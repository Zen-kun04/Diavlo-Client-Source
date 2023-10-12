/*      */ package net.minecraft.client.renderer;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.gson.JsonSyntaxException;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.audio.ISound;
/*      */ import net.minecraft.client.audio.PositionedSoundRecord;
/*      */ import net.minecraft.client.gui.GuiScreen;
/*      */ import net.minecraft.client.multiplayer.WorldClient;
/*      */ import net.minecraft.client.particle.EntityFX;
/*      */ import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
/*      */ import net.minecraft.client.renderer.chunk.CompiledChunk;
/*      */ import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
/*      */ import net.minecraft.client.renderer.chunk.ListChunkFactory;
/*      */ import net.minecraft.client.renderer.chunk.RenderChunk;
/*      */ import net.minecraft.client.renderer.chunk.VboChunkFactory;
/*      */ import net.minecraft.client.renderer.chunk.VisGraph;
/*      */ import net.minecraft.client.renderer.culling.ClippingHelper;
/*      */ import net.minecraft.client.renderer.culling.ClippingHelperImpl;
/*      */ import net.minecraft.client.renderer.culling.Frustum;
/*      */ import net.minecraft.client.renderer.culling.ICamera;
/*      */ import net.minecraft.client.renderer.entity.RenderManager;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureManager;
/*      */ import net.minecraft.client.renderer.texture.TextureMap;
/*      */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*      */ import net.minecraft.client.renderer.vertex.VertexBuffer;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*      */ import net.minecraft.client.renderer.vertex.VertexFormatElement;
/*      */ import net.minecraft.client.resources.IResourceManager;
/*      */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*      */ import net.minecraft.client.shader.Framebuffer;
/*      */ import net.minecraft.client.shader.ShaderGroup;
/*      */ import net.minecraft.client.shader.ShaderLinkHelper;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemDye;
/*      */ import net.minecraft.item.ItemRecord;
/*      */ import net.minecraft.src.Config;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.EnumWorldBlockLayer;
/*      */ import net.minecraft.util.LongHashMap;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Matrix4f;
/*      */ import net.minecraft.util.MovingObjectPosition;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.ResourceLocation;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.util.Vector3d;
/*      */ import net.minecraft.world.IBlockAccess;
/*      */ import net.minecraft.world.IWorldAccess;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldProvider;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import net.minecraft.world.chunk.IChunkProvider;
/*      */ import net.optifine.CustomColors;
/*      */ import net.optifine.CustomSky;
/*      */ import net.optifine.DynamicLights;
/*      */ import net.optifine.Lagometer;
/*      */ import net.optifine.RandomEntities;
/*      */ import net.optifine.SmartAnimations;
/*      */ import net.optifine.model.BlockModelUtils;
/*      */ import net.optifine.reflect.Reflector;
/*      */ import net.optifine.render.ChunkVisibility;
/*      */ import net.optifine.render.CloudRenderer;
/*      */ import net.optifine.render.RenderEnv;
/*      */ import net.optifine.shaders.Shaders;
/*      */ import net.optifine.shaders.ShadersRender;
/*      */ import net.optifine.shaders.ShadowUtils;
/*      */ import net.optifine.shaders.gui.GuiShaderOptions;
/*      */ import net.optifine.util.ChunkUtils;
/*      */ import net.optifine.util.RenderChunkUtils;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.lwjgl.input.Keyboard;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.util.vector.Matrix4f;
/*      */ import org.lwjgl.util.vector.Vector3f;
/*      */ import org.lwjgl.util.vector.Vector4f;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RenderGlobal
/*      */   implements IWorldAccess, IResourceManagerReloadListener
/*      */ {
/*  121 */   private static final Logger logger = LogManager.getLogger();
/*  122 */   private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
/*  123 */   private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
/*  124 */   private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
/*  125 */   private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
/*  126 */   private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
/*      */   public final Minecraft mc;
/*      */   private final TextureManager renderEngine;
/*      */   private final RenderManager renderManager;
/*      */   private WorldClient theWorld;
/*  131 */   private Set<RenderChunk> chunksToUpdate = Sets.newLinkedHashSet();
/*  132 */   private List<ContainerLocalRenderInformation> renderInfos = Lists.newArrayListWithCapacity(69696);
/*  133 */   private final Set<TileEntity> setTileEntities = Sets.newHashSet();
/*      */   private ViewFrustum viewFrustum;
/*  135 */   private int starGLCallList = -1;
/*  136 */   private int glSkyList = -1;
/*  137 */   private int glSkyList2 = -1;
/*      */   private VertexFormat vertexBufferFormat;
/*      */   private VertexBuffer starVBO;
/*      */   private VertexBuffer skyVBO;
/*      */   private VertexBuffer sky2VBO;
/*      */   private int cloudTickCounter;
/*  143 */   public final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.newHashMap();
/*  144 */   private final Map<BlockPos, ISound> mapSoundPositions = Maps.newHashMap();
/*  145 */   private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
/*      */   private Framebuffer entityOutlineFramebuffer;
/*      */   private ShaderGroup entityOutlineShader;
/*  148 */   private double frustumUpdatePosX = Double.MIN_VALUE;
/*  149 */   private double frustumUpdatePosY = Double.MIN_VALUE;
/*  150 */   private double frustumUpdatePosZ = Double.MIN_VALUE;
/*  151 */   private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
/*  152 */   private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
/*  153 */   private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
/*  154 */   private double lastViewEntityX = Double.MIN_VALUE;
/*  155 */   private double lastViewEntityY = Double.MIN_VALUE;
/*  156 */   private double lastViewEntityZ = Double.MIN_VALUE;
/*  157 */   private double lastViewEntityPitch = Double.MIN_VALUE;
/*  158 */   private double lastViewEntityYaw = Double.MIN_VALUE;
/*  159 */   private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
/*      */   private ChunkRenderContainer renderContainer;
/*  161 */   private int renderDistanceChunks = -1;
/*  162 */   private int renderEntitiesStartupCounter = 2;
/*      */   private int countEntitiesTotal;
/*      */   private int countEntitiesRendered;
/*      */   private int countEntitiesHidden;
/*      */   private boolean debugFixTerrainFrustum = false;
/*      */   private ClippingHelper debugFixedClippingHelper;
/*  168 */   private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
/*  169 */   private final Vector3d debugTerrainFrustumPosition = new Vector3d();
/*      */   private boolean vboEnabled = false;
/*      */   IRenderChunkFactory renderChunkFactory;
/*      */   private double prevRenderSortX;
/*      */   private double prevRenderSortY;
/*      */   private double prevRenderSortZ;
/*      */   public boolean displayListEntitiesDirty = true;
/*      */   private CloudRenderer cloudRenderer;
/*      */   public Entity renderedEntity;
/*  178 */   public Set chunksToResortTransparency = new LinkedHashSet();
/*  179 */   public Set chunksToUpdateForced = new LinkedHashSet();
/*  180 */   private Deque visibilityDeque = new ArrayDeque();
/*  181 */   private List renderInfosEntities = new ArrayList(1024);
/*  182 */   private List renderInfosTileEntities = new ArrayList(1024);
/*  183 */   private List renderInfosNormal = new ArrayList(1024);
/*  184 */   private List renderInfosEntitiesNormal = new ArrayList(1024);
/*  185 */   private List renderInfosTileEntitiesNormal = new ArrayList(1024);
/*  186 */   private List renderInfosShadow = new ArrayList(1024);
/*  187 */   private List renderInfosEntitiesShadow = new ArrayList(1024);
/*  188 */   private List renderInfosTileEntitiesShadow = new ArrayList(1024);
/*  189 */   private int renderDistance = 0;
/*  190 */   private int renderDistanceSq = 0;
/*  191 */   private static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList((Object[])EnumFacing.VALUES)));
/*      */   private int countTileEntitiesRendered;
/*  193 */   private IChunkProvider worldChunkProvider = null;
/*  194 */   private LongHashMap worldChunkProviderMap = null;
/*  195 */   private int countLoadedChunksPrev = 0;
/*  196 */   private RenderEnv renderEnv = new RenderEnv(Blocks.air.getDefaultState(), new BlockPos(0, 0, 0));
/*      */   public boolean renderOverlayDamaged = false;
/*      */   public boolean renderOverlayEyes = false;
/*      */   private boolean firstWorldLoad = false;
/*  200 */   private static int renderEntitiesCounter = 0;
/*      */ 
/*      */   
/*      */   public RenderGlobal(Minecraft mcIn) {
/*  204 */     this.cloudRenderer = new CloudRenderer(mcIn);
/*  205 */     this.mc = mcIn;
/*  206 */     this.renderManager = mcIn.getRenderManager();
/*  207 */     this.renderEngine = mcIn.getTextureManager();
/*  208 */     this.renderEngine.bindTexture(locationForcefieldPng);
/*  209 */     GL11.glTexParameteri(3553, 10242, 10497);
/*  210 */     GL11.glTexParameteri(3553, 10243, 10497);
/*  211 */     GlStateManager.bindTexture(0);
/*  212 */     updateDestroyBlockIcons();
/*  213 */     this.vboEnabled = OpenGlHelper.useVbo();
/*      */     
/*  215 */     if (this.vboEnabled) {
/*      */       
/*  217 */       this.renderContainer = new VboRenderList();
/*  218 */       this.renderChunkFactory = (IRenderChunkFactory)new VboChunkFactory();
/*      */     }
/*      */     else {
/*      */       
/*  222 */       this.renderContainer = new RenderList();
/*  223 */       this.renderChunkFactory = (IRenderChunkFactory)new ListChunkFactory();
/*      */     } 
/*      */     
/*  226 */     this.vertexBufferFormat = new VertexFormat();
/*  227 */     this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
/*  228 */     generateStars();
/*  229 */     generateSky();
/*  230 */     generateSky2();
/*      */   }
/*      */ 
/*      */   
/*      */   public void onResourceManagerReload(IResourceManager resourceManager) {
/*  235 */     updateDestroyBlockIcons();
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateDestroyBlockIcons() {
/*  240 */     TextureMap texturemap = this.mc.getTextureMapBlocks();
/*      */     
/*  242 */     for (int i = 0; i < this.destroyBlockIcons.length; i++)
/*      */     {
/*  244 */       this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeEntityOutlineShader() {
/*  250 */     if (OpenGlHelper.shadersSupported) {
/*      */       
/*  252 */       if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
/*      */       {
/*  254 */         ShaderLinkHelper.setNewStaticShaderLinkHelper();
/*      */       }
/*      */       
/*  257 */       ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
/*      */ 
/*      */       
/*      */       try {
/*  261 */         this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
/*  262 */         this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
/*  263 */         this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
/*      */       }
/*  265 */       catch (IOException ioexception) {
/*      */         
/*  267 */         logger.warn("Failed to load shader: " + resourcelocation, ioexception);
/*  268 */         this.entityOutlineShader = null;
/*  269 */         this.entityOutlineFramebuffer = null;
/*      */       }
/*  271 */       catch (JsonSyntaxException jsonsyntaxexception) {
/*      */         
/*  273 */         logger.warn("Failed to load shader: " + resourcelocation, (Throwable)jsonsyntaxexception);
/*  274 */         this.entityOutlineShader = null;
/*  275 */         this.entityOutlineFramebuffer = null;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  280 */       this.entityOutlineShader = null;
/*  281 */       this.entityOutlineFramebuffer = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderEntityOutlineFramebuffer() {
/*  287 */     if (isRenderEntityOutlines()) {
/*      */       
/*  289 */       GlStateManager.enableBlend();
/*  290 */       GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
/*  291 */       this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
/*  292 */       GlStateManager.disableBlend();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isRenderEntityOutlines() {
/*  298 */     return (!Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing()) ? ((this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown())) : false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateSky2() {
/*  303 */     Tessellator tessellator = Tessellator.getInstance();
/*  304 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */     
/*  306 */     if (this.sky2VBO != null)
/*      */     {
/*  308 */       this.sky2VBO.deleteGlBuffers();
/*      */     }
/*      */     
/*  311 */     if (this.glSkyList2 >= 0) {
/*      */       
/*  313 */       GLAllocation.deleteDisplayLists(this.glSkyList2);
/*  314 */       this.glSkyList2 = -1;
/*      */     } 
/*      */     
/*  317 */     if (this.vboEnabled) {
/*      */       
/*  319 */       this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
/*  320 */       renderSky(worldrenderer, -16.0F, true);
/*  321 */       worldrenderer.finishDrawing();
/*  322 */       worldrenderer.reset();
/*  323 */       this.sky2VBO.bufferData(worldrenderer.getByteBuffer());
/*      */     }
/*      */     else {
/*      */       
/*  327 */       this.glSkyList2 = GLAllocation.generateDisplayLists(1);
/*  328 */       GL11.glNewList(this.glSkyList2, 4864);
/*  329 */       renderSky(worldrenderer, -16.0F, true);
/*  330 */       tessellator.draw();
/*  331 */       GL11.glEndList();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateSky() {
/*  337 */     Tessellator tessellator = Tessellator.getInstance();
/*  338 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */     
/*  340 */     if (this.skyVBO != null)
/*      */     {
/*  342 */       this.skyVBO.deleteGlBuffers();
/*      */     }
/*      */     
/*  345 */     if (this.glSkyList >= 0) {
/*      */       
/*  347 */       GLAllocation.deleteDisplayLists(this.glSkyList);
/*  348 */       this.glSkyList = -1;
/*      */     } 
/*      */     
/*  351 */     if (this.vboEnabled) {
/*      */       
/*  353 */       this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
/*  354 */       renderSky(worldrenderer, 16.0F, false);
/*  355 */       worldrenderer.finishDrawing();
/*  356 */       worldrenderer.reset();
/*  357 */       this.skyVBO.bufferData(worldrenderer.getByteBuffer());
/*      */     }
/*      */     else {
/*      */       
/*  361 */       this.glSkyList = GLAllocation.generateDisplayLists(1);
/*  362 */       GL11.glNewList(this.glSkyList, 4864);
/*  363 */       renderSky(worldrenderer, 16.0F, false);
/*  364 */       tessellator.draw();
/*  365 */       GL11.glEndList();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderSky(WorldRenderer worldRendererIn, float posY, boolean reverseX) {
/*  371 */     int i = 64;
/*  372 */     int j = 6;
/*  373 */     worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
/*  374 */     int k = (this.renderDistance / 64 + 1) * 64 + 64;
/*      */     
/*  376 */     for (int l = -k; l <= k; l += 64) {
/*      */       
/*  378 */       for (int i1 = -k; i1 <= k; i1 += 64) {
/*      */         
/*  380 */         float f = l;
/*  381 */         float f1 = (l + 64);
/*      */         
/*  383 */         if (reverseX) {
/*      */           
/*  385 */           f1 = l;
/*  386 */           f = (l + 64);
/*      */         } 
/*      */         
/*  389 */         worldRendererIn.pos(f, posY, i1).endVertex();
/*  390 */         worldRendererIn.pos(f1, posY, i1).endVertex();
/*  391 */         worldRendererIn.pos(f1, posY, (i1 + 64)).endVertex();
/*  392 */         worldRendererIn.pos(f, posY, (i1 + 64)).endVertex();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateStars() {
/*  399 */     Tessellator tessellator = Tessellator.getInstance();
/*  400 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */     
/*  402 */     if (this.starVBO != null)
/*      */     {
/*  404 */       this.starVBO.deleteGlBuffers();
/*      */     }
/*      */     
/*  407 */     if (this.starGLCallList >= 0) {
/*      */       
/*  409 */       GLAllocation.deleteDisplayLists(this.starGLCallList);
/*  410 */       this.starGLCallList = -1;
/*      */     } 
/*      */     
/*  413 */     if (this.vboEnabled) {
/*      */       
/*  415 */       this.starVBO = new VertexBuffer(this.vertexBufferFormat);
/*  416 */       renderStars(worldrenderer);
/*  417 */       worldrenderer.finishDrawing();
/*  418 */       worldrenderer.reset();
/*  419 */       this.starVBO.bufferData(worldrenderer.getByteBuffer());
/*      */     }
/*      */     else {
/*      */       
/*  423 */       this.starGLCallList = GLAllocation.generateDisplayLists(1);
/*  424 */       GlStateManager.pushMatrix();
/*  425 */       GL11.glNewList(this.starGLCallList, 4864);
/*  426 */       renderStars(worldrenderer);
/*  427 */       tessellator.draw();
/*  428 */       GL11.glEndList();
/*  429 */       GlStateManager.popMatrix();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderStars(WorldRenderer worldRendererIn) {
/*  435 */     Random random = new Random(10842L);
/*  436 */     worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
/*      */     
/*  438 */     for (int i = 0; i < 1500; i++) {
/*      */       
/*  440 */       double d0 = (random.nextFloat() * 2.0F - 1.0F);
/*  441 */       double d1 = (random.nextFloat() * 2.0F - 1.0F);
/*  442 */       double d2 = (random.nextFloat() * 2.0F - 1.0F);
/*  443 */       double d3 = (0.15F + random.nextFloat() * 0.1F);
/*  444 */       double d4 = d0 * d0 + d1 * d1 + d2 * d2;
/*      */       
/*  446 */       if (d4 < 1.0D && d4 > 0.01D) {
/*      */         
/*  448 */         d4 = 1.0D / Math.sqrt(d4);
/*  449 */         d0 *= d4;
/*  450 */         d1 *= d4;
/*  451 */         d2 *= d4;
/*  452 */         double d5 = d0 * 100.0D;
/*  453 */         double d6 = d1 * 100.0D;
/*  454 */         double d7 = d2 * 100.0D;
/*  455 */         double d8 = Math.atan2(d0, d2);
/*  456 */         double d9 = Math.sin(d8);
/*  457 */         double d10 = Math.cos(d8);
/*  458 */         double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
/*  459 */         double d12 = Math.sin(d11);
/*  460 */         double d13 = Math.cos(d11);
/*  461 */         double d14 = random.nextDouble() * Math.PI * 2.0D;
/*  462 */         double d15 = Math.sin(d14);
/*  463 */         double d16 = Math.cos(d14);
/*      */         
/*  465 */         for (int j = 0; j < 4; j++) {
/*      */           
/*  467 */           double d17 = 0.0D;
/*  468 */           double d18 = ((j & 0x2) - 1) * d3;
/*  469 */           double d19 = ((j + 1 & 0x2) - 1) * d3;
/*  470 */           double d20 = 0.0D;
/*  471 */           double d21 = d18 * d16 - d19 * d15;
/*  472 */           double d22 = d19 * d16 + d18 * d15;
/*  473 */           double d23 = d21 * d12 + 0.0D * d13;
/*  474 */           double d24 = 0.0D * d12 - d21 * d13;
/*  475 */           double d25 = d24 * d9 - d22 * d10;
/*  476 */           double d26 = d22 * d9 + d24 * d10;
/*  477 */           worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWorldAndLoadRenderers(WorldClient worldClientIn) {
/*  485 */     if (this.theWorld != null)
/*      */     {
/*  487 */       this.theWorld.removeWorldAccess(this);
/*      */     }
/*      */     
/*  490 */     this.frustumUpdatePosX = Double.MIN_VALUE;
/*  491 */     this.frustumUpdatePosY = Double.MIN_VALUE;
/*  492 */     this.frustumUpdatePosZ = Double.MIN_VALUE;
/*  493 */     this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
/*  494 */     this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
/*  495 */     this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
/*  496 */     this.renderManager.set((World)worldClientIn);
/*  497 */     this.theWorld = worldClientIn;
/*      */     
/*  499 */     if (Config.isDynamicLights())
/*      */     {
/*  501 */       DynamicLights.clear();
/*      */     }
/*      */     
/*  504 */     ChunkVisibility.reset();
/*  505 */     this.worldChunkProvider = null;
/*  506 */     this.worldChunkProviderMap = null;
/*  507 */     this.renderEnv.reset((IBlockState)null, (BlockPos)null);
/*  508 */     Shaders.checkWorldChanged((World)this.theWorld);
/*      */     
/*  510 */     if (worldClientIn != null) {
/*      */       
/*  512 */       worldClientIn.addWorldAccess(this);
/*  513 */       loadRenderers();
/*      */     }
/*      */     else {
/*      */       
/*  517 */       this.chunksToUpdate.clear();
/*  518 */       clearRenderInfos();
/*      */       
/*  520 */       if (this.viewFrustum != null)
/*      */       {
/*  522 */         this.viewFrustum.deleteGlResources();
/*      */       }
/*      */       
/*  525 */       this.viewFrustum = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void loadRenderers() {
/*  531 */     if (this.theWorld != null) {
/*      */       
/*  533 */       this.displayListEntitiesDirty = true;
/*  534 */       Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
/*  535 */       Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
/*  536 */       BlockModelRenderer.updateAoLightValue();
/*      */       
/*  538 */       if (Config.isDynamicLights())
/*      */       {
/*  540 */         DynamicLights.clear();
/*      */       }
/*      */       
/*  543 */       SmartAnimations.update();
/*  544 */       this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
/*  545 */       this.renderDistance = this.renderDistanceChunks * 16;
/*  546 */       this.renderDistanceSq = this.renderDistance * this.renderDistance;
/*  547 */       boolean flag = this.vboEnabled;
/*  548 */       this.vboEnabled = OpenGlHelper.useVbo();
/*      */       
/*  550 */       if (flag && !this.vboEnabled) {
/*      */         
/*  552 */         this.renderContainer = new RenderList();
/*  553 */         this.renderChunkFactory = (IRenderChunkFactory)new ListChunkFactory();
/*      */       }
/*  555 */       else if (!flag && this.vboEnabled) {
/*      */         
/*  557 */         this.renderContainer = new VboRenderList();
/*  558 */         this.renderChunkFactory = (IRenderChunkFactory)new VboChunkFactory();
/*      */       } 
/*      */       
/*  561 */       generateStars();
/*  562 */       generateSky();
/*  563 */       generateSky2();
/*      */       
/*  565 */       if (this.viewFrustum != null)
/*      */       {
/*  567 */         this.viewFrustum.deleteGlResources();
/*      */       }
/*      */       
/*  570 */       stopChunkUpdates();
/*      */       
/*  572 */       synchronized (this.setTileEntities) {
/*      */         
/*  574 */         this.setTileEntities.clear();
/*      */       } 
/*      */       
/*  577 */       this.viewFrustum = new ViewFrustum((World)this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
/*      */       
/*  579 */       if (this.theWorld != null) {
/*      */         
/*  581 */         Entity entity = this.mc.getRenderViewEntity();
/*      */         
/*  583 */         if (entity != null)
/*      */         {
/*  585 */           this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
/*      */         }
/*      */       } 
/*      */       
/*  589 */       this.renderEntitiesStartupCounter = 2;
/*      */     } 
/*      */     
/*  592 */     if (this.mc.thePlayer == null)
/*      */     {
/*  594 */       this.firstWorldLoad = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void stopChunkUpdates() {
/*  600 */     this.chunksToUpdate.clear();
/*  601 */     this.renderDispatcher.stopChunkUpdates();
/*      */   }
/*      */ 
/*      */   
/*      */   public void createBindEntityOutlineFbs(int width, int height) {
/*  606 */     if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null)
/*      */     {
/*  608 */       this.entityOutlineShader.createBindFramebuffers(width, height);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks) {
/*      */     // Byte code:
/*      */     //   0: iconst_0
/*      */     //   1: istore #4
/*      */     //   3: getstatic net/optifine/reflect/Reflector.MinecraftForgeClient_getRenderPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   6: invokevirtual exists : ()Z
/*      */     //   9: ifeq -> 24
/*      */     //   12: getstatic net/optifine/reflect/Reflector.MinecraftForgeClient_getRenderPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   15: iconst_0
/*      */     //   16: anewarray java/lang/Object
/*      */     //   19: invokestatic callInt : (Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)I
/*      */     //   22: istore #4
/*      */     //   24: aload_0
/*      */     //   25: getfield renderEntitiesStartupCounter : I
/*      */     //   28: ifle -> 50
/*      */     //   31: iload #4
/*      */     //   33: ifle -> 37
/*      */     //   36: return
/*      */     //   37: aload_0
/*      */     //   38: dup
/*      */     //   39: getfield renderEntitiesStartupCounter : I
/*      */     //   42: iconst_1
/*      */     //   43: isub
/*      */     //   44: putfield renderEntitiesStartupCounter : I
/*      */     //   47: goto -> 2068
/*      */     //   50: aload_1
/*      */     //   51: getfield prevPosX : D
/*      */     //   54: aload_1
/*      */     //   55: getfield posX : D
/*      */     //   58: aload_1
/*      */     //   59: getfield prevPosX : D
/*      */     //   62: dsub
/*      */     //   63: fload_3
/*      */     //   64: f2d
/*      */     //   65: dmul
/*      */     //   66: dadd
/*      */     //   67: dstore #5
/*      */     //   69: aload_1
/*      */     //   70: getfield prevPosY : D
/*      */     //   73: aload_1
/*      */     //   74: getfield posY : D
/*      */     //   77: aload_1
/*      */     //   78: getfield prevPosY : D
/*      */     //   81: dsub
/*      */     //   82: fload_3
/*      */     //   83: f2d
/*      */     //   84: dmul
/*      */     //   85: dadd
/*      */     //   86: dstore #7
/*      */     //   88: aload_1
/*      */     //   89: getfield prevPosZ : D
/*      */     //   92: aload_1
/*      */     //   93: getfield posZ : D
/*      */     //   96: aload_1
/*      */     //   97: getfield prevPosZ : D
/*      */     //   100: dsub
/*      */     //   101: fload_3
/*      */     //   102: f2d
/*      */     //   103: dmul
/*      */     //   104: dadd
/*      */     //   105: dstore #9
/*      */     //   107: aload_0
/*      */     //   108: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   111: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
/*      */     //   114: ldc 'prepare'
/*      */     //   116: invokevirtual startSection : (Ljava/lang/String;)V
/*      */     //   119: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
/*      */     //   122: aload_0
/*      */     //   123: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   126: aload_0
/*      */     //   127: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   130: invokevirtual getTextureManager : ()Lnet/minecraft/client/renderer/texture/TextureManager;
/*      */     //   133: aload_0
/*      */     //   134: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   137: getfield fontRendererObj : Lnet/minecraft/client/gui/FontRenderer;
/*      */     //   140: aload_0
/*      */     //   141: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   144: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   147: fload_3
/*      */     //   148: invokevirtual cacheActiveRenderInfo : (Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/entity/Entity;F)V
/*      */     //   151: aload_0
/*      */     //   152: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   155: aload_0
/*      */     //   156: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   159: aload_0
/*      */     //   160: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   163: getfield fontRendererObj : Lnet/minecraft/client/gui/FontRenderer;
/*      */     //   166: aload_0
/*      */     //   167: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   170: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   173: aload_0
/*      */     //   174: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   177: getfield pointedEntity : Lnet/minecraft/entity/Entity;
/*      */     //   180: aload_0
/*      */     //   181: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   184: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
/*      */     //   187: fload_3
/*      */     //   188: invokevirtual cacheActiveRenderInfo : (Lnet/minecraft/world/World;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/settings/GameSettings;F)V
/*      */     //   191: getstatic net/minecraft/client/renderer/RenderGlobal.renderEntitiesCounter : I
/*      */     //   194: iconst_1
/*      */     //   195: iadd
/*      */     //   196: putstatic net/minecraft/client/renderer/RenderGlobal.renderEntitiesCounter : I
/*      */     //   199: iload #4
/*      */     //   201: ifne -> 224
/*      */     //   204: aload_0
/*      */     //   205: iconst_0
/*      */     //   206: putfield countEntitiesTotal : I
/*      */     //   209: aload_0
/*      */     //   210: iconst_0
/*      */     //   211: putfield countEntitiesRendered : I
/*      */     //   214: aload_0
/*      */     //   215: iconst_0
/*      */     //   216: putfield countEntitiesHidden : I
/*      */     //   219: aload_0
/*      */     //   220: iconst_0
/*      */     //   221: putfield countTileEntitiesRendered : I
/*      */     //   224: aload_0
/*      */     //   225: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   228: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   231: astore #11
/*      */     //   233: aload #11
/*      */     //   235: getfield lastTickPosX : D
/*      */     //   238: aload #11
/*      */     //   240: getfield posX : D
/*      */     //   243: aload #11
/*      */     //   245: getfield lastTickPosX : D
/*      */     //   248: dsub
/*      */     //   249: fload_3
/*      */     //   250: f2d
/*      */     //   251: dmul
/*      */     //   252: dadd
/*      */     //   253: dstore #12
/*      */     //   255: aload #11
/*      */     //   257: getfield lastTickPosY : D
/*      */     //   260: aload #11
/*      */     //   262: getfield posY : D
/*      */     //   265: aload #11
/*      */     //   267: getfield lastTickPosY : D
/*      */     //   270: dsub
/*      */     //   271: fload_3
/*      */     //   272: f2d
/*      */     //   273: dmul
/*      */     //   274: dadd
/*      */     //   275: dstore #14
/*      */     //   277: aload #11
/*      */     //   279: getfield lastTickPosZ : D
/*      */     //   282: aload #11
/*      */     //   284: getfield posZ : D
/*      */     //   287: aload #11
/*      */     //   289: getfield lastTickPosZ : D
/*      */     //   292: dsub
/*      */     //   293: fload_3
/*      */     //   294: f2d
/*      */     //   295: dmul
/*      */     //   296: dadd
/*      */     //   297: dstore #16
/*      */     //   299: dload #12
/*      */     //   301: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerX : D
/*      */     //   304: dload #14
/*      */     //   306: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerY : D
/*      */     //   309: dload #16
/*      */     //   311: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerZ : D
/*      */     //   314: aload_0
/*      */     //   315: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   318: dload #12
/*      */     //   320: dload #14
/*      */     //   322: dload #16
/*      */     //   324: invokevirtual setRenderPosition : (DDD)V
/*      */     //   327: aload_0
/*      */     //   328: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   331: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
/*      */     //   334: invokevirtual enableLightmap : ()V
/*      */     //   337: aload_0
/*      */     //   338: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   341: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
/*      */     //   344: ldc_w 'global'
/*      */     //   347: invokevirtual endStartSection : (Ljava/lang/String;)V
/*      */     //   350: aload_0
/*      */     //   351: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   354: invokevirtual getLoadedEntityList : ()Ljava/util/List;
/*      */     //   357: astore #18
/*      */     //   359: iload #4
/*      */     //   361: ifne -> 375
/*      */     //   364: aload_0
/*      */     //   365: aload #18
/*      */     //   367: invokeinterface size : ()I
/*      */     //   372: putfield countEntitiesTotal : I
/*      */     //   375: invokestatic isFogOff : ()Z
/*      */     //   378: ifeq -> 397
/*      */     //   381: aload_0
/*      */     //   382: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   385: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
/*      */     //   388: getfield fogStandard : Z
/*      */     //   391: ifeq -> 397
/*      */     //   394: invokestatic disableFog : ()V
/*      */     //   397: getstatic net/optifine/reflect/Reflector.ForgeEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   400: invokevirtual exists : ()Z
/*      */     //   403: istore #19
/*      */     //   405: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   408: invokevirtual exists : ()Z
/*      */     //   411: istore #20
/*      */     //   413: iconst_0
/*      */     //   414: istore #21
/*      */     //   416: iload #21
/*      */     //   418: aload_0
/*      */     //   419: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   422: getfield weatherEffects : Ljava/util/List;
/*      */     //   425: invokeinterface size : ()I
/*      */     //   430: if_icmpge -> 521
/*      */     //   433: aload_0
/*      */     //   434: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   437: getfield weatherEffects : Ljava/util/List;
/*      */     //   440: iload #21
/*      */     //   442: invokeinterface get : (I)Ljava/lang/Object;
/*      */     //   447: checkcast net/minecraft/entity/Entity
/*      */     //   450: astore #22
/*      */     //   452: iload #19
/*      */     //   454: ifeq -> 480
/*      */     //   457: aload #22
/*      */     //   459: getstatic net/optifine/reflect/Reflector.ForgeEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   462: iconst_1
/*      */     //   463: anewarray java/lang/Object
/*      */     //   466: dup
/*      */     //   467: iconst_0
/*      */     //   468: iload #4
/*      */     //   470: invokestatic valueOf : (I)Ljava/lang/Integer;
/*      */     //   473: aastore
/*      */     //   474: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   477: ifeq -> 515
/*      */     //   480: aload_0
/*      */     //   481: dup
/*      */     //   482: getfield countEntitiesRendered : I
/*      */     //   485: iconst_1
/*      */     //   486: iadd
/*      */     //   487: putfield countEntitiesRendered : I
/*      */     //   490: aload #22
/*      */     //   492: dload #5
/*      */     //   494: dload #7
/*      */     //   496: dload #9
/*      */     //   498: invokevirtual isInRangeToRender3d : (DDD)Z
/*      */     //   501: ifeq -> 515
/*      */     //   504: aload_0
/*      */     //   505: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   508: aload #22
/*      */     //   510: fload_3
/*      */     //   511: invokevirtual renderEntitySimple : (Lnet/minecraft/entity/Entity;F)Z
/*      */     //   514: pop
/*      */     //   515: iinc #21, 1
/*      */     //   518: goto -> 416
/*      */     //   521: aload_0
/*      */     //   522: invokevirtual isRenderEntityOutlines : ()Z
/*      */     //   525: ifeq -> 821
/*      */     //   528: sipush #519
/*      */     //   531: invokestatic depthFunc : (I)V
/*      */     //   534: invokestatic disableFog : ()V
/*      */     //   537: aload_0
/*      */     //   538: getfield entityOutlineFramebuffer : Lnet/minecraft/client/shader/Framebuffer;
/*      */     //   541: invokevirtual framebufferClear : ()V
/*      */     //   544: aload_0
/*      */     //   545: getfield entityOutlineFramebuffer : Lnet/minecraft/client/shader/Framebuffer;
/*      */     //   548: iconst_0
/*      */     //   549: invokevirtual bindFramebuffer : (Z)V
/*      */     //   552: aload_0
/*      */     //   553: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   556: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
/*      */     //   559: ldc_w 'entityOutlines'
/*      */     //   562: invokevirtual endStartSection : (Ljava/lang/String;)V
/*      */     //   565: invokestatic disableStandardItemLighting : ()V
/*      */     //   568: aload_0
/*      */     //   569: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   572: iconst_1
/*      */     //   573: invokevirtual setRenderOutlines : (Z)V
/*      */     //   576: iconst_0
/*      */     //   577: istore #21
/*      */     //   579: iload #21
/*      */     //   581: aload #18
/*      */     //   583: invokeinterface size : ()I
/*      */     //   588: if_icmpge -> 759
/*      */     //   591: aload #18
/*      */     //   593: iload #21
/*      */     //   595: invokeinterface get : (I)Ljava/lang/Object;
/*      */     //   600: checkcast net/minecraft/entity/Entity
/*      */     //   603: astore #22
/*      */     //   605: aload_0
/*      */     //   606: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   609: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   612: instanceof net/minecraft/entity/EntityLivingBase
/*      */     //   615: ifeq -> 638
/*      */     //   618: aload_0
/*      */     //   619: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   622: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   625: checkcast net/minecraft/entity/EntityLivingBase
/*      */     //   628: invokevirtual isPlayerSleeping : ()Z
/*      */     //   631: ifeq -> 638
/*      */     //   634: iconst_1
/*      */     //   635: goto -> 639
/*      */     //   638: iconst_0
/*      */     //   639: istore #23
/*      */     //   641: aload #22
/*      */     //   643: dload #5
/*      */     //   645: dload #7
/*      */     //   647: dload #9
/*      */     //   649: invokevirtual isInRangeToRender3d : (DDD)Z
/*      */     //   652: ifeq -> 704
/*      */     //   655: aload #22
/*      */     //   657: getfield ignoreFrustumCheck : Z
/*      */     //   660: ifne -> 692
/*      */     //   663: aload_2
/*      */     //   664: aload #22
/*      */     //   666: invokevirtual getEntityBoundingBox : ()Lnet/minecraft/util/AxisAlignedBB;
/*      */     //   669: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
/*      */     //   674: ifne -> 692
/*      */     //   677: aload #22
/*      */     //   679: getfield riddenByEntity : Lnet/minecraft/entity/Entity;
/*      */     //   682: aload_0
/*      */     //   683: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   686: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   689: if_acmpne -> 704
/*      */     //   692: aload #22
/*      */     //   694: instanceof net/minecraft/entity/player/EntityPlayer
/*      */     //   697: ifeq -> 704
/*      */     //   700: iconst_1
/*      */     //   701: goto -> 705
/*      */     //   704: iconst_0
/*      */     //   705: istore #24
/*      */     //   707: aload #22
/*      */     //   709: aload_0
/*      */     //   710: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   713: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   716: if_acmpne -> 737
/*      */     //   719: aload_0
/*      */     //   720: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   723: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
/*      */     //   726: getfield thirdPersonView : I
/*      */     //   729: ifne -> 737
/*      */     //   732: iload #23
/*      */     //   734: ifeq -> 753
/*      */     //   737: iload #24
/*      */     //   739: ifeq -> 753
/*      */     //   742: aload_0
/*      */     //   743: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   746: aload #22
/*      */     //   748: fload_3
/*      */     //   749: invokevirtual renderEntitySimple : (Lnet/minecraft/entity/Entity;F)Z
/*      */     //   752: pop
/*      */     //   753: iinc #21, 1
/*      */     //   756: goto -> 579
/*      */     //   759: aload_0
/*      */     //   760: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   763: iconst_0
/*      */     //   764: invokevirtual setRenderOutlines : (Z)V
/*      */     //   767: invokestatic enableStandardItemLighting : ()V
/*      */     //   770: iconst_0
/*      */     //   771: invokestatic depthMask : (Z)V
/*      */     //   774: aload_0
/*      */     //   775: getfield entityOutlineShader : Lnet/minecraft/client/shader/ShaderGroup;
/*      */     //   778: fload_3
/*      */     //   779: invokevirtual loadShaderGroup : (F)V
/*      */     //   782: invokestatic enableLighting : ()V
/*      */     //   785: iconst_1
/*      */     //   786: invokestatic depthMask : (Z)V
/*      */     //   789: aload_0
/*      */     //   790: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   793: invokevirtual getFramebuffer : ()Lnet/minecraft/client/shader/Framebuffer;
/*      */     //   796: iconst_0
/*      */     //   797: invokevirtual bindFramebuffer : (Z)V
/*      */     //   800: invokestatic enableFog : ()V
/*      */     //   803: invokestatic enableBlend : ()V
/*      */     //   806: invokestatic enableColorMaterial : ()V
/*      */     //   809: sipush #515
/*      */     //   812: invokestatic depthFunc : (I)V
/*      */     //   815: invokestatic enableDepth : ()V
/*      */     //   818: invokestatic enableAlpha : ()V
/*      */     //   821: aload_0
/*      */     //   822: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   825: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
/*      */     //   828: ldc_w 'entities'
/*      */     //   831: invokevirtual endStartSection : (Ljava/lang/String;)V
/*      */     //   834: invokestatic isShaders : ()Z
/*      */     //   837: istore #21
/*      */     //   839: iload #21
/*      */     //   841: ifeq -> 847
/*      */     //   844: invokestatic beginEntities : ()V
/*      */     //   847: invokestatic updateItemRenderDistance : ()V
/*      */     //   850: aload_0
/*      */     //   851: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   854: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
/*      */     //   857: getfield fancyGraphics : Z
/*      */     //   860: istore #22
/*      */     //   862: aload_0
/*      */     //   863: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   866: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
/*      */     //   869: invokestatic isDroppedItemsFancy : ()Z
/*      */     //   872: putfield fancyGraphics : Z
/*      */     //   875: getstatic net/optifine/shaders/Shaders.isShadowPass : Z
/*      */     //   878: ifeq -> 898
/*      */     //   881: aload_0
/*      */     //   882: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   885: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   888: invokevirtual isSpectator : ()Z
/*      */     //   891: ifne -> 898
/*      */     //   894: iconst_1
/*      */     //   895: goto -> 899
/*      */     //   898: iconst_0
/*      */     //   899: istore #23
/*      */     //   901: aload_0
/*      */     //   902: getfield renderInfosEntities : Ljava/util/List;
/*      */     //   905: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   910: astore #24
/*      */     //   912: aload #24
/*      */     //   914: invokeinterface hasNext : ()Z
/*      */     //   919: ifeq -> 1324
/*      */     //   922: aload #24
/*      */     //   924: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   929: astore #25
/*      */     //   931: aload #25
/*      */     //   933: checkcast net/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation
/*      */     //   936: astore #26
/*      */     //   938: aload #26
/*      */     //   940: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
/*      */     //   943: invokevirtual getChunk : ()Lnet/minecraft/world/chunk/Chunk;
/*      */     //   946: astore #27
/*      */     //   948: aload #27
/*      */     //   950: invokevirtual getEntityLists : ()[Lnet/minecraft/util/ClassInheritanceMultiMap;
/*      */     //   953: aload #26
/*      */     //   955: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
/*      */     //   958: invokevirtual getPosition : ()Lnet/minecraft/util/BlockPos;
/*      */     //   961: invokevirtual getY : ()I
/*      */     //   964: bipush #16
/*      */     //   966: idiv
/*      */     //   967: aaload
/*      */     //   968: astore #28
/*      */     //   970: aload #28
/*      */     //   972: invokevirtual isEmpty : ()Z
/*      */     //   975: ifne -> 1321
/*      */     //   978: aload #28
/*      */     //   980: invokevirtual iterator : ()Ljava/util/Iterator;
/*      */     //   983: astore #29
/*      */     //   985: aload #29
/*      */     //   987: invokeinterface hasNext : ()Z
/*      */     //   992: ifne -> 998
/*      */     //   995: goto -> 912
/*      */     //   998: aload #29
/*      */     //   1000: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   1005: checkcast net/minecraft/entity/Entity
/*      */     //   1008: astore #30
/*      */     //   1010: iload #19
/*      */     //   1012: ifeq -> 1038
/*      */     //   1015: aload #30
/*      */     //   1017: getstatic net/optifine/reflect/Reflector.ForgeEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1020: iconst_1
/*      */     //   1021: anewarray java/lang/Object
/*      */     //   1024: dup
/*      */     //   1025: iconst_0
/*      */     //   1026: iload #4
/*      */     //   1028: invokestatic valueOf : (I)Ljava/lang/Integer;
/*      */     //   1031: aastore
/*      */     //   1032: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   1035: ifeq -> 985
/*      */     //   1038: aload_0
/*      */     //   1039: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   1042: aload #30
/*      */     //   1044: aload_2
/*      */     //   1045: dload #5
/*      */     //   1047: dload #7
/*      */     //   1049: dload #9
/*      */     //   1051: invokevirtual shouldRender : (Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z
/*      */     //   1054: ifne -> 1072
/*      */     //   1057: aload #30
/*      */     //   1059: getfield riddenByEntity : Lnet/minecraft/entity/Entity;
/*      */     //   1062: aload_0
/*      */     //   1063: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1066: getfield thePlayer : Lnet/minecraft/client/entity/EntityPlayerSP;
/*      */     //   1069: if_acmpne -> 1076
/*      */     //   1072: iconst_1
/*      */     //   1073: goto -> 1077
/*      */     //   1076: iconst_0
/*      */     //   1077: istore #31
/*      */     //   1079: iload #31
/*      */     //   1081: ifne -> 1087
/*      */     //   1084: goto -> 1243
/*      */     //   1087: aload_0
/*      */     //   1088: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1091: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   1094: instanceof net/minecraft/entity/EntityLivingBase
/*      */     //   1097: ifeq -> 1116
/*      */     //   1100: aload_0
/*      */     //   1101: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1104: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   1107: checkcast net/minecraft/entity/EntityLivingBase
/*      */     //   1110: invokevirtual isPlayerSleeping : ()Z
/*      */     //   1113: goto -> 1117
/*      */     //   1116: iconst_0
/*      */     //   1117: istore #32
/*      */     //   1119: aload #30
/*      */     //   1121: aload_0
/*      */     //   1122: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1125: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
/*      */     //   1128: if_acmpne -> 1154
/*      */     //   1131: iload #23
/*      */     //   1133: ifne -> 1154
/*      */     //   1136: aload_0
/*      */     //   1137: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1140: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
/*      */     //   1143: getfield thirdPersonView : I
/*      */     //   1146: ifne -> 1154
/*      */     //   1149: iload #32
/*      */     //   1151: ifeq -> 1240
/*      */     //   1154: aload #30
/*      */     //   1156: getfield posY : D
/*      */     //   1159: dconst_0
/*      */     //   1160: dcmpg
/*      */     //   1161: iflt -> 1195
/*      */     //   1164: aload #30
/*      */     //   1166: getfield posY : D
/*      */     //   1169: ldc2_w 256.0
/*      */     //   1172: dcmpl
/*      */     //   1173: ifge -> 1195
/*      */     //   1176: aload_0
/*      */     //   1177: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   1180: new net/minecraft/util/BlockPos
/*      */     //   1183: dup
/*      */     //   1184: aload #30
/*      */     //   1186: invokespecial <init> : (Lnet/minecraft/entity/Entity;)V
/*      */     //   1189: invokevirtual isBlockLoaded : (Lnet/minecraft/util/BlockPos;)Z
/*      */     //   1192: ifeq -> 1240
/*      */     //   1195: aload_0
/*      */     //   1196: dup
/*      */     //   1197: getfield countEntitiesRendered : I
/*      */     //   1200: iconst_1
/*      */     //   1201: iadd
/*      */     //   1202: putfield countEntitiesRendered : I
/*      */     //   1205: aload_0
/*      */     //   1206: aload #30
/*      */     //   1208: putfield renderedEntity : Lnet/minecraft/entity/Entity;
/*      */     //   1211: iload #21
/*      */     //   1213: ifeq -> 1221
/*      */     //   1216: aload #30
/*      */     //   1218: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
/*      */     //   1221: aload_0
/*      */     //   1222: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   1225: aload #30
/*      */     //   1227: fload_3
/*      */     //   1228: invokevirtual renderEntitySimple : (Lnet/minecraft/entity/Entity;F)Z
/*      */     //   1231: pop
/*      */     //   1232: aload_0
/*      */     //   1233: aconst_null
/*      */     //   1234: putfield renderedEntity : Lnet/minecraft/entity/Entity;
/*      */     //   1237: goto -> 1243
/*      */     //   1240: goto -> 985
/*      */     //   1243: iload #31
/*      */     //   1245: ifne -> 1318
/*      */     //   1248: aload #30
/*      */     //   1250: instanceof net/minecraft/entity/projectile/EntityWitherSkull
/*      */     //   1253: ifeq -> 1318
/*      */     //   1256: iload #19
/*      */     //   1258: ifeq -> 1284
/*      */     //   1261: aload #30
/*      */     //   1263: getstatic net/optifine/reflect/Reflector.ForgeEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1266: iconst_1
/*      */     //   1267: anewarray java/lang/Object
/*      */     //   1270: dup
/*      */     //   1271: iconst_0
/*      */     //   1272: iload #4
/*      */     //   1274: invokestatic valueOf : (I)Ljava/lang/Integer;
/*      */     //   1277: aastore
/*      */     //   1278: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   1281: ifeq -> 1318
/*      */     //   1284: aload_0
/*      */     //   1285: aload #30
/*      */     //   1287: putfield renderedEntity : Lnet/minecraft/entity/Entity;
/*      */     //   1290: iload #21
/*      */     //   1292: ifeq -> 1300
/*      */     //   1295: aload #30
/*      */     //   1297: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
/*      */     //   1300: aload_0
/*      */     //   1301: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1304: invokevirtual getRenderManager : ()Lnet/minecraft/client/renderer/entity/RenderManager;
/*      */     //   1307: aload #30
/*      */     //   1309: fload_3
/*      */     //   1310: invokevirtual renderWitherSkull : (Lnet/minecraft/entity/Entity;F)V
/*      */     //   1313: aload_0
/*      */     //   1314: aconst_null
/*      */     //   1315: putfield renderedEntity : Lnet/minecraft/entity/Entity;
/*      */     //   1318: goto -> 985
/*      */     //   1321: goto -> 912
/*      */     //   1324: aload_0
/*      */     //   1325: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   1328: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
/*      */     //   1331: iload #22
/*      */     //   1333: putfield fancyGraphics : Z
/*      */     //   1336: iload #21
/*      */     //   1338: ifeq -> 1347
/*      */     //   1341: invokestatic endEntities : ()V
/*      */     //   1344: invokestatic beginBlockEntities : ()V
/*      */     //   1347: aload_0
/*      */     //   1348: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   1351: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
/*      */     //   1354: ldc_w 'blockentities'
/*      */     //   1357: invokevirtual endStartSection : (Ljava/lang/String;)V
/*      */     //   1360: invokestatic enableStandardItemLighting : ()V
/*      */     //   1363: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_hasFastRenderer : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1366: invokevirtual exists : ()Z
/*      */     //   1369: ifeq -> 1378
/*      */     //   1372: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
/*      */     //   1375: invokevirtual preDrawBatch : ()V
/*      */     //   1378: invokestatic updateTextRenderDistance : ()V
/*      */     //   1381: aload_0
/*      */     //   1382: getfield renderInfosTileEntities : Ljava/util/List;
/*      */     //   1385: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   1390: astore #24
/*      */     //   1392: aload #24
/*      */     //   1394: invokeinterface hasNext : ()Z
/*      */     //   1399: ifeq -> 1581
/*      */     //   1402: aload #24
/*      */     //   1404: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   1409: astore #25
/*      */     //   1411: aload #25
/*      */     //   1413: checkcast net/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation
/*      */     //   1416: astore #26
/*      */     //   1418: aload #26
/*      */     //   1420: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
/*      */     //   1423: invokevirtual getCompiledChunk : ()Lnet/minecraft/client/renderer/chunk/CompiledChunk;
/*      */     //   1426: invokevirtual getTileEntities : ()Ljava/util/List;
/*      */     //   1429: astore #27
/*      */     //   1431: aload #27
/*      */     //   1433: invokeinterface isEmpty : ()Z
/*      */     //   1438: ifne -> 1578
/*      */     //   1441: aload #27
/*      */     //   1443: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   1448: astore #28
/*      */     //   1450: aload #28
/*      */     //   1452: invokeinterface hasNext : ()Z
/*      */     //   1457: ifne -> 1463
/*      */     //   1460: goto -> 1392
/*      */     //   1463: aload #28
/*      */     //   1465: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   1470: checkcast net/minecraft/tileentity/TileEntity
/*      */     //   1473: astore #29
/*      */     //   1475: iload #20
/*      */     //   1477: ifne -> 1483
/*      */     //   1480: goto -> 1545
/*      */     //   1483: aload #29
/*      */     //   1485: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1488: iconst_1
/*      */     //   1489: anewarray java/lang/Object
/*      */     //   1492: dup
/*      */     //   1493: iconst_0
/*      */     //   1494: iload #4
/*      */     //   1496: invokestatic valueOf : (I)Ljava/lang/Integer;
/*      */     //   1499: aastore
/*      */     //   1500: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   1503: ifeq -> 1450
/*      */     //   1506: aload #29
/*      */     //   1508: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_getRenderBoundingBox : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1511: iconst_0
/*      */     //   1512: anewarray java/lang/Object
/*      */     //   1515: invokestatic call : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   1518: checkcast net/minecraft/util/AxisAlignedBB
/*      */     //   1521: astore #30
/*      */     //   1523: aload #30
/*      */     //   1525: ifnull -> 1545
/*      */     //   1528: aload_2
/*      */     //   1529: aload #30
/*      */     //   1531: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
/*      */     //   1536: ifeq -> 1542
/*      */     //   1539: goto -> 1545
/*      */     //   1542: goto -> 1450
/*      */     //   1545: iload #21
/*      */     //   1547: ifeq -> 1555
/*      */     //   1550: aload #29
/*      */     //   1552: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
/*      */     //   1555: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
/*      */     //   1558: aload #29
/*      */     //   1560: fload_3
/*      */     //   1561: iconst_m1
/*      */     //   1562: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
/*      */     //   1565: aload_0
/*      */     //   1566: dup
/*      */     //   1567: getfield countTileEntitiesRendered : I
/*      */     //   1570: iconst_1
/*      */     //   1571: iadd
/*      */     //   1572: putfield countTileEntitiesRendered : I
/*      */     //   1575: goto -> 1450
/*      */     //   1578: goto -> 1392
/*      */     //   1581: aload_0
/*      */     //   1582: getfield setTileEntities : Ljava/util/Set;
/*      */     //   1585: dup
/*      */     //   1586: astore #24
/*      */     //   1588: monitorenter
/*      */     //   1589: aload_0
/*      */     //   1590: getfield setTileEntities : Ljava/util/Set;
/*      */     //   1593: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   1598: astore #25
/*      */     //   1600: aload #25
/*      */     //   1602: invokeinterface hasNext : ()Z
/*      */     //   1607: ifeq -> 1673
/*      */     //   1610: aload #25
/*      */     //   1612: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   1617: checkcast net/minecraft/tileentity/TileEntity
/*      */     //   1620: astore #26
/*      */     //   1622: iload #20
/*      */     //   1624: ifeq -> 1650
/*      */     //   1627: aload #26
/*      */     //   1629: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1632: iconst_1
/*      */     //   1633: anewarray java/lang/Object
/*      */     //   1636: dup
/*      */     //   1637: iconst_0
/*      */     //   1638: iload #4
/*      */     //   1640: invokestatic valueOf : (I)Ljava/lang/Integer;
/*      */     //   1643: aastore
/*      */     //   1644: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   1647: ifeq -> 1670
/*      */     //   1650: iload #21
/*      */     //   1652: ifeq -> 1660
/*      */     //   1655: aload #26
/*      */     //   1657: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
/*      */     //   1660: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
/*      */     //   1663: aload #26
/*      */     //   1665: fload_3
/*      */     //   1666: iconst_m1
/*      */     //   1667: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
/*      */     //   1670: goto -> 1600
/*      */     //   1673: aload #24
/*      */     //   1675: monitorexit
/*      */     //   1676: goto -> 1687
/*      */     //   1679: astore #33
/*      */     //   1681: aload #24
/*      */     //   1683: monitorexit
/*      */     //   1684: aload #33
/*      */     //   1686: athrow
/*      */     //   1687: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_hasFastRenderer : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1690: invokevirtual exists : ()Z
/*      */     //   1693: ifeq -> 1704
/*      */     //   1696: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
/*      */     //   1699: iload #4
/*      */     //   1701: invokevirtual drawBatch : (I)V
/*      */     //   1704: aload_0
/*      */     //   1705: iconst_1
/*      */     //   1706: putfield renderOverlayDamaged : Z
/*      */     //   1709: aload_0
/*      */     //   1710: invokespecial preRenderDamagedBlocks : ()V
/*      */     //   1713: aload_0
/*      */     //   1714: getfield damagedBlocks : Ljava/util/Map;
/*      */     //   1717: invokeinterface values : ()Ljava/util/Collection;
/*      */     //   1722: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   1727: astore #24
/*      */     //   1729: aload #24
/*      */     //   1731: invokeinterface hasNext : ()Z
/*      */     //   1736: ifeq -> 2023
/*      */     //   1739: aload #24
/*      */     //   1741: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   1746: checkcast net/minecraft/client/renderer/DestroyBlockProgress
/*      */     //   1749: astore #25
/*      */     //   1751: aload #25
/*      */     //   1753: invokevirtual getPosition : ()Lnet/minecraft/util/BlockPos;
/*      */     //   1756: astore #26
/*      */     //   1758: aload_0
/*      */     //   1759: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   1762: aload #26
/*      */     //   1764: invokevirtual getTileEntity : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
/*      */     //   1767: astore #27
/*      */     //   1769: aload #27
/*      */     //   1771: instanceof net/minecraft/tileentity/TileEntityChest
/*      */     //   1774: ifeq -> 1845
/*      */     //   1777: aload #27
/*      */     //   1779: checkcast net/minecraft/tileentity/TileEntityChest
/*      */     //   1782: astore #28
/*      */     //   1784: aload #28
/*      */     //   1786: getfield adjacentChestXNeg : Lnet/minecraft/tileentity/TileEntityChest;
/*      */     //   1789: ifnull -> 1816
/*      */     //   1792: aload #26
/*      */     //   1794: getstatic net/minecraft/util/EnumFacing.WEST : Lnet/minecraft/util/EnumFacing;
/*      */     //   1797: invokevirtual offset : (Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/BlockPos;
/*      */     //   1800: astore #26
/*      */     //   1802: aload_0
/*      */     //   1803: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   1806: aload #26
/*      */     //   1808: invokevirtual getTileEntity : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
/*      */     //   1811: astore #27
/*      */     //   1813: goto -> 1845
/*      */     //   1816: aload #28
/*      */     //   1818: getfield adjacentChestZNeg : Lnet/minecraft/tileentity/TileEntityChest;
/*      */     //   1821: ifnull -> 1845
/*      */     //   1824: aload #26
/*      */     //   1826: getstatic net/minecraft/util/EnumFacing.NORTH : Lnet/minecraft/util/EnumFacing;
/*      */     //   1829: invokevirtual offset : (Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/BlockPos;
/*      */     //   1832: astore #26
/*      */     //   1834: aload_0
/*      */     //   1835: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   1838: aload #26
/*      */     //   1840: invokevirtual getTileEntity : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
/*      */     //   1843: astore #27
/*      */     //   1845: aload_0
/*      */     //   1846: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
/*      */     //   1849: aload #26
/*      */     //   1851: invokevirtual getBlockState : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;
/*      */     //   1854: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
/*      */     //   1859: astore #28
/*      */     //   1861: iload #20
/*      */     //   1863: ifeq -> 1947
/*      */     //   1866: iconst_0
/*      */     //   1867: istore #29
/*      */     //   1869: aload #27
/*      */     //   1871: ifnull -> 1991
/*      */     //   1874: aload #27
/*      */     //   1876: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_shouldRenderInPass : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1879: iconst_1
/*      */     //   1880: anewarray java/lang/Object
/*      */     //   1883: dup
/*      */     //   1884: iconst_0
/*      */     //   1885: iload #4
/*      */     //   1887: invokestatic valueOf : (I)Ljava/lang/Integer;
/*      */     //   1890: aastore
/*      */     //   1891: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   1894: ifeq -> 1991
/*      */     //   1897: aload #27
/*      */     //   1899: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_canRenderBreaking : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1902: iconst_0
/*      */     //   1903: anewarray java/lang/Object
/*      */     //   1906: invokestatic callBoolean : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Z
/*      */     //   1909: ifeq -> 1991
/*      */     //   1912: aload #27
/*      */     //   1914: getstatic net/optifine/reflect/Reflector.ForgeTileEntity_getRenderBoundingBox : Lnet/optifine/reflect/ReflectorMethod;
/*      */     //   1917: iconst_0
/*      */     //   1918: anewarray java/lang/Object
/*      */     //   1921: invokestatic call : (Ljava/lang/Object;Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   1924: checkcast net/minecraft/util/AxisAlignedBB
/*      */     //   1927: astore #30
/*      */     //   1929: aload #30
/*      */     //   1931: ifnull -> 1944
/*      */     //   1934: aload_2
/*      */     //   1935: aload #30
/*      */     //   1937: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/AxisAlignedBB;)Z
/*      */     //   1942: istore #29
/*      */     //   1944: goto -> 1991
/*      */     //   1947: aload #27
/*      */     //   1949: ifnull -> 1988
/*      */     //   1952: aload #28
/*      */     //   1954: instanceof net/minecraft/block/BlockChest
/*      */     //   1957: ifne -> 1984
/*      */     //   1960: aload #28
/*      */     //   1962: instanceof net/minecraft/block/BlockEnderChest
/*      */     //   1965: ifne -> 1984
/*      */     //   1968: aload #28
/*      */     //   1970: instanceof net/minecraft/block/BlockSign
/*      */     //   1973: ifne -> 1984
/*      */     //   1976: aload #28
/*      */     //   1978: instanceof net/minecraft/block/BlockSkull
/*      */     //   1981: ifeq -> 1988
/*      */     //   1984: iconst_1
/*      */     //   1985: goto -> 1989
/*      */     //   1988: iconst_0
/*      */     //   1989: istore #29
/*      */     //   1991: iload #29
/*      */     //   1993: ifeq -> 2020
/*      */     //   1996: iload #21
/*      */     //   1998: ifeq -> 2006
/*      */     //   2001: aload #27
/*      */     //   2003: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
/*      */     //   2006: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
/*      */     //   2009: aload #27
/*      */     //   2011: fload_3
/*      */     //   2012: aload #25
/*      */     //   2014: invokevirtual getPartialBlockDamage : ()I
/*      */     //   2017: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
/*      */     //   2020: goto -> 1729
/*      */     //   2023: aload_0
/*      */     //   2024: invokespecial postRenderDamagedBlocks : ()V
/*      */     //   2027: aload_0
/*      */     //   2028: iconst_0
/*      */     //   2029: putfield renderOverlayDamaged : Z
/*      */     //   2032: iload #21
/*      */     //   2034: ifeq -> 2040
/*      */     //   2037: invokestatic endBlockEntities : ()V
/*      */     //   2040: getstatic net/minecraft/client/renderer/RenderGlobal.renderEntitiesCounter : I
/*      */     //   2043: iconst_1
/*      */     //   2044: isub
/*      */     //   2045: putstatic net/minecraft/client/renderer/RenderGlobal.renderEntitiesCounter : I
/*      */     //   2048: aload_0
/*      */     //   2049: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   2052: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
/*      */     //   2055: invokevirtual disableLightmap : ()V
/*      */     //   2058: aload_0
/*      */     //   2059: getfield mc : Lnet/minecraft/client/Minecraft;
/*      */     //   2062: getfield mcProfiler : Lnet/minecraft/profiler/Profiler;
/*      */     //   2065: invokevirtual endSection : ()V
/*      */     //   2068: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #614	-> 0
/*      */     //   #616	-> 3
/*      */     //   #618	-> 12
/*      */     //   #621	-> 24
/*      */     //   #623	-> 31
/*      */     //   #625	-> 36
/*      */     //   #628	-> 37
/*      */     //   #632	-> 50
/*      */     //   #633	-> 69
/*      */     //   #634	-> 88
/*      */     //   #635	-> 107
/*      */     //   #636	-> 119
/*      */     //   #637	-> 151
/*      */     //   #638	-> 191
/*      */     //   #640	-> 199
/*      */     //   #642	-> 204
/*      */     //   #643	-> 209
/*      */     //   #644	-> 214
/*      */     //   #645	-> 219
/*      */     //   #648	-> 224
/*      */     //   #649	-> 233
/*      */     //   #650	-> 255
/*      */     //   #651	-> 277
/*      */     //   #652	-> 299
/*      */     //   #653	-> 304
/*      */     //   #654	-> 309
/*      */     //   #655	-> 314
/*      */     //   #656	-> 327
/*      */     //   #657	-> 337
/*      */     //   #658	-> 350
/*      */     //   #660	-> 359
/*      */     //   #662	-> 364
/*      */     //   #665	-> 375
/*      */     //   #667	-> 394
/*      */     //   #670	-> 397
/*      */     //   #671	-> 405
/*      */     //   #673	-> 413
/*      */     //   #675	-> 433
/*      */     //   #677	-> 452
/*      */     //   #679	-> 480
/*      */     //   #681	-> 490
/*      */     //   #683	-> 504
/*      */     //   #673	-> 515
/*      */     //   #688	-> 521
/*      */     //   #690	-> 528
/*      */     //   #691	-> 534
/*      */     //   #692	-> 537
/*      */     //   #693	-> 544
/*      */     //   #694	-> 552
/*      */     //   #695	-> 565
/*      */     //   #696	-> 568
/*      */     //   #698	-> 576
/*      */     //   #700	-> 591
/*      */     //   #701	-> 605
/*      */     //   #702	-> 641
/*      */     //   #704	-> 707
/*      */     //   #706	-> 742
/*      */     //   #698	-> 753
/*      */     //   #710	-> 759
/*      */     //   #711	-> 767
/*      */     //   #712	-> 770
/*      */     //   #713	-> 774
/*      */     //   #714	-> 782
/*      */     //   #715	-> 785
/*      */     //   #716	-> 789
/*      */     //   #717	-> 800
/*      */     //   #718	-> 803
/*      */     //   #719	-> 806
/*      */     //   #720	-> 809
/*      */     //   #721	-> 815
/*      */     //   #722	-> 818
/*      */     //   #725	-> 821
/*      */     //   #726	-> 834
/*      */     //   #728	-> 839
/*      */     //   #730	-> 844
/*      */     //   #733	-> 847
/*      */     //   #734	-> 850
/*      */     //   #735	-> 862
/*      */     //   #736	-> 875
/*      */     //   #739	-> 901
/*      */     //   #741	-> 931
/*      */     //   #742	-> 938
/*      */     //   #743	-> 948
/*      */     //   #745	-> 970
/*      */     //   #747	-> 978
/*      */     //   #756	-> 985
/*      */     //   #758	-> 995
/*      */     //   #761	-> 998
/*      */     //   #763	-> 1010
/*      */     //   #765	-> 1038
/*      */     //   #767	-> 1079
/*      */     //   #769	-> 1084
/*      */     //   #772	-> 1087
/*      */     //   #774	-> 1119
/*      */     //   #776	-> 1195
/*      */     //   #777	-> 1205
/*      */     //   #779	-> 1211
/*      */     //   #781	-> 1216
/*      */     //   #784	-> 1221
/*      */     //   #785	-> 1232
/*      */     //   #786	-> 1237
/*      */     //   #788	-> 1240
/*      */     //   #791	-> 1243
/*      */     //   #793	-> 1284
/*      */     //   #795	-> 1290
/*      */     //   #797	-> 1295
/*      */     //   #800	-> 1300
/*      */     //   #801	-> 1313
/*      */     //   #803	-> 1318
/*      */     //   #805	-> 1321
/*      */     //   #807	-> 1324
/*      */     //   #809	-> 1336
/*      */     //   #811	-> 1341
/*      */     //   #812	-> 1344
/*      */     //   #815	-> 1347
/*      */     //   #816	-> 1360
/*      */     //   #818	-> 1363
/*      */     //   #820	-> 1372
/*      */     //   #823	-> 1378
/*      */     //   #826	-> 1381
/*      */     //   #828	-> 1411
/*      */     //   #829	-> 1418
/*      */     //   #831	-> 1431
/*      */     //   #833	-> 1441
/*      */     //   #841	-> 1450
/*      */     //   #843	-> 1460
/*      */     //   #846	-> 1463
/*      */     //   #848	-> 1475
/*      */     //   #850	-> 1480
/*      */     //   #853	-> 1483
/*      */     //   #855	-> 1506
/*      */     //   #857	-> 1523
/*      */     //   #859	-> 1539
/*      */     //   #861	-> 1542
/*      */     //   #864	-> 1545
/*      */     //   #866	-> 1550
/*      */     //   #869	-> 1555
/*      */     //   #870	-> 1565
/*      */     //   #871	-> 1575
/*      */     //   #873	-> 1578
/*      */     //   #875	-> 1581
/*      */     //   #877	-> 1589
/*      */     //   #879	-> 1622
/*      */     //   #881	-> 1650
/*      */     //   #883	-> 1655
/*      */     //   #886	-> 1660
/*      */     //   #888	-> 1670
/*      */     //   #889	-> 1673
/*      */     //   #891	-> 1687
/*      */     //   #893	-> 1696
/*      */     //   #896	-> 1704
/*      */     //   #897	-> 1709
/*      */     //   #899	-> 1713
/*      */     //   #901	-> 1751
/*      */     //   #902	-> 1758
/*      */     //   #904	-> 1769
/*      */     //   #906	-> 1777
/*      */     //   #908	-> 1784
/*      */     //   #910	-> 1792
/*      */     //   #911	-> 1802
/*      */     //   #913	-> 1816
/*      */     //   #915	-> 1824
/*      */     //   #916	-> 1834
/*      */     //   #920	-> 1845
/*      */     //   #923	-> 1861
/*      */     //   #925	-> 1866
/*      */     //   #927	-> 1869
/*      */     //   #929	-> 1912
/*      */     //   #931	-> 1929
/*      */     //   #933	-> 1934
/*      */     //   #935	-> 1944
/*      */     //   #939	-> 1947
/*      */     //   #942	-> 1991
/*      */     //   #944	-> 1996
/*      */     //   #946	-> 2001
/*      */     //   #949	-> 2006
/*      */     //   #951	-> 2020
/*      */     //   #953	-> 2023
/*      */     //   #954	-> 2027
/*      */     //   #956	-> 2032
/*      */     //   #958	-> 2037
/*      */     //   #961	-> 2040
/*      */     //   #962	-> 2048
/*      */     //   #963	-> 2058
/*      */     //   #965	-> 2068
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   452	63	22	entity1	Lnet/minecraft/entity/Entity;
/*      */     //   416	105	21	j	I
/*      */     //   605	148	22	entity3	Lnet/minecraft/entity/Entity;
/*      */     //   641	112	23	flag2	Z
/*      */     //   707	46	24	flag3	Z
/*      */     //   579	180	21	k	I
/*      */     //   1119	121	32	flag5	Z
/*      */     //   1010	308	30	entity2	Lnet/minecraft/entity/Entity;
/*      */     //   1079	239	31	flag4	Z
/*      */     //   985	336	29	iterator	Ljava/util/Iterator;
/*      */     //   938	383	26	renderglobal$containerlocalrenderinformation	Lnet/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation;
/*      */     //   948	373	27	chunk	Lnet/minecraft/world/chunk/Chunk;
/*      */     //   970	351	28	classinheritancemultimap	Lnet/minecraft/util/ClassInheritanceMultiMap;
/*      */     //   931	390	25	o	Ljava/lang/Object;
/*      */     //   1523	19	30	axisalignedbb1	Lnet/minecraft/util/AxisAlignedBB;
/*      */     //   1475	100	29	tileentity1	Lnet/minecraft/tileentity/TileEntity;
/*      */     //   1450	128	28	iterator1	Ljava/util/Iterator;
/*      */     //   1418	160	26	renderglobal$containerlocalrenderinformation1	Lnet/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation;
/*      */     //   1431	147	27	list1	Ljava/util/List;
/*      */     //   1411	167	25	o	Ljava/lang/Object;
/*      */     //   1622	48	26	tileentity	Lnet/minecraft/tileentity/TileEntity;
/*      */     //   1784	61	28	tileentitychest	Lnet/minecraft/tileentity/TileEntityChest;
/*      */     //   1929	15	30	axisalignedbb	Lnet/minecraft/util/AxisAlignedBB;
/*      */     //   1869	78	29	flag9	Z
/*      */     //   1758	262	26	blockpos	Lnet/minecraft/util/BlockPos;
/*      */     //   1769	251	27	tileentity2	Lnet/minecraft/tileentity/TileEntity;
/*      */     //   1861	159	28	block	Lnet/minecraft/block/Block;
/*      */     //   1991	29	29	flag9	Z
/*      */     //   1751	269	25	destroyblockprogress	Lnet/minecraft/client/renderer/DestroyBlockProgress;
/*      */     //   69	1999	5	d0	D
/*      */     //   88	1980	7	d1	D
/*      */     //   107	1961	9	d2	D
/*      */     //   233	1835	11	entity	Lnet/minecraft/entity/Entity;
/*      */     //   255	1813	12	d3	D
/*      */     //   277	1791	14	d4	D
/*      */     //   299	1769	16	d5	D
/*      */     //   359	1709	18	list	Ljava/util/List;
/*      */     //   405	1663	19	flag	Z
/*      */     //   413	1655	20	flag1	Z
/*      */     //   839	1229	21	flag6	Z
/*      */     //   862	1206	22	flag7	Z
/*      */     //   901	1167	23	flag8	Z
/*      */     //   0	2069	0	this	Lnet/minecraft/client/renderer/RenderGlobal;
/*      */     //   0	2069	1	renderViewEntity	Lnet/minecraft/entity/Entity;
/*      */     //   0	2069	2	camera	Lnet/minecraft/client/renderer/culling/ICamera;
/*      */     //   0	2069	3	partialTicks	F
/*      */     //   3	2066	4	i	I
/*      */     // Local variable type table:
/*      */     //   start	length	slot	name	signature
/*      */     //   970	351	28	classinheritancemultimap	Lnet/minecraft/util/ClassInheritanceMultiMap<Lnet/minecraft/entity/Entity;>;
/*      */     //   1431	147	27	list1	Ljava/util/List<Lnet/minecraft/tileentity/TileEntity;>;
/*      */     //   359	1709	18	list	Ljava/util/List<Lnet/minecraft/entity/Entity;>;
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   1589	1676	1679	finally
/*      */     //   1679	1684	1679	finally
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDebugInfoRenders() {
/*  969 */     int i = this.viewFrustum.renderChunks.length;
/*  970 */     int j = 0;
/*      */     
/*  972 */     for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
/*      */       
/*  974 */       CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
/*      */       
/*  976 */       if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty())
/*      */       {
/*  978 */         j++;
/*      */       }
/*      */     } 
/*      */     
/*  982 */     return String.format("C: %d/%d %sD: %d, %s", new Object[] { Integer.valueOf(j), Integer.valueOf(i), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo() });
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDebugInfoEntities() {
/*  987 */     return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
/*      */   }
/*      */   
/*      */   public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
/*      */     Frustum frustum;
/*  992 */     if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
/*      */     {
/*  994 */       loadRenderers();
/*      */     }
/*      */     
/*  997 */     this.theWorld.theProfiler.startSection("camera");
/*  998 */     double d0 = viewEntity.posX - this.frustumUpdatePosX;
/*  999 */     double d1 = viewEntity.posY - this.frustumUpdatePosY;
/* 1000 */     double d2 = viewEntity.posZ - this.frustumUpdatePosZ;
/*      */     
/* 1002 */     if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0D) {
/*      */       
/* 1004 */       this.frustumUpdatePosX = viewEntity.posX;
/* 1005 */       this.frustumUpdatePosY = viewEntity.posY;
/* 1006 */       this.frustumUpdatePosZ = viewEntity.posZ;
/* 1007 */       this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
/* 1008 */       this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
/* 1009 */       this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
/* 1010 */       this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
/*      */     } 
/*      */     
/* 1013 */     if (Config.isDynamicLights())
/*      */     {
/* 1015 */       DynamicLights.update(this);
/*      */     }
/*      */     
/* 1018 */     this.theWorld.theProfiler.endStartSection("renderlistcamera");
/* 1019 */     double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
/* 1020 */     double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
/* 1021 */     double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
/* 1022 */     this.renderContainer.initialize(d3, d4, d5);
/* 1023 */     this.theWorld.theProfiler.endStartSection("cull");
/*      */     
/* 1025 */     if (this.debugFixedClippingHelper != null) {
/*      */       
/* 1027 */       Frustum frustum1 = new Frustum(this.debugFixedClippingHelper);
/* 1028 */       frustum1.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
/* 1029 */       frustum = frustum1;
/*      */     } 
/*      */     
/* 1032 */     this.mc.mcProfiler.endStartSection("culling");
/* 1033 */     BlockPos blockpos = new BlockPos(d3, d4 + viewEntity.getEyeHeight(), d5);
/* 1034 */     RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos);
/* 1035 */     new BlockPos(MathHelper.floor_double(d3 / 16.0D) * 16, MathHelper.floor_double(d4 / 16.0D) * 16, MathHelper.floor_double(d5 / 16.0D) * 16);
/* 1036 */     this.displayListEntitiesDirty = (this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || viewEntity.rotationPitch != this.lastViewEntityPitch || viewEntity.rotationYaw != this.lastViewEntityYaw);
/* 1037 */     this.lastViewEntityX = viewEntity.posX;
/* 1038 */     this.lastViewEntityY = viewEntity.posY;
/* 1039 */     this.lastViewEntityZ = viewEntity.posZ;
/* 1040 */     this.lastViewEntityPitch = viewEntity.rotationPitch;
/* 1041 */     this.lastViewEntityYaw = viewEntity.rotationYaw;
/* 1042 */     boolean flag = (this.debugFixedClippingHelper != null);
/* 1043 */     this.mc.mcProfiler.endStartSection("update");
/* 1044 */     Lagometer.timerVisibility.start();
/* 1045 */     int i = getCountLoadedChunks();
/*      */     
/* 1047 */     if (i != this.countLoadedChunksPrev) {
/*      */       
/* 1049 */       this.countLoadedChunksPrev = i;
/* 1050 */       this.displayListEntitiesDirty = true;
/*      */     } 
/*      */     
/* 1053 */     int j = 256;
/*      */     
/* 1055 */     if (!ChunkVisibility.isFinished())
/*      */     {
/* 1057 */       this.displayListEntitiesDirty = true;
/*      */     }
/*      */     
/* 1060 */     if (!flag && this.displayListEntitiesDirty && Config.isIntegratedServerRunning())
/*      */     {
/* 1062 */       j = ChunkVisibility.getMaxChunkY((World)this.theWorld, viewEntity, this.renderDistanceChunks);
/*      */     }
/*      */     
/* 1065 */     RenderChunk renderchunk1 = this.viewFrustum.getRenderChunk(new BlockPos(viewEntity.posX, viewEntity.posY, viewEntity.posZ));
/*      */     
/* 1067 */     if (Shaders.isShadowPass) {
/*      */       
/* 1069 */       this.renderInfos = this.renderInfosShadow;
/* 1070 */       this.renderInfosEntities = this.renderInfosEntitiesShadow;
/* 1071 */       this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
/*      */       
/* 1073 */       if (!flag && this.displayListEntitiesDirty) {
/*      */         
/* 1075 */         clearRenderInfos();
/*      */         
/* 1077 */         if (renderchunk1 != null && renderchunk1.getPosition().getY() > j)
/*      */         {
/* 1079 */           this.renderInfosEntities.add(renderchunk1.getRenderInfo());
/*      */         }
/*      */         
/* 1082 */         Iterator<RenderChunk> iterator = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, viewEntity, this.renderDistanceChunks, this.viewFrustum);
/*      */         
/* 1084 */         while (iterator.hasNext()) {
/*      */           
/* 1086 */           RenderChunk renderchunk2 = iterator.next();
/*      */           
/* 1088 */           if (renderchunk2 != null && renderchunk2.getPosition().getY() <= j)
/*      */           {
/* 1090 */             ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = renderchunk2.getRenderInfo();
/*      */             
/* 1092 */             if (!renderchunk2.compiledChunk.isEmpty() || renderchunk2.isNeedsUpdate())
/*      */             {
/* 1094 */               this.renderInfos.add(renderglobal$containerlocalrenderinformation);
/*      */             }
/*      */             
/* 1097 */             if (ChunkUtils.hasEntities(renderchunk2.getChunk()))
/*      */             {
/* 1099 */               this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation);
/*      */             }
/*      */             
/* 1102 */             if (renderchunk2.getCompiledChunk().getTileEntities().size() > 0)
/*      */             {
/* 1104 */               this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation);
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1112 */       this.renderInfos = this.renderInfosNormal;
/* 1113 */       this.renderInfosEntities = this.renderInfosEntitiesNormal;
/* 1114 */       this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
/*      */     } 
/*      */     
/* 1117 */     if (!flag && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
/*      */       
/* 1119 */       this.displayListEntitiesDirty = false;
/* 1120 */       clearRenderInfos();
/* 1121 */       this.visibilityDeque.clear();
/* 1122 */       Deque<ContainerLocalRenderInformation> deque = this.visibilityDeque;
/* 1123 */       boolean flag1 = this.mc.renderChunksMany;
/*      */       
/* 1125 */       if (renderchunk != null && renderchunk.getPosition().getY() <= j) {
/*      */         
/* 1127 */         boolean flag2 = false;
/* 1128 */         ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation4 = new ContainerLocalRenderInformation(renderchunk, (EnumFacing)null, 0);
/* 1129 */         Set set1 = SET_ALL_FACINGS;
/*      */         
/* 1131 */         if (set1.size() == 1) {
/*      */           
/* 1133 */           Vector3f vector3f = getViewVector(viewEntity, partialTicks);
/* 1134 */           EnumFacing enumfacing2 = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
/* 1135 */           set1.remove(enumfacing2);
/*      */         } 
/*      */         
/* 1138 */         if (set1.isEmpty())
/*      */         {
/* 1140 */           flag2 = true;
/*      */         }
/*      */         
/* 1143 */         if (flag2 && !playerSpectator)
/*      */         {
/* 1145 */           this.renderInfos.add(renderglobal$containerlocalrenderinformation4);
/*      */         }
/*      */         else
/*      */         {
/* 1149 */           if (playerSpectator && this.theWorld.getBlockState(blockpos).getBlock().isOpaqueCube())
/*      */           {
/* 1151 */             flag1 = false;
/*      */           }
/*      */           
/* 1154 */           renderchunk.setFrameIndex(frameCount);
/* 1155 */           deque.add(renderglobal$containerlocalrenderinformation4);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1160 */         int j1 = (blockpos.getY() > 0) ? Math.min(j, 248) : 8;
/*      */         
/* 1162 */         if (renderchunk1 != null)
/*      */         {
/* 1164 */           this.renderInfosEntities.add(renderchunk1.getRenderInfo());
/*      */         }
/*      */         
/* 1167 */         for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; k++) {
/*      */           
/* 1169 */           for (int l = -this.renderDistanceChunks; l <= this.renderDistanceChunks; l++) {
/*      */             
/* 1171 */             RenderChunk renderchunk3 = this.viewFrustum.getRenderChunk(new BlockPos((k << 4) + 8, j1, (l << 4) + 8));
/*      */             
/* 1173 */             if (renderchunk3 != null && renderchunk3.isBoundingBoxInFrustum((ICamera)frustum, frameCount)) {
/*      */               
/* 1175 */               renderchunk3.setFrameIndex(frameCount);
/* 1176 */               ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 = renderchunk3.getRenderInfo();
/* 1177 */               renderglobal$containerlocalrenderinformation1.initialize((EnumFacing)null, 0);
/* 1178 */               deque.add(renderglobal$containerlocalrenderinformation1);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1184 */       this.mc.mcProfiler.startSection("iteration");
/* 1185 */       boolean flag3 = Config.isFogOn();
/*      */       
/* 1187 */       while (!deque.isEmpty()) {
/*      */         
/* 1189 */         ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation5 = deque.poll();
/* 1190 */         RenderChunk renderchunk6 = renderglobal$containerlocalrenderinformation5.renderChunk;
/* 1191 */         EnumFacing enumfacing1 = renderglobal$containerlocalrenderinformation5.facing;
/* 1192 */         CompiledChunk compiledchunk = renderchunk6.compiledChunk;
/*      */         
/* 1194 */         if (!compiledchunk.isEmpty() || renderchunk6.isNeedsUpdate())
/*      */         {
/* 1196 */           this.renderInfos.add(renderglobal$containerlocalrenderinformation5);
/*      */         }
/*      */         
/* 1199 */         if (ChunkUtils.hasEntities(renderchunk6.getChunk()))
/*      */         {
/* 1201 */           this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation5);
/*      */         }
/*      */         
/* 1204 */         if (compiledchunk.getTileEntities().size() > 0)
/*      */         {
/* 1206 */           this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation5);
/*      */         }
/*      */         
/* 1209 */         for (EnumFacing enumfacing : flag1 ? ChunkVisibility.getFacingsNotOpposite(renderglobal$containerlocalrenderinformation5.setFacing) : EnumFacing.VALUES) {
/*      */           
/* 1211 */           if (!flag1 || enumfacing1 == null || compiledchunk.isVisible(enumfacing1.getOpposite(), enumfacing)) {
/*      */             
/* 1213 */             RenderChunk renderchunk4 = getRenderChunkOffset(blockpos, renderchunk6, enumfacing, flag3, j);
/*      */             
/* 1215 */             if (renderchunk4 != null && renderchunk4.setFrameIndex(frameCount) && renderchunk4.isBoundingBoxInFrustum((ICamera)frustum, frameCount)) {
/*      */               
/* 1217 */               int i1 = renderglobal$containerlocalrenderinformation5.setFacing | 1 << enumfacing.ordinal();
/* 1218 */               ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 = renderchunk4.getRenderInfo();
/* 1219 */               renderglobal$containerlocalrenderinformation2.initialize(enumfacing, i1);
/* 1220 */               deque.add(renderglobal$containerlocalrenderinformation2);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1226 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */     
/* 1229 */     this.mc.mcProfiler.endStartSection("captureFrustum");
/*      */     
/* 1231 */     if (this.debugFixTerrainFrustum) {
/*      */       
/* 1233 */       fixTerrainFrustum(d3, d4, d5);
/* 1234 */       this.debugFixTerrainFrustum = false;
/*      */     } 
/*      */     
/* 1237 */     Lagometer.timerVisibility.end();
/*      */     
/* 1239 */     if (Shaders.isShadowPass) {
/*      */       
/* 1241 */       Shaders.mcProfilerEndSection();
/*      */     }
/*      */     else {
/*      */       
/* 1245 */       this.mc.mcProfiler.endStartSection("rebuildNear");
/* 1246 */       this.renderDispatcher.clearChunkUpdates();
/* 1247 */       Set<RenderChunk> set = this.chunksToUpdate;
/* 1248 */       this.chunksToUpdate = Sets.newLinkedHashSet();
/* 1249 */       Lagometer.timerChunkUpdate.start();
/*      */       
/* 1251 */       for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 : this.renderInfos) {
/*      */         
/* 1253 */         RenderChunk renderchunk5 = renderglobal$containerlocalrenderinformation3.renderChunk;
/*      */         
/* 1255 */         if (renderchunk5.isNeedsUpdate() || set.contains(renderchunk5)) {
/*      */           
/* 1257 */           this.displayListEntitiesDirty = true;
/* 1258 */           BlockPos blockpos1 = renderchunk5.getPosition();
/* 1259 */           boolean flag4 = (blockpos.distanceSq((blockpos1.getX() + 8), (blockpos1.getY() + 8), (blockpos1.getZ() + 8)) < 768.0D);
/*      */           
/* 1261 */           if (!flag4) {
/*      */             
/* 1263 */             this.chunksToUpdate.add(renderchunk5); continue;
/*      */           } 
/* 1265 */           if (!renderchunk5.isPlayerUpdate()) {
/*      */             
/* 1267 */             this.chunksToUpdateForced.add(renderchunk5);
/*      */             
/*      */             continue;
/*      */           } 
/* 1271 */           this.mc.mcProfiler.startSection("build near");
/* 1272 */           this.renderDispatcher.updateChunkNow(renderchunk5);
/* 1273 */           renderchunk5.setNeedsUpdate(false);
/* 1274 */           this.mc.mcProfiler.endSection();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1279 */       Lagometer.timerChunkUpdate.end();
/* 1280 */       this.chunksToUpdate.addAll(set);
/* 1281 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isPositionInRenderChunk(BlockPos pos, RenderChunk renderChunkIn) {
/* 1287 */     BlockPos blockpos = renderChunkIn.getPosition();
/* 1288 */     return (MathHelper.abs_int(pos.getX() - blockpos.getX()) > 16) ? false : ((MathHelper.abs_int(pos.getY() - blockpos.getY()) > 16) ? false : ((MathHelper.abs_int(pos.getZ() - blockpos.getZ()) <= 16)));
/*      */   }
/*      */ 
/*      */   
/*      */   private Set<EnumFacing> getVisibleFacings(BlockPos pos) {
/* 1293 */     VisGraph visgraph = new VisGraph();
/* 1294 */     BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
/* 1295 */     Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
/*      */     
/* 1297 */     for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15))) {
/*      */       
/* 1299 */       if (chunk.getBlock((BlockPos)blockpos$mutableblockpos).isOpaqueCube())
/*      */       {
/* 1301 */         visgraph.func_178606_a((BlockPos)blockpos$mutableblockpos);
/*      */       }
/*      */     } 
/* 1304 */     return visgraph.func_178609_b(pos);
/*      */   }
/*      */ 
/*      */   
/*      */   private RenderChunk getRenderChunkOffset(BlockPos p_getRenderChunkOffset_1_, RenderChunk p_getRenderChunkOffset_2_, EnumFacing p_getRenderChunkOffset_3_, boolean p_getRenderChunkOffset_4_, int p_getRenderChunkOffset_5_) {
/* 1309 */     RenderChunk renderchunk = p_getRenderChunkOffset_2_.getRenderChunkNeighbour(p_getRenderChunkOffset_3_);
/*      */     
/* 1311 */     if (renderchunk == null)
/*      */     {
/* 1313 */       return null;
/*      */     }
/* 1315 */     if (renderchunk.getPosition().getY() > p_getRenderChunkOffset_5_)
/*      */     {
/* 1317 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1321 */     if (p_getRenderChunkOffset_4_) {
/*      */       
/* 1323 */       BlockPos blockpos = renderchunk.getPosition();
/* 1324 */       int i = p_getRenderChunkOffset_1_.getX() - blockpos.getX();
/* 1325 */       int j = p_getRenderChunkOffset_1_.getZ() - blockpos.getZ();
/* 1326 */       int k = i * i + j * j;
/*      */       
/* 1328 */       if (k > this.renderDistanceSq)
/*      */       {
/* 1330 */         return null;
/*      */       }
/*      */     } 
/*      */     
/* 1334 */     return renderchunk;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void fixTerrainFrustum(double x, double y, double z) {
/* 1340 */     this.debugFixedClippingHelper = (ClippingHelper)new ClippingHelperImpl();
/* 1341 */     ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
/* 1342 */     Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
/* 1343 */     matrix4f.transpose();
/* 1344 */     Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
/* 1345 */     matrix4f1.transpose();
/* 1346 */     Matrix4f matrix4f2 = new Matrix4f();
/* 1347 */     Matrix4f.mul((Matrix4f)matrix4f1, (Matrix4f)matrix4f, (Matrix4f)matrix4f2);
/* 1348 */     matrix4f2.invert();
/* 1349 */     this.debugTerrainFrustumPosition.x = x;
/* 1350 */     this.debugTerrainFrustumPosition.y = y;
/* 1351 */     this.debugTerrainFrustumPosition.z = z;
/* 1352 */     this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
/* 1353 */     this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
/* 1354 */     this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
/* 1355 */     this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
/* 1356 */     this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
/* 1357 */     this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
/* 1358 */     this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 1359 */     this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
/*      */     
/* 1361 */     for (int i = 0; i < 8; i++) {
/*      */       
/* 1363 */       Matrix4f.transform((Matrix4f)matrix4f2, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
/* 1364 */       (this.debugTerrainMatrix[i]).x /= (this.debugTerrainMatrix[i]).w;
/* 1365 */       (this.debugTerrainMatrix[i]).y /= (this.debugTerrainMatrix[i]).w;
/* 1366 */       (this.debugTerrainMatrix[i]).z /= (this.debugTerrainMatrix[i]).w;
/* 1367 */       (this.debugTerrainMatrix[i]).w = 1.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected Vector3f getViewVector(Entity entityIn, double partialTicks) {
/* 1373 */     float f = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
/* 1374 */     float f1 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
/*      */     
/* 1376 */     if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView == 2)
/*      */     {
/* 1378 */       f += 180.0F;
/*      */     }
/*      */     
/* 1381 */     float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
/* 1382 */     float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
/* 1383 */     float f4 = -MathHelper.cos(-f * 0.017453292F);
/* 1384 */     float f5 = MathHelper.sin(-f * 0.017453292F);
/* 1385 */     return new Vector3f(f3 * f4, f5, f2 * f4);
/*      */   }
/*      */ 
/*      */   
/*      */   public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
/* 1390 */     RenderHelper.disableStandardItemLighting();
/*      */     
/* 1392 */     if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT && !Shaders.isShadowPass) {
/*      */       
/* 1394 */       this.mc.mcProfiler.startSection("translucent_sort");
/* 1395 */       double d0 = entityIn.posX - this.prevRenderSortX;
/* 1396 */       double d1 = entityIn.posY - this.prevRenderSortY;
/* 1397 */       double d2 = entityIn.posZ - this.prevRenderSortZ;
/*      */       
/* 1399 */       if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D) {
/*      */         
/* 1401 */         this.prevRenderSortX = entityIn.posX;
/* 1402 */         this.prevRenderSortY = entityIn.posY;
/* 1403 */         this.prevRenderSortZ = entityIn.posZ;
/* 1404 */         int k = 0;
/* 1405 */         this.chunksToResortTransparency.clear();
/*      */         
/* 1407 */         for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
/*      */           
/* 1409 */           if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && k++ < 15)
/*      */           {
/* 1411 */             this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk);
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1416 */       this.mc.mcProfiler.endSection();
/*      */     } 
/*      */     
/* 1419 */     this.mc.mcProfiler.startSection("filterempty");
/* 1420 */     int l = 0;
/* 1421 */     boolean flag = (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT);
/* 1422 */     int i1 = flag ? (this.renderInfos.size() - 1) : 0;
/* 1423 */     int i = flag ? -1 : this.renderInfos.size();
/* 1424 */     int j1 = flag ? -1 : 1;
/*      */     int j;
/* 1426 */     for (j = i1; j != i; j += j1) {
/*      */       
/* 1428 */       RenderChunk renderchunk = ((ContainerLocalRenderInformation)this.renderInfos.get(j)).renderChunk;
/*      */       
/* 1430 */       if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
/*      */         
/* 1432 */         l++;
/* 1433 */         this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
/*      */       } 
/*      */     } 
/*      */     
/* 1437 */     if (l == 0) {
/*      */       
/* 1439 */       this.mc.mcProfiler.endSection();
/* 1440 */       return l;
/*      */     } 
/*      */ 
/*      */     
/* 1444 */     if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
/*      */     {
/* 1446 */       GlStateManager.disableFog();
/*      */     }
/*      */     
/* 1449 */     this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
/* 1450 */     renderBlockLayer(blockLayerIn);
/* 1451 */     this.mc.mcProfiler.endSection();
/* 1452 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn) {
/* 1459 */     this.mc.entityRenderer.enableLightmap();
/*      */     
/* 1461 */     if (OpenGlHelper.useVbo()) {
/*      */       
/* 1463 */       GL11.glEnableClientState(32884);
/* 1464 */       OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/* 1465 */       GL11.glEnableClientState(32888);
/* 1466 */       OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 1467 */       GL11.glEnableClientState(32888);
/* 1468 */       OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/* 1469 */       GL11.glEnableClientState(32886);
/*      */     } 
/*      */     
/* 1472 */     if (Config.isShaders())
/*      */     {
/* 1474 */       ShadersRender.preRenderChunkLayer(blockLayerIn);
/*      */     }
/*      */     
/* 1477 */     this.renderContainer.renderChunkLayer(blockLayerIn);
/*      */     
/* 1479 */     if (Config.isShaders())
/*      */     {
/* 1481 */       ShadersRender.postRenderChunkLayer(blockLayerIn);
/*      */     }
/*      */     
/* 1484 */     if (OpenGlHelper.useVbo())
/*      */     {
/* 1486 */       for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
/*      */         
/* 1488 */         VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
/* 1489 */         int i = vertexformatelement.getIndex();
/*      */         
/* 1491 */         switch (vertexformatelement$enumusage) {
/*      */           
/*      */           case POSITION:
/* 1494 */             GL11.glDisableClientState(32884);
/*      */ 
/*      */           
/*      */           case UV:
/* 1498 */             OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
/* 1499 */             GL11.glDisableClientState(32888);
/* 1500 */             OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
/*      */ 
/*      */           
/*      */           case COLOR:
/* 1504 */             GL11.glDisableClientState(32886);
/* 1505 */             GlStateManager.resetColor();
/*      */         } 
/*      */       
/*      */       } 
/*      */     }
/* 1510 */     this.mc.entityRenderer.disableLightmap();
/*      */   }
/*      */ 
/*      */   
/*      */   private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iteratorIn) {
/* 1515 */     while (iteratorIn.hasNext()) {
/*      */       
/* 1517 */       DestroyBlockProgress destroyblockprogress = iteratorIn.next();
/* 1518 */       int i = destroyblockprogress.getCreationCloudUpdateTick();
/*      */       
/* 1520 */       if (this.cloudTickCounter - i > 400)
/*      */       {
/* 1522 */         iteratorIn.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClouds() {
/* 1529 */     if (Config.isShaders()) {
/*      */       
/* 1531 */       if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(24)) {
/*      */         
/* 1533 */         GuiShaderOptions guishaderoptions = new GuiShaderOptions((GuiScreen)null, Config.getGameSettings());
/* 1534 */         Config.getMinecraft().displayGuiScreen((GuiScreen)guishaderoptions);
/*      */       } 
/*      */       
/* 1537 */       if (Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
/*      */         
/* 1539 */         Shaders.uninit();
/* 1540 */         Shaders.loadShaderPack();
/*      */       } 
/*      */     } 
/*      */     
/* 1544 */     this.cloudTickCounter++;
/*      */     
/* 1546 */     if (this.cloudTickCounter % 20 == 0)
/*      */     {
/* 1548 */       cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderSkyEnd() {
/* 1554 */     if (Config.isSkyEnabled()) {
/*      */       
/* 1556 */       GlStateManager.disableFog();
/* 1557 */       GlStateManager.disableAlpha();
/* 1558 */       GlStateManager.enableBlend();
/* 1559 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1560 */       RenderHelper.disableStandardItemLighting();
/* 1561 */       GlStateManager.depthMask(false);
/* 1562 */       this.renderEngine.bindTexture(locationEndSkyPng);
/* 1563 */       Tessellator tessellator = Tessellator.getInstance();
/* 1564 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*      */       
/* 1566 */       for (int i = 0; i < 6; i++) {
/*      */         
/* 1568 */         GlStateManager.pushMatrix();
/*      */         
/* 1570 */         if (i == 1)
/*      */         {
/* 1572 */           GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/*      */         }
/*      */         
/* 1575 */         if (i == 2)
/*      */         {
/* 1577 */           GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/*      */         }
/*      */         
/* 1580 */         if (i == 3)
/*      */         {
/* 1582 */           GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
/*      */         }
/*      */         
/* 1585 */         if (i == 4)
/*      */         {
/* 1587 */           GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/*      */         }
/*      */         
/* 1590 */         if (i == 5)
/*      */         {
/* 1592 */           GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
/*      */         }
/*      */         
/* 1595 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 1596 */         int j = 40;
/* 1597 */         int k = 40;
/* 1598 */         int l = 40;
/*      */         
/* 1600 */         if (Config.isCustomColors()) {
/*      */           
/* 1602 */           Vec3 vec3 = new Vec3(j / 255.0D, k / 255.0D, l / 255.0D);
/* 1603 */           vec3 = CustomColors.getWorldSkyColor(vec3, (World)this.theWorld, this.mc.getRenderViewEntity(), 0.0F);
/* 1604 */           j = (int)(vec3.xCoord * 255.0D);
/* 1605 */           k = (int)(vec3.yCoord * 255.0D);
/* 1606 */           l = (int)(vec3.zCoord * 255.0D);
/*      */         } 
/*      */         
/* 1609 */         worldrenderer.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(j, k, l, 255).endVertex();
/* 1610 */         worldrenderer.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(j, k, l, 255).endVertex();
/* 1611 */         worldrenderer.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(j, k, l, 255).endVertex();
/* 1612 */         worldrenderer.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(j, k, l, 255).endVertex();
/* 1613 */         tessellator.draw();
/* 1614 */         GlStateManager.popMatrix();
/*      */       } 
/*      */       
/* 1617 */       GlStateManager.depthMask(true);
/* 1618 */       GlStateManager.enableTexture2D();
/* 1619 */       GlStateManager.enableAlpha();
/* 1620 */       GlStateManager.disableBlend();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderSky(float partialTicks, int pass) {
/* 1626 */     if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
/*      */       
/* 1628 */       WorldProvider worldprovider = this.mc.theWorld.provider;
/* 1629 */       Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
/*      */       
/* 1631 */       if (object != null) {
/*      */         
/* 1633 */         Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1638 */     if (this.mc.theWorld.provider.getDimensionId() == 1) {
/*      */       
/* 1640 */       renderSkyEnd();
/*      */     }
/* 1642 */     else if (this.mc.theWorld.provider.isSurfaceWorld()) {
/*      */       
/* 1644 */       GlStateManager.disableTexture2D();
/* 1645 */       boolean flag = Config.isShaders();
/*      */       
/* 1647 */       if (flag)
/*      */       {
/* 1649 */         Shaders.disableTexture2D();
/*      */       }
/*      */       
/* 1652 */       Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
/* 1653 */       vec3 = CustomColors.getSkyColor(vec3, (IBlockAccess)this.mc.theWorld, (this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity()).posY + 1.0D, (this.mc.getRenderViewEntity()).posZ);
/*      */       
/* 1655 */       if (flag)
/*      */       {
/* 1657 */         Shaders.setSkyColor(vec3);
/*      */       }
/*      */       
/* 1660 */       float f = (float)vec3.xCoord;
/* 1661 */       float f1 = (float)vec3.yCoord;
/* 1662 */       float f2 = (float)vec3.zCoord;
/*      */       
/* 1664 */       if (pass != 2) {
/*      */         
/* 1666 */         float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
/* 1667 */         float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
/* 1668 */         float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
/* 1669 */         f = f3;
/* 1670 */         f1 = f4;
/* 1671 */         f2 = f5;
/*      */       } 
/*      */       
/* 1674 */       GlStateManager.color(f, f1, f2);
/* 1675 */       Tessellator tessellator = Tessellator.getInstance();
/* 1676 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 1677 */       GlStateManager.depthMask(false);
/* 1678 */       GlStateManager.enableFog();
/*      */       
/* 1680 */       if (flag)
/*      */       {
/* 1682 */         Shaders.enableFog();
/*      */       }
/*      */       
/* 1685 */       GlStateManager.color(f, f1, f2);
/*      */       
/* 1687 */       if (flag)
/*      */       {
/* 1689 */         Shaders.preSkyList();
/*      */       }
/*      */       
/* 1692 */       if (Config.isSkyEnabled())
/*      */       {
/* 1694 */         if (this.vboEnabled) {
/*      */           
/* 1696 */           this.skyVBO.bindBuffer();
/* 1697 */           GL11.glEnableClientState(32884);
/* 1698 */           GL11.glVertexPointer(3, 5126, 12, 0L);
/* 1699 */           this.skyVBO.drawArrays(7);
/* 1700 */           this.skyVBO.unbindBuffer();
/* 1701 */           GL11.glDisableClientState(32884);
/*      */         }
/*      */         else {
/*      */           
/* 1705 */           GlStateManager.callList(this.glSkyList);
/*      */         } 
/*      */       }
/*      */       
/* 1709 */       GlStateManager.disableFog();
/*      */       
/* 1711 */       if (flag)
/*      */       {
/* 1713 */         Shaders.disableFog();
/*      */       }
/*      */       
/* 1716 */       GlStateManager.disableAlpha();
/* 1717 */       GlStateManager.enableBlend();
/* 1718 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 1719 */       RenderHelper.disableStandardItemLighting();
/* 1720 */       float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
/*      */       
/* 1722 */       if (afloat != null && Config.isSunMoonEnabled()) {
/*      */         
/* 1724 */         GlStateManager.disableTexture2D();
/*      */         
/* 1726 */         if (flag)
/*      */         {
/* 1728 */           Shaders.disableTexture2D();
/*      */         }
/*      */         
/* 1731 */         GlStateManager.shadeModel(7425);
/* 1732 */         GlStateManager.pushMatrix();
/* 1733 */         GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
/* 1734 */         GlStateManager.rotate((MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F) ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
/* 1735 */         GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/* 1736 */         float f6 = afloat[0];
/* 1737 */         float f7 = afloat[1];
/* 1738 */         float f8 = afloat[2];
/*      */         
/* 1740 */         if (pass != 2) {
/*      */           
/* 1742 */           float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
/* 1743 */           float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
/* 1744 */           float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
/* 1745 */           f6 = f9;
/* 1746 */           f7 = f10;
/* 1747 */           f8 = f11;
/*      */         } 
/*      */         
/* 1750 */         worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
/* 1751 */         worldrenderer.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
/* 1752 */         int j = 16;
/*      */         
/* 1754 */         for (int l = 0; l <= 16; l++) {
/*      */           
/* 1756 */           float f18 = l * 3.1415927F * 2.0F / 16.0F;
/* 1757 */           float f12 = MathHelper.sin(f18);
/* 1758 */           float f13 = MathHelper.cos(f18);
/* 1759 */           worldrenderer.pos((f12 * 120.0F), (f13 * 120.0F), (-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
/*      */         } 
/*      */         
/* 1762 */         tessellator.draw();
/* 1763 */         GlStateManager.popMatrix();
/* 1764 */         GlStateManager.shadeModel(7424);
/*      */       } 
/*      */       
/* 1767 */       GlStateManager.enableTexture2D();
/*      */       
/* 1769 */       if (flag)
/*      */       {
/* 1771 */         Shaders.enableTexture2D();
/*      */       }
/*      */       
/* 1774 */       GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/* 1775 */       GlStateManager.pushMatrix();
/* 1776 */       float f15 = 1.0F - this.theWorld.getRainStrength(partialTicks);
/* 1777 */       GlStateManager.color(1.0F, 1.0F, 1.0F, f15);
/* 1778 */       GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
/* 1779 */       CustomSky.renderSky((World)this.theWorld, this.renderEngine, partialTicks);
/*      */       
/* 1781 */       if (flag)
/*      */       {
/* 1783 */         Shaders.preCelestialRotate();
/*      */       }
/*      */       
/* 1786 */       GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
/*      */       
/* 1788 */       if (flag)
/*      */       {
/* 1790 */         Shaders.postCelestialRotate();
/*      */       }
/*      */       
/* 1793 */       float f16 = 30.0F;
/*      */       
/* 1795 */       if (Config.isSunTexture()) {
/*      */         
/* 1797 */         this.renderEngine.bindTexture(locationSunPng);
/* 1798 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 1799 */         worldrenderer.pos(-f16, 100.0D, -f16).tex(0.0D, 0.0D).endVertex();
/* 1800 */         worldrenderer.pos(f16, 100.0D, -f16).tex(1.0D, 0.0D).endVertex();
/* 1801 */         worldrenderer.pos(f16, 100.0D, f16).tex(1.0D, 1.0D).endVertex();
/* 1802 */         worldrenderer.pos(-f16, 100.0D, f16).tex(0.0D, 1.0D).endVertex();
/* 1803 */         tessellator.draw();
/*      */       } 
/*      */       
/* 1806 */       f16 = 20.0F;
/*      */       
/* 1808 */       if (Config.isMoonTexture()) {
/*      */         
/* 1810 */         this.renderEngine.bindTexture(locationMoonPhasesPng);
/* 1811 */         int i = this.theWorld.getMoonPhase();
/* 1812 */         int k = i % 4;
/* 1813 */         int i1 = i / 4 % 2;
/* 1814 */         float f19 = (k + 0) / 4.0F;
/* 1815 */         float f21 = (i1 + 0) / 2.0F;
/* 1816 */         float f23 = (k + 1) / 4.0F;
/* 1817 */         float f14 = (i1 + 1) / 2.0F;
/* 1818 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 1819 */         worldrenderer.pos(-f16, -100.0D, f16).tex(f23, f14).endVertex();
/* 1820 */         worldrenderer.pos(f16, -100.0D, f16).tex(f19, f14).endVertex();
/* 1821 */         worldrenderer.pos(f16, -100.0D, -f16).tex(f19, f21).endVertex();
/* 1822 */         worldrenderer.pos(-f16, -100.0D, -f16).tex(f23, f21).endVertex();
/* 1823 */         tessellator.draw();
/*      */       } 
/*      */       
/* 1826 */       GlStateManager.disableTexture2D();
/*      */       
/* 1828 */       if (flag)
/*      */       {
/* 1830 */         Shaders.disableTexture2D();
/*      */       }
/*      */       
/* 1833 */       float f17 = this.theWorld.getStarBrightness(partialTicks) * f15;
/*      */       
/* 1835 */       if (f17 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers((World)this.theWorld)) {
/*      */         
/* 1837 */         GlStateManager.color(f17, f17, f17, f17);
/*      */         
/* 1839 */         if (this.vboEnabled) {
/*      */           
/* 1841 */           this.starVBO.bindBuffer();
/* 1842 */           GL11.glEnableClientState(32884);
/* 1843 */           GL11.glVertexPointer(3, 5126, 12, 0L);
/* 1844 */           this.starVBO.drawArrays(7);
/* 1845 */           this.starVBO.unbindBuffer();
/* 1846 */           GL11.glDisableClientState(32884);
/*      */         }
/*      */         else {
/*      */           
/* 1850 */           GlStateManager.callList(this.starGLCallList);
/*      */         } 
/*      */       } 
/*      */       
/* 1854 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 1855 */       GlStateManager.disableBlend();
/* 1856 */       GlStateManager.enableAlpha();
/* 1857 */       GlStateManager.enableFog();
/*      */       
/* 1859 */       if (flag)
/*      */       {
/* 1861 */         Shaders.enableFog();
/*      */       }
/*      */       
/* 1864 */       GlStateManager.popMatrix();
/* 1865 */       GlStateManager.disableTexture2D();
/*      */       
/* 1867 */       if (flag)
/*      */       {
/* 1869 */         Shaders.disableTexture2D();
/*      */       }
/*      */       
/* 1872 */       GlStateManager.color(0.0F, 0.0F, 0.0F);
/* 1873 */       double d0 = (this.mc.thePlayer.getPositionEyes(partialTicks)).yCoord - this.theWorld.getHorizon();
/*      */       
/* 1875 */       if (d0 < 0.0D) {
/*      */         
/* 1877 */         GlStateManager.pushMatrix();
/* 1878 */         GlStateManager.translate(0.0F, 12.0F, 0.0F);
/*      */         
/* 1880 */         if (this.vboEnabled) {
/*      */           
/* 1882 */           this.sky2VBO.bindBuffer();
/* 1883 */           GL11.glEnableClientState(32884);
/* 1884 */           GL11.glVertexPointer(3, 5126, 12, 0L);
/* 1885 */           this.sky2VBO.drawArrays(7);
/* 1886 */           this.sky2VBO.unbindBuffer();
/* 1887 */           GL11.glDisableClientState(32884);
/*      */         }
/*      */         else {
/*      */           
/* 1891 */           GlStateManager.callList(this.glSkyList2);
/*      */         } 
/*      */         
/* 1894 */         GlStateManager.popMatrix();
/* 1895 */         float f20 = 1.0F;
/* 1896 */         float f22 = -((float)(d0 + 65.0D));
/* 1897 */         float f24 = -1.0F;
/* 1898 */         worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 1899 */         worldrenderer.pos(-1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1900 */         worldrenderer.pos(1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1901 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1902 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1903 */         worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1904 */         worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1905 */         worldrenderer.pos(1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1906 */         worldrenderer.pos(-1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1907 */         worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1908 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1909 */         worldrenderer.pos(1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1910 */         worldrenderer.pos(1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1911 */         worldrenderer.pos(-1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1912 */         worldrenderer.pos(-1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1913 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1914 */         worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1915 */         worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1916 */         worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1917 */         worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
/* 1918 */         worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
/* 1919 */         tessellator.draw();
/*      */       } 
/*      */       
/* 1922 */       if (this.theWorld.provider.isSkyColored()) {
/*      */         
/* 1924 */         GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
/*      */       }
/*      */       else {
/*      */         
/* 1928 */         GlStateManager.color(f, f1, f2);
/*      */       } 
/*      */       
/* 1931 */       if (this.mc.gameSettings.renderDistanceChunks <= 4)
/*      */       {
/* 1933 */         GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
/*      */       }
/*      */       
/* 1936 */       GlStateManager.pushMatrix();
/* 1937 */       GlStateManager.translate(0.0F, -((float)(d0 - 16.0D)), 0.0F);
/*      */       
/* 1939 */       if (Config.isSkyEnabled())
/*      */       {
/* 1941 */         if (this.vboEnabled) {
/*      */           
/* 1943 */           this.sky2VBO.bindBuffer();
/* 1944 */           GlStateManager.glEnableClientState(32884);
/* 1945 */           GlStateManager.glVertexPointer(3, 5126, 12, 0);
/* 1946 */           this.sky2VBO.drawArrays(7);
/* 1947 */           this.sky2VBO.unbindBuffer();
/* 1948 */           GlStateManager.glDisableClientState(32884);
/*      */         }
/*      */         else {
/*      */           
/* 1952 */           GlStateManager.callList(this.glSkyList2);
/*      */         } 
/*      */       }
/*      */       
/* 1956 */       GlStateManager.popMatrix();
/* 1957 */       GlStateManager.enableTexture2D();
/*      */       
/* 1959 */       if (flag)
/*      */       {
/* 1961 */         Shaders.enableTexture2D();
/*      */       }
/*      */       
/* 1964 */       GlStateManager.depthMask(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void renderClouds(float partialTicks, int pass) {
/* 1970 */     if (!Config.isCloudsOff()) {
/*      */       
/* 1972 */       if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
/*      */         
/* 1974 */         WorldProvider worldprovider = this.mc.theWorld.provider;
/* 1975 */         Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
/*      */         
/* 1977 */         if (object != null) {
/*      */           
/* 1979 */           Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1984 */       if (this.mc.theWorld.provider.isSurfaceWorld()) {
/*      */         
/* 1986 */         if (Config.isShaders())
/*      */         {
/* 1988 */           Shaders.beginClouds();
/*      */         }
/*      */         
/* 1991 */         if (Config.isCloudsFancy()) {
/*      */           
/* 1993 */           renderCloudsFancy(partialTicks, pass);
/*      */         }
/*      */         else {
/*      */           
/* 1997 */           float f9 = partialTicks;
/* 1998 */           partialTicks = 0.0F;
/* 1999 */           GlStateManager.disableCull();
/* 2000 */           float f10 = (float)((this.mc.getRenderViewEntity()).lastTickPosY + ((this.mc.getRenderViewEntity()).posY - (this.mc.getRenderViewEntity()).lastTickPosY) * partialTicks);
/* 2001 */           int i = 32;
/* 2002 */           int j = 8;
/* 2003 */           Tessellator tessellator = Tessellator.getInstance();
/* 2004 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2005 */           this.renderEngine.bindTexture(locationCloudsPng);
/* 2006 */           GlStateManager.enableBlend();
/* 2007 */           GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 2008 */           Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
/* 2009 */           float f = (float)vec3.xCoord;
/* 2010 */           float f1 = (float)vec3.yCoord;
/* 2011 */           float f2 = (float)vec3.zCoord;
/* 2012 */           this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, f9, vec3);
/*      */           
/* 2014 */           if (this.cloudRenderer.shouldUpdateGlList()) {
/*      */             
/* 2016 */             this.cloudRenderer.startUpdateGlList();
/*      */             
/* 2018 */             if (pass != 2) {
/*      */               
/* 2020 */               float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
/* 2021 */               float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
/* 2022 */               float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
/* 2023 */               f = f3;
/* 2024 */               f1 = f4;
/* 2025 */               f2 = f5;
/*      */             } 
/*      */             
/* 2028 */             float f11 = 4.8828125E-4F;
/* 2029 */             double d2 = (this.cloudTickCounter + partialTicks);
/* 2030 */             double d0 = (this.mc.getRenderViewEntity()).prevPosX + ((this.mc.getRenderViewEntity()).posX - (this.mc.getRenderViewEntity()).prevPosX) * partialTicks + d2 * 0.029999999329447746D;
/* 2031 */             double d1 = (this.mc.getRenderViewEntity()).prevPosZ + ((this.mc.getRenderViewEntity()).posZ - (this.mc.getRenderViewEntity()).prevPosZ) * partialTicks;
/* 2032 */             int k = MathHelper.floor_double(d0 / 2048.0D);
/* 2033 */             int l = MathHelper.floor_double(d1 / 2048.0D);
/* 2034 */             d0 -= (k * 2048);
/* 2035 */             d1 -= (l * 2048);
/* 2036 */             float f6 = this.theWorld.provider.getCloudHeight() - f10 + 0.33F;
/* 2037 */             f6 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
/* 2038 */             float f7 = (float)(d0 * 4.8828125E-4D);
/* 2039 */             float f8 = (float)(d1 * 4.8828125E-4D);
/* 2040 */             worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*      */             
/* 2042 */             for (int i1 = -256; i1 < 256; i1 += 32) {
/*      */               
/* 2044 */               for (int j1 = -256; j1 < 256; j1 += 32) {
/*      */                 
/* 2046 */                 worldrenderer.pos((i1 + 0), f6, (j1 + 32)).tex(((i1 + 0) * 4.8828125E-4F + f7), ((j1 + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
/* 2047 */                 worldrenderer.pos((i1 + 32), f6, (j1 + 32)).tex(((i1 + 32) * 4.8828125E-4F + f7), ((j1 + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
/* 2048 */                 worldrenderer.pos((i1 + 32), f6, (j1 + 0)).tex(((i1 + 32) * 4.8828125E-4F + f7), ((j1 + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
/* 2049 */                 worldrenderer.pos((i1 + 0), f6, (j1 + 0)).tex(((i1 + 0) * 4.8828125E-4F + f7), ((j1 + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
/*      */               } 
/*      */             } 
/*      */             
/* 2053 */             tessellator.draw();
/* 2054 */             this.cloudRenderer.endUpdateGlList();
/*      */           } 
/*      */           
/* 2057 */           this.cloudRenderer.renderGlList();
/* 2058 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 2059 */           GlStateManager.disableBlend();
/* 2060 */           GlStateManager.enableCull();
/*      */         } 
/*      */         
/* 2063 */         if (Config.isShaders())
/*      */         {
/* 2065 */           Shaders.endClouds();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasCloudFog(double x, double y, double z, float partialTicks) {
/* 2073 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void renderCloudsFancy(float partialTicks, int pass) {
/* 2078 */     partialTicks = 0.0F;
/* 2079 */     GlStateManager.disableCull();
/* 2080 */     float f = (float)((this.mc.getRenderViewEntity()).lastTickPosY + ((this.mc.getRenderViewEntity()).posY - (this.mc.getRenderViewEntity()).lastTickPosY) * partialTicks);
/* 2081 */     Tessellator tessellator = Tessellator.getInstance();
/* 2082 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2083 */     float f1 = 12.0F;
/* 2084 */     float f2 = 4.0F;
/* 2085 */     double d0 = (this.cloudTickCounter + partialTicks);
/* 2086 */     double d1 = ((this.mc.getRenderViewEntity()).prevPosX + ((this.mc.getRenderViewEntity()).posX - (this.mc.getRenderViewEntity()).prevPosX) * partialTicks + d0 * 0.029999999329447746D) / 12.0D;
/* 2087 */     double d2 = ((this.mc.getRenderViewEntity()).prevPosZ + ((this.mc.getRenderViewEntity()).posZ - (this.mc.getRenderViewEntity()).prevPosZ) * partialTicks) / 12.0D + 0.33000001311302185D;
/* 2088 */     float f3 = this.theWorld.provider.getCloudHeight() - f + 0.33F;
/* 2089 */     f3 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
/* 2090 */     int i = MathHelper.floor_double(d1 / 2048.0D);
/* 2091 */     int j = MathHelper.floor_double(d2 / 2048.0D);
/* 2092 */     d1 -= (i * 2048);
/* 2093 */     d2 -= (j * 2048);
/* 2094 */     this.renderEngine.bindTexture(locationCloudsPng);
/* 2095 */     GlStateManager.enableBlend();
/* 2096 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 2097 */     Vec3 vec3 = this.theWorld.getCloudColour(partialTicks);
/* 2098 */     float f4 = (float)vec3.xCoord;
/* 2099 */     float f5 = (float)vec3.yCoord;
/* 2100 */     float f6 = (float)vec3.zCoord;
/* 2101 */     this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks, vec3);
/*      */     
/* 2103 */     if (pass != 2) {
/*      */       
/* 2105 */       float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
/* 2106 */       float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
/* 2107 */       float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
/* 2108 */       f4 = f7;
/* 2109 */       f5 = f8;
/* 2110 */       f6 = f9;
/*      */     } 
/*      */     
/* 2113 */     float f26 = f4 * 0.9F;
/* 2114 */     float f27 = f5 * 0.9F;
/* 2115 */     float f28 = f6 * 0.9F;
/* 2116 */     float f10 = f4 * 0.7F;
/* 2117 */     float f11 = f5 * 0.7F;
/* 2118 */     float f12 = f6 * 0.7F;
/* 2119 */     float f13 = f4 * 0.8F;
/* 2120 */     float f14 = f5 * 0.8F;
/* 2121 */     float f15 = f6 * 0.8F;
/* 2122 */     float f16 = 0.00390625F;
/* 2123 */     float f17 = MathHelper.floor_double(d1) * 0.00390625F;
/* 2124 */     float f18 = MathHelper.floor_double(d2) * 0.00390625F;
/* 2125 */     float f19 = (float)(d1 - MathHelper.floor_double(d1));
/* 2126 */     float f20 = (float)(d2 - MathHelper.floor_double(d2));
/* 2127 */     int k = 8;
/* 2128 */     int l = 4;
/* 2129 */     float f21 = 9.765625E-4F;
/* 2130 */     GlStateManager.scale(12.0F, 1.0F, 12.0F);
/*      */     
/* 2132 */     for (int i1 = 0; i1 < 2; i1++) {
/*      */       
/* 2134 */       if (i1 == 0) {
/*      */         
/* 2136 */         GlStateManager.colorMask(false, false, false, false);
/*      */       }
/*      */       else {
/*      */         
/* 2140 */         switch (pass) {
/*      */           
/*      */           case 0:
/* 2143 */             GlStateManager.colorMask(false, true, true, true);
/*      */             break;
/*      */           
/*      */           case 1:
/* 2147 */             GlStateManager.colorMask(true, false, false, true);
/*      */             break;
/*      */           
/*      */           case 2:
/* 2151 */             GlStateManager.colorMask(true, true, true, true);
/*      */             break;
/*      */         } 
/*      */       } 
/* 2155 */       this.cloudRenderer.renderGlList();
/*      */     } 
/*      */     
/* 2158 */     if (this.cloudRenderer.shouldUpdateGlList()) {
/*      */       
/* 2160 */       this.cloudRenderer.startUpdateGlList();
/*      */       
/* 2162 */       for (int l1 = -3; l1 <= 4; l1++) {
/*      */         
/* 2164 */         for (int j1 = -3; j1 <= 4; j1++) {
/*      */           
/* 2166 */           worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
/* 2167 */           float f22 = (l1 * 8);
/* 2168 */           float f23 = (j1 * 8);
/* 2169 */           float f24 = f22 - f19;
/* 2170 */           float f25 = f23 - f20;
/*      */           
/* 2172 */           if (f3 > -5.0F) {
/*      */             
/* 2174 */             worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 2175 */             worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 2176 */             worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 2177 */             worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f10, f11, f12, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
/*      */           } 
/*      */           
/* 2180 */           if (f3 <= 5.0F) {
/*      */             
/* 2182 */             worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 8.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 2183 */             worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 8.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 2184 */             worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 2185 */             worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F - 9.765625E-4F), (f25 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f4, f5, f6, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
/*      */           } 
/*      */           
/* 2188 */           if (l1 > -1)
/*      */           {
/* 2190 */             for (int k1 = 0; k1 < 8; k1++) {
/*      */               
/* 2192 */               worldrenderer.pos((f24 + k1 + 0.0F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 2193 */               worldrenderer.pos((f24 + k1 + 0.0F), (f3 + 4.0F), (f25 + 8.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 2194 */               worldrenderer.pos((f24 + k1 + 0.0F), (f3 + 4.0F), (f25 + 0.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 2195 */               worldrenderer.pos((f24 + k1 + 0.0F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + k1 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
/*      */             } 
/*      */           }
/*      */           
/* 2199 */           if (l1 <= 1)
/*      */           {
/* 2201 */             for (int i2 = 0; i2 < 8; i2++) {
/*      */               
/* 2203 */               worldrenderer.pos((f24 + i2 + 1.0F - 9.765625E-4F), (f3 + 0.0F), (f25 + 8.0F)).tex(((f22 + i2 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 2204 */               worldrenderer.pos((f24 + i2 + 1.0F - 9.765625E-4F), (f3 + 4.0F), (f25 + 8.0F)).tex(((f22 + i2 + 0.5F) * 0.00390625F + f17), ((f23 + 8.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 2205 */               worldrenderer.pos((f24 + i2 + 1.0F - 9.765625E-4F), (f3 + 4.0F), (f25 + 0.0F)).tex(((f22 + i2 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 2206 */               worldrenderer.pos((f24 + i2 + 1.0F - 9.765625E-4F), (f3 + 0.0F), (f25 + 0.0F)).tex(((f22 + i2 + 0.5F) * 0.00390625F + f17), ((f23 + 0.0F) * 0.00390625F + f18)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
/*      */             } 
/*      */           }
/*      */           
/* 2210 */           if (j1 > -1)
/*      */           {
/* 2212 */             for (int j2 = 0; j2 < 8; j2++) {
/*      */               
/* 2214 */               worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F), (f25 + j2 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 2215 */               worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F), (f25 + j2 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 2216 */               worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + j2 + 0.0F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 2217 */               worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + j2 + 0.0F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + j2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
/*      */             } 
/*      */           }
/*      */           
/* 2221 */           if (j1 <= 1)
/*      */           {
/* 2223 */             for (int k2 = 0; k2 < 8; k2++) {
/*      */               
/* 2225 */               worldrenderer.pos((f24 + 0.0F), (f3 + 4.0F), (f25 + k2 + 1.0F - 9.765625E-4F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 2226 */               worldrenderer.pos((f24 + 8.0F), (f3 + 4.0F), (f25 + k2 + 1.0F - 9.765625E-4F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 2227 */               worldrenderer.pos((f24 + 8.0F), (f3 + 0.0F), (f25 + k2 + 1.0F - 9.765625E-4F)).tex(((f22 + 8.0F) * 0.00390625F + f17), ((f23 + k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 2228 */               worldrenderer.pos((f24 + 0.0F), (f3 + 0.0F), (f25 + k2 + 1.0F - 9.765625E-4F)).tex(((f22 + 0.0F) * 0.00390625F + f17), ((f23 + k2 + 0.5F) * 0.00390625F + f18)).color(f13, f14, f15, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
/*      */             } 
/*      */           }
/*      */           
/* 2232 */           tessellator.draw();
/*      */         } 
/*      */       } 
/*      */       
/* 2236 */       this.cloudRenderer.endUpdateGlList();
/*      */     } 
/*      */     
/* 2239 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 2240 */     GlStateManager.disableBlend();
/* 2241 */     GlStateManager.enableCull();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateChunks(long finishTimeNano) {
/* 2246 */     finishTimeNano = (long)(finishTimeNano + 1.0E8D);
/* 2247 */     this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
/*      */     
/* 2249 */     if (this.chunksToUpdateForced.size() > 0) {
/*      */       
/* 2251 */       Iterator<RenderChunk> iterator = this.chunksToUpdateForced.iterator();
/*      */       
/* 2253 */       while (iterator.hasNext()) {
/*      */         
/* 2255 */         RenderChunk renderchunk = iterator.next();
/*      */         
/* 2257 */         if (!this.renderDispatcher.updateChunkLater(renderchunk)) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/* 2262 */         renderchunk.setNeedsUpdate(false);
/* 2263 */         iterator.remove();
/* 2264 */         this.chunksToUpdate.remove(renderchunk);
/* 2265 */         this.chunksToResortTransparency.remove(renderchunk);
/*      */       } 
/*      */     } 
/*      */     
/* 2269 */     if (this.chunksToResortTransparency.size() > 0) {
/*      */       
/* 2271 */       Iterator<RenderChunk> iterator2 = this.chunksToResortTransparency.iterator();
/*      */       
/* 2273 */       if (iterator2.hasNext()) {
/*      */         
/* 2275 */         RenderChunk renderchunk2 = iterator2.next();
/*      */         
/* 2277 */         if (this.renderDispatcher.updateTransparencyLater(renderchunk2))
/*      */         {
/* 2279 */           iterator2.remove();
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 2284 */     double d1 = 0.0D;
/* 2285 */     int i = Config.getUpdatesPerFrame();
/*      */     
/* 2287 */     if (!this.chunksToUpdate.isEmpty()) {
/*      */       
/* 2289 */       Iterator<RenderChunk> iterator1 = this.chunksToUpdate.iterator();
/*      */       
/* 2291 */       while (iterator1.hasNext()) {
/*      */         boolean flag1;
/* 2293 */         RenderChunk renderchunk1 = iterator1.next();
/* 2294 */         boolean flag = renderchunk1.isChunkRegionEmpty();
/*      */ 
/*      */         
/* 2297 */         if (flag) {
/*      */           
/* 2299 */           flag1 = this.renderDispatcher.updateChunkNow(renderchunk1);
/*      */         }
/*      */         else {
/*      */           
/* 2303 */           flag1 = this.renderDispatcher.updateChunkLater(renderchunk1);
/*      */         } 
/*      */         
/* 2306 */         if (!flag1) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/* 2311 */         renderchunk1.setNeedsUpdate(false);
/* 2312 */         iterator1.remove();
/*      */         
/* 2314 */         if (!flag) {
/*      */           
/* 2316 */           double d0 = 2.0D * RenderChunkUtils.getRelativeBufferSize(renderchunk1);
/* 2317 */           d1 += d0;
/*      */           
/* 2319 */           if (d1 > i) {
/*      */             break;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void renderWorldBorder(Entity entityIn, float partialTicks) {
/* 2330 */     Tessellator tessellator = Tessellator.getInstance();
/* 2331 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2332 */     WorldBorder worldborder = this.theWorld.getWorldBorder();
/* 2333 */     double d0 = (this.mc.gameSettings.renderDistanceChunks * 16);
/*      */     
/* 2335 */     if (entityIn.posX >= worldborder.maxX() - d0 || entityIn.posX <= worldborder.minX() + d0 || entityIn.posZ >= worldborder.maxZ() - d0 || entityIn.posZ <= worldborder.minZ() + d0) {
/*      */       
/* 2337 */       if (Config.isShaders()) {
/*      */         
/* 2339 */         Shaders.pushProgram();
/* 2340 */         Shaders.useProgram(Shaders.ProgramTexturedLit);
/*      */       } 
/*      */       
/* 2343 */       double d1 = 1.0D - worldborder.getClosestDistance(entityIn) / d0;
/* 2344 */       d1 = Math.pow(d1, 4.0D);
/* 2345 */       double d2 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 2346 */       double d3 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 2347 */       double d4 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 2348 */       GlStateManager.enableBlend();
/* 2349 */       GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
/* 2350 */       this.renderEngine.bindTexture(locationForcefieldPng);
/* 2351 */       GlStateManager.depthMask(false);
/* 2352 */       GlStateManager.pushMatrix();
/* 2353 */       int i = worldborder.getStatus().getID();
/* 2354 */       float f = (i >> 16 & 0xFF) / 255.0F;
/* 2355 */       float f1 = (i >> 8 & 0xFF) / 255.0F;
/* 2356 */       float f2 = (i & 0xFF) / 255.0F;
/* 2357 */       GlStateManager.color(f, f1, f2, (float)d1);
/* 2358 */       GlStateManager.doPolygonOffset(-3.0F, -3.0F);
/* 2359 */       GlStateManager.enablePolygonOffset();
/* 2360 */       GlStateManager.alphaFunc(516, 0.1F);
/* 2361 */       GlStateManager.enableAlpha();
/* 2362 */       GlStateManager.disableCull();
/* 2363 */       float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
/* 2364 */       float f4 = 0.0F;
/* 2365 */       float f5 = 0.0F;
/* 2366 */       float f6 = 128.0F;
/* 2367 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 2368 */       worldrenderer.setTranslation(-d2, -d3, -d4);
/* 2369 */       double d5 = Math.max(MathHelper.floor_double(d4 - d0), worldborder.minZ());
/* 2370 */       double d6 = Math.min(MathHelper.ceiling_double_int(d4 + d0), worldborder.maxZ());
/*      */       
/* 2372 */       if (d2 > worldborder.maxX() - d0) {
/*      */         
/* 2374 */         float f7 = 0.0F;
/*      */         
/* 2376 */         for (double d7 = d5; d7 < d6; f7 += 0.5F) {
/*      */           
/* 2378 */           double d8 = Math.min(1.0D, d6 - d7);
/* 2379 */           float f8 = (float)d8 * 0.5F;
/* 2380 */           worldrenderer.pos(worldborder.maxX(), 256.0D, d7).tex((f3 + f7), (f3 + 0.0F)).endVertex();
/* 2381 */           worldrenderer.pos(worldborder.maxX(), 256.0D, d7 + d8).tex((f3 + f8 + f7), (f3 + 0.0F)).endVertex();
/* 2382 */           worldrenderer.pos(worldborder.maxX(), 0.0D, d7 + d8).tex((f3 + f8 + f7), (f3 + 128.0F)).endVertex();
/* 2383 */           worldrenderer.pos(worldborder.maxX(), 0.0D, d7).tex((f3 + f7), (f3 + 128.0F)).endVertex();
/* 2384 */           d7++;
/*      */         } 
/*      */       } 
/*      */       
/* 2388 */       if (d2 < worldborder.minX() + d0) {
/*      */         
/* 2390 */         float f9 = 0.0F;
/*      */         
/* 2392 */         for (double d9 = d5; d9 < d6; f9 += 0.5F) {
/*      */           
/* 2394 */           double d12 = Math.min(1.0D, d6 - d9);
/* 2395 */           float f12 = (float)d12 * 0.5F;
/* 2396 */           worldrenderer.pos(worldborder.minX(), 256.0D, d9).tex((f3 + f9), (f3 + 0.0F)).endVertex();
/* 2397 */           worldrenderer.pos(worldborder.minX(), 256.0D, d9 + d12).tex((f3 + f12 + f9), (f3 + 0.0F)).endVertex();
/* 2398 */           worldrenderer.pos(worldborder.minX(), 0.0D, d9 + d12).tex((f3 + f12 + f9), (f3 + 128.0F)).endVertex();
/* 2399 */           worldrenderer.pos(worldborder.minX(), 0.0D, d9).tex((f3 + f9), (f3 + 128.0F)).endVertex();
/* 2400 */           d9++;
/*      */         } 
/*      */       } 
/*      */       
/* 2404 */       d5 = Math.max(MathHelper.floor_double(d2 - d0), worldborder.minX());
/* 2405 */       d6 = Math.min(MathHelper.ceiling_double_int(d2 + d0), worldborder.maxX());
/*      */       
/* 2407 */       if (d4 > worldborder.maxZ() - d0) {
/*      */         
/* 2409 */         float f10 = 0.0F;
/*      */         
/* 2411 */         for (double d10 = d5; d10 < d6; f10 += 0.5F) {
/*      */           
/* 2413 */           double d13 = Math.min(1.0D, d6 - d10);
/* 2414 */           float f13 = (float)d13 * 0.5F;
/* 2415 */           worldrenderer.pos(d10, 256.0D, worldborder.maxZ()).tex((f3 + f10), (f3 + 0.0F)).endVertex();
/* 2416 */           worldrenderer.pos(d10 + d13, 256.0D, worldborder.maxZ()).tex((f3 + f13 + f10), (f3 + 0.0F)).endVertex();
/* 2417 */           worldrenderer.pos(d10 + d13, 0.0D, worldborder.maxZ()).tex((f3 + f13 + f10), (f3 + 128.0F)).endVertex();
/* 2418 */           worldrenderer.pos(d10, 0.0D, worldborder.maxZ()).tex((f3 + f10), (f3 + 128.0F)).endVertex();
/* 2419 */           d10++;
/*      */         } 
/*      */       } 
/*      */       
/* 2423 */       if (d4 < worldborder.minZ() + d0) {
/*      */         
/* 2425 */         float f11 = 0.0F;
/*      */         
/* 2427 */         for (double d11 = d5; d11 < d6; f11 += 0.5F) {
/*      */           
/* 2429 */           double d14 = Math.min(1.0D, d6 - d11);
/* 2430 */           float f14 = (float)d14 * 0.5F;
/* 2431 */           worldrenderer.pos(d11, 256.0D, worldborder.minZ()).tex((f3 + f11), (f3 + 0.0F)).endVertex();
/* 2432 */           worldrenderer.pos(d11 + d14, 256.0D, worldborder.minZ()).tex((f3 + f14 + f11), (f3 + 0.0F)).endVertex();
/* 2433 */           worldrenderer.pos(d11 + d14, 0.0D, worldborder.minZ()).tex((f3 + f14 + f11), (f3 + 128.0F)).endVertex();
/* 2434 */           worldrenderer.pos(d11, 0.0D, worldborder.minZ()).tex((f3 + f11), (f3 + 128.0F)).endVertex();
/* 2435 */           d11++;
/*      */         } 
/*      */       } 
/*      */       
/* 2439 */       tessellator.draw();
/* 2440 */       worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 2441 */       GlStateManager.enableCull();
/* 2442 */       GlStateManager.disableAlpha();
/* 2443 */       GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 2444 */       GlStateManager.disablePolygonOffset();
/* 2445 */       GlStateManager.enableAlpha();
/* 2446 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 2447 */       GlStateManager.disableBlend();
/* 2448 */       GlStateManager.popMatrix();
/* 2449 */       GlStateManager.depthMask(true);
/*      */       
/* 2451 */       if (Config.isShaders())
/*      */       {
/* 2453 */         Shaders.popProgram();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void preRenderDamagedBlocks() {
/* 2460 */     GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
/* 2461 */     GlStateManager.enableBlend();
/* 2462 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
/* 2463 */     GlStateManager.doPolygonOffset(-1.0F, -10.0F);
/* 2464 */     GlStateManager.enablePolygonOffset();
/* 2465 */     GlStateManager.alphaFunc(516, 0.1F);
/* 2466 */     GlStateManager.enableAlpha();
/* 2467 */     GlStateManager.pushMatrix();
/*      */     
/* 2469 */     if (Config.isShaders())
/*      */     {
/* 2471 */       ShadersRender.beginBlockDamage();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void postRenderDamagedBlocks() {
/* 2477 */     GlStateManager.disableAlpha();
/* 2478 */     GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 2479 */     GlStateManager.disablePolygonOffset();
/* 2480 */     GlStateManager.enableAlpha();
/* 2481 */     GlStateManager.depthMask(true);
/* 2482 */     GlStateManager.popMatrix();
/*      */     
/* 2484 */     if (Config.isShaders())
/*      */     {
/* 2486 */       ShadersRender.endBlockDamage();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks) {
/* 2492 */     double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 2493 */     double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 2494 */     double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/*      */     
/* 2496 */     if (!this.damagedBlocks.isEmpty()) {
/*      */       
/* 2498 */       this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/* 2499 */       preRenderDamagedBlocks();
/* 2500 */       worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
/* 2501 */       worldRendererIn.setTranslation(-d0, -d1, -d2);
/* 2502 */       worldRendererIn.noColor();
/* 2503 */       Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();
/*      */       
/* 2505 */       while (iterator.hasNext()) {
/*      */         boolean flag;
/* 2507 */         DestroyBlockProgress destroyblockprogress = iterator.next();
/* 2508 */         BlockPos blockpos = destroyblockprogress.getPosition();
/* 2509 */         double d3 = blockpos.getX() - d0;
/* 2510 */         double d4 = blockpos.getY() - d1;
/* 2511 */         double d5 = blockpos.getZ() - d2;
/* 2512 */         Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */ 
/*      */         
/* 2515 */         if (Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
/*      */           
/* 2517 */           boolean flag1 = (block instanceof net.minecraft.block.BlockChest || block instanceof net.minecraft.block.BlockEnderChest || block instanceof net.minecraft.block.BlockSign || block instanceof net.minecraft.block.BlockSkull);
/*      */           
/* 2519 */           if (!flag1) {
/*      */             
/* 2521 */             TileEntity tileentity = this.theWorld.getTileEntity(blockpos);
/*      */             
/* 2523 */             if (tileentity != null)
/*      */             {
/* 2525 */               flag1 = Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
/*      */             }
/*      */           } 
/*      */           
/* 2529 */           flag = !flag1;
/*      */         }
/*      */         else {
/*      */           
/* 2533 */           flag = (!(block instanceof net.minecraft.block.BlockChest) && !(block instanceof net.minecraft.block.BlockEnderChest) && !(block instanceof net.minecraft.block.BlockSign) && !(block instanceof net.minecraft.block.BlockSkull));
/*      */         } 
/*      */         
/* 2536 */         if (flag) {
/*      */           
/* 2538 */           if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D) {
/*      */             
/* 2540 */             iterator.remove();
/*      */             
/*      */             continue;
/*      */           } 
/* 2544 */           IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
/*      */           
/* 2546 */           if (iblockstate.getBlock().getMaterial() != Material.air) {
/*      */             
/* 2548 */             int i = destroyblockprogress.getPartialBlockDamage();
/* 2549 */             TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
/* 2550 */             BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
/* 2551 */             blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, (IBlockAccess)this.theWorld);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2557 */       tessellatorIn.draw();
/* 2558 */       worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
/* 2559 */       postRenderDamagedBlocks();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks) {
/* 2565 */     if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*      */       
/* 2567 */       GlStateManager.enableBlend();
/* 2568 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 2569 */       GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
/* 2570 */       GL11.glLineWidth(2.0F);
/* 2571 */       GlStateManager.disableTexture2D();
/*      */       
/* 2573 */       if (Config.isShaders())
/*      */       {
/* 2575 */         Shaders.disableTexture2D();
/*      */       }
/*      */       
/* 2578 */       GlStateManager.depthMask(false);
/* 2579 */       float f = 0.002F;
/* 2580 */       BlockPos blockpos = movingObjectPositionIn.getBlockPos();
/* 2581 */       Block block = this.theWorld.getBlockState(blockpos).getBlock();
/*      */       
/* 2583 */       if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos)) {
/*      */         
/* 2585 */         block.setBlockBoundsBasedOnState((IBlockAccess)this.theWorld, blockpos);
/* 2586 */         double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
/* 2587 */         double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
/* 2588 */         double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
/* 2589 */         AxisAlignedBB axisalignedbb = block.getSelectedBoundingBox((World)this.theWorld, blockpos);
/* 2590 */         Block.EnumOffsetType block$enumoffsettype = block.getOffsetType();
/*      */         
/* 2592 */         if (block$enumoffsettype != Block.EnumOffsetType.NONE)
/*      */         {
/* 2594 */           axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, blockpos);
/*      */         }
/*      */         
/* 2597 */         drawSelectionBoundingBox(axisalignedbb.expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
/*      */       } 
/*      */       
/* 2600 */       GlStateManager.depthMask(true);
/* 2601 */       GlStateManager.enableTexture2D();
/*      */       
/* 2603 */       if (Config.isShaders())
/*      */       {
/* 2605 */         Shaders.enableTexture2D();
/*      */       }
/*      */       
/* 2608 */       GlStateManager.disableBlend();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
/* 2614 */     Tessellator tessellator = Tessellator.getInstance();
/* 2615 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2616 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION);
/* 2617 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 2618 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 2619 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
/* 2620 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
/* 2621 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 2622 */     tessellator.draw();
/* 2623 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION);
/* 2624 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 2625 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 2626 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/* 2627 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/* 2628 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 2629 */     tessellator.draw();
/* 2630 */     worldrenderer.begin(1, DefaultVertexFormats.POSITION);
/* 2631 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 2632 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 2633 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
/* 2634 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
/* 2635 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
/* 2636 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/* 2637 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
/* 2638 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
/* 2639 */     tessellator.draw();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox, int red, int green, int blue, int alpha) {
/* 2644 */     Tessellator tessellator = Tessellator.getInstance();
/* 2645 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 2646 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/* 2647 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2648 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2649 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2650 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2651 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2652 */     tessellator.draw();
/* 2653 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/* 2654 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2655 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2656 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2657 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2658 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2659 */     tessellator.draw();
/* 2660 */     worldrenderer.begin(1, DefaultVertexFormats.POSITION_COLOR);
/* 2661 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2662 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2663 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2664 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(red, green, blue, alpha).endVertex();
/* 2665 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2666 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2667 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2668 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(red, green, blue, alpha).endVertex();
/* 2669 */     tessellator.draw();
/*      */   }
/*      */ 
/*      */   
/*      */   private void markBlocksForUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
/* 2674 */     this.viewFrustum.markBlocksForUpdate(x1, y1, z1, x2, y2, z2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void markBlockForUpdate(BlockPos pos) {
/* 2679 */     int i = pos.getX();
/* 2680 */     int j = pos.getY();
/* 2681 */     int k = pos.getZ();
/* 2682 */     markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyLightSet(BlockPos pos) {
/* 2687 */     int i = pos.getX();
/* 2688 */     int j = pos.getY();
/* 2689 */     int k = pos.getZ();
/* 2690 */     markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
/* 2695 */     markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void playRecord(String recordName, BlockPos blockPosIn) {
/* 2700 */     ISound isound = this.mapSoundPositions.get(blockPosIn);
/*      */     
/* 2702 */     if (isound != null) {
/*      */       
/* 2704 */       this.mc.getSoundHandler().stopSound(isound);
/* 2705 */       this.mapSoundPositions.remove(blockPosIn);
/*      */     } 
/*      */     
/* 2708 */     if (recordName != null) {
/*      */       
/* 2710 */       ItemRecord itemrecord = ItemRecord.getRecord(recordName);
/*      */       
/* 2712 */       if (itemrecord != null)
/*      */       {
/* 2714 */         this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
/*      */       }
/*      */       
/* 2717 */       PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(new ResourceLocation(recordName), blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ());
/* 2718 */       this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
/* 2719 */       this.mc.getSoundHandler().playSound((ISound)positionedsoundrecord);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void spawnParticle(int particleID, boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, double xOffset, double yOffset, double zOffset, int... parameters) {
/*      */     try {
/* 2735 */       spawnEntityFX(particleID, ignoreRange, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
/*      */     }
/* 2737 */     catch (Throwable throwable) {
/*      */       
/* 2739 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
/* 2740 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
/* 2741 */       crashreportcategory.addCrashSection("ID", Integer.valueOf(particleID));
/*      */       
/* 2743 */       if (parameters != null)
/*      */       {
/* 2745 */         crashreportcategory.addCrashSection("Parameters", parameters);
/*      */       }
/*      */       
/* 2748 */       crashreportcategory.addCrashSectionCallable("Position", new Callable<String>()
/*      */           {
/*      */             public String call() throws Exception
/*      */             {
/* 2752 */               return CrashReportCategory.getCoordinateInfo(xCoord, yCoord, zCoord);
/*      */             }
/*      */           });
/* 2755 */       throw new ReportedException(crashreport);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void spawnParticle(EnumParticleTypes particleIn, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters) {
/* 2761 */     spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
/*      */   }
/*      */ 
/*      */   
/*      */   private EntityFX spawnEntityFX(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... parameters) {
/* 2766 */     if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null) {
/*      */       
/* 2768 */       int i = this.mc.gameSettings.particleSetting;
/*      */       
/* 2770 */       if (i == 1 && this.theWorld.rand.nextInt(3) == 0)
/*      */       {
/* 2772 */         i = 2;
/*      */       }
/*      */       
/* 2775 */       double d0 = (this.mc.getRenderViewEntity()).posX - xCoord;
/* 2776 */       double d1 = (this.mc.getRenderViewEntity()).posY - yCoord;
/* 2777 */       double d2 = (this.mc.getRenderViewEntity()).posZ - zCoord;
/*      */       
/* 2779 */       if (particleID == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion())
/*      */       {
/* 2781 */         return null;
/*      */       }
/* 2783 */       if (particleID == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion())
/*      */       {
/* 2785 */         return null;
/*      */       }
/* 2787 */       if (particleID == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion())
/*      */       {
/* 2789 */         return null;
/*      */       }
/* 2791 */       if (particleID == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles())
/*      */       {
/* 2793 */         return null;
/*      */       }
/* 2795 */       if (particleID == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles())
/*      */       {
/* 2797 */         return null;
/*      */       }
/* 2799 */       if (particleID == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke())
/*      */       {
/* 2801 */         return null;
/*      */       }
/* 2803 */       if (particleID == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke())
/*      */       {
/* 2805 */         return null;
/*      */       }
/* 2807 */       if (particleID == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles())
/*      */       {
/* 2809 */         return null;
/*      */       }
/* 2811 */       if (particleID == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles())
/*      */       {
/* 2813 */         return null;
/*      */       }
/* 2815 */       if (particleID == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles())
/*      */       {
/* 2817 */         return null;
/*      */       }
/* 2819 */       if (particleID == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles())
/*      */       {
/* 2821 */         return null;
/*      */       }
/* 2823 */       if (particleID == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles())
/*      */       {
/* 2825 */         return null;
/*      */       }
/* 2827 */       if (particleID == EnumParticleTypes.PORTAL.getParticleID() && !Config.isPortalParticles())
/*      */       {
/* 2829 */         return null;
/*      */       }
/* 2831 */       if (particleID == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame())
/*      */       {
/* 2833 */         return null;
/*      */       }
/* 2835 */       if (particleID == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone())
/*      */       {
/* 2837 */         return null;
/*      */       }
/* 2839 */       if (particleID == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava())
/*      */       {
/* 2841 */         return null;
/*      */       }
/* 2843 */       if (particleID == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava())
/*      */       {
/* 2845 */         return null;
/*      */       }
/* 2847 */       if (particleID == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles())
/*      */       {
/* 2849 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 2853 */       if (!ignoreRange) {
/*      */         
/* 2855 */         double d3 = 256.0D;
/*      */         
/* 2857 */         if (particleID == EnumParticleTypes.CRIT.getParticleID())
/*      */         {
/* 2859 */           d3 = 38416.0D;
/*      */         }
/*      */         
/* 2862 */         if (d0 * d0 + d1 * d1 + d2 * d2 > d3)
/*      */         {
/* 2864 */           return null;
/*      */         }
/*      */         
/* 2867 */         if (i > 1)
/*      */         {
/* 2869 */           return null;
/*      */         }
/*      */       } 
/*      */       
/* 2873 */       EntityFX entityfx = this.mc.effectRenderer.spawnEffectParticle(particleID, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, parameters);
/*      */       
/* 2875 */       if (particleID == EnumParticleTypes.WATER_BUBBLE.getParticleID())
/*      */       {
/* 2877 */         CustomColors.updateWaterFX(entityfx, (IBlockAccess)this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
/*      */       }
/*      */       
/* 2880 */       if (particleID == EnumParticleTypes.WATER_SPLASH.getParticleID())
/*      */       {
/* 2882 */         CustomColors.updateWaterFX(entityfx, (IBlockAccess)this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
/*      */       }
/*      */       
/* 2885 */       if (particleID == EnumParticleTypes.WATER_DROP.getParticleID())
/*      */       {
/* 2887 */         CustomColors.updateWaterFX(entityfx, (IBlockAccess)this.theWorld, xCoord, yCoord, zCoord, this.renderEnv);
/*      */       }
/*      */       
/* 2890 */       if (particleID == EnumParticleTypes.TOWN_AURA.getParticleID())
/*      */       {
/* 2892 */         CustomColors.updateMyceliumFX(entityfx);
/*      */       }
/*      */       
/* 2895 */       if (particleID == EnumParticleTypes.PORTAL.getParticleID())
/*      */       {
/* 2897 */         CustomColors.updatePortalFX(entityfx);
/*      */       }
/*      */       
/* 2900 */       if (particleID == EnumParticleTypes.REDSTONE.getParticleID())
/*      */       {
/* 2902 */         CustomColors.updateReddustFX(entityfx, (IBlockAccess)this.theWorld, xCoord, yCoord, zCoord);
/*      */       }
/*      */       
/* 2905 */       return entityfx;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2910 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onEntityAdded(Entity entityIn) {
/* 2916 */     RandomEntities.entityLoaded(entityIn, (World)this.theWorld);
/*      */     
/* 2918 */     if (Config.isDynamicLights())
/*      */     {
/* 2920 */       DynamicLights.entityAdded(entityIn, this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEntityRemoved(Entity entityIn) {
/* 2926 */     RandomEntities.entityUnloaded(entityIn, (World)this.theWorld);
/*      */     
/* 2928 */     if (Config.isDynamicLights())
/*      */     {
/* 2930 */       DynamicLights.entityRemoved(entityIn, this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void deleteAllDisplayLists() {}
/*      */ 
/*      */   
/*      */   public void broadcastSound(int soundID, BlockPos pos, int data) {
/* 2940 */     switch (soundID) {
/*      */       
/*      */       case 1013:
/*      */       case 1018:
/* 2944 */         if (this.mc.getRenderViewEntity() != null) {
/*      */           
/* 2946 */           double d0 = pos.getX() - (this.mc.getRenderViewEntity()).posX;
/* 2947 */           double d1 = pos.getY() - (this.mc.getRenderViewEntity()).posY;
/* 2948 */           double d2 = pos.getZ() - (this.mc.getRenderViewEntity()).posZ;
/* 2949 */           double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
/* 2950 */           double d4 = (this.mc.getRenderViewEntity()).posX;
/* 2951 */           double d5 = (this.mc.getRenderViewEntity()).posY;
/* 2952 */           double d6 = (this.mc.getRenderViewEntity()).posZ;
/*      */           
/* 2954 */           if (d3 > 0.0D) {
/*      */             
/* 2956 */             d4 += d0 / d3 * 2.0D;
/* 2957 */             d5 += d1 / d3 * 2.0D;
/* 2958 */             d6 += d2 / d3 * 2.0D;
/*      */           } 
/*      */           
/* 2961 */           if (soundID == 1013) {
/*      */             
/* 2963 */             this.theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0F, 1.0F, false);
/*      */             
/*      */             break;
/*      */           } 
/* 2967 */           this.theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5.0F, 1.0F, false);
/*      */         }  break;
/*      */     }  } public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int data) { int i, j; double d0, d1, d2; int i1; Block block; double d3, d4, d5; int k, j1;
/*      */     float f, f1, f2;
/*      */     EnumParticleTypes enumparticletypes;
/*      */     int k1;
/*      */     double d6, d8, d10;
/*      */     int l1;
/*      */     double d22;
/*      */     int l;
/* 2977 */     Random random = this.theWorld.rand;
/*      */     
/* 2979 */     switch (sfxType) {
/*      */       
/*      */       case 1000:
/* 2982 */         this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1001:
/* 2986 */         this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.2F, false);
/*      */         break;
/*      */       
/*      */       case 1002:
/* 2990 */         this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0F, 1.2F, false);
/*      */         break;
/*      */       
/*      */       case 1003:
/* 2994 */         this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/*      */         break;
/*      */       
/*      */       case 1004:
/* 2998 */         this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
/*      */         break;
/*      */       
/*      */       case 1005:
/* 3002 */         if (Item.getItemById(data) instanceof ItemRecord) {
/*      */           
/* 3004 */           this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById(data)).recordName);
/*      */           
/*      */           break;
/*      */         } 
/* 3008 */         this.theWorld.playRecord(blockPosIn, (String)null);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 1006:
/* 3014 */         this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/*      */         break;
/*      */       
/*      */       case 1007:
/* 3018 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1008:
/* 3022 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1009:
/* 3026 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1010:
/* 3030 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1011:
/* 3034 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1012:
/* 3038 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1014:
/* 3042 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1015:
/* 3046 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1016:
/* 3050 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1017:
/* 3054 */         this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
/*      */         break;
/*      */       
/*      */       case 1020:
/* 3058 */         this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/*      */         break;
/*      */       
/*      */       case 1021:
/* 3062 */         this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/*      */         break;
/*      */       
/*      */       case 1022:
/* 3066 */         this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/*      */         break;
/*      */       
/*      */       case 2000:
/* 3070 */         i = data % 3 - 1;
/* 3071 */         j = data / 3 % 3 - 1;
/* 3072 */         d0 = blockPosIn.getX() + i * 0.6D + 0.5D;
/* 3073 */         d1 = blockPosIn.getY() + 0.5D;
/* 3074 */         d2 = blockPosIn.getZ() + j * 0.6D + 0.5D;
/*      */         
/* 3076 */         for (i1 = 0; i1 < 10; i1++) {
/*      */           
/* 3078 */           double d15 = random.nextDouble() * 0.2D + 0.01D;
/* 3079 */           double d16 = d0 + i * 0.01D + (random.nextDouble() - 0.5D) * j * 0.5D;
/* 3080 */           double d17 = d1 + (random.nextDouble() - 0.5D) * 0.5D;
/* 3081 */           double d18 = d2 + j * 0.01D + (random.nextDouble() - 0.5D) * i * 0.5D;
/* 3082 */           double d19 = i * d15 + random.nextGaussian() * 0.01D;
/* 3083 */           double d20 = -0.03D + random.nextGaussian() * 0.01D;
/* 3084 */           double d21 = j * d15 + random.nextGaussian() * 0.01D;
/* 3085 */           spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d16, d17, d18, d19, d20, d21, new int[0]);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2001:
/* 3091 */         block = Block.getBlockById(data & 0xFFF);
/*      */         
/* 3093 */         if (block.getMaterial() != Material.air)
/*      */         {
/* 3095 */           this.mc.getSoundHandler().playSound((ISound)new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getFrequency() * 0.8F, blockPosIn.getX() + 0.5F, blockPosIn.getY() + 0.5F, blockPosIn.getZ() + 0.5F));
/*      */         }
/*      */         
/* 3098 */         this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(data >> 12 & 0xFF));
/*      */         break;
/*      */       
/*      */       case 2002:
/* 3102 */         d3 = blockPosIn.getX();
/* 3103 */         d4 = blockPosIn.getY();
/* 3104 */         d5 = blockPosIn.getZ();
/*      */         
/* 3106 */         for (k = 0; k < 8; k++) {
/*      */           
/* 3108 */           spawnParticle(EnumParticleTypes.ITEM_CRACK, d3, d4, d5, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem((Item)Items.potionitem), data });
/*      */         } 
/*      */         
/* 3111 */         j1 = Items.potionitem.getColorFromDamage(data);
/* 3112 */         f = (j1 >> 16 & 0xFF) / 255.0F;
/* 3113 */         f1 = (j1 >> 8 & 0xFF) / 255.0F;
/* 3114 */         f2 = (j1 >> 0 & 0xFF) / 255.0F;
/* 3115 */         enumparticletypes = EnumParticleTypes.SPELL;
/*      */         
/* 3117 */         if (Items.potionitem.isEffectInstant(data))
/*      */         {
/* 3119 */           enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
/*      */         }
/*      */         
/* 3122 */         for (k1 = 0; k1 < 100; k1++) {
/*      */           
/* 3124 */           double d7 = random.nextDouble() * 4.0D;
/* 3125 */           double d9 = random.nextDouble() * Math.PI * 2.0D;
/* 3126 */           double d11 = Math.cos(d9) * d7;
/* 3127 */           double d23 = 0.01D + random.nextDouble() * 0.5D;
/* 3128 */           double d24 = Math.sin(d9) * d7;
/* 3129 */           EntityFX entityfx = spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d3 + d11 * 0.1D, d4 + 0.3D, d5 + d24 * 0.1D, d11, d23, d24, new int[0]);
/*      */           
/* 3131 */           if (entityfx != null) {
/*      */             
/* 3133 */             float f3 = 0.75F + random.nextFloat() * 0.25F;
/* 3134 */             entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
/* 3135 */             entityfx.multiplyVelocity((float)d7);
/*      */           } 
/*      */         } 
/*      */         
/* 3139 */         this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
/*      */         break;
/*      */       
/*      */       case 2003:
/* 3143 */         d6 = blockPosIn.getX() + 0.5D;
/* 3144 */         d8 = blockPosIn.getY();
/* 3145 */         d10 = blockPosIn.getZ() + 0.5D;
/*      */         
/* 3147 */         for (l1 = 0; l1 < 8; l1++) {
/*      */           
/* 3149 */           spawnParticle(EnumParticleTypes.ITEM_CRACK, d6, d8, d10, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.ender_eye) });
/*      */         } 
/*      */         
/* 3152 */         for (d22 = 0.0D; d22 < 6.283185307179586D; d22 += 0.15707963267948966D) {
/*      */           
/* 3154 */           spawnParticle(EnumParticleTypes.PORTAL, d6 + Math.cos(d22) * 5.0D, d8 - 0.4D, d10 + Math.sin(d22) * 5.0D, Math.cos(d22) * -5.0D, 0.0D, Math.sin(d22) * -5.0D, new int[0]);
/* 3155 */           spawnParticle(EnumParticleTypes.PORTAL, d6 + Math.cos(d22) * 5.0D, d8 - 0.4D, d10 + Math.sin(d22) * 5.0D, Math.cos(d22) * -7.0D, 0.0D, Math.sin(d22) * -7.0D, new int[0]);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2004:
/* 3161 */         for (l = 0; l < 20; l++) {
/*      */           
/* 3163 */           double d12 = blockPosIn.getX() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
/* 3164 */           double d13 = blockPosIn.getY() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
/* 3165 */           double d14 = blockPosIn.getZ() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
/* 3166 */           this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d12, d13, d14, 0.0D, 0.0D, 0.0D, new int[0]);
/* 3167 */           this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d12, d13, d14, 0.0D, 0.0D, 0.0D, new int[0]);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2005:
/* 3173 */         ItemDye.spawnBonemealParticles((World)this.theWorld, blockPosIn, data);
/*      */         break;
/*      */     }  }
/*      */ 
/*      */   
/*      */   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
/* 3179 */     if (progress >= 0 && progress < 10) {
/*      */       
/* 3181 */       DestroyBlockProgress destroyblockprogress = this.damagedBlocks.get(Integer.valueOf(breakerId));
/*      */       
/* 3183 */       if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ()) {
/*      */         
/* 3185 */         destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
/* 3186 */         this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
/*      */       } 
/*      */       
/* 3189 */       destroyblockprogress.setPartialBlockDamage(progress);
/* 3190 */       destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
/*      */     }
/*      */     else {
/*      */       
/* 3194 */       this.damagedBlocks.remove(Integer.valueOf(breakerId));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDisplayListEntitiesDirty() {
/* 3200 */     this.displayListEntitiesDirty = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasNoChunkUpdates() {
/* 3205 */     return (this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasChunkUpdates());
/*      */   }
/*      */ 
/*      */   
/*      */   public void resetClouds() {
/* 3210 */     this.cloudRenderer.reset();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCountRenderers() {
/* 3215 */     return this.viewFrustum.renderChunks.length;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCountActiveRenderers() {
/* 3220 */     return this.renderInfos.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCountEntitiesRendered() {
/* 3225 */     return this.countEntitiesRendered;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCountTileEntitiesRendered() {
/* 3230 */     return this.countTileEntitiesRendered;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getCountLoadedChunks() {
/* 3235 */     if (this.theWorld == null)
/*      */     {
/* 3237 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 3241 */     IChunkProvider ichunkprovider = this.theWorld.getChunkProvider();
/*      */     
/* 3243 */     if (ichunkprovider == null)
/*      */     {
/* 3245 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 3249 */     if (ichunkprovider != this.worldChunkProvider) {
/*      */       
/* 3251 */       this.worldChunkProvider = ichunkprovider;
/* 3252 */       this.worldChunkProviderMap = (LongHashMap)Reflector.getFieldValue(ichunkprovider, Reflector.ChunkProviderClient_chunkMapping);
/*      */     } 
/*      */     
/* 3255 */     return (this.worldChunkProviderMap == null) ? 0 : this.worldChunkProviderMap.getNumHashElements();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCountChunksToUpdate() {
/* 3262 */     return this.chunksToUpdate.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public RenderChunk getRenderChunk(BlockPos p_getRenderChunk_1_) {
/* 3267 */     return this.viewFrustum.getRenderChunk(p_getRenderChunk_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public WorldClient getWorld() {
/* 3272 */     return this.theWorld;
/*      */   }
/*      */ 
/*      */   
/*      */   private void clearRenderInfos() {
/* 3277 */     if (renderEntitiesCounter > 0) {
/*      */       
/* 3279 */       this.renderInfos = new ArrayList<>(this.renderInfos.size() + 16);
/* 3280 */       this.renderInfosEntities = new ArrayList(this.renderInfosEntities.size() + 16);
/* 3281 */       this.renderInfosTileEntities = new ArrayList(this.renderInfosTileEntities.size() + 16);
/*      */     }
/*      */     else {
/*      */       
/* 3285 */       this.renderInfos.clear();
/* 3286 */       this.renderInfosEntities.clear();
/* 3287 */       this.renderInfosTileEntities.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onPlayerPositionSet() {
/* 3293 */     if (this.firstWorldLoad) {
/*      */       
/* 3295 */       loadRenderers();
/* 3296 */       this.firstWorldLoad = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void pauseChunkUpdates() {
/* 3302 */     if (this.renderDispatcher != null)
/*      */     {
/* 3304 */       this.renderDispatcher.pauseChunkUpdates();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void resumeChunkUpdates() {
/* 3310 */     if (this.renderDispatcher != null)
/*      */     {
/* 3312 */       this.renderDispatcher.resumeChunkUpdates();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd) {
/* 3318 */     synchronized (this.setTileEntities) {
/*      */       
/* 3320 */       this.setTileEntities.removeAll(tileEntitiesToRemove);
/* 3321 */       this.setTileEntities.addAll(tileEntitiesToAdd);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ContainerLocalRenderInformation
/*      */   {
/*      */     final RenderChunk renderChunk;
/*      */     EnumFacing facing;
/*      */     int setFacing;
/*      */     
/*      */     public ContainerLocalRenderInformation(RenderChunk p_i2_1_, EnumFacing p_i2_2_, int p_i2_3_) {
/* 3333 */       this.renderChunk = p_i2_1_;
/* 3334 */       this.facing = p_i2_2_;
/* 3335 */       this.setFacing = p_i2_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setFacingBit(byte p_setFacingBit_1_, EnumFacing p_setFacingBit_2_) {
/* 3340 */       this.setFacing = this.setFacing | p_setFacingBit_1_ | 1 << p_setFacingBit_2_.ordinal();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isFacingBit(EnumFacing p_isFacingBit_1_) {
/* 3345 */       return ((this.setFacing & 1 << p_isFacingBit_1_.ordinal()) > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     private void initialize(EnumFacing p_initialize_1_, int p_initialize_2_) {
/* 3350 */       this.facing = p_initialize_1_;
/* 3351 */       this.setFacing = p_initialize_2_;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\RenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */