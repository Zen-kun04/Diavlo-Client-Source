/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityFireworkRocket
/*     */   extends Entity {
/*     */   private int fireworkAge;
/*     */   private int lifetime;
/*     */   
/*     */   public EntityFireworkRocket(World worldIn) {
/*  17 */     super(worldIn);
/*  18 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  23 */     this.dataWatcher.addObjectByDataType(8, 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/*  28 */     return (distance < 4096.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem) {
/*  33 */     super(worldIn);
/*  34 */     this.fireworkAge = 0;
/*  35 */     setSize(0.25F, 0.25F);
/*  36 */     setPosition(x, y, z);
/*  37 */     int i = 1;
/*     */     
/*  39 */     if (givenItem != null && givenItem.hasTagCompound()) {
/*     */       
/*  41 */       this.dataWatcher.updateObject(8, givenItem);
/*  42 */       NBTTagCompound nbttagcompound = givenItem.getTagCompound();
/*  43 */       NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
/*     */       
/*  45 */       if (nbttagcompound1 != null)
/*     */       {
/*  47 */         i += nbttagcompound1.getByte("Flight");
/*     */       }
/*     */     } 
/*     */     
/*  51 */     this.motionX = this.rand.nextGaussian() * 0.001D;
/*  52 */     this.motionZ = this.rand.nextGaussian() * 0.001D;
/*  53 */     this.motionY = 0.05D;
/*  54 */     this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVelocity(double x, double y, double z) {
/*  59 */     this.motionX = x;
/*  60 */     this.motionY = y;
/*  61 */     this.motionZ = z;
/*     */     
/*  63 */     if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
/*     */       
/*  65 */       float f = MathHelper.sqrt_double(x * x + z * z);
/*  66 */       this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
/*  67 */       this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, f) * 180.0D / Math.PI);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  73 */     this.lastTickPosX = this.posX;
/*  74 */     this.lastTickPosY = this.posY;
/*  75 */     this.lastTickPosZ = this.posZ;
/*  76 */     super.onUpdate();
/*  77 */     this.motionX *= 1.15D;
/*  78 */     this.motionZ *= 1.15D;
/*  79 */     this.motionY += 0.04D;
/*  80 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/*  81 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*  82 */     this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
/*     */     
/*  84 */     for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */     {
/*  91 */       this.prevRotationPitch += 360.0F;
/*     */     }
/*     */     
/*  94 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */     {
/*  96 */       this.prevRotationYaw -= 360.0F;
/*     */     }
/*     */     
/*  99 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */     {
/* 101 */       this.prevRotationYaw += 360.0F;
/*     */     }
/*     */     
/* 104 */     this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
/* 105 */     this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
/*     */     
/* 107 */     if (this.fireworkAge == 0 && !isSilent())
/*     */     {
/* 109 */       this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
/*     */     }
/*     */     
/* 112 */     this.fireworkAge++;
/*     */     
/* 114 */     if (this.worldObj.isRemote && this.fireworkAge % 2 < 2)
/*     */     {
/* 116 */       this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]);
/*     */     }
/*     */     
/* 119 */     if (!this.worldObj.isRemote && this.fireworkAge > this.lifetime) {
/*     */       
/* 121 */       this.worldObj.setEntityState(this, (byte)17);
/* 122 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 128 */     if (id == 17 && this.worldObj.isRemote) {
/*     */       
/* 130 */       ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
/* 131 */       NBTTagCompound nbttagcompound = null;
/*     */       
/* 133 */       if (itemstack != null && itemstack.hasTagCompound())
/*     */       {
/* 135 */         nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
/*     */       }
/*     */       
/* 138 */       this.worldObj.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
/*     */     } 
/*     */     
/* 141 */     super.handleStatusUpdate(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 146 */     tagCompound.setInteger("Life", this.fireworkAge);
/* 147 */     tagCompound.setInteger("LifeTime", this.lifetime);
/* 148 */     ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
/*     */     
/* 150 */     if (itemstack != null) {
/*     */       
/* 152 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 153 */       itemstack.writeToNBT(nbttagcompound);
/* 154 */       tagCompound.setTag("FireworksItem", (NBTBase)nbttagcompound);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 160 */     this.fireworkAge = tagCompund.getInteger("Life");
/* 161 */     this.lifetime = tagCompund.getInteger("LifeTime");
/* 162 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("FireworksItem");
/*     */     
/* 164 */     if (nbttagcompound != null) {
/*     */       
/* 166 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       
/* 168 */       if (itemstack != null)
/*     */       {
/* 170 */         this.dataWatcher.updateObject(8, itemstack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness(float partialTicks) {
/* 177 */     return super.getBrightness(partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/* 182 */     return super.getBrightnessForRender(partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackWithItem() {
/* 187 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityFireworkRocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */