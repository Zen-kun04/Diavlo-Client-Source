/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class EntityAILeapAtTarget
/*    */   extends EntityAIBase {
/*    */   EntityLiving leaper;
/*    */   EntityLivingBase leapTarget;
/*    */   float leapMotionY;
/*    */   
/*    */   public EntityAILeapAtTarget(EntityLiving leapingEntity, float leapMotionYIn) {
/* 15 */     this.leaper = leapingEntity;
/* 16 */     this.leapMotionY = leapMotionYIn;
/* 17 */     setMutexBits(5);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 22 */     this.leapTarget = this.leaper.getAttackTarget();
/*    */     
/* 24 */     if (this.leapTarget == null)
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     double d0 = this.leaper.getDistanceSqToEntity((Entity)this.leapTarget);
/* 31 */     return (d0 >= 4.0D && d0 <= 16.0D) ? (!this.leaper.onGround ? false : ((this.leaper.getRNG().nextInt(5) == 0))) : false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 37 */     return !this.leaper.onGround;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 42 */     double d0 = this.leapTarget.posX - this.leaper.posX;
/* 43 */     double d1 = this.leapTarget.posZ - this.leaper.posZ;
/* 44 */     float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
/* 45 */     this.leaper.motionX += d0 / f * 0.5D * 0.800000011920929D + this.leaper.motionX * 0.20000000298023224D;
/* 46 */     this.leaper.motionZ += d1 / f * 0.5D * 0.800000011920929D + this.leaper.motionZ * 0.20000000298023224D;
/* 47 */     this.leaper.motionY = this.leapMotionY;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAILeapAtTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */