/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.village.Village;
/*     */ import net.minecraft.village.VillageDoorInfo;
/*     */ 
/*     */ public class EntityAIMoveThroughVillage extends EntityAIBase {
/*     */   private EntityCreature theEntity;
/*     */   private double movementSpeed;
/*     */   private PathEntity entityPathNavigate;
/*     */   private VillageDoorInfo doorInfo;
/*     */   private boolean isNocturnal;
/*  21 */   private List<VillageDoorInfo> doorList = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   public EntityAIMoveThroughVillage(EntityCreature theEntityIn, double movementSpeedIn, boolean isNocturnalIn) {
/*  25 */     this.theEntity = theEntityIn;
/*  26 */     this.movementSpeed = movementSpeedIn;
/*  27 */     this.isNocturnal = isNocturnalIn;
/*  28 */     setMutexBits(1);
/*     */     
/*  30 */     if (!(theEntityIn.getNavigator() instanceof PathNavigateGround))
/*     */     {
/*  32 */       throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  38 */     resizeDoorList();
/*     */     
/*  40 */     if (this.isNocturnal && this.theEntity.worldObj.isDaytime())
/*     */     {
/*  42 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  46 */     Village village = this.theEntity.worldObj.getVillageCollection().getNearestVillage(new BlockPos((Entity)this.theEntity), 0);
/*     */     
/*  48 */     if (village == null)
/*     */     {
/*  50 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  54 */     this.doorInfo = findNearestDoor(village);
/*     */     
/*  56 */     if (this.doorInfo == null)
/*     */     {
/*  58 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  62 */     PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
/*  63 */     boolean flag = pathnavigateground.getEnterDoors();
/*  64 */     pathnavigateground.setBreakDoors(false);
/*  65 */     this.entityPathNavigate = pathnavigateground.getPathToPos(this.doorInfo.getDoorBlockPos());
/*  66 */     pathnavigateground.setBreakDoors(flag);
/*     */     
/*  68 */     if (this.entityPathNavigate != null)
/*     */     {
/*  70 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  74 */     Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 10, 7, new Vec3(this.doorInfo.getDoorBlockPos().getX(), this.doorInfo.getDoorBlockPos().getY(), this.doorInfo.getDoorBlockPos().getZ()));
/*     */     
/*  76 */     if (vec3 == null)
/*     */     {
/*  78 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  82 */     pathnavigateground.setBreakDoors(false);
/*  83 */     this.entityPathNavigate = this.theEntity.getNavigator().getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
/*  84 */     pathnavigateground.setBreakDoors(flag);
/*  85 */     return (this.entityPathNavigate != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  95 */     if (this.theEntity.getNavigator().noPath())
/*     */     {
/*  97 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 101 */     float f = this.theEntity.width + 4.0F;
/* 102 */     return (this.theEntity.getDistanceSq(this.doorInfo.getDoorBlockPos()) > (f * f));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 108 */     this.theEntity.getNavigator().setPath(this.entityPathNavigate, this.movementSpeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 113 */     if (this.theEntity.getNavigator().noPath() || this.theEntity.getDistanceSq(this.doorInfo.getDoorBlockPos()) < 16.0D)
/*     */     {
/* 115 */       this.doorList.add(this.doorInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private VillageDoorInfo findNearestDoor(Village villageIn) {
/* 121 */     VillageDoorInfo villagedoorinfo = null;
/* 122 */     int i = Integer.MAX_VALUE;
/*     */     
/* 124 */     for (VillageDoorInfo villagedoorinfo1 : villageIn.getVillageDoorInfoList()) {
/*     */       
/* 126 */       int j = villagedoorinfo1.getDistanceSquared(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ));
/*     */       
/* 128 */       if (j < i && !doesDoorListContain(villagedoorinfo1)) {
/*     */         
/* 130 */         villagedoorinfo = villagedoorinfo1;
/* 131 */         i = j;
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     return villagedoorinfo;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean doesDoorListContain(VillageDoorInfo doorInfoIn) {
/* 140 */     for (VillageDoorInfo villagedoorinfo : this.doorList) {
/*     */       
/* 142 */       if (doorInfoIn.getDoorBlockPos().equals(villagedoorinfo.getDoorBlockPos()))
/*     */       {
/* 144 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizeDoorList() {
/* 153 */     if (this.doorList.size() > 15)
/*     */     {
/* 155 */       this.doorList.remove(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIMoveThroughVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */