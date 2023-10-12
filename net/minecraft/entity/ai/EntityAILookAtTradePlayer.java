/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class EntityAILookAtTradePlayer extends EntityAIWatchClosest {
/*    */   private final EntityVillager theMerchant;
/*    */   
/*    */   public EntityAILookAtTradePlayer(EntityVillager theMerchantIn) {
/* 12 */     super((EntityLiving)theMerchantIn, (Class)EntityPlayer.class, 8.0F);
/* 13 */     this.theMerchant = theMerchantIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 18 */     if (this.theMerchant.isTrading()) {
/*    */       
/* 20 */       this.closestEntity = (Entity)this.theMerchant.getCustomer();
/* 21 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAILookAtTradePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */