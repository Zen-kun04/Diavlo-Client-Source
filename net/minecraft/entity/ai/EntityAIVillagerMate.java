/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.village.Village;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIVillagerMate
/*     */   extends EntityAIBase {
/*     */   private EntityVillager villagerObj;
/*     */   private EntityVillager mate;
/*     */   private World worldObj;
/*     */   private int matingTimeout;
/*     */   Village villageObj;
/*     */   
/*     */   public EntityAIVillagerMate(EntityVillager villagerIn) {
/*  19 */     this.villagerObj = villagerIn;
/*  20 */     this.worldObj = villagerIn.worldObj;
/*  21 */     setMutexBits(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  26 */     if (this.villagerObj.getGrowingAge() != 0)
/*     */     {
/*  28 */       return false;
/*     */     }
/*  30 */     if (this.villagerObj.getRNG().nextInt(500) != 0)
/*     */     {
/*  32 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  36 */     this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos((Entity)this.villagerObj), 0);
/*     */     
/*  38 */     if (this.villageObj == null)
/*     */     {
/*  40 */       return false;
/*     */     }
/*  42 */     if (checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getIsWillingToMate(true)) {
/*     */       
/*  44 */       Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), (Entity)this.villagerObj);
/*     */       
/*  46 */       if (entity == null)
/*     */       {
/*  48 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  52 */       this.mate = (EntityVillager)entity;
/*  53 */       return (this.mate.getGrowingAge() == 0 && this.mate.getIsWillingToMate(true));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  65 */     this.matingTimeout = 300;
/*  66 */     this.villagerObj.setMating(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  71 */     this.villageObj = null;
/*  72 */     this.mate = null;
/*  73 */     this.villagerObj.setMating(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  78 */     return (this.matingTimeout >= 0 && checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getGrowingAge() == 0 && this.villagerObj.getIsWillingToMate(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  83 */     this.matingTimeout--;
/*  84 */     this.villagerObj.getLookHelper().setLookPositionWithEntity((Entity)this.mate, 10.0F, 30.0F);
/*     */     
/*  86 */     if (this.villagerObj.getDistanceSqToEntity((Entity)this.mate) > 2.25D) {
/*     */       
/*  88 */       this.villagerObj.getNavigator().tryMoveToEntityLiving((Entity)this.mate, 0.25D);
/*     */     }
/*  90 */     else if (this.matingTimeout == 0 && this.mate.isMating()) {
/*     */       
/*  92 */       giveBirth();
/*     */     } 
/*     */     
/*  95 */     if (this.villagerObj.getRNG().nextInt(35) == 0)
/*     */     {
/*  97 */       this.worldObj.setEntityState((Entity)this.villagerObj, (byte)12);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkSufficientDoorsPresentForNewVillager() {
/* 103 */     if (!this.villageObj.isMatingSeason())
/*     */     {
/* 105 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 109 */     int i = (int)(this.villageObj.getNumVillageDoors() * 0.35D);
/* 110 */     return (this.villageObj.getNumVillagers() < i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void giveBirth() {
/* 116 */     EntityVillager entityvillager = this.villagerObj.createChild((EntityAgeable)this.mate);
/* 117 */     this.mate.setGrowingAge(6000);
/* 118 */     this.villagerObj.setGrowingAge(6000);
/* 119 */     this.mate.setIsWillingToMate(false);
/* 120 */     this.villagerObj.setIsWillingToMate(false);
/* 121 */     entityvillager.setGrowingAge(-24000);
/* 122 */     entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
/* 123 */     this.worldObj.spawnEntityInWorld((Entity)entityvillager);
/* 124 */     this.worldObj.setEntityState((Entity)entityvillager, (byte)12);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIVillagerMate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */