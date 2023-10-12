/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class EntityLeashKnot
/*     */   extends EntityHanging
/*     */ {
/*     */   public EntityLeashKnot(World worldIn) {
/*  17 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityLeashKnot(World worldIn, BlockPos hangingPositionIn) {
/*  22 */     super(worldIn, hangingPositionIn);
/*  23 */     setPosition(hangingPositionIn.getX() + 0.5D, hangingPositionIn.getY() + 0.5D, hangingPositionIn.getZ() + 0.5D);
/*  24 */     float f = 0.125F;
/*  25 */     float f1 = 0.1875F;
/*  26 */     float f2 = 0.25F;
/*  27 */     setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875D, this.posY - 0.25D + 0.125D, this.posZ - 0.1875D, this.posX + 0.1875D, this.posY + 0.25D + 0.125D, this.posZ + 0.1875D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  32 */     super.entityInit();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateFacingWithBoundingBox(EnumFacing facingDirectionIn) {}
/*     */ 
/*     */   
/*     */   public int getWidthPixels() {
/*  41 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeightPixels() {
/*  46 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/*  51 */     return -0.0625F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/*  56 */     return (distance < 1024.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBroken(Entity brokenEntity) {}
/*     */ 
/*     */   
/*     */   public boolean writeToNBTOptional(NBTTagCompound tagCompund) {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/*  78 */     ItemStack itemstack = playerIn.getHeldItem();
/*  79 */     boolean flag = false;
/*     */     
/*  81 */     if (itemstack != null && itemstack.getItem() == Items.lead && !this.worldObj.isRemote) {
/*     */       
/*  83 */       double d0 = 7.0D;
/*     */       
/*  85 */       for (EntityLiving entityliving : this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0))) {
/*     */         
/*  87 */         if (entityliving.getLeashed() && entityliving.getLeashedToEntity() == playerIn) {
/*     */           
/*  89 */           entityliving.setLeashedToEntity(this, true);
/*  90 */           flag = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (!this.worldObj.isRemote && !flag) {
/*     */       
/*  97 */       setDead();
/*     */       
/*  99 */       if (playerIn.capabilities.isCreativeMode) {
/*     */         
/* 101 */         double d1 = 7.0D;
/*     */         
/* 103 */         for (EntityLiving entityliving1 : this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - d1, this.posY - d1, this.posZ - d1, this.posX + d1, this.posY + d1, this.posZ + d1))) {
/*     */           
/* 105 */           if (entityliving1.getLeashed() && entityliving1.getLeashedToEntity() == this)
/*     */           {
/* 107 */             entityliving1.clearLeashed(true, false);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onValidSurface() {
/* 118 */     return this.worldObj.getBlockState(this.hangingPosition).getBlock() instanceof net.minecraft.block.BlockFence;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EntityLeashKnot createKnot(World worldIn, BlockPos fence) {
/* 123 */     EntityLeashKnot entityleashknot = new EntityLeashKnot(worldIn, fence);
/* 124 */     entityleashknot.forceSpawn = true;
/* 125 */     worldIn.spawnEntityInWorld(entityleashknot);
/* 126 */     return entityleashknot;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EntityLeashKnot getKnotForPosition(World worldIn, BlockPos pos) {
/* 131 */     int i = pos.getX();
/* 132 */     int j = pos.getY();
/* 133 */     int k = pos.getZ();
/*     */     
/* 135 */     for (EntityLeashKnot entityleashknot : worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB(i - 1.0D, j - 1.0D, k - 1.0D, i + 1.0D, j + 1.0D, k + 1.0D))) {
/*     */       
/* 137 */       if (entityleashknot.getHangingPosition().equals(pos))
/*     */       {
/* 139 */         return entityleashknot;
/*     */       }
/*     */     } 
/*     */     
/* 143 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\EntityLeashKnot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */