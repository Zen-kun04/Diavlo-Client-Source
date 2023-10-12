/*    */ package net.minecraft.entity.effect;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityLightningBolt
/*    */   extends EntityWeatherEffect
/*    */ {
/*    */   private int lightningState;
/*    */   public long boltVertex;
/*    */   private int boltLivingTime;
/*    */   
/*    */   public EntityLightningBolt(World worldIn, double posX, double posY, double posZ) {
/* 21 */     super(worldIn);
/* 22 */     setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
/* 23 */     this.lightningState = 2;
/* 24 */     this.boltVertex = this.rand.nextLong();
/* 25 */     this.boltLivingTime = this.rand.nextInt(3) + 1;
/* 26 */     BlockPos blockpos = new BlockPos(this);
/*    */     
/* 28 */     if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10)) {
/*    */       
/* 30 */       if (worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, blockpos))
/*    */       {
/* 32 */         worldIn.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*    */       }
/*    */       
/* 35 */       for (int i = 0; i < 4; i++) {
/*    */         
/* 37 */         BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);
/*    */         
/* 39 */         if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(worldIn, blockpos1))
/*    */         {
/* 41 */           worldIn.setBlockState(blockpos1, Blocks.fire.getDefaultState());
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 49 */     super.onUpdate();
/*    */     
/* 51 */     if (this.lightningState == 2) {
/*    */       
/* 53 */       this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
/* 54 */       this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
/*    */     } 
/*    */     
/* 57 */     this.lightningState--;
/*    */     
/* 59 */     if (this.lightningState < 0)
/*    */     {
/* 61 */       if (this.boltLivingTime == 0) {
/*    */         
/* 63 */         setDead();
/*    */       }
/* 65 */       else if (this.lightningState < -this.rand.nextInt(10)) {
/*    */         
/* 67 */         this.boltLivingTime--;
/* 68 */         this.lightningState = 1;
/* 69 */         this.boltVertex = this.rand.nextLong();
/* 70 */         BlockPos blockpos = new BlockPos(this);
/*    */         
/* 72 */         if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("doFireTick") && this.worldObj.isAreaLoaded(blockpos, 10) && this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air && Blocks.fire.canPlaceBlockAt(this.worldObj, blockpos))
/*    */         {
/* 74 */           this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 79 */     if (this.lightningState >= 0)
/*    */     {
/* 81 */       if (this.worldObj.isRemote) {
/*    */         
/* 83 */         this.worldObj.setLastLightningBolt(2);
/*    */       }
/*    */       else {
/*    */         
/* 87 */         double d0 = 3.0D;
/* 88 */         List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0));
/*    */         
/* 90 */         for (int i = 0; i < list.size(); i++) {
/*    */           
/* 92 */           Entity entity = list.get(i);
/* 93 */           entity.onStruckByLightning(this);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   protected void entityInit() {}
/*    */   
/*    */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*    */   
/*    */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\effect\EntityLightningBolt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */