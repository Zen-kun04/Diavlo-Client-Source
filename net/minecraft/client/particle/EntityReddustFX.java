/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityReddustFX
/*    */   extends EntityFX
/*    */ {
/*    */   float reddustParticleScale;
/*    */   
/*    */   protected EntityReddustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46349_8_, float p_i46349_9_, float p_i46349_10_) {
/* 14 */     this(worldIn, xCoordIn, yCoordIn, zCoordIn, 1.0F, p_i46349_8_, p_i46349_9_, p_i46349_10_);
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityReddustFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float p_i46350_8_, float p_i46350_9_, float p_i46350_10_, float p_i46350_11_) {
/* 19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 20 */     this.motionX *= 0.10000000149011612D;
/* 21 */     this.motionY *= 0.10000000149011612D;
/* 22 */     this.motionZ *= 0.10000000149011612D;
/*    */     
/* 24 */     if (p_i46350_9_ == 0.0F)
/*    */     {
/* 26 */       p_i46350_9_ = 1.0F;
/*    */     }
/*    */     
/* 29 */     float f = (float)Math.random() * 0.4F + 0.6F;
/* 30 */     this.particleRed = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_9_ * f;
/* 31 */     this.particleGreen = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_10_ * f;
/* 32 */     this.particleBlue = ((float)(Math.random() * 0.20000000298023224D) + 0.8F) * p_i46350_11_ * f;
/* 33 */     this.particleScale *= 0.75F;
/* 34 */     this.particleScale *= p_i46350_8_;
/* 35 */     this.reddustParticleScale = this.particleScale;
/* 36 */     this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
/* 37 */     this.particleMaxAge = (int)(this.particleMaxAge * p_i46350_8_);
/* 38 */     this.noClip = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 43 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 44 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 45 */     this.particleScale = this.reddustParticleScale * f;
/* 46 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 51 */     this.prevPosX = this.posX;
/* 52 */     this.prevPosY = this.posY;
/* 53 */     this.prevPosZ = this.posZ;
/*    */     
/* 55 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 57 */       setDead();
/*    */     }
/*    */     
/* 60 */     setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
/* 61 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*    */     
/* 63 */     if (this.posY == this.prevPosY) {
/*    */       
/* 65 */       this.motionX *= 1.1D;
/* 66 */       this.motionZ *= 1.1D;
/*    */     } 
/*    */     
/* 69 */     this.motionX *= 0.9599999785423279D;
/* 70 */     this.motionY *= 0.9599999785423279D;
/* 71 */     this.motionZ *= 0.9599999785423279D;
/*    */     
/* 73 */     if (this.onGround) {
/*    */       
/* 75 */       this.motionX *= 0.699999988079071D;
/* 76 */       this.motionZ *= 0.699999988079071D;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 84 */       return new EntityReddustFX(worldIn, xCoordIn, yCoordIn, zCoordIn, (float)xSpeedIn, (float)ySpeedIn, (float)zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityReddustFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */