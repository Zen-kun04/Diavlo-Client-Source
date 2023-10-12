/*     */ package net.minecraft.entity.monster;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEndermite extends EntityMob {
/*  22 */   private int lifetime = 0;
/*     */   
/*     */   private boolean playerSpawned = false;
/*     */   
/*     */   public EntityEndermite(World worldIn) {
/*  27 */     super(worldIn);
/*  28 */     this.experienceValue = 3;
/*  29 */     setSize(0.4F, 0.3F);
/*  30 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  31 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
/*  32 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  33 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  34 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  35 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
/*  36 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/*  41 */     return 0.1F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  46 */     super.applyEntityAttributes();
/*  47 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*  48 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*  49 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  59 */     return "mob.silverfish.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  64 */     return "mob.silverfish.hit";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  69 */     return "mob.silverfish.kill";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  74 */     playSound("mob.silverfish.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  84 */     super.readEntityFromNBT(tagCompund);
/*  85 */     this.lifetime = tagCompund.getInteger("Lifetime");
/*  86 */     this.playerSpawned = tagCompund.getBoolean("PlayerSpawned");
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  91 */     super.writeEntityToNBT(tagCompound);
/*  92 */     tagCompound.setInteger("Lifetime", this.lifetime);
/*  93 */     tagCompound.setBoolean("PlayerSpawned", this.playerSpawned);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  98 */     this.renderYawOffset = this.rotationYaw;
/*  99 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpawnedByPlayer() {
/* 104 */     return this.playerSpawned;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnedByPlayer(boolean spawnedByPlayer) {
/* 109 */     this.playerSpawned = spawnedByPlayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 114 */     super.onLivingUpdate();
/*     */     
/* 116 */     if (this.worldObj.isRemote) {
/*     */       
/* 118 */       for (int i = 0; i < 2; i++)
/*     */       {
/* 120 */         this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 125 */       if (!isNoDespawnRequired())
/*     */       {
/* 127 */         this.lifetime++;
/*     */       }
/*     */       
/* 130 */       if (this.lifetime >= 2400)
/*     */       {
/* 132 */         setDead();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidLightLevel() {
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 144 */     if (super.getCanSpawnHere()) {
/*     */       
/* 146 */       EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity((Entity)this, 5.0D);
/* 147 */       return (entityplayer == null);
/*     */     } 
/*     */ 
/*     */     
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumCreatureAttribute getCreatureAttribute() {
/* 157 */     return EnumCreatureAttribute.ARTHROPOD;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityEndermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */