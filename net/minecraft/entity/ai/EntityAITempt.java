/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ 
/*     */ public class EntityAITempt
/*     */   extends EntityAIBase {
/*     */   private EntityCreature temptedEntity;
/*     */   private double speed;
/*     */   private double targetX;
/*     */   private double targetY;
/*     */   private double targetZ;
/*     */   private double pitch;
/*     */   private double yaw;
/*     */   private EntityPlayer temptingPlayer;
/*     */   private int delayTemptCounter;
/*     */   private boolean isRunning;
/*     */   private Item temptItem;
/*     */   private boolean scaredByPlayerMovement;
/*     */   private boolean avoidWater;
/*     */   
/*     */   public EntityAITempt(EntityCreature temptedEntityIn, double speedIn, Item temptItemIn, boolean scaredByPlayerMovementIn) {
/*  27 */     this.temptedEntity = temptedEntityIn;
/*  28 */     this.speed = speedIn;
/*  29 */     this.temptItem = temptItemIn;
/*  30 */     this.scaredByPlayerMovement = scaredByPlayerMovementIn;
/*  31 */     setMutexBits(3);
/*     */     
/*  33 */     if (!(temptedEntityIn.getNavigator() instanceof PathNavigateGround))
/*     */     {
/*  35 */       throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  41 */     if (this.delayTemptCounter > 0) {
/*     */       
/*  43 */       this.delayTemptCounter--;
/*  44 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  48 */     this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity((Entity)this.temptedEntity, 10.0D);
/*     */     
/*  50 */     if (this.temptingPlayer == null)
/*     */     {
/*  52 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  56 */     ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
/*  57 */     return (itemstack == null) ? false : ((itemstack.getItem() == this.temptItem));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  64 */     if (this.scaredByPlayerMovement) {
/*     */       
/*  66 */       if (this.temptedEntity.getDistanceSqToEntity((Entity)this.temptingPlayer) < 36.0D) {
/*     */         
/*  68 */         if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D)
/*     */         {
/*  70 */           return false;
/*     */         }
/*     */         
/*  73 */         if (Math.abs(this.temptingPlayer.rotationPitch - this.pitch) > 5.0D || Math.abs(this.temptingPlayer.rotationYaw - this.yaw) > 5.0D)
/*     */         {
/*  75 */           return false;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/*  80 */         this.targetX = this.temptingPlayer.posX;
/*  81 */         this.targetY = this.temptingPlayer.posY;
/*  82 */         this.targetZ = this.temptingPlayer.posZ;
/*     */       } 
/*     */       
/*  85 */       this.pitch = this.temptingPlayer.rotationPitch;
/*  86 */       this.yaw = this.temptingPlayer.rotationYaw;
/*     */     } 
/*     */     
/*  89 */     return shouldExecute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  94 */     this.targetX = this.temptingPlayer.posX;
/*  95 */     this.targetY = this.temptingPlayer.posY;
/*  96 */     this.targetZ = this.temptingPlayer.posZ;
/*  97 */     this.isRunning = true;
/*  98 */     this.avoidWater = ((PathNavigateGround)this.temptedEntity.getNavigator()).getAvoidsWater();
/*  99 */     ((PathNavigateGround)this.temptedEntity.getNavigator()).setAvoidsWater(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 104 */     this.temptingPlayer = null;
/* 105 */     this.temptedEntity.getNavigator().clearPathEntity();
/* 106 */     this.delayTemptCounter = 100;
/* 107 */     this.isRunning = false;
/* 108 */     ((PathNavigateGround)this.temptedEntity.getNavigator()).setAvoidsWater(this.avoidWater);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 113 */     this.temptedEntity.getLookHelper().setLookPositionWithEntity((Entity)this.temptingPlayer, 30.0F, this.temptedEntity.getVerticalFaceSpeed());
/*     */     
/* 115 */     if (this.temptedEntity.getDistanceSqToEntity((Entity)this.temptingPlayer) < 6.25D) {
/*     */       
/* 117 */       this.temptedEntity.getNavigator().clearPathEntity();
/*     */     }
/*     */     else {
/*     */       
/* 121 */       this.temptedEntity.getNavigator().tryMoveToEntityLiving((Entity)this.temptingPlayer, this.speed);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 127 */     return this.isRunning;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAITempt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */