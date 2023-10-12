/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.LockCode;
/*     */ 
/*     */ public class InventoryLargeChest
/*     */   implements ILockableContainer
/*     */ {
/*     */   private String name;
/*     */   private ILockableContainer upperChest;
/*     */   private ILockableContainer lowerChest;
/*     */   
/*     */   public InventoryLargeChest(String nameIn, ILockableContainer upperChestIn, ILockableContainer lowerChestIn) {
/*  20 */     this.name = nameIn;
/*     */     
/*  22 */     if (upperChestIn == null)
/*     */     {
/*  24 */       upperChestIn = lowerChestIn;
/*     */     }
/*     */     
/*  27 */     if (lowerChestIn == null)
/*     */     {
/*  29 */       lowerChestIn = upperChestIn;
/*     */     }
/*     */     
/*  32 */     this.upperChest = upperChestIn;
/*  33 */     this.lowerChest = lowerChestIn;
/*     */     
/*  35 */     if (upperChestIn.isLocked()) {
/*     */       
/*  37 */       lowerChestIn.setLockCode(upperChestIn.getLockCode());
/*     */     }
/*  39 */     else if (lowerChestIn.isLocked()) {
/*     */       
/*  41 */       upperChestIn.setLockCode(lowerChestIn.getLockCode());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  47 */     return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPartOfLargeChest(IInventory inventoryIn) {
/*  52 */     return (this.upperChest == inventoryIn || this.lowerChest == inventoryIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  57 */     return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/*  62 */     return (this.upperChest.hasCustomName() || this.lowerChest.hasCustomName());
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/*  67 */     return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  72 */     return (index >= this.upperChest.getSizeInventory()) ? this.lowerChest.getStackInSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  77 */     return (index >= this.upperChest.getSizeInventory()) ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  82 */     return (index >= this.upperChest.getSizeInventory()) ? this.lowerChest.removeStackFromSlot(index - this.upperChest.getSizeInventory()) : this.upperChest.removeStackFromSlot(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/*  87 */     if (index >= this.upperChest.getSizeInventory()) {
/*     */       
/*  89 */       this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
/*     */     }
/*     */     else {
/*     */       
/*  93 */       this.upperChest.setInventorySlotContents(index, stack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/*  99 */     return this.upperChest.getInventoryStackLimit();
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/* 104 */     this.upperChest.markDirty();
/* 105 */     this.lowerChest.markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 110 */     return (this.upperChest.isUseableByPlayer(player) && this.lowerChest.isUseableByPlayer(player));
/*     */   }
/*     */ 
/*     */   
/*     */   public void openInventory(EntityPlayer player) {
/* 115 */     this.upperChest.openInventory(player);
/* 116 */     this.lowerChest.openInventory(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeInventory(EntityPlayer player) {
/* 121 */     this.upperChest.closeInventory(player);
/* 122 */     this.lowerChest.closeInventory(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemValidForSlot(int index, ItemStack stack) {
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 132 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLocked() {
/* 146 */     return (this.upperChest.isLocked() || this.lowerChest.isLocked());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLockCode(LockCode code) {
/* 151 */     this.upperChest.setLockCode(code);
/* 152 */     this.lowerChest.setLockCode(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public LockCode getLockCode() {
/* 157 */     return this.upperChest.getLockCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGuiID() {
/* 162 */     return this.upperChest.getGuiID();
/*     */   }
/*     */ 
/*     */   
/*     */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
/* 167 */     return new ContainerChest((IInventory)playerInventory, (IInventory)this, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 172 */     this.upperChest.clear();
/* 173 */     this.lowerChest.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\InventoryLargeChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */