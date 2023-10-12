/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityFlameFX
/*    */   extends EntityFX
/*    */ {
/*    */   private float flameScale;
/*    */   
/*    */   protected EntityFlameFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
/* 14 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 15 */     this.motionX = this.motionX * 0.009999999776482582D + xSpeedIn;
/* 16 */     this.motionY = this.motionY * 0.009999999776482582D + ySpeedIn;
/* 17 */     this.motionZ = this.motionZ * 0.009999999776482582D + zSpeedIn;
/* 18 */     this.posX += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
/* 19 */     this.posY += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
/* 20 */     this.posZ += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F);
/* 21 */     this.flameScale = this.particleScale;
/* 22 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
/* 23 */     this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
/* 24 */     this.noClip = true;
/* 25 */     setParticleTextureIndex(48);
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 30 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 31 */     this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
/* 32 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks) {
/* 37 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 38 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 39 */     int i = super.getBrightnessForRender(partialTicks);
/* 40 */     int j = i & 0xFF;
/* 41 */     int k = i >> 16 & 0xFF;
/* 42 */     j += (int)(f * 15.0F * 16.0F);
/*    */     
/* 44 */     if (j > 240)
/*    */     {
/* 46 */       j = 240;
/*    */     }
/*    */     
/* 49 */     return j | k << 16;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getBrightness(float partialTicks) {
/* 54 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 55 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 56 */     float f1 = super.getBrightness(partialTicks);
/* 57 */     return f1 * f + 1.0F - f;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 62 */     this.prevPosX = this.posX;
/* 63 */     this.prevPosY = this.posY;
/* 64 */     this.prevPosZ = this.posZ;
/*    */     
/* 66 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 68 */       setDead();
/*    */     }
/*    */     
/* 71 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 72 */     this.motionX *= 0.9599999785423279D;
/* 73 */     this.motionY *= 0.9599999785423279D;
/* 74 */     this.motionZ *= 0.9599999785423279D;
/*    */     
/* 76 */     if (this.onGround) {
/*    */       
/* 78 */       this.motionX *= 0.699999988079071D;
/* 79 */       this.motionZ *= 0.699999988079071D;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 87 */       return new EntityFlameFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityFlameFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */