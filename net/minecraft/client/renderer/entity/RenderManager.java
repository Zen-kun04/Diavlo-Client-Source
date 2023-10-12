/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBed;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelChicken;
/*     */ import net.minecraft.client.model.ModelCow;
/*     */ import net.minecraft.client.model.ModelHorse;
/*     */ import net.minecraft.client.model.ModelOcelot;
/*     */ import net.minecraft.client.model.ModelPig;
/*     */ import net.minecraft.client.model.ModelRabbit;
/*     */ import net.minecraft.client.model.ModelSheep2;
/*     */ import net.minecraft.client.model.ModelSlime;
/*     */ import net.minecraft.client.model.ModelSquid;
/*     */ import net.minecraft.client.model.ModelWolf;
/*     */ import net.minecraft.client.model.ModelZombie;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
/*     */ import net.minecraft.client.renderer.tileentity.RenderItemFrame;
/*     */ import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLeashKnot;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityMinecartMobSpawner;
/*     */ import net.minecraft.entity.boss.EntityDragon;
/*     */ import net.minecraft.entity.boss.EntityWither;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityEnderEye;
/*     */ import net.minecraft.entity.item.EntityEnderPearl;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityMinecartTNT;
/*     */ import net.minecraft.entity.item.EntityPainting;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.monster.EntityBlaze;
/*     */ import net.minecraft.entity.monster.EntityCaveSpider;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntityEndermite;
/*     */ import net.minecraft.entity.monster.EntityGhast;
/*     */ import net.minecraft.entity.monster.EntityGiantZombie;
/*     */ import net.minecraft.entity.monster.EntityGuardian;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.monster.EntityMagmaCube;
/*     */ import net.minecraft.entity.monster.EntityPigZombie;
/*     */ import net.minecraft.entity.monster.EntitySilverfish;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.monster.EntitySlime;
/*     */ import net.minecraft.entity.monster.EntitySnowman;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityBat;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.entity.passive.EntityMooshroom;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntityRabbit;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityEgg;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.entity.projectile.EntityLargeFireball;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.optifine.entity.model.CustomEntityModels;
/*     */ import net.optifine.player.PlayerItemsLayer;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.shaders.Shaders;
/*     */ 
/*     */ public class RenderManager {
/* 111 */   private Map<Class, Render> entityRenderMap = Maps.newHashMap();
/* 112 */   private Map<String, RenderPlayer> skinMap = Maps.newHashMap();
/*     */   private RenderPlayer playerRenderer;
/*     */   private FontRenderer textRenderer;
/*     */   public double renderPosX;
/*     */   public double renderPosY;
/*     */   public double renderPosZ;
/*     */   public TextureManager renderEngine;
/*     */   public World worldObj;
/*     */   public Entity livingPlayer;
/*     */   public Entity pointedEntity;
/*     */   public float playerViewY;
/*     */   public float playerViewX;
/*     */   public GameSettings options;
/*     */   public double viewerPosX;
/*     */   public double viewerPosY;
/*     */   public double viewerPosZ;
/*     */   private boolean renderOutlines = false;
/*     */   private boolean renderShadow = true;
/*     */   private boolean debugBoundingBox = false;
/* 131 */   public Render renderRender = null;
/*     */ 
/*     */   
/*     */   public RenderManager(TextureManager renderEngineIn, RenderItem itemRendererIn) {
/* 135 */     this.renderEngine = renderEngineIn;
/* 136 */     this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
/* 137 */     this.entityRenderMap.put(EntitySpider.class, new RenderSpider<>(this));
/* 138 */     this.entityRenderMap.put(EntityPig.class, new RenderPig(this, (ModelBase)new ModelPig(), 0.7F));
/* 139 */     this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this, (ModelBase)new ModelSheep2(), 0.7F));
/* 140 */     this.entityRenderMap.put(EntityCow.class, new RenderCow(this, (ModelBase)new ModelCow(), 0.7F));
/* 141 */     this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this, (ModelBase)new ModelCow(), 0.7F));
/* 142 */     this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this, (ModelBase)new ModelWolf(), 0.5F));
/* 143 */     this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this, (ModelBase)new ModelChicken(), 0.3F));
/* 144 */     this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this, (ModelBase)new ModelOcelot(), 0.4F));
/* 145 */     this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this, (ModelBase)new ModelRabbit(), 0.3F));
/* 146 */     this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
/* 147 */     this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
/* 148 */     this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
/* 149 */     this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
/* 150 */     this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
/* 151 */     this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
/* 152 */     this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
/* 153 */     this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
/* 154 */     this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
/* 155 */     this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
/* 156 */     this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this, (ModelBase)new ModelSlime(16), 0.25F));
/* 157 */     this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
/* 158 */     this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, (ModelBase)new ModelZombie(), 0.5F, 6.0F));
/* 159 */     this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
/* 160 */     this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this, (ModelBase)new ModelSquid(), 0.7F));
/* 161 */     this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
/* 162 */     this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
/* 163 */     this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
/* 164 */     this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
/* 165 */     this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
/* 166 */     this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
/* 167 */     this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
/* 168 */     this.entityRenderMap.put(Entity.class, new RenderEntity(this));
/* 169 */     this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
/* 170 */     this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, itemRendererIn));
/* 171 */     this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
/* 172 */     this.entityRenderMap.put(EntityArrow.class, new RenderArrow(this));
/* 173 */     this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball<>(this, Items.snowball, itemRendererIn));
/* 174 */     this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball<>(this, Items.ender_pearl, itemRendererIn));
/* 175 */     this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball<>(this, Items.ender_eye, itemRendererIn));
/* 176 */     this.entityRenderMap.put(EntityEgg.class, new RenderSnowball<>(this, Items.egg, itemRendererIn));
/* 177 */     this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, itemRendererIn));
/* 178 */     this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball<>(this, Items.experience_bottle, itemRendererIn));
/* 179 */     this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball<>(this, Items.fireworks, itemRendererIn));
/* 180 */     this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
/* 181 */     this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
/* 182 */     this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
/* 183 */     this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, itemRendererIn));
/* 184 */     this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
/* 185 */     this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
/* 186 */     this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
/* 187 */     this.entityRenderMap.put(EntityArmorStand.class, new ArmorStandRenderer(this));
/* 188 */     this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
/* 189 */     this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
/* 190 */     this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart<>(this));
/* 191 */     this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
/* 192 */     this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
/* 193 */     this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this, new ModelHorse(), 0.75F));
/* 194 */     this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
/* 195 */     this.playerRenderer = new RenderPlayer(this);
/* 196 */     this.skinMap.put("default", this.playerRenderer);
/* 197 */     this.skinMap.put("slim", new RenderPlayer(this, true));
/* 198 */     PlayerItemsLayer.register(this.skinMap);
/*     */     
/* 200 */     if (Reflector.RenderingRegistry_loadEntityRenderers.exists())
/*     */     {
/* 202 */       Reflector.call(Reflector.RenderingRegistry_loadEntityRenderers, new Object[] { this, this.entityRenderMap });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRenderPosition(double renderPosXIn, double renderPosYIn, double renderPosZIn) {
/* 208 */     this.renderPosX = renderPosXIn;
/* 209 */     this.renderPosY = renderPosYIn;
/* 210 */     this.renderPosZ = renderPosZIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Entity> Render<T> getEntityClassRenderObject(Class<? extends Entity> entityClass) {
/* 215 */     Render<? extends Entity> render = this.entityRenderMap.get(entityClass);
/*     */     
/* 217 */     if (render == null && entityClass != Entity.class) {
/*     */       
/* 219 */       render = getEntityClassRenderObject((Class)entityClass.getSuperclass());
/* 220 */       this.entityRenderMap.put(entityClass, render);
/*     */     } 
/*     */     
/* 223 */     return (Render)render;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn) {
/* 228 */     if (entityIn instanceof AbstractClientPlayer) {
/*     */       
/* 230 */       String s = ((AbstractClientPlayer)entityIn).getSkinType();
/* 231 */       RenderPlayer renderplayer = this.skinMap.get(s);
/* 232 */       return (renderplayer != null) ? renderplayer : this.playerRenderer;
/*     */     } 
/*     */ 
/*     */     
/* 236 */     return getEntityClassRenderObject((Class)entityIn.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cacheActiveRenderInfo(World worldIn, FontRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks) {
/* 242 */     this.worldObj = worldIn;
/* 243 */     this.options = optionsIn;
/* 244 */     this.livingPlayer = livingPlayerIn;
/* 245 */     this.pointedEntity = pointedEntityIn;
/* 246 */     this.textRenderer = textRendererIn;
/*     */     
/* 248 */     if (livingPlayerIn instanceof EntityLivingBase && ((EntityLivingBase)livingPlayerIn).isPlayerSleeping()) {
/*     */       
/* 250 */       IBlockState iblockstate = worldIn.getBlockState(new BlockPos(livingPlayerIn));
/* 251 */       Block block = iblockstate.getBlock();
/*     */       
/* 253 */       if (Reflector.callBoolean(block, Reflector.ForgeBlock_isBed, new Object[] { iblockstate, worldIn, new BlockPos(livingPlayerIn), livingPlayerIn }))
/*     */       {
/* 255 */         EnumFacing enumfacing = (EnumFacing)Reflector.call(block, Reflector.ForgeBlock_getBedDirection, new Object[] { iblockstate, worldIn, new BlockPos(livingPlayerIn) });
/* 256 */         int i = enumfacing.getHorizontalIndex();
/* 257 */         this.playerViewY = (i * 90 + 180);
/* 258 */         this.playerViewX = 0.0F;
/*     */       }
/* 260 */       else if (block == Blocks.bed)
/*     */       {
/* 262 */         int j = ((EnumFacing)iblockstate.getValue((IProperty)BlockBed.FACING)).getHorizontalIndex();
/* 263 */         this.playerViewY = (j * 90 + 180);
/* 264 */         this.playerViewX = 0.0F;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 269 */       this.playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.rotationYaw - livingPlayerIn.prevRotationYaw) * partialTicks;
/* 270 */       this.playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
/*     */     } 
/*     */     
/* 273 */     if (optionsIn.thirdPersonView == 2)
/*     */     {
/* 275 */       this.playerViewY += 180.0F;
/*     */     }
/*     */     
/* 278 */     this.viewerPosX = livingPlayerIn.lastTickPosX + (livingPlayerIn.posX - livingPlayerIn.lastTickPosX) * partialTicks;
/* 279 */     this.viewerPosY = livingPlayerIn.lastTickPosY + (livingPlayerIn.posY - livingPlayerIn.lastTickPosY) * partialTicks;
/* 280 */     this.viewerPosZ = livingPlayerIn.lastTickPosZ + (livingPlayerIn.posZ - livingPlayerIn.lastTickPosZ) * partialTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerViewY(float playerViewYIn) {
/* 285 */     this.playerViewY = playerViewYIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRenderShadow() {
/* 290 */     return this.renderShadow;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRenderShadow(boolean renderShadowIn) {
/* 295 */     this.renderShadow = renderShadowIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebugBoundingBox(boolean debugBoundingBoxIn) {
/* 300 */     this.debugBoundingBox = debugBoundingBoxIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugBoundingBox() {
/* 305 */     return this.debugBoundingBox;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean renderEntitySimple(Entity entityIn, float partialTicks) {
/* 310 */     return renderEntityStatic(entityIn, partialTicks, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ) {
/* 315 */     Render<Entity> render = getEntityRenderObject(entityIn);
/* 316 */     return (render != null && render.shouldRender(entityIn, camera, camX, camY, camZ));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean renderEntityStatic(Entity entity, float partialTicks, boolean hideDebugBox) {
/* 321 */     if (entity.ticksExisted == 0) {
/*     */       
/* 323 */       entity.lastTickPosX = entity.posX;
/* 324 */       entity.lastTickPosY = entity.posY;
/* 325 */       entity.lastTickPosZ = entity.posZ;
/*     */     } 
/*     */     
/* 328 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 329 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 330 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 331 */     float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
/* 332 */     int i = entity.getBrightnessForRender(partialTicks);
/*     */     
/* 334 */     if (entity.isBurning())
/*     */     {
/* 336 */       i = 15728880;
/*     */     }
/*     */     
/* 339 */     int j = i % 65536;
/* 340 */     int k = i / 65536;
/* 341 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 342 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 343 */     return doRenderEntity(entity, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f, partialTicks, hideDebugBox);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWitherSkull(Entity entityIn, float partialTicks) {
/* 348 */     double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 349 */     double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 350 */     double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 351 */     Render<Entity> render = getEntityRenderObject(entityIn);
/*     */     
/* 353 */     if (render != null && this.renderEngine != null) {
/*     */       
/* 355 */       int i = entityIn.getBrightnessForRender(partialTicks);
/* 356 */       int j = i % 65536;
/* 357 */       int k = i / 65536;
/* 358 */       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 359 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 360 */       render.renderName(entityIn, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean renderEntityWithPosYaw(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
/* 366 */     return doRenderEntity(entityIn, x, y, z, entityYaw, partialTicks, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doRenderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox) {
/* 371 */     Render<Entity> render = null;
/*     */ 
/*     */     
/*     */     try {
/* 375 */       render = getEntityRenderObject(entity);
/*     */       
/* 377 */       if (render != null && this.renderEngine != null) {
/*     */ 
/*     */         
/*     */         try {
/* 381 */           if (render instanceof RendererLivingEntity)
/*     */           {
/* 383 */             ((RendererLivingEntity)render).setRenderOutlines(this.renderOutlines);
/*     */           }
/*     */           
/* 386 */           if (CustomEntityModels.isActive())
/*     */           {
/* 388 */             this.renderRender = render;
/*     */           }
/*     */           
/* 391 */           render.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */         }
/* 393 */         catch (Throwable throwable2) {
/*     */           
/* 395 */           throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
/*     */         } 
/*     */ 
/*     */         
/*     */         try {
/* 400 */           if (!this.renderOutlines)
/*     */           {
/* 402 */             render.doRenderShadowAndFire(entity, x, y, z, entityYaw, partialTicks);
/*     */           }
/*     */         }
/* 405 */         catch (Throwable throwable1) {
/*     */           
/* 407 */           throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
/*     */         } 
/*     */         
/* 410 */         if (this.debugBoundingBox && !entity.isInvisible() && !hideDebugBox) {
/*     */           try
/*     */           {
/*     */             
/* 414 */             renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
/*     */           }
/* 416 */           catch (Throwable throwable)
/*     */           {
/* 418 */             throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
/*     */           }
/*     */         
/*     */         }
/* 422 */       } else if (this.renderEngine != null) {
/*     */         
/* 424 */         return false;
/*     */       } 
/*     */       
/* 427 */       return true;
/*     */     }
/* 429 */     catch (Throwable throwable3) {
/*     */       
/* 431 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
/* 432 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
/* 433 */       entity.addEntityCrashInfo(crashreportcategory);
/* 434 */       CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
/* 435 */       crashreportcategory1.addCrashSection("Assigned renderer", render);
/* 436 */       crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
/* 437 */       crashreportcategory1.addCrashSection("Rotation", Float.valueOf(entityYaw));
/* 438 */       crashreportcategory1.addCrashSection("Delta", Float.valueOf(partialTicks));
/* 439 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks) {
/* 445 */     if (!Shaders.isShadowPass) {
/*     */       
/* 447 */       GlStateManager.depthMask(false);
/* 448 */       GlStateManager.disableTexture2D();
/* 449 */       GlStateManager.disableLighting();
/* 450 */       GlStateManager.disableCull();
/* 451 */       GlStateManager.disableBlend();
/* 452 */       float f = entityIn.width / 2.0F;
/* 453 */       AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
/* 454 */       AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z);
/* 455 */       RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, 255, 255, 255, 255);
/*     */       
/* 457 */       if (entityIn instanceof EntityLivingBase) {
/*     */         
/* 459 */         float f1 = 0.01F;
/* 460 */         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(x - f, y + entityIn.getEyeHeight() - 0.009999999776482582D, z - f, x + f, y + entityIn.getEyeHeight() + 0.009999999776482582D, z + f), 255, 0, 0, 255);
/*     */       } 
/*     */       
/* 463 */       Tessellator tessellator = Tessellator.getInstance();
/* 464 */       WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 465 */       Vec3 vec3 = entityIn.getLook(partialTicks);
/* 466 */       worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/* 467 */       worldrenderer.pos(x, y + entityIn.getEyeHeight(), z).color(0, 0, 255, 255).endVertex();
/* 468 */       worldrenderer.pos(x + vec3.xCoord * 2.0D, y + entityIn.getEyeHeight() + vec3.yCoord * 2.0D, z + vec3.zCoord * 2.0D).color(0, 0, 255, 255).endVertex();
/* 469 */       tessellator.draw();
/* 470 */       GlStateManager.enableTexture2D();
/* 471 */       GlStateManager.enableLighting();
/* 472 */       GlStateManager.enableCull();
/* 473 */       GlStateManager.disableBlend();
/* 474 */       GlStateManager.depthMask(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(World worldIn) {
/* 480 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDistanceToCamera(double x, double y, double z) {
/* 485 */     double d0 = x - this.viewerPosX;
/* 486 */     double d1 = y - this.viewerPosY;
/* 487 */     double d2 = z - this.viewerPosZ;
/* 488 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*     */   }
/*     */ 
/*     */   
/*     */   public FontRenderer getFontRenderer() {
/* 493 */     return this.textRenderer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRenderOutlines(boolean renderOutlinesIn) {
/* 498 */     this.renderOutlines = renderOutlinesIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class, Render> getEntityRenderMap() {
/* 503 */     return this.entityRenderMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityRenderMap(Map<Class, Render> p_setEntityRenderMap_1_) {
/* 508 */     this.entityRenderMap = p_setEntityRenderMap_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, RenderPlayer> getSkinMap() {
/* 513 */     return Collections.unmodifiableMap(this.skinMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */