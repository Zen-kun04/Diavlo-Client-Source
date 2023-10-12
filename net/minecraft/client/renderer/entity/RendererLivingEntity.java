/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.optifine.EmissiveTextures;
/*     */ import net.optifine.entity.model.CustomEntityModels;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public abstract class RendererLivingEntity<T extends EntityLivingBase>
/*     */   extends Render<T> {
/*  37 */   private static final Logger logger = LogManager.getLogger();
/*  38 */   private static final DynamicTexture textureBrightness = new DynamicTexture(16, 16);
/*     */   public ModelBase mainModel;
/*  40 */   protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
/*  41 */   protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
/*     */   protected boolean renderOutlines = false;
/*  43 */   public static float NAME_TAG_RANGE = 64.0F;
/*  44 */   public static float NAME_TAG_RANGE_SNEAK = 32.0F;
/*     */   public EntityLivingBase renderEntity;
/*     */   public float renderLimbSwing;
/*     */   public float renderLimbSwingAmount;
/*     */   public float renderAgeInTicks;
/*     */   public float renderHeadYaw;
/*     */   public float renderHeadPitch;
/*     */   public float renderScaleFactor;
/*     */   public float renderPartialTicks;
/*     */   private boolean renderModelPushMatrix;
/*     */   private boolean renderLayersPushMatrix;
/*  55 */   public static final boolean animateModelLiving = Boolean.getBoolean("animate.model.living");
/*     */ 
/*     */   
/*     */   public RendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
/*  59 */     super(renderManagerIn);
/*  60 */     this.mainModel = modelBaseIn;
/*  61 */     this.shadowSize = shadowSizeIn;
/*  62 */     this.renderModelPushMatrix = this.mainModel instanceof net.minecraft.client.model.ModelSpider;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U layer) {
/*  67 */     return this.layerRenderers.add((LayerRenderer<T>)layer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U layer) {
/*  72 */     return this.layerRenderers.remove(layer);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelBase getMainModel() {
/*  77 */     return this.mainModel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float interpolateRotation(float par1, float par2, float par3) {
/*     */     float f;
/*  84 */     for (f = par2 - par1; f < -180.0F; f += 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     while (f >= 180.0F)
/*     */     {
/*  91 */       f -= 360.0F;
/*     */     }
/*     */     
/*  94 */     return par1 + par3 * f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void transformHeldFull3DItemLayer() {}
/*     */ 
/*     */   
/*     */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 103 */     if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) })) {
/*     */       
/* 105 */       if (animateModelLiving)
/*     */       {
/* 107 */         ((EntityLivingBase)entity).limbSwingAmount = 1.0F;
/*     */       }
/*     */       
/* 110 */       GlStateManager.pushMatrix();
/* 111 */       GlStateManager.disableCull();
/* 112 */       this.mainModel.swingProgress = getSwingProgress(entity, partialTicks);
/* 113 */       this.mainModel.isRiding = entity.isRiding();
/*     */       
/* 115 */       if (Reflector.ForgeEntity_shouldRiderSit.exists())
/*     */       {
/* 117 */         this.mainModel.isRiding = (entity.isRiding() && ((EntityLivingBase)entity).ridingEntity != null && Reflector.callBoolean(((EntityLivingBase)entity).ridingEntity, Reflector.ForgeEntity_shouldRiderSit, new Object[0]));
/*     */       }
/*     */       
/* 120 */       this.mainModel.isChild = entity.isChild();
/*     */ 
/*     */       
/*     */       try {
/* 124 */         float f = interpolateRotation(((EntityLivingBase)entity).prevRenderYawOffset, ((EntityLivingBase)entity).renderYawOffset, partialTicks);
/* 125 */         float f1 = interpolateRotation(((EntityLivingBase)entity).prevRotationYawHead, ((EntityLivingBase)entity).rotationYawHead, partialTicks);
/* 126 */         float f2 = f1 - f;
/*     */         
/* 128 */         if (this.mainModel.isRiding && ((EntityLivingBase)entity).ridingEntity instanceof EntityLivingBase) {
/*     */           
/* 130 */           EntityLivingBase entitylivingbase = (EntityLivingBase)((EntityLivingBase)entity).ridingEntity;
/* 131 */           f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
/* 132 */           f2 = f1 - f;
/* 133 */           float f3 = MathHelper.wrapAngleTo180_float(f2);
/*     */           
/* 135 */           if (f3 < -85.0F)
/*     */           {
/* 137 */             f3 = -85.0F;
/*     */           }
/*     */           
/* 140 */           if (f3 >= 85.0F)
/*     */           {
/* 142 */             f3 = 85.0F;
/*     */           }
/*     */           
/* 145 */           f = f1 - f3;
/*     */           
/* 147 */           if (f3 * f3 > 2500.0F)
/*     */           {
/* 149 */             f += f3 * 0.2F;
/*     */           }
/*     */           
/* 152 */           f2 = f1 - f;
/*     */         } 
/*     */         
/* 155 */         float f7 = ((EntityLivingBase)entity).prevRotationPitch + (((EntityLivingBase)entity).rotationPitch - ((EntityLivingBase)entity).prevRotationPitch) * partialTicks;
/* 156 */         renderLivingAt(entity, x, y, z);
/* 157 */         float f8 = handleRotationFloat(entity, partialTicks);
/* 158 */         rotateCorpse(entity, f8, f, partialTicks);
/* 159 */         GlStateManager.enableRescaleNormal();
/* 160 */         GlStateManager.scale(-1.0F, -1.0F, 1.0F);
/* 161 */         preRenderCallback(entity, partialTicks);
/* 162 */         float f4 = 0.0625F;
/* 163 */         GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
/* 164 */         float f5 = ((EntityLivingBase)entity).prevLimbSwingAmount + (((EntityLivingBase)entity).limbSwingAmount - ((EntityLivingBase)entity).prevLimbSwingAmount) * partialTicks;
/* 165 */         float f6 = ((EntityLivingBase)entity).limbSwing - ((EntityLivingBase)entity).limbSwingAmount * (1.0F - partialTicks);
/*     */         
/* 167 */         if (entity.isChild())
/*     */         {
/* 169 */           f6 *= 3.0F;
/*     */         }
/*     */         
/* 172 */         if (f5 > 1.0F)
/*     */         {
/* 174 */           f5 = 1.0F;
/*     */         }
/*     */         
/* 177 */         GlStateManager.enableAlpha();
/* 178 */         this.mainModel.setLivingAnimations((EntityLivingBase)entity, f6, f5, partialTicks);
/* 179 */         this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, 0.0625F, (Entity)entity);
/*     */         
/* 181 */         if (CustomEntityModels.isActive()) {
/*     */           
/* 183 */           this.renderEntity = (EntityLivingBase)entity;
/* 184 */           this.renderLimbSwing = f6;
/* 185 */           this.renderLimbSwingAmount = f5;
/* 186 */           this.renderAgeInTicks = f8;
/* 187 */           this.renderHeadYaw = f2;
/* 188 */           this.renderHeadPitch = f7;
/* 189 */           this.renderScaleFactor = f4;
/* 190 */           this.renderPartialTicks = partialTicks;
/*     */         } 
/*     */         
/* 193 */         if (this.renderOutlines) {
/*     */           
/* 195 */           boolean flag1 = setScoreTeamColor(entity);
/* 196 */           renderModel(entity, f6, f5, f8, f2, f7, 0.0625F);
/*     */           
/* 198 */           if (flag1)
/*     */           {
/* 200 */             unsetScoreTeamColor();
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 205 */           boolean flag = setDoRenderBrightness(entity, partialTicks);
/*     */           
/* 207 */           if (EmissiveTextures.isActive())
/*     */           {
/* 209 */             EmissiveTextures.beginRender();
/*     */           }
/*     */           
/* 212 */           if (this.renderModelPushMatrix)
/*     */           {
/* 214 */             GlStateManager.pushMatrix();
/*     */           }
/*     */           
/* 217 */           renderModel(entity, f6, f5, f8, f2, f7, 0.0625F);
/*     */           
/* 219 */           if (this.renderModelPushMatrix)
/*     */           {
/* 221 */             GlStateManager.popMatrix();
/*     */           }
/*     */           
/* 224 */           if (EmissiveTextures.isActive()) {
/*     */             
/* 226 */             if (EmissiveTextures.hasEmissive()) {
/*     */               
/* 228 */               this.renderModelPushMatrix = true;
/* 229 */               EmissiveTextures.beginRenderEmissive();
/* 230 */               GlStateManager.pushMatrix();
/* 231 */               renderModel(entity, f6, f5, f8, f2, f7, f4);
/* 232 */               GlStateManager.popMatrix();
/* 233 */               EmissiveTextures.endRenderEmissive();
/*     */             } 
/*     */             
/* 236 */             EmissiveTextures.endRender();
/*     */           } 
/*     */           
/* 239 */           if (flag)
/*     */           {
/* 241 */             unsetBrightness();
/*     */           }
/*     */           
/* 244 */           GlStateManager.depthMask(true);
/*     */           
/* 246 */           if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator())
/*     */           {
/* 248 */             renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, 0.0625F);
/*     */           }
/*     */         } 
/*     */         
/* 252 */         if (CustomEntityModels.isActive())
/*     */         {
/* 254 */           this.renderEntity = null;
/*     */         }
/*     */         
/* 257 */         GlStateManager.disableRescaleNormal();
/*     */       }
/* 259 */       catch (Exception exception) {
/*     */         
/* 261 */         logger.error("Couldn't render entity", exception);
/*     */       } 
/*     */       
/* 264 */       GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 265 */       GlStateManager.enableTexture2D();
/* 266 */       GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 267 */       GlStateManager.enableCull();
/* 268 */       GlStateManager.popMatrix();
/*     */       
/* 270 */       if (!this.renderOutlines)
/*     */       {
/* 272 */         super.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */       }
/*     */       
/* 275 */       if (Reflector.RenderLivingEvent_Post_Constructor.exists())
/*     */       {
/* 277 */         Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean setScoreTeamColor(T entityLivingBaseIn) {
/* 284 */     int i = 16777215;
/*     */     
/* 286 */     if (entityLivingBaseIn instanceof EntityPlayer) {
/*     */       
/* 288 */       ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityLivingBaseIn.getTeam();
/*     */       
/* 290 */       if (scoreplayerteam != null) {
/*     */         
/* 292 */         String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
/*     */         
/* 294 */         if (s.length() >= 2)
/*     */         {
/* 296 */           i = getFontRendererFromRenderManager().getColorCode(s.charAt(1));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 301 */     float f1 = (i >> 16 & 0xFF) / 255.0F;
/* 302 */     float f2 = (i >> 8 & 0xFF) / 255.0F;
/* 303 */     float f = (i & 0xFF) / 255.0F;
/* 304 */     GlStateManager.disableLighting();
/* 305 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 306 */     GlStateManager.color(f1, f2, f, 1.0F);
/* 307 */     GlStateManager.disableTexture2D();
/* 308 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 309 */     GlStateManager.disableTexture2D();
/* 310 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 311 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void unsetScoreTeamColor() {
/* 316 */     GlStateManager.enableLighting();
/* 317 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 318 */     GlStateManager.enableTexture2D();
/* 319 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 320 */     GlStateManager.enableTexture2D();
/* 321 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
/* 326 */     boolean flag = !entitylivingbaseIn.isInvisible();
/* 327 */     boolean flag1 = (!flag && !entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)(Minecraft.getMinecraft()).thePlayer));
/*     */     
/* 329 */     if (flag || flag1) {
/*     */       
/* 331 */       if (!bindEntityTexture(entitylivingbaseIn)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 336 */       if (flag1) {
/*     */         
/* 338 */         GlStateManager.pushMatrix();
/* 339 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
/* 340 */         GlStateManager.depthMask(false);
/* 341 */         GlStateManager.enableBlend();
/* 342 */         GlStateManager.blendFunc(770, 771);
/* 343 */         GlStateManager.alphaFunc(516, 0.003921569F);
/*     */       } 
/*     */       
/* 346 */       this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
/*     */       
/* 348 */       if (flag1) {
/*     */         
/* 350 */         GlStateManager.disableBlend();
/* 351 */         GlStateManager.alphaFunc(516, 0.1F);
/* 352 */         GlStateManager.popMatrix();
/* 353 */         GlStateManager.depthMask(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean setDoRenderBrightness(T entityLivingBaseIn, float partialTicks) {
/* 360 */     return setBrightness(entityLivingBaseIn, partialTicks, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
/* 365 */     float f = entitylivingbaseIn.getBrightness(partialTicks);
/* 366 */     int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
/* 367 */     boolean flag = ((i >> 24 & 0xFF) > 0);
/* 368 */     boolean flag1 = (((EntityLivingBase)entitylivingbaseIn).hurtTime > 0 || ((EntityLivingBase)entitylivingbaseIn).deathTime > 0);
/*     */     
/* 370 */     if (!flag && !flag1)
/*     */     {
/* 372 */       return false;
/*     */     }
/* 374 */     if (!flag && !combineTextures)
/*     */     {
/* 376 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 380 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 381 */     GlStateManager.enableTexture2D();
/* 382 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 383 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 384 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
/* 385 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
/* 386 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 387 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 388 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 389 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
/* 390 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 391 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 392 */     GlStateManager.enableTexture2D();
/* 393 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 394 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
/* 395 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
/* 396 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/* 397 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
/* 398 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 399 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 400 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
/* 401 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 402 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
/* 403 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 404 */     this.brightnessBuffer.position(0);
/*     */     
/* 406 */     if (flag1) {
/*     */       
/* 408 */       this.brightnessBuffer.put(1.0F);
/* 409 */       this.brightnessBuffer.put(0.0F);
/* 410 */       this.brightnessBuffer.put(0.0F);
/* 411 */       this.brightnessBuffer.put(0.3F);
/*     */       
/* 413 */       if (Config.isShaders())
/*     */       {
/* 415 */         Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 420 */       float f1 = (i >> 24 & 0xFF) / 255.0F;
/* 421 */       float f2 = (i >> 16 & 0xFF) / 255.0F;
/* 422 */       float f3 = (i >> 8 & 0xFF) / 255.0F;
/* 423 */       float f4 = (i & 0xFF) / 255.0F;
/* 424 */       this.brightnessBuffer.put(f2);
/* 425 */       this.brightnessBuffer.put(f3);
/* 426 */       this.brightnessBuffer.put(f4);
/* 427 */       this.brightnessBuffer.put(1.0F - f1);
/*     */       
/* 429 */       if (Config.isShaders())
/*     */       {
/* 431 */         Shaders.setEntityColor(f2, f3, f4, 1.0F - f1);
/*     */       }
/*     */     } 
/*     */     
/* 435 */     this.brightnessBuffer.flip();
/* 436 */     GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
/* 437 */     GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
/* 438 */     GlStateManager.enableTexture2D();
/* 439 */     GlStateManager.bindTexture(textureBrightness.getGlTextureId());
/* 440 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 441 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 442 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
/* 443 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
/* 444 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 445 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 446 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 447 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
/* 448 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 449 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 450 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void unsetBrightness() {
/* 456 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 457 */     GlStateManager.enableTexture2D();
/* 458 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 459 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 460 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
/* 461 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
/* 462 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 463 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 464 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
/* 465 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
/* 466 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
/* 467 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 468 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
/* 469 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 470 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 471 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 472 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 473 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 474 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
/* 475 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/* 476 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
/* 477 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 478 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
/* 479 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 480 */     GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
/* 481 */     GlStateManager.disableTexture2D();
/* 482 */     GlStateManager.bindTexture(0);
/* 483 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 484 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 485 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 486 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 487 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
/* 488 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/* 489 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
/* 490 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 491 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
/* 492 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*     */     
/* 494 */     if (Config.isShaders())
/*     */     {
/* 496 */       Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {
/* 502 */     GlStateManager.translate((float)x, (float)y, (float)z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rotateCorpse(T bat, float p_77043_2_, float p_77043_3_, float partialTicks) {
/* 507 */     GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
/*     */     
/* 509 */     if (((EntityLivingBase)bat).deathTime > 0) {
/*     */       
/* 511 */       float f = (((EntityLivingBase)bat).deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
/* 512 */       f = MathHelper.sqrt_float(f);
/*     */       
/* 514 */       if (f > 1.0F)
/*     */       {
/* 516 */         f = 1.0F;
/*     */       }
/*     */       
/* 519 */       GlStateManager.rotate(f * getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
/*     */     }
/*     */     else {
/*     */       
/* 523 */       String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
/*     */       
/* 525 */       if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer)bat).isWearing(EnumPlayerModelParts.CAPE))) {
/*     */         
/* 527 */         GlStateManager.translate(0.0F, ((EntityLivingBase)bat).height + 0.1F, 0.0F);
/* 528 */         GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSwingProgress(T livingBase, float partialTickTime) {
/* 535 */     return livingBase.getSwingProgress(partialTickTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float handleRotationFloat(T livingBase, float partialTicks) {
/* 540 */     return ((EntityLivingBase)livingBase).ticksExisted + partialTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
/* 545 */     for (LayerRenderer<T> layerrenderer : this.layerRenderers) {
/*     */       
/* 547 */       boolean flag = setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
/*     */       
/* 549 */       if (EmissiveTextures.isActive())
/*     */       {
/* 551 */         EmissiveTextures.beginRender();
/*     */       }
/*     */       
/* 554 */       if (this.renderLayersPushMatrix)
/*     */       {
/* 556 */         GlStateManager.pushMatrix();
/*     */       }
/*     */       
/* 559 */       layerrenderer.doRenderLayer((EntityLivingBase)entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
/*     */       
/* 561 */       if (this.renderLayersPushMatrix)
/*     */       {
/* 563 */         GlStateManager.popMatrix();
/*     */       }
/*     */       
/* 566 */       if (EmissiveTextures.isActive()) {
/*     */         
/* 568 */         if (EmissiveTextures.hasEmissive()) {
/*     */           
/* 570 */           this.renderLayersPushMatrix = true;
/* 571 */           EmissiveTextures.beginRenderEmissive();
/* 572 */           GlStateManager.pushMatrix();
/* 573 */           layerrenderer.doRenderLayer((EntityLivingBase)entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
/* 574 */           GlStateManager.popMatrix();
/* 575 */           EmissiveTextures.endRenderEmissive();
/*     */         } 
/*     */         
/* 578 */         EmissiveTextures.endRender();
/*     */       } 
/*     */       
/* 581 */       if (flag)
/*     */       {
/* 583 */         unsetBrightness();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getDeathMaxRotation(T entityLivingBaseIn) {
/* 590 */     return 90.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getColorMultiplier(T entitylivingbaseIn, float lightBrightness, float partialTickTime) {
/* 595 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void preRenderCallback(T entitylivingbaseIn, float partialTickTime) {}
/*     */ 
/*     */   
/*     */   public void renderName(T entity, double x, double y, double z) {
/* 604 */     if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) })) {
/*     */       
/* 606 */       if (canRenderName(entity)) {
/*     */         
/* 608 */         double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
/* 609 */         float f = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
/*     */         
/* 611 */         if (d0 < (f * f)) {
/*     */           
/* 613 */           String s = entity.getDisplayName().getFormattedText();
/* 614 */           float f1 = 0.02666667F;
/* 615 */           GlStateManager.alphaFunc(516, 0.1F);
/*     */           
/* 617 */           if (entity.isSneaking()) {
/*     */             
/* 619 */             FontRenderer fontrenderer = getFontRendererFromRenderManager();
/* 620 */             GlStateManager.pushMatrix();
/* 621 */             GlStateManager.translate((float)x, (float)y + ((EntityLivingBase)entity).height + 0.5F - (entity.isChild() ? (((EntityLivingBase)entity).height / 2.0F) : 0.0F), (float)z);
/* 622 */             GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 623 */             GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
/* 624 */             GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
/* 625 */             GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
/* 626 */             GlStateManager.translate(0.0F, 9.374999F, 0.0F);
/* 627 */             GlStateManager.disableLighting();
/* 628 */             GlStateManager.depthMask(false);
/* 629 */             GlStateManager.enableBlend();
/* 630 */             GlStateManager.disableTexture2D();
/* 631 */             GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 632 */             int i = fontrenderer.getStringWidth(s) / 2;
/* 633 */             Tessellator tessellator = Tessellator.getInstance();
/* 634 */             WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 635 */             worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 636 */             worldrenderer.pos((-i - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 637 */             worldrenderer.pos((-i - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 638 */             worldrenderer.pos((i + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 639 */             worldrenderer.pos((i + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 640 */             tessellator.draw();
/* 641 */             GlStateManager.enableTexture2D();
/* 642 */             GlStateManager.depthMask(true);
/* 643 */             fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2), 0.0D, 553648127);
/* 644 */             GlStateManager.enableLighting();
/* 645 */             GlStateManager.disableBlend();
/* 646 */             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 647 */             GlStateManager.popMatrix();
/*     */           }
/*     */           else {
/*     */             
/* 651 */             renderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (((EntityLivingBase)entity).height / 2.0F) : 0.0D), z, s, 0.02666667F, d0);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 656 */       if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists())
/*     */       {
/* 658 */         Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, new Object[] { entity, this, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRenderName(T entity) {
/* 665 */     EntityPlayerSP entityplayersp = (Minecraft.getMinecraft()).thePlayer;
/*     */     
/* 667 */     if (entity instanceof EntityPlayer && entity != entityplayersp) {
/*     */       
/* 669 */       Team team = entity.getTeam();
/* 670 */       Team team1 = entityplayersp.getTeam();
/*     */       
/* 672 */       if (team != null) {
/*     */         
/* 674 */         Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
/*     */         
/* 676 */         switch (team$enumvisible) {
/*     */           
/*     */           case ALWAYS:
/* 679 */             return true;
/*     */           
/*     */           case NEVER:
/* 682 */             return false;
/*     */           
/*     */           case HIDE_FOR_OTHER_TEAMS:
/* 685 */             return (team1 == null || team.isSameTeam(team1));
/*     */           
/*     */           case HIDE_FOR_OWN_TEAM:
/* 688 */             return (team1 == null || !team.isSameTeam(team1));
/*     */         } 
/*     */         
/* 691 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 696 */     return (Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer((EntityPlayer)entityplayersp) && ((EntityLivingBase)entity).riddenByEntity == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRenderOutlines(boolean renderOutlinesIn) {
/* 701 */     this.renderOutlines = renderOutlinesIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<LayerRenderer<T>> getLayerRenderers() {
/* 706 */     return this.layerRenderers;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 711 */     int[] aint = textureBrightness.getTextureData();
/*     */     
/* 713 */     for (int i = 0; i < 256; i++)
/*     */     {
/* 715 */       aint[i] = -1;
/*     */     }
/*     */     
/* 718 */     textureBrightness.updateDynamicTexture();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RendererLivingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */