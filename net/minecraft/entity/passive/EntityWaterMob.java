/*    */ package net.minecraft.entity.passive;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class EntityWaterMob
/*    */   extends EntityLiving implements IAnimals {
/*    */   public EntityWaterMob(World worldIn) {
/* 12 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBreatheUnderwater() {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getCanSpawnHere() {
/* 22 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isNotColliding() {
/* 27 */     return this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTalkInterval() {
/* 32 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canDespawn() {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getExperiencePoints(EntityPlayer player) {
/* 42 */     return 1 + this.worldObj.rand.nextInt(3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEntityUpdate() {
/* 47 */     int i = getAir();
/* 48 */     super.onEntityUpdate();
/*    */     
/* 50 */     if (isEntityAlive() && !isInWater()) {
/*    */       
/* 52 */       i--;
/* 53 */       setAir(i);
/*    */       
/* 55 */       if (getAir() == -20)
/*    */       {
/* 57 */         setAir(0);
/* 58 */         attackEntityFrom(DamageSource.drown, 2.0F);
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 63 */       setAir(300);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPushedByWater() {
/* 69 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityWaterMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */