/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityWolf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIBeg
/*    */   extends EntityAIBase {
/*    */   private EntityWolf theWolf;
/*    */   private EntityPlayer thePlayer;
/*    */   private World worldObject;
/*    */   private float minPlayerDistance;
/*    */   private int timeoutCounter;
/*    */   
/*    */   public EntityAIBeg(EntityWolf wolf, float minDistance) {
/* 19 */     this.theWolf = wolf;
/* 20 */     this.worldObject = wolf.worldObj;
/* 21 */     this.minPlayerDistance = minDistance;
/* 22 */     setMutexBits(2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 27 */     this.thePlayer = this.worldObject.getClosestPlayerToEntity((Entity)this.theWolf, this.minPlayerDistance);
/* 28 */     return (this.thePlayer == null) ? false : hasPlayerGotBoneInHand(this.thePlayer);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 33 */     return !this.thePlayer.isEntityAlive() ? false : ((this.theWolf.getDistanceSqToEntity((Entity)this.thePlayer) > (this.minPlayerDistance * this.minPlayerDistance)) ? false : ((this.timeoutCounter > 0 && hasPlayerGotBoneInHand(this.thePlayer))));
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 38 */     this.theWolf.setBegging(true);
/* 39 */     this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 44 */     this.theWolf.setBegging(false);
/* 45 */     this.thePlayer = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 50 */     this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, this.theWolf.getVerticalFaceSpeed());
/* 51 */     this.timeoutCounter--;
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean hasPlayerGotBoneInHand(EntityPlayer player) {
/* 56 */     ItemStack itemstack = player.inventory.getCurrentItem();
/* 57 */     return (itemstack == null) ? false : ((!this.theWolf.isTamed() && itemstack.getItem() == Items.bone) ? true : this.theWolf.isBreedingItem(itemstack));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIBeg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */