/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityDropParticleFX
/*     */   extends EntityFX {
/*     */   private Material materialType;
/*     */   private int bobTimer;
/*     */   
/*     */   protected EntityDropParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_) {
/*  18 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/*  19 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/*     */     
/*  21 */     if (p_i1203_8_ == Material.water) {
/*     */       
/*  23 */       this.particleRed = 0.0F;
/*  24 */       this.particleGreen = 0.0F;
/*  25 */       this.particleBlue = 1.0F;
/*     */     }
/*     */     else {
/*     */       
/*  29 */       this.particleRed = 1.0F;
/*  30 */       this.particleGreen = 0.0F;
/*  31 */       this.particleBlue = 0.0F;
/*     */     } 
/*     */     
/*  34 */     setParticleTextureIndex(113);
/*  35 */     setSize(0.01F, 0.01F);
/*  36 */     this.particleGravity = 0.06F;
/*  37 */     this.materialType = p_i1203_8_;
/*  38 */     this.bobTimer = 40;
/*  39 */     this.particleMaxAge = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
/*  40 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/*  45 */     return (this.materialType == Material.water) ? super.getBrightnessForRender(partialTicks) : 257;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness(float partialTicks) {
/*  50 */     return (this.materialType == Material.water) ? super.getBrightness(partialTicks) : 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  55 */     this.prevPosX = this.posX;
/*  56 */     this.prevPosY = this.posY;
/*  57 */     this.prevPosZ = this.posZ;
/*     */     
/*  59 */     if (this.materialType == Material.water) {
/*     */       
/*  61 */       this.particleRed = 0.2F;
/*  62 */       this.particleGreen = 0.3F;
/*  63 */       this.particleBlue = 1.0F;
/*     */     }
/*     */     else {
/*     */       
/*  67 */       this.particleRed = 1.0F;
/*  68 */       this.particleGreen = 16.0F / (40 - this.bobTimer + 16);
/*  69 */       this.particleBlue = 4.0F / (40 - this.bobTimer + 8);
/*     */     } 
/*     */     
/*  72 */     this.motionY -= this.particleGravity;
/*     */     
/*  74 */     if (this.bobTimer-- > 0) {
/*     */       
/*  76 */       this.motionX *= 0.02D;
/*  77 */       this.motionY *= 0.02D;
/*  78 */       this.motionZ *= 0.02D;
/*  79 */       setParticleTextureIndex(113);
/*     */     }
/*     */     else {
/*     */       
/*  83 */       setParticleTextureIndex(112);
/*     */     } 
/*     */     
/*  86 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*  87 */     this.motionX *= 0.9800000190734863D;
/*  88 */     this.motionY *= 0.9800000190734863D;
/*  89 */     this.motionZ *= 0.9800000190734863D;
/*     */     
/*  91 */     if (this.particleMaxAge-- <= 0)
/*     */     {
/*  93 */       setDead();
/*     */     }
/*     */     
/*  96 */     if (this.onGround) {
/*     */       
/*  98 */       if (this.materialType == Material.water) {
/*     */         
/* 100 */         setDead();
/* 101 */         this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */       else {
/*     */         
/* 105 */         setParticleTextureIndex(114);
/*     */       } 
/*     */       
/* 108 */       this.motionX *= 0.699999988079071D;
/* 109 */       this.motionZ *= 0.699999988079071D;
/*     */     } 
/*     */     
/* 112 */     BlockPos blockpos = new BlockPos(this);
/* 113 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 114 */     Material material = iblockstate.getBlock().getMaterial();
/*     */     
/* 116 */     if (material.isLiquid() || material.isSolid()) {
/*     */       
/* 118 */       double d0 = 0.0D;
/*     */       
/* 120 */       if (iblockstate.getBlock() instanceof BlockLiquid)
/*     */       {
/* 122 */         d0 = BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue());
/*     */       }
/*     */       
/* 125 */       double d1 = (MathHelper.floor_double(this.posY) + 1) - d0;
/*     */       
/* 127 */       if (this.posY < d1)
/*     */       {
/* 129 */         setDead();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class LavaFactory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 138 */       return new EntityDropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.lava);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class WaterFactory
/*     */     implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
/* 146 */       return new EntityDropParticleFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.water);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityDropParticleFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */