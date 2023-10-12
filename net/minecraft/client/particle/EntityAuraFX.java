/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAuraFX
/*    */   extends EntityFX
/*    */ {
/*    */   protected EntityAuraFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double speedIn) {
/*  9 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, speedIn);
/* 10 */     float f = this.rand.nextFloat() * 0.1F + 0.2F;
/* 11 */     this.particleRed = f;
/* 12 */     this.particleGreen = f;
/* 13 */     this.particleBlue = f;
/* 14 */     setParticleTextureIndex(0);
/* 15 */     setSize(0.02F, 0.02F);
/* 16 */     this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
/* 17 */     this.motionX *= 0.019999999552965164D;
/* 18 */     this.motionY *= 0.019999999552965164D;
/* 19 */     this.motionZ *= 0.019999999552965164D;
/* 20 */     this.particleMaxAge = (int)(20.0D / (Math.random() * 0.8D + 0.2D));
/* 21 */     this.noClip = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 26 */     this.prevPosX = this.posX;
/* 27 */     this.prevPosY = this.posY;
/* 28 */     this.prevPosZ = this.posZ;
/* 29 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 30 */     this.motionX *= 0.99D;
/* 31 */     this.motionY *= 0.99D;
/* 32 */     this.motionZ *= 0.99D;
/*    */     
/* 34 */     if (this.particleMaxAge-- <= 0)
/*    */     {
/* 36 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 44 */       return new EntityAuraFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class HappyVillagerFactory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 52 */       EntityFX entityfx = new EntityAuraFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/* 53 */       entityfx.setParticleTextureIndex(82);
/* 54 */       entityfx.setRBGColorF(1.0F, 1.0F, 1.0F);
/* 55 */       return entityfx;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityAuraFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */