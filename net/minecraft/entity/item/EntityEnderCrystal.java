/*    */ package net.minecraft.entity.item;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class EntityEnderCrystal
/*    */   extends Entity
/*    */ {
/*    */   public int innerRotation;
/*    */   public int health;
/*    */   
/*    */   public EntityEnderCrystal(World worldIn) {
/* 19 */     super(worldIn);
/* 20 */     this.preventEntitySpawning = true;
/* 21 */     setSize(2.0F, 2.0F);
/* 22 */     this.health = 5;
/* 23 */     this.innerRotation = this.rand.nextInt(100000);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityEnderCrystal(World worldIn, double x, double y, double z) {
/* 28 */     this(worldIn);
/* 29 */     setPosition(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canTriggerWalking() {
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void entityInit() {
/* 39 */     this.dataWatcher.addObject(8, Integer.valueOf(this.health));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 44 */     this.prevPosX = this.posX;
/* 45 */     this.prevPosY = this.posY;
/* 46 */     this.prevPosZ = this.posZ;
/* 47 */     this.innerRotation++;
/* 48 */     this.dataWatcher.updateObject(8, Integer.valueOf(this.health));
/* 49 */     int i = MathHelper.floor_double(this.posX);
/* 50 */     int j = MathHelper.floor_double(this.posY);
/* 51 */     int k = MathHelper.floor_double(this.posZ);
/*    */     
/* 53 */     if (this.worldObj.provider instanceof net.minecraft.world.WorldProviderEnd && this.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock() != Blocks.fire)
/*    */     {
/* 55 */       this.worldObj.setBlockState(new BlockPos(i, j, k), Blocks.fire.getDefaultState());
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*    */ 
/*    */   
/*    */   public boolean canBeCollidedWith() {
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 74 */     if (isEntityInvulnerable(source))
/*    */     {
/* 76 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 80 */     if (!this.isDead && !this.worldObj.isRemote) {
/*    */       
/* 82 */       this.health = 0;
/*    */       
/* 84 */       if (this.health <= 0) {
/*    */         
/* 86 */         setDead();
/*    */         
/* 88 */         if (!this.worldObj.isRemote)
/*    */         {
/* 90 */           this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, 6.0F, true);
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 95 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityEnderCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */