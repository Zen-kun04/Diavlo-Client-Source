/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityHeartFX
/*    */   extends EntityFX
/*    */ {
/*    */   float particleScaleOverTime;
/*    */   
/*    */   protected EntityHeartFX(World worldIn, double p_i1211_2_, double p_i1211_4_, double p_i1211_6_, double p_i1211_8_, double p_i1211_10_, double p_i1211_12_) {
/* 14 */     this(worldIn, p_i1211_2_, p_i1211_4_, p_i1211_6_, p_i1211_8_, p_i1211_10_, p_i1211_12_, 2.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityHeartFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46354_8_, double p_i46354_10_, double p_i46354_12_, float scale) {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 20 */     this.motionX *= 0.009999999776482582D;
/* 21 */     this.motionY *= 0.009999999776482582D;
/* 22 */     this.motionZ *= 0.009999999776482582D;
/* 23 */     this.motionY += 0.1D;
/* 24 */     this.particleScale *= 0.75F;
/* 25 */     this.particleScale *= scale;
/* 26 */     this.particleScaleOverTime = this.particleScale;
/* 27 */     this.particleMaxAge = 16;
/* 28 */     this.noClip = false;
/* 29 */     setParticleTextureIndex(80);
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 34 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 35 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 36 */     this.particleScale = this.particleScaleOverTime * f;
/* 37 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 42 */     this.prevPosX = this.posX;
/* 43 */     this.prevPosY = this.posY;
/* 44 */     this.prevPosZ = this.posZ;
/*    */     
/* 46 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 48 */       setDead();
/*    */     }
/*    */     
/* 51 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*    */     
/* 53 */     if (this.posY == this.prevPosY) {
/*    */       
/* 55 */       this.motionX *= 1.1D;
/* 56 */       this.motionZ *= 1.1D;
/*    */     } 
/*    */     
/* 59 */     this.motionX *= 0.8600000143051147D;
/* 60 */     this.motionY *= 0.8600000143051147D;
/* 61 */     this.motionZ *= 0.8600000143051147D;
/*    */     
/* 63 */     if (this.onGround) {
/*    */       
/* 65 */       this.motionX *= 0.699999988079071D;
/* 66 */       this.motionZ *= 0.699999988079071D;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class AngryVillagerFactory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 74 */       EntityFX entityfx = new EntityHeartFX(worldIn, xCoordIn, yCoordIn + 0.5D, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 75 */       entityfx.setParticleTextureIndex(81);
/* 76 */       entityfx.setRBGColorF(1.0F, 1.0F, 1.0F);
/* 77 */       return entityfx;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 85 */       return new EntityHeartFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityHeartFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */