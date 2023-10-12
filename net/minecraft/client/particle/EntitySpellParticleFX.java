/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySpellParticleFX
/*     */   extends EntityFX {
/*  11 */   private static final Random RANDOM = new Random();
/*  12 */   private int baseSpellTextureIndex = 128;
/*     */ 
/*     */   
/*     */   protected EntitySpellParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1229_8_, double p_i1229_10_, double p_i1229_12_) {
/*  16 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.5D - RANDOM.nextDouble(), p_i1229_10_, 0.5D - RANDOM.nextDouble());
/*  17 */     this.motionY *= 0.20000000298023224D;
/*     */     
/*  19 */     if (p_i1229_8_ == 0.0D && p_i1229_12_ == 0.0D) {
/*     */       
/*  21 */       this.motionX *= 0.10000000149011612D;
/*  22 */       this.motionZ *= 0.10000000149011612D;
/*     */     } 
/*     */     
/*  25 */     this.particleScale *= 0.75F;
/*  26 */     this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
/*  27 */     this.noClip = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/*  32 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/*  33 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/*  34 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  39 */     this.prevPosX = this.posX;
/*  40 */     this.prevPosY = this.posY;
/*  41 */     this.prevPosZ = this.posZ;
/*     */     
/*  43 */     if (this.particleAge++ >= this.particleMaxAge)
/*     */     {
/*  45 */       setDead();
/*     */     }
/*     */     
/*  48 */     setParticleTextureIndex(this.baseSpellTextureIndex + 7 - this.particleAge * 8 / this.particleMaxAge);
/*  49 */     this.motionY += 0.004D;
/*  50 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */     
/*  52 */     if (this.posY == this.prevPosY) {
/*     */       
/*  54 */       this.motionX *= 1.1D;
/*  55 */       this.motionZ *= 1.1D;
/*     */     } 
/*     */     
/*  58 */     this.motionX *= 0.9599999785423279D;
/*  59 */     this.motionY *= 0.9599999785423279D;
/*  60 */     this.motionZ *= 0.9599999785423279D;
/*     */     
/*  62 */     if (this.onGround) {
/*     */       
/*  64 */       this.motionX *= 0.699999988079071D;
/*  65 */       this.motionZ *= 0.699999988079071D;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaseSpellTextureIndex(int baseSpellTextureIndexIn) {
/*  71 */     this.baseSpellTextureIndex = baseSpellTextureIndexIn;
/*     */   }
/*     */   
/*     */   public static class AmbientMobFactory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/*  78 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*  79 */       entityfx.setAlphaF(0.15F);
/*  80 */       entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
/*  81 */       return entityfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Factory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/*  89 */       return new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class InstantFactory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/*  97 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*  98 */       ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
/*  99 */       return entityfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MobFactory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 107 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 108 */       entityfx.setRBGColorF((float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
/* 109 */       return entityfx;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WitchFactory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 117 */       EntityFX entityfx = new EntitySpellParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 118 */       ((EntitySpellParticleFX)entityfx).setBaseSpellTextureIndex(144);
/* 119 */       float f = worldIn.rand.nextFloat() * 0.5F + 0.35F;
/* 120 */       entityfx.setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
/* 121 */       return entityfx;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntitySpellParticleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */