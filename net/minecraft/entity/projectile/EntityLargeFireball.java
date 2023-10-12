/*    */ package net.minecraft.entity.projectile;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityLargeFireball
/*    */   extends EntityFireball {
/* 12 */   public int explosionPower = 1;
/*    */ 
/*    */   
/*    */   public EntityLargeFireball(World worldIn) {
/* 16 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityLargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
/* 21 */     super(worldIn, x, y, z, accelX, accelY, accelZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityLargeFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
/* 26 */     super(worldIn, shooter, accelX, accelY, accelZ);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onImpact(MovingObjectPosition movingObject) {
/* 31 */     if (!this.worldObj.isRemote) {
/*    */       
/* 33 */       if (movingObject.entityHit != null) {
/*    */         
/* 35 */         movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, (Entity)this.shootingEntity), 6.0F);
/* 36 */         applyEnchantments(this.shootingEntity, movingObject.entityHit);
/*    */       } 
/*    */       
/* 39 */       boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
/* 40 */       this.worldObj.newExplosion((Entity)null, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
/* 41 */       setDead();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 47 */     super.writeEntityToNBT(tagCompound);
/* 48 */     tagCompound.setInteger("ExplosionPower", this.explosionPower);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 53 */     super.readEntityFromNBT(tagCompund);
/*    */     
/* 55 */     if (tagCompund.hasKey("ExplosionPower", 99))
/*    */     {
/* 57 */       this.explosionPower = tagCompund.getInteger("ExplosionPower");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityLargeFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */