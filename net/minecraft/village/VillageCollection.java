/*     */ package net.minecraft.village;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ 
/*     */ public class VillageCollection extends WorldSavedData {
/*  20 */   private final List<BlockPos> villagerPositionsList = Lists.newArrayList(); private World worldObj;
/*  21 */   private final List<VillageDoorInfo> newDoors = Lists.newArrayList();
/*  22 */   private final List<Village> villageList = Lists.newArrayList();
/*     */   
/*     */   private int tickCounter;
/*     */   
/*     */   public VillageCollection(String name) {
/*  27 */     super(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public VillageCollection(World worldIn) {
/*  32 */     super(fileNameForProvider(worldIn.provider));
/*  33 */     this.worldObj = worldIn;
/*  34 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWorldsForAll(World worldIn) {
/*  39 */     this.worldObj = worldIn;
/*     */     
/*  41 */     for (Village village : this.villageList)
/*     */     {
/*  43 */       village.setWorld(worldIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToVillagerPositionList(BlockPos pos) {
/*  49 */     if (this.villagerPositionsList.size() <= 64)
/*     */     {
/*  51 */       if (!positionInList(pos))
/*     */       {
/*  53 */         this.villagerPositionsList.add(pos);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  60 */     this.tickCounter++;
/*     */     
/*  62 */     for (Village village : this.villageList)
/*     */     {
/*  64 */       village.tick(this.tickCounter);
/*     */     }
/*     */     
/*  67 */     removeAnnihilatedVillages();
/*  68 */     dropOldestVillagerPosition();
/*  69 */     addNewDoorsToVillageOrCreateVillage();
/*     */     
/*  71 */     if (this.tickCounter % 400 == 0)
/*     */     {
/*  73 */       markDirty();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAnnihilatedVillages() {
/*  79 */     Iterator<Village> iterator = this.villageList.iterator();
/*     */     
/*  81 */     while (iterator.hasNext()) {
/*     */       
/*  83 */       Village village = iterator.next();
/*     */       
/*  85 */       if (village.isAnnihilated()) {
/*     */         
/*  87 */         iterator.remove();
/*  88 */         markDirty();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Village> getVillageList() {
/*  95 */     return this.villageList;
/*     */   }
/*     */ 
/*     */   
/*     */   public Village getNearestVillage(BlockPos doorBlock, int radius) {
/* 100 */     Village village = null;
/* 101 */     double d0 = 3.4028234663852886E38D;
/*     */     
/* 103 */     for (Village village1 : this.villageList) {
/*     */       
/* 105 */       double d1 = village1.getCenter().distanceSq((Vec3i)doorBlock);
/*     */       
/* 107 */       if (d1 < d0) {
/*     */         
/* 109 */         float f = (radius + village1.getVillageRadius());
/*     */         
/* 111 */         if (d1 <= (f * f)) {
/*     */           
/* 113 */           village = village1;
/* 114 */           d0 = d1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     return village;
/*     */   }
/*     */ 
/*     */   
/*     */   private void dropOldestVillagerPosition() {
/* 124 */     if (!this.villagerPositionsList.isEmpty())
/*     */     {
/* 126 */       addDoorsAround(this.villagerPositionsList.remove(0));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void addNewDoorsToVillageOrCreateVillage() {
/* 132 */     for (int i = 0; i < this.newDoors.size(); i++) {
/*     */       
/* 134 */       VillageDoorInfo villagedoorinfo = this.newDoors.get(i);
/* 135 */       Village village = getNearestVillage(villagedoorinfo.getDoorBlockPos(), 32);
/*     */       
/* 137 */       if (village == null) {
/*     */         
/* 139 */         village = new Village(this.worldObj);
/* 140 */         this.villageList.add(village);
/* 141 */         markDirty();
/*     */       } 
/*     */       
/* 144 */       village.addVillageDoorInfo(villagedoorinfo);
/*     */     } 
/*     */     
/* 147 */     this.newDoors.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private void addDoorsAround(BlockPos central) {
/* 152 */     int i = 16;
/* 153 */     int j = 4;
/* 154 */     int k = 16;
/*     */     
/* 156 */     for (int l = -i; l < i; l++) {
/*     */       
/* 158 */       for (int i1 = -j; i1 < j; i1++) {
/*     */         
/* 160 */         for (int j1 = -k; j1 < k; j1++) {
/*     */           
/* 162 */           BlockPos blockpos = central.add(l, i1, j1);
/*     */           
/* 164 */           if (isWoodDoor(blockpos)) {
/*     */             
/* 166 */             VillageDoorInfo villagedoorinfo = checkDoorExistence(blockpos);
/*     */             
/* 168 */             if (villagedoorinfo == null) {
/*     */               
/* 170 */               addToNewDoorsList(blockpos);
/*     */             }
/*     */             else {
/*     */               
/* 174 */               villagedoorinfo.func_179849_a(this.tickCounter);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private VillageDoorInfo checkDoorExistence(BlockPos doorBlock) {
/* 184 */     for (VillageDoorInfo villagedoorinfo : this.newDoors) {
/*     */       
/* 186 */       if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1)
/*     */       {
/* 188 */         return villagedoorinfo;
/*     */       }
/*     */     } 
/*     */     
/* 192 */     for (Village village : this.villageList) {
/*     */       
/* 194 */       VillageDoorInfo villagedoorinfo1 = village.getExistedDoor(doorBlock);
/*     */       
/* 196 */       if (villagedoorinfo1 != null)
/*     */       {
/* 198 */         return villagedoorinfo1;
/*     */       }
/*     */     } 
/*     */     
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addToNewDoorsList(BlockPos doorBlock) {
/* 207 */     EnumFacing enumfacing = BlockDoor.getFacing((IBlockAccess)this.worldObj, doorBlock);
/* 208 */     EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 209 */     int i = countBlocksCanSeeSky(doorBlock, enumfacing, 5);
/* 210 */     int j = countBlocksCanSeeSky(doorBlock, enumfacing1, i + 1);
/*     */     
/* 212 */     if (i != j)
/*     */     {
/* 214 */       this.newDoors.add(new VillageDoorInfo(doorBlock, (i < j) ? enumfacing : enumfacing1, this.tickCounter));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation) {
/* 220 */     int i = 0;
/*     */     
/* 222 */     for (int j = 1; j <= 5; j++) {
/*     */       
/* 224 */       if (this.worldObj.canSeeSky(centerPos.offset(direction, j))) {
/*     */         
/* 226 */         i++;
/*     */         
/* 228 */         if (i >= limitation)
/*     */         {
/* 230 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 235 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean positionInList(BlockPos pos) {
/* 240 */     for (BlockPos blockpos : this.villagerPositionsList) {
/*     */       
/* 242 */       if (blockpos.equals(pos))
/*     */       {
/* 244 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isWoodDoor(BlockPos doorPos) {
/* 253 */     Block block = this.worldObj.getBlockState(doorPos).getBlock();
/* 254 */     return (block instanceof BlockDoor) ? ((block.getMaterial() == Material.wood)) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt) {
/* 259 */     this.tickCounter = nbt.getInteger("Tick");
/* 260 */     NBTTagList nbttaglist = nbt.getTagList("Villages", 10);
/*     */     
/* 262 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 264 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 265 */       Village village = new Village();
/* 266 */       village.readVillageDataFromNBT(nbttagcompound);
/* 267 */       this.villageList.add(village);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound nbt) {
/* 273 */     nbt.setInteger("Tick", this.tickCounter);
/* 274 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 276 */     for (Village village : this.villageList) {
/*     */       
/* 278 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 279 */       village.writeVillageDataToNBT(nbttagcompound);
/* 280 */       nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 283 */     nbt.setTag("Villages", (NBTBase)nbttaglist);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String fileNameForProvider(WorldProvider provider) {
/* 288 */     return "villages" + provider.getInternalNameSuffix();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\village\VillageCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */