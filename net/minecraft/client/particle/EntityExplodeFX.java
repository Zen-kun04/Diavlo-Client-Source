/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityExplodeFX
/*    */   extends EntityFX
/*    */ {
/*    */   protected EntityExplodeFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
/*  9 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 10 */     this.motionX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
/* 11 */     this.motionY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
/* 12 */     this.motionZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
/* 13 */     this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.3F + 0.7F;
/* 14 */     this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
/* 15 */     this.particleMaxAge = (int)(16.0D / (this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 20 */     this.prevPosX = this.posX;
/* 21 */     this.prevPosY = this.posY;
/* 22 */     this.prevPosZ = this.posZ;
/*    */     
/* 24 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 26 */       setDead();
/*    */     }
/*    */     
/* 29 */     setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
/* 30 */     this.motionY += 0.004D;
/* 31 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 32 */     this.motionX *= 0.8999999761581421D;
/* 33 */     this.motionY *= 0.8999999761581421D;
/* 34 */     this.motionZ *= 0.8999999761581421D;
/*    */     
/* 36 */     if (this.onGround) {
/*    */       
/* 38 */       this.motionX *= 0.699999988079071D;
/* 39 */       this.motionZ *= 0.699999988079071D;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 47 */       return new EntityExplodeFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityExplodeFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */