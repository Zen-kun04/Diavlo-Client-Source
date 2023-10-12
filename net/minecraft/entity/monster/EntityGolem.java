/*    */ package net.minecraft.entity.monster;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.passive.IAnimals;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class EntityGolem
/*    */   extends EntityCreature
/*    */   implements IAnimals {
/*    */   public EntityGolem(World worldIn) {
/* 11 */     super(worldIn);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void fall(float distance, float damageMultiplier) {}
/*    */ 
/*    */   
/*    */   protected String getLivingSound() {
/* 20 */     return "none";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getHurtSound() {
/* 25 */     return "none";
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getDeathSound() {
/* 30 */     return "none";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTalkInterval() {
/* 35 */     return 120;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canDespawn() {
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */