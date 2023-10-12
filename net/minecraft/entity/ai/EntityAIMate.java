/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIMate
/*     */   extends EntityAIBase {
/*     */   private EntityAnimal theAnimal;
/*     */   World theWorld;
/*     */   private EntityAnimal targetMate;
/*     */   int spawnBabyDelay;
/*     */   double moveSpeed;
/*     */   
/*     */   public EntityAIMate(EntityAnimal animal, double speedIn) {
/*  25 */     this.theAnimal = animal;
/*  26 */     this.theWorld = animal.worldObj;
/*  27 */     this.moveSpeed = speedIn;
/*  28 */     setMutexBits(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  33 */     if (!this.theAnimal.isInLove())
/*     */     {
/*  35 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  39 */     this.targetMate = getNearbyMate();
/*  40 */     return (this.targetMate != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  46 */     return (this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  51 */     this.targetMate = null;
/*  52 */     this.spawnBabyDelay = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  57 */     this.theAnimal.getLookHelper().setLookPositionWithEntity((Entity)this.targetMate, 10.0F, this.theAnimal.getVerticalFaceSpeed());
/*  58 */     this.theAnimal.getNavigator().tryMoveToEntityLiving((Entity)this.targetMate, this.moveSpeed);
/*  59 */     this.spawnBabyDelay++;
/*     */     
/*  61 */     if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity((Entity)this.targetMate) < 9.0D)
/*     */     {
/*  63 */       spawnBaby();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private EntityAnimal getNearbyMate() {
/*  69 */     float f = 8.0F;
/*  70 */     List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand(f, f, f));
/*  71 */     double d0 = Double.MAX_VALUE;
/*  72 */     EntityAnimal entityanimal = null;
/*     */     
/*  74 */     for (EntityAnimal entityanimal1 : list) {
/*     */       
/*  76 */       if (this.theAnimal.canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity((Entity)entityanimal1) < d0) {
/*     */         
/*  78 */         entityanimal = entityanimal1;
/*  79 */         d0 = this.theAnimal.getDistanceSqToEntity((Entity)entityanimal1);
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     return entityanimal;
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnBaby() {
/*  88 */     EntityAgeable entityageable = this.theAnimal.createChild((EntityAgeable)this.targetMate);
/*     */     
/*  90 */     if (entityageable != null) {
/*     */       
/*  92 */       EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();
/*     */       
/*  94 */       if (entityplayer == null && this.targetMate.getPlayerInLove() != null)
/*     */       {
/*  96 */         entityplayer = this.targetMate.getPlayerInLove();
/*     */       }
/*     */       
/*  99 */       if (entityplayer != null) {
/*     */         
/* 101 */         entityplayer.triggerAchievement(StatList.animalsBredStat);
/*     */         
/* 103 */         if (this.theAnimal instanceof net.minecraft.entity.passive.EntityCow)
/*     */         {
/* 105 */           entityplayer.triggerAchievement((StatBase)AchievementList.breedCow);
/*     */         }
/*     */       } 
/*     */       
/* 109 */       this.theAnimal.setGrowingAge(6000);
/* 110 */       this.targetMate.setGrowingAge(6000);
/* 111 */       this.theAnimal.resetInLove();
/* 112 */       this.targetMate.resetInLove();
/* 113 */       entityageable.setGrowingAge(-24000);
/* 114 */       entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
/* 115 */       this.theWorld.spawnEntityInWorld((Entity)entityageable);
/* 116 */       Random random = this.theAnimal.getRNG();
/*     */       
/* 118 */       for (int i = 0; i < 7; i++) {
/*     */         
/* 120 */         double d0 = random.nextGaussian() * 0.02D;
/* 121 */         double d1 = random.nextGaussian() * 0.02D;
/* 122 */         double d2 = random.nextGaussian() * 0.02D;
/* 123 */         double d3 = random.nextDouble() * this.theAnimal.width * 2.0D - this.theAnimal.width;
/* 124 */         double d4 = 0.5D + random.nextDouble() * this.theAnimal.height;
/* 125 */         double d5 = random.nextDouble() * this.theAnimal.width * 2.0D - this.theAnimal.width;
/* 126 */         this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
/*     */       } 
/*     */       
/* 129 */       if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
/*     */       {
/* 131 */         this.theWorld.spawnEntityInWorld((Entity)new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIMate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */