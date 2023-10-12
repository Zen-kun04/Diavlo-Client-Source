/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelBiped;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.optifine.entity.model.IEntityRenderer;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public abstract class Render<T extends Entity>
/*     */   implements IEntityRenderer
/*     */ {
/*  31 */   private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
/*     */   protected final RenderManager renderManager;
/*     */   public float shadowSize;
/*  34 */   protected float shadowOpaque = 1.0F;
/*  35 */   private Class entityClass = null;
/*  36 */   private ResourceLocation locationTextureCustom = null;
/*     */ 
/*     */   
/*     */   protected Render(RenderManager renderManager) {
/*  40 */     this.renderManager = renderManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(T livingEntity, ICamera camera, double camX, double camY, double camZ) {
/*  45 */     AxisAlignedBB axisalignedbb = livingEntity.getEntityBoundingBox();
/*     */     
/*  47 */     if (axisalignedbb.hasNaN() || axisalignedbb.getAverageEdgeLength() == 0.0D)
/*     */     {
/*  49 */       axisalignedbb = new AxisAlignedBB(((Entity)livingEntity).posX - 2.0D, ((Entity)livingEntity).posY - 2.0D, ((Entity)livingEntity).posZ - 2.0D, ((Entity)livingEntity).posX + 2.0D, ((Entity)livingEntity).posY + 2.0D, ((Entity)livingEntity).posZ + 2.0D);
/*     */     }
/*     */     
/*  52 */     return (livingEntity.isInRangeToRender3d(camX, camY, camZ) && (((Entity)livingEntity).ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  57 */     renderName(entity, x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderName(T entity, double x, double y, double z) {
/*  62 */     if (canRenderName(entity))
/*     */     {
/*  64 */       renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRenderName(T entity) {
/*  70 */     return (entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderOffsetLivingLabel(T entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
/*  75 */     renderLivingLabel(entityIn, str, x, y, z, 64);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract ResourceLocation getEntityTexture(T paramT);
/*     */   
/*     */   protected boolean bindEntityTexture(T entity) {
/*  82 */     ResourceLocation resourcelocation = getEntityTexture(entity);
/*     */     
/*  84 */     if (this.locationTextureCustom != null)
/*     */     {
/*  86 */       resourcelocation = this.locationTextureCustom;
/*     */     }
/*     */     
/*  89 */     if (resourcelocation == null)
/*     */     {
/*  91 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  95 */     bindTexture(resourcelocation);
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bindTexture(ResourceLocation location) {
/* 102 */     this.renderManager.renderEngine.bindTexture(location);
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks) {
/* 107 */     GlStateManager.disableLighting();
/* 108 */     TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
/* 109 */     TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
/* 110 */     TextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");
/* 111 */     GlStateManager.pushMatrix();
/* 112 */     GlStateManager.translate((float)x, (float)y, (float)z);
/* 113 */     float f = entity.width * 1.4F;
/* 114 */     GlStateManager.scale(f, f, f);
/* 115 */     Tessellator tessellator = Tessellator.getInstance();
/* 116 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 117 */     float f1 = 0.5F;
/* 118 */     float f2 = 0.0F;
/* 119 */     float f3 = entity.height / f;
/* 120 */     float f4 = (float)(entity.posY - (entity.getEntityBoundingBox()).minY);
/* 121 */     GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 122 */     GlStateManager.translate(0.0F, 0.0F, -0.3F + (int)f3 * 0.02F);
/* 123 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 124 */     float f5 = 0.0F;
/* 125 */     int i = 0;
/* 126 */     boolean flag = Config.isMultiTexture();
/*     */     
/* 128 */     if (flag)
/*     */     {
/* 130 */       worldrenderer.setBlockLayer(EnumWorldBlockLayer.SOLID);
/*     */     }
/*     */     
/* 133 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/*     */     
/* 135 */     while (f3 > 0.0F) {
/*     */       
/* 137 */       TextureAtlasSprite textureatlassprite2 = (i % 2 == 0) ? textureatlassprite : textureatlassprite1;
/* 138 */       worldrenderer.setSprite(textureatlassprite2);
/* 139 */       bindTexture(TextureMap.locationBlocksTexture);
/* 140 */       float f6 = textureatlassprite2.getMinU();
/* 141 */       float f7 = textureatlassprite2.getMinV();
/* 142 */       float f8 = textureatlassprite2.getMaxU();
/* 143 */       float f9 = textureatlassprite2.getMaxV();
/*     */       
/* 145 */       if (i / 2 % 2 == 0) {
/*     */         
/* 147 */         float f10 = f8;
/* 148 */         f8 = f6;
/* 149 */         f6 = f10;
/*     */       } 
/*     */       
/* 152 */       worldrenderer.pos((f1 - f2), (0.0F - f4), f5).tex(f8, f9).endVertex();
/* 153 */       worldrenderer.pos((-f1 - f2), (0.0F - f4), f5).tex(f6, f9).endVertex();
/* 154 */       worldrenderer.pos((-f1 - f2), (1.4F - f4), f5).tex(f6, f7).endVertex();
/* 155 */       worldrenderer.pos((f1 - f2), (1.4F - f4), f5).tex(f8, f7).endVertex();
/* 156 */       f3 -= 0.45F;
/* 157 */       f4 -= 0.45F;
/* 158 */       f1 *= 0.9F;
/* 159 */       f5 += 0.03F;
/* 160 */       i++;
/*     */     } 
/*     */     
/* 163 */     tessellator.draw();
/*     */     
/* 165 */     if (flag) {
/*     */       
/* 167 */       worldrenderer.setBlockLayer((EnumWorldBlockLayer)null);
/* 168 */       GlStateManager.bindCurrentTexture();
/*     */     } 
/*     */     
/* 171 */     GlStateManager.popMatrix();
/* 172 */     GlStateManager.enableLighting();
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks) {
/* 177 */     if (!Config.isShaders() || !Shaders.shouldSkipDefaultShadow) {
/*     */       
/* 179 */       GlStateManager.enableBlend();
/* 180 */       GlStateManager.blendFunc(770, 771);
/* 181 */       this.renderManager.renderEngine.bindTexture(shadowTextures);
/* 182 */       World world = getWorldFromRenderManager();
/* 183 */       GlStateManager.depthMask(false);
/* 184 */       float f = this.shadowSize;
/*     */       
/* 186 */       if (entityIn instanceof EntityLiving) {
/*     */         
/* 188 */         EntityLiving entityliving = (EntityLiving)entityIn;
/* 189 */         f *= entityliving.getRenderSizeModifier();
/*     */         
/* 191 */         if (entityliving.isChild())
/*     */         {
/* 193 */           f *= 0.5F;
/*     */         }
/*     */       } 
/*     */       
/* 197 */       double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 198 */       double d0 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 199 */       double d1 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 200 */       int i = MathHelper.floor_double(d5 - f);
/* 201 */       int j = MathHelper.floor_double(d5 + f);
/* 202 */       int k = MathHelper.floor_double(d0 - f);
/* 203 */       int l = MathHelper.floor_double(d0);
/* 204 */       int i1 = MathHelper.floor_double(d1 - f);
/* 205 */       int j1 = MathHelper.floor_double(d1 + f);
/* 206 */       double d2 = x - d5;
/* 207 */       double d3 = y - d0;
/* 208 */       double d4 = z - d1;
/* 209 */       Tessellator tessellator = Tessellator.getInstance();
/* 210 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 211 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/*     */       
/* 213 */       for (BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(i, k, i1), new BlockPos(j, l, j1))) {
/*     */         
/* 215 */         Block block = world.getBlockState(blockpos.down()).getBlock();
/*     */         
/* 217 */         if (block.getRenderType() != -1 && world.getLightFromNeighbors(blockpos) > 3)
/*     */         {
/* 219 */           renderShadowBlock(block, x, y, z, blockpos, shadowAlpha, f, d2, d3, d4);
/*     */         }
/*     */       } 
/*     */       
/* 223 */       tessellator.draw();
/* 224 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 225 */       GlStateManager.disableBlend();
/* 226 */       GlStateManager.depthMask(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private World getWorldFromRenderManager() {
/* 232 */     return this.renderManager.worldObj;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderShadowBlock(Block blockIn, double p_180549_2_, double p_180549_4_, double p_180549_6_, BlockPos pos, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_) {
/* 237 */     if (blockIn.isFullCube()) {
/*     */       
/* 239 */       Tessellator tessellator = Tessellator.getInstance();
/* 240 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 241 */       double d0 = (p_180549_9_ - (p_180549_4_ - pos.getY() + p_180549_13_) / 2.0D) * 0.5D * getWorldFromRenderManager().getLightBrightness(pos);
/*     */       
/* 243 */       if (d0 >= 0.0D) {
/*     */         
/* 245 */         if (d0 > 1.0D)
/*     */         {
/* 247 */           d0 = 1.0D;
/*     */         }
/*     */         
/* 250 */         double d1 = pos.getX() + blockIn.getBlockBoundsMinX() + p_180549_11_;
/* 251 */         double d2 = pos.getX() + blockIn.getBlockBoundsMaxX() + p_180549_11_;
/* 252 */         double d3 = pos.getY() + blockIn.getBlockBoundsMinY() + p_180549_13_ + 0.015625D;
/* 253 */         double d4 = pos.getZ() + blockIn.getBlockBoundsMinZ() + p_180549_15_;
/* 254 */         double d5 = pos.getZ() + blockIn.getBlockBoundsMaxZ() + p_180549_15_;
/* 255 */         float f = (float)((p_180549_2_ - d1) / 2.0D / p_180549_10_ + 0.5D);
/* 256 */         float f1 = (float)((p_180549_2_ - d2) / 2.0D / p_180549_10_ + 0.5D);
/* 257 */         float f2 = (float)((p_180549_6_ - d4) / 2.0D / p_180549_10_ + 0.5D);
/* 258 */         float f3 = (float)((p_180549_6_ - d5) / 2.0D / p_180549_10_ + 0.5D);
/* 259 */         worldrenderer.pos(d1, d3, d4).tex(f, f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/* 260 */         worldrenderer.pos(d1, d3, d5).tex(f, f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/* 261 */         worldrenderer.pos(d2, d3, d5).tex(f1, f3).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/* 262 */         worldrenderer.pos(d2, d3, d4).tex(f1, f2).color(1.0F, 1.0F, 1.0F, (float)d0).endVertex();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z) {
/* 269 */     GlStateManager.disableTexture2D();
/* 270 */     Tessellator tessellator = Tessellator.getInstance();
/* 271 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 272 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 273 */     worldrenderer.setTranslation(x, y, z);
/* 274 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
/* 275 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 276 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 277 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 278 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, 0.0F, -1.0F).endVertex();
/* 279 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 280 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 281 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 282 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 0.0F, 1.0F).endVertex();
/* 283 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 284 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 285 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 286 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(0.0F, -1.0F, 0.0F).endVertex();
/* 287 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 288 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 289 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 290 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
/* 291 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 292 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 293 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 294 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).normal(-1.0F, 0.0F, 0.0F).endVertex();
/* 295 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 296 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 297 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 298 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).normal(1.0F, 0.0F, 0.0F).endVertex();
/* 299 */     tessellator.draw();
/* 300 */     worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
/* 301 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
/* 306 */     if (this.renderManager.options != null) {
/*     */       
/* 308 */       if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
/*     */         
/* 310 */         double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
/* 311 */         float f = (float)((1.0D - d0 / 256.0D) * this.shadowOpaque);
/*     */         
/* 313 */         if (f > 0.0F)
/*     */         {
/* 315 */           renderShadow(entityIn, x, y, z, f, partialTicks);
/*     */         }
/*     */       } 
/*     */       
/* 319 */       if (entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).isSpectator()))
/*     */       {
/* 321 */         renderEntityOnFire(entityIn, x, y, z, partialTicks);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public FontRenderer getFontRendererFromRenderManager() {
/* 328 */     return this.renderManager.getFontRenderer();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
/* 333 */     double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
/*     */     
/* 335 */     if (d0 <= (maxDistance * maxDistance)) {
/*     */       
/* 337 */       FontRenderer fontrenderer = getFontRendererFromRenderManager();
/* 338 */       float f = 1.6F;
/* 339 */       float f1 = 0.016666668F * f;
/* 340 */       GlStateManager.pushMatrix();
/* 341 */       GlStateManager.translate((float)x + 0.0F, (float)y + ((Entity)entityIn).height + 0.5F, (float)z);
/* 342 */       GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 343 */       GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 344 */       GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 345 */       GlStateManager.scale(-f1, -f1, f1);
/* 346 */       GlStateManager.disableLighting();
/* 347 */       GlStateManager.depthMask(false);
/* 348 */       GlStateManager.disableDepth();
/* 349 */       GlStateManager.enableBlend();
/* 350 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 351 */       Tessellator tessellator = Tessellator.getInstance();
/* 352 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 353 */       int i = 0;
/*     */       
/* 355 */       if (str.equals("deadmau5"))
/*     */       {
/* 357 */         i = -10;
/*     */       }
/*     */       
/* 360 */       int j = fontrenderer.getStringWidth(str) / 2;
/* 361 */       GlStateManager.disableTexture2D();
/* 362 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 363 */       worldrenderer.pos((-j - 1), (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 364 */       worldrenderer.pos((-j - 1), (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 365 */       worldrenderer.pos((j + 1), (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 366 */       worldrenderer.pos((j + 1), (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 367 */       tessellator.draw();
/* 368 */       GlStateManager.enableTexture2D();
/* 369 */       fontrenderer.drawString(str, (-fontrenderer.getStringWidth(str) / 2), i, 553648127);
/* 370 */       GlStateManager.enableDepth();
/* 371 */       GlStateManager.depthMask(true);
/* 372 */       fontrenderer.drawString(str, (-fontrenderer.getStringWidth(str) / 2), i, -1);
/* 373 */       GlStateManager.enableLighting();
/* 374 */       GlStateManager.disableBlend();
/* 375 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 376 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderManager getRenderManager() {
/* 382 */     return this.renderManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMultipass() {
/* 387 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderMultipass(T p_renderMultipass_1_, double p_renderMultipass_2_, double p_renderMultipass_4_, double p_renderMultipass_6_, float p_renderMultipass_8_, float p_renderMultipass_9_) {}
/*     */ 
/*     */   
/*     */   public Class getEntityClass() {
/* 396 */     return this.entityClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityClass(Class p_setEntityClass_1_) {
/* 401 */     this.entityClass = p_setEntityClass_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResourceLocation getLocationTextureCustom() {
/* 406 */     return this.locationTextureCustom;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocationTextureCustom(ResourceLocation p_setLocationTextureCustom_1_) {
/* 411 */     this.locationTextureCustom = p_setLocationTextureCustom_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setModelBipedMain(RenderBiped p_setModelBipedMain_0_, ModelBiped p_setModelBipedMain_1_) {
/* 416 */     p_setModelBipedMain_0_.modelBipedMain = p_setModelBipedMain_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\Render.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */