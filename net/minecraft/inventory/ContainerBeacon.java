/*     */ package net.minecraft.inventory;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public class ContainerBeacon
/*     */   extends Container
/*     */ {
/*     */   private IInventory tileBeacon;
/*     */   private final BeaconSlot beaconSlot;
/*     */   
/*     */   public ContainerBeacon(IInventory playerInventory, IInventory tileBeaconIn) {
/*  14 */     this.tileBeacon = tileBeaconIn;
/*  15 */     addSlotToContainer(this.beaconSlot = new BeaconSlot(tileBeaconIn, 0, 136, 110));
/*  16 */     int i = 36;
/*  17 */     int j = 137;
/*     */     
/*  19 */     for (int k = 0; k < 3; k++) {
/*     */       
/*  21 */       for (int l = 0; l < 9; l++)
/*     */       {
/*  23 */         addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, i + l * 18, j + k * 18));
/*     */       }
/*     */     } 
/*     */     
/*  27 */     for (int i1 = 0; i1 < 9; i1++)
/*     */     {
/*  29 */       addSlotToContainer(new Slot(playerInventory, i1, i + i1 * 18, 58 + j));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftGuiOpened(ICrafting listener) {
/*  35 */     super.onCraftGuiOpened(listener);
/*  36 */     listener.sendAllWindowProperties(this, this.tileBeacon);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateProgressBar(int id, int data) {
/*  41 */     this.tileBeacon.setField(id, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public IInventory func_180611_e() {
/*  46 */     return this.tileBeacon;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onContainerClosed(EntityPlayer playerIn) {
/*  51 */     super.onContainerClosed(playerIn);
/*     */     
/*  53 */     if (playerIn != null && !playerIn.worldObj.isRemote) {
/*     */       
/*  55 */       ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
/*     */       
/*  57 */       if (itemstack != null)
/*     */       {
/*  59 */         playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInteractWith(EntityPlayer playerIn) {
/*  66 */     return this.tileBeacon.isUseableByPlayer(playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
/*  71 */     ItemStack itemstack = null;
/*  72 */     Slot slot = this.inventorySlots.get(index);
/*     */     
/*  74 */     if (slot != null && slot.getHasStack()) {
/*     */       
/*  76 */       ItemStack itemstack1 = slot.getStack();
/*  77 */       itemstack = itemstack1.copy();
/*     */       
/*  79 */       if (index == 0) {
/*     */         
/*  81 */         if (!mergeItemStack(itemstack1, 1, 37, true))
/*     */         {
/*  83 */           return null;
/*     */         }
/*     */         
/*  86 */         slot.onSlotChange(itemstack1, itemstack);
/*     */       }
/*  88 */       else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack1) && itemstack1.stackSize == 1) {
/*     */         
/*  90 */         if (!mergeItemStack(itemstack1, 0, 1, false))
/*     */         {
/*  92 */           return null;
/*     */         }
/*     */       }
/*  95 */       else if (index >= 1 && index < 28) {
/*     */         
/*  97 */         if (!mergeItemStack(itemstack1, 28, 37, false))
/*     */         {
/*  99 */           return null;
/*     */         }
/*     */       }
/* 102 */       else if (index >= 28 && index < 37) {
/*     */         
/* 104 */         if (!mergeItemStack(itemstack1, 1, 28, false))
/*     */         {
/* 106 */           return null;
/*     */         }
/*     */       }
/* 109 */       else if (!mergeItemStack(itemstack1, 1, 37, false)) {
/*     */         
/* 111 */         return null;
/*     */       } 
/*     */       
/* 114 */       if (itemstack1.stackSize == 0) {
/*     */         
/* 116 */         slot.putStack((ItemStack)null);
/*     */       }
/*     */       else {
/*     */         
/* 120 */         slot.onSlotChanged();
/*     */       } 
/*     */       
/* 123 */       if (itemstack1.stackSize == itemstack.stackSize)
/*     */       {
/* 125 */         return null;
/*     */       }
/*     */       
/* 128 */       slot.onPickupFromSlot(playerIn, itemstack1);
/*     */     } 
/*     */     
/* 131 */     return itemstack;
/*     */   }
/*     */   
/*     */   class BeaconSlot
/*     */     extends Slot
/*     */   {
/*     */     public BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_, int p_i1801_4_, int p_i1801_5_) {
/* 138 */       super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isItemValid(ItemStack stack) {
/* 143 */       return (stack == null) ? false : ((stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot));
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSlotStackLimit() {
/* 148 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\ContainerBeacon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */