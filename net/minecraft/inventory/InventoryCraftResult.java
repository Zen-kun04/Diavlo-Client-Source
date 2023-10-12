/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class InventoryCraftResult
/*     */   implements IInventory {
/*  11 */   private ItemStack[] stackResult = new ItemStack[1];
/*     */ 
/*     */   
/*     */   public int getSizeInventory() {
/*  15 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStackInSlot(int index) {
/*  20 */     return this.stackResult[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  25 */     return "Result";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasCustomName() {
/*  30 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getDisplayName() {
/*  35 */     return hasCustomName() ? (IChatComponent)new ChatComponentText(getName()) : (IChatComponent)new ChatComponentTranslation(getName(), new Object[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int index, int count) {
/*  40 */     if (this.stackResult[0] != null) {
/*     */       
/*  42 */       ItemStack itemstack = this.stackResult[0];
/*  43 */       this.stackResult[0] = null;
/*  44 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/*  54 */     if (this.stackResult[0] != null) {
/*     */       
/*  56 */       ItemStack itemstack = this.stackResult[0];
/*  57 */       this.stackResult[0] = null;
/*  58 */       return itemstack;
/*     */     } 
/*     */ 
/*     */     
/*  62 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/*  68 */     this.stackResult[0] = stack;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInventoryStackLimit() {
/*  73 */     return 64;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void markDirty() {}
/*     */ 
/*     */   
/*     */   public boolean isUseableByPlayer(EntityPlayer player) {
/*  82 */     return true;
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
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getField(int id) {
/* 100 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setField(int id, int value) {}
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 109 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 114 */     for (int i = 0; i < this.stackResult.length; i++)
/*     */     {
/* 116 */       this.stackResult[i] = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\InventoryCraftResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */