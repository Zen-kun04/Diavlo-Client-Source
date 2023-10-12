/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.pathfinder.WalkNodeProcessor;
/*     */ 
/*     */ public class EntityAIControlledByPlayer extends EntityAIBase {
/*     */   private final EntityLiving thisEntity;
/*     */   private final float maxSpeed;
/*     */   private float currentSpeed;
/*     */   private boolean speedBoosted;
/*     */   private int speedBoostTime;
/*     */   private int maxSpeedBoostTime;
/*     */   
/*     */   public EntityAIControlledByPlayer(EntityLiving entitylivingIn, float maxspeed) {
/*  27 */     this.thisEntity = entitylivingIn;
/*  28 */     this.maxSpeed = maxspeed;
/*  29 */     setMutexBits(7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  34 */     this.currentSpeed = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  39 */     this.speedBoosted = false;
/*  40 */     this.currentSpeed = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  45 */     return (this.thisEntity.isEntityAlive() && this.thisEntity.riddenByEntity != null && this.thisEntity.riddenByEntity instanceof EntityPlayer && (this.speedBoosted || this.thisEntity.canBeSteered()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  50 */     EntityPlayer entityplayer = (EntityPlayer)this.thisEntity.riddenByEntity;
/*  51 */     EntityCreature entitycreature = (EntityCreature)this.thisEntity;
/*  52 */     float f = MathHelper.wrapAngleTo180_float(entityplayer.rotationYaw - this.thisEntity.rotationYaw) * 0.5F;
/*     */     
/*  54 */     if (f > 5.0F)
/*     */     {
/*  56 */       f = 5.0F;
/*     */     }
/*     */     
/*  59 */     if (f < -5.0F)
/*     */     {
/*  61 */       f = -5.0F;
/*     */     }
/*     */     
/*  64 */     this.thisEntity.rotationYaw = MathHelper.wrapAngleTo180_float(this.thisEntity.rotationYaw + f);
/*     */     
/*  66 */     if (this.currentSpeed < this.maxSpeed)
/*     */     {
/*  68 */       this.currentSpeed += (this.maxSpeed - this.currentSpeed) * 0.01F;
/*     */     }
/*     */     
/*  71 */     if (this.currentSpeed > this.maxSpeed)
/*     */     {
/*  73 */       this.currentSpeed = this.maxSpeed;
/*     */     }
/*     */     
/*  76 */     int i = MathHelper.floor_double(this.thisEntity.posX);
/*  77 */     int j = MathHelper.floor_double(this.thisEntity.posY);
/*  78 */     int k = MathHelper.floor_double(this.thisEntity.posZ);
/*  79 */     float f1 = this.currentSpeed;
/*     */     
/*  81 */     if (this.speedBoosted) {
/*     */       
/*  83 */       if (this.speedBoostTime++ > this.maxSpeedBoostTime)
/*     */       {
/*  85 */         this.speedBoosted = false;
/*     */       }
/*     */       
/*  88 */       f1 += f1 * 1.15F * MathHelper.sin(this.speedBoostTime / this.maxSpeedBoostTime * 3.1415927F);
/*     */     } 
/*     */     
/*  91 */     float f2 = 0.91F;
/*     */     
/*  93 */     if (this.thisEntity.onGround)
/*     */     {
/*  95 */       f2 = (this.thisEntity.worldObj.getBlockState(new BlockPos(MathHelper.floor_float(i), MathHelper.floor_float(j) - 1, MathHelper.floor_float(k))).getBlock()).slipperiness * 0.91F;
/*     */     }
/*     */     
/*  98 */     float f3 = 0.16277136F / f2 * f2 * f2;
/*  99 */     float f4 = MathHelper.sin(entitycreature.rotationYaw * 3.1415927F / 180.0F);
/* 100 */     float f5 = MathHelper.cos(entitycreature.rotationYaw * 3.1415927F / 180.0F);
/* 101 */     float f6 = entitycreature.getAIMoveSpeed() * f3;
/* 102 */     float f7 = Math.max(f1, 1.0F);
/* 103 */     f7 = f6 / f7;
/* 104 */     float f8 = f1 * f7;
/* 105 */     float f9 = -(f8 * f4);
/* 106 */     float f10 = f8 * f5;
/*     */     
/* 108 */     if (MathHelper.abs(f9) > MathHelper.abs(f10)) {
/*     */       
/* 110 */       if (f9 < 0.0F)
/*     */       {
/* 112 */         f9 -= this.thisEntity.width / 2.0F;
/*     */       }
/*     */       
/* 115 */       if (f9 > 0.0F)
/*     */       {
/* 117 */         f9 += this.thisEntity.width / 2.0F;
/*     */       }
/*     */       
/* 120 */       f10 = 0.0F;
/*     */     }
/*     */     else {
/*     */       
/* 124 */       f9 = 0.0F;
/*     */       
/* 126 */       if (f10 < 0.0F)
/*     */       {
/* 128 */         f10 -= this.thisEntity.width / 2.0F;
/*     */       }
/*     */       
/* 131 */       if (f10 > 0.0F)
/*     */       {
/* 133 */         f10 += this.thisEntity.width / 2.0F;
/*     */       }
/*     */     } 
/*     */     
/* 137 */     int l = MathHelper.floor_double(this.thisEntity.posX + f9);
/* 138 */     int i1 = MathHelper.floor_double(this.thisEntity.posZ + f10);
/* 139 */     int j1 = MathHelper.floor_float(this.thisEntity.width + 1.0F);
/* 140 */     int k1 = MathHelper.floor_float(this.thisEntity.height + entityplayer.height + 1.0F);
/* 141 */     int l1 = MathHelper.floor_float(this.thisEntity.width + 1.0F);
/*     */     
/* 143 */     if (i != l || k != i1) {
/*     */       
/* 145 */       Block block = this.thisEntity.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
/* 146 */       boolean flag = (!isStairOrSlab(block) && (block.getMaterial() != Material.air || !isStairOrSlab(this.thisEntity.worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock())));
/*     */       
/* 148 */       if (flag && 0 == WalkNodeProcessor.func_176170_a((IBlockAccess)this.thisEntity.worldObj, (Entity)this.thisEntity, l, j, i1, j1, k1, l1, false, false, true) && 1 == WalkNodeProcessor.func_176170_a((IBlockAccess)this.thisEntity.worldObj, (Entity)this.thisEntity, i, j + 1, k, j1, k1, l1, false, false, true) && 1 == WalkNodeProcessor.func_176170_a((IBlockAccess)this.thisEntity.worldObj, (Entity)this.thisEntity, l, j + 1, i1, j1, k1, l1, false, false, true))
/*     */       {
/* 150 */         entitycreature.getJumpHelper().setJumping();
/*     */       }
/*     */     } 
/*     */     
/* 154 */     if (!entityplayer.capabilities.isCreativeMode && this.currentSpeed >= this.maxSpeed * 0.5F && this.thisEntity.getRNG().nextFloat() < 0.006F && !this.speedBoosted) {
/*     */       
/* 156 */       ItemStack itemstack = entityplayer.getHeldItem();
/*     */       
/* 158 */       if (itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick) {
/*     */         
/* 160 */         itemstack.damageItem(1, (EntityLivingBase)entityplayer);
/*     */         
/* 162 */         if (itemstack.stackSize == 0) {
/*     */           
/* 164 */           ItemStack itemstack1 = new ItemStack((Item)Items.fishing_rod);
/* 165 */           itemstack1.setTagCompound(itemstack.getTagCompound());
/* 166 */           entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = itemstack1;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     this.thisEntity.moveEntityWithHeading(0.0F, f1);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isStairOrSlab(Block blockIn) {
/* 176 */     return (blockIn instanceof net.minecraft.block.BlockStairs || blockIn instanceof net.minecraft.block.BlockSlab);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpeedBoosted() {
/* 181 */     return this.speedBoosted;
/*     */   }
/*     */ 
/*     */   
/*     */   public void boostSpeed() {
/* 186 */     this.speedBoosted = true;
/* 187 */     this.speedBoostTime = 0;
/* 188 */     this.maxSpeedBoostTime = this.thisEntity.getRNG().nextInt(841) + 140;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isControlledByPlayer() {
/* 193 */     return (!isSpeedBoosted() && this.currentSpeed > this.maxSpeed * 0.3F);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIControlledByPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */