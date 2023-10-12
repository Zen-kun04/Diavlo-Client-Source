/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerDispenser;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ 
/*     */ public class TileEntityDispenser extends TileEntityLockable implements IInventory {
/*  15 */   private static final Random RNG = new Random();
/*  16 */   private ItemStack[] stacks = new ItemStack[9];
/*     */   
/*     */   protected String customName;
/*     */   
/*     */   public int getSizeInventory() {
/*  21 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  26 */     return this.stacks[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  31 */     if (this.stacks[index] != null) {
/*     */       
/*  33 */       if ((this.stacks[index]).stackSize <= count) {
/*     */         
/*  35 */         ItemStack itemstack1 = this.stacks[index];
/*  36 */         this.stacks[index] = null;
/*  37 */         markDirty();
/*  38 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  42 */       ItemStack itemstack = this.stacks[index].splitStack(count);
/*     */       
/*  44 */       if ((this.stacks[index]).stackSize == 0)
/*     */       {
/*  46 */         this.stacks[index] = null;
/*     */       }
/*     */       
/*  49 */       markDirty();
/*  50 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  61 */     if (this.stacks[index] != null) {
/*     */       
/*  63 */       ItemStack itemstack = this.stacks[index];
/*  64 */       this.stacks[index] = null;
/*  65 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDispenseSlot() {
/*  75 */     int i = -1;
/*  76 */     int j = 1;
/*     */     
/*  78 */     for (int k = 0; k < this.stacks.length; k++) {
/*     */       
/*  80 */       if (this.stacks[k] != null && RNG.nextInt(j++) == 0)
/*     */       {
/*  82 */         i = k;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/*  91 */     this.stacks[index] = stack;
/*     */     
/*  93 */     if (stack != null && stack.stackSize > getInventoryStackLimit())
/*     */     {
/*  95 */       stack.stackSize = getInventoryStackLimit();
/*     */     }
/*     */     
/*  98 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int addItemStack(ItemStack stack) {
/* 103 */     for (int i = 0; i < this.stacks.length; i++) {
/*     */       
/* 105 */       if (this.stacks[i] == null || this.stacks[i].getItem() == null) {
/*     */         
/* 107 */         setInventorySlotContents(i, stack);
/* 108 */         return i;
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 117 */     return hasCustomName() ? this.customName : "container.dispenser";
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(String customName) {
/* 122 */     this.customName = customName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/* 127 */     return (this.customName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 132 */     super.readFromNBT(compound);
/* 133 */     NBTTagList nbttaglist = compound.getTagList("Items", 10);
/* 134 */     this.stacks = new ItemStack[getSizeInventory()];
/*     */     
/* 136 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 138 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 139 */       int j = nbttagcompound.getByte("Slot") & 0xFF;
/*     */       
/* 141 */       if (j >= 0 && j < this.stacks.length)
/*     */       {
/* 143 */         this.stacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       }
/*     */     } 
/*     */     
/* 147 */     if (compound.hasKey("CustomName", 8))
/*     */     {
/* 149 */       this.customName = compound.getString("CustomName");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 155 */     super.writeToNBT(compound);
/* 156 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 158 */     for (int i = 0; i < this.stacks.length; i++) {
/*     */       
/* 160 */       if (this.stacks[i] != null) {
/*     */         
/* 162 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 163 */         nbttagcompound.setByte("Slot", (byte)i);
/* 164 */         this.stacks[i].writeToNBT(nbttagcompound);
/* 165 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     compound.setTag("Items", (NBTBase)nbttaglist);
/*     */     
/* 171 */     if (hasCustomName())
/*     */     {
/* 173 */       compound.setString("CustomName", this.customName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 179 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 184 */     return (this.worldObj.getTileEntity(this.pos) != this) ? false : ((player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D));
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
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 202 */     return "minecraft:dispenser";
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 207 */     return (Container)new ContainerDispenser((IInventory)playerInventory, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 212 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 221 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 226 */     for (int i = 0; i < this.stacks.length; i++)
/*     */     {
/* 228 */       this.stacks[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */