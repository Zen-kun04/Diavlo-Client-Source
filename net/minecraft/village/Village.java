/*     */ package net.minecraft.village;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TreeMap;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.PlayerProfileCache;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class Village {
/*  29 */   private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList(); private World worldObj;
/*  30 */   private BlockPos centerHelper = BlockPos.ORIGIN;
/*  31 */   private BlockPos center = BlockPos.ORIGIN;
/*     */   private int villageRadius;
/*     */   private int lastAddDoorTimestamp;
/*     */   private int tickCounter;
/*     */   private int numVillagers;
/*     */   private int noBreedTicks;
/*  37 */   private TreeMap<String, Integer> playerReputation = new TreeMap<>();
/*  38 */   private List<VillageAggressor> villageAgressors = Lists.newArrayList();
/*     */   
/*     */   private int numIronGolems;
/*     */ 
/*     */   
/*     */   public Village() {}
/*     */ 
/*     */   
/*     */   public Village(World worldIn) {
/*  47 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorld(World worldIn) {
/*  52 */     this.worldObj = worldIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(int p_75560_1_) {
/*  57 */     this.tickCounter = p_75560_1_;
/*  58 */     removeDeadAndOutOfRangeDoors();
/*  59 */     removeDeadAndOldAgressors();
/*     */     
/*  61 */     if (p_75560_1_ % 20 == 0)
/*     */     {
/*  63 */       updateNumVillagers();
/*     */     }
/*     */     
/*  66 */     if (p_75560_1_ % 30 == 0)
/*     */     {
/*  68 */       updateNumIronGolems();
/*     */     }
/*     */     
/*  71 */     int i = this.numVillagers / 10;
/*     */     
/*  73 */     if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0) {
/*     */       
/*  75 */       Vec3 vec3 = func_179862_a(this.center, 2, 4, 2);
/*     */       
/*  77 */       if (vec3 != null) {
/*     */         
/*  79 */         EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
/*  80 */         entityirongolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
/*  81 */         this.worldObj.spawnEntityInWorld((Entity)entityirongolem);
/*  82 */         this.numIronGolems++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3 func_179862_a(BlockPos p_179862_1_, int p_179862_2_, int p_179862_3_, int p_179862_4_) {
/*  89 */     for (int i = 0; i < 10; i++) {
/*     */       
/*  91 */       BlockPos blockpos = p_179862_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
/*     */       
/*  93 */       if (func_179866_a(blockpos) && func_179861_a(new BlockPos(p_179862_2_, p_179862_3_, p_179862_4_), blockpos))
/*     */       {
/*  95 */         return new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ());
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean func_179861_a(BlockPos p_179861_1_, BlockPos p_179861_2_) {
/* 104 */     if (!World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, p_179861_2_.down()))
/*     */     {
/* 106 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 110 */     int i = p_179861_2_.getX() - p_179861_1_.getX() / 2;
/* 111 */     int j = p_179861_2_.getZ() - p_179861_1_.getZ() / 2;
/*     */     
/* 113 */     for (int k = i; k < i + p_179861_1_.getX(); k++) {
/*     */       
/* 115 */       for (int l = p_179861_2_.getY(); l < p_179861_2_.getY() + p_179861_1_.getY(); l++) {
/*     */         
/* 117 */         for (int i1 = j; i1 < j + p_179861_1_.getZ(); i1++) {
/*     */           
/* 119 */           if (this.worldObj.getBlockState(new BlockPos(k, l, i1)).getBlock().isNormalCube())
/*     */           {
/* 121 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateNumIronGolems() {
/* 133 */     List<EntityIronGolem> list = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB((this.center.getX() - this.villageRadius), (this.center.getY() - 4), (this.center.getZ() - this.villageRadius), (this.center.getX() + this.villageRadius), (this.center.getY() + 4), (this.center.getZ() + this.villageRadius)));
/* 134 */     this.numIronGolems = list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNumVillagers() {
/* 139 */     List<EntityVillager> list = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB((this.center.getX() - this.villageRadius), (this.center.getY() - 4), (this.center.getZ() - this.villageRadius), (this.center.getX() + this.villageRadius), (this.center.getY() + 4), (this.center.getZ() + this.villageRadius)));
/* 140 */     this.numVillagers = list.size();
/*     */     
/* 142 */     if (this.numVillagers == 0)
/*     */     {
/* 144 */       this.playerReputation.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getCenter() {
/* 150 */     return this.center;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVillageRadius() {
/* 155 */     return this.villageRadius;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumVillageDoors() {
/* 160 */     return this.villageDoorInfoList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTicksSinceLastDoorAdding() {
/* 165 */     return this.tickCounter - this.lastAddDoorTimestamp;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumVillagers() {
/* 170 */     return this.numVillagers;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_179866_a(BlockPos pos) {
/* 175 */     return (this.center.distanceSq((Vec3i)pos) < (this.villageRadius * this.villageRadius));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<VillageDoorInfo> getVillageDoorInfoList() {
/* 180 */     return this.villageDoorInfoList;
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageDoorInfo getNearestDoor(BlockPos pos) {
/* 185 */     VillageDoorInfo villagedoorinfo = null;
/* 186 */     int i = Integer.MAX_VALUE;
/*     */     
/* 188 */     for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList) {
/*     */       
/* 190 */       int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
/*     */       
/* 192 */       if (j < i) {
/*     */         
/* 194 */         villagedoorinfo = villagedoorinfo1;
/* 195 */         i = j;
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     return villagedoorinfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageDoorInfo getDoorInfo(BlockPos pos) {
/* 204 */     VillageDoorInfo villagedoorinfo = null;
/* 205 */     int i = Integer.MAX_VALUE;
/*     */     
/* 207 */     for (VillageDoorInfo villagedoorinfo1 : this.villageDoorInfoList) {
/*     */       
/* 209 */       int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
/*     */       
/* 211 */       if (j > 256) {
/*     */         
/* 213 */         j *= 1000;
/*     */       }
/*     */       else {
/*     */         
/* 217 */         j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
/*     */       } 
/*     */       
/* 220 */       if (j < i) {
/*     */         
/* 222 */         villagedoorinfo = villagedoorinfo1;
/* 223 */         i = j;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     return villagedoorinfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageDoorInfo getExistedDoor(BlockPos doorBlock) {
/* 232 */     if (this.center.distanceSq((Vec3i)doorBlock) > (this.villageRadius * this.villageRadius))
/*     */     {
/* 234 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 238 */     for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
/*     */       
/* 240 */       if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1)
/*     */       {
/* 242 */         return villagedoorinfo;
/*     */       }
/*     */     } 
/*     */     
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVillageDoorInfo(VillageDoorInfo doorInfo) {
/* 252 */     this.villageDoorInfoList.add(doorInfo);
/* 253 */     this.centerHelper = this.centerHelper.add((Vec3i)doorInfo.getDoorBlockPos());
/* 254 */     updateVillageRadiusAndCenter();
/* 255 */     this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnihilated() {
/* 260 */     return this.villageDoorInfoList.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn) {
/* 265 */     for (VillageAggressor village$villageaggressor : this.villageAgressors) {
/*     */       
/* 267 */       if (village$villageaggressor.agressor == entitylivingbaseIn) {
/*     */         
/* 269 */         village$villageaggressor.agressionTime = this.tickCounter;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 274 */     this.villageAgressors.add(new VillageAggressor(entitylivingbaseIn, this.tickCounter));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn) {
/* 279 */     double d0 = Double.MAX_VALUE;
/* 280 */     VillageAggressor village$villageaggressor = null;
/*     */     
/* 282 */     for (int i = 0; i < this.villageAgressors.size(); i++) {
/*     */       
/* 284 */       VillageAggressor village$villageaggressor1 = this.villageAgressors.get(i);
/* 285 */       double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity((Entity)entitylivingbaseIn);
/*     */       
/* 287 */       if (d1 <= d0) {
/*     */         
/* 289 */         village$villageaggressor = village$villageaggressor1;
/* 290 */         d0 = d1;
/*     */       } 
/*     */     } 
/*     */     
/* 294 */     return (village$villageaggressor != null) ? village$villageaggressor.agressor : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender) {
/* 299 */     double d0 = Double.MAX_VALUE;
/* 300 */     EntityPlayer entityplayer = null;
/*     */     
/* 302 */     for (String s : this.playerReputation.keySet()) {
/*     */       
/* 304 */       if (isPlayerReputationTooLow(s)) {
/*     */         
/* 306 */         EntityPlayer entityplayer1 = this.worldObj.getPlayerEntityByName(s);
/*     */         
/* 308 */         if (entityplayer1 != null) {
/*     */           
/* 310 */           double d1 = entityplayer1.getDistanceSqToEntity((Entity)villageDefender);
/*     */           
/* 312 */           if (d1 <= d0) {
/*     */             
/* 314 */             entityplayer = entityplayer1;
/* 315 */             d0 = d1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 321 */     return entityplayer;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeDeadAndOldAgressors() {
/* 326 */     Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
/*     */     
/* 328 */     while (iterator.hasNext()) {
/*     */       
/* 330 */       VillageAggressor village$villageaggressor = iterator.next();
/*     */       
/* 332 */       if (!village$villageaggressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - village$villageaggressor.agressionTime) > 300)
/*     */       {
/* 334 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeDeadAndOutOfRangeDoors() {
/* 341 */     boolean flag = false;
/* 342 */     boolean flag1 = (this.worldObj.rand.nextInt(50) == 0);
/* 343 */     Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
/*     */     
/* 345 */     while (iterator.hasNext()) {
/*     */       
/* 347 */       VillageDoorInfo villagedoorinfo = iterator.next();
/*     */       
/* 349 */       if (flag1)
/*     */       {
/* 351 */         villagedoorinfo.resetDoorOpeningRestrictionCounter();
/*     */       }
/*     */       
/* 354 */       if (!isWoodDoor(villagedoorinfo.getDoorBlockPos()) || Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) > 1200) {
/*     */         
/* 356 */         this.centerHelper = this.centerHelper.subtract((Vec3i)villagedoorinfo.getDoorBlockPos());
/* 357 */         flag = true;
/* 358 */         villagedoorinfo.setIsDetachedFromVillageFlag(true);
/* 359 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 363 */     if (flag)
/*     */     {
/* 365 */       updateVillageRadiusAndCenter();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isWoodDoor(BlockPos pos) {
/* 371 */     Block block = this.worldObj.getBlockState(pos).getBlock();
/* 372 */     return (block instanceof net.minecraft.block.BlockDoor) ? ((block.getMaterial() == Material.wood)) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateVillageRadiusAndCenter() {
/* 377 */     int i = this.villageDoorInfoList.size();
/*     */     
/* 379 */     if (i == 0) {
/*     */       
/* 381 */       this.center = new BlockPos(0, 0, 0);
/* 382 */       this.villageRadius = 0;
/*     */     }
/*     */     else {
/*     */       
/* 386 */       this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
/* 387 */       int j = 0;
/*     */       
/* 389 */       for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList)
/*     */       {
/* 391 */         j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
/*     */       }
/*     */       
/* 394 */       this.villageRadius = Math.max(32, (int)Math.sqrt(j) + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getReputationForPlayer(String p_82684_1_) {
/* 400 */     Integer integer = this.playerReputation.get(p_82684_1_);
/* 401 */     return (integer != null) ? integer.intValue() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setReputationForPlayer(String p_82688_1_, int p_82688_2_) {
/* 406 */     int i = getReputationForPlayer(p_82688_1_);
/* 407 */     int j = MathHelper.clamp_int(i + p_82688_2_, -30, 10);
/* 408 */     this.playerReputation.put(p_82688_1_, Integer.valueOf(j));
/* 409 */     return j;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPlayerReputationTooLow(String p_82687_1_) {
/* 414 */     return (getReputationForPlayer(p_82687_1_) <= -15);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readVillageDataFromNBT(NBTTagCompound compound) {
/* 419 */     this.numVillagers = compound.getInteger("PopSize");
/* 420 */     this.villageRadius = compound.getInteger("Radius");
/* 421 */     this.numIronGolems = compound.getInteger("Golems");
/* 422 */     this.lastAddDoorTimestamp = compound.getInteger("Stable");
/* 423 */     this.tickCounter = compound.getInteger("Tick");
/* 424 */     this.noBreedTicks = compound.getInteger("MTick");
/* 425 */     this.center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
/* 426 */     this.centerHelper = new BlockPos(compound.getInteger("ACX"), compound.getInteger("ACY"), compound.getInteger("ACZ"));
/* 427 */     NBTTagList nbttaglist = compound.getTagList("Doors", 10);
/*     */     
/* 429 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 431 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 432 */       VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
/* 433 */       this.villageDoorInfoList.add(villagedoorinfo);
/*     */     } 
/*     */     
/* 436 */     NBTTagList nbttaglist1 = compound.getTagList("Players", 10);
/*     */     
/* 438 */     for (int j = 0; j < nbttaglist1.tagCount(); j++) {
/*     */       
/* 440 */       NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);
/*     */       
/* 442 */       if (nbttagcompound1.hasKey("UUID")) {
/*     */         
/* 444 */         PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
/* 445 */         GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound1.getString("UUID")));
/*     */         
/* 447 */         if (gameprofile != null)
/*     */         {
/* 449 */           this.playerReputation.put(gameprofile.getName(), Integer.valueOf(nbttagcompound1.getInteger("S")));
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 454 */         this.playerReputation.put(nbttagcompound1.getString("Name"), Integer.valueOf(nbttagcompound1.getInteger("S")));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeVillageDataToNBT(NBTTagCompound compound) {
/* 461 */     compound.setInteger("PopSize", this.numVillagers);
/* 462 */     compound.setInteger("Radius", this.villageRadius);
/* 463 */     compound.setInteger("Golems", this.numIronGolems);
/* 464 */     compound.setInteger("Stable", this.lastAddDoorTimestamp);
/* 465 */     compound.setInteger("Tick", this.tickCounter);
/* 466 */     compound.setInteger("MTick", this.noBreedTicks);
/* 467 */     compound.setInteger("CX", this.center.getX());
/* 468 */     compound.setInteger("CY", this.center.getY());
/* 469 */     compound.setInteger("CZ", this.center.getZ());
/* 470 */     compound.setInteger("ACX", this.centerHelper.getX());
/* 471 */     compound.setInteger("ACY", this.centerHelper.getY());
/* 472 */     compound.setInteger("ACZ", this.centerHelper.getZ());
/* 473 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 475 */     for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
/*     */       
/* 477 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 478 */       nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
/* 479 */       nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
/* 480 */       nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
/* 481 */       nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
/* 482 */       nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
/* 483 */       nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
/* 484 */       nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 487 */     compound.setTag("Doors", (NBTBase)nbttaglist);
/* 488 */     NBTTagList nbttaglist1 = new NBTTagList();
/*     */     
/* 490 */     for (String s : this.playerReputation.keySet()) {
/*     */       
/* 492 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 493 */       PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
/* 494 */       GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);
/*     */       
/* 496 */       if (gameprofile != null) {
/*     */         
/* 498 */         nbttagcompound1.setString("UUID", gameprofile.getId().toString());
/* 499 */         nbttagcompound1.setInteger("S", ((Integer)this.playerReputation.get(s)).intValue());
/* 500 */         nbttaglist1.appendTag((NBTBase)nbttagcompound1);
/*     */       } 
/*     */     } 
/*     */     
/* 504 */     compound.setTag("Players", (NBTBase)nbttaglist1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endMatingSeason() {
/* 509 */     this.noBreedTicks = this.tickCounter;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMatingSeason() {
/* 514 */     return (this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultPlayerReputation(int p_82683_1_) {
/* 519 */     for (String s : this.playerReputation.keySet())
/*     */     {
/* 521 */       setReputationForPlayer(s, p_82683_1_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   class VillageAggressor
/*     */   {
/*     */     public EntityLivingBase agressor;
/*     */     public int agressionTime;
/*     */     
/*     */     VillageAggressor(EntityLivingBase p_i1674_2_, int p_i1674_3_) {
/* 532 */       this.agressor = p_i1674_2_;
/* 533 */       this.agressionTime = p_i1674_3_;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\village\Village.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */