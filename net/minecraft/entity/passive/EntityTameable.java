/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityOwnable;
/*     */ import net.minecraft.entity.ai.EntityAISit;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.server.management.PreYggdrasilConverter;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {
/*  17 */   protected EntityAISit aiSit = new EntityAISit(this);
/*     */ 
/*     */   
/*     */   public EntityTameable(World worldIn) {
/*  21 */     super(worldIn);
/*  22 */     setupTamedAI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  27 */     super.entityInit();
/*  28 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*  29 */     this.dataWatcher.addObject(17, "");
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  34 */     super.writeEntityToNBT(tagCompound);
/*     */     
/*  36 */     if (getOwnerId() == null) {
/*     */       
/*  38 */       tagCompound.setString("OwnerUUID", "");
/*     */     }
/*     */     else {
/*     */       
/*  42 */       tagCompound.setString("OwnerUUID", getOwnerId());
/*     */     } 
/*     */     
/*  45 */     tagCompound.setBoolean("Sitting", isSitting());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  50 */     super.readEntityFromNBT(tagCompund);
/*  51 */     String s = "";
/*     */     
/*  53 */     if (tagCompund.hasKey("OwnerUUID", 8)) {
/*     */       
/*  55 */       s = tagCompund.getString("OwnerUUID");
/*     */     }
/*     */     else {
/*     */       
/*  59 */       String s1 = tagCompund.getString("Owner");
/*  60 */       s = PreYggdrasilConverter.getStringUUIDFromName(s1);
/*     */     } 
/*     */     
/*  63 */     if (s.length() > 0) {
/*     */       
/*  65 */       setOwnerId(s);
/*  66 */       setTamed(true);
/*     */     } 
/*     */     
/*  69 */     this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
/*  70 */     setSitting(tagCompund.getBoolean("Sitting"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playTameEffect(boolean play) {
/*  75 */     EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
/*     */     
/*  77 */     if (!play)
/*     */     {
/*  79 */       enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
/*     */     }
/*     */     
/*  82 */     for (int i = 0; i < 7; i++) {
/*     */       
/*  84 */       double d0 = this.rand.nextGaussian() * 0.02D;
/*  85 */       double d1 = this.rand.nextGaussian() * 0.02D;
/*  86 */       double d2 = this.rand.nextGaussian() * 0.02D;
/*  87 */       this.worldObj.spawnParticle(enumparticletypes, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/*  93 */     if (id == 7) {
/*     */       
/*  95 */       playTameEffect(true);
/*     */     }
/*  97 */     else if (id == 6) {
/*     */       
/*  99 */       playTameEffect(false);
/*     */     }
/*     */     else {
/*     */       
/* 103 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTamed() {
/* 109 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTamed(boolean tamed) {
/* 114 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 116 */     if (tamed) {
/*     */       
/* 118 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x4)));
/*     */     }
/*     */     else {
/*     */       
/* 122 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFB)));
/*     */     } 
/*     */     
/* 125 */     setupTamedAI();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setupTamedAI() {}
/*     */ 
/*     */   
/*     */   public boolean isSitting() {
/* 134 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSitting(boolean sitting) {
/* 139 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 141 */     if (sitting) {
/*     */       
/* 143 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else {
/*     */       
/* 147 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOwnerId() {
/* 153 */     return this.dataWatcher.getWatchableObjectString(17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOwnerId(String ownerUuid) {
/* 158 */     this.dataWatcher.updateObject(17, ownerUuid);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityLivingBase getOwner() {
/*     */     try {
/* 165 */       UUID uuid = UUID.fromString(getOwnerId());
/* 166 */       return (uuid == null) ? null : (EntityLivingBase)this.worldObj.getPlayerEntityByUUID(uuid);
/*     */     }
/* 168 */     catch (IllegalArgumentException var2) {
/*     */       
/* 170 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOwner(EntityLivingBase entityIn) {
/* 176 */     return (entityIn == getOwner());
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityAISit getAISit() {
/* 181 */     return this.aiSit;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Team getTeam() {
/* 191 */     if (isTamed()) {
/*     */       
/* 193 */       EntityLivingBase entitylivingbase = getOwner();
/*     */       
/* 195 */       if (entitylivingbase != null)
/*     */       {
/* 197 */         return entitylivingbase.getTeam();
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return super.getTeam();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnSameTeam(EntityLivingBase otherEntity) {
/* 206 */     if (isTamed()) {
/*     */       
/* 208 */       EntityLivingBase entitylivingbase = getOwner();
/*     */       
/* 210 */       if (otherEntity == entitylivingbase)
/*     */       {
/* 212 */         return true;
/*     */       }
/*     */       
/* 215 */       if (entitylivingbase != null)
/*     */       {
/* 217 */         return entitylivingbase.isOnSameTeam(otherEntity);
/*     */       }
/*     */     } 
/*     */     
/* 221 */     return super.isOnSameTeam(otherEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeath(DamageSource cause) {
/* 226 */     if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages") && hasCustomName() && getOwner() instanceof EntityPlayerMP)
/*     */     {
/* 228 */       ((EntityPlayerMP)getOwner()).addChatMessage(getCombatTracker().getDeathMessage());
/*     */     }
/*     */     
/* 231 */     super.onDeath(cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityTameable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */