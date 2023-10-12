/*    */ package net.minecraft.entity.item;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityEndermite;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.entity.projectile.EntityThrowable;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class EntityEnderPearl
/*    */   extends EntityThrowable
/*    */ {
/*    */   private EntityLivingBase field_181555_c;
/*    */   
/*    */   public EntityEnderPearl(World worldIn) {
/* 20 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityEnderPearl(World worldIn, EntityLivingBase p_i1783_2_) {
/* 25 */     super(worldIn, p_i1783_2_);
/* 26 */     this.field_181555_c = p_i1783_2_;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityEnderPearl(World worldIn, double x, double y, double z) {
/* 31 */     super(worldIn, x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onImpact(MovingObjectPosition p_70184_1_) {
/* 36 */     EntityLivingBase entitylivingbase = getThrower();
/*    */     
/* 38 */     if (p_70184_1_.entityHit != null) {
/*    */       
/* 40 */       if (p_70184_1_.entityHit == this.field_181555_c) {
/*    */         return;
/*    */       }
/*    */ 
/*    */       
/* 45 */       p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage((Entity)this, (Entity)entitylivingbase), 0.0F);
/*    */     } 
/*    */     
/* 48 */     for (int i = 0; i < 32; i++)
/*    */     {
/* 50 */       this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
/*    */     }
/*    */     
/* 53 */     if (!this.worldObj.isRemote) {
/*    */       
/* 55 */       if (entitylivingbase instanceof EntityPlayerMP) {
/*    */         
/* 57 */         EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;
/*    */         
/* 59 */         if (entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == this.worldObj && !entityplayermp.isPlayerSleeping())
/*    */         {
/* 61 */           if (this.rand.nextFloat() < 0.05F && this.worldObj.getGameRules().getBoolean("doMobSpawning")) {
/*    */             
/* 63 */             EntityEndermite entityendermite = new EntityEndermite(this.worldObj);
/* 64 */             entityendermite.setSpawnedByPlayer(true);
/* 65 */             entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
/* 66 */             this.worldObj.spawnEntityInWorld((Entity)entityendermite);
/*    */           } 
/*    */           
/* 69 */           if (entitylivingbase.isRiding())
/*    */           {
/* 71 */             entitylivingbase.mountEntity((Entity)null);
/*    */           }
/*    */           
/* 74 */           entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
/* 75 */           entitylivingbase.fallDistance = 0.0F;
/* 76 */           entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0F);
/*    */         }
/*    */       
/* 79 */       } else if (entitylivingbase != null) {
/*    */         
/* 81 */         entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
/* 82 */         entitylivingbase.fallDistance = 0.0F;
/*    */       } 
/*    */       
/* 85 */       setDead();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 91 */     EntityLivingBase entitylivingbase = getThrower();
/*    */     
/* 93 */     if (entitylivingbase != null && entitylivingbase instanceof net.minecraft.entity.player.EntityPlayer && !entitylivingbase.isEntityAlive()) {
/*    */       
/* 95 */       setDead();
/*    */     }
/*    */     else {
/*    */       
/* 99 */       super.onUpdate();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityEnderPearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */