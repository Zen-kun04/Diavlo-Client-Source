/*    */ package net.minecraft.entity.projectile;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntitySnowball
/*    */   extends EntityThrowable
/*    */ {
/*    */   public EntitySnowball(World worldIn) {
/* 14 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntitySnowball(World worldIn, EntityLivingBase throwerIn) {
/* 19 */     super(worldIn, throwerIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntitySnowball(World worldIn, double x, double y, double z) {
/* 24 */     super(worldIn, x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onImpact(MovingObjectPosition p_70184_1_) {
/* 29 */     if (p_70184_1_.entityHit != null) {
/*    */       
/* 31 */       int i = 0;
/*    */       
/* 33 */       if (p_70184_1_.entityHit instanceof net.minecraft.entity.monster.EntityBlaze)
/*    */       {
/* 35 */         i = 3;
/*    */       }
/*    */       
/* 38 */       p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, (Entity)getThrower()), i);
/*    */     } 
/*    */     
/* 41 */     for (int j = 0; j < 8; j++)
/*    */     {
/* 43 */       this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*    */     }
/*    */     
/* 46 */     if (!this.worldObj.isRemote)
/*    */     {
/* 48 */       setDead();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntitySnowball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */