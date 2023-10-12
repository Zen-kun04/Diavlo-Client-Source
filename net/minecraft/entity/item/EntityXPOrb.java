/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityXPOrb
/*     */   extends Entity {
/*     */   public int xpColor;
/*     */   public int xpOrbAge;
/*     */   public int delayBeforeCanPickup;
/*  17 */   private int xpOrbHealth = 5;
/*     */   
/*     */   private int xpValue;
/*     */   private EntityPlayer closestPlayer;
/*     */   private int xpTargetColor;
/*     */   
/*     */   public EntityXPOrb(World worldIn, double x, double y, double z, int expValue) {
/*  24 */     super(worldIn);
/*  25 */     setSize(0.5F, 0.5F);
/*  26 */     setPosition(x, y, z);
/*  27 */     this.rotationYaw = (float)(Math.random() * 360.0D);
/*  28 */     this.motionX = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
/*  29 */     this.motionY = ((float)(Math.random() * 0.2D) * 2.0F);
/*  30 */     this.motionZ = ((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
/*  31 */     this.xpValue = expValue;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  36 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityXPOrb(World worldIn) {
/*  41 */     super(worldIn);
/*  42 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/*  51 */     float f = 0.5F;
/*  52 */     f = MathHelper.clamp_float(f, 0.0F, 1.0F);
/*  53 */     int i = super.getBrightnessForRender(partialTicks);
/*  54 */     int j = i & 0xFF;
/*  55 */     int k = i >> 16 & 0xFF;
/*  56 */     j += (int)(f * 15.0F * 16.0F);
/*     */     
/*  58 */     if (j > 240)
/*     */     {
/*  60 */       j = 240;
/*     */     }
/*     */     
/*  63 */     return j | k << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  68 */     super.onUpdate();
/*     */     
/*  70 */     if (this.delayBeforeCanPickup > 0)
/*     */     {
/*  72 */       this.delayBeforeCanPickup--;
/*     */     }
/*     */     
/*  75 */     this.prevPosX = this.posX;
/*  76 */     this.prevPosY = this.posY;
/*  77 */     this.prevPosZ = this.posZ;
/*  78 */     this.motionY -= 0.029999999329447746D;
/*     */     
/*  80 */     if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
/*     */       
/*  82 */       this.motionY = 0.20000000298023224D;
/*  83 */       this.motionX = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/*  84 */       this.motionZ = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/*  85 */       playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
/*     */     } 
/*     */     
/*  88 */     pushOutOfBlocks(this.posX, ((getEntityBoundingBox()).minY + (getEntityBoundingBox()).maxY) / 2.0D, this.posZ);
/*  89 */     double d0 = 8.0D;
/*     */     
/*  91 */     if (this.xpTargetColor < this.xpColor - 20 + getEntityId() % 100) {
/*     */       
/*  93 */       if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0)
/*     */       {
/*  95 */         this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
/*     */       }
/*     */       
/*  98 */       this.xpTargetColor = this.xpColor;
/*     */     } 
/*     */     
/* 101 */     if (this.closestPlayer != null && this.closestPlayer.isSpectator())
/*     */     {
/* 103 */       this.closestPlayer = null;
/*     */     }
/*     */     
/* 106 */     if (this.closestPlayer != null) {
/*     */       
/* 108 */       double d1 = (this.closestPlayer.posX - this.posX) / d0;
/* 109 */       double d2 = (this.closestPlayer.posY + this.closestPlayer.getEyeHeight() - this.posY) / d0;
/* 110 */       double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
/* 111 */       double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/* 112 */       double d5 = 1.0D - d4;
/*     */       
/* 114 */       if (d5 > 0.0D) {
/*     */         
/* 116 */         d5 *= d5;
/* 117 */         this.motionX += d1 / d4 * d5 * 0.1D;
/* 118 */         this.motionY += d2 / d4 * d5 * 0.1D;
/* 119 */         this.motionZ += d3 / d4 * d5 * 0.1D;
/*     */       } 
/*     */     } 
/*     */     
/* 123 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 124 */     float f = 0.98F;
/*     */     
/* 126 */     if (this.onGround)
/*     */     {
/* 128 */       f = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.98F;
/*     */     }
/*     */     
/* 131 */     this.motionX *= f;
/* 132 */     this.motionY *= 0.9800000190734863D;
/* 133 */     this.motionZ *= f;
/*     */     
/* 135 */     if (this.onGround)
/*     */     {
/* 137 */       this.motionY *= -0.8999999761581421D;
/*     */     }
/*     */     
/* 140 */     this.xpColor++;
/* 141 */     this.xpOrbAge++;
/*     */     
/* 143 */     if (this.xpOrbAge >= 6000)
/*     */     {
/* 145 */       setDead();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleWaterMovement() {
/* 151 */     return this.worldObj.handleMaterialAcceleration(getEntityBoundingBox(), Material.water, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dealFireDamage(int amount) {
/* 156 */     attackEntityFrom(DamageSource.inFire, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 161 */     if (isEntityInvulnerable(source))
/*     */     {
/* 163 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 167 */     setBeenAttacked();
/* 168 */     this.xpOrbHealth = (int)(this.xpOrbHealth - amount);
/*     */     
/* 170 */     if (this.xpOrbHealth <= 0)
/*     */     {
/* 172 */       setDead();
/*     */     }
/*     */     
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 181 */     tagCompound.setShort("Health", (short)(byte)this.xpOrbHealth);
/* 182 */     tagCompound.setShort("Age", (short)this.xpOrbAge);
/* 183 */     tagCompound.setShort("Value", (short)this.xpValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 188 */     this.xpOrbHealth = tagCompund.getShort("Health") & 0xFF;
/* 189 */     this.xpOrbAge = tagCompund.getShort("Age");
/* 190 */     this.xpValue = tagCompund.getShort("Value");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn) {
/* 195 */     if (!this.worldObj.isRemote)
/*     */     {
/* 197 */       if (this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
/*     */         
/* 199 */         entityIn.xpCooldown = 2;
/* 200 */         this.worldObj.playSoundAtEntity((Entity)entityIn, "random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
/* 201 */         entityIn.onItemPickup(this, 1);
/* 202 */         entityIn.addExperience(this.xpValue);
/* 203 */         setDead();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getXpValue() {
/* 210 */     return this.xpValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextureByXP() {
/* 215 */     return (this.xpValue >= 2477) ? 10 : ((this.xpValue >= 1237) ? 9 : ((this.xpValue >= 617) ? 8 : ((this.xpValue >= 307) ? 7 : ((this.xpValue >= 149) ? 6 : ((this.xpValue >= 73) ? 5 : ((this.xpValue >= 37) ? 4 : ((this.xpValue >= 17) ? 3 : ((this.xpValue >= 7) ? 2 : ((this.xpValue >= 3) ? 1 : 0)))))))));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getXPSplit(int expValue) {
/* 220 */     return (expValue >= 2477) ? 2477 : ((expValue >= 1237) ? 1237 : ((expValue >= 617) ? 617 : ((expValue >= 307) ? 307 : ((expValue >= 149) ? 149 : ((expValue >= 73) ? 73 : ((expValue >= 37) ? 37 : ((expValue >= 17) ? 17 : ((expValue >= 7) ? 7 : ((expValue >= 3) ? 3 : 1)))))))));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackWithItem() {
/* 225 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityXPOrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */