/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEnderEye
/*     */   extends Entity
/*     */ {
/*     */   private double targetX;
/*     */   private double targetY;
/*     */   private double targetZ;
/*     */   private int despawnTimer;
/*     */   private boolean shatterOrDrop;
/*     */   
/*     */   public EntityEnderEye(World worldIn) {
/*  22 */     super(worldIn);
/*  23 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/*  32 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/*  34 */     if (Double.isNaN(d0))
/*     */     {
/*  36 */       d0 = 4.0D;
/*     */     }
/*     */     
/*  39 */     d0 *= 64.0D;
/*  40 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityEnderEye(World worldIn, double x, double y, double z) {
/*  45 */     super(worldIn);
/*  46 */     this.despawnTimer = 0;
/*  47 */     setSize(0.25F, 0.25F);
/*  48 */     setPosition(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveTowards(BlockPos p_180465_1_) {
/*  53 */     double d0 = p_180465_1_.getX();
/*  54 */     int i = p_180465_1_.getY();
/*  55 */     double d1 = p_180465_1_.getZ();
/*  56 */     double d2 = d0 - this.posX;
/*  57 */     double d3 = d1 - this.posZ;
/*  58 */     float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
/*     */     
/*  60 */     if (f > 12.0F) {
/*     */       
/*  62 */       this.targetX = this.posX + d2 / f * 12.0D;
/*  63 */       this.targetZ = this.posZ + d3 / f * 12.0D;
/*  64 */       this.targetY = this.posY + 8.0D;
/*     */     }
/*     */     else {
/*     */       
/*  68 */       this.targetX = d0;
/*  69 */       this.targetY = i;
/*  70 */       this.targetZ = d1;
/*     */     } 
/*     */     
/*  73 */     this.despawnTimer = 0;
/*  74 */     this.shatterOrDrop = (this.rand.nextInt(5) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVelocity(double x, double y, double z) {
/*  79 */     this.motionX = x;
/*  80 */     this.motionY = y;
/*  81 */     this.motionZ = z;
/*     */     
/*  83 */     if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
/*     */       
/*  85 */       float f = MathHelper.sqrt_double(x * x + z * z);
/*  86 */       this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
/*  87 */       this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, f) * 180.0D / Math.PI);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  93 */     this.lastTickPosX = this.posX;
/*  94 */     this.lastTickPosY = this.posY;
/*  95 */     this.lastTickPosZ = this.posZ;
/*  96 */     super.onUpdate();
/*  97 */     this.posX += this.motionX;
/*  98 */     this.posY += this.motionY;
/*  99 */     this.posZ += this.motionZ;
/* 100 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 101 */     this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
/*     */     
/* 103 */     for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */     {
/* 110 */       this.prevRotationPitch += 360.0F;
/*     */     }
/*     */     
/* 113 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */     {
/* 115 */       this.prevRotationYaw -= 360.0F;
/*     */     }
/*     */     
/* 118 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */     {
/* 120 */       this.prevRotationYaw += 360.0F;
/*     */     }
/*     */     
/* 123 */     this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
/* 124 */     this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
/*     */     
/* 126 */     if (!this.worldObj.isRemote) {
/*     */       
/* 128 */       double d0 = this.targetX - this.posX;
/* 129 */       double d1 = this.targetZ - this.posZ;
/* 130 */       float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
/* 131 */       float f2 = (float)MathHelper.atan2(d1, d0);
/* 132 */       double d2 = f + (f1 - f) * 0.0025D;
/*     */       
/* 134 */       if (f1 < 1.0F) {
/*     */         
/* 136 */         d2 *= 0.8D;
/* 137 */         this.motionY *= 0.8D;
/*     */       } 
/*     */       
/* 140 */       this.motionX = Math.cos(f2) * d2;
/* 141 */       this.motionZ = Math.sin(f2) * d2;
/*     */       
/* 143 */       if (this.posY < this.targetY) {
/*     */         
/* 145 */         this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
/*     */       }
/*     */       else {
/*     */         
/* 149 */         this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
/*     */       } 
/*     */     } 
/*     */     
/* 153 */     float f3 = 0.25F;
/*     */     
/* 155 */     if (isInWater()) {
/*     */       
/* 157 */       for (int i = 0; i < 4; i++)
/*     */       {
/* 159 */         this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 164 */       this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * f3 - 0.5D, this.posZ - this.motionZ * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */     } 
/*     */     
/* 167 */     if (!this.worldObj.isRemote) {
/*     */       
/* 169 */       setPosition(this.posX, this.posY, this.posZ);
/* 170 */       this.despawnTimer++;
/*     */       
/* 172 */       if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
/*     */         
/* 174 */         setDead();
/*     */         
/* 176 */         if (this.shatterOrDrop) {
/*     */           
/* 178 */           this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
/*     */         }
/*     */         else {
/*     */           
/* 182 */           this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */ 
/*     */   
/*     */   public float getBrightness(float partialTicks) {
/* 198 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/* 203 */     return 15728880;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackWithItem() {
/* 208 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityEnderEye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */