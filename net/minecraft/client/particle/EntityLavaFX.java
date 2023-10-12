/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityLavaFX
/*    */   extends EntityFX
/*    */ {
/*    */   private float lavaParticleScale;
/*    */   
/*    */   protected EntityLavaFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
/* 15 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 16 */     this.motionX *= 0.800000011920929D;
/* 17 */     this.motionY *= 0.800000011920929D;
/* 18 */     this.motionZ *= 0.800000011920929D;
/* 19 */     this.motionY = (this.rand.nextFloat() * 0.4F + 0.05F);
/* 20 */     this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
/* 21 */     this.particleScale *= this.rand.nextFloat() * 2.0F + 0.2F;
/* 22 */     this.lavaParticleScale = this.particleScale;
/* 23 */     this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
/* 24 */     this.noClip = false;
/* 25 */     setParticleTextureIndex(49);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBrightnessForRender(float partialTicks) {
/* 30 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 31 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 32 */     int i = super.getBrightnessForRender(partialTicks);
/* 33 */     int j = 240;
/* 34 */     int k = i >> 16 & 0xFF;
/* 35 */     return j | k << 16;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getBrightness(float partialTicks) {
/* 40 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 45 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge;
/* 46 */     this.particleScale = this.lavaParticleScale * (1.0F - f * f);
/* 47 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 52 */     this.prevPosX = this.posX;
/* 53 */     this.prevPosY = this.posY;
/* 54 */     this.prevPosZ = this.posZ;
/*    */     
/* 56 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 58 */       setDead();
/*    */     }
/*    */     
/* 61 */     float f = this.particleAge / this.particleMaxAge;
/*    */     
/* 63 */     if (this.rand.nextFloat() > f)
/*    */     {
/* 65 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, new int[0]);
/*    */     }
/*    */     
/* 68 */     this.motionY -= 0.03D;
/* 69 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 70 */     this.motionX *= 0.9990000128746033D;
/* 71 */     this.motionY *= 0.9990000128746033D;
/* 72 */     this.motionZ *= 0.9990000128746033D;
/*    */     
/* 74 */     if (this.onGround) {
/*    */       
/* 76 */       this.motionX *= 0.699999988079071D;
/* 77 */       this.motionZ *= 0.699999988079071D;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 85 */       return new EntityLavaFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityLavaFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */