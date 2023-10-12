/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.ActiveRenderInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ public class EffectRenderer
/*     */ {
/*  37 */   private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
/*     */   protected World worldObj;
/*  39 */   private List<EntityFX>[][] fxLayers = (List<EntityFX>[][])new List[4][];
/*  40 */   private List<EntityParticleEmitter> particleEmitters = Lists.newArrayList();
/*     */   private TextureManager renderer;
/*  42 */   private Random rand = new Random();
/*  43 */   private Map<Integer, IParticleFactory> particleTypes = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public EffectRenderer(World worldIn, TextureManager rendererIn) {
/*  47 */     this.worldObj = worldIn;
/*  48 */     this.renderer = rendererIn;
/*     */     
/*  50 */     for (int i = 0; i < 4; i++) {
/*     */       
/*  52 */       this.fxLayers[i] = (List<EntityFX>[])new List[2];
/*     */       
/*  54 */       for (int j = 0; j < 2; j++)
/*     */       {
/*  56 */         this.fxLayers[i][j] = Lists.newArrayList();
/*     */       }
/*     */     } 
/*     */     
/*  60 */     registerVanillaParticles();
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerVanillaParticles() {
/*  65 */     registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
/*  66 */     registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
/*  67 */     registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
/*  68 */     registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
/*  69 */     registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
/*  70 */     registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
/*  71 */     registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
/*  72 */     registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
/*  73 */     registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
/*  74 */     registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
/*  75 */     registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
/*  76 */     registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
/*  77 */     registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
/*  78 */     registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
/*  79 */     registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
/*  80 */     registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
/*  81 */     registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
/*  82 */     registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
/*  83 */     registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
/*  84 */     registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
/*  85 */     registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
/*  86 */     registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
/*  87 */     registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
/*  88 */     registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
/*  89 */     registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
/*  90 */     registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
/*  91 */     registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
/*  92 */     registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
/*  93 */     registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
/*  94 */     registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
/*  95 */     registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
/*  96 */     registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
/*  97 */     registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
/*  98 */     registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
/*  99 */     registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
/* 100 */     registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
/* 101 */     registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
/* 102 */     registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
/* 103 */     registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
/* 104 */     registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
/* 105 */     registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerParticle(int id, IParticleFactory particleFactory) {
/* 110 */     this.particleTypes.put(Integer.valueOf(id), particleFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes) {
/* 115 */     this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, entityIn, particleTypes));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFX spawnEffectParticle(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
/* 120 */     IParticleFactory iparticlefactory = this.particleTypes.get(Integer.valueOf(particleId));
/*     */     
/* 122 */     if (iparticlefactory != null) {
/*     */       
/* 124 */       EntityFX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
/*     */       
/* 126 */       if (entityfx != null) {
/*     */         
/* 128 */         addEffect(entityfx);
/* 129 */         return entityfx;
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEffect(EntityFX effect) {
/* 138 */     if (effect != null)
/*     */     {
/* 140 */       if (!(effect instanceof EntityFirework.SparkFX) || Config.isFireworkParticles()) {
/*     */         
/* 142 */         int i = effect.getFXLayer();
/* 143 */         int j = (effect.getAlpha() != 1.0F) ? 0 : 1;
/*     */         
/* 145 */         if (this.fxLayers[i][j].size() >= 4000)
/*     */         {
/* 147 */           this.fxLayers[i][j].remove(0);
/*     */         }
/*     */         
/* 150 */         this.fxLayers[i][j].add(effect);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateEffects() {
/* 157 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 159 */       updateEffectLayer(i);
/*     */     }
/*     */     
/* 162 */     List<EntityParticleEmitter> list = Lists.newArrayList();
/*     */     
/* 164 */     for (EntityParticleEmitter entityparticleemitter : this.particleEmitters) {
/*     */       
/* 166 */       entityparticleemitter.onUpdate();
/*     */       
/* 168 */       if (entityparticleemitter.isDead)
/*     */       {
/* 170 */         list.add(entityparticleemitter);
/*     */       }
/*     */     } 
/*     */     
/* 174 */     this.particleEmitters.removeAll(list);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateEffectLayer(int layer) {
/* 179 */     for (int i = 0; i < 2; i++)
/*     */     {
/* 181 */       updateEffectAlphaLayer(this.fxLayers[layer][i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateEffectAlphaLayer(List<EntityFX> entitiesFX) {
/* 187 */     List<EntityFX> list = Lists.newArrayList();
/* 188 */     long i = System.currentTimeMillis();
/* 189 */     int j = entitiesFX.size();
/*     */     
/* 191 */     for (int k = 0; k < entitiesFX.size(); k++) {
/*     */       
/* 193 */       EntityFX entityfx = entitiesFX.get(k);
/* 194 */       tickParticle(entityfx);
/*     */       
/* 196 */       if (entityfx.isDead)
/*     */       {
/* 198 */         list.add(entityfx);
/*     */       }
/*     */       
/* 201 */       j--;
/*     */       
/* 203 */       if (System.currentTimeMillis() > i + 20L) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 209 */     if (j > 0) {
/*     */       
/* 211 */       int l = j;
/*     */       
/* 213 */       for (Iterator<EntityFX> iterator = entitiesFX.iterator(); iterator.hasNext() && l > 0; l--) {
/*     */         
/* 215 */         EntityFX entityfx1 = iterator.next();
/* 216 */         entityfx1.setDead();
/* 217 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 221 */     entitiesFX.removeAll(list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void tickParticle(final EntityFX particle) {
/*     */     try {
/* 228 */       particle.onUpdate();
/*     */     }
/* 230 */     catch (Throwable throwable) {
/*     */       
/* 232 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
/* 233 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
/* 234 */       final int i = particle.getFXLayer();
/* 235 */       crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>()
/*     */           {
/*     */             public String call() throws Exception
/*     */             {
/* 239 */               return particle.toString();
/*     */             }
/*     */           });
/* 242 */       crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>()
/*     */           {
/*     */             public String call() throws Exception
/*     */             {
/* 246 */               return (i == 0) ? "MISC_TEXTURE" : ((i == 1) ? "TERRAIN_TEXTURE" : ((i == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + i)));
/*     */             }
/*     */           });
/* 249 */       throw new ReportedException(crashreport);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderParticles(Entity entityIn, float partialTicks) {
/* 255 */     float f = ActiveRenderInfo.getRotationX();
/* 256 */     float f1 = ActiveRenderInfo.getRotationZ();
/* 257 */     float f2 = ActiveRenderInfo.getRotationYZ();
/* 258 */     float f3 = ActiveRenderInfo.getRotationXY();
/* 259 */     float f4 = ActiveRenderInfo.getRotationXZ();
/* 260 */     EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 261 */     EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 262 */     EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 263 */     GlStateManager.enableBlend();
/* 264 */     GlStateManager.blendFunc(770, 771);
/* 265 */     GlStateManager.alphaFunc(516, 0.003921569F);
/* 266 */     Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.worldObj, entityIn, partialTicks);
/* 267 */     boolean flag = (block.getMaterial() == Material.water);
/*     */     
/* 269 */     for (int i = 0; i < 3; i++) {
/*     */       
/* 271 */       for (int j = 0; j < 2; j++) {
/*     */         
/* 273 */         final int i_f = i;
/*     */         
/* 275 */         if (!this.fxLayers[i][j].isEmpty()) {
/*     */           
/* 277 */           switch (j) {
/*     */             
/*     */             case 0:
/* 280 */               GlStateManager.depthMask(false);
/*     */               break;
/*     */             
/*     */             case 1:
/* 284 */               GlStateManager.depthMask(true);
/*     */               break;
/*     */           } 
/* 287 */           switch (i) {
/*     */ 
/*     */             
/*     */             default:
/* 291 */               this.renderer.bindTexture(particleTextures);
/*     */               break;
/*     */             
/*     */             case 1:
/* 295 */               this.renderer.bindTexture(TextureMap.locationBlocksTexture);
/*     */               break;
/*     */           } 
/* 298 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 299 */           Tessellator tessellator = Tessellator.getInstance();
/* 300 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 301 */           worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
/*     */           
/* 303 */           for (int k = 0; k < this.fxLayers[i][j].size(); k++) {
/*     */             
/* 305 */             final EntityFX entityfx = this.fxLayers[i][j].get(k);
/*     */ 
/*     */             
/*     */             try {
/* 309 */               if (flag || !(entityfx instanceof EntitySuspendFX))
/*     */               {
/* 311 */                 entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
/*     */               }
/*     */             }
/* 314 */             catch (Throwable throwable) {
/*     */               
/* 316 */               CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
/* 317 */               CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
/* 318 */               crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>()
/*     */                   {
/*     */                     public String call() throws Exception
/*     */                     {
/* 322 */                       return entityfx.toString();
/*     */                     }
/*     */                   });
/* 325 */               crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>()
/*     */                   {
/*     */                     public String call() throws Exception
/*     */                     {
/* 329 */                       return (i_f == 0) ? "MISC_TEXTURE" : ((i_f == 1) ? "TERRAIN_TEXTURE" : ((i_f == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + i_f)));
/*     */                     }
/*     */                   });
/* 332 */               throw new ReportedException(crashreport);
/*     */             } 
/*     */           } 
/*     */           
/* 336 */           tessellator.draw();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 341 */     GlStateManager.depthMask(true);
/* 342 */     GlStateManager.disableBlend();
/* 343 */     GlStateManager.alphaFunc(516, 0.1F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderLitParticles(Entity entityIn, float partialTick) {
/* 348 */     float f = 0.017453292F;
/* 349 */     float f1 = MathHelper.cos(entityIn.rotationYaw * 0.017453292F);
/* 350 */     float f2 = MathHelper.sin(entityIn.rotationYaw * 0.017453292F);
/* 351 */     float f3 = -f2 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
/* 352 */     float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
/* 353 */     float f5 = MathHelper.cos(entityIn.rotationPitch * 0.017453292F);
/*     */     
/* 355 */     for (int i = 0; i < 2; i++) {
/*     */       
/* 357 */       List<EntityFX> list = this.fxLayers[3][i];
/*     */       
/* 359 */       if (!list.isEmpty()) {
/*     */         
/* 361 */         Tessellator tessellator = Tessellator.getInstance();
/* 362 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*     */         
/* 364 */         for (int j = 0; j < list.size(); j++) {
/*     */           
/* 366 */           EntityFX entityfx = list.get(j);
/* 367 */           entityfx.renderParticle(worldrenderer, entityIn, partialTick, f1, f5, f2, f3, f4);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearEffects(World worldIn) {
/* 375 */     this.worldObj = worldIn;
/*     */     
/* 377 */     for (int i = 0; i < 4; i++) {
/*     */       
/* 379 */       for (int j = 0; j < 2; j++)
/*     */       {
/* 381 */         this.fxLayers[i][j].clear();
/*     */       }
/*     */     } 
/*     */     
/* 385 */     this.particleEmitters.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBlockDestroyEffects(BlockPos pos, IBlockState state) {
/*     */     boolean flag;
/* 392 */     if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
/*     */       
/* 394 */       Block block = state.getBlock();
/* 395 */       flag = (!Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, new Object[] { this.worldObj, pos }) && !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, new Object[] { this.worldObj, pos, this }));
/*     */     }
/*     */     else {
/*     */       
/* 399 */       flag = (state.getBlock().getMaterial() != Material.air);
/*     */     } 
/*     */     
/* 402 */     if (flag) {
/*     */       
/* 404 */       state = state.getBlock().getActualState(state, (IBlockAccess)this.worldObj, pos);
/* 405 */       int l = 4;
/*     */       
/* 407 */       for (int i = 0; i < l; i++) {
/*     */         
/* 409 */         for (int j = 0; j < l; j++) {
/*     */           
/* 411 */           for (int k = 0; k < l; k++) {
/*     */             
/* 413 */             double d0 = pos.getX() + (i + 0.5D) / l;
/* 414 */             double d1 = pos.getY() + (j + 0.5D) / l;
/* 415 */             double d2 = pos.getZ() + (k + 0.5D) / l;
/* 416 */             addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, state)).setBlockPos(pos));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBlockHitEffects(BlockPos pos, EnumFacing side) {
/* 425 */     IBlockState iblockstate = this.worldObj.getBlockState(pos);
/* 426 */     Block block = iblockstate.getBlock();
/*     */     
/* 428 */     if (block.getRenderType() != -1) {
/*     */       
/* 430 */       int i = pos.getX();
/* 431 */       int j = pos.getY();
/* 432 */       int k = pos.getZ();
/* 433 */       float f = 0.1F;
/* 434 */       double d0 = i + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (f * 2.0F)) + f + block.getBlockBoundsMinX();
/* 435 */       double d1 = j + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (f * 2.0F)) + f + block.getBlockBoundsMinY();
/* 436 */       double d2 = k + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (f * 2.0F)) + f + block.getBlockBoundsMinZ();
/*     */       
/* 438 */       if (side == EnumFacing.DOWN)
/*     */       {
/* 440 */         d1 = j + block.getBlockBoundsMinY() - f;
/*     */       }
/*     */       
/* 443 */       if (side == EnumFacing.UP)
/*     */       {
/* 445 */         d1 = j + block.getBlockBoundsMaxY() + f;
/*     */       }
/*     */       
/* 448 */       if (side == EnumFacing.NORTH)
/*     */       {
/* 450 */         d2 = k + block.getBlockBoundsMinZ() - f;
/*     */       }
/*     */       
/* 453 */       if (side == EnumFacing.SOUTH)
/*     */       {
/* 455 */         d2 = k + block.getBlockBoundsMaxZ() + f;
/*     */       }
/*     */       
/* 458 */       if (side == EnumFacing.WEST)
/*     */       {
/* 460 */         d0 = i + block.getBlockBoundsMinX() - f;
/*     */       }
/*     */       
/* 463 */       if (side == EnumFacing.EAST)
/*     */       {
/* 465 */         d0 = i + block.getBlockBoundsMaxX() + f;
/*     */       }
/*     */       
/* 468 */       addEffect((new EntityDiggingFX(this.worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate)).setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveToAlphaLayer(EntityFX effect) {
/* 474 */     moveToLayer(effect, 1, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveToNoAlphaLayer(EntityFX effect) {
/* 479 */     moveToLayer(effect, 0, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void moveToLayer(EntityFX effect, int layerFrom, int layerTo) {
/* 484 */     for (int i = 0; i < 4; i++) {
/*     */       
/* 486 */       if (this.fxLayers[i][layerFrom].contains(effect)) {
/*     */         
/* 488 */         this.fxLayers[i][layerFrom].remove(effect);
/* 489 */         this.fxLayers[i][layerTo].add(effect);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStatistics() {
/* 496 */     int i = 0;
/*     */     
/* 498 */     for (int j = 0; j < 4; j++) {
/*     */       
/* 500 */       for (int k = 0; k < 2; k++)
/*     */       {
/* 502 */         i += this.fxLayers[j][k].size();
/*     */       }
/*     */     } 
/*     */     
/* 506 */     return "" + i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBlockHitEffects(BlockPos p_addBlockHitEffects_1_, MovingObjectPosition p_addBlockHitEffects_2_) {
/* 511 */     IBlockState iblockstate = this.worldObj.getBlockState(p_addBlockHitEffects_1_);
/*     */     
/* 513 */     if (iblockstate != null) {
/*     */       
/* 515 */       boolean flag = Reflector.callBoolean(iblockstate.getBlock(), Reflector.ForgeBlock_addHitEffects, new Object[] { this.worldObj, p_addBlockHitEffects_2_, this });
/*     */       
/* 517 */       if (iblockstate != null && !flag)
/*     */       {
/* 519 */         addBlockHitEffects(p_addBlockHitEffects_1_, p_addBlockHitEffects_2_.sideHit);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EffectRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */