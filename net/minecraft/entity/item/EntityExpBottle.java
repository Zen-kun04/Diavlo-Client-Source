/*    */ package net.minecraft.entity.item;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.projectile.EntityThrowable;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityExpBottle
/*    */   extends EntityThrowable {
/*    */   public EntityExpBottle(World worldIn) {
/* 13 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityExpBottle(World worldIn, EntityLivingBase p_i1786_2_) {
/* 18 */     super(worldIn, p_i1786_2_);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityExpBottle(World worldIn, double x, double y, double z) {
/* 23 */     super(worldIn, x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getGravityVelocity() {
/* 28 */     return 0.07F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getVelocity() {
/* 33 */     return 0.7F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getInaccuracy() {
/* 38 */     return -20.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onImpact(MovingObjectPosition p_70184_1_) {
/* 43 */     if (!this.worldObj.isRemote) {
/*    */       
/* 45 */       this.worldObj.playAuxSFX(2002, new BlockPos((Entity)this), 0);
/* 46 */       int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);
/*    */       
/* 48 */       while (i > 0) {
/*    */         
/* 50 */         int j = EntityXPOrb.getXPSplit(i);
/* 51 */         i -= j;
/* 52 */         this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
/*    */       } 
/*    */       
/* 55 */       setDead();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityExpBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */