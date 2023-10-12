/*    */ package net.minecraft.entity.projectile;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntitySmallFireball
/*    */   extends EntityFireball
/*    */ {
/*    */   public EntitySmallFireball(World worldIn) {
/* 15 */     super(worldIn);
/* 16 */     setSize(0.3125F, 0.3125F);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
/* 21 */     super(worldIn, shooter, accelX, accelY, accelZ);
/* 22 */     setSize(0.3125F, 0.3125F);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
/* 27 */     super(worldIn, x, y, z, accelX, accelY, accelZ);
/* 28 */     setSize(0.3125F, 0.3125F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onImpact(MovingObjectPosition movingObject) {
/* 33 */     if (!this.worldObj.isRemote) {
/*    */       
/* 35 */       if (movingObject.entityHit != null) {
/*    */         
/* 37 */         boolean flag = movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, (Entity)this.shootingEntity), 5.0F);
/*    */         
/* 39 */         if (flag)
/*    */         {
/* 41 */           applyEnchantments(this.shootingEntity, movingObject.entityHit);
/*    */           
/* 43 */           if (!movingObject.entityHit.isImmuneToFire())
/*    */           {
/* 45 */             movingObject.entityHit.setFire(5);
/*    */           }
/*    */         }
/*    */       
/*    */       } else {
/*    */         
/* 51 */         boolean flag1 = true;
/*    */         
/* 53 */         if (this.shootingEntity != null && this.shootingEntity instanceof net.minecraft.entity.EntityLiving)
/*    */         {
/* 55 */           flag1 = this.worldObj.getGameRules().getBoolean("mobGriefing");
/*    */         }
/*    */         
/* 58 */         if (flag1) {
/*    */           
/* 60 */           BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);
/*    */           
/* 62 */           if (this.worldObj.isAirBlock(blockpos))
/*    */           {
/* 64 */             this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*    */           }
/*    */         } 
/*    */       } 
/*    */       
/* 69 */       setDead();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeCollidedWith() {
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntitySmallFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */