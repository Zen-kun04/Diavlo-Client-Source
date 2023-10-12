/*    */ package net.minecraft.entity.projectile;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityChicken;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityEgg
/*    */   extends EntityThrowable {
/*    */   public EntityEgg(World worldIn) {
/* 16 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityEgg(World worldIn, EntityLivingBase throwerIn) {
/* 21 */     super(worldIn, throwerIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityEgg(World worldIn, double x, double y, double z) {
/* 26 */     super(worldIn, x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onImpact(MovingObjectPosition p_70184_1_) {
/* 31 */     if (p_70184_1_.entityHit != null)
/*    */     {
/* 33 */       p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, (Entity)getThrower()), 0.0F);
/*    */     }
/*    */     
/* 36 */     if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0) {
/*    */       
/* 38 */       int i = 1;
/*    */       
/* 40 */       if (this.rand.nextInt(32) == 0)
/*    */       {
/* 42 */         i = 4;
/*    */       }
/*    */       
/* 45 */       for (int j = 0; j < i; j++) {
/*    */         
/* 47 */         EntityChicken entitychicken = new EntityChicken(this.worldObj);
/* 48 */         entitychicken.setGrowingAge(-24000);
/* 49 */         entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 50 */         this.worldObj.spawnEntityInWorld((Entity)entitychicken);
/*    */       } 
/*    */     } 
/*    */     
/* 54 */     double d0 = 0.08D;
/*    */     
/* 56 */     for (int k = 0; k < 8; k++) {
/*    */       
/* 58 */       this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D, (this.rand.nextFloat() - 0.5D) * 0.08D, new int[] { Item.getIdFromItem(Items.egg) });
/*    */     } 
/*    */     
/* 61 */     if (!this.worldObj.isRemote)
/*    */     {
/* 63 */       setDead();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */