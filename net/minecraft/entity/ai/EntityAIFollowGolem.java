/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ 
/*    */ public class EntityAIFollowGolem
/*    */   extends EntityAIBase {
/*    */   private EntityVillager theVillager;
/*    */   private EntityIronGolem theGolem;
/*    */   private int takeGolemRoseTick;
/*    */   private boolean tookGolemRose;
/*    */   
/*    */   public EntityAIFollowGolem(EntityVillager theVillagerIn) {
/* 16 */     this.theVillager = theVillagerIn;
/* 17 */     setMutexBits(3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 22 */     if (this.theVillager.getGrowingAge() >= 0)
/*    */     {
/* 24 */       return false;
/*    */     }
/* 26 */     if (!this.theVillager.worldObj.isDaytime())
/*    */     {
/* 28 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 32 */     List<EntityIronGolem> list = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, this.theVillager.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D));
/*    */     
/* 34 */     if (list.isEmpty())
/*    */     {
/* 36 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 40 */     for (EntityIronGolem entityirongolem : list) {
/*    */       
/* 42 */       if (entityirongolem.getHoldRoseTick() > 0) {
/*    */         
/* 44 */         this.theGolem = entityirongolem;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 49 */     return (this.theGolem != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 56 */     return (this.theGolem.getHoldRoseTick() > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 61 */     this.takeGolemRoseTick = this.theVillager.getRNG().nextInt(320);
/* 62 */     this.tookGolemRose = false;
/* 63 */     this.theGolem.getNavigator().clearPathEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 68 */     this.theGolem = null;
/* 69 */     this.theVillager.getNavigator().clearPathEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 74 */     this.theVillager.getLookHelper().setLookPositionWithEntity((Entity)this.theGolem, 30.0F, 30.0F);
/*    */     
/* 76 */     if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
/*    */       
/* 78 */       this.theVillager.getNavigator().tryMoveToEntityLiving((Entity)this.theGolem, 0.5D);
/* 79 */       this.tookGolemRose = true;
/*    */     } 
/*    */     
/* 82 */     if (this.tookGolemRose && this.theVillager.getDistanceSqToEntity((Entity)this.theGolem) < 4.0D) {
/*    */       
/* 84 */       this.theGolem.setHoldingRose(false);
/* 85 */       this.theVillager.getNavigator().clearPathEntity();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIFollowGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */