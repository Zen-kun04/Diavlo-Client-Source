/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import net.minecraft.block.BlockFurnace;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityMinecartFurnace
/*     */   extends EntityMinecart {
/*     */   private int fuel;
/*     */   public double pushX;
/*     */   public double pushZ;
/*     */   
/*     */   public EntityMinecartFurnace(World worldIn) {
/*  25 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecartFurnace(World worldIn, double x, double y, double z) {
/*  30 */     super(worldIn, x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecart.EnumMinecartType getMinecartType() {
/*  35 */     return EntityMinecart.EnumMinecartType.FURNACE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  40 */     super.entityInit();
/*  41 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  46 */     super.onUpdate();
/*     */     
/*  48 */     if (this.fuel > 0)
/*     */     {
/*  50 */       this.fuel--;
/*     */     }
/*     */     
/*  53 */     if (this.fuel <= 0)
/*     */     {
/*  55 */       this.pushX = this.pushZ = 0.0D;
/*     */     }
/*     */     
/*  58 */     setMinecartPowered((this.fuel > 0));
/*     */     
/*  60 */     if (isMinecartPowered() && this.rand.nextInt(4) == 0)
/*     */     {
/*  62 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getMaximumSpeed() {
/*  68 */     return 0.2D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void killMinecart(DamageSource source) {
/*  73 */     super.killMinecart(source);
/*     */     
/*  75 */     if (!source.isExplosion() && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */     {
/*  77 */       entityDropItem(new ItemStack(Blocks.furnace, 1), 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_) {
/*  83 */     super.func_180460_a(p_180460_1_, p_180460_2_);
/*  84 */     double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
/*     */     
/*  86 */     if (d0 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
/*     */       
/*  88 */       d0 = MathHelper.sqrt_double(d0);
/*  89 */       this.pushX /= d0;
/*  90 */       this.pushZ /= d0;
/*     */       
/*  92 */       if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
/*     */         
/*  94 */         this.pushX = 0.0D;
/*  95 */         this.pushZ = 0.0D;
/*     */       }
/*     */       else {
/*     */         
/*  99 */         double d1 = d0 / getMaximumSpeed();
/* 100 */         this.pushX *= d1;
/* 101 */         this.pushZ *= d1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyDrag() {
/* 108 */     double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
/*     */     
/* 110 */     if (d0 > 1.0E-4D) {
/*     */       
/* 112 */       d0 = MathHelper.sqrt_double(d0);
/* 113 */       this.pushX /= d0;
/* 114 */       this.pushZ /= d0;
/* 115 */       double d1 = 1.0D;
/* 116 */       this.motionX *= 0.800000011920929D;
/* 117 */       this.motionY *= 0.0D;
/* 118 */       this.motionZ *= 0.800000011920929D;
/* 119 */       this.motionX += this.pushX * d1;
/* 120 */       this.motionZ += this.pushZ * d1;
/*     */     }
/*     */     else {
/*     */       
/* 124 */       this.motionX *= 0.9800000190734863D;
/* 125 */       this.motionY *= 0.0D;
/* 126 */       this.motionZ *= 0.9800000190734863D;
/*     */     } 
/*     */     
/* 129 */     super.applyDrag();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/* 134 */     ItemStack itemstack = playerIn.inventory.getCurrentItem();
/*     */     
/* 136 */     if (itemstack != null && itemstack.getItem() == Items.coal) {
/*     */       
/* 138 */       if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize == 0)
/*     */       {
/* 140 */         playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
/*     */       }
/*     */       
/* 143 */       this.fuel += 3600;
/*     */     } 
/*     */     
/* 146 */     this.pushX = this.posX - playerIn.posX;
/* 147 */     this.pushZ = this.posZ - playerIn.posZ;
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 153 */     super.writeEntityToNBT(tagCompound);
/* 154 */     tagCompound.setDouble("PushX", this.pushX);
/* 155 */     tagCompound.setDouble("PushZ", this.pushZ);
/* 156 */     tagCompound.setShort("Fuel", (short)this.fuel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 161 */     super.readEntityFromNBT(tagCompund);
/* 162 */     this.pushX = tagCompund.getDouble("PushX");
/* 163 */     this.pushZ = tagCompund.getDouble("PushZ");
/* 164 */     this.fuel = tagCompund.getShort("Fuel");
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMinecartPowered() {
/* 169 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setMinecartPowered(boolean p_94107_1_) {
/* 174 */     if (p_94107_1_) {
/*     */       
/* 176 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 0x1)));
/*     */     }
/*     */     else {
/*     */       
/* 180 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getDefaultDisplayTile() {
/* 186 */     return (isMinecartPowered() ? Blocks.lit_furnace : Blocks.furnace).getDefaultState().withProperty((IProperty)BlockFurnace.FACING, (Comparable)EnumFacing.NORTH);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecartFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */