/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class EntityAITradePlayer
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityVillager villager;
/*    */   
/*    */   public EntityAITradePlayer(EntityVillager villagerIn) {
/* 13 */     this.villager = villagerIn;
/* 14 */     setMutexBits(5);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 19 */     if (!this.villager.isEntityAlive())
/*    */     {
/* 21 */       return false;
/*    */     }
/* 23 */     if (this.villager.isInWater())
/*    */     {
/* 25 */       return false;
/*    */     }
/* 27 */     if (!this.villager.onGround)
/*    */     {
/* 29 */       return false;
/*    */     }
/* 31 */     if (this.villager.velocityChanged)
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 37 */     EntityPlayer entityplayer = this.villager.getCustomer();
/* 38 */     return (entityplayer == null) ? false : ((this.villager.getDistanceSqToEntity((Entity)entityplayer) > 16.0D) ? false : (entityplayer.openContainer instanceof net.minecraft.inventory.Container));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 44 */     this.villager.getNavigator().clearPathEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 49 */     this.villager.setCustomer((EntityPlayer)null);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAITradePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */