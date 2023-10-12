/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIRunAroundLikeCrazy
/*    */   extends EntityAIBase {
/*    */   private EntityHorse horseHost;
/*    */   private double speed;
/*    */   private double targetX;
/*    */   private double targetY;
/*    */   private double targetZ;
/*    */   
/*    */   public EntityAIRunAroundLikeCrazy(EntityHorse horse, double speedIn) {
/* 18 */     this.horseHost = horse;
/* 19 */     this.speed = speedIn;
/* 20 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 25 */     if (!this.horseHost.isTame() && this.horseHost.riddenByEntity != null) {
/*    */       
/* 27 */       Vec3 vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature)this.horseHost, 5, 4);
/*    */       
/* 29 */       if (vec3 == null)
/*    */       {
/* 31 */         return false;
/*    */       }
/*    */ 
/*    */       
/* 35 */       this.targetX = vec3.xCoord;
/* 36 */       this.targetY = vec3.yCoord;
/* 37 */       this.targetZ = vec3.zCoord;
/* 38 */       return true;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 49 */     this.horseHost.getNavigator().tryMoveToXYZ(this.targetX, this.targetY, this.targetZ, this.speed);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 54 */     return (!this.horseHost.getNavigator().noPath() && this.horseHost.riddenByEntity != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 59 */     if (this.horseHost.getRNG().nextInt(50) == 0) {
/*    */       
/* 61 */       if (this.horseHost.riddenByEntity instanceof EntityPlayer) {
/*    */         
/* 63 */         int i = this.horseHost.getTemper();
/* 64 */         int j = this.horseHost.getMaxTemper();
/*    */         
/* 66 */         if (j > 0 && this.horseHost.getRNG().nextInt(j) < i) {
/*    */           
/* 68 */           this.horseHost.setTamedBy((EntityPlayer)this.horseHost.riddenByEntity);
/* 69 */           this.horseHost.worldObj.setEntityState((Entity)this.horseHost, (byte)7);
/*    */           
/*    */           return;
/*    */         } 
/* 73 */         this.horseHost.increaseTemper(5);
/*    */       } 
/*    */       
/* 76 */       this.horseHost.riddenByEntity.mountEntity((Entity)null);
/* 77 */       this.horseHost.riddenByEntity = null;
/* 78 */       this.horseHost.makeHorseRearWithSound();
/* 79 */       this.horseHost.worldObj.setEntityState((Entity)this.horseHost, (byte)6);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIRunAroundLikeCrazy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */