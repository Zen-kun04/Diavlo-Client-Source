/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityBat
/*     */   extends EntityAmbientCreature
/*     */ {
/*     */   private BlockPos spawnPosition;
/*     */   
/*     */   public EntityBat(World worldIn) {
/*  20 */     super(worldIn);
/*  21 */     setSize(0.5F, 0.9F);
/*  22 */     setIsBatHanging(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  27 */     super.entityInit();
/*  28 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/*  33 */     return 0.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundPitch() {
/*  38 */     return super.getSoundPitch() * 0.95F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  43 */     return (getIsBatHanging() && this.rand.nextInt(4) != 0) ? null : "mob.bat.idle";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  48 */     return "mob.bat.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  53 */     return "mob.bat.death";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBePushed() {
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collideWithEntity(Entity entityIn) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collideWithNearbyEntities() {}
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  71 */     super.applyEntityAttributes();
/*  72 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsBatHanging() {
/*  77 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsBatHanging(boolean isHanging) {
/*  82 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/*  84 */     if (isHanging) {
/*     */       
/*  86 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else {
/*     */       
/*  90 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  96 */     super.onUpdate();
/*     */ 
/*     */ 
/*     */     
/* 100 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/* 101 */     this.posY = MathHelper.floor_double(this.posY) + 1.0D - this.height;
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.motionY *= 0.6000000238418579D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/* 111 */     super.updateAITasks();
/* 112 */     BlockPos blockpos = new BlockPos((Entity)this);
/* 113 */     BlockPos blockpos1 = blockpos.up();
/*     */     
/* 115 */     if (getIsBatHanging()) {
/*     */       
/* 117 */       if (!this.worldObj.getBlockState(blockpos1).getBlock().isNormalCube()) {
/*     */         
/* 119 */         setIsBatHanging(false);
/* 120 */         this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, blockpos, 0);
/*     */       }
/*     */       else {
/*     */         
/* 124 */         if (this.rand.nextInt(200) == 0)
/*     */         {
/* 126 */           this.rotationYawHead = this.rand.nextInt(360);
/*     */         }
/*     */         
/* 129 */         if (this.worldObj.getClosestPlayerToEntity((Entity)this, 4.0D) != null)
/*     */         {
/* 131 */           setIsBatHanging(false);
/* 132 */           this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, blockpos, 0);
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/* 138 */       if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1))
/*     */       {
/* 140 */         this.spawnPosition = null;
/*     */       }
/*     */       
/* 143 */       if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0D)
/*     */       {
/* 145 */         this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
/*     */       }
/*     */       
/* 148 */       double d0 = this.spawnPosition.getX() + 0.5D - this.posX;
/* 149 */       double d1 = this.spawnPosition.getY() + 0.1D - this.posY;
/* 150 */       double d2 = this.spawnPosition.getZ() + 0.5D - this.posZ;
/* 151 */       this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
/* 152 */       this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
/* 153 */       this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
/* 154 */       float f = (float)(MathHelper.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
/* 155 */       float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
/* 156 */       this.moveForward = 0.5F;
/* 157 */       this.rotationYaw += f1;
/*     */       
/* 159 */       if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(blockpos1).getBlock().isNormalCube())
/*     */       {
/* 161 */         setIsBatHanging(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}
/*     */ 
/*     */   
/*     */   public boolean doesEntityNotTriggerPressurePlate() {
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 186 */     if (isEntityInvulnerable(source))
/*     */     {
/* 188 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 192 */     if (!this.worldObj.isRemote && getIsBatHanging())
/*     */     {
/* 194 */       setIsBatHanging(false);
/*     */     }
/*     */     
/* 197 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 203 */     super.readEntityFromNBT(tagCompund);
/* 204 */     this.dataWatcher.updateObject(16, Byte.valueOf(tagCompund.getByte("BatFlags")));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 209 */     super.writeEntityToNBT(tagCompound);
/* 210 */     tagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 215 */     BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
/*     */     
/* 217 */     if (blockpos.getY() >= this.worldObj.getSeaLevel())
/*     */     {
/* 219 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 223 */     int i = this.worldObj.getLightFromNeighbors(blockpos);
/* 224 */     int j = 4;
/*     */     
/* 226 */     if (isDateAroundHalloween(this.worldObj.getCurrentDate())) {
/*     */       
/* 228 */       j = 7;
/*     */     }
/* 230 */     else if (this.rand.nextBoolean()) {
/*     */       
/* 232 */       return false;
/*     */     } 
/*     */     
/* 235 */     return (i > this.rand.nextInt(j)) ? false : super.getCanSpawnHere();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isDateAroundHalloween(Calendar p_175569_1_) {
/* 241 */     return ((p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20) || (p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 246 */     return this.height / 2.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityBat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */