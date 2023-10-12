/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockDoor;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ public class EntityAIBreakDoor extends EntityAIDoorInteract {
/*    */   private int breakingTime;
/* 11 */   private int previousBreakProgress = -1;
/*    */ 
/*    */   
/*    */   public EntityAIBreakDoor(EntityLiving entityIn) {
/* 15 */     super(entityIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 20 */     if (!super.shouldExecute())
/*    */     {
/* 22 */       return false;
/*    */     }
/* 24 */     if (!this.theEntity.worldObj.getGameRules().getBoolean("mobGriefing"))
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     BlockDoor blockdoor = this.doorBlock;
/* 31 */     return !BlockDoor.isOpen((IBlockAccess)this.theEntity.worldObj, this.doorPosition);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 37 */     super.startExecuting();
/* 38 */     this.breakingTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 43 */     double d0 = this.theEntity.getDistanceSq(this.doorPosition);
/*    */ 
/*    */     
/* 46 */     if (this.breakingTime <= 240) {
/*    */       
/* 48 */       BlockDoor blockdoor = this.doorBlock;
/*    */       
/* 50 */       if (!BlockDoor.isOpen((IBlockAccess)this.theEntity.worldObj, this.doorPosition) && d0 < 4.0D) {
/*    */         
/* 52 */         boolean bool = true;
/* 53 */         return bool;
/*    */       } 
/*    */     } 
/*    */     
/* 57 */     boolean flag = false;
/* 58 */     return flag;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 63 */     super.resetTask();
/* 64 */     this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 69 */     super.updateTask();
/*    */     
/* 71 */     if (this.theEntity.getRNG().nextInt(20) == 0)
/*    */     {
/* 73 */       this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0);
/*    */     }
/*    */     
/* 76 */     this.breakingTime++;
/* 77 */     int i = (int)(this.breakingTime / 240.0F * 10.0F);
/*    */     
/* 79 */     if (i != this.previousBreakProgress) {
/*    */       
/* 81 */       this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, i);
/* 82 */       this.previousBreakProgress = i;
/*    */     } 
/*    */     
/* 85 */     if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
/*    */       
/* 87 */       this.theEntity.worldObj.setBlockToAir(this.doorPosition);
/* 88 */       this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
/* 89 */       this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock((Block)this.doorBlock));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIBreakDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */