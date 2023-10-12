/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ 
/*     */ public class Slot
/*     */ {
/*     */   private final int slotIndex;
/*     */   public final IInventory inventory;
/*     */   public int slotNumber;
/*     */   public int xDisplayPosition;
/*     */   public int yDisplayPosition;
/*     */   
/*     */   public Slot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
/*  16 */     this.inventory = inventoryIn;
/*  17 */     this.slotIndex = index;
/*  18 */     this.xDisplayPosition = xPosition;
/*  19 */     this.yDisplayPosition = yPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
/*  24 */     if (p_75220_1_ != null && p_75220_2_ != null)
/*     */     {
/*  26 */       if (p_75220_1_.getItem() == p_75220_2_.getItem()) {
/*     */         
/*  28 */         int i = p_75220_2_.stackSize - p_75220_1_.stackSize;
/*     */         
/*  30 */         if (i > 0)
/*     */         {
/*  32 */           onCrafting(p_75220_1_, i);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onCrafting(ItemStack stack, int amount) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onCrafting(ItemStack stack) {}
/*     */ 
/*     */   
/*     */   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
/*  48 */     onSlotChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isItemValid(ItemStack stack) {
/*  53 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getStack() {
/*  58 */     return this.inventory.getStackInSlot(this.slotIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getHasStack() {
/*  63 */     return (getStack() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putStack(ItemStack stack) {
/*  68 */     this.inventory.setInventorySlotContents(this.slotIndex, stack);
/*  69 */     onSlotChanged();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSlotChanged() {
/*  74 */     this.inventory.markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlotStackLimit() {
/*  79 */     return this.inventory.getInventoryStackLimit();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemStackLimit(ItemStack stack) {
/*  84 */     return getSlotStackLimit();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSlotTexture() {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack decrStackSize(int amount) {
/*  94 */     return this.inventory.decrStackSize(this.slotIndex, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHere(IInventory inv, int slotIn) {
/*  99 */     return (inv == this.inventory && slotIn == this.slotIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeStack(EntityPlayer playerIn) {
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeHovered() {
/* 109 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\Slot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */