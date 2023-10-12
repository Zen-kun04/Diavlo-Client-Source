/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.village.Village;
/*    */ import net.minecraft.village.VillageDoorInfo;
/*    */ 
/*    */ public class EntityAIMoveIndoors extends EntityAIBase {
/*    */   private EntityCreature entityObj;
/*    */   private VillageDoorInfo doorInfo;
/* 13 */   private int insidePosX = -1;
/* 14 */   private int insidePosZ = -1;
/*    */ 
/*    */   
/*    */   public EntityAIMoveIndoors(EntityCreature entityObjIn) {
/* 18 */     this.entityObj = entityObjIn;
/* 19 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 24 */     BlockPos blockpos = new BlockPos((Entity)this.entityObj);
/*    */     
/* 26 */     if ((!this.entityObj.worldObj.isDaytime() || (this.entityObj.worldObj.isRaining() && !this.entityObj.worldObj.getBiomeGenForCoords(blockpos).canRain())) && !this.entityObj.worldObj.provider.getHasNoSky()) {
/*    */       
/* 28 */       if (this.entityObj.getRNG().nextInt(50) != 0)
/*    */       {
/* 30 */         return false;
/*    */       }
/* 32 */       if (this.insidePosX != -1 && this.entityObj.getDistanceSq(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0D)
/*    */       {
/* 34 */         return false;
/*    */       }
/*    */ 
/*    */       
/* 38 */       Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 14);
/*    */       
/* 40 */       if (village == null)
/*    */       {
/* 42 */         return false;
/*    */       }
/*    */ 
/*    */       
/* 46 */       this.doorInfo = village.getDoorInfo(blockpos);
/* 47 */       return (this.doorInfo != null);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 59 */     return !this.entityObj.getNavigator().noPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 64 */     this.insidePosX = -1;
/* 65 */     BlockPos blockpos = this.doorInfo.getInsideBlockPos();
/* 66 */     int i = blockpos.getX();
/* 67 */     int j = blockpos.getY();
/* 68 */     int k = blockpos.getZ();
/*    */     
/* 70 */     if (this.entityObj.getDistanceSq(blockpos) > 256.0D) {
/*    */       
/* 72 */       Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, new Vec3(i + 0.5D, j, k + 0.5D));
/*    */       
/* 74 */       if (vec3 != null)
/*    */       {
/* 76 */         this.entityObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, 1.0D);
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 81 */       this.entityObj.getNavigator().tryMoveToXYZ(i + 0.5D, j, k + 0.5D, 1.0D);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 87 */     this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
/* 88 */     this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
/* 89 */     this.doorInfo = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIMoveIndoors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */