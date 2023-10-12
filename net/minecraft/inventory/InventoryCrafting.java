/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class InventoryCrafting
/*     */   implements IInventory
/*     */ {
/*     */   private final ItemStack[] stackList;
/*     */   private final int inventoryWidth;
/*     */   private final int inventoryHeight;
/*     */   private final Container eventHandler;
/*     */   
/*     */   public InventoryCrafting(Container eventHandlerIn, int width, int height) {
/*  18 */     int i = width * height;
/*  19 */     this.stackList = new ItemStack[i];
/*  20 */     this.eventHandler = eventHandlerIn;
/*  21 */     this.inventoryWidth = width;
/*  22 */     this.inventoryHeight = height;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  27 */     return this.stackList.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  32 */     return (index >= getSizeInventory()) ? null : this.stackList[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInRowAndColumn(int row, int column) {
/*  37 */     return (row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight) ? getStackInSlot(row + column * this.inventoryWidth) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  42 */     return "container.crafting";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/*  52 */     return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  57 */     if (this.stackList[index] != null) {
/*     */       
/*  59 */       ItemStack itemstack = this.stackList[index];
/*  60 */       this.stackList[index] = null;
/*  61 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  71 */     if (this.stackList[index] != null) {
/*     */       
/*  73 */       if ((this.stackList[index]).stackSize <= count) {
/*     */         
/*  75 */         ItemStack itemstack1 = this.stackList[index];
/*  76 */         this.stackList[index] = null;
/*  77 */         this.eventHandler.onCraftMatrixChanged(this);
/*  78 */         return itemstack1;
/*     */       } 
/*     */ 
/*     */       
/*  82 */       ItemStack itemstack = this.stackList[index].splitStack(count);
/*     */       
/*  84 */       if ((this.stackList[index]).stackSize == 0)
/*     */       {
/*  86 */         this.stackList[index] = null;
/*     */       }
/*     */       
/*  89 */       this.eventHandler.onCraftMatrixChanged(this);
/*  90 */       return itemstack;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 101 */     this.stackList[index] = stack;
/* 102 */     this.eventHandler.onCraftMatrixChanged(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/* 107 */     return 64;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void markDirty() {}
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/* 116 */     return true;
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
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 134 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 143 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 148 */     for (int i = 0; i < this.stackList.length; i++)
/*     */     {
/* 150 */       this.stackList[i] = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 156 */     return this.inventoryHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 161 */     return this.inventoryWidth;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\InventoryCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */