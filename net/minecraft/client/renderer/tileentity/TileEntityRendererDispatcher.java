/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.tileentity.TileEntityBeacon;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityEnchantmentTable;
/*     */ import net.minecraft.tileentity.TileEntityEndPortal;
/*     */ import net.minecraft.tileentity.TileEntityEnderChest;
/*     */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*     */ import net.minecraft.tileentity.TileEntityPiston;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.World;
/*     */ import net.optifine.EmissiveTextures;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ public class TileEntityRendererDispatcher
/*     */ {
/*  36 */   public Map<Class, TileEntitySpecialRenderer> mapSpecialRenderers = Maps.newHashMap();
/*  37 */   public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
/*     */   public FontRenderer fontRenderer;
/*     */   public static double staticPlayerX;
/*     */   public static double staticPlayerY;
/*     */   public static double staticPlayerZ;
/*     */   public TextureManager renderEngine;
/*     */   public World worldObj;
/*     */   public Entity entity;
/*     */   public float entityYaw;
/*     */   public float entityPitch;
/*     */   public double entityX;
/*     */   public double entityY;
/*     */   public double entityZ;
/*     */   public TileEntity tileEntityRendered;
/*  51 */   private Tessellator batchBuffer = new Tessellator(2097152);
/*     */   
/*     */   private boolean drawingBatch = false;
/*     */   
/*     */   private TileEntityRendererDispatcher() {
/*  56 */     this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
/*  57 */     this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
/*  58 */     this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
/*  59 */     this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
/*  60 */     this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
/*  61 */     this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
/*  62 */     this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
/*  63 */     this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
/*  64 */     this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
/*  65 */     this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
/*     */     
/*  67 */     for (TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values())
/*     */     {
/*  69 */       tileentityspecialrenderer.setRendererDispatcher(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class<? extends TileEntity> teClass) {
/*  75 */     TileEntitySpecialRenderer<? extends TileEntity> tileentityspecialrenderer = this.mapSpecialRenderers.get(teClass);
/*     */     
/*  77 */     if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
/*     */       
/*  79 */       tileentityspecialrenderer = getSpecialRendererByClass((Class)teClass.getSuperclass());
/*  80 */       this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
/*     */     } 
/*     */     
/*  83 */     return (TileEntitySpecialRenderer)tileentityspecialrenderer;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntityIn) {
/*  88 */     return (tileEntityIn != null && !tileEntityIn.isInvalid()) ? getSpecialRendererByClass((Class)tileEntityIn.getClass()) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cacheActiveRenderInfo(World worldIn, TextureManager textureManagerIn, FontRenderer fontrendererIn, Entity entityIn, float partialTicks) {
/*  93 */     if (this.worldObj != worldIn)
/*     */     {
/*  95 */       setWorld(worldIn);
/*     */     }
/*     */     
/*  98 */     this.renderEngine = textureManagerIn;
/*  99 */     this.entity = entityIn;
/* 100 */     this.fontRenderer = fontrendererIn;
/* 101 */     this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
/* 102 */     this.entityPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
/* 103 */     this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 104 */     this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 105 */     this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage) {
/* 110 */     if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared()) {
/*     */       
/* 112 */       boolean flag = true;
/*     */       
/* 114 */       if (Reflector.ForgeTileEntity_hasFastRenderer.exists())
/*     */       {
/* 116 */         flag = (!this.drawingBatch || !Reflector.callBoolean(tileentityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0]));
/*     */       }
/*     */       
/* 119 */       if (flag) {
/*     */         
/* 121 */         RenderHelper.enableStandardItemLighting();
/* 122 */         int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
/* 123 */         int j = i % 65536;
/* 124 */         int k = i / 65536;
/* 125 */         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 126 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       } 
/*     */       
/* 129 */       BlockPos blockpos = tileentityIn.getPos();
/*     */       
/* 131 */       if (!this.worldObj.isBlockLoaded(blockpos, false)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 136 */       if (EmissiveTextures.isActive())
/*     */       {
/* 138 */         EmissiveTextures.beginRender();
/*     */       }
/*     */       
/* 141 */       renderTileEntityAt(tileentityIn, blockpos.getX() - staticPlayerX, blockpos.getY() - staticPlayerY, blockpos.getZ() - staticPlayerZ, partialTicks, destroyStage);
/*     */       
/* 143 */       if (EmissiveTextures.isActive()) {
/*     */         
/* 145 */         if (EmissiveTextures.hasEmissive()) {
/*     */           
/* 147 */           EmissiveTextures.beginRenderEmissive();
/* 148 */           renderTileEntityAt(tileentityIn, blockpos.getX() - staticPlayerX, blockpos.getY() - staticPlayerY, blockpos.getZ() - staticPlayerZ, partialTicks, destroyStage);
/* 149 */           EmissiveTextures.endRenderEmissive();
/*     */         } 
/*     */         
/* 152 */         EmissiveTextures.endRender();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks) {
/* 159 */     renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
/* 164 */     TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = getSpecialRenderer(tileEntityIn);
/*     */     
/* 166 */     if (tileentityspecialrenderer != null) {
/*     */       
/*     */       try {
/*     */         
/* 170 */         this.tileEntityRendered = tileEntityIn;
/*     */         
/* 172 */         if (this.drawingBatch && Reflector.callBoolean(tileEntityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0])) {
/*     */           
/* 174 */           tileentityspecialrenderer.renderTileEntityFast(tileEntityIn, x, y, z, partialTicks, destroyStage, this.batchBuffer.getWorldRenderer());
/*     */         }
/*     */         else {
/*     */           
/* 178 */           tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
/*     */         } 
/*     */         
/* 181 */         this.tileEntityRendered = null;
/*     */       }
/* 183 */       catch (Throwable throwable) {
/*     */         
/* 185 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
/* 186 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
/* 187 */         tileEntityIn.addInfoToCrashReport(crashreportcategory);
/* 188 */         throw new ReportedException(crashreport);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorld(World worldIn) {
/* 195 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public FontRenderer getFontRenderer() {
/* 200 */     return this.fontRenderer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void preDrawBatch() {
/* 205 */     this.batchBuffer.getWorldRenderer().begin(7, DefaultVertexFormats.BLOCK);
/* 206 */     this.drawingBatch = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawBatch(int p_drawBatch_1_) {
/* 211 */     this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
/* 212 */     RenderHelper.disableStandardItemLighting();
/* 213 */     GlStateManager.blendFunc(770, 771);
/* 214 */     GlStateManager.enableBlend();
/* 215 */     GlStateManager.disableCull();
/*     */     
/* 217 */     if (Minecraft.isAmbientOcclusionEnabled()) {
/*     */       
/* 219 */       GlStateManager.shadeModel(7425);
/*     */     }
/*     */     else {
/*     */       
/* 223 */       GlStateManager.shadeModel(7424);
/*     */     } 
/*     */     
/* 226 */     if (p_drawBatch_1_ > 0)
/*     */     {
/* 228 */       this.batchBuffer.getWorldRenderer().sortVertexData((float)staticPlayerX, (float)staticPlayerY, (float)staticPlayerZ);
/*     */     }
/*     */     
/* 231 */     this.batchBuffer.draw();
/* 232 */     RenderHelper.enableStandardItemLighting();
/* 233 */     this.drawingBatch = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\tileentity\TileEntityRendererDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */