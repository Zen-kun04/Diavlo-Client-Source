/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityAIMoveToBlock extends EntityAIBase {
/*     */   private final EntityCreature theEntity;
/*     */   private final double movementSpeed;
/*     */   protected int runDelay;
/*     */   private int timeoutCounter;
/*     */   private int field_179490_f;
/*  14 */   protected BlockPos destinationBlock = BlockPos.ORIGIN;
/*     */   
/*     */   private boolean isAboveDestination;
/*     */   private int searchLength;
/*     */   
/*     */   public EntityAIMoveToBlock(EntityCreature creature, double speedIn, int length) {
/*  20 */     this.theEntity = creature;
/*  21 */     this.movementSpeed = speedIn;
/*  22 */     this.searchLength = length;
/*  23 */     setMutexBits(5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  28 */     if (this.runDelay > 0) {
/*     */       
/*  30 */       this.runDelay--;
/*  31 */       return false;
/*     */     } 
/*     */ 
/*     */     
/*  35 */     this.runDelay = 200 + this.theEntity.getRNG().nextInt(200);
/*  36 */     return searchForDestination();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  42 */     return (this.timeoutCounter >= -this.field_179490_f && this.timeoutCounter <= 1200 && shouldMoveTo(this.theEntity.worldObj, this.destinationBlock));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  47 */     this.theEntity.getNavigator().tryMoveToXYZ(this.destinationBlock.getX() + 0.5D, (this.destinationBlock.getY() + 1), this.destinationBlock.getZ() + 0.5D, this.movementSpeed);
/*  48 */     this.timeoutCounter = 0;
/*  49 */     this.field_179490_f = this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {}
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  58 */     if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D) {
/*     */       
/*  60 */       this.isAboveDestination = false;
/*  61 */       this.timeoutCounter++;
/*     */       
/*  63 */       if (this.timeoutCounter % 40 == 0)
/*     */       {
/*  65 */         this.theEntity.getNavigator().tryMoveToXYZ(this.destinationBlock.getX() + 0.5D, (this.destinationBlock.getY() + 1), this.destinationBlock.getZ() + 0.5D, this.movementSpeed);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  70 */       this.isAboveDestination = true;
/*  71 */       this.timeoutCounter--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean getIsAboveDestination() {
/*  77 */     return this.isAboveDestination;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean searchForDestination() {
/*  82 */     int i = this.searchLength;
/*  83 */     int j = 1;
/*  84 */     BlockPos blockpos = new BlockPos((Entity)this.theEntity);
/*     */     int k;
/*  86 */     for (k = 0; k <= 1; k = (k > 0) ? -k : (1 - k)) {
/*     */       
/*  88 */       for (int l = 0; l < i; l++) {
/*     */         int i1;
/*  90 */         for (i1 = 0; i1 <= l; i1 = (i1 > 0) ? -i1 : (1 - i1)) {
/*     */           int j1;
/*  92 */           for (j1 = (i1 < l && i1 > -l) ? l : 0; j1 <= l; j1 = (j1 > 0) ? -j1 : (1 - j1)) {
/*     */             
/*  94 */             BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);
/*     */             
/*  96 */             if (this.theEntity.isWithinHomeDistanceFromPosition(blockpos1) && shouldMoveTo(this.theEntity.worldObj, blockpos1)) {
/*     */               
/*  98 */               this.destinationBlock = blockpos1;
/*  99 */               return true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract boolean shouldMoveTo(World paramWorld, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIMoveToBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */