/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderItem;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.entity.RenderPlayer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumAction;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.storage.MapData;
/*     */ import net.optifine.DynamicLights;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class ItemRenderer {
/*  37 */   private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
/*  38 */   private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
/*     */   private final Minecraft mc;
/*     */   private ItemStack itemToRender;
/*     */   private float equippedProgress;
/*     */   private float prevEquippedProgress;
/*     */   private final RenderManager renderManager;
/*     */   private final RenderItem itemRenderer;
/*  45 */   private int equippedItemSlot = -1;
/*     */ 
/*     */   
/*     */   public ItemRenderer(Minecraft mcIn) {
/*  49 */     this.mc = mcIn;
/*  50 */     this.renderManager = mcIn.getRenderManager();
/*  51 */     this.itemRenderer = mcIn.getRenderItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
/*  56 */     if (heldStack != null) {
/*     */       
/*  58 */       Item item = heldStack.getItem();
/*  59 */       Block block = Block.getBlockFromItem(item);
/*  60 */       GlStateManager.pushMatrix();
/*     */       
/*  62 */       if (this.itemRenderer.shouldRenderItemIn3D(heldStack)) {
/*     */         
/*  64 */         GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*     */         
/*  66 */         if (isBlockTranslucent(block) && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask))
/*     */         {
/*  68 */           GlStateManager.depthMask(false);
/*     */         }
/*     */       } 
/*     */       
/*  72 */       this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);
/*     */       
/*  74 */       if (isBlockTranslucent(block))
/*     */       {
/*  76 */         GlStateManager.depthMask(true);
/*     */       }
/*     */       
/*  79 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isBlockTranslucent(Block blockIn) {
/*  85 */     return (blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT);
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotateArroundXAndY(float angle, float angleY) {
/*  90 */     GlStateManager.pushMatrix();
/*  91 */     GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
/*  92 */     GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
/*  93 */     RenderHelper.enableStandardItemLighting();
/*  94 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setLightMapFromPlayer(AbstractClientPlayer clientPlayer) {
/*  99 */     int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);
/*     */     
/* 101 */     if (Config.isDynamicLights())
/*     */     {
/* 103 */       i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
/*     */     }
/*     */     
/* 106 */     float f = (i & 0xFFFF);
/* 107 */     float f1 = (i >> 16);
/* 108 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks) {
/* 113 */     float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
/* 114 */     float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
/* 115 */     GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
/* 116 */     GlStateManager.rotate((entityplayerspIn.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private float getMapAngleFromPitch(float pitch) {
/* 121 */     float f = 1.0F - pitch / 45.0F + 0.1F;
/* 122 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 123 */     f = -MathHelper.cos(f * 3.1415927F) * 0.5F + 0.5F;
/* 124 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderRightArm(RenderPlayer renderPlayerIn) {
/* 129 */     GlStateManager.pushMatrix();
/* 130 */     GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
/* 131 */     GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
/* 132 */     GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
/* 133 */     GlStateManager.translate(0.25F, -0.85F, 0.75F);
/* 134 */     renderPlayerIn.renderRightArm((AbstractClientPlayer)this.mc.thePlayer);
/* 135 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderLeftArm(RenderPlayer renderPlayerIn) {
/* 140 */     GlStateManager.pushMatrix();
/* 141 */     GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
/* 142 */     GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
/* 143 */     GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
/* 144 */     GlStateManager.translate(-0.3F, -1.1F, 0.45F);
/* 145 */     renderPlayerIn.renderLeftArm((AbstractClientPlayer)this.mc.thePlayer);
/* 146 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPlayerArms(AbstractClientPlayer clientPlayer) {
/* 151 */     this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
/* 152 */     Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject((Entity)this.mc.thePlayer);
/* 153 */     RenderPlayer renderplayer = (RenderPlayer)render;
/*     */     
/* 155 */     if (!clientPlayer.isInvisible()) {
/*     */       
/* 157 */       GlStateManager.disableCull();
/* 158 */       renderRightArm(renderplayer);
/* 159 */       renderLeftArm(renderplayer);
/* 160 */       GlStateManager.enableCull();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress) {
/* 166 */     float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 167 */     float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F * 2.0F);
/* 168 */     float f2 = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
/* 169 */     GlStateManager.translate(f, f1, f2);
/* 170 */     float f3 = getMapAngleFromPitch(pitch);
/* 171 */     GlStateManager.translate(0.0F, 0.04F, -0.72F);
/* 172 */     GlStateManager.translate(0.0F, equipmentProgress * -1.2F, 0.0F);
/* 173 */     GlStateManager.translate(0.0F, f3 * -0.5F, 0.0F);
/* 174 */     GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/* 175 */     GlStateManager.rotate(f3 * -85.0F, 0.0F, 0.0F, 1.0F);
/* 176 */     GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
/* 177 */     renderPlayerArms(clientPlayer);
/* 178 */     float f4 = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
/* 179 */     float f5 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 180 */     GlStateManager.rotate(f4 * -20.0F, 0.0F, 1.0F, 0.0F);
/* 181 */     GlStateManager.rotate(f5 * -20.0F, 0.0F, 0.0F, 1.0F);
/* 182 */     GlStateManager.rotate(f5 * -80.0F, 1.0F, 0.0F, 0.0F);
/* 183 */     GlStateManager.scale(0.38F, 0.38F, 0.38F);
/* 184 */     GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
/* 185 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 186 */     GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
/* 187 */     GlStateManager.translate(-1.0F, -1.0F, 0.0F);
/* 188 */     GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
/* 189 */     this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
/* 190 */     Tessellator tessellator = Tessellator.getInstance();
/* 191 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 192 */     GL11.glNormal3f(0.0F, 0.0F, -1.0F);
/* 193 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 194 */     worldrenderer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
/* 195 */     worldrenderer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
/* 196 */     worldrenderer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
/* 197 */     worldrenderer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
/* 198 */     tessellator.draw();
/* 199 */     MapData mapdata = Items.filled_map.getMapData(this.itemToRender, (World)this.mc.theWorld);
/*     */     
/* 201 */     if (mapdata != null)
/*     */     {
/* 203 */       this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress) {
/* 209 */     float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 210 */     float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F * 2.0F);
/* 211 */     float f2 = -0.4F * MathHelper.sin(swingProgress * 3.1415927F);
/* 212 */     GlStateManager.translate(f, f1, f2);
/* 213 */     GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
/* 214 */     GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
/* 215 */     GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 216 */     float f3 = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
/* 217 */     float f4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 218 */     GlStateManager.rotate(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
/* 219 */     GlStateManager.rotate(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
/* 220 */     this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
/* 221 */     GlStateManager.translate(-1.0F, 3.6F, 3.5F);
/* 222 */     GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
/* 223 */     GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
/* 224 */     GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/* 225 */     GlStateManager.scale(1.0F, 1.0F, 1.0F);
/* 226 */     GlStateManager.translate(5.6F, 0.0F, 0.0F);
/* 227 */     Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject((Entity)this.mc.thePlayer);
/* 228 */     GlStateManager.disableCull();
/* 229 */     RenderPlayer renderplayer = (RenderPlayer)render;
/* 230 */     renderplayer.renderRightArm((AbstractClientPlayer)this.mc.thePlayer);
/* 231 */     GlStateManager.enableCull();
/*     */   }
/*     */ 
/*     */   
/*     */   private void doItemUsedTransformations(float swingProgress) {
/* 236 */     float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 237 */     float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F * 2.0F);
/* 238 */     float f2 = -0.2F * MathHelper.sin(swingProgress * 3.1415927F);
/* 239 */     GlStateManager.translate(f, f1, f2);
/*     */   }
/*     */ 
/*     */   
/*     */   private void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks) {
/* 244 */     float f = clientPlayer.getItemInUseCount() - partialTicks + 1.0F;
/* 245 */     float f1 = f / this.itemToRender.getMaxItemUseDuration();
/* 246 */     float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * 3.1415927F) * 0.1F);
/*     */     
/* 248 */     if (f1 >= 0.8F)
/*     */     {
/* 250 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 253 */     GlStateManager.translate(0.0F, f2, 0.0F);
/* 254 */     float f3 = 1.0F - (float)Math.pow(f1, 27.0D);
/* 255 */     GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
/* 256 */     GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
/* 257 */     GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
/* 258 */     GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void transformFirstPersonItem(float equipProgress, float swingProgress) {
/* 263 */     GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
/* 264 */     GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
/* 265 */     GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
/* 266 */     float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927F);
/* 267 */     float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927F);
/* 268 */     GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
/* 269 */     GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
/* 270 */     GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
/* 271 */     GlStateManager.scale(0.4F, 0.4F, 0.4F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer) {
/* 276 */     GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
/* 277 */     GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
/* 278 */     GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
/* 279 */     GlStateManager.translate(-0.9F, 0.2F, 0.0F);
/* 280 */     float f = this.itemToRender.getMaxItemUseDuration() - clientPlayer.getItemInUseCount() - partialTicks + 1.0F;
/* 281 */     float f1 = f / 20.0F;
/* 282 */     f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;
/*     */     
/* 284 */     if (f1 > 1.0F)
/*     */     {
/* 286 */       f1 = 1.0F;
/*     */     }
/*     */     
/* 289 */     if (f1 > 0.1F) {
/*     */       
/* 291 */       float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
/* 292 */       float f3 = f1 - 0.1F;
/* 293 */       float f4 = f2 * f3;
/* 294 */       GlStateManager.translate(f4 * 0.0F, f4 * 0.01F, f4 * 0.0F);
/*     */     } 
/*     */     
/* 297 */     GlStateManager.translate(f1 * 0.0F, f1 * 0.0F, f1 * 0.1F);
/* 298 */     GlStateManager.scale(1.0F, 1.0F, 1.0F + f1 * 0.2F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void doBlockTransformations() {
/* 303 */     GlStateManager.translate(-0.5F, 0.2F, 0.0F);
/* 304 */     GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
/* 305 */     GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
/* 306 */     GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderItemInFirstPerson(float partialTicks) {
/* 311 */     if (!Config.isShaders() || !Shaders.isSkipRenderHand()) {
/*     */       
/* 313 */       float f = 1.0F - this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks;
/* 314 */       EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
/* 315 */       float f1 = entityPlayerSP.getSwingProgress(partialTicks);
/* 316 */       float f2 = ((AbstractClientPlayer)entityPlayerSP).prevRotationPitch + (((AbstractClientPlayer)entityPlayerSP).rotationPitch - ((AbstractClientPlayer)entityPlayerSP).prevRotationPitch) * partialTicks;
/* 317 */       float f3 = ((AbstractClientPlayer)entityPlayerSP).prevRotationYaw + (((AbstractClientPlayer)entityPlayerSP).rotationYaw - ((AbstractClientPlayer)entityPlayerSP).prevRotationYaw) * partialTicks;
/* 318 */       rotateArroundXAndY(f2, f3);
/* 319 */       setLightMapFromPlayer((AbstractClientPlayer)entityPlayerSP);
/* 320 */       rotateWithPlayerRotations(entityPlayerSP, partialTicks);
/* 321 */       GlStateManager.enableRescaleNormal();
/* 322 */       GlStateManager.pushMatrix();
/*     */       
/* 324 */       if (this.itemToRender != null) {
/*     */         
/* 326 */         if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap) {
/*     */           
/* 328 */           renderItemMap((AbstractClientPlayer)entityPlayerSP, f2, f, f1);
/*     */         }
/* 330 */         else if (entityPlayerSP.getItemInUseCount() > 0) {
/*     */           
/* 332 */           EnumAction enumaction = this.itemToRender.getItemUseAction();
/*     */           
/* 334 */           switch (enumaction) {
/*     */             
/*     */             case NONE:
/* 337 */               transformFirstPersonItem(f, 0.0F);
/*     */               break;
/*     */             
/*     */             case EAT:
/*     */             case DRINK:
/* 342 */               performDrinking((AbstractClientPlayer)entityPlayerSP, partialTicks);
/* 343 */               transformFirstPersonItem(f, 0.0F);
/*     */               break;
/*     */             
/*     */             case BLOCK:
/* 347 */               transformFirstPersonItem(f, 0.0F);
/* 348 */               doBlockTransformations();
/*     */               break;
/*     */             
/*     */             case BOW:
/* 352 */               transformFirstPersonItem(f, 0.0F);
/* 353 */               doBowTransformations(partialTicks, (AbstractClientPlayer)entityPlayerSP);
/*     */               break;
/*     */           } 
/*     */         
/*     */         } else {
/* 358 */           doItemUsedTransformations(f1);
/* 359 */           transformFirstPersonItem(f, f1);
/*     */         } 
/*     */         
/* 362 */         renderItem((EntityLivingBase)entityPlayerSP, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
/*     */       }
/* 364 */       else if (!entityPlayerSP.isInvisible()) {
/*     */         
/* 366 */         renderPlayerArm((AbstractClientPlayer)entityPlayerSP, f, f1);
/*     */       } 
/*     */       
/* 369 */       GlStateManager.popMatrix();
/* 370 */       GlStateManager.disableRescaleNormal();
/* 371 */       RenderHelper.disableStandardItemLighting();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderOverlays(float partialTicks) {
/* 377 */     GlStateManager.disableAlpha();
/*     */     
/* 379 */     if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
/*     */       
/* 381 */       IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos((Entity)this.mc.thePlayer));
/* 382 */       BlockPos blockpos = new BlockPos((Entity)this.mc.thePlayer);
/* 383 */       EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
/*     */       
/* 385 */       for (int i = 0; i < 8; i++) {
/*     */         
/* 387 */         double d0 = ((EntityPlayer)entityPlayerSP).posX + ((((i >> 0) % 2) - 0.5F) * ((EntityPlayer)entityPlayerSP).width * 0.8F);
/* 388 */         double d1 = ((EntityPlayer)entityPlayerSP).posY + ((((i >> 1) % 2) - 0.5F) * 0.1F);
/* 389 */         double d2 = ((EntityPlayer)entityPlayerSP).posZ + ((((i >> 2) % 2) - 0.5F) * ((EntityPlayer)entityPlayerSP).width * 0.8F);
/* 390 */         BlockPos blockpos1 = new BlockPos(d0, d1 + entityPlayerSP.getEyeHeight(), d2);
/* 391 */         IBlockState iblockstate1 = this.mc.theWorld.getBlockState(blockpos1);
/*     */         
/* 393 */         if (iblockstate1.getBlock().isVisuallyOpaque()) {
/*     */           
/* 395 */           iblockstate = iblockstate1;
/* 396 */           blockpos = blockpos1;
/*     */         } 
/*     */       } 
/*     */       
/* 400 */       if (iblockstate.getBlock().getRenderType() != -1) {
/*     */         
/* 402 */         Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
/*     */         
/* 404 */         if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, new Object[] { this.mc.thePlayer, Float.valueOf(partialTicks), object, iblockstate, blockpos }))
/*     */         {
/* 406 */           renderBlockInHand(partialTicks, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 411 */     if (!this.mc.thePlayer.isSpectator()) {
/*     */       
/* 413 */       if (this.mc.thePlayer.isInsideOfMaterial(Material.water) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, new Object[] { this.mc.thePlayer, Float.valueOf(partialTicks) }))
/*     */       {
/* 415 */         renderWaterOverlayTexture(partialTicks);
/*     */       }
/*     */       
/* 418 */       if (this.mc.thePlayer.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, new Object[] { this.mc.thePlayer, Float.valueOf(partialTicks) }))
/*     */       {
/* 420 */         renderFireInFirstPerson(partialTicks);
/*     */       }
/*     */     } 
/*     */     
/* 424 */     GlStateManager.enableAlpha();
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderBlockInHand(float partialTicks, TextureAtlasSprite atlas) {
/* 429 */     this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 430 */     Tessellator tessellator = Tessellator.getInstance();
/* 431 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 432 */     float f = 0.1F;
/* 433 */     GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
/* 434 */     GlStateManager.pushMatrix();
/* 435 */     float f1 = -1.0F;
/* 436 */     float f2 = 1.0F;
/* 437 */     float f3 = -1.0F;
/* 438 */     float f4 = 1.0F;
/* 439 */     float f5 = -0.5F;
/* 440 */     float f6 = atlas.getMinU();
/* 441 */     float f7 = atlas.getMaxU();
/* 442 */     float f8 = atlas.getMinV();
/* 443 */     float f9 = atlas.getMaxV();
/* 444 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 445 */     worldrenderer.pos(-1.0D, -1.0D, -0.5D).tex(f7, f9).endVertex();
/* 446 */     worldrenderer.pos(1.0D, -1.0D, -0.5D).tex(f6, f9).endVertex();
/* 447 */     worldrenderer.pos(1.0D, 1.0D, -0.5D).tex(f6, f8).endVertex();
/* 448 */     worldrenderer.pos(-1.0D, 1.0D, -0.5D).tex(f7, f8).endVertex();
/* 449 */     tessellator.draw();
/* 450 */     GlStateManager.popMatrix();
/* 451 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderWaterOverlayTexture(float partialTicks) {
/* 456 */     if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
/*     */       
/* 458 */       this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
/* 459 */       Tessellator tessellator = Tessellator.getInstance();
/* 460 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 461 */       float f = this.mc.thePlayer.getBrightness(partialTicks);
/* 462 */       GlStateManager.color(f, f, f, 0.5F);
/* 463 */       GlStateManager.enableBlend();
/* 464 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 465 */       GlStateManager.pushMatrix();
/* 466 */       float f1 = 4.0F;
/* 467 */       float f2 = -1.0F;
/* 468 */       float f3 = 1.0F;
/* 469 */       float f4 = -1.0F;
/* 470 */       float f5 = 1.0F;
/* 471 */       float f6 = -0.5F;
/* 472 */       float f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
/* 473 */       float f8 = this.mc.thePlayer.rotationPitch / 64.0F;
/* 474 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 475 */       worldrenderer.pos(-1.0D, -1.0D, -0.5D).tex((4.0F + f7), (4.0F + f8)).endVertex();
/* 476 */       worldrenderer.pos(1.0D, -1.0D, -0.5D).tex((0.0F + f7), (4.0F + f8)).endVertex();
/* 477 */       worldrenderer.pos(1.0D, 1.0D, -0.5D).tex((0.0F + f7), (0.0F + f8)).endVertex();
/* 478 */       worldrenderer.pos(-1.0D, 1.0D, -0.5D).tex((4.0F + f7), (0.0F + f8)).endVertex();
/* 479 */       tessellator.draw();
/* 480 */       GlStateManager.popMatrix();
/* 481 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 482 */       GlStateManager.disableBlend();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderFireInFirstPerson(float partialTicks) {
/* 488 */     Tessellator tessellator = Tessellator.getInstance();
/* 489 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 490 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
/* 491 */     GlStateManager.depthFunc(519);
/* 492 */     GlStateManager.depthMask(false);
/* 493 */     GlStateManager.enableBlend();
/* 494 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 495 */     float f = 1.0F;
/*     */     
/* 497 */     for (int i = 0; i < 2; i++) {
/*     */       
/* 499 */       GlStateManager.pushMatrix();
/* 500 */       TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
/* 501 */       this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
/* 502 */       float f1 = textureatlassprite.getMinU();
/* 503 */       float f2 = textureatlassprite.getMaxU();
/* 504 */       float f3 = textureatlassprite.getMinV();
/* 505 */       float f4 = textureatlassprite.getMaxV();
/* 506 */       float f5 = (0.0F - f) / 2.0F;
/* 507 */       float f6 = f5 + f;
/* 508 */       float f7 = 0.0F - f / 2.0F;
/* 509 */       float f8 = f7 + f;
/* 510 */       float f9 = -0.5F;
/* 511 */       GlStateManager.translate(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
/* 512 */       GlStateManager.rotate((i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
/* 513 */       worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 514 */       worldrenderer.setSprite(textureatlassprite);
/* 515 */       worldrenderer.pos(f5, f7, f9).tex(f2, f4).endVertex();
/* 516 */       worldrenderer.pos(f6, f7, f9).tex(f1, f4).endVertex();
/* 517 */       worldrenderer.pos(f6, f8, f9).tex(f1, f3).endVertex();
/* 518 */       worldrenderer.pos(f5, f8, f9).tex(f2, f3).endVertex();
/* 519 */       tessellator.draw();
/* 520 */       GlStateManager.popMatrix();
/*     */     } 
/*     */     
/* 523 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 524 */     GlStateManager.disableBlend();
/* 525 */     GlStateManager.depthMask(true);
/* 526 */     GlStateManager.depthFunc(515);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEquippedItem() {
/* 531 */     this.prevEquippedProgress = this.equippedProgress;
/* 532 */     EntityPlayerSP entityPlayerSP = this.mc.thePlayer;
/* 533 */     ItemStack itemstack = ((EntityPlayer)entityPlayerSP).inventory.getCurrentItem();
/* 534 */     boolean flag = false;
/*     */     
/* 536 */     if (this.itemToRender != null && itemstack != null) {
/*     */       
/* 538 */       if (!this.itemToRender.getIsItemStackEqual(itemstack))
/*     */       {
/* 540 */         if (Reflector.ForgeItem_shouldCauseReequipAnimation.exists()) {
/*     */           
/* 542 */           boolean flag1 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, new Object[] { this.itemToRender, itemstack, Boolean.valueOf((this.equippedItemSlot != ((EntityPlayer)entityPlayerSP).inventory.currentItem)) });
/*     */           
/* 544 */           if (!flag1) {
/*     */             
/* 546 */             this.itemToRender = itemstack;
/* 547 */             this.equippedItemSlot = ((EntityPlayer)entityPlayerSP).inventory.currentItem;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 552 */         flag = true;
/*     */       }
/*     */     
/* 555 */     } else if (this.itemToRender == null && itemstack == null) {
/*     */       
/* 557 */       flag = false;
/*     */     }
/*     */     else {
/*     */       
/* 561 */       flag = true;
/*     */     } 
/*     */     
/* 564 */     float f2 = 0.4F;
/* 565 */     float f = flag ? 0.0F : 1.0F;
/* 566 */     float f1 = MathHelper.clamp_float(f - this.equippedProgress, -f2, f2);
/* 567 */     this.equippedProgress += f1;
/*     */     
/* 569 */     if (this.equippedProgress < 0.1F) {
/*     */       
/* 571 */       this.itemToRender = itemstack;
/* 572 */       this.equippedItemSlot = ((EntityPlayer)entityPlayerSP).inventory.currentItem;
/*     */       
/* 574 */       if (Config.isShaders())
/*     */       {
/* 576 */         Shaders.setItemToRenderMain(itemstack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetEquippedProgress() {
/* 583 */     this.equippedProgress = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetEquippedProgress2() {
/* 588 */     this.equippedProgress = 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\ItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */