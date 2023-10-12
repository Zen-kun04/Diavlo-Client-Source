/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityPortalFX
/*    */   extends EntityFX
/*    */ {
/*    */   private float portalParticleScale;
/*    */   private double portalPosX;
/*    */   private double portalPosY;
/*    */   private double portalPosZ;
/*    */   
/*    */   protected EntityPortalFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
/* 16 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 17 */     this.motionX = xSpeedIn;
/* 18 */     this.motionY = ySpeedIn;
/* 19 */     this.motionZ = zSpeedIn;
/* 20 */     this.portalPosX = this.posX = xCoordIn;
/* 21 */     this.portalPosY = this.posY = yCoordIn;
/* 22 */     this.portalPosZ = this.posZ = zCoordIn;
/* 23 */     float f = this.rand.nextFloat() * 0.6F + 0.4F;
/* 24 */     this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
/* 25 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
/* 26 */     this.particleGreen *= 0.3F;
/* 27 */     this.particleRed *= 0.9F;
/* 28 */     this.particleMaxAge = (int)(Math.random() * 10.0D) + 40;
/* 29 */     this.noClip = true;
/* 30 */     setParticleTextureIndex((int)(Math.random() * 8.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 35 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 36 */     f = 1.0F - f;
/* 37 */     f *= f;
/* 38 */     f = 1.0F - f;
/* 39 */     this.particleScale = this.portalParticleScale * f;
/* 40 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks) {
/* 45 */     int i = super.getBrightnessForRender(partialTicks);
/* 46 */     float f = this.particleAge / this.particleMaxAge;
/* 47 */     f *= f;
/* 48 */     f *= f;
/* 49 */     int j = i & 0xFF;
/* 50 */     int k = i >> 16 & 0xFF;
/* 51 */     k += (int)(f * 15.0F * 16.0F);
/*    */     
/* 53 */     if (k > 240)
/*    */     {
/* 55 */       k = 240;
/*    */     }
/*    */     
/* 58 */     return j | k << 16;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getBrightness(float partialTicks) {
/* 63 */     float f = super.getBrightness(partialTicks);
/* 64 */     float f1 = this.particleAge / this.particleMaxAge;
/* 65 */     f1 = f1 * f1 * f1 * f1;
/* 66 */     return f * (1.0F - f1) + f1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 71 */     this.prevPosX = this.posX;
/* 72 */     this.prevPosY = this.posY;
/* 73 */     this.prevPosZ = this.posZ;
/* 74 */     float f = this.particleAge / this.particleMaxAge;
/* 75 */     f = -f + f * f * 2.0F;
/* 76 */     f = 1.0F - f;
/* 77 */     this.posX = this.portalPosX + this.motionX * f;
/* 78 */     this.posY = this.portalPosY + this.motionY * f + (1.0F - f);
/* 79 */     this.posZ = this.portalPosZ + this.motionZ * f;
/*    */     
/* 81 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 83 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 91 */       return new EntityPortalFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityPortalFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */