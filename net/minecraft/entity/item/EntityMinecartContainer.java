/*     */ package net.minecraft.entity.item;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.LockCode;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityMinecartContainer extends EntityMinecart implements ILockableContainer {
/*  16 */   private ItemStack[] minecartContainerItems = new ItemStack[36];
/*     */   
/*     */   private boolean dropContentsWhenDead = true;
/*     */   
/*     */   public EntityMinecartContainer(World worldIn) {
/*  21 */     super(worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityMinecartContainer(World worldIn, double x, double y, double z) {
/*  26 */     super(worldIn, x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void killMinecart(DamageSource source) {
/*  31 */     super.killMinecart(source);
/*     */     
/*  33 */     if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */     {
/*  35 */       InventoryHelper.dropInventoryItems(this.worldObj, this, (IInventory)this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  41 */     return this.minecartContainerItems[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  46 */     if (this.minecartContainerItems[index] != null) {
/*     */       
/*  48 */       if ((this.minecartContainerItems[index]).stackSize <= count) {
/*     */         
/*  50 */         ItemStack itemstack1 = this.minecartContainerItems[index];
/*  51 */         this.minecartContainerItems[index] = null;
/*  52 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  56 */       ItemStack itemstack = this.minecartContainerItems[index].splitStack(count);
/*     */       
/*  58 */       if ((this.minecartContainerItems[index]).stackSize == 0)
/*     */       {
/*  60 */         this.minecartContainerItems[index] = null;
/*     */       }
/*     */       
/*  63 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  74 */     if (this.minecartContainerItems[index] != null) {
/*     */       
/*  76 */       ItemStack itemstack = this.minecartContainerItems[index];
/*  77 */       this.minecartContainerItems[index] = null;
/*  78 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/*  88 */     this.minecartContainerItems[index] = stack;
/*     */     
/*  90 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/*  92 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void markDirty() {}
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 102 */     return this.isDead ? false : ((player.getDistanceSqToEntity(this) <= 64.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void openInventory(EntityPlayer player) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeInventory(EntityPlayer player) {}
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack) {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 120 */     return hasCustomName() ? getCustomNameTag() : "container.minecart";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 125 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travelToDimension(int dimensionId) {
/* 130 */     this.dropContentsWhenDead = false;
/* 131 */     super.travelToDimension(dimensionId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDead() {
/* 136 */     if (this.dropContentsWhenDead)
/*     */     {
/* 138 */       InventoryHelper.dropInventoryItems(this.worldObj, this, (IInventory)this);
/*     */     }
/*     */     
/* 141 */     super.setDead();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 146 */     super.writeEntityToNBT(tagCompound);
/* 147 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 149 */     for (int i = 0; i < this.minecartContainerItems.length; i++) {
/*     */       
/* 151 */       if (this.minecartContainerItems[i] != null) {
/*     */         
/* 153 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 154 */         nbttagcompound.setByte("Slot", (byte)i);
/* 155 */         this.minecartContainerItems[i].writeToNBT(nbttagcompound);
/* 156 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     tagCompound.setTag("Items", (NBTBase)nbttaglist);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 165 */     super.readEntityFromNBT(tagCompund);
/* 166 */     NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
/* 167 */     this.minecartContainerItems = new ItemStack[getSizeInventory()];
/*     */     
/* 169 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 171 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 172 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/*     */       
/* 174 */       if (j >= 0 && j < this.minecartContainerItems.length)
/*     */       {
/* 176 */         this.minecartContainerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/* 183 */     if (!this.worldObj.isRemote)
/*     */     {
/* 185 */       playerIn.displayGUIChest((IInventory)this);
/*     */     }
/*     */     
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyDrag() {
/* 193 */     int i = 15 - Container.calcRedstoneFromInventory((IInventory)this);
/* 194 */     float f = 0.98F + i * 0.001F;
/* 195 */     this.motionX *= f;
/* 196 */     this.motionY *= 0.0D;
/* 197 */     this.motionZ *= f;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 202 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 211 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocked() {
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLockCode(LockCode code) {}
/*     */ 
/*     */   
/*     */   public LockCode getLockCode() {
/* 225 */     return LockCode.EMPTY_CODE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 230 */     for (int i = 0; i < this.minecartContainerItems.length; i++)
/*     */     {
/* 232 */       this.minecartContainerItems[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecartContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */