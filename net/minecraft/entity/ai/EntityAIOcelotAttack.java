/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIOcelotAttack
/*    */   extends EntityAIBase {
/*    */   World theWorld;
/*    */   EntityLiving theEntity;
/*    */   EntityLivingBase theVictim;
/*    */   int attackCountdown;
/*    */   
/*    */   public EntityAIOcelotAttack(EntityLiving theEntityIn) {
/* 16 */     this.theEntity = theEntityIn;
/* 17 */     this.theWorld = theEntityIn.worldObj;
/* 18 */     setMutexBits(3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 23 */     EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
/*    */     
/* 25 */     if (entitylivingbase == null)
/*    */     {
/* 27 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 31 */     this.theVictim = entitylivingbase;
/* 32 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 38 */     return !this.theVictim.isEntityAlive() ? false : ((this.theEntity.getDistanceSqToEntity((Entity)this.theVictim) > 225.0D) ? false : ((!this.theEntity.getNavigator().noPath() || shouldExecute())));
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 43 */     this.theVictim = null;
/* 44 */     this.theEntity.getNavigator().clearPathEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 49 */     this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)this.theVictim, 30.0F, 30.0F);
/* 50 */     double d0 = (this.theEntity.width * 2.0F * this.theEntity.width * 2.0F);
/* 51 */     double d1 = this.theEntity.getDistanceSq(this.theVictim.posX, (this.theVictim.getEntityBoundingBox()).minY, this.theVictim.posZ);
/* 52 */     double d2 = 0.8D;
/*    */     
/* 54 */     if (d1 > d0 && d1 < 16.0D) {
/*    */       
/* 56 */       d2 = 1.33D;
/*    */     }
/* 58 */     else if (d1 < 225.0D) {
/*    */       
/* 60 */       d2 = 0.6D;
/*    */     } 
/*    */     
/* 63 */     this.theEntity.getNavigator().tryMoveToEntityLiving((Entity)this.theVictim, d2);
/* 64 */     this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
/*    */     
/* 66 */     if (d1 <= d0)
/*    */     {
/* 68 */       if (this.attackCountdown <= 0) {
/*    */         
/* 70 */         this.attackCountdown = 20;
/* 71 */         this.theEntity.attackEntityAsMob((Entity)this.theVictim);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIOcelotAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */