/*     */ package net.minecraft.entity.monster;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAICreeperSwell;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityCreeper extends EntityMob {
/*     */   private int lastActiveTime;
/*  28 */   private int fuseTime = 30; private int timeSinceIgnited;
/*  29 */   private int explosionRadius = 3;
/*  30 */   private int field_175494_bm = 0;
/*     */ 
/*     */   
/*     */   public EntityCreeper(World worldIn) {
/*  34 */     super(worldIn);
/*  35 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  36 */     this.tasks.addTask(2, (EntityAIBase)new EntityAICreeperSwell(this));
/*  37 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
/*  38 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide(this, 1.0D, false));
/*  39 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIWander(this, 0.8D));
/*  40 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  41 */     this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  42 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*  43 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  48 */     super.applyEntityAttributes();
/*  49 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFallHeight() {
/*  54 */     return (getAttackTarget() == null) ? 3 : (3 + (int)(getHealth() - 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {
/*  59 */     super.fall(distance, damageMultiplier);
/*  60 */     this.timeSinceIgnited = (int)(this.timeSinceIgnited + distance * 1.5F);
/*     */     
/*  62 */     if (this.timeSinceIgnited > this.fuseTime - 5)
/*     */     {
/*  64 */       this.timeSinceIgnited = this.fuseTime - 5;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  70 */     super.entityInit();
/*  71 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)-1));
/*  72 */     this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
/*  73 */     this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  78 */     super.writeEntityToNBT(tagCompound);
/*     */     
/*  80 */     if (this.dataWatcher.getWatchableObjectByte(17) == 1)
/*     */     {
/*  82 */       tagCompound.setBoolean("powered", true);
/*     */     }
/*     */     
/*  85 */     tagCompound.setShort("Fuse", (short)this.fuseTime);
/*  86 */     tagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
/*  87 */     tagCompound.setBoolean("ignited", hasIgnited());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  92 */     super.readEntityFromNBT(tagCompund);
/*  93 */     this.dataWatcher.updateObject(17, Byte.valueOf((byte)(tagCompund.getBoolean("powered") ? 1 : 0)));
/*     */     
/*  95 */     if (tagCompund.hasKey("Fuse", 99))
/*     */     {
/*  97 */       this.fuseTime = tagCompund.getShort("Fuse");
/*     */     }
/*     */     
/* 100 */     if (tagCompund.hasKey("ExplosionRadius", 99))
/*     */     {
/* 102 */       this.explosionRadius = tagCompund.getByte("ExplosionRadius");
/*     */     }
/*     */     
/* 105 */     if (tagCompund.getBoolean("ignited"))
/*     */     {
/* 107 */       ignite();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 113 */     if (isEntityAlive()) {
/*     */       
/* 115 */       this.lastActiveTime = this.timeSinceIgnited;
/*     */       
/* 117 */       if (hasIgnited())
/*     */       {
/* 119 */         setCreeperState(1);
/*     */       }
/*     */       
/* 122 */       int i = getCreeperState();
/*     */       
/* 124 */       if (i > 0 && this.timeSinceIgnited == 0)
/*     */       {
/* 126 */         playSound("creeper.primed", 1.0F, 0.5F);
/*     */       }
/*     */       
/* 129 */       this.timeSinceIgnited += i;
/*     */       
/* 131 */       if (this.timeSinceIgnited < 0)
/*     */       {
/* 133 */         this.timeSinceIgnited = 0;
/*     */       }
/*     */       
/* 136 */       if (this.timeSinceIgnited >= this.fuseTime) {
/*     */         
/* 138 */         this.timeSinceIgnited = this.fuseTime;
/* 139 */         explode();
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 148 */     return "mob.creeper.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 153 */     return "mob.creeper.death";
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeath(DamageSource cause) {
/* 158 */     super.onDeath(cause);
/*     */     
/* 160 */     if (cause.getEntity() instanceof EntitySkeleton) {
/*     */       
/* 162 */       int i = Item.getIdFromItem(Items.record_13);
/* 163 */       int j = Item.getIdFromItem(Items.record_wait);
/* 164 */       int k = i + this.rand.nextInt(j - i + 1);
/* 165 */       dropItem(Item.getItemById(k), 1);
/*     */     }
/* 167 */     else if (cause.getEntity() instanceof EntityCreeper && cause.getEntity() != this && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
/*     */       
/* 169 */       ((EntityCreeper)cause.getEntity()).func_175493_co();
/* 170 */       entityDropItem(new ItemStack(Items.skull, 1, 4), 0.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 176 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getPowered() {
/* 181 */     return (this.dataWatcher.getWatchableObjectByte(17) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getCreeperFlashIntensity(float p_70831_1_) {
/* 186 */     return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 191 */     return Items.gunpowder;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCreeperState() {
/* 196 */     return this.dataWatcher.getWatchableObjectByte(16);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreeperState(int state) {
/* 201 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)state));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStruckByLightning(EntityLightningBolt lightningBolt) {
/* 206 */     super.onStruckByLightning(lightningBolt);
/* 207 */     this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean interact(EntityPlayer player) {
/* 212 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 214 */     if (itemstack != null && itemstack.getItem() == Items.flint_and_steel) {
/*     */       
/* 216 */       this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.ignite", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
/* 217 */       player.swingItem();
/*     */       
/* 219 */       if (!this.worldObj.isRemote) {
/*     */         
/* 221 */         ignite();
/* 222 */         itemstack.damageItem(1, (EntityLivingBase)player);
/* 223 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     return super.interact(player);
/*     */   }
/*     */ 
/*     */   
/*     */   private void explode() {
/* 232 */     if (!this.worldObj.isRemote) {
/*     */       
/* 234 */       boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
/* 235 */       float f = getPowered() ? 2.0F : 1.0F;
/* 236 */       this.worldObj.createExplosion((Entity)this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
/* 237 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasIgnited() {
/* 243 */     return (this.dataWatcher.getWatchableObjectByte(18) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ignite() {
/* 248 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAIEnabled() {
/* 253 */     return (this.field_175494_bm < 1 && this.worldObj.getGameRules().getBoolean("doMobLoot"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_175493_co() {
/* 258 */     this.field_175494_bm++;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityCreeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */