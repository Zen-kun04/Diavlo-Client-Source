/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockLiquid;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityRainFX extends EntityFX {
/*    */   protected EntityRainFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
/* 15 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 16 */     this.motionX *= 0.30000001192092896D;
/* 17 */     this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
/* 18 */     this.motionZ *= 0.30000001192092896D;
/* 19 */     this.particleRed = 1.0F;
/* 20 */     this.particleGreen = 1.0F;
/* 21 */     this.particleBlue = 1.0F;
/* 22 */     setParticleTextureIndex(19 + this.rand.nextInt(4));
/* 23 */     setSize(0.01F, 0.01F);
/* 24 */     this.particleGravity = 0.06F;
/* 25 */     this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 30 */     this.prevPosX = this.posX;
/* 31 */     this.prevPosY = this.posY;
/* 32 */     this.prevPosZ = this.posZ;
/* 33 */     this.motionY -= this.particleGravity;
/* 34 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 35 */     this.motionX *= 0.9800000190734863D;
/* 36 */     this.motionY *= 0.9800000190734863D;
/* 37 */     this.motionZ *= 0.9800000190734863D;
/*    */     
/* 39 */     if (this.particleMaxAge-- <= 0)
/*    */     {
/* 41 */       setDead();
/*    */     }
/*    */     
/* 44 */     if (this.onGround) {
/*    */       
/* 46 */       if (Math.random() < 0.5D)
/*    */       {
/* 48 */         setDead();
/*    */       }
/*    */       
/* 51 */       this.motionX *= 0.699999988079071D;
/* 52 */       this.motionZ *= 0.699999988079071D;
/*    */     } 
/*    */     
/* 55 */     BlockPos blockpos = new BlockPos(this);
/* 56 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 57 */     Block block = iblockstate.getBlock();
/* 58 */     block.setBlockBoundsBasedOnState((IBlockAccess)this.worldObj, blockpos);
/* 59 */     Material material = iblockstate.getBlock().getMaterial();
/*    */     
/* 61 */     if (material.isLiquid() || material.isSolid()) {
/*    */       
/* 63 */       double d0 = 0.0D;
/*    */       
/* 65 */       if (iblockstate.getBlock() instanceof BlockLiquid) {
/*    */         
/* 67 */         d0 = (1.0F - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue()));
/*    */       }
/*    */       else {
/*    */         
/* 71 */         d0 = block.getBlockBoundsMaxY();
/*    */       } 
/*    */       
/* 74 */       double d1 = MathHelper.floor_double(this.posY) + d0;
/*    */       
/* 76 */       if (this.posY < d1)
/*    */       {
/* 78 */         setDead();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Factory
/*    */     implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 87 */       return new EntityRainFX(worldIn, xCoordIn, yCoordIn, zCoordIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityRainFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */