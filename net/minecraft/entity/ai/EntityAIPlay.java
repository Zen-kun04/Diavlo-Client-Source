/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ public class EntityAIPlay extends EntityAIBase {
/*     */   private EntityVillager villagerObj;
/*     */   private EntityLivingBase targetVillager;
/*     */   private double speed;
/*     */   private int playTime;
/*     */   
/*     */   public EntityAIPlay(EntityVillager villagerObjIn, double speedIn) {
/*  17 */     this.villagerObj = villagerObjIn;
/*  18 */     this.speed = speedIn;
/*  19 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  24 */     if (this.villagerObj.getGrowingAge() >= 0)
/*     */     {
/*  26 */       return false;
/*     */     }
/*  28 */     if (this.villagerObj.getRNG().nextInt(400) != 0)
/*     */     {
/*  30 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  34 */     List<EntityVillager> list = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
/*  35 */     double d0 = Double.MAX_VALUE;
/*     */     
/*  37 */     for (EntityVillager entityvillager : list) {
/*     */       
/*  39 */       if (entityvillager != this.villagerObj && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0) {
/*     */         
/*  41 */         double d1 = entityvillager.getDistanceSqToEntity((Entity)this.villagerObj);
/*     */         
/*  43 */         if (d1 <= d0) {
/*     */           
/*  45 */           d0 = d1;
/*  46 */           this.targetVillager = (EntityLivingBase)entityvillager;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     if (this.targetVillager == null) {
/*     */       
/*  53 */       Vec3 vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature)this.villagerObj, 16, 3);
/*     */       
/*  55 */       if (vec3 == null)
/*     */       {
/*  57 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  67 */     return (this.playTime > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  72 */     if (this.targetVillager != null)
/*     */     {
/*  74 */       this.villagerObj.setPlaying(true);
/*     */     }
/*     */     
/*  77 */     this.playTime = 1000;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  82 */     this.villagerObj.setPlaying(false);
/*  83 */     this.targetVillager = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  88 */     this.playTime--;
/*     */     
/*  90 */     if (this.targetVillager != null) {
/*     */       
/*  92 */       if (this.villagerObj.getDistanceSqToEntity((Entity)this.targetVillager) > 4.0D)
/*     */       {
/*  94 */         this.villagerObj.getNavigator().tryMoveToEntityLiving((Entity)this.targetVillager, this.speed);
/*     */       }
/*     */     }
/*  97 */     else if (this.villagerObj.getNavigator().noPath()) {
/*     */       
/*  99 */       Vec3 vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature)this.villagerObj, 16, 3);
/*     */       
/* 101 */       if (vec3 == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 106 */       this.villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIPlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */