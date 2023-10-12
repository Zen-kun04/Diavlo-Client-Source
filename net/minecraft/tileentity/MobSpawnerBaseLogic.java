/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public abstract class MobSpawnerBaseLogic
/*     */ {
/*  23 */   private int spawnDelay = 20;
/*  24 */   private String mobID = "Pig";
/*  25 */   private final List<WeightedRandomMinecart> minecartToSpawn = Lists.newArrayList();
/*     */   private WeightedRandomMinecart randomEntity;
/*     */   private double mobRotation;
/*     */   private double prevMobRotation;
/*  29 */   private int minSpawnDelay = 200;
/*  30 */   private int maxSpawnDelay = 800;
/*  31 */   private int spawnCount = 4;
/*     */   private Entity cachedEntity;
/*  33 */   private int maxNearbyEntities = 6;
/*  34 */   private int activatingRangeFromPlayer = 16;
/*  35 */   private int spawnRange = 4;
/*     */ 
/*     */   
/*     */   private String getEntityNameToSpawn() {
/*  39 */     if (getRandomEntity() == null) {
/*     */       
/*  41 */       if (this.mobID != null && this.mobID.equals("Minecart"))
/*     */       {
/*  43 */         this.mobID = "MinecartRideable";
/*     */       }
/*     */       
/*  46 */       return this.mobID;
/*     */     } 
/*     */ 
/*     */     
/*  50 */     return (getRandomEntity()).entityType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityName(String name) {
/*  56 */     this.mobID = name;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isActivated() {
/*  61 */     BlockPos blockpos = getSpawnerPosition();
/*  62 */     return getSpawnerWorld().isAnyPlayerWithinRangeAt(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, this.activatingRangeFromPlayer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSpawner() {
/*  67 */     if (isActivated()) {
/*     */       
/*  69 */       BlockPos blockpos = getSpawnerPosition();
/*     */       
/*  71 */       if ((getSpawnerWorld()).isRemote) {
/*     */         
/*  73 */         double d3 = (blockpos.getX() + (getSpawnerWorld()).rand.nextFloat());
/*  74 */         double d4 = (blockpos.getY() + (getSpawnerWorld()).rand.nextFloat());
/*  75 */         double d5 = (blockpos.getZ() + (getSpawnerWorld()).rand.nextFloat());
/*  76 */         getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
/*  77 */         getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         
/*  79 */         if (this.spawnDelay > 0)
/*     */         {
/*  81 */           this.spawnDelay--;
/*     */         }
/*     */         
/*  84 */         this.prevMobRotation = this.mobRotation;
/*  85 */         this.mobRotation = (this.mobRotation + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
/*     */       }
/*     */       else {
/*     */         
/*  89 */         if (this.spawnDelay == -1)
/*     */         {
/*  91 */           resetTimer();
/*     */         }
/*     */         
/*  94 */         if (this.spawnDelay > 0) {
/*     */           
/*  96 */           this.spawnDelay--;
/*     */           
/*     */           return;
/*     */         } 
/* 100 */         boolean flag = false;
/*     */         
/* 102 */         for (int i = 0; i < this.spawnCount; i++) {
/*     */           
/* 104 */           Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), getSpawnerWorld());
/*     */           
/* 106 */           if (entity == null) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 111 */           int j = getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (blockpos.getX() + 1), (blockpos.getY() + 1), (blockpos.getZ() + 1))).expand(this.spawnRange, this.spawnRange, this.spawnRange)).size();
/*     */           
/* 113 */           if (j >= this.maxNearbyEntities) {
/*     */             
/* 115 */             resetTimer();
/*     */             
/*     */             return;
/*     */           } 
/* 119 */           double d0 = blockpos.getX() + ((getSpawnerWorld()).rand.nextDouble() - (getSpawnerWorld()).rand.nextDouble()) * this.spawnRange + 0.5D;
/* 120 */           double d1 = (blockpos.getY() + (getSpawnerWorld()).rand.nextInt(3) - 1);
/* 121 */           double d2 = blockpos.getZ() + ((getSpawnerWorld()).rand.nextDouble() - (getSpawnerWorld()).rand.nextDouble()) * this.spawnRange + 0.5D;
/* 122 */           EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
/* 123 */           entity.setLocationAndAngles(d0, d1, d2, (getSpawnerWorld()).rand.nextFloat() * 360.0F, 0.0F);
/*     */           
/* 125 */           if (entityliving == null || (entityliving.getCanSpawnHere() && entityliving.isNotColliding())) {
/*     */             
/* 127 */             spawnNewEntity(entity, true);
/* 128 */             getSpawnerWorld().playAuxSFX(2004, blockpos, 0);
/*     */             
/* 130 */             if (entityliving != null)
/*     */             {
/* 132 */               entityliving.spawnExplosionParticle();
/*     */             }
/*     */             
/* 135 */             flag = true;
/*     */           } 
/*     */         } 
/*     */         
/* 139 */         if (flag)
/*     */         {
/* 141 */           resetTimer();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Entity spawnNewEntity(Entity entityIn, boolean spawn) {
/* 149 */     if (getRandomEntity() != null) {
/*     */       
/* 151 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 152 */       entityIn.writeToNBTOptional(nbttagcompound);
/*     */       
/* 154 */       for (String s : (getRandomEntity()).nbtData.getKeySet()) {
/*     */         
/* 156 */         NBTBase nbtbase = (getRandomEntity()).nbtData.getTag(s);
/* 157 */         nbttagcompound.setTag(s, nbtbase.copy());
/*     */       } 
/*     */       
/* 160 */       entityIn.readFromNBT(nbttagcompound);
/*     */       
/* 162 */       if (entityIn.worldObj != null && spawn)
/*     */       {
/* 164 */         entityIn.worldObj.spawnEntityInWorld(entityIn);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 169 */       for (Entity entity = entityIn; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2)
/*     */       {
/* 171 */         NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
/* 172 */         Entity entity1 = EntityList.createEntityByName(nbttagcompound2.getString("id"), entityIn.worldObj);
/*     */         
/* 174 */         if (entity1 != null) {
/*     */           
/* 176 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 177 */           entity1.writeToNBTOptional(nbttagcompound1);
/*     */           
/* 179 */           for (String s1 : nbttagcompound2.getKeySet()) {
/*     */             
/* 181 */             NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
/* 182 */             nbttagcompound1.setTag(s1, nbtbase1.copy());
/*     */           } 
/*     */           
/* 185 */           entity1.readFromNBT(nbttagcompound1);
/* 186 */           entity1.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
/*     */           
/* 188 */           if (entityIn.worldObj != null && spawn)
/*     */           {
/* 190 */             entityIn.worldObj.spawnEntityInWorld(entity1);
/*     */           }
/*     */           
/* 193 */           entity.mountEntity(entity1);
/*     */         } 
/*     */         
/* 196 */         entity = entity1;
/*     */       }
/*     */     
/* 199 */     } else if (entityIn instanceof net.minecraft.entity.EntityLivingBase && entityIn.worldObj != null && spawn) {
/*     */       
/* 201 */       if (entityIn instanceof EntityLiving)
/*     */       {
/* 203 */         ((EntityLiving)entityIn).onInitialSpawn(entityIn.worldObj.getDifficultyForLocation(new BlockPos(entityIn)), (IEntityLivingData)null);
/*     */       }
/*     */       
/* 206 */       entityIn.worldObj.spawnEntityInWorld(entityIn);
/*     */     } 
/*     */     
/* 209 */     return entityIn;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetTimer() {
/* 214 */     if (this.maxSpawnDelay <= this.minSpawnDelay) {
/*     */       
/* 216 */       this.spawnDelay = this.minSpawnDelay;
/*     */     }
/*     */     else {
/*     */       
/* 220 */       int i = this.maxSpawnDelay - this.minSpawnDelay;
/* 221 */       this.spawnDelay = this.minSpawnDelay + (getSpawnerWorld()).rand.nextInt(i);
/*     */     } 
/*     */     
/* 224 */     if (this.minecartToSpawn.size() > 0)
/*     */     {
/* 226 */       setRandomEntity((WeightedRandomMinecart)WeightedRandom.getRandomItem((getSpawnerWorld()).rand, this.minecartToSpawn));
/*     */     }
/*     */     
/* 229 */     func_98267_a(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt) {
/* 234 */     this.mobID = nbt.getString("EntityId");
/* 235 */     this.spawnDelay = nbt.getShort("Delay");
/* 236 */     this.minecartToSpawn.clear();
/*     */     
/* 238 */     if (nbt.hasKey("SpawnPotentials", 9)) {
/*     */       
/* 240 */       NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
/*     */       
/* 242 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/* 244 */         this.minecartToSpawn.add(new WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i)));
/*     */       }
/*     */     } 
/*     */     
/* 248 */     if (nbt.hasKey("SpawnData", 10)) {
/*     */       
/* 250 */       setRandomEntity(new WeightedRandomMinecart(nbt.getCompoundTag("SpawnData"), this.mobID));
/*     */     }
/*     */     else {
/*     */       
/* 254 */       setRandomEntity((WeightedRandomMinecart)null);
/*     */     } 
/*     */     
/* 257 */     if (nbt.hasKey("MinSpawnDelay", 99)) {
/*     */       
/* 259 */       this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
/* 260 */       this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
/* 261 */       this.spawnCount = nbt.getShort("SpawnCount");
/*     */     } 
/*     */     
/* 264 */     if (nbt.hasKey("MaxNearbyEntities", 99)) {
/*     */       
/* 266 */       this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
/* 267 */       this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
/*     */     } 
/*     */     
/* 270 */     if (nbt.hasKey("SpawnRange", 99))
/*     */     {
/* 272 */       this.spawnRange = nbt.getShort("SpawnRange");
/*     */     }
/*     */     
/* 275 */     if (getSpawnerWorld() != null)
/*     */     {
/* 277 */       this.cachedEntity = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound nbt) {
/* 283 */     String s = getEntityNameToSpawn();
/*     */     
/* 285 */     if (!StringUtils.isNullOrEmpty(s)) {
/*     */       
/* 287 */       nbt.setString("EntityId", s);
/* 288 */       nbt.setShort("Delay", (short)this.spawnDelay);
/* 289 */       nbt.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
/* 290 */       nbt.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
/* 291 */       nbt.setShort("SpawnCount", (short)this.spawnCount);
/* 292 */       nbt.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
/* 293 */       nbt.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
/* 294 */       nbt.setShort("SpawnRange", (short)this.spawnRange);
/*     */       
/* 296 */       if (getRandomEntity() != null)
/*     */       {
/* 298 */         nbt.setTag("SpawnData", (getRandomEntity()).nbtData.copy());
/*     */       }
/*     */       
/* 301 */       if (getRandomEntity() != null || this.minecartToSpawn.size() > 0) {
/*     */         
/* 303 */         NBTTagList nbttaglist = new NBTTagList();
/*     */         
/* 305 */         if (this.minecartToSpawn.size() > 0) {
/*     */           
/* 307 */           for (WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart : this.minecartToSpawn)
/*     */           {
/* 309 */             nbttaglist.appendTag((NBTBase)mobspawnerbaselogic$weightedrandomminecart.toNBT());
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 314 */           nbttaglist.appendTag((NBTBase)getRandomEntity().toNBT());
/*     */         } 
/*     */         
/* 317 */         nbt.setTag("SpawnPotentials", (NBTBase)nbttaglist);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity func_180612_a(World worldIn) {
/* 324 */     if (this.cachedEntity == null) {
/*     */       
/* 326 */       Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), worldIn);
/*     */       
/* 328 */       if (entity != null) {
/*     */         
/* 330 */         entity = spawnNewEntity(entity, false);
/* 331 */         this.cachedEntity = entity;
/*     */       } 
/*     */     } 
/*     */     
/* 335 */     return this.cachedEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setDelayToMin(int delay) {
/* 340 */     if (delay == 1 && (getSpawnerWorld()).isRemote) {
/*     */       
/* 342 */       this.spawnDelay = this.minSpawnDelay;
/* 343 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 347 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private WeightedRandomMinecart getRandomEntity() {
/* 353 */     return this.randomEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRandomEntity(WeightedRandomMinecart p_98277_1_) {
/* 358 */     this.randomEntity = p_98277_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void func_98267_a(int paramInt);
/*     */   
/*     */   public abstract World getSpawnerWorld();
/*     */   
/*     */   public abstract BlockPos getSpawnerPosition();
/*     */   
/*     */   public double getMobRotation() {
/* 369 */     return this.mobRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPrevMobRotation() {
/* 374 */     return this.prevMobRotation;
/*     */   }
/*     */   
/*     */   public class WeightedRandomMinecart
/*     */     extends WeightedRandom.Item
/*     */   {
/*     */     private final NBTTagCompound nbtData;
/*     */     private final String entityType;
/*     */     
/*     */     public WeightedRandomMinecart(NBTTagCompound tagCompound) {
/* 384 */       this(tagCompound.getCompoundTag("Properties"), tagCompound.getString("Type"), tagCompound.getInteger("Weight"));
/*     */     }
/*     */ 
/*     */     
/*     */     public WeightedRandomMinecart(NBTTagCompound tagCompound, String type) {
/* 389 */       this(tagCompound, type, 1);
/*     */     }
/*     */ 
/*     */     
/*     */     private WeightedRandomMinecart(NBTTagCompound tagCompound, String type, int weight) {
/* 394 */       super(weight);
/*     */       
/* 396 */       if (type.equals("Minecart"))
/*     */       {
/* 398 */         if (tagCompound != null) {
/*     */           
/* 400 */           type = EntityMinecart.EnumMinecartType.byNetworkID(tagCompound.getInteger("Type")).getName();
/*     */         }
/*     */         else {
/*     */           
/* 404 */           type = "MinecartRideable";
/*     */         } 
/*     */       }
/*     */       
/* 408 */       this.nbtData = tagCompound;
/* 409 */       this.entityType = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public NBTTagCompound toNBT() {
/* 414 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 415 */       nbttagcompound.setTag("Properties", (NBTBase)this.nbtData);
/* 416 */       nbttagcompound.setString("Type", this.entityType);
/* 417 */       nbttagcompound.setInteger("Weight", this.itemWeight);
/* 418 */       return nbttagcompound;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\MobSpawnerBaseLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */