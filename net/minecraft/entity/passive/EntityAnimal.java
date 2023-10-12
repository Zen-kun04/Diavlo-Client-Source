/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
/*  18 */   protected Block spawnableBlock = (Block)Blocks.grass;
/*     */   
/*     */   private int inLove;
/*     */   private EntityPlayer playerInLove;
/*     */   
/*     */   public EntityAnimal(World worldIn) {
/*  24 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/*  29 */     if (getGrowingAge() != 0)
/*     */     {
/*  31 */       this.inLove = 0;
/*     */     }
/*     */     
/*  34 */     super.updateAITasks();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  39 */     super.onLivingUpdate();
/*     */     
/*  41 */     if (getGrowingAge() != 0)
/*     */     {
/*  43 */       this.inLove = 0;
/*     */     }
/*     */     
/*  46 */     if (this.inLove > 0) {
/*     */       
/*  48 */       this.inLove--;
/*     */       
/*  50 */       if (this.inLove % 10 == 0) {
/*     */         
/*  52 */         double d0 = this.rand.nextGaussian() * 0.02D;
/*  53 */         double d1 = this.rand.nextGaussian() * 0.02D;
/*  54 */         double d2 = this.rand.nextGaussian() * 0.02D;
/*  55 */         this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  62 */     if (isEntityInvulnerable(source))
/*     */     {
/*  64 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  68 */     this.inLove = 0;
/*  69 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos) {
/*  75 */     return (this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.grass) ? 10.0F : (this.worldObj.getLightBrightness(pos) - 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  80 */     super.writeEntityToNBT(tagCompound);
/*  81 */     tagCompound.setInteger("InLove", this.inLove);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  86 */     super.readEntityFromNBT(tagCompund);
/*  87 */     this.inLove = tagCompund.getInteger("InLove");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/*  92 */     int i = MathHelper.floor_double(this.posX);
/*  93 */     int j = MathHelper.floor_double((getEntityBoundingBox()).minY);
/*  94 */     int k = MathHelper.floor_double(this.posZ);
/*  95 */     BlockPos blockpos = new BlockPos(i, j, k);
/*  96 */     return (this.worldObj.getBlockState(blockpos.down()).getBlock() == this.spawnableBlock && this.worldObj.getLight(blockpos) > 8 && super.getCanSpawnHere());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTalkInterval() {
/* 101 */     return 120;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDespawn() {
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperiencePoints(EntityPlayer player) {
/* 111 */     return 1 + this.worldObj.rand.nextInt(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreedingItem(ItemStack stack) {
/* 116 */     return (stack == null) ? false : ((stack.getItem() == Items.wheat));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 121 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 123 */     if (itemstack != null) {
/*     */       
/* 125 */       if (isBreedingItem(itemstack) && getGrowingAge() == 0 && this.inLove <= 0) {
/*     */         
/* 127 */         consumeItemFromStack(player, itemstack);
/* 128 */         setInLove(player);
/* 129 */         return true;
/*     */       } 
/*     */       
/* 132 */       if (isChild() && isBreedingItem(itemstack)) {
/*     */         
/* 134 */         consumeItemFromStack(player, itemstack);
/* 135 */         func_175501_a((int)((-getGrowingAge() / 20) * 0.1F), true);
/* 136 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     return super.interact(player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void consumeItemFromStack(EntityPlayer player, ItemStack stack) {
/* 145 */     if (!player.capabilities.isCreativeMode) {
/*     */       
/* 147 */       stack.stackSize--;
/*     */       
/* 149 */       if (stack.stackSize <= 0)
/*     */       {
/* 151 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInLove(EntityPlayer player) {
/* 158 */     this.inLove = 600;
/* 159 */     this.playerInLove = player;
/* 160 */     this.worldObj.setEntityState((Entity)this, (byte)18);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPlayer getPlayerInLove() {
/* 165 */     return this.playerInLove;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInLove() {
/* 170 */     return (this.inLove > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetInLove() {
/* 175 */     this.inLove = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMateWith(EntityAnimal otherAnimal) {
/* 180 */     return (otherAnimal == this) ? false : ((otherAnimal.getClass() != getClass()) ? false : ((isInLove() && otherAnimal.isInLove())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 185 */     if (id == 18) {
/*     */       
/* 187 */       for (int i = 0; i < 7; i++)
/*     */       {
/* 189 */         double d0 = this.rand.nextGaussian() * 0.02D;
/* 190 */         double d1 = this.rand.nextGaussian() * 0.02D;
/* 191 */         double d2 = this.rand.nextGaussian() * 0.02D;
/* 192 */         this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 197 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */